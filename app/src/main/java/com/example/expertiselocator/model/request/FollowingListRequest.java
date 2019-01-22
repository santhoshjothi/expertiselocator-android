package com.example.expertiselocator.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowingListRequest {
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("expertUserId")
    @Expose
    private String expertUserId;
    @SerializedName("startIndex")
    @Expose
    private String startIndex;
    @SerializedName("maxCount")
    @Expose
    private String maxCount;
    @SerializedName("Flag")
    @Expose
    private String flag;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getExpertUserId() {
        return expertUserId;
    }

    public void setExpertUserId(String expertUserId) {
        this.expertUserId = expertUserId;
    }

    public String getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(String startIndex) {
        this.startIndex = startIndex;
    }

    public String getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(String maxCount) {
        this.maxCount = maxCount;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
