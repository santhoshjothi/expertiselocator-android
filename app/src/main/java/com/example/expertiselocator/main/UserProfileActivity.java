package com.example.expertiselocator.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.expertiselocator.R;
import com.example.expertiselocator.model.UserInfoModelPref;
import com.example.expertiselocator.utils.SharedPreferencesWithAES;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class UserProfileActivity extends AppCompatActivity {

    TextView txt_displayname_profile,tx_designation_user,tx_location_user,tx_email_user,tx_landline_user,tx_mobile_user,
            txt_abt_user;
    ImageView img_profile_user;
    SharedPreferencesWithAES prefs;
    UserInfoModelPref userResponse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefs = SharedPreferencesWithAES.getInstance(UserProfileActivity.this, "expertise_Prefs");

        txt_displayname_profile=(TextView)findViewById(R.id.txt_displayname_user);
        tx_designation_user=(TextView)findViewById(R.id.tx_designation_user);
        tx_location_user=(TextView)findViewById(R.id.tx_location_user);
        tx_email_user=(TextView)findViewById(R.id.tx_email_user);
        tx_landline_user=(TextView)findViewById(R.id.tx_landline_user);
        tx_mobile_user=(TextView)findViewById(R.id.tx_mobile_user);
        img_profile_user=(ImageView) findViewById(R.id.img_profile_user);
        txt_abt_user=(TextView) findViewById(R.id.txt_abt_user);



        try {
            String getUserInfo = prefs.getString("user_info", "");
            ObjectMapper mapper = new ObjectMapper();
            userResponse = mapper.readValue(getUserInfo, UserInfoModelPref.class);
            // Log.v("Timeline_Fragment",""+loginResponse.getToken());
            Log.v("UserProfileActivity", "" + userResponse.getUserID());
            byte[] imageByte = Base64.decode(userResponse.getProfilePicture(), Base64.DEFAULT);
            Glide.with(getApplicationContext()).asBitmap().load(imageByte).into(img_profile_user);
            txt_displayname_profile.setText(userResponse.getDisplayName());
            tx_designation_user.setText(userResponse.getDesignation() + "at" + userResponse.getDepartment());

            String abtMe=getIntent().getStringExtra("abtMe");
            txt_abt_user.setText(abtMe);




        } catch (IOException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        }
}
