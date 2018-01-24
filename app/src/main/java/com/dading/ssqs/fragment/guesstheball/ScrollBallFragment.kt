package com.dading.ssqs.fragment.guesstheball

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.IntDef
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
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBasketBallDefaultFragment
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBoDanFragment
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallHalfCourtFragment
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallResultFragment
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallTotalFragment
import com.dading.ssqs.utils.DateUtils

import java.util.ArrayList

/**
 * Created by mazhuang on 2017/11/30.
 * 滚球
 *
 *
 * 因有多个页面比较类似，但是考虑到后续如果不同的页面做了一些功能上的改动
 * 对开发不利，把每个功能都拆成一个页面，方便用于后续功能维护、开发
 */

class ScrollBallFragment : Fragment(), NotificationController.NotificationControllerDelegate {

    private lateinit var topCell: GuessBallTopCell
    private lateinit var tvTitle: TextView
    private lateinit var maskView: View
    private lateinit var rightLayout: LinearLayout
    //fragments
    private var scrollball_parent: ScrollBallDefaultFragment? = null
    private var boDanFragment: ScrollBallBoDanFragment? = null
    private var totalFragment: ScrollBallTotalFragment? = null
    private var halfCourtFragment: ScrollBallHalfCourtFragment? = null
    private var resultFragment: ScrollBallResultFragment? = null
    private var ballBasketBallDefaultFragment: ScrollBallBasketBallDefaultFragment? = null

    private val footBallSubTitles = ArrayList<GuessTopTitle>()
    private val basketBallSubTitles = ArrayList<GuessTopTitle>()

    private var currTitlePosition = 1//一级标题position 默认足球
    private var twoTitleFootPosition = 0//二级标题足球的position  默认第一个选中
    private var twoTitleBasketPosition = -1//二级标题篮球的position

    private var currPage = 1//当前页面

    private var ballType = -1;

    //一级title点击事件
    private val topClickListener = GuessBallTopAdapter.OnGuessTopClickListener { id ->
        if (currTitlePosition != id) {
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

            val str = LocaleController.getString(R.string.scroll_title1)

            tvTitle.text = LocaleController.getString(R.string.scroll_ball) + "-" + (if (currTitlePosition == 1) LocaleController.getString(R.string.football) else LocaleController.getString(R.string.basketball)) + ":" + str

            changePage(str)
        }
    }

    //二级title点击事件
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

        tvTitle.text = LocaleController.getString(R.string.scroll_ball) + "-" + (if (currTitlePosition == 1) LocaleController.getString(R.string.football) else LocaleController.getString(R.string.basketball)) + ":" + str

        changePage(str)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = initView()

        NotificationController.getInstance().addObserver(this, NotificationController.scroll_mask)
        return view
    }

    fun setBallType(ballType: Int) {
        this.ballType = ballType
    }

    override fun onDestroyView() {
        super.onDestroyView()
        NotificationController.getInstance().removeObserver(this, NotificationController.scroll_mask)
    }

    private fun initView(): View {
        val container = LinearLayout(context)
        container.orientation = LinearLayout.VERTICAL
        container.layoutParams = ViewGroup.LayoutParams(LayoutHelper.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        val maskLayout = RelativeLayout(context)
        container.addView(maskLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        val contentLayout = LinearLayout(context)
        contentLayout.orientation = LinearLayout.VERTICAL
        maskLayout.addView(contentLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        maskView = View(context)
        maskView.setOnClickListener {
            hideMask()
            NotificationController.getInstance().postNotification(NotificationController.scroll_mask, "child_close")
        }
        maskView.visibility = View.GONE
        maskView.setBackgroundColor(-0x5b000000)
        maskLayout.addView(maskView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 100))

        topCell = GuessBallTopCell(context)
        topCell.setTopListener(topClickListener)
        topCell.setSubTopListener(subTitleClickListener)
        contentLayout.addView(topCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        val titleLayout = RelativeLayout(context)
        titleLayout.setBackgroundColor(-0xffbda3)
        contentLayout.addView(titleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        tvTitle = TextView(context)
        tvTitle.textSize = 13f
        tvTitle.setTextColor(Color.WHITE)
        tvTitle.text = LocaleController.getString(R.string.scroll_ball) + "-" + LocaleController.getString(R.string.football) + ":" + LocaleController.getString(R.string.scroll_title1)
        tvTitle.gravity = Gravity.CENTER
        titleLayout.addView(tvTitle, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, 30, 12, 0, 0, 0))

        rightLayout = LinearLayout(context)
        rightLayout.visibility = View.GONE
        rightLayout.orientation = LinearLayout.HORIZONTAL
        rightLayout.gravity = Gravity.CENTER_VERTICAL
        titleLayout.addView(rightLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, 30, 0, 0, 12, 0, RelativeLayout.ALIGN_PARENT_RIGHT))

        val leftTextView = TextView(context)
        leftTextView.setTextColor(-0x8c462a)
        leftTextView.textSize = 12f
        leftTextView.text = "主要盘口"
        rightLayout.addView(leftTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT))

        val lineView = View(context)
        lineView.setBackgroundColor(Color.WHITE)
        rightLayout.addView(lineView, LayoutHelper.createLinear(1, 13, 5f, 0f, 5f, 0f))

        val rightTextView = TextView(context)
        rightTextView.setTextColor(-0x8c462a)
        rightTextView.textSize = 12f
        rightTextView.text = "赛节投注"
        rightLayout.addView(rightTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT))

        val parentLayout = LinearLayout(context)//用来替换fragment的布局
        parentLayout.id = R.id.scrollball_parent
        container.addView(parentLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        init()
        return container
    }

    fun fragmentResume() {
        when (currPage) {
            1 -> scrollball_parent?.filterResume()
            2 -> boDanFragment?.filterResume()
            3 -> totalFragment?.filterResume()
            4 -> halfCourtFragment?.filterResume()
            5 -> resultFragment?.filterResume()
            6 -> ballBasketBallDefaultFragment?.filterResume()
        }
    }

    fun fragmentPause() {
        when (currPage) {
            1 -> scrollball_parent?.filterPause()
            2 -> boDanFragment?.filterPause()
            3 -> totalFragment?.filterPause()
            4 -> halfCourtFragment?.filterPause()
            5 -> resultFragment?.filterPause()
            6 -> ballBasketBallDefaultFragment?.filterPause()
        }
    }

    fun selectBasketBall() {
        if (currTitlePosition != 2 || twoTitleBasketPosition != 0 || ballBasketBallDefaultFragment == null) {

            currTitlePosition = 2

            twoTitleBasketPosition = 0

            topCell.setTopTitleSelect(1)
            topCell.setTopSubTitleSelect(twoTitleBasketPosition)
            topCell.refreshTitle()

            tvTitle.text = LocaleController.getString(R.string.scroll_ball) + "-" + LocaleController.getString(R.string.basketball) + ":" + LocaleController.getString(R.string.scroll_title1)

            changePage(LocaleController.getString(R.string.scroll_title1))
        }
    }

    fun selectFootBall() {
        if (currTitlePosition != 1 || twoTitleFootPosition != 0 || scrollball_parent == null) {

            currTitlePosition = 1

            twoTitleFootPosition = 0

            topCell.setTopTitleSelect(twoTitleFootPosition)
            topCell.setTopSubTitleSelect(twoTitleFootPosition)
            topCell.refreshTitle()

            tvTitle.text = LocaleController.getString(R.string.scroll_ball) + "-" + LocaleController.getString(R.string.football) + ":" + LocaleController.getString(R.string.scroll_title1)

            changePage(LocaleController.getString(R.string.scroll_title1))
        }
    }

    private fun changePage(str: String) {
        val fragmentTransaction = fragmentManager!!.beginTransaction()

        scrollball_parent?.let {
            fragmentTransaction.hide(scrollball_parent)
            scrollball_parent!!.filterPause()
        }
        boDanFragment?.let {
            fragmentTransaction.hide(boDanFragment)
            boDanFragment!!.filterPause()
        }
        totalFragment?.let {
            fragmentTransaction.hide(totalFragment)
            totalFragment!!.filterPause()
        }
        halfCourtFragment?.let {
            fragmentTransaction.hide(halfCourtFragment)
            halfCourtFragment!!.filterPause()
        }
        resultFragment?.let {
            fragmentTransaction.hide(resultFragment)
            resultFragment!!.filterPause()
        }
        ballBasketBallDefaultFragment?.let {
            fragmentTransaction.hide(ballBasketBallDefaultFragment)
            ballBasketBallDefaultFragment!!.filterPause()
        }

        rightLayout.visibility = View.GONE

        if (currTitlePosition == 1) {
            when (str) {
                LocaleController.getString(R.string.scroll_title1) -> {
                    if (scrollball_parent == null) {
                        scrollball_parent = ScrollBallDefaultFragment()
                        fragmentTransaction.add(R.id.scrollball_parent, scrollball_parent)
                    } else {
                        scrollball_parent!!.filterResume()
                        fragmentTransaction.show(scrollball_parent)
                    }
                    currPage = 1
                }
                LocaleController.getString(R.string.scroll_title2) -> {
                    if (boDanFragment == null) {
                        boDanFragment = ScrollBallBoDanFragment()
                        fragmentTransaction.add(R.id.scrollball_parent, boDanFragment)
                    } else {
                        boDanFragment!!.filterResume()
                        fragmentTransaction.show(boDanFragment)
                    }
                    currPage = 2
                }
                LocaleController.getString(R.string.scroll_title3) -> {
                    if (totalFragment == null) {
                        totalFragment = ScrollBallTotalFragment()
                        fragmentTransaction.add(R.id.scrollball_parent, totalFragment)
                    } else {
                        totalFragment!!.filterResume()
                        fragmentTransaction.show(totalFragment)
                    }
                    currPage = 3
                }
                LocaleController.getString(R.string.scroll_title4) -> {
                    if (halfCourtFragment == null) {
                        halfCourtFragment = ScrollBallHalfCourtFragment()
                        fragmentTransaction.add(R.id.scrollball_parent, halfCourtFragment)
                    } else {
                        halfCourtFragment!!.filterResume()
                        fragmentTransaction.show(halfCourtFragment)
                    }
                    currPage = 4
                }
                LocaleController.getString(R.string.scroll_title5) -> {
                    if (resultFragment == null) {
                        resultFragment = ScrollBallResultFragment()
                        fragmentTransaction.add(R.id.scrollball_parent, resultFragment)
                    } else {
                        resultFragment!!.filterResume()
                        fragmentTransaction.show(resultFragment)
                    }
                    currPage = 5
                }
            }
        } else {
            rightLayout!!.visibility = View.VISIBLE
            //篮球
            if (str == LocaleController.getString(R.string.scroll_title1)) {
                if (ballBasketBallDefaultFragment == null) {
                    ballBasketBallDefaultFragment = ScrollBallBasketBallDefaultFragment()
                    fragmentTransaction.add(R.id.scrollball_parent, ballBasketBallDefaultFragment)
                } else {
                    ballBasketBallDefaultFragment!!.filterResume()
                    fragmentTransaction.show(ballBasketBallDefaultFragment)
                }
                currPage = 6
            }
        }
        fragmentTransaction.commit()
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
        subTitle5.name = LocaleController.getString(R.string.scroll_title5)

        footBallSubTitles.add(subTitle1)
        footBallSubTitles.add(subTitle2)
        footBallSubTitles.add(subTitle3)
        footBallSubTitles.add(subTitle4)
        footBallSubTitles.add(subTitle5)

        basketBallSubTitles.add(subTitle1)

        createTitle(-1, -1)
    }

    private fun createTitle(fCount: Int, bCount: Int) {
        //一级标题
        val topList = ArrayList<GuessTopTitle>()

        val title1 = GuessTopTitle()
        title1.id = 1
        title1.name = LocaleController.getString(R.string.football)
        title1.count = fCount

        val title2 = GuessTopTitle()
        title2.id = 2
        title2.name = LocaleController.getString(R.string.basketball)
        title2.count = bCount

        topList.add(title1)
        topList.add(title2)

        topCell.setTopTitleData(topList)
        topCell.setTopSubTitleData(footBallSubTitles)
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
                scrollball_parent = ScrollBallDefaultFragment()

                val fragmentTransaction = fragmentManager!!.beginTransaction()
                fragmentTransaction.add(R.id.scrollball_parent, scrollball_parent)
                fragmentTransaction.commit()
            }
        }

        getFootBallTotal()
    }

    override fun didReceivedNotification(id: Int, vararg args: String) {
        if (id == NotificationController.scroll_mask) {
            if (args?.isNotEmpty()) {
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

    private fun getFootBallTotal() {
        val mDate = DateUtils.getCurTime("yyyyMMddHH:mm:ss")

        SSQSApplication.apiClient(0).getGuessBallFootBallTotal(6, mDate) { result ->
            if (result.isOk) {
                var fTotal = result.data as Int

                getBasketBallTotal(fTotal)
            }
        }
    }

    private fun getBasketBallTotal(fTotal: Int) {
        val mDate = DateUtils.getCurTime("yyyyMMddHH:mm:ss")

        SSQSApplication.apiClient(0).getGuessBallBasketBallTotal(6, mDate) { result ->
            if (result.isOk) {
                var bTotal = result.data as Int

                createTitle(fTotal, bTotal)
            }
        }
    }
}
