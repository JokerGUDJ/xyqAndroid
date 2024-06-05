package com.xej.xhjy.bean;

import java.util.List;

/**
 * @class MeettingListBean 会议列表bean
 * @author dazhi
 * @Createtime 2018/6/20 17:09
 * @description describe
 * @Revisetime
 * @Modifier
 */
public class MeettingListBean {
    /**
     * code : 0
     * content : [{"hostOrg":"1","address":"1","modifyDate":"20180615","endDate":"1","meetRole":"0","insertDate":"20180615","applyConfirmFlag":"1","lastApplyDate":"1","meetRoleOth":"0","beginDate":"1","stt":"10","feastConfirmFlag":"1","name":"1","id":"00830a39de8d4f3bb5ee5e54574c9767","issueDate":"1","operatorId":"1000111","allowApplyGroup":"1"},{"hostOrg":"ecc34c304c2a45bb87ee2ff248f4e3c8","address":"南京市玄武区中山路268号","modifyDate":"20180410","endDate":"20180418","meetRole":"17","insertDate":"20180410","applyConfirmFlag":"1","lastApplyDate":"20180416","meetRoleOth":"7","beginDate":"20180416","stt":"20","feastConfirmFlag":"0","name":"会议名称10","id":"10010","issueDate":"20180410","operatorId":"001"},{"hostOrg":"ecc34c304c2a45bb87ee2ff248f4e3c8","address":"南京市玄武区中山路268号","modifyDate":"20180410","endDate":"20180418","meetRole":"161","insertDate":"20180410","applyConfirmFlag":"1","lastApplyDate":"20180416","meetRoleOth":"43","beginDate":"20180416","stt":"20","feastConfirmFlag":"0","name":"会议名称11","id":"10011","issueDate":"20180410","operatorId":"001","allowApplyGroup":"ecc34c304c2a45bb87ee2ff248f4e3c8"},{"hostOrg":"ecc34c304c2a45bb87ee2ff248f4e3c8","address":"南京市玄武区中山路268号","modifyDate":"20180410","endDate":"20180418","meetRole":"138","insertDate":"20180410","applyConfirmFlag":"1","lastApplyDate":"20180416","meetRoleOth":"51","beginDate":"20180416","stt":"10","feastConfirmFlag":"1","name":"会议名称12","id":"10012","issueDate":"20180410","operatorId":"001"},{"hostOrg":"ecc34c304c2a45bb87ee2ff248f4e3c8","address":"南京市玄武区中山路268号","modifyDate":"20180410","endDate":"20180418","meetRole":"127","insertDate":"20180410","applyConfirmFlag":"0","lastApplyDate":"20180416","meetRoleOth":"32","beginDate":"20180416","stt":"10","feastConfirmFlag":"0","name":"会议名称13","id":"10013","issueDate":"20180410","operatorId":"001"},{"hostOrg":"ecc34c304c2a45bb87ee2ff248f4e3c8","address":"南京市玄武区中山路268号","modifyDate":"20180410","endDate":"20180418","meetRole":"183","insertDate":"20180410","applyConfirmFlag":"1","lastApplyDate":"20180416","meetRoleOth":"45","beginDate":"20180416","stt":"10","feastConfirmFlag":"0","name":"会议名称14","id":"10014","issueDate":"20180410","operatorId":"001"},{"hostOrg":"ecc34c304c2a45bb87ee2ff248f4e3c8","address":"南京市玄武区中山路268号","modifyDate":"20180410","endDate":"20180418","meetRole":"96","insertDate":"20180410","applyConfirmFlag":"0","lastApplyDate":"20180416","meetRoleOth":"40","beginDate":"20180416","stt":"00","feastConfirmFlag":"1","name":"会议名称15","id":"10015","issueDate":"20180410","operatorId":"001"},{"hostOrg":"ecc34c304c2a45bb87ee2ff248f4e3c8","address":"南京市玄武区中山路268号","modifyDate":"20180410","endDate":"20180418","meetRole":"65","insertDate":"20180410","applyConfirmFlag":"1","lastApplyDate":"20180416","meetRoleOth":"22","beginDate":"20180416","stt":"20","feastConfirmFlag":"0","name":"会议名称16","id":"10016","issueDate":"20180410","operatorId":"001"},{"hostOrg":"ecc34c304c2a45bb87ee2ff248f4e3c8","address":"南京市玄武区中山路268号","modifyDate":"20180410","endDate":"20180418","meetRole":"69","insertDate":"20180410","applyConfirmFlag":"0","lastApplyDate":"20180416","meetRoleOth":"28","beginDate":"20180416","stt":"20","feastConfirmFlag":"1","name":"会议名称17","id":"10017","issueDate":"20180410","operatorId":"001","allowApplyGroup":"ecc34c304c2a45bb87ee2ff248f4e3c8"},{"hostOrg":"ecc34c304c2a45bb87ee2ff248f4e3c8","address":"南京市玄武区中山路268号","modifyDate":"20180410","endDate":"20180418","meetRole":"152","insertDate":"20180410","applyConfirmFlag":"1","lastApplyDate":"20180416","meetRoleOth":"52","beginDate":"20180416","stt":"20","feastConfirmFlag":"1","name":"会议名称18","id":"10018","issueDate":"20180410","operatorId":"001"}]
     * msg : succ
     * page : {"number":0,"numberOfElements":10,"size":10,"totalElements":24,"totalPages":3}
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
         * numberOfElements : 10
         * size : 10
         * totalElements : 24
         * totalPages : 3
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
         * hostOrg : 1
         * address : 1
         * modifyDate : 20180615
         * endDate : 1
         * meetRole : 0
         * insertDate : 20180615
         * applyConfirmFlag : 1
         * lastApplyDate : 1
         * meetRoleOth : 0
         * beginDate : 1
         * stt : 10
         * feastConfirmFlag : 1
         * name : 1
         * id : 00830a39de8d4f3bb5ee5e54574c9767
         * issueDate : 1
         * operatorId : 1000111
         * allowApplyGroup : 1
         */

        private String hostOrg;
        private String address;
        private String modifyDate;
        private String endDate;
        private String meetRole;
        private String insertDate;
        private String applyConfirmFlag;
        private String lastApplyDate;
        private String meetRoleOth;
        private String beginDate;
        private String stt;
        private String feastConfirmFlag;
        private String name;
        private String id;
        private String issueDate;
        private String operatorId;
        private String allowApplyGroup;
        private String whetherJoin;//Y,N
        private String meetFrom;// M，线下会议，S，线上会议
        private String sistt;//是否签到

        public String getMeetFrom() {
            return meetFrom;
        }

        public void setMeetFrom(String meetFrom) {
            this.meetFrom = meetFrom;
        }

        public String getWhetherJoin() {
            return whetherJoin;
        }

        public void setWhetherJoin(String whetherJoin) {
            this.whetherJoin = whetherJoin;
        }

        public String getCommitId() {
            return commitId;
        }

        public void setCommitId(String commitId) {
            this.commitId = commitId;
        }

        public String getCreateAccId() {
            return createAccId;
        }

        public void setCreateAccId(String createAccId) {
            this.createAccId = createAccId;
        }

        public String getMeetFrontStatus() {
            return meetFrontStatus;
        }

        public void setMeetFrontStatus(String meetFrontStatus) {
            this.meetFrontStatus = meetFrontStatus;
        }

        public String getMeetName() {
            return meetName;
        }

        public void setMeetName(String meetName) {
            this.meetName = meetName;
        }

        public String getMeetStatus() {
            return meetStatus;
        }

        public void setMeetStatus(String meetStatus) {
            this.meetStatus = meetStatus;
        }

        public String getMeetType() {
            return meetType;
        }

        public void setMeetType(String meetType) {
            this.meetType = meetType;
        }

        public String getMeetingId() {
            return meetingId;
        }

        public void setMeetingId(String meetingId) {
            this.meetingId = meetingId;
        }

        public String getReserveEndTime() {
            return reserveEndTime;
        }

        public void setReserveEndTime(String reserveEndTime) {
            this.reserveEndTime = reserveEndTime;
        }

        public String getReserveStartTime() {
            return reserveStartTime;
        }

        public void setReserveStartTime(String reserveStartTime) {
            this.reserveStartTime = reserveStartTime;
        }

        public String getSpeakAccId() {
            return speakAccId;
        }

        public void setSpeakAccId(String speakAccId) {
            this.speakAccId = speakAccId;
        }

        public String getSpeakAccName() {
            return speakAccName;
        }

        public void setSpeakAccName(String speakAccName) {
            this.speakAccName = speakAccName;
        }

        public String getOwnerId() {
            return ownerId;
        }

        public void setOwnerId(String ownerId) {
            this.ownerId = ownerId;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        //线上会议字段
        private String commitId;
        private String createAccId;
        private String meetFrontStatus;
        private String meetName;
        private String meetStatus;
        private String meetType;
        private String meetingId;
        private String reserveEndTime;
        private String reserveStartTime;
        private String speakAccId;
        private String speakAccName;
        private String ownerId;
        private String ownerName;
        private String status;//云信会议状态0无效1未开始2进行中3已终止4已取消5已回收



        public String getHostOrg() {
            return hostOrg;
        }

        public void setHostOrg(String hostOrg) {
            this.hostOrg = hostOrg;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getMeetRole() {
            return meetRole;
        }

        public void setMeetRole(String meetRole) {
            this.meetRole = meetRole;
        }

        public String getInsertDate() {
            return insertDate;
        }

        public void setInsertDate(String insertDate) {
            this.insertDate = insertDate;
        }

        public String getApplyConfirmFlag() {
            return applyConfirmFlag;
        }

        public void setApplyConfirmFlag(String applyConfirmFlag) {
            this.applyConfirmFlag = applyConfirmFlag;
        }

        public String getLastApplyDate() {
            return lastApplyDate;
        }

        public void setLastApplyDate(String lastApplyDate) {
            this.lastApplyDate = lastApplyDate;
        }

        public String getMeetRoleOth() {
            return meetRoleOth;
        }

        public void setMeetRoleOth(String meetRoleOth) {
            this.meetRoleOth = meetRoleOth;
        }

        public String getBeginDate() {
            return beginDate;
        }

        public void setBeginDate(String beginDate) {
            this.beginDate = beginDate;
        }

        public String getStt() {
            return stt;
        }

        public void setStt(String stt) {
            this.stt = stt;
        }

        public String getFeastConfirmFlag() {
            return feastConfirmFlag;
        }

        public void setFeastConfirmFlag(String feastConfirmFlag) {
            this.feastConfirmFlag = feastConfirmFlag;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIssueDate() {
            return issueDate;
        }

        public void setIssueDate(String issueDate) {
            this.issueDate = issueDate;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public String getAllowApplyGroup() {
            return allowApplyGroup;
        }

        public void setAllowApplyGroup(String allowApplyGroup) {
            this.allowApplyGroup = allowApplyGroup;
        }

        public String getSistt() {
            return sistt;
        }

        public void setSistt(String sistt) {
            this.sistt = sistt;
        }
    }
}
