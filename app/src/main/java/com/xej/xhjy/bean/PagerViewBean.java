package com.xej.xhjy.bean;

import java.util.List;

/**
 * @ProjectName: XHJYMobileClient
 * @Package: com.xej.xhjy.bean
 * @ClassName: PagerViewBean
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2019/6/12 下午4:47
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/6/12 下午4:47
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class PagerViewBean {

    /**
     * code : 0
     * content : [{"contentPic":"/tec/12.jpg","guide":"同业专营 全面管理","id":"587595974025822208","insertDate":"20190610","introduce":"同业投融资业务管理系统","modifyDate":"20190610","name":"鑫同业","num":"2","operatorId":"1000111","titlePic":"/tec/11.jpg"},{"contentPic":"/tec/22.jpg","guide":"鑫云 为开放而来","id":"587595974025822209","insertDate":"20190610","introduce":"多法人版本互联网普惠平台","modifyDate":"20190610","name":"鑫云+","num":"2","operatorId":"1000111","titlePic":"/tec/21.jpg"},{"contentPic":"/tec/32.jpg","guide":"海量数据 从鑫启航","id":"587595974025822210","insertDate":"20190610","introduce":"大数据应用门户","modifyDate":"20190610","name":"鑫航标","num":"2","operatorId":"1000111","titlePic":"/tec/31.jpg"},{"contentPic":"/tec/42.jpg","guide":"运筹帷幄 决胜千里","id":"587595974025822211","insertDate":"20190610","introduce":"管理者驾驶舱","modifyDate":"20190610","name":"鑫罗盘","num":"2","operatorId":"1000111","titlePic":"/tec/41.jpg"},{"contentPic":"/tec/52.jpg","guide":"运维智造 创见价值","id":"587595974025822212","insertDate":"20190610","introduce":"BPC业务性能管理平台","modifyDate":"20190610","name":"鑫智维","num":"2","operatorId":"1000111","titlePic":"/tec/51.jpg"},{"contentPic":"/tec/62.jpg","guide":"深度洞察 实时监控","id":"587595974025822213","insertDate":"20190610","introduce":"APM应用性能监控系统","modifyDate":"20190610","name":"鑫探侦","num":"2","operatorId":"1000111","titlePic":"/tec/61.jpg"},{"contentPic":"/tec/72.jpg","guide":"保障头寸安全 提升资金效益","id":"587595974025822214","insertDate":"20190610","introduce":"头寸管理系统软件","modifyDate":"20190610","name":"鑫头寸","num":"2","operatorId":"1000111","titlePic":"/tec/71.jpg"},{"contentPic":"/tec/82.jpg","guide":"融通四海 汇兑天下","id":"587595974025822215","insertDate":"20190610","introduce":"商业汇票交易管理系统","modifyDate":"20190610","name":"鑫票据","num":"2","operatorId":"1000111","titlePic":"/tec/81.jpg"},{"contentPic":"/tec/92.jpg","guide":"火眼金睛 多重验证","id":"587595974025822216","insertDate":"20190610","introduce":"多场景人脸识别工具","modifyDate":"20190610","name":"鑫火眼","num":"2","operatorId":"1000111","titlePic":"/tec/91.jpg"},{"contentPic":"/tec/102.jpg","guide":"夯实基础 合规经营","id":"587595974025822217","insertDate":"20190610","introduce":"EAST风险监测","modifyDate":"20190610","name":"鑫监查","num":"2","operatorId":"1000111","titlePic":"/tec/101.jpg"},{"contentPic":"/tec/112.jpg","guide":"快速定位 化解风险","id":"587595974025822218","insertDate":"20190610","introduce":"Fortify SCA静态源代码安全扫描分析软件","modifyDate":"20190610","name":"鑫智扫","num":"2","operatorId":"1000111","titlePic":"/tec/111.jpg"},{"contentPic":"/tec/122.jpg","guide":"智能评测 胜券在握","id":"587595974025822219","insertDate":"20190610","introduce":"信用评级云平台","modifyDate":"20190610","name":"鑫债评","num":"2","operatorId":"1000111","titlePic":"/tec/121.jpg"}]
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
         * contentPic : /tec/12.jpg
         * guide : 同业专营 全面管理
         * id : 587595974025822208
         * insertDate : 20190610
         * introduce : 同业投融资业务管理系统
         * modifyDate : 20190610
         * name : 鑫同业
         * num : 2
         * operatorId : 1000111
         * titlePic : /tec/11.jpg
         */

        private String contentPic;
        private String guide;
        private String id;
        private String insertDate;
        private String introduce;
        private String modifyDate;
        private String name;
        private String num;
        private String operatorId;
        private String titlePic;

        public String getContentPic() {
            return contentPic;
        }

        public void setContentPic(String contentPic) {
            this.contentPic = contentPic;
        }

        public String getGuide() {
            return guide;
        }

        public void setGuide(String guide) {
            this.guide = guide;
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

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getModifyDate() {
            return modifyDate;
        }

        public void setModifyDate(String modifyDate) {
            this.modifyDate = modifyDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public String getTitlePic() {
            return titlePic;
        }

        public void setTitlePic(String titlePic) {
            this.titlePic = titlePic;
        }
    }
}
