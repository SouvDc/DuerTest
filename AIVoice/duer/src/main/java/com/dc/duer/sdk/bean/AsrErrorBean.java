package com.dc.duer.sdk.bean;

/**
 * 描述：
 * 作者：dc on 2018/9/8 15:40
 * 邮箱：597210600@qq.com
 */
public class AsrErrorBean {


    /**
     * sn : cuid=03D3AA80EEE1411EE923CCF899145B41|0&sn=11ee51d5-02f7-4ba6-b26b-9f85f2179b1e&nettype=4
     * error : 3
     * desc : VAD detect no speech
     * sub_error : 3101
     */

    private OriginResultBean origin_result;
    /**
     * origin_result : {"sn":"cuid=03D3AA80EEE1411EE923CCF899145B41|0&sn=11ee51d5-02f7-4ba6-b26b-9f85f2179b1e&nettype=4","error":3,"desc":"VAD detect no speech","sub_error":3101}
     * error : 3
     * desc : VAD detect no speech
     * sub_error : 3101
     */

    private int error;
    private String desc;
    private int sub_error;

    public OriginResultBean getOrigin_result() {
        return origin_result;
    }

    public void setOrigin_result(OriginResultBean origin_result) {
        this.origin_result = origin_result;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSub_error() {
        return sub_error;
    }

    public void setSub_error(int sub_error) {
        this.sub_error = sub_error;
    }

    public static class OriginResultBean {
        private String sn;
        private int error;
        private String desc;
        private int sub_error;

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public int getError() {
            return error;
        }

        public void setError(int error) {
            this.error = error;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public int getSub_error() {
            return sub_error;
        }

        public void setSub_error(int sub_error) {
            this.sub_error = sub_error;
        }
    }
}
