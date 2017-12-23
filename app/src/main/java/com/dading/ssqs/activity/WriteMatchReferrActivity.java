package com.dading.ssqs.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FouceMatchElement;
import com.dading.ssqs.apis.elements.ReCommUploadElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.JCbean;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.bean.MatchInfoBean;
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
 * 创建时间   2016/8/4 14:02
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class WriteMatchReferrActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "WriteMatchReferrActivity";
    @Bind(R.id.makerefer_type)
    TextView mType;
    @Bind(R.id.makerefer_collect_cb)
    CheckBox mCollectCb;
    @Bind(R.id.makerefer_main_icon)
    ImageView mMainIcon;
    @Bind(R.id.makerefer_main)
    TextView mMain;
    @Bind(R.id.makerefer_main_rank)
    TextView mMainRank;
    @Bind(R.id.makerefer_second_icon)
    ImageView mSecondIcon;
    @Bind(R.id.makerefer_second)
    TextView mSecond;
    @Bind(R.id.makerefer_second_rank)
    TextView mSecondRank;
    @Bind(R.id.makerefer_text)
    EditText mTextReason;
    @Bind(R.id.makerefer_open_time)
    TextView mTextOpenTime;


    @Bind(R.id.write_refer_all_result)
    CheckBox mWriteReferAllResult;
    @Bind(R.id.write_refer_now_lost)
    CheckBox mWriteReferNowLost;
    @Bind(R.id.write_refer_all_sb)
    CheckBox mWriteReferAllSb;
    @Bind(R.id.write_refer_half_result)
    CheckBox mWriteReferHalfResult;
    @Bind(R.id.write_refer_half_lost)
    CheckBox mWriteReferHalfLost;
    @Bind(R.id.write_refer_half_sb)
    CheckBox mWriteReferHalfSb;


    @Bind(R.id.write_refer_main_win_text1)
    TextView mWriteReferMainWinText1;
    @Bind(R.id.write_refer_main_win_text2)
    TextView mWriteReferMainWinText2;
    @Bind(R.id.write_refer_main_win)
    LinearLayout mWriteReferMainWin;

    @Bind(R.id.write_refer_draw_text1)
    TextView mWriteReferDrawText1;
    @Bind(R.id.write_refer_draw_text2)
    TextView mWriteReferDrawText2;
    @Bind(R.id.write_refer_draw)
    LinearLayout mWriteReferDraw;

    @Bind(R.id.write_refer_second_win_text1)
    TextView mWriteReferSecondWinText1;
    @Bind(R.id.write_refer_second_win_text2)
    TextView mWriteReferSecondWinText2;
    @Bind(R.id.write_refer_second_win)
    LinearLayout mWriteReferSecondWin;
    @Bind(R.id.frist_jp)
    EditText jp;

    @Bind(R.id.makerefer_price)
    EditText mWriteReferPrice;

    private String mMatchId;
    private MatchInfoBean mData;
    private int mIsFouce;
    private ArrayList<CheckBox> mListcb;
    private String mChecked;
    private boolean mPlState;
    private List<JCbean> mDataPl;
    private int mSelect;
    private LoadingBean mBean;
    private String mMsg;
    private boolean mIsSet = false;

    @Override
    protected void initView() {
        mListcb = new ArrayList<>();
        mListcb.add(mWriteReferAllResult);
        mListcb.add(mWriteReferNowLost);
        mListcb.add(mWriteReferAllSb);
        mListcb.add(mWriteReferHalfResult);
        mListcb.add(mWriteReferHalfLost);
        mListcb.add(mWriteReferHalfSb);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.write_match_refer;
    }

    @Override
    protected void initData() {
        mSelect = 0;
        Intent intent = getIntent();
        mMatchId = intent.getStringExtra(Constent.MATCH_ID);
        String s = UIUtils.getSputils().getString(Constent.LOADING_STATE_SP, "");
        jp.setInputType(0);
        if (TextUtils.isEmpty(s)) {
            return;
        } else {
            mBean = JSON.parseObject(s, LoadingBean.class);
        }
        /**
         * 根据id获取比赛信息
         */
        SSQSApplication.apiClient(classGuid).getMathchData(true, mMatchId, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    MatchInfoBean bean = (MatchInfoBean) result.getData();

                    if (bean != null) {
                        processData(bean);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(WriteMatchReferrActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processData(MatchInfoBean bean) {
        mData = bean;
        if (mData != null) {
            switch (mData.isFouce) {
                case 0:
                    mCollectCb.setChecked(false);
                    break;
                case 1:
                    mCollectCb.setChecked(true);
                    break;
                default:
                    break;
            }
            mType.setText(mData.leagueName);

            mMain.setText(mData.home);
            mSecond.setText(mData.away);

            String aRankING = mData.leagueName + "排名[" + mData.aOrder + "]";
            String hRankING = mData.leagueName + "排名[" + mData.hOrder + "]";

            mMainRank.setText(hRankING);
            mSecondRank.setText(aRankING);

            if (mData.openTime != null && mData.openTime.length() >= 16)
                mTextOpenTime.setText(mData.openTime.substring(0, 16));

            LogUtil.util(TAG, "主队标记返回数据是------------------------------:" + mData.aImageUrl);
            SSQSApplication.glide.load(mData.hImageUrl).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(this)).into(mMainIcon);

            SSQSApplication.glide.load(mData.aImageUrl).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(this)).into(mSecondIcon);
        }
        getPlData();
    }

    private void getPlData() {

        SSQSApplication.apiClient(classGuid).getMatchResult(mMatchId, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<JCbean> items = (List<JCbean>) result.getData();

                    if (items != null) {
                        mPlState = true;
                        processPLData(items);
                    }
                } else {
                    mPlState = false;
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(WriteMatchReferrActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processPLData(List<JCbean> bean) {
        mWriteReferAllResult.setChecked(true);
        mWriteReferNowLost.setChecked(false);
        mWriteReferAllSb.setChecked(false);
        mWriteReferHalfResult.setChecked(false);
        mWriteReferHalfLost.setChecked(false);
        mWriteReferHalfSb.setChecked(false);

        mChecked = "全场赛果";
        mDataPl = bean;
        if (mDataPl != null) {
            for (JCbean entity : mDataPl) {
                if (mChecked.equals(entity.payTypeName)) {
                    mIsSet = true;
                    mWriteReferMainWin.setVisibility(View.VISIBLE);
                    mWriteReferSecondWin.setVisibility(View.VISIBLE);
                    mWriteReferDraw.setVisibility(View.VISIBLE);

                    mWriteReferMainWinText1.setText(entity.home);
                    mWriteReferMainWinText2.setText(entity.realRate1);

                    mWriteReferDrawText1.setText("平局");
                    mWriteReferDrawText2.setText(entity.realRate2);

                    mWriteReferSecondWinText1.setText(entity.away);
                    mWriteReferSecondWinText2.setText(entity.realRate3);
                } else {
                    if (!mIsSet) {
                        mWriteReferMainWin.setVisibility(View.GONE);
                        mWriteReferSecondWin.setVisibility(View.GONE);
                        mWriteReferDraw.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    @Override
    protected void initListener() {
        mWriteReferAllResult.setOnClickListener(this);
        mWriteReferNowLost.setOnClickListener(this);
        mWriteReferAllSb.setOnClickListener(this);
        mWriteReferHalfResult.setOnClickListener(this);
        mWriteReferHalfLost.setOnClickListener(this);
        mWriteReferHalfSb.setOnClickListener(this);

        mWriteReferPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mWriteReferPrice.clearFocus();
                }
            }
        });
        mWriteReferPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) WriteMatchReferrActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                if (!imm.isActive()) {
                    mWriteReferPrice.clearFocus();
                }
            }
        });
        mTextReason.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    mTextReason.clearFocus();
                }
            }
        });
        mCollectCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FouceMatchElement element = new FouceMatchElement();
                element.setMatchID(String.valueOf(mMatchId));
                element.setStatus(mIsFouce == 0 ? "1" : "0");

                SSQSApplication.apiClient(classGuid).fouceMatch(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            LoadingBean bean = (LoadingBean) result.getData();

                            if (bean != null) {
                                if (mIsFouce == 0) {
                                    mData.isFouce = 1;
                                } else {
                                    mData.isFouce = 0;
                                }

                                UIUtils.SendReRecevice(Constent.LOADING_FOOTBALL_SCORE);
                            }
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(WriteMatchReferrActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }
                });
            }
        });
    }

    @OnClick({R.id.makerefer_back, R.id.makerefer_upload, R.id.write_refer_second_win, R.id.write_refer_draw, R.id.write_refer_main_win})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.makerefer_back:
                finish();
                break;
            case R.id.makerefer_upload:


                if (0 == mSelect) {
                    TmtUtils.midToast(WriteMatchReferrActivity.this, "请选择赔率!", 0);
                    return;
                }

                String reason = mTextReason.getText().toString();
                if (reason.length() < 2) {
                    TmtUtils.midToast(WriteMatchReferrActivity.this, "推荐理由至少不得少于2字!", 0);
                    return;
                }

                String price = mWriteReferPrice.getText().toString();
                if (TextUtils.isEmpty(price)) {
                    TmtUtils.midToast(WriteMatchReferrActivity.this, "请填入推荐价格!", 0);
                    return;
                } else {
                    /**
                     * １、	初级专家：每单推荐收费0～10钻；
                     ２、	中级专家：每单推荐收费0～30钻；
                     ３、	高级专家：每单推荐收费0～50钻；
                     ４、	资深专家：每单推荐收费0～100钻。
                     */
                    final AlertDialog.Builder builder = new AlertDialog.Builder(WriteMatchReferrActivity.this);
                    builder.setNegativeButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    LogUtil.util(TAG, "我的等级是------------------------------:" + mBean.level);
                    if (!TextUtils.isEmpty(price)) {
                        if (3 >= price.length()) {
                            switch (mBean.eLevel) {
                                case 1:
                                    if (Integer.parseInt(price) > 10) {
                                        mMsg = "初级专家：每单推荐收费0～10钻；请确认!";
                                        builder.setMessage(mMsg);
                                        builder.show();
                                    }
                                    break;
                                case 2:
                                    if (Integer.parseInt(price) > 30) {
                                        mMsg = "中级专家：每单推荐收费0～30钻；；请确认!";
                                        builder.setMessage(mMsg);
                                        builder.show();
                                    }
                                    break;
                                case 3:
                                    if (Integer.parseInt(price) > 50) {
                                        mMsg = "高级专家：每单推荐收费0～50钻；请确认!";
                                        builder.setMessage(mMsg);
                                        builder.show();
                                    }
                                    break;
                                case 4:
                                    if (Integer.parseInt(price) > 100) {
                                        mMsg = "资深专家：每单推荐收费0～100钻; 请确认!";
                                        builder.setMessage(mMsg);
                                        builder.show();
                                    }
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            TmtUtils.midToast(WriteMatchReferrActivity.this, "请查看专家等级,输入正确价格!", 0);
                        }
                    } else {
                        TmtUtils.midToast(WriteMatchReferrActivity.this, "请输入价格!", 0);
                    }
                }

                ReCommUploadElement element = new ReCommUploadElement();
                element.setMatchID(mMatchId);
                element.setSelected(String.valueOf(mSelect));
                element.setReason(reason);
                element.setAmount(price);
                if (mDataPl != null) {
                    for (JCbean e : mDataPl) {
                        if (mChecked.equals(e.payTypeName)) {
                            element.setPayRateID(String.valueOf(e.id));
                        }
                    }
                }

                SSQSApplication.apiClient(classGuid).reCommUpload(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            mTextReason.setText("");
                            mWriteReferPrice.setText("");
                            TmtUtils.midToast(WriteMatchReferrActivity.this, "恭喜您,上传推荐成功!", 0);
                            finish();
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(WriteMatchReferrActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                            }
                        }
                    }
                });
                break;
            case R.id.write_refer_main_win:
                mSelect = 1;
                mWriteReferMainWin.setBackgroundColor(this.getResources().getColor(R.color.orange));
                mWriteReferMainWinText1.setTextColor(Color.WHITE);
                mWriteReferMainWinText2.setTextColor(Color.YELLOW);

                mWriteReferSecondWin.setBackgroundColor(Color.WHITE);
                mWriteReferSecondWinText1.setTextColor(Color.BLACK);
                mWriteReferSecondWinText2.setTextColor(this.getResources().getColor(R.color.orange));

                mWriteReferDraw.setBackgroundColor(Color.WHITE);
                mWriteReferDrawText1.setTextColor(Color.BLACK);
                mWriteReferDrawText2.setTextColor(this.getResources().getColor(R.color.orange));
                break;
            case R.id.write_refer_second_win:
                mSelect = 2;
                mWriteReferSecondWin.setBackgroundColor(this.getResources().getColor(R.color.orange));
                mWriteReferSecondWinText1.setTextColor(Color.WHITE);
                mWriteReferSecondWinText2.setTextColor(Color.YELLOW);

                mWriteReferMainWin.setBackgroundColor(Color.WHITE);
                mWriteReferMainWinText1.setTextColor(Color.BLACK);
                mWriteReferMainWinText2.setTextColor(this.getResources().getColor(R.color.orange));

                mWriteReferDraw.setBackgroundColor(Color.WHITE);
                mWriteReferDrawText1.setTextColor(Color.BLACK);
                mWriteReferDrawText2.setTextColor(this.getResources().getColor(R.color.orange));
                break;
            case R.id.write_refer_draw:
                mSelect = 3;
                mWriteReferDraw.setBackgroundColor(this.getResources().getColor(R.color.orange));
                mWriteReferDrawText1.setTextColor(Color.WHITE);
                mWriteReferDrawText2.setTextColor(Color.YELLOW);

                mWriteReferSecondWin.setBackgroundColor(Color.WHITE);
                mWriteReferSecondWinText1.setTextColor(Color.BLACK);
                mWriteReferSecondWinText2.setTextColor(this.getResources().getColor(R.color.orange));

                mWriteReferMainWin.setBackgroundColor(Color.WHITE);
                mWriteReferMainWinText1.setTextColor(Color.BLACK);
                mWriteReferMainWinText2.setTextColor(this.getResources().getColor(R.color.orange));
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_refer_all_result:
                LogUtil.util(TAG, "返回数据是------------------------------:执行全场赛果");
                mWriteReferAllResult.setChecked(true);
                mWriteReferNowLost.setChecked(false);
                mWriteReferAllSb.setChecked(false);
                mWriteReferHalfResult.setChecked(false);
                mWriteReferHalfLost.setChecked(false);
                mWriteReferHalfSb.setChecked(false);
                mChecked = "全场赛果";
                break;
            case R.id.write_refer_now_lost:
                LogUtil.util(TAG, "返回数据是------------------------------:执行当前让球");
                mWriteReferAllResult.setChecked(false);
                mWriteReferNowLost.setChecked(true);
                mWriteReferAllSb.setChecked(false);
                mWriteReferHalfResult.setChecked(false);
                mWriteReferHalfLost.setChecked(false);
                mWriteReferHalfSb.setChecked(false);
                mChecked = "当前让球";
                break;
            case R.id.write_refer_all_sb:
                LogUtil.util(TAG, "返回数据是------------------------------:执行全场大小");
                mWriteReferAllResult.setChecked(false);
                mWriteReferNowLost.setChecked(false);
                mWriteReferAllSb.setChecked(true);
                mWriteReferHalfResult.setChecked(false);
                mWriteReferHalfLost.setChecked(false);
                mWriteReferHalfSb.setChecked(false);
                mChecked = "全场大小";
                break;
            case R.id.write_refer_half_result:
                LogUtil.util(TAG, "返回数据是------------------------------:执行半场赛果");
                mWriteReferAllResult.setChecked(false);
                mWriteReferNowLost.setChecked(false);
                mWriteReferAllSb.setChecked(false);
                mWriteReferHalfResult.setChecked(true);
                mWriteReferHalfLost.setChecked(false);
                mWriteReferHalfSb.setChecked(false);
                mChecked = "半场赛果";
                break;
            case R.id.write_refer_half_lost:
                LogUtil.util(TAG, "返回数据是------------------------------:执行半场让球");
                mWriteReferAllResult.setChecked(false);
                mWriteReferNowLost.setChecked(false);
                mWriteReferAllSb.setChecked(false);
                mWriteReferHalfResult.setChecked(false);
                mWriteReferHalfLost.setChecked(true);
                mWriteReferHalfSb.setChecked(false);
                mChecked = "半场让球";
                break;
            case R.id.write_refer_half_sb:
                LogUtil.util(TAG, "返回数据是------------------------------:执行半场大小");
                mWriteReferAllResult.setChecked(false);
                mWriteReferNowLost.setChecked(false);
                mWriteReferAllSb.setChecked(false);
                mWriteReferHalfResult.setChecked(false);
                mWriteReferHalfLost.setChecked(false);
                mWriteReferHalfSb.setChecked(true);
                mChecked = "半场大小";
                break;
            default:
                break;
        }
        if (!mPlState) {
            getPlData();
        }
        if (!mPlState) {
            TmtUtils.midToast(WriteMatchReferrActivity.this, "拉取赔率信息失败,请重新选择比赛类型!", 0);
            return;
        }
        mWriteReferMainWin.setVisibility(View.GONE);
        mWriteReferSecondWin.setVisibility(View.GONE);
        mWriteReferDraw.setVisibility(View.GONE);
        mSelect = 0;

        mWriteReferDraw.setBackgroundColor(Color.WHITE);
        mWriteReferDrawText1.setTextColor(Color.BLACK);
        mWriteReferDrawText2.setTextColor(this.getResources().getColor(R.color.orange));

        mWriteReferSecondWin.setBackgroundColor(Color.WHITE);
        mWriteReferSecondWinText1.setTextColor(Color.BLACK);
        mWriteReferSecondWinText2.setTextColor(this.getResources().getColor(R.color.orange));

        mWriteReferMainWin.setBackgroundColor(Color.WHITE);
        mWriteReferMainWinText1.setTextColor(Color.BLACK);
        mWriteReferMainWinText2.setTextColor(this.getResources().getColor(R.color.orange));
        //// TODO: 2016/11/19 初始化主平客状态
        if (mDataPl != null)
            for (JCbean entity : mDataPl) {

                if (mChecked.equals(entity.payTypeName)) {

                    String highBs = "高于" + entity.realRate2;
                    String lowBs = "低于" + entity.realRate2;
                    switch (mChecked) {
                        case "全场赛果"://全场赛果
                            mWriteReferMainWin.setVisibility(View.VISIBLE);
                            mWriteReferMainWinText1.setText(entity.home);
                            mWriteReferMainWinText2.setText(entity.realRate1);

                            mWriteReferDraw.setVisibility(View.VISIBLE);
                            mWriteReferDrawText1.setText("平局");
                            mWriteReferDrawText2.setText(entity.realRate2);

                            mWriteReferSecondWinText1.setText(entity.away);
                            mWriteReferSecondWinText2.setText(entity.realRate3);
                            mWriteReferSecondWin.setVisibility(View.VISIBLE);
                            break;
                        case "当前让球"://当前让球
                            mWriteReferMainWin.setVisibility(View.VISIBLE);
                            if (entity.realRate2.contains("/")) {
                                String[] split = entity.realRate2.split("/");
                                if (!split[0].equals(split[1]) && Math.abs(Double.valueOf(split[0])) == Math.abs(Double.valueOf(split[1]))) {
                                    if (Double.valueOf(split[0]) > 0) {
                                        mWriteReferMainWinText1.setText("+" + split[0]);
                                    } else {
                                        mWriteReferMainWinText1.setText(split[0]);
                                    }
                                    if (Double.valueOf(split[1]) > 0) {
                                        mWriteReferSecondWinText1.setText("+" + split[1]);
                                    } else {
                                        mWriteReferSecondWinText1.setText(split[1]);
                                    }
                                } else {
                                    mWriteReferMainWinText1.setText(entity.realRate2);
                                    mWriteReferSecondWinText1.setText(entity.realRate2);
                                }
                            } else {
                                if (Double.valueOf(entity.realRate2) > 0) {
                                    mWriteReferMainWinText1.setText("+" + entity.realRate2);
                                    mWriteReferSecondWinText1.setText("-" + entity.realRate2);
                                } else {
                                    if (Double.valueOf(entity.realRate2) == 0) {
                                        mWriteReferMainWinText1.setText(entity.realRate2);
                                        mWriteReferSecondWinText1.setText(entity.realRate2);
                                    } else {
                                        mWriteReferMainWinText1.setText(entity.realRate2);
                                        mWriteReferSecondWinText1.setText("+" + Math.abs(Double.valueOf(entity.realRate2)));
                                    }
                                }
                            }

                          /*  String text1 = entity.home + " " + entity.realRate2;
                            mWriteReferMainWinText1.setText(text1);
                            String text = entity.away + " " + entity.realRate2;
                            mWriteReferSecondWinText1.setText(text);*/
                            mWriteReferMainWinText2.setText(entity.realRate1);

                            mWriteReferSecondWinText2.setText(entity.realRate3);
                            mWriteReferSecondWin.setVisibility(View.VISIBLE);

                            mWriteReferDraw.setVisibility(View.GONE);
                            mWriteReferDrawText1.setText("平局");
                            mWriteReferDrawText2.setText(entity.realRate2);
                            break;
                        case "全场大小"://全场大小


                            mWriteReferMainWin.setVisibility(View.VISIBLE);
                            mWriteReferMainWinText1.setText(highBs);
                            mWriteReferMainWinText2.setText(entity.realRate1);

                            mWriteReferSecondWinText1.setText(lowBs);
                            mWriteReferSecondWinText2.setText(entity.realRate3);
                            mWriteReferSecondWin.setVisibility(View.VISIBLE);

                            mWriteReferDraw.setVisibility(View.GONE);
                            mWriteReferDrawText1.setText("平局");
                            mWriteReferDrawText2.setText(entity.realRate2);

                            break;
                        case "半场赛果"://半场赛果
                            mWriteReferMainWin.setVisibility(View.VISIBLE);
                            mWriteReferMainWinText1.setText(entity.home);
                            mWriteReferMainWinText2.setText(entity.realRate1);

                            mWriteReferDraw.setVisibility(View.VISIBLE);
                            mWriteReferDrawText1.setText("平局");
                            mWriteReferDrawText2.setText(entity.realRate2);

                            mWriteReferSecondWin.setVisibility(View.VISIBLE);
                            mWriteReferSecondWinText1.setText(entity.away);
                            mWriteReferSecondWinText2.setText(entity.realRate3);
                            mWriteReferSecondWin.setVisibility(View.VISIBLE);

                            break;
                        case "半场让球"://半场让球
                            mWriteReferMainWin.setVisibility(View.VISIBLE);

                            if (entity.realRate2.contains("/")) {
                                String[] split = entity.realRate2.split("/");
                                if (!split[0].equals(split[1]) && Math.abs(Double.valueOf(split[0])) == Math.abs(Double.valueOf(split[1]))) {
                                    if (Double.valueOf(split[0]) > 0) {
                                        mWriteReferMainWinText1.setText("+" + split[0]);
                                    } else {
                                        mWriteReferMainWinText1.setText(split[0]);
                                    }
                                    if (Double.valueOf(split[1]) > 0) {
                                        mWriteReferSecondWinText1.setText("+" + split[1]);
                                    } else {
                                        mWriteReferSecondWinText1.setText(split[1]);
                                    }
                                } else {
                                    mWriteReferMainWinText1.setText(entity.realRate2);
                                    mWriteReferSecondWinText1.setText(entity.realRate2);
                                }
                            } else {
                                if (Double.valueOf(entity.realRate2) > 0) {
                                    mWriteReferMainWinText1.setText("+" + entity.realRate2);
                                    mWriteReferSecondWinText1.setText("-" + entity.realRate2);
                                } else {
                                    if (Double.valueOf(entity.realRate2) == 0) {
                                        mWriteReferMainWinText1.setText(entity.realRate2);
                                        mWriteReferSecondWinText1.setText(entity.realRate2);
                                    } else {
                                        mWriteReferMainWinText1.setText(entity.realRate2);
                                        mWriteReferSecondWinText1.setText("+" + Math.abs(Double.valueOf(entity.realRate2)));
                                    }
                                }
                            }

                            /*String text3 = entity.home + " " + entity.realRate2;
                            mWriteReferMainWinText1.setText(text3);
                            String text2 = entity.home + " " + entity.realRate2;
                            mWriteReferSecondWinText1.setText(text2);*/
                            mWriteReferMainWinText2.setText(entity.realRate1);

                            mWriteReferSecondWinText2.setText(entity.realRate3);
                            mWriteReferSecondWin.setVisibility(View.VISIBLE);

                            mWriteReferDraw.setVisibility(View.GONE);
                            mWriteReferDrawText1.setText("平局");
                            mWriteReferDrawText2.setText(entity.realRate2);
                            mWriteReferMainWin.setVisibility(View.VISIBLE);

                            break;
                        case "半场大小"://半场大小

                            mWriteReferMainWin.setVisibility(View.VISIBLE);
                            mWriteReferMainWinText1.setText(highBs);
                            mWriteReferMainWinText2.setText(entity.realRate1);

                            mWriteReferSecondWinText1.setText(lowBs);
                            mWriteReferSecondWinText2.setText(entity.realRate3);
                            mWriteReferSecondWin.setVisibility(View.VISIBLE);

                            mWriteReferDraw.setVisibility(View.GONE);
                            mWriteReferDrawText1.setText("平局");
                            mWriteReferDrawText2.setText(entity.realRate2);
                            break;
                        default:
                            break;
                    }
                }
            }
    }
}
