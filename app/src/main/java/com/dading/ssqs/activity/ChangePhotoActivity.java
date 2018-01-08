package com.dading.ssqs.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.UserElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.ImgResultBean;
import com.dading.ssqs.bean.LoadingBean;
import com.dading.ssqs.utils.FileImageUpload;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ThreadPoolUtils;
import com.dading.ssqs.utils.ToastUtils;
import com.dading.ssqs.utils.UIUtils;
import com.dading.ssqs.components.GlideCircleTransform;
import com.dading.ssqs.R;
import com.google.gson.Gson;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;


/**
 * 创建者     ZCL
 * 创建时间   2016/9/23 10:50
 * 描述	      更改图片
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ChangePhotoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ChangePhotoActivity";

    private static final int RESULT_OK_SIGNATURE = 22;
    private static final int RESULT_OK_NICKNAME = 11;
    @Bind(R.id.change_photo_img)
    ImageView mChangePhotoImg;
    @Bind(R.id.change_photo_nickname)
    TextView mChangePhotoNickname;
    @Bind(R.id.change_photo_sign_text)
    TextView mChangePhotoSignText;
    @Bind(R.id.change_photo_man_iv)
    ImageView mChangePhotoManIv;
    @Bind(R.id.change_photo_man_tv)
    TextView mChangePhotoManTv;
    @Bind(R.id.change_photo_woman_iv)
    ImageView mChangePhotoWomanIv;
    @Bind(R.id.change_photo_woman_tv)
    TextView mChangePhotoWomanTv;
    @Bind(R.id.change_photo_guess_iv)
    ImageView mChangePhotoGuessIv;
    @Bind(R.id.change_photo_guess_tv)
    TextView mChangePhotoGuessTv;
    @Bind(R.id.change_photo_lv_go)
    ImageView mChangePhotoLvGo;
    @Bind(R.id.change_photo_lv_num)
    TextView mChangePhotoLvNum;
    @Bind(R.id.change_photo_lv_count)
    TextView mChangePhotoLvCount;
    @Bind(R.id.change_photo_photo)
    RelativeLayout mChangePhotoPhoto;
    private PopupWindow mPopup;
    private TextView mCancle;
    private TextView mCarema;
    private TextView mGallery;
    private RelativeLayout mLy;

    public static final int NONE = 0;
    public static final int PHOTO_CAMERA = 1;// 相机拍照
    public static final int PHOTO_COMPILE = 2; // 编辑图片
    public static final int PHOTO_RESOULT = 3;// 结果
    private View mView;
    private LoadingBean mBean;
    private String mToken;
    private String mNickName;
    private String mSignature;
    private int mSex;
    private String ImageName;
    private String mS;
    private String mUrl;
    private Bitmap mImage;

    @Override
    protected void setUnDe() {
        super.setUnDe();
        if (mImage != null && !mImage.isRecycled()) {
            mImage.recycle();
        }
    }

    @Override
    protected void initView() {
        mView = View.inflate(this, R.layout.activity_change_photo, null);

        View view = View.inflate(this, R.layout.popu_carema_gallery, null);
        mCancle = (TextView) view.findViewById(R.id.popu_sugestion_cancle);
        mCarema = (TextView) view.findViewById(R.id.popu_sugestion_carema);
        mGallery = (TextView) view.findViewById(R.id.popu_sugestion_gallery);
        mLy = (RelativeLayout) view.findViewById(R.id.popu_carema_gallery_ly);
        mPopup = PopUtil.popuMake(view);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_change_photo;
    }

    @Override
    protected void initData() {
        mUrl = "http://13.124.189.54:8092/image-service/rest/v1.0/user/image";
        mToken = UIUtils.getSputils().getString(Constent.TOKEN, null);
        Intent intent = getIntent();
        String info = intent.getStringExtra(Constent.MY_INFO);
        mBean = JSON.parseObject(info, LoadingBean.class);
        if (mBean != null) {
            setInfo();
        }
    }

    private void setInfo() {
        mChangePhotoNickname.setText(mBean.username);
        mChangePhotoSignText.setText(mBean.signature);
        String text = mBean.points + "积分";
        String lv = "LV" + mBean.level;
        mChangePhotoLvNum.setText(lv);
        mChangePhotoLvCount.setText(text);
        if (mBean != null) {
            SSQSApplication.glide.load(mBean.avatar).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(this)).into(mChangePhotoImg);
        } else {
            switch (mBean.sex) {
                case 1:
                    mChangePhotoImg.setImageResource(R.mipmap.touxiang_nan);
                    break;
                case 2:
                    mChangePhotoImg.setImageResource(R.mipmap.touxiang_nv);
                    break;
                case 3:
                    mChangePhotoImg.setImageResource(R.mipmap.touxiang_baomi);
                    break;
                default:
                    break;
            }
        }

        switch (mBean.sex) {
            case 1:
                mChangePhotoManIv.setImageResource(R.mipmap.ic_my_male);
                mChangePhotoManTv.setTextColor(this.getResources().getColor(R.color.blue_t1));
                mChangePhotoWomanIv.setImageResource(R.mipmap.ic_female_gray);
                mChangePhotoWomanTv.setTextColor(this.getResources().getColor(R.color.gray6));
                mChangePhotoGuessIv.setImageResource(R.mipmap.ic_other_gray);
                mChangePhotoGuessTv.setTextColor(this.getResources().getColor(R.color.gray6));
                break;
            case 2:
                mChangePhotoManIv.setImageResource(R.mipmap.ic_male_gray);
                mChangePhotoManTv.setTextColor(this.getResources().getColor(R.color.gray6));
                mChangePhotoWomanIv.setImageResource(R.mipmap.ic_my_female);
                mChangePhotoWomanTv.setTextColor(this.getResources().getColor(R.color.red_l1));
                mChangePhotoGuessIv.setImageResource(R.mipmap.ic_other_gray);
                mChangePhotoGuessTv.setTextColor(this.getResources().getColor(R.color.gray6));
                break;
            case 3:
                mChangePhotoManIv.setImageResource(R.mipmap.ic_male_gray);
                mChangePhotoManTv.setTextColor(this.getResources().getColor(R.color.gray6));
                mChangePhotoWomanIv.setImageResource(R.mipmap.ic_female_gray);
                mChangePhotoWomanTv.setTextColor(this.getResources().getColor(R.color.gray6));
                mChangePhotoGuessIv.setImageResource(R.mipmap.ic_my_other);
                mChangePhotoGuessTv.setTextColor(this.getResources().getColor(R.color.PURSE));
                break;
            default:
                break;
        }
    }

    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    @Override
    protected void initListener() {
        mPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mChangePhotoPhoto.setClickable(true);
            }
        });
        mCancle.setOnClickListener(this);
        mCarema.setOnClickListener(this);
        mGallery.setOnClickListener(this);
        mLy.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.popu_sugestion_cancle:
                mPopup.dismiss();
                break;
            case R.id.popu_sugestion_carema:
                //设置图片的名称
                ImageName = "/" + getStringToday() + ".jpg";
                // 设置调用系统摄像头的意图(隐式意图)
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //设置照片的输出路径和文件名
                File file = new File(Environment.getExternalStorageDirectory(), ImageName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                //开启摄像头
                startActivityForResult(intent, PHOTO_CAMERA);
                mPopup.dismiss();

                break;
            case R.id.popu_sugestion_gallery:
                // 设置调用系统相册的意图(隐式意图)
                intent = new Intent();
                //设置值活动//android.intent.action.PICK
                intent.setAction(Intent.ACTION_PICK);
                //设置类型和数据
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                // 开启系统的相册
                startActivityForResult(intent, PHOTO_COMPILE);
                mPopup.dismiss();
                break;
            case R.id.popu_carema_gallery_ly:
                mPopup.dismiss();
                break;
            default:
                break;
        }
    }


    @OnClick({R.id.change_photo_return, R.id.change_photo_save, R.id.change_photo_photo, R.id.change_photo_lv_ly, R.id.change_photo_nickname,
            R.id.change_photo_sign_text, R.id.change_photo_man_iv, R.id.change_photo_man_tv, R.id.change_photo_woman_iv, R.id.change_photo_woman_tv
            , R.id.change_photo_guess_iv, R.id.change_photo_guess_tv})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.change_photo_return:
                finish();
                break;
            case R.id.change_photo_save:
                UserElement element = new UserElement();
                element.setSignature(mSignature);
                element.setSex(String.valueOf(mSex));
                if (mBean.isEdit == 0) {
                    element.setUsername(mNickName);
                }

                SSQSApplication.apiClient(classGuid).updateUserInfo(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            goSp();
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(ChangePhotoActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                ToastUtils.midToast(ChangePhotoActivity.this, result.getMessage(), 0);
                            }
                        }
                    }
                });
                break;
            case R.id.change_photo_nickname:
                if (mBean.isEdit == 1) {
                    return;
                }
                Intent intent = new Intent(this, ChangePhotoNicknameActivity.class);
                intent.putExtra(Constent.NICK_NAME, mBean.username);
                startActivityForResult(intent, RESULT_OK_NICKNAME);
                break;
            case R.id.change_photo_sign_text:
                Intent intentSign = new Intent(this, ChangePhotoSigntextActivity.class);
                intentSign.putExtra(Constent.SIGN_TEXT, mBean.signature);
                startActivityForResult(intentSign, RESULT_OK_SIGNATURE);
                break;
            case R.id.change_photo_photo:
                mPopup.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
                mChangePhotoPhoto.setClickable(false);
                break;

            case R.id.change_photo_man_iv:
            case R.id.change_photo_man_tv:
                mChangePhotoManIv.setImageResource(R.mipmap.ic_my_male);
                mChangePhotoManTv.setTextColor(this.getResources().getColor(R.color.blue_t1));
                mChangePhotoWomanIv.setImageResource(R.mipmap.ic_female_gray);
                mChangePhotoWomanTv.setTextColor(this.getResources().getColor(R.color.gray6));
                mChangePhotoGuessIv.setImageResource(R.mipmap.ic_other_gray);
                mChangePhotoGuessTv.setTextColor(this.getResources().getColor(R.color.gray6));
                mSex = 1;
                break;
            case R.id.change_photo_woman_iv:
            case R.id.change_photo_woman_tv:
                mChangePhotoManIv.setImageResource(R.mipmap.ic_male_gray);
                mChangePhotoManTv.setTextColor(this.getResources().getColor(R.color.gray6));
                mChangePhotoWomanIv.setImageResource(R.mipmap.ic_my_female);
                mChangePhotoWomanTv.setTextColor(this.getResources().getColor(R.color.red_l1));
                mChangePhotoGuessIv.setImageResource(R.mipmap.ic_other_gray);
                mChangePhotoGuessTv.setTextColor(this.getResources().getColor(R.color.gray6));
                mSex = 2;
                break;
            case R.id.change_photo_guess_iv:
            case R.id.change_photo_guess_tv:
                mChangePhotoManIv.setImageResource(R.mipmap.ic_male_gray);
                mChangePhotoManTv.setTextColor(this.getResources().getColor(R.color.gray6));
                mChangePhotoWomanIv.setImageResource(R.mipmap.ic_female_gray);
                mChangePhotoWomanTv.setTextColor(this.getResources().getColor(R.color.gray6));
                mChangePhotoGuessIv.setImageResource(R.mipmap.ic_my_other);
                mChangePhotoGuessTv.setTextColor(this.getResources().getColor(R.color.PURSE));
                mSex = 3;
                break;
            case R.id.change_photo_lv_ly:
                Intent intentLV = new Intent(this, LevelActivity.class);
                startActivity(intentLV);
                break;
            default:
                break;
        }
    }

    private File compressImageFile(String path) {
        //先将所选图片转化为流的形式，path所得到的图片路径
        FileInputStream is = null;
        try {
            is = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int size = 6;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = size;
        //将图片缩小为原来的  1/size ,不然图片很大时会报内存溢出错误
        mImage = BitmapFactory.decodeStream(is, null, options);
        try {
            if (is != null)
                is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //定义一个file，为压缩后的图片   File f = new File("图片保存路径","图片名称");
        String newPath = ChangePhotoActivity.this.getCacheDir().getAbsolutePath() + "/topicImages.jpg";
        File file = new File(newPath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            Logger.INSTANCE.d(TAG, e.getMessage());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);//这里100表示不压缩，将不压缩的数据存放到baos中
        int per = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            mImage.compress(Bitmap.CompressFormat.JPEG, per, baos);// 将图片压缩为原来的(100-per)%，把压缩后的数据存放到baos中
            per -= 10;// 每次都减少10
        }
        Canvas canvas = new Canvas();
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, mImage.getWidth(), mImage.getHeight());//获取图片矩形方阵
        RectF rectR = new RectF(rect);//设置圆形方针
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawOval(rectR, paint);
        //显示在本地
        //mChangePhotoImg.setImageBitmap(mImage);
        //回收图片，清理内存
        //将输出流写入到新文件
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            baos.close();
            //标记压缩图片成功
        } catch (Exception e) {
            Logger.INSTANCE.d(TAG, e.getMessage());
        }
        return file;
    }

    private void goSp() {
        SSQSApplication.apiClient(classGuid).getUserInfo(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    mBean = (LoadingBean) result.getData();

                    Gson gson = new Gson();

                    UIUtils.getSputils().putString(Constent.LOADING_STATE_SP, gson.toJson(mBean, LoadingBean.class));

                    setInfo();
                    //发送广播
                    UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                    finish();
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(ChangePhotoActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtils.midToast(ChangePhotoActivity.this, result.getMessage(), 0);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == NONE)
            return;

        // 通过照相机拍照的图片出理
        if (requestCode == PHOTO_CAMERA) {
            // 设置文件保存路径这里放在跟目录下
            File picture = new File(Environment.getExternalStorageDirectory() + ImageName);
            Logger.INSTANCE.d(TAG, "相机path是------------------------------:" + picture.getPath());
            File file1 = compressImageFile(picture.getPath());
            if (file1 != null) {
                runUp(file1);
            } else {
                runUp(picture);
            }
        }

        if (data == null)
            return;

        // 读取相册裁剪图片
        if (requestCode == PHOTO_COMPILE) {
            //裁剪图片
            Uri selectImag = data.getData();
            String[] filePathColum = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectImag, filePathColum, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColum[0]);
            String picPath = cursor.getString(columnIndex);
            cursor.close();
            File file1 = compressImageFile(picPath);
            Logger.INSTANCE.d(TAG, "图库图片地址是----------:" + picPath + "----file___" + file1.getAbsolutePath());
            if (file1 != null) {
                runUp(file1);
            } else {
                File file = new File(picPath);
                runUp(file);
            }
        }
        //更改昵称
        if (requestCode == RESULT_OK_NICKNAME) {
            mNickName = data.getStringExtra(Constent.NICK_NAME);
            mChangePhotoNickname.setText(mNickName);
            mBean.username = mNickName;
            Logger.INSTANCE.d(TAG, "修改昵称返回数据是------------------------------:" + mNickName);

        }
        //更改签名
        if (requestCode == RESULT_OK_SIGNATURE) {
            mSignature = data.getStringExtra(Constent.SIGN_TEXT);
            mChangePhotoSignText.setText(mSignature);
            mBean.signature = mSignature;
            Logger.INSTANCE.d(TAG, "修改签名返回数据是------------------------------:" + mSignature);
        }
    }

    private void runUp(final File picture) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mS = FileImageUpload.uploadFile(picture, mUrl, mToken);
                UIUtils.getMainThreadHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        String data = null;
                        if (!"FAIL".equals(mS)) {
                            ImgResultBean bean = JSON.parseObject(mS, ImgResultBean.class);
                            data = bean.getData();
                        } else {
                            if (mBean != null) {
                                data = mBean.avatar;
                            }
                            ToastUtils.midToast(ChangePhotoActivity.this, "更新头像失败,请检查图片!", 0);
                        }
                        goImg(data);
                    }
                });
            }
        };
        ThreadPoolUtils.getInstance().addTask(runnable);
    }

    private void goImg(final String data) {
        if (!TextUtils.isEmpty(data)) {
            SSQSApplication.glide.load(data).error(R.mipmap.fail).centerCrop().transform(new GlideCircleTransform(ChangePhotoActivity.this)).into(mChangePhotoImg);
        }
    }
}

