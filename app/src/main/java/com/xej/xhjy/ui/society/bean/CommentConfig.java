package com.xej.xhjy.ui.society.bean;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: CommentConfig
 * @Description:
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CommentConfig {
    public static enum Type{
        PUBLIC("public"), REPLY("reply");

        private String value;
        private Type(String value){
            this.value = value;
        }

    }
    public int circlePosition;
    public int commentPosition;
    public Type commentType;
    public String replyUser;//被回复内容ID
    public String fatherId; //被回复人的ID
    public String  messageId;//帖子ID
    public String posterId; //发帖人ID


    @Override
    public String toString() {
        String replyUserStr = "";
        if(replyUser != null){
            replyUserStr = replyUser.toString();
        }
        return "circlePosition = " + circlePosition
                + "; commentPosition = " + commentPosition
                + "; commentType ＝ " + commentType
                + "; replyUser = " + replyUserStr;
    }
}
