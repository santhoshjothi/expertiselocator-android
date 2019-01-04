package com.example.expertiselocator.model.response;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResponse implements Serializable{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userName")
    @Expose
    private Object userName;
    @SerializedName("password")
    @Expose
    private Object password;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("language")
    @Expose
    private Object language;
    @SerializedName("userID")
    @Expose
    private Integer userID;
    @SerializedName("masterUserID")
    @Expose
    private Integer masterUserID;
    @SerializedName("firstName")
    @Expose
    private Object firstName;
    @SerializedName("lastName")
    @Expose
    private Object lastName;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("division")
    @Expose
    private Object division;
    @SerializedName("phoneNumber")
    @Expose
    private Object phoneNumber;
    @SerializedName("department")
    @Expose
    private Object department;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("picture")
    @Expose
    private Object picture;
    @SerializedName("accountId")
    @Expose
    private Object accountId;
    @SerializedName("accountJId")
    @Expose
    private Object accountJId;
    @SerializedName("profileStatus")
    @Expose
    private Object profileStatus;
    @SerializedName("chatStatus")
    @Expose
    private Object chatStatus;
    @SerializedName("roleId")
    @Expose
    private Integer roleId;
    @SerializedName("role")
    @Expose
    private Object role;
    @SerializedName("profilePicture")
    @Expose
    private Object profilePicture;
    @SerializedName("isDisabledforSearch")
    @Expose
    private Integer isDisabledforSearch;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;
    @SerializedName("createdDate")
    @Expose
    private Object createdDate;
    @SerializedName("modifiedBy")
    @Expose
    private Integer modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    private Object modifiedDate;
    @SerializedName("error")
    @Expose
    private Object error;
    @SerializedName("isMessage")
    @Expose
    private Boolean isMessage;
    @SerializedName("messageCount")
    @Expose
    private Integer messageCount;
    @SerializedName("isChatVisible")
    @Expose
    private Object isChatVisible;
    @SerializedName("termsandConditions")
    @Expose
    private Object termsandConditions;
    @SerializedName("groupID")
    @Expose
    private Integer groupID;
    @SerializedName("versionNo")
    @Expose
    private Object versionNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getUserName() {
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getLanguage() {
        return language;
    }

    public void setLanguage(Object language) {
        this.language = language;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getMasterUserID() {
        return masterUserID;
    }

    public void setMasterUserID(Integer masterUserID) {
        this.masterUserID = masterUserID;
    }

    public Object getFirstName() {
        return firstName;
    }

    public void setFirstName(Object firstName) {
        this.firstName = firstName;
    }

    public Object getLastName() {
        return lastName;
    }

    public void setLastName(Object lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Object getDivision() {
        return division;
    }

    public void setDivision(Object division) {
        this.division = division;
    }

    public Object getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(Object phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Object getDepartment() {
        return department;
    }

    public void setDepartment(Object department) {
        this.department = department;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Object getPicture() {
        return picture;
    }

    public void setPicture(Object picture) {
        this.picture = picture;
    }

    public Object getAccountId() {
        return accountId;
    }

    public void setAccountId(Object accountId) {
        this.accountId = accountId;
    }

    public Object getAccountJId() {
        return accountJId;
    }

    public void setAccountJId(Object accountJId) {
        this.accountJId = accountJId;
    }

    public Object getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(Object profileStatus) {
        this.profileStatus = profileStatus;
    }

    public Object getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(Object chatStatus) {
        this.chatStatus = chatStatus;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Object getRole() {
        return role;
    }

    public void setRole(Object role) {
        this.role = role;
    }

    public Object getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Object profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Integer getIsDisabledforSearch() {
        return isDisabledforSearch;
    }

    public void setIsDisabledforSearch(Integer isDisabledforSearch) {
        this.isDisabledforSearch = isDisabledforSearch;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Object getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Object getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Object modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }

    public Boolean getIsMessage() {
        return isMessage;
    }

    public void setIsMessage(Boolean isMessage) {
        this.isMessage = isMessage;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public Object getIsChatVisible() {
        return isChatVisible;
    }

    public void setIsChatVisible(Object isChatVisible) {
        this.isChatVisible = isChatVisible;
    }

    public Object getTermsandConditions() {
        return termsandConditions;
    }

    public void setTermsandConditions(Object termsandConditions) {
        this.termsandConditions = termsandConditions;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public Object getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(Object versionNo) {
        this.versionNo = versionNo;
    }

}