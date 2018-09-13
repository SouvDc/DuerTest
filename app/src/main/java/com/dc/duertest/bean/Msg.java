package com.dc.duertest.bean;

/**
 * Created by deng jia on 2018/7/25.
 */

public class Msg {
    public static final int INPUT_TYPE = 0;
    public static final int OUTPUT_TYPE = 1;

    private String message;
    private int type;

    public Msg(String message, int type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
