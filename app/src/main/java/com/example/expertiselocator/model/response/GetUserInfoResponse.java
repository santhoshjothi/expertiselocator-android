package com.example.expertiselocator.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetUserInfoResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("userID")
    @Expose
    private Integer userID;
    @SerializedName("masterUserID")
    @Expose
    private Integer masterUserID;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("division")
    @Expose
    private String division;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;
    @SerializedName("department")
    @Expose
    private String department;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("accountId")
    @Expose
    private String accountId;
    @SerializedName("accountJId")
    @Expose
    private String accountJId;
    @SerializedName("profileStatus")
    @Expose
    private String profileStatus;
    @SerializedName("chatStatus")
    @Expose
    private String chatStatus;
    @SerializedName("roleId")
    @Expose
    private Integer roleId;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;
    @SerializedName("isDisabledforSearch")
    @Expose
    private Integer isDisabledforSearch;
    @SerializedName("createdBy")
    @Expose
    private Integer createdBy;
    @SerializedName("createdDate")
    @Expose
    private String createdDate;
    @SerializedName("modifiedBy")
    @Expose
    private Integer modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("isMessage")
    @Expose
    private String isMessage;
    @SerializedName("messageCount")
    @Expose
    private Integer messageCount;
    @SerializedName("isChatVisible")
    @Expose
    private String isChatVisible;
    @SerializedName("termsandConditions")
    @Expose
    private String termsandConditions;
    @SerializedName("groupID")
    @Expose
    private Integer groupID;
    @SerializedName("versionNo")
    @Expose
    private String versionNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
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

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountJId() {
        return accountJId;
    }

    public void setAccountJId(String accountJId) {
        this.accountJId = accountJId;
    }

    public String getProfileStatus() {
        return profileStatus;
    }

    public void setProfileStatus(String profileStatus) {
        this.profileStatus = profileStatus;
    }

    public String getChatStatus() {
        return chatStatus;
    }

    public void setChatStatus(String chatStatus) {
        this.chatStatus = chatStatus;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Integer modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getIsMessage() {
        return isMessage;
    }

    public void setIsMessage(String isMessage) {
        this.isMessage = isMessage;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }

    public String getIsChatVisible() {
        return isChatVisible;
    }

    public void setIsChatVisible(String isChatVisible) {
        this.isChatVisible = isChatVisible;
    }

    public String getTermsandConditions() {
        return termsandConditions;
    }

    public void setTermsandConditions(String termsandConditions) {
        this.termsandConditions = termsandConditions;
    }

    public Integer getGroupID() {
        return groupID;
    }

    public void setGroupID(Integer groupID) {
        this.groupID = groupID;
    }

    public String getVersionNo() {
        return versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

}
