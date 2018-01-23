package com.dading.ssqs.fragment.guesstheball.today

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout

import com.dading.ssqs.LocaleController
import com.dading.ssqs.NotificationController
import com.dading.ssqs.R
import com.dading.ssqs.SSQSApplication
import com.dading.ssqs.adapter.newAdapter.ScrollBallResultAdapter
import com.dading.ssqs.apis.CcApiResult
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.bean.CommonTitle
import com.dading.ssqs.bean.ScoreBean
import com.dading.ssqs.bean.ScrollBallFootBallResultBean
import com.dading.ssqs.cells.GuessFilterCell
import com.dading.ssqs.cells.ResultTimeLayout
import com.dading.ssqs.components.FilterDialog
import com.dading.ssqs.components.LoadingDialog
import com.dading.ssqs.components.PageDialog
import com.dading.ssqs.components.RecyclerScrollview
import com.dading.ssqs.components.ResultSelectTimeDialog
import com.dading.ssqs.components.SelectMatchDialog
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout
import com.dading.ssqs.fragment.guesstheball.DataController
import com.dading.ssqs.utils.DateUtils
import com.dading.ssqs.utils.ToastUtils

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

/**
 * Created by mazhuang on 2017/12/7.
 * 今日-足球-赛果
 */

class ToDayResultFragment : Fragment(), OnRefreshListener, NotificationController.NotificationControllerDelegate {

    private var swipeToLoadLayout: SwipeToLoadLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var adapter: ScrollBallResultAdapter? = null
    private var loadingDialog: LoadingDialog? = null
    private var filterCell: GuessFilterCell? = null
    private var pageDialog: PageDialog? = null
    private var filterDialog: FilterDialog? = null
    private var selectTimeDialog: ResultSelectTimeDialog? = null
    private var selectMatchDialog: SelectMatchDialog? = null
    private var defaultView: ImageView? = null
    private var resultTimeLayout: ResultTimeLayout? = null

    private var offset = 1
    private val limit = 20

    private var totalPage: Int = 0
    private var isRefresh = false

    private var sType = 0

    private var filter_str = "按时间顺序"

    private var leagueIDs = "0"
    private var isFilter = false

    private var currTime: String? = null

    private val timesArray = ArrayList<ResultSelectTimeDialog.ResultSelectTimeBean>()

    private val time: List<ResultSelectTimeDialog.ResultSelectTimeBean>
        get() {
            if (timesArray.size > 0) {
                return timesArray
            } else {
                val calendar = Calendar.getInstance()

                for (i in 1..7) {
                    calendar.time = Date()
                    calendar.add(Calendar.DATE, -i)

                    val resultDate = calendar.time
                    val sdf = SimpleDateFormat("yyyy-MM-dd")

                    val weekDay = calendar.get(Calendar.DAY_OF_WEEK)

                    var week = ""

                    when (weekDay) {
                        Calendar.MONDAY -> week = "星期一"
                        Calendar.TUESDAY -> week = "星期二"
                        Calendar.WEDNESDAY -> week = "星期三"
                        Calendar.THURSDAY -> week = "星期四"
                        Calendar.FRIDAY -> week = "星期五"
                        Calendar.SATURDAY -> week = "星期六"
                        Calendar.SUNDAY -> week = "星期日"
                    }

                    timesArray.add(ResultSelectTimeDialog.ResultSelectTimeBean("(" + week + ")　" + sdf.format(resultDate), i))
                }
                return timesArray
            }
        }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        NotificationController.getInstance().addObserver(this, NotificationController.footBallFilter)
        return initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        filterCell?.destoryRunnable(false)

        NotificationController.getInstance().removeObserver(this, NotificationController.footBallFilter)
    }

    fun filterPause() {
        if (filterCell != null && filterCell!!.startStatus) {
            filterCell!!.destoryRunnable(false)
        }
    }

    fun filterResume() {
        if (filterCell != null && filterCell!!.startStatus) {
            filterCell!!.beginRunnable(false)
        }
    }

    private fun initView(): View {
        val container = RelativeLayout(context)

        val contentLayout = LinearLayout(context)
        contentLayout.orientation = LinearLayout.VERTICAL
        container.addView(contentLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        filterCell = GuessFilterCell(context)
        filterCell!!.setSecondRefresh(180)
        filterCell!!.setRefreshListener {
            if (isFilter) {
                filterRefresh()
            } else {
                swipeToLoadLayout!!.isRefreshing = true
            }
        }
        filterCell!!.setSelectClickListener {
            if (selectMatchDialog == null) {
                selectMatchDialog = SelectMatchDialog(context)
                selectMatchDialog!!.setListener { list, isAll ->
                    if (isAll) {
                        filterCell!!.setSelectText(LocaleController.getString(R.string.select_all))
                    } else {
                        filterCell!!.setSelectText("选择联赛(" + list.size + ")")
                    }
                    leagueIDs = ""

                    for (i in list.indices) {
                        leagueIDs += list[i] + ","
                    }

                    if (leagueIDs.isNotEmpty()) {
                        leagueIDs = leagueIDs.substring(0, leagueIDs.length - 1)
                    }

                    offset = 1

                    filterRefresh()
                }
            }
            //判断是否有联赛的数据  没有的话网路请求
            if (DataController.getInstance().todayResultData == null) {
                DataController.getInstance().syncScrollFootBall(TAG, currTime!!)
                loadingDialog!!.show()
            } else {
                selectMatchDialog!!.show(DataController.getInstance().todayResultData, DataController.getInstance().todayResultHotData, "联赛选择")
            }
        }
        filterCell!!.setFilterClickListener {
            if (filterDialog == null) {
                filterDialog = FilterDialog(context)
                filterDialog!!.setItemListener { title ->
                    filter_str = title

                    filterDialog!!.dismiss()

                    filterCell!!.setTimeText(title)

                    sType = if ("按时间排序" == title) {
                        0
                    } else {
                        1
                    }

                    offset = 1

                    filterRefresh()
                }
            }
            filterDialog!!.show("顺序选择", filter_str)
        }
        filterCell!!.setPageClickListener {
            if (pageDialog == null) {
                pageDialog = PageDialog(context)
                pageDialog!!.setItemListener { page ->
                    pageDialog!!.dismiss()

                    offset = page

                    filterRefresh()
                }
            }

            val pageList = ArrayList<Int>()

            pageList += 1..totalPage

            pageDialog!!.show(pageList, "页数选择", offset)
        }
        contentLayout.addView(filterCell)

        val view = View.inflate(context, R.layout.fragment_scrollball, null)
        contentLayout.addView(view)

        swipeToLoadLayout = view.findViewById<View>(R.id.swipeToLoadLayout) as SwipeToLoadLayout
        //为swipeToLoadLayout设置下拉刷新监听者
        swipeToLoadLayout!!.setOnRefreshListener(this)
        swipeToLoadLayout!!.setRefreshEnabled(false)//初始先不能刷新

        val scrollview = view.findViewById<View>(R.id.swipe_target) as RecyclerScrollview

        val scrollContainer = LinearLayout(context)
        scrollContainer.orientation = LinearLayout.VERTICAL
        scrollview.addView(scrollContainer, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        resultTimeLayout = ResultTimeLayout(context)
        resultTimeLayout!!.setTime(DateUtils.getCurTimeAddND(-1, "yyyy-MM-dd"))
        resultTimeLayout!!.setListener(object : ResultTimeLayout.TimeListener {
            override fun onChange(day: Int) {
                setSelectTime(day)
            }

            override fun onSelectTime() {
                if (selectTimeDialog == null) {
                    selectTimeDialog = ResultSelectTimeDialog(context)
                    selectTimeDialog!!.setItemListener { day ->
                        resultTimeLayout!!.day = day

                        selectTimeDialog!!.dismiss()

                        resultTimeLayout!!.changeImageStyle(day)

                        setSelectTime(day)
                    }
                }
                selectTimeDialog!!.show(time, "选择时间", resultTimeLayout!!.day)
            }
        })
        scrollContainer.addView(resultTimeLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 28))

        mRecyclerView = RecyclerView(context)
        scrollContainer.addView(mRecyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        adapter = ScrollBallResultAdapter(context)
        mRecyclerView!!.adapter = adapter

        defaultView = ImageView(context)
        defaultView!!.visibility = View.GONE
        defaultView!!.setImageResource(R.mipmap.no_data)
        container.addView(defaultView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT))

        init()
        return container
    }

    fun init() {
        mRecyclerView!!.setHasFixedSize(true)
        mRecyclerView!!.layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.itemAnimator = DefaultItemAnimator()

        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(context)
        }

        loadingDialog!!.show()
        getNetDataWork(offset, limit)
    }

    private fun setSelectTime(day: Int) {
        currTime = DateUtils.getCurTimeAddND(-day, "yyyyMMddHH:mm:ss")
        resultTimeLayout!!.setTime(DateUtils.getCurTimeAddND(-day, "yyyy-MM-dd"))

        DataController.getInstance().clearTodayResultData()

        swipeToLoadLayout!!.isRefreshing = true
    }

    private fun getNetDataWork(off: Int, lim: Int) {
        if (TextUtils.isEmpty(currTime)) {
            currTime = DateUtils.getCurTimeAddND(-1, "yyyyMMddHH:mm:ss")
        }

        SSQSApplication.apiClient(0).getScrollBallList(true, 3, currTime, sType, leagueIDs, off, lim) { result ->
            loadingDialog!!.dismiss()
            swipeToLoadLayout!!.isRefreshing = false
            swipeToLoadLayout!!.setRefreshEnabled(true)

            if (result.isOk) {
                val page = result.data as CcApiResult.ResultScorePage

                filterCell!!.setCurrPage(off)

                if (page?.items != null && page.items.size >= 1) {
                    defaultView!!.visibility = View.GONE

                    filterCell!!.beginRunnable(true)

                    totalPage = page.totalCount

                    filterCell!!.setTotalPage(totalPage)

                    adapter!!.setList(getData(page.items))
                } else {
                    adapter!!.clearData()

                    filterCell!!.destoryRunnable(true)

                    defaultView!!.visibility = View.VISIBLE
                }

                isRefresh = false
            } else {
                adapter!!.clearData()

                filterCell!!.destoryRunnable(true)

                defaultView!!.visibility = View.VISIBLE

                ToastUtils.midToast(context, result.message, 1000)
            }
        }
    }

    //筛选刷新
    private fun filterRefresh() {
        if (!isRefresh) {
            isRefresh = true
            isFilter = true

            loadingDialog?.show()
            getNetDataWork(offset, limit)
        }
    }

    override fun onRefresh() {
        if (!isRefresh) {
            isRefresh = true
            isFilter = false

            filterCell!!.setSelectText(LocaleController.getString(R.string.select_all))
            leagueIDs = "0"
            sType = 0

            offset = 1
            getNetDataWork(offset, limit)
        }
    }


    private fun getData(data: List<ScoreBean>): List<ScrollBallFootBallResultBean> {

        val list = getFilterData(data)

        val items = ArrayList<ScrollBallFootBallResultBean>()

        for (i in list.indices) {
            val bean = ScrollBallFootBallResultBean()
            bean.title = list[i]
            bean.items = handleOtherData(data, list[i])

            items.add(bean)
        }

        return items
    }

    //获取筛选完的数据
    private fun getFilterData(data: List<ScoreBean>): List<CommonTitle> {
        val list = ArrayList<CommonTitle>()

        for (i in data.indices) {
            if (list.size > 0) {
                val isAdd = list.indices
                        .map { list[it] }
                        .none { it.title == data[i].leagueName }

                if (isAdd) {
                    val title = CommonTitle()
                    title.id = data[i].id
                    title.title = data[i].leagueName

                    list.add(title)
                }
            } else {

                val title = CommonTitle()
                title.id = data[i].id
                title.title = data[i].leagueName

                list.add(title)
            }
        }
        return list
    }

    //对返回数据的处理  返回的数据用不了 各种格式不同 解析成自己能用的  冗余代码
    private fun handleOtherData(currData: List<ScoreBean>, title: CommonTitle): List<ScrollBallFootBallResultBean.ScrollBallFootBallResultItems> {
        val beanItems2 = ArrayList<ScrollBallFootBallResultBean.ScrollBallFootBallResultItems>()

        //半场/全场 数据
        for (i in currData.indices) {
            if (currData[i].leagueName == title.title) {
                val item = ScrollBallFootBallResultBean.ScrollBallFootBallResultItems()
                item.id = currData[i].id
                item.title = currData[i].home
                item.byTitle = currData[i].away
                item.integral1 = currData[i].hHalfScore
                item.integral2 = currData[i].hScore
                item.integral3 = currData[i].aHalfScore
                item.integral4 = currData[i].aScore

                var result = true

                try {
                    val hscore = Integer.valueOf(currData[i].hScore)!!
                    val ascore = Integer.valueOf(currData[i].aScore)!!

                    result = hscore >= ascore
                } catch (ex: Exception) {
                    Log.e("result", "failure")
                }

                item.isHome = result

                beanItems2.add(item)
            }
        }

        return beanItems2
    }

    override fun didReceivedNotification(id: Int, vararg args: String) {
        if (id == NotificationController.footBallFilter) {
            if (args?.isNotEmpty()) {
                if (TAG == args[0]) {
                    loadingDialog!!.dismiss()
                    selectMatchDialog!!.show(DataController.getInstance().todayResultData, DataController.getInstance().todayResultHotData, "联赛选择")
                }
            }
        }
    }

    companion object {
        val TAG = "ToDayResultFragment"
    }
}
