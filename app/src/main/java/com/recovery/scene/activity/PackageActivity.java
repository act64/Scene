package com.recovery.scene.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.recovery.scene.R;
import com.recovery.scene.model.ProductItemPOJO;

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
    ProductAdapter productAdapter;
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContet(R.layout.activity_package);
        lv= (ListView) findViewById(R.id.lv_package);
        // TODO: 2017/4/20 测试效果
        ArrayList<ProductItemPOJO>productItemPOJOs=new ArrayList<>();
        for (int i=0;i<50;i++) {
            ProductItemPOJO productItemPOJO = new ProductItemPOJO();
            productItemPOJO.setCode("safasfasf");
            productItemPOJO.setOwner("王大毛"+i);
            productItemPOJO.setProductName("鸭蛋");
            productItemPOJO.setTime(System.currentTimeMillis());
            productItemPOJOs.add(productItemPOJO);
        }
        productAdapter=new ProductAdapter(this,R.layout.item_package_product,productItemPOJOs);
        lv.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();

    }

    private class ProductAdapter extends ArrayAdapter<ProductItemPOJO> {
        public ProductAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<ProductItemPOJO> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder=null;
            if (convertView==null){
                convertView= LayoutInflater.from(PackageActivity.this).inflate(R.layout.item_package_product,parent,false);
                 viewHolder=new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.setValue(getItem(position));
            return convertView;
        }

        private  class ViewHolder{
            private TextView tvCode;
            private TextView tvTime;
            private TextView tvProduceName;
            private TextView tvState;
            private TextView tvOwner;

            public ViewHolder(View v){
                tvCode= (TextView) v.findViewById(R.id.tv_value_code);
                tvProduceName= (TextView) v.findViewById(R.id.tv_value_productname);
                tvOwner= (TextView) v.findViewById(R.id.tv_value_bussinessman);
                tvState= (TextView) v.findViewById(R.id.tv_state);
                tvTime= (TextView) v.findViewById(R.id.tv_time);
            }

            public void setValue(ProductItemPOJO productItemPOJO){
                tvCode.setText(productItemPOJO.getCode());
                tvTime.setText(simpleDateFormat.format(new Date(productItemPOJO.getTime())));
                tvOwner.setText(productItemPOJO.getOwner());
                tvState.setText(productItemPOJO.getState());
                tvProduceName.setText(productItemPOJO.getProductName());
            }
        }
    }
}
