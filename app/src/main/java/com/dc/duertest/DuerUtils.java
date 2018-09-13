package com.dc.duertest;

import android.os.Handler;
import android.util.Log;

import com.baidu.duer.dcs.api.DcsSdkBuilder;
import com.baidu.duer.dcs.api.IDcsSdk;
import com.baidu.duer.dcs.api.IMessageSender;
import com.baidu.duer.dcs.api.config.DefaultSdkConfigProvider;
import com.baidu.duer.dcs.api.config.SdkConfigProvider;
import com.baidu.duer.dcs.api.recorder.AudioRecordImpl;
import com.baidu.duer.dcs.api.recorder.BaseAudioRecorder;
import com.baidu.duer.dcs.devicemodule.custominteraction.CustomUserInteractionDeviceModule;
import com.baidu.duer.dcs.framework.DcsSdkImpl;
import com.baidu.duer.dcs.framework.ILoginListener;
import com.baidu.duer.dcs.framework.InternalApi;
import com.baidu.duer.dcs.framework.internalapi.IErrorListener;
import com.baidu.duer.dcs.systeminterface.IOauth;
import com.baidu.duer.dcs.tts.TtsImpl;
import com.baidu.duer.dcs.util.DcsErrorCode;
import com.baidu.duer.dcs.util.HttpProxy;
import com.baidu.duer.dcs.util.api.IDcsRequestBodySentListener;
import com.baidu.duer.dcs.util.dispatcher.DialogRequestIdHandler;
import com.baidu.duer.dcs.util.message.DcsRequestBody;
import com.baidu.duer.dcs.util.util.StandbyDeviceIdUtil;
import com.dc.duer.OAuth;
import com.dc.duer.sdk.devicemodule.screen.ScreenDeviceModule;
import com.dc.duer.sdk.devicemodule.screen.message.HtmlPayload;
import com.dc.duer.sdk.devicemodule.screen.message.RenderCardPayload;
import com.dc.duer.sdk.devicemodule.screen.message.RenderHintPayload;
import com.dc.duer.sdk.devicemodule.screen.message.RenderVoiceInputTextPayload;

import static com.baidu.turbonet.base.ContextUtils.getApplicationContext;
import static com.dc.duertest.BuildConfig.APP_KEY;
import static com.dc.duertest.BuildConfig.CLIENT_ID;
import static com.dc.duertest.BuildConfig.PID;

/**
 * 描述：
 * 作者：dc on 2018/9/11 19:30
 * 邮箱：597210600@qq.com
 */
public class DuerUtils {
    private static final String TAG = "DuerUtils";

    private Handler handler;
    protected ScreenDeviceModule screenDeviceModule;
    protected IDcsSdk dcsSdk;
    private RenderCardPayLoadResultListener renderCardPayLoadResultListener;
    private DcsRequestBodyStatusListener dcsRequestBodyStatusListener;

    /**
     * @descriptoin	初始化DuerOS,包括tts
     * @author	dc
     * @date 2018/9/13 15:16
     */
    public void initSDK(){
        handler = new Handler();
        // 第一步初始化sdk
        // BaseAudioRecorder audioRecorder = new PcmAudioRecorderImpl(); pcm 输入方式
        BaseAudioRecorder audioRecorder = new AudioRecordImpl();
        IOauth oauth = OAuth.getOauth(Constant.CLIENT_ID);

        // proxyIp 为代理IP
        // proxyPort  为代理port
        HttpProxy httpProxy = new HttpProxy("172.24.194.28", 8888);

        // SDK配置，ClientId、语音PID、代理等
        SdkConfigProvider sdkConfigProvider = getSdkConfigProvider();
        // 构造dcs sdk
        DcsSdkBuilder builder = new DcsSdkBuilder();
        dcsSdk = builder.withSdkConfig(sdkConfigProvider)
                .withOauth(oauth)
                .withAudioRecorder(audioRecorder)
                // 1.withDeviceId设置设备唯一ID
                // 2.强烈建议！！！！
                //   如果开发者清晰的知道自己设备的唯一id，可以按照自己的规则传入
                //   需要保证设置正确，保证唯一、刷机和升级后不变
                // 3.sdk提供的方法，但是不保证所有的设别都是唯一的
                //   StandbyDeviceIdUtil.getStandbyDeviceId()
                //   该方法的算法是MD5（android_id + imei + Mac地址）32位  +  32位UUID总共64位
                //   生成：首次按照上述算法生成ID，生成后依次存储apk内部->存储系统数据库->存储外部文件
                //   获取：存储apk内部->存储系统数据库->存储外部文件，都没有则重新生成
                .withDeviceId(StandbyDeviceIdUtil.getStandbyDeviceId())
                // 设置音乐播放器的实现，sdk 内部默认实现为MediaPlayerImpl
                // .withMediaPlayer(new MediaPlayerImpl(AudioManager.STREAM_MUSIC))
                .build();


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TtsImpl impl = getInternalApi().initLocalTts(getApplicationContext(), null, null,
                        BuildConfig.TTS_APIKEY,
                        BuildConfig.TTS_SERCERTKEY, BuildConfig.TTS_APPID, null);
                impl.setSpeaker(2);
                getInternalApi().setVolume(0.8f);
            }
        }, 200);

        // 设置Oneshot
        getInternalApi().setSupportOneshot(false);

        // 第二步：可以按需添加内置端能力和用户自定义端能力（需要继承BaseDeviceModule）
        // 屏幕展示
        IMessageSender messageSender = getInternalApi().getMessageSender();
        // 上屏
        screenDeviceModule = new ScreenDeviceModule(messageSender);
        screenDeviceModule.addScreenListener(screenListener);
        dcsSdk.putDeviceModule(screenDeviceModule);

        // 在线返回文本的播报，eg:你好，返回你好的播报
        DialogRequestIdHandler dialogRequestIdHandler =
                ((DcsSdkImpl) dcsSdk).getProvider().getDialogRequestIdHandler();
        CustomUserInteractionDeviceModule customUserInteractionDeviceModule =
                new CustomUserInteractionDeviceModule(messageSender, dialogRequestIdHandler);
        dcsSdk.putDeviceModule(customUserInteractionDeviceModule);

        // 扩展自定义DeviceModule,eg...
        addOtherDeviceModule(dcsSdk, messageSender);
        // 获取设备列表
        // getInternalApi().getSmartHomeManager().getDeviceList(null, null);
    }



    /**
     * @descriptoin	登录操作
     * @author	dc
     * @date 2018/9/13 15:17
     */
    protected void sdkRun() {
        // 第三步，将sdk跑起来
        ((DcsSdkImpl) dcsSdk).getInternalApi().login(new ILoginListener() {
            @Override
            public void onSucceed(String accessToken) {
                dcsSdk.run(null);
//                Toast.makeText(MainActivity.this.getApplicationContext(), "登录成功", Toast
//                        .LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String errorMessage) {
//                Toast.makeText(MainActivity.this.getApplicationContext(), "登录失败", Toast
//                        .LENGTH_SHORT).show();
//                Log.e(TAG, "login onFailed. ");
//                finish();
            }

            @Override
            public void onCancel() {
//                Toast.makeText(MainActivity.this.getApplicationContext(), "登录被取消", Toast
//                        .LENGTH_SHORT).show();
//                Log.e(TAG, "login onCancel. ");
//                finish();
            }
        });
    }

    /**
     * @descriptoin	配置sdk
     * @author	dc
     * @param
     * @date 2018/9/13 15:19
     */
    protected SdkConfigProvider getSdkConfigProvider() {
        return new DefaultSdkConfigProvider() {
            @Override
            public String clientId() {
                return CLIENT_ID;
            }

            @Override
            public int pid() {
                return PID;
            }

            @Override
            public String appKey() {
                return APP_KEY;
            }
        };
    }

    public InternalApi getInternalApi() {
        return ((DcsSdkImpl) dcsSdk).getInternalApi();
    }

    /**
     * @descriptoin	发送需要查询的文本内容
     * @author	dc
     * @param str 文本内容
     * @date 2018/9/13 15:17
     */
    public void sendDuer(String str){
        getInternalApi().sendQuery(str);
    }

    /**
     * @descriptoin	添加语音分析结果返回监听器
     * @author	dc
     * @param listener
     * @date 2018/9/13 15:17
     */
    public void setRenerCardListener(RenderCardPayLoadResultListener listener){
        renderCardPayLoadResultListener = listener;
    }


    protected void addOtherDeviceModule(IDcsSdk dcsSdk, IMessageSender messageSender) {

    }

    private ScreenDeviceModule.IScreenListener screenListener = new ScreenDeviceModule.IScreenListener() {
        @Override
        public void onRenderVoiceInputText(RenderVoiceInputTextPayload payload) {
//            handleRenderVoiceInputTextPayload(payload);
        }

        @Override
        public void onHtmlPayload(HtmlPayload htmlPayload) {
//            handleHtmlPayload(htmlPayload);
        }

        @Override
        public void onRenderCard(RenderCardPayload renderCardPayload) {
            renderCardPayLoadResultListener.onRenDerCardResult(renderCardPayload);
        }

        @Override
        public void onRenderHint(RenderHintPayload renderHintPayload) {

        }
    };




    /**
     * @descriptoin	开始离线合成
     * @author	dc
     * @param inputText
     * @date 2018/9/13 11:04
     */
    public void startOffSpeech(String inputText){
        getInternalApi().speakOfflineRequest(inputText);
    }

    /**
     * @descriptoin	语音合成
     * @author	dc
     * @param
     * @date 2018/9/13 11:09
     */
    public void startSpeech(String inputText){
        getInternalApi().speakRequest(inputText);
    }

    /**
     * @descriptoin	停止合成
     * @author	dc
     * @date 2018/9/13 19:28
     */
    public void stopSpeech(){
        getInternalApi().stopSpeaker();
    }

    /**
     * @descriptoin 添加语音播报状态结果返回监听器
     * @author	dc
     * @date 2018/9/13 15:22
     */
    public void addRequestListener(DcsRequestBodyStatusListener listener){
        dcsRequestBodyStatusListener = listener;
        // event发送
        getInternalApi().addRequestBodySentListener(dcsRequestBodySentListener);
    }

    /**
     * @descriptoin	添加错误监听器
     * @author	dc
     * @date 2018/9/13 15:23
     */
    public void addErrorListener(){
        // 错误
        getInternalApi().addErrorListener(errorListener);
    }

    /**
     * @descriptoin	是否dcs资源
     * @author	dc
     * @date 2018/9/13 15:23
     */
    public void release(){
        dcsSdk.release();
    }


    /**
     * @descriptoin	语音播报状态监听器
     * @author	dc
     * @date 2018/9/13 15:25
     */
    private IDcsRequestBodySentListener dcsRequestBodySentListener = new IDcsRequestBodySentListener() {

        @Override
        public void onDcsRequestBody(DcsRequestBody dcsRequestBody) {
            String eventName = dcsRequestBody.getEvent().getHeader().getName();
            Log.v(TAG, "eventName:" + eventName);
            if (eventName.equals("PlaybackStopped") || eventName.equals("PlaybackFinished")
                    || eventName.equals("PlaybackFailed")) {
//                playButton.setText("等待音乐");
//                isPlaying = false;
            } else if (eventName.equals("PlaybackPaused")) {
//                playButton.setText("暂停中");
//                isPlaying = false;
            } else if (eventName.equals("PlaybackStarted") || eventName.equals("PlaybackResumed")) {
//                playButton.setText("播放中...");
//                isPlaying = true;
            } else if (eventName.equals("SpeechStarted")) {
                // 开始语音播报
                dcsRequestBodyStatusListener.speechStarted();
            } else if (eventName.equals("SpeechFinished")) {
                // 结束语音播报
                dcsRequestBodyStatusListener.speechFinished();
            }
        }
    };

    /**
     * @descriptoin	DuerOS错误码监听器
     * @author	dc
     * @date 2018/9/13 15:25
     */
    private IErrorListener errorListener = new IErrorListener() {
        @Override
        public void onErrorCode(DcsErrorCode errorCode) {
            if (errorCode.error == DcsErrorCode.VOICE_REQUEST_EXCEPTION) {
                if (errorCode.subError == DcsErrorCode.NETWORK_UNAVAILABLE) {
                    //   "网络不可用",
                } else {
                    //识别失败，请稍后再试
                }

            } else if (errorCode.error == DcsErrorCode.LOGIN_FAILED) {
                // 未登录

            } else if (errorCode.subError == DcsErrorCode.UNAUTHORIZED_REQUEST) {
                // 以下仅针对 passport 登陆情况下的账号刷新，非 passport 刷新请参看文档。
            }
        }
    };


    /****************************  接口代码区  **************************************/
    /**
     * @descriptoin	语义分析结果
     * @author	dc
     * @date 2018/9/13 15:13
     */
    public interface RenderCardPayLoadResultListener{
        void onRenDerCardResult(RenderCardPayload renderCardPayload);
    }

    /**
     * @descriptoin	语音播报状态
     * @author	dc
     * @date 2018/9/13 15:14
     */
    public interface DcsRequestBodyStatusListener{
        void speechStarted();

        void speechFinished();
    }
}
