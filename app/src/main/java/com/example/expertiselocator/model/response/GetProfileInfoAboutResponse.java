package com.example.expertiselocator.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetProfileInfoAboutResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("userID")
    @Expose
    private Integer userID;
    @SerializedName("language")
    @Expose
    private Object language;
    @SerializedName("picture")
    @Expose
    private Object picture;
    @SerializedName("location")
    @Expose
    private Object location;
    @SerializedName("landLine")
    @Expose
    private Object landLine;
    @SerializedName("mobile")
    @Expose
    private Object mobile;
    @SerializedName("aboutMe")
    @Expose
    private String aboutMe;
    @SerializedName("languages")
    @Expose
    private Object languages;
    @SerializedName("expertIn")
    @Expose
    private String expertIn;
    @SerializedName("specializationSkills")
    @Expose
    private Object specializationSkills;
    @SerializedName("technologySkills")
    @Expose
    private Object technologySkills;
    @SerializedName("status")
    @Expose
    private Object status;
    @SerializedName("createdBy")
    @Expose
    private Object createdBy;
    @SerializedName("createdDate")
    @Expose
    private Object createdDate;
    @SerializedName("modifiedBy")
    @Expose
    private Object modifiedBy;
    @SerializedName("modifiedDate")
    @Expose
    private Object modifiedDate;
    @SerializedName("flag")
    @Expose
    private Object flag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Object getLanguage() {
        return language;
    }

    public void setLanguage(Object language) {
        this.language = language;
    }

    public Object getPicture() {
        return picture;
    }

    public void setPicture(Object picture) {
        this.picture = picture;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public Object getLandLine() {
        return landLine;
    }

    public void setLandLine(Object landLine) {
        this.landLine = landLine;
    }

    public Object getMobile() {
        return mobile;
    }

    public void setMobile(Object mobile) {
        this.mobile = mobile;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public Object getLanguages() {
        return languages;
    }

    public void setLanguages(Object languages) {
        this.languages = languages;
    }

    public String getExpertIn() {
        return expertIn;
    }

    public void setExpertIn(String expertIn) {
        this.expertIn = expertIn;
    }

    public Object getSpecializationSkills() {
        return specializationSkills;
    }

    public void setSpecializationSkills(Object specializationSkills) {
        this.specializationSkills = specializationSkills;
    }

    public Object getTechnologySkills() {
        return technologySkills;
    }

    public void setTechnologySkills(Object technologySkills) {
        this.technologySkills = technologySkills;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Object getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Object createdDate) {
        this.createdDate = createdDate;
    }

    public Object getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Object modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Object getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Object modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Object getFlag() {
        return flag;
    }

    public void setFlag(Object flag) {
        this.flag = flag;
    }
}
