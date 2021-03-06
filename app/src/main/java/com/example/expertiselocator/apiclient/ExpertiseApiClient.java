package com.example.expertiselocator.apiclient;

import android.content.Context;
import android.util.Log;

import com.example.expertiselocator.main.PostActivity;
import com.example.expertiselocator.utils.SharedPreferencesWithAES;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExpertiseApiClient {

    private static final String BASE_URL = "http://el.dewa.salzerinfo.com/api/";

    private static Retrofit retrofit = null;
    private static Retrofit retrofitWithAuthorization = null;

    private static SharedPreferencesWithAES prefs;
    private Context context;
    private static String token;

    public ExpertiseApiClient(Context contex) {
        this.context = contex;
        prefs = SharedPreferencesWithAES.getInstance(context, "expertise_Prefs");
        token = prefs.getString("loginresponse", "");
    }

    public static Retrofit getRetrofit() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static void setRetrofitWithAuthorization(Retrofit retrofitWithAuthorization) {
        ExpertiseApiClient.retrofitWithAuthorization = retrofitWithAuthorization;
    }

    public static Retrofit getRetrofitWithAuthorization() {
        Gson gson = new GsonBuilder().setLenient().create();
        if (retrofitWithAuthorization == null) {
            retrofitWithAuthorization = new Retrofit.Builder()
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(chain -> {
                                Request newRequest = chain.request().newBuilder()
                                        .addHeader("Authorization", "Bearer " + token)
                                        .build();
                                return chain.proceed(newRequest);
                            }).build())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofitWithAuthorization;
    }
}
