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
public class PageLoadMoreFooterView extends SwipeLoadMoreFooterLayout {

    private Context mContext;
    private TextView tvLoadMore;
    private ImageView ivSuccess;
    private ImageView loading;

    private Animation loadingAnim;

    private int mFooterHeight;//高度

    public PageLoadMoreFooterView(Context context) {
        this(context, null);
    }

    public PageLoadMoreFooterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mFooterHeight = getResources().getDimensionPixelOffset(R.dimen.load_more_footer_height_twitter);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        RelativeLayout layout = new RelativeLayout(mContext);
        layout.setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40));
        addView(layout);

        tvLoadMore = new TextView(mContext);
        tvLoadMore.setGravity(Gravity.CENTER);
        tvLoadMore.setId(R.id.page_loadmore_text);
        tvLoadMore.setTextColor(0xFFB8B3B4);
        tvLoadMore.setTextSize(14);
        RelativeLayout.LayoutParams refreLP = LayoutHelper.createRelative(130, LayoutHelper.WRAP_CONTENT);
        refreLP.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(tvLoadMore, refreLP);

        ivSuccess = new ImageView(mContext);
        ivSuccess.setBackgroundResource(R.mipmap.ic_page_success);
        RelativeLayout.LayoutParams image2LP = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 20, 0);
        image2LP.addRule(RelativeLayout.CENTER_VERTICAL);
        image2LP.addRule(RelativeLayout.LEFT_OF, tvLoadMore.getId());
        layout.addView(ivSuccess, image2LP);

        loading = new ImageView(mContext);
        loading.setVisibility(View.GONE);
        loading.setImageResource(R.mipmap.ic_page_loading);
        RelativeLayout.LayoutParams loadingLP = LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 0, 0, 20, 0);
        loadingLP.addRule(RelativeLayout.CENTER_IN_PARENT);
        loadingLP.addRule(RelativeLayout.LEFT_OF, tvLoadMore.getId());

        layout.addView(loading, loadingLP);
        loadingAnim = AnimationUtils.loadAnimation(mContext, R.anim.loading);
        loadingAnim.setDuration(800);
    }

    @Override
    public void onLoadMore() {
        tvLoadMore.setText(LocaleController.getString(R.string.loading_more));
        loading.setVisibility(VISIBLE);
        loading.startAnimation(loadingAnim);
    }

    @Override
    public void onPrepare() {
        ivSuccess.setVisibility(GONE);
    }

    @Override
    public void onMove(int yScrolled, boolean isComplete, boolean automatic) {
        if (!isComplete) {
            ivSuccess.setVisibility(GONE);
            loading.clearAnimation();
            loading.setVisibility(GONE);
            tvLoadMore.setText(LocaleController.getString(R.string.next_page));
        }
    }

    @Override
    public void onRelease() {
    }

    @Override
    public void onComplete() {
        loading.clearAnimation();
        tvLoadMore.setText(LocaleController.getString(R.string.done));
        loading.setVisibility(GONE);
        ivSuccess.setVisibility(VISIBLE);
    }

    @Override
    public void onReset() {
        ivSuccess.setVisibility(GONE);
        loading.clearAnimation();
        loading.setVisibility(View.GONE);
    }
}
