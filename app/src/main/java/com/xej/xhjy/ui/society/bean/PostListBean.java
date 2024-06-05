package com.xej.xhjy.ui.society.bean;

import java.util.List;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: PostListBean
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午4:40
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午4:40
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PostListBean {


    /**
     * code : 0
     * content : [{"commitId":"586914089029906454","content":"I just ","createTime":"1548319652908","flag":"0","id":"538037150424424448","insertDate":"20190124","modifyDate":"20190124","operatorId":"1000111","orgId":"499177429575827456","orgNm":"鑫合易家","posterId":"535762808038678528","posterName":"黄祖祥","replyList":[{"content":"Www","from":"黄祖祥","id":"538037600708124672","to":"","utterId":"535762808038678528"},{"content":"Yuyyyyy","from":"黄祖祥","id":"538066578265268224","to":"","utterId":"535762808038678528"},{"content":"Tyttt","from":"北凉王","id":"538071116812574720","to":"黄祖祥","utterId":"535762808038678528"},{"content":"Yyyttttt","from":"北凉王","id":"538071173771223040","to":"黄祖祥","utterId":"535762808038678528"}],"socialLike":[{"commitId":"586914089029906454","creatTime":"1548320781217","flag":"0","id":"538041882954096640","insertDate":"20190124","likeName":"黄祖祥","messageId":"538037150424424448","modifyDate":"20190124","operatorId":"1000111","peopleId":"535762808038678528","stt":"0","userId":"535762808038678528"},{"commitId":"586914089029906454","creatTime":"1548327739126","flag":"0","id":"538071066518675456","insertDate":"20190124","likeName":"北凉王","messageId":"538037150424424448","modifyDate":"20190124","operatorId":"1000111","peopleId":"528516309684805632","stt":"0","userId":"535762808038678528"}],"userCenterId":"499226128427417600","whetherAttention":"1","whetherLike":true,"whetherReply":true},{"accessoryUoloadList":"/tosend/201901/1548319615844.mp4,/tosend/201901/1548319619342.jpg","commitId":"586914089029906458","content":"事情","createTime":"1548319622576","fileType":"2","flag":"0","id":"538037023202795520","insertDate":"20190124","modifyDate":"20190124","operatorId":"1000111","orgId":"499177429575827456","orgNm":"鑫合易家","posterId":"528516309684805632","posterName":"北凉王","replyList":[],"site":"你在哪里","userCenterId":"499226799595749376","whetherAttention":"0","whetherLike":false,"whetherReply":false},{"accessoryUoloadList":"/tosend/201901/1548317602694.jpg,/tosend/201901/1548317599882.jpg","commitId":"586914089029906454","content":"年终奖可以啊","createTime":"1548317627384","fileType":"1","flag":"0","id":"538028654761009152","insertDate":"20190124","modifyDate":"20190124","operatorId":"1000111","orgId":"499177429575827456","orgNm":"鑫合易家","posterId":"528516309684805632","posterName":"北凉王","replyList":[],"site":"你在哪里","topicId":"534771733438324736","topicName":"44444","userCenterId":"499226799595749376","whetherAttention":"0","whetherLike":false,"whetherReply":false}]
     * msg : succ
     * page : {"number":0,"numberOfElements":3,"size":10,"totalElements":3,"totalPages":1}
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
         * numberOfElements : 3
         * size : 10
         * totalElements : 3
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
         * commitId : 586914089029906454
         * content : I just
         * createTime : 1548319652908
         * flag : 0
         * id : 538037150424424448
         * insertDate : 20190124
         * modifyDate : 20190124
         * operatorId : 1000111
         * orgId : 499177429575827456
         * orgNm : 鑫合易家
         * posterId : 535762808038678528
         * posterName : 黄祖祥
         * replyList : [{"content":"Www","from":"黄祖祥","id":"538037600708124672","to":"","utterId":"535762808038678528"},{"content":"Yuyyyyy","from":"黄祖祥","id":"538066578265268224","to":"","utterId":"535762808038678528"},{"content":"Tyttt","from":"北凉王","id":"538071116812574720","to":"黄祖祥","utterId":"535762808038678528"},{"content":"Yyyttttt","from":"北凉王","id":"538071173771223040","to":"黄祖祥","utterId":"535762808038678528"}]
         * socialLike : [{"commitId":"586914089029906454","creatTime":"1548320781217","flag":"0","id":"538041882954096640","insertDate":"20190124","likeName":"黄祖祥","messageId":"538037150424424448","modifyDate":"20190124","operatorId":"1000111","peopleId":"535762808038678528","stt":"0","userId":"535762808038678528"},{"commitId":"586914089029906454","creatTime":"1548327739126","flag":"0","id":"538071066518675456","insertDate":"20190124","likeName":"北凉王","messageId":"538037150424424448","modifyDate":"20190124","operatorId":"1000111","peopleId":"528516309684805632","stt":"0","userId":"535762808038678528"}]
         * userCenterId : 499226128427417600
         * whetherAttention : 1
         * whetherLike : true
         * whetherReply : true
         * accessoryUoloadList : /tosend/201901/1548319615844.mp4,/tosend/201901/1548319619342.jpg
         * fileType : 2
         * site : 你在哪里
         * topicId : 534771733438324736
         * topicName : 44444
         */

        private String commitId;
        private String adShow;
        private String content;
        private String createTime;
        private String flag;
        private String id;
        private String insertDate;
        private String modifyDate;
        private String operatorId;
        private String orgId;
        private String orgNm;
        private String posterId;
        private String posterName;
        private String userCenterId;
        private String whetherAttention;
        private boolean whetherLike;
        private boolean whetherReply;
        private String accessoryUoloadList;
        private String fileType;
        private String site;
        private String topicId;
        private String topicName;
        private String topicContent;
        private String accessoryList;
        private List<ReplyListBean> replyList;
        private List<SocialLikeBean> socialLike;

        public String getAdShow() {
            return adShow;
        }

        public void setAdShow(String adShow) {
            this.adShow = adShow;
        }

        public String getAccessoryList() {
            return accessoryList;
        }

        public void setAccessoryList(String accessoryList) {
            this.accessoryList = accessoryList;
        }

        public String getCommitId() {
            return commitId;
        }

        public void setCommitId(String commitId) {
            this.commitId = commitId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
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

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public String getOrgNm() {
            return orgNm;
        }

        public void setOrgNm(String orgNm) {
            this.orgNm = orgNm;
        }

        public String getPosterId() {
            return posterId;
        }

        public void setPosterId(String posterId) {
            this.posterId = posterId;
        }

        public String getPosterName() {
            return posterName;
        }

        public void setPosterName(String posterName) {
            this.posterName = posterName;
        }

        public String getUserCenterId() {
            return userCenterId;
        }

        public void setUserCenterId(String userCenterId) {
            this.userCenterId = userCenterId;
        }

        public String getWhetherAttention() {
            return whetherAttention;
        }

        public void setWhetherAttention(String whetherAttention) {
            this.whetherAttention = whetherAttention;
        }

        public boolean isWhetherLike() {
            return whetherLike;
        }

        public void setWhetherLike(boolean whetherLike) {
            this.whetherLike = whetherLike;
        }

        public boolean isWhetherReply() {
            return whetherReply;
        }

        public void setWhetherReply(boolean whetherReply) {
            this.whetherReply = whetherReply;
        }

        public String getAccessoryUoloadList() {
            return accessoryUoloadList;
        }

        public void setAccessoryUoloadList(String accessoryUoloadList) {
            this.accessoryUoloadList = accessoryUoloadList;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getTopicId() {
            return topicId;
        }

        public void setTopicId(String topicId) {
            this.topicId = topicId;
        }

        public String getTopicName() {
            return topicName;
        }

        public void setTopicName(String topicName) {
            this.topicName = topicName;
        }

        public String getTopicContent() {
            return topicContent;
        }

        public void setTopicContent(String topicContent) {
            this.topicContent = topicContent;
        }

        public List<ReplyListBean> getReplyList() {
            return replyList;
        }

        public void setReplyList(List<ReplyListBean> replyList) {
            this.replyList = replyList;
        }

        public List<SocialLikeBean> getSocialLike() {
            return socialLike;
        }

        public void setSocialLike(List<SocialLikeBean> socialLike) {
            this.socialLike = socialLike;
        }

        public static class ReplyListBean {
            /**
             * content : Www
             * from : 黄祖祥
             * id : 538037600708124672
             * to :
             * utterId : 535762808038678528
             */

            private String content;
            private String from;
            private String id;
            private String to;
            private String utterId;
            private String createTime;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getFrom() {
                return from;
            }

            public void setFrom(String from) {
                this.from = from;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTo() {
                return to;
            }

            public void setTo(String to) {
                this.to = to;
            }

            public String getUtterId() {
                return utterId;
            }

            public void setUtterId(String utterId) {
                this.utterId = utterId;
            }
        }

        public static class SocialLikeBean {
            /**
             * commitId : 586914089029906454   专委会id
             * creatTime : 1548320781217
             * flag : 0
             * id : 538041882954096640
             * insertDate : 20190124
             * likeName : 黄祖祥    点赞的人
             * messageId : 538037150424424448   帖子id
             * modifyDate : 20190124
             * operatorId : 1000111
             * peopleId : 535762808038678528   发帖id
             * stt : 0
             * userId : 535762808038678528
             */

            private String commitId;
            private String creatTime;
            private String flag;
            private String id;
            private String insertDate;
            private String likeName;
            private String messageId;
            private String modifyDate;
            private String operatorId;
            private String peopleId;
            private String stt;
            private String userId;

            public String getCommitId() {
                return commitId;
            }

            public void setCommitId(String commitId) {
                this.commitId = commitId;
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

            public String getLikeName() {
                return likeName;
            }

            public void setLikeName(String likeName) {
                this.likeName = likeName;
            }

            public String getMessageId() {
                return messageId;
            }

            public void setMessageId(String messageId) {
                this.messageId = messageId;
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

            public String getPeopleId() {
                return peopleId;
            }

            public void setPeopleId(String peopleId) {
                this.peopleId = peopleId;
            }

            public String getStt() {
                return stt;
            }

            public void setStt(String stt) {
                this.stt = stt;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }

    }

}
