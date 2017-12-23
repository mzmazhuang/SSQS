package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.SavantInfoActivity;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FocusUserElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.RedPopleARBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.GlideCircleTransform;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/p3 16:28
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RedPeopleSingleHRAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "RedPeopleSingleAdapter";
    private final Context context;
    private final List<RedPopleARBean> data;
    private int mStatus;

    public RedPeopleSingleHRAdapter(Context context, List<RedPopleARBean> list) {
        this.context = context;
        this.data = list;
    }

    @Override
    public int getCount() {
        if (data != null) {
            return data.size();
        }
        return 0;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder hoder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.red_people_single_lv_item, null);
            hoder = new ViewHolder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHolder) convertView.getTag();
        }

        final RedPopleARBean entity = data.get(position);
        if (entity != null) {

            SSQSApplication.glide.load(entity.avatar).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(context)).into(hoder.mRedPeopleArItemSavantpoto);
            hoder.mRedPeopleArItemNickname.setText(entity.userName);
            hoder.mRedPeopleArItemNumber.setText(String.valueOf(1 + position));
            switch (entity.level) {
                case 1:
                    hoder.mRedPeopleArItemNickleve.setText("初级");
                    break;
                case 2:
                    hoder.mRedPeopleArItemNickleve.setText("中级");
                    break;
                case 3:
                    hoder.mRedPeopleArItemNickleve.setText("高级");
                    break;
                default:
                    break;
            }
            if (entity.isFouce == 0) {
                hoder.mRedPeopleArItemFollow.setChecked(false);
            } else if (entity.isFouce == 1) {
                hoder.mRedPeopleArItemFollow.setChecked(true);
            }

            String LH = entity.tag + "连红";
            if (TextUtils.isEmpty(entity.tag))
                LH = "0连红";
            hoder.mRedPeopleArItemLianhong.setText(LH);

            String winLv = entity.rate + "%";
            hoder.mRedPeopleArItemSevenWin.setText(winLv);

            String howZhong = entity.counts + "中" + entity.win;
            hoder.mRedPeopleArItemWinHow.setText(howZhong);

            String winMoneyLv = entity.profit + "%";
            hoder.mRedPeopleArItemWinMoneyLv.setText(winMoneyLv);

            if (entity.isSelf == 0) {
                hoder.mRedPeopleArItemFollow.setVisibility(View.VISIBLE);
            } else {
                hoder.mRedPeopleArItemFollow.setVisibility(View.GONE);
            }

            hoder.mRedPeopleArItemFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hoder.mRedPeopleArItemFollow.setClickable(false);
                    if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                        return;
                    }
                    /**
                     * okHttp post同步请求表单提交
                     * @param actionUrl 接口地址
                     * @param paramsMap 请求参数
                     */
                    FocusUserElement element = new FocusUserElement();
                    element.setFouceUserID(entity.id);
                    element.setStatus(entity.isFouce == 0 ? "1" : "0");

                    SSQSApplication.apiClient(0).focusUser(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            hoder.mRedPeopleArItemFollow.setClickable(true);

                            if (result.isOk()) {
                                if (entity.isFouce == 0) {
                                    entity.isFouce = 1;
                                    hoder.mRedPeopleArItemFollow.setChecked(true);
                                } else if (entity.isFouce == 1) {
                                    entity.isFouce = 0;
                                    hoder.mRedPeopleArItemFollow.setChecked(false);
                                }
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                } else {
                                    Logger.d(TAG, result.getMessage());
                                    if (entity.isFouce == 0) {
                                        entity.isFouce = 0;
                                        hoder.mRedPeopleArItemFollow.setChecked(false);
                                    } else if (entity.isFouce == 1) {
                                        entity.isFouce = 1;
                                        hoder.mRedPeopleArItemFollow.setChecked(true);
                                    }
                                }
                            }
                        }
                    });
                }
            });
            hoder.mRedPeopleArItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SavantInfoActivity.class);
                    intent.putExtra(Constent.SAVANT_ID, data.get(position).id);
                    context.startActivity(intent);
                }
            });
            hoder.mRedPeopleArItemSavantpoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentSavant = new Intent(context, SavantInfoActivity.class);
                    intentSavant.putExtra(Constent.SAVANT_ID, entity.id);
                    context.startActivity(intentSavant);
                }
            });
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.red_people_ar_item_number)
        TextView mRedPeopleArItemNumber;
        @Bind(R.id.red_people_ar_item)
        LinearLayout mRedPeopleArItem;
        @Bind(R.id.red_people_ar_item_savantpoto)
        ImageView mRedPeopleArItemSavantpoto;
        @Bind(R.id.red_people_ar_item_nickname)
        TextView mRedPeopleArItemNickname;
        @Bind(R.id.red_people_ar_item_nickleve)
        TextView mRedPeopleArItemNickleve;
        @Bind(R.id.red_people_ar_item_follow)
        CheckBox mRedPeopleArItemFollow;
        @Bind(R.id.red_people_ar_item_lianhong)
        TextView mRedPeopleArItemLianhong;
        @Bind(R.id.red_people_ar_item_seven_win)
        TextView mRedPeopleArItemSevenWin;
        @Bind(R.id.red_people_ar_item_win_how)
        TextView mRedPeopleArItemWinHow;
        @Bind(R.id.red_people_ar_item_win_money_lv)
        TextView mRedPeopleArItemWinMoneyLv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
