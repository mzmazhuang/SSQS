package com.dading.ssqs.fragment.recharge;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.base.LayoutHelper;

/**
 * Created by mazhuang on 2017/11/24.
 */

public class OnLineFragment extends Fragment {

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        context = getActivity();

        LinearLayout container = new LinearLayout(context);

        TextView tvTest = new TextView(context);
        tvTest.setText("暂时不支持");
        tvTest.setTextColor(0xFFFC3232);
        tvTest.setGravity(Gravity.CENTER);
        container.addView(tvTest, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        return container;
    }
}
