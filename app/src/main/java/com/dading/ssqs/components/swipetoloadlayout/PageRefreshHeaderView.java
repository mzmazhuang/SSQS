package com.dading.ssqs.components.swipetoloadlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.LocaleController;
import com.dading.ssqs.R;
import com.dading.ssqs.base.LayoutHelper;

/**
 * Created by mazhuang on 2016/8/17.
 */
public class PageRefreshHeaderView extends SwipeRefreshHeaderLayout {

    private Context mContext;

    private ImageView ivArrow;
    private ImageView ivSuccess;
    private TextView tvRefresh;
    private ImageView loading;

    private Animation rotateUp;

    private Animation rotateDown;

    private Animation loadingAnim;

    private int mHeaderHeight;//高度

    private boolean rotated = false;

    public PageRefreshHeaderView(Context context) {
        this(context, null);
    }

    public PageRefreshHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageRefreshHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mHeaderHeight = getResources().getDimensionPixelOffset(R.dimen.refresh_header_height_twitter);
        setGravity(Gravity.CENTER);

        rotateUp = AnimationUtils.loadAnimation(context, R.anim.page_rotate_up);
        rotateDown = AnimationUtils.loadAnimation(context, R.anim.page_rotate_down);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        RelativeLayout layout = new RelativeLayout(mContext);
        layout.setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 80));
        addView(layout);

        tvRefresh = new TextView(mContext);
        tvRefresh.setGravity(Gravity.CENTER);
        tvRefresh.setId(R.id.page_refresh_text);
        tvRefresh.setTextColor(0xFFB8B3B4);
        tvRefresh.setTextSize(14);
        RelativeLayout.LayoutParams refreLP = LayoutHelper.createRelative(130, LayoutHelper.WRAP_CONTENT);
        refreLP.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(tvRefresh, refreLP);

        ivArrow = new ImageView(mContext);
        ivArrow.setBackgroundResource(R.mipmap.ic_page_pull_arrow);
        RelativeLayout.LayoutParams image1LP = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 20, 0);
        image1LP.addRule(RelativeLayout.CENTER_VERTICAL);
        image1LP.addRule(RelativeLayout.LEFT_OF, tvRefresh.getId());

        layout.addView(ivArrow, image1LP);

        ivSuccess = new ImageView(mContext);
        ivSuccess.setBackgroundResource(R.mipmap.ic_page_success);
        RelativeLayout.LayoutParams image2LP = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 20, 0);
        image2LP.addRule(RelativeLayout.CENTER_VERTICAL);
        image2LP.addRule(RelativeLayout.LEFT_OF, tvRefresh.getId());

        layout.addView(ivSuccess, image2LP);

        loading = new ImageView(mContext);
        loading.setVisibility(View.GONE);
        loading.setImageResource(R.mipmap.ic_page_loading);
        RelativeLayout.LayoutParams loadingLP = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 20, 0);
        loadingLP.addRule(RelativeLayout.CENTER_IN_PARENT);
        loadingLP.addRule(RelativeLayout.LEFT_OF, tvRefresh.getId());

        layout.addView(loading, loadingLP);
        loadingAnim = AnimationUtils.loadAnimation(mContext, R.anim.loading);
        loadingAnim.setDuration(800);
    }

    @Override
    public void onRefresh() {
        tvRefresh.setText("刷新中...");
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        loading.setVisibility(View.VISIBLE);
        loading.startAnimation(loadingAnim);
    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            ivArrow.setVisibility(VISIBLE);
            ivSuccess.setVisibility(GONE);
            loading.clearAnimation();
            loading.setVisibility(View.GONE);
            if (yScrolled > mHeaderHeight) {
                tvRefresh.setText("松开刷新");
                if (!rotated) {
                    ivArrow.clearAnimation();
                    ivArrow.startAnimation(rotateUp);
                    rotated = true;
                }
            } else if (yScrolled < mHeaderHeight) {
                if (rotated) {
                    ivArrow.clearAnimation();
                    ivArrow.startAnimation(rotateDown);
                    rotated = false;
                }
                tvRefresh.setText("下拉刷新");
            }
        }
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void onComplete() {
        rotated = false;
        tvRefresh.setText(LocaleController.getString(R.string.done));
        ivSuccess.setVisibility(VISIBLE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        loading.clearAnimation();
        loading.setVisibility(View.GONE);
    }

    @Override
    public void onReset() {
        rotated = false;
        ivSuccess.setVisibility(GONE);
        ivArrow.clearAnimation();
        ivArrow.setVisibility(GONE);
        loading.clearAnimation();
        loading.setVisibility(View.GONE);
    }
}
