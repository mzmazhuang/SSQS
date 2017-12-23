package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.ProxyCodeUpdateElement;
import com.dading.ssqs.bean.ProxyIntroLookBean;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/8/10.
 */
public class ProxyCodeAdapter extends BaseAdapter implements ListAdapter, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "ProxyCodeAdapter";
    private final Context context;
    private final List<ProxyIntroLookBean> data;
    private final TextView mInviteCode;
    private final CheckBox mSetUp;
    private final CheckBox mForbidden;
    private final Button mConfirm;
    private final RadioButton mUserType;
    private final PopupWindow mPopu;
    private final View mView;
    int states;
    private String mCodeC;
    private int mId;
    private int mPostionC;

    public ProxyCodeAdapter(Context context, List<ProxyIntroLookBean> data) {
        this.context = context;
        this.data = data;
        mView = View.inflate(context, R.layout.pop_proxy_code, null);
        mInviteCode = (TextView) mView.findViewById(R.id.pop_proxy_code);
        mUserType = (RadioButton) mView.findViewById(R.id.pop_proxy_user_type);
        mSetUp = (CheckBox) mView.findViewById(R.id.pop_proxy_user_setup);
        mForbidden = (CheckBox) mView.findViewById(R.id.pop_proxy_user_forbidden);
        mConfirm = (Button) mView.findViewById(R.id.pop_proxy_code_confirm);


        mPopu = PopUtil.popuMake(mView);

        initListerner();
    }

    private void initListerner() {
        mSetUp.setOnCheckedChangeListener(this);
        mForbidden.setOnCheckedChangeListener(this);
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopu.isShowing()) {
                    mPopu.dismiss();
                    /**
                     * 35.	代理邀请码修改
                     a)	请求地址：/v1.0/agent/code/msg
                     b)	请求方式:post
                     auth_token	string		是	token
                     id	int		是	邀请码ID
                     code	string		否	邀请码
                     status	int		否	状态 1：启用：禁用
                     */
                    ProxyCodeUpdateElement element = new ProxyCodeUpdateElement();
                    element.setId(String.valueOf(mId));
                    element.setCode(mCodeC);
                    element.setStatus(String.valueOf(states));

                    SSQSApplication.apiClient(0).proxyCodeUpdate(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                TmtUtils.midToast(UIUtils.getContext(), "更改成功", 0);
                                data.get(mPostionC).setStatus(states);
                                notifyDataSetChanged();
                            } else {
                                TmtUtils.midToast(context, "更改失败!" + result.getMessage(), 0);
                            }
                        }
                    });
                }
            }
        });
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_proxy_code, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ProxyIntroLookBean bean = data.get(position);
        holder.mItemProxyCodeCode.setText(bean.getCode());
        int type = bean.getType();
        switch (type) {
            case 1:
                holder.mItemProxyCodeType.setText(context.getString(R.string.proxy));
                break;
            case 2:
                holder.mItemProxyCodeType.setText(context.getString(R.string.other));
                break;
        }
        holder.mItemProxyCodeCommison.setText(bean.getFee());

        int code = bean.getStatus();
        holder.mItemProxyCodeState.setText(context.getString(code == 1 ? R.string.setup : R.string.forbidden));

        holder.mItemProxyCodeChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPopu.isShowing())
                    mPopu.dismiss();
                mId = bean.getId();
                mCodeC = bean.getCode();
                mPostionC = position;
                mInviteCode.setText(bean.getCode());
                if (bean.getStatus() == 1) {
                    mSetUp.setChecked(true);
                    mForbidden.setChecked(false);
                } else {
                    mSetUp.setChecked(false);
                    mForbidden.setChecked(true);
                }
                mUserType.setChecked(true);
                mPopu.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
            }
        });
        holder.mItemProxyCodeDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 34.	代理邀请码删除
                 a)	请求地址：/v1.0/agent/code/del/{id}
                 b)	请求方式:get
                 c)	请求参数说明
                 字段名	类型	长度	是否必填	备注
                 auth_token	string		是	token
                 id	int		是	邀请码ID
                 */

                SSQSApplication.apiClient(0).agentCodeDelete(bean.getId(), new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            TmtUtils.midToast(UIUtils.getContext(), "删除成功!", 0);
                            data.remove(bean);
                            notifyDataSetChanged();
                        } else {
                            TmtUtils.midToast(UIUtils.getContext(), "删除失败!" + result.getMessage(), 0);
                        }
                    }
                });
            }
        });
        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        LogUtil.util(TAG, "返回数据是------------------------------:" + buttonView.getId());
        switch (buttonView.getId()) {
            case R.id.pop_proxy_user_setup:
                if (isChecked) {
                    states = 1;
                    mSetUp.setChecked(true);
                    mForbidden.setChecked(false);
                } else {
                    states = 0;
                    mSetUp.setChecked(false);
                    mForbidden.setChecked(true);
                }
                break;
            case R.id.pop_proxy_user_forbidden:
                if (isChecked) {
                    states = 0;
                    mSetUp.setChecked(false);
                    mForbidden.setChecked(true);
                } else {
                    states = 1;
                    mSetUp.setChecked(true);
                    mForbidden.setChecked(false);
                }
                break;
        }
    }

    static class ViewHolder {
        @Bind(R.id.item_proxy_code_code)
        TextView mItemProxyCodeCode;
        @Bind(R.id.item_proxy_code_type)
        TextView mItemProxyCodeType;
        @Bind(R.id.item_proxy_code_commison)
        TextView mItemProxyCodeCommison;
        @Bind(R.id.item_proxy_code_state)
        TextView mItemProxyCodeState;
        @Bind(R.id.item_proxy_code_change)
        Button mItemProxyCodeChange;
        @Bind(R.id.item_proxy_code_del)
        Button mItemProxyCodeDel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
