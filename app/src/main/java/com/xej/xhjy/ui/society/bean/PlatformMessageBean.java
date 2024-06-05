package com.xej.xhjy.ui.society.bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: PlatformMessageBean
 * @Description: 平台消息
 * @Author: lihy_0203
 * @CreateDate: 2019/1/30 上午10:40
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/30 上午10:40
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PlatformMessageBean {


    /**
     * code : 0
     * content : [{"accessoryList":"/tosend/201902/1551338124960.jpg","centerId":"530401504759283712","content":"AUTO","creatTime":"1551406190458","flag":"0","id":"550983027820474368","insertDate":"20190301","mark":"1","modifyDate":"20190301","notifyStt":"1","operatorId":"1000111","phone":"18000000000","tab":"2","userId":"530420846263754752","userName":"由于1"},{"accessoryList":"/tosend/201903/1551402867154.JPG","centerId":"530401504759283712","content":"AUTO","creatTime":"1551406780789","flag":"0","id":"550985504234024960","insertDate":"20190301","mark":"1","modifyDate":"20190301","operatorId":"1000111","phone":"18000000000","tab":"2","userId":"530420846263754752","userName":"由于1"},{"accessoryList":"/tosend/201903/1551402867154.JPG","centerId":"530401504759283712","content":"%E6%8B%B1%E6%89%8B%E7%9B%B8%E8%AE%A9%E4%B8%AD","creatTime":"1551406784787","flag":"0","id":"550985520486952960","insertDate":"20190301","mark":"1","modifyDate":"20190301","operatorId":"1000111","phone":"18000000000","tab":"0","userId":"530420846263754752","userName":"由于1"},{"id":"550992406527512576","insertDate":"20190301","mark":"0","meetId":"549687910857846784","messageId":"AUTO","modifyDate":"20190301","noticeType":"0","notifyStt":"0","operatorId":"1000111","phone":"18000000000","placeName":"AUTO","pushMessage":"阿斯顿发送到发送到安慰法士大夫","title":"下次VB主打歌","userId":"AUTO"}]
     * msg : succ
     * page : {"number":0,"numberOfElements":4,"size":10,"totalElements":4,"totalPages":1}
     */
    /**
     * 会议消息
     */
    public static final int MEETING = 1;
    /**
     * 帖子消息
     */
    public static final int SOCIETY = 2;
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


    public static class ContentBean implements MultiItemEntity {
        /**
         * accessoryList : /tosend/201902/1551338124960.jpg
         * centerId : 530401504759283712
         * content : AUTO
         * creatTime : 1551406190458
         * flag : 0
         * id : 550983027820474368
         * insertDate : 20190301
         * mark : 1
         * modifyDate : 20190301
         * notifyStt : 1
         * operatorId : 1000111
         * phone : 18000000000
         * tab : 2
         * userId : 530420846263754752
         * userName : 由于1
         * meetId : 549687910857846784
         * messageId : AUTO
         * noticeType : 0
         * placeName : AUTO
         * pushMessage : 阿斯顿发送到发送到安慰法士大夫
         * title : 下次VB主打歌
         */

        private String accessoryList;
        private String centerId;
        private String content;
        private String creatTime;
        private String flag;
        private String id;
        private String insertDate;
        private String mark;
        private String modifyDate;
        private String notifyStt;
        private String operatorId;
        private String phone;
        private String tab;
        private String userId;
        private String userName;
        private String meetId;
        private String messageId;
        private String noticeType;
        private String placeName;
        private String pushMessage;
        private String title;

        private int itemType;

        public String getAccessoryList() {
            return accessoryList;
        }

        public void setAccessoryList(String accessoryList) {
            this.accessoryList = accessoryList;
        }

        public String getCenterId() {
            return centerId;
        }

        public void setCenterId(String centerId) {
            this.centerId = centerId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(String creatTime) {
            this.creatTime = creatTime;
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

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
        }

        public String getNotifyStt() {
            return notifyStt;
        }

        public void setNotifyStt(String notifyStt) {
            this.notifyStt = notifyStt;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
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

        public String getMeetId() {
            return meetId;
        }

        public void setMeetId(String meetId) {
            this.meetId = meetId;
        }

        public String getMessageId() {
            return messageId;
        }

        public void setMessageId(String messageId) {
            this.messageId = messageId;
        }

        public String getNoticeType() {
            return noticeType;
        }

        public void setNoticeType(String noticeType) {
            this.noticeType = noticeType;
        }

        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }

        public String getPushMessage() {
            return pushMessage;
        }

        public void setPushMessage(String pushMessage) {
            this.pushMessage = pushMessage;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        public ContentBean(int itemType) {
            this.itemType = itemType;
        }
    }


}
