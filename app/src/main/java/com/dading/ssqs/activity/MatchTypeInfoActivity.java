package com.dading.ssqs.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.MyPostAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FouceArticleCategoryElement;
import com.dading.ssqs.bean.ALLCircleSingleHome;
import com.dading.ssqs.bean.ALLCircleThings;
import com.dading.ssqs.bean.AllCircleSingleBean;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.ListScrollUtil;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.GlideCircleTransform;

import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/15 18:00
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MatchTypeInfoActivity extends BaseActivity {

    private static final String TAG = "MatchTypeInfoActivity";
    @Bind(R.id.match_type_info_sc)
    ScrollView mMatchTypeInfoSc;
    @Bind(R.id.match_type_info_savantphoto)
    ImageView mMatchTypeInfoSavantphoto;
    @Bind(R.id.match_type_info_nickname)
    TextView mMatchTypeInfoNickname;
    @Bind(R.id.match_type_info_follow_cb)
    CheckBox mMatchTypeInfoFollowCb;
    @Bind(R.id.match_type_info_fans_num)
    TextView mMatchTypeInfoFansNum;
    @Bind(R.id.match_type_info_tiezi_num)
    TextView mMatchTypeInfoTieziNum;
    @Bind(R.id.match_type_info_lv)
    ListView mLv;
    @Bind(R.id.match_type_info_top)
    RelativeLayout mTop;
    private AllCircleSingleBean mSingleBean;
    private int mCbTag;
    private int mId;
    private int mIsFouce;
    private String mName;
    private TZRecevice mRecevice;
    private List<ALLCircleThings> mData;
    private int mFanCount;
    private boolean mIsFinish;
    private ALLCircleSingleHome mSingleHome;

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            mMatchTypeInfoFollowCb.setVisibility(View.VISIBLE);
        } else {
            mMatchTypeInfoFollowCb.setVisibility(View.GONE);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_match_type_info;
    }

    @Override
    protected void initData() {
        mRecevice = new TZRecevice();
        UIUtils.ReRecevice(mRecevice, Constent.TZ_SUC);

        mMatchTypeInfoSc.smoothScrollTo(0, 20);
        mLv.setFocusable(false);
        mLv.setFocusableInTouchMode(true);

        Intent intent = getIntent();

        String comeTag = intent.getStringExtra(Constent.ALL_CIRCLE_TYPE_TAG);
        String s = intent.getStringExtra(Constent.ALL_CIRCLE_TYPE);
        Logger.d(TAG, "得到sp圈子关注返回数据是------------------------------:" + s + comeTag);
        Logger.d(TAG, "得到sp圈子关注返回数据是------------------------------:" + comeTag);


        mIsFinish = true;

        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            mMatchTypeInfoFollowCb.setVisibility(View.VISIBLE);
        } else {
            mMatchTypeInfoFollowCb.setVisibility(View.GONE);
        }

        switch (comeTag) {
            case Constent.ALL_CIRCLE2_COME:
                mSingleBean = JSON.parseObject(s, AllCircleSingleBean.class);
                if (mSingleBean != null) {
                    mId = mSingleBean.id;
                    SSQSApplication.glide.load(mSingleBean.imageUrl).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(this)).into(mMatchTypeInfoSavantphoto);

                    Logger.d(TAG, "图片地址返回数据是------------------------------:" + mSingleBean.imageUrl);
                    mMatchTypeInfoNickname.setText(mSingleBean.name);
                    mFanCount = mSingleBean.fanCount;
                    String text = mFanCount + "";
                    mMatchTypeInfoFansNum.setText(text);
                    String text1 = mSingleBean.totalCount + "";
                    mMatchTypeInfoTieziNum.setText(text1);
                    mIsFouce = mSingleBean.isFouce;
                    Logger.d(TAG, "是否关注数据是------------------------------:" + mIsFouce);
                    if (mSingleBean.isFouce == 0) {
                        mMatchTypeInfoFollowCb.setChecked(false);
                    } else {
                        mMatchTypeInfoFollowCb.setChecked(true);
                    }
                }
                break;
            case Constent.HOME_THING2_COME:
                mSingleHome = JSON.parseObject(s, ALLCircleSingleHome.class);
                if (mSingleHome != null) {
                    mId = mSingleHome.categoryID;
                    mMatchTypeInfoNickname.setText(mSingleHome.userName);
                    mName = mSingleHome.categoryName;
                    if (mSingleHome != null)
                        SSQSApplication.glide.load(mSingleHome.categoryImageUrl).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(this)).into(mMatchTypeInfoSavantphoto);

                    mMatchTypeInfoNickname.setText(mName);
                    mFanCount = mSingleHome.fanCount;
                    mMatchTypeInfoFansNum.setText(String.valueOf(mFanCount));
                    String s2 = mSingleHome.hotCount + "";
                    mMatchTypeInfoTieziNum.setText(s2);
                    mIsFouce = mSingleHome.isFouce;
                    if (mSingleHome.isFouce == 0) {
                        mMatchTypeInfoFollowCb.setChecked(false);
                    } else {
                        mMatchTypeInfoFollowCb.setChecked(true);
                    }
                } else {
                    Logger.d(TAG, "woshi kong返回数据是------------------------------:" + s);
                }
                break;
            default:
                break;
        }
        /**
         * 6.	根据文章类别获取文章列表
         1)	请求地址：
         /v1.0/article/categoryID/{categoryID}/page/{page}/count/{count}
         2)	请求方式:
         get
         p3)	请求参数说明：
         categoryID：文章类型ID
         page:第几页
         count:页数
         auth_token：登陆后加入请求头
         */
        int page = 1;

        SSQSApplication.apiClient(classGuid).getArticleListById(mId, page, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultALLCircleThingsPage page1 = (CcApiResult.ResultALLCircleThingsPage) result.getData();

                    if (page1 != null && page1.getItems() != null) {
                        mData = page1.getItems();
                        processData(mData);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);

                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(MatchTypeInfoActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    @Override
    protected void initListener() {
        mMatchTypeInfoFollowCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!mIsFinish) {
                    if (isChecked) {
                        mMatchTypeInfoFollowCb.setChecked(false);
                    } else {
                        mMatchTypeInfoFollowCb.setChecked(true);
                    }
                }
                mIsFinish = false;
                if (mSingleBean != null || mSingleHome != null) {
                    final HashMap<String, Integer> body = new HashMap<>();
                    if (mIsFouce == 0) {
                        mCbTag = 1;

                        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                            FouceArticleCategoryElement element = new FouceArticleCategoryElement();
                            element.setArtCategoryID(String.valueOf(mData.get(0).categoryID));
                            element.setStatus(String.valueOf(mCbTag));

                            setFouce(element);
                        } else {
                            Intent intent = new Intent(MatchTypeInfoActivity.this, LoginActivity.class);
                            MatchTypeInfoActivity.this.startActivity(intent);
                        }
                    } else {
                        mCbTag = 0;

                        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                            FouceArticleCategoryElement element = new FouceArticleCategoryElement();
                            element.setArtCategoryID(String.valueOf(mData.get(0).categoryID));
                            element.setStatus(String.valueOf(mCbTag));

                            setFouce(element);
                        } else {
                            Intent intent = new Intent(MatchTypeInfoActivity.this, LoginActivity.class);
                            MatchTypeInfoActivity.this.startActivity(intent);
                        }
                    }
                } else {
                    Logger.d(TAG, "我是空");
                }
            }
        });
    }

    @Override
    protected void setUnDe() {
        UIUtils.UnReRecevice(mRecevice);
    }

    private void processData(List<ALLCircleThings> data) {
        if (data != null && data.size() != 0) {
            mLv.setAdapter(new MyPostAdapter(this, data));
            ListScrollUtil.setListViewHeightBasedOnChildren(mLv);
        }
    }

    @OnClick({R.id.match_type_info_return, R.id.match_type_info_publish})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.match_type_info_return:
                finish();
                break;
            case R.id.match_type_info_publish:
                Intent intent = new Intent(this, PuBlishTieZiActivity.class);
                intent.putExtra(Constent.ALL_CIRCLE_ID, mId);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    private void setFouce(FouceArticleCategoryElement element) {
        SSQSApplication.apiClient(classGuid).fouceArticleCategory(element, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                mIsFinish = true;

                if (result.isOk()) {
                    UIUtils.SendReRecevice(Constent.ALL_CIRCLE);
                    if (mIsFouce == 0) {
                        mIsFouce = 1;
                        mFanCount = mFanCount + 1;
                    } else {
                        mIsFouce = 0;
                        mFanCount = mFanCount - 1;
                    }
                    mMatchTypeInfoFansNum.setText(String.valueOf(mFanCount));
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(MatchTypeInfoActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (mIsFouce == 0) {
                            mMatchTypeInfoFollowCb.setChecked(false);
                        } else {
                            mMatchTypeInfoFollowCb.setChecked(true);
                        }
                    }
                }
            }
        });
    }

    private class TZRecevice extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }
}
