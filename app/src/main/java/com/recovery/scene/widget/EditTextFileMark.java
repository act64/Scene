package com.recovery.scene.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

import com.recovery.scene.utils.APPUtils;

/**
 * Created by ylei on 2017/3/31.
 */

public class EditTextFileMark extends EditText {
    private String mText;

    public EditTextFileMark(Context context) {
        super(context);
        iniview();
    }

    public EditTextFileMark(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniview();
    }

    public EditTextFileMark(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        iniview();
    }

    private void iniview() {
//        setOnKeyListener(new OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode==KeyEvent.KEYCODE_DEL){
//                    if (maxEditableLength-1>=5) {
//                        maxEditableLength--;
//                        setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxEditableLength)});
//                    }
//                }
//                return false;
//            }
//        });
    }

    private int maxEditableLength = -1;

    public int getMaxEditableLength() {
        return maxEditableLength;
    }

    public void setMaxEditableLength(int maxEditableLength) {
        this.maxEditableLength = maxEditableLength;
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new MyInputConnect(super.onCreateInputConnection(outAttrs), true);
    }

    private class MyInputConnect extends InputConnectionWrapper {
        private int composing;
        private boolean del = false;
        private boolean composed = false;

        /**
         * Initializes a wrapper.
         * <p>
         * <p><b>Caveat:</b> Although the system can accept {@code (InputConnection) null} in some
         * places, you cannot emulate such a behavior by non-null {@link InputConnectionWrapper} that
         * has {@code null} in {@code target}.</p>
         *
         * @param target  the {@link InputConnection} to be proxied.
         * @param mutable set {@code true} to protect this object from being reconfigured to target
         *                another {@link InputConnection}.  Note that this is ignored while the target is {@code null}.
         */
        public MyInputConnect(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        private long lasttime = 0;
        private String mark;

        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            long currentTime = System.currentTimeMillis();
            if (TextUtils.isEmpty(mark)) {
                mark = text.toString();
                lasttime = currentTime;
                return false;
            } else if (currentTime - lasttime > 100) {
                mark = "";
            } else {
                mark += text.toString();
            }
          text=  APPUtils.getTagCode(mark);
            if (!TextUtils.isEmpty(text)) {
                return super.commitText(text, newCursorPosition);

            }
            return false;
        }


    }
}
