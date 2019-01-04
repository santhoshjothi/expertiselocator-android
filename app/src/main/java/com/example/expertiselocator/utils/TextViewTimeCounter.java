package com.example.expertiselocator.utils;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;

import java.util.Locale;

public class TextViewTimeCounter extends android.support.v7.widget.AppCompatTextView {

    private long mStartTime = 0;
    private long mTimeNow = 0;
    private long mDelay = 0;
    private String mPart1 = "";
    private String mPart2 = "";

    private Handler mHandler;

    public TextViewTimeCounter(Context context) {
        super(context, null, 0);
    }

    public TextViewTimeCounter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextViewTimeCounter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void startTimer(long delay, String part1, String part2) {
        mStartTime = System.currentTimeMillis();
        mTimeNow = System.currentTimeMillis();
        mDelay = delay;
        mHandler = new Handler();
        mHandler.postDelayed(r, delay);
        convertDatesToMinutes(mStartTime, mTimeNow);
    }

    public void stopTimer() {
        if (mHandler != null) {
            mHandler = null;
            mStartTime = 0;
            mTimeNow = 0;
        }
    }

    public boolean isTimerRunning() {
        return mHandler != null;
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            mTimeNow += mDelay;
            convertDatesToMinutes(mStartTime, mTimeNow);
            if (mHandler != null) {
                mHandler.postDelayed(r, mDelay);
            }
        }
    };

    public void convertDatesToMinutes(long start, long end) {
        long secs = (end - start);
        long minute = (secs / (1000 * 60)) % 60;
        long hour = (secs / (1000 * 60 * 60)) % 24;
        String time = null;
        if (hour >= 1) {
            time = String.format(Locale.getDefault(), "%2d hours %2d minutes", hour, minute);
        } else {
            time = String.format(Locale.getDefault(), "%2d minutes", minute);
        }
        String timer = mPart1 + time + mPart2;
        setText(timer);
    }
}