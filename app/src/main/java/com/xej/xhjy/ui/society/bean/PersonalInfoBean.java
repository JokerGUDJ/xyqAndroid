package com.xej.xhjy.ui.society.bean;


/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: PersonalInfoBean
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2019/1/26 下午3:44
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/26 下午3:44
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PersonalInfoBean {

    /**
     * code : 0
     * content : {"address":"江苏省南京市玄武区","conPeople":3,"conTopic":0,"department":"产品开发部","eMail":"1906033859@qq.com","id":"535762808038678528","job":"总监","orgAllName":"江苏鑫合易家信息技术有限责任公司","orgNm":"鑫合易家","phone":"18070509742","telPhone":"025-78923453","userName":"黄祖祥"}
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
         * address : 江苏省南京市玄武区
         * conPeople : 3
         * conTopic : 0
         * department : 产品开发部
         * eMail : 1906033859@qq.com
         * id : 535762808038678528
         * job : 总监
         * orgAllName : 江苏鑫合易家信息技术有限责任公司
         * orgNm : 鑫合易家
         * phone : 18070509742
         * telPhone : 025-78923453
         * userName : 黄祖祥
         */

        private String address;
        private int conPeople;
        private int conTopic;
        private String department;
        private String eMail;
        private String id;
        private String job;
        private String orgAllName;
        private String orgNm;
        private String phone;
        private String telPhone;
        private String userName;
        private String userId;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getConPeople() {
            return conPeople;
        }

        public void setConPeople(int conPeople) {
            this.conPeople = conPeople;
        }

        public int getConTopic() {
            return conTopic;
        }

        public void setConTopic(int conTopic) {
            this.conTopic = conTopic;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getEMail() {
            return eMail;
        }

        public void setEMail(String eMail) {
            this.eMail = eMail;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getOrgAllName() {
            return orgAllName;
        }

        public void setOrgAllName(String orgAllName) {
            this.orgAllName = orgAllName;
        }

        public String getOrgNm() {
            return orgNm;
        }

        public void setOrgNm(String orgNm) {
            this.orgNm = orgNm;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getTelPhone() {
            return telPhone;
        }

        public void setTelPhone(String telPhone) {
            this.telPhone = telPhone;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }
}
