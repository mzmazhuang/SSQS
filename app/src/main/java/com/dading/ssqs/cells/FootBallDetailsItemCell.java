package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.activity.ScrollFootBallDetailsActivity;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2018/1/29.
 */

public class FootBallDetailsItemCell extends LinearLayout {

    private Context mContext;
    private TextView tvTitle;
    private ImageView collectionView;

    private LinearLayout layout;

    private LinearLayout oneAndTwoLayout;
    private LinearLayout oneRowTwoColumLayout;
    private LinearLayout oneRowMantColumLayout;
    private LinearLayout moreRowTwoColumLayout;

    private List<FootDetailsChildCell> oneRowTwoColumCells = new ArrayList<>();//一行两列
    private List<FootDetailsChildCell> oneRowManyColumCells = new ArrayList<>();//一行多列的cells


    public FootBallDetailsItemCell(Context context) {
        super(context);

        mContext = context;

        setOrientation(LinearLayout.VERTICAL);

        RelativeLayout topLayout = new RelativeLayout(context);
        topLayout.setBackgroundColor(0xFF673221);
        topLayout.setPadding(AndroidUtilities.INSTANCE.dp(12), 0, AndroidUtilities.INSTANCE.dp(11), 0);
        addView(topLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 25));

        tvTitle = new TextView(context);
        tvTitle.setTextSize(11);
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        topLayout.addView(tvTitle, LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, RelativeLayout.CENTER_VERTICAL));

        collectionView = new ImageView(context);
        collectionView.setScaleType(ImageView.ScaleType.CENTER);
        RelativeLayout.LayoutParams collLP = LayoutHelper.createRelative(25, 25);
        collLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        collLP.addRule(RelativeLayout.CENTER_VERTICAL);
        topLayout.addView(collectionView, collLP);

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        addView(layout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        oneAndTwoLayout = new LinearLayout(context);
        oneAndTwoLayout.setOrientation(LinearLayout.VERTICAL);
        oneAndTwoLayout.setVisibility(View.GONE);
        layout.addView(oneAndTwoLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        oneRowTwoColumLayout = new LinearLayout(context);
        oneRowTwoColumLayout.setOrientation(LinearLayout.HORIZONTAL);
        oneRowTwoColumLayout.setVisibility(View.GONE);
        layout.addView(oneRowTwoColumLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 28));

        //固定的一行两列
        for (int j = 0; j < 2; j++) {
            FootDetailsChildCell childCell = new FootDetailsChildCell(mContext);
            oneRowTwoColumLayout.addView(childCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));
            oneRowTwoColumCells.add(childCell);

            if (j == 0) {
                childCell.isShowLine(true);
                childCell.setLeftPadding(12);
                childCell.setRightPadding(8);
            } else {
                childCell.isShowLine(false);
                childCell.setLeftPadding(8);
                childCell.setRightPadding(12);
            }
        }

        //固定的一行多列  总进球个数
        oneRowMantColumLayout = new LinearLayout(context);
        oneRowMantColumLayout.setOrientation(LinearLayout.HORIZONTAL);
        oneRowMantColumLayout.setVisibility(View.GONE);
        layout.addView(oneRowMantColumLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 28));

        for (int i = 0; i < 4; i++) {
            FootDetailsChildCell childCell = new FootDetailsChildCell(mContext);
            oneRowMantColumLayout.addView(childCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));
            oneRowManyColumCells.add(childCell);

            if (i == 0) {
                childCell.isShowLine(true);
                childCell.setLeftPadding(11);
                childCell.setRightPadding(7);
            } else if (i == 3) {
                childCell.setLeftPadding(7);
                childCell.setRightPadding(11);
                childCell.isShowLine(false);
            } else {
                childCell.isShowLine(true);
                childCell.setLeftPadding(8);
                childCell.setRightPadding(8);
            }
        }

        moreRowTwoColumLayout = new LinearLayout(context);
        moreRowTwoColumLayout.setOrientation(LinearLayout.VERTICAL);
        moreRowTwoColumLayout.setVisibility(View.GONE);
        layout.addView(moreRowTwoColumLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

        View view = new View(context);
        view.setBackgroundColor(0xFFF5F4F9);
        addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8));
    }

    public void setCollectionClick(final OnClickListener listener) {
        collectionView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }

    public void setData(ScrollFootBallDetailsActivity.FootData data, final OnItemClickListener itemClickListener, List<ScrollFootBallDetailsActivity.FootData.FootItemData> focusList) {
        if (data.getTitle().contains("font")) {
            tvTitle.setText(Html.fromHtml(data.getTitle()));
        } else {
            tvTitle.setTextColor(Color.WHITE);
            tvTitle.setText(data.getTitle());
        }

        if (data.isLike()) {
            collectionView.setImageResource(R.mipmap.ic_basket_collection);
        } else {
            collectionView.setImageResource(R.mipmap.ic_basket_collection_un);
        }

        List<ScrollFootBallDetailsActivity.FootData.FootItemData> list = data.getItems();
        if (list != null) {
            if ("总进球个数".equals(list.get(0).getTagName())) {//总进球的样式
                oneAndTwoLayout.setVisibility(View.GONE);
                oneRowTwoColumLayout.setVisibility(View.GONE);
                oneRowMantColumLayout.setVisibility(View.VISIBLE);
                moreRowTwoColumLayout.setVisibility(View.GONE);

                for (int i = 0; i < list.size(); i++) {
                    oneRowManyColumCells.get(i).setData(list.get(i), data, focusList, 0, false);
                    oneRowManyColumCells.get(i).changeParams();
                    oneRowManyColumCells.get(i).setListener(itemClickListener);
                }
            } else if ("独赢".equals(list.get(0).getTagName())) {//一行三列
                oneAndTwoLayout.setVisibility(View.VISIBLE);
                oneRowTwoColumLayout.setVisibility(View.GONE);
                oneRowMantColumLayout.setVisibility(View.GONE);
                moreRowTwoColumLayout.setVisibility(View.GONE);

                oneAndTwoLayout.removeAllViews();

                LinearLayout linearLayout = new LinearLayout(mContext);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                oneAndTwoLayout.addView(linearLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 28));

                View view = new View(mContext);
                view.setBackgroundColor(0xFFE7E7E7);
                oneAndTwoLayout.addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));

                for (int j = 0; j < 3; j++) {
                    FootDetailsChildCell childCell = new FootDetailsChildCell(mContext);
                    childCell.setData(list.get(j), data, focusList, j, false);
                    childCell.setListener(itemClickListener);

                    linearLayout.addView(childCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

                    if (j == 0) {
                        childCell.isShowLine(true);
                        childCell.setLeftPadding(12);
                        childCell.setRightPadding(8);
                    } else if (j == 2) {
                        childCell.isShowLine(false);
                        childCell.setLeftPadding(8);
                        childCell.setRightPadding(12);
                    } else {
                        childCell.isShowLine(true);
                        childCell.setLeftPadding(8);
                        childCell.setRightPadding(8);
                    }
                }
            } else if (list.size() == 2) {//一行 两列的
                oneAndTwoLayout.setVisibility(View.GONE);
                oneRowMantColumLayout.setVisibility(View.GONE);
                oneRowTwoColumLayout.setVisibility(View.VISIBLE);
                moreRowTwoColumLayout.setVisibility(View.GONE);

                for (int i = 0; i < list.size(); i++) {
                    oneRowTwoColumCells.get(i).setData(list.get(i), data, focusList, i, false);
                    oneRowTwoColumCells.get(i).setListener(itemClickListener);
                }
            } else if ("半场/全场".equals(list.get(0).getTagName())) {//半全场的样式
                oneAndTwoLayout.setVisibility(View.GONE);
                oneRowMantColumLayout.setVisibility(View.GONE);
                oneRowTwoColumLayout.setVisibility(View.GONE);
                moreRowTwoColumLayout.setVisibility(View.VISIBLE);

                moreRowTwoColumLayout.removeAllViews();

                int position = 0;

                int rows;

                if (list.size() % 4 == 0) {
                    rows = list.size() / 4;
                } else {
                    rows = list.size() / 4 + 1;
                }

                for (int i = 0; i < rows; i++) {
                    LinearLayout linearLayout = new LinearLayout(mContext);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    moreRowTwoColumLayout.addView(linearLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 28));

                    View view = new View(mContext);
                    view.setBackgroundColor(0xFFE7E7E7);
                    moreRowTwoColumLayout.addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));

                    if (i != rows - 1) {
                        View lineView = new View(mContext);
                        lineView.setBackgroundColor(0xFFF5F4F9);
                        moreRowTwoColumLayout.addView(lineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8));
                    }

                    for (int j = 0; j < 4; j++) {
                        FootDetailsChildCell childCell = new FootDetailsChildCell(mContext);
                        linearLayout.addView(childCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

                        if (j == 0) {
                            childCell.isShowLine(true);
                            childCell.setLeftPadding(12);
                            childCell.setRightPadding(4);
                        } else if (j == 3) {
                            childCell.isShowLine(false);
                            childCell.setLeftPadding(4);
                            childCell.setRightPadding(12);
                        } else {
                            childCell.isShowLine(true);
                            childCell.setLeftPadding(4);
                            childCell.setRightPadding(4);
                        }

                        if (position >= 9) {//半全场 固定size==9
                            //什么都不做 占位
                        } else {
                            childCell.setData(list.get(position), data, focusList, position, false);
                            childCell.setListener(itemClickListener);
                        }

                        position++;
                    }
                }
            } else if ("波胆".equals(list.get(0).getTagName())) {//波胆的样式
                oneAndTwoLayout.setVisibility(View.GONE);
                oneRowMantColumLayout.setVisibility(View.GONE);
                oneRowTwoColumLayout.setVisibility(View.GONE);
                moreRowTwoColumLayout.setVisibility(View.VISIBLE);

                moreRowTwoColumLayout.removeAllViews();

                int position = 0;

                int rows;

                if (list.size() % 4 == 0) {
                    rows = list.size() / 4;
                } else {
                    rows = list.size() / 4 + 1;
                }

                for (int i = 0; i < rows; i++) {
                    LinearLayout linearLayout = new LinearLayout(mContext);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    if (i % 2 == 1) {
                        linearLayout.setBackgroundColor(0xFFFFFBE2);
                    } else {
                        linearLayout.setBackgroundColor(0xFFFFFFFF);
                    }
                    moreRowTwoColumLayout.addView(linearLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 28));

                    View view = new View(mContext);
                    view.setBackgroundColor(0xFFE7E7E7);
                    moreRowTwoColumLayout.addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));

                    if (i != rows - 1) {
                        View lineView = new View(mContext);
                        lineView.setBackgroundColor(0xFFF5F4F9);
                        moreRowTwoColumLayout.addView(lineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 8));
                    }

                    for (int j = 0; j < 4; j++) {
                        FootDetailsChildCell childCell = new FootDetailsChildCell(mContext);
                        if (position == 24 || position == 25) {
                            linearLayout.addView(childCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 2f));

                            if (position == 24) {
                                childCell.isShowLine(true);
                                childCell.setLeftPadding(12);
                                childCell.setRightPadding(4);
                            } else {
                                childCell.isShowLine(false);
                                childCell.setLeftPadding(4);
                                childCell.setRightPadding(12);
                            }
                        } else if (position <= 23) {
                            linearLayout.addView(childCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

                            if (j == 0) {
                                childCell.isShowLine(true);
                                childCell.setLeftPadding(12);
                                childCell.setRightPadding(4);
                            } else if (j == 3) {
                                childCell.isShowLine(false);
                                childCell.setLeftPadding(4);
                                childCell.setRightPadding(12);
                            } else {
                                childCell.isShowLine(true);
                                childCell.setLeftPadding(4);
                                childCell.setRightPadding(4);
                            }
                        }

                        if (position >= 26) {//波胆 固定size==26
                            //什么都不做 占位
                        } else {
                            childCell.setData(list.get(position), data, focusList, position, false);
                            childCell.setListener(itemClickListener);
                        }

                        position++;
                    }
                }
            } else {//一行两列 或 多行两列 的样式
                oneAndTwoLayout.setVisibility(View.VISIBLE);
                oneRowTwoColumLayout.setVisibility(View.GONE);
                oneRowMantColumLayout.setVisibility(View.GONE);
                moreRowTwoColumLayout.setVisibility(View.GONE);

                oneAndTwoLayout.removeAllViews();

                int position = 0;

                for (int i = 0; i < (list.size() / 2); i++) {
                    LinearLayout linearLayout = new LinearLayout(mContext);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    oneAndTwoLayout.addView(linearLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 28));

                    View view = new View(mContext);
                    view.setBackgroundColor(0xFFE7E7E7);
                    oneAndTwoLayout.addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));

                    for (int j = 0; j < 2; j++) {
                        FootDetailsChildCell childCell = new FootDetailsChildCell(mContext);
                        childCell.setData(list.get(position), data, focusList, position, "全场让球".equals(list.get(0).getTagName()));
                        childCell.setListener(itemClickListener);

                        linearLayout.addView(childCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));

                        if (j != 1) {
                            childCell.isShowLine(true);
                            childCell.setLeftPadding(12);
                            childCell.setRightPadding(8);
                        } else {
                            childCell.isShowLine(false);
                            childCell.setLeftPadding(8);
                            childCell.setRightPadding(12);
                        }

                        position++;
                    }
                }
            }
        }
    }

    public interface OnItemClickListener {
        boolean onClick(ScrollFootBallDetailsActivity.FootData.FootItemData data, ScrollFootBallDetailsActivity.FootData footData, boolean isAdd);
    }
}
