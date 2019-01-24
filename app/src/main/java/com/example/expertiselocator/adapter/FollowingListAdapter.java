package com.example.expertiselocator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.expertiselocator.R;
import com.example.expertiselocator.main.UserFollowingListActivity;
import com.example.expertiselocator.model.response.FollowingResponse;

import java.util.List;

public class FollowingListAdapter extends RecyclerView.Adapter<FollowingListAdapter.MyViewHolder> {

    private List<FollowingResponse> FollowingResponseList;
    private Context context;
    private UserFollowingListActivity userFollowingListActivity;


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDisplayName, txtDesignation;
        private ImageView profilePicture, follwinglist_profile_menu;
        private Button btnUnfollow;

        MyViewHolder(View rowView) {
            super(rowView);
            txtDisplayName = (TextView) rowView.findViewById(R.id.txt_displayname_followinglist);
            txtDesignation = (TextView) rowView.findViewById(R.id.txt_designation_followinglist);
            profilePicture = (ImageView) rowView.findViewById(R.id.img_profile_followinglist);
            follwinglist_profile_menu = (ImageView) rowView.findViewById(R.id.follwinglist_profile_menu);
            btnUnfollow = (Button) rowView.findViewById(R.id.btn_unfollow_following);

        }
    }

    public void refreshAdapter(int postion) {
        FollowingResponseList.remove(postion);
        notifyDataSetChanged();

    }


    public FollowingListAdapter(UserFollowingListActivity followersActivity, List<FollowingResponse> followingList) {
        this.FollowingResponseList = followingList;
        this.context = followersActivity;
        this.userFollowingListActivity = (UserFollowingListActivity) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FollowingResponse listResponse = FollowingResponseList.get(position);


        String userProfileImage = listResponse.getProfilePicture().replace("data:image/png;base64,", "");
        holder.txtDisplayName.setText(listResponse.getFollowingUserName());
        if (!userProfileImage.equalsIgnoreCase("/img/user-default.png")) {
            byte[] imageByte = Base64.decode(userProfileImage, Base64.DEFAULT);
            Glide.with(context).asBitmap().load(imageByte).into(holder.profilePicture);
        } else {
            holder.profilePicture.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.collaborations));
        }

        holder.txtDesignation.setText(listResponse.getDesignation() + " - " + listResponse.getDepartment());

        holder.follwinglist_profile_menu.setOnClickListener(View -> {
            userFollowingListActivity.onItemClick(View, position);

        });
        holder.btnUnfollow.setOnClickListener(View -> {
            userFollowingListActivity.onItemClick(View, position);

        });

    }

    @Override
    public int getItemCount() {
        return FollowingResponseList.size();
    }
}
