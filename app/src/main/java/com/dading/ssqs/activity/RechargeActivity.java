package com.dading.ssqs.activity;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.ChargeUploadElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.QRCodeBean;
import com.dading.ssqs.bean.WXDFBean;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2017/5/23 17:17
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RechargeActivity extends BaseActivity {

    private static final String TAG = "RechargeActivity";
    @Bind(R.id.recharge_content)
    TextView mRechargeContent;
    @Bind(R.id.recharge_tip)
    TextView mRechargeTip;
    @Bind(R.id.recharge_money)
    TextView mRecharge_money;
    @Bind(R.id.recharge_iv)
    ImageView mRechargeIv;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    private WXDFBean.InfoBean mBean;
    private String mName;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        /**
         * bankAddress : http://192.168.0.115:8080/images/charge/wechatCode.jpg
         * bankName : 微信支付平台
         * cardNumber : 13926978352
         * id : 2
         * logo : /charge/20170525034058hdlbliZCgl-650x613.jpg
         * moneys : [10,100,300,800]
         * name : 支付宝支付
         * owner : 郑贤
         * remark : 扫码步骤：
         */

        mBean = (WXDFBean.InfoBean) intent.getSerializableExtra(Constent.RECHARGE_INFO);

        //如果等于1就要请求动态支付码
        if (mBean != null) {
            mTopTitle.setText(mBean.getName());
            mRechargeTip.setText(mBean.getName() + "扫码信息");
            mRecharge_money.setText("充值金额:" + mBean.getMoney() + "元");
            mRechargeContent.setText(mBean.getRemark());
            mName = mBean.getName();

            if (mBean.getIsThird() == 1) {
                /**
                 48.第三方支付方式获取二维码图片
                 a)	请求地址：/v1.0/charge/qrcode/id/{id}/amount/{amount}
                 b)	请求方式:get
                 c)	请求参数说明：字段名	    类型	    长度	    是   否必填	备注
                 auth_token	string		    是	    Token
                 id	        Int		        是	支付平台主键
                 amount	    Int		        是	充值金额
                 */

                SSQSApplication.apiClient(classGuid).getThirdImage(mBean.getPayType(), mBean.getMoney(), new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            QRCodeBean bean = (QRCodeBean) result.getData();

                            if (bean != null) {
                                setIv(bean.getData());
                            }
                        } else {
                            TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                        }
                    }
                });
            } else {
                setIv(mBean.getBankAddress());
            }
        }
    }

    private void setIv(String url) {
        SSQSApplication.glide.load(url).asBitmap().fitCenter().error(R.mipmap.fail).into(mRechargeIv);
    }

    @OnClick({R.id.top_back, R.id.recharge_at_once, R.id.recharge_finish, R.id.recharge_up_step})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.recharge_up_step:
                finish();
                break;
            case R.id.recharge_at_once:
                saveImageToGallery();
                // OpenPay( );
                break;
            case R.id.recharge_finish:
                /**
                 * 17.充值上传
                 a)	请求地址：/v1.0/charge/save
                 b)	请求方式:post
                 c)	请求参数说明
                 字段名	类型	长度	是否必填	备注
                 auth_token	string		是	token
                 amount	int		是	充值金额
                 id	int		是	支付平台主键
                 */
                ChargeUploadElement element = new ChargeUploadElement();
                element.setAmount(String.valueOf(mBean.getMoney()));
                element.setId(String.valueOf(mBean.getId()));

                SSQSApplication.apiClient(classGuid).chargeUpload(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            Intent intent = new Intent(RechargeActivity.this, RechargeResultAcitvity.class);
                            intent.putExtra(Constent.ACCOUNT, mBean.getMoney() + "");
                            startActivity(intent);
                            finish();
                        } else {
                            TmtUtils.midToast(RechargeActivity.this, result.getMessage(), 0);
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 截图保存
     */
    private void saveImageToGallery() {
        View dView = getWindow().getDecorView();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = dView.getDrawingCache();
        String dirName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "MImages";
        File fileDir = new File(dirName);
        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        long mImageTime = System.currentTimeMillis();
        long dateSeconds = mImageTime / 1000;
        String mImageFileName = dateSeconds + ".png"; //以保存时间命名
        String mImageFilePath = dirName + File.separator + mImageFileName; //这里的mImageFilePath是： 目录名称+文件名
        int mImageWidth = bitmap.getWidth();
        int mImageHeight = bitmap.getHeight();
        ContentValues values = new ContentValues();
        ContentResolver resolver = getApplicationContext().getContentResolver();
        values.put(MediaStore.Images.ImageColumns.DATA, mImageFilePath);
        values.put(MediaStore.Images.ImageColumns.TITLE, mImageFileName);
        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, mImageFileName);
        values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, mImageTime);
        values.put(MediaStore.Images.ImageColumns.DATE_ADDED, dateSeconds);
        values.put(MediaStore.Images.ImageColumns.DATE_MODIFIED, dateSeconds);
        values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.ImageColumns.WIDTH, mImageWidth);
        values.put(MediaStore.Images.ImageColumns.HEIGHT, mImageHeight);
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream out = resolver.openOutputStream(uri);
            if (out == null || bitmap == null)
                return;
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        values.clear();
        values.put(MediaStore.Images.ImageColumns.SIZE, new File(mImageFilePath).length());
        resolver.update(uri, values, null, null);

        openAppByPackageName();
    }

    /**
     * 打开qq  支付宝 微信
     */
    public void openAppByPackageName() {
        String packageName = "com.tencent.mm";
        PackageInfo pi;
        try {
            if (mName.contains("微信支付")) {
                packageName = "com.tencent.mm";
            } else if (mName.contains("支付宝支付")) {
                packageName = "com.eg.android.AlipayGphone";
            } else if (mName.contains("QQ")) {
                packageName = "com.tencent.mobileqq";
            }
            //获取包信息
            pi = getPackageManager().getPackageInfo(packageName, 0);
            //创建意图
            Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
            //设置意图包名
            resolveIntent.setPackage(pi.packageName);

            PackageManager pManager = getPackageManager();
            List<ResolveInfo> apps = pManager.queryIntentActivities(resolveIntent, 0);
            ResolveInfo ri = apps.iterator().next();
            //防止空指针
            if (ri != null) {
                packageName = ri.activityInfo.packageName;
                String className = ri.activityInfo.name;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//重点是加这个

                ComponentName cn = new ComponentName(packageName, className);

                intent.setComponent(cn);
                startActivity(intent);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            TmtUtils.midToast(this, "设备上没有安装此应用", 0);
        }
    }
}
