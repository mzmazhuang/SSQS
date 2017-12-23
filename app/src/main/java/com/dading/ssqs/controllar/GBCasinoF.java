package com.dading.ssqs.controllar;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.CasinoAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseTabsContainer;
import com.dading.ssqs.bean.CasinoBean;
import com.dading.ssqs.utils.Logger;

import java.util.List;

/**
 * 创建者     ZCL
 * 创建时间   2017/5/22 9:52
 * 描述	      娱乐场
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class GBCasinoF extends BaseTabsContainer {
    private static final String TAG = "GBCasino";
    GridView casinoGv;
    private RelativeLayout mErrWithDraw;
    private Button mLoadAgain;
    private ImageView mCasinoGo;
    private TextView mCasinoTitle;
    private CasinoAdapter adapter;

    @Override
    public void initTitleBar() {
        super.initTitleBar();
        mContenLy.setVisibility(View.GONE);
    }

    @Override
    public View initContentView(Context context) {
        View view = View.inflate(mContent, R.layout.guessball_casino, null);
        mCasinoGo = (ImageView) view.findViewById(R.id.top_back);
        mCasinoTitle = (TextView) view.findViewById(R.id.top_title);
        casinoGv = (GridView) view.findViewById(R.id.casino_gv);
        mErrWithDraw = (RelativeLayout) view.findViewById(R.id.err_with_draw);
        mLoadAgain = (Button) view.findViewById(R.id.err_load_again);

        adapter = new CasinoAdapter(mContent);
        casinoGv.setAdapter(adapter);

        return view;
    }


    @Override
    public void initData() {
        mCasinoTitle.setText(getString(R.string.casino));
        getDataNetWork();
    }

    private void getDataNetWork() {
        /**
         * 7.	娱乐场接口
         5)	请求地址：
         /v1.0/disport/list
         6)	请求方式:
         get
         7)	请求参数说明：
         无
         字段名	类型	长度	备注
         id	int		主键
         name	sting		游戏名称
         url	string		跳转的url
         imageUrl	String		图片地址
         status	int		是否调整 0:否1：是
         */

        SSQSApplication.apiClient(0).getDisportList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    mLoadAgain.setClickable(true);

                    List<CasinoBean> items = (List<CasinoBean>) result.getData();

                    if (items != null && items.size() >= 1) {
                        mErrWithDraw.setVisibility(View.GONE);
                        processedData(items);
                    }
                } else {
                    mLoadAgain.setClickable(false);
                    Logger.d(TAG, result.getMessage() + "娱乐场失败信息");
                }
            }
        });
    }

    private void processedData(List<CasinoBean> bean) {
        adapter.setList(bean);
    }

    private OnBackListener listener;

    public void setListener(OnBackListener listener) {
        this.listener = listener;
    }

    public interface OnBackListener {
        void onBack();
    }


    @Override
    protected void initListener() {
        mCasinoGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onBack();
                }
            }
        });
        mLoadAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadAgain.setClickable(false);
                getDataNetWork();
            }
        });
    }
}
