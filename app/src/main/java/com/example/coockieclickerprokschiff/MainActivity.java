package com.example.coockieclickerprokschiff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.os.Bundle;
import android.util.AtomicFile;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import pl.droidsonroids.gif.GifImageView;

import static androidx.dynamicanimation.animation.SpringForce.DAMPING_RATIO_HIGH_BOUNCY;
import static androidx.dynamicanimation.animation.SpringForce.DAMPING_RATIO_LOW_BOUNCY;
import static androidx.dynamicanimation.animation.SpringForce.STIFFNESS_LOW;

public class MainActivity extends AppCompatActivity {
TextView count;
ImageView cookie;
int addCount = 0;
ImageView upgrade;
ConstraintLayout layout;
AtomicInteger countNum;
boolean ifUpgradeUnlocked = false;
boolean canBuy = false;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.9f,1.0f,0.9f,1.0f, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(300);
        PassiveIncomeThread i = new PassiveIncomeThread();
        i.start();
        count = findViewById(R.id.count);
        upgrade = findViewById(R.id.upgradeImage);
        upgrade.setEnabled(false);
        upgrade.setAlpha(0.5f);
        final SpringAnimation springAnim = new SpringAnimation(upgrade, DynamicAnimation.TRANSLATION_Y, 100f);
        SpringForce force = new SpringForce();
        force.setDampingRatio(DAMPING_RATIO_HIGH_BOUNCY).setStiffness(STIFFNESS_LOW);
        force.setFinalPosition(-100f);
        springAnim.setSpring(force);
        springAnim.addEndListener(new DynamicAnimation.OnAnimationEndListener() {
            @Override
            public void onAnimationEnd(DynamicAnimation animation, boolean canceled, float value, float velocity) {
                final SpringAnimation sAnim = new SpringAnimation(upgrade, DynamicAnimation.TRANSLATION_Y, -100f);
                SpringForce force = new SpringForce();
                force.setDampingRatio(DAMPING_RATIO_HIGH_BOUNCY).setStiffness(STIFFNESS_LOW);
                force.setFinalPosition(20f);
                sAnim.setSpring(force);
                sAnim.start();
            }
        });
        cookie = findViewById(R.id.cookie);
        layout = findViewById(R.id.layout);
        countNum = new AtomicInteger(0);


        cookie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(scaleAnimation);
                count.bringToFront();
                countNum.getAndAdd(1);
                count.setText("Count: "+countNum);
                makePlusOne();
                if(countNum.get()>=30) {
                    ifUpgradeUnlocked = true;
                    Log.d("Test","New Upgrade Unlocked");


                }
            }
        });

        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                springAnim.start();
                makeUpgrade();

            }
        });

    }

    public void makeUpgrade()
    {
        if(countNum.get()>=30) {
            countNum.getAndAdd(-30);
            addCount++;


            final GifImageView plusOne = new GifImageView(MainActivity.this);
            float rNumx = (float) (Math.random() * 1200);
            float rNumy = ((float) (Math.random() * 500)+650);
            AlphaAnimation fadeIn = new AlphaAnimation(0f, 1.0f);
            fadeIn.setDuration(2000);
            plusOne.setAnimation(fadeIn);
            plusOne.setId(View.generateViewId());
            plusOne.setImageResource(R.drawable.grand);
            plusOne.setY(-rNumy);
            plusOne.setX(rNumx - 600);

            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

            plusOne.setLayoutParams(layoutParams);
            layout.addView(plusOne);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(layout);
            constraintSet.connect(plusOne.getId(), ConstraintSet.TOP, cookie.getId(), constraintSet.TOP);
            constraintSet.connect(plusOne.getId(), ConstraintSet.BOTTOM, cookie.getId(), constraintSet.BOTTOM);
            constraintSet.connect(plusOne.getId(), ConstraintSet.LEFT, cookie.getId(), constraintSet.LEFT);
            constraintSet.connect(plusOne.getId(), ConstraintSet.RIGHT, cookie.getId(), constraintSet.RIGHT);
            constraintSet.applyTo(layout);
            plusOne.animate().rotationBy(360f).setDuration(2000);
            plusOne.getLayoutParams().height = 250;
        }

    }
    public class PassiveIncomeThread extends Thread {
        @Override
        public void run() {
            Timer t = new Timer();
            t.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (countNum.get() >=30)
                    {
                        upgrade.setEnabled(true);
                        upgrade.setAlpha(1.0f);

                    }
                    else if(countNum.get()<0)
                    {
                        upgrade.setEnabled(false);
                        upgrade.setAlpha(0.5f);

                    }
                    if (countNum.get() <30)
                    {
                        upgrade.setEnabled(false);
                        upgrade.setAlpha(0.5f);

                    }
                    countNum.getAndAdd(addCount);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            count.setText("Count: " + String.valueOf(countNum.get()));
                        }
                    });

                    Log.d("Tag-1", String.valueOf(layout.getChildCount()));

                }
            }, 0, 1000);

        }
    }
    public void makePlusOne()
    {
        final TextView plusOne = new TextView(MainActivity.this);
        plusOne.setId(View.generateViewId());
        plusOne.setTextSize(24);
        plusOne.setText("+1");

        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

        plusOne.setLayoutParams(layoutParams);
        layout.addView(plusOne);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(plusOne.getId(), ConstraintSet.TOP, cookie.getId(), constraintSet.TOP);
        constraintSet.connect(plusOne.getId(), ConstraintSet.BOTTOM, cookie.getId(), constraintSet.BOTTOM);
        constraintSet.connect(plusOne.getId(), ConstraintSet.LEFT, cookie.getId(), constraintSet.LEFT);
        constraintSet.connect(plusOne.getId(), ConstraintSet.RIGHT, cookie.getId(), constraintSet.RIGHT);
        constraintSet.setHorizontalBias(plusOne.getId(), (float)(Math.random()));
        constraintSet.applyTo(layout);

        TranslateAnimation translateAnimation = new TranslateAnimation(plusOne.getX(), plusOne.getX(), cookie.getHeight()/4, -1*cookie.getHeight()/4);
        translateAnimation.setDuration(1000);
        plusOne.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                layout.removeView(plusOne);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



    }
}