package com.dading.ssqs.components;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.base.LayoutHelper;

/**
 * Created by mazhuang on 2017/12/6.
 * 加载
 */

public class LoadingDialog extends Dialog {

    private MomentLoading loading;
    private final Activity activity;

    public LoadingDialog(Context context) {
        super(context, R.style.ProgressDialog);
        activity = (Activity) context;

        LinearLayout container = new LinearLayout(context);
        container.setLayoutParams(LayoutHelper.createRelative(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        loading = new MomentLoading(context);
        container.addView(loading, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT));

        setContentView(container);
        getWindow().getAttributes().gravity = Gravity.CENTER;
        setCanceledOnTouchOutside(false);

        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (onListener != null) {
                    onListener.onDismiss();
                }
            }
        });
    }

    public interface OnListener {
        void onDismiss();
    }

    private OnListener onListener;

    public LoadingDialog setOnListener(OnListener onListener) {
        this.onListener = onListener;
        return this;
    }

    @Override
    public void show() {
        if (activity != null) {
            if (!activity.isFinishing()) {
                super.show();
                loading.show();
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        loading.dismiss();
    }
}
