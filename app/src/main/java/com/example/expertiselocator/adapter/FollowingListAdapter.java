package com.example.expertiselocator.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.expertiselocator.R;
import com.example.expertiselocator.main.FollowersActivity;
import com.example.expertiselocator.main.UserFollowingListActivity;
import com.example.expertiselocator.model.response.FollowingListResponse;

import java.util.List;

public class FollowingListAdapter extends BaseAdapter {

    private List<FollowingListResponse> followingListResponseList;
    private Context context;
    private LayoutInflater thisInflater;
    UserFollowingListActivity userFollowingListActivity;
    public FollowingListAdapter(UserFollowingListActivity followersActivity, List<FollowingListResponse> followingList) {
        this.followingListResponseList = followingList;
        this.context = followersActivity;
        this.thisInflater = LayoutInflater.from(context);
        this.userFollowingListActivity=(UserFollowingListActivity)context;
    }

    @Override
    public int getCount() {
        return followingListResponseList.size();
    }

    @Override
    public Object getItem(int i) {
        return followingListResponseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder holder = new MyViewHolder();
        View rowView;
        rowView = thisInflater.inflate(R.layout.following_list_item, viewGroup, false);
        holder.txtDisplayName = (TextView) rowView.findViewById(R.id.txt_displayname_followinglist);
        holder.txtDesignation = (TextView) rowView.findViewById(R.id.txt_designation_followinglist);
        holder.profilePicture = (ImageView) rowView.findViewById(R.id.img_profile_followinglist);
        holder.follwinglist_profile_menu= (ImageView) rowView.findViewById(R.id.follwinglist_profile_menu);
        String userProfileImage = followingListResponseList.get(i).getProfilePicture().replace("data:image/png;base64,", "");
        holder.txtDisplayName.setText(followingListResponseList.get(i).getFollowingUserName());
        byte[] imageByte = Base64.decode(userProfileImage, Base64.DEFAULT);
        Glide.with(context).asBitmap().load(imageByte).into(holder.profilePicture );
        holder.txtDesignation.setText(followingListResponseList.get(i).getDesignation()+ " - " + followingListResponseList.get(i).getDepartment());

        holder.follwinglist_profile_menu.setOnClickListener(View ->{
            userFollowingListActivity.onItemClick(View ,i);

        });
        return rowView;
    }
    public class MyViewHolder {
        public TextView txtDisplayName,txtDesignation;
        public ImageView profilePicture,follwinglist_profile_menu;



    }

}
