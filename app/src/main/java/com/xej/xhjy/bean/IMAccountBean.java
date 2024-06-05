package com.xej.xhjy.bean;


/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.bean
 * @ClassName: IMAccountBean
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2019/1/23 下午5:00
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/23 下午5:00
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class IMAccountBean {

    /**
     * code : 0
     * content : {"accId":"528516309034688512","flag":"0","id":"528516309684805632","insertDate":"20181229","modifyDate":"20181229","name":"北凉王","operatorId":"1000111","token":"e5556eec51d3ca2f4e4ac74d6b02513c"}
     * msg : succ
     */

    private String code;
    private ContentBean content;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class ContentBean {
        /**
         * accId : 528516309034688512
         * flag : 0
         * id : 528516309684805632
         * insertDate : 20181229
         * modifyDate : 20181229
         * name : 北凉王
         * operatorId : 1000111
         * token : e5556eec51d3ca2f4e4ac74d6b02513c
         */

        private String accId;
        private String flag;
        private String id;
        private String insertDate;
        private String modifyDate;
        private String name;
        private String operatorId;
        private String token;
        private String accountId;

        public String getAccountToken() {
            return accountToken;
        }

        public void setAccountToken(String accountToken) {
            this.accountToken = accountToken;
        }

        private String accountToken;

        public String getAccId() {
            return accId;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public void setAccId(String accId) {
            this.accId = accId;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
