package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.SavantInfoActivity;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FocusUserElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.GeneralUserBean;
import com.dading.ssqs.bean.SavantFansBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.UIUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/8 14:56
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HeFollowAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "HeFollowAdapter";
    private final Context context;
    private final List<SavantFansBean> data;
    private final TextView mGlod;
    private final LinearLayout mpopLy;
    private final View mView;
    private final ImageView mPhoto;
    private final TextView mNumber;
    private final ImageView mClose;
    private final TextView mNickName;
    private final TextView mRanking;
    private final TextView mInstr;
    private final CheckBox mFollow;
    private final TextView mWeekResult;
    private final TextView mWeekResultLv;
    private final TextView mGetWeekGlod;
    private final TextView mGetWeekGlodLv;
    private final TextView mMonthResult;
    private final TextView mMonthResultLv;
    private final TextView mGetMonthGlod;
    private final TextView mMonthGlodLv;
    private PopupWindow mPop;

    public HeFollowAdapter(Context context, List<SavantFansBean> list) {
        this.context = context;
        this.data = list;
        mView = View.inflate(context, R.layout.activity_general_number, null);
        mpopLy = ButterKnife.findById(mView, R.id.general_number_ly);
        mPhoto = ButterKnife.findById(mView, R.id.general_number_photo);
        mNumber = ButterKnife.findById(mView, R.id.general_number_nick_num);
        mClose = ButterKnife.findById(mView, R.id.general_number_close);
        mNickName = ButterKnife.findById(mView, R.id.general_nickname);
        mRanking = ButterKnife.findById(mView, R.id.general_ranking);
        mGlod = ButterKnife.findById(mView, R.id.general_glod);
        mInstr = ButterKnife.findById(mView, R.id.general_instro);

        //mGivePresent = ButterKnife.findById(mView, R.id.general_give_present);
        mFollow = ButterKnife.findById(mView, R.id.general_follow);
        mWeekResult = ButterKnife.findById(mView, R.id.general_week_guess_result);
        mWeekResultLv = ButterKnife.findById(mView, R.id.general_week_guess_result_lv);
        mGetWeekGlod = ButterKnife.findById(mView, R.id.general_week_guess_get_glod);
        mGetWeekGlodLv = ButterKnife.findById(mView, R.id.general_week_guess_result_get_lv);
        mMonthResult = ButterKnife.findById(mView, R.id.general_month_guess_result);
        mMonthResultLv = ButterKnife.findById(mView, R.id.general_month_guess_result_lv);
        mGetMonthGlod = ButterKnife.findById(mView, R.id.general_month_guess_get_glod);
        mMonthGlodLv = ButterKnife.findById(mView, R.id.general_month_guess_result_get_lv);

        mPop = PopUtil.popuMake(mView);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder hoder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.he_follow_item, null);
            hoder = new ViewHolder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHolder) convertView.getTag();
        }

        final SavantFansBean entity = data.get(position);

        if (entity != null)
            SSQSApplication.glide.load(entity.avatar).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(context)).into(hoder.mFollowInfoSavantpoto);

        if (entity.userType == 2) {
            hoder.mFollowInfoNickname.setText(entity.userName);
            hoder.mFollowInfoNickname.setTextColor(context.getResources().getColor(R.color.yellow_orange));
        } else {
            hoder.mFollowInfoNickname.setText(entity.userName);
            hoder.mFollowInfoNickname.setTextColor(Color.BLACK);
        }
        if (entity.isFouce == 0) {
            hoder.mFollowInfoFollowCb.setChecked(false);
        } else {
            hoder.mFollowInfoFollowCb.setChecked(true);
        }

        if (entity.isSelf == 1) {
            hoder.mFollowInfoFollowCb.setVisibility(View.GONE);
        } else {
            hoder.mFollowInfoFollowCb.setVisibility(View.VISIBLE);
        }

        hoder.mFollowInfoFollowText.setText(entity.signature);
        hoder.mFollowInfoLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (entity.userType == 2) {
                    Intent intent = new Intent(context, SavantInfoActivity.class);
                    intent.putExtra(Constent.SAVANT_ID, entity.id);
                    Logger.INSTANCE.d(TAG, "传入的专家id是------------------------------:" + entity.id);
                    context.startActivity(intent);
                } else {
                    /**
                     * 12.	用户信息统计
                     a)	请求地址：
                     /v1.0/user/userInfo/userID/{userID}
                     b)	请求方式:
                     get
                     c)	请求参数说明：
                     userID:用户ID
                     auth_token：登陆后加入请求头
                     */

                    SSQSApplication.apiClient(0).uploadUserInfo(entity.id, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                GeneralUserBean bean = (GeneralUserBean) result.getData();

                                if (bean != null) {
                                    processData(bean, entity.id);
                                }
                            } else {
                                Logger.INSTANCE.d(TAG, result.getMessage() + "红人明星中獎失败信息");
                            }
                        }
                    });
                }
            }
        });
        /**
         * 8.	关注用户
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
        hoder.mFollowInfoFollowCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
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
                                hoder.mFollowInfoFollowCb.setChecked(true);
                            } else if (entity.isFouce == 1) {
                                entity.isFouce = 0;
                                hoder.mFollowInfoFollowCb.setChecked(false);
                            }
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                            } else {
                                Logger.INSTANCE.d(TAG, result.getMessage());
                                if (entity.isFouce == 0) {
                                    entity.isFouce = 0;
                                    hoder.mFollowInfoFollowCb.setChecked(false);
                                } else if (entity.isFouce == 1) {
                                    entity.isFouce = 1;
                                    hoder.mFollowInfoFollowCb.setChecked(true);
                                }
                            }
                        }
                    }
                });
            }
        });
        return convertView;
    }

    public class GlideCircleTransform extends BitmapTransformation {
        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

    private void processData(final GeneralUserBean data, final String id) {
        if (data != null && !TextUtils.isEmpty(data.avatar)) {
            SSQSApplication.glide.load(data.avatar).transform(new GlideCircleTransform(context)).error(R.mipmap.nologinportrait).into(mPhoto);
        }

        String text = "会员号:" + data.id;
        mNumber.setText(text);
        mNickName.setText(data.userName);
        mRanking.setText(data.rank);
        String glod = data.banlance + "";
        mGlod.setText(glod);
        mInstr.setText(data.signature);
        if (data.isSelf == 1) {
            mFollow.setChecked(true);
        } else {
            if (data.isFriend == 1) {
                mFollow.setChecked(true);
            } else {
                mFollow.setChecked(false);
            }
        }
        if (data.record.size() == 2) {
            for (GeneralUserBean.RecordEntity re : data.record) {
                String banlance = re.banlance + "";
                String winLV = re.winRate + "%";
                String weekResult = re.win + "贏 " + re.draw + "平 " + re.lost + " 负";
                String getBack = re.repayRate + "%";
                if (re.type == 1) {
                    mWeekResult.setText(weekResult);
                    mWeekResultLv.setText(winLV);
                    mGetWeekGlod.setText(banlance);
                    mGetWeekGlodLv.setText(getBack);
                } else if (re.type == 2) {
                    mMonthResult.setText(weekResult);
                    mMonthResultLv.setText(winLV);
                    mGetMonthGlod.setText(banlance);
                    mMonthGlodLv.setText(getBack);
                }
            }
        }

        mpopLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });
        mFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status2 = 0;
                if (!UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    return;
                }
                if (data.isSelf == 1) {
                    return;
                } else {
                    /**
                     * okHttp post同步请求表单提交
                     * @param actionUrl 接口地址
                     * @param paramsMap 请求参数
                     */

                    FocusUserElement element = new FocusUserElement();
                    element.setFouceUserID(id);
                    element.setStatus(data.isFriend == 0 ? "1" : "0");

                    SSQSApplication.apiClient(0).focusUser(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                if (data.isFriend == 0) {
                                    data.isFriend = 1;
                                    mFollow.setChecked(true);
                                } else if (data.isFriend == 1) {
                                    data.isFriend = 0;
                                    mFollow.setChecked(false);
                                }
                            } else {
                                Logger.INSTANCE.d(TAG, result.getMessage());
                                if (data.isFriend == 0) {
                                    data.isFriend = 0;
                                    mFollow.setChecked(false);
                                } else if (data.isFriend == 1) {
                                    data.isFriend = 1;
                                    mFollow.setChecked(true);
                                }
                            }
                        }
                    });
                }
            }
        });
        mpopLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
                Logger.INSTANCE.d(TAG, "pop消失------------------------------:");
            }
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPop.dismiss();
            }
        });

        mPop.showAtLocation(mView, Gravity.CENTER, 0, 0);
    }


    static class ViewHolder {
        @Bind(R.id.follow_info_ly)
        LinearLayout mFollowInfoLy;
        @Bind(R.id.follow_info_savantpoto)
        ImageView mFollowInfoSavantpoto;
        @Bind(R.id.follow_info_nickname)
        TextView mFollowInfoNickname;
        @Bind(R.id.follow_info_follow_cb)
        CheckBox mFollowInfoFollowCb;
        @Bind(R.id.follow_info_follow_text)
        TextView mFollowInfoFollowText;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
