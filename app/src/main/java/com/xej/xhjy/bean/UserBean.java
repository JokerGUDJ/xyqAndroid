package com.xej.xhjy.bean;

import java.util.List;

/**
 * 用户信息bean
 */
public class UserBean {


    /**
     * code : 0
     * msg : succ
     * page : null
     * content : {"id":"530401504759283712","orgId":"499248647884668928","divId":"530401504650231808","userName":"由于1","password":null,"idType":null,"idNo":null,"email":null,"phone":null,"userState":"N","gender":"WOMAN","nationality":null,"mobilephone":"18000000000","userplace":null,"createUser":null,"createTime":"2019-01-03 15:06:13","updateUser":null,"addr":"南京浦口区","updateTime":"2019-03-04 11:30:35","postCode":null,"headImg":null,"fax":null,"lockState":null,"authTime":"2019-03-04 11:30:35","authUser":null,"testFlag":null,"recorderMobile":null,"jobId":"487667351622430720","lockStartTime":null,"lockEndTime":null,"createChannel":"WEB","createModule":"MEET","updatePasswordTime":null,"wrongPassCount":0,"unLockTime":null,"firstLoginTime":"2019-03-04 16:15:08","lastLoginTime":null,"lastLoginAddr":null,"qq":null,"divInfo":{"divNm":null,"id":"530401504650231808","divName":"发疯1","addr":null,"zipcode":null,"divState":"N","createTime":"2019-01-03 15:06:13","updateTime":"2019-01-03 15:06:13","orgId":"499248647884668928","orgInfo":{"id":"499248647884668928","orgNo":null,"orgLicence":null,"orgName":"南京银行股份有限公司","orgDictId":"49","orgNm":null,"orgState":"N","addr":null,"orgPro":"BANK","orgType":"Z","zipCode":null,"corpName":null,"createTime":"2018-10-09 15:55:53","corpPasstype":null,"corpPassno":null,"parentOrgId":null,"lockStopTime":null,"lockStartTime":null,"createdUser":null,"updateUser":null,"updateTime":"2018-10-09 15:55:53","platRelation":"M","clubRelation":"C","img":null,"recorderMobile":null,"simpleName":null,"createChannel":"MWEB","createModule":"MWEB","joinDate":"2006-06-01","authTime":null,"authUser":null,"financeId":"499248647863697408","financeInfo":{"id":"499248647863697408","paidMoney":40000,"cbrcRate":"3A","totalMoney":11412.1001,"badLoanRate":0.01,"localShare":5,"shareRank":8,"createTime":"2018-10-09 15:55:53","updateTime":"2018-10-09 15:55:53"},"orgDict":{"id":"49","orgAllName":"南京银行股份有限公司","orgNm":"南京银行","orgAbbr":"njyh","orgNo":null,"orgPy":"nanjingyinhang","orgDictType":"CSSYYH","orgTypeName":"城市商业银行","financialLicenseOrgNo":"B0140H232010001"}}},"orgInfo":{"id":"499248647884668928","orgNo":null,"orgLicence":null,"orgName":"南京银行股份有限公司","orgDictId":"49","orgNm":null,"orgState":"N","addr":null,"orgPro":"BANK","orgType":"Z","zipCode":null,"corpName":null,"createTime":"2018-10-09 15:55:53","corpPasstype":null,"corpPassno":null,"parentOrgId":null,"lockStopTime":null,"lockStartTime":null,"createdUser":null,"updateUser":null,"updateTime":"2018-10-09 15:55:53","platRelation":"M","clubRelation":"C","img":null,"recorderMobile":null,"simpleName":null,"createChannel":"MWEB","createModule":"MWEB","joinDate":"2006-06-01","authTime":null,"authUser":null,"financeId":"499248647863697408","financeInfo":{"id":"499248647863697408","paidMoney":40000,"cbrcRate":"3A","totalMoney":11412.1001,"badLoanRate":0.01,"localShare":5,"shareRank":8,"createTime":"2018-10-09 15:55:53","updateTime":"2018-10-09 15:55:53"},"orgDict":{"id":"49","orgAllName":"南京银行股份有限公司","orgNm":"南京银行","orgAbbr":"njyh","orgNo":null,"orgPy":"nanjingyinhang","orgDictType":"CSSYYH","orgTypeName":"城市商业银行","financialLicenseOrgNo":"B0140H232010001"}},"jobTitle":{"id":"487667351622430720","jobName":"副总经理","jobLevel":"2.2","jobState":"N","createUser":null,"updateUser":null,"createTime":"2018-09-07 16:55:57","updateTime":"2018-09-07 16:55:57","createCifSeq":null,"createModuleId":null},"roleList":null,"commitId":"586914089029906454","ignore":null,"commit":{"id":"586914089029906454","name":"金融市场委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},"commitList":[{"id":"586914089029906454","name":"金融市场委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906455","name":"零售金融委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906456","name":"贸易金融委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906457","name":"公司金融委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906458","name":"小微金融委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906459","name":"金融科技委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906460","name":"风险管理委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906461","name":"人力资源委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906462","name":"其他","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"}]}
     */

    private String code;
    private String msg;
    private Object page;
    private ContentBean content;

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

    public Object getPage() {
        return page;
    }

    public void setPage(Object page) {
        this.page = page;
    }

    public ContentBean getContent() {
        return content;
    }

    public void setContent(ContentBean content) {
        this.content = content;
    }

    public static class ContentBean {
        /**
         * id : 530401504759283712
         * orgId : 499248647884668928
         * divId : 530401504650231808
         * userName : 由于1
         * password : null
         * idType : null
         * idNo : null
         * email : null
         * phone : null
         * userState : N
         * gender : WOMAN
         * nationality : null
         * mobilephone : 18000000000
         * userplace : null
         * createUser : null
         * createTime : 2019-01-03 15:06:13
         * updateUser : null
         * addr : 南京浦口区
         * updateTime : 2019-03-04 11:30:35
         * postCode : null
         * headImg : null
         * fax : null
         * lockState : null
         * authTime : 2019-03-04 11:30:35
         * authUser : null
         * testFlag : null
         * recorderMobile : null
         * jobId : 487667351622430720
         * lockStartTime : null
         * lockEndTime : null
         * createChannel : WEB
         * createModule : MEET
         * updatePasswordTime : null
         * wrongPassCount : 0
         * unLockTime : null
         * firstLoginTime : 2019-03-04 16:15:08
         * lastLoginTime : null
         * lastLoginAddr : null
         * qq : null
         * divInfo : {"divNm":null,"id":"530401504650231808","divName":"发疯1","addr":null,"zipcode":null,"divState":"N","createTime":"2019-01-03 15:06:13","updateTime":"2019-01-03 15:06:13","orgId":"499248647884668928","orgInfo":{"id":"499248647884668928","orgNo":null,"orgLicence":null,"orgName":"南京银行股份有限公司","orgDictId":"49","orgNm":null,"orgState":"N","addr":null,"orgPro":"BANK","orgType":"Z","zipCode":null,"corpName":null,"createTime":"2018-10-09 15:55:53","corpPasstype":null,"corpPassno":null,"parentOrgId":null,"lockStopTime":null,"lockStartTime":null,"createdUser":null,"updateUser":null,"updateTime":"2018-10-09 15:55:53","platRelation":"M","clubRelation":"C","img":null,"recorderMobile":null,"simpleName":null,"createChannel":"MWEB","createModule":"MWEB","joinDate":"2006-06-01","authTime":null,"authUser":null,"financeId":"499248647863697408","financeInfo":{"id":"499248647863697408","paidMoney":40000,"cbrcRate":"3A","totalMoney":11412.1001,"badLoanRate":0.01,"localShare":5,"shareRank":8,"createTime":"2018-10-09 15:55:53","updateTime":"2018-10-09 15:55:53"},"orgDict":{"id":"49","orgAllName":"南京银行股份有限公司","orgNm":"南京银行","orgAbbr":"njyh","orgNo":null,"orgPy":"nanjingyinhang","orgDictType":"CSSYYH","orgTypeName":"城市商业银行","financialLicenseOrgNo":"B0140H232010001"}}}
         * orgInfo : {"id":"499248647884668928","orgNo":null,"orgLicence":null,"orgName":"南京银行股份有限公司","orgDictId":"49","orgNm":null,"orgState":"N","addr":null,"orgPro":"BANK","orgType":"Z","zipCode":null,"corpName":null,"createTime":"2018-10-09 15:55:53","corpPasstype":null,"corpPassno":null,"parentOrgId":null,"lockStopTime":null,"lockStartTime":null,"createdUser":null,"updateUser":null,"updateTime":"2018-10-09 15:55:53","platRelation":"M","clubRelation":"C","img":null,"recorderMobile":null,"simpleName":null,"createChannel":"MWEB","createModule":"MWEB","joinDate":"2006-06-01","authTime":null,"authUser":null,"financeId":"499248647863697408","financeInfo":{"id":"499248647863697408","paidMoney":40000,"cbrcRate":"3A","totalMoney":11412.1001,"badLoanRate":0.01,"localShare":5,"shareRank":8,"createTime":"2018-10-09 15:55:53","updateTime":"2018-10-09 15:55:53"},"orgDict":{"id":"49","orgAllName":"南京银行股份有限公司","orgNm":"南京银行","orgAbbr":"njyh","orgNo":null,"orgPy":"nanjingyinhang","orgDictType":"CSSYYH","orgTypeName":"城市商业银行","financialLicenseOrgNo":"B0140H232010001"}}
         * jobTitle : {"id":"487667351622430720","jobName":"副总经理","jobLevel":"2.2","jobState":"N","createUser":null,"updateUser":null,"createTime":"2018-09-07 16:55:57","updateTime":"2018-09-07 16:55:57","createCifSeq":null,"createModuleId":null}
         * roleList : null
         * commitId : 586914089029906454
         * ignore : null
         * commit : {"id":"586914089029906454","name":"金融市场委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"}
         * commitList : [{"id":"586914089029906454","name":"金融市场委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906455","name":"零售金融委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906456","name":"贸易金融委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906457","name":"公司金融委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906458","name":"小微金融委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906459","name":"金融科技委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906460","name":"风险管理委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906461","name":"人力资源委员会","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"},{"id":"586914089029906462","name":"其他","insertDate":"20180913","modifyDate":"20180913","operatorId":"1001"}]
         */

        private String id;
        private String orgId;
        private String divId;
        private String userName;
        private Object password;
        private Object idType;
        private Object idNo;
        private String email;
        private String phone;
        private String userState;
        private String gender;
        private Object nationality;
        private String mobilephone;
        private Object userplace;
        private Object createUser;
        private String createTime;
        private Object updateUser;
        private String addr;
        private String updateTime;
        private Object postCode;
        private Object headImg;
        private Object fax;
        private Object lockState;
        private String authTime;
        private Object authUser;
        private Object testFlag;
        private Object recorderMobile;
        private String jobId;
        private Object lockStartTime;
        private Object lockEndTime;
        private String createChannel;
        private String createModule;
        private Object updatePasswordTime;
        private int wrongPassCount;
        private Object unLockTime;
        private String firstLoginTime;
        private Object lastLoginTime;
        private Object lastLoginAddr;
        private Object qq;
        private DivInfoBean divInfo;
        private OrgInfoBeanX orgInfo;
        private JobTitleBean jobTitle;
        private Object roleList;
        private String commitId;
        private String ignore;
        private CommitBean commit;
        private List<CommitListBean> commitList;

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

        public String getDivId() {
            return divId;
        }

        public void setDivId(String divId) {
            this.divId = divId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public Object getPassword() {
            return password;
        }

        public void setPassword(Object password) {
            this.password = password;
        }

        public Object getIdType() {
            return idType;
        }

        public void setIdType(Object idType) {
            this.idType = idType;
        }

        public Object getIdNo() {
            return idNo;
        }

        public void setIdNo(Object idNo) {
            this.idNo = idNo;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getUserState() {
            return userState;
        }

        public void setUserState(String userState) {
            this.userState = userState;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Object getNationality() {
            return nationality;
        }

        public void setNationality(Object nationality) {
            this.nationality = nationality;
        }

        public String getMobilephone() {
            return mobilephone;
        }

        public void setMobilephone(String mobilephone) {
            this.mobilephone = mobilephone;
        }

        public Object getUserplace() {
            return userplace;
        }

        public void setUserplace(Object userplace) {
            this.userplace = userplace;
        }

        public Object getCreateUser() {
            return createUser;
        }

        public void setCreateUser(Object createUser) {
            this.createUser = createUser;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(Object updateUser) {
            this.updateUser = updateUser;
        }

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public Object getPostCode() {
            return postCode;
        }

        public void setPostCode(Object postCode) {
            this.postCode = postCode;
        }

        public Object getHeadImg() {
            return headImg;
        }

        public void setHeadImg(Object headImg) {
            this.headImg = headImg;
        }

        public Object getFax() {
            return fax;
        }

        public void setFax(Object fax) {
            this.fax = fax;
        }

        public Object getLockState() {
            return lockState;
        }

        public void setLockState(Object lockState) {
            this.lockState = lockState;
        }

        public String getAuthTime() {
            return authTime;
        }

        public void setAuthTime(String authTime) {
            this.authTime = authTime;
        }

        public Object getAuthUser() {
            return authUser;
        }

        public void setAuthUser(Object authUser) {
            this.authUser = authUser;
        }

        public Object getTestFlag() {
            return testFlag;
        }

        public void setTestFlag(Object testFlag) {
            this.testFlag = testFlag;
        }

        public Object getRecorderMobile() {
            return recorderMobile;
        }

        public void setRecorderMobile(Object recorderMobile) {
            this.recorderMobile = recorderMobile;
        }

        public String getJobId() {
            return jobId;
        }

        public void setJobId(String jobId) {
            this.jobId = jobId;
        }

        public Object getLockStartTime() {
            return lockStartTime;
        }

        public void setLockStartTime(Object lockStartTime) {
            this.lockStartTime = lockStartTime;
        }

        public Object getLockEndTime() {
            return lockEndTime;
        }

        public void setLockEndTime(Object lockEndTime) {
            this.lockEndTime = lockEndTime;
        }

        public String getCreateChannel() {
            return createChannel;
        }

        public void setCreateChannel(String createChannel) {
            this.createChannel = createChannel;
        }

        public String getCreateModule() {
            return createModule;
        }

        public void setCreateModule(String createModule) {
            this.createModule = createModule;
        }

        public Object getUpdatePasswordTime() {
            return updatePasswordTime;
        }

        public void setUpdatePasswordTime(Object updatePasswordTime) {
            this.updatePasswordTime = updatePasswordTime;
        }

        public int getWrongPassCount() {
            return wrongPassCount;
        }

        public void setWrongPassCount(int wrongPassCount) {
            this.wrongPassCount = wrongPassCount;
        }

        public Object getUnLockTime() {
            return unLockTime;
        }

        public void setUnLockTime(Object unLockTime) {
            this.unLockTime = unLockTime;
        }

        public String getFirstLoginTime() {
            return firstLoginTime;
        }

        public void setFirstLoginTime(String firstLoginTime) {
            this.firstLoginTime = firstLoginTime;
        }

        public Object getLastLoginTime() {
            return lastLoginTime;
        }

        public void setLastLoginTime(Object lastLoginTime) {
            this.lastLoginTime = lastLoginTime;
        }

        public Object getLastLoginAddr() {
            return lastLoginAddr;
        }

        public void setLastLoginAddr(Object lastLoginAddr) {
            this.lastLoginAddr = lastLoginAddr;
        }

        public Object getQq() {
            return qq;
        }

        public void setQq(Object qq) {
            this.qq = qq;
        }

        public DivInfoBean getDivInfo() {
            return divInfo;
        }

        public void setDivInfo(DivInfoBean divInfo) {
            this.divInfo = divInfo;
        }

        public OrgInfoBeanX getOrgInfo() {
            return orgInfo;
        }

        public void setOrgInfo(OrgInfoBeanX orgInfo) {
            this.orgInfo = orgInfo;
        }

        public JobTitleBean getJobTitle() {
            return jobTitle;
        }

        public void setJobTitle(JobTitleBean jobTitle) {
            this.jobTitle = jobTitle;
        }

        public Object getRoleList() {
            return roleList;
        }

        public void setRoleList(Object roleList) {
            this.roleList = roleList;
        }

        public String getCommitId() {
            return commitId;
        }

        public void setCommitId(String commitId) {
            this.commitId = commitId;
        }

        public String getIgnore() {
            return ignore;
        }

        public void setIgnore(String ignore) {
            this.ignore = ignore;
        }

        public CommitBean getCommit() {
            return commit;
        }

        public void setCommit(CommitBean commit) {
            this.commit = commit;
        }

        public List<CommitListBean> getCommitList() {
            return commitList;
        }

        public void setCommitList(List<CommitListBean> commitList) {
            this.commitList = commitList;
        }

        public static class DivInfoBean {
            /**
             * divNm : null
             * id : 530401504650231808
             * divName : 发疯1
             * addr : null
             * zipcode : null
             * divState : N
             * createTime : 2019-01-03 15:06:13
             * updateTime : 2019-01-03 15:06:13
             * orgId : 499248647884668928
             * orgInfo : {"id":"499248647884668928","orgNo":null,"orgLicence":null,"orgName":"南京银行股份有限公司","orgDictId":"49","orgNm":null,"orgState":"N","addr":null,"orgPro":"BANK","orgType":"Z","zipCode":null,"corpName":null,"createTime":"2018-10-09 15:55:53","corpPasstype":null,"corpPassno":null,"parentOrgId":null,"lockStopTime":null,"lockStartTime":null,"createdUser":null,"updateUser":null,"updateTime":"2018-10-09 15:55:53","platRelation":"M","clubRelation":"C","img":null,"recorderMobile":null,"simpleName":null,"createChannel":"MWEB","createModule":"MWEB","joinDate":"2006-06-01","authTime":null,"authUser":null,"financeId":"499248647863697408","financeInfo":{"id":"499248647863697408","paidMoney":40000,"cbrcRate":"3A","totalMoney":11412.1001,"badLoanRate":0.01,"localShare":5,"shareRank":8,"createTime":"2018-10-09 15:55:53","updateTime":"2018-10-09 15:55:53"},"orgDict":{"id":"49","orgAllName":"南京银行股份有限公司","orgNm":"南京银行","orgAbbr":"njyh","orgNo":null,"orgPy":"nanjingyinhang","orgDictType":"CSSYYH","orgTypeName":"城市商业银行","financialLicenseOrgNo":"B0140H232010001"}}
             */

            private Object divNm;
            private String id;
            private String divName;
            private Object addr;
            private Object zipcode;
            private String divState;
            private String createTime;
            private String updateTime;
            private String orgId;
            private OrgInfoBean orgInfo;

            public Object getDivNm() {
                return divNm;
            }

            public void setDivNm(Object divNm) {
                this.divNm = divNm;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getDivName() {
                return divName;
            }

            public void setDivName(String divName) {
                this.divName = divName;
            }

            public Object getAddr() {
                return addr;
            }

            public void setAddr(Object addr) {
                this.addr = addr;
            }

            public Object getZipcode() {
                return zipcode;
            }

            public void setZipcode(Object zipcode) {
                this.zipcode = zipcode;
            }

            public String getDivState() {
                return divState;
            }

            public void setDivState(String divState) {
                this.divState = divState;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getOrgId() {
                return orgId;
            }

            public void setOrgId(String orgId) {
                this.orgId = orgId;
            }

            public OrgInfoBean getOrgInfo() {
                return orgInfo;
            }

            public void setOrgInfo(OrgInfoBean orgInfo) {
                this.orgInfo = orgInfo;
            }

            public static class OrgInfoBean {
                /**
                 * id : 499248647884668928
                 * orgNo : null
                 * orgLicence : null
                 * orgName : 南京银行股份有限公司
                 * orgDictId : 49
                 * orgNm : null
                 * orgState : N
                 * addr : null
                 * orgPro : BANK
                 * orgType : Z
                 * zipCode : null
                 * corpName : null
                 * createTime : 2018-10-09 15:55:53
                 * corpPasstype : null
                 * corpPassno : null
                 * parentOrgId : null
                 * lockStopTime : null
                 * lockStartTime : null
                 * createdUser : null
                 * updateUser : null
                 * updateTime : 2018-10-09 15:55:53
                 * platRelation : M
                 * clubRelation : C
                 * img : null
                 * recorderMobile : null
                 * simpleName : null
                 * createChannel : MWEB
                 * createModule : MWEB
                 * joinDate : 2006-06-01
                 * authTime : null
                 * authUser : null
                 * financeId : 499248647863697408
                 * financeInfo : {"id":"499248647863697408","paidMoney":40000,"cbrcRate":"3A","totalMoney":11412.1001,"badLoanRate":0.01,"localShare":5,"shareRank":8,"createTime":"2018-10-09 15:55:53","updateTime":"2018-10-09 15:55:53"}
                 * orgDict : {"id":"49","orgAllName":"南京银行股份有限公司","orgNm":"南京银行","orgAbbr":"njyh","orgNo":null,"orgPy":"nanjingyinhang","orgDictType":"CSSYYH","orgTypeName":"城市商业银行","financialLicenseOrgNo":"B0140H232010001"}
                 */

                private String id;
                private Object orgNo;
                private Object orgLicence;
                private String orgName;
                private String orgDictId;
                private Object orgNm;
                private String orgState;
                private Object addr;
                private String orgPro;
                private String orgType;
                private Object zipCode;
                private Object corpName;
                private String createTime;
                private Object corpPasstype;
                private Object corpPassno;
                private Object parentOrgId;
                private Object lockStopTime;
                private Object lockStartTime;
                private Object createdUser;
                private Object updateUser;
                private String updateTime;
                private String platRelation;
                private String clubRelation;
                private Object img;
                private Object recorderMobile;
                private Object simpleName;
                private String createChannel;
                private String createModule;
                private String joinDate;
                private Object authTime;
                private Object authUser;
                private String financeId;
                private FinanceInfoBean financeInfo;
                private OrgDictBean orgDict;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public Object getOrgNo() {
                    return orgNo;
                }

                public void setOrgNo(Object orgNo) {
                    this.orgNo = orgNo;
                }

                public Object getOrgLicence() {
                    return orgLicence;
                }

                public void setOrgLicence(Object orgLicence) {
                    this.orgLicence = orgLicence;
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

                public Object getOrgNm() {
                    return orgNm;
                }

                public void setOrgNm(Object orgNm) {
                    this.orgNm = orgNm;
                }

                public String getOrgState() {
                    return orgState;
                }

                public void setOrgState(String orgState) {
                    this.orgState = orgState;
                }

                public Object getAddr() {
                    return addr;
                }

                public void setAddr(Object addr) {
                    this.addr = addr;
                }

                public String getOrgPro() {
                    return orgPro;
                }

                public void setOrgPro(String orgPro) {
                    this.orgPro = orgPro;
                }

                public String getOrgType() {
                    return orgType;
                }

                public void setOrgType(String orgType) {
                    this.orgType = orgType;
                }

                public Object getZipCode() {
                    return zipCode;
                }

                public void setZipCode(Object zipCode) {
                    this.zipCode = zipCode;
                }

                public Object getCorpName() {
                    return corpName;
                }

                public void setCorpName(Object corpName) {
                    this.corpName = corpName;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }

                public Object getCorpPasstype() {
                    return corpPasstype;
                }

                public void setCorpPasstype(Object corpPasstype) {
                    this.corpPasstype = corpPasstype;
                }

                public Object getCorpPassno() {
                    return corpPassno;
                }

                public void setCorpPassno(Object corpPassno) {
                    this.corpPassno = corpPassno;
                }

                public Object getParentOrgId() {
                    return parentOrgId;
                }

                public void setParentOrgId(Object parentOrgId) {
                    this.parentOrgId = parentOrgId;
                }

                public Object getLockStopTime() {
                    return lockStopTime;
                }

                public void setLockStopTime(Object lockStopTime) {
                    this.lockStopTime = lockStopTime;
                }

                public Object getLockStartTime() {
                    return lockStartTime;
                }

                public void setLockStartTime(Object lockStartTime) {
                    this.lockStartTime = lockStartTime;
                }

                public Object getCreatedUser() {
                    return createdUser;
                }

                public void setCreatedUser(Object createdUser) {
                    this.createdUser = createdUser;
                }

                public Object getUpdateUser() {
                    return updateUser;
                }

                public void setUpdateUser(Object updateUser) {
                    this.updateUser = updateUser;
                }

                public String getUpdateTime() {
                    return updateTime;
                }

                public void setUpdateTime(String updateTime) {
                    this.updateTime = updateTime;
                }

                public String getPlatRelation() {
                    return platRelation;
                }

                public void setPlatRelation(String platRelation) {
                    this.platRelation = platRelation;
                }

                public String getClubRelation() {
                    return clubRelation;
                }

                public void setClubRelation(String clubRelation) {
                    this.clubRelation = clubRelation;
                }

                public Object getImg() {
                    return img;
                }

                public void setImg(Object img) {
                    this.img = img;
                }

                public Object getRecorderMobile() {
                    return recorderMobile;
                }

                public void setRecorderMobile(Object recorderMobile) {
                    this.recorderMobile = recorderMobile;
                }

                public Object getSimpleName() {
                    return simpleName;
                }

                public void setSimpleName(Object simpleName) {
                    this.simpleName = simpleName;
                }

                public String getCreateChannel() {
                    return createChannel;
                }

                public void setCreateChannel(String createChannel) {
                    this.createChannel = createChannel;
                }

                public String getCreateModule() {
                    return createModule;
                }

                public void setCreateModule(String createModule) {
                    this.createModule = createModule;
                }

                public String getJoinDate() {
                    return joinDate;
                }

                public void setJoinDate(String joinDate) {
                    this.joinDate = joinDate;
                }

                public Object getAuthTime() {
                    return authTime;
                }

                public void setAuthTime(Object authTime) {
                    this.authTime = authTime;
                }

                public Object getAuthUser() {
                    return authUser;
                }

                public void setAuthUser(Object authUser) {
                    this.authUser = authUser;
                }

                public String getFinanceId() {
                    return financeId;
                }

                public void setFinanceId(String financeId) {
                    this.financeId = financeId;
                }

                public FinanceInfoBean getFinanceInfo() {
                    return financeInfo;
                }

                public void setFinanceInfo(FinanceInfoBean financeInfo) {
                    this.financeInfo = financeInfo;
                }

                public OrgDictBean getOrgDict() {
                    return orgDict;
                }

                public void setOrgDict(OrgDictBean orgDict) {
                    this.orgDict = orgDict;
                }

                public static class FinanceInfoBean {
                    /**
                     * id : 499248647863697408
                     * paidMoney : 40000
                     * cbrcRate : 3A
                     * totalMoney : 11412.1001
                     * badLoanRate : 0.01
                     * localShare : 5
                     * shareRank : 8
                     * createTime : 2018-10-09 15:55:53
                     * updateTime : 2018-10-09 15:55:53
                     */

                    private String id;
                    private int paidMoney;
                    private String cbrcRate;
                    private double totalMoney;
                    private double badLoanRate;
                    private int localShare;
                    private int shareRank;
                    private String createTime;
                    private String updateTime;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public int getPaidMoney() {
                        return paidMoney;
                    }

                    public void setPaidMoney(int paidMoney) {
                        this.paidMoney = paidMoney;
                    }

                    public String getCbrcRate() {
                        return cbrcRate;
                    }

                    public void setCbrcRate(String cbrcRate) {
                        this.cbrcRate = cbrcRate;
                    }

                    public double getTotalMoney() {
                        return totalMoney;
                    }

                    public void setTotalMoney(double totalMoney) {
                        this.totalMoney = totalMoney;
                    }

                    public double getBadLoanRate() {
                        return badLoanRate;
                    }

                    public void setBadLoanRate(double badLoanRate) {
                        this.badLoanRate = badLoanRate;
                    }

                    public int getLocalShare() {
                        return localShare;
                    }

                    public void setLocalShare(int localShare) {
                        this.localShare = localShare;
                    }

                    public int getShareRank() {
                        return shareRank;
                    }

                    public void setShareRank(int shareRank) {
                        this.shareRank = shareRank;
                    }

                    public String getCreateTime() {
                        return createTime;
                    }

                    public void setCreateTime(String createTime) {
                        this.createTime = createTime;
                    }

                    public String getUpdateTime() {
                        return updateTime;
                    }

                    public void setUpdateTime(String updateTime) {
                        this.updateTime = updateTime;
                    }
                }

                public static class OrgDictBean {
                    /**
                     * id : 49
                     * orgAllName : 南京银行股份有限公司
                     * orgNm : 南京银行
                     * orgAbbr : njyh
                     * orgNo : null
                     * orgPy : nanjingyinhang
                     * orgDictType : CSSYYH
                     * orgTypeName : 城市商业银行
                     * financialLicenseOrgNo : B0140H232010001
                     */

                    private String id;
                    private String orgAllName;
                    private String orgNm;
                    private String orgAbbr;
                    private Object orgNo;
                    private String orgPy;
                    private String orgDictType;
                    private String orgTypeName;
                    private String financialLicenseOrgNo;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String getOrgAllName() {
                        return orgAllName;
                    }

                    public void setOrgAllName(String orgAllName) {
                        this.orgAllName = orgAllName;
                    }

                    public String getOrgNm() {
                        return orgNm;
                    }

                    public void setOrgNm(String orgNm) {
                        this.orgNm = orgNm;
                    }

                    public String getOrgAbbr() {
                        return orgAbbr;
                    }

                    public void setOrgAbbr(String orgAbbr) {
                        this.orgAbbr = orgAbbr;
                    }

                    public Object getOrgNo() {
                        return orgNo;
                    }

                    public void setOrgNo(Object orgNo) {
                        this.orgNo = orgNo;
                    }

                    public String getOrgPy() {
                        return orgPy;
                    }

                    public void setOrgPy(String orgPy) {
                        this.orgPy = orgPy;
                    }

                    public String getOrgDictType() {
                        return orgDictType;
                    }

                    public void setOrgDictType(String orgDictType) {
                        this.orgDictType = orgDictType;
                    }

                    public String getOrgTypeName() {
                        return orgTypeName;
                    }

                    public void setOrgTypeName(String orgTypeName) {
                        this.orgTypeName = orgTypeName;
                    }

                    public String getFinancialLicenseOrgNo() {
                        return financialLicenseOrgNo;
                    }

                    public void setFinancialLicenseOrgNo(String financialLicenseOrgNo) {
                        this.financialLicenseOrgNo = financialLicenseOrgNo;
                    }
                }
            }
        }

        public static class OrgInfoBeanX {
            /**
             * id : 499248647884668928
             * orgNo : null
             * orgLicence : null
             * orgName : 南京银行股份有限公司
             * orgDictId : 49
             * orgNm : null
             * orgState : N
             * addr : null
             * orgPro : BANK
             * orgType : Z
             * zipCode : null
             * corpName : null
             * createTime : 2018-10-09 15:55:53
             * corpPasstype : null
             * corpPassno : null
             * parentOrgId : null
             * lockStopTime : null
             * lockStartTime : null
             * createdUser : null
             * updateUser : null
             * updateTime : 2018-10-09 15:55:53
             * platRelation : M
             * clubRelation : C
             * img : null
             * recorderMobile : null
             * simpleName : null
             * createChannel : MWEB
             * createModule : MWEB
             * joinDate : 2006-06-01
             * authTime : null
             * authUser : null
             * financeId : 499248647863697408
             * financeInfo : {"id":"499248647863697408","paidMoney":40000,"cbrcRate":"3A","totalMoney":11412.1001,"badLoanRate":0.01,"localShare":5,"shareRank":8,"createTime":"2018-10-09 15:55:53","updateTime":"2018-10-09 15:55:53"}
             * orgDict : {"id":"49","orgAllName":"南京银行股份有限公司","orgNm":"南京银行","orgAbbr":"njyh","orgNo":null,"orgPy":"nanjingyinhang","orgDictType":"CSSYYH","orgTypeName":"城市商业银行","financialLicenseOrgNo":"B0140H232010001"}
             */

            private String id;
            private Object orgNo;
            private Object orgLicence;
            private String orgName;
            private String orgDictId;
            private Object orgNm;
            private String orgState;
            private Object addr;
            private String orgPro;
            private String orgType;
            private Object zipCode;
            private Object corpName;
            private String createTime;
            private Object corpPasstype;
            private Object corpPassno;
            private Object parentOrgId;
            private Object lockStopTime;
            private Object lockStartTime;
            private Object createdUser;
            private Object updateUser;
            private String updateTime;
            private String platRelation;
            private String clubRelation;
            private Object img;
            private Object recorderMobile;
            private Object simpleName;
            private String createChannel;
            private String createModule;
            private String joinDate;
            private Object authTime;
            private Object authUser;
            private String financeId;
            private FinanceInfoBeanX financeInfo;
            private OrgDictBeanX orgDict;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Object getOrgNo() {
                return orgNo;
            }

            public void setOrgNo(Object orgNo) {
                this.orgNo = orgNo;
            }

            public Object getOrgLicence() {
                return orgLicence;
            }

            public void setOrgLicence(Object orgLicence) {
                this.orgLicence = orgLicence;
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

            public Object getOrgNm() {
                return orgNm;
            }

            public void setOrgNm(Object orgNm) {
                this.orgNm = orgNm;
            }

            public String getOrgState() {
                return orgState;
            }

            public void setOrgState(String orgState) {
                this.orgState = orgState;
            }

            public Object getAddr() {
                return addr;
            }

            public void setAddr(Object addr) {
                this.addr = addr;
            }

            public String getOrgPro() {
                return orgPro;
            }

            public void setOrgPro(String orgPro) {
                this.orgPro = orgPro;
            }

            public String getOrgType() {
                return orgType;
            }

            public void setOrgType(String orgType) {
                this.orgType = orgType;
            }

            public Object getZipCode() {
                return zipCode;
            }

            public void setZipCode(Object zipCode) {
                this.zipCode = zipCode;
            }

            public Object getCorpName() {
                return corpName;
            }

            public void setCorpName(Object corpName) {
                this.corpName = corpName;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public Object getCorpPasstype() {
                return corpPasstype;
            }

            public void setCorpPasstype(Object corpPasstype) {
                this.corpPasstype = corpPasstype;
            }

            public Object getCorpPassno() {
                return corpPassno;
            }

            public void setCorpPassno(Object corpPassno) {
                this.corpPassno = corpPassno;
            }

            public Object getParentOrgId() {
                return parentOrgId;
            }

            public void setParentOrgId(Object parentOrgId) {
                this.parentOrgId = parentOrgId;
            }

            public Object getLockStopTime() {
                return lockStopTime;
            }

            public void setLockStopTime(Object lockStopTime) {
                this.lockStopTime = lockStopTime;
            }

            public Object getLockStartTime() {
                return lockStartTime;
            }

            public void setLockStartTime(Object lockStartTime) {
                this.lockStartTime = lockStartTime;
            }

            public Object getCreatedUser() {
                return createdUser;
            }

            public void setCreatedUser(Object createdUser) {
                this.createdUser = createdUser;
            }

            public Object getUpdateUser() {
                return updateUser;
            }

            public void setUpdateUser(Object updateUser) {
                this.updateUser = updateUser;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getPlatRelation() {
                return platRelation;
            }

            public void setPlatRelation(String platRelation) {
                this.platRelation = platRelation;
            }

            public String getClubRelation() {
                return clubRelation;
            }

            public void setClubRelation(String clubRelation) {
                this.clubRelation = clubRelation;
            }

            public Object getImg() {
                return img;
            }

            public void setImg(Object img) {
                this.img = img;
            }

            public Object getRecorderMobile() {
                return recorderMobile;
            }

            public void setRecorderMobile(Object recorderMobile) {
                this.recorderMobile = recorderMobile;
            }

            public Object getSimpleName() {
                return simpleName;
            }

            public void setSimpleName(Object simpleName) {
                this.simpleName = simpleName;
            }

            public String getCreateChannel() {
                return createChannel;
            }

            public void setCreateChannel(String createChannel) {
                this.createChannel = createChannel;
            }

            public String getCreateModule() {
                return createModule;
            }

            public void setCreateModule(String createModule) {
                this.createModule = createModule;
            }

            public String getJoinDate() {
                return joinDate;
            }

            public void setJoinDate(String joinDate) {
                this.joinDate = joinDate;
            }

            public Object getAuthTime() {
                return authTime;
            }

            public void setAuthTime(Object authTime) {
                this.authTime = authTime;
            }

            public Object getAuthUser() {
                return authUser;
            }

            public void setAuthUser(Object authUser) {
                this.authUser = authUser;
            }

            public String getFinanceId() {
                return financeId;
            }

            public void setFinanceId(String financeId) {
                this.financeId = financeId;
            }

            public FinanceInfoBeanX getFinanceInfo() {
                return financeInfo;
            }

            public void setFinanceInfo(FinanceInfoBeanX financeInfo) {
                this.financeInfo = financeInfo;
            }

            public OrgDictBeanX getOrgDict() {
                return orgDict;
            }

            public void setOrgDict(OrgDictBeanX orgDict) {
                this.orgDict = orgDict;
            }

            public static class FinanceInfoBeanX {
                /**
                 * id : 499248647863697408
                 * paidMoney : 40000
                 * cbrcRate : 3A
                 * totalMoney : 11412.1001
                 * badLoanRate : 0.01
                 * localShare : 5
                 * shareRank : 8
                 * createTime : 2018-10-09 15:55:53
                 * updateTime : 2018-10-09 15:55:53
                 */

                private String id;
                private int paidMoney;
                private String cbrcRate;
                private double totalMoney;
                private double badLoanRate;
                private int localShare;
                private int shareRank;
                private String createTime;
                private String updateTime;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public int getPaidMoney() {
                    return paidMoney;
                }

                public void setPaidMoney(int paidMoney) {
                    this.paidMoney = paidMoney;
                }

                public String getCbrcRate() {
                    return cbrcRate;
                }

                public void setCbrcRate(String cbrcRate) {
                    this.cbrcRate = cbrcRate;
                }

                public double getTotalMoney() {
                    return totalMoney;
                }

                public void setTotalMoney(double totalMoney) {
                    this.totalMoney = totalMoney;
                }

                public double getBadLoanRate() {
                    return badLoanRate;
                }

                public void setBadLoanRate(double badLoanRate) {
                    this.badLoanRate = badLoanRate;
                }

                public int getLocalShare() {
                    return localShare;
                }

                public void setLocalShare(int localShare) {
                    this.localShare = localShare;
                }

                public int getShareRank() {
                    return shareRank;
                }

                public void setShareRank(int shareRank) {
                    this.shareRank = shareRank;
                }

                public String getCreateTime() {
                    return createTime;
                }

                public void setCreateTime(String createTime) {
                    this.createTime = createTime;
                }

                public String getUpdateTime() {
                    return updateTime;
                }

                public void setUpdateTime(String updateTime) {
                    this.updateTime = updateTime;
                }
            }

            public static class OrgDictBeanX {
                /**
                 * id : 49
                 * orgAllName : 南京银行股份有限公司
                 * orgNm : 南京银行
                 * orgAbbr : njyh
                 * orgNo : null
                 * orgPy : nanjingyinhang
                 * orgDictType : CSSYYH
                 * orgTypeName : 城市商业银行
                 * financialLicenseOrgNo : B0140H232010001
                 */

                private String id;
                private String orgAllName;
                private String orgNm;
                private String orgAbbr;
                private Object orgNo;
                private String orgPy;
                private String orgDictType;
                private String orgTypeName;
                private String financialLicenseOrgNo;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getOrgAllName() {
                    return orgAllName;
                }

                public void setOrgAllName(String orgAllName) {
                    this.orgAllName = orgAllName;
                }

                public String getOrgNm() {
                    return orgNm;
                }

                public void setOrgNm(String orgNm) {
                    this.orgNm = orgNm;
                }

                public String getOrgAbbr() {
                    return orgAbbr;
                }

                public void setOrgAbbr(String orgAbbr) {
                    this.orgAbbr = orgAbbr;
                }

                public Object getOrgNo() {
                    return orgNo;
                }

                public void setOrgNo(Object orgNo) {
                    this.orgNo = orgNo;
                }

                public String getOrgPy() {
                    return orgPy;
                }

                public void setOrgPy(String orgPy) {
                    this.orgPy = orgPy;
                }

                public String getOrgDictType() {
                    return orgDictType;
                }

                public void setOrgDictType(String orgDictType) {
                    this.orgDictType = orgDictType;
                }

                public String getOrgTypeName() {
                    return orgTypeName;
                }

                public void setOrgTypeName(String orgTypeName) {
                    this.orgTypeName = orgTypeName;
                }

                public String getFinancialLicenseOrgNo() {
                    return financialLicenseOrgNo;
                }

                public void setFinancialLicenseOrgNo(String financialLicenseOrgNo) {
                    this.financialLicenseOrgNo = financialLicenseOrgNo;
                }
            }
        }

        public static class JobTitleBean {
            /**
             * id : 487667351622430720
             * jobName : 副总经理
             * jobLevel : 2.2
             * jobState : N
             * createUser : null
             * updateUser : null
             * createTime : 2018-09-07 16:55:57
             * updateTime : 2018-09-07 16:55:57
             * createCifSeq : null
             * createModuleId : null
             */

            private String id;
            private String jobName;
            private String jobLevel;
            private String jobState;
            private Object createUser;
            private Object updateUser;
            private String createTime;
            private String updateTime;
            private Object createCifSeq;
            private Object createModuleId;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getJobName() {
                return jobName;
            }

            public void setJobName(String jobName) {
                this.jobName = jobName;
            }

            public String getJobLevel() {
                return jobLevel;
            }

            public void setJobLevel(String jobLevel) {
                this.jobLevel = jobLevel;
            }

            public String getJobState() {
                return jobState;
            }

            public void setJobState(String jobState) {
                this.jobState = jobState;
            }

            public Object getCreateUser() {
                return createUser;
            }

            public void setCreateUser(Object createUser) {
                this.createUser = createUser;
            }

            public Object getUpdateUser() {
                return updateUser;
            }

            public void setUpdateUser(Object updateUser) {
                this.updateUser = updateUser;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public Object getCreateCifSeq() {
                return createCifSeq;
            }

            public void setCreateCifSeq(Object createCifSeq) {
                this.createCifSeq = createCifSeq;
            }

            public Object getCreateModuleId() {
                return createModuleId;
            }

            public void setCreateModuleId(Object createModuleId) {
                this.createModuleId = createModuleId;
            }
        }

        public static class CommitBean {
            /**
             * id : 586914089029906454
             * name : 金融市场委员会
             * insertDate : 20180913
             * modifyDate : 20180913
             * operatorId : 1001
             */

            private String id;
            private String name;
            private String insertDate;
            private String modifyDate;
            private String operatorId;

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

            public String getInsertDate() {
                return insertDate;
            }

            public void setInsertDate(String insertDate) {
                this.insertDate = insertDate;
            }

            public String getModifyDate() {
                return modifyDate;
            }

            public void setModifyDate(String modifyDate) {
                this.modifyDate = modifyDate;
            }

            public String getOperatorId() {
                return operatorId;
            }

            public void setOperatorId(String operatorId) {
                this.operatorId = operatorId;
            }
        }

        public static class CommitListBean {
            /**
             * id : 586914089029906454
             * name : 金融市场委员会
             * insertDate : 20180913
             * modifyDate : 20180913
             * operatorId : 1001
             */

            private String id;
            private String name;
            private String insertDate;
            private String modifyDate;
            private String operatorId;

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

            public String getInsertDate() {
                return insertDate;
            }

            public void setInsertDate(String insertDate) {
                this.insertDate = insertDate;
            }

            public String getModifyDate() {
                return modifyDate;
            }

            public void setModifyDate(String modifyDate) {
                this.modifyDate = modifyDate;
            }

            public String getOperatorId() {
                return operatorId;
            }

            public void setOperatorId(String operatorId) {
                this.operatorId = operatorId;
            }
        }
    }
}
