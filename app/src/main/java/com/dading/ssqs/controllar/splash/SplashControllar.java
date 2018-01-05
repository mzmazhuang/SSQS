package com.dading.ssqs.controllar.splash;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.activity.MainActivity;
import com.dading.ssqs.activity.SplashAcitivty;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.base.BaseFragnment;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.bean.SplashBeanGG;
import com.dading.ssqs.utils.AndroidUtilities;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.google.gson.Gson;

import java.util.List;

import butterknife.Bind;

/**
 * 创建者     ZCL
 * 创建时间   2016/9/19 10:05
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SplashControllar extends BaseFragnment {

    private static final String TAG = "SplashControllar";
    @Bind(R.id.splash_top_iv)
    ImageView mSplashTopIv;
    @Bind(R.id.splash_progress_bar)
    TextView mSplashProgressBar;
    @Bind(R.id.splash_taste)
    ImageView mSplashTaste;
    @Bind(R.id.splash_version)
    TextView mSplashVersion;
    private String currentName;
    public View mRootView;
    private List<SplashBeanGG> mData;
    private int mTime;
    private String mToken;
    private boolean IS_GO_MAIN = false;
    private Runnable mTask;

    @Override
    protected int setLayout() {
        return R.layout.splash_four;
    }

    @Override
    public void initData() {
        currentName = AndroidUtilities.getVersionName(mContent);
        UIUtils.getSputils().putBoolean(Constent.IS_CLICK, false);
        mTime = 3;

        mTask = new Runnable() {
            @Override
            public void run() {
                if (mSplashProgressBar != null)
                    if (mTime > 0) {
                        mSplashProgressBar.setText(String.valueOf(mTime));
                        mTime--;
                        UIUtils.postTaskDelay(this, 1000);
                    } else {
                        mSplashProgressBar.setText("跳过");

                        if (!UIUtils.getSputils().getString(Constent.IS_FRISE, "0").equals("0")) {
                            goMain();
                        }
                    }
            }
        };
        UIUtils.postTaskDelay(mTask, 1000);

        String text = "版本:" + currentName;
        mSplashVersion.setText(text);

        SSQSApplication.apiClient(0).getGuideData(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    mData = (List<SplashBeanGG>) result.getData();

                    if (mData != null) {
                        processData(mData);
                    }
                } else {
                    if (!IS_GO_MAIN) {
                        IS_GO_MAIN = true;
                        Intent intent = new Intent(mContent, MainActivity.class);
                        mContent.startActivity(intent);
                        ((SplashAcitivty) mContent).finish();
                    }
                }
            }
        });

        //判断是否自动登陆
        mToken = UIUtils.getSputils().getString(Constent.TOKEN, "");
        if (!mToken.equals("")) {
            SSQSApplication.apiClient(0).getUserInfo(new CcApiClient.OnCcListener() {
                @Override
                public void onResponse(CcApiResult result) {
                    if (result.isOk()) {

                        LoadingBean bean = (LoadingBean) result.getData();

                        Gson gson = new Gson();

                        UIUtils.getSputils().putString(Constent.LOADING_STATE_SP, gson.toJson(bean, LoadingBean.class));

                        if (bean != null) {
                            UIUtils.getSputils().putBoolean(Constent.IS_BIND_CARD, bean.isBindCard == 1);
                            UIUtils.getSputils().putString(Constent.TOKEN, bean.authToken);
                            UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, true);
                            UIUtils.getSputils().putInt(Constent.IS_VIP, bean.isVip);
                            UIUtils.getSputils().putBoolean(Constent.USER_TYPE, bean.userType == 3);
                            UIUtils.getSputils().putInt(Constent.USER_TYPE_NUM, bean.userType);

                            UIUtils.getSputils().putString(Constent.GLODS, bean.banlance + "");
                            UIUtils.getSputils().putString(Constent.DIAMONDS, bean.diamond + "");

                            //发送广播
                            UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        }
                    } else {
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        UIUtils.getSputils().putInt(Constent.USER_TYPE_NUM, 0);
                        ToastUtils.midToast(UIUtils.getContext(), "登录失效,请重新登录.", 0);
                    }
                }
            });
        }
    }

    private void processData(List<SplashBeanGG> data) {
        if (data != null && data.size() > 0 && mSplashTopIv != null) {
            SSQSApplication.glide.load(data.get(0).imageUrl).centerCrop().into(mSplashTopIv);
        }
    }

    @Override
    public void initListener() {
        mSplashTopIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData != null && mData.size() > 0) {
                    if (!IS_GO_MAIN) {
                        IS_GO_MAIN = true;

                        UIUtils.removeTask(mTask);

                        Intent intent = new Intent(mContent, MainActivity.class);
                        if (mData != null && mData.size() > 0) {
                            intent.putExtra(Constent.SPLASH_URL, mData.get(0).forwardUrl);
                        }
                        mContent.startActivity(intent);

                        UIUtils.getSputils().putString(Constent.IS_FRISE, "1");
                        UIUtils.getSputils().putBoolean(Constent.IS_CLICK, true);
                        ((SplashAcitivty) mContent).finish();
                    }
                }
            }
        });
        mSplashTaste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMain();
            }
        });
        mSplashProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMain();
            }
        });
    }

    private void goMain() {
        if (!IS_GO_MAIN) {
            IS_GO_MAIN = true;

            UIUtils.removeTask(mTask);

            UIUtils.getSputils().putString(Constent.IS_FRISE, "1");

            Intent intent = new Intent(mContent, MainActivity.class);
            mContent.startActivity(intent);

            ((SplashAcitivty) mContent).finish();
        }
    }

    @Override
    protected void setUnDe() {
        UIUtils.removeTask(mTask);
    }
}
