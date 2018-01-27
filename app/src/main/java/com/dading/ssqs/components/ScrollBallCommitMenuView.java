package com.dading.ssqs.components;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.activity.BasketBallDetailsActivity;
import com.dading.ssqs.adapter.newAdapter.ScrollBallCommitMenuAdapter;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.bean.ChampionBean;
import com.dading.ssqs.bean.ScrollBallBasketBallBean;
import com.dading.ssqs.bean.ScrollBallFootBallBean;
import com.dading.ssqs.bean.ScrollBallFootBallBoDanBean;
import com.dading.ssqs.bean.ScrollBallFootBallHalfCourtBean;
import com.dading.ssqs.bean.ScrollBallFootBallTotalBean;
import com.dading.ssqs.fragment.guesstheball.today.ToDayFootBallChampionFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBasketBallDefaultFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallBoDanFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallDefaultFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallHalfCourtFragment;
import com.dading.ssqs.fragment.guesstheball.scrollball.ScrollBallTotalFragment;
import com.dading.ssqs.utils.AndroidUtilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/2.
 * 猜球中提交view
 */

public class ScrollBallCommitMenuView extends RelativeLayout {

    private TextView leftTextView;
    private TextView rightTextView;
    private TextView tvTitle;

    private RecyclerView recyclerView;
    private ScrollBallCommitMenuAdapter adapter;

    private NumberView numberView;

    private OnCommitMenuListener menuListener;

    private Context mContext;

    private int currPosition = -1;

    private RelativeLayout moneyLayout;
    private EditText moneyTextView;
    private TextView cashTextView;

    public void setMenuListener(OnCommitMenuListener menuListener) {
        this.menuListener = menuListener;
    }

    public interface OnCommitMenuListener {
        void onClear();

        void onHide();

        void onDone();
    }

    public void setType(int type) {//2==串关  布局不一样
        if (type == 2) {
            moneyLayout.setVisibility(View.VISIBLE);
            adapter.setType(type);
        }
    }

    public ScrollBallCommitMenuView(Context context, String leftText, String rightText) {
        super(context);
        mContext = context;

        View maskView = new View(context);
        maskView.setBackgroundColor(0xA5000000);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                hide(true);
            }
        });
        addView(maskView, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, 0, 0, 0, 45));

        LinearLayout container = new LinearLayout(context);
        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //防止进入遮罩层点击事件
            }
        });
        container.setOrientation(LinearLayout.VERTICAL);
        addView(container, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, 0, 0, 0, 45, ALIGN_PARENT_BOTTOM));

        RelativeLayout topLayout = new RelativeLayout(context);
        container.addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 40));

        LinearLayout topContainerLayout = new LinearLayout(context);
        topContainerLayout.setOrientation(LinearLayout.HORIZONTAL);
        topLayout.addView(topContainerLayout, LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));

        leftTextView = new TextView(context);
        leftTextView.setTextSize(14);
        leftTextView.setTextColor(0xFF009BDB);
        leftTextView.setBackgroundResource(R.drawable.bg_guess_ball_commit_white);
        leftTextView.setGravity(Gravity.CENTER);
        leftTextView.setText(leftText);
        topContainerLayout.addView(leftTextView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

        rightTextView = new TextView(context);
        rightTextView.setTextSize(14);
        rightTextView.setTextColor(Color.WHITE);
        rightTextView.setBackgroundResource(R.drawable.bg_guess_ball_commit_blue);
        rightTextView.setGravity(Gravity.CENTER);
        rightTextView.setText(rightText);
        topContainerLayout.addView(rightTextView, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f, 0, 5, 0, 0));

        View view = new View(context);
        view.setBackgroundColor(0xFF009BDB);
        RelativeLayout.LayoutParams viewLP = new RelativeLayout.LayoutParams(LayoutHelper.MATCH_PARENT, 1);
        viewLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        topLayout.addView(view, viewLP);

        LinearLayout infoLayout = new LinearLayout(context);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(infoLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        tvTitle = new TextView(context);
        tvTitle.setTextColor(0xFF222222);
        tvTitle.setPadding(AndroidUtilities.INSTANCE.dp(12), 0, 0, 0);
        tvTitle.setBackgroundColor(Color.WHITE);
        tvTitle.setTextSize(12);
        tvTitle.setGravity(Gravity.CENTER_VERTICAL);
        infoLayout.addView(tvTitle, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 35));

        recyclerView = new RecyclerView(context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        infoLayout.addView(recyclerView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        adapter = new ScrollBallCommitMenuAdapter(context);
        adapter.setNumberListener(new ScrollBallCommitMenuAdapter.OnNumberListener() {
            @Override
            public void onReady(int position) {
                currPosition = position;
            }
        });
        recyclerView.setAdapter(adapter);

        LinearLayout keyBoardLayout = new LinearLayout(context);
        keyBoardLayout.setOrientation(LinearLayout.VERTICAL);
        container.addView(keyBoardLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        moneyLayout = new RelativeLayout(context);
        moneyLayout.setVisibility(View.GONE);
        moneyLayout.setBackgroundColor(0xFFE9F9FF);
        moneyLayout.setPadding(AndroidUtilities.INSTANCE.dp(12), 0, AndroidUtilities.INSTANCE.dp(12), 0);
        keyBoardLayout.addView(moneyLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 35));

        View moneyView = new View(context);
        moneyView.setId(R.id.scroll_commit_menu_money_view);
        moneyView.setBackgroundColor(0xFF009BDB);
        moneyLayout.addView(moneyView, LayoutHelper.createRelative(1, 15, RelativeLayout.CENTER_IN_PARENT));

        LinearLayout moneyLeftLayout = new LinearLayout(context);
        moneyLeftLayout.setGravity(Gravity.CENTER_VERTICAL);
        moneyLeftLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams moneyLeftLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 30, 0, 0, 15, 0);
        moneyLeftLP.addRule(RelativeLayout.LEFT_OF, moneyView.getId());
        moneyLeftLP.addRule(RelativeLayout.CENTER_VERTICAL);
        moneyLayout.addView(moneyLeftLayout, moneyLeftLP);

        TextView tvMoneyTipView = new TextView(context);
        tvMoneyTipView.setTextColor(0xFF222222);
        tvMoneyTipView.setTextSize(12);
        tvMoneyTipView.setText("下注金额:");
        moneyLeftLayout.addView(tvMoneyTipView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        moneyTextView = new EditText(context);
        AndroidUtilities.INSTANCE.disableShowInput(moneyTextView);
        moneyTextView.setPadding(AndroidUtilities.INSTANCE.dp(2), 0, AndroidUtilities.INSTANCE.dp(2), 0);
        moneyTextView.setTextSize(12);
        moneyTextView.setSingleLine();
        moneyTextView.setTextColor(0xFF222222);
        moneyTextView.setGravity(Gravity.CENTER_VERTICAL);
        moneyTextView.setBackgroundResource(R.drawable.bg_guess_ball_menu_money);
        moneyTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                moneyTextView.setSelection(moneyTextView.getText().length());
            }
        });
        moneyLeftLayout.addView(moneyTextView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 25, 5, 0, 0, 0));

        LinearLayout moneyRightLayout = new LinearLayout(context);
        moneyRightLayout.setGravity(Gravity.CENTER_VERTICAL);
        moneyRightLayout.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams moneyRightLP = LayoutHelper.createRelative(LayoutHelper.MATCH_PARENT, 30, 15, 0, 0, 0);
        moneyRightLP.addRule(RelativeLayout.RIGHT_OF, moneyView.getId());
        moneyRightLP.addRule(RelativeLayout.CENTER_VERTICAL);
        moneyLayout.addView(moneyRightLayout, moneyRightLP);

        TextView estimateTextView = new TextView(context);
        estimateTextView.setText("预计奖金:");
        estimateTextView.setTextColor(0xFF222222);
        estimateTextView.setTextSize(12);
        moneyRightLayout.addView(estimateTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        cashTextView = new TextView(context);
        cashTextView.setTextSize(12);
        cashTextView.setText("0元");
        cashTextView.setTextColor(0xFFFF0000);
        cashTextView.setSingleLine();
        moneyRightLayout.addView(cashTextView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, 3, 0, 0, 0));

        View rightView = new View(context);
        rightView.setBackgroundColor(0xFF009BDB);
        keyBoardLayout.addView(rightView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));

        numberView = new NumberView(context);
        numberView.setListener(new OnNumberListener() {
            @Override
            public void onNumber(int num) {
                if (moneyLayout.getVisibility() == View.VISIBLE) {
                    if (moneyTextView.getText().toString().length() < 9) {
                        String str = moneyTextView.getText().toString() + num;
                        moneyTextView.setText(str);

                        int total = 0;

                        List<MergeBean> list = adapter.getData();
                        for (int i = 0; i < list.size(); i++) {
                            try {
                                Double result = Integer.valueOf(str) * Double.valueOf(list.get(i).getBeanStr());
                                total += result;
                            } catch (Exception ex) {
                                Log.e("commitMenuView", "转换失败");
                                total = 0;
                            }
                        }

                        cashTextView.setText(total + "元");
                    }
                } else {
                    if (currPosition >= 0) {
                        List<MergeBean> list = new ArrayList<>();
                        list.addAll(adapter.getData());

                        for (int i = 0; i < list.size(); i++) {
                            if (i == currPosition) {
                                MergeBean bean = list.get(i);
                                if (TextUtils.isEmpty(bean.getMoney()) || bean.getMoney().length() < 9) {
                                    if (!TextUtils.isEmpty(bean.getMoney())) {
                                        bean.setMoney(bean.getMoney() + num);
                                    } else {
                                        bean.setMoney(num + "");
                                    }
                                }
                                break;
                            }
                        }
                        adapter.setCurrPosition(currPosition);
                        adapter.setData(list);
                    }

                }
            }

            @Override
            public void onClear() {
                if (moneyLayout.getVisibility() == View.VISIBLE) {
                    String str = moneyTextView.getText().toString().trim();
                    if (str.length() >= 1) {
                        moneyTextView.setText("");
                    }
                } else {
                    if (currPosition >= 0) {
                        List<MergeBean> list = new ArrayList<>();
                        list.addAll(adapter.getData());

                        for (int i = 0; i < list.size(); i++) {
                            if (i == currPosition) {
                                MergeBean bean = list.get(i);
                                if (!TextUtils.isEmpty(bean.getMoney()) && bean.getMoney().length() >= 1) {
                                    bean.setMoney("");
                                }
                                break;
                            }
                        }
                        adapter.setCurrPosition(currPosition);
                        adapter.setData(list);
                    }
                }
            }

            @Override
            public void onDone() {
                if (menuListener != null) {
                    menuListener.onDone();
                }
            }

            @Override
            public void onBackSpace() {
                if (moneyLayout.getVisibility() == View.VISIBLE) {
                    String str = moneyTextView.getText().toString().trim();
                    if (str.length() >= 1) {
                        moneyTextView.setText(str.substring(0, str.length() - 1));
                    }
                } else {
                    if (currPosition >= 0) {
                        List<MergeBean> list = new ArrayList<>();
                        list.addAll(adapter.getData());

                        for (int i = 0; i < list.size(); i++) {
                            if (i == currPosition) {
                                MergeBean bean = list.get(i);
                                if (!TextUtils.isEmpty(bean.getMoney()) && bean.getMoney().length() >= 1) {
                                    bean.setMoney(bean.getMoney().substring(0, bean.getMoney().length() - 1));
                                }
                                break;
                            }
                        }
                        adapter.setCurrPosition(currPosition);
                        adapter.setData(list);
                    }
                }
            }
        });
        keyBoardLayout.addView(numberView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));
    }

    public void setMenuItemDeleteListener(ScrollBallCommitMenuAdapter.OnMenuClickListener listener) {
        adapter.setListener(listener);
    }

    boolean isFirst;

    public void changeData(int position) {
        int count = adapter.getItemCount();

        adapter.refreshData(position);

        if (count == 1) {
            ScrollBallCommitMenuView.this.setVisibility(View.GONE);

            if (menuListener != null) {
                menuListener.onClear();
                menuListener.onHide();
            }
        } else if (count == 3) {
            if (!isFirst) {
                isFirst = true;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
                layoutParams.height = LayoutHelper.WRAP_CONTENT;
                recyclerView.setLayoutParams(layoutParams);
            }
        }
    }

    public void setData(List<ScrollBallDefaultFragment.MergeBean> list) {
        isFirst = false;

        List<MergeBean> data = getData(list);
        getMergeBean(data);
    }

    public void setBaskData(List<ScrollBallBasketBallDefaultFragment.MergeBean> list) {
        isFirst = false;

        List<MergeBean> data = getData(list);
        getMergeBean(data);
    }

    public void setBoDanData(List<ScrollBallBoDanFragment.MergeBean> list) {
        isFirst = false;

        List<MergeBean> data = getData(list);
        getMergeBean(data);
    }

    public void setTotalData(List<ScrollBallTotalFragment.MergeBean> list) {
        isFirst = false;

        List<MergeBean> data = getData(list);
        getMergeBean(data);
    }

    public void setHalfCourtData(List<ScrollBallHalfCourtFragment.MergeBean> list) {
        isFirst = false;

        List<MergeBean> data = getData(list);
        getMergeBean(data);
    }

    public void setChampionData(List<ToDayFootBallChampionFragment.MergeBean> list) {
        isFirst = false;

        List<MergeBean> data = getData(list);
        getMergeBean(data);
    }

    public void setBasketDetailsData(List<BasketBallDetailsActivity.BasketData.BasketItemData> list) {
        isFirst = false;

        List<MergeBean> data = getData(list);
        getMergeBean(data);
    }

    private void getMergeBean(List<MergeBean> data) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();

        if (data.size() > 2) {
            layoutParams.height = AndroidUtilities.INSTANCE.dp(156);//78*2
        } else {
            layoutParams.height = LayoutHelper.WRAP_CONTENT;
        }
        recyclerView.setLayoutParams(layoutParams);

        adapter.setData(data);
    }

    private List<MergeBean> getData(List<?> list) {
        List<MergeBean> items = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ScrollBallDefaultFragment.MergeBean) {
                ScrollBallDefaultFragment.MergeBean bean = (ScrollBallDefaultFragment.MergeBean) list.get(i);

                List<ScrollBallFootBallBean.ScrollBeanItems.ScrollBeanItem> beanItems = bean.getBean();

                for (int j = 0; j < beanItems.size(); j++) {
                    MergeBean mergeBean = new MergeBean();
                    mergeBean.setBeanId(beanItems.get(j).getId());
                    mergeBean.setBeanStr(beanItems.get(j).getRightStr());
                    mergeBean.setItemsId(bean.getItems().getId());
                    mergeBean.setItemsTitle(bean.getItems().getTitle());
                    mergeBean.setHome(beanItems.get(j).getSelected() != 2);
                    mergeBean.setItemsByTitle(bean.getItems().getByTitle());
                    if (beanItems.get(j).getSelected() != 2) {
                        mergeBean.setOddsName(bean.getItems().getTitle());
                    } else {
                        mergeBean.setOddsName(bean.getItems().getByTitle());
                    }
                    mergeBean.setTitle(bean.getTitle());

                    mergeBean.setMoney("");

                    items.add(mergeBean);
                }
            } else if (list.get(i) instanceof ScrollBallBasketBallDefaultFragment.MergeBean) {
                ScrollBallBasketBallDefaultFragment.MergeBean bean = (ScrollBallBasketBallDefaultFragment.MergeBean) list.get(i);

                List<ScrollBallBasketBallBean.ScrollBaksetBallItems.ScrollBeanItem> beanItems = bean.getBean();

                for (int j = 0; j < beanItems.size(); j++) {
                    MergeBean mergeBean = new MergeBean();
                    mergeBean.setBeanId(beanItems.get(j).getId());
                    mergeBean.setBeanStr(beanItems.get(j).getRightStr());
                    mergeBean.setItemsId(bean.getItems().getId());
                    mergeBean.setItemsTitle(bean.getItems().getTitle());
                    mergeBean.setItemsByTitle(bean.getItems().getByTitle());
                    mergeBean.setHome(beanItems.get(j).getSelected() != 2);
                    mergeBean.setTitle(bean.getTitle());
                    if (beanItems.get(j).getSelected() != 2) {
                        mergeBean.setOddsName(mergeBean.getItemsTitle());
                    } else {
                        mergeBean.setOddsName(mergeBean.getItemsByTitle());
                    }
                    mergeBean.setMoney("");

                    items.add(mergeBean);
                }
            } else if (list.get(i) instanceof ScrollBallBoDanFragment.MergeBean) {
                ScrollBallBoDanFragment.MergeBean bean = (ScrollBallBoDanFragment.MergeBean) list.get(i);

                List<ScrollBallFootBallBoDanBean.ScrollBallFootBallBoDanItem> beanItems = bean.getBean();

                for (int j = 0; j < beanItems.size(); j++) {
                    MergeBean mergeBean = new MergeBean();
                    mergeBean.setBeanId(beanItems.get(j).getId());
                    mergeBean.setBeanStr(beanItems.get(j).getStr());
                    mergeBean.setItemsId(bean.getItems().getId());
                    mergeBean.setItemsTitle(bean.getItems().getTitle());
                    mergeBean.setItemsByTitle(bean.getItems().getByTitle());
                    mergeBean.setHome(beanItems.get(j).getSelected() != 2);
                    mergeBean.setTitle(bean.getTitle());
                    if (beanItems.get(j).getSelected() != 2) {
                        mergeBean.setOddsName(mergeBean.getItemsTitle());
                    } else {
                        mergeBean.setOddsName(mergeBean.getItemsByTitle());
                    }
                    mergeBean.setMoney("");

                    items.add(mergeBean);
                }
            } else if (list.get(i) instanceof ScrollBallTotalFragment.MergeBean) {
                ScrollBallTotalFragment.MergeBean bean = (ScrollBallTotalFragment.MergeBean) list.get(i);

                List<ScrollBallFootBallTotalBean.ScrollBallFootBallTotalItem> beanItems = bean.getBean();

                for (int j = 0; j < beanItems.size(); j++) {
                    MergeBean mergeBean = new MergeBean();
                    mergeBean.setBeanId(beanItems.get(j).getId());
                    mergeBean.setBeanStr(beanItems.get(j).getStr());
                    mergeBean.setItemsId(bean.getItems().getId());
                    mergeBean.setItemsTitle(bean.getItems().getTitle());
                    mergeBean.setItemsByTitle(bean.getItems().getByTitle());
                    mergeBean.setHome(true);
                    mergeBean.setTitle(bean.getTitle());
                    mergeBean.setOddsName(mergeBean.getItemsTitle());
                    mergeBean.setMoney("");

                    items.add(mergeBean);
                }
            } else if (list.get(i) instanceof ScrollBallHalfCourtFragment.MergeBean) {
                ScrollBallHalfCourtFragment.MergeBean bean = (ScrollBallHalfCourtFragment.MergeBean) list.get(i);

                List<ScrollBallFootBallHalfCourtBean.ScrollBallFootBallHalfCourtItem> beanItems = bean.getBean();

                for (int j = 0; j < beanItems.size(); j++) {
                    MergeBean mergeBean = new MergeBean();
                    mergeBean.setBeanId(beanItems.get(j).getId());
                    mergeBean.setBeanStr(beanItems.get(j).getStr());
                    mergeBean.setItemsId(bean.getItems().getId());
                    mergeBean.setItemsTitle(bean.getItems().getTitle());
                    mergeBean.setItemsByTitle(bean.getItems().getByTitle());
                    mergeBean.setHome(true);
                    mergeBean.setTitle(bean.getTitle());
                    mergeBean.setOddsName(mergeBean.getItemsTitle());
                    mergeBean.setMoney("");

                    items.add(mergeBean);
                }
            } else if (list.get(i) instanceof ToDayFootBallChampionFragment.MergeBean) {
                ToDayFootBallChampionFragment.MergeBean bean = (ToDayFootBallChampionFragment.MergeBean) list.get(i);

                List<ChampionBean.ChampionItems.ChampionItem> beanItems = bean.getBean();

                for (int j = 0; j < beanItems.size(); j++) {
                    MergeBean mergeBean = new MergeBean();
                    mergeBean.setBeanId(bean.getItems().getLeagueId());
                    mergeBean.setBeanStr(beanItems.get(j).getRightStr());
                    mergeBean.setItemsId(beanItems.get(j).getId());
                    mergeBean.setItemsTitle(bean.getItems().getTitle());
                    mergeBean.setHome(bean.isHome());
                    mergeBean.setTitle(bean.getTitle());
                    mergeBean.setOddsName(beanItems.get(j).getLeftStr());
                    mergeBean.setMoney("");

                    items.add(mergeBean);
                }
            } else if (list.get(i) instanceof BasketBallDetailsActivity.BasketData.BasketItemData) {
                BasketBallDetailsActivity.BasketData.BasketItemData bean = (BasketBallDetailsActivity.BasketData.BasketItemData) list.get(i);

                MergeBean mergeBean = new MergeBean();
                mergeBean.setItemsId(bean.getId());
                mergeBean.setItemsTitle(bean.getHomeName());
                mergeBean.setItemsByTitle(bean.getAwayName());
                mergeBean.setHome(true);
                mergeBean.setTitle(bean.getTitle());
                mergeBean.setOddsName(bean.getTitle());
                mergeBean.setBeanStr(bean.getNumber());
                mergeBean.setMoney("");

                items.add(mergeBean);
            }
        }
        return items;
    }

    public static class MergeBean implements Serializable {

        private static final long serialVersionUID = -1454297989073317162L;

        private int itemsId;
        private int beanId;
        private String beanStr;
        private String itemsTitle;
        private String itemsByTitle;

        private boolean isHome;
        private String title;
        private int position;
        private String money;
        private String oddsName;

        public String getOddsName() {
            return oddsName;
        }

        public void setOddsName(String oddsName) {
            this.oddsName = oddsName;
        }

        public int getItemsId() {
            return itemsId;
        }

        public void setItemsId(int itemsId) {
            this.itemsId = itemsId;
        }

        public int getBeanId() {
            return beanId;
        }

        public void setBeanId(int beanId) {
            this.beanId = beanId;
        }

        public String getBeanStr() {
            return beanStr;
        }

        public void setBeanStr(String beanStr) {
            this.beanStr = beanStr;
        }

        public String getItemsTitle() {
            return itemsTitle;
        }

        public void setItemsTitle(String itemsTitle) {
            this.itemsTitle = itemsTitle;
        }

        public String getItemsByTitle() {
            return itemsByTitle;
        }

        public void setItemsByTitle(String itemsByTitle) {
            this.itemsByTitle = itemsByTitle;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isHome() {
            return isHome;
        }

        public void setHome(boolean home) {
            isHome = home;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }

    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    public void show() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1.0f);
        AnimatorSet set = new AnimatorSet();
        set.play(animator);
        set.setDuration(200);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                currPosition = -1;

                ScrollBallCommitMenuView.this.setVisibility(View.VISIBLE);
            }
        });
        set.start();
    }

    public void hide(final boolean isNotice) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
        AnimatorSet set = new AnimatorSet();
        set.play(animator);
        set.setDuration(200);
        set.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                ScrollBallCommitMenuView.this.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                if (isNotice && menuListener != null) {
                    menuListener.onHide();
                }
            }
        });
        set.start();
    }

    public interface OnNumberListener {
        void onNumber(int num);

        void onClear();

        void onDone();

        void onBackSpace();
    }

    public class NumberView extends LinearLayout {

        private OnNumberListener listener;

        public void setListener(OnNumberListener listener) {
            this.listener = listener;
        }

        public NumberView(Context context) {
            super(context);

            setOrientation(LinearLayout.VERTICAL);
            setBackgroundColor(Color.WHITE);
            setPadding(AndroidUtilities.INSTANCE.dp(12), AndroidUtilities.INSTANCE.dp(8), AndroidUtilities.INSTANCE.dp(12), AndroidUtilities.INSTANCE.dp(8));

            LinearLayout numberInfoLayout = new LinearLayout(context);
            numberInfoLayout.setOrientation(LinearLayout.HORIZONTAL);
            addView(numberInfoLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

            for (int i = 0; i < 10; i++) {
                final TextView numberText = new TextView(context);
                numberText.setBackgroundResource(R.drawable.bg_submit_number);
                numberText.setGravity(Gravity.CENTER);
                numberText.setTextSize(12);
                numberText.setTextColor(Color.WHITE);
                if (i == 9) {
                    numberText.setText("0");
                } else {
                    numberText.setText("" + (i + 1));
                }
                numberText.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onNumber(Integer.valueOf(numberText.getText().toString().trim()));
                        }
                    }
                });

                int right;
                if (i == 9) {
                    right = 0;
                } else {
                    right = 6;
                }

                numberInfoLayout.addView(numberText, LayoutHelper.createLinear(0, 28, 1f, 0, 0, right, 0));
            }

            LinearLayout operationLayout = new LinearLayout(context);
            operationLayout.setPadding(0, AndroidUtilities.INSTANCE.dp(6), 0, 0);
            operationLayout.setOrientation(LinearLayout.HORIZONTAL);
            addView(operationLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

            TextView clearView = new TextView(context);
            clearView.setGravity(Gravity.CENTER);
            clearView.setTextSize(12);
            clearView.setTextColor(Color.WHITE);
            clearView.setText("清除");
            clearView.setBackgroundResource(R.drawable.bg_submit_number);
            clearView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onClear();
                    }
                }
            });
            operationLayout.addView(clearView, LayoutHelper.createLinear(0, 28, 3f, 0, 0, 6, 0));

            LinearLayout backSpaceLayout = new LinearLayout(context);
            backSpaceLayout.setGravity(Gravity.CENTER);
            backSpaceLayout.setBackgroundResource(R.drawable.bg_submit_number);
            backSpaceLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onBackSpace();
                    }
                }
            });
            operationLayout.addView(backSpaceLayout, LayoutHelper.createLinear(0, 28, 2f, 0, 0, 6, 0));

            ImageView backSpaceView = new ImageView(context);
            backSpaceView.setImageResource(R.mipmap.ic_guessball_backspace);
            backSpaceLayout.addView(backSpaceView, LayoutHelper.createLinear(40, 20));

            TextView doneView = new TextView(context);
            doneView.setGravity(Gravity.CENTER);
            doneView.setTextSize(12);
            doneView.setTextColor(Color.WHITE);
            doneView.setText("完成");
            doneView.setBackgroundResource(R.drawable.bg_submit_number);
            doneView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onDone();
                    }
                }
            });
            operationLayout.addView(doneView, LayoutHelper.createLinear(0, 28, 6f));
        }
    }

    public List<MergeBean> getMoney() {
        return adapter.getData();
    }

    public String getStandResult() {
        return moneyTextView.getText().toString().trim();
    }
}