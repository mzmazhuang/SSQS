package com.dading.ssqs.apis;

import android.content.Context;
import android.text.TextUtils;

import com.dading.ssqs.apis.elements.AddressInfoElement;
import com.dading.ssqs.apis.elements.ArticleSaveElement;
import com.dading.ssqs.apis.elements.AwardExchangeElement;
import com.dading.ssqs.apis.elements.ChargeUploadElement;
import com.dading.ssqs.apis.elements.CommentSaveElement;
import com.dading.ssqs.apis.elements.DiamondExchangeElement;
import com.dading.ssqs.apis.elements.ExpertElement;
import com.dading.ssqs.apis.elements.ExpertInfoByNameElement;
import com.dading.ssqs.apis.elements.ExtractInfoElement;
import com.dading.ssqs.apis.elements.ExtractUploadElement;
import com.dading.ssqs.apis.elements.FeedBackElement;
import com.dading.ssqs.apis.elements.FocusMatchElement;
import com.dading.ssqs.apis.elements.FocusUserElement;
import com.dading.ssqs.apis.elements.ForgetPasswordElement;
import com.dading.ssqs.apis.elements.ForgetSecondPasswordElement;
import com.dading.ssqs.apis.elements.ForgetUserPasswordElement;
import com.dading.ssqs.apis.elements.FouceArticleCategoryElement;
import com.dading.ssqs.apis.elements.FouceArticleElement;
import com.dading.ssqs.apis.elements.FouceMatchElement;
import com.dading.ssqs.apis.elements.LoginElement;
import com.dading.ssqs.apis.elements.OrderStatusElement;
import com.dading.ssqs.apis.elements.PayBallDoubleElement;
import com.dading.ssqs.apis.elements.PayBallElement;
import com.dading.ssqs.apis.elements.PayBallScoreElement;
import com.dading.ssqs.apis.elements.ProxyCodeUpdateElement;
import com.dading.ssqs.apis.elements.ReCommUploadElement;
import com.dading.ssqs.apis.elements.RecommDetailElement;
import com.dading.ssqs.apis.elements.RegAccountElement;
import com.dading.ssqs.apis.elements.RegTrialElement;
import com.dading.ssqs.apis.elements.SendArticleCommentElement;
import com.dading.ssqs.apis.elements.SendBindPhoneYZMElement;
import com.dading.ssqs.apis.elements.SendYZMElement;
import com.dading.ssqs.apis.elements.ShareArticleElement;
import com.dading.ssqs.apis.elements.ThreeLoginElement;
import com.dading.ssqs.apis.elements.TopicElement;
import com.dading.ssqs.apis.elements.UpdatePassWordElement;
import com.dading.ssqs.apis.elements.UserBindElement;
import com.dading.ssqs.apis.elements.FouceMatchBallElement;
import com.dading.ssqs.apis.elements.UserElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.NetUtil;
import com.dading.ssqs.utils.UIUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by mazhuang on 2017/11/20.
 * 统一接口请求
 */

public class CcApiClient {
    private static final String TAG = "CcApiClient";

    private static final String APIVersion = "1.0.0";

    final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final Context mContext;

    private String mBaseUri;

    private String mDevice;

    private String mRequestId = "0";

    private final OkHttpClient client;

    private static final String[] API_URLS = {"http://13.124.189.54:8092/zc-intf/rest"};//多种服务器

    private static final HashMap<String, List<Call>> calls = new HashMap<>();

    private CcApiClient(final Context context) {
        this.mContext = context;
        this.setRouterIndex(-1);

        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        try {
                            return chain.proceed(request);
                        } catch (Exception e) {
                            if (!NetUtil.isConnected(mContext)) {
                                //没有网络时, 抛出网络错误。
                                Logger.e(TAG, "error noconnect");
                                throw e;
                            } else {
                                if (Constent.DEBUG_VERSION) {
                                    throw e;
                                } else {
                                    //构建其他节点请求
                                    //return chain.proceed(buildOtherNodeRequest(request));
                                    return chain.proceed(request);
                                }
                            }
                        }
                    }
                })
                .build();
    }


    public void setRouterIndex(int index) {
        String debugUrl = UIUtils.getSputils().getString("API_BASE_URL_DEBUG", "");
        if (!TextUtils.isEmpty(debugUrl)) {
            this.mBaseUri = debugUrl;
            return;
        }

        if (Constent.DEBUG_VERSION) {//如果是debug环境 改用debug服务器
//            this.mBaseUri = "https://www.ddzlink.com/zc-intf/rest";
//            this.mBaseUri = "http://10.10.31.71:8080/zc-intf/rest";//阿木
            this.mBaseUri = "http://13.124.189.54:8092/zc-intf/rest";//服务器
            return;
        }

        if (index > -1) {
            this.mBaseUri = API_URLS[index];
            UIUtils.getSputils().putString("API_BASE_URL", this.mBaseUri);
            Logger.d(TAG, "************** Select URL Router: " + index + " **************");
        } else {
            String baseUri = UIUtils.getSputils().getString("API_BASE_URL", "");
            if (TextUtils.isEmpty(baseUri)) {
                this.mBaseUri = API_URLS[0];
            } else {
                this.mBaseUri = baseUri;
            }
        }
    }

    public String getBaseUri() {
        return this.mBaseUri;
    }

    public void resetBaseUrl(String url) {
        this.mBaseUri = url;
    }

    public void setUserDevice(String device) {
        this.mDevice = device + "&api=" + APIVersion;
    }

    public void setRequestId(String requestId) {
        this.mRequestId = requestId;
    }

    public void cancelRequest(String requestId) {
        if (calls.containsKey(requestId)) {
            List<Call> request_calls = calls.get(requestId);
            for (int i = 0; i < request_calls.size(); i++) {
                request_calls.get(i).cancel();
            }

            request_calls.clear();

            calls.remove(requestId);
        }
    }

    /**
     * instance CcApiClient
     *
     * @param context
     * @return
     */

    public static CcApiClient instance(Context context) {
        return new CcApiClient(context);
    }

    /**
     * 获取用户信息
     */
    public void getUserInfo(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "getUserInfo");

        Request("/v1.0/user/getUser", null, mListener, false);
    }

    /**
     * 账户绑定
     *
     * @param element
     * @param listener
     */
    public void bindUserInfo(UserBindElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doUserBind");

        Request("/v1.0/user/bind", element.buildParams(), mListener, true);
    }

    /**
     * 关注比赛
     *
     * @param listener
     */
    public void fouceMatch(FouceMatchElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doFouceMatch");

        Request("/v1.0/fouceMatch", element.buildParams(), mListener, true);
    }

    /**
     * 修改密码
     *
     * @param listener
     */
    public void updatePassWord(UpdatePassWordElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doUodatePassWord");

        Request("/v1.0/user/password", element.buildParams(), mListener, true);
    }

    /**
     * 第三方登陆
     *
     * @param element
     * @param listener
     */
    public void threeLogin(ThreeLoginElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doThreeLogin");

        Request("/v1.0/user/third", element.buildParams(), mListener, true);
    }

    /**
     * 登陆
     *
     * @param listener
     */
    public void login(LoginElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doLogin");

        Request("/v1.0/user/login", element.buildParams(), mListener, true);
    }

    /**
     * 添加进收藏/移除进收藏
     *
     * @param listener
     */
    public void fouceMatchBall(FouceMatchBallElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doFouceMatchBall");

        Request("/v1.0/fouceMatch" + (UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true) ? "" : "/ball"), element.buildParams(), mListener, true);
    }

    /**
     * 注册试用账号
     *
     * @param listener
     */
    public void regTrial(RegTrialElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doRegistTrial");

        Request("/v1.0/user/reg/trial", element.buildParams(), mListener, true);
    }

    /**
     * 文章类别类型列表
     *
     * @param listener
     */
    public void getCategoryTypeList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doCategoryList");

        Request("/v1.0/aCategoryType/list", null, mListener, false);
    }

    /**
     * 我的投注列表-历史投注
     *
     * @param listener
     */
    public void getMyPayBallHistoryList(String startTime, String endTime, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMyPayBallList");

        Request("/v1.0/payBall/" + (UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true) ? "" : "ball/") + "history/startDate/" + startTime
                + "/endDate/" + endTime, null, mListener, false);
    }

    /**
     * 我的投注列表-未结投注
     */
    public void getMyPayBallUnChecked(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMyPayBallUnCheck");

        Request("/v1.0/payBall/" + (UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true) ? "" : "ball/") + "unchecked", null, mListener, false);
    }

    /**
     * 安场次1
     *
     * @param listener
     */
    public void getRecentType(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doRecentType");

        Request("/v1.0/payBall/" + (UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true) ? "" : "ball/") + "recent/type/1", null, mListener, false);
    }

    /**
     * 安场次2
     *
     * @param listener
     */
    public void getRecentType2(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doRecentType2");

        Request("/v1.0/payBall/" + (UIUtils.getSputils().getBoolean(Constent.IS_FOOTBALL, true) ? "" : "ball/") + "recent/type/2", null, mListener, false);
    }

    /**
     * 用户提现信息上传
     *
     * @param element
     * @param listener
     */
    public void extractInfoUpload(ExtractInfoElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doExtractUpload");

        Request("/v1.0/extract/msg", element.buildParams(), mListener, true);
    }

    /**
     * 银行列表
     *
     * @param listener
     */
    public void getBankList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doBankList");

        Request("/v1.0/bank", null, mListener, false);
    }

    /**
     * 修改交易密码
     *
     * @param listener
     */
    public void forgetPassword(ForgetPasswordElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doForgetPassword");

        Request("/v1.0/extract/pwd/forget", element.buildParams(), mListener, true);
    }

    /**
     * 找回密码-发送验证码
     *
     * @param listener
     */
    public void forgetUserPassword(ForgetUserPasswordElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doForgetUserPassword");

        Request("/v1.0/authCode/forget", element.buildParams(), mListener, true);
    }

    /**
     * 收藏文章/取消收藏
     *
     * @param listener
     */
    public void fouceArticle(FouceArticleElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doFouceArticle");

        Request("/v1.0/fouceArticle", element.buildParams(), mListener, true);
    }

    /**
     * 点赞文章
     *
     * @param listener
     */
    public void articleSave(ArticleSaveElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doArticleSave");

        Request("/v1.0/zanArticle/save", element.buildParams(), mListener, true);
    }

    /**
     * 发送绑定手机验证码
     *
     * @param listener
     */
    public void sendBindPhoneYZM(SendBindPhoneYZMElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doSendBindYZM");

        Request("/v1.0/authCode/bind", element.buildParams(), mListener, true);
    }

    /**
     * 分享文章
     *
     * @param listener
     */
    public void shareArticle(ShareArticleElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doShareArticel");

        Request("/v1.0/wShare/save", element.buildParams(), mListener, true);
    }

    /**
     * 充值上传
     *
     * @param listener
     */
    public void chargeUpload(ChargeUploadElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doChargeUpload");

        Request("/v1.0/charge/save", element.buildParams(), mListener, true);
    }

    /**
     * 发送验证码
     *
     * @param listener
     */
    public void sendYZM(SendYZMElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doSendYzm");

        Request("/v1.0/authCode", element.buildParams(), mListener, true);
    }

    /**
     * 账户注册
     *
     * @param listener
     */
    public void regAccount(RegAccountElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doRegAccount");

        Request("/v1.0/user/reg/account", element.buildParams(), mListener, true);
    }

    /**
     * 注册界面2手机与密码
     *
     * @param listener
     */
    public void regAccount2(RegAccountElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doRegAccount2");

        Request("/v1.0/user/reg", element.buildParams(), mListener, true);
    }

    /**
     * 申请专家
     *
     * @param element
     * @param listener
     */
    public void ExpertSave(ExpertElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doExpert");

        Request("/v1.0/applyExpert/save", element.buildParams(), mListener, true);
    }

    /**
     * 关注用户
     *
     * @param element
     * @param listener
     */
    public void focusUser(FocusUserElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doFocusUser");

        Request("/v1.0/fouce", element.buildParams(), mListener, true);
    }

    /**
     * 支持或反对推荐比赛
     */
    public void recommDetails(RecommDetailElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doRecommDetails");

        Request("/v1.0/recommDetail", element.buildParams(), mListener, true);
    }

    /**
     * 用户提现上传
     *
     * @param element
     * @param listener
     */
    public void extractUpload(ExtractUploadElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doExtractUpload");

        Request("/v1.0/extract", element.buildParams(), mListener, true);
    }

    /**
     * 提交推荐比赛
     *
     * @param element
     * @param listener
     */
    public void reCommUpload(ReCommUploadElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doRecommUpload");

        Request("/v1.0/recomm", element.buildParams(), mListener, true);
    }

    /**
     * 代理邀请码修改
     *
     * @param element
     * @param listener
     */
    public void proxyCodeUpdate(ProxyCodeUpdateElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doProxyCodeUpdate");

        Request("/v1.0/agent/code/msg", element.buildParams(), mListener, true);
    }

    /**
     * 发表评论
     *
     * @param isMb
     * @param element
     * @param listener
     */
    public void commentSend(boolean isMb, CommentSaveElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doCommentSave");

        Request("/v1.0/" + (isMb ? "matchMessage" : "matchMsgBall") + "/save", element.buildParams(), mListener, true);
    }

    /**
     * 奖品兑换
     *
     * @param element
     * @param listener
     */
    public void awardExchange(AwardExchangeElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAwardExchange");

        Request("/v1.0/awardExchange", element.buildParams(), mListener, true);
    }

    /**
     * 兑换Vip奖品
     *
     * @param element
     * @param listener
     */
    public void awardExchangeVip(AwardExchangeElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAwardExchangeVip");

        Request("/v1.0/awardExchange/vip", element.buildParams(), mListener, true);
    }

    /**
     * 更新用户信息
     *
     * @param element
     * @param listener
     */
    public void updateUserInfo(UserElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doUpdateUser");

        Request("/v1.0/user", element.buildParams(), mListener, true);
    }

    /**
     * 账号明细
     * type	int	1	是	类型
     * 1：全部
     * 2：充值
     * 3：提款
     * 4：购彩
     * 5：中奖
     * 6：佣金提成
     * 7：其他
     * page	int		是	当前页数
     *
     * @param listener
     */
    public void getAccountDetailsList(int type, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAccountList");

        Request("/v1.0/accountDetail/type/" + type + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 帖子列表
     *
     * @param id
     * @param offset
     * @param limit
     * @param listener
     */
    public void getArticleList(String id, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doArticleList");

        Request("/v1.0/article/detail/userID/" + id + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 忘记密码
     *
     * @param element
     * @param listener
     */
    public void forgetSecondPassword(ForgetSecondPasswordElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doForgetSecondPassword");

        Request("/v1.0/user/forget", element.buildParams(), mListener, true);
    }

    /**
     * 竞彩高手
     *
     * @param offset
     * @param limit
     * @param listener
     */
    public void expertGuessList(int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doExpertGuessList");

        Request("/v1.0/expert/guess/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 获取分享信息
     */
    public void getShareInfo(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doShareInfo");

        Request("/v1.0/share", null, mListener, false);
    }

    /**
     * 文章详情
     *
     * @param id
     * @param listener
     */
    public void getArticleDetails(String id, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doArticleDetails");

        Request("/v1.0/article/detail/id/" + id, null, mListener, false);
    }

    /**
     * 评论数据
     *
     * @param id
     * @param offset
     * @param limit
     * @param listener
     */
    public void getCommentDetailsList(int id, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doCommentDetailsList");

        Request("/v1.0/comment/detail/articleID/" + id + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 发评论
     *
     * @param element
     * @param listener
     */
    public void sendArticleComment(SendArticleCommentElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doSendArticleComment");

        Request("/v1.0/comment/save", element.buildParams(), mListener, true);
    }

    /**
     * 连红榜
     *
     * @param offset
     * @param limit
     * @param listener
     */
    public void recommHotList(int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doRecommHot");

        Request("/v1.0/expert/recomm/hot/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 获取公告
     *
     * @param listener
     */
    public void getNotices(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doNotices");

        Request("/v1.0/notice", null, mListener, false);
    }

    /**
     * 版本是否更新
     *
     * @param listener
     */
    public void getVersionIsUpdate(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doVersionIsUpdate");

        Request("/v1.0/version/isShow", null, mListener, false);
    }

    /**
     * 通过比赛id获取比赛两队的球队信息
     *
     * @param b
     * @param mMatchId
     * @param listener
     */
    public void getMathchData(boolean b, String mMatchId, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchData");

        Request((b ? "/v1.0/match/id/" : "/v1.0/match/ball/id/") + mMatchId, null, mListener, false);
    }

    /**
     * 根据文章类别获取文章列表
     */
    public void getArticleListById(int id, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "foArticleListById");

        Request("/v1.0/article/categoryID/" + id + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 关注文章类别
     *
     * @param element
     * @param listener
     */
    public void fouceArticleCategory(FouceArticleCategoryElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doFouceArticleCateGory");

        Request("/v1.0/fouceArticleCategory", element.buildParams(), mListener, true);
    }

    /**
     * 通知开关查询
     *
     * @param listener
     */
    public void getMatchVoice(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchVoice");

        Request("/v1.0/matchVoice", null, mListener, false);
    }

    /**
     * 进球通知开关编辑
     *
     * @param isOpen
     * @param listener
     */
    public void setMatchVoice(boolean isOpen, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchVoice");

        if (!isOpen) {
            Request("/v1.0/matchVoice/status/1", null, mListener, false);
        } else {
            Request("/v1.0/matchVoice/status/0", null, mListener, false);
        }
    }

    /**
     * 获取我的信息类型列表
     *
     * @param listener
     */
    public void getMyInfoTypeList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMyInfoTypeList");

        Request("/v1.0/msg/type", null, mListener, false);
    }

    /**
     * 团队报表/个人报表
     *
     * @param isTeam
     * @param startDate
     * @param endDate
     * @param listener
     */
    public void getReportList(boolean isTeam, String startDate, String endDate, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doReportList");

        Request("/v1.0/report/" + (isTeam ? "" : "team/") + "startDate/" + startDate + "/endDate/" + endDate, null, mListener, false);
    }

    /**
     * 奖品兑换记录
     *
     * @param listener
     */
    public void getGiftHistory(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doGiftHistory");

        Request("/v1.0/awardExchange/list/gift", null, mListener, false);
    }

    /**
     * 佣金信息
     *
     * @param listener
     */
    public void getAgentInfo(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAgentInfo");

        Request("/v1.0/agent", null, mListener, false);
    }

    /**
     * 代理邀请码列表
     *
     * @param listener
     */
    public void agentCodeList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAgentCodeList");

        Request("/v1.0/agent/code", null, mListener, false);
    }

    /**
     * 代理邀请码生成/代理邀请码上传
     *
     * @param code
     * @param listener
     */
    public void agentCodeOperation(String code, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAgentCode");

        if (!TextUtils.isEmpty(code)) {
            Request("/v1.0/agent/code/create/" + code + "", null, mListener, false);
        } else {
            Request("/v1.0/agent/code/create", null, mListener, false);
        }
    }

    /**
     * 代理邀请码删除
     *
     * @param id
     * @param listener
     */
    public void agentCodeDelete(int id, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAgentCodeDelete");

        Request("  /v1.0/agent/code/del/" + id, null, mListener, false);
    }

    /**
     * 转盘兑换记录
     *
     * @param listener
     */
    public void awardExchangeList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAwardExchangeList");

        Request("/v1.0/awardExchange/list/turn", null, mListener, false);
    }

    /**
     * 代理佣金时间查询列表
     *
     * @param startTime
     * @param endTime
     * @param listener
     */
    public void agentCheckList(String startTime, String endTime, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAgentCheckList");

        Request("/v1.0/agent/check/startDate/" + startTime + "/endDate/" + endTime, null, mListener, false);
    }

    /**
     * 代理说明
     *
     * @param listener
     */
    public void getAgentRemark(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAgentRemark");

        Request("/v1.0/agent/remark", null, mListener, false);
    }

    /**
     * 发帖
     *
     * @param element
     * @param listener
     */
    public void createTopic(TopicElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doCreateTopic");

        Request("/v1.0/article/save", element.buildParams(), mListener, true);
    }

    /**
     * 第三方支付方式获取二维码图片
     *
     * @param type
     * @param money
     * @param listener
     */
    public void getThirdImage(int type, int money, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doThirdImage");

        Request("/v1.0/charge/qrcode/id/" + type + "/amount/" + money, null, mListener, false);
    }

    /**
     * 添加邀请码接口
     *
     * @param code
     * @param listener
     */
    public void createInvite(String code, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doCreateInvite");

        Request("/v1.0/user/invite/" + code, null, mListener, false);
    }

    /**
     * 获取邀请码
     *
     * @param listener
     */
    public void getInviteCode(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doGetInviteCode");

        Request("/v1.0/user/inviteCode", null, mListener, false);
    }

    /**
     * 推荐收入接口
     *
     * @param listener
     */
    public void expertMoney(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doExpertMoney");

        Request("/v1.0/expert/money", null, mListener, false);
    }

    /**
     * 查看专家详情
     *
     * @param userId
     * @param listener
     */
    public void getExpertDetails(String userId, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doExpertDetails");

        Request("/v1.0/expert/detail/android/userID/" + userId, null, mListener, false);
    }

    /**
     * 推荐的比赛详情
     *
     * @param id
     * @param listener
     */
    public void getRecommentMatchDetails(String id, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doRecommentMatchDetails");

        Request("/v1.0/recomm/id/" + id, null, mListener, false);
    }

    /**
     * 按用户名字模糊搜索专家
     *
     * @param element
     * @param offset
     * @param limit
     * @param listener
     */
    public void getInfoByName(ExpertInfoByNameElement element, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doExpertInfoByName");

        Request("/v1.0/expert/search/name/page/" + offset + "/count/" + limit + "", element.buildParams(), mListener, true);
    }

    /**
     * 增加用户收货地址
     *
     * @param element
     * @param listener
     */
    public void getAddressInfo(AddressInfoElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doUserAddressInfo");

        Request("/v1.0/user/address", element.buildParams(), mListener, true);
    }

    /**
     * 获取用户收货地址
     *
     * @param listener
     */
    public void getUserAddress(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doUserAddress");

        Request("/v1.0/user/get/address", null, mListener, false);
    }

    /**
     * 意见反馈
     *
     * @param element
     * @param listener
     */
    public void feedback(FeedBackElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doFeedBack");

        Request("/v1.0/feedback", element.buildParams(), mListener, true);
    }

    /**
     * 获取我的信息列表
     *
     * @param offset
     * @param limit
     * @param listener
     */
    public void getMyMsgList(int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMyMsgList");

        Request("/v1.0/msg/list/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 清空我的消息
     *
     * @param listener
     */
    public void clearMsg(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doClearMsg");

        Request("/v1.0/msg/clear", null, mListener, false);
    }

    /**
     * 获取试玩账户
     *
     * @param listener
     */
    public void getDemoUser(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doGetDemoUser");

        Request("/v1.0/user/reg/trial/msg", null, mListener, false);
    }

    /**
     * 用户提现详情
     *
     * @param listener
     */
    public void getUserWithdrawalsDetails(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doUserWithdrawalsDetails");

        Request("/v1.0/extract/msg/detail", null, mListener, false);
    }

    /**
     * 比赛的竞猜
     *
     * @param matchId
     * @param listener
     */
    public void getMatchResult(String matchId, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchResult");

        Request("/v1.0/pay/matchID/" + matchId, null, mListener, false);
    }

    //篮球信息
    public void getMatchBasketBallResult(String matchId, String status, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchBasketBallResult");

        Request("/v1.0/pay/ball/matchID/" + matchId + "/statue/" + status, null, mListener, false);
    }

    //篮球 我的盘口
    public void getMatchMyBasketBallResult(int matchId, String status, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchBasketBallResult");

        Request("/v1.0/pay/fouce/matchID/" + matchId + "/statue/" + status, null, mListener, false);
    }

    /**
     * 关注赔率玩法
     */
    public void focusMatch(FocusMatchElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doFocusMatchResult");

        Request("/v1.0/fouceMatch/payRateball", element.buildParams(), mListener, true);
    }

    //篮球 最后一位
    public void getMatchBasketLastResult(int matchId, String status, OnCcListener listener) {
        CcListener mListemer = new CcListener(listener, "doMatchBasketLastResult");

        Request("/v1.0/pay/score/ball/matchID/" + matchId + "/statue/" + status, null, mListemer, false);
    }

    //篮球 我的盘口最后一位
    public void getMatchMyBasketLastResult(int matchId, String status, OnCcListener listener) {
        CcListener mListemer = new CcListener(listener, "doMatchBasketLastResult");

        Request("/v1.0/pay/fouce/ball/matchID/" + matchId + "/statue/" + status, null, mListemer, false);
    }

    /**
     * 赛前列表
     *
     * @param listener
     */
    public void getMatchGuessList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchGuessList");

        Request("/v1.0/match/guess", null, mListener, false);
    }

    /**
     * 篮球赛前列表
     *
     * @param listener
     */
    public void getMatchBallGuessList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchBallGuessList");

        Request("/v1.0/match/ball/guess", null, mListener, false);
    }

    /**
     * 篮球早盘列表
     *
     * @param listener
     */
    public void getMatchBallGuessEarlyList(String date, int sType, String leagusIDs, int page, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchBallEarlyList");

        Request("/v1.0/match/ball/guess/date/" + date + "/leagueIDs/" + leagusIDs + "/stype/" + sType + "/page/" + page + "/count/" + limit, null, mListener, false);
    }

    /**
     * 充值类型列表
     *
     * @param listener
     */
    public void getChargeList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doChargeList");

        Request("/v1.0/charge/type", null, mListener, false);
    }

    /**
     * 10.	根据文章类别获取文章类别列表
     *
     * @param id
     * @param listener
     */
    public void getAllArticelData(String id, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAllArticleList");

        Request("/v1.0/articleCategory/typeID/" + id, null, mListener, false);
    }

    /**
     * 支付状态提交
     *
     * @param element
     * @param listener
     */
    public void orderStausUpload(OrderStatusElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doOrderStatusUpload");

        Request("/v1.0/awardExchange/order", element.buildParams(), mListener, true);
    }

    /**
     * 用户信息统计
     *
     * @param userId
     * @param listener
     */
    public void uploadUserInfo(String userId, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doUploadUserInfo");

        Request("/v1.0/user/userInfo/userID/" + userId, null, mListener, false);
    }

    /**
     * 用户签到
     *
     * @param listener
     */
    public void userSign(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doUserSign");

        Request("/v1.0/userTask", null, mListener, true);
    }

    /**
     * 提现记录查询
     *
     * @param type
     * @param offset
     * @param limit
     * @param listener
     */
    public void withDrawalsHistory(int type, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doWithDrawalsHistory");

        Request("/v1.0/extract/record/type/" + type + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 娱乐场接口
     *
     * @param listener
     */
    public void getDisportList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doDisportList");

        Request("/v1.0/disport/list", null, mListener, false);
    }

    /**
     * 设置文字滑动
     *
     * @param listener
     */
    public void getSysMessageList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doSysMessageList");

        Request("/v1.0/sysMessage/list", null, mListener, false);
    }

    /**
     * 主页数据
     *
     * @param listener
     */
    public void getMainDataList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMainDataList");

        Request("/v1.0/home", null, mListener, false);
    }

    /**
     * 竞猜比分列表
     */
    public void getScoreByIdList(String matchId, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doScoreByIdList");

        Request("/v1.0/pay/score/matchID/" + matchId, null, mListener, false);
    }

    /**
     * 根据比赛ID获取篮球赔率列表接口（优化后的）
     *
     * @param isFootBall
     * @param matchId
     * @param listener
     */
    public void getPayListById(boolean isFootBall, String matchId, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doPayByIdList");

        Request("/v1.0/pay/" + (isFootBall ? "" : "ball/") + "matchID/" + matchId, null, mListener, false);
    }

    /**
     * 竞猜比分投注
     *
     * @param element
     * @param listener
     */
    public void payBallScore(PayBallScoreElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doPayBallScore");

        Request("/v1.0/payBall/score", element.buildParams(), mListener, true);
    }

    /**
     * 专项活动数据
     *
     * @param listener
     */
    public void getActivityList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doActivityList");

        Request("/v1.0/activity", null, mListener, false);
    }

    /**
     * 篮球筛选列表
     *
     * @param type
     * @param subType
     * @param date
     * @param listener
     */
    public void getMatchBasketBallFilterList(int type, int subType, String date, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchBasketBallFilterList");

        Request("/v1.0/match/ball/filter/type/" + type + "/subType/" + subType + "/date/" + date, null, mListener, false);
    }

    /**
     * 足球筛选列表
     *
     * @param type
     * @param subType
     * @param date
     * @param listener
     */
    public void getMatchFilterList(int type, int subType, String date, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchFilterList");

        Request("/v1.0/match/filter/type/" + type + "/subType/" + subType + "/date/" + date, null, mListener, false);
    }

    /**
     * 篮球赛前筛选列表
     *
     * @param listener
     */
    public void getMatchBallFilterList(int type, int subType, String date, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchBallFilterList");

        Request("/v1.0/match/ball/filter/type/" + type + "/subType/" + subType + "/date/" + date, null, mListener, false);
    }

    /**
     * 账号明细
     *
     * @param type
     * @param offset
     * @param limit
     * @param listener
     */
    public void getAccount(int type, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doGetAccount");

        Request("/v1.0/accountDetail/type/" + type + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 猜球-筛选项列表
     *
     * @param listener
     */
    public void getMatchFilter(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchFilter");

        Request("/v1.0/match/filter", null, mListener, false);
    }

    /**
     * 猜球-根据筛选项获取赛前列表
     *
     * @param listener
     */
    public void getMatchGuessLeagueIds(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchGuessLeague");

        Request("/v1.0/match/guess/leagueIDs/", null, mListener, false);
    }

    /**
     * 根据筛选获取篮球赛前列表
     *
     * @param listener
     */
    public void getMatchBallGuessLeagueIds(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchBallGuessLeague");

        Request("/v1.0/match/guess/ball/leagueIDs/", null, mListener, false);
    }

    /**
     * 比赛的分析
     *
     * @param matchId
     * @param listener
     */
    public void getMatchAnalyAll(int matchId, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchFx");

        Request("/v1.0/matchAnaly/all/matchID/" + matchId, null, mListener, false);
    }

    /**
     * 下注 (独赢、大小、让球界面)
     *
     * @param element
     * @param listener
     */
    public void payBall(PayBallElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doPayBall");

        Request("/v1.0/payBall", element.buildParams(), mListener, true);
    }

    /**
     * 串关下注
     *
     * @param element
     * @param listener
     */
    public void payBallDouble(PayBallElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doPayBall");

        Request("/v1.0/payBall/double", element.buildParams(), mListener, true);
    }

    /**
     * 下注  波胆、赛果、冠军、总入球、半场/全场
     *
     * @param element
     * @param listener
     */
    public void payBallScore(PayBallElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doPayBall");

        Request("/v1.0/payBall/score", element.buildParams(), mListener, true);
    }

    /**
     * a)	请求地址：/v1.0/matchMessage/matchID/{matchID}
     * b)	请求方式:Get
     * c)	请求参数说明：id：比赛ID
     * auth_token：登陆后加入请求头
     * <p>
     * 篮球
     * a)	请求地址：
     * /v1.0/matchMsgBall/matchID/{matchID}
     * b)	请求方式:
     * get
     * c)	请求参数说明
     */
    public void matchInfoMessage(boolean b, int matchId, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchInfoMessage");

        Request("/v1.0/" + (b ? "matchMessage" : "matchMsgBall") + "/matchID/" + matchId, null, mListener, false);
    }

    /**
     * down 时间
     *
     * @param b
     * @param matchId
     * @param time
     * @param listener
     */
    public void matchInfoMessageTime(boolean b, int matchId, String time, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchInfoDownTimeMessage");

        Request("/v1.0/" + (b ? "matchMessage" : "matchMsgBall") + "/matchID/" + matchId + "/down/time/" + time, null, mListener, false);
    }

    /**
     * up 时间
     *
     * @param b
     * @param matchId
     * @param time
     * @param listener
     */
    public void matchInfoMessageUpTime(boolean b, int matchId, String time, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchInfoUpTimeMessage");

        Request("/v1.0/" + (b ? "matchMessage" : "matchMsgBall") + "/matchID/" + matchId + "/up/time/" + time, null, mListener, false);
    }

    /**
     * 赔率列表
     *
     * @param matchId
     * @param offset
     * @param limit
     * @param listener
     */
    public void getPayTypeList(int matchId, int type, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doPayTypeList");

        Request("/v1.0/pay/type/" + type + "/matchID/" + matchId + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 实况统计
     *
     * @param matchId
     * @param listener
     */
    public void getMatchCountById(int matchId, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchCount");

        Request("/v1.0/matchDetail/count/matchID/" + matchId, null, mListener, false);
    }

    /**
     * 实况事件
     *
     * @param matchId
     * @param listener
     */
    public void getMatchDetailsEvent(int matchId, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchDetailsEvent");

        Request("/v1.0/matchDetail/event/matchID/" + matchId, null, mListener, false);
    }

    /**
     * 阵容
     *
     * @param matchId
     * @param listener
     */
    public void getMatchDetailsPlay(int matchId, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchDetailsPlay");

        Request("/v1.0/matchDetail/play/matchID/" + matchId, null, mListener, false);
    }

    /**
     * 赛事推荐
     *
     * @param matchId
     * @param offset
     * @param limit
     * @param listener
     */
    public void getRecommMatchList(int matchId, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doRecommMatchList");

        Request("/v1.0/recomm/matchID/" + matchId + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 滚球列表
     *
     * @param b
     * @param mDate
     * @param offset
     * @param limit
     * @param listener
     */
    public void getScrollBallList(boolean b, int type, String mDate, int sType, String leagueIDs, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doScrollBallList");

        if (b) {
            Request("/v1.0/match/type/" + type + "/date/" + mDate + "/subType/0/leagueIDs/" + leagueIDs + "/stype/" + sType + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
        } else {
            Request("/v1.0/match/roll/ball/subType/0/leagueIDs/" + leagueIDs + "/stype/" + sType + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
        }
    }

    //足球早盘数据
    public void getEarlyFootBallList(String date, int sType, String leagusIDs, int page, int count, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doEarlyFootBallList");

        Request("/v1.0/match/guess/date/" + date + "/leagueIDs/" + leagusIDs + "/stype/" + sType + "/page/" + page + "/count/" + count, null, mListener, false);
    }

    /**
     * 冠军
     *
     * @param type     1==足球  2==篮球
     * @param listener
     */
    public void getChampionList(int type, int sType, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doChampionList");

        Request("/v1.0/pay/champions/type/" + type + "/stype/" + sType + "/page/" + offset + "/count/" + limit, null, mListener, false);
    }

    /**
     * 篮球
     *
     * @param b
     * @param mFormatData
     * @param mCount
     * @param listener
     */
    public void getMatchBallOrTypeList(boolean b, int type, String mFormatData, String subType, int sType, String leagueIDs, int page, int mCount, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchBallTypeList");

        if (b) {
            Request("/v1.0/match/type/" + type + "/date/" + mFormatData + "/subType/" + subType + "/leagueIDs/" + leagueIDs + "/stype/" + sType + "/page/" + page + "/count/" + mCount, null, mListener, false);
        } else {
            Request("/v1.0/match/ball/type/" + type + "/date/" + mFormatData + "/subType/" + subType + "/leagueIDs/" + leagueIDs + "/stype/" + sType + "/page/" + page + "/count/" + mCount, null, mListener, false);
        }
    }

    /**
     * 财富榜
     *
     * @param offset
     * @param listener
     */
    public void getMyOrderList(int offset, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMyOrderList");

        Request("/v1.0/order/page/" + offset + "/count/10", null, mListener, false);
    }

    /**
     * 盈利榜
     *
     * @param type
     * @param time
     * @param listener
     */
    public void getMyYLlist(int type, String time, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMyYlList");

        Request("/v1.0/order/type/" + type + "/time/" + time, null, mListener, false);
    }

    /**
     * 我的跟帖
     *
     * @param offset
     * @param limit
     * @param listener
     */
    public void getMyFollowArticle(int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMyFollowArticle");

        Request("/v1.0/article/follow/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 我的帖子
     *
     * @param offset
     * @param limit
     * @param listener
     */
    public void getMyArticleList(int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMyArticleList");

        Request("/v1.0/article/mine/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 成就奖励
     *
     * @param listener
     */
    public void getAchieveList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAchieveList");

        Request("/v1.0/achieve/list", null, mListener, false);
    }

    /**
     * 任务列表
     *
     * @param listener
     */
    public void getTaskList(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doTaskList");

        Request("/v1.0/userTask/task", null, mListener, false);
    }

    /**
     * 七天签到数据
     *
     * @param listener
     */
    public void getTaskDay(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doDayTask");

        Request("/v1.0/userTask/day", null, mListener, false);
    }

    /**
     * 充值记录查询
     *
     * @param type
     * @param offset
     * @param limit
     * @param listener
     */
    public void getChargeHistoryList(int type, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doChargeHistoryList");

        Request("/v1.0/charge/record/type/" + type + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 启动图数据
     *
     * @param listener
     */
    public void getGuideData(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doGuideData");

        Request("/v1.0/advert/start", null, mListener, false);
    }

    /**
     * 专家/用户粉丝
     *
     * @param userId
     * @param type
     * @param offset
     * @param count
     * @param listener
     */
    public void getFansList(String userId, int type, int offset, int count, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doFansList");

        Request("/v1.0/fouce/other/id/" + userId + "/type/" + type + "/page/" + offset + "/count/" + count + "", null, mListener, false);
    }

    /**
     * 我的粉丝
     *
     * @param type
     * @param offset
     * @param limit
     * @param listener
     */
    public void getMyFansList(int type, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMyFansList");

        Request("/v1.0/fouce/list/type/" + type + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 专家的关注
     *
     * @param offset
     * @param limit
     * @param listener
     */
    public void getExpertHotList(int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doExpertHotList");

        Request("/v1.0/expert/day/hot/3/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * @param b
     * @param listener
     */
    public void getMatchGuessCg(boolean b, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchGuessCg");

        Request((b ? "/v1.0/match/guess/cg" : "/v1.0/match/guess/ball/cg"), null, mListener, false);
    }

    /**
     * @param b
     * @param params
     * @param listener
     */
    public void getMatchGuessByIds(boolean b, String params, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doMatchGuessByIds");

        Request((b ? "/v1.0/match/guess/cg/leagueIDs/" : "/v1.0/match/guess/ball/cg/leagueIDs/") + params, null, mListener, false);
    }

    /**
     * 串关下注
     *
     * @param element
     * @param listener
     */
    public void payBallDouble(PayBallDoubleElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doPayBallDouble");

        Request("/v1.0/payBall/double", element.buildParams(), mListener, true);
    }

    /**
     * 我的收藏
     *
     * @param offset
     * @param limit
     * @param listener
     */
    public void getFouceArticleList(int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doFouceArticleList");

        Request("/v1.0/fouceArticle/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 社区我的收藏
     *
     * @param offset
     * @param limit
     * @param listener
     */
    public void getWcollectList(int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doWcollectList");

        Request("/v1.0/wCollect/page/" + offset + "/count/" + limit, null, mListener, false);
    }

    /**
     * 我的推荐全场赛果
     *
     * @param type
     * @param offset
     * @param limit
     * @param listener
     */
    public void getRecommPage(int type, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doRecommPage");

        Request("/v1.0/recomm/type/" + type + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 红人数据
     *
     * @param type
     * @param offset
     * @param limit
     * @param listener
     */
    public void getHotDataPage(int type, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doHotDataPage");

        Request("/v1.0/expert/day/hot/" + type + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 专家推荐
     *
     * @param type
     * @param userId
     * @param offset
     * @param limit
     * @param listener
     */
    public void getRecommExpert(int type, String userId, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doRecommentExpert");

        Request("/v1.0/recomm/type/" + type + "/userID/" + userId + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 五大联赛
     *
     * @param type
     * @param offset
     * @param limit
     * @param listener
     */
    public void getFiveDataList(int type, int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doFiveDataList");

        Request("/v1.0/writes/type/" + type + "/page/" + offset + "/count/" + limit + "", null, mListener, false);
    }

    /**
     * 钻石兑换
     *
     * @param element
     * @param listener
     */
    public void diamondExchange(DiamondExchangeElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doDiamondExchange");

        Request("/v1.0/awardExchange/zs", element.buildParams(), mListener, true);
    }

    /**
     * 钻石兑换2
     *
     * @param element
     * @param listener
     */
    public void diamondExchange2(DiamondExchangeElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doDiamondExchange2");

        Request("/v1.0/awardExchange/zs", element.buildParams(), mListener, true);
    }

    /**
     * 支付宝
     *
     * @param element
     * @param listener
     */
    public void awardExchangeProps(DiamondExchangeElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAwardExchangeProps");

        Request("/v1.0/awardExchange/props", element.buildParams(), mListener, true);
    }

    /**
     * 支付宝
     *
     * @param element
     * @param listener
     */
    public void awardExchangeProps2(DiamondExchangeElement element, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAwardExchangeProps2");

        Request("/v1.0/awardExchange/props", element.buildParams(), mListener, true);
    }

    /**
     * 奖品列表
     *
     * @param type
     * @param listener
     */
    public void getAwardType(int type, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAwardTypeList");

        Request("/v1.0/award/type/" + type + "", null, mListener, false);
    }

    /**
     * 奖品列表
     *
     * @param type
     * @param listener
     */
    public void getAwardType2(int type, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAwardTypeList2");

        Request("/v1.0/award/type/" + type + "", null, mListener, false);
    }

    /**
     * 轮播信息
     *
     * @param type
     * @param listener
     */
    public void getAwardMessageType(int type, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAwardMessageTypeList");

        Request("/v1.0/award/msg/type/" + type + "", null, mListener, false);
    }

    /**
     * 转盘
     *
     * @param listener
     */
    public void awardTurn(OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doAwardTurn");

        Request("/v1.0/award/turn", null, mListener, false);
    }

    /**
     * 中奖纪录
     *
     * @param offset
     * @param limit
     * @param listener
     */
    public void getRankList(int offset, int limit, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doRankListResult");

        Request("/v1.0/sysMessage/winning/list/page/" + offset + "/count/" + limit, null, mListener, false);
    }

    /**
     * 篮球详情获取头部信息
     *
     * @param matchId
     * @param listener
     */
    public void getBasketBallHeadInfo(int matchId, OnCcListener listener) {
        CcListener mListener = new CcListener(listener, "doBasketBallHeadInfoResult");

        Request("/v1.0/match/ball/part/matchID/" + matchId, null, mListener, false);
    }

    public interface OnCcListener {
        void onResponse(CcApiResult result);
    }

    private class CcListener implements Callback {
        public final String mTag;
        public final OnCcListener mListener;
        public boolean shouldCheckRouter = false;
        public boolean handled = false;

        public CcListener(OnCcListener listener, String tag) {
            mTag = tag;
            mListener = listener;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            if (call.isCanceled()) return;

            if (handled) return;

            //记录网络请求失败的原因
            if (NetUtil.isConnected(mContext)) {
                Request request = call.request();
                String url = request.url().toString().split("[?]")[0];
                //请求超时不记录错误日志
                if (!(e instanceof SocketTimeoutException) && !(e instanceof UnknownHostException)) {
                    Logger.e(TAG, "Exception " + url + "=" + e);
                }
            }

            final int statusCode = 1;

            final String errMessage = "Network connect failed";
            AndroidUtilities.runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (mListener != null)
                        mListener.onResponse(new CcApiResult(statusCode, errMessage));
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (call.isCanceled()) return;

            Request request = call.request();
            String url = request.url().toString();

            if (response == null || !response.isSuccessful()) {
                int statusCode = 1;
                String errMessage = "Network connect failed";
                if (null != response) {
                    statusCode = response.code();
                    if (statusCode >= 500) {
                        statusCode = 500;
                    } else if (statusCode == 401 || statusCode == 403) {
                        //不变
                    } else {
                        statusCode = 1;
                    }
                    errMessage = response.message();

                    if (statusCode > 1) {
                        Logger.e(TAG, new IOException("Unexpected code " + response));
                    }
                }

                if (handled) {
                    Logger.d(TAG, url + ": too slow");
                    return;
                }
                handled = true;

                if (shouldCheckRouter) {
                    for (int i = 0; i < API_URLS.length; i++) {
                        if (url.startsWith(API_URLS[i])) {
                            setRouterIndex(i);
                            break;
                        }
                    }
                }
                Logger.d(TAG, "Error Response Status code:" + String.valueOf(statusCode) + ", Error message:" + errMessage);
                final int finalStatusCode = statusCode;
                final String finalErrMessage = errMessage;
                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null)
                            mListener.onResponse(new CcApiResult(finalStatusCode, finalErrMessage));
                    }
                });
            } else {
                ResponseBody responseBody = response.body();

                String arg0;

                try {
                    arg0 = responseBody.string();
                } catch (SocketTimeoutException e) {
                    Logger.d(TAG, "read-timeout:" + url);
                    final int statusCode = 1;

                    final String errMessage = "Network connect failed";

                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null)
                                mListener.onResponse(new CcApiResult(statusCode, errMessage));
                        }
                    });
                    return;
                } catch (Exception ex) {
                    Logger.d(TAG, "Exception:" + url);
                    final int statusCode = 1;
                    final String errMessage = "Network connect failed";
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null)
                                mListener.onResponse(new CcApiResult(statusCode, errMessage));
                        }
                    });
                    return;
                }
                if (handled) {
                    Logger.d(TAG, url + ": too slow");
                    return;
                }

                handled = true;

                if (shouldCheckRouter) {
                    for (int i = 0; i < API_URLS.length; i++) {
                        if (url.startsWith(API_URLS[i])) {
                            setRouterIndex(i);
                            break;
                        }
                    }
                }

                Logger.d(TAG, url + ": " + arg0);

                final CcApiResult mRes = new CcApiResult();

                //数据被篡改
                if (!arg0.startsWith("{")) {
                    AndroidUtilities.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mListener != null)
                                mListener.onResponse(new CcApiResult(1, "Network connect failed"));
                        }
                    });
                    return;
                }

                if (mTag.equals("doUserBind") || mTag.equals("getUserInfo") || mTag.equals("doFouceMatch") || mTag.equals("doUodatePassWord")
                        || mTag.equals("doThreeLogin") || mTag.equals("doLogin") || mTag.equals("doFouceMatchBall") || mTag.equals("doRegistTrial")) {
                    mRes.fromUserInfoResult(arg0);
                } else if (mTag.equals("doCategoryList")) {
                    mRes.fromCategoryListResult(arg0);
                } else if (mTag.equals("doMyPayBallList") || mTag.equals("doMyPayBallUnCheck") || mTag.equals("doRecentType")) {
                    mRes.fromMyBallHistoryResult(arg0);
                } else if (mTag.equals("doRecentType2")) {
                    mRes.fromRecentType2Result(arg0);
                } else if (mTag.equals("doBankList")) {
                    mRes.fromBankListResult(arg0);
                } else if (mTag.equals("doAccountList")) {
                    mRes.fromAccountListResult(arg0);
                } else if (mTag.equals("doArticleList")) {
                    mRes.fromArticleListResult(arg0);
                } else if (mTag.equals("doExpertGuessList")) {
                    mRes.fromExpertGuessListResult(arg0);
                } else if (mTag.equals("doShareInfo")) {
                    mRes.fromShareInfoResult(arg0);
                } else if (mTag.equals("doArticleDetails")) {
                    mRes.fromArticleDetailsResult(arg0);
                } else if (mTag.equals("doCommentDetailsList")) {
                    mRes.fromCommentDetailsListResult(arg0);
                } else if (mTag.equals("doRecommHot")) {
                    mRes.fromRecommHotListResult(arg0);
                } else if (mTag.equals("doNotices")) {
                    mRes.fromNoticesResult(arg0);
                } else if (mTag.equals("doVersionIsUpdate")) {
                    mRes.fromVersionIsUpdateResult(arg0);
                } else if (mTag.equals("doMatchData")) {
                    mRes.fromMatchDataResult(arg0);
                } else if (mTag.equals("foArticleListById")) {
                    mRes.fromArticleListByIdResult(arg0);
                } else if (mTag.equals("doMatchVoice")) {
                    mRes.fromMatchVoiceResult(arg0);
                } else if (mTag.equals("doMyInfoTypeList")) {
                    mRes.fromMyInfoTypeListResult(arg0);
                } else if (mTag.equals("doReportList")) {
                    mRes.fromRepostListResult(arg0);
                } else if (mTag.equals("doGiftHistory") || mTag.equals("doAwardExchangeList")) {
                    mRes.fromGiftHistoryResult(arg0);
                } else if (mTag.equals("doAgentInfo")) {
                    mRes.fromAgentInfoResult(arg0);
                } else if (mTag.equals("doAgentCodeList")) {
                    mRes.fromAgentCodeListResult(arg0);
                } else if (mTag.equals("doAgentCode")) {
                    mRes.fromAgentCodeResult(arg0);
                } else if (mTag.equals("doAgentCheckList")) {
                    mRes.fromAgentCheckListResult(arg0);
                } else if (mTag.equals("doAgentRemark")) {
                    mRes.fromAgentRemarkResult(arg0);
                } else if (mTag.equals("doThirdImage")) {
                    mRes.fromThirdImage(arg0);
                } else if (mTag.equals("doGetInviteCode")) {
                    mRes.fromInviteCodeResult(arg0);
                } else if (mTag.equals("doExpertMoney")) {
                    mRes.fromExpertMoneyResult(arg0);
                } else if (mTag.equals("doExpertDetails")) {
                    mRes.fromExpertDetailsResult(arg0);
                } else if (mTag.equals("doRecommentMatchDetails")) {
                    mRes.fromRecommentMatchDetailsResult(arg0);
                } else if (mTag.equals("doExpertInfoByName")) {
                    mRes.fromExpertInfoByNameResult(arg0);
                } else if (mTag.equals("doUserAddress")) {
                    mRes.fromUserAddressResult(arg0);
                } else if (mTag.equals("doMyMsgList")) {
                    mRes.fromMyMsgListResult(arg0);
                } else if (mTag.equals("doGetDemoUser")) {
                    mRes.fromDemoUserResult(arg0);
                } else if (mTag.equals("doUserWithdrawalsDetails")) {
                    mRes.fromUserWithdrawalsDetailsResult(arg0);
                } else if (mTag.equals("doMatchResult") || mTag.equals("doPayByIdList") || mTag.equals("doMatchBasketBallResult")) {
                    mRes.fromMatchResult(arg0);
                } else if (mTag.equals("doMatchGuessList") || mTag.equals("doMatchBallGuessList") || mTag.equals("doMatchGuessLeague") || mTag.equals("doMatchBallGuessLeague")) {
                    mRes.fromMatchGuessListResult(arg0);
                } else if (mTag.equals("doChargeList")) {
                    mRes.fromChargeListResult(arg0);
                } else if (mTag.equals("doAllArticleList")) {
                    mRes.fromAllArticleListResult(arg0);
                } else if (mTag.equals("doUploadUserInfo")) {
                    mRes.fromUploadUserInfo(arg0);
                } else if (mTag.equals("doUserSign")) {
                    mRes.fromUserSignResult(arg0);
                } else if (mTag.equals("doWithDrawalsHistory")) {
                    mRes.fromWithDrawalsHistoryResult(arg0);
                } else if (mTag.equals("doDisportList")) {
                    mRes.fromDisportListResult(arg0);
                } else if (mTag.equals("doSysMessageList")) {
                    mRes.fromSysMessageListResult(arg0);
                } else if (mTag.equals("doMainDataList")) {
                    mRes.fromMainDataListResult(arg0);
                } else if (mTag.equals("doScoreByIdList")) {
                    mRes.fromScoreByIdListResult(arg0);
                } else if (mTag.equals("doMatchFilterList") || mTag.equals("doMatchBallFilterList") || mTag.equals("doMatchFilter") || mTag.equals("doMatchBasketBallFilterList")) {
                    mRes.fromMatchFilterListResult(arg0);
                } else if (mTag.equals("doActivityList")) {
                    mRes.fromActivityListResult(arg0);
                } else if (mTag.equals("doGetAccount")) {
                    mRes.fromAccountResult(arg0);
                } else if (mTag.equals("doMatchFx")) {
                    mRes.fromMatchFxResult(arg0);
                } else if (mTag.equals("doMatchInfoMessage") || mTag.equals("doMatchInfoUpTimeMessage") || mTag.equals("doMatchInfoDownTimeMessage")) {
                    mRes.fromMatchInfoMessageResult(arg0);
                } else if (mTag.equals("doPayTypeList")) {
                    mRes.fromPayTypeListResult(arg0);
                } else if (mTag.equals("doMatchCount")) {
                    mRes.fromMatchCountResult(arg0);
                } else if (mTag.equals("doMatchDetailsEvent")) {
                    mRes.fromMatchDetailsEventResult(arg0);
                } else if (mTag.equals("doMatchDetailsPlay")) {
                    mRes.fromMatchDetailsPlayResult(arg0);
                } else if (mTag.equals("doRecommMatchList")) {
                    mRes.fromRecommMatchListResult(arg0);
                } else if (mTag.equals("doScrollBallList") || mTag.equals("doMatchBallTypeList")) {
                    mRes.fromScrollBallListResult(arg0);
                } else if (mTag.equals("doEarlyFootBallList") || mTag.equals("doMatchBallEarlyList")) {
                    mRes.fromEarlyListResult(arg0);
                } else if (mTag.equals("doMyOrderList")) {
                    mRes.fromMyOrderListResult(arg0);
                } else if (mTag.equals("doMyYlList")) {
                    mRes.fromMyYlListResult(arg0);
                } else if (mTag.equals("doMyFollowArticle")) {
                    mRes.fromMyFollowArticleResult(arg0);
                } else if (mTag.equals("doMyArticleList") || mTag.equals("doFouceArticleList")) {
                    mRes.fromMyArticleListResult(arg0);
                } else if (mTag.equals("doAchieveList")) {
                    mRes.fromAchieveListResult(arg0);
                } else if (mTag.equals("doTaskList")) {
                    mRes.fromTaskListResult(arg0);
                } else if (mTag.equals("doDayTask")) {
                    mRes.fromDayTaskResult(arg0);
                } else if (mTag.equals("doChargeHistoryList")) {
                    mRes.fromChargeHistoryListResult(arg0);
                } else if (mTag.equals("doGuideData")) {
                    mRes.fromGuideDataResult(arg0);
                } else if (mTag.equals("doFansList") || mTag.equals("doExpertHotList")) {
                    mRes.fromFansListResult(arg0);
                } else if (mTag.equals("doMyFansList")) {
                    mRes.fromMyFansListResult(arg0);
                } else if (mTag.equals("doMatchGuessCg") || mTag.equals("doMatchGuessByIds")) {
                    mRes.fromMatchGuessCgResult(arg0);
                } else if (mTag.equals("doWcollectList")) {
                    mRes.fromWcollectListResult(arg0);
                } else if (mTag.equals("doRecommPage")) {
                    mRes.fromRecommPageResult(arg0);
                } else if (mTag.equals("doHotDataPage")) {
                    mRes.fromHotDataPageResult(arg0);
                } else if (mTag.equals("doRecommentExpert")) {
                    mRes.fromRecommExpertResult(arg0);
                } else if (mTag.equals("doFiveDataList")) {
                    mRes.fromFiveDataListResult(arg0);
                } else if (mTag.equals("doDiamondExchange") || mTag.equals("doAwardExchangeProps2")) {
                    mRes.fromDiamondExchangeResult(arg0);
                } else if (mTag.equals("doDiamondExchange2") || mTag.equals("doAwardExchangeProps")) {
                    mRes.fromDiamondExchangeResult2(arg0);
                } else if (mTag.equals("doAwardTypeList")) {
                    mRes.fromAwardTypeListResult(arg0);
                } else if (mTag.equals("doAwardTypeList2")) {
                    mRes.fromAwardTypeList2Result(arg0);
                } else if (mTag.equals("doAwardMessageTypeList")) {
                    mRes.fromAwardMessageTypeListResult(arg0);
                } else if (mTag.equals("doAwardTurn")) {
                    mRes.fromAwardTurnResult(arg0);
                } else if (mTag.equals("doChampionList")) {
                    mRes.fromChampionsResult(arg0);
                } else if (mTag.equals("doMatchBasketLastResult")) {
                    mRes.fromMatchBasketLastResult(arg0);
                } else if (mTag.equals("doRankListResult")) {
                    mRes.fromRankListResult(arg0);
                } else if (mTag.equals("doBasketBallHeadInfoResult")) {
                    mRes.fromBasketBallHeadInfoResult(arg0);
                } else {
                    mRes.fromDefaultResult(arg0);
                }

                if (mRes.getErrno() < 0 && TextUtils.isEmpty(mRes.getMessage())) {
                    mRes.setMessage("抱歉, 我们的服务出现了一点故障, 工程师正在紧张修复中, 请稍候再试");
                }

                AndroidUtilities.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mListener != null)
                            mListener.onResponse(mRes);
                    }
                });
            }
        }
    }

    private void Request(String path, String params, final CcListener listener, boolean isPost) {
        if (!NetUtil.isConnected(mContext)) {
            if (listener != null && listener.mListener != null)
                listener.mListener.onResponse(new CcApiResult(1, "Network connect failed"));
            return;
        }
        RequestURL(mBaseUri + path, params, listener, isPost);
    }

    private void RequestURL(String url, String params, final CcListener listener, boolean isPost) {
        Logger.d(TAG, "Request url:" + url);
        Logger.d(TAG, "Request params:" + params);

        Request.Builder builder;

        if (isPost) {
            if (TextUtils.isEmpty(params)) {
                params = "";
            }

            RequestBody body = RequestBody.create(JSON, params);

            builder = new Request.Builder().url(url).post(body);
        } else {
            builder = new Request.Builder().url(url);
        }

        if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
            builder.addHeader(Constent.TOKEN, UIUtils.getSputils().getString(Constent.TOKEN, ""));
            Logger.d(TAG, "token " + UIUtils.getSputils().getString(Constent.TOKEN, ""));
        } else {
            builder.addHeader(Constent.TOKEN, "");
            Logger.d(TAG, "token " + "");
        }
        builder.addHeader("Content-Type", "application/json;charset=UTF-8'");

        if (null != mDevice) {
            String device = mDevice + "&wifi=" + (NetUtil.isConnectedWifi(mContext) ? "1" : "0");
//            builder.addHeader("X-Cc-Device", device);
            Logger.d(TAG, "X-Cc-Device " + device);
        }

        Call call = client.newCall(builder.build());

        call.enqueue(listener);

        if (!calls.containsKey(mRequestId)) {
            calls.put(mRequestId, new ArrayList<Call>());
        }
        calls.get(mRequestId).add(call);
    }
}
