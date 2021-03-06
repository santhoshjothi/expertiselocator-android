package com.example.expertiselocator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.expertiselocator.R;
import com.example.expertiselocator.main.TimelineActivity;
import com.example.expertiselocator.model.response.GetPostedMessagesResponse;
import com.example.expertiselocator.utils.CommonMethods;
import com.github.abdularis.civ.CircleImageView;

import java.util.List;

public class TimelineReplyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<GetPostedMessagesResponse.Timeline_Replies> getTimelineReplies;
    private TimelineActivity timelineActivity;
    private String userProfilePicture;
    private CommonMethods commonMethods;
    private int timelinePostion;
    TimelineReplyAdapter(Context context, List<GetPostedMessagesResponse.Timeline_Replies> getTimelineReplies, int timelinePostion) {
        this.context = context;
        this.getTimelineReplies = getTimelineReplies;
        timelineActivity = (TimelineActivity) context;
        commonMethods = new CommonMethods(context);
        this.timelinePostion=timelinePostion;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewTimelineReply = LayoutInflater.from(context).inflate(R.layout.list_timeline_action_reply, parent, false);
        return new TimelineReplyHolder(viewTimelineReply);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GetPostedMessagesResponse.Timeline_Replies getTimelineReplyData = getTimelineReplies.get(position);
        TimelineReplyHolder timelineReplyHolder = (TimelineReplyHolder) holder;
        commonMethods.showLog("TimelineCommentRplyAdater. : ",  + position + " " + getTimelineReplies.size());
        timelineReplyHolder.imgTimelineReplyProfileMenu.setVisibility(View.GONE);
        String timelineReplyName = getTimelineReplyData.getUserName();
        timelineReplyHolder.tvTimelineReplyProfileName.setText(timelineReplyName);
        String timelineReplyTime = getTimelineReplyData.getModifiedDate();
        timelineReplyHolder.tvTimelineReplyPostedTime.setText(timelineReplyTime);
        String timelineReplyMessage = getTimelineReplyData.getReplyMessage();
        timelineReplyHolder.tvTimelineReplyMessage.setText(timelineReplyMessage);
        String profilePicture = getTimelineReplyData.getProfilePicture();
        userProfilePicture = profilePicture.replace("data:image/png;base64,", "");
        byte[] userProfilePic = Base64.decode(userProfilePicture, Base64.DEFAULT);
        Glide.with(context).asBitmap().load(userProfilePic)
                .into(timelineReplyHolder.imgTimelineReplyProfilePicture);

        if (getTimelineReplyData.getUserID().trim().equals(commonMethods.getUserId())) {
            timelineReplyHolder.imgTimelineReplyProfileMenu.setVisibility(View.VISIBLE);
        }

        timelineReplyHolder.imgTimelineReplyProfileMenu.setOnClickListener(View -> {
            timelineActivity.onItemClick(View, position);
            commonMethods.showLog("TimelineCommentRplyAdater. : ",  + position + " timelinePostion :" + timelinePostion);

        });

    }

    @Override
    public int getItemCount() {
        return getTimelineReplies.size();
    }

    class TimelineReplyHolder extends RecyclerView.ViewHolder {

        ImageView  imgTimelineReplyProfileMenu;
        CircleImageView imgTimelineReplyProfilePicture;
        TextView tvTimelineReplyProfileName, tvTimelineReplyPostedTime, tvTimelineReplyMessage;

        TimelineReplyHolder(View itemView) {
            super(itemView);

            imgTimelineReplyProfilePicture = (CircleImageView) itemView.findViewById(R.id.imgTimelineReplyProfilePicture);
            imgTimelineReplyProfileMenu = (ImageView) itemView.findViewById(R.id.imgTimelineReplyProfileMenu);

            tvTimelineReplyProfileName = (TextView) itemView.findViewById(R.id.tvTimelineReplyProfileName);
            tvTimelineReplyPostedTime = (TextView) itemView.findViewById(R.id.tvTimelineReplyPostedTime);
            tvTimelineReplyMessage = (TextView) itemView.findViewById(R.id.tvTimelineReplyMessage);
        }
    }
}
