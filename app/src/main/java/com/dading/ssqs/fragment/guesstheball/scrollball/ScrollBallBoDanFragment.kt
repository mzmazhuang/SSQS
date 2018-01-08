package com.dading.ssqs.fragment.guesstheball.scrollball

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
import com.dading.ssqs.adapter.newAdapter.ScrollBallBoDanAdapter
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter
import com.dading.ssqs.apis.CcApiResult
import com.dading.ssqs.apis.elements.PayBallElement
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.bean.CommonTitle
import com.dading.ssqs.bean.JCScorebean
import com.dading.ssqs.bean.ScoreBean
import com.dading.ssqs.bean.ScrollBallFootBallBoDanBean
import com.dading.ssqs.cells.BoDanChildCell
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
import com.dading.ssqs.utils.AndroidUtilities
import com.dading.ssqs.utils.DateUtils
import com.dading.ssqs.utils.ToastUtils

import java.io.Serializable
import java.util.ArrayList

/**
 * Created by asus on 2017/12/7.
 * 滚球-足球-波胆
 */

class ScrollBallBoDanFragment : Fragment(), OnRefreshListener, NotificationController.NotificationControllerDelegate {

    private var swipeToLoadLayout: SwipeToLoadLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var adapter: ScrollBallBoDanAdapter? = null
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

    private var sType = 0

    private var isRefresh = false

    private var totalPage: Int = 0

    private var originalData: List<ScoreBean>? = null//原始数据

    private var currFootBallBoDanBean: ScrollBallFootBallBoDanBean? = null
    private val newData = ArrayList<ScrollBallFootBallBoDanBean>()
    private val networkData = ArrayList<ScoreBean>()

    private var leagueMatchId = -1//当前所点击的联赛id  用于判断 不能多个联赛一起选
    private val leagusList = ArrayList<MergeBean>()

    private var filter_str = "按时间顺序"

    private var leagueIDs = "0"
    private var isFilter = false

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

                        if (beanItem.id == dataId && beanItem.str == value) {
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

    private val itemClickListener = BoDanChildCell.OnItemClickListener { items, bean, id, isAdd, position ->
        if (leagueMatchId == id || leagueMatchId == -1) {
            leagueMatchId = id

            if (isAdd) {//是添加 还是删除
                if (leagusList.size == 0) {
                    val mergeBean = MergeBean()
                    mergeBean.items = items
                    mergeBean.isHome = bean.selected != 2

                    for (i in originalData!!.indices) {
                        if (originalData!![i].id == id) {
                            mergeBean.title = originalData!![i].leagueName
                            break
                        }
                    }

                    val beanItems = ArrayList<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem>()
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
                        val mergeBean = MergeBean()
                        mergeBean.items = items

                        for (i in originalData!!.indices) {
                            if (originalData!![i].id == id) {
                                mergeBean.title = originalData!![i].leagueName
                                break
                            }
                        }

                        val beanItems = ArrayList<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem>()
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
                leagueMatchId = -1
                commitView!!.visibility = View.GONE
            } else {
                commitView!!.setCount(leagusList.size)
                commitView!!.visibility = View.VISIBLE
            }

            true
        } else {
            false
        }
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
                currFootBallBoDanBean = temporaryBean
                break
            }
        }

        //把子数据添加进去

        if (currFootBallBoDanBean != null) {
            var params = ""
            for (i in networkData.indices) {
                params += networkData[i].id.toString() + ","
            }

            if (params.isNotEmpty()) {
                params = params.substring(0, params.length - 1)
            }

            SSQSApplication.apiClient(0).getScoreByIdList(params) { result ->
                if (result.isOk) {
                    val items = result.data as List<JCScorebean>

                    if (items?.isNotEmpty()) {

                        val thread = Thread(Runnable {
                            currFootBallBoDanBean!!.items = handleOtherData(items, networkData)

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

                        ToastUtils.midToast(context, "波胆 暂无数据!!", 0)
                    }
                } else {
                    ToastUtils.midToast(context, result.message, 0)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        NotificationController.getInstance().addObserver(this, NotificationController.footBallFilter)
        NotificationController.getInstance().addObserver(this, NotificationController.scroll_mask)
        return initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        filterCell?.destoryRunnable(false)

        NotificationController.getInstance().removeObserver(this, NotificationController.footBallFilter)
        NotificationController.getInstance().removeObserver(this, NotificationController.scroll_mask)
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
        filterCell!!.setSecondRefresh(30)
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
            if (DataController.getInstance().footBallData == null) {
                DataController.getInstance().syncFootBall(TAG, 6)
                loadingDialog!!.show()
            } else {
                selectMatchDialog!!.show(DataController.getInstance().footBallData, DataController.getInstance().footBallHotData, "联赛选择")
            }
        }
        filterCell!!.setFilterClickListener {
            if (filterDialog == null) {
                filterDialog = FilterDialog(context)
                filterDialog!!.setItemListener { title ->
                    filter_str = title

                    filterDialog!!.dismiss()

                    filterCell!!.setTimeText(title)

                    sType = if (title == "按时间排序") {
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

        mRecyclerView = RecyclerView(context)
        scrollview.addView(mRecyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        adapter = ScrollBallBoDanAdapter(context)
        adapter!!.setPageType(1)
        adapter!!.setReadyListener(readyListener)
        adapter!!.setListener(itemClickListener)
        mRecyclerView!!.adapter = adapter

        commitView = ScrollBallCommitView(context)
        commitView!!.visibility = View.GONE
        commitView!!.setOnSubmitClickListener {
            if (commitMenuView!!.visibility == View.GONE) {
                commitMenuView!!.setTitle("滚球-足球")
                commitMenuView!!.setBoDanData(leagusList)
                commitMenuView!!.show()

                NotificationController.getInstance().postNotification(NotificationController.scroll_mask, "open")
            } else {
                pay()
            }
        }
        commitView!!.setDeleteClickListener {
            commitMenuView!!.hide(true)
            commitView!!.visibility = View.GONE

            adapter!!.refreshData()

            leagusList.clear()
            leagueMatchId = -1
        }
        container.addView(commitView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_BOTTOM))

        commitMenuView = ScrollBallCommitMenuView(context, LocaleController.getString(R.string.betting_slips), LocaleController.getString(R.string.latest_ten_transactions))
        commitMenuView!!.setMenuItemDeleteListener(menuListener)
        commitMenuView!!.setMenuListener(object : ScrollBallCommitMenuView.OnCommitMenuListener {
            override fun onClear() {
                commitView!!.visibility = View.GONE
                leagusList.clear()
                leagueMatchId = -1
            }

            override fun onHide() {
                NotificationController.getInstance().postNotification(NotificationController.scroll_mask, "close")
            }

            override fun onDone() {
                pay()
            }
        })
        commitMenuView!!.visibility = View.GONE
        container.addView(commitMenuView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        defaultView = ImageView(context)
        defaultView!!.setImageResource(R.mipmap.no_data)
        defaultView!!.visibility = View.GONE
        container.addView(defaultView, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_IN_PARENT))

        init()
        return container
    }

    private fun pay() {
        val moneyLists = commitMenuView!!.money

        val isEmpty = moneyLists.indices
                .map { moneyLists[it] }
                .any { TextUtils.isEmpty(it.money) || it.money == "0" || Integer.valueOf(it.money) < 10 }

        if (isEmpty) {
            Toast.makeText(context, "请输入投注金额,并且不能小于10元", Toast.LENGTH_SHORT).show()
            return
        }

        adapter!!.refreshData()

        commitMenuView!!.hide(true)
        commitView!!.visibility = View.GONE

        loadingDialog!!.show()

        val element = PayBallElement()

        val items = ArrayList<PayBallElement.BetBean>()

        for (i in leagusList.indices) {
            val list = leagusList[i].bean

            for (j in list!!.indices) {
                val bean = PayBallElement.BetBean()
                bean.type = 1
                bean.itemID = list[j].id
                bean.amount = moneyLists[j].money
                items.add(bean)
            }
        }

        element.items = items

        leagusList.clear()
        leagueMatchId = -1

        SSQSApplication.apiClient(0).payBallScore(element) { result ->
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

        SSQSApplication.apiClient(0).getScrollBallList(true, 6, mDate, sType, leagueIDs, off, lim) { result ->
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
            sType = 0;

            offset = 1
            getNetDataWork(offset, limit)
        }
    }


    private fun getData(list: List<CommonTitle>): List<ScrollBallFootBallBoDanBean> {
        val items = ArrayList<ScrollBallFootBallBoDanBean>()

        for (i in list.indices) {
            val bean = ScrollBallFootBallBoDanBean()
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

    //对返回数据的处理  返回的数据用不了 各种格式不同 解析成自己能用的  冗余代码
    private fun handleOtherData(items: List<JCScorebean>?, currData: List<ScoreBean>): List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems> {
        val beanItems1 = ArrayList<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems>()

        //波胆数据
        for (i in currData.indices) {
            val item = ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems()
            item.id = currData[i].id
            item.title = currData[i].home
            item.byTitle = currData[i].away
            item.setaScore(currData[i].aScore)
            item.sethScore(currData[i].hScore)
            item.protTime = currData[i].protime

            val time = DateUtils.changeFormater(currData[i].openTime, "yyyy-MM-dd HH:mm:ss", "HH:mm")

            item.time = time

            val testItems = ArrayList<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem>()

            val twoRowData = ArrayList<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem>()
            twoRowData.add(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(currData[i].home))

            val threeRowData = ArrayList<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem>()
            threeRowData.add(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(currData[i].away))

            val fiveRowData = ArrayList<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem>()
            fiveRowData.add(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(""))

            if (items!!.isNotEmpty()) {
                for (j in items.indices) {
                    val entityList = items[j].list
                    for (k in entityList.indices) {
                        val itemsEntities = entityList[k].items
                        when {
                            entityList[k].name == "主胜" -> itemsEntities.indices
                                    .filter { currData[i].id == itemsEntities[it].matchID }
                                    .forEach {
                                        if (twoRowData.size < 11) {
                                            twoRowData.add(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(itemsEntities[it].id, itemsEntities[it].payRate, 1))
                                        }
                                    }
                            entityList[k].name == "主负" -> itemsEntities.indices
                                    .filter { currData[i].id == itemsEntities[it].matchID }
                                    .forEach {
                                        if (threeRowData.size < 11) {
                                            threeRowData.add(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(itemsEntities[it].id, itemsEntities[it].payRate, 2))
                                        }
                                    }
                            entityList[k].name == "平" -> itemsEntities.indices
                                    .filter { currData[i].id == itemsEntities[it].matchID }
                                    .forEach {
                                        if (fiveRowData.size < 6) {
                                            fiveRowData.add(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(itemsEntities[it].id, itemsEntities[it].payRate, 1))
                                        }
                                    }
                            entityList[k].name == "其他" -> itemsEntities.indices
                                    .filter { currData[i].id == itemsEntities[it].matchID }
                                    .forEach {
                                        if (fiveRowData.size < 7) {
                                            fiveRowData.add(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(itemsEntities[it].id, itemsEntities[it].payRate, 1))
                                        }
                                    }
                        }
                    }
                }

                if (twoRowData.size < 11) {
                    for (j in twoRowData.size..10) {
                        twoRowData.add(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(""))
                    }
                }

                if (threeRowData.size < 11) {
                    for (j in threeRowData.size..10) {
                        threeRowData.add(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(""))
                    }
                }

                if (fiveRowData.size < 7) {
                    for (j in fiveRowData.size..6) {
                        fiveRowData.add(ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem(""))
                    }
                }

                testItems.addAll(twoRowData)
                testItems.addAll(threeRowData)
                testItems.addAll(fiveRowData)

                item.itemList = testItems
                beanItems1.add(item)
            }
        }
        return beanItems1
    }

    override fun didReceivedNotification(id: Int, vararg args: String) {
        if (id == NotificationController.scroll_mask) {
            if (args?.isNotEmpty()) {
                if ("child_close" == args[0]) {
                    if (commitMenuView != null && commitMenuView!!.visibility == View.VISIBLE) {
                        commitMenuView!!.hide(false)
                    }
                }
            }
        } else if (id == NotificationController.footBallFilter) {
            if (args.isNotEmpty()) {
                if (TAG == args[0]) {
                    loadingDialog!!.dismiss()
                    selectMatchDialog!!.show(DataController.getInstance().footBallData, DataController.getInstance().footBallHotData, "联赛选择")
                }
            }
        }
    }

    class MergeBean : Serializable {

        var bean: MutableList<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem>? = null
        var items: ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItems? = null
        var isHome: Boolean = false
        var title: String? = null

        companion object {
            private const val serialVersionUID = -3943234106674174323L
        }
    }

    companion object {
        private val TAG = "ScrollBallBoDanFragment"
    }
}
