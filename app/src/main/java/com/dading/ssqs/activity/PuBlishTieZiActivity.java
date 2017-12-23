package com.dading.ssqs.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.TopicElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.FileUpResultBean;
import com.dading.ssqs.utils.EmojiFilter;
import com.dading.ssqs.utils.FileImageUpload;
import com.dading.ssqs.utils.LocationUtils;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.TmtUtils;
import com.dading.ssqs.utils.UIUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/16 9:30
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class PuBlishTieZiActivity extends BaseActivity implements View.OnClickListener {
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private static final String TAG = "PuBlishTieZiActivity";
    @Bind(R.id.publish_note_title)
    EditText mPublishNoteTitle;
    @Bind(R.id.publish_note_context)
    EditText mPublishNoteContext;
    @Bind(R.id.publish_note_image)
    ImageView mPublishNoteImage;
    @Bind(R.id.publish_note_location)
    ImageView mPublishNoteLocation;
    @Bind(R.id.publish_note_iv1)
    ImageView mPublishNoteIv1;
    @Bind(R.id.publish_note_iv2)
    ImageView mPublishNoteIv2;
    @Bind(R.id.publish_note_iv3)
    ImageView mPublishNoteIv3;
    @Bind(R.id.publish_note_iv4)
    ImageView mPublishNoteIv4;
    @Bind(R.id.publish_note_iv5)
    ImageView mPublishNoteIv5;
    @Bind(R.id.publish_note_city)
    TextView mPublishNoteCity;
    @Bind(R.id.publish_note_tag)
    LinearLayout mPublishNoteTag;

    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.top_icon)
    ImageView mTopIcon;

    private PopupWindow mPopup;
    private TextView mCancle;
    private TextView mCarema;
    private TextView mGallery;
    private RelativeLayout mLy;
    private ArrayList<ImageView> mListB;
    private ArrayList<ImageView> mListT;
    private View mView;
    private String ImageName;
    public static final int NONE = 0;
    public static final int PHOTO_CAMERA = 1;// 相机拍照
    public static final int PHOTO_COMPILE = 2; // 编辑图片
    public static final int PHOTO_RESOULT = 3;// 结果
    private String mS;
    private HashMap<ImageView, String> mListURL;
    private String mToken;
    private int mId;
    private boolean mIsCompressSuccess;
    private Bitmap mImage;

    @Override
    protected void setUnDe() {
        //回收图片，清理内存
        if (mImage != null && !mImage.isRecycled()) {
            mImage.recycle();
        }
    }

    @Override
    protected void initView() {
        mView = View.inflate(this, R.layout.activity_publish_note, null);
        View photoView = View.inflate(this, R.layout.popu_carema_gallery, null);
        mCancle = (TextView) photoView.findViewById(R.id.popu_sugestion_cancle);
        mCarema = (TextView) photoView.findViewById(R.id.popu_sugestion_carema);
        mGallery = (TextView) photoView.findViewById(R.id.popu_sugestion_gallery);
        mLy = (RelativeLayout) photoView.findViewById(R.id.popu_carema_gallery_ly);
        mPopup = PopUtil.popuMake(photoView);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_publish_note;
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.publish_note));
        mTopIcon.setImageResource(R.mipmap.t_announce);
        mTopIcon.setVisibility(View.VISIBLE);

        mListURL = new HashMap<>();
        Intent intent = getIntent();
        mId = intent.getIntExtra(Constent.ALL_CIRCLE_ID, 0);

        mPublishNoteIv1.setVisibility(View.GONE);
        mPublishNoteIv2.setVisibility(View.GONE);
        mPublishNoteIv3.setVisibility(View.GONE);
        mPublishNoteIv4.setVisibility(View.GONE);

        mListB = new ArrayList<>();
        mListB.add(mPublishNoteIv1);
        mListB.add(mPublishNoteIv2);
        mListB.add(mPublishNoteIv3);
        mListB.add(mPublishNoteIv4);
        mListT = new ArrayList<>();
        mPublishNoteTag.setVisibility(View.GONE);
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
                mPublishNoteIv5.setClickable(true);
            }
        });
        mCancle.setOnClickListener(this);
        mCarema.setOnClickListener(this);
        mGallery.setOnClickListener(this);
        mLy.setOnClickListener(this);
        mPublishNoteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @OnClick({R.id.top_back, R.id.top_icon, R.id.publish_note_image, R.id.publish_note_location,
            R.id.publish_note_iv1, R.id.publish_note_iv2, R.id.publish_note_city_close,
            R.id.publish_note_iv3, R.id.publish_note_iv4, R.id.publish_note_iv5})
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.top_icon:
                String content = mPublishNoteContext.getText().toString();
                String contentOk = EmojiFilter.filterEmoji(content);
                if (contentOk.length() >= 5) {
                    if (UIUtils.getSputils().getBoolean(Constent.LOADING_BROCAST_TAG, false)) {
                        Set<ImageView> iVs = mListURL.keySet();

                        String arr = "[";
                        for (ImageView i : iVs) {
                            arr += mListURL.get(i) + ",";
                        }
                        arr = arr.substring(0, arr.length() - 1) + "]";

                        String value = mPublishNoteTitle.getText().toString();
                        String valueOk = EmojiFilter.filterEmoji(value);
                        if (TextUtils.isEmpty(valueOk)) {
                            TmtUtils.midToast(PuBlishTieZiActivity.this, "请输入标题!", 0);
                            return;
                        } else if (valueOk.length() > 56) {
                            TmtUtils.midToast(PuBlishTieZiActivity.this, "标题长度不得大于五十六!", 0);
                            return;
                        }

                        /**
                         *12.发帖
                         a)	请求地址：/v1.0/article/save
                         b)	请求方式:post
                         c)	请求参数说明：title:标题  content:内容  categoryID:文章类别ID, imageUrl:图片数组
                         */
                        TopicElement element = new TopicElement();
                        element.setTitle(valueOk);
                        element.setContent(contentOk);
                        element.setCategoryID(String.valueOf(mId));
                        element.setImageUrl(arr);

                        SSQSApplication.apiClient(classGuid).createTopic(element, new CcApiClient.OnCcListener() {
                            @Override
                            public void onResponse(CcApiResult result) {
                                if (result.isOk()) {
                                    mPublishNoteTitle.setText("");
                                    mPublishNoteContext.setText("");
                                    mPublishNoteIv1.setVisibility(View.GONE);
                                    mPublishNoteIv2.setVisibility(View.GONE);
                                    mPublishNoteIv3.setVisibility(View.GONE);
                                    mPublishNoteIv4.setVisibility(View.GONE);
                                    mPublishNoteIv5.setVisibility(View.VISIBLE);

                                    TmtUtils.midToast(PuBlishTieZiActivity.this, "发帖成功!", 0);

                                    UIUtils.SendReRecevice(Constent.TZ_SUC);

                                    finish();
                                } else {
                                    if (403 == result.getErrno()) {
                                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                        Intent intent = new Intent(PuBlishTieZiActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                                    }
                                }
                            }
                        });
                    } else {
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    }
                    Logger.d(TAG, "已经提交------------------------------:");
                } else {
                    TmtUtils.midToast(PuBlishTieZiActivity.this, "文章内容不得少于五字!", 0);
                    return;
                }
                break;
            case R.id.publish_note_image:
                Logger.d(TAG, "显示相机------------------------------:");
                break;
            case R.id.publish_note_location:
                LocationUtils.getCNBylocation(this);
                Logger.d(TAG, "显示城市------------------------------:");
                mPublishNoteTag.setVisibility(View.VISIBLE);
                mPublishNoteCity.setText(LocationUtils.cityName);
                break;
            case R.id.publish_note_city_close:
                mPublishNoteTag.setVisibility(View.GONE);
                mPublishNoteCity.setText("");
                break;
            case R.id.publish_note_iv1:
                UIUtils.hideKeyBord(this);
                mPublishNoteIv1.setVisibility(View.GONE);
                mListT.remove(mPublishNoteIv1);
                mListB.add(mPublishNoteIv1);
                mListURL.remove(mPublishNoteIv1);
                Logger.d(TAG, mListT.toString());
                break;
            case R.id.publish_note_iv2:
                UIUtils.hideKeyBord(this);
                mPublishNoteIv2.setVisibility(View.GONE);
                mListT.remove(mPublishNoteIv2);
                mListB.add(mPublishNoteIv2);
                mListURL.remove(mPublishNoteIv2);
                Logger.d(TAG, mListT.toString());
                break;
            case R.id.publish_note_iv3:
                UIUtils.hideKeyBord(this);
                mPublishNoteIv3.setVisibility(View.GONE);
                mListT.remove(mPublishNoteIv3);
                mListB.add(mPublishNoteIv3);
                mListURL.remove(mPublishNoteIv3);
                Logger.d(TAG, mListT.toString());
                break;
           /* case R.id.publish_note_iv4:
                mPublishNoteIv4.setVisibility(View.GONE);
                mListT.remove(mPublishNoteIv4);
                Logger.d(TAG, mListT.toString());
                mListB.add(mPublishNoteIv4);
                mListURL.remove(mPublishNoteIv4);
                break;*/
            case R.id.publish_note_iv5:
                UIUtils.hideKeyBord(this);
                Logger.d(TAG, "显示相机------------------------------:");
                mPopup.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
                mPublishNoteIv5.setClickable(false);
                break;
            default:
                break;
        }
        if (mListURL.size() < 3) {
            mPublishNoteIv5.setVisibility(View.VISIBLE);
        }
        Logger.d(TAG, "上传的图数是------------------------------:" + mListURL.size());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == NONE)
            return;

        // 通过照相机拍照的图片出理
        if (requestCode == PHOTO_CAMERA) {
            // 设置文件保存路径这里放在跟目录下
            File file = new File(Environment.getExternalStorageDirectory() + ImageName);
            Logger.d(TAG, "相机path是------------------------------:" + file.getPath());
            if (mListB.size() <= 0) {
                mPublishNoteIv5.setVisibility(View.GONE);
            } else {
                mPublishNoteIv5.setVisibility(View.VISIBLE);
            }
            File file1 = compressImageFile(file.getPath());
            if (mIsCompressSuccess) {
                runUp(file1);
            } else {
                runUp(file);
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
            Logger.d(TAG, "图库图片地址是------------------------------:" + picPath);
            File file = new File(picPath);
            File file1 = compressImageFile(picPath);
            if (mIsCompressSuccess) {
                runUp(file1);
            } else {
                runUp(file);
            }
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
        //显示在本地
        //        mImageView.setImageBitmap(image);
        try {
            if (is != null)
                is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //定义一个file，为压缩后的图片   File f = new File("图片保存路径","图片名称");
        String newPath = PuBlishTieZiActivity.this.getCacheDir().getAbsolutePath() + "/topicImages.jpg";
        File file = new File(newPath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            Logger.d(TAG, e.getMessage());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        mImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);//这里100表示不压缩，将不压缩的数据存放到baos中
        int per = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            mImage.compress(Bitmap.CompressFormat.JPEG, per, baos);// 将图片压缩为原来的(100-per)%，把压缩后的数据存放到baos中
            per -= 10;// 每次都减少10
        }

        //将输出流写入到新文件
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
            baos.close();
            //标记压缩图片成功
            mIsCompressSuccess = true;
        } catch (Exception e) {
            Logger.d(TAG, e.getMessage());
        }
        return file;
    }

    private void runUp(final File picture) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                /**
                 e)请求地址：/v1.0/article/uploadImage
                 f)	请求方式:post
                 g)	请求参数说明：
                 图片:二进制数据
                 */
                String url = SSQSApplication.apiClient(classGuid).getBaseUri() + "/v1.0/article/uploadImage";
                mS = FileImageUpload.uploadFile(picture, url, UIUtils.getSputils().getString(Constent.TOKEN, null));
                Logger.d(TAG, "上传图片返回数据是------------------------------:" + mS);
                if (!"FAIL".equals(mS)) {
                    FileUpResultBean bean = JSON.parseObject(mS, FileUpResultBean.class);
                    if (bean.status && bean.data != null && bean.data.imageUrl != null) {
                        goImg(bean.data.imageUrl.get(0));
                    }
                } else {
                    TmtUtils.midToast(PuBlishTieZiActivity.this, "更新头像失败,请检查图片!", 0);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void goImg(final String data) {
        if (!TextUtils.isEmpty(data)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ImageView imageView = mListB.get(0);
                    SSQSApplication.glide.load(data).error(R.mipmap.fail).centerCrop().into(imageView);

                    mListT.add(imageView);
                    mListB.remove(imageView);
                    mListURL.put(imageView, data);
                    if (mListURL.size() >= 3) {
                        mPublishNoteIv5.setVisibility(View.GONE);
                    }
                    imageView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            return;
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.popu_sugestion_cancle:
            case R.id.popu_carema_gallery_ly:
                mPopup.dismiss();
                break;
            case R.id.popu_sugestion_carema:
                //设置图片的名称
                ImageName = "/" + getStringToday() + "ft.jpg";
                // 设置调用系统摄像头的意图(隐式意图)
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //设置照片的输出路径和文件名
                File file = new File(Environment.getExternalStorageDirectory(), ImageName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                //开启摄像头
                if (ContextCompat.checkSelfPermission(PuBlishTieZiActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    TmtUtils.midToast(PuBlishTieZiActivity.this, "没有授权,请到应用设置界面手动打开相机权限!", 0);
                } else {
                    // 已经权限
                    startActivityForResult(intent, PHOTO_CAMERA);
                    Log.d("111", "已经授权");
                }
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

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
