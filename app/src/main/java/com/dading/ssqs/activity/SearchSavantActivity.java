package com.dading.ssqs.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.MySavantGvAdapter;
import com.dading.ssqs.adapter.SavantLeveAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.ExpertInfoByNameElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.SavantLeveBean;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/20 16:49
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SearchSavantActivity extends BaseActivity {

    private static final String TAG = "SearchSavantActivity";
    @Bind(R.id.search_savant_history)
    GridView mSearchSavantHistory;
    @Bind(R.id.search_savant_search_text)
    EditText mSearchSavantSearchText;
    @Bind(R.id.search_savant_search_result)
    PullToRefreshListView mSearchSavantSearchResult;
    @Bind(R.id.search_savant_search_ly)
    LinearLayout mSearchSavantSearchLy;
    @Bind(R.id.search_savant_clear)
    ImageView mSearchSavantClear;

    @Bind(R.id.top_back)
    TextView mTopBack;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    private ArrayList<String> mListHistory;
    private MySavantGvAdapter mAdapter;
    private int mPage;
    private String mName;
    private int mTotalPage;
    private int mCurrPage;
    private List<SavantLeveBean> mItems;

    @OnClick({R.id.search_savant_search, R.id.search_savant_seniof, R.id.search_savant_hight, R.id.search_savant_mid
            , R.id.search_savant_low})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.search_savant_search:
                mName = mSearchSavantSearchText.getText().toString();
                break;
            case R.id.search_savant_seniof:
                mName = "资深专家";
                break;
            case R.id.search_savant_hight:
                mName = "高级专家";
                break;
            case R.id.search_savant_mid:
                mName = "中级专家";
                break;
            case R.id.search_savant_low:
                mName = "初级专家";
                break;
            default:
                break;
        }
        /**
         10.	按用户名字模糊搜索专家
         a)	请求地址：
         /v1.0/expert/search/name/page/{page}/count/{count}
         b)	请求方式:post
         c)	请求参数说明：
         name:用户名称
         page:当前页数
         count:条数
         */
        if (TextUtils.isEmpty(mName)) {
            TmtUtils.midToast(SearchSavantActivity.this, "请输入搜索条件!", 0);
            return;
        }
        mSearchSavantSearchText.setText(mName);
        Editable text = mSearchSavantSearchText.getText();
        Selection.setSelection(text, text.length());
        mListHistory.add(0, mName);
        mPage = 1;
        getData();
        mAdapter.notifyDataSetChanged();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mListHistory.size(); i++) {
            if (i < mListHistory.size() - 1) {
                sb.append(mListHistory.get(i)).append(",");
            } else {
                sb.append(mListHistory.get(i));
            }
        }
        UIUtils.getSputils().putString(Constent.SEACHER_HISTORY, sb.toString());
    }

    private void getData() {
        ExpertInfoByNameElement element = new ExpertInfoByNameElement();
        element.setUserName(mName);

        SSQSApplication.apiClient(classGuid).getInfoByName(element, mPage, 10, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultSavantLevePage page = (CcApiResult.ResultSavantLevePage) result.getData();

                    if (page != null) {
                        mTotalPage = page.getTotalCount();
                        mCurrPage = page.getTotalPage();
                        if (page.getItems() != null) {
                            mItems = page.getItems();
                            processLeveData(mItems);
                        }
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(SearchSavantActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processLeveData(List<SavantLeveBean> items) {
        mSearchSavantSearchLy.setVisibility(View.GONE);
        mSearchSavantSearchResult.setVisibility(View.VISIBLE);
        if (items.size() == 0) {
            TmtUtils.midToast(SearchSavantActivity.this, "没有符合条件的专家", 0);
        }
        mSearchSavantSearchResult.setAdapter(new SavantLeveAdapter(SearchSavantActivity.this, items));
    }

    @Override
    protected void initView() {
        mSearchSavantSearchResult.setVisibility(View.GONE);
        mSearchSavantSearchLy.setVisibility(View.VISIBLE);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_search_savant;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.savant_search));
        String s = UIUtils.getSputils().getString(Constent.SEACHER_HISTORY, "");
        if (!TextUtils.isEmpty(s)) {
            String[] split = s.split(",");
            List<String> list = Arrays.asList(split);
            mListHistory = new ArrayList<>(list);
        } else {
            mListHistory = new ArrayList<>();
        }
        mAdapter = new MySavantGvAdapter(this, mListHistory);
        mSearchSavantHistory.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        mSearchSavantSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() <= 0) {
                    mSearchSavantSearchResult.setVisibility(View.GONE);
                    mSearchSavantSearchLy.setVisibility(View.VISIBLE);
                }
            }
        });
        mSearchSavantSearchResult.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        mSearchSavantSearchResult.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (mCurrPage >= mTotalPage) {
                    TmtUtils.midToast(SearchSavantActivity.this, "没有更多数据!", 0);
                    mSearchSavantSearchResult.onRefreshComplete();
                    return;
                }
                mPage++;

                ExpertInfoByNameElement element = new ExpertInfoByNameElement();
                element.setUserName(mName);

                SSQSApplication.apiClient(classGuid).getInfoByName(element, mPage, 10, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            CcApiResult.ResultSavantLevePage page = (CcApiResult.ResultSavantLevePage) result.getData();

                            if (page != null) {
                                mTotalPage = page.getTotalCount();
                                mCurrPage = page.getTotalPage();

                                if (page.getItems() != null) {
                                    mItems.addAll(page.getItems());
                                    processLeveData(mItems);
                                    mSearchSavantSearchResult.onRefreshComplete();
                                }
                            }
                        } else {
                            LogUtil.util(TAG, result.getMessage() + "按级别搜索专家失败信息");
                        }
                    }
                });
            }
        });
        mTopBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearchSavantClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListHistory.clear();
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}