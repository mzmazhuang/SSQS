package com.dading.ssqs.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView

import com.alibaba.fastjson.JSON
import com.dading.ssqs.LocaleController
import com.dading.ssqs.R
import com.dading.ssqs.activity.AccountDetailActivity
import com.dading.ssqs.activity.BettingRecordActivity
import com.dading.ssqs.activity.ChangePhotoActivity
import com.dading.ssqs.activity.HomeFreeGlodActivity
import com.dading.ssqs.activity.LoginActivity
import com.dading.ssqs.activity.MoreSettingActivity
import com.dading.ssqs.activity.MyMessageActivity
import com.dading.ssqs.activity.NewBindBankCardActivity
import com.dading.ssqs.activity.NewRechargeActivity
import com.dading.ssqs.activity.ProxyCenterActivity
import com.dading.ssqs.activity.RechargeDetailActivity
import com.dading.ssqs.activity.RecomCodePrizeActivity
import com.dading.ssqs.activity.RecomWardsActivity
import com.dading.ssqs.activity.SuggestionActivity
import com.dading.ssqs.activity.WithDrawActivity
import com.dading.ssqs.activity.WithDrawDentailActivity
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.bean.Constent
import com.dading.ssqs.bean.LoadingBean
import com.dading.ssqs.components.AvatarView
import com.dading.ssqs.utils.AndroidUtilities
import com.dading.ssqs.utils.ToastUtils
import com.dading.ssqs.utils.UIUtils

/**
 * Created by mazhuang on 2018/1/4.
 */

class MyFragment : Fragment(), View.OnClickListener {

    private var mContext: Context? = null
    private var hasInit = false

    private var noLoadingAvatarView: AvatarView? = null
    private var avatarView: AvatarView? = null
    private var messageView: ImageView? = null

    private var nickNameTextView: TextView? = null
    private var numberTextView: TextView? = null
    private var signTextView: TextView? = null
    private var moneyTextView: TextView? = null

    private var rechargeLayout: LinearLayout? = null
    private var withdrawalLayout: LinearLayout? = null
    private var receiveLayout: LinearLayout? = null

    private var infoLayout: LinearLayout? = null
    private var noLoadingAvatarLayout: LinearLayout? = null

    private var accountDetailsCell: MeCell? = null
    private var bettingRecordCell: MeCell? = null
    private var rechargeRecordCell: MeCell? = null
    private var withdrawalRecordCell: MeCell? = null
    private var agencyCenterCell: MeCell? = null
    private var inviteFriendCell: MeCell? = null
    private var inviteMoneyCell: MeCell? = null
    private var settingCell: MeCell? = null
    private var feedbackCell: MeCell? = null

    private var mReceiver: MyBroadcastReciver? = null

    private var mSt: String? = null

    override fun onDestroyView() {
        super.onDestroyView()
        UIUtils.UnReRecevice(mReceiver)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mContext = context

        mReceiver = MyBroadcastReciver()
        UIUtils.ReRecevice(mReceiver, Constent.LOADING_ACTION)
        UIUtils.ReRecevice(mReceiver, Constent.SERIES)
        UIUtils.ReRecevice(mReceiver, Constent.IS_VIP)
        UIUtils.ReRecevice(mReceiver, Constent.REFRESH_MONY)

        return initView()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser) {
            if (!hasInit) {
                hasInit = true
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    updateInfo()
                }
            }
        }
    }

    private fun updateInfo() {
        mSt = UIUtils.getSputils().getString(Constent.LOADING_STATE_SP, "nobean")

        if (!TextUtils.isEmpty(mSt) && "nobean" != mSt) {
            noLoadingAvatarLayout!!.visibility = View.GONE
            infoLayout!!.visibility = View.VISIBLE

            val mBean = JSON.parseObject(mSt, LoadingBean::class.java)
            if (mBean != null) {
                UIUtils.getSputils().putBoolean(Constent.IS_BIND_CARD, mBean.isBindCard == 1)

                UIUtils.getSputils().putBoolean(Constent.USER_TYPE, mBean.userType == 3)
                UIUtils.getSputils().putInt(Constent.USER_TYPE_NUM, mBean.userType)

                if (!TextUtils.isEmpty(mBean.avatar)) {
                    avatarView!!.setImageResource(mBean.avatar)
                }

                when (mBean.sex) {
                    1 -> nickNameTextView!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_my_male, 0)
                    2 -> nickNameTextView!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_my_female, 0)
                    3 -> nickNameTextView!!.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_my_other, 0)
                    else -> {
                    }
                }

                nickNameTextView!!.text = mBean.username

                val text = mBean.diamond.toString() + ""
                UIUtils.getSputils().putString(Constent.DIAMONDS, text)

                val num = mBean.banlance.toString() + ""
                UIUtils.getSputils().putString(Constent.GLODS, num)
                moneyTextView!!.text = num

                numberTextView!!.text = "会员号:" + mBean.id
                signTextView!!.text = mBean.signature
            }
        } else {
            noLoadingAvatarLayout!!.visibility = View.VISIBLE
            infoLayout!!.visibility = View.GONE
        }
    }

    private fun initView(): View {
        val container = LinearLayout(mContext)
        container.setBackgroundColor(Color.WHITE)
        container.orientation = LinearLayout.VERTICAL

        val contentLayout = LinearLayout(mContext)
        contentLayout.orientation = LinearLayout.VERTICAL
        container.addView(contentLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        val topLayout = RelativeLayout(mContext)
        topLayout.setBackgroundResource(R.mipmap.ic_my_top_background)
        contentLayout.addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 220))

        messageView = ImageView(mContext)
        messageView!!.setBackgroundDrawable(AndroidUtilities.createBarSelectorDrawable())
        messageView!!.scaleType = ImageView.ScaleType.CENTER
        messageView!!.setImageResource(R.mipmap.ic_my_message)
        messageView!!.setOnClickListener(this)
        topLayout.addView(messageView, LayoutHelper.createRelative(48, 48, 0, 10, 12, 0, RelativeLayout.ALIGN_PARENT_RIGHT))

        //已经登录的信息布局
        infoLayout = LinearLayout(mContext)
        infoLayout!!.visibility = View.GONE
        infoLayout!!.orientation = LinearLayout.HORIZONTAL
        infoLayout!!.setOnClickListener(this)
        topLayout.addView(infoLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 12, 35, 50, 0))

        avatarView = AvatarView(mContext)
        avatarView!!.setImageResource(R.mipmap.ic_default_avatar)
        infoLayout!!.addView(avatarView, LayoutHelper.createLinear(90, 90))

        val infoContentLayout = LinearLayout(mContext)
        infoContentLayout.orientation = LinearLayout.VERTICAL
        infoLayout!!.addView(infoContentLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 15f, 0f, 0f, 0f))

        nickNameTextView = TextView(mContext)
        nickNameTextView!!.setTextColor(Color.WHITE)
        nickNameTextView!!.typeface = Typeface.DEFAULT_BOLD
        nickNameTextView!!.textSize = 14f
        nickNameTextView!!.compoundDrawablePadding = AndroidUtilities.dp(5f)
        nickNameTextView!!.setSingleLine()
        nickNameTextView!!.ellipsize = TextUtils.TruncateAt.END
        infoContentLayout.addView(nickNameTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT))

        numberTextView = TextView(mContext)
        numberTextView!!.setTextColor(-0x231e01)
        numberTextView!!.textSize = 12f
        numberTextView!!.setSingleLine()
        numberTextView!!.ellipsize = TextUtils.TruncateAt.END
        infoContentLayout.addView(numberTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0f, 8f, 0f, 0f))

        signTextView = TextView(mContext)
        signTextView!!.setTextColor(-0x231e01)
        signTextView!!.textSize = 12f
        signTextView!!.setSingleLine()
        signTextView!!.ellipsize = TextUtils.TruncateAt.END
        infoContentLayout.addView(signTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0f, 8f, 0f, 0f))

        moneyTextView = TextView(mContext)
        moneyTextView!!.textSize = 14f
        moneyTextView!!.setTextColor(Color.WHITE)
        moneyTextView!!.compoundDrawablePadding = AndroidUtilities.dp(5f)
        moneyTextView!!.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_my_gold, 0, 0, 0)
        moneyTextView!!.setSingleLine()
        moneyTextView!!.ellipsize = TextUtils.TruncateAt.END
        infoContentLayout.addView(moneyTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0f, 8f, 0f, 0f))

        //没有登录的布局
        noLoadingAvatarLayout = LinearLayout(mContext)
        noLoadingAvatarLayout!!.gravity = Gravity.CENTER_HORIZONTAL
        noLoadingAvatarLayout!!.orientation = LinearLayout.VERTICAL
        noLoadingAvatarLayout!!.setOnClickListener { intentLogin() }
        topLayout.addView(noLoadingAvatarLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 35, 0, 0, RelativeLayout.CENTER_HORIZONTAL))

        noLoadingAvatarView = AvatarView(mContext)
        noLoadingAvatarView!!.setImageResource(R.mipmap.ic_default_avatar)
        noLoadingAvatarLayout!!.addView(noLoadingAvatarView, LayoutHelper.createLinear(90, 90))

        val noLoadingTipTextView = TextView(mContext)
        noLoadingTipTextView.textSize = 14f
        noLoadingTipTextView.setTextColor(Color.WHITE)
        noLoadingTipTextView.text = LocaleController.getString(R.string.no_loading_tip)
        noLoadingAvatarLayout!!.addView(noLoadingTipTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0f, 10f, 0f, 0f))

        val operationLayout = LinearLayout(mContext)
        operationLayout.orientation = LinearLayout.HORIZONTAL
        contentLayout.addView(operationLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40))

        rechargeLayout = LinearLayout(mContext)
        rechargeLayout!!.gravity = Gravity.CENTER_VERTICAL
        rechargeLayout!!.setBackgroundDrawable(AndroidUtilities.createListSelectorDrawable(mContext!!))
        rechargeLayout!!.setOnClickListener(this)
        operationLayout.addView(rechargeLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f))

        val rechargeTextView = TextView(mContext)
        rechargeTextView.textSize = 14f
        rechargeTextView.setTextColor(-0xcdcdce)
        rechargeTextView.text = LocaleController.getString(R.string.recharge)
        rechargeTextView.compoundDrawablePadding = AndroidUtilities.dp(5f)
        rechargeTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_my_recharge, 0, 0, 0)
        rechargeLayout!!.addView(rechargeTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 12f, 0f, 0f, 0f))

        withdrawalLayout = LinearLayout(mContext)
        withdrawalLayout!!.gravity = Gravity.CENTER
        withdrawalLayout!!.setBackgroundDrawable(AndroidUtilities.createListSelectorDrawable(mContext!!))
        withdrawalLayout!!.setOnClickListener(this)
        operationLayout.addView(withdrawalLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f))

        val withdrawalTextView = TextView(mContext)
        withdrawalTextView.textSize = 14f
        withdrawalTextView.setTextColor(-0xcdcdce)
        withdrawalTextView.text = LocaleController.getString(R.string.with_draw)
        withdrawalTextView.compoundDrawablePadding = AndroidUtilities.dp(5f)
        withdrawalTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_my_withdraw, 0, 0, 0)
        withdrawalLayout!!.addView(withdrawalTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT))

        receiveLayout = LinearLayout(mContext)
        receiveLayout!!.gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT
        receiveLayout!!.setBackgroundDrawable(AndroidUtilities.createListSelectorDrawable(mContext!!))
        receiveLayout!!.setOnClickListener(this)
        operationLayout.addView(receiveLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f))

        val receiveTextView = TextView(mContext)
        receiveTextView.textSize = 14f
        receiveTextView.setTextColor(-0xcdcdce)
        receiveTextView.text = LocaleController.getString(R.string.free_get_glod)
        receiveTextView.compoundDrawablePadding = AndroidUtilities.dp(5f)
        receiveTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_my_receive, 0, 0, 0)
        receiveLayout!!.addView(receiveTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0f, 0f, 12f, 0f))

        val lineView = View(mContext)
        lineView.setBackgroundColor(-0xa0b07)
        contentLayout.addView(lineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8, 0f, 15f, 0f, 0f))

        val scrollView = ScrollView(mContext)
        scrollView.isVerticalScrollBarEnabled = false
        contentLayout.addView(scrollView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        val cellLayout = LinearLayout(mContext)
        cellLayout.orientation = LinearLayout.VERTICAL
        scrollView.addView(cellLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        accountDetailsCell = MeCell(mContext!!, R.mipmap.ic_my_account_details, LocaleController.getString(R.string.account_detail), true)
        accountDetailsCell!!.setOnClickListener(this)
        cellLayout.addView(accountDetailsCell)

        bettingRecordCell = MeCell(mContext!!, R.mipmap.ic_my_betting_record, LocaleController.getString(R.string.betting_history), true)
        bettingRecordCell!!.setOnClickListener(this)
        cellLayout.addView(bettingRecordCell)

        rechargeRecordCell = MeCell(mContext!!, R.mipmap.ic_recharge_record, LocaleController.getString(R.string.recharge_detail), true)
        rechargeRecordCell!!.setOnClickListener(this)
        cellLayout.addView(rechargeRecordCell)

        withdrawalRecordCell = MeCell(mContext!!, R.mipmap.ic_witharaw_record, LocaleController.getString(R.string.witharaw_detail), false)
        withdrawalRecordCell!!.setOnClickListener(this)
        cellLayout.addView(withdrawalRecordCell)

        val lineView1 = View(mContext)
        lineView1.setBackgroundColor(-0xa0b07)
        cellLayout.addView(lineView1, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8))

        agencyCenterCell = MeCell(mContext!!, R.mipmap.ic_agency_center, LocaleController.getString(R.string.proxy_center), true)
        agencyCenterCell!!.setOnClickListener(this)
        cellLayout.addView(agencyCenterCell)

        inviteFriendCell = MeCell(mContext!!, R.mipmap.ic_invite_friend, LocaleController.getString(R.string.invite_friend), true)
        inviteFriendCell!!.setOnClickListener(this)
        cellLayout.addView(inviteFriendCell)

        inviteMoneyCell = MeCell(mContext!!, R.mipmap.ic_invite_money, LocaleController.getString(R.string.invite_money), false)
        inviteMoneyCell!!.setOnClickListener(this)
        cellLayout.addView(inviteMoneyCell)

        val lineView2 = View(mContext)
        lineView2.setBackgroundColor(-0xa0b07)
        cellLayout.addView(lineView2, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8))

        settingCell = MeCell(mContext!!, R.mipmap.ic_my_settings, LocaleController.getString(R.string.more_setting), true)
        settingCell!!.setOnClickListener(this)
        cellLayout.addView(settingCell)

        feedbackCell = MeCell(mContext!!, R.mipmap.ic_my_feedback, LocaleController.getString(R.string.suggestion), false)
        feedbackCell!!.setOnClickListener(this)
        cellLayout.addView(feedbackCell)

        val lineView3 = View(mContext)
        lineView3.setBackgroundColor(-0xa0b07)
        cellLayout.addView(lineView3, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8))

        return container
    }

    private fun intentLogin() {
        val mIntent = Intent(mContext, LoginActivity::class.java)
        startActivity(mIntent)
    }

    override fun onClick(view: View) {
        if (view === settingCell) {//更多设置
            val intentSetting = Intent(mContext, MoreSettingActivity::class.java)
            startActivity(intentSetting)
        } else if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            intentLogin()
        } else {
            val isTourist = UIUtils.getSputils().getBoolean(Constent.USER_TYPE, false)//是否是试玩

            var intent: Intent? = null
            if (view === messageView) {//消息
                intent = Intent(mContext, MyMessageActivity::class.java)
            } else if (view === rechargeLayout) {//充值
                if (isTourist) {
                    ToastUtils.midToast(mContext, "试玩账号不能进行充值!", 0)
                    return
                }
                intent = Intent(mContext, NewRechargeActivity::class.java)
            } else if (view === withdrawalLayout) {//提现
                if (isTourist) {
                    ToastUtils.midToast(mContext, "试玩账号不能进行提现!", 0)
                    return
                }
                if (UIUtils.getSputils().getBoolean(Constent.IS_BIND_CARD, false)) {
                    intent = Intent(mContext, WithDrawActivity::class.java)
                } else {
                    intent = Intent(mContext, NewBindBankCardActivity::class.java)
                }
            } else if (view === receiveLayout) {//领币
                intent = Intent(mContext, HomeFreeGlodActivity::class.java)
            } else if (view === accountDetailsCell) {//账户明细
                if (isTourist) {
                    ToastUtils.midToast(mContext, "试玩账号不能查看账户明细!", 0)
                    return
                }
                intent = Intent(mContext, AccountDetailActivity::class.java)
            } else if (view === bettingRecordCell) {//投注记录
                intent = Intent(mContext, BettingRecordActivity::class.java)
            } else if (view === rechargeRecordCell) {//充值记录
                if (isTourist) {
                    ToastUtils.midToast(mContext, "试玩账号不能查看充值记录!", 0)
                    return
                }
                intent = Intent(mContext, RechargeDetailActivity::class.java)
            } else if (view === withdrawalRecordCell) {//提款记录
                if (isTourist) {
                    ToastUtils.midToast(mContext, "试玩账号不能查看提现!", 0)
                    return
                }
                intent = Intent(mContext, WithDrawDentailActivity::class.java)
            } else if (view === agencyCenterCell) {//代理中心
                intent = Intent(mContext, ProxyCenterActivity::class.java)
            } else if (view === inviteFriendCell) {//邀请好友
                intent = Intent(mContext, RecomWardsActivity::class.java)
            } else if (view === inviteMoneyCell) {//邀请领奖
                intent = Intent(mContext, RecomCodePrizeActivity::class.java)
            } else if (view === feedbackCell) {//意见反馈
                intent = Intent(mContext, SuggestionActivity::class.java)
            } else if (view === infoLayout) {//修改信息
                intent = Intent(mContext, ChangePhotoActivity::class.java)
                intent.putExtra(Constent.MY_INFO, mSt)
            }

            if (intent != null) {
                startActivity(intent)
            }
        }
    }

    private inner class MyBroadcastReciver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action

            when (action) {
                Constent.LOADING_ACTION -> {
                    //通过传递广播的数据来判断是否登录成功
                    val b = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)
                    if (b) {
                        updateInfo()
                    } else {
                        noLoadingAvatarLayout!!.visibility = View.VISIBLE
                        infoLayout!!.visibility = View.GONE
                    }
                }
                Constent.REFRESH_MONY -> {
                    val banlance = UIUtils.getSputils().getInt("banlance", 0)
                    val num: String
                    if (banlance > 0) {
                        num = (Integer.valueOf(UIUtils.getSputils().getString(Constent.GLODS, ""))!! + banlance).toString() + ""

                        UIUtils.getSputils().putString(Constent.GLODS, num)
                    } else {
                        num = UIUtils.getSputils().getString(Constent.GLODS, "")
                    }
                    moneyTextView!!.text = num
                }
            }
        }
    }

    internal inner class MeCell(context: Context, resId: Int, title: String, isLine: Boolean) : RelativeLayout(context) {

        private val iconView: ImageView
        private val tvTitleView: TextView

        init {

            setBackgroundDrawable(AndroidUtilities.createListSelectorDrawable(context))
            layoutParams = LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48)

            iconView = ImageView(context)
            iconView.scaleType = ImageView.ScaleType.CENTER
            iconView.setImageResource(resId)
            addView(iconView, LayoutHelper.createRelative(20, LayoutHelper.WRAP_CONTENT, 12, 0, 0, 0, RelativeLayout.CENTER_VERTICAL))

            tvTitleView = TextView(context)
            tvTitleView.textSize = 14f
            tvTitleView.setTextColor(-0xcdcdce)
            tvTitleView.text = title
            addView(tvTitleView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 42, 0, 0, 0, RelativeLayout.CENTER_VERTICAL))

            val arrowView = ImageView(context)
            arrowView.setImageResource(R.mipmap.ic_my_arrow)
            val arrowLP = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 12, 0)
            arrowLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            arrowLP.addRule(RelativeLayout.CENTER_VERTICAL)
            addView(arrowView, arrowLP)

            if (isLine) {
                val view = View(context)
                view.setBackgroundColor(-0x121213)
                val viewLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 1)
                viewLP.setMargins(AndroidUtilities.dp(42f), 0, 0, 0)
                viewLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                addView(view, viewLP)
            }
        }
    }
}
