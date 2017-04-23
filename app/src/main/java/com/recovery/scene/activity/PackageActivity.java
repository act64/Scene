package com.recovery.scene.activity;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.recovery.netwrok.apiservice.ApiService;
import com.recovery.netwrok.model.PackCreateResultInfo;
import com.recovery.netwrok.model.PackageCreateInfo;
import com.recovery.netwrok.model.TagInfo;
import com.recovery.netwrok.model.TagStateInfo;
import com.recovery.netwrok.subscriber.ApiSubscriber;
import com.recovery.scene.R;
import com.recovery.scene.model.ProductItemPOJO;
import com.recovery.scene.utils.APPUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hotjavi.lei.com.base_module.activity.BaseSetMainActivity;
import hotjavi.lei.com.base_module.present.BaseObjectPresent;

/**
 * Created by tom on 2017/4/20.
 */

public class PackageActivity extends BaseSetMainActivity {

    ListView lv;
    View rlCode;
    ProductAdapter productAdapter;
    @BindView(R.id.tv_counts)
    TextView tvCounts;
    @BindView(R.id.btn_save)
    Button btnSave;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private AlertDialog alertDialog;
    private TextView tvPackagecode;

    private static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    IntentFilter mSCTestIntentfilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
    private Present present;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContet(R.layout.activity_package);
        ButterKnife.bind(this);
        registerReceiver(mReceiver, mSCTestIntentfilter);
        setTitle("打包");
        lv = (ListView) findViewById(R.id.lv_package);
        rlCode = findViewById(R.id.rl_code);
        tvPackagecode = (TextView) findViewById(R.id.tv_package_code);
        rlCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alertDialog == null) {
                    alertDialog = new AlertDialog.Builder(PackageActivity.this).setCancelable(true).setView(R.layout.item_waiting)
                            .setNegativeButton("取消输入", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.dismiss();
                                }
                            }).create();
                }
                alertDialog.show();
            }
        });
        productAdapter = new ProductAdapter(this, R.layout.item_package_product, new ArrayList<TagInfo>());
        lv.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();
         present=new Present(this);

    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        // 监听条码读取广播，并获得条码字符串
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            Log.d("scan", "##91 action=" + action);
            if (action.equals(SCN_CUST_ACTION_SCODE)) {
                String code = intent.getStringExtra("scannerdata");
                code = APPUtils.getTagCode(code);
                if (code == null) return;
                if (alertDialog != null && alertDialog.isShowing()) {
                    present.getCodeState(code);
                    alertDialog.dismiss();
                    showLoading("查询打包码状态中");
                } else {
                    for (int i = 0; i < productAdapter.getCount(); i++) {
                        if (productAdapter.getItem(i).getCodeID().equals(code)) {
                            Toast.makeText(PackageActivity.this,"该产品已经在列表中",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    present.getCodeInfo(code);
                    showLoading("查询产品中");


                }
            }
        }

    };
private int productCounts=0;
    private String codeID="";
    @OnClick(R.id.btn_save)
    public void onViewClicked() {
        if (TextUtils.isEmpty(codeID)){
            Toast.makeText(PackageActivity.this,"请输入打包码",Toast.LENGTH_SHORT).show();
            return;
        }
        if (productAdapter.getCount()==0){
            Toast.makeText(PackageActivity.this,"请扫码输入产品",Toast.LENGTH_SHORT).show();
            return;
        }
        savePackage();
    }

    private void savePackage() {
        PackageCreateInfo packageCreateInfo=new PackageCreateInfo();
        packageCreateInfo.setPackageCode(codeID);
        ArrayList<String> idLists=new ArrayList<>();
        for (int i=0;i<productAdapter.getCount();i++) {
            idLists.add(  productAdapter.getItem(i).getCodeID());
        }

        packageCreateInfo.setProductList(idLists);
        present.savePackage(packageCreateInfo);
        showLoading("保存打包信息中");
    }

    private class ProductAdapter extends ArrayAdapter<TagInfo> {
        public ProductAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<TagInfo> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(PackageActivity.this).inflate(R.layout.item_package_product, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.setValue(getItem(position));
            if (position == 0) {
                ObjectAnimator.ofFloat(convertView, "alpha", 0, 1).setDuration(500).start();
            }
            return convertView;
        }

        private class ViewHolder {
            private TextView tvCode;
            private TextView tvTime;
            private TextView tvProduceName;
            private TextView tvState;
            private TextView tvOwner;

            public ViewHolder(View v) {
                tvCode = (TextView) v.findViewById(R.id.tv_value_code);
                tvProduceName = (TextView) v.findViewById(R.id.tv_value_productname);
                tvOwner = (TextView) v.findViewById(R.id.tv_value_bussinessman);
                tvState = (TextView) v.findViewById(R.id.tv_value_state);
                tvTime = (TextView) v.findViewById(R.id.tv_time);
            }

            public void setValue(TagInfo productItemPOJO) {
                tvCode.setText(productItemPOJO.getCodeID());
                tvTime.setText(productItemPOJO.getCreatedAt());
                tvOwner.setText(productItemPOJO.getVendor());
                tvState.setText(productItemPOJO.getStateStr());
                tvProduceName.setText(productItemPOJO.getProduct());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    private void useTagStateInfo(TagStateInfo tagStateInfo) {
        if (tagStateInfo.getState()!=1){
            Toast.makeText(this,"扫描的打包码有误",Toast.LENGTH_SHORT).show();
            return;
        }
        codeID=tagStateInfo.getCodeId();
        tvPackagecode.setText(codeID);
    }

    private void useTagInfo(TagInfo tagInfo) {
        productAdapter.insert(tagInfo,0);
        lv.setSelection(0);
        productCounts++;
        tvCounts.setText(productCounts+"");
    }


    private static class Present extends BaseObjectPresent<PackageActivity> {
        public Present(PackageActivity packageActivity) {
            super(packageActivity);
        }

        public void getCodeState(final String code) {
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

        public void getCodeInfo(final String code) {
            ApiService.getTagInfo(code).subscribe(new ApiSubscriber<TagInfo>() {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if (!isAvaiable()) return;
                    getRefObj().hideLoading();

                }

                @Override
                public void onNext(TagInfo tagInfo) {
                    super.onNext(tagInfo);
                    if (!isAvaiable()) return;
                    getRefObj().hideLoading();
                    if (tagInfo!=null) {
                        tagInfo.setCodeID(code);
                        getRefObj().useTagInfo(tagInfo);
                    }
                }
            });
        }

        public void savePackage(PackageCreateInfo packageCreateInfo) {
            ApiService.packageCreate(packageCreateInfo).subscribe(new ApiSubscriber<PackCreateResultInfo>() {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if (!isAvaiable()) return;
                    getRefObj().hideLoading();


                }

                @Override
                public void onNext(PackCreateResultInfo packCreateResultInfo) {
                    super.onNext(packCreateResultInfo);
                    if (!isAvaiable()) return;
                    getRefObj().hideLoading();
                    Toast.makeText(getRefObj(), "打包成功", Toast.LENGTH_SHORT).show();
                    getRefObj().finish();
                }
            });
        }

    }

}
