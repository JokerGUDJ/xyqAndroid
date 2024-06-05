package com.xej.xhjy.ui.society.bean;

import java.util.List;

public class BranchOfBean {

    /**
     * code : 0
     * msg : succ
     * page : null
     * content : [{"id":"586914089029906454","name":"金融市场专委会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906455","name":"零售金融专委会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906456","name":"贸易金融专委会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906457","name":"公司金融专委会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906458","name":"小微金融专委会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906459","name":"金融科技专委会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906460","name":"风险管理专委会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906461","name":"人力资源专委会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906464","name":"资金联合投资项目","insertDate":"20190321","modifyDate":"20190321","operatorId":"1001"},{"id":"586914089029906463","name":"办公室","insertDate":"20190321","modifyDate":"20190321","operatorId":"1001"}]
     */

    private String code;
    private String msg;
    private Object page;
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

    public Object getPage() {
        return page;
    }

    public void setPage(Object page) {
        this.page = page;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * id : 586914089029906454
         * name : 金融市场专委会
         * insertDate : 20180913
         * modifyDate : 20180913
         * operatorId : 1001
         */

        private String id;
        private String name;
        private String insertDate;
        private String modifyDate;
        private String operatorId;

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

        public String getInsertDate() {
            return insertDate;
        }

        public void setInsertDate(String insertDate) {
            this.insertDate = insertDate;
        }

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }
    }
}
