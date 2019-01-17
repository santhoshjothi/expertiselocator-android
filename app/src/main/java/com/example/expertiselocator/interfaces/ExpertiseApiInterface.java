package com.example.expertiselocator.interfaces;

import com.example.expertiselocator.model.request.AddPostRequest;
import com.example.expertiselocator.model.request.DeletePostRequest;
import com.example.expertiselocator.model.request.EditDeleteCommentRequest;
import com.example.expertiselocator.model.request.EditPostRequest;
import com.example.expertiselocator.model.request.GetPostedMessageRequest;
import com.example.expertiselocator.model.request.GetUserProfileRequest;
import com.example.expertiselocator.model.request.LoginRequest;
import com.example.expertiselocator.model.request.PostCommentRequest;
import com.example.expertiselocator.model.request.UserInfoRequest;
import com.example.expertiselocator.model.response.GetPostedMessagesResponse;
import com.example.expertiselocator.model.response.GetProfileInfoAboutResponse;
import com.example.expertiselocator.model.response.GetUserInfoResponse;
import com.example.expertiselocator.model.response.LoginResponse;

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
    Call<LoginResponse> getLogin(@Body LoginRequest getLoginDetails);

    @Headers({"Content-Type: application/json"})
    @POST("Login/GetUserInfo_ByUserID")
    Call<List<GetUserInfoResponse>> getUserInfo(@Body UserInfoRequest getUserInfo);

    @Headers({"Content-Type: application/json"})
    @POST("PostComment/AddPosts")
    Call<String> getAddPosts(@Body AddPostRequest request);

    @Headers({"Content-Type: application/json"})
    @POST("Profile/GetProfileInfo_AbtMe")
    Call<List<GetProfileInfoAboutResponse>> getProfileInfoAbout(@Body GetUserProfileRequest request);

    @Headers({"Content-Type: application/json"})
    @POST("PostComment/AddComments")
    Call<Integer> postComment(@Body PostCommentRequest request);

    @Headers({"Content-Type: application/json"})
    @POST("PostComment/DeleteComments")
    Call<Integer> deleteComment(@Body EditDeleteCommentRequest request);

    @Headers({"Content-Type: application/json"})
    @POST("PostComment/EditComments")
    Call<Integer> editComment(@Body EditDeleteCommentRequest request);

    @Headers({"Content-Type: application/json"})
    @POST("PostComment/EditPosts")
    Call<Integer> editPost(@Body EditPostRequest request);

    @Headers({"Content-Type: application/json"})
    @POST("PostComment/DeletePosts")
    Call<Integer> DeletePost(@Body DeletePostRequest request);



}
