package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.base.LayoutHelper;

/**
 * Created by mazhuang on 2018/1/15.
 */

public class PreferentialActivitiesCell extends LinearLayout {

    private ImageView iconView;
    private TextView tvTitle;
    private TextView tvTime;

    public PreferentialActivitiesCell(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);

        iconView = new ImageView(context);
        iconView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(iconView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 150));

        RelativeLayout bottomLayout = new RelativeLayout(context);
        addView(bottomLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 50));

        ImageView nextView = new ImageView(context);
        nextView.setId(R.id.perfer_next);
        nextView.setImageResource(R.mipmap.ic_activity_next);
        RelativeLayout.LayoutParams nextLP = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 12, 0);
        nextLP.addRule(RelativeLayout.CENTER_VERTICAL);
        nextLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        bottomLayout.addView(nextView, nextLP);

        LinearLayout leftLayout = new LinearLayout(context);
        leftLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams leftLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 15, 0, 0, 0);
        leftLP.addRule(RelativeLayout.LEFT_OF, nextView.getId());
        leftLP.addRule(RelativeLayout.CENTER_VERTICAL);
        bottomLayout.addView(leftLayout, leftLP);

        tvTitle = new TextView(context);
        tvTitle.setTextColor(0xFF323232);
        tvTitle.setTextSize(16);
        tvTitle.setSingleLine();
        tvTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        leftLayout.addView(tvTitle, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 2, 0, 0));

        tvTime = new TextView(context);
        tvTime.setTextColor(0xFF999999);
        tvTime.setTextSize(13);
        tvTime.setSingleLine();
        tvTime.setEllipsize(TextUtils.TruncateAt.END);
        leftLayout.addView(tvTime, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 2, 0, 0));

        View view = new View(context);
        view.setBackgroundColor(0xFFF5F4F9);
        addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8));
    }

    public void setIconResource(String url) {
        iconView.setBackgroundColor(0xFFF7F7F7);
        SSQSApplication.glide.load(url).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                iconView.setBackgroundColor(0xFFF7F7F7);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                iconView.setBackgroundColor(0);
                return false;
            }
        }).into(iconView);
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void setTime(String time) {
        tvTime.setText(time);
    }
}
