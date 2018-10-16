package com.dc.duertest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.dc.duertest.utils.SharedPreferenceUtil;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;

import org.json.JSONException;
import org.json.JSONObject;

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


    // 语音唤醒对象
    private VoiceWakeuper mIvw;
    // 唤醒结果内容
    private String resultString;
    private final static int MAX = 60;
    private final static int MIN = -20;
    private int curThresh = 10;
    private String keep_alive = "1";
    private String ivwNetMode = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        ButterKnife.bind(this);

        initView();

        initWakeUp();

    }

    private void initWakeUp() {
        // 初始化唤醒对象
        mIvw = VoiceWakeuper.createWakeuper(this, null);
        //非空判断，防止因空指针使程序崩溃
        mIvw = VoiceWakeuper.getWakeuper();
        if(mIvw != null) {
            resultString = "";

            // 清空参数
            mIvw.setParameter(SpeechConstant.PARAMS, null);
            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:"+ curThresh);
            // 设置唤醒模式
            mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
            // 设置持续进行唤醒
            mIvw.setParameter(SpeechConstant.KEEP_ALIVE, keep_alive);
            // 设置闭环优化网络模式
            mIvw.setParameter(SpeechConstant.IVW_NET_MODE, ivwNetMode);
            // 设置唤醒资源路径
            mIvw.setParameter(SpeechConstant.IVW_RES_PATH, getResource());
            // 设置唤醒录音保存路径，保存最近一分钟的音频
            mIvw.setParameter( SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().getPath()+"/msc/ivw.wav" );
            mIvw.setParameter( SpeechConstant.AUDIO_FORMAT, "wav" );
            // 如有需要，设置 NOTIFY_RECORD_DATA 以实时通过 onEvent 返回录音音频流字节
            //mIvw.setParameter( SpeechConstant.NOTIFY_RECORD_DATA, "1" );

            // 启动唤醒
            mIvw.startListening(mWakeuperListener);
        } else {
            Toast.makeText(ConfigurationActivity.this, "唤醒未初始化", Toast.LENGTH_LONG).show();
        }
    }

    private String getResource() {
        final String resPath = ResourceUtil.generateResourcePath(ConfigurationActivity.this,
                ResourceUtil.RESOURCE_TYPE.assets, "ivw/"+getString(R.string.app_id)+".jet");
        Log.d( TAG, "resPath: "+resPath );
        return resPath;
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


    private WakeuperListener mWakeuperListener = new WakeuperListener() {

        @Override
        public void onResult(WakeuperResult result) {
            Log.d(TAG, "onResult");
            if(!"1".equalsIgnoreCase(keep_alive)) {
//                setRadioEnable(true);
            }
            try {
                String text = result.getResultString();
                JSONObject object;
                object = new JSONObject(text);
                StringBuffer buffer = new StringBuffer();
                buffer.append("【RAW】 "+text);
                buffer.append("\n");
                buffer.append("【操作类型】"+ object.optString("sst"));
                buffer.append("\n");
                buffer.append("【唤醒词id】"+ object.optString("id"));
                buffer.append("\n");
                buffer.append("【得分】" + object.optString("score"));
                buffer.append("\n");
                buffer.append("【前端点】" + object.optString("bos"));
                buffer.append("\n");
                buffer.append("【尾端点】" + object.optString("eos"));
                resultString =buffer.toString();
            } catch (JSONException e) {
                resultString = "结果解析出错";
                e.printStackTrace();
            }
//            textView.setText(resultString);
        }

        @Override
        public void onError(SpeechError error) {
            Toast.makeText(ConfigurationActivity.this, error.getPlainDescription(true), Toast.LENGTH_LONG).show();
//            setRadioEnable(true);
        }

        @Override
        public void onBeginOfSpeech() {
        }

        @Override
        public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
            switch( eventType ){
                // EVENT_RECORD_DATA 事件仅在 NOTIFY_RECORD_DATA 参数值为 真 时返回
                case SpeechEvent.EVENT_RECORD_DATA:
                    final byte[] audio = obj.getByteArray( SpeechEvent.KEY_EVENT_RECORD_DATA );
                    Log.i( TAG, "ivw audio length: "+audio.length );
                    break;
            }
        }

        @Override
        public void onVolumeChanged(int volume) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy WakeDemo");
        // 销毁合成对象
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.destroy();
        }
    }
}
