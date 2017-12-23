package com.dading.ssqs.components;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.activity.AllMatchActivity;
import com.dading.ssqs.activity.HotMatchActivity;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.GusessChoiceBean;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/12
 * 猜球-选择联赛
 */

public class SelectMatchDialog extends Dialog {

    private final Context mContext;
    private final Activity activity;
    private LinearLayout bottomLayout;
    private TextView tvTitle;
    private TextView allTextView;
    private TextView allNoTextView;
    private TextView okView;

    private List<String> mTitleList = new ArrayList<>();
    private List<View> mFragments = new ArrayList<>();

    private HotMatchActivity hotMatchActivity;
    private AllMatchActivity allMatchActivity;

    private OnSubmitListener listener;

    private int totalSize;

    public void setListener(OnSubmitListener listener) {
        this.listener = listener;
    }

    public interface OnSubmitListener {
        void onSubmit(List<String> list, boolean isAll);
    }

    public SelectMatchDialog(Context context) {
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
        bottomLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(bottomLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        View maskView = new View(mContext);
        maskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectMatchDialog.this.dismiss();
            }
        });
        maskView.setBackgroundColor(0x00000000);
        bottomLayout.addView(maskView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 200));

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

        allMatchActivity = new AllMatchActivity(mContext);
        mFragments.add(allMatchActivity);
        hotMatchActivity = new HotMatchActivity(mContext);
        mFragments.add(hotMatchActivity);

        RelativeLayout layout = new RelativeLayout(mContext);
        layout.setBackgroundColor(Color.WHITE);
        bottomLayout.addView(layout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        RelativeLayout operationLayout = new RelativeLayout(mContext);
        operationLayout.setId(R.id.select_match_operation);
        operationLayout.setBackgroundColor(0xFFF5F4F9);
        operationLayout.setPadding(AndroidUtilities.dp(10), 0, AndroidUtilities.dp(10), 0);
        layout.addView(operationLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 40, RelativeLayout.ALIGN_PARENT_BOTTOM));

        LinearLayout textLayout = new LinearLayout(mContext);
        textLayout.setOrientation(LinearLayout.HORIZONTAL);
        operationLayout.addView(textLayout, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_VERTICAL));

        allTextView = new TextView(mContext);
        allTextView.setGravity(Gravity.CENTER);
        allTextView.setTextSize(14);
        allTextView.setText("全选");
        allTextView.setTextColor(Color.WHITE);
        allTextView.setBackgroundResource(R.drawable.bg_all_select);
        allTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check(true);
            }
        });
        textLayout.addView(allTextView, LayoutHelper.createLinear(65, 30));

        allNoTextView = new TextView(mContext);
        allNoTextView.setPadding(AndroidUtilities.dp(10), 0, AndroidUtilities.dp(10), 0);
        allNoTextView.setTextSize(14);
        allNoTextView.setGravity(Gravity.CENTER);
        allNoTextView.setText("全不选");
        allNoTextView.setTextColor(0xFFFF9600);
        allNoTextView.setBackgroundResource(R.drawable.bg_no_all_select);
        allNoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check(false);
            }
        });
        textLayout.addView(allNoTextView, LayoutHelper.createLinear(65, 30, 20, 0, 0, 0));

        okView = new TextView(mContext);
        okView.setTextSize(14);
        okView.setText("确定");
        okView.setTextColor(0xFFFF9600);
        okView.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        okView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    List<String> list = getSelectedData();

                    boolean isAll = list.size() == totalSize;

                    listener.onSubmit(list, isAll);
                }
                dismiss();
            }
        });
        operationLayout.addView(okView, LayoutHelper.createRelative(80, 40, RelativeLayout.ALIGN_PARENT_RIGHT));

        LinearLayout infoLayout = new LinearLayout(mContext);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout.LayoutParams infoLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT);
        infoLP.addRule(RelativeLayout.ABOVE, operationLayout.getId());
        layout.addView(infoLayout, infoLP);

        TabLayout tabLayout = new TabLayout(mContext);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setSelectedTabIndicatorColor(0xFF009BDB);
        tabLayout.setTabTextColors(0xFF222222, 0xFF009BDB);
        infoLayout.addView(tabLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40));

        mTitleList.add("全部联赛");
        mTitleList.add("热门联赛");

        for (int i = 0; i < mTitleList.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(i)));
        }

        ViewPager viewPager = new ViewPager(mContext);
        viewPager.setId(R.id.select_match_viewPager);
        viewPager.setLayoutParams(LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        viewPager.setAdapter(new MatchPagerAdapter());
        infoLayout.addView(viewPager);

        tabLayout.setupWithViewPager(viewPager);
    }

    public void show(List<GusessChoiceBean> list, List<GusessChoiceBean> hotList, String title) {
        this.show();

        tvTitle.setText(title);

        allMatchActivity.setData(list);
        hotMatchActivity.setData(hotList);

        check(true);
    }

    private void check(boolean check) {
        if (check) {
            allTextView.setTextColor(Color.WHITE);
            allTextView.setBackgroundResource(R.drawable.bg_all_select);

            allNoTextView.setTextColor(0xFFFF9600);
            allNoTextView.setBackgroundResource(R.drawable.bg_no_all_select);

        } else {
            allNoTextView.setTextColor(Color.WHITE);
            allNoTextView.setBackgroundResource(R.drawable.bg_all_select);

            allTextView.setTextColor(0xFFFF9600);
            allTextView.setBackgroundResource(R.drawable.bg_no_all_select);
        }
        allMatchActivity.isAll(check);
        hotMatchActivity.isAll(check);
    }

    private List<String> getSelectedData() {
        List<String> list = new ArrayList<>();

        List<GusessChoiceBean> allData = new ArrayList<>();
        List<GusessChoiceBean> hotData = new ArrayList<>();

        allData.addAll(allMatchActivity.getData());
        hotData.addAll(hotMatchActivity.getData());

        int allSize = 0;
        int hotSize = 0;

        for (int i = 0; i < allData.size(); i++) {
            List<GusessChoiceBean.FilterEntity> allFilterList = allData.get(i).filter;
            allSize += allFilterList.size();
            for (int j = 0; j < allFilterList.size(); j++) {
                if (allFilterList.get(j).checked) {
                    list.add(allFilterList.get(j).id + "");
                }
            }
        }

        for (int i = 0; i < hotData.size(); i++) {
            List<GusessChoiceBean.FilterEntity> hotFilterList = hotData.get(i).filter;
            hotSize += hotFilterList.size();
            for (int j = 0; j < hotFilterList.size(); j++) {
                if (hotFilterList.get(j).checked) {
                    list.add(hotFilterList.get(j).id + "");
                }
            }
        }

        totalSize = allSize + hotSize;
        return list;
    }

    class MatchPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mFragments.get(position));
            if ("全部联赛".equals(mTitleList.get(position))) {
                ((AllMatchActivity) mFragments.get(position)).init();
            } else if ("热门联赛".equals(mTitleList.get(position))) {
                ((HotMatchActivity) mFragments.get(position)).init();
            }
            return mFragments.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewGroup view = (ViewGroup) object;
            container.removeView(view);
        }
    }
}
