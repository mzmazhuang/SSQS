package com.dading.ssqs.apis;

import com.dading.ssqs.bean.ALLCircleThings;
import com.dading.ssqs.bean.AccountDetailBean;
import com.dading.ssqs.bean.AchieveBean;
import com.dading.ssqs.bean.AllCircleLBean;
import com.dading.ssqs.bean.AllCircleRBean;
import com.dading.ssqs.bean.BankBean;
import com.dading.ssqs.bean.BasketBallLastBean;
import com.dading.ssqs.bean.BettingMBean;
import com.dading.ssqs.bean.BettingTBean;
import com.dading.ssqs.bean.CasinoBean;
import com.dading.ssqs.bean.Champion;
import com.dading.ssqs.bean.CommentsBean;
import com.dading.ssqs.bean.DiamondsGlodRecordBean;
import com.dading.ssqs.bean.EarlyBean;
import com.dading.ssqs.bean.EssayInfoBean;
import com.dading.ssqs.bean.FXBean;
import com.dading.ssqs.bean.GBSeriesBean;
import com.dading.ssqs.bean.GeneralUserBean;
import com.dading.ssqs.bean.GuessACEBean;
import com.dading.ssqs.bean.GusessChoiceBean;
import com.dading.ssqs.bean.HomeBean;
import com.dading.ssqs.bean.HomeMessageBean;
import com.dading.ssqs.bean.InfoPLBean;
import com.dading.ssqs.bean.InviteCodeBean;
import com.dading.ssqs.bean.JCScorebean;
import com.dading.ssqs.bean.JCbean;
import com.dading.ssqs.bean.JPushCheckedBean;
import com.dading.ssqs.bean.LHRankingBean;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.bean.MatchBeforBeanAll;
import com.dading.ssqs.bean.MatchInfoBean;
import com.dading.ssqs.bean.MatchInfoLqBean;
import com.dading.ssqs.bean.MyFollowBean;
import com.dading.ssqs.bean.MyNoReadBean;
import com.dading.ssqs.bean.MyReferBean;
import com.dading.ssqs.bean.MyTzBean;
import com.dading.ssqs.bean.MyTzGTBean;
import com.dading.ssqs.bean.NewInfoBean;
import com.dading.ssqs.bean.NoticegBean;
import com.dading.ssqs.bean.OrderBean;
import com.dading.ssqs.bean.PerferentialBean;
import com.dading.ssqs.bean.PersonalReportBean;
import com.dading.ssqs.bean.ProxyCenterBean;
import com.dading.ssqs.bean.ProxyCmmsionsBean;
import com.dading.ssqs.bean.ProxyCodeBean;
import com.dading.ssqs.bean.ProxyIntroBean;
import com.dading.ssqs.bean.ProxyIntroLookBean;
import com.dading.ssqs.bean.QRCodeBean;
import com.dading.ssqs.bean.RankingBean;
import com.dading.ssqs.bean.RankingBean2;
import com.dading.ssqs.bean.RechargeDetailBean;
import com.dading.ssqs.bean.RedPopleARBean;
import com.dading.ssqs.bean.ReferIncomeBean;
import com.dading.ssqs.bean.ReferInfoARBean;
import com.dading.ssqs.bean.ReferInfoBean;
import com.dading.ssqs.bean.ReferReferBean;
import com.dading.ssqs.bean.SKSJBean;
import com.dading.ssqs.bean.SKTJBean;
import com.dading.ssqs.bean.SKZRBean;
import com.dading.ssqs.bean.SNSCollectBean;
import com.dading.ssqs.bean.SavantFansBean;
import com.dading.ssqs.bean.SavantInfoBean;
import com.dading.ssqs.bean.SavantLeveBean;
import com.dading.ssqs.bean.ScoreBean;
import com.dading.ssqs.bean.SevenPopBean;
import com.dading.ssqs.bean.ShareBean;
import com.dading.ssqs.bean.ShoppingAddBean;
import com.dading.ssqs.bean.SignResultBean;
import com.dading.ssqs.bean.SnsBean;
import com.dading.ssqs.bean.SplashBeanGG;
import com.dading.ssqs.bean.StoreBean;
import com.dading.ssqs.bean.StoreBean2;
import com.dading.ssqs.bean.TaskBean;
import com.dading.ssqs.bean.TodayTopicBean;
import com.dading.ssqs.bean.TryPlayBean;
import com.dading.ssqs.bean.TurnTablePrizeBean;
import com.dading.ssqs.bean.TurnTablePrizeTextBean;
import com.dading.ssqs.bean.TurnTableResultBean;
import com.dading.ssqs.bean.VersionBean;
import com.dading.ssqs.bean.WXDFBean;
import com.dading.ssqs.bean.WXOrderBean;
import com.dading.ssqs.bean.WithDrawBean;
import com.dading.ssqs.bean.WithDrawDetailBean;
import com.dading.ssqs.utils.LogUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/11/20.
 * 解析类
 */

public class CcApiResult {
    private static final int ERRNO_OK = 0;

    private int errno;

    private String message;

    private boolean status;

    private Object data;

    public CcApiResult() {
        this.errno = -1;
    }

    public CcApiResult(int errno) {
        this.setErrno(errno);
    }

    public CcApiResult(int errno, String message) {
        this.setErrno(errno);
        this.setMessage(message);
    }

    public CcApiResult(int errno, String message, Object data) {
        this.setErrno(errno);
        this.setMessage(message);
        this.setData(data);
    }

    private void setErrno(int errno) {
        this.errno = errno;
    }

    public int getErrno() {
        return this.errno;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    private void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }

    public boolean isOk() {
        return getErrno() == ERRNO_OK && status;
    }

    class BooleanTypeAdapter implements JsonSerializer<Boolean>, JsonDeserializer<Boolean> {

        public JsonElement serialize(Boolean value, Type typeOfT, JsonSerializationContext context) {
            return new JsonPrimitive(value ? 1 : 0);
        }

        public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            int code = json.getAsInt();
            return code == 0 ? false : code == 1 ? true : null;
        }
    }

    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        return builder.create();
    }

    public class ResultPage implements Serializable {

        private static final long serialVersionUID = 3293454715860578988L;

        private int totalPage;
        private int totalCount;

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }

    public class ResultAccountPage extends ResultPage {
        private List<DiamondsGlodRecordBean> items = new ArrayList();

        public List<DiamondsGlodRecordBean> getItems() {
            return items;
        }

        public void setItems(List<DiamondsGlodRecordBean> items) {
            this.items = items;
        }
    }

    public class ResultArticlePage extends ResultPage {
        private List<EssayInfoBean> items = new ArrayList<>();

        public List<EssayInfoBean> getItems() {
            return items;
        }

        public void setItems(List<EssayInfoBean> items) {
            this.items = items;
        }
    }

    public class ResultExpertGuessPage extends ResultPage {
        private List<GuessACEBean> items = new ArrayList<>();

        public List<GuessACEBean> getItems() {
            return items;
        }

        public void setItems(List<GuessACEBean> items) {
            this.items = items;
        }
    }

    public class ResultCommentDetailsListPage extends ResultPage {
        private List<CommentsBean> items = new ArrayList<>();

        public List<CommentsBean> getItems() {
            return items;
        }

        public void setItems(List<CommentsBean> items) {
            this.items = items;
        }
    }

    public class ResultLHRankPage extends ResultPage {
        private List<LHRankingBean> items = new ArrayList<>();

        public List<LHRankingBean> getItems() {
            return items;
        }

        public void setItems(List<LHRankingBean> items) {
            this.items = items;
        }
    }

    public class ResultALLCircleThingsPage extends ResultPage {
        private List<ALLCircleThings> items = new ArrayList<>();

        public List<ALLCircleThings> getItems() {
            return items;
        }

        public void setItems(List<ALLCircleThings> items) {
            this.items = items;
        }
    }

    public class ResultSavantLevePage extends ResultPage {
        private List<SavantLeveBean> items = new ArrayList<>();

        public List<SavantLeveBean> getItems() {
            return items;
        }

        public void setItems(List<SavantLeveBean> items) {
            this.items = items;
        }
    }

    public class ResultTodayTopicPage extends ResultPage {
        private List<TodayTopicBean> items = new ArrayList<>();

        public List<TodayTopicBean> getItems() {
            return items;
        }

        public void setItems(List<TodayTopicBean> items) {
            this.items = items;
        }
    }

    public class ResultWithDrawDetailBeanPage extends ResultPage {
        private List<WithDrawDetailBean> items = new ArrayList<>();

        public List<WithDrawDetailBean> getItems() {
            return items;
        }

        public void setItems(List<WithDrawDetailBean> items) {
            this.items = items;
        }
    }

    public class ResultAccountDetailsPage extends ResultPage {
        private List<AccountDetailBean> items = new ArrayList<>();

        public List<AccountDetailBean> getItems() {
            return items;
        }

        public void setItems(List<AccountDetailBean> items) {
            this.items = items;
        }
    }

    public class ResultMatchInfoMessagePage extends ResultPage {
        private List<MatchInfoLqBean> items = new ArrayList<>();

        public List<MatchInfoLqBean> getItems() {
            return items;
        }

        public void setItems(List<MatchInfoLqBean> items) {
            this.items = items;
        }
    }

    public class ResultInfoPLPage extends ResultPage {
        private List<InfoPLBean> items = new ArrayList<>();

        public List<InfoPLBean> getItems() {
            return items;
        }

        public void setItems(List<InfoPLBean> items) {
            this.items = items;
        }
    }

    public class ResultReferReferPage extends ResultPage {
        private List<ReferReferBean> items = new ArrayList<>();

        public List<ReferReferBean> getItems() {
            return items;
        }

        public void setItems(List<ReferReferBean> items) {
            this.items = items;
        }
    }

    public class ResultScorePage extends ResultPage {
        private List<ScoreBean> items = new ArrayList<>();

        public List<ScoreBean> getItems() {
            return items;
        }

        public void setItems(List<ScoreBean> items) {
            this.items = items;
        }
    }

    public class ResultRankingPage extends ResultPage {
        private List<RankingBean> items = new ArrayList<>();

        public List<RankingBean> getItems() {
            return items;
        }

        public void setItems(List<RankingBean> items) {
            this.items = items;
        }
    }

    public class ResultMyTzGTPage extends ResultPage {
        private List<MyTzGTBean> items = new ArrayList<>();

        public List<MyTzGTBean> getItems() {
            return items;
        }

        public void setItems(List<MyTzGTBean> items) {
            this.items = items;
        }
    }

    public class ResultMyTzPage extends ResultPage {
        private List<MyTzBean> items = new ArrayList<>();

        public List<MyTzBean> getItems() {
            return items;
        }

        public void setItems(List<MyTzBean> items) {
            this.items = items;
        }
    }

    public class ResultChargeHistoryPage extends ResultPage {
        private List<RechargeDetailBean> items = new ArrayList<>();

        public List<RechargeDetailBean> getItems() {
            return items;
        }

        public void setItems(List<RechargeDetailBean> items) {
            this.items = items;
        }
    }

    public class ResultFansPage extends ResultPage {
        private List<SavantFansBean> items = new ArrayList<>();

        public List<SavantFansBean> getItems() {
            return items;
        }

        public void setItems(List<SavantFansBean> items) {
            this.items = items;
        }
    }

    public class ResultMyFansPage extends ResultPage {
        private List<MyFollowBean> items = new ArrayList<>();

        public List<MyFollowBean> getItems() {
            return items;
        }

        public void setItems(List<MyFollowBean> items) {
            this.items = items;
        }
    }

    public class ResultSNSCollectPage extends ResultPage {
        private List<SNSCollectBean> items = new ArrayList<>();

        public List<SNSCollectBean> getItems() {
            return items;
        }

        public void setItems(List<SNSCollectBean> items) {
            this.items = items;
        }
    }

    public class ResultMyReferPage extends ResultPage {
        private List<MyReferBean> items = new ArrayList<>();

        public List<MyReferBean> getItems() {
            return items;
        }

        public void setItems(List<MyReferBean> items) {
            this.items = items;
        }
    }

    public class ResultRedPoplePage extends ResultPage {
        private List<RedPopleARBean> items = new ArrayList<>();

        public List<RedPopleARBean> getItems() {
            return items;
        }

        public void setItems(List<RedPopleARBean> items) {
            this.items = items;
        }
    }

    public class ResultReferInfoARPage extends ResultPage {
        private List<ReferInfoARBean> items = new ArrayList<>();

        public List<ReferInfoARBean> getItems() {
            return items;
        }

        public void setItems(List<ReferInfoARBean> items) {
            this.items = items;
        }
    }

    public class ResultSnsPage extends ResultPage {
        public List<SnsBean.TopPicEntity> topPic = new ArrayList<>();
        public List<SnsBean.WritesEntity> writes = new ArrayList<>();
        public int hasTop;

        public List<SnsBean.TopPicEntity> getTopPic() {
            return topPic;
        }

        public void setTopPic(List<SnsBean.TopPicEntity> topPic) {
            this.topPic = topPic;
        }

        public List<SnsBean.WritesEntity> getWrites() {
            return writes;
        }

        public void setWrites(List<SnsBean.WritesEntity> writes) {
            this.writes = writes;
        }

        public int getHasTop() {
            return hasTop;
        }

        public void setHasTop(int hasTop) {
            this.hasTop = hasTop;
        }
    }

    public class ResultChampionPage extends ResultPage {
        private List<Champion> items = new ArrayList<>();

        public List<Champion> getItems() {
            return items;
        }

        public void setItems(List<Champion> items) {
            this.items = items;
        }
    }

    public CcApiResult fromUserInfoResult(String str) {
        Gson gson = getGson();
        LoadingBean bean = new LoadingBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), LoadingBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromCategoryListResult(String str) {
        Gson gson = getGson();
        List<AllCircleLBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<AllCircleLBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromMyBallHistoryResult(String str) {
        Gson gson = getGson();
        List<BettingTBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<BettingTBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromRecentType2Result(String str) {
        Gson gson = getGson();
        List<BettingMBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<BettingMBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromBankListResult(String str) {
        Gson gson = getGson();
        List<BankBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<BankBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromAccountListResult(String str) {
        Gson gson = getGson();
        ResultAccountPage page = new ResultAccountPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultAccountPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromArticleListResult(String str) {
        Gson gson = getGson();
        ResultArticlePage page = new ResultArticlePage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultArticlePage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromExpertGuessListResult(String str) {
        Gson gson = getGson();
        ResultExpertGuessPage page = new ResultExpertGuessPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultExpertGuessPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromShareInfoResult(String str) {
        Gson gson = getGson();
        ShareBean bean = new ShareBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), ShareBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromArticleDetailsResult(String str) {
        Gson gson = getGson();
        NewInfoBean bean = new NewInfoBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), NewInfoBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromCommentDetailsListResult(String str) {
        Gson gson = getGson();
        ResultCommentDetailsListPage page = new ResultCommentDetailsListPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultCommentDetailsListPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromRecommHotListResult(String str) {
        Gson gson = getGson();
        ResultLHRankPage page = new ResultLHRankPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultLHRankPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromNoticesResult(String str) {
        Gson gson = getGson();
        NoticegBean bean = new NoticegBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), NoticegBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromVersionIsUpdateResult(String str) {
        Gson gson = getGson();
        VersionBean bean = new VersionBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), VersionBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromMatchDataResult(String str) {
        Gson gson = getGson();
        MatchInfoBean bean = new MatchInfoBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), MatchInfoBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromArticleListByIdResult(String str) {
        Gson gson = getGson();
        ResultALLCircleThingsPage page = new ResultALLCircleThingsPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultALLCircleThingsPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromMatchVoiceResult(String str) {
        JPushCheckedBean bean = new JPushCheckedBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                bean.setData(json.getInt("data"));
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromMyInfoTypeListResult(String str) {
        Gson gson = getGson();
        MyNoReadBean bean = new MyNoReadBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), MyNoReadBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromRepostListResult(String str) {
        Gson gson = getGson();
        PersonalReportBean bean = new PersonalReportBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), PersonalReportBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromGiftHistoryResult(String str) {
        Gson gson = getGson();
        List<TurnTablePrizeBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<TurnTablePrizeBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromAgentInfoResult(String str) {
        Gson gson = getGson();
        ProxyCenterBean bean = new ProxyCenterBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), ProxyCenterBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromAgentCodeListResult(String str) {
        Gson gson = getGson();
        List<ProxyIntroLookBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<ProxyIntroLookBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromAgentCodeResult(String str) {
        ProxyCodeBean bean = new ProxyCodeBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                bean.setData(json.getString("data"));
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromAgentCheckListResult(String str) {
        Gson gson = getGson();
        ProxyCmmsionsBean bean = new ProxyCmmsionsBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), ProxyCmmsionsBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromAgentRemarkResult(String str) {
        Gson gson = getGson();
        ProxyIntroBean bean = new ProxyIntroBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), ProxyIntroBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromThirdImage(String str) {
        QRCodeBean bean = new QRCodeBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                bean.setData(json.getString("data"));
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromInviteCodeResult(String str) {
        Gson gson = getGson();
        InviteCodeBean bean = new InviteCodeBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), InviteCodeBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromExpertMoneyResult(String str) {
        ReferIncomeBean bean = new ReferIncomeBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                bean.setData(json.getInt("data"));
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromExpertDetailsResult(String str) {
        Gson gson = getGson();
        SavantInfoBean bean = new SavantInfoBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), SavantInfoBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromRecommentMatchDetailsResult(String str) {
        Gson gson = getGson();
        ReferInfoBean bean = new ReferInfoBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), ReferInfoBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromExpertInfoByNameResult(String str) {
        Gson gson = getGson();
        ResultSavantLevePage bean = new ResultSavantLevePage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), ResultSavantLevePage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromUserAddressResult(String str) {
        Gson gson = getGson();
        ShoppingAddBean bean = new ShoppingAddBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), ShoppingAddBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromMyMsgListResult(String str) {
        Gson gson = getGson();
        ResultTodayTopicPage page = new ResultTodayTopicPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultTodayTopicPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromDemoUserResult(String str) {
        Gson gson = getGson();
        TryPlayBean bean = new TryPlayBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), TryPlayBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromUserWithdrawalsDetailsResult(String str) {
        Gson gson = getGson();
        WithDrawBean bean = new WithDrawBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), WithDrawBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromMatchResult(String str) {
        Gson gson = getGson();
        List<JCbean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<JCbean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromMatchGuessListResult(String str) {
        Gson gson = getGson();
        MatchBeforBeanAll bean = new MatchBeforBeanAll();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), MatchBeforBeanAll.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromChargeListResult(String str) {
        Gson gson = getGson();
        List<WXDFBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<WXDFBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromAllArticleListResult(String str) {
        Gson gson = getGson();
        List<AllCircleRBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<AllCircleRBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromUploadUserInfo(String str) {
        Gson gson = getGson();
        GeneralUserBean bean = new GeneralUserBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), GeneralUserBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromUserSignResult(String str) {
        Gson gson = getGson();
        SignResultBean bean = new SignResultBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                bean = gson.fromJson(data.toString(), SignResultBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromWithDrawalsHistoryResult(String str) {
        Gson gson = getGson();
        ResultWithDrawDetailBeanPage page = new ResultWithDrawDetailBeanPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultWithDrawDetailBeanPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromDisportListResult(String str) {
        Gson gson = getGson();
        List<CasinoBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<CasinoBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromSysMessageListResult(String str) {
        Gson gson = getGson();
        List<HomeMessageBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<HomeMessageBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromMainDataListResult(String str) {
        Gson gson = getGson();
        HomeBean bean = new HomeBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), HomeBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromScoreByIdListResult(String str) {
        Gson gson = getGson();
        List<JCScorebean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<JCScorebean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromMatchFilterListResult(String str) {
        Gson gson = getGson();
        List<GusessChoiceBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<GusessChoiceBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromActivityListResult(String str) {
        Gson gson = getGson();
        List<PerferentialBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<PerferentialBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromAccountResult(String str) {
        Gson gson = getGson();
        ResultAccountDetailsPage page = new ResultAccountDetailsPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultAccountDetailsPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromMatchFxResult(String str) {
        Gson gson = getGson();
        FXBean bean = new FXBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), FXBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromMatchInfoMessageResult(String str) {
        Gson gson = getGson();
        ResultMatchInfoMessagePage page = new ResultMatchInfoMessagePage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultMatchInfoMessagePage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromPayTypeListResult(String str) {
        Gson gson = getGson();
        ResultInfoPLPage page = new ResultInfoPLPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultInfoPLPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromMatchCountResult(String str) {
        Gson gson = getGson();
        List<SKTJBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<SKTJBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromMatchDetailsEventResult(String str) {
        Gson gson = getGson();
        List<SKSJBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<SKSJBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromMatchDetailsPlayResult(String str) {
        Gson gson = getGson();
        SKZRBean bean = new SKZRBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), SKZRBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromRecommMatchListResult(String str) {
        Gson gson = getGson();
        ResultReferReferPage page = new ResultReferReferPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultReferReferPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromScrollBallListResult(String str) {
        Gson gson = getGson();
        ResultScorePage page = new ResultScorePage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultScorePage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromMyOrderListResult(String str) {
        Gson gson = getGson();
        ResultRankingPage page = new ResultRankingPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultRankingPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromMyYlListResult(String str) {
        Gson gson = getGson();
        RankingBean2 bean = new RankingBean2();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), RankingBean2.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromMyFollowArticleResult(String str) {
        Gson gson = getGson();
        ResultMyTzGTPage page = new ResultMyTzGTPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultMyTzGTPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromMyArticleListResult(String str) {
        Gson gson = getGson();
        ResultMyTzPage page = new ResultMyTzPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultMyTzPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromAchieveListResult(String str) {
        Gson gson = getGson();
        List<AchieveBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<AchieveBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromTaskListResult(String str) {
        Gson gson = getGson();
        TaskBean bean = new TaskBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), TaskBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromDayTaskResult(String str) {
        Gson gson = getGson();
        SevenPopBean bean = new SevenPopBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), SevenPopBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromChargeHistoryListResult(String str) {
        Gson gson = getGson();
        ResultChargeHistoryPage page = new ResultChargeHistoryPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultChargeHistoryPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromGuideDataResult(String str) {
        Gson gson = getGson();
        List<SplashBeanGG> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<SplashBeanGG>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromFansListResult(String str) {
        Gson gson = getGson();
        ResultFansPage page = new ResultFansPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultFansPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromMyFansListResult(String str) {
        Gson gson = getGson();
        ResultMyFansPage page = new ResultMyFansPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultMyFansPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromMatchGuessCgResult(String str) {
        Gson gson = getGson();
        List<GBSeriesBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<GBSeriesBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromWcollectListResult(String str) {
        Gson gson = getGson();
        ResultSNSCollectPage page = new ResultSNSCollectPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultSNSCollectPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromRecommPageResult(String str) {
        Gson gson = getGson();
        ResultMyReferPage page = new ResultMyReferPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultMyReferPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromHotDataPageResult(String str) {
        Gson gson = getGson();
        ResultRedPoplePage page = new ResultRedPoplePage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultRedPoplePage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromRecommExpertResult(String str) {
        Gson gson = getGson();
        ResultReferInfoARPage page = new ResultReferInfoARPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultReferInfoARPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromFiveDataListResult(String str) {
        Gson gson = getGson();
        ResultSnsPage page = new ResultSnsPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultSnsPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromDiamondExchangeResult(String str) {
        OrderBean bean = new OrderBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                bean.setData(json.getString("data"));
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromDiamondExchangeResult2(String str) {
        Gson gson = getGson();
        WXOrderBean bean = new WXOrderBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), WXOrderBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromAwardTypeListResult(String str) {
        Gson gson = getGson();
        List<StoreBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<StoreBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromAwardTypeList2Result(String str) {
        Gson gson = getGson();
        StoreBean2 bean = new StoreBean2();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), StoreBean2.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromAwardTurnResult(String str) {
        Gson gson = getGson();
        TurnTableResultBean bean = new TurnTableResultBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), TurnTableResultBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromAwardMessageTypeListResult(String str) {
        Gson gson = getGson();
        TurnTablePrizeTextBean bean = new TurnTablePrizeTextBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                bean.data = gson.fromJson(data.toString(), new TypeToken<List<String>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromChampionsResult(String str) {
        Gson gson = getGson();
        ResultChampionPage page = new ResultChampionPage();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                page = gson.fromJson(data.toString(), ResultChampionPage.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(page);
        return this;
    }

    public CcApiResult fromEarlyListResult(String str) {
        Gson gson = getGson();
        EarlyBean bean = new EarlyBean();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONObject data = json.getJSONObject("data");
                bean = gson.fromJson(data.toString(), EarlyBean.class);
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(bean);
        return this;
    }

    public CcApiResult fromMatchBasketLastResult(String str) {
        Gson gson = getGson();
        List<BasketBallLastBean> items = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
            if (json.has("data") && !json.isNull("data")) {
                JSONArray data = json.getJSONArray("data");
                items = gson.fromJson(data.toString(), new TypeToken<List<BasketBallLastBean>>() {
                }.getType());
            }
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        this.setData(items);
        return this;
    }

    public CcApiResult fromDefaultResult(String str) {
        try {
            JSONObject json = new JSONObject(str);
            this.setErrno(json.getInt("code"));
            this.setMessage(json.getString("msg"));
            this.setStatus(json.getBoolean("status"));
        } catch (Exception e) {
            LogUtil.e("CcApiResult", e);
        }
        return this;
    }
}
