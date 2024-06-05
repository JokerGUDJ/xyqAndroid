package com.xej.xhjy.ui.society.bean;


import java.util.List;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: PostNewMessageBean
 * @Description: 帖子消息bean
 * @Author: lihy_0203
 * @CreateDate: 2019/1/28 下午2:05
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/28 下午2:05
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PostNewMessageBean {


    /**
     * code : 0
     * content : [{"accessoryList":"/tosend/201902/1550296225640.jpg,/tosend/201902/1550296223955.jpg","centerId":"505388891050770432","content":"AUTO","creatTime":"1550730003902","flag":"0","id":"548146895239962624","tab":"2","userId":"532513862503849984","userName":"李七夜"},{"centerId":"505388891050770432","content":"AUTO","creatTime":"1550730562725","flag":"0","id":"548149239113506816","tab":"2","userId":"532513862503849984","userName":"李七夜"},{"centerId":"505388891050770432","content":"AUTO","creatTime":"1550730570466","flag":"0","id":"548149271581614080","tab":"2","userId":"532513862503849984","userName":"李七夜"}]
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
         * accessoryList : /tosend/201902/1550296225640.jpg,/tosend/201902/1550296223955.jpg
         * centerId : 505388891050770432
         * content : AUTO
         * creatTime : 1550730003902
         * flag : 0
         * id : 548146895239962624
         * tab : 2
         * userId : 532513862503849984
         * userName : 李七夜
         */

        private String accessoryList;
        private String centerId;
        private String content;
        private String creatTime;
        private String flag;
        private String id;
        private String tab;
        private String userId;
        private String userName;

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
