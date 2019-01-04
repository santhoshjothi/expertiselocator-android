package com.example.expertiselocator.apiclient;

import android.util.Log;

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

    public static Retrofit getRetrofitWithAuthorization(final String tokens) {
        Gson gson = new GsonBuilder().setLenient().create();
       // Log.v("Authorization",""+token);
       //final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1bmlxdWVfbmFtZSI6InR1c2VyNiIsIm5iZiI6MTU0NjQyNjM3MiwiZXhwIjoxNTQ3MDMxMTcyLCJpYXQiOjE1NDY0MjYzNzJ9.0X5s5FUHS79Ks69xBR9ytqlpzccEMXl-qyNfqbJHibY";
        final String token =tokens;
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
