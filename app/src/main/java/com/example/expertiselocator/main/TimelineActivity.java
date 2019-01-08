package com.example.expertiselocator.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.expertiselocator.R;
import com.example.expertiselocator.adapter.TimelineAdapter;
import com.example.expertiselocator.apiclient.ExpertiseApiClient;
import com.example.expertiselocator.interfaces.ExpertiseApiInterface;
import com.example.expertiselocator.interfaces.OnItemClick;
import com.example.expertiselocator.model.UserInfoModelPref;
import com.example.expertiselocator.model.request.AddPostRequest;
import com.example.expertiselocator.model.request.GetPostedMessageRequest;
import com.example.expertiselocator.model.request.GetUserProfileRequest;
import com.example.expertiselocator.model.response.GetPostedMessagesResponse;
import com.example.expertiselocator.model.response.GetProfileInfoAboutResponse;
import com.example.expertiselocator.model.response.GetUserInfoResponse;
import com.example.expertiselocator.utils.CommonMethods;
import com.example.expertiselocator.utils.SharedPreferencesWithAES;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimelineActivity extends AppCompatActivity implements OnItemClick,
        PopupMenu.OnMenuItemClickListener, OnItemClick.OnItemClickComment {

    CommonMethods commonMethods;
    ImageView img_search_toolbar;
    RecyclerView rvTimelinePost;
    ShimmerFrameLayout shimmerViewContainerTimeline;
    LinearLayoutManager layoutManager;
    public static final String TAG = TimelineActivity.class.getSimpleName();
    List<GetPostedMessagesResponse> getPostedMessagesResponses;
    TimelineAdapter timelineAdapter;
    private int menuItemClickedPosition = 0, menuItemClickedCommentPosition = 0, menuItemClickedReplyPosition = 0;
    LinearLayout lin_post_timeline;
    SharedPreferencesWithAES prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        commonMethods = new CommonMethods(TimelineActivity.this);
        prefs = SharedPreferencesWithAES.getInstance(TimelineActivity.this, commonMethods.expertisePreference); //provide context & preferences name.
        shimmerViewContainerTimeline = (ShimmerFrameLayout) findViewById(R.id.shimmerViewContainerTimeline);
        rvTimelinePost = (RecyclerView) findViewById(R.id.rvTimelinePost);
        lin_post_timeline = (LinearLayout) findViewById(R.id.lin_post_timeline);
        img_search_toolbar = (ImageView)toolbar.findViewById(R.id.img_search_toolbar);

        layoutManager = new LinearLayoutManager(TimelineActivity.this, LinearLayoutManager.VERTICAL, false);
        rvTimelinePost.setLayoutManager(layoutManager);
        prefs = SharedPreferencesWithAES.getInstance(TimelineActivity.this,"expertise_Prefs");
        getPostedTimelineMsg();

        lin_post_timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent postActitity =new Intent(TimelineActivity.this,PostActivity.class);
                startActivity(postActitity);
            }
        });
        img_search_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent postActitity =new Intent(TimelineActivity.this,SearchActivty.class);
                startActivity(postActitity);
            }
        });

    }

    @Override
    protected void onPause() {
        shimmerViewContainerTimeline.stopShimmer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shimmerViewContainerTimeline.startShimmer();
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        getPostedTimelineMsg();
    }

    public void getPostedTimelineMsg() {

        GetPostedMessageRequest getPostedMessageRequest = new GetPostedMessageRequest();

        try {
            String getUserInfo = prefs.getString("user_info", "");
            ObjectMapper mapper = new ObjectMapper();
            UserInfoModelPref userResponse = mapper.readValue(getUserInfo, UserInfoModelPref.class);
            getPostedMessageRequest.setUserID(userResponse.getUserID());
            getPostedMessageRequest.setStartIndex("1");
            getPostedMessageRequest.setMaxCount("2");
            getPostedMessageRequest.setPostID("");
        } catch (IOException e) {
            e.printStackTrace();
        }


        ExpertiseApiClient expertiseApiClient=new ExpertiseApiClient(TimelineActivity.this);
        ExpertiseApiInterface apiInterface = expertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<List<GetPostedMessagesResponse>> getPostedMessage = apiInterface.getPostedMessage(getPostedMessageRequest);
        getPostedMessage.enqueue(new Callback<List<GetPostedMessagesResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetPostedMessagesResponse>> call, @NonNull Response<List<GetPostedMessagesResponse>> response) {

                commonMethods.showLog("URL Success : ", TAG+ call.request().url());
                commonMethods.showLog("Response Code : ",TAG + response.code());
                commonMethods.showLog("Response Body : " ,TAG+ response.body());
                List<GetPostedMessagesResponse> getPostedMessagesResponseResult = response.body();
                assert getPostedMessagesResponseResult != null;
                for (GetPostedMessagesResponse messages : getPostedMessagesResponseResult) {
                    commonMethods.showLog("Message : " ,TAG+ messages.getMessage());
                    commonMethods.showLog("Username : " ,TAG+ messages.getUserName());
                    commonMethods.showLog("Shared Post Id : " ,TAG+ messages.getSharedPostId());
                    commonMethods.showLog("Post Image : " ,TAG+ messages.getPostImage());
                }
                getPostedMessagesResponses = response.body();
                timelineAdapter = new TimelineAdapter(TimelineActivity.this, getPostedMessagesResponses);
                rvTimelinePost.setAdapter(timelineAdapter);
                shimmerViewContainerTimeline.stopShimmer();
                shimmerViewContainerTimeline.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<GetPostedMessagesResponse>> call, Throwable t) {
                commonMethods.showLog("URL Failure : " ,TAG+ call.request().url());
                commonMethods.showLog("Failure : " ,TAG+ t.getMessage());
                shimmerViewContainerTimeline.stopShimmer();
                shimmerViewContainerTimeline.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        int viewType = view.getId();
        switch (viewType) {
            case R.id.imgTimelineProfileMenu:
                menuItemClickedPosition = position;
                PopupMenu popupMenuTimeline = new PopupMenu(TimelineActivity.this, view);
                popupMenuTimeline.setOnMenuItemClickListener(this);
                popupMenuTimeline.inflate(R.menu.timeline_popup_menu);
                popupMenuTimeline.show();
                break;

            case R.id.imgTimelineCommentProfileMenu:
                menuItemClickedCommentPosition = position;
                PopupMenu popupCommentMenuTimeline = new PopupMenu(TimelineActivity.this, view);
                popupCommentMenuTimeline.setOnMenuItemClickListener(this);
                popupCommentMenuTimeline.inflate(R.menu.timeline_popup_comment_menu);
                popupCommentMenuTimeline.show();
                break;

            case R.id.imgTimelineReplyProfileMenu:
                menuItemClickedReplyPosition = position;
                PopupMenu popupReplyMenuTimeline = new PopupMenu(TimelineActivity.this, view);
                popupReplyMenuTimeline.setOnMenuItemClickListener(this);
                popupReplyMenuTimeline.inflate(R.menu.timeline_popup_reply_menu);
                popupReplyMenuTimeline.show();
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int timelineMenuId = item.getItemId();
        switch (timelineMenuId) {
            case R.id.timelineMenuEdit:
                commonMethods.showToast("Edit Menu " + menuItemClickedPosition);
                break;

            case R.id.timelineMenuDelete:
                commonMethods.showToast("Delete Menu " + menuItemClickedPosition);
                break;

            case R.id.timelineCommentMenuEdit:
                showEditDeleteCommentDialog("Edit", "Comment");
                break;

            case R.id.timelineCommentMenuDelete:
                showEditDeleteCommentDialog("Delete", "Comment");
                break;

            case R.id.timelineReplyMenuEdit:
                showEditDeleteCommentDialog("Edit", "Reply");
                break;

            case R.id.timelineReplyMenuDelete:
                showEditDeleteCommentDialog("Delete", "Reply");
                break;

            default:
                break;
        }
        return false;
    }

    public void showEditDeleteCommentDialog(String menuType, String menuOption) {
        AlertDialog.Builder alertEditDeleteComment = new AlertDialog.Builder(TimelineActivity.this);
        View viewEditDeleteComment = LayoutInflater.from(TimelineActivity.this).inflate(R.layout.alert_edit_delete_comment_reply, null);
        alertEditDeleteComment.setView(viewEditDeleteComment);
        alertEditDeleteComment.setCancelable(false);

        AlertDialog dialogEditDeleteComment = alertEditDeleteComment.create();
        TextView tvEditDeleteTitle, tvEditDeleteMessage, tvEditDeleteQuery, tvEditDeleteSubmit, tvEditDeleteCancel;
        EditText etEditDeleteMessageContent;

        tvEditDeleteTitle = (TextView) viewEditDeleteComment.findViewById(R.id.tvEditDeleteTitle);
        tvEditDeleteMessage = (TextView) viewEditDeleteComment.findViewById(R.id.tvEditDeleteMessage);
        tvEditDeleteQuery = (TextView) viewEditDeleteComment.findViewById(R.id.tvEditDeleteQuery);
        tvEditDeleteSubmit = (TextView) viewEditDeleteComment.findViewById(R.id.tvEditDeleteSubmit);
        tvEditDeleteCancel = (TextView) viewEditDeleteComment.findViewById(R.id.tvEditDeleteCancel);
        etEditDeleteMessageContent = (EditText) viewEditDeleteComment.findViewById(R.id.etEditDeleteMessageContent);

        if (menuType.equals("Edit")) {
            if (menuOption.equals("Comment")) {
                tvEditDeleteTitle.setText(getResources().getString(R.string.timeline_edit_comment_title));
                tvEditDeleteMessage.setText(getResources().getString(R.string.timeline_edit_comment_message));
                tvEditDeleteQuery.setVisibility(View.GONE);
            }

            if (menuOption.equals("Reply")) {
                tvEditDeleteTitle.setText(getResources().getString(R.string.timeline_edit_reply_title));
                tvEditDeleteMessage.setText(getResources().getString(R.string.timeline_edit_reply_message));
                tvEditDeleteQuery.setVisibility(View.GONE);
            }

            tvEditDeleteSubmit.setText(getResources().getString(R.string.timeline_edit_delete_update));
        }

        if (menuType.equals("Delete")) {
            if (menuOption.equals("Comment")) {
                tvEditDeleteTitle.setText(getResources().getString(R.string.timeline_delete_comment_title));
                tvEditDeleteMessage.setText(getResources().getString(R.string.timeline_delete_comment_message));
                tvEditDeleteQuery.setVisibility(View.VISIBLE);
                tvEditDeleteQuery.setText(getResources().getString(R.string.timeline_delete_comment_query));
                etEditDeleteMessageContent.setEnabled(false);
            }

            if (menuOption.equals("Reply")) {
                tvEditDeleteTitle.setText(getResources().getString(R.string.timeline_delete_reply_title));
                tvEditDeleteMessage.setText(getResources().getString(R.string.timeline_delete_reply_message));
                tvEditDeleteQuery.setVisibility(View.VISIBLE);
                tvEditDeleteQuery.setText(getResources().getString(R.string.timeline_delete_reply_query));
                etEditDeleteMessageContent.setEnabled(false);
            }

            tvEditDeleteSubmit.setText(getResources().getString(R.string.timeline_edit_delete_delete));
        }

        tvEditDeleteSubmit.setOnClickListener(View -> {
            dialogEditDeleteComment.dismiss();
        });

        tvEditDeleteCancel.setOnClickListener(View -> {
            dialogEditDeleteComment.dismiss();
        });

        dialogEditDeleteComment.show();
    }

    @Override
    public void OnCommentItemClick(View view, int position, String[] commentData) {
        int commentView = view.getId();
        switch (commentView) {
            case R.id.linearTimelineSendComment:
                commonMethods.showLog("Comment Post Id : " ,TAG+ commentData[0]
                        + " Message : " + commentData[1]
                        + " Position : " + position);
                break;

            case R.id.linearTimelineActionLike:
                commonMethods.showLog("Like Action : " ,TAG+ commentData[0]
                        + " Message : " + commentData[1]
                        + " Position : " + position);
                Log.v("IsLiked",""+commentData[2]);


                break;

            case R.id.imgTimelineProfilePicture:
                commonMethods.showLog("Like Action : " ,TAG+ commentData[0]
                        + " Position : " + position);

               try {
                    String getUserInfo = prefs.getString(commonMethods.expertiseUserInfo, "");
                    ObjectMapper mapper = new ObjectMapper();
                    UserInfoModelPref userResponse = mapper.readValue(getUserInfo, UserInfoModelPref.class);
                    // Log.v("Timeline_Fragment",""+loginResponse.getToken());
                    Log.v("Timeline_Fragment", "" + userResponse.getUserID());

                    GetUserProfileRequest profileRequest = new GetUserProfileRequest();
                    profileRequest.setUserID(userResponse.getUserID());
                    profileRequest.setLanguage(getResources().getString(R.string.language));
                    callUserAbt(profileRequest);

                } catch (IOException e) {
                    e.printStackTrace();
                }catch(NullPointerException e){
                    e.printStackTrace();
                }





            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //do whatever
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){

        super.onBackPressed();

    }


    public void callUserAbt(GetUserProfileRequest userInfo) {
       ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(TimelineActivity.this);
        ExpertiseApiInterface apiInterface = expertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<List<GetProfileInfoAboutResponse>> getUserProfileAbout= apiInterface.getProfileInfoAbout(userInfo);

        getUserProfileAbout.enqueue(new Callback<List<GetProfileInfoAboutResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetProfileInfoAboutResponse>> call, @NonNull Response<List<GetProfileInfoAboutResponse>> response) {

                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());

                try {

                    if (response.code() == 200) {

                        Intent userProfileIntent=new Intent(TimelineActivity.this, UserProfileActivity.class);
                        startActivity(userProfileIntent);





                    } else {

                        commonMethods.showLog("Response code : ", TAG + response.code());

                    }


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<List<GetProfileInfoAboutResponse>> call, Throwable t) {

                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());
            }
        });


    }




}
