package com.dading.ssqs.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.adapter.MyNoteAdapter;
import com.dading.ssqs.controllar.tz.AnserNoteControllar;
import com.dading.ssqs.controllar.tz.UpLoadTZControllar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import tabindicator.TabIndicator;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/17 18:01
 * 描述	      我的帖子的数据
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class NoteRecordActivity extends BaseActivity {
    private static final String TAG = "NoteRecordActivity";
    @Bind(R.id.my_post_indicator)
    TabIndicator mMyPostIndicator;
    @Bind(R.id.my_post_vp)
    ViewPager mMyPostVp;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    private ArrayList<Fragment> mList;
    private ArrayList<String> mListTab;
    private int mPageID;

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        initData();
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_note_record;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.my_note));
        mList = new ArrayList<>();
        mListTab = new ArrayList<>();

        mListTab.add("发  帖");
        mListTab.add("跟  帖");

        mList.add(new UpLoadTZControllar());//界面1
        mList.add(new AnserNoteControllar());//界面2

        mMyPostVp.setAdapter(new MyNoteAdapter(getSupportFragmentManager(), mList, mListTab));
        mMyPostIndicator.setViewPager(mMyPostVp);

        mMyPostVp.setCurrentItem(mPageID);
    }

    @Override
    protected void initListener() {
        mMyPostVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPageID = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @OnClick({R.id.top_back})
    public void OnClik(View v) {
        finish();
    }
}
