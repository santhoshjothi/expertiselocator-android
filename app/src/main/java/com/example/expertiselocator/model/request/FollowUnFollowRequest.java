package com.example.expertiselocator.model.request;

public class FollowUnFollowRequest {

    String userId;
    String followingsUserID;
    String createdBy;
    String modifiedBy;
    String Flag;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFollowingsUserID() {
        return followingsUserID;
    }

    public void setFollowingsUserID(String followingsUserID) {
        this.followingsUserID = followingsUserID;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }
}
