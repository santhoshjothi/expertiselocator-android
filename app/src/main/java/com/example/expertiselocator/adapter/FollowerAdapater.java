package com.example.expertiselocator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.expertiselocator.R;
import com.example.expertiselocator.main.FollowersActivity;
import com.example.expertiselocator.main.UserFollowingListActivity;
import com.example.expertiselocator.model.response.FollowersReponse;
import com.example.expertiselocator.utils.CommonMethods;


import java.util.List;

public class FollowerAdapater extends RecyclerView.Adapter<FollowerAdapater.MyViewHolder> {

    private List<FollowersReponse> followingListResponseList;
    private Context context;
    private FollowersActivity userFollowingListActivity;
    private CommonMethods commonMethods;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView txtDisplayName, txtDesignation;
        private ImageView profilePicture, follwinglist_profile_menu;
        private Button btnUnfollow;

        private MyViewHolder(View rowView) {
            super(rowView);
            txtDisplayName = (TextView) rowView.findViewById(R.id.txt_displayname_followinglist);
            txtDesignation = (TextView) rowView.findViewById(R.id.txt_designation_followinglist);
            profilePicture = (ImageView) rowView.findViewById(R.id.img_profile_followinglist);
            follwinglist_profile_menu = (ImageView) rowView.findViewById(R.id.follwinglist_profile_menu);
            btnUnfollow = (Button) rowView.findViewById(R.id.btn_unfollow_following);
        }
    }


    public FollowerAdapater(FollowersActivity followersActivity, List<FollowersReponse> followingList) {
        this.followingListResponseList = followingList;
        this.context = followersActivity;
        this.userFollowingListActivity = (FollowersActivity) context;
        commonMethods = new CommonMethods(context);
    }

    @Override
    public FollowerAdapater.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.following_list_item, parent, false);

        return new FollowerAdapater.MyViewHolder(itemView);
    }

    public void refreshFollowerAdapter(List<FollowersReponse> followingList,int position) {
        followingListResponseList.remove(position);
        followingListResponseList.add(position,followingList.get(position));
        notifyDataSetChanged();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(FollowerAdapater.MyViewHolder holder, int position) {
        FollowersReponse listResponse = followingListResponseList.get(position);
        Log.v("getIsFollowed", "" + listResponse.getIsFollowed());
        String userProfileImage = listResponse.getProfilePicture().replace("data:image/png;base64,", "");
        holder.txtDisplayName.setText(listResponse.getFollowersUserName());

        if(!userProfileImage.equalsIgnoreCase("/img/user-default.png")){
            byte[] imageByte = Base64.decode(userProfileImage, Base64.DEFAULT);
            Glide.with(context).asBitmap().load(imageByte).into(holder.profilePicture);
        }else{
            holder.profilePicture.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.collaborations));
        }

        holder.txtDesignation.setText(listResponse.getDesignation() + " - " + listResponse.getDepartment());

        if (listResponse.getIsFollowed().equalsIgnoreCase("TRUE")) {
            holder.btnUnfollow.setText("UNFOLLOW");
        } else if (listResponse.getIsFollowed().equalsIgnoreCase("UNFOLLOW")) {
            holder.btnUnfollow.setText("FOLLOW");
        }
        holder.follwinglist_profile_menu.setOnClickListener(View -> {
            userFollowingListActivity.onItemClick(View, position);

        });
        holder.btnUnfollow.setOnClickListener(View -> {
            userFollowingListActivity.onItemClick(View, position);

        });

    }

    @Override
    public int getItemCount() {
        return followingListResponseList.size();
    }
}

