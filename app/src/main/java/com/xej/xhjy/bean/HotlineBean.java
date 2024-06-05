package com.xej.xhjy.bean;

import java.util.List;

public class HotlineBean {
    /**
     * code : 0
     * content : [{"titleImg":"20180712/615c2de1-2d3b-4d9c-947b-8b7bba3e9306.jpg","infoWebLink":"","infoId":2514,"infoStatus":1,"informationExtendId":"0","infoNumber":0,"modifyUserId":1027,"veryfiTime":"2018-07-13 15:46:29","verifyReason":"","source":"","title":"测试44","templateId":0,"infoType":1,"infoCreateTime":"2018-07-12 15:22:02","subTitle":"测试44","titleColor":"","isToStatic":1,"channelId":1316,"summary":"","topType":"0","publishTime":"2018-07-12 00:00:00","verifyUserId":1027,"author":"","bankSeq":9999,"userId":1027,"isBold":0,"fromUrl":"","lastModifyTime":"2018-07-12 19:22:22","siteId":1003,"mediaPath":""},{"titleImg":"20180712/54271028-e4b9-474b-8855-ce8556899141.jpg","infoWebLink":"","infoId":2515,"infoStatus":1,"informationExtendId":"0","infoNumber":0,"modifyUserId":1027,"veryfiTime":"2018-07-13 15:46:29","verifyReason":"","source":"","title":"444","templateId":0,"infoType":1,"infoCreateTime":"2018-07-12 15:30:26","subTitle":"444二恶恶","titleColor":"","isToStatic":1,"channelId":1316,"summary":"","topType":"0","publishTime":"2018-07-12 00:00:00","verifyUserId":1027,"author":"","bankSeq":9999,"userId":1027,"isBold":0,"fromUrl":"","lastModifyTime":"2018-07-12 17:03:09","siteId":1003,"mediaPath":""},{"titleImg":"20180605/38c9cb3c-369f-430e-9b58-d912d1122bd1.jpg","infoWebLink":"","infoId":2230,"infoStatus":1,"informationExtendId":"0","infoNumber":0,"modifyUserId":2,"veryfiTime":"2018-06-05 17:28:37","verifyReason":"","source":"","title":"测试一","templateId":1438,"infoType":1,"infoCreateTime":"2018-06-05 17:28:24","subTitle":"测试一","titleColor":"","isToStatic":1,"channelId":1299,"summary":"测试一测试一测试一测试一测试一测试一测试一测试一测试一测试一测试一","topType":"0","publishTime":"2018-06-06 17:28:09","verifyUserId":2,"author":"aaa","bankSeq":9999,"userId":2,"isBold":0,"fromUrl":"","lastModifyTime":"2018-06-05 17:28:24","siteId":1003,"mediaPath":""}]
     * msg : succ
     */

    private String code;
    private String msg;
    private List<ContentBean> content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
         * titleImg : 20180712/615c2de1-2d3b-4d9c-947b-8b7bba3e9306.jpg
         * infoWebLink :
         * infoId : 2514
         * infoStatus : 1
         * informationExtendId : 0
         * infoNumber : 0
         * modifyUserId : 1027
         * veryfiTime : 2018-07-13 15:46:29
         * verifyReason :
         * source :
         * title : 测试44
         * templateId : 0
         * infoType : 1
         * infoCreateTime : 2018-07-12 15:22:02
         * subTitle : 测试44
         * titleColor :
         * isToStatic : 1
         * channelId : 1316
         * summary :
         * topType : 0
         * publishTime : 2018-07-12 00:00:00
         * verifyUserId : 1027
         * author :
         * bankSeq : 9999
         * userId : 1027
         * isBold : 0
         * fromUrl :
         * lastModifyTime : 2018-07-12 19:22:22
         * siteId : 1003
         * mediaPath :
         */

        private String titleImg;
        private String infoWebLink;
        private int infoId;
        private int infoStatus;
        private String informationExtendId;
        private int infoNumber;
        private int modifyUserId;
        private String veryfiTime;
        private String verifyReason;
        private String source;
        private String title;
        private int templateId;
        private int infoType;
        private String infoCreateTime;
        private String subTitle;
        private String titleColor;
        private int isToStatic;
        private int channelId;
        private String summary;
        private String topType;
        private String publishTime;
        private int verifyUserId;
        private String author;
        private int bankSeq;
        private int userId;
        private int isBold;
        private String fromUrl;
        private String lastModifyTime;
        private int siteId;
        private String mediaPath;

        public String getTitleImg() {
            return titleImg;
        }

        public void setTitleImg(String titleImg) {
            this.titleImg = titleImg;
        }

        public String getInfoWebLink() {
            return infoWebLink;
        }

        public void setInfoWebLink(String infoWebLink) {
            this.infoWebLink = infoWebLink;
        }

        public int getInfoId() {
            return infoId;
        }

        public void setInfoId(int infoId) {
            this.infoId = infoId;
        }

        public int getInfoStatus() {
            return infoStatus;
        }

        public void setInfoStatus(int infoStatus) {
            this.infoStatus = infoStatus;
        }

        public String getInformationExtendId() {
            return informationExtendId;
        }

        public void setInformationExtendId(String informationExtendId) {
            this.informationExtendId = informationExtendId;
        }

        public int getInfoNumber() {
            return infoNumber;
        }

        public void setInfoNumber(int infoNumber) {
            this.infoNumber = infoNumber;
        }

        public int getModifyUserId() {
            return modifyUserId;
        }

        public void setModifyUserId(int modifyUserId) {
            this.modifyUserId = modifyUserId;
        }

        public String getVeryfiTime() {
            return veryfiTime;
        }

        public void setVeryfiTime(String veryfiTime) {
            this.veryfiTime = veryfiTime;
        }

        public String getVerifyReason() {
            return verifyReason;
        }

        public void setVerifyReason(String verifyReason) {
            this.verifyReason = verifyReason;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getTemplateId() {
            return templateId;
        }

        public void setTemplateId(int templateId) {
            this.templateId = templateId;
        }

        public int getInfoType() {
            return infoType;
        }

        public void setInfoType(int infoType) {
            this.infoType = infoType;
        }

        public String getInfoCreateTime() {
            return infoCreateTime;
        }

        public void setInfoCreateTime(String infoCreateTime) {
            this.infoCreateTime = infoCreateTime;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getTitleColor() {
            return titleColor;
        }

        public void setTitleColor(String titleColor) {
            this.titleColor = titleColor;
        }

        public int getIsToStatic() {
            return isToStatic;
        }

        public void setIsToStatic(int isToStatic) {
            this.isToStatic = isToStatic;
        }

        public int getChannelId() {
            return channelId;
        }

        public void setChannelId(int channelId) {
            this.channelId = channelId;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getTopType() {
            return topType;
        }

        public void setTopType(String topType) {
            this.topType = topType;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public int getVerifyUserId() {
            return verifyUserId;
        }

        public void setVerifyUserId(int verifyUserId) {
            this.verifyUserId = verifyUserId;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public int getBankSeq() {
            return bankSeq;
        }

        public void setBankSeq(int bankSeq) {
            this.bankSeq = bankSeq;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getIsBold() {
            return isBold;
        }

        public void setIsBold(int isBold) {
            this.isBold = isBold;
        }

        public String getFromUrl() {
            return fromUrl;
        }

        public void setFromUrl(String fromUrl) {
            this.fromUrl = fromUrl;
        }

        public String getLastModifyTime() {
            return lastModifyTime;
        }

        public void setLastModifyTime(String lastModifyTime) {
            this.lastModifyTime = lastModifyTime;
        }

        public int getSiteId() {
            return siteId;
        }

        public void setSiteId(int siteId) {
            this.siteId = siteId;
        }

        public String getMediaPath() {
            return mediaPath;
        }

        public void setMediaPath(String mediaPath) {
            this.mediaPath = mediaPath;
        }
    }
}
