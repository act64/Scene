package com.recovery.scene.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.recovery.netwrok.apiservice.ApiService;
import com.recovery.netwrok.model.CollectRecordInfo;
import com.recovery.netwrok.model.TagStateInfo;
import com.recovery.netwrok.subscriber.ApiSubscriber;
import com.recovery.scene.R;
import com.recovery.scene.utils.APPUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hotjavi.lei.com.base_module.activity.BaseSetMainActivity;
import hotjavi.lei.com.base_module.present.BaseObjectPresent;

/**
 * Created by tom on 2017/4/25.
 */

public class ActivityCollectOutLand extends BaseSetMainActivity {
    @BindView(R.id.spiner_seller)
    Spinner spinerSeller;
    @BindView(R.id.spiner_product)
    Spinner spinerProduct;
    @BindView(R.id.tv_counts)
    EditText tvCounts;
    @BindView(R.id.spiner_units)
    Spinner spinerUnits;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_timess)
    TextView tvTimess;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.et_code_input)
    EditText etCodeInput;
    private String mark;
    private TagStateInfo mtagStateInfo;

    private static HashMap<String,String> sellerMaps=new HashMap<>();
    static {
        sellerMaps.put("金满园","9f4e0380fc1f013446f30bbdb1217378");
        sellerMaps.put("新疆农垦","9fe10880fc1f013446f30bbdb1217378");
        sellerMaps.put("莫干山菇业","9fbdb0f0fc1f013446f30bbdb1217378");
        sellerMaps.put("谷舞仁生农业","4e8b9c38298b11e7b4d700163e0c842d");

    }
Timer timer;

    private class LoadingViewHolde {

        private final View itemView;

        public LoadingViewHolde() {
            ViewGroup parentView = (ViewGroup) findViewById(android.R.id.content);
            itemView = LayoutInflater.from(ActivityCollectOutLand.this)
                    .inflate(R.layout.item_loading, parentView,false);
            parentView.addView(itemView);
            itemView.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                    etCodeInput.requestFocus();
                }
            });
        }

        public boolean isShowing(){
            return itemView.getVisibility()==View.VISIBLE;
        }

        public void show() {
            itemView.setVisibility(View.VISIBLE);
        }

        public void hide() {
            itemView.setVisibility(View.GONE);
        }
    }
    LoadingViewHolde loadingVIew;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContet(R.layout.activity_colletland);
        ButterKnife.bind(this);
        timer=new Timer();
        loadingVIew=new LoadingViewHolde();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTimess.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    }
                });
            }
        },0,1000);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, new ArrayList<>(sellerMaps.keySet()));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinerSeller.setAdapter(adapter);
        getCustomToolBar().setTitle("源品出库");
        etCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("12", s.toString());
                if (TextUtils.isEmpty(s)) return;
                if (s.toString().contains("\n")) {
                    mark=s.toString().replace("\n","");
                    mark = APPUtils.getTagCode(mark);
                    if (mark != null) {
                        queryCode(mark);
//                        Toast.makeText(PackageLandActivity.this, mark, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ActivityCollectOutLand.this, "二维码非法", Toast.LENGTH_SHORT).show();
                    }
                    etCodeInput.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etCodeInput.requestFocus();

    }

    private void queryCode(String mark) {
        loadingVIew.show();
        new Present(this).queryCode(mark);
    }

    private void collectProduct(){
        if (TextUtils.isEmpty(tvCounts.getText())){
            Toast.makeText(this,"请输入数量",Toast.LENGTH_SHORT).show();
            return ;

        }
        if (!tvCounts.getText().toString().matches("[1-9]\\d*(\\.\\d+)?")) {
            Toast.makeText(this, "数量必须为有效的整数或者小数，不能以0开头", Toast.LENGTH_SHORT).show();
            return ;
        }
        if (mtagStateInfo==null){
            Toast.makeText(this, "必须要有标签码", Toast.LENGTH_SHORT).show();
            return ;
        }


        CollectRecordInfo collectRecord=new CollectRecordInfo();
        collectRecord.setVendorId(sellerMaps.get(spinerSeller.getSelectedItem()));
        collectRecord.setImages(null);
        collectRecord.setProduct((String) spinerProduct.getSelectedItem());
        collectRecord.setUnitIds(null);
        collectRecord.setUnit((String) spinerUnits.getSelectedItem());
        collectRecord.setTagId(mtagStateInfo.getCodeId());
        collectRecord.setQuantity(Double.parseDouble(tvCounts.getText().toString()));
        new Present(this).saveCollectData(collectRecord);
        showLoading("保存中");
    }

    @Override
    protected void changeFocus() {
        etCodeInput.requestFocus();
    }

    @OnClick(R.id.btn_save)
    public void onViewClicked() {
        collectProduct();
    }

    @Override
    protected void hideLoading() {
        super.hideLoading();
        loadingVIew.hide();
    }

    private static class Present extends BaseObjectPresent<ActivityCollectOutLand> {
        public Present(ActivityCollectOutLand activityCollectOutLand) {
            super(activityCollectOutLand);
        }
        public void queryCode(final String code){
            ApiService.tagqueryState(code).subscribe(new ApiSubscriber<TagStateInfo>() {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if (!isAvaiable()) return;
                    getRefObj().hideLoading();
                }

                @Override
                public void onNext(TagStateInfo tagStateInfo) {
                    super.onNext(tagStateInfo);
                    if (!isAvaiable()) return;
                    getRefObj().hideLoading();
                    if (tagStateInfo != null) {
                        tagStateInfo.setCodeId(code);
                        getRefObj().useTagStateInfo(tagStateInfo);
                    }
                }
            });
        }

        public void saveCollectData(CollectRecordInfo collectRecordInfo){
            ApiService.savePurchase(collectRecordInfo).subscribe(new ApiSubscriber<Object>(){
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if (!isAvaiable())return;
                    getRefObj().hideLoading();
                }

                @Override
                public void onNext(Object o) {
                    super.onNext(o);
                    if (!isAvaiable())return;
                    getRefObj().hideLoading();
                    Toast.makeText(getRefObj(),"保存成功",Toast.LENGTH_SHORT).show();
                    getRefObj().finish();
                }
            });
        }
    }

    private void useTagStateInfo(TagStateInfo tagStateInfo) {
        if (tagStateInfo.getState()==0){
            mtagStateInfo=tagStateInfo;
            tvCode.setText(tagStateInfo.getCodeId());
        }else {
            Toast.makeText(this,"必须是未使用的码",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer=null;
    }
}
