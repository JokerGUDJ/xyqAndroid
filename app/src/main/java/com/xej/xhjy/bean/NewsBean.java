package com.xej.xhjy.bean;


import java.util.List;

/**
 * @ProjectName: XHJY_learn_platform
 * @Package: com.xej.xhjy.bean
 * @ClassName: NewsBean
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2019/4/23 下午2:52
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/4/23 下午2:52
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class NewsBean {


    /**
     * content : [{"summary":"","infoType":2,"infoCreateTime":"2018-10-11 06:37:51","author":"","subTitle":"汇总国内外财经要闻 总揽最新经济形势","title":"【音频早报】鑫合财经（2018-10-11）","informationExtendId":"0","infoNumber":0,"bankSeq":9999,"verifyUserId":"0","userId":1,"titleImg":"","infoWebLink":"http://mp.weixin.qq.com/s?timestamp=1539222424&src=3&ver=1&signature=P*FX9RjmNgAvWiE5iKZ3rgOs-rx9Mvz8R5dyN-S-sBV*I9ViZCcFX9ZLcIlZMVkzkPKgMcpcnMCzYkUmIFeTDKyVKferoeB5wi8Jmz-qHh-VIHCL8fXWpO1Ok*Tm3skRkza7c-S5TuQkImM9oCv3THBUhA76Lmm5l6-bAsMecUk=","verifyReason":"","veryfiTime":null,"publishTime":"2018-10-11 06:37:51","mediaPath":"","channelId":1401,"titleColor":"","infoId":4617,"autoIssueTime":null,"templateId":null,"infoStatus":1,"source":"OfficialAccount","modifyUserId":1,"siteId":1005,"fromUrl":"","isToStatic":0,"topType":"0","isBold":"0","lastModifyTime":"2018-10-11 06:37:51"},{"summary":"","infoType":2,"infoCreateTime":"2018-10-11 06:37:51","author":"","subTitle":"贸易紧张局势加剧削弱世界经济增长前景","title":"【今日关注】贸易紧张局势加剧削弱世界经济增长前景","informationExtendId":"0","infoNumber":0,"bankSeq":9999,"verifyUserId":"0","userId":1,"titleImg":"","infoWebLink":"http://mp.weixin.qq.com/s?timestamp=1539222424&src=3&ver=1&signature=P*FX9RjmNgAvWiE5iKZ3rgOs-rx9Mvz8R5dyN-S-sBV*I9ViZCcFX9ZLcIlZMVkzkPKgMcpcnMCzYkUmIFeTDKyVKferoeB5wi8Jmz-qHh*FMbYzfby9LTubUioLjufCcDe-h2pLnVM*39FM8Yu0YzcjBeFIVHRr7HMtphBpICk=","verifyReason":"","veryfiTime":null,"publishTime":"2018-10-11 06:37:51","mediaPath":"","channelId":1401,"titleColor":"","infoId":4619,"autoIssueTime":null,"templateId":null,"infoStatus":1,"source":"OfficialAccount","modifyUserId":1,"siteId":1005,"fromUrl":"","isToStatic":0,"topType":"0","isBold":"0","lastModifyTime":"2018-10-11 06:37:51"},{"summary":"","infoType":2,"infoCreateTime":"2018-10-11 06:37:51","author":"","subTitle":"资金市场维持宽松 国债期货全天震荡","title":"【昨日行情】资金市场维持宽松 国债期货全天震荡","informationExtendId":"0","infoNumber":0,"bankSeq":9999,"verifyUserId":"0","userId":1,"titleImg":"","infoWebLink":"http://mp.weixin.qq.com/s?timestamp=1539222424&src=3&ver=1&signature=P*FX9RjmNgAvWiE5iKZ3rgOs-rx9Mvz8R5dyN-S-sBV*I9ViZCcFX9ZLcIlZMVkzkPKgMcpcnMCzYkUmIFeTDKyVKferoeB5wi8Jmz-qHh-L1S4j7Uo0QhuK9PVibe2z8CviMG5uIVseRdcgRKRStF3HE1XsFc3A9fOrcM09998=","verifyReason":"","veryfiTime":null,"publishTime":"2018-10-11 06:37:51","mediaPath":"","channelId":1401,"titleColor":"","infoId":4618,"autoIssueTime":null,"templateId":null,"infoStatus":1,"source":"OfficialAccount","modifyUserId":1,"siteId":1005,"fromUrl":"","isToStatic":0,"topType":"0","isBold":"0","lastModifyTime":"2018-10-11 06:37:51"}]
     * code : 0
     * msg : succ
     * page : {"size":3,"number":0,"totalElements":32,"totalPages":11,"numberOfElements":3}
     */

    private String code;
    private String msg;
    private PageBean page;
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

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class PageBean {
        /**
         * size : 3
         * number : 0
         * totalElements : 32
         * totalPages : 11
         * numberOfElements : 3
         */

        private int size;
        private int number;
        private int totalElements;
        private int totalPages;
        private int numberOfElements;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }
    }

    public static class ContentBean {
        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getCommitId() {
            return commitId;
        }

        public void setCommitId(String commitId) {
            this.commitId = commitId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getExhibit() {
            return exhibit;
        }

        public void setExhibit(String exhibit) {
            this.exhibit = exhibit;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInsertDate() {
            return insertDate;
        }

        public void setInsertDate(String insertDate) {
            this.insertDate = insertDate;
        }

        public String getJudge() {
            return judge;
        }

        public void setJudge(String judge) {
            this.judge = judge;
        }

        public String getJudgeTime() {
            return judgeTime;
        }

        public void setJudgeTime(String judgeTime) {
            this.judgeTime = judgeTime;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProviderTime() {
            return providerTime;
        }

        public void setProviderTime(String providerTime) {
            this.providerTime = providerTime;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWhetherUrl() {
            return whetherUrl;
        }

        public void setWhetherUrl(String whetherUrl) {
            this.whetherUrl = whetherUrl;
        }

        /**
         * summary :
         * infoType : 2
         * infoCreateTime : 2018-10-11 06:37:51
         * author :
         * subTitle : 汇总国内外财经要闻 总揽最新经济形势
         * title : 【音频早报】鑫合财经（2018-10-11）
         * informationExtendId : 0
         * infoNumber : 0
         * bankSeq : 9999
         * verifyUserId : 0
         * userId : 1
         * titleImg :
         * infoWebLink : http://mp.weixin.qq.com/s?timestamp=1539222424&src=3&ver=1&signature=P*FX9RjmNgAvWiE5iKZ3rgOs-rx9Mvz8R5dyN-S-sBV*I9ViZCcFX9ZLcIlZMVkzkPKgMcpcnMCzYkUmIFeTDKyVKferoeB5wi8Jmz-qHh-VIHCL8fXWpO1Ok*Tm3skRkza7c-S5TuQkImM9oCv3THBUhA76Lmm5l6-bAsMecUk=
         * verifyReason :
         * veryfiTime : null
         * publishTime : 2018-10-11 06:37:51
         * mediaPath :
         * channelId : 1401
         * titleColor :
         * infoId : 4617
         * autoIssueTime : null
         * templateId : null
         * infoStatus : 1
         * source : OfficialAccount
         * modifyUserId : 1
         * siteId : 1005
         * fromUrl :
         * isToStatic : 0
         * topType : 0
         * isBold : 0
         * lastModifyTime : 2018-10-11 06:37:51
         */

        private String author;
        private String commitId;
        private String content;
        private String exhibit;
        private String id;
        private String insertDate;
        private String judge;
        private String judgeTime;
        private String mark;
        private String modifyDate;
        private String name;
        private String providerTime;
        private String publishTime;
        private String type;
        private String whetherUrl;

    }
}
