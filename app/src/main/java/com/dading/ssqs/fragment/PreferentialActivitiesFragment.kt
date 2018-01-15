package com.dading.ssqs.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast

import com.dading.ssqs.LocaleController
import com.dading.ssqs.R
import com.dading.ssqs.SSQSApplication
import com.dading.ssqs.activity.PerferentialInfoActivity
import com.dading.ssqs.adapter.newAdapter.PreferentialActivitiesAdapter
import com.dading.ssqs.apis.CcApiClient
import com.dading.ssqs.apis.CcApiResult
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.bean.Constent
import com.dading.ssqs.bean.PerferentialBean
import com.dading.ssqs.cells.TitleCell
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout

/**
 * Created by mazhuang on 2018/1/15.
 * 优惠活动
 */

class PreferentialActivitiesFragment : Fragment(), OnRefreshListener {

    private lateinit var swipeToLoadLayout: SwipeToLoadLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PreferentialActivitiesAdapter
    private var hasInit = false
    private var isRefresh = false

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initView()
    }

    private fun initView(): View {
        val container = LinearLayout(context)
        container.setBackgroundColor(Color.WHITE)
        container.orientation = LinearLayout.VERTICAL

        val titleCell = TitleCell(context, LocaleController.getString(R.string.perferential_activity))
        titleCell.hideBack()
        container.addView(titleCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48))

        val view = LayoutInflater.from(context).inflate(R.layout.custom_rela_refresh_load, null)
        container.addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout)
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout.setOnRefreshListener(this)
        swipeToLoadLayout.setLoadMoreEnabled(false)

        recyclerView = view.findViewById(R.id.swipe_target)

        adapter = PreferentialActivitiesAdapter(context)
        adapter.setListener { bean ->
            val intent = Intent(context, PerferentialInfoActivity::class.java)
            if (bean != null) {
                intent.putExtra(Constent.PERFERENTIAL_WEB, bean.webUrl)
                intent.putExtra(Constent.PERFERENTIAL_TITLE, bean.title)
                intent.putExtra(Constent.PERFERENTIAL_CONTENT, bean.content)
            }
            context.startActivity(intent)
        }
        recyclerView.adapter = adapter

        init()
        return container
    }

    private fun init() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.itemAnimator = DefaultItemAnimator()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            if (!hasInit) {
                hasInit = true
                swipeToLoadLayout.isRefreshing = true
            }
        }
    }

    private fun getNetDataWork() {
        SSQSApplication.apiClient(0).getActivityList { result ->
            swipeToLoadLayout.isRefreshing = false
            swipeToLoadLayout.setRefreshEnabled(true)

            if (result.isOk) {
                val items = result.data as List<PerferentialBean>

                if (items != null) {
                    adapter.setData(items)
                }

                isRefresh = false
            } else {
                Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRefresh() {
        if (!isRefresh) {
            isRefresh = true
            getNetDataWork()
        }
    }

    companion object {

        private val TAG = "PreferentialActivitiesFragment"
    }
}
