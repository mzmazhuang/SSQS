package com.dading.ssqs.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.SavantInfoAdapter;
import com.dading.ssqs.adapter.SavantInfoVpAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FocusUserElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.SavantInfoBean;
import com.dading.ssqs.controllar.savantinfo.SavantInfoStatetrendControllarSingle;
import com.dading.ssqs.utils.ListScrollUtil;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.GlideCircleTransform;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/5 15:12
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SavantInfoActivity extends BaseActivity implements ViewPager.OnPageChangeListener,
        AdapterView.OnItemClickListener {
    private static final String TAG = "SavantInfoActivity";
    @Bind(R.id.savant_info_sc)
    ScrollView mSavantInfSC;
    @Bind(R.id.savant_info_savantphoto)
    ImageView mSavantInfphoto;
    @Bind(R.id.savant_info_nickname)
    TextView mSavantInfoNickname;
    @Bind(R.id.savant_info_nickleve)
    TextView mSavantInfoNickleve;
    @Bind(R.id.savant_info_top_text)
    TextView mSavantInfoTopText;


    @Bind(R.id.savant_info_lianhong)
    TextView mSavantInfoLianhong;
    @Bind(R.id.savant_info_fans_num)
    TextView mSavantInfoFansNum;
    @Bind(R.id.savant_info_refer_num)
    TextView mSavantInfoReferNum;
    @Bind(R.id.savant_info_essay_num)
    TextView mSavantInfoEssayNum;
    @Bind(R.id.savant_info_viewpager)
    ViewPager mSavantInfoVp;

    @Bind(R.id.savant_info_rg1)
    RadioButton mRg1;
    @Bind(R.id.savant_info_rg2)
    RadioButton mRg2;
    @Bind(R.id.savant_info_rg3)
    RadioButton mRg3;
    @Bind(R.id.savant_info_listview)
    ListView mSavantInfoListview;

    @Bind(R.id.savant_info_follow_cb)
    CheckBox mSavantInfoFollowCb;
    @Bind(R.id.savat_info_up_down)
    ImageView mSavantInfoUpDown;

    @Bind(R.id.savant_info_match_all)
    TextView mSavantInfoMatchAll;

    @Bind(R.id.loading_animal)
    LinearLayout loadingAnimal;
    private String mUserID;
    private SavantInfoBean mInfoData;
    private ArrayList<View> mList;
    private int mFansNum;
    private boolean arrowDown = true;
    private String mUrl;

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        initData();
        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            mSavantInfoFollowCb.setVisibility(View.VISIBLE);
        } else {
            mSavantInfoFollowCb.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mUserID = intent.getStringExtra(Constent.SAVANT_ID);

        mSavantInfoListview.setFocusable(false);
        mSavantInfoListview.setFocusableInTouchMode(true);
        mSavantInfoVp.setFocusable(false);
        mSavantInfoVp.setFocusableInTouchMode(true);

        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            mSavantInfoFollowCb.setVisibility(View.VISIBLE);
        } else {
            mSavantInfoFollowCb.setVisibility(View.GONE);
        }
        /**
         * a)	请求地址：
         /v1.0/expert/detail/userID/{userID}
         b)	请求方式:
         get
         c)	请求参数说明：
         userID :用户id
         auth_token：登陆后加入请求头
         */

        SSQSApplication.apiClient(classGuid).getExpertDetails(mUserID, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    SavantInfoBean bean = (SavantInfoBean) result.getData();

                    if (bean != null) {
                        processData(bean);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(SavantInfoActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                    loadingAnimal.setVisibility(View.GONE);
                    mSavantInfSC.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_savant_info;
    }

    @Override
    protected void initListener() {
        mSavantInfoFollowCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    Intent intent = new Intent(SavantInfoActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                /**
                 a)	请求地址：
                 /v1.0/fouce
                 b)	请求方式:
                 Post
                 c)	请求参数说明：
                 {"fouceUserID":"20160708134662","status":1}
                 fouceUserID:被关注的用户ID，
                 status:  0-取消关注 1-关注
                 auth_token：登陆后加入请求头
                 */
                LogUtil.util(TAG, "我被选中了");

                FocusUserElement element = new FocusUserElement();
                element.setFouceUserID(mUserID);
                element.setStatus(mInfoData.isFouce == 0 ? "1" : "0");

                SSQSApplication.apiClient(classGuid).focusUser(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            if (mInfoData.isFouce == 0) {
                                LogUtil.util(TAG, result.getMessage() + "关注成功信息");
                                mInfoData.isFouce = 1;
                                mFansNum = mFansNum + 1;
                                mSavantInfoFansNum.setText(String.valueOf(mFansNum));
                            } else {
                                LogUtil.util(TAG, result.getMessage() + "取消关注成功信息");
                                mFansNum = mFansNum - 1;
                                mSavantInfoFansNum.setText(String.valueOf(mFansNum));
                                mInfoData.isFouce = 0;
                            }
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(SavantInfoActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                            }
                        }
                    }
                });
            }
        });
        mSavantInfoVp.addOnPageChangeListener(this);
        mSavantInfoListview.setOnItemClickListener(this);
    }

    @OnClick({R.id.savant_info_return, R.id.savat_info_up_down, R.id.savant_info_essay, R.id.savant_info_essay_num,
            R.id.savant_info_refer, R.id.savant_info_refer_num, R.id.savant_info_fans, R.id.savant_info_fans_num,
            R.id.savant_info_match_all, R.id.savant_info_more_data})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.savant_info_return:
                finish();
                break;
            case R.id.savant_info_fans:
            case R.id.savant_info_fans_num:
                Intent intentFans = new Intent(this, FansInfoActivity.class);
                intentFans.putExtra(Constent.SAVANT_ID, mUserID);
                startActivity(intentFans);
                break;
            case R.id.savant_info_refer:
            case R.id.savant_info_refer_num:
            case R.id.savant_info_match_all:
            case R.id.savant_info_more_data:
                Intent intentRefer = new Intent(this, ReferInfosActivity.class);
                intentRefer.putExtra(Constent.SAVANT_ID, mUserID);
                startActivity(intentRefer);
                break;
            case R.id.savant_info_essay_num:
            case R.id.savant_info_essay:
                Intent intentEssay = new Intent(this, EssayInfoActivity.class);
                intentEssay.putExtra(Constent.SAVANT_ID, mUserID);
                startActivity(intentEssay);
                break;
            case R.id.savat_info_up_down:

                if (!arrowDown) {
                    arrowDown = true;
                    mSavantInfoTopText.setMaxLines(2);
                    mSavantInfoTopText.setText(mInfoData.intro);
                } else {
                    arrowDown = false;
                    mSavantInfoTopText.setMaxLines(5);
                    mSavantInfoTopText.setText(mInfoData.intro);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            mRg1.setChecked(true);
        } else if (position == 1) {
            mRg2.setChecked(true);
        } else {
            mRg3.setChecked(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intentRefer = new Intent(this, SavantLvItemActivity.class);
        if (mInfoData.recomm != null) {
            SavantInfoBean.RecommEntity entity = mInfoData.recomm.get(position);
            String value = entity.id + "";
            intentRefer.putExtra(Constent.MATCH_ID, value);
            String diamons = UIUtils.getSputils().getString(Constent.DIAMONDS, "0");
            int i = Integer.parseInt(diamons);
            if (entity.isBuy == 0) {
                if (entity.amount <= i) {
                    entity.isBuy = 1;
                    if (entity.amount != -1) {
                        i = i - entity.amount;
                    }
                    UIUtils.getSputils().putString(Constent.DIAMONDS, i + "");
                    UIUtils.SendReRecevice(Constent.SERIES);
                    SavantInfoActivity.this.startActivity(intentRefer);
                } else {
                    TmtUtils.midToast(SavantInfoActivity.this, "对不起您的余额不足请充值!", 0);
                }
            } else {
                SavantInfoActivity.this.startActivity(intentRefer);
            }
        }
    }

    private void processData(final SavantInfoBean data) {
        mInfoData = data;
        SSQSApplication.glide.load(data.avatar).error(R.mipmap.nologinportrait).centerCrop().transform(new GlideCircleTransform(this)).into(mSavantInfphoto);

        mSavantInfoNickname.setText(mInfoData.userName);
        switch (data.level) {
            case 1:
                mSavantInfoNickleve.setText("初级专家");
                break;
            case 2:
                mSavantInfoNickleve.setText("中级专家");
                break;
            case 3:
                mSavantInfoNickleve.setText("高级专家");
                break;
            case 4:
                mSavantInfoNickleve.setText("资深专家");
                break;
            default:
                break;
        }
        int isFouce = mInfoData.isFouce;
        if (isFouce == 0) {
            mSavantInfoFollowCb.setChecked(false);
        } else {
            mSavantInfoFollowCb.setChecked(true);
        }
        String lh = mInfoData.shotCount + "连红";
        mSavantInfoLianhong.setText(lh);
        mFansNum = mInfoData.fanCount;
        mSavantInfoFansNum.setText(String.valueOf(mFansNum));
        String tjNum = mInfoData.recomCount + "";
        mSavantInfoReferNum.setText(tjNum);
        String wzNum = mInfoData.contentCount + "";
        mSavantInfoEssayNum.setText(wzNum);
        //String referCount = "共" + mInfoData.recomCount + "场";
        mSavantInfoMatchAll.setText("查看全部");

        String intro = mInfoData.intro;
        if (intro != null && ("").equals(intro)) {
            mSavantInfoUpDown.setVisibility(View.GONE);
        } else {
            if (intro.length() < 23) {
                mSavantInfoUpDown.setVisibility(View.GONE);
            } else {
                mSavantInfoUpDown.setVisibility(View.GONE);
            }
            mSavantInfoTopText.setText(intro);
        }
        mList = new ArrayList<>();
        List<SavantInfoBean.RatesEntity> rates = mInfoData.rates;
        if (rates != null) {
            mList.add(new SavantInfoStatetrendControllarSingle(this, rates.get(0)).mRootView);
            mList.add(new SavantInfoStatetrendControllarSingle(this, rates.get(1)).mRootView);
            mList.add(new SavantInfoStatetrendControllarSingle(this, rates.get(2)).mRootView);
            mList.add(new SavantInfoStatetrendControllarSingle(this, rates.get(3)).mRootView);
            mList.add(new SavantInfoStatetrendControllarSingle(this, rates.get(4)).mRootView);
            mList.add(new SavantInfoStatetrendControllarSingle(this, rates.get(5)).mRootView);
        }
        mSavantInfoVp.setAdapter(new SavantInfoVpAdapter(this, mList));
        SavantInfoAdapter adapter = new SavantInfoAdapter(this, mInfoData.recomm);
        mSavantInfoListview.setAdapter(adapter);
        ListScrollUtil.setListViewHeightBasedOnChildren(mSavantInfoListview);
        loadingAnimal.setVisibility(View.GONE);
    }
}

