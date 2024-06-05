package com.netease.nim.uikit.utils;


/**
 * @ProjectName: XHJY_NEW_UAT
 * @Package: com.netease.nim.uikit.utils
 * @ClassName: ImUtils
 * @Description: 常量配置类
 * @Author: lihy_0203
 * @CreateDate: 2019/2/27 下午1:46
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/2/27 下午1:46
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ImUtils {
    /**
     * 生产地址
     */
    //public static String BASE_IP  = "https://www.goldenalliance.com.cn/";//全局IP
    /**
     * UAT地址
     */
    public static String BASE_IP = "https://mbank-test.njcb.com.cn:8080/";//全局IP
//      public static String BASE_IP = "http://47.101.50.46:8888/";//全局IP

    public static String HEAD_IMAG_URL = BASE_IP + "xhyjcms/mobile/upload/userImage/";//用户头像地址

    //获取联系人列表
    public static String CONTACT_LIST = BASE_IP + "xhyjcms/gateway/social/socialUser/queryOnePageForTeam.do";//用户头像地址
    //根据teamid查询联系人
    public static String CONTACT_LIST_FOR_TEAM = BASE_IP + "xhyjcms/gateway/social/socialMember/queryOnePageForTid.do";
    //上传会议信息
    public static String UPLOAD_CALL_INFO = BASE_IP + "xhyjcms/gateway/social/videoMeetInfo/quickMeetAdd.do";
    //上传邀请联系人信息
    public static String UPLOAD_USER_INFO = BASE_IP + "xhyjcms/gateway/social/videoMeetUser/addUser.do";
    //获取当前用户被邀请的音视频会议列表
    public final static String QUERY_AV_LIST = BASE_IP + "xhyjcms/gateway/social/videoMeetInfo/queryInvitation.do";
    //更新会议状态
    public final static String UPDATE_MEETING_STATUA = BASE_IP + "xhyjcms/gateway/social/videoMeetInfo/videoMeetUpdate.do";
}
