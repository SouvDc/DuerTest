package com.cnbot.aiui;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import com.cnbot.aiui.bean.AiuiNlpResult;
import com.cnbot.aiui.bean.RawMessage;
import com.cnbot.aiui.bean.SemanticResult;
import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import static com.cnbot.aiui.bean.RawMessage.FromType.AIUI;
import static com.cnbot.aiui.bean.RawMessage.MsgType.TEXT;


/**
 * Created by deng jia on 2018/7/23.
 */

public class AIUINlpUtil {
    private static final String TAG = AIUINlpUtil.class.getSimpleName();

    //定义对象
    private static AIUINlpUtil sInstance;
    private AIUIAgent mAIUIAgent = null;
    private Context mContext;
    private AiuiNlpResult mResultListener;

    /**                  语义结果             **/
    private SemanticResult parsedSemanticResult;
    private static final String KEY_SEMANTIC = "semantic";
    private static final String KEY_OPERATION = "operation";
    private static final String SLOTS = "slots";

    private AIUINlpUtil(Context context) {
        mContext = context;
    }

    public static AIUINlpUtil getInstance(Context context) {
        if (sInstance == null) {
            synchronized (AIUINlpUtil.class) {
                if (sInstance == null) {
                    sInstance = new AIUINlpUtil(context);
                }
            }
        }
        return sInstance;
    }

    public void createAgent() {
        if (null == mAIUIAgent) {
            Log.i("main", "create aiui agent");
            mAIUIAgent = AIUIAgent.createAgent(mContext, getAIUIParams(), mAIUIListener);
        }
        if (null == mAIUIAgent) {
            Log.i(TAG, "创建AIUIAgent失败！");
        } else {
            Log.i(TAG, "AIUIAgent已创建");
        }
    }

    public void startTextNlp(String text) {
        if (null == mAIUIAgent) {
            Log.i(TAG, "AIUIAgent 为空，请先创建");
            return;
        }

        // 先发送唤醒消息，改变AIUI内部状态，只有唤醒状态才能接收文本输入
//		if (AIUIConstant.STATE_WORKING != mAIUIState)
        {
            AIUIMessage wakeupMsg = new AIUIMessage(AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
            mAIUIAgent.sendMessage(wakeupMsg);
        }

        Log.i(TAG, "start text nlp");

        try {
            // 在输入参数中设置tag，则对应结果中也将携带该tag，可用于关联输入输出
            String params = "data_type=text,tag=text-tag";
            byte[] textData = text.getBytes("utf-8");

            AIUIMessage write = new AIUIMessage(AIUIConstant.CMD_WRITE, 0, 0, params, textData);
            mAIUIAgent.sendMessage(write);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String getAIUIParams() {
        String params = "";

        AssetManager assetManager = mContext.getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];

            ins.read(buffer);
            ins.close();

            params = new String(buffer);

            JSONObject paramsJson = new JSONObject(params);

            params = paramsJson.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    /**
     * 处理AIUI事件结果（语义结果）
     * @param event 结果事件
     */
    private AIUIListener mAIUIListener = new AIUIListener() {

        @Override
        public void onEvent(AIUIEvent event) {
            Log.i( TAG,  "on event: " + event.eventType );

            switch (event.eventType) {
                case AIUIConstant.EVENT_CONNECTED_TO_SERVER:
                    Log.i( TAG,  "connected to server: " + "已连接aiui服务器" );
                    break;

                case AIUIConstant.EVENT_SERVER_DISCONNECTED:
                    Log.i( TAG,  "server disconnected: " + "与服务器断连" );
                    break;

                case AIUIConstant.EVENT_WAKEUP:
                    Log.i( TAG,  "wake: " + "进入识别状态" );
                    break;

                case AIUIConstant.EVENT_RESULT: {
                    try {
                        JSONObject bizParamJson = new JSONObject(event.info);
                        JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
                        JSONObject params = data.getJSONObject("params");
                        JSONObject content = data.getJSONArray("content").getJSONObject(0);

                        long rspTime = event.data.getLong("eos_rslt", -1);  //响应时间
                        if (content.has("cnt_id")) {
                            String cnt_id = content.getString("cnt_id");
                            String cntStr = new String(event.data.getByteArray(cnt_id), "utf-8");

                            // 获取该路会话的id，将其提供给支持人员，有助于问题排查
                            // 也可以从Json结果中看到
                            String sid = event.data.getString("sid");
                            String tag = event.data.getString("tag");

                            Log.i( TAG,  "tag = " + tag );

                            // 获取从数据发送完到获取结果的耗时，单位：ms
                            // 也可以通过键名"bos_rslt"获取从开始发送数据到获取结果的耗时
                            long eosRsltTime = event.data.getLong("eos_rslt", -1);

                            if (TextUtils.isEmpty(cntStr)) {
                                return;
                            }

                            JSONObject cntJson = new JSONObject(cntStr);

                            String sub = params.optString("sub");
                            if ("nlp".equals(sub)) {
                                // 解析得到语义结果
                                JSONObject semanticResult = cntJson.optJSONObject("intent");
                                if (semanticResult != null && semanticResult.length() != 0) {
                                    //解析得到语义结果，将语义结果作为消息插入到消息列表中
                                    RawMessage rawMessage = new RawMessage(AIUI, TEXT,
                                            semanticResult.toString().getBytes(), null, System.currentTimeMillis(), rspTime);
                                    /*etNlp.setText(rawMessage.msgData.);*/
                                    nlpResult(rawMessage);
                                }
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        Log.i( TAG,  "异常" + "\n"  + e.getLocalizedMessage());
                    }
                } break;

                case AIUIConstant.EVENT_ERROR: {
                    Log.i( TAG,  "错误: " + event.arg1);
                } break;

                case AIUIConstant.EVENT_VAD: {
                    if (AIUIConstant.VAD_BOS == event.arg1) {
                        Log.i( TAG,  "找到vad_bos");
                    } else if (AIUIConstant.VAD_EOS == event.arg1) {
                        Log.i( TAG,  "找到vad_bos");
                    } else {
                        Log.i( TAG,  "" + event.arg2);
                    }
                } break;
                default:
                    break;
            }
        }

    };


    /**
     * 解析AIUI返回结果
     * @param mMessage 返回结果
     */
    public void nlpResult(RawMessage mMessage){
        String text = "";
        JSONObject semanticResult;
        parsedSemanticResult = new SemanticResult();
        try {
            semanticResult = new JSONObject(new String(mMessage.msgData));
            int rc = parsedSemanticResult.rc = semanticResult.optInt("rc");
            if (rc == 4) {
                parsedSemanticResult.service = "unknown";
                text = "你好，我不懂你的意思";
            } else if (rc == 1) {
                parsedSemanticResult.service = semanticResult.optString("service");
                parsedSemanticResult.answer = "语义错误";
                text = "我听不太懂你在说什么";
            } else {//rc不是4或1
                parsedSemanticResult.service = semanticResult.optString("service");
                parsedSemanticResult.answer = semanticResult.optJSONObject("answer") == null ?
                        "已为您完成操作" : semanticResult.optJSONObject("answer").optString("text");
                // 兼容3.1和4.0的语义结果，通过判断结果最外层的operation字段
                boolean isAIUI3_0 = semanticResult.has(KEY_OPERATION);
                if (isAIUI3_0) {
                    //将3.1语义格式的语义转换成4.1
                    JSONObject semantic = semanticResult.optJSONObject(KEY_SEMANTIC);
                    if (semantic != null) {
                        JSONObject slots = semantic.optJSONObject(SLOTS);
                        JSONArray fakeSlots = new JSONArray();
                        Iterator<String> keys = slots.keys();
                        while (keys.hasNext()) {
                            JSONObject item = new JSONObject();
                            String name = keys.next();
                            item.put("name", name);
                            item.put("value", slots.get(name));

                            fakeSlots.put(item);
                        }

                        semantic.put(SLOTS, fakeSlots);
                        semantic.put("intent", semanticResult.optString(KEY_OPERATION));
                        parsedSemanticResult.semantic = semantic;
                    }
                } else {
                    parsedSemanticResult.semantic = semanticResult.optJSONArray(KEY_SEMANTIC) == null ?
                            semanticResult.optJSONObject(KEY_SEMANTIC) :
                            semanticResult.optJSONArray(KEY_SEMANTIC).optJSONObject(0);
                }
                parsedSemanticResult.answer = parsedSemanticResult.answer.replaceAll("\\[[a-zA-Z0-9]{2}\\]", "");
                parsedSemanticResult.data = semanticResult.optJSONObject("data");
                text =  parsedSemanticResult.answer;
//
            }
            mResultListener.aiuiNlpResult(rc, text);
        } catch (JSONException e) {
            parsedSemanticResult.rc = 4;
            parsedSemanticResult.service = "unknown";
        }
    }

    public void setListener(AiuiNlpResult resultListener) {
        mResultListener = resultListener;
    }
}
