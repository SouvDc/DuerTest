package com.dc.duertest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.baidu.duer.dcs.systeminterface.IOauth;
import com.baidu.duer.dcs.util.HttpProxy;
import com.baidu.duer.dcs.util.dispatcher.DialogRequestIdHandler;
import com.baidu.duer.dcs.util.util.LogUtil;
import com.baidu.duer.dcs.util.util.StandbyDeviceIdUtil;
import com.dc.duer.OAuth;
import com.dc.duer.sdk.devicemodule.screen.ScreenDeviceModule;
import com.dc.duer.sdk.devicemodule.screen.message.HtmlPayload;
import com.dc.duer.sdk.devicemodule.screen.message.RenderCardPayload;
import com.dc.duer.sdk.devicemodule.screen.message.RenderHintPayload;
import com.dc.duer.sdk.devicemodule.screen.message.RenderVoiceInputTextPayload;

import static com.dc.duertest.BuildConfig.APP_KEY;
import static com.dc.duertest.BuildConfig.CLIENT_ID;
import static com.dc.duertest.BuildConfig.PID;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private EditText inputEt;
    private Button   sendBtn;

    protected ScreenDeviceModule screenDeviceModule;
    protected IDcsSdk dcsSdk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initSdk();
        sdkRun();
    }

    private void initView() {
        inputEt = (EditText) findViewById(R.id.et_input);
        sendBtn = (Button) findViewById(R.id.btn_send);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = inputEt.getText().toString().trim();
                if (TextUtils.isEmpty(inputText)) {
                    Toast.makeText(MainActivity.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 清空并收起键盘
//                inputEt.getEditableText().clear();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputEt.getWindowToken(), 0);

                getInternalApi().sendQuery(inputText);
            }
        });
    }

    protected void initSdk() {
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

        // 设置Oneshot
        getInternalApi().setSupportOneshot(false);
        // ！！！！临时配置需要在run之前设置！！！！
        // 临时配置开始
        // 暂时没有定的API接口，可以通过getInternalApi设置后使用
        // 设置唤醒参数后，初始化唤醒
        getInternalApi().initWakeUp();
//        getInternalApi().setOnPlayingWakeUpSensitivity(WAKEUP_ON_PLAYING_SENSITIVITY);
//        getInternalApi().setOnPlayingWakeUpHighSensitivity(WAKEUP_ON_PLAYING_HIGH_SENSITIVITY);
//        getInternalApi().setAsrMode(getAsrMode());
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


    protected void sdkRun() {
        // 第三步，将sdk跑起来
        ((DcsSdkImpl) dcsSdk).getInternalApi().login(new ILoginListener() {
            @Override
            public void onSucceed(String accessToken) {
                dcsSdk.run(null);
                Toast.makeText(MainActivity.this.getApplicationContext(), "登录成功", Toast
                        .LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String errorMessage) {
                Toast.makeText(MainActivity.this.getApplicationContext(), "登录失败", Toast
                        .LENGTH_SHORT).show();
                Log.e(TAG, "login onFailed. ");
                finish();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this.getApplicationContext(), "登录被取消", Toast
                        .LENGTH_SHORT).show();
                Log.e(TAG, "login onCancel. ");
                finish();
            }
        });
    }

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

    protected void addOtherDeviceModule(IDcsSdk dcsSdk, IMessageSender messageSender) {

    }

    /**
     * asr的识别类型-在线or离线
     *
     * @return
     */
//    public abstract int getAsrMode();


    private ScreenDeviceModule.IScreenListener screenListener = new ScreenDeviceModule.IScreenListener() {
        @Override
        public void onRenderVoiceInputText(RenderVoiceInputTextPayload payload) {
            handleRenderVoiceInputTextPayload(payload);
        }

        @Override
        public void onHtmlPayload(HtmlPayload htmlPayload) {
//            handleHtmlPayload(htmlPayload);
        }

        @Override
        public void onRenderCard(RenderCardPayload renderCardPayload) {
            Log.e(TAG, "onRenderCard" + renderCardPayload.content);
        }

        @Override
        public void onRenderHint(RenderHintPayload renderHintPayload) {

        }
    };

    private void handleRenderVoiceInputTextPayload(RenderVoiceInputTextPayload payload) {
        Log.e(TAG, "handleRenderVoiceInputTextPayload: " + payload.text);
        if (payload.type == RenderVoiceInputTextPayload.Type.FINAL) {
            LogUtil.dc("ASR-FINAL-RESULT", payload.text);
        }
    }

}
