package com.dc.duertest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnbot.aiui.AIUINlpUtil;
import com.cnbot.aiui.bean.AiuiNlpResult;
import com.dc.duer.sdk.devicemodule.screen.message.RenderCardPayload;
import com.dc.duertest.bean.MsgBean;
import com.dc.duertest.listener.SpeechAsrListener;
import com.dc.duertest.utils.BaiDuASR;
import com.dc.duertest.utils.SharedPreferenceUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.dc.duertest.Constant.CONTINUITY;


public class MainActivity extends AppCompatActivity implements DuerUtils.RenderCardPayLoadResultListener, SpeechAsrListener,
        AiuiNlpResult {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.main_recyclerView)
    RecyclerView mainRecyclerView;
    @BindView(R.id.tv_asr_result)
    TextView tvAsrResult;
    @BindView(R.id.btn_voice_input)
    Button btnVoiceInput;
    @BindView(R.id.main_input_edit)
    EditText mainInputEdit;
    @BindView(R.id.main_send_bt)
    Button mainSendBt;
    @BindView(R.id.main_stop_asr_bt)
    Button mainStopAsrBt;
    @BindView(R.id.main_bottom)
    LinearLayout mainBottom;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.btn_admin_config)
    Button btnAdminConfig;

    //定义对象
//    private final List<MsgBean> history = new ArrayList<>();
    private ChatHistoryAdapter adapter;
    private List<MsgBean> msgBeanList = null;

    private BaiDuASR baiduASR = null;
    private DuerUtils duerUtils = null;

    //属性定义
    private String inputStr = ""; //语音识别或者手动输入需要查询的内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
        init();

        initListener();
    }


    private void init() {
        Constant.CONTINUITY = (boolean) SharedPreferenceUtil.get(MainActivity.this, "xunfei_model", false);

        //初始化ASR
        baiduASR = new BaiDuASR(MainActivity.this);
        baiduASR.initASR();
        //初始化DuerOS
        duerUtils = new DuerUtils();
        duerUtils.initSDK();
        duerUtils.sdkRun();
        duerUtils.setRenerCardListener(this);


        AIUINlpUtil.getInstance(this).createAgent();

    }

    private void initListener() {
        BaiDuASR.setAsrListener(this);

        duerUtils.setRenerCardListener(this);

        //初始化AIUI
        AIUINlpUtil.getInstance(this).setListener(this);

        duerUtils.addRequestListener(new DuerUtils.DcsRequestBodyStatusListener() {
            @Override
            public void speechStarted() {
                btnVoiceInput.setText("播报中...");
            }

            @Override
            public void speechFinished() {
                btnVoiceInput.setText("点击说话...");
            }
        });
    }

    private void initView() {
        msgBeanList = new ArrayList<>();

        mainRecyclerView.setHasFixedSize(true);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChatHistoryAdapter(this, msgBeanList);
        mainRecyclerView.setAdapter(adapter);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Constant.CONTINUITY = (boolean) SharedPreferenceUtil.get(MainActivity.this, "xunfei_model", false);
        Log.e(TAG, "连续对话 = " + Constant.CONTINUITY);
    }

    /**
     * @descriptoin 开始录音
     * @author dc
     * @date 2018/7/26 10:45
     */
    private void startVoice() {
        duerUtils.stopSpeech();
        baiduASR.startASR();
        btnVoiceInput.setText("开始录音");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        duerUtils.release();
        baiduASR.releaseASR();
    }

    @OnClick({R.id.btn_voice_input, R.id.main_send_bt, R.id.main_stop_asr_bt, R.id.btn_send,R.id.btn_admin_config})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_voice_input:
                tvAsrResult.setText("");
                startVoice();
                break;
            case R.id.main_send_bt:
                break;
            case R.id.main_stop_asr_bt:
                baiduASR.stopASR();
                btnVoiceInput.setText("点击说话");
//                duerUtils.stopSpeech();
                break;
            case R.id.btn_send:
                String inputText = etInput.getText().toString().trim();
                if (TextUtils.isEmpty(inputText)) {
                    Toast.makeText(MainActivity.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 清空并收起键盘
//                inputEt.getEditableText().clear();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etInput.getWindowToken(), 0);
                duerUtils.sendDuer(inputText);
                break;
            case R.id.btn_admin_config:
                startActivity(new Intent(MainActivity.this, ConfigurationActivity.class));
                break;
        }
    }

    @Override
    public void asrResult(String asr, boolean isFinal) {
        //识别结果、
        inputStr = asr;
        if (isFinal) {
            tvAsrResult.setText(inputStr);
            duerUtils.sendDuer(inputStr);
//            aiuiNlp(inputStr);
            notifyInputMsg(inputStr);
        } else {
            tvAsrResult.setText(inputStr);
        }
    }

    @Override
    public void asrRecording(boolean recording) {
        if (recording) {
            btnVoiceInput.setText("录音中...");
        }
    }

    @Override
    public void asrRecognizing(boolean recognizing) {
        if (recognizing) {
            btnVoiceInput.setText("识别中...");
        }
    }

    @Override
    public void asrThinking(boolean thinking) {
        if (thinking) {
            btnVoiceInput.setText("思考中...");
        }
    }

    @Override
    public void asrFinish(boolean finish) {
        if (finish) {
            btnVoiceInput.setText("点击说话");
            // TODO: dc 2018/7/27 连续对话，test
            /*if(ConstantToken.NLPMODEL.equals(CONTINUITY)){
                baiduASR.startASR();
            }*/
        }
    }

    /**
     * 讯飞nlp
     *
     * @param msg 百度asr或文本框输入
     */
    private void aiuiNlp(String msg) {
        //文本语义
        if (!TextUtils.isEmpty(msg)) {
            AIUINlpUtil.getInstance(this).startTextNlp(msg);
        } else {
            Toast.makeText(this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void aiuiNlpResult(int rc, String result) {
        Log.e(TAG, "AIUI_Result = " + result + "    rc = " + rc);
        if (rc == 4) {
            duerUtils.sendDuer(inputStr);
        } else {
            // AIUI语义结果 
            // TODO: 2018/9/14 为了兼容DuerOS的平台效果暂时，需要传入DuerOS结果对象，因AIUI没有该对象所以传null，注意在解析的时候需要做判断
            notifyOutputMsg(null, result);

            duerUtils.startSpeech(result);
        }
    }

    @Override
    public void onRenDerCardResult(RenderCardPayload renderCardPayload) {
        Log.e(TAG, "onRenDerCardResult: " + renderCardPayload.content);
//        btnVoiceInput.setText("播报中...");
        RenderCardPayload.Type type = renderCardPayload.type;

        String content = renderCardPayload.content;
        if (TextUtils.isEmpty(content)) {
            content = "暂不支持该技能";
        }

        notifyOutputMsg(renderCardPayload, content);

        // 判断type


    }


    /**
     * 在语义分析之前，将输入信息更新到界面上
     *
     * @param input 百度asr输入或输入框输入
     */
    private void notifyInputMsg(String input) {
        mainInputEdit.setText("");


        Log.e(TAG, "识别内容 = " + input);
        MsgBean<String> msgBean = new MsgBean(input, MsgBean.INPUT_TYPE);
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        msgBean.setChatTime(sdf.format(now));
        msgBeanList.add(msgBean);
        adapter.notifyItemInserted(msgBeanList.size() - 1);
        adapter.notifyItemRangeChanged(0, msgBeanList.size());
        mainRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    /**
     * 在语义分析结束之后，将输入信息更新到界面上，并播放百度tts
     *
     * @param output 灵聚nlp或者讯飞nlp识别结果
     */
    private void notifyOutputMsg(RenderCardPayload type, String output) {
        btnVoiceInput.setText("点击说话...");

        // TODO: 2018/9/17 是否联系对话
        if (CONTINUITY) {
            baiduASR.startASR();
        }

        String content = output;
        MsgBean<String> msgBean = new MsgBean(output, type, MsgBean.OUTPUT_TYPE);
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        msgBean.setChatTime(sdf.format(now));
        msgBeanList.add(msgBean);
        Log.e(TAG, "item size " + (adapter.getItemCount() - 1));
        adapter.notifyItemInserted(msgBeanList.size() - 1);
        adapter.notifyItemRangeChanged(0, msgBeanList.size());
        mainRecyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }


}
