package com.dading.ssqs.fragment.score

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.dading.ssqs.LocaleController
import com.dading.ssqs.NotificationController
import com.dading.ssqs.R
import com.dading.ssqs.adapter.ScrollAdapterInner
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.components.tabindicator.TabIndicator
import com.dading.ssqs.fragment.score.basketball.BFollowFragment
import com.dading.ssqs.fragment.score.basketball.BImmediateFragment
import com.dading.ssqs.fragment.score.basketball.BResultFragment
import com.dading.ssqs.fragment.score.basketball.BScheduleFragment
import com.dading.ssqs.utils.AndroidUtilities

import java.util.ArrayList

/**
 * Created by mazhuang on 2018/1/26.
 */

class ScoreBasketBallFragment : Fragment() {

    private var mContext: Context? = null

    private lateinit var bImmediateFragment: BImmediateFragment
    private lateinit var bResultFragment: BResultFragment
    private lateinit var bScheduleFragment: BScheduleFragment
    private lateinit var bFollowFragment: BFollowFragment
    private var viewPager: ViewPager? = null

    private var pageType: Int = 0

    fun setPageType(pageType: Int) {
        this.pageType = pageType

        if (viewPager != null) {
            viewPager!!.currentItem = pageType - 1

            NotificationController.getInstance().postNotification(NotificationController.scoreBasketChildPage, pageType.toString() + "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = context

        return initView()
    }

    private fun initView(): View {
        val container = LinearLayout(mContext)
        container.orientation = LinearLayout.VERTICAL

        val tabIndicator = TabIndicator(mContext)
        tabIndicator.lineColor = -0xff6425
        tabIndicator.lineStyle = TabIndicator.LINE_STYLE_WRAP
        tabIndicator.tabMode = TabIndicator.TAB_MODE_LINE
        tabIndicator.underLineColor = -0x121213
        tabIndicator.setTabPadding(AndroidUtilities.dp(26f), 0, AndroidUtilities.dp(26f), 0)
        container.addView(tabIndicator, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40))

        viewPager = ViewPager(mContext)
        viewPager!!.id = R.id.viewpager
        container.addView(viewPager, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        val titles = ArrayList<String>()
        titles.add(LocaleController.getString(R.string.pl_odds_js))
        titles.add(LocaleController.getString(R.string.scroll_title5))
        titles.add(LocaleController.getString(R.string.schedule))
        titles.add(LocaleController.getString(R.string.follow))

        bImmediateFragment = BImmediateFragment()

        bResultFragment = BResultFragment()

        bScheduleFragment = BScheduleFragment()

        bFollowFragment = BFollowFragment()

        if (pageType == 0 || pageType == 1) {
            bImmediateFragment.setBeginInit(true)
        } else if (pageType == 2) {
            bResultFragment.setBeginInit(true)
        } else if (pageType == 3) {
            bScheduleFragment.setBeginInit(true)
        } else if (pageType == 4) {
            bFollowFragment.setBeginInit(true)
        }

        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(bImmediateFragment)
        fragmentList.add(bResultFragment)
        fragmentList.add(bScheduleFragment)
        fragmentList.add(bFollowFragment)

        viewPager!!.adapter = ScrollAdapterInner(this.childFragmentManager, fragmentList, titles)
        viewPager!!.offscreenPageLimit = 4
        viewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    bImmediateFragment.hasInit()
                } else if (position == 1) {
                    bResultFragment.hasInit()
                } else if (position == 2) {
                    bScheduleFragment.hasInit()
                } else if (position == 3) {
                    bFollowFragment.hasInit()
                }

                NotificationController.getInstance().postNotification(NotificationController.scoreBasketChildPage, (position + 1).toString() + "")
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        tabIndicator.setViewPager(viewPager)
        if (pageType > 1) {
            viewPager!!.currentItem = pageType - 1
        }

        val type: String = if (pageType == 0) {
            "1"
        } else {
            pageType.toString() + ""
        }

        NotificationController.getInstance().postNotification(NotificationController.scoreBasketChildPage, type)

        return container
    }
}
