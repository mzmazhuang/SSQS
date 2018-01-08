package com.dading.ssqs.fragment.recharge

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.dading.ssqs.adapter.newAdapter.BankAdapter
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.bean.WXDFBean

/**
 * Created by mazhuang on 2017/11/24.
 */

@SuppressLint("ValidFragment")
class BankFragment constructor(private val listener: BankAdapter.OnRechargeClickListener) : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var adapter: BankAdapter? = null

    fun setList(list: List<WXDFBean.InfoBean>?) {
        if (list != null) {
            adapter!!.setData(list)
        }
    }


    override fun onCreateView(inflater: LayoutInflater?, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View? {
        val container = LinearLayout(context)

        recyclerView = RecyclerView(context)
        container.addView(recyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        adapter = BankAdapter(context)
        adapter!!.setListener(listener)
        recyclerView!!.adapter = adapter

        init()
        return container
    }

    private fun init() {
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)
        recyclerView!!.itemAnimator = DefaultItemAnimator()
    }
}
