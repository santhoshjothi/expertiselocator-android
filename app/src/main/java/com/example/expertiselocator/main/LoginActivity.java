package com.example.expertiselocator.main;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.expertiselocator.R;
import com.example.expertiselocator.adapter.TimelineAdapter;
import com.example.expertiselocator.apiclient.ExpertiseApiClient;
import com.example.expertiselocator.interfaces.ExpertiseApiInterface;
import com.example.expertiselocator.interfaces.MenuItemClick;
import com.example.expertiselocator.model.UserInfoModelPref;
import com.example.expertiselocator.model.request.LoginRequest;
import com.example.expertiselocator.model.request.UserInfoRequest;
import com.example.expertiselocator.model.response.GetPostedMessagesResponse;
import com.example.expertiselocator.model.response.GetUserInfoResponse;
import com.example.expertiselocator.model.response.LoginResponse;
import com.example.expertiselocator.utils.AESEncryption;
import com.example.expertiselocator.utils.CommonMethods;
import com.example.expertiselocator.utils.ConnectivityReceiver;
import com.example.expertiselocator.utils.SharedPreferencesWithAES;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.rey.material.widget.ProgressView;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.expertiselocator.utils.AESEncryption.decrypt;

public class LoginActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener{

    EditText edt_username, edt_passd;
    Button btn_submit;
    CommonMethods commonMethods;
    public static final String TAG = LoginActivity.class.getSimpleName();
    String userNameEncryt;
    ProgressView progress_login;
    SharedPreferencesWithAES prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Objects.requireNonNull(getSupportActionBar()).hide();

        commonMethods = new CommonMethods(LoginActivity.this);
        edt_username = (EditText) findViewById(R.id.edt_username_login);
        edt_passd = (EditText) findViewById(R.id.edt_pssd_login);
        btn_submit = (Button) findViewById(R.id.btn_submit_login);
        progress_login = (ProgressView) findViewById(R.id.progress_login);
         prefs = SharedPreferencesWithAES.getInstance(LoginActivity.this,"expertise_Prefs");



        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    try {
                        InputMethodManager inputManager = (InputMethodManager) LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        userNameEncryt = AESEncryption.encrypt( edt_username.getText().toString(),"8080808080808080");
                        String PasswordEncrpt = AESEncryption.encrypt( edt_passd.getText().toString(),"8080808080808080");
                        LoginRequest request = new LoginRequest();
                        request.setUserName(userNameEncryt);
                        request.setPassword(PasswordEncrpt);
                        callGetLogin(request);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    }

                }else{
                    commonMethods.showToast(getResources().getString(R.string.no_network));
                }

            }
        });
    }


    public void callGetLogin(LoginRequest loginRequest){
        progress_login.setVisibility(View.VISIBLE);
        ExpertiseApiClient expertiseApiClient=new ExpertiseApiClient(LoginActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<LoginResponse> getPostedMessage = apiInterface.getLogin(loginRequest);
        getPostedMessage.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {

                commonMethods.showLog("URL Success : " , TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG +  response.code());
                commonMethods.showLog("Response Body : " ,TAG + response.body());
                LoginResponse loginResponse = response.body();


                try {
                    if(loginResponse.getToken()!=null){

//                        commonMethods.showLog("Token : " ,TAG + loginResponse.getToken());
//                        commonMethods.showLog("userNameEncryt : " ,TAG + userNameEncryt);

                        prefs.putString("loginresponse", loginResponse.getToken());
                        prefs.commit();

                        UserInfoRequest userInfoRequest = new UserInfoRequest();
                        userInfoRequest.setUsername(userNameEncryt);
                        userInfoRequest.setLanguage(getResources().getString(R.string.language));
                        callGetUserInfo(userInfoRequest,loginResponse.getToken());
                        Log.v("UserInfoRequest",""+userInfoRequest.getUsername());
                        Log.v("UserInfoRequest",""+userInfoRequest.getLanguage());


                    }else{

                        progress_login.setVisibility(View.GONE);
                        commonMethods.showLog("Null Token  : " , TAG + "");
                        commonMethods.showToast("Login Failure");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    progress_login.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                commonMethods.showLog("URL Failure : " ,TAG + call.request().url());
                commonMethods.showLog("Failure : ",TAG  + t.getMessage());
                progress_login.setVisibility(View.GONE);
                commonMethods.showToast("Login Failure");
            }
        });

    }


    public void callGetUserInfo(UserInfoRequest userInfo,String token){

        ExpertiseApiClient expertiseApiClient=new ExpertiseApiClient(LoginActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<List<GetUserInfoResponse>> getPostedMessage = apiInterface.getUserInfo(userInfo);
        getPostedMessage.enqueue(new Callback<List<GetUserInfoResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetUserInfoResponse>> call, @NonNull Response<List<GetUserInfoResponse>> response) {

               String userId=null;
                String UserNameInfo =null;
                String firtNameInfo=null;
                String lastNameInfo=null;
                String userProfileImage = null;
                commonMethods.showLog("URL Success : " , TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG +  response.code());
                commonMethods.showLog("Response Body : " ,TAG + response.body());
                try {
                List<GetUserInfoResponse> loginResponse = response.body();

                    assert loginResponse != null;
                    for(int i=0; i<loginResponse.size();i++){
                        userId=String.valueOf(loginResponse.get(i).getId());
                        UserNameInfo = loginResponse.get(i).getUserName();
                        firtNameInfo = loginResponse.get(i).getFirstName();
                        lastNameInfo = loginResponse.get(i).getLastName();
                        String profilePicture = loginResponse.get(i).getProfilePicture();
                        userProfileImage = profilePicture.replace("data:image/png;base64,", "");
                        commonMethods.showLog("userId: " ,TAG + userId);
                        }

                    String userNameDecrpt = decrypt( UserNameInfo,"8080808080808080");
                    String firtNamDecrpt = decrypt( firtNameInfo,"8080808080808080");
                    String lastNameDecrpt = decrypt( lastNameInfo,"8080808080808080");

                    Gson gson = new Gson();
                    UserInfoModelPref userDetailPreferences =new UserInfoModelPref();
                    userDetailPreferences.setUserId(userId);
                    userDetailPreferences.setUserName(userNameDecrpt);
                    userDetailPreferences.setFirstName(firtNamDecrpt);
                    userDetailPreferences.setLastName(lastNameDecrpt);
                    userDetailPreferences.setProfilepicture(userProfileImage);
                    // Get java object list json format string.
                    String userInfoListJsonString = gson.toJson(userDetailPreferences);
                    //SharedPreferencesWithAES prefs = SharedPreferencesWithAES.getInstance(LoginActivity.this,"expertise_Prefs"); //provide context & preferences name.
                    //Storing the username inside shared preferences
                    prefs.putString("user_info", userInfoListJsonString);
                    prefs.commit();
                    progress_login.setVisibility(View.GONE);

                    Intent postInten=new Intent(LoginActivity.this,HomeScreenActivity.class);
                    startActivity(postInten);

                } catch (Exception e) {
                    e.printStackTrace();
                    progress_login.setVisibility(View.GONE);
                    commonMethods.showToast("GetInfo Failure");

                }


            }

            @Override
            public void onFailure(Call<List<GetUserInfoResponse>> call, Throwable t) {
                commonMethods.showLog("URL Failure : " ,TAG + call.request().url());
                commonMethods.showLog("Failure : ",TAG  + t.getMessage());
                progress_login.setVisibility(View.GONE);
                commonMethods.showToast("GetInfo Failure");
            }
        });




    }
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }
}
