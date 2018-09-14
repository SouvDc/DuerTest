package com.dc.duertest.bean;

/**
 * 描述：TextCard类型的返回结果
 * 作者：dc on 2018/1/26 14:36
 * 邮箱：597210600@qq.com
 */
public class TextCardBean {


    /**
     * header : {"namespace":"ai.dueros.device_interface.screen","name":"RenderCard","dialogRequestId":"04c3acfc-07f7-4adc-89de-9c956213cf1a","messageId":"NWE2YWNjMGUzOTRkZTE5NDc="}
     * payload : {"type":"TextCard","content":"你好，老板。咱们聊一会呀","token":"eyJib3RfaWQiOiJ1cyIsInJlc3VsdF90b2tlbiI6IjQzN2YxZDg3LWY2MDYtNDNiMC05ODdkLWM0ZDRlNTBhOWUzZSIsImJvdF90b2tlbiI6Im51bGwifQ=="}
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
         * dialogRequestId : 04c3acfc-07f7-4adc-89de-9c956213cf1a
         * messageId : NWE2YWNjMGUzOTRkZTE5NDc=
         */

        private HeaderBean header;
        /**
         * type : TextCard
         * content : 你好，老板。咱们聊一会呀
         * token : eyJib3RfaWQiOiJ1cyIsInJlc3VsdF90b2tlbiI6IjQzN2YxZDg3LWY2MDYtNDNiMC05ODdkLWM0ZDRlNTBhOWUzZSIsImJvdF90b2tlbiI6Im51bGwifQ==
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
            private String content;
            private String token;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }
        }
    }
}
