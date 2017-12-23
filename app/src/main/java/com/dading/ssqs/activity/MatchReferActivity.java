package com.dading.ssqs.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.MyReferPagerAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.controllar.myrefer.MyReferAllResult;
import com.dading.ssqs.controllar.myrefer.MyReferHBigSmall;
import com.dading.ssqs.controllar.myrefer.MyReferHLost;
import com.dading.ssqs.controllar.myrefer.MyReferHResult;
import com.dading.ssqs.controllar.myrefer.MyReferNowLost;
import com.dading.ssqs.controllar.myrefer.MyReferSmallBig;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.SpUtils;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import com.dading.ssqs.components.tabindicator.TabIndicator;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/17 16:45
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MatchReferActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MatchReferActivity";
    @Bind(R.id.my_referr_indicator)
    TabIndicator mMyReferrIndicator;
    @Bind(R.id.my_referr_viewpager)
    ViewPager mMyReferrViewpager;

    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.top_icon)
    ImageView mTopIcon;
    private ArrayList<Fragment> mList;
    private ArrayList<String> mListTab;
    private View mPopWindowWriter;
    private RelativeLayout mWriterPopuLy;
    private TextView mWriterToText;
    private TextView mWriterToLook;
    private TextView mWriterToAuthentication;
    private PopupWindow mPopupWindowWrite;
    private View mView;
    private int mPageID = 0;

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        initData();
    }

    @Override
    protected void initView() {
        mView = View.inflate(this, R.layout.activity_match_refer, null);
        /**
         * 写推荐popu在推荐里面进行监听
         */
        mPopWindowWriter = View.inflate(this, R.layout.refer_pop_view, null);
        mWriterPopuLy = (RelativeLayout) mPopWindowWriter.findViewById(R.id.refer_popu_ly);
        mWriterToText = (TextView) mPopWindowWriter.findViewById(R.id.refer_popu_text);
        mWriterToLook = (TextView) mPopWindowWriter.findViewById(R.id.refer_write_look);
        mWriterToAuthentication = (TextView) mPopWindowWriter.findViewById(R.id.refer_write_authentication);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_match_refer;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.my_refer));
        mTopIcon.setImageResource(R.mipmap.writing_recommend);
        mTopIcon.setVisibility(View.VISIBLE);
        mList = new ArrayList<>();
        mList.add(new MyReferAllResult());
        mList.add(new MyReferNowLost());
        mList.add(new MyReferSmallBig());
        mList.add(new MyReferHResult());
        mList.add(new MyReferHLost());
        mList.add(new MyReferHBigSmall());
        mListTab = new ArrayList<>();
        mListTab.add("全场赛果");
        mListTab.add("当前让球");
        mListTab.add("全场大小");
        mListTab.add("半场赛果");
        mListTab.add("半场让球");
        mListTab.add("半场大小");
        mMyReferrViewpager.setAdapter(new MyReferPagerAdapter(getSupportFragmentManager(), mList, mListTab));
        mMyReferrIndicator.setViewPager(mMyReferrViewpager);
        mMyReferrViewpager.setCurrentItem(mPageID);

        mPopupWindowWrite = PopUtil.popuMake(mPopWindowWriter);
    }

    @Override
    protected void initListener() {
        mPopupWindowWrite.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mTopIcon.setClickable(true);
            }
        });
        mMyReferrViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPageID = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        final SpUtils spUtils = new SpUtils(this);

        mWriterToText.setOnClickListener(this);
        mWriterPopuLy.setOnClickListener(this);
        mWriterToLook.setOnClickListener(this);
        mWriterToAuthentication.setOnClickListener(this);
    }

    @OnClick({R.id.top_back, R.id.top_icon})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.top_icon:
                boolean isLogin = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                if (isLogin) {
                    SSQSApplication.apiClient(classGuid).getUserInfo(new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                LoadingBean bean = (LoadingBean) result.getData();

                                if (bean != null) {
                                    if (bean.userType == 2) {
                                        Intent intent = new Intent(MatchReferActivity.this, WriteReferActivity.class);
                                        MatchReferActivity.this.startActivity(intent);
                                    } else {
                                        //弹出popwindow进行注册
                                        /**
                                         * PopupWindow显示在指定View对象的下面
                                         * 参数一:在这个View对象的下面
                                         * 参数二:x坐标的偏移量,给定的值是往参数目标控件宽度的负4分之3宽度移动
                                         * 参数二:y坐标的偏移量
                                         */
                                        mPopupWindowWrite.showAtLocation(mView, Gravity.CENTER, 0, 0);
                                        mTopIcon.setClickable(false);
                                    }
                                }
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(MatchReferActivity.this, LoginActivity.class);
                                    MatchReferActivity.this.startActivity(intent);
                                }
                            }
                        }
                    });
                } else {
                    Intent intentWriteReferr = new Intent(this, LoginActivity.class);
                    intentWriteReferr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(intentWriteReferr);
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refer_popu_text:
                break;
            case R.id.refer_popu_ly:
            case R.id.refer_write_look:
                mPopupWindowWrite.dismiss();
                break;
            case R.id.refer_write_authentication:
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    Logger.d(TAG, "pop消失------------------------------:");
                    SSQSApplication.apiClient(classGuid).getUserInfo(new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                LoadingBean bean = (LoadingBean) result.getData();

                                if (bean != null) {
                                    if (bean.isQualify != 1) {
                                        ToastUtils.midToast(MatchReferActivity.this, "对不起,您暂时还没有申请资格..", 0);
                                    } else if (bean.isApply == 1) {
                                        ToastUtils.midToast(MatchReferActivity.this, "您已经申请专家认证,请勿重复申请,请等待审核!", 0);
                                    } else {
                                        Intent savantAuth = new Intent(MatchReferActivity.this, SavantAuthenticationActivity.class);
                                        MatchReferActivity.this.startActivity(savantAuth);
                                        Logger.d(TAG, "用户是否注册过专家返回数据是------------------------------:" + bean.isApply);
                                    }
                                    mPopupWindowWrite.dismiss();

                                }
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(MatchReferActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    ToastUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                }
                            }
                        }
                    });
                } else {
                    Intent intentLoading = new Intent(MatchReferActivity.this, LoginActivity.class);
                    MatchReferActivity.this.startActivity(intentLoading);
                }
                break;
            default:
                break;
        }
    }

}
