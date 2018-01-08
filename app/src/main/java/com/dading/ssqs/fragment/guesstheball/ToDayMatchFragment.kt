package com.dading.ssqs.fragment.guesstheball

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.dading.ssqs.LocaleController
import com.dading.ssqs.NotificationController
import com.dading.ssqs.R
import com.dading.ssqs.adapter.newAdapter.GuessBallTopAdapter
import com.dading.ssqs.adapter.newAdapter.GuessBallTopSubAdapter
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.bean.GuessTopTitle
import com.dading.ssqs.cells.GuessBallTopCell
import com.dading.ssqs.fragment.guesstheball.today.ToDayBasketBallChampionFragment
import com.dading.ssqs.fragment.guesstheball.today.ToDayFootBallChampionFragment
import com.dading.ssqs.fragment.guesstheball.today.ToDayBasketBallDefaultFragment
import com.dading.ssqs.fragment.guesstheball.today.ToDayFootBallPassFragment
import com.dading.ssqs.fragment.guesstheball.today.ToDayDefaultFragment
import com.dading.ssqs.fragment.guesstheball.today.ToDayBasketBallPassFragment
import com.dading.ssqs.fragment.guesstheball.today.ToDayHalfCourtFragment
import com.dading.ssqs.fragment.guesstheball.today.ToDayResultFragment
import com.dading.ssqs.fragment.guesstheball.today.ToDayTotalFragment
import com.dading.ssqs.fragment.guesstheball.today.TodayBoDanFragment

import java.util.ArrayList
import java.util.Calendar

/**
 * Created by mazhuang on 2017/11/30.
 * 今日赛事
 *
 *
 * 因有多个页面比较类似，但是考虑到后续如果不同的页面做了一些功能上的改动
 * 对开发不利，把每个功能都拆成一个页面，方便用于后续功能维护、开发
 */

class ToDayMatchFragment : Fragment(), NotificationController.NotificationControllerDelegate {

    private var topCell: GuessBallTopCell? = null
    private var tvTitle: TextView? = null

    private val footBallSubTitles = ArrayList<GuessTopTitle>()
    private val basketBallSubTitles = ArrayList<GuessTopTitle>()

    private var currTitlePosition = 1//一级标题position 默认足球
    private var twoTitleFootPosition = 0//二级标题足球的position  默认第一个选中
    private var twoTitleBasketPosition = -1//二级标题篮球的position

    private val calendar = Calendar.getInstance()

    private var defaultFragment: ToDayDefaultFragment? = null
    private var boDanFragment: TodayBoDanFragment? = null
    private var toDayTotalFragment: ToDayTotalFragment? = null
    private var halfCourtFragment: ToDayHalfCourtFragment? = null
    private var resultFragment: ToDayResultFragment? = null

    private var basketBallDefaultFragment: ToDayBasketBallDefaultFragment? = null
    private var toDayChampionFragment: ToDayFootBallChampionFragment? = null
    private var toDayBasketBallChampionFragment: ToDayBasketBallChampionFragment? = null
    private var basketBallPassFragment: ToDayFootBallPassFragment? = null
    private var footBallPassFragment: ToDayBasketBallPassFragment? = null

    private var maskView: View? = null

    private var currPage = 1//当前页面

    //一级标题点击事件
    private val topClickListener = GuessBallTopAdapter.OnGuessTopClickListener { id ->
        if (id == 1) {//足球
            currTitlePosition = 1
            twoTitleFootPosition = 0
            topCell!!.setTopSubTitleData(footBallSubTitles)
            topCell!!.setTopSubTitleSelect(twoTitleFootPosition)
        } else if (id == 2) {//篮球
            currTitlePosition = 2
            twoTitleBasketPosition = 0
            topCell!!.setTopSubTitleData(basketBallSubTitles)
            topCell!!.setTopSubTitleSelect(twoTitleBasketPosition)
        }

        tvTitle!!.text = "今日-" + (if (currTitlePosition == 1) "足球" else "篮球") + ":" + LocaleController.getString(R.string.scroll_title1)

        changePage(LocaleController.getString(R.string.scroll_title1))
    }

    //二级标题点击事件
    private val subTitleClickListener = GuessBallTopSubAdapter.OnGuessSubTitleClickListener { id, position ->
        if (currTitlePosition == 1) {
            twoTitleFootPosition = position
            twoTitleBasketPosition = -1
        } else {
            twoTitleBasketPosition = position
            twoTitleFootPosition = -1
        }

        val str = footBallSubTitles.indices
                .firstOrNull { footBallSubTitles[it].id == id }
                ?.let { footBallSubTitles[it].name }
                ?: ""

        tvTitle!!.text = "今日-" + (if (currTitlePosition == 1) "足球" else "篮球") + ":" + str

        changePage(str)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        NotificationController.getInstance().addObserver(this, NotificationController.today_mask)

        return initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        NotificationController.getInstance().removeObserver(this, NotificationController.today_mask)
    }

    private fun initView(): View {
        calendar.timeInMillis = System.currentTimeMillis()

        val container = LinearLayout(context)
        container.orientation = LinearLayout.VERTICAL

        val maskLayout = RelativeLayout(context)
        container.addView(maskLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        val contentLayout = LinearLayout(context)
        contentLayout.orientation = LinearLayout.VERTICAL
        maskLayout.addView(contentLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        maskView = View(context)
        maskView!!.setOnClickListener {
            hideMask()
            NotificationController.getInstance().postNotification(NotificationController.today_mask, "child_close")
        }
        maskView!!.visibility = View.GONE
        maskView!!.setBackgroundColor(-0x5b000000)
        maskLayout.addView(maskView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 100))

        topCell = GuessBallTopCell(context)
        topCell!!.setTopListener(topClickListener)
        topCell!!.setSubTopListener(subTitleClickListener)
        contentLayout.addView(topCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        val titleLayout = LinearLayout(context)
        titleLayout.setBackgroundColor(-0xffbda3)
        contentLayout.addView(titleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        tvTitle = TextView(context)
        tvTitle!!.typeface = Typeface.DEFAULT_BOLD
        tvTitle!!.textSize = 13f
        tvTitle!!.setTextColor(Color.WHITE)
        tvTitle!!.text = LocaleController.getString(R.string.scroll_title15) + "-" + LocaleController.getString(R.string.football) + ":" + LocaleController.getString(R.string.scroll_title1)
        tvTitle!!.gravity = Gravity.CENTER
        titleLayout.addView(tvTitle, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, 30, 12f, 0f, 0f, 0f))

        val parentLayout = LinearLayout(context)//用来替换fragment的布局
        parentLayout.id = R.id.today_parent
        container.addView(parentLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        init()
        return container
    }

    private fun initTitles() {
        val subTitle1 = GuessTopTitle()
        subTitle1.id = 3
        subTitle1.name = LocaleController.getString(R.string.scroll_title1)

        val subTitle2 = GuessTopTitle()
        subTitle2.id = 4
        subTitle2.name = LocaleController.getString(R.string.scroll_title2)

        val subTitle3 = GuessTopTitle()
        subTitle3.id = 5
        subTitle3.name = LocaleController.getString(R.string.scroll_title3)

        val subTitle4 = GuessTopTitle()
        subTitle4.id = 6
        subTitle4.name = LocaleController.getString(R.string.scroll_title4)

        val subTitle5 = GuessTopTitle()
        subTitle5.id = 7
        subTitle5.name = LocaleController.getString(R.string.scroll_title13)

        val subTitle6 = GuessTopTitle()
        subTitle6.id = 8
        subTitle6.name = LocaleController.getString(R.string.scroll_title14)

        val subTitle7 = GuessTopTitle()
        subTitle7.id = 9
        subTitle7.name = LocaleController.getString(R.string.scroll_title5)

        footBallSubTitles.add(subTitle1)
        footBallSubTitles.add(subTitle2)
        footBallSubTitles.add(subTitle3)
        footBallSubTitles.add(subTitle4)
        footBallSubTitles.add(subTitle5)
        footBallSubTitles.add(subTitle6)
        footBallSubTitles.add(subTitle7)

        basketBallSubTitles.add(subTitle1)
        basketBallSubTitles.add(subTitle5)
        basketBallSubTitles.add(subTitle6)

        //一级标题
        val topList = ArrayList<GuessTopTitle>()

        val title1 = GuessTopTitle()
        title1.id = 1
        title1.name = LocaleController.getString(R.string.football)
        title1.count = 7

        val title2 = GuessTopTitle()
        title2.id = 2
        title2.name = LocaleController.getString(R.string.basketball)
        title2.count = 3

        topList.add(title1)
        topList.add(title2)

        topCell!!.setTopTitleData(topList)
        topCell!!.setTopSubTitleData(footBallSubTitles)
    }

    private fun init() {
        initTitles()

        defaultFragment = ToDayDefaultFragment()

        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.add(R.id.today_parent, defaultFragment)
        fragmentTransaction.commit()
    }

    fun fragmentResume() {
        if (currPage == 1 && defaultFragment != null) {
            defaultFragment!!.filterResume()
        } else if (currPage == 2 && boDanFragment != null) {
            boDanFragment!!.filterResume()
        } else if (currPage == 3 && toDayTotalFragment != null) {
            toDayTotalFragment!!.filterResume()
        } else if (currPage == 4 && halfCourtFragment != null) {
            halfCourtFragment!!.filterResume()
        } else if (currPage == 5 && toDayChampionFragment != null) {
            toDayChampionFragment!!.filterResume()
        } else if (currPage == 6 && basketBallPassFragment != null) {
            basketBallPassFragment!!.filterResume()
        } else if (currPage == 7 && resultFragment != null) {
            resultFragment!!.filterResume()
        } else if (currPage == 8 && basketBallDefaultFragment != null) {
            basketBallDefaultFragment!!.filterResume()
        } else if (currPage == 9 && toDayBasketBallChampionFragment != null) {
            toDayBasketBallChampionFragment!!.filterResume()
        } else if (currPage == 10 && footBallPassFragment != null) {
            footBallPassFragment!!.filterResume()
        }
    }

    fun fragmentPause() {
        if (currPage == 1 && defaultFragment != null) {
            defaultFragment!!.filterPause()
        } else if (currPage == 2 && boDanFragment != null) {
            boDanFragment!!.filterPause()
        } else if (currPage == 3 && toDayTotalFragment != null) {
            toDayTotalFragment!!.filterPause()
        } else if (currPage == 4 && halfCourtFragment != null) {
            halfCourtFragment!!.filterPause()
        } else if (currPage == 5 && toDayChampionFragment != null) {
            toDayChampionFragment!!.filterPause()
        } else if (currPage == 6 && basketBallPassFragment != null) {
            basketBallPassFragment!!.filterPause()
        } else if (currPage == 7 && resultFragment != null) {
            resultFragment!!.filterPause()
        } else if (currPage == 8 && basketBallDefaultFragment != null) {
            basketBallDefaultFragment!!.filterPause()
        } else if (currPage == 9 && toDayBasketBallChampionFragment != null) {
            toDayBasketBallChampionFragment!!.filterPause()
        } else if (currPage == 10 && footBallPassFragment != null) {
            footBallPassFragment!!.filterPause()
        }
    }

    fun clearFilterData() {
        DataController.getInstance().clearScrollFootBallData()
    }

    private fun changePage(str: String) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()

        if (defaultFragment != null) {
            defaultFragment!!.filterPause()
            fragmentTransaction.hide(defaultFragment)
        }
        if (boDanFragment != null) {
            boDanFragment!!.filterPause()
            fragmentTransaction.hide(boDanFragment)
        }
        if (toDayTotalFragment != null) {
            toDayTotalFragment!!.filterPause()
            fragmentTransaction.hide(toDayTotalFragment)
        }
        if (halfCourtFragment != null) {
            halfCourtFragment!!.filterPause()
            fragmentTransaction.hide(halfCourtFragment)
        }
        if (toDayChampionFragment != null) {
            toDayChampionFragment!!.filterPause()
            fragmentTransaction.hide(toDayChampionFragment)
        }
        if (toDayBasketBallChampionFragment != null) {
            toDayBasketBallChampionFragment!!.filterPause()
            fragmentTransaction.hide(toDayBasketBallChampionFragment)
        }
        if (basketBallDefaultFragment != null) {
            basketBallDefaultFragment!!.filterPause()
            fragmentTransaction.hide(basketBallDefaultFragment)
        }
        if (resultFragment != null) {
            resultFragment!!.filterPause()
            fragmentTransaction.hide(resultFragment)
            clearFilterData()
        }
        if (basketBallPassFragment != null) {
            basketBallPassFragment!!.filterPause()
            fragmentTransaction.hide(basketBallPassFragment)
        }
        if (footBallPassFragment != null) {
            footBallPassFragment!!.filterPause()
            fragmentTransaction.hide(footBallPassFragment)
        }

        if (currTitlePosition == 1) {//足球
            when (str) {
                LocaleController.getString(R.string.scroll_title1) -> {
                    if (defaultFragment == null) {
                        defaultFragment = ToDayDefaultFragment()
                        fragmentTransaction.add(R.id.today_parent, defaultFragment)
                    } else {
                        defaultFragment!!.filterResume()
                        fragmentTransaction.show(defaultFragment)
                    }
                    currPage = 1
                }
                LocaleController.getString(R.string.scroll_title2) -> {
                    if (boDanFragment == null) {
                        boDanFragment = TodayBoDanFragment()
                        fragmentTransaction.add(R.id.today_parent, boDanFragment)
                    } else {
                        boDanFragment!!.filterResume()
                        fragmentTransaction.show(boDanFragment)
                    }
                    currPage = 2
                }
                LocaleController.getString(R.string.scroll_title3) -> {
                    if (toDayTotalFragment == null) {
                        toDayTotalFragment = ToDayTotalFragment()
                        fragmentTransaction.add(R.id.today_parent, toDayTotalFragment)
                    } else {
                        toDayTotalFragment!!.filterResume()
                        fragmentTransaction.show(toDayTotalFragment)
                    }
                    currPage = 3
                }
                LocaleController.getString(R.string.scroll_title4) -> {
                    if (halfCourtFragment == null) {
                        halfCourtFragment = ToDayHalfCourtFragment()
                        fragmentTransaction.add(R.id.today_parent, halfCourtFragment)
                    } else {
                        halfCourtFragment!!.filterResume()
                        fragmentTransaction.show(halfCourtFragment)
                    }
                    currPage = 4
                }
                LocaleController.getString(R.string.scroll_title13) -> {
                    if (toDayChampionFragment == null) {
                        toDayChampionFragment = ToDayFootBallChampionFragment()
                        fragmentTransaction.add(R.id.today_parent, toDayChampionFragment)
                    } else {
                        toDayChampionFragment!!.filterResume()
                        fragmentTransaction.show(toDayChampionFragment)
                    }
                    currPage = 5
                }
                LocaleController.getString(R.string.scroll_title14) -> {
                    if (basketBallPassFragment == null) {
                        basketBallPassFragment = ToDayFootBallPassFragment()
                        fragmentTransaction.add(R.id.today_parent, basketBallPassFragment)
                    } else {
                        basketBallPassFragment!!.filterResume()
                        fragmentTransaction.show(basketBallPassFragment)
                    }
                    currPage = 6
                }
                LocaleController.getString(R.string.scroll_title5) -> {
                    if (resultFragment == null) {
                        resultFragment = ToDayResultFragment()
                        fragmentTransaction.add(R.id.today_parent, resultFragment)
                    } else {
                        resultFragment!!.filterResume()
                        fragmentTransaction.show(resultFragment)
                    }
                    clearFilterData()
                    currPage = 7
                }
            }
        } else {//篮球
            when (str) {
                LocaleController.getString(R.string.scroll_title1) -> {
                    if (basketBallDefaultFragment == null) {
                        basketBallDefaultFragment = ToDayBasketBallDefaultFragment()
                        fragmentTransaction.add(R.id.today_parent, basketBallDefaultFragment)
                    } else {
                        basketBallDefaultFragment!!.filterResume()
                        fragmentTransaction.show(basketBallDefaultFragment)
                    }
                    currPage = 8
                }
                LocaleController.getString(R.string.scroll_title13) -> {
                    if (toDayBasketBallChampionFragment == null) {
                        toDayBasketBallChampionFragment = ToDayBasketBallChampionFragment()
                        fragmentTransaction.add(R.id.today_parent, toDayBasketBallChampionFragment)
                    } else {
                        toDayBasketBallChampionFragment!!.filterResume()
                        fragmentTransaction.show(toDayBasketBallChampionFragment)
                    }
                    currPage = 9
                }
                LocaleController.getString(R.string.scroll_title14) -> {
                    if (footBallPassFragment == null) {
                        footBallPassFragment = ToDayBasketBallPassFragment()
                        fragmentTransaction.add(R.id.today_parent, footBallPassFragment)
                    } else {
                        footBallPassFragment!!.filterResume()
                        fragmentTransaction.show(footBallPassFragment)
                    }
                    currPage = 10
                }
            }
        }
        fragmentTransaction.commit()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun openMask() {
        val animator = ObjectAnimator.ofFloat(maskView, "alpha", 0.0f, 1.0f)
        val set = AnimatorSet()
        set.play(animator)
        set.duration = 200
        set.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationStart(animation: Animator) {
                maskView!!.visibility = View.VISIBLE
            }
        })
        set.start()
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun hideMask() {
        val animator = ObjectAnimator.ofFloat(maskView, "alpha", 1.0f, 0.0f)
        val set = AnimatorSet()
        set.play(animator)
        set.duration = 200
        set.addListener(object : AnimatorListenerAdapter() {

            override fun onAnimationEnd(animation: Animator) {
                maskView!!.visibility = View.GONE
            }
        })
        set.start()
    }

    override fun didReceivedNotification(id: Int, vararg args: String) {
        if (id == NotificationController.today_mask) {
            if (args != null && args.size >= 1) {
                if ("open" == args[0]) {
                    openMask()
                } else if ("close" == args[0]) {
                    hideMask()
                }
            }
        }
    }
}
