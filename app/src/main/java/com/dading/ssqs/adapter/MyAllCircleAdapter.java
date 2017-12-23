package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.MatchTypeInfoActivity;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FouceArticleCategoryElement;
import com.dading.ssqs.bean.AllCircleRBean;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.GlideCircleTransform;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/18 14:30
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MyAllCircleAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "MyAllCircleAdapter";
    private final Context context;
    private final List<AllCircleRBean> data;

    public MyAllCircleAdapter(Context context, List<AllCircleRBean> list) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.lv_all_circle_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final AllCircleRBean dataEntity = data.get(position);
        if (dataEntity != null) {

            Glide.with(context.getApplicationContext())
                    .load(dataEntity.imageUrl)
                    .error(R.mipmap.fail)
                    .centerCrop()
                    .transform(new GlideCircleTransform(context))
                    .into(holder.mAllCircleItemIv);

            String text = dataEntity.fanCount + "";
            holder.mAllCircleItemFans.setText(text);
            holder.mAllCircleItemTitle.setText(dataEntity.name);
            if (dataEntity.isFouce == 1) {
                holder.mAllCircleItemColloect.setChecked(true);
            } else {
                holder.mAllCircleItemColloect.setChecked(false);
            }
            /**
             * a)	请求地址：
             /v1.0/fouceArticleCategory
             b)	请求方式:
             post
             c)	请求参数说明：
             artCategoryID:文章类别ID
             status:状态 0-取消关注1-关注
             auth_token：登陆后加入请求头
             */
            holder.mAllCircleItemColloect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int value = dataEntity.id;
                    if (isChecked) {
                        /**
                         * okHttp post同步请求表单提交
                         * @param actionUrl 接口地址
                         * @param paramsMap 请求参数
                         */
                        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                            FouceArticleCategoryElement element = new FouceArticleCategoryElement();
                            element.setArtCategoryID(String.valueOf(value));
                            element.setStatus("1");

                            setFouce(element);
                        } else {
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }
                    } else {
                        /**
                         * okHttp post同步请求表单提交
                         * @param actionUrl 接口地址
                         * @param paramsMap 请求参数
                         */
                        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                            FouceArticleCategoryElement element = new FouceArticleCategoryElement();
                            element.setArtCategoryID(String.valueOf(value));
                            element.setStatus("0");

                            setFouce(element);
                        } else {
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }
                    }
                }

                private void setFouce(FouceArticleCategoryElement element) {

                    SSQSApplication.apiClient(0).fouceArticleCategory(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                UIUtils.SendReRecevice(Constent.ALL_CIRCLE);
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                } else {
                                    TmtUtils.midToast(context, result.getMessage(), 0);
                                }
                            }
                        }
                    });
                }
            });
            holder.mAllCircleItemLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MatchTypeInfoActivity.class);
                    intent.putExtra(Constent.ALL_CIRCLE_TYPE, dataEntity.toString());
                    intent.putExtra(Constent.ALL_CIRCLE_TYPE_TAG, Constent.ALL_CIRCLE2_COME);
                    LogUtil.util(TAG, "推荐圈子我被执行了------------------------------:");
                    context.startActivity(intent);

                }

            });
        }
        return convertView;

    }


    static class ViewHolder {
        @Bind(R.id.all_circle_item_iv)
        ImageView mAllCircleItemIv;
        @Bind(R.id.all_circle_item_title)
        TextView mAllCircleItemTitle;
        @Bind(R.id.all_circle_item_fans)
        TextView mAllCircleItemFans;
        @Bind(R.id.all_circle_item_colloect)
        CheckBox mAllCircleItemColloect;
        @Bind(R.id.all_circle_item_ly)
        LinearLayout mAllCircleItemLy;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
