package hotjavi.lei.com.base_module.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import hotjavi.lei.com.base_module.R;

/**
 * Created by tom on 2017/4/11.
 */

public class BaseTopActivity extends FragmentActivity {

    private AlertDialog BaseAlertDialog;
    private View v;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                changeFocus();
                HideSoftInput(view.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    protected void  changeFocus(){}
    // 判定是否需要隐藏
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top
                    && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
    // 隐藏软键盘
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void showLoading(String text){
         v= LayoutInflater.from(this).inflate(R.layout.item_waiting,null);
        if (!TextUtils.isEmpty(text)){
            ( (TextView) v.findViewById(R.id.tv_dialog_title)).setText(text);
            if (BaseAlertDialog!=null){
                ( (TextView) BaseAlertDialog.findViewById(R.id.tv_dialog_title)).setText(text);
            }
        }
        if (BaseAlertDialog == null) {
            BaseAlertDialog = new AlertDialog.Builder(this).setCancelable(true).setView(v)
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            BaseAlertDialog.dismiss();
                        }
                    }).create();
        }
        if (!BaseAlertDialog .isShowing()) {
            BaseAlertDialog.show();
        }
    }
    protected void hideLoading(){
        if (BaseAlertDialog!=null&&BaseAlertDialog.isShowing()){
            BaseAlertDialog.dismiss();
        }
    }

    protected boolean isLoading(){
        if (BaseAlertDialog!=null&&BaseAlertDialog.isShowing()){
            return true;
        }return false;
    }

}
