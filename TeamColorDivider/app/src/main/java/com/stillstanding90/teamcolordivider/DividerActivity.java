package com.stillstanding90.teamcolordivider;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

public class DividerActivity extends Activity {
    private static final String TAG = DividerActivity.class.getSimpleName();

    private ArrayList<Integer> mColors;

    private Button mResetButton;

    private int mGroupCount;
    private int mPeopleCount;
    private int mProcessCount;

    private int[] mCountColors;
    private int[] mNowColors;
    private int[] mLeaderColors;

    private ArrayList<View> mViewColors;
    private ArrayList<TextView> mTextColors;

    private LottieAnimationView mLottie;
    private View mTouchFrame;
    private View mBackground;
    private ImageView mRaccoon;
    private OutlineTextView mTouchText;
    private long[] mPatten = {0, 100};
    private int mLottieCenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_divider);

        Intent intent = getIntent();
        if (intent == null) {
            Log.e(TAG, "onCreate. Invalid intent.");
            return;
        }

        mGroupCount = intent.getIntExtra("GROUP_COUNT", -1);
        mPeopleCount = intent.getIntExtra("PEOPLE_COUNT", -1);
        mColors = intent.getIntegerArrayListExtra("COLORS");

        mViewColors = new ArrayList<>();
        mViewColors.add(findViewById(R.id.lo_color1));
        mViewColors.add(findViewById(R.id.lo_color2));
        mViewColors.add(findViewById(R.id.lo_color3));
        mViewColors.add(findViewById(R.id.lo_color4));
        mViewColors.add(findViewById(R.id.lo_color5));
        mViewColors.add(findViewById(R.id.lo_color6));
        mViewColors.add(findViewById(R.id.lo_color7));
        mViewColors.add(findViewById(R.id.lo_color8));
        mViewColors.add(findViewById(R.id.lo_color9));

        mTextColors = new ArrayList<>();
        mTextColors.add((TextView) findViewById(R.id.tv_color1));
        mTextColors.add((TextView) findViewById(R.id.tv_color2));
        mTextColors.add((TextView) findViewById(R.id.tv_color3));
        mTextColors.add((TextView) findViewById(R.id.tv_color4));
        mTextColors.add((TextView) findViewById(R.id.tv_color5));
        mTextColors.add((TextView) findViewById(R.id.tv_color6));
        mTextColors.add((TextView) findViewById(R.id.tv_color7));
        mTextColors.add((TextView) findViewById(R.id.tv_color8));
        mTextColors.add((TextView) findViewById(R.id.tv_color9));

        mNowColors = new int[mGroupCount];
        mCountColors = new int[mGroupCount];
        mLeaderColors = new int[mGroupCount];

        int result = mPeopleCount / mGroupCount;
        for (int i = 0; i < mViewColors.size(); i++) {
            if (mGroupCount > i) {
                mViewColors.get(i).setVisibility(View.VISIBLE);
                mViewColors.get(i).setBackgroundColor(mColors.get(i));

                mNowColors[i] = 0;
                mCountColors[i] = result;
            } else {
                mViewColors.get(i).setVisibility(View.GONE);
            }
        }

        int remainder = mPeopleCount % mGroupCount;
        for (int i = 0; i < remainder; i++) {
            mCountColors[i]++;
        }

        for (int i = 0; i < mGroupCount; i++) {
            mViewColors.get(i).setBackgroundColor(mColors.get(i));
            mTextColors.get(i).setText(mNowColors[i] + "/" + mCountColors[i]);
            mLeaderColors[i] = (int) (Math.random() * mCountColors[i]) + 1;
        }

        mLottieCenter = getResources().getDimensionPixelSize(R.dimen.animation_size) / 2;

        mTouchFrame = findViewById(R.id.fl_touch);
        mBackground = findViewById(R.id.view_background);
        mRaccoon = findViewById(R.id.iv_raccoon);
        mTouchText = findViewById(R.id.tv_touch);
        mTouchFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (MotionEvent.ACTION_DOWN == motionEvent.getAction()) {
                    mLottie.setX(motionEvent.getX() - mLottieCenter);
                    mLottie.setY(motionEvent.getY() - mLottieCenter);

                    mRaccoon.setVisibility(View.GONE);
                    mLottie.cancelAnimation();
                    randomPick();

                    Vibrator vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    vb.vibrate(mPatten, -1);
                    mPatten = new long[]{0, 100};

                    mProcessCount++;

                    if (mProcessCount == mPeopleCount) {
                        mTouchText.setText(mProcessCount + "\nDONE!");
                        mTouchFrame.setEnabled(false);
                        mTouchFrame.setClickable(false);
                    } else {
                        mTouchText.setText(mProcessCount + "\nTOUCH");
                    }
                }
                return false;
            }
        });

        mResetButton = findViewById(R.id.btn_reset);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLottie.cancelAnimation();
                finish();
            }
        });

        mLottie = findViewById(R.id.lav_favorite);
        mLottie.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                mLottie.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                mLottie.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


    }

    private void randomPick() {
        int random = (int) (Math.random() * mGroupCount);
        Log.e(TAG, "randomPick. random: " + random);

        if (mNowColors[random] < mCountColors[random]) {
            mNowColors[random]++;
            mTextColors.get(random).setText(mNowColors[random] + "/" + mCountColors[random]);
            mBackground.setBackgroundColor(mColors.get(random));
            mLottie.setVisibility(View.VISIBLE);
            mLottie.playAnimation();

            if (mLeaderColors[random] == mNowColors[random]) {
                mRaccoon.setVisibility(View.VISIBLE);
                mPatten = new long[]{0, 150, 50, 100, 100, 50};
            }
        } else {
            Log.e(TAG, "randomPick. mNowColors[random]: " + mNowColors[random] + ", mCountColors[random]: " + mCountColors[random]);
            randomPick();
        }
    }
}
