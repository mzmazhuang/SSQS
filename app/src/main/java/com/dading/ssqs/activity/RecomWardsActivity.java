package com.dading.ssqs.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.InviteCodeBean;
import com.dading.ssqs.bean.ShareBean;
import com.dading.ssqs.onekeyshare.OnekeyShare;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

import butterknife.Bind;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;

/**
 * 创建者     ZCL
 * 创建时间   2017/2/10 16:15
 * 描述	      ${邀请码邀请}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class RecomWardsActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RecomWardsActivity";
    String text;
    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.recom_wards_friend_num)
    TextView recomwardsfriendnum;
    @Bind(R.id.recom_wards_incoome_num)
    TextView recomwardsincoomenum;
    @Bind(R.id.recom_wards_qr_code_iv)
    ImageView recomwardsqrcodeiv;
    @Bind(R.id.recom_wards_invite_code)
    TextView recomwardsinvitecode;
    @Bind(R.id.recom_wards_share)
    Button recomwardsshare;

    private InviteCodeBean mBean;
    private Bitmap mBitmap;
    private Bitmap mBmp;
    private Bitmap logoBmp;
    private Bitmap mBitmap1;

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.recom_wards));
        /**
         * 获取邀请码
         */

        SSQSApplication.apiClient(classGuid).getInviteCode(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    InviteCodeBean bean = (InviteCodeBean) result.getData();

                    if (bean != null) {
                        processData(bean);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(RecomWardsActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(RecomWardsActivity.this, result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processData(InviteCodeBean bean) {
        try {
            create2Code(bean.shareUrl);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        mBean = bean;
        recomwardsfriendnum.setText(String.valueOf(bean.count));
        recomwardsincoomenum.setText(String.valueOf(bean.banlance));
        recomwardsinvitecode.setText(bean.code);
    }


    private void showShare(ShareBean data) {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        // 参考代码配置章节，设置分享参数
        // 构造一个图标
        Bitmap enableLogo = BitmapFactory.decodeResource(this.getResources(), R.mipmap.share_copy);
        String label = "复制链接";
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                TmtUtils.midToast(RecomWardsActivity.this, "复制链接成功!", 0);
                int sdkInt = Build.VERSION.SDK_INT;
                if (sdkInt > Build.VERSION_CODES.HONEYCOMB) {// api11
                    ClipboardManager copy = (ClipboardManager) RecomWardsActivity.this
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    if (mBean != null)
                        copy.setText(mBean.shareUrl);
                } else if (sdkInt <= Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager copyq = (android.text.ClipboardManager) RecomWardsActivity.this
                            .getSystemService(Context.CLIPBOARD_SERVICE);
                    if (!TextUtils.isEmpty(mBean.shareUrl))
                        copyq.setText(mBean.shareUrl);
                }
            }
        };
        oks.setCustomerLogo(enableLogo, label, listener);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("分享邀请码拿奖励");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(mBean.shareUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(mBean.shareContent);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setImageUrl(mBean.shareUrl);
        oks.setUrl(mBean.shareUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(mBean.shareContent);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("分享邀请码拿奖励");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(mBean.shareUrl);
        // 启动分享GUI
        oks.show(this);
    }

    @OnClick({R.id.top_back, R.id.recom_wards_share})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.recom_wards_share:
                SSQSApplication.apiClient(classGuid).getShareInfo(new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            ShareBean bean = (ShareBean) result.getData();

                            if (bean != null) {
                                showShare(bean);
                            }
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(RecomWardsActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                TmtUtils.midToast(RecomWardsActivity.this, result.getMessage(), 0);
                            }
                        }
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.recom_wards;
    }

    /**
     * 方法说明：生成有图片二维码
     *
     * @param s
     * @throws WriterException
     */
    public void create2Code(String s) throws WriterException {
        String content = s;
        mBmp = createTwoCode(content, 500);
        logoBmp = small(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mBitmap = Bitmap.createBitmap(mBmp.getWidth(), mBmp.getHeight(), mBmp.getConfig());
        Canvas canvas = new Canvas(mBitmap);
        canvas.drawBitmap(mBmp, 0, 0, null);
        canvas.drawBitmap(logoBmp, mBmp.getWidth() / 2 - logoBmp.getWidth() / 2, mBmp.getHeight() / 2 - logoBmp.getHeight() / 2, null);
        recomwardsqrcodeiv.setImageBitmap(mBitmap);
    }

    /**
     * 方法说明：生成无图片二维码
     */
    @SuppressWarnings("unused")
    private Bitmap createTwoCode(String content) throws WriterException {
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 生成二维码
     */
    @SuppressWarnings("unused")
    private Bitmap createTwoCode(String str, int widthAndHeight) throws WriterException {
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }
        mBitmap1 = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mBitmap1.setPixels(pixels, 0, width, 0, 0, width, height);
        return mBitmap1;
    }

    /**
     * 方法说明：缩小Bitmap
     */
    @SuppressWarnings("unused")
    private static Bitmap small(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(1f, 1f);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizeBmp;
    }

    @Override
    public void reDisPlay() {
        super.reDisPlay();
        initData();
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        ShareSDK.stopSDK(this);
        if (mBitmap != null)
            mBitmap.recycle();
        if (mBmp != null)
            mBmp.recycle();
        if (logoBmp != null)
            logoBmp.recycle();
        if (mBitmap1 != null)
            mBitmap1.recycle();
    }

}
