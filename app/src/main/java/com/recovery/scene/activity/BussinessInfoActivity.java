package com.recovery.scene.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.recovery.netwrok.apiservice.ApiService;
import com.recovery.netwrok.model.SellerInfo;
import com.recovery.netwrok.subscriber.ApiSubscriber;
import com.recovery.scene.R;
import com.recovery.scene.utils.ImageLoadManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import hotjavi.lei.com.base_module.present.BaseObjectPresent;

/**
 * Created by tom on 2017/4/20.
 */

public class BussinessInfoActivity extends BaseDeviceFuncActivity {
    LinearLayout llPics;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_register_time)
    TextView tvRegisterTime;
    @BindView(R.id.tv_postion)
    TextView tvPostion;
    @BindView(R.id.tv_warn)
    TextView tvWarn;
    @BindView(R.id.tv_sell_item)
    TextView tvSellItem;
    @BindView(R.id.tv_last_trade)
    TextView tvLastTrade;
    private boolean isFirst = true;

    private BDLocation bdLocation;
    private Present mPresent = new Present(this);
    ;

    @Override
    protected void onBdLocationFind(BDLocation bdLocatio) {
        bdLocation = bdLocatio;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContet(R.layout.activity_bussiness_info);
        ButterKnife.bind(this);
        getCustomToolBar().setTitle("商户信息");
        llPics = (LinearLayout) findViewById(R.id.ll_pics);
        showLoading("刷卡查询商户信息");
//        createImage("http://placekitten.com/500/300");
//        createImage("http://placekitten.com/300/500");

    }

    @Override
    protected void onNFCIDRead(String str) {
        if (isFirst) {
            hideLoading();
            isFirst = false;
        }
        if (!isLoading()) {
            showLoading("正在查询商户信息");
            mPresent.queryInfo(str,bdLocation);
        }
    }

    private void setSellerInfo(SellerInfo sellerInfo) {
        tvName.setText(sellerInfo.getName());
        tvLastTrade.setText(sellerInfo.getLastTrade());
        tvPostion.setText(sellerInfo.getRegAddr());
        tvSex.setText(sellerInfo.getSex());
        if (TextUtils.isEmpty(sellerInfo.getWarn())) {
            tvWarn.setVisibility(View.GONE);
        }else {
            tvWarn.setVisibility(View.VISIBLE);

        }
        tvWarn.setText(sellerInfo.getWarn());
        tvRegisterTime.setText(sellerInfo.getRegDate());
        tvSellItem.setText(sellerInfo.getMainBiz());
        llPics.removeAllViews();
        if (sellerInfo.getImages() != null) {
            for (int i = 0; i < sellerInfo.getImages().size(); i++) {
                createImage(sellerInfo.getImages().get(i));
            }
        }
    }

    private void createImage(String url) {
        ImageView iv = new ImageView(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 30);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        llPics.addView(iv);
        ImageLoadManager.loadImageByDefaultImage(url, iv, R.drawable.farmer_bg_select_image);

    }

    private static class Present extends BaseObjectPresent<BussinessInfoActivity> {
        public Present(BussinessInfoActivity bussinessInfoActivity) {
            super(bussinessInfoActivity);
        }

        public void queryInfo(String id, BDLocation location) {
            Double lantitude;
            Double longitude;
            if (location == null) {
                lantitude = null;
                longitude = null;
            } else {
                lantitude = location.getLatitude();
                longitude = location.getLongitude();
            }
            ApiService.getSellerInfo(id, lantitude, longitude).subscribe(new ApiSubscriber<SellerInfo>() {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if (!isAvaiable()) return;
                    getRefObj().hideLoading();

                }

                @Override
                public void onNext(SellerInfo sellerInfo) {
                    super.onNext(sellerInfo);
                    if (!isAvaiable()) return;
                    getRefObj().hideLoading();
                    if (sellerInfo == null) return;
                    getRefObj().setSellerInfo(sellerInfo);
                }
            });
        }
    }

}
