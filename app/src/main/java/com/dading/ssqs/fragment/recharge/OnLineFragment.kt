package com.dading.ssqs.fragment.recharge

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import com.dading.ssqs.base.LayoutHelper

/**
 * Created by mazhuang on 2017/11/24.
 */

class OnLineFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View? {
        val container = LinearLayout(context)

        val tvTest = TextView(context)
        tvTest.text = "暂时不支持"
        tvTest.setTextColor(-0x3cdce)
        tvTest.gravity = Gravity.CENTER
        container.addView(tvTest, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))
        return container
    }
}
