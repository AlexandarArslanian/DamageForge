package com.example.damageforge;

import android.content.Context;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

    private final Context context;
    private final Class<?> leftActivity;
    private final Class<?> rightActivity;

    public SwipeGestureListener(Context context, Class<?> leftActivity, Class<?> rightActivity) { //to be used to tell the Activity where to go
        this.context = context;
        this.leftActivity = leftActivity;
        this.rightActivity = rightActivity;
    }

    //controlling the swipe gesture trigger
    private static final int SWIPE_THRESHOLD = 200;
    private static final int SWIPE_VELOCITY_THRESHOLD = 200;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffX = e2.getX() - e1.getX();

        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
            if (diffX > 0 && leftActivity != null) {
                // Swipe right
                context.startActivity(new Intent(context, leftActivity));
                ((android.app.Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            } else if (diffX < 0 && rightActivity != null) {
                // Swipe left
                context.startActivity(new Intent(context, rightActivity));
                ((android.app.Activity) context).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
            return true;
        }
        return false;
    }
}
