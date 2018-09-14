package com.dc.duertest.bean;

/**
 * 描述：StandardCard(百科)\航班\高铁
 * 作者：dc on 2018/1/26 14:55
 * 邮箱：597210600@qq.com
 */
public class StandardCardBean {


    /**
     * header : {"namespace":"ai.dueros.device_interface.screen","name":"RenderCard","dialogRequestId":"d007d6a4-9bd4-4667-a083-48c8d8aae9bb","messageId":"NWE2YWQwZWU0MWEyZjUzNDM="}
     * payload : {"type":"StandardCard","image":{"src":"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3354453085,3795415816&fm=58&w=255&h=255&img.PNG&bpow=600&bpoh=396"},"title":"周杰伦","content":"周杰伦（Jay Chou），1979年1月18日出生于台湾省新北市，中国台湾流行乐男歌手、音乐人、演员、导演、编剧、监制、商人。2000年发行首张个人专辑《Jay》。2001年发行的专辑《范特西》奠定其融合中西方音乐的风格。2002年举行\u201cThe One\u201d世界巡回演唱会。2003年成为美国《时代周刊》封面人物。2004年获得世界音乐大奖中国区最畅销艺人奖。2005年凭借动作片《头文字D》获得台湾电影金马奖、香港电影金像奖最佳新人奖。2006年起连续三年获得世界音乐大奖中国区最畅销艺人奖。2007年自编自导的文艺片《不能说的秘密》获得台湾电影金马奖年度台湾杰出电影奖。2008年凭借歌曲《青花瓷》获得第19届金曲奖最佳作曲人奖。2009年入选美国CNN评出的\u201c25位亚洲最具影响力的人物\u201d；同年凭借专辑《魔杰座》获得第20届金曲奖最佳国语男歌手奖","link":{"url":"https://baike.baidu.com/item/%E5%91%A8%E6%9D%B0%E4%BC%A6/129156","anchorText":"查看更多"},"token":"eyJib3RfaWQiOiJ1cyIsInJlc3VsdF90b2tlbiI6IjNkODYwMDA5LWQ0N2YtNDljYi1iNDUwLTdkZjg5ODBhZWU0YiIsImJvdF90b2tlbiI6Im51bGwifQ=="}
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
         * dialogRequestId : d007d6a4-9bd4-4667-a083-48c8d8aae9bb
         * messageId : NWE2YWQwZWU0MWEyZjUzNDM=
         */

        private HeaderBean header;
        /**
         * type : StandardCard
         * image : {"src":"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3354453085,3795415816&fm=58&w=255&h=255&img.PNG&bpow=600&bpoh=396"}
         * title : 周杰伦
         * content : 周杰伦（Jay Chou），1979年1月18日出生于台湾省新北市，中国台湾流行乐男歌手、音乐人、演员、导演、编剧、监制、商人。2000年发行首张个人专辑《Jay》。2001年发行的专辑《范特西》奠定其融合中西方音乐的风格。2002年举行“The One”世界巡回演唱会。2003年成为美国《时代周刊》封面人物。2004年获得世界音乐大奖中国区最畅销艺人奖。2005年凭借动作片《头文字D》获得台湾电影金马奖、香港电影金像奖最佳新人奖。2006年起连续三年获得世界音乐大奖中国区最畅销艺人奖。2007年自编自导的文艺片《不能说的秘密》获得台湾电影金马奖年度台湾杰出电影奖。2008年凭借歌曲《青花瓷》获得第19届金曲奖最佳作曲人奖。2009年入选美国CNN评出的“25位亚洲最具影响力的人物”；同年凭借专辑《魔杰座》获得第20届金曲奖最佳国语男歌手奖
         * link : {"url":"https://baike.baidu.com/item/%E5%91%A8%E6%9D%B0%E4%BC%A6/129156","anchorText":"查看更多"}
         * token : eyJib3RfaWQiOiJ1cyIsInJlc3VsdF90b2tlbiI6IjNkODYwMDA5LWQ0N2YtNDljYi1iNDUwLTdkZjg5ODBhZWU0YiIsImJvdF90b2tlbiI6Im51bGwifQ==
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
            /**
             * src : https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3354453085,3795415816&fm=58&w=255&h=255&img.PNG&bpow=600&bpoh=396
             */

            private ImageBean image;
            private String title;
            private String content;
            /**
             * url : https://baike.baidu.com/item/%E5%91%A8%E6%9D%B0%E4%BC%A6/129156
             * anchorText : 查看更多
             */

            private LinkBean link;
            private String token;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public ImageBean getImage() {
                return image;
            }

            public void setImage(ImageBean image) {
                this.image = image;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public LinkBean getLink() {
                return link;
            }

            public void setLink(LinkBean link) {
                this.link = link;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public static class ImageBean {
                private String src;

                public String getSrc() {
                    return src;
                }

                public void setSrc(String src) {
                    this.src = src;
                }
            }

            public static class LinkBean {
                private String url;
                private String anchorText;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getAnchorText() {
                    return anchorText;
                }

                public void setAnchorText(String anchorText) {
                    this.anchorText = anchorText;
                }
            }
        }
    }
}
