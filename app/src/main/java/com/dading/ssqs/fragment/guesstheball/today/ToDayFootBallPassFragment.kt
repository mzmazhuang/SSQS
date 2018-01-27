package com.dading.ssqs.fragment.guesstheball.today

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast

import com.dading.ssqs.LocaleController
import com.dading.ssqs.NotificationController
import com.dading.ssqs.R
import com.dading.ssqs.SSQSApplication
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter
import com.dading.ssqs.adapter.newAdapter.ScrollBallItemAdapter
import com.dading.ssqs.adapter.newAdapter.ToDayBasketBallPassAdapter
import com.dading.ssqs.apis.CcApiResult
import com.dading.ssqs.apis.elements.PayBallElement
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.bean.CommonTitle
import com.dading.ssqs.bean.JCbean
import com.dading.ssqs.bean.ScoreBean
import com.dading.ssqs.bean.ScrollBallFootBallBean
import com.dading.ssqs.cells.GuessFilterCell
import com.dading.ssqs.cells.ScrollBallCell
import com.dading.ssqs.components.FilterDialog
import com.dading.ssqs.components.LoadingDialog
import com.dading.ssqs.components.PageDialog
import com.dading.ssqs.components.RecyclerScrollview
import com.dading.ssqs.components.ScrollBallCommitMenuView
import com.dading.ssqs.components.ScrollBallCommitView
import com.dading.ssqs.components.SelectMatchDialog
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout
import com.dading.ssqs.fragment.guesstheball.DataController
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment
import com.dading.ssqs.utils.AndroidUtilities
import com.dading.ssqs.utils.DateUtils
import com.dading.ssqs.utils.ToastUtils

import java.util.ArrayList

/**
 * Created by mazhuang on 2017/12/8.
 * 今日-足球-综合过关
 */

class ToDayFootBallPassFragment : Fragment(), OnRefreshListener, NotificationController.NotificationControllerDelegate {

    private var swipeToLoadLayout: SwipeToLoadLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var adapter: ToDayBasketBallPassAdapter? = null
    private var loadingDialog: LoadingDialog? = null
    private var pageDialog: PageDialog? = null
    private var filterDialog: FilterDialog? = null
    private var selectMatchDialog: SelectMatchDialog? = null
    private var filterCell: GuessFilterCell? = null
    private var commitView: ScrollBallCommitView? = null
    private var commitMenuView: ScrollBallCommitMenuView? = null
    private var defaultView: ImageView? = null

    private var offset = 1
    private val limit = 20
    private var isRefresh = false

    private var sType = 0

    private var totalPage: Int = 0

    private var originalData: List<ScoreBean>? = null//原始数据
    private var currBasketBallDefaultBean: ScrollBallFootBallBean? = null
    private val newData = ArrayList<ScrollBallFootBallBean>()
    private val networkData = ArrayList<ScoreBean>()

    private val leagusList = ArrayList<ScrollBallDefaultFragment.MergeBean>()

    private var filter_str = "按时间顺序"

    private var leagueIDs = "0"
    private var isFilter = false

    private val itemClickListener = object : ScrollBallItemAdapter.OnItemClickListener {
        
        override fun onItemClick(id: Int, bean: ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem, items: ScrollBallFootBallBean.ScrollBeanItems, isAdd: Boolean, isHome: Boolean, position: Int): Boolean {
            //联赛id                                      //所点击的item                          //所点击的联赛信息                                                //是否是主场       //点击的位置
            leagusList.indices
                    .map { leagusList[it] }
                    .filter { it.items!!.id == items.id }
                    .forEach {
                        if (isAdd) {
                            return false
                        }
                    }
            if (isAdd) {//是添加 还是删除
                if (leagusList.size == 0) {
                    val mergeBean = ScrollBallDefaultFragment.MergeBean()
                    mergeBean.items = items

                    for (i in originalData!!.indices) {
                        if (originalData!![i].id == id) {
                            mergeBean.title = originalData!![i].leagueName
                            break
                        }
                    }

                    val beanItems = ArrayList<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem>()
                    bean.position = position
                    beanItems.add(bean)

                    mergeBean.bean = beanItems

                    leagusList.add(mergeBean)
                } else {
                    var isNew = true//是否添加新的数据
                    for (i in leagusList.indices) {
                        val mergeBean = leagusList[i]

                        if (mergeBean.items!!.id == items.id) {//一个比赛下 不同的item
                            isNew = false

                            val beanItems = mergeBean.bean
                            bean.position = position
                            beanItems!!.add(bean)

                            mergeBean.bean = beanItems
                            break
                        }
                    }

                    if (isNew) {
                        val mergeBean = ScrollBallDefaultFragment.MergeBean()
                        mergeBean.items = items

                        for (i in originalData!!.indices) {
                            if (originalData!![i].id == id) {
                                mergeBean.title = originalData!![i].leagueName
                                break
                            }
                        }

                        val beanItems = ArrayList<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem>()
                        bean.position = position
                        beanItems.add(bean)

                        mergeBean.bean = beanItems

                        leagusList.add(mergeBean)
                    }
                }
            } else {
                val mergeBeanIterator = leagusList.iterator()

                while (mergeBeanIterator.hasNext()) {
                    val mergeBean = mergeBeanIterator.next()

                    if (mergeBean.items!!.id == items.id) {
                        val beanItems = mergeBean.bean

                        val beanItemIterator = beanItems!!.iterator()
                        while (beanItemIterator.hasNext()) {
                            val beanItem = beanItemIterator.next()
                            if (beanItem.id == bean.id) {
                                beanItemIterator.remove()

                                if (beanItems.size == 0) {
                                    mergeBeanIterator.remove()
                                }
                                break
                            }
                        }
                    }
                }
            }

            if (leagusList.size == 0) {
                commitView!!.visibility = View.GONE
            } else {
                commitView!!.setCount(leagusList.size)
                commitView!!.visibility = View.VISIBLE
            }

            return true
        }

        override fun onAllClick(id: Int) {

        }
    }

    private val menuListener = ScrollBallCommitMenuAdapter.OnMenuClickListener { position, dataId, itemId, value ->
        commitMenuView!!.changeData(position)

        val thread = Thread(Runnable {
            val iterator = leagusList.iterator()

            while (iterator.hasNext()) {
                val mergeBean = iterator.next()

                val items = mergeBean.bean

                if (mergeBean.items!!.id == itemId) {
                    val itemIterator = items!!.iterator()

                    while (itemIterator.hasNext()) {
                        val beanItem = itemIterator.next()

                        if (beanItem.id == dataId && beanItem.rightStr == value) {
                            itemIterator.remove()

                            if (items.size == 0) {
                                iterator.remove()
                            }
                            break
                        }
                    }
                }
            }

            SSQSApplication.getHandler().post {
                adapter!!.setFocus(leagusList)
                adapter!!.refreshData()

                if (leagusList.size >= 1) {
                    commitView!!.setCount(leagusList.size)
                }
            }
        })
        SSQSApplication.cachedThreadPool.execute(thread)
    }

    private val readyListener = ScrollBallCell.OnReadyListener { title ->
        newData.clear()
        networkData.clear()

        loadingDialog!!.show()

        //当前联赛下面的item 取出来 根据id去获取对应的数据
        originalData!!.indices
                .filter { originalData!![it].leagueName == title }
                .mapTo(networkData) { originalData!![it] }

        newData.addAll(adapter!!.data)//当前的数据

        //找到当前点击的那一个item
        for (i in newData.indices) {
            val temporaryBean = newData[i]
            if (temporaryBean.title.title == title) {
                currBasketBallDefaultBean = temporaryBean
                break
            }
        }

        //把子数据添加进去

        if (currBasketBallDefaultBean != null) {
            var params = ""
            for (i in networkData.indices) {
                params += networkData[i].id.toString() + ","
            }
            if (params.isNotEmpty()) {
                params = params.substring(0, params.length - 1)
            }

            SSQSApplication.apiClient(0).getMatchResult(params) { result ->
                if (result.isOk) {
                    val items = result.data as List<JCbean>

                    if (items?.isNotEmpty()) {

                        val thread = Thread(Runnable {
                            currBasketBallDefaultBean!!.items = handleItemData(items, networkData)

                            SSQSApplication.getHandler().post {
                                adapter!!.setOpenTitle(title)
                                adapter!!.setFocus(leagusList)
                                adapter!!.setList(newData)

                                loadingDialog!!.dismiss()
                            }
                        })
                        SSQSApplication.cachedThreadPool.execute(thread)
                    } else {
                        loadingDialog!!.dismiss()

                        ToastUtils.midToast(context, "足球-综合过关 暂无数据!!", 0)
                    }
                } else {
                    ToastUtils.midToast(context, result.message, 0)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        NotificationController.getInstance().addObserver(this, NotificationController.today_mask)
        NotificationController.getInstance().addObserver(this, NotificationController.footBallFilter)
        return initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        filterCell?.destoryRunnable(false)

        NotificationController.getInstance().removeObserver(this, NotificationController.today_mask)
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
            if (DataController.getInstance().todayFootBallData == null) {
                DataController.getInstance().syncFootBall(TAG, 2)
                loadingDialog!!.show()
            } else {
                selectMatchDialog!!.show(DataController.getInstance().todayFootBallData, DataController.getInstance().todayFootBallHotData, "联赛选择")
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
                    isFilter = true

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

        mRecyclerView = RecyclerView(context)
        scrollview.addView(mRecyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        adapter = ToDayBasketBallPassAdapter(context)
        adapter!!.setReadyListener(readyListener)
        adapter!!.setItemClickListener(itemClickListener)
        mRecyclerView!!.adapter = adapter

        commitView = ScrollBallCommitView(context)
        commitView!!.visibility = View.GONE
        commitView!!.setOnSubmitClickListener {
            if (commitMenuView!!.visibility == View.GONE) {
                commitMenuView!!.setTitle("今日-足球")
                commitMenuView!!.setData(leagusList)
                commitMenuView!!.show()

                NotificationController.getInstance().postNotification(NotificationController.today_mask, "open")
            } else {
                pay()
            }
        }
        commitView!!.setDeleteClickListener {
            commitMenuView!!.hide(true)
            commitView!!.visibility = View.GONE

            adapter!!.refreshData()

            leagusList.clear()
        }
        container.addView(commitView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_BOTTOM))

        commitMenuView = ScrollBallCommitMenuView(context, LocaleController.getString(R.string.betting_slips), LocaleController.getString(R.string.latest_ten_transactions))
        commitMenuView!!.setType(2)
        commitMenuView!!.setMenuItemDeleteListener(menuListener)
        commitMenuView!!.setMenuListener(object : ScrollBallCommitMenuView.OnCommitMenuListener {
            override fun onClear() {
                commitView!!.visibility = View.GONE
                leagusList.clear()
            }

            override fun onHide() {
                NotificationController.getInstance().postNotification(NotificationController.today_mask, "close")
            }

            override fun onDone() {
                pay()
            }
        })
        commitMenuView!!.visibility = View.GONE
        container.addView(commitMenuView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        defaultView = ImageView(context)
        defaultView!!.visibility = View.GONE
        defaultView!!.setImageResource(R.mipmap.no_data)
        container.addView(defaultView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT))

        init()
        return container
    }

    private fun pay() {
        if (TextUtils.isEmpty(commitMenuView!!.standResult) || commitMenuView!!.standResult == "0" || Integer.valueOf(commitMenuView!!.standResult) < 10) {
            Toast.makeText(context, "请输入投注金额,并且不能小于10元", Toast.LENGTH_SHORT).show()
            return
        }

        adapter!!.refreshData()

        commitMenuView!!.hide(true)
        commitView!!.visibility = View.GONE

        loadingDialog!!.show()

        val element = PayBallElement()

        val items = ArrayList<PayBallElement.BetBean>()

        var moneys = commitMenuView!!.standResult

        for (i in leagusList.indices) {
            val list = leagusList[i].bean

            for (j in list!!.indices) {
                val bean = PayBallElement.BetBean()
                bean.matchID = leagusList[i].items!!.id
                bean.type = 1
                bean.amount = moneys
                bean.payRateID = list[j].id
                bean.selected = list[j].selected
                items.add(bean)

                moneys = "0"
            }
        }

        element.items = items

        leagusList.clear()

        SSQSApplication.apiClient(0).payBallDouble(element) { result ->
            loadingDialog!!.dismiss()

            if (result.isOk) {
                Toast.makeText(context, "下注成功", Toast.LENGTH_SHORT).show()
            } else {
                if (!AndroidUtilities.checkIsLogin(result.errno, context)) {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
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

    private fun getNetDataWork(off: Int, lim: Int) {
        val mDate = DateUtils.getCurTime("yyyyMMddHH:mm:ss")

        SSQSApplication.apiClient(0).getScrollBallList(true, 2, mDate, sType, leagueIDs, off, lim) { result ->
            loadingDialog!!.dismiss()
            swipeToLoadLayout!!.isRefreshing = false
            swipeToLoadLayout!!.setRefreshEnabled(true)

            if (result.isOk) {
                val page = result.data as CcApiResult.ResultScorePage

                filterCell!!.setCurrPage(off)

                if (page?.items != null && page.items.size >= 1) {
                    defaultView!!.visibility = View.GONE

                    filterCell!!.beginRunnable(true)

                    originalData = page.items

                    totalPage = page.totalCount

                    filterCell!!.setTotalPage(totalPage)

                    adapter!!.setList(getData(getFilterData(page.items)))
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


    private fun getData(list: List<CommonTitle>): List<ScrollBallFootBallBean> {
        val items = ArrayList<ScrollBallFootBallBean>()

        for (i in list.indices) {
            val bean = ScrollBallFootBallBean()
            bean.title = list[i]

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

    private fun getRate2Str(str: String, isBig: Boolean): String {
        var value: String
        if (!TextUtils.isEmpty(str)) {

            var array: Array<String>? = null

            if (str.contains("/")) {
                array = str.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            }

            val rate2: String

            rate2 = if (array != null && array.size == 2) {
                array[1]
            } else {
                str
            }

            try {
                if (isBig) {
                    value = if (rate2 == "0") {
                        "0"
                    } else if (java.lang.Double.valueOf(rate2) > 0.0) {
                        if (array != null) {
                            array[0] + "/" + Math.abs(java.lang.Double.valueOf(rate2)!!) + ""
                        } else {
                            Math.abs(java.lang.Double.valueOf(rate2)!!).toString() + ""
                        }
                    } else {
                        "null"
                    }
                } else {
                    value = if (rate2 == "-0") {
                        "0"
                    } else if (java.lang.Double.valueOf(rate2) < 0.0) {
                        if (array != null) {
                            array[0] + "/" + Math.abs(java.lang.Double.valueOf(rate2)!!) + ""
                        } else {
                            Math.abs(java.lang.Double.valueOf(rate2)!!).toString() + ""
                        }
                    } else {
                        "null"
                    }
                }
            } catch (ex: Exception) {//不是double类型
                value = "null"
            }

        } else {
            value = "null"
        }

        return value
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

    //对返回数据的处理  返回的数据用不了 各种格式不同 解析成自己能用的  冗余代码
    private fun handleItemData(items: List<JCbean>?, currData: List<ScoreBean>): List<ScrollBallFootBallBean.ScrollBeanItems> {
        val beanItems = ArrayList<ScrollBallFootBallBean.ScrollBeanItems>()

        for (i in currData.indices) {
            val item = ScrollBallFootBallBean.ScrollBeanItems()

            val currScoreBean = currData[i]

            item.id = currScoreBean.id
            item.title = currScoreBean.home
            item.byTitle = currScoreBean.away

            val time = DateUtils.changeFormater(currScoreBean.openTime, "yyyy-MM-dd HH:mm:ss", "HH:mm")

            item.time = time

            val scrollBeanItems = ArrayList<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem>()

            val oneRowData = ArrayList<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem>()
            val twoRowData = ArrayList<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem>()
            val threeRowData = ArrayList<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem>()
            val fourRowData = ArrayList<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem>()
            val fiveRowData = ArrayList<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem>()
            val sixRowData = ArrayList<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem>()

            if (items!!.isNotEmpty()) {
                //独赢
                items.indices
                        .filter { currData[i].id == items[it].matchID }
                        .forEach {
                            if ("全场赛果" == items[it].payTypeName) {
                                for (k in 0..2) {
                                    val beanItem = ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem()
                                    beanItem.background = -0x1
                                    beanItem.id = items[it].id
                                    when (k) {
                                        0 -> {
                                            beanItem.selected = 1
                                            beanItem.rightStr = items[it].realRate1
                                            oneRowData.add(beanItem)
                                        }
                                        1 -> {
                                            beanItem.selected = 3
                                            beanItem.rightStr = items[it].realRate2
                                            twoRowData.add(beanItem)
                                        }
                                        else -> {
                                            beanItem.selected = 2
                                            beanItem.rightStr = items[it].realRate3
                                            threeRowData.add(beanItem)
                                        }
                                    }
                                }
                            } else if ("半场赛果" == items[it].payTypeName) {
                                for (k in 0..2) {
                                    val beanItem = ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem()
                                    beanItem.background = -0x524
                                    beanItem.id = items[it].id
                                    when (k) {
                                        0 -> {
                                            beanItem.selected = 1
                                            beanItem.rightStr = items[it].realRate1
                                            fourRowData.add(beanItem)
                                        }
                                        1 -> {
                                            beanItem.selected = 3
                                            beanItem.rightStr = items[it].realRate2
                                            fiveRowData.add(beanItem)
                                        }
                                        else -> {
                                            beanItem.selected = 2
                                            beanItem.rightStr = items[it].realRate3
                                            sixRowData.add(beanItem)
                                        }
                                    }
                                }
                            }
                        }

                if (oneRowData.size == 0) {
                    oneRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(-0x1))
                }
                if (twoRowData.size == 0) {
                    twoRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(-0x1))
                }
                if (threeRowData.size == 0) {
                    threeRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(-0x1))
                }
                if (fourRowData.size == 0) {
                    fourRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem())
                }
                if (fiveRowData.size == 0) {
                    fiveRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem())
                }
                if (sixRowData.size == 0) {
                    sixRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem())
                }

                //让球
                items.indices
                        .filter { currData[i].id == items[it].matchID }
                        .forEach {
                            if ("全场让球" == items[it].payTypeName) {
                                for (k in 0..2) {
                                    val beanItem = ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem()
                                    beanItem.background = -0x1
                                    beanItem.id = items[it].id
                                    when (k) {
                                        0 -> {
                                            beanItem.selected = 1
                                            beanItem.leftStr = getRate2Str(items[it].realRate2, false)
                                            beanItem.rightStr = items[it].realRate1
                                            oneRowData.add(beanItem)
                                        }
                                        1 -> {
                                            beanItem.selected = 2
                                            beanItem.leftStr = getRate2Str(items[it].realRate2, true)
                                            beanItem.rightStr = items[it].realRate3
                                            twoRowData.add(beanItem)
                                        }
                                        else -> threeRowData.add(beanItem)
                                    }
                                }
                            } else if ("半场让球" == items[it].payTypeName) {
                                for (k in 0..2) {
                                    val beanItem = ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem()
                                    beanItem.background = -0x524
                                    beanItem.id = items[it].id
                                    when (k) {
                                        0 -> {
                                            beanItem.selected = 1
                                            beanItem.leftStr = getRate2Str(items[it].realRate2, false)
                                            beanItem.rightStr = items[it].realRate1
                                            fourRowData.add(beanItem)
                                        }
                                        1 -> {
                                            beanItem.selected = 2
                                            beanItem.leftStr = getRate2Str(items[it].realRate2, true)
                                            beanItem.rightStr = items[it].realRate3
                                            fiveRowData.add(beanItem)
                                        }
                                        else -> sixRowData.add(beanItem)
                                    }
                                }
                            }
                        }

                if (oneRowData.size == 1) {
                    oneRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(-0x1))
                }
                if (twoRowData.size == 1) {
                    twoRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(-0x1))
                }
                if (threeRowData.size == 1) {
                    threeRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(-0x1))
                }
                if (fourRowData.size == 1) {
                    fourRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem())
                }
                if (fiveRowData.size == 1) {
                    fiveRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem())
                }
                if (sixRowData.size == 1) {
                    sixRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem())
                }

                //大小
                items.indices
                        .filter { currData[i].id == items[it].matchID }
                        .forEach {
                            if ("全场大小" == items[it].payTypeName) {
                                for (k in 0..2) {
                                    val beanItem = ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem()
                                    beanItem.background = -0x1
                                    beanItem.id = items[it].id
                                    when (k) {
                                        0 -> {
                                            beanItem.selected = 1
                                            beanItem.leftStr = "大" + items[it].realRate2
                                            beanItem.rightStr = items[it].realRate1
                                            oneRowData.add(beanItem)
                                        }
                                        1 -> {
                                            beanItem.selected = 2
                                            beanItem.leftStr = "小" + items[it].realRate2
                                            beanItem.rightStr = items[it].realRate3
                                            twoRowData.add(beanItem)
                                        }
                                        else -> threeRowData.add(beanItem)
                                    }
                                }

                            } else if ("半场大小" == items[it].payTypeName) {
                                for (k in 0..2) {
                                    val beanItem = ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem()
                                    beanItem.background = -0x524
                                    beanItem.id = items[it].id
                                    when (k) {
                                        0 -> {
                                            beanItem.selected = 1
                                            beanItem.leftStr = "大" + items[it].realRate2
                                            beanItem.rightStr = items[it].realRate1
                                            fourRowData.add(beanItem)
                                        }
                                        1 -> {
                                            beanItem.selected = 2
                                            beanItem.leftStr = "小" + items[it].realRate2
                                            beanItem.rightStr = items[it].realRate3
                                            fiveRowData.add(beanItem)
                                        }
                                        else -> sixRowData.add(beanItem)
                                    }
                                }
                            }
                        }

                if (oneRowData.size == 2) {
                    oneRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(-0x1))
                }
                if (twoRowData.size == 2) {
                    twoRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(-0x1))
                }
                if (threeRowData.size == 2) {
                    threeRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(-0x1))
                }
                if (fourRowData.size == 2) {
                    fourRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem())
                }
                if (fiveRowData.size == 2) {
                    fiveRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem())
                }
                if (sixRowData.size == 2) {
                    sixRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem())
                }

                //单双
                items.indices
                        .filter { currData[i].id == items[it].matchID }
                        .forEach {
                            if ("全场单双" == items[it].payTypeName) {
                                for (k in 0..2) {
                                    val beanItem = ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem()
                                    beanItem.background = -0x1
                                    beanItem.id = items[it].id
                                    when (k) {
                                        0 -> {
                                            beanItem.selected = 1
                                            beanItem.rightStr = items[it].realRate1
                                            oneRowData.add(beanItem)
                                        }
                                        1 -> {
                                            beanItem.selected = 2
                                            beanItem.rightStr = items[it].realRate3
                                            twoRowData.add(beanItem)
                                        }
                                        else -> threeRowData.add(beanItem)
                                    }
                                }

                            } else if ("半场单双" == items[it].payTypeName) {
                                for (k in 0..2) {
                                    val beanItem = ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem()
                                    beanItem.background = -0x524
                                    beanItem.id = items[it].id
                                    when (k) {
                                        0 -> {
                                            beanItem.selected = 1
                                            beanItem.rightStr = items[it].realRate1
                                            fourRowData.add(beanItem)
                                        }
                                        1 -> {
                                            beanItem.selected = 2
                                            beanItem.rightStr = items[it].realRate3
                                            fiveRowData.add(beanItem)
                                        }
                                        else -> sixRowData.add(beanItem)
                                    }
                                }
                            }
                        }

                if (oneRowData.size == 3) {
                    oneRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(-0x1))
                }
                if (twoRowData.size == 3) {
                    twoRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(-0x1))
                }
                if (threeRowData.size == 3) {
                    threeRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem(-0x1))
                }
                if (fourRowData.size == 3) {
                    fourRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem())
                }
                if (fiveRowData.size == 3) {
                    fiveRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem())
                }
                if (sixRowData.size == 3) {
                    sixRowData.add(ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem())
                }

                scrollBeanItems.addAll(oneRowData)
                scrollBeanItems.addAll(twoRowData)
                scrollBeanItems.addAll(threeRowData)
                scrollBeanItems.addAll(fourRowData)
                scrollBeanItems.addAll(fiveRowData)
                scrollBeanItems.addAll(sixRowData)

                item.testItems = scrollBeanItems

                beanItems.add(item)
            }
        }
        return beanItems
    }

    override fun didReceivedNotification(id: Int, vararg args: String) {
        if (id == NotificationController.today_mask) {
            if (args?.isNotEmpty()) {
                if ("child_close" == args[0]) {
                    if (commitMenuView != null && commitMenuView!!.visibility == View.VISIBLE) {
                        commitMenuView!!.hide(false)
                    }
                }
            }
        } else if (id == NotificationController.footBallFilter) {
            if (args?.isNotEmpty()) {
                if (TAG == args[0]) {
                    loadingDialog!!.dismiss()
                    selectMatchDialog!!.show(DataController.getInstance().todayFootBallData, DataController.getInstance().todayFootBallHotData, "联赛选择")
                }
            }
        }
    }

    companion object {
        private val TAG = "ToDayFootBallPassFragment"
    }
}
