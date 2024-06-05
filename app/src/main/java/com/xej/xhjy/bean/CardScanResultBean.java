package com.xej.xhjy.bean;

import java.io.Serializable;
import java.util.List;

public class CardScanResultBean implements Serializable{

    private static final long serialVersionUID = 3504911024883643860L;
    /**
     * code : 0
     * content : {"Json":[{"tel_cell":["13718186577"],"success":true,"name":"朱宣安","company":["杭州信雅达风险管理信息技术有限公司"],"addr":["杭州市(滨江区)江南大道3888号信雅达科技大厦","杭州市"],"department":[],"title":["副总经理"],"request_id":"20181023101036_0a88b7326ae168f28f6854013196c10c","email":["zxa@sunyard.com"],"tel_work":["0086057156686376"]}]}
     * msg : succ
     */

    private String code;
    private ContentBean content;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class ContentBean implements Serializable {
        private static final long serialVersionUID = 7114863467185912731L;
        private List<JsonBean> Json;

        public List<JsonBean> getJson() {
            return Json;
        }

        public void setJson(List<JsonBean> Json) {
            this.Json = Json;
        }

        public static class JsonBean implements Serializable{
            private static final long serialVersionUID = -4511875155614252689L;
            /**
             * tel_cell : ["13718186577"]
             * success : true
             * name : 朱宣安
             * company : ["杭州信雅达风险管理信息技术有限公司"]
             * addr : ["杭州市(滨江区)江南大道3888号信雅达科技大厦","杭州市"]
             * department : []
             * title : ["副总经理"]
             * request_id : 20181023101036_0a88b7326ae168f28f6854013196c10c
             * email : ["zxa@sunyard.com"]
             * tel_work : ["0086057156686376"]
             */

            private boolean success;
            private String name;
            private String request_id;
            private List<String> tel_cell;
            private List<String> company;
            private List<String> addr;
            private List<String> department;
            private List<String> title;
            private List<String> email;
            private List<String> tel_work;

            public boolean isSuccess() {
                return success;
            }

            public void setSuccess(boolean success) {
                this.success = success;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getRequest_id() {
                return request_id;
            }

            public void setRequest_id(String request_id) {
                this.request_id = request_id;
            }

            public List<String> getTel_cell() {
                return tel_cell;
            }

            public void setTel_cell(List<String> tel_cell) {
                this.tel_cell = tel_cell;
            }

            public List<String> getCompany() {
                return company;
            }

            public void setCompany(List<String> company) {
                this.company = company;
            }

            public List<String> getAddr() {
                return addr;
            }

            public void setAddr(List<String> addr) {
                this.addr = addr;
            }

            public List<String> getDepartment() {
                return department;
            }

            public void setDepartment(List<String> department) {
                this.department = department;
            }

            public List<String> getTitle() {
                return title;
            }

            public void setTitle(List<String> title) {
                this.title = title;
            }

            public List<String> getEmail() {
                return email;
            }

            public void setEmail(List<String> email) {
                this.email = email;
            }

            public List<String> getTel_work() {
                return tel_work;
            }

            public void setTel_work(List<String> tel_work) {
                this.tel_work = tel_work;
            }
        }
    }
}
