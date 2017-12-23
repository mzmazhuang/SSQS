package com.dading.ssqs.components;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.newAdapter.PageDialogAdapter;
import com.dading.ssqs.adapter.newAdapter.ResultSelectTimeDialogAdapter;
import com.dading.ssqs.base.LayoutHelper;

import java.io.Serializable;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/12.
 * 选择时间
 */

public class ResultSelectTimeDialog extends Dialog {

    private final Context mContext;
    private final Activity activity;
    private LinearLayout bottomLayout;
    private TextView tvTitle;

    private ResultSelectTimeDialogAdapter adapter;

    public ResultSelectTimeDialog(Context context) {
        super(context, R.style.ProgressDialog);

        activity = (Activity) context;
        mContext = context;

        LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        onCreateView(container);

        setContentView(container);

        Window window = getWindow();
        assert window != null;
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = LayoutHelper.MATCH_PARENT;
        params.height = LayoutHelper.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);

        setCanceledOnTouchOutside(true);
    }

    private void onCreateView(LinearLayout container) {
        bottomLayout = new LinearLayout(mContext);
        bottomLayout.setBackgroundColor(Color.WHITE);
        bottomLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(bottomLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 250));

        RelativeLayout topLayout = new RelativeLayout(mContext);
        topLayout.setBackgroundColor(0xFFF5F4F9);
        bottomLayout.addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 35));

        tvTitle = new TextView(mContext);
        tvTitle.setTextColor(0xFF323232);
        tvTitle.setTextSize(14);
        topLayout.addView(tvTitle, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 12, 0, 0, 0, RelativeLayout.CENTER_VERTICAL));

        ImageView closeView = new ImageView(mContext);
        closeView.setScaleType(ImageView.ScaleType.CENTER);
        closeView.setImageResource(R.mipmap.ic_dialog_close);
        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        RelativeLayout.LayoutParams closeLP = LayoutHelper.createRelative(30, 30, 0, 0, 12, 0);
        closeLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        closeLP.addRule(RelativeLayout.CENTER_VERTICAL);
        topLayout.addView(closeView, closeLP);

        View view = new View(mContext);
        view.setBackgroundColor(0xFFEDEDED);
        bottomLayout.addView(view, new LinearLayout.LayoutParams(LayoutHelper.MATCH_PARENT, 1));

        RecyclerView recyclerView = new RecyclerView(mContext);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        bottomLayout.addView(recyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        adapter = new ResultSelectTimeDialogAdapter(mContext);
        recyclerView.setAdapter(adapter);
    }

    public void setItemListener(ResultSelectTimeDialogAdapter.OnClickListener listener) {
        adapter.setListener(listener);
    }

    public void show(List<ResultSelectTimeBean> list, String title, int select) {
        this.show();

        tvTitle.setText(title);

        adapter.setSelect(select);
        adapter.setList(list);
    }

    public static class ResultSelectTimeBean implements Serializable {
        private static final long serialVersionUID = -6962709282914142905L;

        public ResultSelectTimeBean(String time, int day) {
            setTime(time);
            setDay(day);
        }

        private String time;
        private int day;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }
    }
}
