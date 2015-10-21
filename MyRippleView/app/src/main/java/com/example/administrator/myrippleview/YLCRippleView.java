package com.example.administrator.myrippleview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

public class YLCRippleView extends RelativeLayout {
    public YLCRippleView(Context context) {
        super(context);
    }
    private View rippleView;
    private int iRecordX = 0;
    private int iRecordy = 0;
    public YLCRippleView(final Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                iRecordX = (int)event.getX();
                iRecordy = (int) event.getY();

                if(rippleView != null){
                    removeView(rippleView);
                    rippleView = null;
                }
                if(obj1 != null){
                    obj1.cancel();
                    obj1 = null;
                }
                if(obj2 != null){
                    obj2.cancel();
                    obj2 = null;
                }

                if(rippleView == null){
                    rippleView = new View(getContext());
                    rippleView.setClickable(false);
                    rippleView.setBackgroundResource(R.drawable.ripple_bg);
                    YLCRippleView.this.addView(rippleView);

                    RelativeLayout.LayoutParams params = (LayoutParams) rippleView.getLayoutParams();
                    params.width = 80;
                    params.height = 80;
                    rippleView.setLayoutParams(params);
                    rippleView.setX(iRecordX);
                    rippleView.setY(iRecordy);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                int x = (int)event.getX();
                int y = (int) event.getY();

                int deltax = x - iRecordX;
                int deltay = y- iRecordy;
                if(rippleView != null){
                    rippleView.setX((int) (rippleView.getX() + deltax));
                    rippleView.setY((int) (rippleView.getY() + deltay));
                }

                iRecordX = x;
                iRecordy = y;
                break;

            case MotionEvent.ACTION_UP:

                if(isInViewRect(rippleView, event.getRawX(), event.getRawY())){
                    int max = Math.max(getWidth(),getHeight());
                    int scaleTT = 2*(max / 80 +1);
                    obj1 = ObjectAnimator.ofFloat(rippleView,"scaleX",1,scaleTT);
                    obj2 = ObjectAnimator.ofFloat(rippleView,"scaleY",1,scaleTT);
                    obj1.setTarget(rippleView);
                    obj1.setInterpolator(new AccelerateDecelerateInterpolator());
                    obj1.setDuration(400);
                    obj2.setTarget(rippleView);
                    obj2.setInterpolator(new AccelerateDecelerateInterpolator());
                    obj2.setDuration(400);
                    AnimatorSet set = new AnimatorSet();
                    set.playTogether(obj1,obj2);
                    set.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(rippleView != null){
                                rippleView.setVisibility(View.GONE);
                                removeView(rippleView);
                                rippleView = null;
                            }
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    set.start();
                }
                break;
        }
        super.onTouchEvent(event);
        return true;
    }

    private boolean isInViewRect(View view, float x, float y){
        int[] ll = new int[2];
        getLocationInWindow(ll);
        if(ll[0] <  x && ll[1] < y && (ll[0]+getWidth()) > x&& (ll[1]+getHeight())>y){
            return true;
        }
        else{
            return false;
        }
    }

    private  ObjectAnimator obj1;
    private  ObjectAnimator obj2;
    public YLCRippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int getPx(int idp) {
        DisplayMetrics ccc = new DisplayMetrics();
        ((Activity) getContext())
                .getWindowManager().getDefaultDisplay().getMetrics(ccc);
        return (int) (ccc.density * (float)idp + 0.5f);
    }
}
