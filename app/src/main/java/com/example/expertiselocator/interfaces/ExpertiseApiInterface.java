package com.example.expertiselocator.interfaces;

import com.example.expertiselocator.model.request.GetPostedMessageRequest;
import com.example.expertiselocator.model.response.GetPostedMessagesResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ExpertiseApiInterface {

    @Headers({
            "Content-Type: application/json"
    })

    @POST("PostComment/GetPostedMessages")
    Call<List<GetPostedMessagesResponse>> getPostedMessage(@Body GetPostedMessageRequest getPostedMessageRequest);
}
