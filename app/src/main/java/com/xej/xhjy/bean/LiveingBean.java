package com.xej.xhjy.bean;

import java.util.List;

public class LiveingBean {

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

    public List<List<ContentBean>> getContent() {
        return content;
    }

    public void setContent(List<List<ContentBean>> content) {
        this.content = content;
    }

    private String code;
    private String msg;
    private List<List<ContentBean>> content;

    public class ContentBean{
        private String id;
        private String name;//直播会议名称
        private long liveTime;//直播时间
        private String viewUrlPath;// 直播地址

        private String coverImage; //直播封面url
        private String liveStatus;//会议类型
        private String joinCount;//参加人数
        private String subscribeCount;//预约人数
        private String unrealCount;

        public String getUnrealCount() {
            return unrealCount;
        }

        public void setUnrealCount(String unrealCount) {
            this.unrealCount = unrealCount;
        }

        public String getSubscribeCount() {
            return subscribeCount;
        }

        public void setSubscribeCount(String subscribeCount) {
            this.subscribeCount = subscribeCount;
        }

        public String getAnnounCement() {
            return announCement;
        }

        public void setAnnounCement(String announCement) {
            this.announCement = announCement;
        }

        private String announCement;//会议说明

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getLiveTime() {
            return liveTime;
        }

        public void setLiveTime(long liveTime) {
            this.liveTime = liveTime;
        }

        public String getViewUrlPath() {
            return viewUrlPath;
        }

        public void setViewUrlPath(String viewUrlPath) {
            this.viewUrlPath = viewUrlPath;
        }

        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }

        public String getLiveStatus() {
            return liveStatus;
        }

        public void setLiveStatus(String liveStatus) {
            this.liveStatus = liveStatus;
        }

        public String getJoinCount() {
            return joinCount;
        }

        public void setJoinCount(String joinCount) {
            this.joinCount = joinCount;
        }
    }
}
