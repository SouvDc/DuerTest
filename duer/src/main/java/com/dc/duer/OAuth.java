package com.dc.duer;

import com.baidu.duer.dcs.oauth.api.silent.SilentLoginImpl;
import com.baidu.duer.dcs.systeminterface.IOauth;

/**
 * 描述：
 * 作者：dc on 2018/9/10 14:52
 * 邮箱：597210600@qq.com
 */
public class OAuth {

    public static IOauth getOauth(String CLIENT_ID) {
        return new SilentLoginImpl(CLIENT_ID);
    }
}
