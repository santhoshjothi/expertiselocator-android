package com.example.expertiselocator.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class CommonMethods {

    private Context context;

    public CommonMethods(Context context) {
        this.context = context;
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

    public void showLog(String logMessage) {
        Log.e("Expertise Locator", logMessage);
    }
}
