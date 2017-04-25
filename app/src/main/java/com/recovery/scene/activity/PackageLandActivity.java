package com.recovery.scene.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.recovery.netwrok.apiservice.ApiService;
import com.recovery.netwrok.commoninfo.UserInfoUtil;
import com.recovery.netwrok.model.PackCreateResultInfo;
import com.recovery.netwrok.model.PackageCreateInfo;
import com.recovery.netwrok.model.TagInfo;
import com.recovery.netwrok.model.TagStateInfo;
import com.recovery.netwrok.model.UserInfo;
import com.recovery.netwrok.subscriber.ApiSubscriber;
import com.recovery.scene.R;
import com.recovery.scene.utils.APPUtils;
import com.recovery.scene.widget.MyListView;

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
 * Created by tom on 2017/4/24.
 */

public class PackageLandActivity extends BaseSetMainActivity implements View.OnLongClickListener, AdapterView.OnItemLongClickListener {
    @BindView(R.id.et_class)
    EditText etClass;
    @BindView(R.id.et_person)
    TextView etPerson;
    @BindView(R.id.et_date)
    TextView etDate;
    @BindView(R.id.et_package)
    TextView etPackage;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.tv_counts)
    TextView tvCounts;
    @BindView(R.id.lv_package)
    MyListView lvPackage;
    @BindView(R.id.et_scanner)
    EditText etScanner;
    private long lasttime;
    private LoadingViewHolde loadingViewHolde;
    private ProductAdapter productAdapter;
    private int productCounts;
    private AlertDialog alter;

    @Override
    public boolean onLongClick(View v) {

        return false;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (productAdapter.getCount()>0&&position>0&&productAdapter.getCount()>position) {
            new AlertDialog.Builder(this).setTitle("是否删除此条记录?").setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    etScanner.requestFocus();
                }
            }).setPositiveButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    productAdapter.remove(productAdapter.getItem(position));
                    productCounts--;
                    etScanner.requestFocus();
                }
            }).show();
        }
        return false;
    }

    private class LoadingViewHolde {

        private final View itemView;

        public LoadingViewHolde() {
            ViewGroup parentView = (ViewGroup) findViewById(android.R.id.content);
            itemView = LayoutInflater.from(PackageLandActivity.this)
                    .inflate(R.layout.item_loading, parentView,false);
            parentView.addView(itemView);
            itemView.setVisibility(View.GONE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hide();
                    etScanner.requestFocus();
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

    @Override
    protected void changeFocus() {
        super.changeFocus();
        etScanner.requestFocus();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getCustomToolBar().setBackVisble(false, null);
        getCustomToolBar().setTitle("打包");
        getCustomToolBar().setLeftView(R.mipmap.ic_logout);
        getCustomToolBar().setBackVisble(true,null);
        getCustomToolBar().setRightText("源品出库");
        getCustomToolBar().setLeftClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alter = new AlertDialog.Builder(PackageLandActivity.this).setTitle("是否退出？").setPositiveButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfoUtil.putUserInfo(null);
                        startActivity(new Intent(PackageLandActivity.this,LoginActivity.class));
                        alter.dismiss();
                        finish();
                    }
                }).setNegativeButton("否", null).show();

            }
        });
        getCustomToolBar().setRightClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PackageLandActivity.this,ActivityCollectOutLand.class));
            }
        });
        setMainContet(R.layout.activity_package_land);
        ButterKnife.bind(this);
       etPerson.setText( UserInfoUtil.getUserInfo().getName());
        etDate.setText(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
        loadingViewHolde=new LoadingViewHolde();
        productAdapter=new ProductAdapter(this,R.layout.item_package_product,new ArrayList<TagInfo>());
        lvPackage.setAdapter(productAdapter);
        lvPackage.setOnItemLongClickListener(this);
        etScanner.requestFocus();
        etScanner.addTextChangedListener(new TextWatcher() {
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
                        Toast.makeText(PackageLandActivity.this, "二维码非法", Toast.LENGTH_SHORT).show();
                    }
                    etScanner.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) return;
                if (TextUtils.isEmpty(mark)) {

                }
            }
        });
        findViewById(R.id.rl_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingViewHolde.show();
                etScanner.requestFocus();
            }
        });
    }

    private String mark = "";

    private void queryCode(String strMark){
        if (loadingViewHolde.isShowing()) {
            new Present(this).getCodeState(strMark);
        }else {
         for (int i=0; i< productAdapter.getCount();i++){
             if (productAdapter.getItem(i).getCodeID().equals(strMark)){
                 Toast.makeText(this,"该产品已经在列表中",Toast.LENGTH_SHORT).show();
                 return;
             }
         }
            new Present(this).getCodeInfo(strMark);

            showLoading("正在查询");
        }
    }
    private TagStateInfo pageTageState;
   private void useTagStateInfo(TagStateInfo tagStateInfo){
       if (tagStateInfo.getState()==0){
           etPackage.setText(tagStateInfo.getCodeId());
           pageTageState=tagStateInfo;
       }else {
           Toast.makeText(PackageLandActivity.this,"必须使用未使用的打包码",Toast.LENGTH_SHORT).show();
       }
       loadingViewHolde.hide();
       etScanner.requestFocus();


   }

    @Override
    protected void hideLoading() {
        super.hideLoading();
        changeFocus();
    }

    private void useTagInfo (TagInfo tagInfo){
       productAdapter.insert(tagInfo,0);
       lvPackage.setSelection(0);
       productCounts++;
       tvCounts.setText(productCounts+"");
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
                convertView = LayoutInflater.from(PackageLandActivity.this).inflate(R.layout.item_package_product, parent, false);
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


    private static class Present extends BaseObjectPresent<PackageLandActivity> {
        public Present(PackageLandActivity packageLandActivity) {
            super(packageLandActivity);
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
                    getRefObj().productAdapter.clear();
                    getRefObj().pageTageState=null;
                    getRefObj().productCounts=0;
                    getRefObj().etPackage.setText("点击扫描打包码");
                    getRefObj().tvCounts.setText("0");
                }
            });
        }

    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_ENTER){
            etScanner.requestFocus();
        }
        return super.onKeyDown(keyCode, event);

    }

    @OnClick(R.id.btn_save)
    public void onViewClicked() {

        if (productCounts==0){
            return;
        }
        if (pageTageState==null){
            return;
        }
        PackageCreateInfo packageCreateInfo=new PackageCreateInfo();
        packageCreateInfo.setPackageCode(pageTageState.getCodeId());
        ArrayList<String> ids=new ArrayList<>();
        for (int i=0;i<productAdapter.getCount();i++){
            ids.add(productAdapter.getItem(i).getCodeID());
        }
        packageCreateInfo.setProductList(ids);
        packageCreateInfo.setClassGroup(etClass.getText().toString());
        new Present(this).savePackage(packageCreateInfo);
    }
}
