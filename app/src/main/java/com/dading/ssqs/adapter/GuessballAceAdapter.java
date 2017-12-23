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

import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.GuessBallACEActivity;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.SavantInfoActivity;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FocusUserElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.GuessACEBean;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.GlideCircleTransform;
import com.dading.ssqs.R;


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
public class GuessballAceAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "GuessballAceAdapter";
    private final List<GuessACEBean> data;
    private final Context context;
    private int mStatus;


    public GuessballAceAdapter(GuessBallACEActivity context, List<GuessACEBean> data) {
        this.context = context;
        this.data = data;
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
            convertView = View.inflate(context, R.layout.guessball_ace_lv_item, null);
            hoder = new ViewHolder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHolder) convertView.getTag();
        }
        final GuessACEBean entity = data.get(position);
        String text = entity.order + "";
        hoder.mGuessballAceWinRanking.setText(text);

        SSQSApplication.glide.load(entity.avatar).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(context)).into(hoder.mGuessballAceItemSavantpoto);

        hoder.mGuessballAceItemNickname.setText(entity.userName);
        switch (entity.level) {
            case 1:
                hoder.mGuessballAceItemNickleve.setText("初级");
                break;
            case 2:
                hoder.mGuessballAceItemNickleve.setText("中级");
                break;
            case 3:
                hoder.mGuessballAceItemNickleve.setText("高级");
                break;
            default:
                break;
        }
        if (entity.isFouce == 0) {
            hoder.mGuessballAceItemGuessFollow.setChecked(false);
        } else if (entity.isFouce == 1) {
            hoder.mGuessballAceItemGuessFollow.setChecked(true);
        }

        if (entity.isSelf == 0) {
            hoder.mGuessballAceItemGuessFollow.setVisibility(View.VISIBLE);
        } else {
            hoder.mGuessballAceItemGuessFollow.setVisibility(View.GONE);
        }

        String fan = entity.tag + "连红";
        hoder.mGuessballAceItemScoreIcon.setText(fan);

        hoder.mGuessballAceItemText.setText(entity.intro);

        /**
         * 7.	关注用户
         a)	请求地址：
         /v1.0/fouce
         b)	请求方式:
         Post
         c)	请求参数说明：
         {"fouceUserID":"20160708134662","status":1}
         fouceUserID:被关注的用户ID，
         status:  0-取消关注 1-关注
         auth_token：登陆后加入请求头
         d)	返回格式
         {
         "status": true,
         "code": 0,
         "msg": "",
         "data": {}
         }
         */
        hoder.mGuessballAceItemGuessFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    hoder.mGuessballAceItemGuessFollow.setChecked(false);
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
                                hoder.mGuessballAceItemGuessFollow.setChecked(true);
                            } else if (entity.isFouce == 1) {
                                entity.isFouce = 0;
                                hoder.mGuessballAceItemGuessFollow.setChecked(false);
                            }
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                            } else {
                                LogUtil.util(TAG, result.getMessage());
                                if (entity.isFouce == 0) {
                                    entity.isFouce = 0;
                                    hoder.mGuessballAceItemGuessFollow.setChecked(false);
                                } else if (entity.isFouce == 1) {
                                    entity.isFouce = 1;
                                    hoder.mGuessballAceItemGuessFollow.setChecked(true);
                                }
                            }
                        }
                    }
                });
            }
        });

        hoder.mGuessballAceItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SavantInfoActivity.class);
                intent.putExtra(Constent.SAVANT_ID, data.get(position).id);
                context.startActivity(intent);
            }
        });

        hoder.mGuessballAceItemSavantpoto.setOnClickListener(new View.OnClickListener() {
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
        @Bind(R.id.guessball_ace_item)
        LinearLayout mGuessballAceItem;
        @Bind(R.id.guessball_ace_win_ranking)
        TextView mGuessballAceWinRanking;
        @Bind(R.id.guessball_ace_item_savantpoto)
        ImageView mGuessballAceItemSavantpoto;
        @Bind(R.id.guessball_ace_item_nickname)
        TextView mGuessballAceItemNickname;
        @Bind(R.id.guessball_ace_item_nickleve)
        TextView mGuessballAceItemNickleve;
        @Bind(R.id.guessball_ace_item_guess_follow)
        CheckBox mGuessballAceItemGuessFollow;
        @Bind(R.id.guessball_ace_item_score_icon)
        TextView mGuessballAceItemScoreIcon;
        @Bind(R.id.guessball_ace_item_text)
        TextView mGuessballAceItemText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
