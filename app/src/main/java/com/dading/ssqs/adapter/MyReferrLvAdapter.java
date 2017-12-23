package com.dading.ssqs.adapter;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/26 10:15
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dading.ssqs.R;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.SavantInfoActivity;
import com.dading.ssqs.activity.SavantLvItemActivity;
import com.dading.ssqs.activity.StoreActivity;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ReferReferBean;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.GlideCircleTransform;

import java.util.List;

/**
 * 推荐比赛适配器---referControllar
 */
public class MyReferrLvAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "MyReferrLvAdapter";
    private final Context mContent;
    private final List<ReferReferBean> data;

    public MyReferrLvAdapter(Context content, List<ReferReferBean> list) {
        this.mContent = content;
        this.data = list;
    }

    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContent);
        // 将layout转成view,因为dialog表示容器所以不能写true要写成空
        View contentView = View.inflate(mContent, R.layout.dialog_init, null);
        TextView notice = (TextView) contentView.findViewById(R.id.dialog_tv_notice);
        notice.setText(mContent.getString(R.string.balance_dialog_diamons));
        builder.setView(contentView);

        final AlertDialog dialog = builder.create();

        // 找到cancel的控件id实行监听
        contentView.findViewById(R.id.btn_cancel).setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "取消对话框");
                        dialog.dismiss();
                    }
                });

        // 找到confirm的控件id实行监听
        contentView.findViewById(R.id.btn_confirm).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "确认对话框");
                        dialog.dismiss();
                        Intent intent = new Intent(mContent, StoreActivity.class);
                        mContent.startActivity(intent);
                    }
                });
        dialog.show();
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 3;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContent, R.layout.referrpager_lv_item, null);
            holder.savantItem = (LinearLayout) convertView.findViewById(R.id.referr_lv_item);
            holder.savantPhoto = (ImageView) convertView.findViewById(R.id.referr_savant_lv_savantpoto);
            holder.savantNickName = (TextView) convertView.findViewById(R.id.referr_savant_lv_nickname);
            holder.savantNickLeve = (TextView) convertView.findViewById(R.id.referr_savant_lv_nickleve);
            holder.savantMatchGuessData = (TextView) convertView.findViewById(R.id.referr_savant_lv_guess_data);
            holder.savantMatchGuessScore = (TextView) convertView.findViewById(R.id.referr_savant_lv_guess_score);
            holder.savantMatchType = (TextView) convertView.findViewById(R.id.referr_savant_lv_match_type);
            holder.savantMatchMain = (TextView) convertView.findViewById(R.id.referr_savant_lv_match_main);
            holder.savantMatchSecond = (TextView) convertView.findViewById(R.id.referr_savant_lv_match_second);
            holder.savantLh = (TextView) convertView.findViewById(R.id.referr_savant_lv_lianhong);
            holder.savantDr = (ImageView) convertView.findViewById(R.id.referr_savant_lv_doyen);
            holder.savantMatchData = (TextView) convertView.findViewById(R.id.referr_savant_lv_match_data);
            holder.savantGuessType = (TextView) convertView.findViewById(R.id.referr_savant_lv_guess_type);
            holder.savantGuessPrice = (TextView) convertView.findViewById(R.id.referr_savant_lv_guess_price);
            holder.savantGuessDesc = (TextView) convertView.findViewById(R.id.referr_savant_lv_guess_desc);
            holder.savantUpLoadTime = (TextView) convertView.findViewById(R.id.referr_savant_lv_upload_time);

            holder.savantGoodNum = (TextView) convertView.findViewById(R.id.referr_savant_lv_goodnum);
            holder.savantBadNum = (TextView) convertView.findViewById(R.id.referr_savant_lv_badnum);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ReferReferBean entity = data.get(position);
        if (entity != null) {
            Glide.with(mContent.getApplicationContext())
                    .load(entity.avatar)
                    .error(R.mipmap.fail)
                    .centerCrop()
                    .transform(new GlideCircleTransform(mContent))
                    .into(holder.savantPhoto);

            switch (entity.level) {
                case 1:
                    holder.savantNickLeve.setText("初级专家");
                    break;
                case 2:
                    holder.savantNickLeve.setText("中级专家");
                    break;
                case 3:
                    holder.savantNickLeve.setText("高级专家");
                    break;
                case 4:
                    holder.savantNickLeve.setText("资深专家");
                    break;
                default:
                    break;
            }
            holder.savantNickName.setText(entity.userName);
            String s = "近" + entity.tip + "天";
            holder.savantMatchGuessData.setText(s);
            holder.savantMatchGuessScore.setText(entity.tipContent);
            String s1 = entity.lh + "连红";
            holder.savantLh.setText(s1);
            holder.savantDr.setVisibility(View.GONE);
            holder.savantGuessType.setText(entity.payRateName);

            holder.savantMatchType.setText(entity.leagueName);
            holder.savantMatchMain.setText(entity.home);
            holder.savantMatchSecond.setText(entity.away);
            holder.savantMatchData.setText(entity.openTime.substring(5, 10));
            //holder.savantMatchData.setText(entity.openTime.substring(9, 15));
            if (entity.isBuy == 2) {
                holder.savantGuessPrice.setText("我的推荐");
            } else if (entity.isBuy == 0) {
                String s2 = entity.amount + "钻购买";
                holder.savantGuessPrice.setText(s2);
            } else {
                holder.savantGuessPrice.setText("已购买");
            }
            holder.savantGuessDesc.setText(entity.reason);
            String date = entity.createDate;
            String diffCurTime = DateUtils.diffCurTime(date, DateUtils.getCurTime("yyyy-MM-dd HH:mm:ss"));
            if (TextUtils.isEmpty(diffCurTime))
                diffCurTime = "0";
            String text2 = diffCurTime + "前发布";
            holder.savantUpLoadTime.setText(text2);
            String text = entity.suppCount + "";
            holder.savantGoodNum.setText(text);
            String text1 = entity.hateCount + "";
            holder.savantBadNum.setText(text1);

            holder.savantPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentSavant = new Intent(mContent, SavantInfoActivity.class);
                    intentSavant.putExtra(Constent.SAVANT_ID, entity.userID);
                    mContent.startActivity(intentSavant);
                }
            });
            holder.savantNickName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentSavant = new Intent(mContent, SavantInfoActivity.class);
                    intentSavant.putExtra(Constent.SAVANT_ID, entity.userID);
                    mContent.startActivity(intentSavant);
                }
            });
            final ViewHolder finalHolder = holder;
            holder.savantItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentRefer = new Intent(mContent, SavantLvItemActivity.class);
                    String value = entity.id + "";
                    intentRefer.putExtra(Constent.MATCH_ID, value);
                    //跳转进推荐内容也
                    if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                        if (entity.isBuy == 0) {
                            String glod = UIUtils.getSputils().getString(Constent.DIAMONDS, "0");
                            int i = Integer.parseInt(glod);
                            if (entity.amount <= i) {
                                i = i - entity.amount;
                                UIUtils.getSputils().putString(Constent.DIAMONDS, i + "");
                                entity.isBuy = 1;
                                finalHolder.savantGuessPrice.setText("已购买");

                                UIUtils.SendReRecevice(Constent.SERIES);
                                mContent.startActivity(intentRefer);
                            } else {
                                // TmtUtils.midToast(mContent, "对不起您的余额不足请充值!", 0);
                                initDialog();
                            }
                        } else {
                            mContent.startActivity(intentRefer);
                        }
                    } else {
                        Intent intent = new Intent(mContent, LoginActivity.class);
                        mContent.startActivity(intent);
                    }
                }
            });
            holder.savantGuessPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentRefer = new Intent(mContent, SavantLvItemActivity.class);
                    String value = entity.id + "";
                    intentRefer.putExtra(Constent.MATCH_ID, value);
                    //跳转进推荐内容也
                    if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                        if (entity.isBuy == 0) {
                            String glod = UIUtils.getSputils().getString(Constent.DIAMONDS, "0");
                            int i = Integer.parseInt(glod);
                            if (entity.amount <= i) {
                                i = i - entity.amount;
                                UIUtils.getSputils().putString(Constent.DIAMONDS, i + "");
                                entity.isBuy = 1;
                                finalHolder.savantGuessPrice.setText("已购买");
                                UIUtils.SendReRecevice(Constent.SERIES);
                                mContent.startActivity(intentRefer);
                            } else {
                                // TmtUtils.midToast(mContent, "对不起您的余额不足请充值!", 0);
                                initDialog();
                            }
                        } else {
                            mContent.startActivity(intentRefer);
                        }
                    } else {
                        Intent intent = new Intent(mContent, LoginActivity.class);
                        mContent.startActivity(intent);
                    }
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        public ImageView savantPhoto;
        public TextView savantNickName;
        public TextView savantNickLeve;
        public TextView savantMatchData;
        public TextView savantGuessType;
        public TextView savantGuessPrice;
        public TextView savantGuessDesc;
        public TextView savantUpLoadTime;
        public TextView savantGoodNum;
        public LinearLayout savantItem;
        public TextView savantMatchGuessData;
        public TextView savantMatchGuessScore;
        public TextView savantLh;
        public ImageView savantDr;
        public TextView savantMatchType;
        public TextView savantMatchMain;
        public TextView savantMatchSecond;
        public TextView savantBadNum;
    }
}
