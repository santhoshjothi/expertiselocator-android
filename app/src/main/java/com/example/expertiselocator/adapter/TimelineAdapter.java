package com.example.expertiselocator.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.expertiselocator.R;
import com.example.expertiselocator.main.TimelineActivity;
import com.example.expertiselocator.model.UserInfoModelPref;
import com.example.expertiselocator.model.response.GetPostedMessagesResponse;
import com.example.expertiselocator.utils.CommonMethods;
import com.example.expertiselocator.utils.SharedPreferencesWithAES;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private CommonMethods commonMethods;
    private TimelineActivity timelineActivity;
    public static final String TAG = TimelineAdapter.class.getSimpleName();
    private List<GetPostedMessagesResponse> getPostedMessagesResponses;
    private List<GetPostedMessagesResponse.Timeline_Comments> getTimelineComments;
    private TimelineCommentAdapter timelineCommentAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String userProfilePicture, userTimelinePicture, shareProfilePicture, shareTimelinePicture;
    private MediaController mediaController;
    private Uri videoUri;
    private boolean isCommentShow = false;
    private int currentExpand = 0, previousExpand = 0;
    private String commentPostId = "", commentPostMessage = "";
    private String isliked;
    private SharedPreferencesWithAES prefs = null;

    public TimelineAdapter(Context context, List<GetPostedMessagesResponse> getPostedMessagesResponses) {
        this.context = context;
        this.getPostedMessagesResponses = getPostedMessagesResponses;
        commonMethods = new CommonMethods(context);
        timelineActivity = (TimelineActivity) context;
        prefs = SharedPreferencesWithAES.getInstance(context, commonMethods.expertisePreference);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View timelineView = LayoutInflater.from(context).inflate(R.layout.list_timeline_post, parent, false);
        return new TimelineViewHolder(timelineView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetPostedMessagesResponse getPostedMessagesResponse = getPostedMessagesResponses.get(position);
        TimelineViewHolder timelineViewHolder = (TimelineViewHolder) holder;

        commonMethods.showLog("TimelineAdapter : ", getPostedMessagesResponse.getId() + " " + position);

        timelineViewHolder.includeTimelineShare.setVisibility(View.GONE);
        timelineViewHolder.includeTimelineImage.setVisibility(View.GONE);
        timelineViewHolder.includeTimelineVideo.setVisibility(View.GONE);
        timelineViewHolder.imgTimelineProfileMenu.setVisibility(View.GONE);
        timelineViewHolder.linearTimelineActionShowComment.setVisibility(View.GONE);
        timelineViewHolder.includeTimelineAction.setVisibility(View.VISIBLE);

        try {

            isliked = getPostedMessagesResponse.getIsLiked();
            String profilePicture = getPostedMessagesResponse.getProfilePicture();
            userProfilePicture = profilePicture.replace("data:image/png;base64,", "");
            byte[] userProfilePic = Base64.decode(userProfilePicture, Base64.DEFAULT);
            Glide.with(context).asBitmap().load(userProfilePic).into(timelineViewHolder.imgTimelineProfilePicture);

            timelineViewHolder.imgTimelineProfilePicture.setOnClickListener(View -> {
                String[] finalCommentPostData = new String[]{commentPostId};
                timelineActivity.OnCommentItemClick(View, position, finalCommentPostData);
            });

            String timelineProfileName = getPostedMessagesResponse.getUserName();
            timelineViewHolder.tvTimelineProfileName.setText(timelineProfileName);

            String timelineMessage = getPostedMessagesResponse.getMessage();
            timelineViewHolder.tvTimelineMessage.setText(timelineMessage);

            String timelinePostedTime = getPostedMessagesResponse.getPostedDate();
            timelineViewHolder.tvTimelinePostedTime.setText(timelinePostedTime);

            if (getPostedMessagesResponse.getPostImage().trim().length() != 0) {
                //commonMethods.showLog("Post Image Adapter 00 : ", TAG + getPostedMessagesResponse.getPostImage());
            }

            if (getPostedMessagesResponse.getPostVideo().trim().length() != 0) {
                commonMethods.showLog("Post Video Adapter 00 : ", TAG + getPostedMessagesResponse.getPostVideo());
            }

            if (!getPostedMessagesResponse.getSharedPostId().equals("0")) {
                commonMethods.showLog("Post Share Adapter 00 : ", TAG + getPostedMessagesResponse.getSharedPostId());
            }

            if (getPostedMessagesResponse.getPostImage() != null) {
                if (getPostedMessagesResponse.getPostImage().trim().length() != 0) {
                    //commonMethods.showLog("Post Image Adapter 01 : ", TAG + getPostedMessagesResponse.getPostImage());
                    timelineViewHolder.includeTimelineImage.setVisibility(View.VISIBLE);
                    String timelinePicture = getPostedMessagesResponse.getPostImage();
                    userTimelinePicture = timelinePicture.replace("data:image/png;base64,", "");
                    byte[] timelinePic = Base64.decode(userTimelinePicture, Base64.DEFAULT);
                    Glide.with(context).asBitmap().load(timelinePic)
                            .into(timelineViewHolder.viewTimelineImage);
                }
            }

            if (getPostedMessagesResponse.getPostVideo() != null) {
                if (getPostedMessagesResponse.getPostVideo().trim().length() != 0) {
                    if (URLUtil.isValidUrl(getPostedMessagesResponse.getPostVideo().trim())) {
                        commonMethods.showLog("Post Video Adapter 01 : ", TAG + getPostedMessagesResponse.getPostVideo());
                        timelineViewHolder.includeTimelineVideo.setVisibility(View.VISIBLE);
                        videoUri = Uri.parse(getPostedMessagesResponse.getPostVideo());
                        timelineViewHolder.viewTimelineVideo.setVideoURI(videoUri);
                        timelineViewHolder.viewTimelineVideo.start();
                        mediaController = new MediaController(context);
                        mediaController.setMediaPlayer(timelineViewHolder.viewTimelineVideo);
                        timelineViewHolder.viewTimelineVideo.setMediaController(mediaController);
                        timelineViewHolder.viewTimelineVideo.requestFocus();
                    }
                }
            }

            if (getPostedMessagesResponse.getSharedPostId() != null) {
                if (getPostedMessagesResponse.getSharedPostId().trim().length() != 0) {
                    if (!getPostedMessagesResponse.getSharedPostId().trim().equals("0")) {
                        commonMethods.showLog("Post Share Adapter 01 : ", TAG + getPostedMessagesResponse.getSharedPostId());
                        timelineViewHolder.includeTimelineShare.setVisibility(View.VISIBLE);
                        timelineViewHolder.viewShareImage.setVisibility(View.GONE);
                        timelineViewHolder.viewShareVideo.setVisibility(View.GONE);

                        if (getPostedMessagesResponse.getTimeline_SharedPost().getPostImage() != null) {
                            commonMethods.showLog("Share Image Adapter 00 : ", TAG
                                    + getPostedMessagesResponse.getTimeline_SharedPost().getPostImage());
                        }

                        if (getPostedMessagesResponse.getTimeline_SharedPost().getPostVideo() != null) {
                            commonMethods.showLog("Share Video Adapter 00 : ", TAG
                                    + getPostedMessagesResponse.getTimeline_SharedPost().getPostVideo());
                        }

                        String sharePicture = getPostedMessagesResponse.getTimeline_SharedPost().getProfilePicture();
                        shareProfilePicture = sharePicture.replace("data:image/png;base64,", "");
                        byte[] shareProfilePic = Base64.decode(shareProfilePicture, Base64.DEFAULT);
                        Glide.with(context).asBitmap().load(shareProfilePic)
                                .into(timelineViewHolder.imgShareProfilePicture);

                        String shareProfileName = getPostedMessagesResponse.getTimeline_SharedPost().getUserName();
                        timelineViewHolder.tvShareProfileName.setText(shareProfileName);

                        String shareMessage = getPostedMessagesResponse.getTimeline_SharedPost().getMessage();
                        timelineViewHolder.tvShareMessage.setText(shareMessage);

                        if (getPostedMessagesResponse.getTimeline_SharedPost().getPostImage() != null) {
                            if (getPostedMessagesResponse.getTimeline_SharedPost().getPostImage().length() != 0) {
                                commonMethods.showLog("Share Image Adapter 01 : ", TAG
                                        + getPostedMessagesResponse.getTimeline_SharedPost().getPostImage());
                                timelineViewHolder.viewShareImage.setVisibility(View.VISIBLE);
                                String shareTimelinePic = getPostedMessagesResponse.getTimeline_SharedPost().getPostImage();
                                shareTimelinePicture = shareTimelinePic.replace("data:image/png;base64,", "");
                                byte[] sharetimelinePic = Base64.decode(shareTimelinePicture, Base64.DEFAULT);
                                Glide.with(context).asBitmap().load(sharetimelinePic)
                                        .into(timelineViewHolder.viewShareImage);
                            }
                        }

                        if (getPostedMessagesResponse.getTimeline_SharedPost().getPostVideo() != null) {
                            if (getPostedMessagesResponse.getTimeline_SharedPost().getPostVideo().length() != 0) {
                                if (URLUtil.isValidUrl(getPostedMessagesResponse.getTimeline_SharedPost().getPostVideo().trim())) {
                                    commonMethods.showLog("Share Video Adapter 01 : ", TAG
                                            + getPostedMessagesResponse.getTimeline_SharedPost().getPostVideo());
                                    timelineViewHolder.viewShareVideo.setVisibility(View.VISIBLE);
                                    videoUri = Uri.parse(getPostedMessagesResponse.getTimeline_SharedPost().getPostVideo());
                                    timelineViewHolder.viewShareVideo.setVideoURI(videoUri);
                                    timelineViewHolder.viewShareVideo.start();
                                    mediaController = new MediaController(context);
                                    mediaController.setMediaPlayer(timelineViewHolder.viewShareVideo);
                                    timelineViewHolder.viewShareVideo.setMediaController(mediaController);
                                    timelineViewHolder.viewShareVideo.requestFocus();
                                }
                            }
                        }
                    }
                }
            }

            if (getPostedMessagesResponse.getPostOwnerId().trim().equalsIgnoreCase(commonMethods.getUserId())) {
                timelineViewHolder.imgTimelineProfileMenu.setVisibility(View.VISIBLE);
            }

            timelineViewHolder.imgTimelineProfileMenu.setOnClickListener(View -> {
                timelineActivity.onItemClick(View, position);
            });

            timelineViewHolder.linearTimelineActionLike.setOnClickListener(View -> {

                commentPostId = getPostedMessagesResponse.getId();
                View view = timelineViewHolder.tvTimelineActionLike;
                String[] likeDetails = new String[]{commentPostId, isliked, String.valueOf(view)};

                if (isliked.equalsIgnoreCase("0")) {
                    isliked = "1";
                    Drawable like = context.getResources().getDrawable(R.drawable.ic_liked);
                    timelineViewHolder.tvTimelineActionLike.setCompoundDrawablesWithIntrinsicBounds(like, null, null, null);
                    timelineActivity.OnCommentItemClick(View, position, likeDetails);

                } else {
                    isliked = "0";
                    Drawable liked = context.getResources().getDrawable(R.drawable.ic_like);
                    timelineViewHolder.tvTimelineActionLike.setCompoundDrawablesWithIntrinsicBounds(liked, null, null, null);
                    timelineActivity.OnCommentItemClick(View, position, likeDetails);
                }
            });

            /* For Edit and Post Comment */

            if (getPostedMessagesResponse.getTimeline_Comments() != null && !getPostedMessagesResponse.getTimeline_Comments().isEmpty()) {
                getTimelineComments = getPostedMessagesResponse.getTimeline_Comments();
                commonMethods.showLog("TimelineAdapter For Comment : ", String.valueOf(getTimelineComments.size()));
                timelineCommentAdapter = new TimelineCommentAdapter(context, getTimelineComments, position);
                commonMethods.showLog("TimelineAdapter : ", getTimelineComments.get(0).getId() + " " + position);
                timelineViewHolder.rvTimelineActionComments.setAdapter(timelineCommentAdapter);
            }

            timelineViewHolder.linearTimelineActionComment.setOnClickListener(View -> {
                if (!isCommentShow) {
                    timelineViewHolder.etTimelineAddComment.requestFocus();
                    commonMethods.expandTheView(timelineViewHolder.linearTimelineActionShowComment);
                    timelineViewHolder.etTimelineAddComment.requestFocus();
                    isCommentShow = true;
                    commonMethods.showLog("Position : ", TAG + position);
                } else {
                    commonMethods.closeTheView(timelineViewHolder.linearTimelineActionShowComment);
                    isCommentShow = false;
                    commonMethods.showLog("Position : ", TAG + position);
                }
            });

            timelineViewHolder.linearTimelineSendComment.setOnClickListener(View -> {
                if (timelineViewHolder.etTimelineAddComment.getText().toString().trim().length() != 0) {
                    commentPostId = getPostedMessagesResponse.getId();
                    commentPostMessage = timelineViewHolder.etTimelineAddComment.getText().toString();
                    String[] finalCommentPostData = new String[]{commentPostId, commentPostMessage};
                    timelineActivity.OnCommentItemClick(View, position, finalCommentPostData);
                    timelineViewHolder.etTimelineAddComment.clearFocus();
                    timelineViewHolder.etTimelineAddComment.setText("");
                } else {
                    commonMethods.showToast("Comment Message Needed");
                    timelineViewHolder.etTimelineAddComment.requestFocus();
                }
            });


            /* Timeline Post Background */
            String timelineAssetType = getPostedMessagesResponse.getAssestType().trim();
            commonMethods.showLog("TimelineAdapter : ", timelineAssetType + " " + position);
            if (!timelineAssetType.trim().equals("Post")) {
                timelineViewHolder.imgTimelineProfileMenu.setVisibility(View.VISIBLE);
                timelineViewHolder.linearTimelineHeader.setBackgroundColor(context
                        .getResources().getColor(R.color.colorPrimaryDark));
                timelineViewHolder.tvTimelineProfileName.setTextColor(context
                        .getResources().getColor(R.color.white));
                timelineViewHolder.tvTimelinePostedTime.setTextColor(context
                        .getResources().getColor(R.color.white));
            }

            /* Timeline Comment and Like Count Label */

            String timelineLikeCount = null, timelineCommentCount = null, timelineLikeCommentCount = null;

            if (getPostedMessagesResponse.getLikesCounts().trim().equals("0")) {
                timelineLikeCount = context.getResources().getString(R.string.timeline_like_status);
            } else {
                if (getPostedMessagesResponse.getLikesCounts().trim().equals("1")) {
                    timelineLikeCount = getPostedMessagesResponse.getLikesCounts().trim() + " "
                            + context.getResources().getString(R.string.timeline_like_count);
                } else {
                    timelineLikeCount = getPostedMessagesResponse.getLikesCounts().trim() + " "
                            + context.getResources().getString(R.string.timeline_likes_count);
                }
            }

            if (getPostedMessagesResponse.getCommentsCounts().trim().equals("0")) {
                timelineCommentCount = context.getResources().getString(R.string.timeline_comment_status);
            } else {
                if (getPostedMessagesResponse.getCommentsCounts().trim().equals("1")) {
                    timelineCommentCount = getPostedMessagesResponse.getCommentsCounts().trim() + " "
                            + context.getResources().getString(R.string.timeline_comment_count);
                } else {
                    timelineCommentCount = getPostedMessagesResponse.getCommentsCounts().trim() + " "
                            + context.getResources().getString(R.string.timeline_comments_count);
                }
            }

            timelineLikeCommentCount = timelineLikeCount + " - " + timelineCommentCount;

            timelineViewHolder.tvTimelineActionLikeCommentStatus.setText(timelineLikeCommentCount);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void refreshPostedMessage(List<GetPostedMessagesResponse> getPostedMessagesResponses) {
        this.getPostedMessagesResponses = getPostedMessagesResponses;
        notifyDataSetChanged();

    }

    public void refreshCommentMessage(List<GetPostedMessagesResponse> getPostedMessagesResponses){
            this.getPostedMessagesResponses = getPostedMessagesResponses;
             notifyDataSetChanged();
            timelineCommentAdapter.refreshPostedMessage(getPostedMessagesResponses.get(0).getTimeline_Comments());



    }

    @Override
    public int getItemCount() {
        return getPostedMessagesResponses.size();
    }

    class TimelineViewHolder extends RecyclerView.ViewHolder {

        ImageView imgTimelineProfilePicture, imgTimelineProfileMenu;
        TextView tvTimelineProfileName, tvTimelineMessage, tvTimelinePostedTime;
        View includeTimelineShare, includeTimelineImage, includeTimelineVideo, includeTimelineAction;

        ImageView imgShareProfilePicture, viewShareImage;
        TextView tvShareProfileName, tvShareMessage, tvSharePostedTime;
        VideoView viewShareVideo;

        ImageView viewTimelineImage;
        VideoView viewTimelineVideo;

        LinearLayout linearTimelineActionLike, linearTimelineActionComment, linearTimelineActionShare, linearTimelineActionShowComment;
        TextView tvTimelineActionLike, tvTimelineActionComment, tvTimelineActionShare;
        RecyclerView rvTimelineActionComments;

        EditText etTimelineAddComment;
        LinearLayout linearTimelineSendComment;

        LinearLayout linearTimelineHeader;
        TextView tvTimelineActionLikeCommentStatus;

        TimelineViewHolder(View itemView) {
            super(itemView);

            linearTimelineHeader = (LinearLayout) itemView.findViewById(R.id.linearTimelineHeader);
            imgTimelineProfilePicture = (ImageView) itemView.findViewById(R.id.imgTimelineProfilePicture);
            imgTimelineProfileMenu = (ImageView) itemView.findViewById(R.id.imgTimelineProfileMenu);
            tvTimelineProfileName = (TextView) itemView.findViewById(R.id.tvTimelineProfileName);
            tvTimelinePostedTime = (TextView) itemView.findViewById(R.id.tvTimelinePostedTime);
            tvTimelineMessage = (TextView) itemView.findViewById(R.id.tvTimelineMessage);

            includeTimelineShare = (View) itemView.findViewById(R.id.includeTimelineShare);
            includeTimelineImage = (View) itemView.findViewById(R.id.includeTimelineImage);
            includeTimelineVideo = (View) itemView.findViewById(R.id.includeTimelineVideo);
            includeTimelineAction = (View) itemView.findViewById(R.id.includeTimelineAction);

            imgShareProfilePicture = (ImageView) includeTimelineShare.findViewById(R.id.imgShareProfilePicture);
            tvShareProfileName = (TextView) includeTimelineShare.findViewById(R.id.tvShareProfileName);
            tvShareMessage = (TextView) includeTimelineShare.findViewById(R.id.tvShareMessage);
            tvSharePostedTime = (TextView) includeTimelineShare.findViewById(R.id.tvSharePostedTime);
            viewShareImage = (ImageView) includeTimelineShare.findViewById(R.id.viewShareImage);
            viewShareVideo = (VideoView) includeTimelineShare.findViewById(R.id.viewShareVideo);

            viewTimelineImage = (ImageView) includeTimelineImage.findViewById(R.id.viewTimelineImage);
            viewTimelineVideo = (VideoView) includeTimelineVideo.findViewById(R.id.viewTimelineVideo);

            linearTimelineActionLike = (LinearLayout) includeTimelineAction.findViewById(R.id.linearTimelineActionLike);
            linearTimelineActionComment = (LinearLayout) includeTimelineAction.findViewById(R.id.linearTimelineActionComment);
            linearTimelineActionShare = (LinearLayout) includeTimelineAction.findViewById(R.id.linearTimelineActionShare);
            linearTimelineActionShowComment = (LinearLayout) includeTimelineAction.findViewById(R.id.linearTimelineActionShowComment);

            tvTimelineActionLike = (TextView) includeTimelineAction.findViewById(R.id.tvTimelineActionLike);
            tvTimelineActionComment = (TextView) includeTimelineAction.findViewById(R.id.tvTimelineActionComment);
            tvTimelineActionShare = (TextView) includeTimelineAction.findViewById(R.id.tvTimelineActionShare);
            tvTimelineActionLikeCommentStatus = (TextView) includeTimelineAction.findViewById(R.id.tvTimelineActionLikeCommentStatus);

            etTimelineAddComment = (EditText) includeTimelineAction.findViewById(R.id.etTimelineAddComment);
            linearTimelineSendComment = (LinearLayout) includeTimelineAction.findViewById(R.id.linearTimelineSendComment);

            rvTimelineActionComments = (RecyclerView) includeTimelineAction.findViewById(R.id.rvTimelineActionComments);
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            rvTimelineActionComments.setLayoutManager(layoutManager);
        }
    }
}
