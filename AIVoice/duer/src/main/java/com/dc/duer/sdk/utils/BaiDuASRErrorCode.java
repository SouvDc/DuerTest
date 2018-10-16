package com.dc.duer.sdk.utils;

/**
 * 描述：
 * 作者：dc on 2018/9/17 15:19
 * 邮箱：597210600@qq.com
 */
public class BaiDuASRErrorCode {

    public static String asrErrorCode(int error){
        String errorStr = "未知错误" + error;
        switch (error){
            //	出现原因可能为网络已经连接但质量比较差，建议检测网络状态
            case 1000:
                errorStr = "DNS连接超时";
                break;
            case 1001:
                errorStr = "网络连接超时";
                break;
            case 1002:
                errorStr = "网络读取超时";
                break;
            case 1003:
                errorStr = "上行网络连接超时";
                break;
            case 1004:
                errorStr = "上行网络读取超时";
                break;
            case 1005:
                errorStr = "下行网络连接超时";
                break;
            case 1006:
                errorStr = "下行网络读取超时";
                break;

            // 出现原因可能是网络权限被禁用，或网络确实未连接，需要开启网络或检测无法联网的原因
            case 2000:
                errorStr = "网络连接失败";
                break;
            case 2001:
                errorStr = "网络读取失败";
                break;
            case 2002:
                errorStr = "上行网络连接失败";
                break;
            case 2003:
                errorStr = "上行网络读取失败";
                break;
            case 2004:
                errorStr = "下行网络连接失败";
                break;
            case 2005:
                errorStr = "下行网络读取失败";
                break;
            case 2006:
                errorStr = "下行数据异常";
                break;
            case 2100:
                errorStr = "本地网络不可用";
                break;

            // 出现原因可能为：未声明录音权限，或 被安全软件限制，或 录音设备被占用，需要开发者检测权限声明。
            case 3001:
                errorStr = "录音机打开失败";
                break;
            case 3002:
                errorStr = "录音机参数错误";
                break;
            case 3003:
                errorStr = "录音机不可用";
                break;
            case 3006:
                errorStr = "录音机读取失败";
                break;
            case 3007:
                errorStr = "录音机关闭失败";
                break;
            case 3008:
                errorStr = "文件打开失败";
                break;
            case 3009:
                errorStr = "文件读取失败";
                break;
            case 3010:
                errorStr = "文件关闭失败";
                break;
            case 3100:
                errorStr = "VAD异常，通常是VAD资源设置不正确";
                break;
            case 3101:
                errorStr = "长时间未检测到人说话，请重新识别";
                break;
            case 3102:
                errorStr = "检测到人说话，但语音过短";
                break;

                //出现原因可能是appid和appkey的鉴权失败
            case 4001:
                errorStr = "协议出错";
                break;
            case 4002:
                errorStr = "协议出错";
                break;
            case 4003:
                errorStr = "识别出错";
                break;
            case 4004:
                errorStr = "鉴权错误 ，一般情况是pid appkey secretkey不正确";
                break;

                //一般是开发阶段的调用错误，需要开发者检测调用逻辑或对照文档和demo进行修复。

        }



        return errorStr;
    }
}
