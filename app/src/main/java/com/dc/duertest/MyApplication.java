package com.dc.duertest;

import android.app.Application;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * 描述：
 * 作者：dc on 2018/10/15 11:45
 * 邮箱：597210600@qq.com
 */
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        StringBuffer param = new StringBuffer();
        param.append("appid="+getString(R.string.app_id));
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(MyApplication.this, param.toString());


        super.onCreate();
    }
}
