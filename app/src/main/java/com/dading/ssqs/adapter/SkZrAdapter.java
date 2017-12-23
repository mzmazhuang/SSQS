package com.dading.ssqs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.SKZRBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/27 14:33
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SkZrAdapter extends BaseAdapter implements ListAdapter {
    private final Context                                  context;
    private final List<SKZRBean.HPlayersEntity> dataH;
    private final List<SKZRBean.APlayersEntity> dataA;

    public SkZrAdapter(Context context, List<SKZRBean.HPlayersEntity> hPlayers, List<SKZRBean.APlayersEntity> aPlayers) {
        this.context = context;
        this.dataH = hPlayers;
        this.dataA = aPlayers;
    }

    @Override
    public int getCount() {
        if (dataA != null) {
            return dataA.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoder hoder =null;
        if (convertView ==null){
            convertView = View.inflate(context, R.layout.sk_zr_item,null);
            hoder = new ViewHoder(convertView);
            convertView.setTag(hoder);
        }else{
            hoder = (ViewHoder) convertView.getTag();
        }

        hoder.mainPlayerName.setText(dataH.get(position).name);
        String h = dataH.get(position).num + "";
        hoder.mainShirtNumber.setText(h);
        hoder.secondPlayerName.setText(dataA.get(position).name);
        String a = dataA.get(position).num + "";
        hoder.secondShirtNumber.setText(a);


        return convertView;
    }
    class ViewHoder{
        @Bind(R.id.sk_zr_main_item_shirt)
        TextView mainShirtNumber;
        @Bind(R.id.sk_zr_main_item_player_name)
        TextView mainPlayerName;
        @Bind(R.id.sk_zr_second_item_shirt)
        TextView secondShirtNumber;
        @Bind(R.id.sk_zr_second_item_player_name)
        TextView secondPlayerName;

        public ViewHoder(View v){
            ButterKnife.bind(this, v);
        }
    }
}
