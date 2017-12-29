package com.dading.ssqs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/5 17:43
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HomeBean implements Serializable {
    private static final long serialVersionUID = 9135578669932398974L;

    /**
     * status : true
     * code : 0
     * msg :
     * data : {"adverts":[{"id":3,"name":"20场投注上榜领取大奖","imageUrl":"http://192.168.0.115:8080/images/advert/home_vp_pic2.png","sno":1,"type":1,"forwardID":229},{"id":4,"name":"高手上榜领取手机！","imageUrl":"http://192.168.0.115:8080/images/advert/home_vp_pic3.png","sno":2,"type":1,"forwardID":229},{"id":5,"name":"新手注册就送500金币了！","imageUrl":"http://192.168.0.115:8080/images/advert/home_vp_pic4.png","sno":3,"type":1,"forwardID":225}],"matchs":[{"id":48597,"home":"沙尔克04","away":"拜仁","hOrder":"10","aOrder":"1","openTime":"2017-09-20 02:30:00","leagueName":"德甲","hImageUrl":"http://192.168.0.115:8080/images/team/20160819032954rFUlsysfOU.png","aImageUrl":"http://192.168.0.115:8080/images/team/20160819032458QRtfPNRPhD.png","joinCount":0,"isFouce":0,"hScore":"","aScore":"","type":1,"fouceDate":null,"hHalfScore":null,"aHalfScore":null,"hRed":0,"aRed":0,"hYellow":0,"aYellow":0,"isOver":0,"hOrderFrom":"德甲","aOrderFrom":"德甲","liveCastUrl":"","source":"0","protime":"","payRate":{"id":826338,"home":"沙尔克04","away":"拜仁","payTypeName":"当前让球","payTypeID":2,"realRate1":"7.72","realRate2":"0/0","realRate3":"2.12"},"num":16},{"id":48608,"home":"博洛尼亚","away":"国际米兰","hOrder":"15","aOrder":"7","openTime":"2017-09-20 02:45:00","leagueName":"意甲","hImageUrl":"http://192.168.0.115:8080/images/team/20160818034633aMmjysvQGH.png","aImageUrl":"http://192.168.0.115:8080/images/team/20160818034841JJoFEmyYRn.png","joinCount":0,"isFouce":0,"hScore":"","aScore":"","type":1,"fouceDate":null,"hHalfScore":null,"aHalfScore":null,"hRed":0,"aRed":0,"hYellow":0,"aYellow":0,"isOver":0,"hOrderFrom":"意甲","aOrderFrom":"意甲","liveCastUrl":"","source":"0","protime":"","payRate":{"id":826422,"home":"博洛尼亚","away":"国际米兰","payTypeName":"当前让球","payTypeID":2,"realRate1":"2.56","realRate2":"0/0","realRate3":"2.50"},"num":16},{"id":48847,"home":"热刺","away":"巴恩斯利","hOrder":"2","aOrder":"14","openTime":"2017-09-20 03:00:00","leagueName":"英联杯","hImageUrl":"http://192.168.0.115:8080/images/team/20160816035338eqACXkWzXS.png","aImageUrl":"http://192.168.0.115:8080/images/team/20160823063821iCMZdDDCyL-100x100.png","joinCount":0,"isFouce":0,"hScore":"","aScore":"","type":2,"fouceDate":null,"hHalfScore":null,"aHalfScore":null,"hRed":0,"aRed":0,"hYellow":0,"aYellow":0,"isOver":0,"hOrderFrom":"英超","aOrderFrom":"英冠","liveCastUrl":"","source":"0","protime":"","payRate":{"id":827340,"home":"热刺","away":"巴恩斯利","payTypeName":"当前让球","payTypeID":2,"realRate1":"0.90","realRate2":"0/0","realRate3":"0.90"},"num":16}],"message":[{"content":"恭喜\"我。积极探索与创新与\"在猜球中赢得3,170个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"我。积极探索与创新与\"在猜球中赢得2,640个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"我。积极探索与创新与\"在猜球中赢得2,640个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"匿名\"在滚球下注中赢得242个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"艾薇\"在滚球下注中赢得2,020个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"艾薇\"在滚球下注中赢得3,600个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"艾薇\"在猜球中赢得1,850个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"艾薇\"在猜球中赢得1,790个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"艾薇\"在猜球中赢得1,010个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"艾薇\"在猜球中赢得1,000个金币","type":1,"typeName":"中奖信息:"}],"basketBalls":[{"id":48597,"home":"沙尔克04","away":"拜仁","hOrder":"10","aOrder":"1","openTime":"2017-09-20 02:30:00","leagueName":"德甲","hImageUrl":"http://192.168.0.115:8080/images/team/20160819032954rFUlsysfOU.png","aImageUrl":"http://192.168.0.115:8080/images/team/20160819032458QRtfPNRPhD.png","joinCount":0,"isFouce":0,"hScore":"","aScore":"","type":1,"fouceDate":null,"hHalfScore":null,"aHalfScore":null,"hRed":0,"aRed":0,"hYellow":0,"aYellow":0,"isOver":0,"hOrderFrom":"德甲","aOrderFrom":"德甲","liveCastUrl":"","source":"0","protime":"","payRate":{"id":826338,"home":"沙尔克04","away":"拜仁","payTypeName":"当前让球","payTypeID":2,"realRate1":"7.72","realRate2":"0/0","realRate3":"2.12"},"num":16},{"id":48608,"home":"博洛尼亚","away":"国际米兰","hOrder":"15","aOrder":"7","openTime":"2017-09-20 02:45:00","leagueName":"意甲","hImageUrl":"http://192.168.0.115:8080/images/team/20160818034633aMmjysvQGH.png","aImageUrl":"http://192.168.0.115:8080/images/team/20160818034841JJoFEmyYRn.png","joinCount":0,"isFouce":0,"hScore":"","aScore":"","type":1,"fouceDate":null,"hHalfScore":null,"aHalfScore":null,"hRed":0,"aRed":0,"hYellow":0,"aYellow":0,"isOver":0,"hOrderFrom":"意甲","aOrderFrom":"意甲","liveCastUrl":"","source":"0","protime":"","payRate":{"id":826422,"home":"博洛尼亚","away":"国际米兰","payTypeName":"当前让球","payTypeID":2,"realRate1":"2.56","realRate2":"0/0","realRate3":"2.50"},"num":16},{"id":48847,"home":"热刺","away":"巴恩斯利","hOrder":"2","aOrder":"14","openTime":"2017-09-20 03:00:00","leagueName":"英联杯","hImageUrl":"http://192.168.0.115:8080/images/team/20160816035338eqACXkWzXS.png","aImageUrl":"http://192.168.0.115:8080/images/team/20160823063821iCMZdDDCyL-100x100.png","joinCount":0,"isFouce":0,"hScore":"","aScore":"","type":2,"fouceDate":null,"hHalfScore":null,"aHalfScore":null,"hRed":0,"aRed":0,"hYellow":0,"aYellow":0,"isOver":0,"hOrderFrom":"英超","aOrderFrom":"英冠","liveCastUrl":"","source":"0","protime":"","payRate":{"id":827340,"home":"热刺","away":"巴恩斯利","payTypeName":"当前让球","payTypeID":2,"realRate1":"0.90","realRate2":"0/0","realRate3":"0.90"},"num":16}],"orders":{"isEnd":0,"startDate":"2016-11-29 11:32:09","endDate":"2017-12-01 11:32:09","orders":[{"userID":"20161226000000","userName":"名匠","avatar":"http://192.168.0.115:8080/images/avatar/default/8ca7fd41950a45f98aea5c1351436450-100x100.jpg","ranking":"1","value":"1000000","awardName":"iphone 7 Plus","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014035103AvHKyTokwL-66x87.png"],"status":1},{"userID":"20161226000001","userName":"藏经阁","avatar":"http://192.168.0.115:8080/images/avatar/default/2277b60973a74fb59309078fb091b4d8-100x100.jpg","ranking":"2","value":"699000","awardName":"佳能微单","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014035213fYaDsDSYwP-131x77.png"],"status":1},{"userID":"20161226000002","userName":"飞虎队","avatar":"http://192.168.0.115:8080/images/avatar/default/7d612bd1601e4f60b0072414e41da2ce-100x100.jpg","ranking":"3","value":"50000","awardName":"电子阅读器","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014035523EitNAEQDZg-68x97.png"],"status":1},{"userID":"20161226000003","userName":"球迷","avatar":"http://192.168.0.115:8080/images/avatar/default/PfkLCcDbt6iaibzq18WFm8EeZi-100x100.jpg","ranking":"4","value":"27700","awardName":"魅族头戴式耳机","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014035623yLTauwTkAH-96x99.png"],"status":1},{"userID":"20161226000004","userName":"虎豹竞技","avatar":"http://192.168.0.115:8080/images/avatar/default/dfb2ad30jw8ey9xbh1gubj20ku0ktmy9.jpg","ranking":"5","value":"23000","awardName":"美的豆浆机","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040048jDaYsTPIzw-66x95.png"],"status":1},{"userID":"20161226000005","userName":"全明星","avatar":"http://192.168.0.115:8080/images/avatar/default/9030b11043ab4f0cac047253dd26f3e1-100x100.jpg","ranking":"6","value":"15050","awardName":"京东卡300","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040129FDRFNyXjTM-97x61.png"],"status":1},{"userID":"20161226000006","userName":"足彩收割机","avatar":"http://192.168.0.115:8080/images/avatar/default/85deb155a7df4dd79bdcf99adefb4780-100x100.jpg","ranking":"7","value":"12000","awardName":"京东卡300","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040129FDRFNyXjTM-97x61.png"],"status":1},{"userID":"20161226000007","userName":"峰言峰语","avatar":"http://192.168.0.115:8080/images/avatar/default/87351fa5a27748b59b4c57f856faef20-100x100.jpg","ranking":"8","value":"10000","awardName":"100元话费","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040227QuRyunoqNl-108x61.png"],"status":1},{"userID":"20161226000008","userName":"南柯一梦","avatar":"http://192.168.0.115:8080/images/avatar/default/qNibSZLZmd4I0twBdsAfu3-100x100.jpg","ranking":"9","value":"7800","awardName":"100元话费","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040227QuRyunoqNl-108x61.png"],"status":1},{"userID":"20161226000009","userName":"大球哥","avatar":"http://192.168.0.115:8080/images/avatar/default/5c65a4165428405ba59f85d52cfd103e-100x100.jpg","ranking":"10","value":"5665","awardName":"100元话费","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040227QuRyunoqNl-108x61.png"],"status":1}]}}
     */

    /**
     * adverts : [{"id":3,"name":"20场投注上榜领取大奖","imageUrl":"http://192.168.0.115:8080/images/advert/home_vp_pic2.png","sno":1,"type":1,"forwardID":229},{"id":4,"name":"高手上榜领取手机！","imageUrl":"http://192.168.0.115:8080/images/advert/home_vp_pic3.png","sno":2,"type":1,"forwardID":229},{"id":5,"name":"新手注册就送500金币了！","imageUrl":"http://192.168.0.115:8080/images/advert/home_vp_pic4.png","sno":3,"type":1,"forwardID":225}]
     * matchs : [{"id":48597,"home":"沙尔克04","away":"拜仁","hOrder":"10","aOrder":"1","openTime":"2017-09-20 02:30:00","leagueName":"德甲","hImageUrl":"http://192.168.0.115:8080/images/team/20160819032954rFUlsysfOU.png","aImageUrl":"http://192.168.0.115:8080/images/team/20160819032458QRtfPNRPhD.png","joinCount":0,"isFouce":0,"hScore":"","aScore":"","type":1,"fouceDate":null,"hHalfScore":null,"aHalfScore":null,"hRed":0,"aRed":0,"hYellow":0,"aYellow":0,"isOver":0,"hOrderFrom":"德甲","aOrderFrom":"德甲","liveCastUrl":"","source":"0","protime":"","payRate":{"id":826338,"home":"沙尔克04","away":"拜仁","payTypeName":"当前让球","payTypeID":2,"realRate1":"7.72","realRate2":"0/0","realRate3":"2.12"},"num":16},{"id":48608,"home":"博洛尼亚","away":"国际米兰","hOrder":"15","aOrder":"7","openTime":"2017-09-20 02:45:00","leagueName":"意甲","hImageUrl":"http://192.168.0.115:8080/images/team/20160818034633aMmjysvQGH.png","aImageUrl":"http://192.168.0.115:8080/images/team/20160818034841JJoFEmyYRn.png","joinCount":0,"isFouce":0,"hScore":"","aScore":"","type":1,"fouceDate":null,"hHalfScore":null,"aHalfScore":null,"hRed":0,"aRed":0,"hYellow":0,"aYellow":0,"isOver":0,"hOrderFrom":"意甲","aOrderFrom":"意甲","liveCastUrl":"","source":"0","protime":"","payRate":{"id":826422,"home":"博洛尼亚","away":"国际米兰","payTypeName":"当前让球","payTypeID":2,"realRate1":"2.56","realRate2":"0/0","realRate3":"2.50"},"num":16},{"id":48847,"home":"热刺","away":"巴恩斯利","hOrder":"2","aOrder":"14","openTime":"2017-09-20 03:00:00","leagueName":"英联杯","hImageUrl":"http://192.168.0.115:8080/images/team/20160816035338eqACXkWzXS.png","aImageUrl":"http://192.168.0.115:8080/images/team/20160823063821iCMZdDDCyL-100x100.png","joinCount":0,"isFouce":0,"hScore":"","aScore":"","type":2,"fouceDate":null,"hHalfScore":null,"aHalfScore":null,"hRed":0,"aRed":0,"hYellow":0,"aYellow":0,"isOver":0,"hOrderFrom":"英超","aOrderFrom":"英冠","liveCastUrl":"","source":"0","protime":"","payRate":{"id":827340,"home":"热刺","away":"巴恩斯利","payTypeName":"当前让球","payTypeID":2,"realRate1":"0.90","realRate2":"0/0","realRate3":"0.90"},"num":16}]
     * message : [{"content":"恭喜\"我。积极探索与创新与\"在猜球中赢得3,170个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"我。积极探索与创新与\"在猜球中赢得2,640个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"我。积极探索与创新与\"在猜球中赢得2,640个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"匿名\"在滚球下注中赢得242个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"艾薇\"在滚球下注中赢得2,020个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"艾薇\"在滚球下注中赢得3,600个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"艾薇\"在猜球中赢得1,850个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"艾薇\"在猜球中赢得1,790个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"艾薇\"在猜球中赢得1,010个金币","type":1,"typeName":"中奖信息:"},{"content":"恭喜\"艾薇\"在猜球中赢得1,000个金币","type":1,"typeName":"中奖信息:"}]
     * basketBalls : [{"id":48597,"home":"沙尔克04","away":"拜仁","hOrder":"10","aOrder":"1","openTime":"2017-09-20 02:30:00","leagueName":"德甲","hImageUrl":"http://192.168.0.115:8080/images/team/20160819032954rFUlsysfOU.png","aImageUrl":"http://192.168.0.115:8080/images/team/20160819032458QRtfPNRPhD.png","joinCount":0,"isFouce":0,"hScore":"","aScore":"","type":1,"fouceDate":null,"hHalfScore":null,"aHalfScore":null,"hRed":0,"aRed":0,"hYellow":0,"aYellow":0,"isOver":0,"hOrderFrom":"德甲","aOrderFrom":"德甲","liveCastUrl":"","source":"0","protime":"","payRate":{"id":826338,"home":"沙尔克04","away":"拜仁","payTypeName":"当前让球","payTypeID":2,"realRate1":"7.72","realRate2":"0/0","realRate3":"2.12"},"num":16},{"id":48608,"home":"博洛尼亚","away":"国际米兰","hOrder":"15","aOrder":"7","openTime":"2017-09-20 02:45:00","leagueName":"意甲","hImageUrl":"http://192.168.0.115:8080/images/team/20160818034633aMmjysvQGH.png","aImageUrl":"http://192.168.0.115:8080/images/team/20160818034841JJoFEmyYRn.png","joinCount":0,"isFouce":0,"hScore":"","aScore":"","type":1,"fouceDate":null,"hHalfScore":null,"aHalfScore":null,"hRed":0,"aRed":0,"hYellow":0,"aYellow":0,"isOver":0,"hOrderFrom":"意甲","aOrderFrom":"意甲","liveCastUrl":"","source":"0","protime":"","payRate":{"id":826422,"home":"博洛尼亚","away":"国际米兰","payTypeName":"当前让球","payTypeID":2,"realRate1":"2.56","realRate2":"0/0","realRate3":"2.50"},"num":16},{"id":48847,"home":"热刺","away":"巴恩斯利","hOrder":"2","aOrder":"14","openTime":"2017-09-20 03:00:00","leagueName":"英联杯","hImageUrl":"http://192.168.0.115:8080/images/team/20160816035338eqACXkWzXS.png","aImageUrl":"http://192.168.0.115:8080/images/team/20160823063821iCMZdDDCyL-100x100.png","joinCount":0,"isFouce":0,"hScore":"","aScore":"","type":2,"fouceDate":null,"hHalfScore":null,"aHalfScore":null,"hRed":0,"aRed":0,"hYellow":0,"aYellow":0,"isOver":0,"hOrderFrom":"英超","aOrderFrom":"英冠","liveCastUrl":"","source":"0","protime":"","payRate":{"id":827340,"home":"热刺","away":"巴恩斯利","payTypeName":"当前让球","payTypeID":2,"realRate1":"0.90","realRate2":"0/0","realRate3":"0.90"},"num":16}]
     * orders : {"isEnd":0,"startDate":"2016-11-29 11:32:09","endDate":"2017-12-01 11:32:09","orders":[{"userID":"20161226000000","userName":"名匠","avatar":"http://192.168.0.115:8080/images/avatar/default/8ca7fd41950a45f98aea5c1351436450-100x100.jpg","ranking":"1","value":"1000000","awardName":"iphone 7 Plus","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014035103AvHKyTokwL-66x87.png"],"status":1},{"userID":"20161226000001","userName":"藏经阁","avatar":"http://192.168.0.115:8080/images/avatar/default/2277b60973a74fb59309078fb091b4d8-100x100.jpg","ranking":"2","value":"699000","awardName":"佳能微单","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014035213fYaDsDSYwP-131x77.png"],"status":1},{"userID":"20161226000002","userName":"飞虎队","avatar":"http://192.168.0.115:8080/images/avatar/default/7d612bd1601e4f60b0072414e41da2ce-100x100.jpg","ranking":"3","value":"50000","awardName":"电子阅读器","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014035523EitNAEQDZg-68x97.png"],"status":1},{"userID":"20161226000003","userName":"球迷","avatar":"http://192.168.0.115:8080/images/avatar/default/PfkLCcDbt6iaibzq18WFm8EeZi-100x100.jpg","ranking":"4","value":"27700","awardName":"魅族头戴式耳机","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014035623yLTauwTkAH-96x99.png"],"status":1},{"userID":"20161226000004","userName":"虎豹竞技","avatar":"http://192.168.0.115:8080/images/avatar/default/dfb2ad30jw8ey9xbh1gubj20ku0ktmy9.jpg","ranking":"5","value":"23000","awardName":"美的豆浆机","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040048jDaYsTPIzw-66x95.png"],"status":1},{"userID":"20161226000005","userName":"全明星","avatar":"http://192.168.0.115:8080/images/avatar/default/9030b11043ab4f0cac047253dd26f3e1-100x100.jpg","ranking":"6","value":"15050","awardName":"京东卡300","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040129FDRFNyXjTM-97x61.png"],"status":1},{"userID":"20161226000006","userName":"足彩收割机","avatar":"http://192.168.0.115:8080/images/avatar/default/85deb155a7df4dd79bdcf99adefb4780-100x100.jpg","ranking":"7","value":"12000","awardName":"京东卡300","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040129FDRFNyXjTM-97x61.png"],"status":1},{"userID":"20161226000007","userName":"峰言峰语","avatar":"http://192.168.0.115:8080/images/avatar/default/87351fa5a27748b59b4c57f856faef20-100x100.jpg","ranking":"8","value":"10000","awardName":"100元话费","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040227QuRyunoqNl-108x61.png"],"status":1},{"userID":"20161226000008","userName":"南柯一梦","avatar":"http://192.168.0.115:8080/images/avatar/default/qNibSZLZmd4I0twBdsAfu3-100x100.jpg","ranking":"9","value":"7800","awardName":"100元话费","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040227QuRyunoqNl-108x61.png"],"status":1},{"userID":"20161226000009","userName":"大球哥","avatar":"http://192.168.0.115:8080/images/avatar/default/5c65a4165428405ba59f85d52cfd103e-100x100.jpg","ranking":"10","value":"5665","awardName":"100元话费","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040227QuRyunoqNl-108x61.png"],"status":1}]}
     */

    private OrdersBeanX orders;
    private List<AdvertsBean> adverts;
    private List<MatchsBean> matchs;
    private List<MessageBean> message;
    private List<BasketBallsBean> basketBalls;

    public OrdersBeanX getOrders() {
        return orders;
    }

    public void setOrders(OrdersBeanX orders) {
        this.orders = orders;
    }

    public List<AdvertsBean> getAdverts() {
        return adverts;
    }

    public void setAdverts(List<AdvertsBean> adverts) {
        this.adverts = adverts;
    }

    public List<MatchsBean> getMatchs() {
        return matchs;
    }

    public void setMatchs(List<MatchsBean> matchs) {
        this.matchs = matchs;
    }

    public List<MessageBean> getMessage() {
        return message;
    }

    public void setMessage(List<MessageBean> message) {
        this.message = message;
    }

    public List<BasketBallsBean> getBasketBalls() {
        return basketBalls;
    }

    public void setBasketBalls(List<BasketBallsBean> basketBalls) {
        this.basketBalls = basketBalls;
    }

    public static class OrdersBeanX implements Serializable {
        /**
         * isEnd : 0
         * startDate : 2016-11-29 11:32:09
         * endDate : 2017-12-01 11:32:09
         * orders : [{"userID":"20161226000000","userName":"名匠","avatar":"http://192.168.0.115:8080/images/avatar/default/8ca7fd41950a45f98aea5c1351436450-100x100.jpg","ranking":"1","value":"1000000","awardName":"iphone 7 Plus","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014035103AvHKyTokwL-66x87.png"],"status":1},{"userID":"20161226000001","userName":"藏经阁","avatar":"http://192.168.0.115:8080/images/avatar/default/2277b60973a74fb59309078fb091b4d8-100x100.jpg","ranking":"2","value":"699000","awardName":"佳能微单","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014035213fYaDsDSYwP-131x77.png"],"status":1},{"userID":"20161226000002","userName":"飞虎队","avatar":"http://192.168.0.115:8080/images/avatar/default/7d612bd1601e4f60b0072414e41da2ce-100x100.jpg","ranking":"3","value":"50000","awardName":"电子阅读器","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014035523EitNAEQDZg-68x97.png"],"status":1},{"userID":"20161226000003","userName":"球迷","avatar":"http://192.168.0.115:8080/images/avatar/default/PfkLCcDbt6iaibzq18WFm8EeZi-100x100.jpg","ranking":"4","value":"27700","awardName":"魅族头戴式耳机","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014035623yLTauwTkAH-96x99.png"],"status":1},{"userID":"20161226000004","userName":"虎豹竞技","avatar":"http://192.168.0.115:8080/images/avatar/default/dfb2ad30jw8ey9xbh1gubj20ku0ktmy9.jpg","ranking":"5","value":"23000","awardName":"美的豆浆机","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040048jDaYsTPIzw-66x95.png"],"status":1},{"userID":"20161226000005","userName":"全明星","avatar":"http://192.168.0.115:8080/images/avatar/default/9030b11043ab4f0cac047253dd26f3e1-100x100.jpg","ranking":"6","value":"15050","awardName":"京东卡300","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040129FDRFNyXjTM-97x61.png"],"status":1},{"userID":"20161226000006","userName":"足彩收割机","avatar":"http://192.168.0.115:8080/images/avatar/default/85deb155a7df4dd79bdcf99adefb4780-100x100.jpg","ranking":"7","value":"12000","awardName":"京东卡300","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040129FDRFNyXjTM-97x61.png"],"status":1},{"userID":"20161226000007","userName":"峰言峰语","avatar":"http://192.168.0.115:8080/images/avatar/default/87351fa5a27748b59b4c57f856faef20-100x100.jpg","ranking":"8","value":"10000","awardName":"100元话费","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040227QuRyunoqNl-108x61.png"],"status":1},{"userID":"20161226000008","userName":"南柯一梦","avatar":"http://192.168.0.115:8080/images/avatar/default/qNibSZLZmd4I0twBdsAfu3-100x100.jpg","ranking":"9","value":"7800","awardName":"100元话费","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040227QuRyunoqNl-108x61.png"],"status":1},{"userID":"20161226000009","userName":"大球哥","avatar":"http://192.168.0.115:8080/images/avatar/default/5c65a4165428405ba59f85d52cfd103e-100x100.jpg","ranking":"10","value":"5665","awardName":"100元话费","awardUrlArr":["http://192.168.0.115:8080/images/award/20161014040227QuRyunoqNl-108x61.png"],"status":1}]
         */

        private int isEnd;
        private String startDate;
        private String endDate;
        private List<OrdersBean> sysMessge;

        public int getIsEnd() {
            return isEnd;
        }

        public void setIsEnd(int isEnd) {
            this.isEnd = isEnd;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public List<OrdersBean> getSysMessge() {
            return sysMessge;
        }

        public void setSysMessge(List<OrdersBean> sysMessge) {
            this.sysMessge = sysMessge;
        }

        public static class OrdersBean implements Serializable {

            private String content;
            private String type;
            private String typeName;
            private String matchName;//比赛名字
            private String userName;//用户名字
            private String matchType;//中奖比赛类型1，足球；2，篮球3，冠军
            private String payBallType;//1:单关2:串关3:比分4:滚球5：冠军
            private String mark;//1：登陆后个人信息，null未登录其他人员信息
            private String amount;//获奖金额

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getTypeName() {
                return typeName;
            }

            public void setTypeName(String typeName) {
                this.typeName = typeName;
            }

            public String getMatchName() {
                return matchName;
            }

            public void setMatchName(String matchName) {
                this.matchName = matchName;
            }

            public String getUsername() {
                return userName;
            }

            public void setUsername(String username) {
                this.userName = username;
            }

            public String getMatchType() {
                return matchType;
            }

            public void setMatchType(String matchType) {
                this.matchType = matchType;
            }

            public String getPayBallType() {
                return payBallType;
            }

            public void setPayBallType(String payBallType) {
                this.payBallType = payBallType;
            }

            public String getMark() {
                return mark;
            }

            public void setMark(String mark) {
                this.mark = mark;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }
        }
    }

    public static class AdvertsBean implements Serializable {
        /**
         * id : 3
         * name : 20场投注上榜领取大奖
         * imageUrl : http://192.168.0.115:8080/images/advert/home_vp_pic2.png
         * sno : 1
         * type : 1
         * forwardID : 229
         */

        private int id;
        private String name;
        private String imageUrl;
        private int sno;
        private int type;
        private int forwardID;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public int getSno() {
            return sno;
        }

        public void setSno(int sno) {
            this.sno = sno;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getForwardID() {
            return forwardID;
        }

        public void setForwardID(int forwardID) {
            this.forwardID = forwardID;
        }
    }

    public static class MatchsBean implements Serializable {
        /**
         * id : 48597
         * home : 沙尔克04
         * away : 拜仁
         * hOrder : 10
         * aOrder : 1
         * openTime : 2017-09-20 02:30:00
         * leagueName : 德甲
         * hImageUrl : http://192.168.0.115:8080/images/team/20160819032954rFUlsysfOU.png
         * aImageUrl : http://192.168.0.115:8080/images/team/20160819032458QRtfPNRPhD.png
         * joinCount : 0
         * isFouce : 0
         * hScore :
         * aScore :
         * type : 1
         * fouceDate : null
         * hHalfScore : null
         * aHalfScore : null
         * hRed : 0
         * aRed : 0
         * hYellow : 0
         * aYellow : 0
         * isOver : 0
         * hOrderFrom : 德甲
         * aOrderFrom : 德甲
         * liveCastUrl :
         * source : 0
         * protime :
         * payRate : {"id":826338,"home":"沙尔克04","away":"拜仁","payTypeName":"当前让球","payTypeID":2,"realRate1":"7.72","realRate2":"0/0","realRate3":"2.12"}
         * num : 16
         */

        private int id;
        private String home;
        private String away;
        private String hOrder;
        private String aOrder;
        private String openTime;
        private String leagueName;
        private String hImageUrl;
        private String aImageUrl;
        private int joinCount;
        private int isFouce;
        private String hScore;
        private String aScore;
        private int type;
        private Object fouceDate;
        private Object hHalfScore;
        private Object aHalfScore;
        private int hRed;
        private int aRed;
        private int hYellow;
        private int aYellow;
        private int isOver;
        private String hOrderFrom;
        private String aOrderFrom;
        private String liveCastUrl;
        private String source;
        private String protime;
        private PayRateBean payRate;
        private int num;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHome() {
            return home;
        }

        public void setHome(String home) {
            this.home = home;
        }

        public String getAway() {
            return away;
        }

        public void setAway(String away) {
            this.away = away;
        }

        public String getHOrder() {
            return hOrder;
        }

        public void setHOrder(String hOrder) {
            this.hOrder = hOrder;
        }

        public String getAOrder() {
            return aOrder;
        }

        public void setAOrder(String aOrder) {
            this.aOrder = aOrder;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String getHImageUrl() {
            return hImageUrl;
        }

        public void setHImageUrl(String hImageUrl) {
            this.hImageUrl = hImageUrl;
        }

        public String getAImageUrl() {
            return aImageUrl;
        }

        public void setAImageUrl(String aImageUrl) {
            this.aImageUrl = aImageUrl;
        }

        public int getJoinCount() {
            return joinCount;
        }

        public void setJoinCount(int joinCount) {
            this.joinCount = joinCount;
        }

        public int getIsFouce() {
            return isFouce;
        }

        public void setIsFouce(int isFouce) {
            this.isFouce = isFouce;
        }

        public String getHScore() {
            return hScore;
        }

        public void setHScore(String hScore) {
            this.hScore = hScore;
        }

        public String getAScore() {
            return aScore;
        }

        public void setAScore(String aScore) {
            this.aScore = aScore;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Object getFouceDate() {
            return fouceDate;
        }

        public void setFouceDate(Object fouceDate) {
            this.fouceDate = fouceDate;
        }

        public Object getHHalfScore() {
            return hHalfScore;
        }

        public void setHHalfScore(Object hHalfScore) {
            this.hHalfScore = hHalfScore;
        }

        public Object getAHalfScore() {
            return aHalfScore;
        }

        public void setAHalfScore(Object aHalfScore) {
            this.aHalfScore = aHalfScore;
        }

        public int getHRed() {
            return hRed;
        }

        public void setHRed(int hRed) {
            this.hRed = hRed;
        }

        public int getARed() {
            return aRed;
        }

        public void setARed(int aRed) {
            this.aRed = aRed;
        }

        public int getHYellow() {
            return hYellow;
        }

        public void setHYellow(int hYellow) {
            this.hYellow = hYellow;
        }

        public int getAYellow() {
            return aYellow;
        }

        public void setAYellow(int aYellow) {
            this.aYellow = aYellow;
        }

        public int getIsOver() {
            return isOver;
        }

        public void setIsOver(int isOver) {
            this.isOver = isOver;
        }

        public String getHOrderFrom() {
            return hOrderFrom;
        }

        public void setHOrderFrom(String hOrderFrom) {
            this.hOrderFrom = hOrderFrom;
        }

        public String getAOrderFrom() {
            return aOrderFrom;
        }

        public void setAOrderFrom(String aOrderFrom) {
            this.aOrderFrom = aOrderFrom;
        }

        public String getLiveCastUrl() {
            return liveCastUrl;
        }

        public void setLiveCastUrl(String liveCastUrl) {
            this.liveCastUrl = liveCastUrl;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getProtime() {
            return protime;
        }

        public void setProtime(String protime) {
            this.protime = protime;
        }

        public PayRateBean getPayRate() {
            return payRate;
        }

        public void setPayRate(PayRateBean payRate) {
            this.payRate = payRate;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public static class PayRateBean implements Serializable {
            /**
             * id : 826338
             * home : 沙尔克04
             * away : 拜仁
             * payTypeName : 当前让球
             * payTypeID : 2
             * realRate1 : 7.72
             * realRate2 : 0/0
             * realRate3 : 2.12
             */

            private int id;
            private String home;
            private String away;
            private String payTypeName;
            private int payTypeID;
            private String realRate1;
            private String realRate2;
            private String realRate3;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getHome() {
                return home;
            }

            public void setHome(String home) {
                this.home = home;
            }

            public String getAway() {
                return away;
            }

            public void setAway(String away) {
                this.away = away;
            }

            public String getPayTypeName() {
                return payTypeName;
            }

            public void setPayTypeName(String payTypeName) {
                this.payTypeName = payTypeName;
            }

            public int getPayTypeID() {
                return payTypeID;
            }

            public void setPayTypeID(int payTypeID) {
                this.payTypeID = payTypeID;
            }

            public String getRealRate1() {
                return realRate1;
            }

            public void setRealRate1(String realRate1) {
                this.realRate1 = realRate1;
            }

            public String getRealRate2() {
                return realRate2;
            }

            public void setRealRate2(String realRate2) {
                this.realRate2 = realRate2;
            }

            public String getRealRate3() {
                return realRate3;
            }

            public void setRealRate3(String realRate3) {
                this.realRate3 = realRate3;
            }
        }
    }

    public static class MessageBean implements Serializable {
        /**
         * content : 恭喜"我。积极探索与创新与"在猜球中赢得3,170个金币
         * type : 1
         * typeName : 中奖信息:
         */

        private String content;
        private int type;
        private String typeName;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

    public static class BasketBallsBean implements Serializable {
        /**
         * id : 48597
         * home : 沙尔克04
         * away : 拜仁
         * hOrder : 10
         * aOrder : 1
         * openTime : 2017-09-20 02:30:00
         * leagueName : 德甲
         * hImageUrl : http://192.168.0.115:8080/images/team/20160819032954rFUlsysfOU.png
         * aImageUrl : http://192.168.0.115:8080/images/team/20160819032458QRtfPNRPhD.png
         * joinCount : 0
         * isFouce : 0
         * hScore :
         * aScore :
         * type : 1
         * fouceDate : null
         * hHalfScore : null
         * aHalfScore : null
         * hRed : 0
         * aRed : 0
         * hYellow : 0
         * aYellow : 0
         * isOver : 0
         * hOrderFrom : 德甲
         * aOrderFrom : 德甲
         * liveCastUrl :
         * source : 0
         * protime :
         * payRate : {"id":826338,"home":"沙尔克04","away":"拜仁","payTypeName":"当前让球","payTypeID":2,"realRate1":"7.72","realRate2":"0/0","realRate3":"2.12"}
         * num : 16
         */

        private int id;
        private String home;
        private String away;
        private String hOrder;
        private String aOrder;
        private String openTime;
        private String leagueName;
        private String hImageUrl;
        private String aImageUrl;
        private int joinCount;
        private int isFouce;
        private String hScore;
        private String aScore;
        private int type;
        private Object fouceDate;
        private Object hHalfScore;
        private Object aHalfScore;
        private int hRed;
        private int aRed;
        private int hYellow;
        private int aYellow;
        private int isOver;
        private String hOrderFrom;
        private String aOrderFrom;
        private String liveCastUrl;
        private String source;
        private String protime;
        private PayRateBeanX payRate;
        private int num;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getHome() {
            return home;
        }

        public void setHome(String home) {
            this.home = home;
        }

        public String getAway() {
            return away;
        }

        public void setAway(String away) {
            this.away = away;
        }

        public String getHOrder() {
            return hOrder;
        }

        public void setHOrder(String hOrder) {
            this.hOrder = hOrder;
        }

        public String getAOrder() {
            return aOrder;
        }

        public void setAOrder(String aOrder) {
            this.aOrder = aOrder;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public String getLeagueName() {
            return leagueName;
        }

        public void setLeagueName(String leagueName) {
            this.leagueName = leagueName;
        }

        public String getHImageUrl() {
            return hImageUrl;
        }

        public void setHImageUrl(String hImageUrl) {
            this.hImageUrl = hImageUrl;
        }

        public String getAImageUrl() {
            return aImageUrl;
        }

        public void setAImageUrl(String aImageUrl) {
            this.aImageUrl = aImageUrl;
        }

        public int getJoinCount() {
            return joinCount;
        }

        public void setJoinCount(int joinCount) {
            this.joinCount = joinCount;
        }

        public int getIsFouce() {
            return isFouce;
        }

        public void setIsFouce(int isFouce) {
            this.isFouce = isFouce;
        }

        public String getHScore() {
            return hScore;
        }

        public void setHScore(String hScore) {
            this.hScore = hScore;
        }

        public String getAScore() {
            return aScore;
        }

        public void setAScore(String aScore) {
            this.aScore = aScore;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public Object getFouceDate() {
            return fouceDate;
        }

        public void setFouceDate(Object fouceDate) {
            this.fouceDate = fouceDate;
        }

        public Object getHHalfScore() {
            return hHalfScore;
        }

        public void setHHalfScore(Object hHalfScore) {
            this.hHalfScore = hHalfScore;
        }

        public Object getAHalfScore() {
            return aHalfScore;
        }

        public void setAHalfScore(Object aHalfScore) {
            this.aHalfScore = aHalfScore;
        }

        public int getHRed() {
            return hRed;
        }

        public void setHRed(int hRed) {
            this.hRed = hRed;
        }

        public int getARed() {
            return aRed;
        }

        public void setARed(int aRed) {
            this.aRed = aRed;
        }

        public int getHYellow() {
            return hYellow;
        }

        public void setHYellow(int hYellow) {
            this.hYellow = hYellow;
        }

        public int getAYellow() {
            return aYellow;
        }

        public void setAYellow(int aYellow) {
            this.aYellow = aYellow;
        }

        public int getIsOver() {
            return isOver;
        }

        public void setIsOver(int isOver) {
            this.isOver = isOver;
        }

        public String getHOrderFrom() {
            return hOrderFrom;
        }

        public void setHOrderFrom(String hOrderFrom) {
            this.hOrderFrom = hOrderFrom;
        }

        public String getAOrderFrom() {
            return aOrderFrom;
        }

        public void setAOrderFrom(String aOrderFrom) {
            this.aOrderFrom = aOrderFrom;
        }

        public String getLiveCastUrl() {
            return liveCastUrl;
        }

        public void setLiveCastUrl(String liveCastUrl) {
            this.liveCastUrl = liveCastUrl;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getProtime() {
            return protime;
        }

        public void setProtime(String protime) {
            this.protime = protime;
        }

        public PayRateBeanX getPayRate() {
            return payRate;
        }

        public void setPayRate(PayRateBeanX payRate) {
            this.payRate = payRate;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public static class PayRateBeanX implements Serializable {
            /**
             * id : 826338
             * home : 沙尔克04
             * away : 拜仁
             * payTypeName : 当前让球
             * payTypeID : 2
             * realRate1 : 7.72
             * realRate2 : 0/0
             * realRate3 : 2.12
             */

            private int id;
            private String home;
            private String away;
            private String payTypeName;
            private int payTypeID;
            private String realRate1;
            private String realRate2;
            private String realRate3;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getHome() {
                return home;
            }

            public void setHome(String home) {
                this.home = home;
            }

            public String getAway() {
                return away;
            }

            public void setAway(String away) {
                this.away = away;
            }

            public String getPayTypeName() {
                return payTypeName;
            }

            public void setPayTypeName(String payTypeName) {
                this.payTypeName = payTypeName;
            }

            public int getPayTypeID() {
                return payTypeID;
            }

            public void setPayTypeID(int payTypeID) {
                this.payTypeID = payTypeID;
            }

            public String getRealRate1() {
                return realRate1;
            }

            public void setRealRate1(String realRate1) {
                this.realRate1 = realRate1;
            }

            public String getRealRate2() {
                return realRate2;
            }

            public void setRealRate2(String realRate2) {
                this.realRate2 = realRate2;
            }

            public String getRealRate3() {
                return realRate3;
            }

            public void setRealRate3(String realRate3) {
                this.realRate3 = realRate3;
            }
        }
    }
}
