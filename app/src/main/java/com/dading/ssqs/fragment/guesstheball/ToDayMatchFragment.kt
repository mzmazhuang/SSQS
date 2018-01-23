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
import com.dading.ssqs.SSQSApplication
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
import com.dading.ssqs.utils.DateUtils

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

    private lateinit var topCell: GuessBallTopCell
    private lateinit var tvTitle: TextView

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

    private lateinit var maskView: View

    private var currPage = 1//当前页面
    private var ballType = -1;

    //一级标题点击事件
    private val topClickListener = GuessBallTopAdapter.OnGuessTopClickListener { id ->
        if (id == 1) {//足球
            currTitlePosition = 1
            twoTitleFootPosition = 0
            topCell.setTopSubTitleData(footBallSubTitles)
            topCell.setTopSubTitleSelect(twoTitleFootPosition)
        } else if (id == 2) {//篮球
            currTitlePosition = 2
            twoTitleBasketPosition = 0
            topCell.setTopSubTitleData(basketBallSubTitles)
            topCell.setTopSubTitleSelect(twoTitleBasketPosition)
        }

        tvTitle.text = "今日-" + (if (currTitlePosition == 1) "足球" else "篮球") + ":" + LocaleController.getString(R.string.scroll_title1)

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

        tvTitle.text = "今日-" + (if (currTitlePosition == 1) "足球" else "篮球") + ":" + str

        changePage(str)
    }

    fun selectBasketBall() {
        if (currTitlePosition != 2 || twoTitleBasketPosition != 0 || basketBallDefaultFragment == null) {

            currTitlePosition = 2

            twoTitleBasketPosition = 0

            topCell.setTopTitleSelect(1)
            topCell.setTopSubTitleSelect(twoTitleBasketPosition)
            topCell.refreshTitle()

            tvTitle.text = "今日" + "-" + LocaleController.getString(R.string.basketball) + ":" + LocaleController.getString(R.string.scroll_title1)

            changePage(LocaleController.getString(R.string.scroll_title1))
        }
    }

    fun selectFootBall() {
        if (currTitlePosition != 1 || twoTitleFootPosition != 0 || defaultFragment == null) {

            currTitlePosition = 1

            twoTitleFootPosition = 0

            topCell.setTopTitleSelect(twoTitleFootPosition)
            topCell.setTopSubTitleSelect(twoTitleFootPosition)
            topCell.refreshTitle()

            tvTitle.text = "今日" + "-" + LocaleController.getString(R.string.football) + ":" + LocaleController.getString(R.string.scroll_title1)

            changePage(LocaleController.getString(R.string.scroll_title1))
        }
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
        maskView.setOnClickListener {
            hideMask()
            NotificationController.getInstance().postNotification(NotificationController.today_mask, "child_close")
        }
        maskView.visibility = View.GONE
        maskView.setBackgroundColor(-0x5b000000)
        maskLayout.addView(maskView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 100))

        topCell = GuessBallTopCell(context)
        topCell.setTopListener(topClickListener)
        topCell.setSubTopListener(subTitleClickListener)
        contentLayout.addView(topCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        val titleLayout = LinearLayout(context)
        titleLayout.setBackgroundColor(-0xffbda3)
        contentLayout.addView(titleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        tvTitle = TextView(context)
        tvTitle.typeface = Typeface.DEFAULT_BOLD
        tvTitle.textSize = 13f
        tvTitle.setTextColor(Color.WHITE)
        tvTitle.text = LocaleController.getString(R.string.scroll_title15) + "-" + LocaleController.getString(R.string.football) + ":" + LocaleController.getString(R.string.scroll_title1)
        tvTitle.gravity = Gravity.CENTER
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

        createTitle(0, 0)
    }

    private fun createTitle(fTotal: Int, bTotal: Int) {
        //一级标题
        val topList = ArrayList<GuessTopTitle>()

        val title1 = GuessTopTitle()
        title1.id = 1
        title1.name = LocaleController.getString(R.string.football)
        title1.count = fTotal

        val title2 = GuessTopTitle()
        title2.id = 2
        title2.name = LocaleController.getString(R.string.basketball)
        title2.count = bTotal

        topList.add(title1)
        topList.add(title2)

        topCell.setTopTitleData(topList)
        topCell.setTopSubTitleData(footBallSubTitles)
    }

    fun setBallType(ballType: Int) {
        this.ballType = ballType
    }

    private fun init() {
        initTitles()

        when (ballType) {
            1 -> {
                selectFootBall()
            }
            2 -> {
                selectBasketBall()
            }
            else -> {
                defaultFragment = ToDayDefaultFragment()

                val fragmentTransaction = fragmentManager!!.beginTransaction()
                fragmentTransaction.add(R.id.today_parent, defaultFragment)
                fragmentTransaction.commit()
            }
        }

        getFootBallTotal()
    }

    fun fragmentResume() {
        when (currPage) {
            1 -> defaultFragment?.filterResume()
            2 -> boDanFragment?.filterResume()
            3 -> toDayTotalFragment?.filterResume()
            4 -> halfCourtFragment?.filterResume()
            5 -> toDayChampionFragment?.filterResume()
            6 -> basketBallPassFragment?.filterResume()
            7 -> resultFragment?.filterResume()
            8 -> basketBallDefaultFragment?.filterResume()
            9 -> toDayBasketBallChampionFragment?.filterResume()
            10 -> footBallPassFragment?.filterResume()
        }
    }

    fun fragmentPause() {
        when (currPage) {
            1 -> defaultFragment?.filterPause()
            2 -> boDanFragment?.filterPause()
            3 -> toDayTotalFragment?.filterPause()
            4 -> halfCourtFragment?.filterPause()
            5 -> toDayChampionFragment?.filterPause()
            6 -> basketBallPassFragment?.filterPause()
            7 -> resultFragment?.filterPause()
            8 -> basketBallDefaultFragment?.filterPause()
            9 -> toDayBasketBallChampionFragment?.filterPause()
            10 -> footBallPassFragment?.filterPause()
        }
    }

    private fun changePage(str: String) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()

        defaultFragment?.let {
            defaultFragment!!.filterPause()
            fragmentTransaction.hide(defaultFragment)
        }
        boDanFragment?.let {
            boDanFragment!!.filterPause()
            fragmentTransaction.hide(boDanFragment)
        }
        toDayTotalFragment?.let {
            toDayTotalFragment!!.filterPause()
            fragmentTransaction.hide(toDayTotalFragment)
        }
        halfCourtFragment?.let {
            halfCourtFragment!!.filterPause()
            fragmentTransaction.hide(halfCourtFragment)
        }
        toDayChampionFragment?.let {
            toDayChampionFragment!!.filterPause()
            fragmentTransaction.hide(toDayChampionFragment)
        }
        toDayBasketBallChampionFragment?.let {
            toDayBasketBallChampionFragment!!.filterPause()
            fragmentTransaction.hide(toDayBasketBallChampionFragment)
        }
        basketBallDefaultFragment?.let {
            basketBallDefaultFragment!!.filterPause()
            fragmentTransaction.hide(basketBallDefaultFragment)
        }
        resultFragment?.let {
            resultFragment!!.filterPause()
            fragmentTransaction.hide(resultFragment)
        }
        basketBallPassFragment?.let {
            basketBallPassFragment!!.filterPause()
            fragmentTransaction.hide(basketBallPassFragment)
        }
        footBallPassFragment?.let {
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
                maskView.visibility = View.VISIBLE
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
                maskView.visibility = View.GONE
            }
        })
        set.start()
    }

    override fun didReceivedNotification(id: Int, vararg args: String) {
        if (id == NotificationController.today_mask) {
            if (args?.isNotEmpty()) {
                if ("open" == args[0]) {
                    openMask()
                } else if ("close" == args[0]) {
                    hideMask()
                }
            }
        }
    }

    private fun getFootBallTotal() {
        val mDate = DateUtils.getCurTime("yyyyMMddHH:mm:ss")

        SSQSApplication.apiClient(0).getGuessBallFootBallTotal(2, mDate) { result ->
            if (result.isOk) {
                var fTotal = result.data as Int

                getBasketBallTotal(fTotal)
            }
        }
    }

    private fun getBasketBallTotal(fTotal: Int) {
        val mDate = DateUtils.getCurTime("yyyyMMddHH:mm:ss")

        SSQSApplication.apiClient(0).getGuessBallBasketBallTotal(2, mDate) { result ->
            if (result.isOk) {
                var bTotal = result.data as Int

                createTitle(fTotal, bTotal)
            }
        }
    }
}
