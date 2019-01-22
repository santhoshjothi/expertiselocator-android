package com.example.expertiselocator.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.expertiselocator.R;
import com.example.expertiselocator.adapter.MenuAdapter;
import com.example.expertiselocator.adapter.TimelineAdapter;
import com.example.expertiselocator.apiclient.ExpertiseApiClient;
import com.example.expertiselocator.interfaces.ExpertiseApiInterface;
import com.example.expertiselocator.interfaces.MenuItemClick;
import com.example.expertiselocator.model.MenuModel;
import com.example.expertiselocator.model.UserInfoModelPref;
import com.example.expertiselocator.model.request.GetUserProfileRequest;
import com.example.expertiselocator.model.response.GetProfileInfoAboutResponse;
import com.example.expertiselocator.utils.CommonMethods;
import com.example.expertiselocator.utils.SharedPreferencesWithAES;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abdularis.civ.CircleImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreenActivity extends AppCompatActivity implements View.OnClickListener, MenuItemClick {

    CommonMethods commonMethods;

    int[] menuIcon;
    String[] menuTitle, menuBadgeCount;
    boolean[] isBadgeAvailable;

    MenuModel menuModel;
    ArrayList<MenuModel> menuModels = new ArrayList<>();
    int maxGridCount = 3;
    MenuAdapter menuAdapter;
    RecyclerView rvMenu;
    GridLayoutManager gridLayoutManager;
    SharedPreferencesWithAES prefs;
    UserInfoModelPref userResponse;
    TextView txtDesignation,txtDisplayName;
    CircleImageView imgProfilePicture;

    public static final String TAG = HomeScreenActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        commonMethods = new CommonMethods(this);
        prefs = SharedPreferencesWithAES.getInstance(HomeScreenActivity.this, commonMethods.expertisePreference);


        txtDisplayName=(TextView)findViewById(R.id.txt_displayname_home);
        txtDesignation=(TextView)findViewById(R.id.tx_designation_home);
        imgProfilePicture=(CircleImageView) findViewById(R.id.img_profile_home);



        try {
            String getUserInfo = prefs.getString("user_info", "");
            ObjectMapper mapper = new ObjectMapper();
            userResponse = mapper.readValue(getUserInfo, UserInfoModelPref.class);
            // Log.v("Timeline_Fragment",""+loginResponse.getToken());
            Log.v("UserProfileActivity", "" + userResponse.getUserID());
            byte[] imageByte = Base64.decode(userResponse.getProfilePicture(), Base64.DEFAULT);
            Glide.with(getApplicationContext()).asBitmap().load(imageByte).into(imgProfilePicture);
            txtDisplayName.setText(userResponse.getDisplayName());
            txtDesignation.setText(userResponse.getDesignation() + "at" + userResponse.getDepartment());


        } catch (IOException e) {
            e.printStackTrace();
        }catch (NullPointerException e){
            e.printStackTrace();
        }



        menuIcon = new int[]{R.drawable.ic_menu_timeline,
                R.drawable.ic_menu_messaging,
                R.drawable.ic_menu_thanks,
                R.drawable.ic_menu_solutions,
                R.drawable.ic_menu_collaborations,
                R.drawable.ic_menu_awards,
                R.drawable.ic_menu_followers,
                R.drawable.ic_menu_following,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png,
                R.drawable.ic_send_black_png};

        menuTitle = new String[]{"Timeline",
                "Messaging",
                "Thanks",
                "Solutions",
                "Collaborations",
                "Awards",
                "Followers",
                "Following",
                "Events",
                "KSPT",
                "My Group",
                "My Communities",
                "Recommended Profiles",
                "Recommended Groups"};

        menuBadgeCount = new String[]{"0", "4", "2", "0", "3", "0", "99+",
                "6", "0", "4", "0", "0", "0", "0"};

        isBadgeAvailable = new boolean[]{false, true, true, true, true, true, true,
                true, true, true, false, false, false, false, false};

        for (int menu = 0; menu < menuTitle.length; menu++) {
            menuModel = new MenuModel();
            menuModel.setMenuIcon(menuIcon[menu]);
            menuModel.setMenuTitle(menuTitle[menu]);
            menuModel.setMenuBadgeCount(menuBadgeCount[menu]);
            menuModel.setBadgeAvailable(isBadgeAvailable[menu]);
            menuModels.add(menuModel);
        }

        menuAdapter = new MenuAdapter(HomeScreenActivity.this, menuModels);
        rvMenu = (RecyclerView) findViewById(R.id.rvMenu);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), maxGridCount);
        rvMenu.setLayoutManager(gridLayoutManager);
        rvMenu.setAdapter(menuAdapter);


        imgProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                GetUserProfileRequest profileRequest = new GetUserProfileRequest();
                profileRequest.setUserID(commonMethods.getUserId());
                profileRequest.setLanguage(getResources().getString(R.string.language));
                callUserAbt(profileRequest);
            }
        });

    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            // Timeline
            case 0:
                startActivity(new Intent(HomeScreenActivity.this, TimelineActivity.class));
                break;
            case 6:
                startActivity(new Intent(HomeScreenActivity.this, FollowersActivity.class));
                break;
            case 7:
                startActivity(new Intent(HomeScreenActivity.this, UserFollowingListActivity.class));
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void callUserAbt(GetUserProfileRequest userInfo) {
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(HomeScreenActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<List<GetProfileInfoAboutResponse>> getUserProfileAbout = apiInterface.getProfileInfoAbout(userInfo);

        getUserProfileAbout.enqueue(new Callback<List<GetProfileInfoAboutResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetProfileInfoAboutResponse>> call, @NonNull Response<List<GetProfileInfoAboutResponse>> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());

                String abtMe = null;
                try {
                    if (response.code() == 200) {
                        List<GetProfileInfoAboutResponse> getReponse = response.body();
                        for (int i = 0; i < getReponse.size(); i++) {
                            abtMe = getReponse.get(i).getAboutMe();
                        }
                        Intent userProfileIntent = new Intent(HomeScreenActivity.this, UserProfileActivity.class);
                        userProfileIntent.putExtra("abtMe", abtMe);
                        startActivity(userProfileIntent);
                    } else {
                        commonMethods.showLog("Response code : ", TAG + response.code());
                        commonMethods.showToast(getResources().getString(R.string.fail_abtme_userprofile));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<GetProfileInfoAboutResponse>> call, Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());
                commonMethods.showToast(getResources().getString(R.string.fail_abtme_userprofile));
            }
        });
    }
}
