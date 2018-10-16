package com.dc.duer.sdk.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：ImageListCard类型的返回结果
 * 作者：dc on 2018/1/26 14:54
 * 邮箱：597210600@qq.com
 */
public class ImageListCardBean {


    /**
     * header : {"namespace":"ai.dueros.device_interface.screen","name":"RenderCard","dialogRequestId":"916f7ff8-e9c7-4c9e-9bc7-c3fdd80bbf81","messageId":"NWE2YWQwODUyNGU1NTkxNw=="}
     * payload : {"type":"ImageListCard","imageList":[{"src":"http://img5.duitang.com/uploads/item/201501/17/20150117230303_RFeVm.jpeg"},{"src":"https://ss1.baidu.com/70cFfyinKgQFm2e88IuM_a/forum/pic/item/d50735fae6cd7b8965ffe6c60f2442a7d8330ee0.jpg"},{"src":"http://pic16.photophoto.cn/20100801/0036036385989333_b.jpg"},{"src":"http://img2.jc001.cn/img/001/1/1245660240619.jpg"},{"src":"http://img5.duitang.com/uploads/item/201411/09/20141109225823_U4kEe.jpeg"},{"src":"http://img5.duitang.com/uploads/item/201508/24/20150824213254_aFSGE.jpeg"},{"src":"http://img4.duitang.com/uploads/item/201508/13/20150813215938_RUeY4.jpeg"},{"src":"http://img6.blog.eastmoney.com/wf/wfcx123/201002/20100219213136530.jpg"},{"src":"http://image82.360doc.com/DownloadImg/2015/02/1220/50124496_81.jpg"},{"src":"http://img.hkwb.net/att/site2/20110621/986b8c18946db5f76567d0af930d3595.jpg"},{"src":"http://news.cctv.com/20080317/images/1205740575014_8788673725539539109.jpg"},{"src":"http://img5.duitang.com/uploads/item/201603/14/20160314152811_3QTnY.jpeg"},{"src":"http://img5.duitang.com/uploads/item/201412/04/20141204144234_ZKXkw.thumb.700_0.jpeg"},{"src":"http://c4.haibao.cn/img/600_0_100_0/1515734015.5695/8ec1a7f248e06875ef1c2fb161623259.jpg"},{"src":"http://cdn.duitang.com/uploads/blog/201505/30/20150530211945_vZMxT.jpeg"},{"src":"http://img3.duitang.com/uploads/item/201601/12/20160112140212_dVFMc.jpeg"},{"src":"https://ss1.baidu.com/70cFfyinKgQFm2e88IuM_a/forum/w=580/sign=0aa87d5f8982b9013dadc33b438ca97e/05ee5366d01609246e4cf463d70735fae6cd3474.jpg"},{"src":"http://t-1.tuzhan.com/572ace899953/c-1/l/2012/09/20/21/2200cbe99dcd435cacff56a05bdd4ffd.jpg"},{"src":"http://pic5.nipic.com/20100126/4264500_160313021216_2.jpg"},{"src":"https://ss1.baidu.com/70cFfyinKgQFm2e88IuM_a/forum/pic/item/d8dd5bb5c9ea15cea1a432fdb6003af33887b2e7.jpg"}],"token":"eyJib3RfaWQiOiJ1cyIsInJlc3VsdF90b2tlbiI6IjJkMDBkMWYzLTMxYzktNDc4YS05NGU3LTI5MzQ2YzNkYjM4YyIsImJvdF90b2tlbiI6Im51bGwifQ=="}
     */

    private DirectiveBean directive;

    public DirectiveBean getDirective() {
        return directive;
    }

    public void setDirective(DirectiveBean directive) {
        this.directive = directive;
    }

    public static class DirectiveBean {
        /**
         * namespace : ai.dueros.device_interface.screen
         * name : RenderCard
         * dialogRequestId : 916f7ff8-e9c7-4c9e-9bc7-c3fdd80bbf81
         * messageId : NWE2YWQwODUyNGU1NTkxNw==
         */

        private HeaderBean header;
        /**
         * type : ImageListCard
         * imageList : [{"src":"http://img5.duitang.com/uploads/item/201501/17/20150117230303_RFeVm.jpeg"},{"src":"https://ss1.baidu.com/70cFfyinKgQFm2e88IuM_a/forum/pic/item/d50735fae6cd7b8965ffe6c60f2442a7d8330ee0.jpg"},{"src":"http://pic16.photophoto.cn/20100801/0036036385989333_b.jpg"},{"src":"http://img2.jc001.cn/img/001/1/1245660240619.jpg"},{"src":"http://img5.duitang.com/uploads/item/201411/09/20141109225823_U4kEe.jpeg"},{"src":"http://img5.duitang.com/uploads/item/201508/24/20150824213254_aFSGE.jpeg"},{"src":"http://img4.duitang.com/uploads/item/201508/13/20150813215938_RUeY4.jpeg"},{"src":"http://img6.blog.eastmoney.com/wf/wfcx123/201002/20100219213136530.jpg"},{"src":"http://image82.360doc.com/DownloadImg/2015/02/1220/50124496_81.jpg"},{"src":"http://img.hkwb.net/att/site2/20110621/986b8c18946db5f76567d0af930d3595.jpg"},{"src":"http://news.cctv.com/20080317/images/1205740575014_8788673725539539109.jpg"},{"src":"http://img5.duitang.com/uploads/item/201603/14/20160314152811_3QTnY.jpeg"},{"src":"http://img5.duitang.com/uploads/item/201412/04/20141204144234_ZKXkw.thumb.700_0.jpeg"},{"src":"http://c4.haibao.cn/img/600_0_100_0/1515734015.5695/8ec1a7f248e06875ef1c2fb161623259.jpg"},{"src":"http://cdn.duitang.com/uploads/blog/201505/30/20150530211945_vZMxT.jpeg"},{"src":"http://img3.duitang.com/uploads/item/201601/12/20160112140212_dVFMc.jpeg"},{"src":"https://ss1.baidu.com/70cFfyinKgQFm2e88IuM_a/forum/w=580/sign=0aa87d5f8982b9013dadc33b438ca97e/05ee5366d01609246e4cf463d70735fae6cd3474.jpg"},{"src":"http://t-1.tuzhan.com/572ace899953/c-1/l/2012/09/20/21/2200cbe99dcd435cacff56a05bdd4ffd.jpg"},{"src":"http://pic5.nipic.com/20100126/4264500_160313021216_2.jpg"},{"src":"https://ss1.baidu.com/70cFfyinKgQFm2e88IuM_a/forum/pic/item/d8dd5bb5c9ea15cea1a432fdb6003af33887b2e7.jpg"}]
         * token : eyJib3RfaWQiOiJ1cyIsInJlc3VsdF90b2tlbiI6IjJkMDBkMWYzLTMxYzktNDc4YS05NGU3LTI5MzQ2YzNkYjM4YyIsImJvdF90b2tlbiI6Im51bGwifQ==
         */

        private PayloadBean payload;

        public HeaderBean getHeader() {
            return header;
        }

        public void setHeader(HeaderBean header) {
            this.header = header;
        }

        public PayloadBean getPayload() {
            return payload;
        }

        public void setPayload(PayloadBean payload) {
            this.payload = payload;
        }

        public static class HeaderBean {
            private String namespace;
            private String name;
            private String dialogRequestId;
            private String messageId;

            public String getNamespace() {
                return namespace;
            }

            public void setNamespace(String namespace) {
                this.namespace = namespace;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDialogRequestId() {
                return dialogRequestId;
            }

            public void setDialogRequestId(String dialogRequestId) {
                this.dialogRequestId = dialogRequestId;
            }

            public String getMessageId() {
                return messageId;
            }

            public void setMessageId(String messageId) {
                this.messageId = messageId;
            }
        }

        public static class PayloadBean {
            private String type;
            private String token;
            /**
             * src : http://img5.duitang.com/uploads/item/201501/17/20150117230303_RFeVm.jpeg
             */

            private List<ImageListBean> imageList;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public List<ImageListBean> getImageList() {
                return imageList;
            }

            public void setImageList(List<ImageListBean> imageList) {
                this.imageList = imageList;
            }

            public static class ImageListBean implements Serializable {
                private String src;

                public String getSrc() {
                    return src;
                }

                public void setSrc(String src) {
                    this.src = src;
                }
            }
        }
    }
}
