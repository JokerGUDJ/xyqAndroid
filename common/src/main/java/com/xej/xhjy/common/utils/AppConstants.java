package com.xej.xhjy.common.utils;

import com.xej.xhjy.common.storage.PerferenceUtils;

import org.json.JSONObject;

/**
 * @class AppConstants 全局常量类 网络接口常量在NetConstants里
 * @author dazhi
 * @Createtime 2018/6/14 15:36
 * @description describe
 * @Revisetimeh
 * @Modifier
 */
public class AppConstants {
    /**是否处于开发模式*/
    public final static boolean IS_DEBUG = false;

    /**全局保证同时只有一个dialog弹出*/
    public static boolean IS_DIALOG_SHOW = false;

    /**是否有网络*/
    public static boolean IS_NETWORK = true;

    /**是否有新消息*/
    public static boolean HAS_NEW_MESSAGE = false;
    /**状态栏高度，px*/
    public static int STATUS_BAR_HEIGHT;
    public static String VERSION = android.os.Build.VERSION.RELEASE;//Android系统版本
    public static String PHONEMODLE = android.os.Build.MODEL;//手机型号
    public static String PHONEBRAND = android.os.Build.BRAND;//手机品牌
    public static String DEVICEID = "";//手机ID
    public static String APP_VERSION = "";//App版本

    /**全局字典*/
    public static JSONObject DATA_DICTIONARY;
    /**全局 userID */
    public static String USER_ID;
    /**全局 用户角色类型 general_member普通用户，org_manager机构用户 ，club_manager机构管理员*/
    public static String USER_ROLES;
    /**全局 用户认证信息 W 待审核 N 已认证 R 已拒绝 */
    public static String USER_STATE;
    /**全局 是否登录 */
    public static boolean IS_LOGIN = false;
    /**网络家园在腾讯开发者上到appid */
    public static String APPID = "wx1e8a8d982d540883";
    /**网络家园在sharesdk上的appid */
    public static String SSDK_APP_ID = "308569f0a6ece";
    /**网络家园在sharesdk上的appsecret */
    public static String SSDK_APP_SECRET = "60541dbc406d847b7d1f9771f88b6636";
    //会议正式appkey
    public static String MEETING_APP_KEY = "mf5a85df9d073731b48bbad138cb";
    /**用户信息存储key*/
    public interface User {
        /**手机号存储key*/
        String PHONE = "user_mobile_phone";
        String HIDDEN_PHONE = "user_mobile_hidden_phone";
        /**固话存储key*/
        String TEL = "user_tel";
        /**userid 存储key*/
        String ID = "user_id";
        /**名字 存储key*/
        String NAME = "user_name";
        /**性别 存储key*/
        String GENDER = "user_gender";
        /**机构名称 存储key*/
        String COMPLANY = "user_complany";
        /**职位名 存储key*/
        String JOB = "user_job_list";
        /**专委会LIST 存储key*/
        String BRANCH_OF = "user_branch_of_list";
        /**专委会id 存储key*/
        String BRANCH_OF_COMMIT_ID= "user_branch_of_commit_id";
        /**专委会name 存储key*/
        String BRANCH_OF_COMMIT_NAME = "user_branch_of_commit_name";
        /**组织机构ID 存储key*/
        String ORGID = "user_orgid";
        /**部门名 存储key*/
        String DEPARTMENT = "user_department";
        /**邮箱名 存储key*/
        String EMAIL = "user_email";
        /**地址 存储key*/
        String ADDRESS = "user_address";
        /**角色 存储key*/
        String ROLES = "user_roles";
        /**审核状态 存储key*/
        String STATE = "user_state";
        /**审核状态 存储key*/
        String IM_ACCOUNT = "im_account";
        /**审核状态 存储key*/
        String IM_TOKEN = "im_token";
        /**IM账号获取 存储key*/
        String IM_CHAT_ACCOUNT = "im_chat_account";
        /**IM账号Token 存储key*/
        String IM_CHAT_TOKEN = "im_chat_token";
        /**帖子列表ID 存储key*/
        String IM_POST_ID = "im_post_id";
        /**专委会查询 存储key**/
        String COMMIT_ID = "commit_id";
        /**会议登录id存储key*/
        String MEETING_ACCOUNT_ID = "meeting_account_id";
        /**会议登录token 存储key*/
        String MEETING_ACCOUNT_TOKEN = "meeting_account_token";
    }

    /**JPUSH跳转信息*/
    public interface JpushClick {
        /**跳转到消息*/
        int GO_MESSAGE = 1;
    }
    /**全局字典缓存key*/
    public static final String DATA_DICTIONARY_KEY = "data_dictionary";
    /**岗位列表缓存key*/
    public static final String DATA_JOB_LIS_KEY = "job_list_save_key";
    /**首页轮播图缓存key*/
    public static final String DATA_BANNER_KEY = "banner_list";
    /**首页轮播图缓存key*/
    public static final String DATA_FIRST_MEET = "first_meet";
    /**线下会议列表缓存key*/
    public static final String DATA_MEET_LIST = "meet_list";
    /**线上会议列表缓存key*/
    public static final String DATA_ONLINE_MEET_LIST = "online_meet_list";
    /**会议列表缓存key*/
    public static final String DATA_FETE_CHECK = "is_fete_check";
    /**专委会缓存key*/
    public static final String DATA_BRANCHOF_ID_KEY = "branch_of_id";
    /**培训标识缓存key*/
    public static final String DATA_TRAINING_ID_KEY = "training_of_meet";
    /**第一次安装app*/
    public static final String IS_FIRST ="first_open";
    /*** 社交地图加载地址*/
    public static final String AMAP_URL = "https://apis.map.qq.com/tools/locpicker?search=1&radius=1000&mapdraggable=0&type=0&key=N5GBZ-3RGCI-GZNG4-5RJTG-4J6D2-T5FTC&referer=myapp&backurl=http://callback";
    /**协议地址*/
    public static final String  AGREEMENT_URL ="https://www.goldenalliance.com.cn/xhyjcms/mobile/Privacypolicy.html";
    /**直播图片前缀*/
    public static final String BYTE_IMG_PREFIX= "http://p1-live.byteimg.com/";
    /**直播图片后缀*/
    public static final String BYTE_IMG_SUFFIX= "~tplv-gjr78lqtd0-image.image";

    /**鑫合会议*/
    public final static int XH_MEETING = 0;
    /**我的会议*/
    public final static int MY_MEETING = 1;

    /**
     * 获取是否俱乐部管理员 club_manager  普通用户 general_member   专委会管理员 com_manager 机构管理员 org_manager
     *
     * @return
     */
    public static boolean isClubManager() {
        USER_ROLES = PerferenceUtils.get(AppConstants.User.ROLES, "");
        if (USER_ROLES.contains("club_manager")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取是否专委会管理员
     *
     * @return
     */
    public static boolean isComManager() {
        USER_ROLES = PerferenceUtils.get(AppConstants.User.ROLES, "");
        if (USER_ROLES.contains("com_manager")) {
            return true;
        } else {
            return false;
        }
    }
}
