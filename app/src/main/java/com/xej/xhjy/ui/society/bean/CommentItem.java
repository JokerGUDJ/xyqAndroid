package com.xej.xhjy.ui.society.bean;

import java.io.Serializable;


/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: CommentItem
 * @Description:
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class CommentItem implements Serializable {

	private String id;
	private User user;
	private User toReplyUser;
	private String content;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public User getToReplyUser() {
		return toReplyUser;
	}
	public void setToReplyUser(User toReplyUser) {
		this.toReplyUser = toReplyUser;
	}
	
}
