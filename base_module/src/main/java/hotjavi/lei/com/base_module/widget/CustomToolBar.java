package hotjavi.lei.com.base_module.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import hotjavi.lei.com.base_module.R;

/**
 * Created by tom on 2017/4/11.
 */

public class CustomToolBar extends Toolbar {
    private View mView;
//    right aera
    private View rlRight;
    //title
    private TextView tvTitle;
    //back icon
    private ImageView ivLeft;
//    right icon
    private ImageView ivRight;
//    right text
    private TextView tvRight;

    public CustomToolBar(Context context) {
        super(context);
        initView(context, null);
    }

    public CustomToolBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CustomToolBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.view_custom_toolbar, null);
        rlRight = mView.findViewById(R.id.title_rl_right);
        tvTitle= (TextView) mView.findViewById(R.id.tv_title);
        ivLeft = (ImageView) mView.findViewById(R.id.iv_back);
        ivRight= (ImageView) mView.findViewById(R.id.iv_right);
        tvRight= (TextView) mView.findViewById(R.id.tv_right);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomToolBar);
        if (typedArray != null) {
            String titleStr = typedArray.getString(R.styleable.CustomToolBar_custom_title);
            String titleRightStr = typedArray.getString(R.styleable.CustomToolBar_right_text);
            typedArray.recycle();
            if (!TextUtils.isEmpty(titleStr)){
                tvTitle.setText(titleStr);
            }
            if (!TextUtils.isEmpty(titleRightStr)){
                tvRight.setText(titleRightStr);
                tvRight.setVisibility(VISIBLE);
                ivRight.setVisibility(GONE);
            }
        }

        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, Gravity.CENTER_HORIZONTAL);
        addView(mView, lp);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        tvTitle.setText(resId);

    }

    @Override
    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    /**
     * whether the back icon visable
     * @param backVisble
     * @param onClickListener
     */
    public void setBackVisble(boolean backVisble,OnClickListener onClickListener){
        if (backVisble){
            ivLeft.setVisibility(VISIBLE);
            if (onClickListener!=null) {
                ivLeft.setOnClickListener(onClickListener);
            }
        }else {
            ivLeft.setVisibility(GONE);
        }
    }

    /**
     * set OnclickListener for right area
     * @param onClickListener
     */
    public void setRightClick(OnClickListener onClickListener){
        rlRight.setOnClickListener(onClickListener);
    }
    /**
     * set OnclickListener for left area
     * @param onClickListener
     */
    public void setLeftClick(OnClickListener onClickListener){
        ivLeft.setOnClickListener(onClickListener);
    }

    /**
     * set right text and make right icon gone
     * @param text
     */
    public void setRightText(String text){
        tvRight.setText(text);
        tvRight.setVisibility(VISIBLE);
        ivRight.setVisibility(GONE);
    }

    /**
     * set right icon and make right text gone
     * @param resId
     */
    public void setRightIcon(@DrawableRes int resId){
        ivRight.setImageResource(resId);
        tvRight.setVisibility(GONE);
        ivRight.setVisibility(VISIBLE);
    }

    public void setLeftView(@DrawableRes int resId){
        ivLeft.setImageResource(resId);
    }

    public View getRlRight() {
        return rlRight;
    }
}
