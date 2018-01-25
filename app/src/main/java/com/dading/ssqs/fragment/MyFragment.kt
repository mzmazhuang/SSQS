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

    private var hasInit = false

    private lateinit var noLoadingAvatarView: AvatarView
    private lateinit var avatarView: AvatarView
    private lateinit var messageView: ImageView

    private lateinit var nickNameTextView: TextView
    private lateinit var numberTextView: TextView
    private lateinit var signTextView: TextView
    private lateinit var moneyTextView: TextView

    private lateinit var rechargeLayout: LinearLayout
    private lateinit var withdrawalLayout: LinearLayout
    private lateinit var receiveLayout: LinearLayout

    private lateinit var infoLayout: LinearLayout
    private lateinit var noLoadingAvatarLayout: LinearLayout

    private lateinit var accountDetailsCell: MeCell
    private lateinit var bettingRecordCell: MeCell
    private lateinit var rechargeRecordCell: MeCell
    private lateinit var withdrawalRecordCell: MeCell
    private lateinit var agencyCenterCell: MeCell
    private lateinit var inviteFriendCell: MeCell
    private lateinit var inviteMoneyCell: MeCell
    private lateinit var settingCell: MeCell
    private lateinit var feedbackCell: MeCell

    private lateinit var mReceiver: MyBroadcastReciver

    private var mSt: String? = null

    override fun onDestroyView() {
        super.onDestroyView()
        UIUtils.UnReRecevice(mReceiver)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

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

        if (!mSt.isNullOrEmpty() && "nobean" != mSt) {
            noLoadingAvatarLayout.visibility = View.GONE
            infoLayout.visibility = View.VISIBLE

            val mBean = JSON.parseObject(mSt, LoadingBean::class.java)
            if (mBean != null) {
                UIUtils.getSputils().putBoolean(Constent.IS_BIND_CARD, mBean.isBindCard == 1)

                UIUtils.getSputils().putBoolean(Constent.USER_TYPE, mBean.userType == 3)
                UIUtils.getSputils().putInt(Constent.USER_TYPE_NUM, mBean.userType)

                if (!mBean.avatar.isNullOrEmpty()) {
                    avatarView.setImageResource(mBean.avatar)
                }

                when (mBean.sex) {
                    1 -> nickNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_my_male, 0)
                    2 -> nickNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_my_female, 0)
                    3 -> nickNameTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_my_other, 0)
                }

                nickNameTextView.text = mBean.username

                val text = mBean.diamond.toString() + ""
                UIUtils.getSputils().putString(Constent.DIAMONDS, text)

                val num = mBean.banlance.toString() + ""
                UIUtils.getSputils().putString(Constent.GLODS, num)
                moneyTextView.text = num

                numberTextView.text = "会员号:" + mBean.id
                signTextView.text = mBean.signature
            }
        } else {
            noLoadingAvatarLayout.visibility = View.VISIBLE
            infoLayout.visibility = View.GONE
        }
    }

    private fun initView(): View {
        val container = LinearLayout(context)
        container.setBackgroundColor(Color.WHITE)
        container.orientation = LinearLayout.VERTICAL

        val contentLayout = LinearLayout(context)
        contentLayout.orientation = LinearLayout.VERTICAL
        container.addView(contentLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        val topLayout = RelativeLayout(context)
        topLayout.setBackgroundResource(R.mipmap.ic_my_top_background)
        contentLayout.addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 220))

        messageView = ImageView(context)
        messageView.setBackgroundDrawable(AndroidUtilities.createBarSelectorDrawable())
        messageView.scaleType = ImageView.ScaleType.CENTER
        messageView.setImageResource(R.mipmap.ic_my_message)
        messageView.setOnClickListener(this)
        topLayout.addView(messageView, LayoutHelper.createRelative(48, 48, 0, 10, 12, 0, RelativeLayout.ALIGN_PARENT_RIGHT))

        //已经登录的信息布局
        infoLayout = LinearLayout(context)
        infoLayout.visibility = View.GONE
        infoLayout.orientation = LinearLayout.HORIZONTAL
        infoLayout.setOnClickListener(this)
        topLayout.addView(infoLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 12, 35, 50, 0))

        avatarView = AvatarView(context)
        avatarView.setImageResource(R.mipmap.ic_default_avatar)
        infoLayout.addView(avatarView, LayoutHelper.createLinear(90, 90))

        val infoContentLayout = LinearLayout(context)
        infoContentLayout.orientation = LinearLayout.VERTICAL
        infoLayout.addView(infoContentLayout, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 15f, 0f, 0f, 0f))

        nickNameTextView = TextView(context)
        nickNameTextView.setTextColor(Color.WHITE)
        nickNameTextView.typeface = Typeface.DEFAULT_BOLD
        nickNameTextView.textSize = 14f
        nickNameTextView.compoundDrawablePadding = AndroidUtilities.dp(5f)
        nickNameTextView.setSingleLine()
        nickNameTextView.ellipsize = TextUtils.TruncateAt.END
        infoContentLayout.addView(nickNameTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT))

        numberTextView = TextView(context)
        numberTextView.setTextColor(-0x231e01)
        numberTextView.textSize = 12f
        numberTextView.setSingleLine()
        numberTextView.ellipsize = TextUtils.TruncateAt.END
        infoContentLayout.addView(numberTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0f, 8f, 0f, 0f))

        signTextView = TextView(context)
        signTextView.setTextColor(-0x231e01)
        signTextView.textSize = 12f
        signTextView.setSingleLine()
        signTextView.ellipsize = TextUtils.TruncateAt.END
        infoContentLayout.addView(signTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0f, 8f, 0f, 0f))

        moneyTextView = TextView(context)
        moneyTextView.textSize = 14f
        moneyTextView.setTextColor(Color.WHITE)
        moneyTextView.compoundDrawablePadding = AndroidUtilities.dp(5f)
        moneyTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_my_gold, 0, 0, 0)
        moneyTextView.setSingleLine()
        moneyTextView.ellipsize = TextUtils.TruncateAt.END
        infoContentLayout.addView(moneyTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0f, 8f, 0f, 0f))

        //没有登录的布局
        noLoadingAvatarLayout = LinearLayout(context)
        noLoadingAvatarLayout.gravity = Gravity.CENTER_HORIZONTAL
        noLoadingAvatarLayout.orientation = LinearLayout.VERTICAL
        noLoadingAvatarLayout.setOnClickListener { intentLogin() }
        topLayout.addView(noLoadingAvatarLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 35, 0, 0, RelativeLayout.CENTER_HORIZONTAL))

        noLoadingAvatarView = AvatarView(context)
        noLoadingAvatarView.setImageResource(R.mipmap.ic_default_avatar)
        noLoadingAvatarLayout.addView(noLoadingAvatarView, LayoutHelper.createLinear(90, 90))

        val noLoadingTipTextView = TextView(context)
        noLoadingTipTextView.textSize = 14f
        noLoadingTipTextView.setTextColor(Color.WHITE)
        noLoadingTipTextView.text = LocaleController.getString(R.string.no_loading_tip)
        noLoadingAvatarLayout.addView(noLoadingTipTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0f, 10f, 0f, 0f))

        val operationLayout = LinearLayout(context)
        operationLayout.orientation = LinearLayout.HORIZONTAL
        contentLayout.addView(operationLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40))

        rechargeLayout = LinearLayout(context)
        rechargeLayout.gravity = Gravity.CENTER_VERTICAL
        rechargeLayout.setBackgroundDrawable(AndroidUtilities.createListSelectorDrawable(context))
        rechargeLayout.setOnClickListener(this)
        operationLayout.addView(rechargeLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f))

        val rechargeTextView = TextView(context)
        rechargeTextView.textSize = 14f
        rechargeTextView.setTextColor(-0xcdcdce)
        rechargeTextView.text = LocaleController.getString(R.string.recharge)
        rechargeTextView.compoundDrawablePadding = AndroidUtilities.dp(5f)
        rechargeTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_my_recharge, 0, 0, 0)
        rechargeLayout.addView(rechargeTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 12f, 0f, 0f, 0f))

        withdrawalLayout = LinearLayout(context)
        withdrawalLayout.gravity = Gravity.CENTER
        withdrawalLayout.setBackgroundDrawable(AndroidUtilities.createListSelectorDrawable(context))
        withdrawalLayout.setOnClickListener(this)
        operationLayout.addView(withdrawalLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f))

        val withdrawalTextView = TextView(context)
        withdrawalTextView.textSize = 14f
        withdrawalTextView.setTextColor(-0xcdcdce)
        withdrawalTextView.text = LocaleController.getString(R.string.with_draw)
        withdrawalTextView.compoundDrawablePadding = AndroidUtilities.dp(5f)
        withdrawalTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_my_withdraw, 0, 0, 0)
        withdrawalLayout.addView(withdrawalTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT))

        receiveLayout = LinearLayout(context)
        receiveLayout.gravity = Gravity.CENTER_VERTICAL or Gravity.RIGHT
        receiveLayout.setBackgroundDrawable(AndroidUtilities.createListSelectorDrawable(context))
        receiveLayout.setOnClickListener(this)
        operationLayout.addView(receiveLayout, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f))

        val receiveTextView = TextView(context)
        receiveTextView.textSize = 14f
        receiveTextView.setTextColor(-0xcdcdce)
        receiveTextView.text = LocaleController.getString(R.string.free_get_glod)
        receiveTextView.compoundDrawablePadding = AndroidUtilities.dp(5f)
        receiveTextView.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_my_receive, 0, 0, 0)
        receiveLayout.addView(receiveTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0f, 0f, 12f, 0f))

        val lineView = View(context)
        lineView.setBackgroundColor(-0xa0b07)
        contentLayout.addView(lineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8, 0f, 15f, 0f, 0f))

        val scrollView = ScrollView(context)
        scrollView.isVerticalScrollBarEnabled = false
        contentLayout.addView(scrollView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        val cellLayout = LinearLayout(context)
        cellLayout.orientation = LinearLayout.VERTICAL
        scrollView.addView(cellLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        accountDetailsCell = MeCell(context, R.mipmap.ic_my_account_details, LocaleController.getString(R.string.account_detail), true)
        accountDetailsCell.setOnClickListener(this)
        cellLayout.addView(accountDetailsCell)

        bettingRecordCell = MeCell(context, R.mipmap.ic_my_betting_record, LocaleController.getString(R.string.betting_history), true)
        bettingRecordCell.setOnClickListener(this)
        cellLayout.addView(bettingRecordCell)

        rechargeRecordCell = MeCell(context, R.mipmap.ic_recharge_record, LocaleController.getString(R.string.recharge_detail), true)
        rechargeRecordCell.setOnClickListener(this)
        cellLayout.addView(rechargeRecordCell)

        withdrawalRecordCell = MeCell(context, R.mipmap.ic_witharaw_record, LocaleController.getString(R.string.witharaw_detail), false)
        withdrawalRecordCell.setOnClickListener(this)
        cellLayout.addView(withdrawalRecordCell)

        val lineView1 = View(context)
        lineView1.setBackgroundColor(-0xa0b07)
        cellLayout.addView(lineView1, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8))

        agencyCenterCell = MeCell(context, R.mipmap.ic_agency_center, LocaleController.getString(R.string.proxy_center), true)
        agencyCenterCell.setOnClickListener(this)
        cellLayout.addView(agencyCenterCell)

        inviteFriendCell = MeCell(context, R.mipmap.ic_invite_friend, LocaleController.getString(R.string.invite_friend), true)
        inviteFriendCell.setOnClickListener(this)
        cellLayout.addView(inviteFriendCell)

        inviteMoneyCell = MeCell(context, R.mipmap.ic_invite_money, LocaleController.getString(R.string.invite_money), false)
        inviteMoneyCell.setOnClickListener(this)
        cellLayout.addView(inviteMoneyCell)

        val lineView2 = View(context)
        lineView2.setBackgroundColor(-0xa0b07)
        cellLayout.addView(lineView2, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8))

        settingCell = MeCell(context, R.mipmap.ic_my_settings, LocaleController.getString(R.string.more_setting), true)
        settingCell.setOnClickListener(this)
        cellLayout.addView(settingCell)

        feedbackCell = MeCell(context, R.mipmap.ic_my_feedback, LocaleController.getString(R.string.suggestion), false)
        feedbackCell.setOnClickListener(this)
        cellLayout.addView(feedbackCell)

        val lineView3 = View(context)
        lineView3.setBackgroundColor(-0xa0b07)
        cellLayout.addView(lineView3, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8))

        return container
    }

    private fun intentLogin() {
        val mIntent = Intent(context, LoginActivity::class.java)
        startActivity(mIntent)
    }

    override fun onClick(view: View) {
        if (view === settingCell) {//更多设置
            val intentSetting = Intent(context, MoreSettingActivity::class.java)
            startActivity(intentSetting)
        } else if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            intentLogin()
        } else {
            val isTourist = UIUtils.getSputils().getBoolean(Constent.USER_TYPE, false)//是否是试玩

            var intent: Intent? = null
            when {
                view === messageView -> //消息
                    intent = Intent(context, MyMessageActivity::class.java)
                view === rechargeLayout -> {//充值
                    if (isTourist) {
                        ToastUtils.midToast(context, "试玩账号不能进行充值!", 0)
                    } else {
                        intent = Intent(context, NewRechargeActivity::class.java)
                    }
                }
                view === withdrawalLayout -> {//提现
                    if (isTourist) {
                        ToastUtils.midToast(context, "试玩账号不能进行提现!", 0)
                    } else {
                        intent = if (UIUtils.getSputils().getBoolean(Constent.IS_BIND_CARD, false)) {
                            Intent(context, WithDrawActivity::class.java)
                        } else {
                            Intent(context, NewBindBankCardActivity::class.java)
                        }
                    }
                }
                view === receiveLayout -> //领币
                    intent = Intent(context, HomeFreeGlodActivity::class.java)
                view === accountDetailsCell -> {//账户明细
                    if (isTourist) {
                        ToastUtils.midToast(context, "试玩账号不能查看账户明细!", 0)
                    }else{
                        intent = Intent(context, AccountDetailActivity::class.java)
                    }
                }
                view === bettingRecordCell -> //投注记录
                    intent = Intent(context, BettingRecordActivity::class.java)
                view === rechargeRecordCell -> {//充值记录
                    if (isTourist) {
                        ToastUtils.midToast(context, "试玩账号不能查看充值记录!", 0)
                    }else{
                        intent = Intent(context, RechargeDetailActivity::class.java)
                    }
                }
                view === withdrawalRecordCell -> {//提款记录
                    if (isTourist) {
                        ToastUtils.midToast(context, "试玩账号不能查看提款记录!", 0)
                    }else{
                        intent = Intent(context, WithDrawDentailActivity::class.java)
                    }
                }
                view === agencyCenterCell -> //代理中心
                    intent = Intent(context, ProxyCenterActivity::class.java)
                view === inviteFriendCell -> //邀请好友
                    intent = Intent(context, RecomWardsActivity::class.java)
                view === inviteMoneyCell -> //邀请领奖
                    intent = Intent(context, RecomCodePrizeActivity::class.java)
                view === feedbackCell -> //意见反馈
                    intent = Intent(context, SuggestionActivity::class.java)
                view === infoLayout -> {//修改信息
                    intent = Intent(context, ChangePhotoActivity::class.java)
                    intent.putExtra(Constent.MY_INFO, mSt)
                }
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
                        noLoadingAvatarLayout.visibility = View.VISIBLE
                        infoLayout.visibility = View.GONE
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
                    moneyTextView.text = num
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
