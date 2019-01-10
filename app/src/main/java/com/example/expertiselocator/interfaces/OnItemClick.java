package com.example.expertiselocator.interfaces;

import android.view.View;

public interface OnItemClick {

    void onItemClick(View view, int position);

    interface onItemCommentClick{
        void onItemCommentClick(View view,int timelinePostion,int commentPostion);
    }
    interface OnItemClickComment {
        void OnCommentItemClick(View view, int position, String[] commentData);

    }
}
