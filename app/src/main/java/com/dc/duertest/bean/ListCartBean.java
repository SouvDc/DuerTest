package com.dc.duertest.bean;

import java.util.List;

/**
 * 描述：电影、美食等介绍
 * 作者：dc on 2018/1/26 14:57
 * 邮箱：597210600@qq.com
 */
public class ListCartBean {

    /**
     * header : {"namespace":"ai.dueros.device_interface.screen","name":"RenderCard","dialogRequestId":"14d864e1-1e3d-4d02-9730-c7f3263eb243","messageId":"NWE2YWQxN2NmMWVhMjgwMDc="}
     * payload : {"type":"ListCard","list":[{"image":{"src":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1692762530,1901134749&fm=58&s=6DD07C8C1AB39BF346982C9E03005082"},"title":"惊天魔盗团2","url":"https://m.baidu.com/s?word=惊天魔盗团2"},{"image":{"src":"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1782891699,1828052137&fm=58&s=71CC67A68E126DF54A3B57730300505A"},"title":"满城尽带黄金甲","url":"https://m.baidu.com/s?word=满城尽带黄金甲"},{"image":{"src":"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=935927911,1383629229&fm=58&s=D7383EC75A61460D0C3235370300C048"},"title":"不能说的秘密","url":"https://m.baidu.com/s?word=不能说的秘密"},{"image":{"src":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=879576938,1568916453&fm=58&s=4601D907DAF137ADF4BC5CA00300E060"},"title":"苏乞儿","url":"https://m.baidu.com/s?word=苏乞儿"},{"image":{"src":"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=1693552654,1963515046&fm=58&s=F7A48EAE0AF28BE102B0ADBC03008008"},"title":"天台爱情","url":"https://m.baidu.com/s?word=天台爱情"},{"image":{"src":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=990317396,1744737154&fm=58&s=E0202AF4540B22E6560276770300805C"},"title":"头文字d","url":"https://m.baidu.com/s?word=头文字d"},{"image":{"src":"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3304318341,4109936684&fm=58&s=EE164F854022EEE85620909F030050E9"},"title":"自行我路","url":"https://m.baidu.com/s?word=自行我路"},{"image":{"src":"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2893384936,3664625227&fm=58&s=2CB069948CF23894C0ACD8230300F0C3"},"title":"双刀","url":"https://m.baidu.com/s?word=双刀"}],"link":{"url":"https://m.baidu.com/sf?pd=movie_general&word=%E5%91%A8%E6%9D%B0%E4%BC%A6%E6%BC%94%E8%BF%87%E4%BB%80%E4%B9%88%E7%94%B5%E5%BD%B1&sort_key=%E6%9C%80%E7%83%AD&from_sf=1&openapi=1&resource_id=28261&stat0=&stat1=&ms=1&dspName=iphone&top=%7B%22sfhs%22%3A2%7D","anchorText":"查看更多"},"token":"eyJib3RfaWQiOiJ1cyIsInJlc3VsdF90b2tlbiI6ImUyMTFiZWEyLWVkNDgtNGU3MS1iZWJjLTRjZTYyMTcyNWUxMSIsImJvdF90b2tlbiI6Im51bGwifQ=="}
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
         * dialogRequestId : 14d864e1-1e3d-4d02-9730-c7f3263eb243
         * messageId : NWE2YWQxN2NmMWVhMjgwMDc=
         */

        private HeaderBean header;
        /**
         * type : ListCard
         * list : [{"image":{"src":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1692762530,1901134749&fm=58&s=6DD07C8C1AB39BF346982C9E03005082"},"title":"惊天魔盗团2","url":"https://m.baidu.com/s?word=惊天魔盗团2"},{"image":{"src":"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=1782891699,1828052137&fm=58&s=71CC67A68E126DF54A3B57730300505A"},"title":"满城尽带黄金甲","url":"https://m.baidu.com/s?word=满城尽带黄金甲"},{"image":{"src":"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=935927911,1383629229&fm=58&s=D7383EC75A61460D0C3235370300C048"},"title":"不能说的秘密","url":"https://m.baidu.com/s?word=不能说的秘密"},{"image":{"src":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=879576938,1568916453&fm=58&s=4601D907DAF137ADF4BC5CA00300E060"},"title":"苏乞儿","url":"https://m.baidu.com/s?word=苏乞儿"},{"image":{"src":"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=1693552654,1963515046&fm=58&s=F7A48EAE0AF28BE102B0ADBC03008008"},"title":"天台爱情","url":"https://m.baidu.com/s?word=天台爱情"},{"image":{"src":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=990317396,1744737154&fm=58&s=E0202AF4540B22E6560276770300805C"},"title":"头文字d","url":"https://m.baidu.com/s?word=头文字d"},{"image":{"src":"https://ss0.baidu.com/6ONWsjip0QIZ8tyhnq/it/u=3304318341,4109936684&fm=58&s=EE164F854022EEE85620909F030050E9"},"title":"自行我路","url":"https://m.baidu.com/s?word=自行我路"},{"image":{"src":"https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=2893384936,3664625227&fm=58&s=2CB069948CF23894C0ACD8230300F0C3"},"title":"双刀","url":"https://m.baidu.com/s?word=双刀"}]
         * link : {"url":"https://m.baidu.com/sf?pd=movie_general&word=%E5%91%A8%E6%9D%B0%E4%BC%A6%E6%BC%94%E8%BF%87%E4%BB%80%E4%B9%88%E7%94%B5%E5%BD%B1&sort_key=%E6%9C%80%E7%83%AD&from_sf=1&openapi=1&resource_id=28261&stat0=&stat1=&ms=1&dspName=iphone&top=%7B%22sfhs%22%3A2%7D","anchorText":"查看更多"}
         * token : eyJib3RfaWQiOiJ1cyIsInJlc3VsdF90b2tlbiI6ImUyMTFiZWEyLWVkNDgtNGU3MS1iZWJjLTRjZTYyMTcyNWUxMSIsImJvdF90b2tlbiI6Im51bGwifQ==
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
             * url : https://m.baidu.com/sf?pd=movie_general&word=%E5%91%A8%E6%9D%B0%E4%BC%A6%E6%BC%94%E8%BF%87%E4%BB%80%E4%B9%88%E7%94%B5%E5%BD%B1&sort_key=%E6%9C%80%E7%83%AD&from_sf=1&openapi=1&resource_id=28261&stat0=&stat1=&ms=1&dspName=iphone&top=%7B%22sfhs%22%3A2%7D
             * anchorText : 查看更多
             */

            private LinkBean link;
            private String token;
            /**
             * image : {"src":"https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1692762530,1901134749&fm=58&s=6DD07C8C1AB39BF346982C9E03005082"}
             * title : 惊天魔盗团2
             * url : https://m.baidu.com/s?word=惊天魔盗团2
             */

            private List<ListBean> list;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
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

            public List<ListBean> getList() {
                return list;
            }

            public void setList(List<ListBean> list) {
                this.list = list;
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

            public static class ListBean {
                /**
                 * src : https://ss2.baidu.com/6ONYsjip0QIZ8tyhnq/it/u=1692762530,1901134749&fm=58&s=6DD07C8C1AB39BF346982C9E03005082
                 */

                private ImageBean image;
                private String title;
                private String url;

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

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
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
            }
        }
    }
}
