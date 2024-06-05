package com.xej.xhjy.bean;

import java.util.List;

public class LiveingMoreBean {
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

    public PageInfo getPage() {
        return page;
    }

    public void setPage(PageInfo page) {
        this.page = page;
    }
    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    private String code;
    private String msg;
    private PageInfo page;
    private List<ContentBean> content;

    public class PageInfo{
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

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        int totalElements;
        int totalPages;
        int number;
        int size;
    }

    public class ContentBean{


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

        public String getLiveTime() {
            return liveTime;
        }

        public void setLiveTime(String liveTime) {
            this.liveTime = liveTime;
        }

        public String getViewUrlPath() {
            return viewUrlPath;
        }

        public void setViewUrlPath(String viewUrlPath) {
            this.viewUrlPath = viewUrlPath;
        }

        public String getLiveStatus() {
            return liveStatus;
        }

        public void setLiveStatus(String liveStatus) {
            this.liveStatus = liveStatus;
        }

        public String getSubscribeCount() {
            return subscribeCount;
        }

        public void setSubscribeCount(String subscribeCount) {
            this.subscribeCount = subscribeCount;
        }
        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }
        public String getAnnounCement() {
            return announCement;
        }

        public void setAnnounCement(String announCement) {
            this.announCement = announCement;
        }
        public String getJoinCount() {
            return joinCount;
        }

        public void setJoinCount(String joinCount) {
            this.joinCount = joinCount;
        }

        public String getUnrealCount() {
            return unrealCount;
        }

        public void setUnrealCount(String unrealCount) {
            this.unrealCount = unrealCount;
        }


        String id;
        String name;
        String liveTime;
        String viewUrlPath;
        String coverImage;
        String liveStatus;
        String subscribeCount;
        String announCement;
        String joinCount;
        String unrealCount;
    }
}
