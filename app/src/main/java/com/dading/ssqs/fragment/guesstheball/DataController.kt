package com.dading.ssqs.fragment.guesstheball

import android.text.TextUtils

import com.dading.ssqs.NotificationController
import com.dading.ssqs.SSQSApplication
import com.dading.ssqs.apis.CcApiClient
import com.dading.ssqs.apis.CcApiResult
import com.dading.ssqs.bean.GusessChoiceBean
import com.dading.ssqs.fragment.guesstheball.early.EarlyResultFragment
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallResultFragment
import com.dading.ssqs.fragment.guesstheball.today.ToDayResultFragment
import com.dading.ssqs.utils.DateUtils

/**
 * Created by mazhuang on 2017/12/12.
 */

class DataController {

    var footBallData: List<GusessChoiceBean>? = null
        private set//足球滚球筛选数据
    var footBallHotData: List<GusessChoiceBean>? = null
        private set//足球滚球筛选热门数据
    var baskteBallData: List<GusessChoiceBean>? = null
        private set//篮球滚球筛选数据
    var basketBallHotData: List<GusessChoiceBean>? = null
        private set//篮球滚球筛选热门数据
    var scrollResultData: List<GusessChoiceBean>? = null
        private set//滚球赛果
    var scrollResultHotData: List<GusessChoiceBean>? = null
        private set//滚球赛果热门

    var todayFootBallData: List<GusessChoiceBean>? = null
        private set//足球今日筛选数据
    var todayFootBallHotData: List<GusessChoiceBean>? = null
        private set//足球今日筛选热门数据
    var todayBaskteBallData: List<GusessChoiceBean>? = null
        private set//篮球今日筛选数据
    var todayBasketBallHotData: List<GusessChoiceBean>? = null
        private set//篮球今日筛选热门数据
    var todayResultData: List<GusessChoiceBean>? = null
        private set//今日赛果
    var todayResultHotData: List<GusessChoiceBean>? = null
        private set//今日赛果热门

    var earlyFootBallData: List<GusessChoiceBean>? = null
        private set//足球早盘筛选数据
    var earlyFootBallHotData: List<GusessChoiceBean>? = null
        private set//足球早盘筛选热门数据
    var earlyBaskteBallData: List<GusessChoiceBean>? = null
        private set//篮球早盘筛选数据
    var earlyBasketBallHotData: List<GusessChoiceBean>? = null
        private set//篮球早盘筛选热门数据
    var earlyResultData: List<GusessChoiceBean>? = null
        private set//早盘赛果
    var earlyResultHotData: List<GusessChoiceBean>? = null
        private set//早盘赛果热门

    fun clearEarlyFootBallData() {
        earlyFootBallData = null
        earlyFootBallHotData = null
    }

    fun clearEarlyBasketBallData() {
        earlyBaskteBallData = null
        earlyBasketBallHotData = null
    }

    fun clearScrollFootBallData() {
        footBallData = null
        footBallHotData = null
    }

    fun clearToDayFootBallData() {
        todayFootBallData = null
        todayFootBallHotData = null
    }

    fun clearScrollResultData() {
        scrollResultData = null
        scrollResultHotData = null
    }

    fun clearTodayResultData() {
        todayResultData = null
        todayResultHotData = null
    }

    fun clearEarlyResultData() {
        earlyResultData = null
        earlyResultHotData = null
    }

    //获取足球
    fun syncFootBall(tag: String, type: Int) {
        syncFootBallData(tag, type, "")
    }

    //获取赛果
    fun syncScrollFootBall(tag: String, date: String) {
        syncFootBallData(tag, 3, date)
    }

    //获取早盘足球
    fun syncEarlyFootBall(tag: String, date: String) {
        syncFootBallData(tag, 7, date)
    }

    //获取篮球
    fun syncBasketBall(tag: String, type: Int) {
        syncBasketBallData(tag, type, "")
    }

    //获取早盘篮球
    fun syncEarlyBasketBall(tag: String, date: String) {
        syncBasketBallData(tag, 7, date)
    }

    private fun syncBasketBallData(tag: String, type: Int, date: String) {
        val mDate: String = if (!TextUtils.isEmpty(date)) {
            date
        } else {
            DateUtils.getCurTime("yyyyMMddHH:mm:ss")
        }

        SSQSApplication.apiClient(0).getMatchBasketBallFilterList(type, 0, mDate) { result ->
            if (result.isOk) {
                when (type) {
                    6 -> {
                        baskteBallData = result.data as List<GusessChoiceBean>

                        selectAll(baskteBallData)
                    }
                    2 -> {
                        todayBaskteBallData = result.data as List<GusessChoiceBean>

                        selectAll(todayBaskteBallData)
                    }
                    7 -> {
                        earlyBaskteBallData = result.data as List<GusessChoiceBean>

                        selectAll(earlyBaskteBallData)
                    }
                }

                syncBasketBallHotData(tag, type, date)
            }
        }
    }

    private fun syncBasketBallHotData(tag: String, type: Int, date: String) {
        val mDate: String = if (!TextUtils.isEmpty(date)) {
            date
        } else {
            DateUtils.getCurTime("yyyyMMddHH:mm:ss")
        }

        SSQSApplication.apiClient(0).getMatchBasketBallFilterList(type, 1, mDate) { result ->
            if (result.isOk) {
                when (type) {
                    6 -> {
                        basketBallHotData = result.data as List<GusessChoiceBean>

                        selectAll(basketBallHotData)
                    }
                    2 -> {
                        todayBasketBallHotData = result.data as List<GusessChoiceBean>

                        selectAll(todayBasketBallHotData)
                    }
                    7 -> {
                        earlyBasketBallHotData = result.data as List<GusessChoiceBean>

                        selectAll(earlyBasketBallHotData)
                    }
                }

                NotificationController.getInstance().postNotification(NotificationController.basketBallFilter, tag)
            }
        }
    }

    private fun syncFootBallHotData(tag: String, type: Int, date: String) {
        val mDate: String = if (!TextUtils.isEmpty(date)) {
            date
        } else {
            DateUtils.getCurTime("yyyyMMddHH:mm:ss")
        }

        SSQSApplication.apiClient(0).getMatchFilterList(type, 1, mDate) { result ->
            if (result.isOk) {
                when (tag) {
                    ScrollBallResultFragment.TAG -> {
                        scrollResultHotData = result.data as List<GusessChoiceBean>

                        selectAll(scrollResultHotData)
                    }
                    ToDayResultFragment.TAG -> {
                        todayResultHotData = result.data as List<GusessChoiceBean>

                        selectAll(todayResultHotData)
                    }
                    EarlyResultFragment.TAG -> {
                        earlyResultHotData = result.data as List<GusessChoiceBean>

                        selectAll(earlyResultHotData)
                    }
                    else -> when (type) {
                        6 -> {
                            footBallHotData = result.data as List<GusessChoiceBean>

                            selectAll(footBallHotData)
                        }
                        2 -> {
                            todayFootBallHotData = result.data as List<GusessChoiceBean>

                            selectAll(todayFootBallHotData)
                        }
                        7 -> {
                            earlyFootBallHotData = result.data as List<GusessChoiceBean>

                            selectAll(earlyFootBallHotData)
                        }
                    }
                }

                NotificationController.getInstance().postNotification(NotificationController.footBallFilter, tag)
            }
        }
    }

    private fun syncFootBallData(tag: String, type: Int, date: String) {
        val mDate: String = if (!TextUtils.isEmpty(date)) {
            date
        } else {
            DateUtils.getCurTime("yyyyMMddHH:mm:ss")
        }

        SSQSApplication.apiClient(0).getMatchFilterList(type, 0, mDate) { result ->
            if (result.isOk) {
                when (tag) {
                    ScrollBallResultFragment.TAG -> {
                        scrollResultData = result.data as List<GusessChoiceBean>

                        selectAll(footBallData)
                    }
                    ToDayResultFragment.TAG -> {
                        todayResultData = result.data as List<GusessChoiceBean>

                        selectAll(todayResultData)
                    }
                    EarlyResultFragment.TAG -> {
                        earlyResultData = result.data as List<GusessChoiceBean>

                        selectAll(earlyResultData)
                    }
                    else -> when (type) {
                        6 -> {
                            footBallData = result.data as List<GusessChoiceBean>

                            selectAll(footBallData)
                        }
                        2 -> {
                            todayFootBallData = result.data as List<GusessChoiceBean>

                            selectAll(todayFootBallData)
                        }
                        7 -> {
                            earlyFootBallData = result.data as List<GusessChoiceBean>

                            selectAll(earlyFootBallData)
                        }
                    }
                }

                syncFootBallHotData(tag, type, date)
            }
        }
    }

    //改成默认全部选中
    private fun selectAll(list: List<GusessChoiceBean>?) {
        if (list != null) {
            for (i in list.indices) {
                val filters = list[i].filter
                for (j in filters.indices) {
                    filters[j].checked = true
                }
            }
        }
    }

    companion object {
        val TAG = "DataController"

        fun getInstance(): DataController {
            return Instance.dataController
        }
    }

    private object Instance {
        val dataController = DataController()
    }
}
