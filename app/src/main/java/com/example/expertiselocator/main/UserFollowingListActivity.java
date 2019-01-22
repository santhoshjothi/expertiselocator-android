package com.example.expertiselocator.main;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.PopupMenu;

import com.example.expertiselocator.R;
import com.example.expertiselocator.adapter.FollowingListAdapter;
import com.example.expertiselocator.apiclient.ExpertiseApiClient;
import com.example.expertiselocator.interfaces.ExpertiseApiInterface;
import com.example.expertiselocator.interfaces.OnItemClick;
import com.example.expertiselocator.model.request.FollowingListRequest;
import com.example.expertiselocator.model.response.FollowingListResponse;
import com.example.expertiselocator.utils.CommonMethods;
import com.example.expertiselocator.utils.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFollowingListActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener,
        PopupMenu.OnMenuItemClickListener, OnItemClick {

    GridView follwingListGrid;
    private List<FollowingListResponse> followingList = new ArrayList<>();
    private FollowingListAdapter followingListAdapter;
    CommonMethods commonMethods;
    public static final String TAG = UserFollowingListActivity.class.getSimpleName();

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
        follwingListGrid = (GridView) findViewById(R.id.gridView_follewinglist);
        if (follwingListGrid != null) {
            follwingListGrid.setAdapter(followingListAdapter);
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
        Call<List<FollowingListResponse>> getPostedMessage = apiInterface.getFollowingListUser(followingListRequest);
        getPostedMessage.enqueue(new Callback<List<FollowingListResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<FollowingListResponse>> call, @NonNull Response<List<FollowingListResponse>> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());

                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        //followingList =response.body();
                        commonMethods.showLog("Response code : ", TAG + response.body().size());
                        //followingList.add((FollowingListResponse) response.body());
                        //recyclerView.setAdapter(followingListAdapter);
                        followingList.addAll(response.body());
                        followingListAdapter.notifyDataSetChanged();
                    } else {
                        commonMethods.showLog("Response code : ", TAG + response.code());
                        commonMethods.showToast(getResources().getString(R.string.fail_addcomment_timeline));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<FollowingListResponse>> call, Throwable t) {
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
        switch (itemType){

            case R.id.follwinglist_profile_menu:

                PopupMenu popupFollowingMenu= new PopupMenu(UserFollowingListActivity.this, view);
                popupFollowingMenu.setOnMenuItemClickListener(this);
                popupFollowingMenu.inflate(R.menu.following_popup_menu);
                popupFollowingMenu.show();
                break;
            default:
                break;
        }


    }


}