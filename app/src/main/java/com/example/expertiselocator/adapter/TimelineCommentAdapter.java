package com.example.expertiselocator.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.expertiselocator.R;
import com.example.expertiselocator.main.LoginActivity;
import com.example.expertiselocator.main.TimelineActivity;
import com.example.expertiselocator.model.response.GetPostedMessagesResponse;
import com.example.expertiselocator.utils.CommonMethods;

import java.util.List;

public class TimelineCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<GetPostedMessagesResponse.Timeline_Comments> getTimelineComments;
    private TimelineActivity timelineActivity;
    private String userProfilePicture;
    private boolean isReplyShown = false;
    private CommonMethods commonMethods;
    public static final String TAG = TimelineCommentAdapter.class.getSimpleName();
    private LinearLayoutManager linearLayoutManager;
    private List<GetPostedMessagesResponse.Timeline_Replies> getTimelineReplies;
    private TimelineReplyAdapter timelineReplyAdapter;

    TimelineCommentAdapter(Context context, List<GetPostedMessagesResponse.Timeline_Comments> getTimelineComments) {
        this.context = context;
        this.getTimelineComments = getTimelineComments;
        commonMethods = new CommonMethods(context);
        timelineActivity = (TimelineActivity) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewTimelineComments = LayoutInflater.from(context).inflate(R.layout.list_timeline_action_comment, parent, false);
        return new TimelineCommentHolder(viewTimelineComments);
    }

    public void loadMoreComment(List<GetPostedMessagesResponse.Timeline_Comments> getTimelineComments) {
        this.getTimelineComments = getTimelineComments;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetPostedMessagesResponse.Timeline_Comments getTimelineCommentData = getTimelineComments.get(position);
        TimelineCommentHolder timelineCommentHolder = (TimelineCommentHolder) holder;

        timelineCommentHolder.imgTimelineCommentProfileMenu.setVisibility(View.GONE);
        timelineCommentHolder.linearTimelineActionAddReply.setVisibility(View.GONE);

        String commentProfileName = getTimelineCommentData.getUserName();
        timelineCommentHolder.tvTimelineCommentProfileName.setText(commentProfileName);

        String commentPostedTime = getTimelineCommentData.getCommentedDate();
        timelineCommentHolder.tvTimelineCommentPostedTime.setText(commentPostedTime);

        String commentPostedMessage = getTimelineCommentData.getComments();
        timelineCommentHolder.tvTimelineCommentMessage.setText(commentPostedMessage);

        String profilePicture = getTimelineCommentData.getProfilePicture();
        userProfilePicture = profilePicture.replace("data:image/png;base64,", "");
        byte[] userProfilePic = Base64.decode(userProfilePicture, Base64.DEFAULT);
        Glide.with(context).asBitmap().load(userProfilePic)
                .into(timelineCommentHolder.imgTimelineCommentProfilePicture);

        if (getTimelineCommentData.getTimeline_Replies() != null) {
            if (getTimelineCommentData.getTimeline_Replies().size() != 0) {
                getTimelineReplies = getTimelineCommentData.getTimeline_Replies();
                timelineReplyAdapter = new TimelineReplyAdapter(context, getTimelineReplies);
                timelineCommentHolder.rvTimelineReplyMessages.setAdapter(timelineReplyAdapter);
            }
        }

        if (getTimelineCommentData.getUserID().trim().equals(commonMethods.getUserId())) {
            timelineCommentHolder.imgTimelineCommentProfileMenu.setVisibility(View.VISIBLE);

        }

        timelineCommentHolder.imgTimelineCommentProfileMenu.setOnClickListener(View -> {
            String deleteMsg=getTimelineCommentData.getComments();
            String commentsID=getTimelineCommentData.getCommentID();

            Log.v("commentsID",""+" / "+commentsID + deleteMsg);

            String [] deleteComment=new String[]{deleteMsg};
            timelineActivity.onItemClick(View, position,deleteComment);
        });

        timelineCommentHolder.tvTimelineCommentReply.setOnClickListener(View -> {
            if (!isReplyShown) {
                commonMethods.expandTheView(timelineCommentHolder.linearTimelineActionAddReply);
                isReplyShown = true;
                commonMethods.showLog("Position : " ,TAG+ position);
            } else {
                commonMethods.closeTheView(timelineCommentHolder.linearTimelineActionAddReply);
                isReplyShown = false;
                commonMethods.showLog("Position : " ,TAG+ position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getTimelineComments.size();
    }

    class TimelineCommentHolder extends RecyclerView.ViewHolder {

        ImageView imgTimelineCommentProfilePicture, imgTimelineCommentProfileMenu;
        TextView tvTimelineCommentProfileName, tvTimelineCommentPostedTime, tvTimelineCommentMessage, tvTimelineCommentReply;
        RecyclerView rvTimelineReplyMessages;
        LinearLayout linearTimelineActionAddReply;

        TimelineCommentHolder(View itemView) {
            super(itemView);

            linearTimelineActionAddReply = (LinearLayout) itemView.findViewById(R.id.linearTimelineActionAddReply);

            imgTimelineCommentProfilePicture = (ImageView) itemView.findViewById(R.id.imgTimelineCommentProfilePicture);
            imgTimelineCommentProfileMenu = (ImageView) itemView.findViewById(R.id.imgTimelineCommentProfileMenu);

            tvTimelineCommentProfileName = (TextView) itemView.findViewById(R.id.tvTimelineCommentProfileName);
            tvTimelineCommentPostedTime = (TextView) itemView.findViewById(R.id.tvTimelineCommentPostedTime);
            tvTimelineCommentMessage = (TextView) itemView.findViewById(R.id.tvTimelineCommentMessage);
            tvTimelineCommentReply = (TextView) itemView.findViewById(R.id.tvTimelineCommentReply);

            rvTimelineReplyMessages = (RecyclerView) itemView.findViewById(R.id.rvTimelineReplyMessages);
            linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            rvTimelineReplyMessages.setLayoutManager(linearLayoutManager);
        }
    }
}
