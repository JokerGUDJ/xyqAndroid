package com.xej.xhjy.bean;

import java.util.List;

/**
 * @class SearchComplanyBean
 * @author dazhi
 * @Createtime 2018/6/18 11:30
 * @description 机构搜索bean
 * @Revisetime
 * @Modifier
 */
public class SearchComplanyBean {
    /**
     * code : 0
     * content : [{"id":"5afd88fcc66a4968ada5a1bbb8a02d2e","addr":"1","orgName":"南京银行股份有限公司","orgDictId":"49"},{"id":"acba59e9f456462e8ea48b45ecccc190","addr":"11","orgName":"合肥科技农村商业银行股份有限公司","orgDictId":"30426"},{"id":"44d3b2fc269940b2950f55dbf2a272db","addr":"1","orgName":"江苏东台农村商业银行股份有限公司","orgDictId":"30366"},{"orgNo":"123","id":"0aa20ca85d2741558a57f51cb1b581e1","orgName":"test1"},{"orgNo":"123","id":"4b3e803c00ea437b9884a21997b96af9","orgName":"test2"},{"id":"6fa15d6decb243438ac6be59f17c5668","orgName":"山东乳山农村商业银行股份有限公司","orgDictId":"30609"}]
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
         * id : 5afd88fcc66a4968ada5a1bbb8a02d2e
         * addr : 1
         * orgName : 南京银行股份有限公司
         * orgDictId : 49
         * orgNo : 123
         */

        private String id;
        private String addr;
        private String orgName;
        private String orgDictId;
        private String orgNo;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getOrgDictId() {
            return orgDictId;
        }

        public void setOrgDictId(String orgDictId) {
            this.orgDictId = orgDictId;
        }

        public String getOrgNo() {
            return orgNo;
        }

        public void setOrgNo(String orgNo) {
            this.orgNo = orgNo;
        }
    }
}
