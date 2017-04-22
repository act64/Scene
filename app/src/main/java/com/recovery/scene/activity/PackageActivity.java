package com.recovery.scene.activity;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.recovery.scene.R;
import com.recovery.scene.model.ProductItemPOJO;
import com.recovery.scene.utils.APPUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hotjavi.lei.com.base_module.activity.BaseSetMainActivity;

/**
 * Created by tom on 2017/4/20.
 */

public class PackageActivity extends BaseSetMainActivity {

    ListView lv;
    View rlCode;
    ProductAdapter productAdapter;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private AlertDialog alertDialog;
    private TextView tvPackagecode;

    private static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    IntentFilter mSCTestIntentfilter = new IntentFilter(SCN_CUST_ACTION_SCODE);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContet(R.layout.activity_package);
        registerReceiver(mReceiver, mSCTestIntentfilter);
        lv = (ListView) findViewById(R.id.lv_package);
        rlCode = findViewById(R.id.rl_code);
        tvPackagecode= (TextView) findViewById(R.id.tv_package_code);
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
        // TODO: 2017/4/20 测试效果
        ArrayList<ProductItemPOJO> productItemPOJOs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ProductItemPOJO productItemPOJO = new ProductItemPOJO();
            productItemPOJO.setCode("safasfasf");
            productItemPOJO.setOwner("王大毛" + i);
            productItemPOJO.setProductName("鸭蛋");
            productItemPOJO.setTime(System.currentTimeMillis());
            productItemPOJOs.add(productItemPOJO);
        }
        productAdapter = new ProductAdapter(this, R.layout.item_package_product, productItemPOJOs);
        lv.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();

    }



    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        // 监听条码读取广播，并获得条码字符串
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String action = intent.getAction();
            Log.d("scan","##91 action="+action);
            if (action.equals(SCN_CUST_ACTION_SCODE)) {
                String code = intent.getStringExtra("scannerdata");
                code= APPUtils.getTagCode(code);
                if (code==null)return;
                if (alertDialog!=null &&alertDialog.isShowing()){
                    tvPackagecode.setText(code);
                    alertDialog.dismiss();
                }else {


                    for (int i=0;i<productAdapter.getCount();i++){
                        if (productAdapter.getItem(i).getCode().equals(code)){
                            return;
                        }
                    }
                    ProductItemPOJO productItemPOJO=new ProductItemPOJO();
                    productItemPOJO.setCode(code);
                    productItemPOJO.setOwner("王大毛code" );
                    productItemPOJO.setProductName("鸭蛋");
                    productItemPOJO.setTime(System.currentTimeMillis());
                    productAdapter.add(productItemPOJO);
                }
            }
        }

    };

    private class ProductAdapter extends ArrayAdapter<ProductItemPOJO> {
        public ProductAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ProductItemPOJO> objects) {
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

            public void setValue(ProductItemPOJO productItemPOJO) {
                tvCode.setText(productItemPOJO.getCode());
                tvTime.setText(simpleDateFormat.format(new Date(productItemPOJO.getTime())));
                tvOwner.setText(productItemPOJO.getOwner());
                tvState.setText(productItemPOJO.getState());
                tvProduceName.setText(productItemPOJO.getProductName());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
    }
}
