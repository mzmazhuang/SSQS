package com.dading.ssqs.fragment.score.football

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout

import com.dading.ssqs.NotificationController
import com.dading.ssqs.R
import com.dading.ssqs.SSQSApplication
import com.dading.ssqs.activity.LoginActivity
import com.dading.ssqs.activity.MatchInfoActivity
import com.dading.ssqs.adapter.newAdapter.ScoreFImmediateAdapter
import com.dading.ssqs.apis.CcApiClient
import com.dading.ssqs.apis.CcApiResult
import com.dading.ssqs.apis.elements.FouceMatchBallElement
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.bean.Constent
import com.dading.ssqs.bean.ScoreBean
import com.dading.ssqs.components.LoadingDialog
import com.dading.ssqs.components.swipetoloadlayout.OnLoadMoreListener
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout
import com.dading.ssqs.utils.DateUtils
import com.dading.ssqs.utils.ToastUtils
import com.dading.ssqs.utils.UIUtils

import java.util.ArrayList

/**
 * Created by mazhuang on 2018/1/26.
 * 足球即时
 */

class FImmediateFragment : Fragment(), OnRefreshListener, OnLoadMoreListener, NotificationController.NotificationControllerDelegate {

    private var mContext: Context? = null
    private var swipeToLoadLayout: SwipeToLoadLayout? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: ScoreFImmediateAdapter? = null
    private var loadingDialog: LoadingDialog? = null
    private var defaultView: ImageView? = null

    private var offset = 1
    private val limit = 10

    private var leagusId = "0"

    private var isRefresh = false
    private var isLoadMore = false

    private var hasInit = false//是否有过初始化

    private var beginInit = false

    private val listener = object : ScoreFImmediateAdapter.OnScoreFootBallListener {

        override fun onFavorite(isFavor: Int, matchId: String) {
            if (checkIsLogin()) {
                favorOperation(isFavor, matchId)
            }
        }

        override fun onItemClick(id: Int) {
            if (checkIsLogin()) {
                UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, true)

                val intent = Intent(mContext, MatchInfoActivity::class.java)
                intent.putExtra(Constent.MATCH_ID, id)
                intent.putExtra(Constent.INTENT_FROM, "JS")
                startActivity(intent)
            }
        }
    }

    fun setBeginInit(beginInit: Boolean) {
        this.beginInit = beginInit
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = context

        NotificationController.getInstance().addObserver(this, NotificationController.scoreChildRefresh)
        return initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        NotificationController.getInstance().removeObserver(this, NotificationController.scoreChildRefresh)
    }

    private fun initView(): View {
        val container = RelativeLayout(mContext)

        val view = LayoutInflater.from(mContext).inflate(R.layout.custom_rela_refresh_load, null)
        container.addView(view, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        defaultView = ImageView(mContext)
        defaultView!!.setImageResource(R.mipmap.no_data)
        defaultView!!.visibility = View.GONE
        container.addView(defaultView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT))

        swipeToLoadLayout = view.findViewById(R.id.swipeToLoadLayout)
        swipeToLoadLayout!!.setOnRefreshListener(this)
        swipeToLoadLayout!!.setOnLoadMoreListener(this)
        swipeToLoadLayout!!.setLoadMoreEnabled(false)

        recyclerView = view.findViewById(R.id.swipe_target)

        adapter = ScoreFImmediateAdapter(mContext)
        adapter!!.setListener(listener)
        recyclerView!!.adapter = adapter

        init()
        return container
    }

    private fun favorOperation(isFavor: Int, id: String) {
        loadingDialog!!.show()

        val element = FouceMatchBallElement()
        element.matchID = id
        element.status = if (isFavor == 1) "0" else "1"

        SSQSApplication.apiClient(0).fouceMatchBall(element) { result ->
            loadingDialog!!.dismiss()

            if (result.isOk) {
                val list = ArrayList<ScoreBean>()
                list.addAll(adapter!!.data)

                for (i in list.indices) {
                    if (id == list[i].id.toString() + "") {
                        list[i].isFouce = if (isFavor == 1) 0 else 1
                        break
                    }
                }

                adapter!!.setList(list)

            } else {
                ToastUtils.midToast(mContext, result.message, 0)
            }
        }

    }

    private fun checkIsLogin(): Boolean {
        return if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            true
        } else {
            val intent = Intent(mContext, LoginActivity::class.java)
            startActivity(intent)

            false
        }
    }

    private fun init() {
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(mContext)
        recyclerView!!.itemAnimator = DefaultItemAnimator()

        loadingDialog = LoadingDialog(mContext)

        if (beginInit && hasInit) {
            swipeToLoadLayout!!.isRefreshing = true
        } else if (beginInit) {
            hasInit()
        }
    }

    fun hasInit() {
        if (!hasInit) {
            hasInit = true

            if (swipeToLoadLayout != null) {
                swipeToLoadLayout!!.isRefreshing = true
            }
        }
    }

    private fun getNetDataWork(leagusId: String, off: Int, lim: Int, isRefre: Boolean) {
        val date = DateUtils.getCurTime("yyyyMMddHH:mm:ss")

        SSQSApplication.apiClient(0).getMatchBallOrTypeList(true, 2, date, "0", 0, leagusId, off, lim) { result ->
            swipeToLoadLayout!!.isRefreshing = false
            swipeToLoadLayout!!.isLoadingMore = false
            loadingDialog!!.dismiss()

            if (result.isOk) {
                val page = result.data as CcApiResult.ResultScorePage

                if (page != null) {
                    val totalCount = page.totalCount

                    if (page.items != null && page.items.size >= 1) {
                        defaultView!!.visibility = View.GONE

                        if (isRefre) {
                            adapter!!.setList(page.items)
                        } else {
                            adapter!!.addList(page.items)
                        }
                    } else {
                        adapter!!.clearData()

                        defaultView!!.visibility = View.VISIBLE
                    }

                    swipeToLoadLayout!!.setLoadMoreEnabled(offset < totalCount)
                }

                isRefresh = false
                isLoadMore = false
            } else {
                ToastUtils.midToast(mContext, result.message, 0)
            }
        }
    }

    override fun onRefresh() {
        if (!isRefresh) {
            isRefresh = true

            offset = 1
            leagusId = "0"

            getNetDataWork(leagusId, offset, limit, true)
        }
    }

    override fun onLoadMore() {
        if (!isLoadMore) {
            isLoadMore = true

            offset++

            getNetDataWork(leagusId, offset, limit, false)
        }
    }

    override fun didReceivedNotification(id: Int, vararg args: String) {
        if (id == NotificationController.scoreChildRefresh) {
            if (args.isNotEmpty()) {
                if (TAG == args[0]) {
                    leagusId = args[1]

                    loadingDialog!!.show()

                    offset = 1

                    getNetDataWork(leagusId, offset, limit, true)
                }
            }
        }
    }

    companion object {

        val TAG = "FImmediateFragment"
    }
}
