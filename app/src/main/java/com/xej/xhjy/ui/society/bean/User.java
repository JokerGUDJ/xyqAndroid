package com.xej.xhjy.ui.society.bean;


/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: User
 * @Description:
 * @Author: lihy_0203
 * @CreateDate: 2018/12/26 下午5:08
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/26 下午5:08
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class User {
	private String id;
	private String name;
	private String headUrl;
	public User(String id, String name, String headUrl){
		this.id = id;
		this.name = name;
		this.headUrl = headUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	@Override
	public String toString() {
		return "id = " + id
				+ "; name = " + name
				+ "; headUrl = " + headUrl;
	}
}
