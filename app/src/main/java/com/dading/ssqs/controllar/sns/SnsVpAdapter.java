package com.dading.ssqs.controllar.sns;

import android.content.Context;
import android.view.View;

import com.dading.ssqs.adapter.BaseMePagerAdapter;

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
public class SnsVpAdapter extends BaseMePagerAdapter {
    private final Context           context;
    private final ArrayList<View>   data;
    private final ArrayList<String> listTitle;

    public SnsVpAdapter(Context context, ArrayList<View> list, ArrayList<String> listTitle) {
        this.context = context;
        this.data = list;
        this.listTitle = listTitle;
    }

    @Override
    protected View setView(int position) {
        View view = data.get(position);
      /*  view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });*/
        return view;
    }

    @Override
    protected int setSize() {
        if (data != null) {
            return data.size();
        }
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (listTitle != null && listTitle.size() > position) {
            return listTitle.get(position);
        } else {
            return super.getPageTitle(position);
        }
    }
}
