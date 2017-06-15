package hotjavi.lei.com.base_module.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import hotjavi.lei.com.base_module.R;
import hotjavi.lei.com.base_module.widget.CustomToolBar;

/**
 * Created by tom on 2017/4/11.
 */

public class BaseSetMainActivity extends BaseTopActivity {
    CustomToolBar customToolBar;
    FrameLayout mainContent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        customToolBar= (CustomToolBar) findViewById(R.id.custom_titletoolbar);
        mainContent= (FrameLayout) findViewById(R.id.content_main);
        iniBackVisable();
        mainContent.requestFocus();

    }

    public void setMainContet(@LayoutRes int layoutId){
        LayoutInflater.from(this).inflate(layoutId,mainContent,true);
    }

    public CustomToolBar getCustomToolBar() {
        return customToolBar;
    }

    @Override
    public void setTitle(CharSequence title) {
        getCustomToolBar().setTitle(title);
    }

    protected void iniBackVisable(){
        getCustomToolBar().setBackVisble(true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}
