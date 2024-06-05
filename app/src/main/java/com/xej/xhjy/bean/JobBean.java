package com.xej.xhjy.bean;

import java.util.List;

public class JobBean {

    /**
     * code : 0
     * content : [{"jobName":"职员","createTime":"2018-06-13 20:39:06","jobState":"N","updateTime":"2018-06-13 20:39:06","id":"520488b31d9b4e108a37ac595157c05c","jobLevel":"4.7"}]
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
         * jobName : 职员
         * createTime : 2018-06-13 20:39:06
         * jobState : N
         * updateTime : 2018-06-13 20:39:06
         * id : 520488b31d9b4e108a37ac595157c05c
         * jobLevel : 4.7
         */

        private String jobName;
        private String createTime;
        private String jobState;
        private String updateTime;
        private String id;
        private String jobLevel;

        public String getJobName() {
            return jobName;
        }

        public void setJobName(String jobName) {
            this.jobName = jobName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getJobState() {
            return jobState;
        }

        public void setJobState(String jobState) {
            this.jobState = jobState;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getJobLevel() {
            return jobLevel;
        }

        public void setJobLevel(String jobLevel) {
            this.jobLevel = jobLevel;
        }
    }
}
