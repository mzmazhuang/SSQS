package com.dading.ssqs.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.activity.MatchInfoActivity;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.MatchBeforBeanAll;
import com.dading.ssqs.utils.DateUtils;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2016/10/31 11:08
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GBMatchBeforeAdapter extends BaseExpandableListAdapter {
    private final MatchBeforBeanAll groupData;
    private final Context                      mContent;
    private final int                          type;

    public GBMatchBeforeAdapter(Context content, MatchBeforBeanAll groupNameList, int type) {
        this.mContent = content;
        this.groupData = groupNameList;
        this.type = type;
    }

    @Override
    public int getGroupCount() {
        if (groupData == null) {
            return 0;
        }
        if (type == 1) {
            if (groupData.leagueDate.size() != 0) {
                return groupData.leagueDate.size();
            }
        } else if (type == 2) {
            if (groupData.leagueName.size() != 0) {
                return groupData.leagueName.size();
            }
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (type == 1) {
            return groupData.leagueDate.get(groupPosition).matchs.size();
        } else if (type == 2) {
            return groupData.leagueName.get(groupPosition).matchs.size();
        }
        return 0;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = View.inflate(mContent, R.layout.gb_matchbefore_group_ly, null);
        }

        TextView tv = (TextView) convertView.findViewById(R.id.matchbefore_group_item);
        TextView tvNum = (TextView) convertView.findViewById(R.id.matchbefore_group_item_num);
        ImageView iv = (ImageView) convertView.findViewById(R.id.matchbefore_group_item_arrow);

        if (type == 1) {
            List<MatchBeforBeanAll.LeagueDateEntity> leagueDate = groupData.leagueDate;
            setGroupData(groupPosition, tv,tvNum, leagueDate);
        } else if (type == 2) {
            List<MatchBeforBeanAll.LeagueNameEntity> leagueName = groupData.leagueName;
            setGroupName(groupPosition, tv, tvNum,leagueName);
        }

        if (isExpanded) {
            iv.setImageResource(R.mipmap.shang_);
        } else {
            iv.setImageResource(R.mipmap.xia);
        }
        return convertView;
    }

    private void setGroupData(int groupPosition, TextView tv, TextView tvnum, List<MatchBeforBeanAll.LeagueDateEntity> leagueDate) {
        MatchBeforBeanAll.LeagueDateEntity entity = leagueDate.get(groupPosition);
        int nums = entity.nums;
        String title = entity.title;
        String s = DateUtils.getweekdayBystr(title);
        String groupTitle = title + " " + s + "";
        tv.setText(groupTitle);
        String num = "(" + nums + ")";
        tvnum.setText(num);
    }

    private void setGroupName(int groupPosition, TextView tv, TextView tvnum, List<MatchBeforBeanAll.LeagueNameEntity> leagueDate) {
        MatchBeforBeanAll.LeagueNameEntity entity = leagueDate.get(groupPosition);
        int nums = entity.nums;
        String title = entity.title;
        String groupTitle = title + " ";
        tv.setText(groupTitle);
        String num = "(" + nums + ")";
        tvnum.setText(num);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContent, R.layout.gb_matchbefore_children_ly, null);
            holder.mMatchLy = (LinearLayout) convertView.findViewById(R.id.match_ly);
            holder.mMatchType = (TextView) convertView.findViewById(R.id.match_type);
            holder.mMatchTime = (TextView) convertView.findViewById(R.id.match_time);
            holder.mMatchMain = (TextView) convertView.findViewById(R.id.match_main);
            holder.mMatchSecond = (TextView) convertView.findViewById(R.id.match_second);
            holder.mGuessPeople = (TextView) convertView.findViewById(R.id.guess_people);
            holder.mMatchGoImage = (ImageView) convertView.findViewById(R.id.go_match_info);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (type == 1) {
            List<MatchBeforBeanAll.LeagueDateEntity> leagueDate = groupData.leagueDate;
            setItemChildData(groupPosition, childPosition, holder, leagueDate);
        } else if (type == 2) {
            List<MatchBeforBeanAll.LeagueNameEntity> leagueName = groupData.leagueName;
            setItemChildName(groupPosition, childPosition, holder, leagueName);
        }
        return convertView;
    }

    private void setItemChildData(final int groupPosition, final int childPosition, ViewHolder holder, final List<MatchBeforBeanAll.LeagueDateEntity> leagueDate) {
        String leagueNames = leagueDate.get(groupPosition).matchs.get(childPosition).leagueName;
        String openTime = groupData.leagueDate.get(groupPosition).matchs.get(childPosition).openTime;
        if (openTime.length() > 16) {
            String time = openTime.substring(11, 16);
            holder.mMatchTime.setTextSize(10);
            holder.mMatchTime.setText(time);
        }
        String home = groupData.leagueDate.get(groupPosition).matchs.get(childPosition).home;
        String away = groupData.leagueDate.get(groupPosition).matchs.get(childPosition).away;
        int joinCount = groupData.leagueDate.get(groupPosition).matchs.get(childPosition).joinCount;

        holder.mMatchType.setText(leagueNames);
        holder.mMatchMain.setText(home);
        holder.mMatchSecond.setText(away);
        String text = joinCount + "人竞猜";
        holder.mGuessPeople.setText(text);

        holder.mMatchLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContent, MatchInfoActivity.class);
                intent.putExtra(Constent.MATCH_ID, leagueDate.get(groupPosition).matchs.get(childPosition).id);
                intent.putExtra(Constent.INTENT_FROM, "match_before");
                mContent.startActivity(intent);//跳转进比赛内容页
            }
        });
    }

    private void setItemChildName(final int groupPosition, final int childPosition, ViewHolder holder, final List<MatchBeforBeanAll.LeagueNameEntity> leagueName) {
        String leagueNames = leagueName.get(groupPosition).matchs.get(childPosition).leagueName;
        String openTime = groupData.leagueName.get(groupPosition).matchs.get(childPosition).openTime;
        if (openTime.length() > 16) {
            String time = openTime.substring(5, 16);
            holder.mMatchTime.setText(time);
            holder.mMatchTime.setTextSize(8);
        }
        String home = groupData.leagueName.get(groupPosition).matchs.get(childPosition).home;
        String away = groupData.leagueName.get(groupPosition).matchs.get(childPosition).away;
        int joinCount = groupData.leagueName.get(groupPosition).matchs.get(childPosition).joinCount;

        holder.mMatchType.setText(leagueNames);
        holder.mMatchMain.setText(home);
        holder.mMatchSecond.setText(away);
        String text = joinCount + "人竞猜";
        holder.mGuessPeople.setText(text);

        holder.mMatchLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContent, MatchInfoActivity.class);
                intent.putExtra(Constent.MATCH_ID, leagueName.get(groupPosition).matchs.get(childPosition).id);
                intent.putExtra("where", "match_before");
                mContent.startActivity(intent);//跳转进比赛内容页
            }
        });
    }

    class ViewHolder {
        TextView  mMatchTime;
        TextView  mMatchMain;
        TextView  mMatchSecond;
        ImageView mMatchGoImage;
        public TextView     mMatchType;
        public TextView     mGuessPeople;
        public LinearLayout mMatchLy;
    }
        /*-----------------------------------------------------------------------------------------------------*/

    @Override
    public Object getGroup(int groupPosition) {
        if (type == 1) {
            return groupData.leagueDate.get(groupPosition);
        } else if (type == 2) {
            return groupData.leagueName.get(groupPosition);
        }
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (type == 2) {
            return groupData.leagueDate.get(groupPosition).matchs.get(childPosition);
        } else if (type == 1) {
            return groupData.leagueName.get(groupPosition).matchs.get(childPosition);
        }
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
