package com.xej.xhjy.ui.society.bean;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: XEJClubClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: ContactBean
 * @Description: 通讯录实体类
 * @Author: lihy_0203
 * @CreateDate: 2018/12/27 下午2:36
 * @UpdateUser: 更新者
 * @UpdateDate: 2018/12/27 下午2:36
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ContactBean implements Serializable {


    /**
     * code : 0
     * msg : succ
     * page : {"totalElements":18,"totalPages":6,"number":0,"size":3,"numberOfElements":3}
     * content : [{"id":"694185678933733376","orgId":"499248648870330368","orgInfoLess":{"id":"499248648870330368","orgName":"齐鲁银行股份有限公司"},"commitId":"586914089029906454","userroleId":"691956530026860544","roleRole":{"id":"691956530026860544","usertype":"分管行领导","commitId":"586914089029906454","userentype":null,"sequence":"7"},"title":"斯蒂芬斯蒂芬     1","name":"132r","phone":"111-11111111","mobile":"11111111111","wechat":"1111111111","mail":"11111111111111111","address":"11111111","updateTime":"2020-04-08 16:56:20","centerId":null,"isManager":"no"},{"id":"694185679017619456","orgId":"499248648870330368","orgInfoLess":{"id":"499248648870330368","orgName":"齐鲁银行股份有限公司"},"commitId":"586914089029906454","userroleId":"691956655264583680","roleRole":{"id":"691956655264583680","usertype":"理财部门负责人","commitId":"586914089029906454","userentype":null,"sequence":"6"},"title":"vba","name":"csad","phone":"111-11111111","mobile":"11111111111","wechat":"111111","mail":"111111111","address":"11","updateTime":"2020-04-08 16:56:20","centerId":null,"isManager":"no"},{"id":"694185679084728320","orgId":"499248648870330368","orgInfoLess":{"id":"499248648870330368","orgName":"齐鲁银行股份有限公司"},"commitId":"586914089029906454","userroleId":"691956707907293184","roleRole":{"id":"691956707907293184","usertype":"投行部门负责人","commitId":"586914089029906454","userentype":null,"sequence":"5"},"title":"ns","name":"jw","phone":"111-11111111","mobile":"11111111111","wechat":"11111111","mail":"11111111","address":"11111111","updateTime":"2020-04-08 16:56:20","centerId":null,"isManager":"no"}]
     */

    private String code;
    private String msg;
    private PageBean page;
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

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<ContentBean> getContent() {
        return content;
    }

    public void setContent(List<ContentBean> content) {
        this.content = content;
    }

    public static class PageBean {
        /**
         * totalElements : 18
         * totalPages : 6
         * number : 0
         * size : 3
         * numberOfElements : 3
         */

        private int totalElements;
        private int totalPages;
        private int number;
        private int size;
        private int numberOfElements;

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }
    }

    public static class ContentBean extends BaseIndexPinyinBean implements Serializable{
        /**
         * id : 694185678933733376
         * orgId : 499248648870330368
         * orgInfoLess : {"id":"499248648870330368","orgName":"齐鲁银行股份有限公司"}
         * commitId : 586914089029906454
         * userroleId : 691956530026860544
         * roleRole : {"id":"691956530026860544","usertype":"分管行领导","commitId":"586914089029906454","userentype":null,"sequence":"7"}
         * title : 斯蒂芬斯蒂芬     1
         * name : 132r
         * phone : 111-11111111
         * mobile : 11111111111
         * wechat : 1111111111
         * mail : 11111111111111111
         * address : 11111111
         * updateTime : 2020-04-08 16:56:20
         * centerId : null
         * isManager : no
         */

        private String id;
        private String orgId;
        private OrgInfoLessBean orgInfoLess;
        private String commitId;
        private String userroleId;
        private RoleRoleBean roleRole;
        private String title;
        private String name;
        private String phone;
        private String mobile;
        private String wechat;
        private String mail;
        private String address;
        private String updateTime;
        private Object centerId;
        private String isManager;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrgId() {
            return orgId;
        }

        public void setOrgId(String orgId) {
            this.orgId = orgId;
        }

        public OrgInfoLessBean getOrgInfoLess() {
            return orgInfoLess;
        }

        public void setOrgInfoLess(OrgInfoLessBean orgInfoLess) {
            this.orgInfoLess = orgInfoLess;
        }

        public String getCommitId() {
            return commitId;
        }

        public void setCommitId(String commitId) {
            this.commitId = commitId;
        }

        public String getUserroleId() {
            return userroleId;
        }

        public void setUserroleId(String userroleId) {
            this.userroleId = userroleId;
        }

        public RoleRoleBean getRoleRole() {
            return roleRole;
        }

        public void setRoleRole(RoleRoleBean roleRole) {
            this.roleRole = roleRole;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getWechat() {
            return wechat;
        }

        public void setWechat(String wechat) {
            this.wechat = wechat;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public Object getCenterId() {
            return centerId;
        }

        public void setCenterId(Object centerId) {
            this.centerId = centerId;
        }

        public String getIsManager() {
            return isManager;
        }

        public void setIsManager(String isManager) {
            this.isManager = isManager;
        }

        @Override
        public String getTarget() {
            return name;
        }

        public static class OrgInfoLessBean implements Serializable{
            /**
             * id : 499248648870330368
             * orgName : 齐鲁银行股份有限公司
             */

            private String id;
            private String orgName;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrgName() {
                return orgName;
            }

            public void setOrgName(String orgName) {
                this.orgName = orgName;
            }
        }

        public static class RoleRoleBean implements Serializable{
            /**
             * id : 691956530026860544
             * usertype : 分管行领导
             * commitId : 586914089029906454
             * userentype : null
             * sequence : 7
             */

            private String id;
            private String usertype;
            private String commitId;
            private Object userentype;
            private String sequence;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUsertype() {
                return usertype;
            }

            public void setUsertype(String usertype) {
                this.usertype = usertype;
            }

            public String getCommitId() {
                return commitId;
            }

            public void setCommitId(String commitId) {
                this.commitId = commitId;
            }

            public Object getUserentype() {
                return userentype;
            }

            public void setUserentype(Object userentype) {
                this.userentype = userentype;
            }

            public String getSequence() {
                return sequence;
            }

            public void setSequence(String sequence) {
                this.sequence = sequence;
            }
        }
    }
}
