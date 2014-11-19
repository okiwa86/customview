package com.example.administrator.viewpagertest;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.renderscript.Sampler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class MyActivity extends ActionBarActivity {


    boolean slideOn;

    RelativeLayout mSlideWrapper;
    LinearLayout mHeaderLayout;
    ImageView mSlideButton;

    // The height of header linearLayout when slide menu is opened
    int minimumHeight = 600;
    final int ANIMATION_DURATION = 150;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        // Variables
        mSlideWrapper = (RelativeLayout)findViewById(R.id.slideWrapper);
        mHeaderLayout = (LinearLayout)findViewById(R.id.slidingHeader);
        mSlideButton = (ImageView)findViewById(R.id.slideButton);

        mSlideButton.setOnTouchListener(new View.OnTouchListener() {

            int initPositionY;
            int initHeaderHeight;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int currPositionY = (int)mHeaderLayout.getHeight() - (int)mSlideButton.getHeight() + (int)event.getY();

                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN :
                        // initialize first touch position
                        initPositionY = (int)mHeaderLayout.getHeight() - (int)mSlideButton.getHeight() + (int)event.getY();
                        initHeaderHeight = (int)mHeaderLayout.getHeight();
                        return true;

                    case MotionEvent.ACTION_MOVE :
                        // move according to move action
                        mHeaderLayout.getLayoutParams().height = initHeaderHeight-(initPositionY-currPositionY);
                        mHeaderLayout.requestLayout();
                        break;

                    case MotionEvent.ACTION_UP :
                        // Do show or hide action

                        if(Math.abs(initPositionY-currPositionY) <= 20)
                        {
                            if(slideOn)
                            {
                                slideOn = false;
                                getAnimator(mHeaderLayout.getHeight(), mSlideWrapper.getHeight()).start();
                            }
                            else
                            {
                                slideOn = true;
                                getAnimator(mHeaderLayout.getHeight(), minimumHeight).start();
                            }

                        }
                        else
                        {
                            if((initPositionY-currPositionY) < 0)
                            {
                                slideOn = false;
                                getAnimator(mHeaderLayout.getHeight(), mSlideWrapper.getHeight()).start();
                            }
                            else
                            {
                                slideOn = true;
                                getAnimator(mHeaderLayout.getHeight(), minimumHeight).start();
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    private ValueAnimator getAnimator(int from, int to)
    {
        ValueAnimator va = ValueAnimator.ofInt(from, to);
        va.setDuration(ANIMATION_DURATION);
        va.setInterpolator(new DecelerateInterpolator());
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer)animation.getAnimatedValue();
                mHeaderLayout.getLayoutParams().height = value.intValue();
                mHeaderLayout.requestLayout();
            }
        });
        return va;
    }

    public int getPixelFromDp(int dps)
    {
        float scale = getBaseContext().getResources().getDisplayMetrics().density;
        return (int) (dps * scale + 0.5f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
