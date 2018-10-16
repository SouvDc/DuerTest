package com.dc.duertest.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 描述：
 * 作者：dc on 2018/10/15 14:48
 * 邮箱：597210600@qq.com
 */
public class MakeUpUtils {
    private static final String TAG = MakeUpUtils.class.getSimpleName();

    private Context context;
    private static MakeUpUtils instanll = null;
    private MakeUpEventLinstener makeUpLinstener = null;
    // 语音唤醒对象
    private VoiceWakeuper mIvw;
    private final static int MAX = 60;
    private final static int MIN = -20;
    private int curThresh = 10;
    private String threshStr = "门限值：";
    private String keep_alive = "1";
    private String ivwNetMode = "0";




    public static MakeUpUtils getInstanll(Context ctx){
        if(instanll == null){
            synchronized (MakeUpUtils.class){
                if(instanll == null){
                    instanll = new MakeUpUtils(ctx);
                }
            }
        }

        return instanll;
    }

    public MakeUpUtils(Context ctx) {
        context = ctx;
    }

    public void setMakeUpLinstener(MakeUpEventLinstener linstener){
        makeUpLinstener = linstener;
    }

    public void initWakeUp(){
        // 初始化唤醒对象
        mIvw = VoiceWakeuper.createWakeuper(context, null);
    }

    public void startWakeUp(){
        //非空判断，防止因空指针使程序崩溃
        mIvw = VoiceWakeuper.getWakeuper();
        if(mIvw != null) {

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
            Log.e(TAG, "唤醒未初始化");
        }
    }

    private String getResource() {
        final String resPath = ResourceUtil.generateResourcePath(context
                , ResourceUtil.RESOURCE_TYPE.assets, "ivw/"+context.getString(com.cnbot.aiui.R.string.app_id)+".jet");
        Log.d( TAG, "resPath: "+resPath );
        return resPath;
    }

    private WakeuperListener mWakeuperListener = new WakeuperListener() {

        @Override
        public void onResult(WakeuperResult result) {
            Log.d(TAG, "onResult");
            if(!"1".equalsIgnoreCase(keep_alive)) {
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
//                resultString =buffer.toString();
                if(makeUpLinstener != null){
                    makeUpLinstener.makeUpEvent(object.optInt("id"));
                }
            } catch (JSONException e) {
//                resultString = "结果解析出错";
                e.printStackTrace();
            }
//            textView.setText(resultString);
        }

        @Override
        public void onError(SpeechError error) {

            Log.e(TAG, error.getPlainDescription(true));
            if(makeUpLinstener != null){
                makeUpLinstener.makeUpEventError(error.getErrorCode());
            }
//            showTip(error.getPlainDescription(true));
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


    public void stopWakeUp(){
        mIvw.stopListening();
    }


    public void release(){
        Log.d(TAG, "release WakeDemo");
        // 销毁合成对象
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.destroy();
        }
    }

    public interface MakeUpEventLinstener{
        void makeUpEvent(int id);

        void makeUpEventError(int error);
    }
}
