package com.dading.ssqs.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.bean.SmoothImageView;

/**
 * 创建者     ZCL
 * 创建时间   2017/4/12 15:28
 * 描述	      ${显示大图}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SpaceImageDetailActivity extends BaseActivity {
    private int    mLocationX;
    private int    mLocationY;
    private int    mWidth;
    private int    mHeight;
    private String mUrl;

    @Override
    protected int setLayoutId ( ) {
        return R.layout.activity_space_img;
    }

    @Override
    protected void initData ( ) {
        Intent intent = getIntent( );
        mUrl = intent.getStringExtra("imageUrl");
        mLocationX = intent.getIntExtra("locationX", 0);
        mLocationY = intent.getIntExtra("locationY", 0);
        mWidth = intent.getIntExtra("width", 0);
        mHeight = intent.getIntExtra("height", 0);

        SmoothImageView imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn( );
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setContentView(imageView);
        SSQSApplication.glide.load(mUrl).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick (View v) {
                finish();
            }
        });
    }

}
