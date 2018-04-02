package com.commai.commaplayer.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by fanqi on 2018/4/2.
 * Description:可控制viewpager是否可以滑动切换
 */

public class ControlViewPager extends ViewPager {

    public boolean canScrollAble=true;

    public ControlViewPager(Context context) {
        super(context);
    }

    public ControlViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setVpScrollAble(boolean scrollAble){
        this.canScrollAble=scrollAble;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return canScrollAble && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return canScrollAble && super.onInterceptTouchEvent(ev);
    }
}
