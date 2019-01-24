package com.example.expertiselocator.main;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.expertiselocator.R;
import com.example.expertiselocator.adapter.FollowingListAdapter;
import com.example.expertiselocator.apiclient.ExpertiseApiClient;
import com.example.expertiselocator.interfaces.ExpertiseApiInterface;
import com.example.expertiselocator.interfaces.OnItemClick;
import com.example.expertiselocator.model.request.FollowUnFollowRequest;
import com.example.expertiselocator.model.request.FollowingListRequest;
import com.example.expertiselocator.model.response.FollowingResponse;
import com.example.expertiselocator.utils.CommonMethods;
import com.example.expertiselocator.utils.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFollowingListActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,
        PopupMenu.OnMenuItemClickListener, OnItemClick {

    RecyclerView followingRecyclerView;
    private List<FollowingResponse> followingList = new ArrayList<>();
    private FollowingListAdapter followingListAdapter;
    CommonMethods commonMethods;
    public static final String TAG = UserFollowingListActivity.class.getSimpleName();
    TextView txtNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_following_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        commonMethods = new CommonMethods(UserFollowingListActivity.this);

        followingListAdapter = new FollowingListAdapter(UserFollowingListActivity.this, followingList);
        followingRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_following);
        txtNoData = (TextView) findViewById(R.id.txt_nodata_following);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        followingRecyclerView.setLayoutManager(mLayoutManager);
        if (followingRecyclerView != null) {
            followingRecyclerView.setAdapter(followingListAdapter);
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
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(UserFollowingListActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<List<FollowingResponse>> getPostedMessage = apiInterface.getFollowingUser(followingListRequest);
        getPostedMessage.enqueue(new Callback<List<FollowingResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<FollowingResponse>> call, @NonNull Response<List<FollowingResponse>> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());

                try {
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            //followingList =response.body();
                            commonMethods.showLog("Response code : ", TAG + response.body().size());
                            //followingList.add((FollowingResponse) response.body());
                            //recyclerView.setAdapter(followingListAdapter);
                            followingList.addAll(response.body());
                            followingListAdapter.notifyDataSetChanged();
                        } else {
                            followingRecyclerView.setVisibility(View.GONE);
                            txtNoData.setVisibility(View.VISIBLE);
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
            public void onFailure(Call<List<FollowingResponse>> call, Throwable t) {
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

                PopupMenu popupFollowingMenu = new PopupMenu(UserFollowingListActivity.this, view);
                popupFollowingMenu.setOnMenuItemClickListener(this);
                popupFollowingMenu.inflate(R.menu.following_popup_menu);
                popupFollowingMenu.show();
                break;

            case R.id.btn_unfollow_following:

                commonMethods.showLog(TAG, "btn_unfollow_following" + position + " ID :" + String.valueOf(followingList.get(position).getFollowingsUserID()));
                FollowUnFollowRequest unFollowRequest = new FollowUnFollowRequest();
                unFollowRequest.setUserId(commonMethods.getUserId());
                unFollowRequest.setFollowingsUserID(String.valueOf(followingList.get(position).getFollowingsUserID()));
                unFollowRequest.setCreatedBy(commonMethods.getUserId());
                unFollowRequest.setModifiedBy(commonMethods.getUserId());
                unFollowRequest.setFlag("UnFollow");
                callUnFollowUser(unFollowRequest, position);
                break;
            default:
                break;
        }
    }

    public void callUnFollowUser(FollowUnFollowRequest followingListRequest, int position) {
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(UserFollowingListActivity.this);
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
                        //followingList =response.body();
                        commonMethods.showLog("Response code : ", TAG + response.body() + " position : " + position);
                        //followingList.add((FollowingResponse) response.body());
                        //recyclerView.setAdapter(followingListAdapter);
                        followingListAdapter.refreshAdapter(position);
                    } else {
                        commonMethods.showLog("Response code : ", TAG + response.code());
                        commonMethods.showToast(getResources().getString(R.string.fail_addcomment_timeline));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());

            }
        });
    }

}