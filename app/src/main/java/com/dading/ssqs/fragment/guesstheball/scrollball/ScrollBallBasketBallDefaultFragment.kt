package com.dading.ssqs.fragment.guesstheball.scrollball

import android.content.Intent
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
import com.dading.ssqs.activity.BasketBallDetailsActivity
import com.dading.ssqs.adapter.newAdapter.BasketScrollBallItemAdapter
import com.dading.ssqs.adapter.newAdapter.ScrollBallBasketBallDefaultAdapter
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter
import com.dading.ssqs.apis.CcApiResult
import com.dading.ssqs.apis.elements.PayBallElement
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.bean.CommonTitle
import com.dading.ssqs.bean.JCbean
import com.dading.ssqs.bean.ScoreBean
import com.dading.ssqs.bean.ScrollBallBasketBallBean
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
 * Created by mazhuang on 2017/12/8.
 * 滚球-篮球-默认
 */

class ScrollBallBasketBallDefaultFragment : Fragment(), OnRefreshListener, NotificationController.NotificationControllerDelegate {

    private var swipeToLoadLayout: SwipeToLoadLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var adapter: ScrollBallBasketBallDefaultAdapter? = null
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

    private var currBasletBallDefaultBean: ScrollBallBasketBallBean? = null
    private val newData = ArrayList<ScrollBallBasketBallBean>()
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

    private val itemClickListener = object : BasketScrollBallItemAdapter.OnItemClickListener {

        override fun onItemClick(id: Int, bean: ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem, items: ScrollBallBasketBallBean.ScrollBaksetBallItems, isAdd: Boolean, isHome: Boolean, position: Int): Boolean {
            if (leagueMatchId == id || leagueMatchId == -1) {
                leagueMatchId = id

                if (isAdd) {//是添加 还是删除
                    if (leagusList.size == 0) {
                        val mergeBean = MergeBean()
                        mergeBean.items = items
                        mergeBean.isHome = isHome

                        for (i in originalData!!.indices) {
                            if (originalData!![i].id == id) {
                                mergeBean.title = originalData!![i].leagueName
                                break
                            }
                        }

                        val beanItems = ArrayList<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem>()
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
                            mergeBean.isHome = isHome

                            for (i in originalData!!.indices) {
                                if (originalData!![i].id == id) {
                                    mergeBean.title = originalData!![i].leagueName
                                    break
                                }
                            }

                            val beanItems = ArrayList<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem>()
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

                return true
            } else {
                return false
            }
        }

        override fun onInfoClick(matchId: Int, title: String) {
            val intent = Intent(context!!.applicationContext, BasketBallDetailsActivity::class.java)

            intent.putExtra("data_id", matchId)
            intent.putExtra("data_title", title)

            startActivity(intent)
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
                currBasletBallDefaultBean = temporaryBean
                break
            }
        }

        //把子数据添加进去

        if (currBasletBallDefaultBean != null) {
            var params = ""
            for (i in networkData.indices) {
                params += networkData[i].id.toString() + ","
            }

            if (params.isNotEmpty()) {
                params = params.substring(0, params.length - 1)
            }

            SSQSApplication.apiClient(0).getMatchBasketBallResult(params, "0") { result ->
                if (result.isOk) {
                    val items = result.data as List<JCbean>

                    if (items?.isNotEmpty()) {

                        val thread = Thread(Runnable {
                            currBasletBallDefaultBean!!.items = handleItemData(items, networkData)

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

                        ToastUtils.midToast(context, "篮球 暂无数据!!", 0)
                    }
                } else {
                    ToastUtils.midToast(context, result.message, 0)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        NotificationController.getInstance().addObserver(this, NotificationController.basketBallFilter)
        NotificationController.getInstance().addObserver(this, NotificationController.scroll_mask)
        return initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        filterCell?.destoryRunnable(false)

        NotificationController.getInstance().removeObserver(this, NotificationController.scroll_mask)
        NotificationController.getInstance().removeObserver(this, NotificationController.basketBallFilter)
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
            if (DataController.getInstance().baskteBallData == null) {
                DataController.getInstance().syncBasketBall(TAG, 6)
                loadingDialog!!.show()
            } else {
                selectMatchDialog!!.show(DataController.getInstance().baskteBallData, DataController.getInstance().basketBallHotData, "联赛选择")
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

        mRecyclerView = RecyclerView(context)
        scrollview.addView(mRecyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT))

        adapter = ScrollBallBasketBallDefaultAdapter(context)
        adapter!!.setReadyListener(readyListener)
        adapter!!.setItemClickListener(itemClickListener)
        mRecyclerView!!.adapter = adapter

        commitView = ScrollBallCommitView(context)
        commitView!!.visibility = View.GONE
        commitView!!.setOnSubmitClickListener {
            if (commitMenuView!!.visibility == View.GONE) {
                commitMenuView!!.setTitle("滚球-篮球")
                commitMenuView!!.setBaskData(leagusList)
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
        defaultView!!.visibility = View.GONE
        defaultView!!.setImageResource(R.mipmap.no_data)
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
                bean.matchID = leagusList[i].items!!.id
                bean.type = 2
                bean.amount = moneyLists[j].money
                bean.payRateID = list[j].id
                bean.selected = list[j].selected
                items.add(bean)
            }
        }

        element.items = items

        leagusList.clear()
        leagueMatchId = -1

        SSQSApplication.apiClient(0).payBall(element) { result ->
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

        SSQSApplication.apiClient(0).getScrollBallList(false, 6, mDate, sType, leagueIDs, off, lim) { result ->
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


    private fun getData(list: List<CommonTitle>): List<ScrollBallBasketBallBean> {
        val items = ArrayList<ScrollBallBasketBallBean>()

        for (i in list.indices) {
            val bean = ScrollBallBasketBallBean()
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
    private fun handleItemData(items: List<JCbean>?, currData: List<ScoreBean>): List<ScrollBallBasketBallBean.ScrollBaksetBallItems> {
        val beanItems = ArrayList<ScrollBallBasketBallBean.ScrollBaksetBallItems>()

        for (i in currData.indices) {
            val item = ScrollBallBasketBallBean.ScrollBaksetBallItems()

            val currScoreBean = currData[i]

            item.id = currScoreBean.id
            item.title = currScoreBean.home
            item.byTitle = currScoreBean.away

            val time = DateUtils.changeFormater(currScoreBean.openTime, "yyyy-MM-dd HH:mm:ss", "HH:mm")

            item.time = time

            val score = ScrollBallBasketBallBean.ScrollBaksetBallItems.Score()
            score.leftScore = currScoreBean.hScore
            score.rightScore = currScoreBean.aScore
            score.currSchedule = currScoreBean.protime

            item.score = score

            val scrollBeanItems = ArrayList<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem>()

            val oneRowData = ArrayList<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem>()
            val twoRowData = ArrayList<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem>()
            val threeRowData = ArrayList<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem>()
            val fourRowData = ArrayList<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem>()

            threeRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
            fourRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())

            if (items!!.isNotEmpty()) {
                //独赢的数据
                for (j in items.indices) {
                    val jCbean = items[j]

                    if (jCbean.payTypeID == 1 && currScoreBean.id == jCbean.matchID) {
                        oneRowData.add(getBeanItems("", jCbean.realRate1, 1, jCbean.id))

                        twoRowData.add(getBeanItems("", jCbean.realRate3, 2, jCbean.id))

                        break
                    }
                }

                if (oneRowData.size == 0) {
                    oneRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }
                if (twoRowData.size == 0) {
                    twoRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }

                //让球的数据
                for (j in items.indices) {
                    val jCbean = items[j]

                    if (jCbean.payTypeID == 2 && currScoreBean.id == jCbean.matchID) {
                        val topStr = getRate2Str(items[j].realRate2, false)
                        val bottomStr = getRate2Str(items[j].realRate2, true)

                        if (oneRowData.size == 2 && twoRowData.size == 2) {
                            threeRowData.add(getBeanItems(topStr, jCbean.realRate1, 1, jCbean.id))

                            fourRowData.add(getBeanItems(bottomStr, jCbean.realRate3, 2, jCbean.id))

                            break
                        } else if (oneRowData.size == 1 && twoRowData.size == 1) {
                            oneRowData.add(getBeanItems(topStr, jCbean.realRate1, 1, jCbean.id))

                            twoRowData.add(getBeanItems(bottomStr, jCbean.realRate3, 2, jCbean.id))
                        }
                    }
                }
                if (oneRowData.size == 1) {
                    oneRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }
                if (twoRowData.size == 1) {
                    twoRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }
                if (threeRowData.size == 1) {
                    threeRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }
                if (fourRowData.size == 1) {
                    fourRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }

                //大小的数据
                items.indices
                        .map { items[it] }
                        .filter { it.payTypeID == 3 && currScoreBean.id == it.matchID }
                        .forEach {
                            if (oneRowData.size == 3 && twoRowData.size == 3 && threeRowData.size == 2 && fourRowData.size == 2) {
                                threeRowData.add(getBeanItems("大" + it.realRate2, it.realRate1, 1, it.id))

                                fourRowData.add(getBeanItems("小" + it.realRate2, it.realRate3, 2, it.id))
                            } else if (oneRowData.size == 2 && twoRowData.size == 2) {
                                oneRowData.add(getBeanItems("大" + it.realRate2, it.realRate1, 1, it.id))

                                twoRowData.add(getBeanItems("小" + it.realRate2, it.realRate3, 2, it.id))
                            }
                        }
                if (oneRowData.size == 2) {
                    oneRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }
                if (twoRowData.size == 2) {
                    twoRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }
                if (threeRowData.size == 2) {
                    threeRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }
                if (fourRowData.size == 2) {
                    fourRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }

                //球队得分 大/小 的数据
                for (j in items.indices) {
                    val jCbean = items[j]

                    if (jCbean.payTypeID == 48) {//主
                        if (currScoreBean.id == jCbean.matchID && oneRowData.size == 3 && twoRowData.size == 3) {
                            oneRowData.add(getBeanItems("<font color=\"#E91212\">大</font><font color=\"#222222\">" + jCbean.realRate2 + "</font>", jCbean.realRate1, 1, jCbean.id))

                            twoRowData.add(getBeanItems("<font color=\"#8BEF81\">小</font><font color=\"#222222\">" + jCbean.realRate2 + "</font>", jCbean.realRate3, 2, jCbean.id))
                        }
                    }
                    if (jCbean.payTypeID == 49) {//客队
                        if (currScoreBean.id == jCbean.matchID && threeRowData.size == 3 && fourRowData.size == 3) {
                            threeRowData.add(getBeanItems("<font color=\"#E91212\">大</font><font color=\"#222222\">" + jCbean.realRate2 + "</font>", jCbean.realRate1, 1, jCbean.id))

                            fourRowData.add(getBeanItems("<font color=\"#8BEF81\">小</font><font color=\"#222222\">" + jCbean.realRate2 + "</font>", jCbean.realRate3, 2, jCbean.id))
                        }
                    }
                }

                if (oneRowData.size == 3) {
                    oneRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }
                if (twoRowData.size == 3) {
                    twoRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }
                if (threeRowData.size == 3) {
                    threeRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }
                if (fourRowData.size == 3) {
                    fourRowData.add(ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem())
                }

                scrollBeanItems.addAll(oneRowData)
                scrollBeanItems.addAll(twoRowData)
                scrollBeanItems.addAll(threeRowData)
                scrollBeanItems.addAll(fourRowData)

                item.testItems = scrollBeanItems

                beanItems.add(item)
            }
        }
        return beanItems
    }

    private fun getBeanItems(leftStr: String, rightStr: String, selected: Int, id: Int): ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem {
        val item = ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem()
        item.selected = selected
        item.id = id

        if (!TextUtils.isEmpty(leftStr)) {
            item.leftStr = leftStr
        }

        if (!TextUtils.isEmpty(rightStr)) {
            item.rightStr = rightStr
        }
        return item
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
        } else if (id == NotificationController.basketBallFilter) {
            if (args?.isNotEmpty()) {
                if (TAG == args[0]) {
                    loadingDialog!!.dismiss()
                    selectMatchDialog!!.show(DataController.getInstance().baskteBallData, DataController.getInstance().basketBallHotData, "联赛选择")
                }
            }
        }
    }

    class MergeBean : Serializable {

        var bean: MutableList<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem>? = null
        var items: ScrollBallBasketBallBean.ScrollBaksetBallItems? = null
        var isHome: Boolean = false
        var title: String? = null

        companion object {
            private const val serialVersionUID = -3943234106674174323L
        }
    }

    companion object {
        private val TAG = "ScrollBallBasketBallDefaultFragment"
    }
}
