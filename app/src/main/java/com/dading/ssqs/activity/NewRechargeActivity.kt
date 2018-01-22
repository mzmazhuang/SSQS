package com.dading.ssqs.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.dading.ssqs.LocaleController
import com.dading.ssqs.R
import com.dading.ssqs.SSQSApplication
import com.dading.ssqs.adapter.newAdapter.BankAdapter
import com.dading.ssqs.adapter.newAdapter.FindTabAdapter
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.bean.Constent
import com.dading.ssqs.bean.WXDFBean
import com.dading.ssqs.cells.TitleCell
import com.dading.ssqs.components.SelectRechargeMoneyView
import com.dading.ssqs.fragment.recharge.BankFragment
import com.dading.ssqs.fragment.recharge.OnLineFragment
import com.dading.ssqs.fragment.recharge.WxFragment
import com.dading.ssqs.fragment.recharge.ZfbFragment
import com.dading.ssqs.utils.ToastUtils
import com.dading.ssqs.utils.UIUtils

import java.util.ArrayList

import com.dading.ssqs.bean.Constent.RECHARGE_INFO
import com.dading.ssqs.bean.QRCodeBean
import com.dading.ssqs.components.LoadingDialog

/**
 * Created by mazhuang on 2017/11/24.
 */

class NewRechargeActivity : BaseActivity() {

    private lateinit var moneyView: EditText
    private lateinit var selectRechargeMoneyView: SelectRechargeMoneyView
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    private lateinit var wxFragment: WxFragment
    private lateinit var zfbFragment: ZfbFragment
    private lateinit var bankFragment: BankFragment
    private lateinit var onLineFragment: OnLineFragment
    private var loadingDialog: LoadingDialog? = null

    private var mContext: Context? = null

    private var items: List<WXDFBean>? = null

    private var mType = 0

    private val listener = BankAdapter.OnRechargeClickListener { bean ->
        val money = Integer.valueOf(moneyView.text.toString())
        if (money <= 0) {
            ToastUtils.midToast(UIUtils.getContext(), "请输入具体金额", 0)
            return@OnRechargeClickListener
        }

        if (bean.addrType == 1) {
            bean.money = money
        } else {
            bean.money = money
            bean.payType = bean.id
            bean.remark = items!![mType].remark
        }

        val addrType = bean.addrType

        val intent: Intent
        val bundle: Bundle
        if (addrType == 1) {//银行转账
            intent = Intent(mContext, RechargeBankActivity::class.java)

            bundle = Bundle()
            bundle.putSerializable(Constent.RECHARGE_BANK, bean)

            setStartIntent(bundle, intent)
        } else if (addrType == 2 || addrType == 4 || addrType == 6 || addrType == 10) {//普通二维码
            intent = Intent(mContext, RechargeActivity::class.java)

            bundle = Bundle()
            bundle.putSerializable(RECHARGE_INFO, bean)

            setStartIntent(bundle, intent)
        } else if (addrType == 3) {//网页链接
            intent = Intent()
            intent.action = "android.intent.action.VIEW"
            val content_url = Uri.parse(bean.bankAddress)
            intent.data = content_url
            startActivity(intent)
        } else if (addrType == 7 || addrType == 8) {//网页源代码
            if (loadingDialog == null) {
                loadingDialog = LoadingDialog(this)
            }
            loadingDialog?.show()
            SSQSApplication.apiClient(classGuid).getThirdImage(bean.payType, bean.money) { result ->
                loadingDialog?.dismiss()
                if (result.isOk) {
                    val bean = result.data as QRCodeBean

                    val intent = Intent(mContext, WebActivity::class.java)

                    val bundle = Bundle()
                    bundle.putString("url_content", bean.data)

                    setStartIntent(bundle, intent)
                } else {
                    ToastUtils.midToast(UIUtils.getContext(), result.message, 0)
                }
            }
        }
    }

    override fun setLayoutId(): Int {
        return 0
    }

    //代码化布局
    override fun getContentView(): View? {
        mContext = this

        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        container.setBackgroundColor(Color.WHITE)

        val titleCell = TitleCell(this, resources.getString(R.string.recharge))
        titleCell.setBackListener { finish() }
        container.addView(titleCell, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 48))

        val topLayout = LinearLayout(this)
        topLayout.orientation = LinearLayout.VERTICAL
        container.addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        val inputLayout = RelativeLayout(this)
        inputLayout.isFocusable = true
        inputLayout.isFocusableInTouchMode = true
        topLayout.addView(inputLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 50, 10f, 8f, 10f, 0f))

        val chargeMoneyView = TextView(this)
        chargeMoneyView.id = R.id.chargeMoney
        chargeMoneyView.text = LocaleController.getString(R.string.recharge_num) + ":"
        chargeMoneyView.setTextColor(-0x777667)
        chargeMoneyView.textSize = 14f
        inputLayout.addView(chargeMoneyView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_VERTICAL))

        val tvMoney = TextView(this)
        tvMoney.textSize = 14f
        tvMoney.text = LocaleController.getString(R.string.yuan)
        tvMoney.setTextColor(-0x3cdce)
        val moneyLayoutParams = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 10, 0)
        moneyLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        moneyLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL)
        inputLayout.addView(tvMoney, moneyLayoutParams)

        moneyView = EditText(this)
        moneyView.inputType = InputType.TYPE_CLASS_NUMBER
        moneyView.background = null
        moneyView.textSize = 14f
        moneyView.gravity = Gravity.CENTER_VERTICAL
        moneyView.setTextColor(-0x8bda)
        moneyView.setText("10")//默认值
        val layoutParams = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, 10, 0, 10, 0)
        layoutParams.addRule(RelativeLayout.RIGHT_OF, chargeMoneyView.id)
        layoutParams.addRule(RelativeLayout.LEFT_OF, tvMoney.id)
        inputLayout.addView(moneyView, layoutParams)

        selectRechargeMoneyView = SelectRechargeMoneyView(this)
        selectRechargeMoneyView.setListener { text -> moneyView.setText(text) }
        topLayout.addView(selectRechargeMoneyView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 10f, 0f, 10f, 0f))

        val tipView = TextView(this)
        tipView.textSize = 14f
        tipView.text = LocaleController.getString(R.string.please_selelct_recharge)
        tipView.setTextColor(-0x1000000)
        topLayout.addView(tipView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 15f, 10f, 0f, 0f))

        tabLayout = TabLayout(this)
        tabLayout.tabMode = TabLayout.MODE_FIXED
        container.addView(tabLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 30, 0f, 10f, 0f, 0f))

        viewPager = ViewPager(this)
        viewPager.id = R.id.viewpager
        viewPager.offscreenPageLimit = 4
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                mType = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        container.addView(viewPager, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        val mListTitle = ArrayList<String>()
        mListTitle.add("微信支付")
        mListTitle.add("支付宝支付")
        mListTitle.add("银行转账")
        mListTitle.add("在线支付")

        wxFragment = WxFragment(listener)
        zfbFragment = ZfbFragment(listener)
        bankFragment = BankFragment(listener)
        onLineFragment = OnLineFragment()

        val listFragments = ArrayList<Fragment>()
        listFragments.add(wxFragment)
        listFragments.add(zfbFragment)
        listFragments.add(bankFragment)
        listFragments.add(onLineFragment)

        for (i in mListTitle.indices) {
            tabLayout.addTab(tabLayout.newTab().setText(mListTitle[i]))
        }

        val mFindTabAdapter = FindTabAdapter(supportFragmentManager, listFragments, mListTitle)

        viewPager.adapter = mFindTabAdapter
        tabLayout.setupWithViewPager(viewPager)

        getData()
        return container
    }

    private fun setStartIntent(bundle: Bundle, intent: Intent) {
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun getData() {
        SSQSApplication.apiClient(classGuid).getChargeList { result ->
            if (result.isOk) {
                items = result.data as List<WXDFBean>

                if (items != null && items!!.isNotEmpty()) {
                    if (items!![0].moneys != null) {
                        selectRechargeMoneyView.setList(items!![0].moneys)
                    }

                    for (i in items!!.indices) {
                        when {
                            items!![i].name == "微信支付" -> wxFragment.setList(items!![i].info)
                            items!![i].name == "支付宝支付" -> zfbFragment.setList(items!![i].info)
                            items!![i].name == "银行转账" -> bankFragment.setList(items!![i].info)
                        }
                    }
                }
            } else {
                ToastUtils.midToast(mContext, result.message, 0)
            }
        }
    }
}
