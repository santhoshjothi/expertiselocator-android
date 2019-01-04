package com.example.expertiselocator.model.request;

public class AddPostRequest {
    public int userID ;
    public String message;
    public String postImage;
    public String postVideo;
    public String assestType ;
    public int isActive ;
    public int postedBy ;
    public int modifiedBy  ;
    public int postOwnerId   ;
    public int sharedPostId  ;


    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostVideo() {
        return postVideo;
    }

    public void setPostVideo(String postVideo) {
        this.postVideo = postVideo;
    }

    public String getAssestType() {
        return assestType;
    }

    public void setAssestType(String assestType) {
        this.assestType = assestType;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(int postedBy) {
        this.postedBy = postedBy;
    }

    public int getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(int modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public int getPostOwnerId() {
        return postOwnerId;
    }

    public void setPostOwnerId(int postOwnerId) {
        this.postOwnerId = postOwnerId;
    }

    public int getSharedPostId() {
        return sharedPostId;
    }

    public void setSharedPostId(int sharedPostId) {
        this.sharedPostId = sharedPostId;
    }
}
