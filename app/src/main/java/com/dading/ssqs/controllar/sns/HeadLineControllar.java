package com.dading.ssqs.controllar.sns;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.SnsBean;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import com.dading.ssqs.components.pulltorefresh.PullToRefreshBase;
import com.dading.ssqs.components.pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2017/3/9 10:17
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HeadLineControllar {

    private static final String TAG = "HeadLineControllar";
    private Context context;
    public final View mRootView;
    private ViewPager mVp;
    private PullToRefreshListView mLv;
    private AutoScrollTask mAutoScrollTask;
    private int mPage;
    public HeadLineHeadAdapter mAdapter;
    private View mHeadView;

    private int totalCount;

    public HeadLineControllar(Context context) {
        this.context = context;
        mRootView = initView();
        initData();
        initListener();
    }

    private View initView() {
        View view = View.inflate(context, R.layout.head_line, null);
        mLv = (PullToRefreshListView) view.findViewById(R.id.head_line_lv);
      /* View footView = View.inflate(context, R.layout.bottom_foot, null);
        mLoading = (LinearLayout) footView.findViewById(R.id.sns_bottom_loading);
        mLoadMore = (TextView) footView.findViewById(R.id.sns_bottom_load_more);
        //mVp = (ViewPager) view.findViewById(R.id.head_line_vp);*/
        mHeadView = View.inflate(context, R.layout.sns_head, null);
        mVp = (ViewPager) mHeadView.findViewById(R.id.head_line_vp);
        mLv.getRefreshableView().addHeaderView(mHeadView);

        mAdapter = new HeadLineHeadAdapter(context);
        mLv.setAdapter(mAdapter);
        return view;
    }

    private void initData() {
        mAutoScrollTask = new AutoScrollTask();
        mAutoScrollTask.start();
        mLv.setMode(PullToRefreshBase.Mode.BOTH);
        getData();

    }

    private void getData() {
        mPage = 1;

        SSQSApplication.apiClient(0).getFiveDataList(1, mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultSnsPage page = (CcApiResult.ResultSnsPage) result.getData();

                    if (page != null) {
                        totalCount = page.getTotalCount();

                        if (page.getWrites() != null && page.getTopPic() != null) {
                            processData(page.getHasTop(), page.getTopPic(), page.getWrites());
                        }
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "头条失败信息");
                }
            }
        });
    }

    private void processData(int hasTop, List<SnsBean.TopPicEntity> topPic, List<SnsBean.WritesEntity> writes) {
        ArrayList<View> listiv = new ArrayList<>();
        ArrayList<Integer> listId = new ArrayList<>();

        if (hasTop == 1 && topPic.size() > 0)
            for (SnsBean.TopPicEntity entity : topPic) {
                ImageView target = new ImageView(context);

                Logger.d(TAG, "社区轮播返回数据id---------:" + entity.id + "---" + entity.smallImage);

                SSQSApplication.glide.load(entity.smallImage).error(R.mipmap.fail).centerCrop().into(target);

                listiv.add(target);
                listId.add(entity.id);
            }
        mVp.setAdapter(new SnsVpHeadAdapter(context, listiv, listId));
        mAdapter.setList(writes);
    }

    private void initListener() {
        mLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData();
                UIUtils.postTaskDelay(new Runnable() {
                    @Override
                    public void run() {
                        mLv.onRefreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mPage <= totalCount) {
                    loadMore();
                }
                UIUtils.postTaskDelay(new Runnable() {
                    @Override
                    public void run() {
                        mLv.onRefreshComplete();
                        ToastUtils.midToast(context, "没有更多数据!", 0);
                    }
                }, 1000);
            }
        });
        // 设置切换效果
        mVp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.i(TAG, "ACTION_DOWN");
                        mAutoScrollTask.stop();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.i(TAG, "ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i(TAG, "ACTION_UP");
                        mAutoScrollTask.start();
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        Log.i(TAG, "ACTION_CANCEL");
                        mAutoScrollTask.stop();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    private void loadMore() {
        mPage++;

        SSQSApplication.apiClient(0).getFiveDataList(1, mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultSnsPage page = (CcApiResult.ResultSnsPage) result.getData();

                    if (page != null) {

                        if (page.getWrites() != null) {
                            mAdapter.addList(page.getWrites());
                        }
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "头条失败信息");
                }
            }
        });
    }

    public class AutoScrollTask implements Runnable {
        /**
         * 开始轮播
         * 2016-4-8 下午2:42:26
         */
        public void start() {
            //移除置空
            UIUtils.postTaskDelay(this, 5000);
        }

        /**
         * 停止轮播
         * 2016-4-8 下午2:42:32
         */
        public void stop() {
            UIUtils.getMainThreadHandler().removeCallbacks(this);
        }

        @Override
        public void run() {
            // 完成viewpager的切换
            int currentItem = mVp.getCurrentItem();
            if (mVp.getAdapter() != null && currentItem == mVp.getAdapter().getCount() - 1) {
                currentItem = 0;
            } else {
                currentItem++;
            }
            //切换
            mVp.setCurrentItem(currentItem);
            //再次调用
            start();
        }
    }
}
