package com.dading.ssqs.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.RecommDetailElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ReferInfoBean;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.GlideCircleTransform;


import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/8 17:23
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SavantLvItemActivity extends BaseActivity {
    private static final String TAG = "SavantLvItemActivity";

    @Bind(R.id.savant_refer_savantphoto)
    ImageView mSavantReferSavantphoto;
    @Bind(R.id.savant_refer_nickname)
    TextView mSavantReferNickname;
    @Bind(R.id.savant_refer_nickleve)
    TextView mSavantReferNickleve;
    /*  @Bind(R.id.savant_refer_invited)
      ImageView mSavantReferInvited;
      @Bind(R.id.savant_refer_savant)
      ImageView mSavantReferSavant;*/
    @Bind(R.id.savant_refer_follow_cb)
    CheckBox mSavantReferFollowCb;
    /*@Bind(R.id.savant_refer_doyen)
    ImageView mSavantReferDoyen;*/
    @Bind(R.id.savant_refer_score_icon)
    TextView mSavantReferScoreIcon;
    @Bind(R.id.savant_refer_main_icon)
    ImageView mSavantReferMainIcon;
    @Bind(R.id.savant_refer_main)
    TextView mSavantReferMain;
    @Bind(R.id.savant_refer_main_ranking)
    TextView mSavantReferMainRanking;
    @Bind(R.id.savant_refer_mid_type)
    TextView mSavantReferMidType;
    @Bind(R.id.savant_refer_mid_score)
    TextView mSavantReferMidScore;
    @Bind(R.id.savant_refer_second_icon)
    ImageView mSavantReferSecondIcon;
    @Bind(R.id.savant_refer_second)
    TextView mSavantReferSecond;
    @Bind(R.id.savant_refer_second_ranking)
    TextView mSavantReferSecondRanking;
    @Bind(R.id.savant_refer_type_title)
    TextView mSavantReferTypeTitle;
    @Bind(R.id.savant_refer_publish)
    TextView mSavantReferPublish;
    /*  @Bind(R.id.savant_refer_type)
      TextView  mSavantReferType;*/
    @Bind(R.id.savant_refer_left_btn)
    TextView mSavantReferLeftBtn;
    @Bind(R.id.savant_refer_mid_btn)
    TextView mSavantReferMidBtn;
    @Bind(R.id.savant_refer_right_btn)
    TextView mSavantReferRightBtn;
    @Bind(R.id.savant_refer_intro)
    TextView mSavantReferIntro;
    @Bind(R.id.savant_refer_good)
    TextView mSavantReferGood;
    @Bind(R.id.savant_refer_bad)
    TextView mSavantReferBad;


    private ReferInfoBean mData;
    private boolean mFollowFlag;
    private String mMatchID;


    private void processData(final ReferInfoBean data) {
        Glide.with(UIUtils.getContext())
                .load(data.avatar)
                .error(R.mipmap.nologinportrait)
                .centerCrop()
                .transform(new GlideCircleTransform(this))
                .into(mSavantReferSavantphoto);

        mSavantReferNickname.setText(data.userName);

        switch (data.level) {
            case 1:
                mSavantReferNickleve.setText("初级专家");
                break;
            case 2:
                mSavantReferNickleve.setText("中级专家");
                break;
            case 3:
                mSavantReferNickleve.setText("高级专家");
                break;
            default:
                break;
        }
        String text = data.rRed + "连红";
        mSavantReferScoreIcon.setText(text);
        if (data.isFouce == 0) {
            mSavantReferFollowCb.setChecked(false);
        } else {
            mSavantReferFollowCb.setChecked(true);
        }
        mSavantReferMain.setText(data.home);
        mSavantReferSecond.setText(data.away);

        Glide.with(UIUtils.getContext())
                .load(data.aImageUrl)
                .error(R.mipmap.fail)
                .centerCrop()
                .into(mSavantReferSecondIcon);


        Glide.with(UIUtils.getContext())
                .load(data.hImageUrl)
                .error(R.mipmap.fail)
                .centerCrop()
                .into(mSavantReferMainIcon);

        String hRanking = data.leagueName + "[" + data.hOrder + "]";
        mSavantReferMainRanking.setText(hRanking);
        String aRanking = data.leagueName + "[" + data.aOrder + "]";
        mSavantReferSecondRanking.setText(aRanking);

        String leagueName = data.leagueName;
        mSavantReferMidType.setText(leagueName);
        String midScore = "(" + data.hScore + ":" + data.aScore + ")";
        mSavantReferMidScore.setVisibility(View.VISIBLE);
        mSavantReferMidScore.setText(midScore);
        if ("(:)".equals(midScore)) {
            mSavantReferMidScore.setVisibility(View.GONE);
        }
        mSavantReferTypeTitle.setText(data.payRateName);
        String date = data.createDate;
        if (date != null && !TextUtils.isEmpty(date)) {
            String diffCurTime = DateUtils.diffCurTime(date, DateUtils.getCurTime("yyyy-MM-dd HH:mm:ss"));
            if (diffCurTime.isEmpty()) {
                String t1 = "刚刚发布";
                mSavantReferPublish.setText(t1);
            }
            String t2 = diffCurTime + "前发布";
            mSavantReferPublish.setText(t2);
        }
        //mSavantReferType.setText(data.companyName);


        switch (data.payRateName) {
            case "全场赛果":
            case "半场赛果":
                String text1 = "主 " + data.realRate1;
                mSavantReferLeftBtn.setText(text1);
                String text2 = "平 " + data.realRate2;
                mSavantReferMidBtn.setText(text2);
                String text3 = "客 " + data.realRate3;
                mSavantReferRightBtn.setText(text3);
                mSavantReferLeftBtn.setVisibility(View.VISIBLE);
                mSavantReferMidBtn.setVisibility(View.VISIBLE);
                mSavantReferRightBtn.setVisibility(View.VISIBLE);

                switch (data.selected) {
                    case 1:
                        mSavantReferLeftBtn.setTextColor(Color.RED);
                        mSavantReferMidBtn.setTextColor(Color.BLACK);
                        mSavantReferRightBtn.setTextColor(Color.BLACK);
                        break;
                    case 2:
                        mSavantReferLeftBtn.setTextColor(Color.BLACK);
                        mSavantReferMidBtn.setTextColor(Color.RED);
                        mSavantReferRightBtn.setTextColor(Color.BLACK);
                        break;
                    case 3:
                        mSavantReferLeftBtn.setTextColor(Color.BLACK);
                        mSavantReferMidBtn.setTextColor(Color.BLACK);
                        mSavantReferRightBtn.setTextColor(Color.RED);
                        break;

                    default:
                        break;
                }

                break;
            case "当前让球":
            case "半场让球":
                mSavantReferLeftBtn.setVisibility(View.VISIBLE);
                mSavantReferMidBtn.setVisibility(View.GONE);
                mSavantReferRightBtn.setVisibility(View.VISIBLE);
                if (data.realRate2.contains("/")) {
                    String[] split = data.realRate2.split("/");
                    if (!split[0].equals(split[1]) && Math.abs(Double.valueOf(split[0])) == Math.abs(Double.valueOf(split[1]))) {
                        if (Double.valueOf(split[0]) > 0) {
                            mSavantReferLeftBtn.setText(data.home + " " + "+" + split[0] + "  " + data.realRate1);
                        } else {
                            mSavantReferLeftBtn.setText(data.home + " " + split[0] + "  " + data.realRate1);
                        }
                        if (Double.valueOf(split[1]) > 0) {
                            mSavantReferRightBtn.setText(data.away + " " + "+" + split[1] + "  " + data.realRate1);
                        } else {
                            mSavantReferRightBtn.setText(data.away + " " + split[1] + "  " + data.realRate3);
                        }
                    } else {
                        mSavantReferLeftBtn.setText(data.home + " " + data.realRate2 + "  " + data.realRate1);
                        mSavantReferRightBtn.setText(data.away + " " + data.realRate2 + "  " + data.realRate3);
                    }
                } else {
                    if (Double.valueOf(data.realRate2) > 0) {
                        mSavantReferLeftBtn.setText(data.home + "+" + data.realRate2 + "  " + data.realRate1);
                        mSavantReferRightBtn.setText(data.away + "-" + data.realRate2 + "  " + data.realRate3);
                    } else {
                        if (Double.valueOf(data.realRate2) == 0) {
                            mSavantReferLeftBtn.setText(data.home + " " + Math.abs(Double.valueOf(data.realRate2)) + "  " + data.realRate1);
                            mSavantReferRightBtn.setText(data.away + " " + Math.abs(Double.valueOf(data.realRate2)) + "  " + data.realRate3);
                        } else {
                            mSavantReferLeftBtn.setText(data.home + " " + data.realRate2 + "  " + data.realRate1);
                            mSavantReferRightBtn.setText(data.away + " " + Math.abs(Double.valueOf(data.realRate2)) + "  " + data.realRate3);
                        }
                    }
                }

                switch (data.selected) {
                    case 1:
                        mSavantReferLeftBtn.setTextColor(Color.RED);
                        mSavantReferMidBtn.setTextColor(Color.BLACK);
                        mSavantReferRightBtn.setTextColor(Color.BLACK);
                        break;
                    case 2:
                        mSavantReferLeftBtn.setTextColor(Color.BLACK);
                        mSavantReferMidBtn.setTextColor(Color.BLACK);
                        mSavantReferRightBtn.setTextColor(Color.RED);
                        break;
                    default:
                        break;
                }
                break;
            case "全场大小":
            case "半场大小":
                String text6 = "高于 " + data.realRate2 + " " + data.realRate1;
                mSavantReferLeftBtn.setText(
                        text6);
                String text7 = "低于 " + data.realRate2 + " " + data.realRate3;
                mSavantReferRightBtn.setText(text7);
                mSavantReferLeftBtn.setVisibility(View.VISIBLE);
                mSavantReferMidBtn.setVisibility(View.GONE);
                mSavantReferRightBtn.setVisibility(View.VISIBLE);
                switch (data.selected) {
                    case 1:
                        mSavantReferLeftBtn.setTextColor(Color.RED);
                        mSavantReferMidBtn.setTextColor(Color.BLACK);
                        mSavantReferRightBtn.setTextColor(Color.BLACK);
                        break;
                    case 2:
                        mSavantReferLeftBtn.setTextColor(Color.BLACK);
                        mSavantReferMidBtn.setTextColor(Color.BLACK);
                        mSavantReferRightBtn.setTextColor(Color.RED);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        mSavantReferIntro.setText(data.reason);

        String good = data.suppCount + "";
        mSavantReferGood.setText(good);
        String bad = data.hateCount + "";
        mSavantReferBad.setText(bad);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.activity_savant_info_refer_context;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mMatchID = intent.getStringExtra(Constent.MATCH_ID);
        /**
         /v1.0/recomm/id/{id}
         b)	请求方式:
         get
         c)	请求参数说明：
         id：推荐比赛ID
         auto_token:用户token
         d)	返回格式
         */

        SSQSApplication.apiClient(classGuid).getRecommentMatchDetails(mMatchID, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    ReferInfoBean bean = (ReferInfoBean) result.getData();

                    if (bean != null) {
                        mData = bean;
                        processData(mData);
                        mSavantReferBad.setClickable(true);
                        mSavantReferGood.setClickable(true);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(SavantLvItemActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                        mSavantReferBad.setClickable(false);
                        mSavantReferGood.setClickable(false);
                    }
                }
            }
        });
    }

    @Override
    protected void initListener() {
        mSavantReferGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    Intent intent = new Intent(SavantLvItemActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                if (mData != null && mData.isClick == 0) {
                    /**
                     * 6.支持或反对推荐比赛
                     a)	请求地址：
                     /v1.0/recommDetail
                     b)	请求方式:
                     post
                     c)	请求参数说明：
                     recommMatchID:推荐比赛ID
                     status:状态，0：反对1：支持
                     */
                    RecommDetailElement element = new RecommDetailElement();
                    element.setRecommMatchID(mMatchID);
                    element.setStatus("1");

                    SSQSApplication.apiClient(classGuid).recommDetails(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                ++mData.suppCount;
                                String s = mData.suppCount + "";
                                mSavantReferGood.setText(s);
                                mData.isClick = 1;
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(SavantLvItemActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    TmtUtils.midToast(SavantLvItemActivity.this, "点赞失败,请重新点击!", 0);
                                }
                            }
                        }
                    });
                } else {
                    TmtUtils.midToast(SavantLvItemActivity.this, "您已点赞或反对,请勿重复点击。", 0);
                }
            }
        });
        mSavantReferBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    Intent intent = new Intent(SavantLvItemActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                if (mData != null && mData.isClick == 0) {
                    /**
                     * 6.支持或反对推荐比赛
                     a)	请求地址：
                     /v1.0/recommDetail
                     b)	请求方式:
                     post
                     c)	请求参数说明：
                     recommMatchID:推荐比赛ID
                     status:状态，0：反对1：支持
                     */
                    RecommDetailElement element = new RecommDetailElement();
                    element.setRecommMatchID(mMatchID);
                    element.setStatus("0");

                    SSQSApplication.apiClient(classGuid).recommDetails(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                ++mData.hateCount;
                                String s = mData.hateCount + "";
                                mSavantReferBad.setText(s);
                                mData.isClick = 1;
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(SavantLvItemActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                }
                            }
                        }
                    });
                } else {
                    TmtUtils.midToast(SavantLvItemActivity.this, "您已点赞或反对,请勿重复点击。", 0);
                }
            }
        });
        mSavantReferFollowCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    /**
                     * a)	请求地址：
                     /v1.0/expert/detail/android/userID/{userID}
                     b)	请求方式:
                     get
                     c)	请求参数说明：
                     userID:用户ID
                     auth_token：登陆后加入请求头
                     */
                    LogUtil.util(TAG, "我被选中了");

                    SSQSApplication.apiClient(classGuid).getExpertDetails(mMatchID, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {

                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(SavantLvItemActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    TmtUtils.midToast(SavantLvItemActivity.this, "服务器开小差了,请重新关注.", 0);
                                    mSavantReferFollowCb.setChecked(false);
                                }
                            }
                        }
                    });
                    mFollowFlag = true;
                } else {
                    LogUtil.util(TAG, "我No选中了");
                    mFollowFlag = false;
                }
            }
        });
    }

    @OnClick({R.id.savant_refer_context_back})
    public void OnClik(View v) {
        finish();
    }
}
