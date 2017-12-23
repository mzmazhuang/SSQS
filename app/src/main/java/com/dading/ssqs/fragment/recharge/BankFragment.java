package com.dading.ssqs.fragment.recharge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.adapter.newAdapter.BankAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.WXDFBean;

import java.util.List;

/**
 * Created by mazhuang on 2017/11/24.
 */

public class BankFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private BankAdapter adapter;

    public void setList(List<WXDFBean.InfoBean> list) {
        if (list != null) {
            adapter.setData(list);
        }
    }

    @SuppressLint("ValidFragment")
    public BankFragment(BankAdapter.OnRechargeClickListener listener) {
        this.listener = listener;
    }

    private BankAdapter.OnRechargeClickListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        context = getActivity();

        LinearLayout container = new LinearLayout(context);

        recyclerView = new RecyclerView(context);
        container.addView(recyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        adapter = new BankAdapter(context);
        adapter.setListener(listener);
        recyclerView.setAdapter(adapter);

        init();
        return container;
    }

    private void init() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
}
