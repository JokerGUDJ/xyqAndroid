package com.xej.xhjy.bean;

import java.util.List;

public class AdvertBean {

    /**
     * content : [{"channelName":"手机广告3","channelModifyTime":"","channelId":1472,"channelDetail":"","titleImagePath":"20180829/5aeef326-4920-46a3-8dff-5034516802eb.jpg","channelCreateTime":"2018-08-29 17:21:08","isParent":null,"type":2,"channelURL":"addthree","channelCreateUserId":1,"isWebLink":1,"parentId":1462,"isAuth":"0","channelStatus":1,"channelModifyUser":null,"channelNumber":0,"subtitle":"","isToStatic":1,"channelWebLink":"http://www.baidu.com"},{"channelName":"手机广告1","channelModifyTime":"2018-08-29 17:19:03","channelId":1465,"channelDetail":"","titleImagePath":"20180828/b8388250-d636-41a8-9943-09f82c11a25c.jpg","channelCreateTime":"2018-08-28 16:48:24","isParent":null,"type":0,"channelURL":"addone","channelCreateUserId":2,"isWebLink":0,"parentId":1462,"isAuth":"0","channelStatus":1,"channelModifyUser":"1","channelNumber":0,"subtitle":"","isToStatic":1,"channelWebLink":""},{"channelName":"手机广告2","channelModifyTime":"2018-08-29 17:20:01","channelId":1467,"channelDetail":"XinTrain","titleImagePath":"20180828/23814bac-4424-4686-b1e4-c3ab1ef2fcba.jpg","channelCreateTime":"2018-08-28 17:08:00","isParent":null,"type":1,"channelURL":"addtwo","channelCreateUserId":2,"isWebLink":0,"parentId":1462,"isAuth":"0","channelStatus":1,"channelModifyUser":"1","channelNumber":0,"subtitle":"","isToStatic":1,"channelWebLink":""}]
     * code : 0
     * page : null11
     * msg : succ
     */

    private String code;
    private Object page;
    private String msg;
    private List<ContentBean> content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getPage() {
        return page;
    }

    public void setPage(Object page) {
        this.page = page;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {

        /**
         * channelName : 手机广告3
         * channelModifyTime :
         * channelId : 1472
         * channelDetail :
         * titleImagePath : 20180829/5aeef326-4920-46a3-8dff-5034516802eb.jpg
         * channelCreateTime : 2018-08-29 17:21:08
         * isParent : null
         * type : 2
         * channelURL : addthree
         * channelCreateUserId : 1
         * isWebLink : 1
         * parentId : 1462
         * isAuth : 0
         * channelStatus : 1
         * channelModifyUser : null
         * channelNumber : 0
         * subtitle :
         * isToStatic : 1
         * channelWebLink : http://www.baidu.com
         */

        private String picFileAddr;
        private String referLink;
        public String getPicFileAddr() {
            return picFileAddr;
        }

        public void setPicFileAddr(String picFileAddr) {
            this.picFileAddr = picFileAddr;
        }

        public String getReferLink() {
            return referLink;
        }

        public void setReferLink(String referLink) {
            this.referLink = referLink;
        }
    }
}
