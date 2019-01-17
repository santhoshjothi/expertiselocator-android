package com.example.expertiselocator.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.expertiselocator.R;
import com.example.expertiselocator.adapter.TimelineAdapter;
import com.example.expertiselocator.apiclient.ExpertiseApiClient;
import com.example.expertiselocator.interfaces.ExpertiseApiInterface;
import com.example.expertiselocator.interfaces.OnItemClick;
import com.example.expertiselocator.model.request.DeletePostRequest;
import com.example.expertiselocator.model.request.EditDeleteCommentRequest;
import com.example.expertiselocator.model.request.EditPostRequest;
import com.example.expertiselocator.model.request.GetPostedMessageRequest;
import com.example.expertiselocator.model.request.GetUserProfileRequest;
import com.example.expertiselocator.model.request.PostCommentRequest;
import com.example.expertiselocator.model.response.GetPostedMessagesResponse;
import com.example.expertiselocator.model.response.GetProfileInfoAboutResponse;
import com.example.expertiselocator.utils.CommonMethods;
import com.example.expertiselocator.utils.SharedPreferencesWithAES;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;
import java.util.Objects;

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

    public int menuItemClickedPosition, menuItemClickedCommentPosition, menuItemClickedReplyPosition;
    LinearLayout lin_post_timeline;
    SharedPreferencesWithAES prefs;

    boolean timelinePageScrolled = false, loadTimelineOnScroll = false, hasNextPage = false;
    int timelineLastPosition = 0, timelineTotalCount = 0, timelineContentPage = 1, timelineMaxCount = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        commonMethods = new CommonMethods(TimelineActivity.this);
        prefs = SharedPreferencesWithAES.getInstance(TimelineActivity.this, commonMethods.expertisePreference);
        shimmerViewContainerTimeline = (ShimmerFrameLayout) findViewById(R.id.shimmerViewContainerTimeline);
        rvTimelinePost = (RecyclerView) findViewById(R.id.rvTimelinePost);
        lin_post_timeline = (LinearLayout) findViewById(R.id.lin_post_timeline);
        img_search_toolbar = (ImageView) toolbar.findViewById(R.id.img_search_toolbar);

        layoutManager = new LinearLayoutManager(TimelineActivity.this, LinearLayoutManager.VERTICAL, false);
        rvTimelinePost.setLayoutManager(layoutManager);
        prefs = SharedPreferencesWithAES.getInstance(TimelineActivity.this, commonMethods.expertisePreference);
        callTimeline();

        lin_post_timeline.setOnClickListener(view -> {

            Intent postActitity = new Intent(TimelineActivity.this, PostActivity.class);
            startActivity(postActitity);
            finish();
        });
        img_search_toolbar.setOnClickListener(view -> {

            Intent postActitity = new Intent(TimelineActivity.this, SearchActivty.class);
            startActivity(postActitity);
        });

        rvTimelinePost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    timelinePageScrolled = true;
                }

                LinearLayoutManager layoutManager = ((LinearLayoutManager) rvTimelinePost.getLayoutManager());
                timelineLastPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                timelineTotalCount = rvTimelinePost.getAdapter().getItemCount();

                commonMethods.showLog(TAG, "On Scroll 00 " + timelineLastPosition + " " + (timelineTotalCount - 1));
                commonMethods.showLog(TAG, "On Scroll 00 " + timelinePageScrolled + " " + hasNextPage + " " + loadTimelineOnScroll);

                if (timelineLastPosition >= timelineTotalCount - 1) {
                    if (timelinePageScrolled) {
                        timelinePageScrolled = false;
                        if (hasNextPage) {
                            hasNextPage = false;
                            loadTimelineOnScroll = true;
                            callTimelineOnScroll(String.valueOf(timelineContentPage), String.valueOf(timelineMaxCount));
                        } else {
                            commonMethods.showToast("That's All For Now.!");
                        }
                    }
                }
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
    protected void onRestart() {
        super.onRestart();
        callTimeline();
    }


    @Override
    public void onItemClick(View view, int position) {
        Log.v("onItemClick", "" + position);
        int viewType = view.getId();
        switch (viewType) {
            case R.id.imgTimelineProfileMenu:
                menuItemClickedPosition = position;
                PopupMenu popupMenuTimeline = new PopupMenu(TimelineActivity.this, view);
                popupMenuTimeline.setOnMenuItemClickListener(this);
                popupMenuTimeline.inflate(R.menu.timeline_popup_menu);
                if (!getPostedMessagesResponses.get(position).getAssestType().trim().equals("Post")) {
                    if (getPostedMessagesResponses.get(position).getIsAnswered().trim().equals("1")) {
                        popupMenuTimeline.getMenu().findItem(R.id.timelineMenuAnswered).setVisible(false);

                    } else {
                        popupMenuTimeline.getMenu().findItem(R.id.timelineMenuAnswered).setVisible(true);
                    }
                }
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
                commonMethods.showToast("Edit_Menu " + menuItemClickedPosition);
                showEditDeleteCommentDialog("Edit", "Post");
                break;

            case R.id.timelineMenuDelete:
                commonMethods.showToast("Delete Menu " + menuItemClickedPosition);
                showEditDeleteCommentDialog("Delete", "Post");
                break;

            case R.id.timelineCommentMenuEdit:
                commonMethods.showLog("Timeline Activity Position : ", menuItemClickedCommentPosition + " ");
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
                commonMethods.showLog("Timeline Activity Position", " " + menuItemClickedCommentPosition);
                etEditDeleteMessageContent.setText(getPostedMessagesResponses.get(menuItemClickedCommentPosition)
                        .getTimeline_Comments().get(0).getComments());
            }
            if (menuOption.equals("Reply")) {
                tvEditDeleteTitle.setText(getResources().getString(R.string.timeline_edit_reply_title));
                tvEditDeleteMessage.setText(getResources().getString(R.string.timeline_edit_reply_message));
                tvEditDeleteQuery.setVisibility(View.GONE);
            }
            if (menuOption.equals("Post")) {
                tvEditDeleteTitle.setText(getResources().getString(R.string.timeline_edit_post_heading));
                tvEditDeleteMessage.setText(getResources().getString(R.string.timeline_edit_post_message));
                tvEditDeleteQuery.setVisibility(View.GONE);
                etEditDeleteMessageContent.setText(getPostedMessagesResponses.get(menuItemClickedPosition)
                        .getMessage());
            }
            tvEditDeleteSubmit.setText(getResources().getString(R.string.timeline_edit_delete_update));
        }
        if (menuType.equals("Delete")) {
            if (menuOption.equals("Comment")) {
                try {
                    tvEditDeleteTitle.setText(getResources().getString(R.string.timeline_delete_comment_title));
                    tvEditDeleteMessage.setText(getResources().getString(R.string.timeline_delete_comment_message));
                    tvEditDeleteQuery.setVisibility(View.VISIBLE);
                    tvEditDeleteQuery.setText(getResources().getString(R.string.timeline_delete_comment_query));
                    etEditDeleteMessageContent.setText(getPostedMessagesResponses.get(menuItemClickedCommentPosition)
                            .getTimeline_Comments().get(0).getComments());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                etEditDeleteMessageContent.setEnabled(false);
            }
            if (menuOption.equals("Post")) {
                tvEditDeleteTitle.setText(getResources().getString(R.string.timeline_edit_post_heading));
                tvEditDeleteMessage.setText(getResources().getString(R.string.timeline_edit_post_message));
                tvEditDeleteQuery.setVisibility(View.GONE);
                etEditDeleteMessageContent.setText(getPostedMessagesResponses.get(menuItemClickedPosition)
                        .getMessage());
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
            if (menuType.equals("Edit")) {
                if (menuOption.equals("Comment")) {
                    try {
                        EditDeleteCommentRequest actionRequest = new EditDeleteCommentRequest();
                        Log.v("Edit_Submit_Comment", " Comment :" + getPostedMessagesResponses.get(menuItemClickedCommentPosition).getTimeline_Comments().get(0).getComments());
                        Log.v("Edit_Submit_Comment", " ID  : " + getPostedMessagesResponses.get(menuItemClickedCommentPosition).getId());
                        Log.v("Edit_Submit_Comment", " TimelineAdapter_postion  : " + menuItemClickedCommentPosition);
                        actionRequest.setComments(etEditDeleteMessageContent.getText().toString());
                        actionRequest.setId(getPostedMessagesResponses.get(menuItemClickedCommentPosition).getTimeline_Comments().get(0).getId());
                        actionRequest.setUserID(commonMethods.getUserId());
                        dialogEditDeleteComment.dismiss();
                        callEditComment(actionRequest, menuItemClickedCommentPosition, getPostedMessagesResponses.get(menuItemClickedCommentPosition).getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (menuOption.equals("Post")) {
                    try {
                        Log.v("Edit_Submit_Post", " TimelineAdapter_postion : " + menuItemClickedPosition);
                        EditPostRequest postRequest = new EditPostRequest();
                        postRequest.setMessage(etEditDeleteMessageContent.getText().toString());
                        postRequest.setSubject("Dynamic");
                        postRequest.setPostImage(getPostedMessagesResponses.get(menuItemClickedPosition).getPostImage());
                        postRequest.setPostVideo(getPostedMessagesResponses.get(menuItemClickedPosition).getPostVideo());
                        postRequest.setUserID(commonMethods.getUserId());
                        postRequest.setAssestType("Dynamic");
                        postRequest.setIsActive("0");
                        postRequest.setPostedBy(commonMethods.getUserId());
                        postRequest.setModifiedBy(commonMethods.getUserId());
                        postRequest.setPostOwnerId(getPostedMessagesResponses.get(menuItemClickedPosition).getPostOwnerId());
                        postRequest.setPostID(getPostedMessagesResponses.get(menuItemClickedPosition).getId());
                        dialogEditDeleteComment.dismiss();
                        callEditPost(postRequest, menuItemClickedPosition, getPostedMessagesResponses.get(menuItemClickedPosition).getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (menuType.equals("Delete")) {
                if (menuOption.equals("Comment")) {
                    try {
                        //getPostedMessagesResponses.get(menuItemClickedPosition).getTimeline_Comments().get(0).getComments() != null)
                        EditDeleteCommentRequest actionRequest = new EditDeleteCommentRequest();
                        Log.v("Delete_Submit_Comment", " Comment :" + getPostedMessagesResponses.get(menuItemClickedCommentPosition).getTimeline_Comments().get(0).getComments());
                        Log.v("Delete_Submit_Comment", " ID  : " + getPostedMessagesResponses.get(menuItemClickedCommentPosition).getId());
                        Log.v("Delete_Submit_Comment", " TimelineAdapter_postion  : " + menuItemClickedCommentPosition);
                        actionRequest.setComments(getPostedMessagesResponses.get(menuItemClickedCommentPosition).getTimeline_Comments().get(0).getComments());
                        actionRequest.setId(getPostedMessagesResponses.get(menuItemClickedCommentPosition).getTimeline_Comments().get(0).getId());
                        actionRequest.setUserID(commonMethods.getUserId());
                        dialogEditDeleteComment.dismiss();
                        callDeleteComment(actionRequest, menuItemClickedCommentPosition, getPostedMessagesResponses.get(menuItemClickedCommentPosition).getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (menuOption.equals("Post")) {
                    try {
                        Log.v("Edit_Submit_Post", " TimelineAdapter_postion : " + menuItemClickedPosition);
                        DeletePostRequest postRequest = new DeletePostRequest();
                        postRequest.setMessage(getPostedMessagesResponses.get(menuItemClickedPosition).getMessage());
                        postRequest.setPostImage(getPostedMessagesResponses.get(menuItemClickedPosition).getPostImage());
                        postRequest.setPostVideo(getPostedMessagesResponses.get(menuItemClickedPosition).getPostVideo());
                        postRequest.setUserID(commonMethods.getUserId());
                        postRequest.setAssestType("Dynamic");
                        postRequest.setIsActive("0");
                        postRequest.setPostedBy(commonMethods.getUserId());
                        postRequest.setModifiedBy(commonMethods.getUserId());
                        postRequest.setPostOwnerId(getPostedMessagesResponses.get(menuItemClickedPosition).getPostOwnerId());
                        postRequest.setPostID(getPostedMessagesResponses.get(menuItemClickedPosition).getId());
                        dialogEditDeleteComment.dismiss();
                        callDeletePost(postRequest, menuItemClickedPosition, getPostedMessagesResponses.get(menuItemClickedPosition).getId());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
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

                PostCommentRequest postCommentRequest = new PostCommentRequest();
                postCommentRequest.setUserID(commonMethods.getUserId());
                postCommentRequest.setPostID(commentData[0]);
                postCommentRequest.setComments(commentData[1]);
                postCommentRequest.setCommentedBy(commonMethods.getUserId());
                postCommentRequest.setModifiedBy(commonMethods.getUserId());
                callPostComment(postCommentRequest, commentData[0], position);

                commonMethods.showLog("Comment Post Id : ", TAG + commentData[0]
                        + " PostID : " + commentData[0]
                        + " Comment : " + commentData[1]
                        + " CommentedBy : " + commonMethods.getUserId()
                        + " Position : " + position);
                break;

            case R.id.linearTimelineActionLike:
                commonMethods.showLog("Like Action : ", TAG + commentData[0]
                        + " Message : " + commentData[1]
                        + " Position : " + position);
                Log.v("IsLiked", "" + commentData[2]);
                break;

            case R.id.imgTimelineProfilePicture:
                commonMethods.showLog("Like Action : ", TAG + commentData[0]
                        + " Position : " + position);
                GetUserProfileRequest profileRequest = new GetUserProfileRequest();
                profileRequest.setUserID(commonMethods.getUserId());
                profileRequest.setLanguage(getResources().getString(R.string.language));
                callUserAbt(profileRequest);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void callUserAbt(GetUserProfileRequest userInfo) {
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(TimelineActivity.this);
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
                        Intent userProfileIntent = new Intent(TimelineActivity.this, UserProfileActivity.class);
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

    public void callPostComment(PostCommentRequest commentRequest, String postId, final int position) {
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(TimelineActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<Integer> getUserProfileAbout = apiInterface.postComment(commentRequest);
        getUserProfileAbout.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());

                try {
                    if (response.code() == 200) {

                        if (!response.body().toString().equalsIgnoreCase("0")) {

                            callTimelineParticularPost(postId, position);


                        } else {
                            commonMethods.showToast(getResources().getString(R.string.fail_addcomment_timeline));

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
            public void onFailure(Call<Integer> call, Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());
                commonMethods.showToast(getResources().getString(R.string.fail_addcomment_timeline));
            }
        });
    }

    public void callTimeline() {
        GetPostedMessageRequest getPostedMessageRequest = new GetPostedMessageRequest();
        getPostedMessageRequest.setUserID(commonMethods.getUserId());
        getPostedMessageRequest.setStartIndex(String.valueOf(timelineContentPage));
        getPostedMessageRequest.setMaxCount(String.valueOf(timelineMaxCount));
        getPostedMessageRequest.setPostID("");

        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(TimelineActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<List<GetPostedMessagesResponse>> getPostedMessage = apiInterface.getPostedMessage(getPostedMessageRequest);
        getPostedMessage.enqueue(new Callback<List<GetPostedMessagesResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetPostedMessagesResponse>> call, @NonNull Response<List<GetPostedMessagesResponse>> response) {
                commonMethods.showLog("On Scroll 01 URL : ", response.code() + " " + call.request().url());
                commonMethods.showLog("On Scroll 01 Response Body : ", TAG + response.body());

                List<GetPostedMessagesResponse> getPostedMessagesResponseResult = response.body();
                assert getPostedMessagesResponseResult != null;
                for (GetPostedMessagesResponse messages : getPostedMessagesResponseResult) {
                    commonMethods.showLog("On Scroll 01 Post Id : ", TAG + messages.getSharedPostId());
                }

                hasNextPage = true;
                timelineContentPage = timelineContentPage + 1;
                getPostedMessagesResponses = response.body();
                timelineAdapter = new TimelineAdapter(TimelineActivity.this, getPostedMessagesResponses);
                rvTimelinePost.setAdapter(timelineAdapter);
                shimmerViewContainerTimeline.stopShimmer();
                shimmerViewContainerTimeline.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<List<GetPostedMessagesResponse>> call, Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());
                shimmerViewContainerTimeline.stopShimmer();
                shimmerViewContainerTimeline.setVisibility(View.GONE);
            }
        });
    }

    public void callTimelineParticularPost(String postId, int position) {
        GetPostedMessageRequest getPostedMessageRequest = new GetPostedMessageRequest();
        getPostedMessageRequest.setUserID(commonMethods.getUserId());
        getPostedMessageRequest.setStartIndex("1");
        getPostedMessageRequest.setMaxCount("1");
        getPostedMessageRequest.setPostID(postId);
        commonMethods.showLog("Timeline Activity ParticularPost : ", TAG + " postId " + postId + "position " + position);
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(TimelineActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<List<GetPostedMessagesResponse>> getPostedMessage = apiInterface.getPostedMessage(getPostedMessageRequest);
        getPostedMessage.enqueue(new Callback<List<GetPostedMessagesResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetPostedMessagesResponse>> call, @NonNull Response<List<GetPostedMessagesResponse>> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());
                List<GetPostedMessagesResponse> getPostedMessagesResponseResult = response.body();
                assert getPostedMessagesResponseResult != null;
                getPostedMessagesResponses.remove(position);
                getPostedMessagesResponses.add(position, getPostedMessagesResponseResult.get(0));
                timelineAdapter.refreshPostedMessage(getPostedMessagesResponses);
                commonMethods.showToast("Sucessfully Update");
            }

            @Override
            public void onFailure(Call<List<GetPostedMessagesResponse>> call, Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());
                shimmerViewContainerTimeline.stopShimmer();
                shimmerViewContainerTimeline.setVisibility(View.GONE);
            }
        });
    }

    public void callTimelineParticularDeletePost(String postId, int position) {
        GetPostedMessageRequest getPostedMessageRequest = new GetPostedMessageRequest();
        getPostedMessageRequest.setUserID(commonMethods.getUserId());
        getPostedMessageRequest.setStartIndex("1");
        getPostedMessageRequest.setMaxCount("1");
        getPostedMessageRequest.setPostID(postId);
        commonMethods.showLog("Timeline Activity ParticularPost : ", TAG + " postId " + postId + "position " + position);
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(TimelineActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<List<GetPostedMessagesResponse>> getPostedMessage = apiInterface.getPostedMessage(getPostedMessageRequest);
        getPostedMessage.enqueue(new Callback<List<GetPostedMessagesResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetPostedMessagesResponse>> call, @NonNull Response<List<GetPostedMessagesResponse>> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());
                List<GetPostedMessagesResponse> getPostedMessagesResponseResult = response.body();
                assert getPostedMessagesResponseResult != null;
                getPostedMessagesResponses.remove(position);
                timelineAdapter.refreshPostedMessage(getPostedMessagesResponses);
                commonMethods.showToast("Sucessfully Update");
            }

            @Override
            public void onFailure(Call<List<GetPostedMessagesResponse>> call, Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());
                shimmerViewContainerTimeline.stopShimmer();
                shimmerViewContainerTimeline.setVisibility(View.GONE);
            }
        });
    }

    public void callDeleteComment(EditDeleteCommentRequest actionRequest, int position, String postId) {
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(TimelineActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<Integer> getPostedMessage = apiInterface.deleteComment(actionRequest);
        getPostedMessage.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body().toString());
                commonMethods.showLog("TimelineActivity callDelete: ", "postId" + postId + "position" + position);
                try {
                    if (response.code() == 200) {

                        assert response.body() != null;
                        if (!response.body().toString().equalsIgnoreCase("0")) {

                            callDeletecommentparticular(postId, position);


                        } else {
                            commonMethods.showToast(getResources().getString(R.string.fail_addcomment_timeline));

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
            public void onFailure(Call<Integer> call, Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());
                shimmerViewContainerTimeline.stopShimmer();
                shimmerViewContainerTimeline.setVisibility(View.GONE);
            }
        });
    }

    public void callDeletecommentparticular(String postId, int position) {
        GetPostedMessageRequest getPostedMessageRequest = new GetPostedMessageRequest();
        getPostedMessageRequest.setUserID(commonMethods.getUserId());
        getPostedMessageRequest.setStartIndex("1");
        getPostedMessageRequest.setMaxCount("1");
        getPostedMessageRequest.setPostID(postId);
        commonMethods.showLog("Timeline Activity ParticularPost : ", TAG + " postId " + postId + "position " + position);
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(TimelineActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<List<GetPostedMessagesResponse>> getPostedMessage = apiInterface.getPostedMessage(getPostedMessageRequest);
        getPostedMessage.enqueue(new Callback<List<GetPostedMessagesResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetPostedMessagesResponse>> call, @NonNull Response<List<GetPostedMessagesResponse>> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());
                List<GetPostedMessagesResponse> getPostedMessagesResponseResult = response.body();
                commonMethods.showLog("CommentNAme: ", TAG + " SIZE " + response.body().get(0).getMessage());
                commonMethods.showLog("CountComments : ", TAG + " SIZE " + response.body().get(0).getTimeline_Comments().size());
                //   commonMethods.showLog("CountComments : ", TAG + " SIZE " +response.body().get(0).getTimeline_Comments().get(0).getComments());
                assert getPostedMessagesResponseResult != null;
                timelineAdapter.refreshCommentMessage(getPostedMessagesResponseResult, position);
                commonMethods.showToast("Sucessfully Update");
            }

            @Override
            public void onFailure(Call<List<GetPostedMessagesResponse>> call, Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());
                shimmerViewContainerTimeline.stopShimmer();
                shimmerViewContainerTimeline.setVisibility(View.GONE);
            }
        });
    }

    public void callEditComment(EditDeleteCommentRequest actionRequest, int position, String postId) {
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(TimelineActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<Integer> getPostedMessage = apiInterface.editComment(actionRequest);
        getPostedMessage.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());
                commonMethods.showLog("Timeline Activity CallEdit : ", "postId" + postId + "position" + position);
                try {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        if (!response.body().toString().equalsIgnoreCase("0")) {
                            callTimelineEditComment(postId, position);
                        } else {
                            commonMethods.showToast(getResources().getString(R.string.fail_addcomment_timeline));
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
            public void onFailure(Call<Integer> call, Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());
                shimmerViewContainerTimeline.stopShimmer();
                shimmerViewContainerTimeline.setVisibility(View.GONE);
            }
        });
    }

    public void callTimelineEditComment(String postId, int position) {
        GetPostedMessageRequest getPostedMessageRequest = new GetPostedMessageRequest();
        getPostedMessageRequest.setUserID(commonMethods.getUserId());
        getPostedMessageRequest.setStartIndex("1");
        getPostedMessageRequest.setMaxCount("1");
        getPostedMessageRequest.setPostID(postId);
        commonMethods.showLog("TimelineActivity EditDeleteCommentParticularPostID: ", TAG + "PostId" + postId + "Postion" + position);
        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(TimelineActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<List<GetPostedMessagesResponse>> getPostedMessage = apiInterface.getPostedMessage(getPostedMessageRequest);
        getPostedMessage.enqueue(new Callback<List<GetPostedMessagesResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetPostedMessagesResponse>> call, @NonNull Response<List<GetPostedMessagesResponse>> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());
                List<GetPostedMessagesResponse> getPostedMessagesResponseResult = response.body();
                assert getPostedMessagesResponseResult != null;
                getPostedMessagesResponses.remove(position);
                getPostedMessagesResponses.add(position, getPostedMessagesResponseResult.get(0));
                timelineAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<GetPostedMessagesResponse>> call, Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());
                shimmerViewContainerTimeline.stopShimmer();
                shimmerViewContainerTimeline.setVisibility(View.GONE);
            }
        });
    }

    /* Call Timeline On Page Scroll */
    public void callTimelineOnScroll(String startIndexPosition, String maxCount) {
        commonMethods.showHideDialog(true);
        GetPostedMessageRequest getPostedMessageRequest = new GetPostedMessageRequest();
        getPostedMessageRequest.setUserID(commonMethods.getUserId());
        getPostedMessageRequest.setStartIndex(startIndexPosition);
        getPostedMessageRequest.setMaxCount(maxCount);
        getPostedMessageRequest.setPostID("");

        commonMethods.showLog(TAG, "On Scroll " + commonMethods.getUserId() + " " + startIndexPosition + " " + maxCount);

        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(TimelineActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<List<GetPostedMessagesResponse>> getPostedMessage = apiInterface.getPostedMessage(getPostedMessageRequest);
        getPostedMessage.enqueue(new Callback<List<GetPostedMessagesResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<GetPostedMessagesResponse>> call, @NonNull Response<List<GetPostedMessagesResponse>> response) {
                commonMethods.showLog("On Scroll URL : ", response.code() + " " + call.request().url());
                commonMethods.showLog("On Scroll Response Body : ", TAG + response.body());

                List<GetPostedMessagesResponse> getPostedMessagesResponseResult = response.body();
                assert getPostedMessagesResponseResult != null;
                for (GetPostedMessagesResponse messages : getPostedMessagesResponseResult) {
                    commonMethods.showLog("On Scroll Post Id : ", TAG + messages.getId());
                }
                getPostedMessagesResponses.addAll(Objects.requireNonNull(response.body()));
                timelineAdapter = new TimelineAdapter(TimelineActivity.this, getPostedMessagesResponses);
                rvTimelinePost.setAdapter(timelineAdapter);
                timelineAdapter.refreshPostedMessage(getPostedMessagesResponses);
                commonMethods.showHideDialog(false);
                if (loadTimelineOnScroll) {
                    loadTimelineOnScroll = false;
                    rvTimelinePost.scrollToPosition(timelineLastPosition + 1);
                    hasNextPage = true;
                    timelineContentPage = timelineContentPage + 1;
                }
                commonMethods.showLog(TAG, "On Scroll " + timelineContentPage);
            }

            @Override
            public void onFailure(Call<List<GetPostedMessagesResponse>> call, Throwable t) {
                commonMethods.showLog("On Scroll URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("On Scroll Failure : ", TAG + t.getMessage());
                shimmerViewContainerTimeline.stopShimmer();
                shimmerViewContainerTimeline.setVisibility(View.GONE);
            }
        });
    }

    public void callEditPost(EditPostRequest postRequest, int position, String postID) {

        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(TimelineActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<Integer> getPostedMessage = apiInterface.editPost(postRequest);
        getPostedMessage.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());
                commonMethods.showLog("TimelineActivity_callEidt: ", "postID" + postID + "position" + position);
                try {
                    if (response.code() == 200) {

                        if (!response.body().toString().equalsIgnoreCase("0")) {

                            callTimelineParticularPost(postID, position);


                        } else {
                            commonMethods.showToast(getResources().getString(R.string.fail_edit_post_timeline));

                        }

                    } else {
                        commonMethods.showLog("Response code : ", TAG + response.code());
                        commonMethods.showToast(getResources().getString(R.string.fail_edit_post_timeline));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());
                shimmerViewContainerTimeline.stopShimmer();
                shimmerViewContainerTimeline.setVisibility(View.GONE);
            }
        });
    }

    private void callDeletePost(DeletePostRequest postRequest, int position, String postID) {

        ExpertiseApiClient expertiseApiClient = new ExpertiseApiClient(TimelineActivity.this);
        ExpertiseApiInterface apiInterface = ExpertiseApiClient.getRetrofitWithAuthorization().create(ExpertiseApiInterface.class);
        Call<Integer> getPostedMessage = apiInterface.DeletePost(postRequest);
        getPostedMessage.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                commonMethods.showLog("URL Success : ", TAG + call.request().url());
                commonMethods.showLog("Response Code : ", TAG + response.code());
                commonMethods.showLog("Response Body : ", TAG + response.body());
                commonMethods.showLog("TimelineActivity_callEidt: ", "postID" + postID + "position" + position);
                try {
                    if (response.code() == 200) {

                        if (!response.body().toString().equalsIgnoreCase("0")) {

                            callTimelineParticularDeletePost(postID, position);


                        } else {
                            commonMethods.showToast(getResources().getString(R.string.fail_delete_post_timeline));

                        }

                    } else {
                        commonMethods.showLog("Response code : ", TAG + response.code());
                        commonMethods.showToast(getResources().getString(R.string.fail_delete_post_timeline));
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                commonMethods.showLog("URL Failure : ", TAG + call.request().url());
                commonMethods.showLog("Failure : ", TAG + t.getMessage());
                shimmerViewContainerTimeline.stopShimmer();
                shimmerViewContainerTimeline.setVisibility(View.GONE);
            }
        });
    }
}