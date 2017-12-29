package com.dading.ssqs.cells;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.activity.BasketBallDetailsActivity;
import com.dading.ssqs.base.LayoutHelper;
import com.dading.ssqs.utils.AndroidUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mazhuang on 2017/12/5.
 */

public class BasketBallDetailsItemCell extends LinearLayout {

    private Context mContext;
    private TextView tvTitle;
    private ImageView collectionView;

    private LinearLayout layout;

    private LinearLayout oneAndTwoLayout;
    private LinearLayout oneRowTwoColumLayout;
    private LinearLayout oneRowMantColumLayout;
    private LinearLayout moreRowTwoColumLayout;

    private List<BasketDetailsChildCell> oneRowTwoColumCells = new ArrayList<>();//一行两列
    private List<BasketDetailsChildCell> oneRowManyColumCells = new ArrayList<>();//一行多列的cells

    public BasketBallDetailsItemCell(Context context) {
        super(context);
        mContext = context;

        setOrientation(LinearLayout.VERTICAL);

        RelativeLayout topLayout = new RelativeLayout(context);
        topLayout.setBackgroundColor(0xFF673221);
        topLayout.setPadding(AndroidUtilities.dp(12), 0, AndroidUtilities.dp(11), 0);
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

        for (int j = 0; j < 2; j++) {
            BasketDetailsChildCell childCell = new BasketDetailsChildCell(mContext);
            oneRowTwoColumLayout.addView(childCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));
            oneRowTwoColumCells.add(childCell);

            if (j != 1) {
                childCell.isShowLine(true);
                childCell.setLeftPadding(12);
                childCell.setRightPadding(8);
            } else {
                childCell.isShowLine(false);
                childCell.setLeftPadding(8);
                childCell.setRightPadding(12);
            }
        }

        oneRowMantColumLayout = new LinearLayout(context);
        oneRowMantColumLayout.setOrientation(LinearLayout.HORIZONTAL);
        oneRowMantColumLayout.setVisibility(View.GONE);
        layout.addView(oneRowMantColumLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 28));

        for (int i = 0; i < 5; i++) {
            BasketDetailsChildCell childCell = new BasketDetailsChildCell(mContext);
            oneRowMantColumLayout.addView(childCell, LayoutHelper.createLinear(0, LayoutHelper.MATCH_PARENT, 1f));
            oneRowManyColumCells.add(childCell);

            if (i == 0) {
                childCell.isShowLine(true);
                childCell.setLeftPadding(11);
                childCell.setRightPadding(7);
            } else if (i == 4) {
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
        moreRowTwoColumLayout.setOrientation(LinearLayout.HORIZONTAL);
        moreRowTwoColumLayout.setVisibility(View.GONE);
        layout.addView(moreRowTwoColumLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 28));

        View view = new View(context);
        view.setBackgroundColor(0xFFE7E7E7);
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

    public void setData(BasketBallDetailsActivity.BasketData data, final OnItemClickListener itemClickListener, List<BasketBallDetailsActivity.BasketData.BasketItemData> focusList) {
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

        List<BasketBallDetailsActivity.BasketData.BasketItemData> list = data.getItems();
        if (list != null) {
            //第一和第二的样式跟其他不同  多行多列
            if ("全场大小".equals(list.get(0).getTagName()) || "全场让球".equals(list.get(0).getTagName())) {
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
                        BasketDetailsChildCell childCell = new BasketDetailsChildCell(mContext);
                        childCell.setData(list.get(position), data, focusList);
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
            } else if ("最后一位".equals(list.get(0).getTagName())) {//最后一位
                oneAndTwoLayout.setVisibility(View.GONE);
                oneRowTwoColumLayout.setVisibility(View.GONE);
                oneRowMantColumLayout.setVisibility(View.VISIBLE);
                moreRowTwoColumLayout.setVisibility(View.GONE);
                //一行 多列的

                for (int i = 0; i < list.size(); i++) {
                    oneRowManyColumCells.get(i).setData(list.get(i), data, focusList);
                    oneRowManyColumCells.get(i).changeParams();
                    oneRowManyColumCells.get(i).setListener(itemClickListener);
                }
            } else if (list.size() == 2) {
                oneAndTwoLayout.setVisibility(View.GONE);
                oneRowMantColumLayout.setVisibility(View.GONE);
                oneRowTwoColumLayout.setVisibility(View.VISIBLE);
                moreRowTwoColumLayout.setVisibility(View.GONE);
                //一行 两列的

                for (int i = 0; i < list.size(); i++) {
                    oneRowTwoColumCells.get(i).setData(list.get(i), data, focusList);
                    oneRowTwoColumCells.get(i).setListener(itemClickListener);
                }
            } else {
                oneAndTwoLayout.setVisibility(View.GONE);
                oneRowMantColumLayout.setVisibility(View.GONE);
                oneRowTwoColumLayout.setVisibility(View.GONE);
                moreRowTwoColumLayout.setVisibility(View.VISIBLE);

                moreRowTwoColumLayout.removeAllViews();

                int position = 0;

                for (int i = 0; i < (list.size() / 2); i++) {
                    LinearLayout linearLayout = new LinearLayout(mContext);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    oneAndTwoLayout.addView(linearLayout, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 28));

                    View view = new View(mContext);
                    view.setBackgroundColor(0xFFE7E7E7);
                    oneAndTwoLayout.addView(view, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, 1));

                    for (int j = 0; j < 2; j++) {
                        BasketDetailsChildCell childCell = new BasketDetailsChildCell(mContext);
                        childCell.setData(list.get(position), data, focusList);
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
        boolean onClick(BasketBallDetailsActivity.BasketData.BasketItemData data, BasketBallDetailsActivity.BasketData basketData, boolean isAdd);
    }
}
