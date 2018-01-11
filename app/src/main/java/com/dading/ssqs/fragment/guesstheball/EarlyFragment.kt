package com.dading.ssqs.fragment.guesstheball

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout

import com.dading.ssqs.LocaleController
import com.dading.ssqs.NotificationController
import com.dading.ssqs.R
import com.dading.ssqs.adapter.newAdapter.GuessBallTopAdapter
import com.dading.ssqs.adapter.newAdapter.GuessBallTopSubAdapter
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.bean.GuessTopTitle
import com.dading.ssqs.cells.GuessBallTopCell
import com.dading.ssqs.fragment.guesstheball.early.EarlyBasketBallChampionFragment
import com.dading.ssqs.fragment.guesstheball.early.EarlyBasketBallDefaultFragment
import com.dading.ssqs.fragment.guesstheball.early.EarlyBasketBallPassFragment
import com.dading.ssqs.fragment.guesstheball.early.EarlyBoDanFragment
import com.dading.ssqs.fragment.guesstheball.early.EarlyChampionFragment
import com.dading.ssqs.fragment.guesstheball.early.EarlyDefaultFragment
import com.dading.ssqs.fragment.guesstheball.early.EarlyFootBallPassFragment
import com.dading.ssqs.fragment.guesstheball.early.EarlyHalfCourtFragment
import com.dading.ssqs.fragment.guesstheball.early.EarlyResultFragment
import com.dading.ssqs.fragment.guesstheball.early.EarlyTotalFragment

import java.util.ArrayList

/**
 * Created by mazhuang on 2017/11/30.
 * 早盘
 *
 *
 * 因有多个页面比较类似，但是考虑到后续如果不同的页面做了一些功能上的改动
 * 对开发不利，把每个功能都拆成一个页面，方便用于后续功能维护、开发
 */

class EarlyFragment : Fragment(), NotificationController.NotificationControllerDelegate {

    private lateinit var topCell: GuessBallTopCell

    private val footBallSubTitles = ArrayList<GuessTopTitle>()
    private val basketBallSubTitles = ArrayList<GuessTopTitle>()

    private var currTitlePosition = 1//一级标题position 默认足球
    private var twoTitleFootPosition = 0//二级标题足球的position  默认第一个选中
    private var twoTitleBasketPosition = -1//二级标题篮球的position

    private var defaultFragment: EarlyDefaultFragment? = null
    private var boDanFragment: EarlyBoDanFragment? = null
    private var totalFragment: EarlyTotalFragment? = null
    private var halfCourtFragment: EarlyHalfCourtFragment? = null
    private var resultFragment: EarlyResultFragment? = null
    private var earlyChampionFragment: EarlyChampionFragment? = null
    private var earlyBasketBallChampionFragmen: EarlyBasketBallChampionFragment? = null
    private var basketBallDefaultFragment: EarlyBasketBallDefaultFragment? = null
    private var basketBallPassFragment: EarlyBasketBallPassFragment? = null
    private var footBallPassFragment: EarlyFootBallPassFragment? = null

    private lateinit var maskView: View

    private var currPage = 1
    private var ballType = -1

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

        changePage(LocaleController.getString(R.string.scroll_title1))
    }

    private val subTitleClickListener = GuessBallTopSubAdapter.OnGuessSubTitleClickListener { id, position ->
        if (currTitlePosition == 1) {
            twoTitleFootPosition = position
            twoTitleBasketPosition = -1
        } else {
            twoTitleBasketPosition = position
            twoTitleFootPosition = -1
        }

        //网络请求数据

        val str = footBallSubTitles.indices
                .firstOrNull { footBallSubTitles[it].id == id }
                ?.let { footBallSubTitles[it].name }
                ?: ""

        changePage(str)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        NotificationController.getInstance().addObserver(this, NotificationController.early_mask)
        return initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        NotificationController.getInstance().removeObserver(this, NotificationController.early_mask)
    }

    private fun initView(): View {
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
            NotificationController.getInstance().postNotification(NotificationController.early_mask, "child_close")
        }
        maskView.visibility = View.GONE
        maskView.setBackgroundColor(-0x5b000000)
        maskLayout.addView(maskView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 70))

        topCell = GuessBallTopCell(context)
        topCell.setTopListener(topClickListener)
        topCell.setSubTopListener(subTitleClickListener)
        contentLayout.addView(topCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        val parentLayout = LinearLayout(context)//用来替换fragment的布局
        parentLayout.id = R.id.early_parent
        container.addView(parentLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        init()
        return container
    }

    private fun initTitels() {
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
        subTitle5.name = LocaleController.getString(R.string.scroll_title14)

        val subTitle6 = GuessTopTitle()
        subTitle6.id = 8
        subTitle6.name = LocaleController.getString(R.string.scroll_title13)

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
        title1.count = 1

        val title2 = GuessTopTitle()
        title2.id = 2
        title2.name = LocaleController.getString(R.string.basketball)
        title2.count = 3

        topList.add(title1)
        topList.add(title2)

        topCell.setTopTitleData(topList)
        topCell.setTopSubTitleData(footBallSubTitles)
    }

    fun selectBasketBall() {
        if (currTitlePosition != 2 || twoTitleBasketPosition != 0) {

            currTitlePosition = 2

            twoTitleBasketPosition = 0

            topCell.setTopTitleSelect(1)
            topCell.setTopSubTitleSelect(twoTitleBasketPosition)
            topCell.refreshTitle()

            changePage(LocaleController.getString(R.string.scroll_title1))
        }
    }

    fun selectFootBall() {
        if (currTitlePosition != 1 || twoTitleFootPosition != 0) {

            currTitlePosition = 1

            twoTitleFootPosition = 0

            topCell.setTopTitleSelect(twoTitleFootPosition)
            topCell.setTopSubTitleSelect(twoTitleFootPosition)
            topCell.refreshTitle()

            changePage(LocaleController.getString(R.string.scroll_title1))
        }
    }

    fun setBallType(ballType: Int) {
        this.ballType = ballType
    }

    private fun init() {
        initTitels()

        when (ballType) {
            1 -> {
                selectFootBall()
            }
            2 -> {
                selectBasketBall()
            }
            else -> {
                defaultFragment = EarlyDefaultFragment()

                val fragmentTransaction = fragmentManager?.beginTransaction()
                fragmentTransaction?.add(R.id.early_parent, defaultFragment)
                fragmentTransaction?.commit()
            }
        }
    }

    fun fragmentResume() {
        when (currPage) {
            1 -> defaultFragment?.filterResume()
            2 -> boDanFragment?.filterResume()
            3 -> totalFragment?.filterResume()
            4 -> halfCourtFragment?.filterResume()
            5 -> resultFragment?.filterResume()
            6 -> earlyChampionFragment?.filterResume()
            7 -> footBallPassFragment?.filterResume()
            8 -> basketBallDefaultFragment?.filterResume()
            9 -> earlyBasketBallChampionFragmen?.filterResume()
            10 -> basketBallPassFragment?.filterResume()
        }
    }

    fun fragmentPause() {
        when (currPage) {
            1 -> defaultFragment?.filterPause()
            2 -> boDanFragment?.filterPause()
            3 -> totalFragment?.filterPause()
            4 -> halfCourtFragment?.filterPause()
            5 -> resultFragment?.filterPause()
            6 -> earlyChampionFragment?.filterPause()
            7 -> footBallPassFragment?.filterPause()
            8 -> basketBallDefaultFragment?.filterPause()
            9 -> earlyBasketBallChampionFragmen?.filterPause()
            10 -> basketBallPassFragment?.filterPause()
        }
    }

    private fun clearFilterData() {
        DataController.getInstance().clearEarlyFootBallData()
        DataController.getInstance().clearEarlyBasketBallData()
    }

    private fun changePage(str: String) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()

        //因早盘里每个页面都有可以选择的7天 这样就不能保持数据一致 所以每次更换页面都把筛选数据清空
        clearFilterData()

        defaultFragment?.let {
            defaultFragment?.filterPause()
            fragmentTransaction.hide(defaultFragment)
        }
        boDanFragment?.let {
            boDanFragment?.filterPause()
            fragmentTransaction.hide(boDanFragment)
        }
        totalFragment?.let {
            totalFragment?.filterPause()
            fragmentTransaction.hide(totalFragment)
        }
        halfCourtFragment?.let {
            halfCourtFragment?.filterPause()
            fragmentTransaction.hide(halfCourtFragment)
        }
        resultFragment?.let {
            resultFragment?.filterPause()
            fragmentTransaction.hide(resultFragment)
        }
        earlyChampionFragment?.let {
            earlyChampionFragment?.filterPause()
            fragmentTransaction.hide(earlyChampionFragment)
        }
        earlyBasketBallChampionFragmen?.let {
            earlyBasketBallChampionFragmen?.filterPause()
            fragmentTransaction.hide(earlyBasketBallChampionFragmen)
        }
        basketBallDefaultFragment?.let {
            basketBallDefaultFragment?.filterPause()
            fragmentTransaction.hide(basketBallDefaultFragment)
        }
        basketBallPassFragment?.let {
            basketBallPassFragment?.filterPause()
            fragmentTransaction.hide(basketBallPassFragment)
        }
        footBallPassFragment?.let {
            footBallPassFragment?.filterPause()
            fragmentTransaction.hide(footBallPassFragment)
        }

        when (currTitlePosition) {
            1 -> {
                when (str) {
                    LocaleController.getString(R.string.scroll_title1) -> {
                        if (defaultFragment == null) {
                            defaultFragment = EarlyDefaultFragment()
                            fragmentTransaction.add(R.id.early_parent, defaultFragment)
                        } else {
                            defaultFragment?.filterResume()
                            fragmentTransaction.show(defaultFragment)
                        }

                        currPage = 1
                    }
                    LocaleController.getString(R.string.scroll_title2) -> {
                        if (boDanFragment == null) {
                            boDanFragment = EarlyBoDanFragment()
                            fragmentTransaction.add(R.id.early_parent, boDanFragment)
                        } else {
                            boDanFragment!!.filterResume()
                            fragmentTransaction.show(boDanFragment)
                        }

                        currPage = 2
                    }
                    LocaleController.getString(R.string.scroll_title3) -> {
                        if (totalFragment == null) {
                            totalFragment = EarlyTotalFragment()
                            fragmentTransaction.add(R.id.early_parent, totalFragment)
                        } else {
                            totalFragment!!.filterResume()
                            fragmentTransaction.show(totalFragment)
                        }
                        currPage = 3
                    }
                    LocaleController.getString(R.string.scroll_title4) -> {
                        if (halfCourtFragment == null) {
                            halfCourtFragment = EarlyHalfCourtFragment()
                            fragmentTransaction.add(R.id.early_parent, halfCourtFragment)
                        } else {
                            halfCourtFragment!!.filterResume()
                            fragmentTransaction.show(halfCourtFragment)
                        }
                        currPage = 4
                    }
                    LocaleController.getString(R.string.scroll_title5) -> {
                        if (resultFragment == null) {
                            resultFragment = EarlyResultFragment()
                            fragmentTransaction.add(R.id.early_parent, resultFragment)
                        } else {
                            resultFragment!!.filterResume()
                            fragmentTransaction.show(resultFragment)
                        }
                        currPage = 5
                    }
                    LocaleController.getString(R.string.scroll_title13) -> {
                        if (earlyChampionFragment == null) {
                            earlyChampionFragment = EarlyChampionFragment()
                            fragmentTransaction.add(R.id.early_parent, earlyChampionFragment)
                        } else {
                            earlyChampionFragment!!.filterResume()
                            fragmentTransaction.show(earlyChampionFragment)
                        }
                        currPage = 6
                    }
                    LocaleController.getString(R.string.scroll_title14) -> {
                        if (footBallPassFragment == null) {
                            footBallPassFragment = EarlyFootBallPassFragment()
                            fragmentTransaction.add(R.id.early_parent, footBallPassFragment)
                        } else {
                            footBallPassFragment!!.filterResume()
                            fragmentTransaction.show(footBallPassFragment)
                        }
                        currPage = 7
                    }
                }
            }
            2 -> {
                when (str) {
                    LocaleController.getString(R.string.scroll_title1) -> {
                        if (basketBallDefaultFragment == null) {
                            basketBallDefaultFragment = EarlyBasketBallDefaultFragment()
                            fragmentTransaction.add(R.id.early_parent, basketBallDefaultFragment)
                        } else {
                            basketBallDefaultFragment!!.filterResume()
                            fragmentTransaction.show(basketBallDefaultFragment)
                        }
                        currPage = 8
                    }
                    LocaleController.getString(R.string.scroll_title13) -> {
                        if (earlyBasketBallChampionFragmen == null) {
                            earlyBasketBallChampionFragmen = EarlyBasketBallChampionFragment()
                            fragmentTransaction.add(R.id.early_parent, earlyBasketBallChampionFragmen)
                        } else {
                            earlyBasketBallChampionFragmen!!.filterResume()
                            fragmentTransaction.show(earlyBasketBallChampionFragmen)
                        }
                        currPage = 9
                    }
                    LocaleController.getString(R.string.scroll_title14) -> {
                        if (basketBallPassFragment == null) {
                            basketBallPassFragment = EarlyBasketBallPassFragment()
                            fragmentTransaction.add(R.id.early_parent, basketBallPassFragment)
                        } else {
                            basketBallPassFragment!!.filterResume()
                            fragmentTransaction.show(basketBallPassFragment)
                        }
                        currPage = 10
                    }
                }
            }
        }
        fragmentTransaction.commit()
    }

    override fun didReceivedNotification(id: Int, vararg args: String) {
        if (id == NotificationController.early_mask) {
            if (args != null && args.isNotEmpty()) {
                if ("open" == args[0]) {
                    openMask()
                } else if ("close" == args[0]) {
                    hideMask()
                }
            }
        }
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
}
