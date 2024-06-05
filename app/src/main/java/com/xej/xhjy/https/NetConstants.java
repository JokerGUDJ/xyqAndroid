package com.xej.xhjy.https;

import com.netease.nim.uikit.utils.ImUtils;

/**
 * @author dazhi
 * @class NetConstants  全局网关以及接口常量
 * @Createtime 2018/6/14 09:05
 * @description describem
 * @Revisetime
 * @Modifier
 */
public class NetConstants {
    /**
     * 开发模式 修改不同的数字即可
     * 0-uat环境 1-sit环境  2-uat内网环境 3-预上线环境 4-生产环境
     */
    public final static int DEVELOP_MODEL = 4;

    public static String BASE_IP = ImUtils.BASE_IP;//全局IP
    public static String BASE_URL = BASE_IP + "xhyjcms/gateway/";//全局请求BaseUrl
    public static String H5_BASE_URL = BASE_IP + "xhyjcms/mobile/index.html#/";//全局H5页面Base地址
    public static String POINT_BASE_URL = BASE_IP + "xhyjcms/mobile/point/index.html#/";//积分权益H5页面Base地址
    public static String HEAD_IMAG_URL = BASE_IP + "xhyjcms/mobile/upload/userImage/";//用户头像地址
    public static String IMAGE_NORMAL = BASE_IP + "xhyjcms/meet";//全局图片Base地址
    public static String POST_IMAGE = BASE_IP + "xhyjcms/social";//帖子图片地址

    public static String IMAGE_VIEWPAGER = BASE_IP + "xhyjcms/meet/";

    public static String WEB_URL = "cmsweb/";//网站相关接口前缀变化

    public static String WEB_LEARN_PLATFROM_UPLOADIMG = "https://tests.exexm.com:813/api/file/images";//移动学习平台上传图片
    public static String WEB_LEARN_PLATFROM_UPLOADVIDEO = "https://tests.exexm.com:813/api/file/video";//移动学习平台上传视频
    public static String WEB_LEARN_PLATFORM_URL = "https://test.exexm.com:8060/test/xhjy/xhjyindex.html?token=";

    static {
        if (DEVELOP_MODEL == 1) {//sit环境
            BASE_IP = "http://222.190.125.58:8080/";//全局IP
//            BASE_URL = BASE_IP + "wljysit/gateway/";//全局请求BaseUrl
            BASE_URL = BASE_IP + "majq/";//全局请求BaseUrl
            H5_BASE_URL = BASE_IP + "wljysit/mobile/index.html#/";//全局H5页面Base地址
            HEAD_IMAG_URL = BASE_IP + "wljysit/mobile/upload/userImage/";//用户头像地址
            IMAGE_NORMAL = BASE_IP + "xhyjcms/meet";//全局图片Base地址
            WEB_URL = "cmssit/";//网站相关接口前缀变化
        } else if (DEVELOP_MODEL == 2) {//UAT内网环境
            BASE_IP = "http://159.1.64.228/";//全局IP
            BASE_URL = BASE_IP + "xhyjcms/gatewayuat/";//全局请求BaseUrl
            H5_BASE_URL = BASE_IP + "xhyjcms/mobile/index.html#/";//全局H5页面Base地址
            HEAD_IMAG_URL = BASE_IP + "xhyjcms/mobile/upload/userImage/";//用户头像地址
            IMAGE_NORMAL = BASE_IP + "xhyjcms/meet";//全局图片Base地址
            WEB_URL = "cmsweb/";//网站相关接口前缀变化
        } else if (DEVELOP_MODEL == 3) {//预上线环境
            BASE_IP = "http://www.goldenalliance.com.cn:8080/";//全局IP
            BASE_URL = BASE_IP + "xhyjcms/gateway/";//全局请求BaseUrl
            H5_BASE_URL = BASE_IP + "xhyjcms/mobile/index.html#/";//全局H5页面Base地址
            HEAD_IMAG_URL = BASE_IP + "xhyjcms/mobile/upload/userImage/";//用户头像地址
            IMAGE_NORMAL = BASE_IP + "xhyjcms/meet";//全局图片Base地址
            WEB_URL = "cmsweb/";//网站相关接口前缀变化
        } else if (DEVELOP_MODEL == 4) {//生产环境
            BASE_IP = "https://www.goldenalliance.com.cn/";//全局IP
            BASE_URL = BASE_IP + "xhyjcms/gateway/";//全局请求BaseUrl
            H5_BASE_URL = BASE_IP + "xhyjcms/mobile/index.html#/";//全局H5页面Base地址
            HEAD_IMAG_URL = BASE_IP + "xhyjcms/mobile/upload/userImage/";//用户头像地址
            IMAGE_NORMAL = BASE_IP + "xhyjcms/meet";//全局图片Base地址
            WEB_URL = "cmsweb/";//网站相关接口前缀变化
            POST_IMAGE = BASE_IP + "xhyjcms/social";//帖子图片地址
            WEB_LEARN_PLATFORM_URL = "https://res.exexm.com/xhjy/common/xhjyindex.html?token=";//移动学习平台地址
            WEB_LEARN_PLATFROM_UPLOADIMG = "https://file.exexm.com/api/file/images";//移动学习平台上传图片
            WEB_LEARN_PLATFROM_UPLOADVIDEO = "https://file.exexm.com/api/file/video";//移动学习平台上产视频
            IMAGE_VIEWPAGER = BASE_IP + "xhyjcms/meet/";
        }
        //本地调试H5页面在此改地址
        //姜
//        H5_BASE_URL = "http://172.17.202.19:8080/#/";
        //李海渊
//        H5_BASE_URL = "http://172.17.211.208:8080/#/";
//        H5_BASE_URL = "http://192.168.0.101:8080/#/";
        //王然
//        H5_BASE_URL = "http://172.17.202.33:8080/#/";
//          H5_BASE_URL = "http://172.17.211.157:8080/#/";
    }


    /**
     * 全局网络请求超时时间,单位是秒
     */
    public final static long CONNECT_TIME_OUT = 20;

    /**
     * 全局网络读取超时时间,单位是秒
     */
    public final static long READ_TIME_OUT = 20;

    /**
     * 全局写入超时时间,单位是秒
     */
    public final static long WRITE_TIME_OUT = 20;

    /**
     * 存储cookie的所有key值的key
     */
    public final static String COOKIE_KEY = "cookie_key";

    /**
     * 鑫E家平台下载地址
     */
    public final static String XEJ_DOWNLOAD_URL = "https://www.xin-e-jia.com/btbMobile/ShareAppToWeiXin.do?_locale=zh_CN&BankId=9999";

    /**
     * app更新文件请求地址
     */
    public final static String CHECK_VERSION = BASE_IP + "xhyjcms/mobile/updateApp.json";

    /**
     * 全局数据字典
     */
    public final static String DATA_DICTIONARY = "meeting/dict/queryAll.do";

    /**
     * 名片识别提交接口
     */
    public final static String CARD_RECOGNITION = "meeting/registerCard/convertCard.do";


    /**
     * 会议列表待审核接口
     */
    public final static String MEETTING_LIST_AUDIT = "meeting/meetBaseInfo/queryOnePageAuditFailed.do";


    /**
     * 登录前获取时间戳
     */
    public final static String LOGIN_TIME = "userCenter/token/getCurrTimeMillis.do";
    /**
     * 获取重置密码的随机数
     */
    public final static String PWD_TIME_MILLIS = "userCenter/token/getResetSafe.do";
    /**
     * 获取注册随机数
     */
    public final static String REGISTER_TIME_MILLIS = "userCenter/token/getRegisterSafe.do";

    /**
     * 登录
     */
    public final static String LOGIN_USER = "userCenter/userInfo/loginByPassword.do";

    /**
     * 注册用户
     */
    public final static String REGISTER_USER = "userCenter/register/addUser.do";

    /**
     * 获取用户信息
     */
    public final static String GET_USER_INFO = "userCenter/userInfo/queryCurrByTokenBody.do";

    /**
     * 检查手机号是否被注册
     */
    public final static String CHECK_MOBILE = "userCenter/userInfo/queryOne.do";

    /**
     * 修改密码
     */
    public final static String MODIFY_PASSWORD = "userCenter/password/modify.do";
    /**
     * 新会议列表接口
     */
    public final static String NEW_MEETTING_LIST = "meeting/meetBaseInfo/queryOnePageForMobileNew.do";
    /**
     * 会议列表校验权限
     */
    public final static String NEW_MEETTING_CHECKED = "meeting/meetBaseInfo/authority.do";
    /**
     * 会议报名校验权限
     */
    public final static String NEW_MEETTING_SIGNUP_CHECKED = "meeting/meetBaseInfo/authorityForSignup.do";
    /**
     * 移动学习平台校验白名单
     */
    public final static String CHECK_WHITE_LIST = "training/TrainingUserInfo/queryByPhone.do";

    /**
     * 忘记密码
     */
    public final static String RESET_PASSWORD = "userCenter/password/reset.do";

    /**
     * 修改手机号之校验原手机号
     */
    public final static String RESET_PHONE_PRE = "userCenter/userInfo/resetMobilephonePre.do";

    /**
     * 修改手机号之校验新手机号
     */
    public final static String RESET_PHONE_NEW = "userCenter/userInfo/resetMobilephone.do";

    /**
     * 修改用户信息
     */
    public final static String UPDATE_USERINFO = "userCenter/userInfo/userInfoUpdate.do";

    /**
     * 登出
     */
    public final static String LOGIN_OUT = "userCenter/userInfo/logout.do";

    /**
     * 机构搜索
     */
    public final static String SEARCH_COMPLANY = "userCenter/orgInfo/getListByKeyWords.do";

    /**
     * 获取职务列表
     */
    public final static String JOB_LIST = "userCenter/jobTitle/queryAll.do";

    /**
     * 获取图形验证码接口
     */
    public final static String GET_IMAGE_AUTH = "userCenter/token/getImageTokenStr.do";

    /**
     * 获取短信验证码接口
     */
    public final static String GET_SMS_AUTH = "userCenter/token/getMsgToken.do";

    /**
     * 二维码签到接口
     */
    public final static String QR_SIGN_IN = "meeting/qrcode/qrCodeSignIn.do";

    /***
     * 扫码查询会议座位/宴会座位
     */
    public final static String QR_FOR_SEAT = "meeting/qrcode/qrCodeForSeat.do";
    /***
     * 扫码查询会议签到信息
     */
    public final static String QR_FOR_SIGNUP = "meeting/meetConferee/findFirstByMobileAndMeetId.do";

    /**
     * 首页最新会议接口
     */
    public final static String FIRST_METTING = "meeting/meetBaseInfo/queryFirstBaseInfo.do";
    /**
     * 首页最新会议待审核接口
     */
    public final static String FIRST_METTING_AUDIT = "meeting/meetBaseInfo/queryFirstBaseInfoFroMobile.do";

    /**
     * 首页头条接口
     */
    public final static String HOT_LINE = "cmsweb/getLastCouncilContent.do";
    /**
     * 公众号消息
     */
    public final static String NEW_HOT_LINE = "cmsweb/getContentList.do";

    /**
     * 首页广告轮播图接口
     */
    public final static String ADVERT_IMAGE = "userCenter/referPic/queryOnePage.do";

    /**
     * 头像上传接口
     */
    public final static String HEAD_IMG_UPLOAD = "userCenter/userInfo/uploadUserImageForMini.do";
    /**
     * 科技产品图片
     */
    public final static String IMAGE_VIEW_PAGER = "meeting/tecProduct/queryAll.do";

    /**
     * jpush注册接口
     */
    public final static String JPUSH_REGITST = "userCenter/mobilePush/add.do";

    /**
     * 是否有新消息接口
     */
    public final static String POST_NEW_MESSAGE = "meeting/meetAndSocial/findByPhoneAndNotifyStt.do";


    /**
     * 庆祝上线接口
     */
    public final static String FETE_CHECK = "userCenter/dict/queryForActivity.do";

    /**
     * 获取登录验证码接口
     */
    public final static String GET_LOGIN_CODE = "userCenter/token/loginGetMsgToken.do";

    /**
     * 验证码登录接口
     */
    public final static String LOGIN_BY_CODE = "userCenter/userInfo/loginByMsgTokenForMobile.do";

    /**
     * 发送帖子
     */
    public final static String SOCIETY_POST = "social/socialMessage/add.do";
    /**
     * 发送图片
     */
    public final static String SOCIETY_POOT_IMAGE = "social/FileUpload/upload.do";

    /**
     * 会议报名上传图片
     */
    public final static String MEET_UPLOAD_HEAD = "meeting/meetPhoto/uploadPhotoConf.do";

    /**
     * 帖子列表查询
     */
    public final static String POST_LIST = "social/socialMessage/queryOnePageForCommit.do";
    /**
     * 帖子搜索查询
     */

    public final static String POST_SEARCH_LIST = "social/socialMessage/queryOnePageForUncer.do";
    /**
     * 个人主页帖子
     */
    public final static String POST_PERSONAL_LIST = "social/socialMessage/queryAllForPersonal.do";
    /**
     * 热门话题
     */
    public final static String HOT_TOPIC = "social/socialTopic/queryOnePage.do";
    /**
     * 我的关注
     */
    public final static String MY_HOT_TOPIC = "social/socialAttention/queryAttentionTopicPage.do";
    /**
     * 话题详情帖子
     */
    public final static String HOT_TOPIC_DETAILS_CONTENT = "social/socialMessage/queryOnePageForCommit.do";
    /**
     * 关注的人
     */
    public final static String FOLLOW_PERSON = "social/socialAttention/queryOnePage.do";

    /**
     * 通讯录
     */
    public final static String ADDRESS_BOOK = "userCenter/socialContact/queryOnePageByCommitId.do";

    /**
     * 通讯录机构搜索
     */
    public final static String ADDRESS_BOOK_ORG = "userCenter/orgInfo/getPageListByKeyWords.do";
    /**
     * 专委会列表查询
     */
    public final static String BRANCH_OF_LIST = "userCenter/Commit/queryAllContactExceptOther.do";
    /**
     * 根据专委会查询联系人
     */
    public final static String BRANCH_OF_CONTACT = "userCenter/socialContact/queryByOrgAndCommitForMobile.do";

    /**
     * 查询多少条未读消息
     */
    public final static String QUERY_NOREAD_MESSAGE = "social/socialNewMessage/queryCount.do";

    /**
     * 删除我的帖子
     */
    public final static String DELATE_MY_POST = "social/socialMessage/delete.do";

    /**
     * 根据手机号查询云信账号
     */
    public final static String QUERY_PHONE_IM_ACCOUND = "social/socialUser/findByPhone.do";

    /**
     * 关注话题/人
     */
    public final static String POST_FOLLOW = "social/socialAttention/add.do";
    /**
     * 取消关注话题/人
     */
    public final static String POST_UNFOLLOW = "social/socialAttention/logDelete.do";

    /**
     * 点赞
     */
    public final static String POST_SOCIALLIKE = "social/socialLike/add.do";
    /**
     * 取消点赞
     */
    public final static String POST_SOCIALUNLIKE = "social/socialLike/cancel.do";
    /**
     * 评论
     */
    public final static String POST_COMMENT = "social/socialReply/add.do";
    /**
     * 取消评论
     */
    public final static String POST_DELETE_COMMENT = "social/socialReply/delete.do";
    /**
     * 个人主页个人信息
     */
    public final static String POST_PERSONAL_INFO = "social/socialUserCenter/queryFromUserCenterCheck.do";


    /**
     * 查询群组Id
     */
    public final static String QUERY_ORGID_FOR_GROUP_ID = "social/socialGroup/queryOneById.do";
    /**
     * 话题详情关注
     */
    public final static String QUERY_HOT_TOPIC_FOLLOW = "social/socialTopic/whetherAtt.do";
    /**
     * 帖子未读消息
     */
    public final static String QUERY_NOREADNOTICE = "social/socialNewMessage/queryNoReadNotice.do";
    /**
     * 忽略不修改专委会
     */

    public final static String SET_IGNORE = "userCenter/userInfo/setIgnore.do";

    /**
     * 平台消息新接口
     */
    public final static String SYSTEM_NEW_MESSAGE = "meeting/meetAndSocial/findNewByMeetAndSocial.do";

    /**
     * 消息已读未读
     */

    public final static String POST__NEW_MESSAGE_READ = "meeting/meetAndSocial/updateStt.do";

    /**
     * 根据手机号查询云信账号
     */
    public final static String QUERY_PHONE_IM_ACCOUNT = "social/socialUser/findByOtherPhone.do";

    /**
     * 培训弹框接口
     */
    public final static String QUERY_TRAINING = "meeting/annualRegister/regInfo.do";

    //鑫直播相关接口

    /**
     * 鑫直播首页查询接口
     */
    public final static String  QUERY_XING_LIVEING= "live/liveInfo/queryForMobileFront.do";

    /**
     * 鑫直播更多查询接口
     */
    public final static String  QUERY_LIVEING_FOR_PAGE= "live/liveInfo/queryOnePageForMobile.do";
    /**
     * 鑫直播权限查询
     */
    public final static String  QUERY_VIEW_AUTHORITY= "live/liveMember/queryAuthority.do";
    /**
     * 鑫直播banner接口
     */
    public final static String GET_BANNER_LIVEING = "live/liveInfo/queryForMiniBanner.do";
    /**
     * 在线会议预约接口
     */
    public final static String ONLINE_MEETING_ORDER = "social/videoMeetInfo/addSec.do";

    /**
     * 查下首页鑫合会议
     */
    public final static String QUERY_MEETINGS_HOME = "social/videoMeetInfo/queryOnePageFrontForMobile.do";

    /**
     * 线上会议详情查询
     */
    public final static String QUERY_MEETING_INFO = "social/videoMeetInfo/queryMeetAndMemberById.do";
    /**
     * 更新线上会议信息
     */
    public final static String UPDATE_MEETING_INFO = "social/videoMeetInfo/updateSec.do";
    /**
     * 查询线上会议列表
     */
    public final static String QUERY_ONLINE_MEETINGS = "social/videoMeetInfo/queryOnePageForListV2.do";
    /**
     * 删除线上会议
     */
    public final static String DEL_ONLINE_MEETING = "social/videoMeetInfo/delete.do";
    /**
     * 删除线上会议
     */
    public final static String NEW_HOT_NEWS = "meeting/article/getContentList.do";

    /**
     * 全部会议列表接口
     */
    public final static String ALL_MEETTING_LIST = "social/videoMeetInfo/queryOnePageForAll.do";
    /**
     * 线上会议报名
     */
    public final static String SINGUP_MEETING = "social/videoMeetUser/addMeetUser.do";
    /**
     * 我的会议，线下会议
     */
    public final static String MY_OFFLINE_MEETING = "meeting/meetBaseInfo/queryOnePage.do";
    /**
     * 我的会议，线上会议
     */
    public final static String MY_ONLINE_MEETING = "social/videoMeetInfo/queryOnePageForSelfV2.do";

    /**
     * 积分获取接口
     */
    public final static String MY_POINTS = "meeting/pack/integralCount/queryIntegralTotalForMobile.do";

    /**
     * 埋点接口
     */
    public final static String EVENT_TRACKING = "userCenter/dataEventTracking/add.do";

    /**
     * 注销账户
     */
    public final static String LOGOUT_ACCOUNT = "userCenter/userInfo/updateStateBySelf.do";

    /**
     * 给机构联系人发送会议报名短信
     */
    public final static String SEND_SMS_TO_ORG_MANAGER = "userCenter/orgInfo/sendSMSToOrgManager.do";
    /**
     * 查询机构联系人
     */
    public final static String QUERY_ORG_MANAGER = "userCenter/userInfo/queryOrgMembersByRole.do";

}
