package com.dading.ssqs.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;

/**
 * Created by mazhuang on 2018/1/4.
 */

public class AvatarView extends LinearLayout {

    private CircleImageView avatar;

    public AvatarView(Context context) {
        super(context);

        setPadding(AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5), AndroidUtilities.INSTANCE.dp(5));
        setBackgroundResource(R.drawable.bg_avatar_border);

        avatar = new CircleImageView(context);
        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        avatar.setPadding(AndroidUtilities.INSTANCE.dp(1), AndroidUtilities.INSTANCE.dp(1), AndroidUtilities.INSTANCE.dp(1), AndroidUtilities.INSTANCE.dp(1));
        addView(avatar, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
    }

    public void setImageResource(int resId) {
        avatar.setImageResource(resId);
    }

    public void setImageResource(String url) {
        avatar.setImageResource(R.mipmap.ic_default_avatar);
        SSQSApplication.glide.load(url).asBitmap().listener(new RequestListener<String, Bitmap>() {
            @Override
            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                avatar.setImageResource(R.mipmap.ic_default_avatar);
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).into(avatar);
    }
}
