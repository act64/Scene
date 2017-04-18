package com.recovery.scene.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.recovery.scene.R;

public class FixedAspectRatioFrameLayout extends FrameLayout {
	private int mAspectRatioWidth;
	private int mAspectRatioHeight;

	public FixedAspectRatioFrameLayout(Context context) {
		super(context);
	}

	public FixedAspectRatioFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		Init(context, attrs);
	}

	public FixedAspectRatioFrameLayout(Context context, AttributeSet attrs,
                                       int defStyle) {
		super(context, attrs, defStyle);

		Init(context, attrs);
	}

	private void Init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.fixedaspectratio);

		mAspectRatioWidth = a.getInt(
				R.styleable.fixedaspectratio_aspectRatioWidth, 3);
		mAspectRatioHeight = a.getInt(
				R.styleable.fixedaspectratio_aspectRatioHeight, 2);

		a.recycle();
	}

	// **overrides**

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int originalWidth = MeasureSpec.getSize(widthMeasureSpec);

		int calculatedHeight = originalWidth * mAspectRatioHeight
				/ mAspectRatioWidth;

		super.onMeasure(MeasureSpec.makeMeasureSpec(originalWidth,
				MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
				calculatedHeight, MeasureSpec.EXACTLY));
	}
}