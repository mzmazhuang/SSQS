package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.TransferBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by lenovo on 2017/8/18.
 */
public class TransferTypeAdapter extends BaseAdapter implements ListAdapter {

    private final ArrayList<TransferBean> mData;
    private final Context                 context;
    private final ArrayList<String>       mList;
    private       String                  mTitle;
    boolean isFrist;

    public TransferTypeAdapter (Context context) {
        this.context = context;
        mData = new ArrayList<>( );
        mList = new ArrayList<>( );
        mList.add("ATM自动柜员机");
        mList.add("ATM现金入款");
        mList.add("银行柜台转账");
        mList.add("手机银行转账");
        mList.add("其他的方式");
        for (String s : mList) {
            TransferBean bean = new TransferBean(s, false);
            mData.add(bean);
        }
        isFrist = true;

    }

    @Override
    public int getCount ( ) {
        if (mData != null) {
            return mData.size( );
        }
        return 0;
    }

    @Override
    public Object getItem (int position) {
        return null;
    }

    @Override
    public long getItemId (int position) {
        return 0;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_transfer_type, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag( );
        }

        final TransferBean bean = mData.get(position);
        if (isFrist && position == 0) {
            bean.setChecked(true);
            holder.mTransferTypeCb.setChecked(bean.isChecked( ));
            isFrist = false;
        } else {
            holder.mTransferTypeCb.setChecked(bean.isChecked( ));
        }
        holder.mTransferTypeTxt.setText(bean.getTitle( ));

        holder.mTransferTypeCb.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick (View v) {
                if (bean.isChecked( )) {
                    bean.setChecked(false);
                    mTitle = null;
                } else {
                    for (TransferBean b : mData) {
                        b.setChecked(false);
                    }
                    bean.setChecked(true);
                    mTitle = bean.getTitle( );
                }
                notifyDataSetChanged( );
            }
        });
        return convertView;
    }

    public String getCheckTitle ( ) {
        return mTitle;
    }

    static class ViewHolder {
        @Bind(R.id.transfer_type_cb)
        CheckBox mTransferTypeCb;
        @Bind(R.id.transfer_type_txt)
        TextView mTransferTypeTxt;

        ViewHolder (View view) {
            ButterKnife.bind(this, view);
        }
    }
}
