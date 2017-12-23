package com.dading.ssqs.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dading.ssqs.R;
import com.dading.ssqs.SSQSApplication;
import com.dading.ssqs.adapter.SavantAuthPopAdapter;
import com.dading.ssqs.apis.CcApiClient;
import com.dading.ssqs.apis.CcApiResult;
import com.dading.ssqs.apis.elements.ExpertElement;
import com.dading.ssqs.bean.BankBean;
import com.dading.ssqs.bean.Constent;
import com.dading.ssqs.bean.FileUpResultBean;
import com.dading.ssqs.utils.FileImageUpload;
import com.dading.ssqs.utils.Logger;
import com.dading.ssqs.utils.PopUtil;
import com.dading.ssqs.utils.ThreadPoolUtils;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 创建者     ZCL
 * 创建时间   2016/7/29 17:34
 * 描述	      ${TODO}
 * <p/>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SavantAuthenticationActivity extends BaseActivity implements TextWatcher, View.OnClickListener {
    private static final String TAG = "SavantAuthentication";
    @Bind(R.id.savant_auth_cue_ly)
    protected LinearLayout mSavantAuthCueLy;

    @Bind(R.id.savant_auth_good_match)
    protected EditText mSavantAuthGoodMatch;

    @Bind(R.id.savant_auth_yp)
    protected CheckBox mSavantAuthYp;
    @Bind(R.id.savant_auth_op)
    protected CheckBox mSavantAuthOp;
    @Bind(R.id.savant_auth_against_historical)
    protected CheckBox mSavantAuthAgainstHistorical;

    @Bind(R.id.savant_auth_weather)
    protected CheckBox mSavantAuthWeather;
    @Bind(R.id.savant_auth_team_sucess)
    protected CheckBox mSavantAuthTeamSucess;
    @Bind(R.id.savant_auth_tean_play_method)
    protected CheckBox mSavantAuthTeanPlayMethod;

    @Bind(R.id.savant_auth_pre_match)
    protected CheckBox mSavantAuthPreMatch;
    @Bind(R.id.savant_auth_other)
    protected CheckBox mSavantAuthOther;

    @Bind(R.id.savant_auth_introduce_content)
    protected EditText mSavantAuthIntroduceContent;
    @Bind(R.id.savant_auth_nickNumber_info)
    protected TextView mSavantAuthNickNumberInfo;
    @Bind(R.id.savant_auth_real_name)
    protected EditText mSavantAuthRealName;
    @Bind(R.id.savant_auth_id_card)
    protected EditText mSavantAuthIdCard;
    @Bind(R.id.savant_auth_bank_card)
    protected EditText mSavantAuthBankCard;

    //银行类型选择
    @Bind(R.id.savant_auth_pop_img)
    protected ImageView mSavantAuthPopImg;
    @Bind(R.id.savant_auth_toll)
    protected TextView mSavantAuthToll;
    @Bind(R.id.savant_auth_pop_ly)
    protected LinearLayout mSavantAuthPopLy;

    @Bind(R.id.savant_auth_agree_img)
    protected CheckBox mSavantAuthAgreeImg;

    @Bind(R.id.savant_auth_phone)
    protected EditText mSavantAuthPhone;
    @Bind(R.id.savant_auth_weixin)
    protected EditText mSavantAuthWeixin;
    @Bind(R.id.savant_auth_email)
    protected EditText mSavantAuthEmail;

    //身份证认证
    @Bind(R.id.savant_auth_id_attestation_pic1)
    protected ImageView mSavantAuthIdAttestationPic1;
    @Bind(R.id.savant_auth_id_attestation_pic2)
    protected ImageView mSavantAuthIdAttestationPic2;
    @Bind(R.id.savant_auth_id_attestation_pic3)
    protected ImageView mSavantAuthIdAttestationPic3;

    @Bind(R.id.top_title)
    TextView mTopTitle;
    @Bind(R.id.top_icon)
    ImageView mTopIcon;


    private ListView mLv;
    private PopupWindow mPopupWindow;
    private PopupWindow mPopup;
    private TextView mCancle;
    private TextView mCarema;
    private TextView mGallery;
    private RelativeLayout mLy;
    private View mView;
    private int mI = 0;
    private int num = 0;
    private String mS;

    public static final int NONE = 0;
    public static final int PHOTO_CAMERA = 1;// 相机拍照
    public static final int PHOTO_COMPILE = 2; // 编辑图片
    public static final int PHOTO_RESOULT = 3;// 结果
    private String ImageName;
    private HashMap<Integer, String> mMapUrl;
    private boolean mId;
    private HashMap<Integer, CheckBox> mMapCB;
    private StringBuilder mRefer;
    private List<BankBean> mDataBank;
    private int mBankId;
    private String mSb;
    private String mUrl;
    private ArrayList<Integer> mList;
    private File mFile1;
    private File mFile;
    private Bitmap mImage;

    @Override
    protected void initView() {
        mView = View.inflate(this, R.layout.savant_authentication, null);

        View view = View.inflate(this, R.layout.popu_carema_gallery, null);
        mCancle = (TextView) view.findViewById(R.id.popu_sugestion_cancle);
        mCarema = (TextView) view.findViewById(R.id.popu_sugestion_carema);
        mGallery = (TextView) view.findViewById(R.id.popu_sugestion_gallery);
        mLy = (RelativeLayout) view.findViewById(R.id.popu_carema_gallery_ly);
        mPopup = PopUtil.popuMake(view);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.savant_authentication;
    }

    @Override
    protected void setUnDe() {
        if (mImage != null && !mImage.isRecycled())
            mImage.recycle();
    }

    @Override
    protected void initData() {
        mTopTitle.setText(getString(R.string.savant_regist_text));
        mTopIcon.setVisibility(View.VISIBLE);
        mList = new ArrayList<>();

        mUrl = SSQSApplication.apiClient(classGuid).getBaseUri() + "/v1.0/feedback/uploadImage";
        mMapCB = new HashMap<>();
        mMapCB.put(1, mSavantAuthYp);
        mMapCB.put(2, mSavantAuthOp);
        mMapCB.put(3, mSavantAuthAgainstHistorical);

        mMapCB.put(4, mSavantAuthWeather);
        mMapCB.put(5, mSavantAuthTeamSucess);
        mMapCB.put(6, mSavantAuthTeanPlayMethod);

        mMapCB.put(7, mSavantAuthPreMatch);
        mMapCB.put(8, mSavantAuthOther);

        mLv = new ListView(this);
        mMapUrl = new HashMap<>();

        /**
         * a)	请求地址：
         /v1.0/bank
         b)	请求方式:
         get
         c)	请求参数说明：
         auth_token：登陆后加入请求头
         */
        SSQSApplication.apiClient(classGuid).getBankList(new CcApiClient.OnCcListener() {
            @Override
            public void onResponse(CcApiResult result) {
                if (result.isOk()) {
                    mDataBank = (List<BankBean>) result.getData();

                    if (mDataBank != null) {
                        processDataBanK(mDataBank);
                    }
                } else {
                    if (403 == result.getErrno()) {
                        UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                        UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                        Intent intent = new Intent(UIUtils.getContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                    }
                }
            }
        });
    }

    private void processDataBanK(List<BankBean> data) {
        mLv.setAdapter(new SavantAuthPopAdapter(this, data));
    }

    public static String getStringToday() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    @Override
    protected void initListener() {
        mSavantAuthIdCard.addTextChangedListener(this);
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSavantAuthToll.setText(mDataBank.get(position).name);
                mSavantAuthToll.setTextColor(Color.BLACK);
                mBankId = mDataBank.get(position).id;
                mPopupWindow.dismiss();
            }
        });
        mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.dismiss();
                setCt();
            }
        });

        mCarema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置图片的名称
                ImageName = "/" + getStringToday() + "ft.jpg";
                // 设置调用系统摄像头的意图(隐式意图)
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //设置照片的输出路径和文件名
                File file = new File(Environment.getExternalStorageDirectory(), ImageName);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                //开启摄像头
                if (ContextCompat.checkSelfPermission(UIUtils.getContext(), Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    TmtUtils.midToast(UIUtils.getContext(), "没有授权,请到应用设置界面手动打开相机权限!", 0);
                } else {
                    // 已经权限
                    startActivityForResult(intent, PHOTO_CAMERA);
                    Log.d("111", "已经授权");
                }
                mPopup.dismiss();
                setCt();
            }
        });
        mGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置调用系统相册的意图(隐式意图)
                Intent intent = new Intent();
                //设置值活动//android.intent.action.PICK
                intent.setAction(Intent.ACTION_PICK);
                //设置类型和数据
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                // 开启系统的相册
                startActivityForResult(intent, PHOTO_COMPILE);

                mPopup.dismiss();
                setCt();
            }
        });
        mLy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopup.dismiss();
                setCt();
            }
        });
    }

    private void setCt() {
        mSavantAuthIdAttestationPic1.setClickable(true);
        mSavantAuthIdAttestationPic2.setClickable(true);
        mSavantAuthIdAttestationPic3.setClickable(true);
    }


    @OnClick({R.id.top_back, R.id.top_icon, R.id.savant_auth_close, R.id.savant_auth_pop_ly,
            R.id.savant_auth_id_attestation_pic1, R.id.savant_auth_id_attestation_pic2, R.id.savant_auth_id_attestation_pic3,
            R.id.savant_auth_savnt_protocol, R.id.savant_auth_loading})
    public void onClick(View v) {
        UIUtils.hideKeyBord(this);
        switch (v.getId()) {
            case R.id.top_back:
                finish();
                break;
            case R.id.top_icon:
                Intent helpIntent = new Intent(this, ReferProtocolActivity.class);
                startActivity(helpIntent);
                break;
            case R.id.savant_auth_close:
                mSavantAuthCueLy.setVisibility(View.GONE);
                break;
            case R.id.savant_auth_pop_ly:
                mPopupWindow = PopUtil.popuMakemm(mLv);
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    return;
                }
                mSavantAuthPopImg.setImageResource(R.mipmap.shang_);
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                mPopupWindow.getBackground().setAlpha(200);
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.showAsDropDown(mSavantAuthPopLy);
                mSavantAuthPopLy.setClickable(false);
                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        mSavantAuthPopLy.setClickable(true);
                        mSavantAuthPopImg.setImageResource(R.mipmap.xia);
                    }
                });
                break;

            case R.id.savant_auth_id_attestation_pic1:
                mI = 1;
                mPopup.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
                setCF();
                break;
            case R.id.savant_auth_id_attestation_pic2:
                mI = 2;
                mPopup.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
                setCF();
                break;
            case R.id.savant_auth_id_attestation_pic3:
                mI = 3;
                mPopup.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
                setCF();
                break;
            case R.id.savant_auth_loading://提交
                Logger.d(TAG, "提交被点击了------------------------------:");
                /**
                 2.	申请专家
                 a)	请求地址：
                 /v1.0/applyExpert/save
                 b)	请求方式:
                 post
                 c)	请求参数说明：
                 identity: 身份证号码（必填）
                 bankCard: 银行卡号（必填）
                 realName:真实姓名（必填）
                 bankID:银行ID（必填）
                 skill：擅长联赛（必填）
                 refer: 主要参考1亚盘2欧盘3对阵历史4天气5球队战绩6球队打法7赛前信息8其他
                 intro：专家介绍（必填）
                 mobile:手机号码（必填）
                 wechat:微信号
                 email:邮箱
                 imageUrl:身份证图片数组（必填）
                 auth_token：登陆后加入请求头
                 */
                String goodMatch = mSavantAuthGoodMatch.getText().toString();
                if (TextUtils.isEmpty(goodMatch)) {
                    TmtUtils.midToast(this, "请输入您擅长的联赛!", 0);
                    return;
                }

                String intro = mSavantAuthIntroduceContent.getText().toString();
                if (TextUtils.isEmpty(intro)) {
                    TmtUtils.midToast(this, "请输入专家介绍!", 0);
                    return;
                }
                String name = mSavantAuthRealName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    TmtUtils.midToast(this, "请输入姓名!", 0);
                    return;
                }

                String idNUm = mSavantAuthIdCard.getText().toString();
                if (TextUtils.isEmpty(idNUm)) {
                    TmtUtils.midToast(this, "請輸入身份证号!", 0);
                    return;
                }

                mId = UIUtils.isID(mSavantAuthIdCard.getText());
                if (!mId) {
                    TmtUtils.midToast(UIUtils.getContext(), "身份证号码有误,请重新输入..", 0);
                    mSavantAuthIdCard.setFocusable(true);
                    return;
                }
                String backCard = mSavantAuthBankCard.getText().toString();
                if (TextUtils.isEmpty(backCard)) {
                    TmtUtils.midToast(this, "请输入银行卡号!", 0);
                    return;
                } else if (backCard.length() > 19 || backCard.length() < 16) {
                    TmtUtils.midToast(this, "请输入正确的银行卡号!", 0);
                    return;
                }

                if (!mSavantAuthAgreeImg.isChecked()) {
                    TmtUtils.midToast(UIUtils.getContext(), "请确认您是否同意该银行协议,同意请打勾!", 0);
                }

                String phoneNum = mSavantAuthPhone.getText().toString();
                if (TextUtils.isEmpty(phoneNum)) {
                    TmtUtils.midToast(this, "請輸入手机号!", 0);
                    return;
                }

                if (!UIUtils.isMobileNO(phoneNum)) {
                    TmtUtils.midToast(UIUtils.getContext(), "请输入正确的电话号码!", 0);
                    return;
                }

                if ("请选择银行".equals(mSavantAuthToll.getText())) {
                    TmtUtils.midToast(UIUtils.getContext(), "请选择您的银行卡所属的银行!", 0);
                    return;
                }

                ArrayList<String> list = new ArrayList<>();
                Set<Map.Entry<Integer, String>> entrySet = mMapUrl.entrySet();
                for (Map.Entry<Integer, String> en : entrySet) {
                    list.add(en.getValue());
                }
                if (list.size() <= 0 && list.size() != 3) {
                    TmtUtils.midToast(UIUtils.getContext(), "请上传您的身份认证图片!", 0);
                    return;
                }

                String imageUrl = "[";

                for (int i = 0; i < list.size(); i++) {
                    imageUrl += list.get(i) + ",";
                }
                imageUrl = imageUrl.substring(0, imageUrl.length() - 1) + "]";

                Set<Integer> keySet = mMapCB.keySet();
                mRefer = new StringBuilder();
                for (int i = 1; i <= keySet.size(); i++) {
                    boolean b = mMapCB.get(i).isChecked();
                    if (b) {
                        mRefer.append(i).append(",");
                    }
                }
                if (mRefer.length() >= 2) {
                    mSb = mRefer.substring(0, mRefer.length() - 1);
                }

                ExpertElement element = new ExpertElement();
                element.setIdentity(idNUm);
                element.setBankCard(backCard);
                element.setRealName(name);
                element.setBankID(mBankId + "");
                element.setSkill(mSavantAuthGoodMatch.getText().toString());
                element.setIntro(mSavantAuthIntroduceContent.getText().toString());
                element.setMobile(mSavantAuthPhone.getText().toString());
                element.setWechat(mSavantAuthWeixin.getText().toString());
                element.setEmail(mSavantAuthEmail.getText().toString());
                element.setImageUrl(imageUrl);
                element.setRefer(mSb);

                SSQSApplication.apiClient(classGuid).ExpertSave(element, new CcApiClient.OnCcListener() {
                    @Override
                    public void onResponse(CcApiResult result) {
                        if (result.isOk()) {
                            finish();
                        } else {
                            if (403 == result.getErrno()) {
                                UIUtils.SendReRecevice(Constent.LOADING_ACTION);
                                UIUtils.getSputils().putBoolean(Constent.LOADING_BROCAST_TAG, false);
                                Intent intent = new Intent(UIUtils.getContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                TmtUtils.midToast(UIUtils.getContext(), result.getMessage(), 0);
                            }
                        }
                    }
                });
                break;
            case R.id.savant_auth_savnt_protocol://专家协议
                Intent intentProtocol = new Intent(UIUtils.getContext(), ReferProtocolActivity.class);
                startActivity(intentProtocol);
                break;

            default:
                break;
        }
    }

    private void setCF() {
        mSavantAuthIdAttestationPic1.setClickable(false);
        mSavantAuthIdAttestationPic2.setClickable(false);
        mSavantAuthIdAttestationPic3.setClickable(false);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!mSavantAuthIdCard.isFocusable()) {
            Editable text = mSavantAuthIdCard.getText();
            mId = UIUtils.isID(text);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == NONE)
            return;

        // 通过照相机拍照的图片出理
        if (requestCode == PHOTO_CAMERA) {
            // 设置文件保存路径这里放在跟目录下
            final File picture = new File(Environment.getExternalStorageDirectory() + ImageName);
            Logger.d(TAG, "相机path是------------------------------:" + picture.getPath());
            mFile1 = compressImageFile(picture.getPath());
            ThreadPoolUtils.getInstance().addTask(new Runnable() {
                @Override
                public void run() {
                    runUp(mFile1);
                }
            });
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
            mFile = new File(picPath);
            mFile1 = compressImageFile(picPath);
            if (mFile1 != null) {
                ThreadPoolUtils.getInstance().addTask(new Runnable() {
                    @Override
                    public void run() {
                        runUp(mFile1);
                    }
                });
            } else {
                ThreadPoolUtils.getInstance().addTask(new Runnable() {
                    @Override
                    public void run() {
                        runUp(mFile);
                    }
                });
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
        String newPath = UIUtils.getContext().getCacheDir().getAbsolutePath() + "/topicImages.jpg";
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

        } catch (Exception e) {
            Logger.d(TAG, e.getMessage());
        }
        return file;
    }

    private void runUp(final File picture) {
        mList.add(mI);

        /**
         a)	请求地址：
         /v1.0/applyExpert/upload
         b)	请求方式:
         post
         c)	请求参数说明：
         图片3张
         auth_token：登陆后加入请求头
         */
        mS = FileImageUpload.uploadFile(picture, mUrl, UIUtils.getSputils().getString(Constent.TOKEN, null));
        Logger.d(TAG, "上传图片返回数据是------------------------------:" + mS);
        if (!"FAIL".equals(mS)) {
            num++;
            FileUpResultBean bean = JSON.parseObject(mS, FileUpResultBean.class);
            if (bean.status && bean.data != null && bean.data.imageUrl != null) {
                String URL = bean.data.imageUrl.get(0);
                mMapUrl.put(mList.get(num - 1), URL);
                int integer = mList.get(num - 1);
                for (int i : mMapUrl.keySet()) {
                    Logger.d(TAG, "tupian ---:" + i + "----" + mMapUrl.get(i));
                }
                goImg(integer, URL);
            }
        } else {
            num++;
            TmtUtils.midToast(UIUtils.getContext(), "更新头像失败,请重试图片!", 0);
        }
    }

    private void goImg(final int integer, final String data) {
        if (!TextUtils.isEmpty(data)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    /*for (int integer : mMapUrl.keySet()) {*/
                    switch (integer) {
                        case 1:
                            SSQSApplication.glide.load(mMapUrl.get(integer)).error(R.mipmap.fail).centerCrop().into(mSavantAuthIdAttestationPic1);
                            Logger.d(TAG, "第一章执行------------------------------:");
                            break;
                        case 2:
                            SSQSApplication.glide.load(mMapUrl.get(integer)).error(R.mipmap.fail).centerCrop().into(mSavantAuthIdAttestationPic2);
                            Logger.d(TAG, "第二章执行------------------------------:");
                            break;
                        case 3:
                            SSQSApplication.glide.load(mMapUrl.get(integer)).error(R.mipmap.fail).centerCrop().into(mSavantAuthIdAttestationPic3);
                            Logger.d(TAG, "第三章执行------------------------------:" + mMapUrl.get(integer));
                            break;
                        default:
                            break;
                    }
                }
                //}
            });
        }
    }
}
