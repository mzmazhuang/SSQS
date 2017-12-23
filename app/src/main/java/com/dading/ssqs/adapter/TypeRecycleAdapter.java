package com.dading.ssqs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dading.ssqs.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/6/8.
 */
public class TypeRecycleAdapter extends RecyclerView.Adapter<TypeRecycleAdapter.MyViewHolder> implements View.OnClickListener {
    private final Context           context;
    private final ArrayList<String> data;
    private OnItemClickListener mOnItemClickListener = null;

    public TypeRecycleAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
        data.add("网银转账");
        data.add("ATM自动柜员机");
        data.add("ATM现金入款");
        data.add("银行柜台转账");
        data.add("手机银行转账");
        data.add("其他的方式");
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public String getRechargeType(int pos) {
        if (pos < data.size())
            return data.get(pos);
        else
            return null;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recharge_type_item, parent, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mRechargeTypeItemTv.setText(data.get(position));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.recharge_type_item_tv)
        TextView mRechargeTypeItemTv;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
