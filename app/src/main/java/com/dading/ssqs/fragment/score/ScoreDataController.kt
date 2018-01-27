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

class ScoreDataController {

    var footBallImmediateData: List<GusessChoiceBean>? = null
        private set//足球即时筛选数据
    var footBallImmediateHotData: List<GusessChoiceBean>? = null
        private set//足球即时筛选热门数据
    var footBallResultData: List<GusessChoiceBean>? = null
        private set//足球赛果
    var footBallResultHotData: List<GusessChoiceBean>? = null
        private set//足球赛果热门
    var footBallScheduleData: List<GusessChoiceBean>? = null
        private set//足球赛程
    var footBallScheduleHotData: List<GusessChoiceBean>? = null
        private set//足球赛程热门

    var basketBallImmediateData: List<GusessChoiceBean>? = null
        private set//篮球即时筛选数据
    var basketBallImmediateHotData: List<GusessChoiceBean>? = null
        private set//篮球即时筛选热门数据
    var basketBallResultData: List<GusessChoiceBean>? = null
        private set//篮球赛果
    var basketBallResultHotData: List<GusessChoiceBean>? = null
        private set//篮球赛果热门
    var basketBallScheduleData: List<GusessChoiceBean>? = null
        private set//篮球赛程
    var basketBallScheduleHotData: List<GusessChoiceBean>? = null
        private set//篮球赛程热门

    fun clearFootBallResult() {
        footBallResultData = null
        footBallResultHotData = null
    }

    fun clearFootBallSchedule() {
        footBallScheduleData = null
        footBallScheduleHotData = null
    }

    fun clearBasketBallResult() {
        basketBallResultData = null
        basketBallResultHotData = null
    }

    fun clearBasketBallSchedule() {
        basketBallScheduleData = null
        basketBallScheduleHotData = null
    }

    //获取足球
    fun syncFootBall(tag: String, type: Int, date: String) {
        syncFootBallData(tag, type, date)
    }

    //获取篮球
    fun syncBasketBall(tag: String, type: Int, date: String) {
        syncBasketBallData(tag, type, date)
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
                    2 -> {
                        basketBallImmediateData = result.data as List<GusessChoiceBean>

                        selectAll(basketBallImmediateData)
                    }
                    3 -> {
                        basketBallResultData = result.data as List<GusessChoiceBean>

                        selectAll(basketBallResultData)
                    }
                    4 -> {
                        basketBallScheduleData = result.data as List<GusessChoiceBean>

                        selectAll(basketBallScheduleData)
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
                    2 -> {
                        basketBallImmediateHotData = result.data as List<GusessChoiceBean>

                        selectAll(basketBallImmediateHotData)
                    }
                    3 -> {
                        basketBallResultHotData = result.data as List<GusessChoiceBean>

                        selectAll(basketBallResultHotData)
                    }
                    4 -> {
                        basketBallScheduleHotData = result.data as List<GusessChoiceBean>

                        selectAll(basketBallScheduleHotData)
                    }
                }

                NotificationController.getInstance().postNotification(NotificationController.scoreBasketBallFilter, tag, type.toString())
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
                when (type) {
                    2 -> {
                        footBallImmediateHotData = result.data as List<GusessChoiceBean>

                        selectAll(footBallImmediateHotData)
                    }
                    3 -> {
                        footBallResultHotData = result.data as List<GusessChoiceBean>

                        selectAll(footBallResultHotData)
                    }
                    4 -> {
                        footBallScheduleHotData = result.data as List<GusessChoiceBean>

                        selectAll(footBallScheduleHotData)
                    }
                }
            }

            NotificationController.getInstance().postNotification(NotificationController.scoreFootBallFilter, tag, type.toString())
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
                when (type) {
                    2 -> {
                        footBallImmediateData = result.data as List<GusessChoiceBean>

                        selectAll(footBallImmediateData)
                    }
                    3 -> {
                        footBallResultData = result.data as List<GusessChoiceBean>

                        selectAll(footBallResultData)
                    }
                    4 -> {
                        footBallScheduleData = result.data as List<GusessChoiceBean>

                        selectAll(footBallScheduleData)
                    }
                }
            }

            syncFootBallHotData(tag, type, date)
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

        fun getInstance(): ScoreDataController {
            return Instance.dataController
        }
    }

    private object Instance {
        val dataController = ScoreDataController()
    }
}
