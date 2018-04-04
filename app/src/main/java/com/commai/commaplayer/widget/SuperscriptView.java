package com.commai.commaplayer.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

public class SuperscriptView extends AppCompatTextView {
	private float mDegress,mX,mY;
	private int mHeight,mWidth;
	
	public SuperscriptView(Context context) {
		super(context);
		init(context, null);
	}

	public SuperscriptView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SuperscriptView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}
	@Override
	public void setVisibility(int visibility) {
		setAnimation(visibility == View.VISIBLE? mAnimation: null);
		super.setVisibility(visibility);
	}

	private void init(Context context, AttributeSet attrs) {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int topEdge = Math.round(TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 41.333f, dm));
		int  leftEdge= Math.round(TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, 41.333f, dm));
		calc(leftEdge, topEdge);

		mAnimation.setFillBefore(true);
		mAnimation.setFillAfter(true);
		mAnimation.setFillEnabled(true);
		startAnimation(mAnimation);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (mHeight < 1 || mWidth < 1) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		} else {
			setMeasuredDimension(mWidth, mHeight);
		}
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		int vi= getVisibility();
		setAnimation(null);
		setVisibility(vi);
	}

	/**
	 * 计算旋转的角度
	 * @param leftEdge
	 * @param topEdge
	 */
	private void calc(int leftEdge, int topEdge) {
		final double ab = Math.sqrt(Math.pow(topEdge, 2d)+ Math.pow(leftEdge, 2d));
		final double sinB = leftEdge / ab;
		mDegress = -(float) Math.toDegrees(Math.asin(sinB));

		mHeight = Math.round((float) (sinB * topEdge));

		final double de = sinB * leftEdge;

		mX = -(float) ((topEdge / ab) * de);

		mY = (float) (sinB * de);
		mWidth = Math.round((float) ab);
	}

	/**
	 * 设置可见和不可见时的动画
	 */
	private Animation mAnimation = new Animation() {
		@Override
		protected void applyTransformation(float interpolatedTime, Transformation t) {
			if (mHeight < 1 || mWidth < 1) {
				return;
			}
			Matrix tran = t.getMatrix();
			tran.setTranslate(mX, mY);
			tran.postRotate(mDegress, mX, mY);
		}
	};
}
