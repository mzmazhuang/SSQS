package com.dading.ssqs.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.PersonalReportActivity;
import com.dading.ssqs.activity.ProxyCodeActivty;
import com.dading.ssqs.activity.ProxyCommissionActivty;
import com.dading.ssqs.activity.ProxyIntroActivty;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.MultiItem;
import com.dading.ssqs.bean.ProxyCenterBean;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.GlideCircleTransform;
import com.dading.ssqs.view.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     zcl
 * 创建时间   2017/8/3 16:01
 * 描述	      ${代理中心界面}$
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}$
 */
public class ProxyAdapter extends BaseMultiItemQuickAdapter<MultiItem> {
    private final Context context;


    public ProxyAdapter(Context context, List<MultiItem> data) {
        super(data);
        this.context = context;
        addItemType(MultiItem.PROXY_HEAD, R.layout.proxy_head);
        addItemType(MultiItem.PROXY_RECYCLE_VIEW, R.layout.proxy_recycleview);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, MultiItem multiItem) {
        int type = multiItem.getItemType();

        if (type == MultiItem.PROXY_HEAD) {
            ProxyCenterBean data = multiItem.getData();
            baseViewHolder.setText(R.id.proxy_head_username, "用户名: " + data.getUserName())
                    .setText(R.id.proxy_head_account_balance, "账户余额: " + data.getBanlance())
                    .setText(R.id.proxy_head_commisions_earned, "佣金收入: " + data.getFee());

            ImageView photo = baseViewHolder.getView(R.id.proxy_head_photo);
            SSQSApplication.glide.load(data.getAvatar()).asBitmap().centerCrop().transform(new GlideCircleTransform(mContext)).into(photo);

        } else if (type == MultiItem.PROXY_RECYCLE_VIEW) {
            RecyclerView recycleView = baseViewHolder.getView(R.id.templete_recycleview);
            recycleView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

            ArrayList<String> list = new ArrayList<>();
            int userType = UIUtils.getSputils().getInt(Constent.USER_TYPE_NUM, 0);
            if (userType == 4) {
                list.add(context.getString(R.string.proxy_intro));
                list.add(context.getString(R.string.proxy_code));
                list.add(context.getString(R.string.proxy_commission));
                list.add(context.getString(R.string.proxy_report));
            } else {
                list.add(context.getString(R.string.proxy_intro));
            }

            recycleView.setFocusable(false);
            recycleView.setFocusableInTouchMode(true);
            recycleView.requestFocus();
            recycleView.addItemDecoration(new RecyclerViewDivider(context, LinearLayoutManager.HORIZONTAL, 1, R.color.gray_e));
            ProxyRecycleAdapter adapter = new ProxyRecycleAdapter(context, R.layout.my_lv_item, list);
            recycleView.setAdapter(adapter);
            adapter.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, int i) {
                    Intent intent = null;
                    switch (i) {
                        case 0:
                            intent = new Intent(mContext, ProxyIntroActivty.class);
                            break;
                        case 1:
                            intent = new Intent(mContext, ProxyCodeActivty.class);
                            break;
                        case 2:
                            intent = new Intent(mContext, ProxyCommissionActivty.class);
                            break;
                        case 3:
                            intent = new Intent(mContext, PersonalReportActivity.class);
                            break;
                    }
                    if (intent != null)
                        mContext.startActivity(intent);
                }
            });
        }
    }
}
