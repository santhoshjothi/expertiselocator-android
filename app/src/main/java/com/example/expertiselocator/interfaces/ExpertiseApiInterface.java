package com.example.expertiselocator.interfaces;

import com.example.expertiselocator.model.request.AddPostRequest;
import com.example.expertiselocator.model.request.GetPostedMessageRequest;
import com.example.expertiselocator.model.request.LoginRequest;
import com.example.expertiselocator.model.request.UserInfoRequest;
import com.example.expertiselocator.model.response.GetPostedMessagesResponse;
import com.example.expertiselocator.model.response.GetUserInfoResponse;
import com.example.expertiselocator.model.response.LoginResponse;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ExpertiseApiInterface {

    @Headers({"Content-Type: application/json"})
    @POST("PostComment/GetPostedMessages")
    Call<List<GetPostedMessagesResponse>> getPostedMessage(@Body GetPostedMessageRequest getPostedMessageRequest);

    @Headers({"Content-Type: application/json"})
    @POST("Login/AuthenticateUser")
    Call<LoginResponse> getLogin(@Body LoginRequest getPostedMessageRequest);


    @Headers({"Content-Type: application/json"})
    @POST("Login/GetUserInfo_ByUserID")
    Call<List<GetUserInfoResponse>> getUserInfo(@Body UserInfoRequest getPostedMessageRequest);

    @Headers({"Content-Type: application/json"})
    @POST("PostComment/AddPosts")
    Call<String> getAddPosts(@Body AddPostRequest request);


}
