package com.dading.ssqs.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.CommentsLvAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.ArticleSaveElement;
import com.dading.ssqs.apis.elements.FouceArticleElement;
import com.dading.ssqs.apis.elements.SendArticleCommentElement;
import com.dading.ssqs.bean.CommentsBean;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.NewInfoBean;
import com.dading.ssqs.bean.ShareBean;
import com.dading.ssqs.onekeyshare.OnekeyShare;
import com.dading.ssqs.utils.ListScrollUtil;
import com.dading.ssqs.utils.LogUtil;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.view.GlideCircleTransform;
import com.dading.ssqs.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/15 10:27
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HomeViewPagerActivity extends BaseActivity implements View.OnClickListener {

    private final String TAG = "HomeViewPagerActivity";
    @Bind(R.id.match_thing_info_type_icon)
    ImageView mMatchThingInfoTypeIcon;
    @Bind(R.id.match_thing_info_type_text)
    TextView mMatchThingInfoTypeText;
    @Bind(R.id.match_thing_info_savantphoto)
    ImageView mMatchThingInfoSavantphoto;
    @Bind(R.id.match_thing_info_savantname)
    TextView mMatchThingInfoSavantName;
    @Bind(R.id.match_thing_info_host)
    ImageView mMatchThingInfoHost;//楼主默认图标
    @Bind(R.id.match_thing_info_time)
    TextView mMatchThingInfoTime;
    @Bind(R.id.match_thing_info_hot_tiezi)
    ImageView mMatchThingInfoHotTiezi;
    @Bind(R.id.home_match_thing_info_context_title)
    TextView mHomeMatchThingInfoContextTitle;
    @Bind(R.id.home_match_thing_info_context)
    TextView mHomeMatchThingInfoContext;
    @Bind(R.id.home_match_thing_info_iv_contrllar)
    LinearLayout mHomeMatchThingInfoIvContrllar;
    @Bind(R.id.home_match_thing_info_context_good_iv)
    ImageView mHomeMatchThingInfoContextGoodIv;
    @Bind(R.id.home_match_thing_info_comments_lv)
    ListView mHomeMatchThingInfoCommentsLv;
    @Bind(R.id.home_match_thing_info_context_good_text)
    TextView mHomeMatchThingInfoContextGoodText;
    @Bind(R.id.home_match_thing_info_publish)
    ImageView mHomeMatchThingInfoPublish;
    @Bind(R.id.home_match_thing_info_publish_content)
    EditText mHomeMatchThingInfoPublishContent;
    @Bind(R.id.home_match_thing_info_top_ly)
    RelativeLayout mToply;
    @Bind(R.id.answer_num)
    TextView mAnswerNum;
    @Bind(R.id.answer_num_no)
    TextView mAnswerNumNo;
    @Bind(R.id.comments_lv_pb)
    ProgressBar mAnswerPb;
    @Bind(R.id.comments_lv_add_more)
    TextView mAnswerAdMore;
    @Bind(R.id.match_thing_info_type_num)
    TextView mMatchThingHotNum;
    @Bind(R.id.home_match_thing_info_context_iv3)
    com.dading.ssqs.bean.SmoothImageView iv3;
    @Bind(R.id.home_match_thing_info_context_iv2)
    com.dading.ssqs.bean.SmoothImageView iv2;
    @Bind(R.id.home_match_thing_info_context_iv1)
    com.dading.ssqs.bean.SmoothImageView iv1;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.top_icon)
    ImageView mTopIcon;


    private View mPopView;
    private String mCreateDate;
    private NewInfoBean mData;
    private int mPage = 2;
    private CommentsLvAdapter mAdapter;
    private List<NewInfoBean.CommentsEntity> mComments;
    private List<CommentsBean> mItems;
    private int mVpinfo;
    private NewInfoBean.ArticlesEntity mArticles;
    private TextView mPopShare;
    private TextView mPopCollect;
    private TextView mPopTip;
    private LinearLayout mPopShareLy;
    private LinearLayout mPopCollectLy;
    private LinearLayout mPopTipLy;
    private boolean mIsSUC = false;
    private PopupWindow mPop;
    private ShareBean mDataShare;
    private int mAnswerCount;
    private int mZanCount;
    private List<CommentsBean> mData1;
    int i = 1;

    private List<String> mUrlContrllar;
    private String mValue;

    @Override
    protected void setUnDe() {
        UIUtils.removeTaskAll(null);
        ShareSDK.stopSDK(this);
    }

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        initData();
    }

    private void showShare(ShareBean data) {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(data.title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(data.forwardUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(data.content + data.forwardUrl);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(data.forwardUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(data.content);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(data.title);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setImageUrl(data.logoUrl);
        LogUtil.util(TAG, "shouye赛事返回数据是------------------------------:" + data.logoUrl);
        oks.setSiteUrl(data.forwardUrl);
        // 启动分享GUI
        oks.show(this);
    }


    @Override
    protected void initListener() {
        mPopTipLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataShare != null) {
                    int sdkInt = Build.VERSION.SDK_INT;
                    if (sdkInt > Build.VERSION_CODES.HONEYCOMB) {// api11
                        ClipboardManager copy = (ClipboardManager) HomeViewPagerActivity.this
                                .getSystemService(Context.CLIPBOARD_SERVICE);
                        copy.setText(mDataShare.forwardUrl);
                        TmtUtils.midToast(UIUtils.getContext(), "URL链接成功复制到粘贴板", 0);
                    } else if (sdkInt <= Build.VERSION_CODES.HONEYCOMB) {
                        android.text.ClipboardManager copyq = (android.text.ClipboardManager) HomeViewPagerActivity.this
                                .getSystemService(Context.CLIPBOARD_SERVICE);
                        copyq.setText(mDataShare.forwardUrl);
                        TmtUtils.midToast(UIUtils.getContext(), "URL链接成功复制到粘贴板", 0);
                    }
                    mPop.dismiss();
                }
            }
        });
        mPopShareLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    if (mDataShare != null) {
                        showShare(mDataShare);
                    }
                    mPop.dismiss();
                } else {
                    Intent intent = new Intent(HomeViewPagerActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        mHomeMatchThingInfoPublishContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 50) {
                    TmtUtils.midToast(HomeViewPagerActivity.this, "评论数不得大于50字!", 0);
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mPopCollectLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mIsSUC) {
                    return;
                }
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    /**
                     * 8.	收藏文章
                     a)	请求地址：
                     /v1.0/fouceArticle
                     b)	请求方式:
                     post
                     c)	请求参数说明：
                     articleID:文章ID
                     status:状态 0-取消关注1-关注
                     auth_token：登陆后加入请求头
                     */
                    FouceArticleElement element = new FouceArticleElement();
                    element.setArticleID(String.valueOf(mVpinfo));
                    element.setStatus(mArticles.isCollect == 0 ? "1" : "0");

                    SSQSApplication.apiClient(classGuid).fouceArticle(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                if (mArticles.isCollect == 0) {
                                    mArticles.isCollect = 1;
                                    mPopCollect.setText("取消收藏");
                                } else {
                                    mArticles.isCollect = 0;
                                    mPopCollect.setText("收藏");
                                }
                                mPop.dismiss();
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(HomeViewPagerActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                }
                            }
                        }
                    });
                } else {
                    Intent intent = new Intent(HomeViewPagerActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void initView() {
        mPopView = View.inflate(this, R.layout.home_match_thing_item_info_pop, null);
        mPopShare = (TextView) mPopView.findViewById(R.id.tiezi_share);
        mPopCollect = (TextView) mPopView.findViewById(R.id.tiezi_collect);
        mPopTip = (TextView) mPopView.findViewById(R.id.tiezi_tip);
        mPopShareLy = (LinearLayout) mPopView.findViewById(R.id.tiezi_share_ly);
        mPopCollectLy = (LinearLayout) mPopView.findViewById(R.id.tiezi_collect_ly);
        mPopTipLy = (LinearLayout) mPopView.findViewById(R.id.tiezi_tip_ly);

        //获取赞的数据信息 是都点过
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_home_viewpager_info;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.main_stye_thread));
        mTopIcon.setImageResource(R.mipmap.t_share_dot);
        mTopIcon.setVisibility(View.VISIBLE);

        mPop = PopUtil.popuMakeWrap(mPopView);
        /**
         * /v1.0/share
         */

        SSQSApplication.apiClient(classGuid).getShareInfo(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    mDataShare = (ShareBean) result.getData();
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(HomeViewPagerActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
        mItems = new ArrayList<>();
        //接受跳转ID
        Intent intent = getIntent();
        mVpinfo = intent.getIntExtra("infoId", 0);

        SSQSApplication.apiClient(classGuid).getArticleDetails(mVpinfo + "", new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    NewInfoBean bean = (NewInfoBean) result.getData();

                    if (bean != null) {
                        processData(bean);
                    }
                    mIsSUC = true;
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(HomeViewPagerActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        mIsSUC = false;
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processData(NewInfoBean bean) {
        mData = bean;
        if (mData.articles != null) {
            mArticles = mData.articles;
            if (mMatchThingInfoTypeIcon != null)
                Glide.with(UIUtils.getContext())
                        .load(mArticles.categoryImageUrl)
                        .error(R.mipmap.fail)
                        .centerCrop()
                        .transform(new GlideCircleTransform(this))
                        .into(mMatchThingInfoTypeIcon);

            if (mArticles.isCollect == 0) {
                mPopCollect.setText("收藏");
            } else {
                mPopCollect.setText("取消收藏");
            }

            if (mMatchThingInfoSavantphoto != null)
                Glide.with(UIUtils.getContext())
                        .load(mArticles.avatar)
                        .error(R.mipmap.nologinportrait)
                        .centerCrop()
                        .transform(new GlideCircleTransform(this))
                        .into(mMatchThingInfoSavantphoto);

            mMatchThingInfoTypeText.setText(mData.articles.categoryName);
        }
        String hotnum = mArticles.hotCount + "条热帖";
        mMatchThingHotNum.setText(hotnum);

        //回帖
        mAnswerCount = mArticles.commentCount;
        String text = "回帖 " + mAnswerCount;
        mAnswerNum.setText(text);

        mMatchThingInfoSavantName.setText(mData.articles.userName);
        mCreateDate = mData.articles.createDate;
        String date = mCreateDate.substring(5, 16);
        mMatchThingInfoTime.setText(date);
        mHomeMatchThingInfoContextTitle.setText(mData.articles.title);
        String content = mData.articles.content;
        String s1 = content.replaceAll("_ssqs_", "");
        mHomeMatchThingInfoContext.setText(s1);

        mUrlContrllar = bean.articles.imageUrl;

        if (mUrlContrllar != null)
            switch (mUrlContrllar.size()) {
                case 0:
                    iv1.setVisibility(View.GONE);
                    iv2.setVisibility(View.GONE);
                    iv3.setVisibility(View.GONE);
                    break;
                case 1:
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.GONE);
                    iv3.setVisibility(View.GONE);
                    Glide.with(UIUtils.getContext()).load(mUrlContrllar.get(0)).error(R.mipmap.fail)/*.centerCrop( )*/.into(iv1);

                    break;
                case 2:
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.GONE);
                    Glide.with(UIUtils.getContext()).load(mUrlContrllar.get(0)).error(R.mipmap.fail)/*.centerCrop( )*/.into(iv1);
                    Glide.with(UIUtils.getContext()).load(mUrlContrllar.get(1)).error(R.mipmap.fail)/*.centerCrop( )*/.into(iv2);
                    break;
                case 3:
                    iv1.setVisibility(View.VISIBLE);
                    iv2.setVisibility(View.VISIBLE);
                    iv3.setVisibility(View.VISIBLE);
                    Glide.with(UIUtils.getContext()).load(mUrlContrllar.get(0)).error(R.mipmap.fail)/*.centerCrop( )*/.into(iv1);
                    Glide.with(UIUtils.getContext()).load(mUrlContrllar.get(1)).error(R.mipmap.fail)/*.centerCrop( )*/.into(iv2);
                    Glide.with(UIUtils.getContext()).load(mUrlContrllar.get(2)).error(R.mipmap.fail)/*.centerCrop( )*/.into(iv3);
                    break;
            }
        mZanCount = mData.articles.zanCount;
        String s = mZanCount + "个赞";

        mHomeMatchThingInfoContextGoodText.setText(s);
        if (mData.articles.isZan == 0) {
            mHomeMatchThingInfoContextGoodIv.setImageResource(R.mipmap.t_user_recoms);
            mHomeMatchThingInfoContextGoodIv.setClickable(true);
        } else {
            mHomeMatchThingInfoContextGoodIv.setImageResource(R.mipmap.t_user_recoms_sel);
            mHomeMatchThingInfoContextGoodIv.setClickable(false);
        }
        mComments = mData.comments;
        if (mComments.size() == 0) {
            mAnswerNumNo.setVisibility(View.VISIBLE);
        } else {
            mAnswerNumNo.setVisibility(View.GONE);
        }
        LogUtil.util(TAG, mComments.toString());

        mAdapter = new CommentsLvAdapter(this, mComments);
        mHomeMatchThingInfoCommentsLv.setAdapter(mAdapter);
        ListScrollUtil.setListViewHeightBasedOnChildren(mHomeMatchThingInfoCommentsLv);
    }

    @OnClick({R.id.top_back, R.id.top_icon, R.id.match_thing_info_go, R.id.match_thing_info_savantphoto,
            R.id.home_match_thing_info_publish, R.id.home_match_thing_info_context_good_iv, R.id.comments_lv_add_more,
            R.id.home_match_thing_info_context_iv1, R.id.home_match_thing_info_context_iv2, R.id.home_match_thing_info_context_iv3})
    public void onClick(View v) {
        UIUtils.hideKeyBord(this);
        int[] location = new int[2];
        Intent intent = new Intent(HomeViewPagerActivity.this, SpaceImageDetailActivity.class);
        switch (v.getId()) {
            case R.id.home_match_thing_info_context_iv1:
                setBigPic(mUrlContrllar, location, intent, iv1, 0);
                break;
            case R.id.home_match_thing_info_context_iv2:
                setBigPic(mUrlContrllar, location, intent, iv2, 1);
                break;
            case R.id.home_match_thing_info_context_iv3:
                setBigPic(mUrlContrllar, location, intent, iv3, 2);
                break;
            case R.id.top_back:
                finish();
                break;
            case R.id.top_icon:
                mPop.showAsDropDown(mToply, 0, 0);
                break;
            case R.id.match_thing_info_go:
                //ReferCircleBean.DataEntity.CategorysEntity entity = data.categorys.get(position);
                Intent mIntent = new Intent(this, MatchTypeInfoActivity.class);
                //來自首頁的跳轉詳情類型
                mIntent.putExtra(Constent.ALL_CIRCLE_TYPE_TAG, Constent.HOME_THING2_COME);
                if (mArticles != null) {
                    Object o1 = JSON.toJSON(mArticles);
                    LogUtil.util(TAG, "首页跳转类型返回数据是------------------------------:" + o1.toString());
                    mIntent.putExtra(Constent.ALL_CIRCLE_TYPE, o1.toString());
                    this.startActivity(mIntent);
                }
                break;
            case R.id.match_thing_info_savantphoto:
                break;
            case R.id.home_match_thing_info_context_good_iv:
                if (!mIsSUC) {
                    return;
                }
                /*int i = mData.articles.zanCount + 1;
                String s = i + "个赞";
                mHomeMatchThingInfoContextGoodText.setText(s);*/
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    /**
                     * 点赞
                     * a)	请求地址：
                     /v1.0/zanArticle/save
                     b)	请求方式:
                     post
                     c)	请求参数说明：
                     articleID：文章ID
                     auth_token：登陆后加入请求头
                     */
                    ArticleSaveElement element = new ArticleSaveElement();
                    element.setArticleID(String.valueOf(mVpinfo));

                    SSQSApplication.apiClient(classGuid).articleSave(element, new CcApiClient.OnCcListener() {
                        @Override
                        public void onResponse(CcApiResult result) {
                            if (result.isOk()) {
                                int i = mZanCount + 1;
                                String s = i + "个赞";
                                mHomeMatchThingInfoContextGoodText.setText(s);
                                mHomeMatchThingInfoContextGoodIv.setImageResource(R.mipmap.t_user_recoms_sel);
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(HomeViewPagerActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                }
                            }
                        }
                    });
                } else {
                    Intent intentLoad = new Intent(HomeViewPagerActivity.this, LoginActivity.class);
                    startActivity(intentLoad);
                }

                break;
            case R.id.comments_lv_add_more:
                mAnswerPb.setVisibility(View.VISIBLE);
                mAnswerAdMore.setVisibility(View.GONE);

                SSQSApplication.apiClient(classGuid).getCommentDetailsList(mData.articles.id, mPage, 10, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            CcApiResult.ResultCommentDetailsListPage page = (CcApiResult.ResultCommentDetailsListPage) result.getData();

                            if (page != null && page.getItems() != null) {
                                mData1 = page.getItems();
                                if (page.getItems().size() == 0) {
                                    TmtUtils.midToast(UIUtils.getContext(), "没有最新评论啦!", 0);
                                    return;
                                } else {
                                    UIUtils.postTaskDelay(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAnswerPb.setVisibility(View.GONE);
                                            mAnswerAdMore.setVisibility(View.VISIBLE);
                                            changeData(mData1);
                                        }
                                    }, 1500);
                                }
                            } else {
                                if (403 == result.getErrno()) {
                                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                    UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                    Intent intent = new Intent(HomeViewPagerActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                }
                            }
                        }
                    }
                });
                UIUtils.postTaskDelay(new Runnable() {
                    @Override
                    public void run() {
                        mAnswerPb.setVisibility(View.GONE);
                        mAnswerAdMore.setVisibility(View.VISIBLE);
                    }
                }, 1500);
                break;
            case R.id.home_match_thing_info_publish:
                /**
                 * 请求地址：/v1.0/comment/save
                 2)	请求方式:post
                 3)	请求参数说明：articleID：文章IDcontent:发表内容
                 4)	返回格式
                 {
                 "status": true,
                 "code": 0,
                 "msg": "",
                 "data": []
                 }
                 */
                String s1 = mHomeMatchThingInfoPublishContent.getText().toString();
                if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                    if (s1.equals("")) {
                        TmtUtils.midToast(UIUtils.getContext(), "请输入您要发表的内容....", 0);
                    } else {
                        SendArticleCommentElement element = new SendArticleCommentElement();
                        element.setArticleID(String.valueOf(mData.articles.id));
                        element.setContent(s1);

                        SSQSApplication.apiClient(classGuid).sendArticleComment(element, new CcApiClient.OnCcListener() {
                            @Override
                            public void onResponse(CcApiResult result) {
                                if (result.isOk()) {
                                    TmtUtils.midToast(UIUtils.getContext(), "发表成功!", 0);
                                    mHomeMatchThingInfoPublishContent.setText("");
                                    goRest();
                                } else {
                                    if (403 == result.getErrno()) {
                                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                        Intent intent = new Intent(HomeViewPagerActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        TmtUtils.midToast(UIUtils.getContext(), "发表失败,请重新发布!", 0);
                                    }
                                }
                            }
                        });
                        mHomeMatchThingInfoPublish.setClickable(false);
                    }
                    break;
                } else {
                    Intent intentl = new Intent(this, LoginActivity.class);
                    startActivity(intentl);
                }

        }
    }

    private void setBigPic(List<String> urlContrllar, int[] location, Intent intent, ImageView iv1, int pos) {
        iv1.getLocationOnScreen(location);
        if (urlContrllar != null && urlContrllar.size() > pos)
            mValue = mUrlContrllar.get(pos);

        intent.putExtra("imageUrl", mValue);//必须
        intent.putExtra("locationX", location[0]);//必须
        intent.putExtra("locationY", location[1]);//必须

        intent.putExtra("width", this.iv1.getWidth());//必须
        intent.putExtra("height", this.iv1.getHeight());//必须

        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void changeData(List<CommentsBean> data) {
        if (!mIsSUC) {
            return;
        }
        mPage++;
        /*if (mPage <= data.totalCount) {*/
        TmtUtils.midToast(UIUtils.getContext(), "刷新成功!", 0);
        LogUtil.util(TAG, "加载更多返回数据是------------------------------:条目" + data.size());
        //mItems.addAll(data.items);
        for (CommentsBean s1 : data) {
               /* mItems.add(s1);
                LogUtil.util(TAG,"返回数据是------------------------------:"+s1.content +i);
                i++;*/
            NewInfoBean.CommentsEntity commentsEntity = new NewInfoBean.CommentsEntity();
            commentsEntity.content = s1.content;
            commentsEntity.id = s1.id;
            commentsEntity.userID = s1.userID;
            commentsEntity.userName = s1.userName;
            commentsEntity.avatar = s1.avatar;
            commentsEntity.createDate = s1.createDate;
            mComments.add(commentsEntity);
        }

        if (mItems != null)
            LogUtil.util(TAG, "加载更多返回数据是------------------------------:条目总" + mItems.size());

        mAdapter = new CommentsLvAdapter(this, mComments);
        mHomeMatchThingInfoCommentsLv.setAdapter(mAdapter);
        ListScrollUtil.setListViewHeightBasedOnChildren(mHomeMatchThingInfoCommentsLv);
            /*mHomeMatchThingInfoCommentsLv2.setAdapter(new CommentsLvRestAddAdapter(this, mItems));
            ListScrollUtil.setListViewHeightBasedOnChildren(mHomeMatchThingInfoCommentsLv2);*/
       /* } else {
            Toast.makeText(HomeViewPagerActivity.this, "没有新数据！", Toast.LENGTH_SHORT).show();
        }*/
    }

    public void goRest() {
        SSQSApplication.apiClient(classGuid).getArticleDetails(mVpinfo + "", new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    NewInfoBean bean = (NewInfoBean) result.getData();

                    if (bean != null) {
                        processDataAdd(bean);
                        mIsSUC = true;
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(HomeViewPagerActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        mIsSUC = false;
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processDataAdd(NewInfoBean bean) {
        mComments.add(0, bean.comments.get(0));
        mAnswerCount = mAnswerCount + 1;
        String text = "回帖" + mAnswerCount;
        mAnswerNum.setText(text);
        if (mComments.size() == 0) {
            mAnswerNumNo.setVisibility(View.VISIBLE);
        } else {
            mAnswerNumNo.setVisibility(View.GONE);
        }
        LogUtil.util(TAG, mComments.toString());

        mAdapter = new CommentsLvAdapter(this, mComments);
        mHomeMatchThingInfoCommentsLv.setAdapter(mAdapter);
        ListScrollUtil.setListViewHeightBasedOnChildren(mHomeMatchThingInfoCommentsLv);
    }
}