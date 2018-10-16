package com.dc.duer.sdk.utils;

import android.content.Context;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;
import com.dc.duer.sdk.Constant;
import com.dc.duer.sdk.bean.AsrErrorBean;
import com.dc.duer.sdk.bean.RecogResult;
import com.dc.duer.sdk.listener.SpeechAsrListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Created by 75213 on 2017/12/19.
 */

public class BaiDuASR implements EventListener {
    private final String TAG = "BaiDuASR";

    private EventManager asr;
    private Context context;
    private SpeechAsrListener mListener;
    private BaiduAsrErrorListener baiduAsrErrorListener;
    private static BaiDuASR instance;

    public static BaiDuASR getInstanll(Context context){
        if (instance == null) {
            synchronized (BaiDuASR.class) {
                if (instance == null) {
                    instance = new BaiDuASR(context);
                }
            }
        }
        return instance;
    }

    public BaiDuASR(Context context ){
        this.context = context;
    }

    /**
     * 初始化
     */
    public void initASR(){
        asr = EventManagerFactory.create(context, "asr");
        asr.registerListener(this); //  EventListener 中 onEvent方法

    }

    /**
     * 描述：开始识别
     */
    public void startASR(){
        Log.e(TAG , "开始识别");
        Map<String ,Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.PID , 15361); //普通话
        params.put(SpeechConstant.VAD , SpeechConstant.VAD_DNN);
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT , 1000); //长时间录音
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false); //禁止音量回掉
        String event = SpeechConstant.ASR_START; //识别事件
        String json = new JSONObject(params).toString();
        asr.send(event , json , null ,0 , 0);
    }

    /**
     * 暂停识别
     */
    public void stopASR(){
        if (asr != null){
            Log.e(TAG , "停止识别");
            asr.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
            asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        }
    }

    /**
     * 描述：释放资源
     */
    public void releaseASR(){
        if (asr == null){
            return;
        }
        stopASR();
        asr.unregisterListener(this);
        asr = null;
    }

    /**
     * 解析工具
     */
    public String getText(String str){
        String startStr = "sult\":\"";
        String endStr = "\",\"res";
        int start = str.indexOf(startStr) + startStr.length();
        int end = str.indexOf(endStr);
        Log.e(TAG , "int" + start + end);
        if (start > 0 && end > 0){
            return str.substring(start , end);
        }else {
            return "";
        }
    }

    @Override
    public void onEvent(String name, String params, byte[] bytes, int i, int i1) {
        Log.e("TestActivity", "name = "  + name + "     params =" + params);
        //唤醒事件
        if(name.equals("wp.data")){
            try {
                JSONObject json = new JSONObject(params);
                int errorCode = json.getInt("errorCode");
                if(errorCode == 0){
                    //唤醒成功
                } else {
                    //唤醒失败
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if("wp.exit".equals(name)){
            //唤醒已停止
        }
        if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_READY)){
        } else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_BEGIN)){
            mListener.asrRecording(true);
        } else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_AUDIO)){
        } else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_VOLUME)){
        } else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_END)){
            mListener.asrRecognizing(true);
            // TODO: 2018/7/27 加入判断，如果没识别到文字则进行提示
        } else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)){
            // 语音识别过程
            RecogResult result = RecogResult.parseJson(params);
            String[] strs = result.getResultsRecognition();
            if(strs != null && strs.length > 0) {
                mListener.asrResult(strs[strs.length - 1], false);
                Log.e(TAG, "asrPartial = " + strs[0]);
            }
        } else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_FINISH)){
            //  识别结束
            mListener.asrFinish(true);
            // 3101 长时间未检测到人说话，请重新识别
            try{
                AsrErrorBean asrErrorBean = new Gson().fromJson(params, AsrErrorBean.class);

                if (asrErrorBean != null){
                    int code = asrErrorBean.getSub_error();
                    if(code == 3101){
                        //长时间未检测到人说话，请重新识别
                        Log.e(TAG, "在这里启动识别");
                        if(Constant.CONTINUITY){
                            startASR();
                        }
                    }
                    baiduAsrErrorListener.baiduAsrError(code);
                }


            } catch (Exception e){
                e.printStackTrace();
            }
        } else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_EXIT)){
        } else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_CANCEL)){
            Log.e(TAG, "语音识别取消");
        } else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_ERROR)){
            Log.e(TAG, "语音识别错误");
        }

        if (null == params || params.length() == 0) {
            return;
        } else {
            RecogResult result = RecogResult.parseJson(params);
            if (result.isFinalResult()) {
                String[] strs = result.getResultsRecognition();
                Log.e(TAG, "识别结果 = " + strs[strs.length - 1]);
                mListener.asrResult(strs[strs.length - 1], true);
                mListener.asrThinking(true);
                stopASR();
            } else {

            }
        }
    }

    /**
     * @descriptoin	添加错误监听器
     * @author	dc
     * @date 2018/9/13 15:23
     */
    public void setAsrErrorListener(BaiduAsrErrorListener listener){
        // 错误
        baiduAsrErrorListener = listener;
    }

    public void setAsrListener(SpeechAsrListener listener) {
        mListener = listener;
    }


    /**
     * @descriptoin	语音识别错误码
     * @author	dc
     * @date 2018/9/17 15:40
     */
    public interface BaiduAsrErrorListener{
        void baiduAsrError(int errorCode);
    }
}
