package com.dading.ssqs.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.FeedBackElement;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.FileUpResultBean;
import com.dading.ssqs.utils.EmojiFilter;
import com.dading.ssqs.utils.FileImageUpload;
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
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.MediaType;

/**
 * 创建者     ZCL
 * 创建时间   2016/8/17 18:05
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SuggestionActivity extends BaseActivity implements View.OnClickListener {


    private static final String TAG = "SuggestionActivity";
    @Bind(R.id.suggestion_upload_edit)
    EditText mSuggestionUploadEdit;
    @Bind(R.id.suggestion_upload_confirm)
    TextView mSuggestionUploadConfirm;
    @Bind(R.id.suggestion_upload_iv1)
    ImageView mSuggestionUploadIv1;
    @Bind(R.id.suggestion_upload_iv2)
    ImageView mSuggestionUploadIv2;
    @Bind(R.id.suggestion_upload_iv3)
    ImageView mSuggestionUploadIv3;
    @Bind(R.id.suggestion_upload_iv4)
    ImageView mSuggestionUploadIv4;
    @Bind(R.id.suggestion_upload_iv5)
    ImageView mSuggestionUploadIv5;
    @Bind(R.id.top_title)
    TextView mTopTitle;

    public static final int NONE = 0;
    public static final int PHOTO_CAMERA = 1;// 相机拍照
    public static final int PHOTO_COMPILE = 2; // 编辑图片
    public static final int PHOTO_RESOULT = 3;// 结果

    /* 头像名称 */
    private PopupWindow mPopup;
    private TextView mCancle;
    private TextView mCarema;
    private TextView mGallery;
    private RelativeLayout mLy;
    private View mView;
    private ArrayList<ImageView> mListT;
    private ArrayList<ImageView> mListB;
    private HashMap<ImageView, String> mListURL;
    private String ImageName;
    private String mS;
    private boolean mIsCompressSuccess;
    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private Call.Factory mOkHttpClient;
    private String mUrl;
    private Bitmap mImage;

    @Override
    protected void initView() {
        mView = View.inflate(this, R.layout.activity_suggestion, null);

        View view = View.inflate(this, R.layout.popu_carema_gallery, null);
        mCancle = (TextView) view.findViewById(R.id.popu_sugestion_cancle);
        mCarema = (TextView) view.findViewById(R.id.popu_sugestion_carema);
        mGallery = (TextView) view.findViewById(R.id.popu_sugestion_gallery);
        mLy = (RelativeLayout) view.findViewById(R.id.popu_carema_gallery_ly);

        mPopup = PopUtil.popuMake(view);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_suggestion;
    }

    @Override
    protected void setUnDe() {
        super.setUnDe();
        //回收图片，清理内存
        if (mImage != null && !mImage.isRecycled()) {
            mImage.recycle();
        }
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.suggestion_upload));
        mUrl = SSQSApplication.apiClient(classGuid).getBaseUri() + "/v1.0/feedback/uploadImage";

        mListURL = new HashMap<>();

        mSuggestionUploadIv1.setVisibility(View.GONE);
        mSuggestionUploadIv2.setVisibility(View.GONE);
        mSuggestionUploadIv3.setVisibility(View.GONE);
        mSuggestionUploadIv4.setVisibility(View.GONE);

        mListB = new ArrayList<>();
        mListB.add(mSuggestionUploadIv1);
        mListB.add(mSuggestionUploadIv2);
        mListB.add(mSuggestionUploadIv3);
        mListB.add(mSuggestionUploadIv4);
        mListT = new ArrayList<>();
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
                mSuggestionUploadIv5.setClickable(false);
            }
        });
        mCancle.setOnClickListener(this);
        mCarema.setOnClickListener(this);
        mGallery.setOnClickListener(this);
        mLy.setOnClickListener(this);
        mSuggestionUploadEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!mSuggestionUploadConfirm.getText().equals("")) {
                    mSuggestionUploadConfirm.setClickable(true);
                    mSuggestionUploadConfirm.setBackgroundColor(getResources().getColor(R.color.orange));
                } else {
                    mSuggestionUploadConfirm.setClickable(false);
                    mSuggestionUploadConfirm.setBackgroundColor(getResources().getColor(R.color.gray_c));
                }
            }
        });

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
                if (ContextCompat.checkSelfPermission(SuggestionActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    TmtUtils.midToast(SuggestionActivity.this, "没有授权,请到应用设置界面手动打开相机权限!", 0);
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


    @OnClick({R.id.top_back, R.id.suggestion_upload_confirm, R.id.suggestion_upload_iv1,
            R.id.suggestion_upload_iv2, R.id.suggestion_upload_iv3, R.id.suggestion_upload_iv4,
            R.id.suggestion_upload_iv5
    })
    public void OnClik(View v) {
        switch (v.getId()) {
            case R.id.suggestion_upload_iv1:
                UIUtils.hideKeyBord(this);
                mSuggestionUploadIv1.setVisibility(View.GONE);
                mListT.remove(mSuggestionUploadIv1);
                mListB.add(mSuggestionUploadIv1);
                mListURL.remove(mSuggestionUploadIv1);
                Logger.d(TAG, mListT.toString());
                break;
            case R.id.suggestion_upload_iv2:
                UIUtils.hideKeyBord(this);
                mSuggestionUploadIv2.setVisibility(View.GONE);
                mListT.remove(mSuggestionUploadIv2);
                mListB.add(mSuggestionUploadIv2);
                mListURL.remove(mSuggestionUploadIv2);
                Logger.d(TAG, mListT.toString());
                break;
            case R.id.suggestion_upload_iv3:
                UIUtils.hideKeyBord(this);
                mSuggestionUploadIv3.setVisibility(View.GONE);
                mListT.remove(mSuggestionUploadIv3);
                mListB.add(mSuggestionUploadIv3);
                mListURL.remove(mSuggestionUploadIv3);
                Logger.d(TAG, mListT.toString());
                break;
            case R.id.suggestion_upload_iv4:
                UIUtils.hideKeyBord(this);
                mSuggestionUploadIv4.setVisibility(View.GONE);
                mListT.remove(mSuggestionUploadIv4);
                Logger.d(TAG, mListT.toString());
                mListB.add(mSuggestionUploadIv4);
                mListURL.remove(mSuggestionUploadIv4);
                break;
            case R.id.suggestion_upload_iv5:
                UIUtils.hideKeyBord(this);
                mPopup.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
                mSuggestionUploadIv5.setClickable(false);
                break;
            case R.id.top_back:
                UIUtils.hideKeyBord(this);
                finish();
                break;
            case R.id.suggestion_upload_confirm:
                /**
                 a)	请求地址：
                 /v1.0/feedback
                 b)	请求方式:
                 Post
                 c)	请求参数说明：
                 suggest:建议内容 
                 auth_token：登陆后加入请求头
                 */
                String arr = "[";

                Set<ImageView> iVs = mListURL.keySet();
                for (ImageView i : iVs) {
                    arr += mListURL.get(i) + ",";
                }

                arr = arr.substring(0, arr.length() - 1) + "]";

                String value = mSuggestionUploadEdit.getText().toString();
                if (value.length() >= 13 && value.length() > 400) {
                    TmtUtils.midToast(SuggestionActivity.this, "请检查字数!字数不得少于13,大于400", 0);
                    return;
                }
                String valueOk = EmojiFilter.filterEmoji(value);

                FeedBackElement element = new FeedBackElement();
                element.setSuggest(valueOk);
                element.setImageUrl(arr);

                SSQSApplication.apiClient(classGuid).feedback(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            mSuggestionUploadEdit.setText("");
                            mSuggestionUploadIv1.setVisibility(View.GONE);
                            mSuggestionUploadIv2.setVisibility(View.GONE);
                            mSuggestionUploadIv3.setVisibility(View.GONE);
                            mSuggestionUploadIv4.setVisibility(View.GONE);
                            mSuggestionUploadIv5.setVisibility(View.VISIBLE);
                            TmtUtils.midToast(SuggestionActivity.this, "意见反馈成功!", 0);
                            finish();
                        } else {
                            mSuggestionUploadConfirm.setClickable(true);
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(SuggestionActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                            }
                        }
                    }
                });
                break;
        }
        if (mListT.size() < 4) {
            mSuggestionUploadIv5.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == NONE)
            return;
        // 通过照相机拍照的图片出理
        if (requestCode == PHOTO_CAMERA) {
            // 设置文件保存路径这里放在跟目录下
            final File picture = new File(Environment.getExternalStorageDirectory() + ImageName);
            Logger.d(TAG, "相机path是------------------------------:" + picture.getPath());
            if (mListB.size() <= 0) {
                mSuggestionUploadIv5.setVisibility(View.GONE);
            } else {
                mSuggestionUploadIv5.setVisibility(View.VISIBLE);
            }
            runUp(compressImageFile(picture.getPath()));
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
            File file1 = compressImageFile(picPath);
            File file = new File(picPath);
            if (file1 != null) {
                runUp(file1);
                // postAsynFile(file1);
            } else {
                //postAsynFile(file);
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
        String newPath = SuggestionActivity.this.getCacheDir().getAbsolutePath() + "/topicImages.jpg";
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
                 a)	请求地址：
                 /v1.0/feedback/uploadImage
                 b)	请求方式:
                 Post
                 */
                mS = FileImageUpload.uploadFile(picture, mUrl, UIUtils.getSputils().getString(Constent.TOKEN, null));
                Logger.d(TAG, "上传图片返回数据是------------------------------:" + mS);
                if (!"FAIL".equals(mS)) {
                    FileUpResultBean bean = JSON.parseObject(mS, FileUpResultBean.class);
                    if (bean.status && bean.data != null && bean.data.imageUrl != null) {
                        goImg(bean.data.imageUrl.get(0));
                    }
                } else {
                    TmtUtils.midToast(SuggestionActivity.this, "上传图片失败,请检查图片!", 0);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
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
                    if (mListURL.size() >= 4) {
                        mSuggestionUploadIv5.setVisibility(View.GONE);
                    }
                    imageView.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}
