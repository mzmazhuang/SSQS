package com.dading.ssqs.bean;

import com.dading.ssqs.BuildConfig;

public interface Constent {

    boolean DEBUG_VERSION = BuildConfig.DEBUG;

    /**
     * loadingSuc 是否登录成功传递bean
     */
    String LOADING_STATE_SP = "loadingSuc";
    /**
     * isLoading 发送广播--登录成功或者退出或者失败
     */
    String LOADING_BROCAST_TAG = "isLoading";
    /**
     * LOADING_SUC 登录的广播FILTE限制
     */
    String LOADING_ACTION = "LOADING_SUC";
    /**
     * 刷新我的金币
     */
    String REFRESH_MONY="Refresh_money";
    /**
     * 令牌
     */
    String TOKEN = "auth_token";
    /**
     * 比赛id请求标记
     */
    String MATCH_ID = "match_Id";
    /**
     * 界面跳转进比赛详情标记
     */
    String INTENT_FROM = "where";
    /**
     * 首页缓存
     */
    String HOME_CACHE = "home_cache";
    /**
     * 比赛详情内跳转标记
     */
    String MATCH_INFO_TAG = "match_info_tag";
    /**
     * my-->login action
     */
    String LOADING_ACTION_MY = "LOADING_ACTION_MY";
    /**
     * 钻石数
     */
    String DIAMONDS = "DIAMONDS";
    /**
     * 金币数
     */
    String GLODS = "GLODS";

    String SERIES = "SERIES";
    /**
     * tongzhi
     */
    String POP_CLOSE = "POP_CLOSE";
    /**
     * 是否是首次登陆
     */
    String IS_FRISE = "IS_FRISE";
    /**
     * 签到获得的金币数
     */
    String SIGN_GLOD = "SIGN_GLOD";
    /**
     * 是否签到成功
     */
    String SIGN_SUC = "SIGN_SUC";
    /**
     * 保存个人信息
     */
    String MY_INFO = "MY_INFO";
    /**
     * 个人用户名
     */
    String NICK_NAME = "NICK_NAME";
    /**
     * 个人签名
     */
    String SIGN_TEXT = "SIGN_TEXT";
    /**
     * VP切换
     */
    String LOADING_HOME = "LOADING_HOME";
    /**
     * 专家更多
     */
    String LOADING_HOME_SAVANT = "LOADING_HOME_SAVANT";
    /**
     * 跳轉個人信息
     */
    String LOADING_MY = "LOADING_MY";
    int RG_ID = 0x1111111;
    /**
     * 圈子廣播
     */
    String ALL_CIRCLE = "ALL_CIRCLE";
    /**
     * 圈子關注傳遞的是那個
     */
    String ALL_CIRCLE_TYPE = "ALL_CIRCLE_TYPE";
    String ALL_CIRCLE_TYPE_TAG = "ALL_CIRCLE_TYPE_TAG";
    /**
     * 來自首頁内容后的跳轉
     */
    String HOME_THING2_COME = "HOME_THING2_COME";
    /**
     * 來自圈子的跳轉
     */
    String ALL_CIRCLE2_COME = "ALL_CIRCLE2_COME";
    /**
     * 发帖类型id
     */
    String ALL_CIRCLE_ID = "ALL_CIRCLE_ID";
    /**
     * 發帖成功通知
     */
    String TZ_SUC = "TZ_SUC";
    String CHIOCE = "CHIOCE";
    String SC_TIME = "SC_TIME";
    String SG_TIME = "SG_TIME";
    String LEAGUEIDS = "LEAGUEIDS";
    String JS_TIME = "JS_TIME";
    String SUBTYPE = "SUBTYPE";
    String SCORE_TYPE = "SCORE_TYPE";
    String SG_RECEVICE = "SG_RECEVICE";
    String JS_RECEVICE = "JS_RECEVICE";
    String SC_RECEVICE = "SC_RECEVICE";
    String TASK_TAG = "TASK_TAG";
    String SAVANT_ID = "SAVANT_ID";
    String GREEN_HAND = "GREEN_HAND";
    String SAVANT_FOLLOW_TAG = "SAVANT_FOLLOW_TAG";
    String ISFOUCE = "ISFOUCE";
    String SEACHER_HISTORY = "SEACHER_HISTORY";
    String LOADING_GUESS_BALL = "LOADING_GUESS_BALL";
    String ADD_MSG = "ADD_MSG";
    String _SHOPING_GLOD = "_SHOPING_GLOD";
    /**
     * 主题帖id也就是赛事详情
     */
    String INFO_ID = "infoId";
    String IS_VIP = "IS_VIP";
    /**
     * 是否点击了广告\广告地址
     */
    String IS_CLICK = "IS_CLICK";
    String IS_CLICK_URL = "IS_CLICK_URL";
    String TASK_TAG2 = "TASK_TAG2";
    String IS_SHOW2 = "IS_SHOW2";
    String GZ_RECEVICE = "GZ_RECEVICE";
    String TURN_TABLE = "TURN_TABLE";
    String WX = "WX";
    java.lang.String APP_ID = "wxf03354b453566347";
    String MY_FOLLOW_TAG = "MY_FOLLOW_TAG";
    String MY_FANS_TAG = "MY_FANS_TAG";
    String LIVING = "living";
    String NEWS_ID = "NEWS_ID";
    /**
     * 是否显示亚盘推荐
     */
    String YP_IS_VISIABLE = "YP_IS_VISIABLE";
    /**
     * 娱乐场项目名
     */
    String CASINO_NAME = "CASINO_NAME";
    /**
     * 娛樂場加載url
     */
    String CASINO_URL = "CASINO_URL";
    /**
     * 引导页跳转url
     */
    String SPLASH_URL = "SPLASH_URL";
    /**
     * 是否绑定了银行卡
     */
    String IS_BIND_CARD = "IS_BIND_CARD";
    /**
     * 传递提现信息
     */
    String WITH_DRAW = "with_draw";
    /**
     * 足球
     */
    String LOADING_FOOTBALL = "LOADING_FOOTBALL";
    /**
     * 排行榜
     */
    String LOADING_RANKING = "LOADING_RANKING";
    /**
     * 娱乐场
     */
    String LOADING_CASINO = "LOADING_CASINO";
    /**
     * 滚球
     */
    String GQ_RECEVICE = "GQ_RECEVICE";
    /**
     * 是否是足球
     */
    String IS_FOOTBALL = "IS_FOOTBALL";
    /**
     * 充值信息
     */
    String RECHARGE_INFO = "RECHARGE_INFO";
    /**
     * 商城要充值金額
     */
    String ACCOUNT = "ACCOUNT";
    /**
     * 比分通知
     */
    String LOADING_FOOTBALL_SCORE = "LOADING_FOOTBALL_SCORE";
    /**
     * 活动详情
     */
    String PERFERENTIAL_WEB = "PERFERENTIAL_WEB";
    /**
     * 试玩账户为3
     */
    String USER_TYPE = "USER_TYPE";
    /**
     * 活动标题
     */
    String PERFERENTIAL_TITLE = "PERFERENTIAL_TITLE";

    String JS_RECEVICE_CB = "JS_RECEVICE_CB";
    String SC_RECEVICE_CB = "SC_RECEVICE_CB";
    String SG_RECEVICE_CB = "SG_RECEVICE_CB";
    String SQ_RECEVICE = "SQ_RECEVICE";
    String CG_RECEVICE = "CG_RECEVICE";


    String RECHARGE_BANK = "RECHARGE_BANK";
    String PERFERENTIAL_CONTENT = "PERFERENTIAL_CONTENT";
    /**
     * 个人报表属性
     */
    String WINS = "盈亏总额";
    String AMOUNT = "投注总额";
    String REWARDS = "派彩总额";
    String FEES = "佣金总额";
    String RECHARGES = "充值总额";
    String EXTRACTS = "提现总额";
    String HOT_DATA = "HOT_DATA";
    /**
     * 首頁籃球足球
     */
    String HOME_BALL = "HOME_BALL";
    /**
     * 切换比分标题
     */
    String HOME_SCORE = "HOME_SCORE";
    /**
     * 代表用戶的類型
     * 用户类型1—普通用户2—专家3-试玩用户4-代理商用户
     */
    String USER_TYPE_NUM = "USER_TYPE_NUM";
    /**
     * 赛程赛果即时界面筛选
     */
    String JS_SG_SC_FITTER = "JS_SG_SC_FITTER";

    String GO_TO_BEFORE = "GO_TO_BEFORE";
    /**
     * 跳轉比分
     */
    String LOADING_SCORE = "LOADING_SCORE";
}

