package com.xej.xhjy.ui.society.bean;

import java.util.List;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: ImageUpLoadBean
 * @Description: 图片上传bean
 * @Author: lihy_0203
 * @CreateDate: 2018/12/29 上午9:33
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/29 上午9:33
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ImageUpLoadBean {


    /**
     * code : 0
     * content : [{"imgPath":"/tosend/201812/1546047078220.jpg","localPath":"/home/wljy/uat/files/social/messageImage/201812/1546047076686.jpg","type":"1"},{"imgPath":"/tosend/201812/1546047073693.jpg","localPath":"/home/wljy/uat/files/social/messageImage/201812/1546047072269.jpg","type":"1"}]
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
         * imgPath : /tosend/201812/1546047078220.jpg
         * localPath : /home/wljy/uat/files/social/messageImage/201812/1546047076686.jpg
         * type : 1
         */

        private String imgPath;
        private String localPath;
        private String type;

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getLocalPath() {
            return localPath;
        }

        public void setLocalPath(String localPath) {
            this.localPath = localPath;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
