package com.recovery.scene.utils;

import android.graphics.Rect;
import android.view.TouchDelegate;
import android.view.View;

/**
 * Created by yangxiaolong on 15/9/17.
 */
public class WidgetUtil {

    public static void expandTouchArea(final View parentView, final View smallView, final int extraPadding) {
        parentView.post(new Runnable() {
            @Override
            public void run() {
                Rect rect = new Rect();
                smallView.getHitRect(rect);
                rect.top -= extraPadding;
                rect.left -= extraPadding;
                rect.right += extraPadding;
                rect.bottom += extraPadding;
                parentView.setTouchDelegate(new TouchDelegate(rect, smallView));
            }
        });
    }
}
