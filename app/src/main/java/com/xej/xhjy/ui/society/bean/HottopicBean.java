package com.xej.xhjy.ui.society.bean;

import java.util.List;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: HottopicBean
 * @Description: 热门话题bean
 * @Author: lihy_0203
 * @CreateDate: 2018/12/27 上午9:24
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/27 上午9:24
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class HottopicBean {


    /**
     * code : 0
     * content : [{"accessoryId":"路径3","beginDate":"2018-12-28","content":"11测试删除内容11","flag":"1","id":"509025536954253312","insertDate":"20181105","modifyDate":"20181105","operatorId":"1000111","title":"11测试删除"}]
     * msg : succ
     * page : {"number":2,"numberOfElements":1,"size":1,"totalElements":4,"totalPages":4}
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
         * number : 2
         * numberOfElements : 1
         * size : 1
         * totalElements : 4
         * totalPages : 4
         */

        private int number;
        private int numberOfElements;
        private int size;
        private int totalElements;
        private int totalPages;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
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
    }

    public static class ContentBean {
        /**
         * accessoryId : 路径3
         * beginDate : 2018-12-28
         * content : 11测试删除内容11
         * flag : 1
         * id : 509025536954253312
         * insertDate : 20181105
         * modifyDate : 20181105
         * operatorId : 1000111
         * title : 11测试删除
         */

        private String accessoryId;
        private String beginDate;
        private String content;
        private String flag;
        private String id;
        private String insertDate;
        private String modifyDate;
        private String operatorId;
        private String title;

        public String getAccessoryId() {
            return accessoryId;
        }

        public void setAccessoryId(String accessoryId) {
            this.accessoryId = accessoryId;
        }

        public String getBeginDate() {
            return beginDate;
        }

        public void setBeginDate(String beginDate) {
            this.beginDate = beginDate;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
