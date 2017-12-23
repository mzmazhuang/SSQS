package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
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

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.SavantInfoActivity;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FocusUserElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.GeneralUserBean;
import com.dading.ssqs.bean.MyFollowBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.components.GlideCircleTransform;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/29 16:18
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyFollowListFansAdapter extends BaseAdapter implements ListAdapter {

    private static final String TAG = "MyFollowListAdapter";
    private final Context context;
    private final List<MyFollowBean> data;
    private final View mView;
    private final LinearLayout mpopLy;
    private ImageView mPhoto;
    private TextView mNumber;
    private ImageView mClose;
    private TextView mNickName;
    private TextView mRanking;
    private TextView mInstr;
    private CheckBox mFollow;
    private TextView mWeekResult;
    private TextView mWeekResultLv;
    private TextView mGetWeekGlod;
    private TextView mGetWeekGlodLv;
    private TextView mMonthResult;
    private TextView mMonthResultLv;
    private TextView mGetMonthGlod;
    private TextView mMonthGlodLv;
    private TextView mGlod;
    private PopupWindow mPop;

    public MyFollowListFansAdapter(Context context, List<MyFollowBean> data, int i) {
        this.context = context;
        this.data = data;
        /**
         * pop界面
         */
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.my_follow_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final MyFollowBean entity = data.get(position);

        if (entity != null) {
            SSQSApplication.glide.load(entity.avatar).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(context)).into(holder.mMyFollowItemPhoto);

            holder.mMyFollowItemNickName.setText(entity.userName);
            holder.mMyFollowItemDesc.setText(entity.signature);
            holder.mMyFollowItemDesc.setVisibility(View.VISIBLE);
            if (entity.isFouce == 1) {
                holder.mMyFollowFollowTag.setChecked(true);
            } else {
                holder.mMyFollowFollowTag.setChecked(false);
            }

            holder.mMyFollowItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (entity.userType == 2) {
                        Intent intent = new Intent(context, SavantInfoActivity.class);
                        intent.putExtra(Constent.SAVANT_ID, entity.id);
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
                                        processDataPop(bean);
                                    }
                                } else {
                                    Logger.d(TAG, result.getMessage() + "红人明星中獎失败信息");
                                }
                            }
                        });
                    }
                }
            });


            holder.mMyFollowFollowTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**
                     * okHttp post同步请求表单提交
                     * @param actionUrl 接口地址
                     * @param paramsMap 请求参数
                     */

                    FocusUserElement element = new FocusUserElement();
                    element.setFouceUserID(entity.id);
                    element.setStatus(entity.isFouce == 1 ? "0" : "1");

                    SSQSApplication.apiClient(0).focusUser(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                UIUtils.SendReRecevice(Constent.MY_FOLLOW_TAG);

                                if (entity.isFouce == 1) {
                                    entity.isFouce = 0;
                                    holder.mMyFollowFollowTag.setChecked(false);
                                } else {
                                    entity.isFouce = 1;
                                    holder.mMyFollowFollowTag.setChecked(true);
                                }
                                Logger.d(TAG, "更新数据------------------------------:");
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                } else {
                                    ToastUtils.midToast(context, result.getMessage(), 0);
                                }
                            }
                        }
                    });
                }
            });
        }
        return convertView;
    }

    private void processDataPop(final GeneralUserBean data) {
        if (data != null && !TextUtils.isEmpty(data.avatar)) {

            SSQSApplication.glide.load(data.avatar).asBitmap().centerCrop().
                    into(new BitmapImageViewTarget(mPhoto) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            mPhoto.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            mPhoto.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rect_circle_45));

        }
        String text = "会员号:" + data.id;
        mNumber.setText(text);
        mNickName.setText(data.userName);
        mRanking.setText(data.rank);
        String glod = data.banlance + "";
        mGlod.setText(glod);
        mInstr.setText(data.signature);
        if (data.isSelf == 1) {
            mFollow.setChecked(false);
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
                    mFollow.setChecked(false);
                    ToastUtils.midToast(context, "用户自己不能关注自己!", 0);
                    return;
                } else {
                    /**
                     * okHttp post同步请求表单提交
                     * @param actionUrl 接口地址
                     * @param paramsMap 请求参数
                     */
                    FocusUserElement element = new FocusUserElement();
                    element.setFouceUserID(data.id);
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
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                } else {
                                    Logger.d(TAG, result.getMessage());
                                    if (data.isFriend == 0) {
                                        data.isFriend = 0;
                                        mFollow.setChecked(false);
                                    } else if (data.isFriend == 1) {
                                        data.isFriend = 1;
                                        mFollow.setChecked(true);
                                    }
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
                Logger.d(TAG, "pop消失------------------------------:");
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

    public List<MyFollowBean> getData() {
        return data;
    }

    static class ViewHolder {
        @Bind(R.id.my_follow_item)
        LinearLayout mMyFollowItem;
        @Bind(R.id.my_follow_item_photo)
        ImageView mMyFollowItemPhoto;
        @Bind(R.id.my_follow_item_nickname)
        TextView mMyFollowItemNickName;
        @Bind(R.id.my_follow_item_desc)
        TextView mMyFollowItemDesc;
        @Bind(R.id.my_follow_follow_tag)
        CheckBox mMyFollowFollowTag;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
