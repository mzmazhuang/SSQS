package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.MatchInfoActivity;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FouceMatchElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.HomeBean;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.UIUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/6/30 12:39
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HomeMatchAdapter extends BaseAdapter {
    private static final String TAG = "HomeMatchAdapter";
    private final Context content;
    private final List<HomeBean.MatchsBean> mData;
    private boolean mIsLogin;
    ViewHolder holder;
    private final HashMap<Integer, ImageView> mMap1;
    private final HashMap<Integer, ImageView> mMap2;

    public HomeMatchAdapter(Context content) {
        this.content = content;
        this.mData = new ArrayList<>();
        mMap1 = new HashMap<>();
        mMap2 = new HashMap<>();
    }

    public void setList(List<HomeBean.MatchsBean> dataMatch) {
        if (dataMatch != null) {
            this.mData.clear();
            this.mData.addAll(dataMatch);
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(content, R.layout.home_lv_match_item, null);
            holder = new ViewHolder();

            holder.item = (LinearLayout) convertView.findViewById(R.id.home_lv_match_item);
            holder.leagues = (TextView) convertView.findViewById(R.id.home_lv_match_item_time_icon);
            holder.type = (TextView) convertView.findViewById(R.id.home_lv_match_item_place);
            holder.data = (TextView) convertView.findViewById(R.id.home_lv_match_item_data);
            holder.rightPl = (TextView) convertView.findViewById(R.id.home_lv_match_item_right_pl);
            holder.midPl = (TextView) convertView.findViewById(R.id.home_lv_match_item_mid_pl);
            holder.leftPl = (TextView) convertView.findViewById(R.id.home_lv_match_item_left_pl);

            holder.time = (TextView) convertView.findViewById(R.id.home_lv_match_item_time);
            holder.collect1 = (ImageView) convertView.findViewById(R.id.home_lv_match_item_collect_1);
            holder.collect2 = (ImageView) convertView.findViewById(R.id.home_lv_match_item_collect_2);

            holder.guessball = (ImageView) convertView.findViewById(R.id.home_lv_match_item_guessball);
            holder.people = (TextView) convertView.findViewById(R.id.home_lv_match_item_people);
            holder.main = (TextView) convertView.findViewById(R.id.home_lv_match_item_main);
            holder.mainIcon = (ImageView) convertView.findViewById(R.id.home_lv_match_item_main_icon);
            holder.second = (TextView) convertView.findViewById(R.id.home_lv_match_item_second);
            holder.secondIcon = (ImageView) convertView.findViewById(R.id.home_lv_match_item_second_icon);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        mMap1.put(position, holder.collect1);
        mMap2.put(position, holder.collect2);


        final HomeBean.MatchsBean entity = mData.get(position);
        if (entity != null) {
            holder.leagues.setText(entity.getLeagueName());
            holder.data.setText(entity.getOpenTime());
            holder.main.setText(entity.getHome());
            holder.second.setText(entity.getAway());

            SSQSApplication.glide.load(entity.getHImageUrl()).error(R.mipmap.image_not_white).centerCrop().into(holder.mainIcon);

            SSQSApplication.glide.load(entity.getAImageUrl()).error(R.mipmap.image_not_white).centerCrop().into(holder.secondIcon);

            String entityOpenTime = entity.getOpenTime();
            String playTime;
            if (!TextUtils.isEmpty(entityOpenTime)) {
                String openTime = entityOpenTime.substring(5, 10);
                playTime = entityOpenTime.substring(5, 16);
                String s = DateUtils.getTodayZh(openTime);
                if (TextUtils.isEmpty(s)) {
                    holder.data.setText(playTime);
                } else {
                    playTime = entityOpenTime.substring(10);
                    holder.data.setText(s + playTime);
                }
            }
            HomeBean.MatchsBean.PayRateBean rate = entity.getPayRate();
            if (rate != null) {
                Logger.d(TAG, "---" + rate.getRealRate1() + "----" + rate.getRealRate2() + "----:" + rate.getRealRate3());
                holder.leftPl.setText(rate.getRealRate1());
                holder.midPl.setText(rate.getRealRate2());
                holder.rightPl.setText(rate.getRealRate3());
            }
            holder.time.setText("共" + entity.getNum() + "玩法");

            if (TextUtils.isEmpty(entity.getAScore()) && TextUtils.isEmpty(entity.getHScore()))
                holder.type.setText(R.string.VS);
            else
                holder.type.setText(entity.getHScore() + " : " + entity.getAScore());

            String text = entity.getJoinCount() + "人参与";
            holder.people.setText(text);

            boolean b = entity.getIsFouce() == 1;
            holder.collect1.setVisibility(b ? View.GONE : View.VISIBLE);
            holder.collect2.setVisibility(b ? View.VISIBLE : View.GONE);
        }
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(content, MatchInfoActivity.class);
                UIUtils.getSputils().putBoolean(Constent.IS_FOOTBALL, true);
                int id = entity.getId();
                intent.putExtra(Constent.MATCH_ID, id);
                Logger.d(TAG, "home比赛传送的数据是------------------------------:" + id);
                intent.putExtra(Constent.INTENT_FROM, "home");
                content.startActivity(intent);
            }
        });
        holder.collect1.setOnClickListener(new MyHomeListerner(position) {
            @Override
            public void onClick(View v) {
                mIsLogin = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                if (mIsLogin) {
                    mMap1.get(position).setVisibility(View.GONE);
                    mMap2.get(position).setVisibility(View.VISIBLE);
                    mMap1.get(position).setClickable(false);
                    mMap2.get(position).setClickable(false);
                    addGz(position);
                } else {
                    Intent intent = new Intent(content, LoginActivity.class);
                    content.startActivity(intent);
                }
            }

            private void addGz(final int position) {
                //TODO 添加进收藏
                FouceMatchElement element = new FouceMatchElement();
                element.setMatchID(String.valueOf(entity.getId()));
                element.setStatus("1");

                SSQSApplication.apiClient(0).fouceMatch(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            LoadingBean bean = (LoadingBean) result.getData();

                            if (bean != null) {
                                //TmtUtils.midToast(content, "添加关注成功", 0);
                                UIUtils.SendReRecevice(Constent.GZ_RECEVICE);
                                UIUtils.SendReRecevice(Constent.JS_RECEVICE);
                                UIUtils.SendReRecevice(Constent.SC_RECEVICE);
                                UIUtils.SendReRecevice(Constent.SG_RECEVICE);
                            }
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(content, LoginActivity.class);
                                content.startActivity(intent);
                            }/* else {
                                TmtUtils.midToast(content, "添加关注失败" + bean.msg + "请刷新界面后添加", 0);
                                Logger.d(TAG,"返回数据是------------------------------:"+"添加关注失败" + bean.msg + "请刷新界面后添加");
                            }*/
                        }
                        mMap1.get(position).setClickable(true);
                        mMap2.get(position).setClickable(true);
                    }
                });
            }
        });
        holder.collect2.setOnClickListener(new MyHomeListerner(position) {
            @Override
            public void onClick(View v) {
                mIsLogin = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
                if (mIsLogin) {
                    mMap1.get(position).setVisibility(View.VISIBLE);
                    mMap2.get(position).setVisibility(View.GONE);
                    //移除收藏
                    FouceMatchElement element = new FouceMatchElement();
                    element.setMatchID(String.valueOf(entity.getId()));
                    element.setStatus("0");

                    SSQSApplication.apiClient(0).fouceMatch(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                LoadingBean bean = (LoadingBean) result.getData();

                                if (bean != null) {
                                    //TmtUtils.midToast(content, "取消关注成功", 0);
                                    UIUtils.SendReRecevice(Constent.GZ_RECEVICE);
                                }
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(content, LoginActivity.class);
                                    content.startActivity(intent);
                                } /*else {
                                    TmtUtils.midToast(content, "取消关注失败" + bean.msg + "请刷新界面后添加", 0);
                                }*/
                            }
                        }
                    });
                } else {
                    Intent intent = new Intent(content, LoginActivity.class);
                    content.startActivity(intent);
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        public TextView type;
        public TextView data;
        public ImageView guessball;
        public TextView people;
        public TextView main;
        public TextView second;
        public ImageView collect1;
        public ImageView collect2;
        public LinearLayout item;
        public ImageView mainIcon;
        public ImageView secondIcon;
        public TextView time;
        public TextView rightPl;
        public TextView midPl;
        public TextView leftPl;
        public TextView leagues;
    }


    private class MyHomeListerner implements View.OnClickListener {
        private final int postion;

        public MyHomeListerner(int pos) {
            this.postion = pos;
        }

        @Override
        public void onClick(View v) {

        }
    }
}
