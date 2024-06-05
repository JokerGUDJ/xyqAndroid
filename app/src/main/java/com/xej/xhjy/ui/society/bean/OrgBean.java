package com.xej.xhjy.ui.society.bean;

import java.util.List;

public class OrgBean {


    /**
     * code : 0
     * msg : succ
     * page : {"totalElements":3,"totalPages":1,"number":0,"size":15,"numberOfElements":3}
     * content : [{"id":"499248647884668928","orgName":"南京银行股份有限公司"},{"id":"595245542846656512","orgName":"南京六合九银村镇银行股份有限公司"},{"id":"627104517774798848","orgName":"南京浦口靖发村镇银行股份有限公司"}]
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
         * totalElements : 3
         * totalPages : 1
         * number : 0
         * size : 15
         * numberOfElements : 3
         */

        private int totalElements;
        private int totalPages;
        private int number;
        private int size;
        private int numberOfElements;

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

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }
    }

    public static class ContentBean {
        /**
         * id : 499248647884668928
         * orgName : 南京银行股份有限公司
         */

        private String id;
        private String orgName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }
    }
}
