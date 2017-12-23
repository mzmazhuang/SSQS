package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.dading.ssqs.bean.LHRankingBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.components.GlideCircleTransform;


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
public class LianHongAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "LianHongAdapter";
    private final List<LHRankingBean> data;
    private final Context context;
    private int mStatus;

    public LianHongAdapter(Context context, List<LHRankingBean> list) {
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
            convertView = View.inflate(context, R.layout.lianhong_lv_item, null);
            hoder = new ViewHolder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHolder) convertView.getTag();
        }

        final LHRankingBean entity = data.get(position);

        String text = entity.order + "";
        hoder.mPopuStarWinRanking.setText(text);

        SSQSApplication.glide.load(entity.avatar).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(context)).into(hoder.mRedPeopleYpItemSavantpoto);

        hoder.mLianhongItemNickname.setText(entity.userName);
        switch (entity.level) {
            case 1:
                hoder.mLianhongItemNickleve.setText("初级");
                break;
            case 2:
                hoder.mLianhongItemNickleve.setText("中级");
                break;
            case 3:
                hoder.mLianhongItemNickleve.setText("高级");
                break;
            default:
                break;
        }
        if (entity.isFouce == 0) {
            hoder.mLianhongItemGuessFollow.setChecked(false);
        } else if (entity.isFouce == 1) {
            hoder.mLianhongItemGuessFollow.setChecked(true);
        }

        if (entity.isSelf == 0) {
            hoder.mLianhongItemGuessFollow.setVisibility(View.VISIBLE);
        } else {
            hoder.mLianhongItemGuessFollow.setVisibility(View.GONE);
        }

        String fan = entity.tag + "";
        hoder.mLinhongFansNum.setText(fan);

        hoder.mLianhongItemGuessFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    hoder.mLianhongItemGuessFollow.setChecked(false);
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
                        if (result.isOk()) {
                            if (entity.isFouce == 0) {
                                entity.isFouce = 1;
                                hoder.mLianhongItemGuessFollow.setChecked(true);
                            } else if (entity.isFouce == 1) {
                                entity.isFouce = 0;
                                hoder.mLianhongItemGuessFollow.setChecked(false);
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
                                    hoder.mLianhongItemGuessFollow.setChecked(false);
                                } else if (entity.isFouce == 1) {
                                    entity.isFouce = 1;
                                    hoder.mLianhongItemGuessFollow.setChecked(true);
                                }
                            }
                        }
                    }
                });
            }
        });

        hoder.mLHItem.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 Intent intent = new Intent(context, SavantInfoActivity.class);
                                                 intent.putExtra(Constent.SAVANT_ID, data.get(position).id);
                                                 context.startActivity(intent);
                                             }
                                         }


        );
        hoder.mRedPeopleYpItemSavantpoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSavant = new Intent(context, SavantInfoActivity.class);
                intentSavant.putExtra(Constent.SAVANT_ID, entity.id);
                context.startActivity(intentSavant);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.lianhong_ranking)
        TextView mPopuStarWinRanking;
        @Bind(R.id.lianhong_item)
        LinearLayout mLHItem;
        @Bind(R.id.lianhong_item_savantpoto)
        ImageView mRedPeopleYpItemSavantpoto;
        @Bind(R.id.lianhong_item_nickname)
        TextView mLianhongItemNickname;
        @Bind(R.id.lianhong_item_nickleve)
        TextView mLianhongItemNickleve;
        @Bind(R.id.lianhong_item_guess_follow)
        CheckBox mLianhongItemGuessFollow;
        @Bind(R.id.linhong_fans_num)
        TextView mLinhongFansNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
