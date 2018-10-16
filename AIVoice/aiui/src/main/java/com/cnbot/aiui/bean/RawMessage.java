package com.cnbot.aiui.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.text.TextUtils;

import java.nio.ByteBuffer;

/**
 * AIUI聊天原始数据
 */
@Entity
public class RawMessage {
    public enum MsgType {
        TEXT, Voice
    }

    public enum FromType {
        USER, AIUI
    }

    @PrimaryKey
    public long timestamp;
    public long responeTime;
    public FromType fromType;
    public MsgType msgType;
    public String cacheContent;
    public byte[] msgData;

    public RawMessage(FromType fromType, MsgType msgType, byte[] msgData, String cacheContent
            , long timestamp, long responeTime) {
        this.fromType = fromType;
        this.msgType = msgType;
        this.msgData = msgData;
        this.timestamp = timestamp;
        this.responeTime = responeTime;
        this.cacheContent = cacheContent;
    }

    @Ignore
    public RawMessage(FromType fromType, MsgType msgType, byte[] msgData) {
        this(fromType, msgType, msgData, null, System.currentTimeMillis(), 0);
    }

    public boolean isText() {
        return msgType == MsgType.TEXT;
    }

    public boolean isEmptyContent() {return TextUtils.isEmpty(cacheContent);}

    public boolean isFromUser() {
        return fromType == FromType.USER;
    }

    public int getAudioLen() {
        if(msgType == MsgType.Voice){
            return Math.round(ByteBuffer.wrap(msgData).getFloat());
        } else {
            return 0;
        }
    }
}
