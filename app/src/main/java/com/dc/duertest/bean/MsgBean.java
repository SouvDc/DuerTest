package com.dc.duertest.bean;

import com.dc.duer.sdk.devicemodule.screen.message.RenderCardPayload;

/**
 * Created by deng jia on 2018/7/25.
 */

public class MsgBean<T> {
    public static final int INPUT_TYPE = 0;
    public static final int OUTPUT_TYPE = 1;

    private RenderCardPayload renderCardPayload; //duerOS数据类型
    private String content;
    private int type;           //数据结构类型
    private String chatTime;	//时间

    // output
    public MsgBean(String ctx, RenderCardPayload cardPayload, int t) {
        this.content = ctx;
        this.renderCardPayload = cardPayload;
        this.type = t;
    }

    // input
    public MsgBean(String ctx, int t) {
        this.content = ctx;
        this.type = t;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public RenderCardPayload getRenderCardPayload() {
        return renderCardPayload;
    }

    public void setRenderCardPayload(RenderCardPayload renderCardPayload) {
        this.renderCardPayload = renderCardPayload;
    }

    public String getChatTime() {
        return chatTime == null ? "" : chatTime;
    }

    public void setChatTime(String chatTime) {
        this.chatTime = chatTime;
    }
}
