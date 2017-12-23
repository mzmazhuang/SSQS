package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dading.ssqs.R;
import com.dading.ssqs.bean.ProxyIntroLookBean;

import java.util.List;

/**
 * Created by lenovo on 2017/8/10.
 */
public class ProxyLookCodeAdapter extends BaseQuickAdapter<ProxyIntroLookBean> {

    private final Context  context;
    private final TextView mInviteCode;
    private final CheckBox mSetUp;
    private final CheckBox mForbidden;

    public ProxyLookCodeAdapter (int layoutResId, List<ProxyIntroLookBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
        View view = View.inflate(context, R.layout.pop_proxy_code, null);
        mInviteCode = (TextView) view.findViewById(R.id.pop_proxy_code);
        mSetUp = (CheckBox) view.findViewById(R.id.pop_proxy_user_setup);
        mForbidden = (CheckBox) view.findViewById(R.id.pop_proxy_user_forbidden);
    }

    @Override
    protected void convert (BaseViewHolder baseViewHolder, final ProxyIntroLookBean dataBean) {
        baseViewHolder.setText(R.id.item_proxy_code_code, dataBean.getCode( ))
                .setText(R.id.item_proxy_code_type, dataBean.getType( ) == 1 ? "代理" :"其他")
                .setText(R.id.item_proxy_code_commison, dataBean.getFee( ))
                .setText(R.id.item_proxy_code_state, dataBean.getStatus( ) == 1 ? "启用" :"禁用");
        baseViewHolder.setOnClickListener(R.id.item_proxy_code_change, new View.OnClickListener( ) {
            @Override
            public void onClick (View v) {

            }
        }).setOnClickListener(R.id.item_proxy_code_del, new View.OnClickListener( ) {
            @Override
            public void onClick (View v) {

            }
        });
    }
}
