package com.dading.ssqs.fragment.guesstheball.early

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast

import com.dading.ssqs.LocaleController
import com.dading.ssqs.NotificationController
import com.dading.ssqs.R
import com.dading.ssqs.SSQSApplication
import com.dading.ssqs.adapter.newAdapter.MyChampionAdapter
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter
import com.dading.ssqs.apis.CcApiResult
import com.dading.ssqs.apis.elements.PayBallElement
import com.dading.ssqs.base.LayoutHelper
import com.dading.ssqs.bean.Champion
import com.dading.ssqs.bean.ChampionBean
import com.dading.ssqs.bean.CommonTitle
import com.dading.ssqs.cells.ChampionChildCell
import com.dading.ssqs.cells.GuessFilterCell
import com.dading.ssqs.components.FilterDialog
import com.dading.ssqs.components.LoadingDialog
import com.dading.ssqs.components.PageDialog
import com.dading.ssqs.components.RecyclerScrollview
import com.dading.ssqs.components.ScrollBallCommitMenuView
import com.dading.ssqs.components.ScrollBallCommitView
import com.dading.ssqs.components.swipetoloadlayout.OnRefreshListener
import com.dading.ssqs.components.swipetoloadlayout.SwipeToLoadLayout
import com.dading.ssqs.fragment.guesstheball.today.ToDayFootBallChampionFragment
import com.dading.ssqs.utils.AndroidUtilities
import com.dading.ssqs.utils.ToastUtils

import java.util.ArrayList


/**
 * Created by mazhuang on 2017/12/8.
 * 早盘-足球-冠军
 */

class EarlyChampionFragment : Fragment(), OnRefreshListener, NotificationController.NotificationControllerDelegate {

    private var swipeToLoadLayout: SwipeToLoadLayout? = null
    private var mRecyclerView: RecyclerView? = null
    private var adapter: MyChampionAdapter? = null
    private var loadingDialog: LoadingDialog? = null
    private var pageDialog: PageDialog? = null
    private var filterDialog: FilterDialog? = null
    private var filterCell: GuessFilterCell? = null
    private var commitView: ScrollBallCommitView? = null
    private var commitMenuView: ScrollBallCommitMenuView? = null
    private var defaultView: ImageView? = null
    private var offset = 1
    private val limit = 10

    private var totalPage: Int = 0

    private var isRefresh = false

    private var leagueMatchId = -1//当前所点击的联赛id  用于判断 不能多个联赛一起选
    private val leagusList = ArrayList<ToDayFootBallChampionFragment.MergeBean>()

    private var currTitle = ""

    private var filter_str = "按时间顺序"

    private var sType = 0

    private val menuListener = ScrollBallCommitMenuAdapter.OnMenuClickListener { position, dataId, itemId, value ->
        commitMenuView!!.changeData(position)

        val thread = Thread(Runnable {
            val iterator = leagusList.iterator()

            while (iterator.hasNext()) {
                val mergeBean = iterator.next()

                val items = mergeBean.bean

                if (mergeBean.items!!.leagueId == dataId) {
                    val itemIterator = items!!.iterator()

                    while (itemIterator.hasNext()) {
                        val beanItem = itemIterator.next()

                        if (beanItem.id == itemId && beanItem.rightStr == value) {
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

    private val clickListener = ChampionChildCell.OnClickListener { bean, items, isAdd, position, title ->
        if (leagueMatchId == -1 || leagueMatchId == bean.leagueId) {
            leagueMatchId = bean.leagueId
            currTitle = title

            if (isAdd) {
                if (leagusList.size == 0) {
                    val mergeBean = ToDayFootBallChampionFragment.MergeBean()
                    mergeBean.items = items
                    mergeBean.title = items.title

                    val beanItems = ArrayList<ChampionBean.ChampionItems.ChampionItem>()
                    bean.position = position
                    beanItems.add(bean)

                    mergeBean.bean = beanItems

                    leagusList.add(mergeBean)
                } else {
                    var isNew = true//是否添加新的数据
                    for (i in leagusList.indices) {
                        val mergeBean = leagusList[i]

                        if (mergeBean.items!!.leagueId == items.leagueId) {//一个比赛下 不同的item
                            isNew = false

                            val beanItems = mergeBean.bean
                            bean.position = position
                            beanItems!!.add(bean)

                            mergeBean.bean = beanItems
                            break
                        }
                    }

                    if (isNew) {
                        val mergeBean = ToDayFootBallChampionFragment.MergeBean()
                        mergeBean.items = items
                        mergeBean.title = items.title

                        val beanItems = ArrayList<ChampionBean.ChampionItems.ChampionItem>()
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

                    if (mergeBean.items!!.leagueId == items.leagueId) {
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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        NotificationController.getInstance().addObserver(this, NotificationController.early_mask)
        return initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        filterCell?.destoryRunnable(false)

        NotificationController.getInstance().removeObserver(this, NotificationController.early_mask)
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

        val titleLayout = LinearLayout(context)
        titleLayout.setBackgroundColor(-0xffbda3)
        contentLayout.addView(titleLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT))

        val tvTitle = TextView(context)
        tvTitle.typeface = Typeface.DEFAULT_BOLD
        tvTitle.textSize = 13f
        tvTitle.setTextColor(Color.WHITE)
        tvTitle.text = "早盘-足球:" + LocaleController.getString(R.string.scroll_title13)
        tvTitle.gravity = Gravity.CENTER
        titleLayout.addView(tvTitle, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, 30, 12f, 0f, 0f, 0f))

        filterCell = GuessFilterCell(context)
        filterCell!!.setSecondRefresh(180)
        filterCell!!.setRefreshListener { swipeToLoadLayout!!.isRefreshing = true }
        filterCell!!.hideSelectMatch()
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

                    swipeToLoadLayout!!.isRefreshing = true
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

                    swipeToLoadLayout!!.isRefreshing = true
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

        adapter = MyChampionAdapter(context)
        adapter!!.setListener(clickListener)
        mRecyclerView!!.adapter = adapter

        commitView = ScrollBallCommitView(context)
        commitView!!.visibility = View.GONE
        commitView!!.setOnSubmitClickListener {
            if (commitMenuView!!.visibility == View.GONE) {
                commitMenuView!!.setTitle(currTitle)
                commitMenuView!!.setChampionData(leagusList)
                commitMenuView!!.show()

                NotificationController.getInstance().postNotification(NotificationController.early_mask, "open")
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
                NotificationController.getInstance().postNotification(NotificationController.early_mask, "close")
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
                bean.type = 3
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
        SSQSApplication.apiClient(0).getChampionList(1, sType, off, lim) { result ->
            loadingDialog!!.dismiss()
            swipeToLoadLayout!!.isRefreshing = false
            swipeToLoadLayout!!.setRefreshEnabled(true)

            if (result.isOk) {
                val page = result.data as CcApiResult.ResultChampionPage

                filterCell!!.setCurrPage(off)

                if (page?.items != null && page.items.size >= 1) {
                    defaultView!!.visibility = View.GONE

                    filterCell!!.beginRunnable(true)

                    totalPage = page.totalCount

                    filterCell!!.setTotalPage(totalPage)

                    adapter!!.setList(getData(page.items))
                } else {
                    adapter!!.clear()

                    filterCell!!.destoryRunnable(true)

                    defaultView!!.visibility = View.VISIBLE
                }

                isRefresh = false
            } else {
                adapter!!.clear()

                filterCell!!.destoryRunnable(true)

                defaultView!!.visibility = View.VISIBLE

                ToastUtils.midToast(context, result.message, 1000)
            }
        }
    }

    //对返回数据的处理  返回的数据用不了 各种格式不同 解析成自己能用的  冗余代码
    private fun getData(data: List<Champion>?): List<ChampionBean> {
        val list = getFilterData(data)

        val items = ArrayList<ChampionBean>()

        if (data != null && data.isNotEmpty()) {

            for (i in list.indices) {
                val bean = ChampionBean()
                bean.commonTitle = list[i]

                val championItems = ArrayList<ChampionBean.ChampionItems>()

                val championItems1 = data[0].listSeason//二级标题
                val teamRates = data[0].listTeamRate//value

                if (championItems1 != null) {

                    for (j in championItems1.indices) {
                        val championItem = championItems1[j]

                        if (list[i].title == championItem.leagueName) {
                            val championData = ChampionBean.ChampionItems()
                            championData.leagueId = championItem.leagueId
                            championData.title = championItem.matchName

                            val testItems = ArrayList<ChampionBean.ChampionItems.ChampionItem>()

                            for (k in teamRates.indices) {
                                val teamRate = teamRates[k]

                                if (list[i].title == teamRate.leagueName && championItem.matchName == teamRate.matchName) {
                                    val item = ChampionBean.ChampionItems.ChampionItem()
                                    if (testItems.size < 16) {
                                        item.id = teamRate.id
                                        item.leagueId = teamRate.leagueId
                                        item.leftStr = teamRate.teamName
                                        item.rightStr = teamRate.payRate
                                        testItems.add(item)
                                    } else {
                                        break
                                    }
                                }
                            }
                            championData.items = testItems
                            championItems.add(championData)
                        }
                    }

                    bean.items = championItems

                    items.add(bean)
                }
            }
        }

        return items
    }

    //获取筛选完的数据
    private fun getFilterData(items: List<Champion>?): List<CommonTitle> {
        val list = ArrayList<CommonTitle>()

        val leaguses = items!![0].listLeague

        if (leaguses != null) {
            for (i in leaguses.indices) {
                if (list.size > 0) {
                    val isAdd = list.indices
                            .map { list[it] }
                            .none { it.title == leaguses[i].leagueName }

                    if (isAdd) {
                        val title = CommonTitle()
                        title.title = leaguses[i].leagueName

                        list.add(title)
                    }
                } else {
                    val title = CommonTitle()
                    title.title = leaguses[i].leagueName

                    list.add(title)
                }
            }
        }
        return list
    }

    override fun onRefresh() {
        if (!isRefresh) {
            isRefresh = true

            offset = 1

            getNetDataWork(offset, limit)
        }
    }

    override fun didReceivedNotification(id: Int, vararg args: String) {
        if (id == NotificationController.early_mask) {
            if (args?.isNotEmpty()) {
                if ("child_close" == args[0]) {
                    if (commitMenuView != null && commitMenuView!!.visibility == View.VISIBLE) {
                        commitMenuView!!.hide(false)
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = "ToDayFootBallChampionFragment"
    }
}
