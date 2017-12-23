package com.dading.ssqs.controllar.sns;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.dading.ssqs.activity.MyJsActivity;
import com.dading.ssqs.adapter.BaseMePagerAdapter;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.LogUtil;

import java.util.ArrayList;

/**
 * 创建者     ZCL
 * 创建时间   2017/3/9 10:47
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SnsVpHeadAdapter extends BaseMePagerAdapter {
    private static final String TAG = "SnsVpHeadAdapter";
    private final Context            context;
    private final ArrayList<View>    data;
    private final ArrayList<Integer> dataId;

    public SnsVpHeadAdapter(Context context, ArrayList<View> list, ArrayList<Integer> listId) {
        this.context = context;
        this.data = list;
        this.dataId = listId;
    }

    @Override
    protected View setView(final int position) {
        View view = data.get(position);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.util(TAG, "社区轮播数据id------:" + dataId.get(position));
                Intent intent = new Intent(context, MyJsActivity.class);
                intent.putExtra(Constent.NEWS_ID, dataId.get(position).toString());
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    protected int setSize() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }
}
