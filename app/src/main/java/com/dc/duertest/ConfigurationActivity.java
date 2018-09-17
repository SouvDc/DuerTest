package com.dc.duertest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dc.duertest.utils.SharedPreferenceUtil;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by DengJia on 2018/7/20.
 */

public class ConfigurationActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = ConfigurationActivity.class.getSimpleName();
    @BindView(R.id.xunfei_continuous_check)
    CheckBox xunfeiContinuousCheck;
    @BindView(R.id.btn_submit)
    Button btnSubmit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        ButterKnife.bind(this);

        initView();
    }

    public void initView() {
        initSnFeature();
    }

    /**
         * 设置spinner的adapter
         */
    private void initSnFeature() {
        xunfeiContinuousCheck.setOnCheckedChangeListener(this);

        boolean model = (boolean) SharedPreferenceUtil.get(this, "xunfei_model", false);

        if (model) {
            xunfeiContinuousCheck.setChecked(true);
        }
    }




    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) {
            SharedPreferenceUtil.put(this, "xunfei_model", true);
        } else {
            SharedPreferenceUtil.put(this, "xunfei_model", false);
        }
    }
}
