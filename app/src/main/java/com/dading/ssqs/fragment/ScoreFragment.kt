package com.dading.ssqs.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.dading.ssqs.LocaleController
import com.dading.ssqs.NotificationController
import com.dading.ssqs.R
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.components.LoadingDialog
import com.dading.ssqs.components.SelectMatchDialog
import com.dading.ssqs.fragment.guesstheball.ScoreDataController
import com.dading.ssqs.fragment.score.ScoreBasketBallFragment
import com.dading.ssqs.fragment.score.ScoreFootBallFragment
import com.dading.ssqs.fragment.score.basketball.BImmediateFragment
import com.dading.ssqs.fragment.score.basketball.BResultFragment
import com.dading.ssqs.fragment.score.basketball.BScheduleFragment
import com.dading.ssqs.fragment.score.football.FImmediateFragment
import com.dading.ssqs.fragment.score.football.FResultFragment
import com.dading.ssqs.fragment.score.football.FScheduleFragment
import com.dading.ssqs.utils.AndroidUtilities
import com.dading.ssqs.utils.DateUtils

/**
 * Created by mazhuang on 2018/1/26.
 * 比分
 */

class ScoreFragment : Fragment(), NotificationController.NotificationControllerDelegate {

    private val TAG = "ScoreFragment"

    private var mContext: Context? = null
    private var hasInit = false

    private var footBallTextView: TextView? = null
    private var basketBallTextView: TextView? = null
    private var filterView: ImageView? = null

    private var scoreFootBallFragment: ScoreFootBallFragment? = null
    private var scoreBasketBallFragment: ScoreBasketBallFragment? = null

    private var selectMatchDialog: SelectMatchDialog? = null
    private var loadingDialog: LoadingDialog? = null

    private var currPgae = 1

    private var footChildPage = 1//  1==即时  2==赛果  3==赛程  4==关注
    private var basketChildPage = 1//  1==即时  2==赛果  3==赛程  4==关注

    private var filterDate: String? = null//筛选的时间

    private var pageType: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = context

        NotificationController.getInstance().addObserver(this, NotificationController.scoreFootChildPage)
        NotificationController.getInstance().addObserver(this, NotificationController.scoreBasketChildPage)
        NotificationController.getInstance().addObserver(this, NotificationController.scoreFootBallFilter)
        NotificationController.getInstance().addObserver(this, NotificationController.scoreBasketBallFilter)
        return initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        NotificationController.getInstance().removeObserver(this, NotificationController.scoreFootChildPage)
        NotificationController.getInstance().removeObserver(this, NotificationController.scoreBasketChildPage)
        NotificationController.getInstance().removeObserver(this, NotificationController.scoreFootBallFilter)
        NotificationController.getInstance().removeObserver(this, NotificationController.scoreBasketBallFilter)
    }

    fun initView(): View {
        val container = LinearLayout(mContext)
        container.orientation = LinearLayout.VERTICAL

        val topLayout = RelativeLayout(mContext)
        topLayout.setBackgroundColor(resources.getColor(R.color.home_top_blue))
        container.addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48))

        val titleLayout = LinearLayout(mContext)
        titleLayout.orientation = LinearLayout.HORIZONTAL
        topLayout.addView(titleLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT, RelativeLayout.CENTER_IN_PARENT))

        footBallTextView = TextView(mContext)
        footBallTextView!!.textSize = 18f
        footBallTextView!!.setTextColor(Color.WHITE)
        footBallTextView!!.text = LocaleController.getString(R.string.football)
        footBallTextView!!.gravity = Gravity.CENTER_VERTICAL
        footBallTextView!!.setPadding(AndroidUtilities.dp(10f), 0, AndroidUtilities.dp(10f), 0)
        footBallTextView!!.setOnClickListener {
            changeTextColor(1)
            changePage(1)
        }
        titleLayout.addView(footBallTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT))

        basketBallTextView = TextView(mContext)
        basketBallTextView!!.textSize = 18f
        basketBallTextView!!.setTextColor(-0x9a2d01)
        basketBallTextView!!.setPadding(AndroidUtilities.dp(10f), 0, AndroidUtilities.dp(10f), 0)
        basketBallTextView!!.text = LocaleController.getString(R.string.basketball)
        basketBallTextView!!.gravity = Gravity.CENTER_VERTICAL
        basketBallTextView!!.setOnClickListener {
            changeTextColor(2)
            changePage(2)
        }
        titleLayout.addView(basketBallTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.MATCH_PARENT))

        filterView = ImageView(mContext)
        filterView!!.scaleType = ImageView.ScaleType.CENTER
        filterView!!.setImageResource(R.mipmap.ic_score_filter)
        filterView!!.setBackgroundDrawable(AndroidUtilities.createBarSelectorDrawable())
        filterView!!.setOnClickListener { filterMatch() }
        val filerLP = LayoutHelper.createRelative(40, 40, 0, 0, 15, 0)
        filerLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        filerLP.addRule(RelativeLayout.CENTER_VERTICAL)
        topLayout.addView(filterView, filerLP)

        val layout_parent = FrameLayout(mContext!!)
        layout_parent.id = R.id.score_parent
        container.addView(layout_parent, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        return container
    }

    //筛选比赛
    private fun filterMatch() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(mContext)
        }

        if (selectMatchDialog == null) {
            selectMatchDialog = SelectMatchDialog(mContext)
            selectMatchDialog!!.setListener { list, _ ->
                var leagueIDs = ""

                for (i in list.indices) {
                    leagueIDs += list[i] + ","
                }

                if (!TextUtils.isEmpty(leagueIDs)) {
                    leagueIDs = leagueIDs.substring(0, leagueIDs.length - 1)
                }

                //发通知
                sendNotices(leagueIDs)
            }
        }

        if (currPgae == 1) {//足球筛选
            if (footChildPage == 1) {//即时页面
                if (ScoreDataController.getInstance().footBallImmediateData == null) {
                    loadingDialog!!.show()
                    ScoreDataController.getInstance().syncFootBall(TAG, 2, filterDate!!)
                } else {
                    selectMatchDialog!!.show(ScoreDataController.getInstance().footBallImmediateData, ScoreDataController.getInstance().footBallImmediateHotData, "联赛选择")
                }
            } else if (footChildPage == 2) {//赛果
                if (ScoreDataController.getInstance().footBallResultData == null) {
                    loadingDialog!!.show()
                    ScoreDataController.getInstance().syncFootBall(TAG, 3, filterDate!!)
                } else {
                    selectMatchDialog!!.show(ScoreDataController.getInstance().footBallResultData, ScoreDataController.getInstance().footBallResultHotData, "联赛选择")
                }
            } else if (footChildPage == 3) {//赛程
                if (ScoreDataController.getInstance().footBallScheduleData == null) {
                    loadingDialog!!.show()
                    ScoreDataController.getInstance().syncFootBall(TAG, 4, filterDate!!)
                } else {
                    selectMatchDialog!!.show(ScoreDataController.getInstance().footBallScheduleData, ScoreDataController.getInstance().footBallScheduleHotData, "联赛选择")
                }
            }
        } else if (currPgae == 2) {//篮球筛选
            if (basketChildPage == 1) {//即时页面
                if (ScoreDataController.getInstance().basketBallImmediateData == null) {
                    loadingDialog!!.show()
                    ScoreDataController.getInstance().syncBasketBall(TAG, 2, filterDate!!)
                } else {
                    selectMatchDialog!!.show(ScoreDataController.getInstance().basketBallImmediateData, ScoreDataController.getInstance().basketBallImmediateHotData, "联赛选择")
                }
            } else if (basketChildPage == 2) {//赛果
                if (ScoreDataController.getInstance().basketBallResultData == null) {
                    loadingDialog!!.show()
                    ScoreDataController.getInstance().syncBasketBall(TAG, 3, filterDate!!)
                } else {
                    selectMatchDialog!!.show(ScoreDataController.getInstance().basketBallResultData, ScoreDataController.getInstance().basketBallResultData, "联赛选择")
                }
            } else if (basketChildPage == 3) {//赛程
                if (ScoreDataController.getInstance().basketBallScheduleData == null) {
                    loadingDialog!!.show()
                    ScoreDataController.getInstance().syncBasketBall(TAG, 4, filterDate!!)
                } else {
                    selectMatchDialog!!.show(ScoreDataController.getInstance().basketBallScheduleData, ScoreDataController.getInstance().basketBallScheduleHotData, "联赛选择")
                }
            }
        }
    }

    private fun sendNotices(leaguesId: String) {
        if (currPgae == 1) {//足球筛选
            when (footChildPage) {
                1 -> //即时页面
                    NotificationController.getInstance().postNotification(NotificationController.scoreChildRefresh, FImmediateFragment.TAG, leaguesId)
                2 -> NotificationController.getInstance().postNotification(NotificationController.scoreChildRefresh, FResultFragment.TAG, leaguesId)
                3 -> NotificationController.getInstance().postNotification(NotificationController.scoreChildRefresh, FScheduleFragment.TAG, leaguesId)
            }
        } else if (currPgae == 2) {//篮球筛选
            when (basketChildPage) {
                1 -> //即时页面
                    NotificationController.getInstance().postNotification(NotificationController.scoreChildRefresh, BImmediateFragment.TAG, leaguesId)
                2 -> NotificationController.getInstance().postNotification(NotificationController.scoreChildRefresh, BResultFragment.TAG, leaguesId)
                3 -> NotificationController.getInstance().postNotification(NotificationController.scoreChildRefresh, BScheduleFragment.TAG, leaguesId)
            }
        }
    }

    private fun changeTextColor(page: Int) {
        clearTextColor()

        if (page == 1) {
            footBallTextView!!.setTextColor(Color.WHITE)
        } else if (page == 2) {
            basketBallTextView!!.setTextColor(Color.WHITE)
        }
    }

    private fun changePage(pageType: Int) {
        val transaction = childFragmentManager.beginTransaction()

        if (scoreFootBallFragment != null) {
            transaction.hide(scoreFootBallFragment)
        }

        if (scoreBasketBallFragment != null) {
            transaction.hide(scoreBasketBallFragment)
        }

        if (pageType == 1) {
            if (scoreFootBallFragment == null) {
                scoreFootBallFragment = ScoreFootBallFragment()
                transaction.add(R.id.score_parent, scoreFootBallFragment)
            } else {
                transaction.show(scoreFootBallFragment)
            }

            currPgae = 1
        } else if (pageType == 2) {
            if (scoreBasketBallFragment == null) {
                scoreBasketBallFragment = ScoreBasketBallFragment()
                transaction.add(R.id.score_parent, scoreBasketBallFragment)
            } else {
                transaction.show(scoreBasketBallFragment)
            }

            currPgae = 2
        }

        transaction.commit()
    }

    private fun clearTextColor() {
        footBallTextView!!.setTextColor(-0x9a2d01)
        basketBallTextView!!.setTextColor(-0x9a2d01)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            if (!hasInit) {
                hasInit = true
                init()
            }
        }
    }

    private fun init() {
        scoreFootBallFragment = ScoreFootBallFragment()

        if (pageType > 0) {
            scoreFootBallFragment!!.setPageType(pageType)
        }

        val transaction = childFragmentManager.beginTransaction()
        transaction.add(R.id.score_parent, scoreFootBallFragment)
        transaction.commit()
    }

    fun setTitleType(type: Int, pageType: Int) {
        hasInit = true

        this.pageType = pageType

        changeTextColor(type)

        val transaction = childFragmentManager.beginTransaction()

        if (scoreFootBallFragment != null) {
            transaction.hide(scoreFootBallFragment)
        }

        if (scoreBasketBallFragment != null) {
            transaction.hide(scoreBasketBallFragment)
        }

        if (type == 1) {//足球
            if (scoreFootBallFragment == null) {
                init()
            } else {
                transaction.show(scoreFootBallFragment)

                if (pageType > 0) {
                    scoreFootBallFragment!!.setPageType(pageType)
                }
            }

            currPgae = 1
        } else if (type == 2) {//篮球
            if (scoreBasketBallFragment == null) {
                scoreBasketBallFragment = ScoreBasketBallFragment()

                if (pageType > 0) {
                    scoreBasketBallFragment!!.setPageType(pageType)
                }
                transaction.add(R.id.score_parent, scoreBasketBallFragment)
            } else {
                transaction.show(scoreBasketBallFragment)

                if (pageType > 0) {
                    scoreBasketBallFragment!!.setPageType(pageType)
                }
            }

            currPgae = 2
        }

        transaction.commit()
    }

    private fun handlerChildPage(childPage: Int, date: String) {
        if (childPage == 4) {
            filterView!!.visibility = View.GONE
        } else {
            filterView!!.visibility = View.VISIBLE
        }

        if (childPage == 2 && (TextUtils.isEmpty(filterDate) || TextUtils.isEmpty(date))) {//赛果是从昨天开始
            filterDate = DateUtils.getCurTimeAddND(-1, "yyyyMMddHH:mm:ss")
        } else if (childPage == 3 && (TextUtils.isEmpty(filterDate) || TextUtils.isEmpty(date))) {//赛程是从明天开始
            filterDate = DateUtils.getCurTimeAddND(1, "yyyyMMddHH:mm:ss")
        } else if (!TextUtils.isEmpty(date)) {
            filterDate = date
        } else {
            filterDate = ""
        }
    }

    override fun didReceivedNotification(id: Int, vararg args: String) {
        if (id == NotificationController.scoreFootChildPage) {
            if (args.isNotEmpty()) {
                footChildPage = Integer.valueOf(args[0])!!

                var date = ""

                if (args.size == 2) {
                    date = args[1]
                }

                handlerChildPage(footChildPage, date)

            }
        } else if (id == NotificationController.scoreBasketChildPage) {
            if (args.isNotEmpty()) {
                basketChildPage = Integer.valueOf(args[0])!!

                var date = ""

                if (args.size == 2) {
                    date = args[1]
                }

                handlerChildPage(basketChildPage, date)

            }
        } else if (id == NotificationController.scoreFootBallFilter) {
            if (args.isNotEmpty()) {
                if (TAG == args[0]) {
                    loadingDialog!!.dismiss()
                    when {
                        "2" == args[1] -> selectMatchDialog!!.show(ScoreDataController.getInstance().footBallImmediateData, ScoreDataController.getInstance().footBallImmediateHotData, "联赛选择")
                        "3" == args[1] -> selectMatchDialog!!.show(ScoreDataController.getInstance().footBallResultData, ScoreDataController.getInstance().footBallResultHotData, "联赛选择")
                        "4" == args[1] -> selectMatchDialog!!.show(ScoreDataController.getInstance().footBallScheduleData, ScoreDataController.getInstance().footBallScheduleHotData, "联赛选择")
                    }
                }
            }
        } else if (id == NotificationController.scoreBasketBallFilter) {
            if (args.isNotEmpty()) {
                if (TAG == args[0]) {
                    loadingDialog!!.dismiss()
                    when {
                        "2" == args[1] -> selectMatchDialog!!.show(ScoreDataController.getInstance().basketBallImmediateData, ScoreDataController.getInstance().basketBallImmediateHotData, "联赛选择")
                        "3" == args[1] -> selectMatchDialog!!.show(ScoreDataController.getInstance().basketBallResultData, ScoreDataController.getInstance().basketBallResultHotData, "联赛选择")
                        "4" == args[1] -> selectMatchDialog!!.show(ScoreDataController.getInstance().basketBallScheduleData, ScoreDataController.getInstance().basketBallScheduleHotData, "联赛选择")
                    }
                }
            }
        }
    }
}
