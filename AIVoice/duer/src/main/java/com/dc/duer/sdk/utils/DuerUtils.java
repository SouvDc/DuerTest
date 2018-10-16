package com.dc.duer.sdk.utils;

import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

import com.baidu.duer.dcs.api.DcsSdkBuilder;
import com.baidu.duer.dcs.api.IDcsSdk;
import com.baidu.duer.dcs.api.IMessageSender;
import com.baidu.duer.dcs.api.config.DefaultSdkConfigProvider;
import com.baidu.duer.dcs.api.config.SdkConfigProvider;
import com.baidu.duer.dcs.api.recorder.AudioRecordImpl;
import com.baidu.duer.dcs.api.recorder.BaseAudioRecorder;
import com.baidu.duer.dcs.api.wakeup.BaseWakeup;
import com.baidu.duer.dcs.api.wakeup.IWakeupAgent;
import com.baidu.duer.dcs.api.wakeup.IWakeupProvider;
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
import com.baidu.duer.kitt.KittWakeUpServiceImpl;
import com.baidu.duer.kitt.WakeUpConfig;
import com.baidu.duer.kitt.WakeUpWord;
import com.dc.duer.BuildConfig;
import com.dc.duer.OAuth;
import com.dc.duer.sdk.devicemodule.screen.ScreenDeviceModule;
import com.dc.duer.sdk.devicemodule.screen.extend.card.ScreenExtendDeviceModule;
import com.dc.duer.sdk.devicemodule.screen.message.HtmlPayload;
import com.dc.duer.sdk.devicemodule.screen.message.RenderCardPayload;
import com.dc.duer.sdk.devicemodule.screen.message.RenderHintPayload;
import com.dc.duer.sdk.devicemodule.screen.message.RenderVoiceInputTextPayload;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.turbonet.base.ContextUtils.getApplicationContext;
import static com.dc.duer.BuildConfig.APP_KEY;
import static com.dc.duer.BuildConfig.CLIENT_ID;
import static com.dc.duer.BuildConfig.PID;

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
    private DuerDcsErrorListener duerDcsErrorListener = null;

    // 唤醒配置
    // 格式必须为：浮点数，用','分隔，每个模型对应3个灵敏度
    // 例如有2个模型,就需要6个灵敏度，0.35,0.35,0.40,0.45,0.45,0.55
    private static final String WAKEUP_RES_PATH = "snowboy/common.res";
    private static final String WAKEUP_UMDL_PATH = "snowboy/xiaoduxiaodu_all_11272017.umdl";
    private static final String WAKEUP_SENSITIVITY = "0.35,0.35,0.40";
    private static final String WAKEUP_HIGH_SENSITIVITY = "0.45,0.45,0.55";
    // 唤醒成功后是否需要播放提示音
    private static final boolean ENABLE_PLAY_WARNING = true;

    public static DuerUtils instance = null;

    public static DuerUtils getInstance(){
        if(instance == null){
            synchronized (DuerUtils.class){
                if(instance == null){
                    instance = new DuerUtils();
                }
            }
        }
        return instance;
    }


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
        IOauth oauth =  OAuth.getOauth(CLIENT_ID);
        // 唤醒单独开启唤醒进程；  如果不需要将唤醒放入一个单独进程，可以使用KittWakeUpImpl
        final BaseWakeup wakeup = new KittWakeUpServiceImpl(audioRecorder);
        // 百度语音团队的离线asr和百度语音团队的唤醒，2个so库冲突，暂时不要用WakeupImpl实现的唤醒功能！！
//        final BaseWakeup wakeup = new WakeupImpl();
        final IWakeupProvider wakeupProvider = new IWakeupProvider() {
            @Override
            public WakeUpConfig wakeUpConfig() {
                // 添加多唤醒词和索引
                // 此处传入的index需要和Snowboy唤醒模型文件一致
                // 例：模型文件中有3个唤醒词，分别为不同语速的"小度小度"，index分别为1-3，则需要按照以下格式添加
                // 唤醒成功后，回调中会包含被唤醒的WakeUpWord
                List<WakeUpWord> wakeupWordList = new ArrayList<>();
                wakeupWordList.add(new WakeUpWord(1, "小虎小虎"));
                wakeupWordList.add(new WakeUpWord(2, "小丁小丁"));
                wakeupWordList.add(new WakeUpWord(3, "小超小超"));
                final List<String> umdlPaths = new ArrayList<>();
                umdlPaths.add(WAKEUP_UMDL_PATH);
                return new WakeUpConfig.Builder()
                        .resPath(WAKEUP_RES_PATH)
                        .umdlPath(umdlPaths)
                        .sensitivity(WAKEUP_SENSITIVITY)
                        .highSensitivity(WAKEUP_HIGH_SENSITIVITY)
                        .wakeUpWords(wakeupWordList)
                        .build();
            }

            @Override
            public boolean enableWarning() {
                return ENABLE_PLAY_WARNING;
            }

            @Override
            public String warningSource() {
                // 每次在播放唤醒提示音前调用该方法
                // assets目录下的以assets://开头
                // 文件为绝对路径
                return "assets://ding.wav";
            }

            @Override
            public float volume() {
                // 每次在播放唤醒提示音前调用该方法
                // [0-1]
                return 0.8f;
            }

            @Override
            public boolean wakeAlways() {
                return true;//SDKBaseActivity.this.enableWakeUp();
            }

            @Override
            public BaseWakeup wakeupImpl() {
                return wakeup;
            }

            @Override
            public int audioType() {
                // 用户自定义类型
                return AudioManager.STREAM_MUSIC;
            }
        };


        // proxyIp 为代理IP
        // proxyPort  为代理port
        HttpProxy httpProxy = new HttpProxy("172.24.194.28", 8888);

        // SDK配置，ClientId、语音PID、代理等
        SdkConfigProvider sdkConfigProvider = getSdkConfigProvider();
        // 构造dcs sdk
        DcsSdkBuilder builder = new DcsSdkBuilder();
        dcsSdk = builder.withSdkConfig(sdkConfigProvider)
//                .withWakeupProvider(wakeupProvider)
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
        // ！！！！临时配置需要在run之前设置！！！！
        // 临时配置开始
        // 暂时没有定的API接口，可以通过getInternalApi设置后使用
        // 设置唤醒参数后，初始化唤醒
        getInternalApi().initWakeUp();
//        getInternalApi().setOnPlayingWakeUpSensitivity(WAKEUP_ON_PLAYING_SENSITIVITY);
//        getInternalApi().setOnPlayingWakeUpHighSensitivity(WAKEUP_ON_PLAYING_HIGH_SENSITIVITY);
        getInternalApi().setAsrMode(1);
        // 测试数据，具体bduss值
        // getInternalApi().setBDuss("");
        // 临时配置结束
        // dbp平台
        // getInternalApi().setDebugBot("f15be387-1348-b71b-2ae5-8f19f2375ea1");

        // 第二步：可以按需添加内置端能力和用户自定义端能力（需要继承BaseDeviceModule）
        // 屏幕展示
        IMessageSender messageSender = getInternalApi().getMessageSender();

        // 上屏
        screenDeviceModule = new ScreenDeviceModule(messageSender);
        screenDeviceModule.addScreenListener(screenListener);
        dcsSdk.putDeviceModule(screenDeviceModule);

        ScreenExtendDeviceModule screenExtendDeviceModule = new ScreenExtendDeviceModule(messageSender);
//        screenExtendDeviceModule.addExtensionListener(mScreenExtensionListener);
        dcsSdk.putDeviceModule(screenExtendDeviceModule);

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
    public void sdkRun() {
        // 第三步，将sdk跑起来
        ((DcsSdkImpl) dcsSdk).getInternalApi().login(new ILoginListener() {
            @Override
            public void onSucceed(String accessToken) {
                dcsSdk.run(null);
                duerDcsErrorListener.duerLoginCode("登录成功");
            }

            @Override
            public void onFailed(String errorMessage) {
                duerDcsErrorListener.duerLoginCode("登录失败");
            }

            @Override
            public void onCancel() {
                duerDcsErrorListener.duerLoginCode("登录被取消");
            }
        });
        // 错误
        getInternalApi().addErrorListener(errorListener);
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


    public void initWakeUp(){
        IWakeupAgent wakeupAgent = getInternalApi().getWakeupAgent();
        if (wakeupAgent != null) {
            wakeupAgent.addWakeupAgentListener(wakeupAgentListener);
        }
    }

    private IWakeupAgent.IWakeupAgentListener wakeupAgentListener = new IWakeupAgent.SimpleWakeUpAgentListener() {
        @Override
        public void onWakeupSucceed(WakeUpWord wakeUpWord) {
            Log.e(TAG, "onWakeupSucceed: 唤醒成功"  );
        }
    };

    public void wakeUp() {
        getInternalApi().startWakeup();
    }

    public void stopWakeUp(){
        // 如果有唤醒，则停止唤醒
        getInternalApi().stopWakeup(null);
    }

    /**
     * @descriptoin	添加错误监听器
     * @author	dc
     * @date 2018/9/13 15:23
     */
    public void addErrorListener(DuerDcsErrorListener listener){
        duerDcsErrorListener = listener;

    }


    /**
     * @descriptoin	释放dcs资源
     * @author	dc
     * @date 2018/9/13 15:23
     */
    public void release(){
        if(dcsSdk != null)
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
            // TODO: 2018/9/17 这里只错错误码的监听处理，具体的错误结果码请看DuerOS文档
            duerDcsErrorListener.duerErrorCode(errorCode.error);
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

    public interface DuerDcsErrorListener{
        void duerErrorCode(int errorCode);

        void duerLoginCode(String loginCode);
    }
}
