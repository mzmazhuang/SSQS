package com.dading.ssqs.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.LinearLayout;

import com.dading.ssqs.R;
import com.dading.ssqs.controllar.GBCasinoF;

/**
 * Created by mazhuang on 2017/11/30.
 */

public class CasionActivity extends BaseActivity {

    @Override
    protected int setLayoutId() {
        return 0;
    }

    @Override
    protected View getContentView() {
        LinearLayout container = new LinearLayout(this);
        container.setId(R.id.casion_container);

        FragmentManager manager = getSupportFragmentManager();

        GBCasinoF mGbCasino = new GBCasinoF();
        mGbCasino.setListener(new GBCasinoF.OnBackListener() {
            @Override
            public void onBack() {
                finish();
            }
        });

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.casion_container, mGbCasino, "casion_fragment");
        transaction.commit();
        return container;
    }
}
