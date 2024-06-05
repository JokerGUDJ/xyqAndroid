package com.xej.xhjy.ui.society.bean;

import java.util.List;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: followPerson
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2019/1/18 上午10:09
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/18 上午10:09
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class followPerson {

    /**
     * code : 0
     * content : [{"attId":"530420846263754752","flag":"0","id":"535422312749654016","insertDate":"20190117","modifyDate":"20190117","operatorId":"1000111","orgnizationName":"南京银行股份有限公司","peopleId":"528516309684805632","tab":"0","userId":"530401504759283712","userName":"由于1"},{"attId":"532235042576097280","flag":"0","id":"537957545843810304","insertDate":"20190124","modifyDate":"20190124","operatorId":"1000111","orgnizationName":"江苏南通农村商业银行股份有限公司","peopleId":"528516309684805632","tab":"0","userId":"500335819060576256","userName":"阿斯顿发"},{"attId":"535762808038678528","flag":"0","id":"539475310853414912","insertDate":"20190128","modifyDate":"20190128","operatorId":"1000111","orgnizationName":"江苏鑫合易家信息技术有限责任公司","peopleId":"528516309684805632","tab":"0","userId":"499226128427417600","userName":"黄祖祥"},{"attId":"532513862503849984","flag":"0","id":"539475356344836096","insertDate":"20190128","modifyDate":"20190128","operatorId":"1000111","orgnizationName":"江苏鑫合易家信息技术有限责任公司","peopleId":"528516309684805632","tab":"0","userId":"505388891050770432","userName":"李七夜"}]
     * msg : succ
     * page : {"number":0,"numberOfElements":4,"size":10,"totalElements":4,"totalPages":1}
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
         * number : 0
         * numberOfElements : 4
         * size : 10
         * totalElements : 4
         * totalPages : 1
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
         * attId : 530420846263754752
         * flag : 0
         * id : 535422312749654016
         * insertDate : 20190117
         * modifyDate : 20190117
         * operatorId : 1000111
         * orgnizationName : 南京银行股份有限公司
         * peopleId : 528516309684805632
         * tab : 0
         * userId : 530401504759283712
         * userName : 由于1
         */

        private String attId;
        private String flag;
        private String id;
        private String insertDate;
        private String modifyDate;
        private String operatorId;
        private String orgnizationName;
        private String peopleId;
        private String tab;
        private String userId;
        private String userName;

        public String getAttId() {
            return attId;
        }

        public void setAttId(String attId) {
            this.attId = attId;
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

        public String getOrgnizationName() {
            return orgnizationName;
        }

        public void setOrgnizationName(String orgnizationName) {
            this.orgnizationName = orgnizationName;
        }

        public String getPeopleId() {
            return peopleId;
        }

        public void setPeopleId(String peopleId) {
            this.peopleId = peopleId;
        }

        public String getTab() {
            return tab;
        }

        public void setTab(String tab) {
            this.tab = tab;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
