package com.dading.ssqs.controllar.details;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.bean.BetBean;
import com.dading.ssqs.interfaces.MyItemClickListern;
import com.dading.ssqs.interfaces.MyItemSonCloseClickListern;
import com.dading.ssqs.utils.DensityUtil;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.ToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/6 17:15
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class PopBettingBodyAdapter extends BaseAdapter implements ListAdapter {
    private static final String TAG = "PopBettingBodyAdapter";
    private       ArrayList<BetBean>         mListBean;
    private       Context                    context;
    private       HashMap<CheckBox, BetBean> map;
    private final DecimalFormat              mDf;
    private final HashMap<Integer, Boolean>  mListKeyBoard;

    public PopBettingBodyAdapter(Context context, HashMap<CheckBox, BetBean> map) {
        this.context = context;
        this.map = map;

        mDf = new DecimalFormat(".00");
        mListBean = new ArrayList<>();
        for (Map.Entry<CheckBox, BetBean> cb : map.entrySet()) {
            mListBean.add(cb.getValue());//获取bean的集合
        }
        mListKeyBoard = new HashMap<>();
    }


    @Override
    public int getCount() {
        if (mListBean != null) {
            return mListBean.size();
        }
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder hoder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.pop_betting_lv_item, null);
            hoder = new ViewHolder(convertView);
            convertView.setTag(hoder);
        } else {
            hoder = (ViewHolder) convertView.getTag();
        }
        /**
         * 还原初始化状态
         */
        switch (mListBean.get(position).selected) {
            case 1:
                hoder.mBettingBodyItemResult.setText(mListBean.get(position).home);
                break;
            case 2:
                hoder.mBettingBodyItemResult.setText("平局");
                break;
            case 3:
                hoder.mBettingBodyItemResult.setText(mListBean.get(position).away);
                break;
            default:
                break;
        }

        final BetBean bean = mListBean.get(position);

        /**
         * 还原状态
         */
        hoder.mBettingBodyItemType.setText(bean.payTypeName);//下注
        String text = bean.home + "vs" + bean.away;
        hoder.mBettingBodyItemTeam.setText(text);//队伍
        hoder.mItemBettingBodyPL.setText(bean.realRate);//赔率

        hoder.mKeybordLy.setVisibility(bean.cbTag ? View.VISIBLE : View.GONE);//键盘
        if (bean.cbTag) {
            hoder.mBettingBodyItemInputGold.setChecked(true);//输入金额
        } else {
            hoder.mBettingBodyItemInputGold.setChecked(false);//输入金额
        }
        hoder.mBettingBodyItemInputGold.setText(bean.amount);//输入金额
        String returnNum = bean.returnNum + "";
        hoder.mBettingBodyItemExpectedReturn.setText(returnNum);

        hoder.mItemBettingBodyClose.setOnClickListener(new MyItemSonCloseClickListern(context, position) {
            @Override
            public void onClick(View v) {
                ArrayList<CheckBox> list = new ArrayList<>();
                for (Map.Entry<CheckBox, BetBean> cb : map.entrySet()) {
                    list.add(cb.getKey());
                }
                Logger.d(TAG, "关闭的postion是------------------------------:" + postion);
                CheckBox key = list.get(postion);
                mListBean.remove(postion);
                key.setChecked(false);//将外面选择状态改变
                map.remove(key);

                if (map.size() <= 1) {
                    int i = DensityUtil.dip2px(context, 200);
                    hoder.mItemBettingBodyLy.setMinimumHeight(i);
                }
                Logger.d(TAG, "我被点击了....................close" + map.size());
                PopBettingBodyAdapter.this.notifyDataSetChanged();
            }
        });

        hoder.mBettingBodyItemInputGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 用于拼接键盘数字
                 */
                boolean now = bean.cbTag;
                for (Integer i : mListKeyBoard.keySet()) {
                    mListBean.get(i).cbTag = false;
                }
                synchronized (this) {
                    notifyDataSetChanged();
                }
                Log.d(TAG, "返回数据是------------------------------:" + now);
                hoder.mKeybordLy.setVisibility(now ? View.GONE : View.VISIBLE);
                bean.cbTag = !now;
                if (bean.cbTag) {
                    mListKeyBoard.put(position, true);
                } else {
                    mListKeyBoard.remove(position);
                }
            }
        });

        hoder.mKeybord0.setOnClickListener(new MyItemClickListern(bean) {
                                               @Override
                                               public void onClick(View v) {
                                                   if (bean.amount.length() != 0 && !"请输入金币".equals(bean.amount)) {
                                                       bean.amount = bean.amount + "0";
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                       Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                       String s = String.valueOf(mDf.format(resid));
                                                       bean.returnNum = s;
                                                       hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                   }
                                               }
                                           }
        );
        hoder.mKeybord1.setOnClickListener(new MyItemClickListern(bean) {
                                               @Override
                                               public void onClick(View v) {
                                                   if ("请输入金币".equals(bean.amount)) {
                                                       bean.amount = "1";
                                                   } else {
                                                       bean.amount = bean.amount + "1";
                                                   }
                                                   hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                   Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                   String s = String.valueOf(mDf.format(resid));
                                                   bean.returnNum = s;
                                                   hoder.mBettingBodyItemExpectedReturn.setText(s);
                                               }
                                           }

        );
        hoder.mKeybord2.setOnClickListener(new MyItemClickListern(bean) {
                                               @Override
                                               public void onClick(View v) {
                                                   if ("请输入金币".equals(bean.amount)) {
                                                       bean.amount = "2";
                                                   } else {
                                                       bean.amount = bean.amount + "2";
                                                   }
                                                   hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                   Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                   String s = String.valueOf(mDf.format(resid));
                                                   bean.returnNum = s;
                                                   hoder.mBettingBodyItemExpectedReturn.setText(s);
                                               }
                                           }

        );
        hoder.mKeybord3.setOnClickListener(new MyItemClickListern(bean) {
                                               @Override
                                               public void onClick(View v) {
                                                   if ("请输入金币".equals(bean.amount)) {
                                                       bean.amount = "3";
                                                   } else {
                                                       bean.amount = bean.amount + "3";
                                                   }
                                                   hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                   Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                   String s = String.valueOf(mDf.format(resid));
                                                   bean.returnNum = s;
                                                   hoder.mBettingBodyItemExpectedReturn.setText(s);
                                               }
                                           }

        );
        hoder.mKeybord4.setOnClickListener(new MyItemClickListern(bean) {
                                               @Override
                                               public void onClick(View v) {
                                                   if ("请输入金币".equals(bean.amount)) {
                                                       bean.amount = "4";
                                                   } else {
                                                       bean.amount = bean.amount + "4";
                                                   }
                                                   hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                   Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                   String s = String.valueOf(mDf.format(resid));
                                                   bean.returnNum = s;
                                                   hoder.mBettingBodyItemExpectedReturn.setText(s);
                                               }
                                           }

        );
        hoder.mKeybord5.setOnClickListener(new MyItemClickListern(bean) {
                                               @Override
                                               public void onClick(View v) {
                                                   if ("请输入金币".equals(bean.amount)) {
                                                       bean.amount = "5";
                                                   } else {
                                                       bean.amount = bean.amount + "5";
                                                   }
                                                   hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                   Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                   String s = String.valueOf(mDf.format(resid));
                                                   bean.returnNum = s;
                                                   hoder.mBettingBodyItemExpectedReturn.setText(s);
                                               }
                                           }

        );
        hoder.mKeybord6.setOnClickListener(new MyItemClickListern(bean)

                                           {
                                               @Override
                                               public void onClick(View v) {
                                                   if ("请输入金币".equals(bean.amount)) {
                                                       bean.amount = "6";
                                                   } else {
                                                       bean.amount = bean.amount + "6";
                                                   }
                                                   hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                   Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                   String s = String.valueOf(mDf.format(resid));
                                                   bean.returnNum = s;
                                                   hoder.mBettingBodyItemExpectedReturn.setText(s);
                                               }
                                           }

        );
        hoder.mKeybord7.setOnClickListener(new MyItemClickListern(bean)

                                           {
                                               @Override
                                               public void onClick(View v) {
                                                   if ("请输入金币".equals(bean.amount)) {
                                                       bean.amount = "7";
                                                   } else {
                                                       bean.amount = bean.amount + "7";
                                                   }
                                                   hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                   Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                   String s = String.valueOf(mDf.format(resid));
                                                   bean.returnNum = s;
                                                   hoder.mBettingBodyItemExpectedReturn.setText(s);
                                               }
                                           }

        );
        hoder.mKeybord8.setOnClickListener(new MyItemClickListern(bean)

                                           {
                                               @Override
                                               public void onClick(View v) {
                                                   if ("请输入金币".equals(bean.amount)) {
                                                       bean.amount = "8";
                                                   } else {
                                                       bean.amount = bean.amount + "8";
                                                   }
                                                   hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                   Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                   String s = String.valueOf(mDf.format(resid));
                                                   bean.returnNum = s;
                                                   hoder.mBettingBodyItemExpectedReturn.setText(s);
                                               }
                                           }

        );
        hoder.mKeybord9.setOnClickListener(new MyItemClickListern(bean)

                                           {
                                               @Override
                                               public void onClick(View v) {
                                                   if ("请输入金币".equals(bean.amount)) {
                                                       bean.amount = "9";
                                                   } else {
                                                       bean.amount = bean.amount + "9";
                                                   }
                                                   hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                   Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                   String s = String.valueOf(mDf.format(resid));
                                                   bean.returnNum = s;
                                                   hoder.mBettingBodyItemExpectedReturn.setText(s);
                                               }
                                           }

        );
        hoder.mKeybordThousand.setOnClickListener(new MyItemClickListern(bean)

                                                  {
                                                      @Override
                                                      public void onClick(View v) {
                                                          if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                                                              bean.amount = "";
                                                              hoder.mBettingBodyItemExpectedReturn.setText("");
                                                          } else {
                                                              bean.amount = bean.amount + "000";
                                                              hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                              Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                              String s = String.valueOf(mDf.format(resid));
                                                              bean.returnNum = s;
                                                              hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                          }
                                                      }
                                                  }

        );
        hoder.mKeybordTenThousand.setOnClickListener(new MyItemClickListern(bean)

                                                     {
                                                         @Override
                                                         public void onClick(View v) {
                                                             if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                                                                 bean.amount = "";
                                                                 hoder.mBettingBodyItemExpectedReturn.setText("");
                                                             } else {
                                                                 bean.amount = bean.amount + "0000";
                                                                 hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                                 Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                                 String s = String.valueOf(mDf.format(resid));
                                                                 bean.returnNum = s;
                                                                 hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                             }
                                                         }
                                                     }

        );
        hoder.mKeybordHundredThousand.setOnClickListener(new MyItemClickListern(bean)

                                                         {
                                                             @Override
                                                             public void onClick(View v) {
                                                                 if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                                                                     bean.amount = "";
                                                                     hoder.mBettingBodyItemExpectedReturn.setText("");
                                                                 } else {
                                                                     bean.amount = bean.amount + "00000";
                                                                     hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                                     Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                                     String s = String.valueOf(mDf.format(resid));
                                                                     bean.returnNum = s;
                                                                     hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                                 }
                                                             }
                                                         }

        );
        hoder.mKeybordMillion.setOnClickListener(new MyItemClickListern(bean)

                                                 {
                                                     @Override
                                                     public void onClick(View v) {
                                                         if ("请输入金币".equals(bean.amount) || bean.amount.length() == 0) {
                                                             bean.amount = "";
                                                             hoder.mBettingBodyItemExpectedReturn.setText("");
                                                         } else {
                                                             bean.amount = bean.amount + "000000";
                                                             hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                             Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                             String s = String.valueOf(mDf.format(resid));
                                                             bean.returnNum = s;
                                                             hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                         }
                                                     }
                                                 }

        );
        hoder.mKeybordClear.setOnClickListener(new MyItemClickListern(bean) {
                                                   @Override
                                                   public void onClick(View v) {
                                                       bean.amount = "";
                                                       hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                       bean.returnNum = "0";
                                                       hoder.mBettingBodyItemExpectedReturn.setText("0");
                                                   }
                                               }

        );
        hoder.mKeybordConfirm.setOnClickListener(new MyItemClickListern(bean) {
                                                     @Override
                                                     public void onClick(View v) {
                                                         if (!"请输入金币".equals(bean.amount) && bean.amount.length() > 0) {
                                                             hoder.mBettingBodyItemInputGold.setText(bean.amount);
                                                             hoder.mBettingBodyItemInputGold.setChecked(bean.cbTag);
                                                             Double resid = Double.parseDouble(bean.amount.trim()) * Double.parseDouble(bean.realRate.trim());
                                                             String s = String.valueOf(mDf.format(resid));
                                                             hoder.mBettingBodyItemExpectedReturn.setText(s);
                                                             hoder.mBettingBodyItemInputGold.setTextColor(Color.GRAY);
                                                             hoder.mBettingBodyItemInputGold.setChecked(false);
                                                             bean.cbTag = false;
                                                             mListKeyBoard.remove(position);
                                                             hoder.mKeybordLy.setVisibility(View.GONE);
                                                         } else {
                                                             ToastUtils.midToast(context, "请输入下注金额", 0);
                                                         }
                                                     }
                                                 }
        );

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.item_betting_body_pl)
        TextView     mItemBettingBodyPL;
        @Bind(R.id.item_betting_body_ly)
        LinearLayout mItemBettingBodyLy;
        @Bind(R.id.item_betting_body_close)
        ImageView    mItemBettingBodyClose;
        @Bind(R.id.betting_body_item_type)
        TextView     mBettingBodyItemType;
        @Bind(R.id.betting_body_item_result)
        TextView     mBettingBodyItemResult;
        @Bind(R.id.betting_body_item_team)
        TextView     mBettingBodyItemTeam;
        @Bind(R.id.betting_body_item_input_gold)
        CheckBox     mBettingBodyItemInputGold;
        @Bind(R.id.betting_body_item_expected_return)
        TextView     mBettingBodyItemExpectedReturn;
        @Bind(R.id.keybord_1)
        Button       mKeybord1;
        @Bind(R.id.keybord_2)
        Button       mKeybord2;
        @Bind(R.id.keybord_3)
        Button       mKeybord3;
        @Bind(R.id.keybord_4)
        Button       mKeybord4;
        @Bind(R.id.keybord_5)
        Button       mKeybord5;
        @Bind(R.id.keybord_6)
        Button       mKeybord6;
        @Bind(R.id.keybord_7)
        Button       mKeybord7;
        @Bind(R.id.keybord_8)
        Button       mKeybord8;
        @Bind(R.id.keybord_9)
        Button       mKeybord9;
        @Bind(R.id.keybord_0)
        Button       mKeybord0;
        @Bind(R.id.keybord_thousand)
        Button       mKeybordThousand;
        @Bind(R.id.keybord_ten_thousand)
        Button       mKeybordTenThousand;
        @Bind(R.id.keybord_hundred_thousand)
        Button       mKeybordHundredThousand;
        @Bind(R.id.keybord_million)
        Button       mKeybordMillion;
        @Bind(R.id.keybord_clear)
        Button       mKeybordClear;
        @Bind(R.id.keybord_confirm)
        Button       mKeybordConfirm;
        @Bind(R.id.keybord_ly)
        LinearLayout mKeybordLy;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public ArrayList<BetBean> getGlod() {
        return mListBean;
    }

}
