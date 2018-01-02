package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FouceMatchBallElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/1 14:45
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public abstract class ScoreMatchAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "ScoreMatchAdapter";
    private final int type;
    private final HashMap<Integer, RelativeLayout> mMapLy;
    public List<ScoreBean> mData;
    public Context content;
    public ViewHolder holder;
    private String mText1;
    private String mText2;
    private String mText3;
    private String mText4;
    private String mText5;
    private boolean mIsLoding;
    private ScoreBean mEntity;
    private final HashMap<Integer, CheckBox> mMap;
    private int mPostion;
    private AlphaAnimation mAlphaAnim;
    private HashMap<Integer, ImageView> mMapRed;

    public ScoreMatchAdapter(Context content, List<ScoreBean> sgData, int type) {
        this.content = content;
        this.mData = sgData;
        this.type = type;
        mMap = new HashMap<>();
        mMapRed = new HashMap<>();
        mMapLy = new HashMap<>();
    }

    public ScoreMatchAdapter(Context content, int type) {
        this.content = content;
        this.type = type;
        mMap = new HashMap<>();
        mMapRed = new HashMap<>();
        mMapLy = new HashMap<>();
    }

    public void setData(List<ScoreBean> list) {
        if (list != null) {
            if (mData != null) {
                mData.clear();
                mData.addAll(list);
            } else {
                this.mData = list;
            }
            notifyDataSetChanged();
        }
    }

    public void addData(List<ScoreBean> list) {
        if (list != null && mData != null) {
            this.mData.addAll(list);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(content, R.layout.score_lv_item, null);
            holder = new ViewHolder();
            holder.scoreSgItem = (LinearLayout) convertView.findViewById(R.id.score_sg_item);

            //比赛星期,图标
            holder.scoreSgWeekStarCbLy = (RelativeLayout) convertView.findViewById(R.id.score_sg_week_star_cb_ly);
            holder.scoreSgWeekStarCb = (CheckBox) convertView.findViewById(R.id.score_sg_week_star_cb);
            holder.scoreSgTvWeek = (TextView) convertView.findViewById(R.id.score_sg_tv_week);
            //类型,开始时间
            holder.scoreSgMatchType = (TextView) convertView.findViewById(R.id.score_sg_match_type);
            holder.scoreSgMatchStart = (TextView) convertView.findViewById(R.id.score_sg_match_start);

            //红黄牌主队名
            holder.scoreSgMainName = (TextView) convertView.findViewById(R.id.score_sg_team_name_main);
            holder.scoreSgMainRanking = (TextView) convertView.findViewById(R.id.score_sg_team_ranking_main);
            holder.scoreSgMainRed = (TextView) convertView.findViewById(R.id.score_sg_team_red_main);
            holder.scoreSgMainYellow = (TextView) convertView.findViewById(R.id.score_sg_team_yellow_main);
            //亚盘
            holder.scoreSgYpStart = (TextView) convertView.findViewById(R.id.score_sg_yp_start);
            holder.scoreSgYpGroup = (TextView) convertView.findViewById(R.id.score_sg_yp_grow);
            holder.scoreSgYpBehind = (TextView) convertView.findViewById(R.id.score_sg_yp_behind);

            //上半场评分及目前,完场
            holder.middle = (TextView) convertView.findViewById(R.id.middle);
            holder.left = (TextView) convertView.findViewById(R.id.left);
            holder.right = (TextView) convertView.findViewById(R.id.right);
            holder.scoreSgMatchResultTime = (TextView) convertView.findViewById(R.id.time_result);
            holder.scoreSgMatchRedTwinkle = (ImageView) convertView.findViewById(R.id.red_twinkle);
            holder.scoreSgMatchRedTwinkleIvLy = (LinearLayout) convertView.findViewById(R.id.red_twinkle_zbo_cqiu);
            holder.scoreSgMatchHalfScoreMain = (TextView) convertView.findViewById(R.id.score_sg_half_score_main);
            holder.scoreSgMatchHalfScoreSecond = (TextView) convertView.findViewById(R.id.score_sg_half_score_second);
            holder.scoreSgMatchResultScoreMain = (TextView) convertView.findViewById(R.id.score_sg_result_score_main);
            holder.scoreSgMatchResultScoreSecond = (TextView) convertView.findViewById(R.id.score_sg_result_score_second);

            //客队信息
            holder.scoreSgVistorRanking = (TextView) convertView.findViewById(R.id.score_sg_team_ranking_visitor);
            holder.scoreSgVisitorRed = (TextView) convertView.findViewById(R.id.score_sg_team_red_visitor);
            holder.scoreSgVisitorYellow = (TextView) convertView.findViewById(R.id.score_sg_team_yellow_visitor);
            holder.scoreSgVistorName = (TextView) convertView.findViewById(R.id.score_sg_team_name_visitor);
            //皇冠赔率
            holder.scoreSgHgStart = (TextView) convertView.findViewById(R.id.score_sg_hg_start);
            holder.scoreSgHgGroup = (TextView) convertView.findViewById(R.id.score_sg_hg_grow);
            holder.scoreSgHgBehind = (TextView) convertView.findViewById(R.id.score_sg_hg_behind);

            holder.scoreSgPeople = (TextView) convertView.findViewById(R.id.score_sg_people);

            holder.scoreYpLy = (RelativeLayout) convertView.findViewById(R.id.score_yp_ly);
            holder.scoreHgLy = (RelativeLayout) convertView.findViewById(R.id.score_hg_ly);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        mEntity = mData.get(position);
        mMap.put(position, holder.scoreSgWeekStarCb);
        mMapRed.put(position, holder.scoreSgMatchRedTwinkle);
        mMapLy.put(position, holder.scoreSgWeekStarCbLy);

        holder.scoreSgTvWeek.setText("星期一");
        switch (position % 3) {
            case 0:
                holder.scoreSgMatchType.setTextColor(ContextCompat.getColor(content, R.color.orange_tv));
                break;
            case 1:
                holder.scoreSgMatchType.setTextColor(ContextCompat.getColor(content, R.color.green_tv));
                break;
            case 2:
                holder.scoreSgMatchType.setTextColor(ContextCompat.getColor(content, R.color.blue_tv));
                break;
            default:
                break;
        }
        holder.scoreSgMatchType.setText(mEntity.leagueName);

        String openTime = mEntity.openTime;
        final String dateTime = openTime.substring(11, 16);
        holder.scoreSgMatchStart.setText(dateTime);
        holder.scoreSgMainName.setText(mEntity.home);
        //判断是否有红黄牌记得隐藏
        holder.scoreSgMainRed.setVisibility(View.GONE);
        holder.scoreSgMainYellow.setVisibility(View.GONE);

        mText2 = "0.95";

        holder.scoreSgYpStart.setText(mText2);
        mText1 = "0.05";
        holder.scoreSgYpGroup.setText(mText1);
        holder.scoreSgYpBehind.setText("1.0");

        //收藏比赛
        mIsLoding = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
        //Logger.d(TAG, "登录状态-----------------------------:" + mIsLoding + "-----" + mEntity.isFouce);
        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            mMap.get(position).setClickable(true);
            mMapLy.get(position).setClickable(true);
            if (mEntity.isFouce == 0) {
                holder.scoreSgWeekStarCb.setChecked(false);
            } else if (mEntity.isFouce == 1) {
                holder.scoreSgWeekStarCb.setChecked(true);
            }
        } else {
            holder.scoreSgWeekStarCb.setChecked(false);
        }

        //比分
        holder.scoreSgMatchHalfScoreMain.setText(mEntity.hHalfScore);
        holder.scoreSgMatchHalfScoreSecond.setText(mEntity.aHalfScore);
        holder.scoreSgMatchResultScoreMain.setText(mEntity.hScore);
        holder.scoreSgMatchResultScoreSecond.setText(mEntity.aScore);
        //隐藏红点
        holder.scoreSgMatchRedTwinkle.setVisibility(View.GONE);
        //客队信息
        if (!TextUtils.isEmpty(mEntity.hOrder) && !"0".equals(mEntity.hOrder)) {
            String hOrder = "[" + mEntity.hOrder + "]";
            holder.scoreSgMainRanking.setText(hOrder);
        }
        if (!TextUtils.isEmpty(mEntity.aOrder) && !"0".equals(mEntity.aOrder)) {
            String text = "[" + mEntity.aOrder + "]";
            holder.scoreSgVistorRanking.setText(text);
        }
        //判断是否有红黄牌记得隐藏
        holder.scoreSgVisitorRed.setVisibility(View.GONE);
        holder.scoreSgVisitorYellow.setVisibility(View.GONE);
        holder.scoreSgVistorName.setText(mEntity.away);

        mText3 = "1.95";
        holder.scoreSgHgStart.setText(mText3);
        mText4 = "0.65";
        holder.scoreSgHgGroup.setText(mText4);
        holder.scoreSgHgBehind.setText("2.0");

        mText5 = mEntity.joinCount + "";
        holder.scoreSgPeople.setText(mText5);
        holder.scoreSgMatchRedTwinkle.setVisibility(View.GONE);

        Date date = new Date();
        holder.scoreSgMatchResultTime.setVisibility(View.VISIBLE);
        mEntity = mData.get(position);
        mAlphaAnim = new AlphaAnimation(0.0f, 1.0f);
        mAlphaAnim.setDuration(500);
        mAlphaAnim.setRepeatCount(Animation.INFINITE);
        mMapRed.get(position).setAnimation(mAlphaAnim);
        long openTimeL = DateUtils.formatDate(mEntity.openTime).getTime();

        long curTimeL = 0;
        if (!TextUtils.isEmpty(mEntity.systemTime)) {
            curTimeL = DateUtils.formatDate(mEntity.systemTime).getTime();
        }

        int m = (int) (((curTimeL - openTimeL) / 1000) / 60);
        mEntity.playTime = m;

        switch (mEntity.isOver) {
            case 0:
                //根据protime时间长
                if (!TextUtils.isEmpty(mEntity.protime)) {//如果protime为空不处理
                    if (!"半场".equals(mEntity.protime)) {
                        mEntity.isVisibleTwilke = true;
                        holder.scoreSgMatchResultTime.setText(mEntity.protime);
                    } else {
                        mEntity.isVisibleTwilke = false;
                        holder.scoreSgMatchResultTime.setText(String.valueOf(mEntity.protime));
                        mAlphaAnim.cancel();
                    }
                } else {
                    holder.scoreSgMatchResultTime.setText("赛前");
                    mEntity.isVisibleTwilke = false;
                    holder.scoreSgMatchHalfScoreMain.setVisibility(View.GONE);
                    holder.scoreSgMatchHalfScoreSecond.setVisibility(View.GONE);
                    holder.middle.setVisibility(View.GONE);
                    holder.left.setVisibility(View.GONE);
                    holder.right.setVisibility(View.GONE);
                    mAlphaAnim.cancel();
                }
                break;
            case 1:
                mEntity.isVisibleTwilke = false;
                holder.scoreSgMatchResultTime.setText("完场");
                holder.scoreSgMatchHalfScoreMain.setVisibility(View.VISIBLE);
                holder.scoreSgMatchHalfScoreSecond.setVisibility(View.VISIBLE);
                holder.middle.setVisibility(View.VISIBLE);
                holder.left.setVisibility(View.VISIBLE);
                holder.right.setVisibility(View.VISIBLE);
                mAlphaAnim.cancel();
                break;
            case 2:
                mEntity.isVisibleTwilke = false;
                holder.scoreSgMatchResultTime.setText("中断");
                holder.scoreSgMatchHalfScoreMain.setVisibility(View.GONE);
                holder.scoreSgMatchHalfScoreSecond.setVisibility(View.GONE);
                holder.middle.setVisibility(View.GONE);
                holder.left.setVisibility(View.GONE);
                holder.right.setVisibility(View.GONE);
                mAlphaAnim.cancel();
                break;
            case 3:
                mEntity.isVisibleTwilke = false;
                holder.scoreSgMatchResultTime.setText("半场");
                if (!TextUtils.isEmpty(mEntity.protime)) {//如果protime为空不处理
                    if (!"半场".equals(mEntity.protime)) {
                        holder.scoreSgMatchResultTime.setText(mEntity.protime);
                        mEntity.isVisibleTwilke = true;
                    }
                    mAlphaAnim.start();
                }
                holder.scoreSgMatchHalfScoreMain.setVisibility(View.VISIBLE);
                holder.scoreSgMatchHalfScoreSecond.setVisibility(View.VISIBLE);
                holder.middle.setVisibility(View.VISIBLE);
                holder.left.setVisibility(View.VISIBLE);
                holder.right.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        if (mEntity.isVisibleTwilke) {
            mMapRed.get(position).setVisibility(View.VISIBLE);
            holder.scoreSgMatchRedTwinkleIvLy.setVisibility(View.VISIBLE);
        } else {
            mMapRed.get(position).setVisibility(View.GONE);
            holder.scoreSgMatchRedTwinkleIvLy.setVisibility(View.GONE);
        }

        holder.scoreSgWeekStarCbLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.get(position).setClickable(false);
                mMapLy.get(position).setClickable(false);
                mIsLoding = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);

                //okHttp post同步请求表单提交

                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    int fouce = mData.get(position).isFouce;
                    if (fouce == 1) {
                        removeGz(position);
                    } else {
                        addGz(position);
                    }
                } else {
                    holder.scoreSgWeekStarCb.setClickable(true);
                    holder.scoreSgWeekStarCbLy.setClickable(true);
                    mData.get(position).isFouce = 0;
                    holder.scoreSgWeekStarCb.setChecked(false);
                    Intent intent = new Intent(content, LoginActivity.class);
                    content.startActivity(intent);
                }
            }
        });
        holder.scoreSgWeekStarCb.setOnClickListener(new MyAdapterListener(position) {
            @Override
            public void onClick(View v) {
                mMap.get(position).setClickable(true);
                mMapLy.get(position).setClickable(true);
                mPostion = position;
                mIsLoding = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
               /*
                 * okHttp post同步请求表单提交
                 */
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    int fouce = mData.get(position).isFouce;
                    Logger.d(TAG, "添加关注时是否已经关注--------:" + fouce + "---" + mData.get(position).toString());
                    if (fouce == 1) {
                        removeGz(position);

                    } else {
                        addGz(position);
                    }
                } else {
                    mData.get(position).isFouce = 0;
                    holder.scoreSgWeekStarCb.setChecked(false);
                    mMap.get(position).setClickable(true);
                    mMapLy.get(position).setClickable(true);
                    Intent intent = new Intent(content, LoginActivity.class);
                    content.startActivity(intent);
                }
            }
        });

        holder.scoreSgItem.setOnClickListener(new MyAdapterListener(position) {
            @Override
            public void onClick(View v) {
                Intent intent = getIntentS(mData.get(position).id);
                content.startActivity(intent);
            }
        });
        // setVisiblesOrData(position);

        return convertView;
    }

    public abstract Intent getIntentS(int id);

    private void addGz(final int position) {
        mMap.get(position).setChecked(true);
        // 添加进收藏
        FouceMatchBallElement element = new FouceMatchBallElement();
        element.setMatchID(String.valueOf(mData.get(position).id));
        element.setStatus("1");

        SSQSApplication.apiClient(0).fouceMatchBall(element, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    LoadingBean bean = (LoadingBean) result.getData();

                    if (bean != null) {
                        mData.get(position).isFouce = 1;
                        //ToastUtils.midToast(content, "添加关注成功", 0);
                        mMap.get(position).setChecked(true);
                        mMap.get(position).setClickable(true);
                        mMapLy.get(position).setClickable(true);
                        if (type == 3) {
                            UIUtils.SendReRecevice(Constent.LOADING_FOOTBALL_SCORE);
                            UIUtils.SendReRecevice(Constent.GQ_RECEVICE);
                        } else {
                            switch (type) {
                                case 0:
                                    UIUtils.SendReRecevice(Constent.JS_RECEVICE_CB);
                                    break;
                                case 1:
                                    UIUtils.SendReRecevice(Constent.SG_RECEVICE_CB);
                                    break;
                                case 2:
                                    UIUtils.SendReRecevice(Constent.SC_RECEVICE_CB);
                                    break;
                                case 4:
                                    UIUtils.SendReRecevice(Constent.GQ_RECEVICE);
                                    break;
                                default:
                                    break;
                            }

                            UIUtils.SendReRecevice(Constent.GZ_RECEVICE);
                        }
                    }
                } else {
                    if ("该比赛已经关注，请勿重复关注！".equals(result.getMessage())) {
                        Intent intent = new Intent();
                        switch (type) {
                            case 0:
                                intent.setAction(Constent.JS_RECEVICE_CB);
                                break;
                            case 1:
                                intent.setAction(Constent.SG_RECEVICE_CB);
                                break;
                            case 2:
                                intent.setAction(Constent.SC_RECEVICE_CB);
                                break;
                            case 4:
                                intent.setAction(Constent.GQ_RECEVICE);
                                break;
                            default:
                                break;
                        }
                        UIUtils.getContext().sendBroadcast(intent);
                    }
                    mMap.get(position).setChecked(false);
                    mData.get(position).isFouce = 0;
                    mMap.get(position).setClickable(true);
                    mMapLy.get(position).setClickable(true);
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(content, LoginActivity.class);
                        content.startActivity(intent);
                    } else {
                        //   ToastUtils.midToast(content, "添加关注失败" + bean.msg, 0);
                    }
                }
            }
        });
        Logger.d(TAG, "点击的条目是------------------------------:" + position);
    }

    private void removeGz(final int position) {
        //移除进收藏
        FouceMatchBallElement element = new FouceMatchBallElement();
        element.setMatchID(String.valueOf(mData.get(position).id));
        element.setStatus("0");

        SSQSApplication.apiClient(0).fouceMatchBall(element, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    LoadingBean bean = (LoadingBean) result.getData();

                    if (bean != null) {
                        mData.get(position).isFouce = 0;
                        // Toast.makeText(content, "取消关注成功", Toast.LENGTH_SHORT).show();
                        //ToastUtils.midToast(content, "取消关注成功", 0);
                        mMap.get(position).setChecked(false);
                        mMapLy.get(position).setClickable(true);
                        /**
                         * 通知关注界面
                         */
                        if (type == 3) {
                            UIUtils.SendReRecevice(Constent.LOADING_FOOTBALL_SCORE);
                            UIUtils.SendReRecevice(Constent.GQ_RECEVICE);

                            mData.remove(position);
                            notifyDataSetChanged();
                        } else {
                            switch (type) {
                                case 0:
                                    UIUtils.SendReRecevice(Constent.JS_RECEVICE_CB);
                                    break;
                                case 1:
                                    UIUtils.SendReRecevice(Constent.SG_RECEVICE_CB);
                                    break;
                                case 2:
                                    UIUtils.SendReRecevice(Constent.SC_RECEVICE_CB);
                                    break;
                                case 4:
                                    UIUtils.SendReRecevice(Constent.GQ_RECEVICE);
                                    break;
                                default:
                                    break;
                            }
                            UIUtils.SendReRecevice(Constent.GZ_RECEVICE);
                        }

                    }
                } else {
                    if ("该比赛已经关注，请勿重复关注！".equals(result.getMessage())) {
                        Intent intent = new Intent();
                        switch (type) {
                            case 0:
                                intent.setAction(Constent.JS_RECEVICE_CB);
                                break;
                            case 1:
                                intent.setAction(Constent.SG_RECEVICE_CB);
                                break;
                            case 2:
                                intent.setAction(Constent.SC_RECEVICE_CB);
                                break;
                            case 4:
                                intent.setAction(Constent.GQ_RECEVICE);
                                break;
                            default:
                                break;
                        }
                        UIUtils.getContext().sendBroadcast(intent);
                    }
                    mData.get(position).isFouce = 1;
                    mMap.get(position).setChecked(true);
                    mMap.get(position).setClickable(true);
                    mMapLy.get(position).setClickable(true);
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(content, LoginActivity.class);
                        content.startActivity(intent);
                    } else {
                        // ToastUtils.midToast(content, "取消关注失败" + bean.msg, 0);
                    }
                }
            }
        });
    }

    public int getScorePostion() {
        return mPostion;
    }

    public static class ViewHolder {
        public TextView scoreSgTvWeek;
        public TextView scoreSgMatchType;
        public TextView scoreSgMatchStart;
        public TextView scoreSgMainName;
        public TextView scoreSgMainRanking;
        public TextView scoreSgMainRed;
        public TextView scoreSgMainYellow;
        public TextView scoreSgYpStart;
        public TextView scoreSgYpGroup;
        public TextView scoreSgYpBehind;
        public TextView scoreSgMatchHalfScoreMain;
        public TextView scoreSgMatchHalfScoreSecond;
        public TextView scoreSgMatchResultTime;
        public TextView scoreSgVisitorRed;
        public TextView scoreSgVisitorYellow;
        public TextView scoreSgVistorName;
        public TextView scoreSgVistorRanking;
        public TextView scoreSgHgStart;
        public TextView scoreSgHgGroup;
        public TextView scoreSgHgBehind;
        public TextView scoreSgPeople;
        public ImageView scoreSgMatchRedTwinkle;
        public RelativeLayout scoreYpLy;
        public RelativeLayout scoreHgLy;
        public TextView scoreSgMatchResultScoreMain;
        public TextView scoreSgMatchResultScoreSecond;
        public LinearLayout scoreSgItem;
        public CheckBox scoreSgWeekStarCb;
        public LinearLayout scoreSgHalfLy;
        public TextView middle;
        public TextView left;
        public TextView right;
        public LinearLayout scoreSgMatchRedTwinkleIvLy;
        public RelativeLayout scoreSgWeekStarCbLy;
    }


    class MyAdapterListener implements View.OnClickListener {
        private int position;

        public MyAdapterListener(int pos) {
            this.position = pos;
        }

        @Override
        public void onClick(View v) {
            ToastUtils.midToast(UIUtils.getContext(), "您点击了-" + position, 0);
        }
    }
}
