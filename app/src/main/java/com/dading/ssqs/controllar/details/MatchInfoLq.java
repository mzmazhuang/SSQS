package com.dading.ssqs.controllar.details;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.LoginActivity;
import com.dading.ssqs.activity.MatchInfoActivity;
import com.dading.ssqs.adapter.MyLqAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.CommentSaveElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.HomeMessageBean;
import com.dading.ssqs.bean.MatchInfoLqBean;
import com.dading.ssqs.bean.MySingletonData;
import com.dading.ssqs.utils.ConcurrentDateUtil;
import com.dading.ssqs.utils.DateUtils;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;


import java.util.List;

import butterknife.ButterKnife;
import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshListView;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/14 17:49
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class MatchInfoLq implements TextWatcher, View.OnClickListener {
    private static final String TAG = "MatchInfoLq";
    private final int matchId;
    private Context context;
    public final View mRootView;
    private PullToRefreshListView mLvMessage;
    private EditText mEdtText;
    private TextView mSendBtn;
    private MyLqAdapter mAdapter;
    private boolean mIsLoading;
    private String mToken;
    private String mStr;
    private List<MatchInfoLqBean> mItems;
    private ListView mLv;
    private com.dading.ssqs.view.AutoVerticalScrollTextView mTalkBall;
    private int number = 0;
    private List<HomeMessageBean> mData;
    private String mDate;
    private String mTime;
    private boolean mB;


    public MatchInfoLq(Context context, int matchId) {
        this.context = context;
        this.matchId = matchId;
        mRootView = initView(context);
        initData();
        getData();
        initListener();
    }

    private void getData() {
        /**
         * 设置文字滑动
         * a)	请求地址：
         /v1.0/sysMessage/list
         b)	请求方式:
         get
         a)	请求地址：
         /v1.0/sysMessage/list
         b)	请求方式:
         get
         */

        SSQSApplication.apiClient(0).getSysMessageList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    List<HomeMessageBean> items = (List<HomeMessageBean>) result.getData();

                    if (items != null) {
                        processDataMessage(items);
                    }
                } else {
                    Logger.d(TAG, result.getMessage() + "中奖信息失败信息");
                }
            }
        });
    }

    private void processDataMessage(List<HomeMessageBean> bean) {
        if (bean != null) {
            mData = bean;
            mTalkBall.setText(mData.get(number).content);

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if (number < mData.size()) {
                        mTalkBall.setText(mData.get(number).content);
                        ++number;
                    } else {
                        number = 0;
                        mTalkBall.setText(mData.get(number).content);
                    }
                    UIUtils.postTaskDelay(this, 1500);
                }
            };
            UIUtils.postTaskDelay(r, 1500);
        }
    }

    private void initListener() {
        mLvMessage.setMode(PullToRefreshBase.Mode.BOTH);
        mLvMessage.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                /**
                 b)	请求方式:
                 Get
                 c)	请求参数说明：
                 id：比赛ID
                 auth_token：登陆后加入请求头
                 time:格式为yyyyMMddHHmms:默认时间1990 01 01 00 00 00
                 这个是第一条数据的时间
                 */

                SSQSApplication.apiClient(0).matchInfoMessageTime(mB, matchId, mTime, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        mLvMessage.onRefreshComplete();

                        if (result.isOk()) {
                            CcApiResult.ResultMatchInfoMessagePage page = (CcApiResult.ResultMatchInfoMessagePage) result.getData();

                            if (page != null && page.getItems() != null) {
                                mItems = page.getItems();
                                mAdapter.notifyDataSetChanged();
                                mLv.setStackFromBottom(false);
                            }
                        } else {
                            TmtUtils.midToast(context, result.getMessage(), 0);
                        }
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                /**
                 * a)	请求地址：
                 /v1.0/matchMessage/matchID/{matchID}/up/time/{time}
                 b)	请求方式:
                 Get
                 c)	请求参数说明：
                 id：比赛ID
                 auth_token：登陆后加入请求头
                 time:格式为yyyyMMddHHmmss:默认时间为当前时间
                 这个是最后一条数据的时间
                 */
                if (mItems != null && mItems.size() > 0) {
                    MatchInfoLqBean entity = mItems.get(mItems.size() - 1);
                    mDate = DateUtils.changeFormater(entity.createDate, "yyyy-MM-dd HH:mm:ss", "yyyyMMddHHmmss");
                } else {
                    mDate = DateUtils.getCurTime("yyyMMddHHmmss");
                }

                SSQSApplication.apiClient(0).matchInfoMessageUpTime(mB, matchId, mDate, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        mLvMessage.onRefreshComplete();

                        if (result.isOk()) {
                            CcApiResult.ResultMatchInfoMessagePage page = (CcApiResult.ResultMatchInfoMessagePage) result.getData();

                            if (page != null && page.getItems() != null) {
                                mItems.addAll(mItems.size(), page.getItems());
                                mAdapter.notifyDataSetChanged();
                                mLv.setStackFromBottom(false);
                            }
                        } else {
                            TmtUtils.midToast(context, result.getMessage(), 0);
                        }
                    }
                });
            }
        });
        mEdtText.addTextChangedListener(this);
        mSendBtn.setOnClickListener(this);
    }

    private void initData() {
        /**
         a)	请求地址：/v1.0/matchMessage/matchID/{matchID}
         b)	请求方式:Get
         c)	请求参数说明：id：比赛ID
         auth_token：登陆后加入请求头

         篮球
         a)	请求地址：
         /v1.0/matchMsgBall/matchID/{matchID}
         b)	请求方式:
         get
         c)	请求参数说明
         */

        SSQSApplication.apiClient(0).matchInfoMessage(mB, matchId, new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    CcApiResult.ResultMatchInfoMessagePage page = (CcApiResult.ResultMatchInfoMessagePage) result.getData();

                    if (page != null && page.getItems() != result) {
                        processData(page.getItems());
                    }
                } else {
                    TmtUtils.midToast(context, result.getMessage(), 0);
                }
            }
        });
    }

    private void processData(List<MatchInfoLqBean> lqBean) {

        mItems = lqBean;
        mAdapter = new MyLqAdapter(context, mItems);
        mLvMessage.setAdapter(mAdapter);
        mLv = mLvMessage.getRefreshableView();
        mLv.setStackFromBottom(false);
    }

    private View initView(Context context) {
        View view = View.inflate(context, R.layout.lv_tallball, null);

        mLvMessage = ButterKnife.findById(view, R.id.lq_message_lv);
        mEdtText = ButterKnife.findById(view, R.id.lq_edt_text);
        mSendBtn = ButterKnife.findById(view, R.id.lq_send_btn);
        mTalkBall = ButterKnife.findById(view, R.id.talk_ball_win_text);

        mIsLoading = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);
        mToken = UIUtils.getSputils().getString(Constent.TOKEN, "");
        mB = UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true);

        mTime = DateUtils.getCurTime("yyyyMMddHHmmss");


        return view;
    }

    @Override
    public void onClick(View v) {
        MySingletonData data = MySingletonData.getInstance(context);
        String formatGood = ConcurrentDateUtil.formatGood(data);

        mToken = UIUtils.getSputils().getString(Constent.TOKEN, null);
        mIsLoading = UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false);

        Logger.d(TAG, "返回token数据是------------------------------:" + mToken);
        Logger.d(TAG, formatGood);

        mStr = mEdtText.getText().toString();
        if (mIsLoading) {
            UIUtils.hideKeyBord(((MatchInfoActivity) context));
            if (mStr.equals("")) {
                TmtUtils.midToast(UIUtils.getContext(), "请输入您要发送的评论!", 0);
            } else {
                /**
                 * /v1.0/matchMessage/save
                 b)	请求方式:
                 post
                 c)	请求参数说明：
                 auth_token：登陆后加入请求头
                 content:评论内容，
                 matchID:比赛ID
                 * okHttp post同步请求表单提交
                 * @param actionUrl 接口地址
                 * @param paramsMap 请求参数
                 */
                CommentSaveElement element = new CommentSaveElement();
                element.setMatchID(String.valueOf(matchId));
                element.setContent(mStr);

                SSQSApplication.apiClient(0).commentSend(mB, element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            mEdtText.setText(null);
                            initData();
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(context, LoginActivity.class);
                                context.startActivity(intent);
                            } else {
                                TmtUtils.midToast(context, result.getMessage(), 0);
                            }
                        }
                    }
                });
            }
        } else {
            Intent intent = new Intent(context, LoginActivity.class);
            context.startActivity(intent);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (count > 0) {
            mSendBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rect_button_select));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().equals("")) {
            mSendBtn.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.rect_button));
        }
    }
}
