package com.example.expertiselocator.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.expertiselocator.R;
import com.example.expertiselocator.model.UserInfoModelPref;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CommonMethods {

    private Context context;
    private SharedPreferencesWithAES prefs;
    ProgressDialog dialogLoad;

    public String expertisePreference = "expertise_Prefs";
    public String expertiseLoginToken = "loginresponse";
    public String expertiseUserInfo = "user_info";

    public CommonMethods(Context context) {
        this.context = context;
        dialogLoad = new ProgressDialog(context);
        prefs = SharedPreferencesWithAES.getInstance(context, expertisePreference);
    }

    public void showHideDialog(boolean isDialogShown) {
        try {
            if (isDialogShown) {
                dialogLoad.setCancelable(false);
                dialogLoad.setCanceledOnTouchOutside(false);
                dialogLoad.show();
                dialogLoad.setContentView(R.layout.custom_progress_dialog);
                dialogLoad.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            } else {
                if (dialogLoad != null) {
                    dialogLoad.dismiss();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void expandTheView(LinearLayout $MsgHideLayout) {
        $MsgHideLayout.setVisibility(View.VISIBLE);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        $MsgHideLayout.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0,
                $MsgHideLayout.getMeasuredHeight(), $MsgHideLayout);
        mAnimator.start();
    }

    public void closeTheView(final LinearLayout $MsgHideLayout) {
        int finalHeight = $MsgHideLayout.getHeight();
        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, $MsgHideLayout);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                // Height=0, but it set visibility to GONE
                $MsgHideLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationStart(Animator animation) {
                // TODO Auto-generated method stub
            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end, final LinearLayout $MsgHideLayout) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(valueAnimator -> {
            // Update Height
            int value = (Integer) valueAnimator.getAnimatedValue();
            ViewGroup.LayoutParams layoutParams = $MsgHideLayout
                    .getLayoutParams();
            layoutParams.height = value;
            $MsgHideLayout.setLayoutParams(layoutParams);
        });
        return animator;
    }

    public void showToast(String toastMessage) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }

    public void showLog(String TAG, String logMessage) {
        Log.e("Expertise Locator", TAG + " / " + logMessage);
    }

    public String getUserId() {
        UserInfoModelPref userResponse = null;
        try {
            String getUserInfo = prefs.getString(expertiseUserInfo, "");
            ObjectMapper mapper = new ObjectMapper();
            userResponse = mapper.readValue(getUserInfo, UserInfoModelPref.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        assert userResponse != null;
        return userResponse.getUserID();
    }
}
