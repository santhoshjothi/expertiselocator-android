package com.example.expertiselocator.model.request;

public class GetUserProfileRequest {

    public String UserID;
    public String language;


    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
