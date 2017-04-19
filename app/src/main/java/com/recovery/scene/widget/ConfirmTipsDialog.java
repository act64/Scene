package com.recovery.scene.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.recovery.scene.R;

/**
 * Created by yangxiaolong on 15/9/18.
 */
public class ConfirmTipsDialog extends Dialog {

    final Activity mContext;

    TextView tipsTV;

    TextView confirmTV;

    TextView cancelTV;

    String tips;
    String confirmTips;
    String cancelText;

    View.OnClickListener confirmListener;
    private Object tag;

    int cancelVisibility = View.VISIBLE;

    public ConfirmTipsDialog(Activity context, String tips, View.OnClickListener listener) {
        super(context, R.style.ProgressBarDialog);
        mContext = context;
        this.tips = tips;
        confirmListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().getAttributes().width = (int) (getContext().getResources().getDisplayMetrics().widthPixels * 0.6);
//        getWindow().getAttributes().height = getWindow().getAttributes().width;
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.3f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
        getWindow().setAttributes(lp);
        setContentView(R.layout.dialog_tips);

        confirmTV = (TextView) findViewById(R.id.confirmDel);
        cancelTV = (TextView) findViewById(R.id.cancelDel);
        tipsTV = (TextView) findViewById(R.id.tips);
        tipsTV.setText(tips);
        cancelTV.setVisibility(cancelVisibility);
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        if (!TextUtils.isEmpty(cancelText)) {
            cancelTV.setText(cancelText);
        }
        confirmTV.setTag(tag);
        if (!TextUtils.isEmpty(confirmTips))
            confirmTV.setText(confirmTips);
        confirmTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmListener.onClick(v);
                dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.width = (int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.6);
//        getWindow().getAttributes().height = getWindow().getAttributes().width;
        getWindow().getCallback().onWindowAttributesChanged(attrs);
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public void setConfirmText(String text) {
        confirmTips = text;
        if (confirmTV != null && !TextUtils.isEmpty(confirmTips)) {
            //onCreate之后调用
            confirmTV.setText(confirmTips);
        }
    }

    public void setCancelText(String text) {
        cancelText = text;
        if (confirmTV != null && !TextUtils.isEmpty(text)) {
            //onCreate之后调用
            cancelTV.setText(text);
        }
    }

    public void setCancelVisibility(int visibility) {
        cancelVisibility = visibility;
    }

}
