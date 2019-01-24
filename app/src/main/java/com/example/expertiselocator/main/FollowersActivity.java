package com.example.expertiselocator.main;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.expertiselocator.R;
import com.example.expertiselocator.adapter.FollowerAdapater;
import com.example.expertiselocator.apiclient.ExpertiseApiClient;
import com.example.expertiselocator.interfaces.ExpertiseApiInterface;
import com.example.expertiselocator.interfaces.OnItemClick;
import com.example.expertiselocator.model.request.FollowUnFollowRequest;
import com.example.expertiselocator.model.request.FollowingListRequest;
import com.example.expertiselocator.model.response.FollowersReponse;
import com.example.expertiselocator.utils.CommonMethods;
import com.example.expertiselocator.utils.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowersActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,
        PopupMenu.OnMenuItemClickListener, OnItemClick {

    RecyclerView followerRecyclerView;
    private List<FollowersReponse> followingList = new ArrayList<>();
    private FollowerAdapater followerAdapater;
    CommonMethods commonMethods;
    public static final String TAG = FollowersActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        commonMethods = new CommonMethods(FollowersActivity.this);

        followerAdapater = new FollowerAdapater(FollowersActivity.this, followingList);
        followerRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_follower);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        ;
        followerRecyclerView.setLayoutManager(mLayoutManager);
        if (followerRecyclerView != null) {
            followerRecyclerView.setAdapter(followerAdapater);
        }

        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            FollowingListRequest followingListRequest = new FollowingListRequest();
            followingListRequest.setUserId(commonMethods.getUserId());
            followingListRequest.setExpertUserId(commonMethods.getUserId());
            followingListRequest.setStartIndex("1");
            followingListRequest.setMaxCount("10");
            followingListRequest.setFlag("UserProfile");
            callFollowingList(followingListRequest);
        } else {
            commonMethods.showToast(getResources().getString(R.string.no_network));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
    }

    public void callFollowingList(FollowingListRequest followingListRequest) {
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(FollowersActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<List<FollowersReponse>> getPostedMessage = apiInterface.getFollowerUser(followingListRequest);
        getPostedMessage.enqueue(new Callback<List<FollowersReponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<FollowersReponse>> call, @NonNull Response<List<FollowersReponse>> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());

                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        commonMethods.showLog("Response code : ", TAG + response.body().size());
                        followingList.addAll(response.body());
                        followerAdapater.notifyDataSetChanged();
                    } else {
                        commonMethods.showLog("Response code : ", TAG + response.code());
                        commonMethods.showToast(getResources().getString(R.string.fail_addcomment_timeline));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FollowersReponse>> call, @NonNull Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());

            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        int timelineMenuId = menuItem.getItemId();
        switch (timelineMenuId) {
            case R.id.follwinglist_MenuRequest:


                break;

            case R.id.follwinglist_MenuMessage:
                break;
        }
        return false;
    }

    @Override
    public void onItemClick(View view, int position) {
        commonMethods.showLog(TAG, "onItem" + position);
        int itemType = view.getId();
        switch (itemType) {
            case R.id.follwinglist_profile_menu:
                PopupMenu popupFollowingMenu = new PopupMenu(FollowersActivity.this, view);
                popupFollowingMenu.setOnMenuItemClickListener(this);
                popupFollowingMenu.inflate(R.menu.following_popup_menu);
                popupFollowingMenu.show();
                break;
            case R.id.btn_unfollow_following:

                commonMethods.showLog(TAG, "btn_unfollow_following" + position);
                FollowUnFollowRequest unFollowRequest = new FollowUnFollowRequest();
                unFollowRequest.setUserId(commonMethods.getUserId());
                unFollowRequest.setCreatedBy(commonMethods.getUserId());
                unFollowRequest.setModifiedBy(commonMethods.getUserId());
                unFollowRequest.setFollowingsUserID(String.valueOf(followingList.get(position).getUserId()));
                if (followingList.get(position).getIsFollowed().equalsIgnoreCase("TRUE")) {
                    unFollowRequest.setFlag("UnFollow");
                    callUnFollowUser(unFollowRequest, position);
                } else if (followingList.get(position).getIsFollowed().equalsIgnoreCase("UNFOLLOW")) {
                    unFollowRequest.setFlag("Follow");
                    callFollowUser(unFollowRequest, position);
                }
                break;
            default:
                break;
        }


    }

    public void callUnFollowUser(FollowUnFollowRequest followingListRequest, int position) {
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(FollowersActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<String> getPostedMessage = apiInterface.getUnFollowUser(followingListRequest);
        getPostedMessage.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());

                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        //followingList =response.body();
                        if (response.body().equalsIgnoreCase("success")) {
                            commonMethods.showLog("Response code : ", TAG + response.body() + " position : " + position);
                            FollowingListRequest followingListRequest = new FollowingListRequest();
                            followingListRequest.setUserId(commonMethods.getUserId());
                            followingListRequest.setExpertUserId(commonMethods.getUserId());
                            followingListRequest.setStartIndex("1");
                            followingListRequest.setMaxCount("10");
                            followingListRequest.setFlag("UserProfile");
                            callgetFollowingList(followingListRequest, position);
                        } else {

                        }
                    } else {
                        commonMethods.showLog("Response code : ", TAG + response.code());
                        commonMethods.showToast(getResources().getString(R.string.fail_addcomment_timeline));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());

            }
        });
    }

    public void callFollowUser(FollowUnFollowRequest followingListRequest, int position) {
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(FollowersActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<String> getPostedMessage = apiInterface.getFollowUser(followingListRequest);
        getPostedMessage.enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());

                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        if (response.body().equalsIgnoreCase("success")) {
                            commonMethods.showLog("Response code : ", TAG + response.body() + " position : " + position);
                            FollowingListRequest followingListRequest = new FollowingListRequest();
                            followingListRequest.setUserId(commonMethods.getUserId());
                            followingListRequest.setExpertUserId(commonMethods.getUserId());
                            followingListRequest.setStartIndex("1");
                            followingListRequest.setMaxCount("10");
                            followingListRequest.setFlag("UserProfile");
                            callgetFollowingList(followingListRequest, position);
                        } else {

                        }
                    } else {
                        commonMethods.showLog("Response code : ", TAG + response.code());
                        commonMethods.showToast(getResources().getString(R.string.fail_addcomment_timeline));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());

            }
        });
    }

    public void callgetFollowingList(FollowingListRequest followingListRequest, int poistion) {
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(FollowersActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<List<FollowersReponse>> getPostedMessage = apiInterface.getFollowerUser(followingListRequest);
        getPostedMessage.enqueue(new Callback<List<FollowersReponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<FollowersReponse>> call, @NonNull Response<List<FollowersReponse>> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());

                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        commonMethods.showLog("Response code : ", TAG + response.body().size());
                        followerAdapater.refreshFollowerAdapter(response.body(), poistion);
                    } else {
                        commonMethods.showLog("Response code : ", TAG + response.code());
                        commonMethods.showToast(getResources().getString(R.string.fail_addcomment_timeline));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<FollowersReponse>> call, @NonNull Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());

            }
        });
    }
}