package com.netease.nim.uikit.business.team.Bean;


import android.os.Parcel;
import android.os.Parcelable;

import com.mcxtzhang.indexlib.IndexBar.bean.BaseIndexPinyinBean;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.List;

/**
 * @ProjectName: XHJYMobileClient
 * @Package: com.xej.xhjy.ui.society.bean
 * @ClassName: ContactGroupBean
 * @Description: 类作用描述
 * @Author: lihy_0203
 * @CreateDate: 2019/7/2 上午9:34
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/7/2 上午9:34
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class ContactGroupBean implements Parcelable {


    /**
     * code : 0
     * content : [{"accId":"578577551765676032","centerId":"578577549920215040","flag":"0","id":"578577553288208384","insertDate":"20190516","modifyDate":"20190516","name":"213","nameFirst":"###","operatorId":"1000111","token":"9146a712f2dfd82ba9fde2d563bdba2e"},{"accId":"578541979403689984","centerId":"578541977923133440","flag":"0","id":"578541980091555840","insertDate":"20190516","modifyDate":"20190516","name":"123","nameFirst":"###","operatorId":"1000111","token":"3f345587db1c657dcf96df3bfbbd4192"},{"accId":"579960589980409856","centerId":"552223021729988608","flag":"0","id":"579960590806687744","insertDate":"20190520","modifyDate":"20190520","name":"艾弗森","nameFirst":"AFS","operatorId":"1000111","token":"dadb4547c9b469bdf5a77454c16ca802"},{"accId":"594184796259950592","centerId":"594184794938699776","flag":"0","id":"594184796826181632","insertDate":"20190628","modifyDate":"20190628","name":"阿士大夫","nameFirst":"ASDF","operatorId":"1000111","token":"edc2473b09c88f07606b739a6475b68a"},{"accId":"562663763556311040","centerId":"562663761832484864","flag":"0","id":"562663764298702848","insertDate":"20190402","modifyDate":"20190402","name":"阿士大夫","nameFirst":"ASDF","operatorId":"1000111","token":"986e6c9f4807ef7017a8cec80eeef51a"},{"accId":"565894513793572864","centerId":"499535528518123520","flag":"0","id":"565894514577907712","insertDate":"20190411","modifyDate":"20190411","name":"boby","nameFirst":"BOBY","operatorId":"1000111","token":"16f256da8319be152211713f76fa428c"},{"accId":"565894423565705216","centerId":"501354789318328320","flag":"0","id":"565894424228405248","insertDate":"20190411","modifyDate":"20190411","name":"boby","nameFirst":"BOBY","operatorId":"1000111","token":"2dd960b27bb5213505ced08b0d15d170"},{"accId":"560895500321169408","centerId":"560895496936398848","flag":"0","id":"560895501134864384","insertDate":"20190328","modifyDate":"20190328","name":"出差3","nameFirst":"CC#","operatorId":"1000111","token":"05081f9c4dcc13a54e2d892a8371a930"},{"accId":"560893546912161792","centerId":"560893539735740416","flag":"0","id":"560893549328080896","insertDate":"20190328","modifyDate":"20190328","name":"出差1","nameFirst":"CC#","operatorId":"1000111","token":"f4edea0de4e53427b3dec1ccf6efc66e"},{"accId":"566277894129721344","flag":"0","id":"566277894616260608","insertDate":"20190412","modifyDate":"20190412","name":"超管","nameFirst":"CG","operatorId":"1000111","token":"c5c272899680863d392d9c6ee8189d2f"}]
     * msg : succ
     * page : {"number":0,"numberOfElements":10,"size":10,"totalElements":234,"totalPages":24}
     */

    private String code;
    private String msg;
    private PageBean page;
    private List<ContentBean> content;

    protected ContactGroupBean(Parcel in) {
        code = in.readString();
        msg = in.readString();
    }

    public static final Creator<ContactGroupBean> CREATOR = new Creator<ContactGroupBean>() {
        @Override
        public ContactGroupBean createFromParcel(Parcel in) {
            return new ContactGroupBean(in);
        }

        @Override
        public ContactGroupBean[] newArray(int size) {
            return new ContactGroupBean[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(code);
        parcel.writeString(msg);
    }


    public static class PageBean {
        /**
         * number : 0
         * numberOfElements : 10
         * size : 10
         * totalElements : 234
         * totalPages : 24
         */

        private int number;
        private int numberOfElements;
        private int size;
        private int totalElements;
        private int totalPages;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

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
    }

    public static class ContentBean extends BaseIndexPinyinBean implements Parcelable {
        /**
         * accId : 578577551765676032
         * centerId : 578577549920215040
         * flag : 0
         * id : 578577553288208384
         * insertDate : 20190516
         * modifyDate : 20190516
         * name : 213
         * nameFirst : ###
         * operatorId : 1000111
         * token : 9146a712f2dfd82ba9fde2d563bdba2e
         */

        private String accId;
        private String centerId;
        private String flag;
        private String id;
        private String insertDate;

        public ContentBean(NimUserInfo userInfo) {
            accId = userInfo.getAccount();
            name = userInfo.getName();
            orgName = userInfo.getSignature();
        }

        public ContentBean(String accId, String name, String orgName) {
            this.accId = accId;
            this.name = name;
            this.orgName = orgName;
        }

        protected ContentBean(Parcel in) {
            accId = in.readString();
            centerId = in.readString();
            flag = in.readString();
            id = in.readString();
            insertDate = in.readString();
            modifyDate = in.readString();
            name = in.readString();
            nameFirst = in.readString();
            operatorId = in.readString();
            token = in.readString();
            forPhone = in.readByte() != 0;
            orgName = in.readString();
        }

        public static final Creator<ContentBean> CREATOR = new Creator<ContentBean>() {
            @Override
            public ContentBean createFromParcel(Parcel in) {
                return new ContentBean(in);
            }

            @Override
            public ContentBean[] newArray(int size) {
                return new ContentBean[size];
            }
        };

        public boolean isForPhone() {
            return forPhone;
        }

        public void setForPhone(boolean forPhone) {
            this.forPhone = forPhone;
        }

        private String modifyDate;
        private String name;
        private String nameFirst;
        private String operatorId;
        private String token;
        private boolean forPhone;
        private String orgName;

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getOrgName() {
            return orgName;
        }

        public String getAccId() {
            return accId;
        }

        public void setAccId(String accId) {
            this.accId = accId;
        }

        public String getCenterId() {
            return centerId;
        }

        public void setCenterId(String centerId) {
            this.centerId = centerId;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
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

        public String getNameFirst() {
            return nameFirst;
        }

        public void setNameFirst(String nameFirst) {
            this.nameFirst = nameFirst;
        }

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @Override
        public String getTarget() {
            return name;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(accId);
            parcel.writeString(centerId);
            parcel.writeString(flag);
            parcel.writeString(id);
            parcel.writeString(insertDate);
            parcel.writeString(modifyDate);
            parcel.writeString(name);
            parcel.writeString(nameFirst);
            parcel.writeString(operatorId);
            parcel.writeString(token);
            parcel.writeByte((byte) (forPhone ? 1 : 0));
            parcel.writeString(orgName);
        }
    }
}
