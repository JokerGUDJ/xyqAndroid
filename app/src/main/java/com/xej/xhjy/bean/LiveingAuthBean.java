package com.xej.xhjy.bean;

public class LiveingAuthBean {

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

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    String code; //0成功，1失败
    String msg;
    Content content;

    public class Content{
        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getParameters() {
            return parameter;
        }

        public void setParameters(String parameters) {
            this.parameter = parameters;
        }


        public String getViewUrlPath() {
            return viewUrlPath;
        }

        public void setViewUrlPath(String viewUrlPath) {
            this.viewUrlPath = viewUrlPath;
        }

        public String getLiveId() {
            return liveId;
        }

        public void setLiveId(String liveId) {
            this.liveId = liveId;
        }

        public String getLiveStatus() {
            return liveStatus;
        }

        public void setLiveStatus(String liveStatus) {
            this.liveStatus = liveStatus;
        }

        public String getLiveName() {
            return liveName;
        }

        public void setLiveName(String liveName) {
            this.liveName = liveName;
        }

        public String getParameter() {
            return parameter;
        }

        public void setParameter(String parameter) {
            this.parameter = parameter;
        }


        public String getCoverImage() {
            return coverImage;
        }

        public void setCoverImage(String coverImage) {
            this.coverImage = coverImage;
        }
        public String getRealCount() {
            return realCount;
        }

        public void setRealCount(String realCount) {
            this.realCount = realCount;
        }
        public String getAnnounCement() {
            return announCement;
        }

        public void setAnnounCement(String announCement) {
            this.announCement = announCement;
        }


        String liveName;
        String url; //观看url
        String parameter;//拼接需要的参数
        String viewUrlPath;
        String liveId;
        String liveStatus;
        String coverImage;
        String realCount;
        String announCement;
    }
}
