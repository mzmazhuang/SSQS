package com.dading.ssqs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ReportInfoBean;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by lenovo on 2017/9/11.
 */
public class PersonalReportAdapter extends RecyclerView.Adapter<PersonalReportAdapter.MyViewHolder> {
    private final Context           context;
    private final LayoutInflater    mInflater;
    private final ArrayList<ReportInfoBean> data;


    public PersonalReportAdapter (Context context, ArrayList<ReportInfoBean> map) {
        this.context = context;
        this.data = map;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.personal_report_item, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder (MyViewHolder holder, int position) {
        ReportInfoBean bean = data.get(position);

        holder.mPersonalReportTitle.setText(bean.getTitle());

        if (bean.getTitle().equals(Constent.RECHARGES)||bean.getTitle().equals(Constent.EXTRACTS)){
            String s = bean.getData( ) + context.getString(R.string.yuan);
            holder.mPersonalReportNum.setText(s);
        }else{
            String text = bean.getData( ) + context.getString(R.string.glod);
            holder.mPersonalReportNum.setText(text);
        }

    }

    @Override
    public int getItemCount ( ) {
        if (data != null)
            return data.size( );
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.personal_report_title)
        TextView mPersonalReportTitle;
        @Bind(R.id.personal_report_num)
        TextView mPersonalReportNum;

        public MyViewHolder (View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
