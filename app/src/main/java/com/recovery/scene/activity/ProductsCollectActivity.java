package com.recovery.scene.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.recovery.netwrok.apiservice.ApiService;
import com.recovery.netwrok.model.CollectRecordInfo;
import com.recovery.netwrok.model.ImageUploadInfo;
import com.recovery.netwrok.model.SellerInfo;
import com.recovery.netwrok.model.TagStateInfo;
import com.recovery.netwrok.model.UnitsInfo;
import com.recovery.netwrok.subscriber.ApiSubscriber;
import com.recovery.scene.R;
import com.recovery.scene.widget.MultiSelectSpinner;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hotjavi.lei.com.base_module.present.BaseObjectPresent;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by tom on 2017/4/18.
 */

public class ProductsCollectActivity extends BaseDeviceFuncActivity implements View.OnClickListener {
    private static final int THUMBNAIL_ACTIVITY = 0;
    private static final int DELETE_ACTIVITY = 11;
    @BindView(R.id.tv_vendorname)
    TextView tvVendorname;
    @BindView(R.id.et_product)
    EditText etProduct;
    @BindView(R.id.et_count)
    EditText etCount;
    @BindView(R.id.spinner_unit)
    Spinner spinnerUnit;
    @BindView(R.id.spiner_listunits)
    MultiSelectSpinner spinerListunits;
    @BindView(R.id.tv_tag_code)
    TextView tvTagCode;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.rl_units)
    RelativeLayout rlUnits;
    private int maxImageSelectCount = 6;
    private GridView imageGridView;
    private PublishNewsPicAdapter adapter;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.drawable.farmer_bg_select_image) //设置图片在下载期间显示的图片
            .cacheInMemory(false)//设置下载的图片是否缓存在内存中
            .cacheOnDisk(false)//设置下载的图片是否缓存在SD卡中
            .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
            .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
            .build();//构建完成
    private TextView tvLocation;
    private Present mPresent = new Present(this);
    private Subscription sub;
    private BDLocation bdlocation;
    private String venid;

    @Override
    protected void onBdLocationFind(BDLocation bdLocatio) {
        tvLocation.setText("在" + bdLocatio.getProvince() + bdLocatio.getCity() + bdLocatio.getLocationDescribe().replace("在", ""));
        bdlocation=bdLocatio;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContet(R.layout.activity_prodcts_collects);
        ButterKnife.bind(this);
        getCustomToolBar().setTitle("产品采收");
        getCustomToolBar().setBackVisble(true, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imageGridView = (GridView) findViewById(R.id.gridView);
        adapter = new PublishNewsPicAdapter();
        imageGridView.setAdapter(adapter);
        findViewById(R.id.tv_click_takePhoto).setOnClickListener(adapter.getTakePhotoClickListener());
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvLocation.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        rlUnits.setVisibility(View.GONE);

    }

    private void useSellerInfo(SellerInfo sellInfo) {
        tvVendorname.setText(sellInfo.getName());
        venid=sellInfo.getId();
    }


    private void useTagState(TagStateInfo tagStateInfo) {

        tvTagCode.append("  " + tagStateInfo.toString());
    }

    private HashMap<String, String> unitsMap;

    private void useUits(UnitsInfo unitsInfo) {
        unitsMap = new HashMap<>();
        spinerListunits.setSelection(new ArrayList<String>());
        if (unitsInfo.getUnits() != null && unitsInfo.getUnits().size() > 0)
            rlUnits.setVisibility(View.VISIBLE);
        for (UnitsInfo.UnitsBean unitsBean : unitsInfo.getUnits()) {
                unitsMap.put(unitsBean.getName(), unitsBean.getId());
            }
        spinerListunits.setItems(new ArrayList<String>(unitsMap.keySet()));
    }

    @Override
    protected void onNFCIDRead(String str) {
        if (isLoading()) return;
        showLoading("查询商户信息中");
        mPresent.getCard(str);
        rlUnits.setVisibility(View.GONE);

    }

    @Override
    protected void onScanCode(String tagCode) {
        super.onScanCode(tagCode);
        tvTagCode.setText(tagCode);
        mPresent.getCodeState(tagCode);
        showLoading("查询标签信息");


    }

    @OnClick(R.id.tv_tag_code)
    public void onViewClicked() {
        Toast.makeText(this, "扫描标签二维码替换标签", Toast.LENGTH_SHORT).show();
    }


    private static class Present extends BaseObjectPresent<ProductsCollectActivity> {
        public Present(ProductsCollectActivity productsCollectActivity) {
            super(productsCollectActivity);

        }

        public void getCard(String card) {
            ApiService.getSellerInfo(card, null, null).map(new Func1<SellerInfo, SellerInfo>() {
                @Override
                public SellerInfo call(SellerInfo sellerInfo) {
                    getRefObj().hideLoading();

                    if (sellerInfo == null) {
                        throw new RuntimeException("");
                    }
                    getRefObj().useSellerInfo(sellerInfo);
                    return sellerInfo;
                }
            }).observeOn(Schedulers.io())
                    .flatMap(new Func1<SellerInfo, Observable<UnitsInfo>>() {
                        @Override
                        public Observable<UnitsInfo> call(SellerInfo sellerInfo) {
                            return ApiService.getUnitsInfo(sellerInfo.getId());
                        }
                    }).observeOn(AndroidSchedulers.mainThread()).subscribe(new ApiSubscriber<UnitsInfo>() {
                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if (!isAvaiable()) return;
                    getRefObj().hideLoading();
                }

                @Override
                public void onNext(UnitsInfo unitsInfo) {
                    super.onNext(unitsInfo);
                    if (!isAvaiable()) return;
                    getRefObj().hideLoading();
                    if (unitsInfo != null) {
                        getRefObj().useUits(unitsInfo);
                    }
                }
            });
        }

        public void getCodeState(String code) {
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
                        getRefObj().useTagState(tagStateInfo);
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

    private boolean checkVaild(){
        if (TextUtils.isEmpty(tvVendorname.getText())){
            Toast.makeText(this,"请使用商户刷卡",Toast.LENGTH_SHORT).show();
            return false;

        }
        if (!tvTagCode.getText().toString().contains("未使用")){
            Toast.makeText(this,"请选取未使用的标签",Toast.LENGTH_SHORT).show();
            return false;


        }
        if (TextUtils.isEmpty(etProduct.getText())){
            Toast.makeText(this,"请输入品名",Toast.LENGTH_SHORT).show();
            return false;


        }
        Editable etCountText = etCount.getText();
        if (TextUtils.isEmpty(etCountText)){
            Toast.makeText(this,"请输入数量",Toast.LENGTH_SHORT).show();
            return false;

        }
           if (!etCountText.toString().matches("[1-9]\\d*(\\.\\d+)?")) {
               Toast.makeText(this, "数量必须为有效的整数或者小数，不能以0开头", Toast.LENGTH_SHORT).show();
               return false;
           }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_location: {
                startLocation();
                break;

            }
            case R.id.btn_save: {
               if (!checkVaild()) return;
                if (adapter==null ||adapter.getPicList()==null||adapter.getPicList().size()==0){
                    Toast.makeText(this,"没有选择图片",Toast.LENGTH_SHORT).show();
                }

                showLoading("上传图片中");
                picAll=0;
                for (int i = 0; i < adapter.getPicList().size(); i++) {
                    if (adapter.getPicList().get(i)!=null){
                        picAll++;
                    }
                }
                uploaded=0;
                uploadImgs=new ArrayList<>();

               Observable.from(adapter.getPicList())
                       .observeOn(Schedulers.io()).filter(new Func1<String, Boolean>() {
                   @Override
                   public Boolean call(String s) {
                       return !TextUtils.isEmpty(s);
                   }
               }) .observeOn(Schedulers.io())
                       .flatMap(new Func1<String, Observable<ImageUploadInfo>>() {
                   @Override
                   public Observable<ImageUploadInfo> call(String s) {
                       return ApiService.uploadFile(new File(s));
                   }
               }).observeOn(AndroidSchedulers.mainThread()).subscribe(new ApiSubscriber<ImageUploadInfo>() {
                   @Override
                   public void onError(Throwable e) {
                       super.onError(e);
                       hideLoading();
                       Toast.makeText(ProductsCollectActivity.this,"上传图片失败，请重试",Toast.LENGTH_SHORT).show();
                   }

                   @Override
                   public void onNext(ImageUploadInfo imageUploadInfo) {
                       super.onNext(imageUploadInfo);
                       uploaded++;
                       uploadImgs.add(imageUploadInfo.getImage());
                       if (uploaded==picAll){
                           if (!checkVaild())return;
                           hideLoading();
                           CollectRecordInfo collectRecordInfo=new CollectRecordInfo();
                           collectRecordInfo.setImages(uploadImgs);
                           if (bdlocation!=null) {
                               collectRecordInfo.setLatitude(bdlocation.getLatitude()+"");
                               collectRecordInfo.setLongitude(bdlocation.getLongitude()+"");

                           }
                           if (!tvLocation.getText().toString().contains("定位中")){
                               collectRecordInfo.setPosition(tvLocation.getText().toString());
                           }
                           collectRecordInfo.setPosition("");
                           collectRecordInfo.setTagId(tvTagCode.getText().toString().split(" ")[0]);
                           collectRecordInfo.setUnit((String) spinnerUnit.getSelectedItem());
                           if (unitsMap!=null &&spinerListunits!=null){
                               ArrayList<String> selectIds=new ArrayList<String>();
                               for (int i = 0; i < spinerListunits.getSelectedStrings().size(); i++) {
                                   selectIds.add(unitsMap.get(spinerListunits.getSelectedStrings().get(i)));
                               }
                               collectRecordInfo.setUnitIds(selectIds);
                           }
                           collectRecordInfo.setProduct(etProduct.getText().toString());

                           String s = etCount.getText().toString();
                           collectRecordInfo.setQuantity( Double.parseDouble(s));
                           collectRecordInfo.setVendorId(venid);
                           mPresent.saveCollectData(collectRecordInfo);
                       }
                   }
               });
                break;
            }
        }
    }


    int picAll ;
    int uploaded;
    ArrayList<String> uploadImgs;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == THUMBNAIL_ACTIVITY) {
            // 选择图片返回
            if (resultCode == PhotoSelectedThumbnailActivity.SELECT_PHOTO) {
                if (data != null) {
                    @SuppressWarnings("unchecked")
                    ArrayList<String> res = (ArrayList<String>) data
                            .getSerializableExtra("tu_ji");
                    for (String path : res) {
                        adapter.addPic(path);
                    }
                }
            } else if (resultCode == PhotoSelectedThumbnailActivity.TAKE_PHOTO) {
                if (data != null && data.getData() != null && data.getData().getPath() != null) {
                    adapter.addPic(data.getData().getPath());
                }
            }
        } else if (requestCode == DELETE_ACTIVITY) {
            if (data != null) {
                ArrayList<String> li = (ArrayList<String>) data.getSerializableExtra("imageList");
                List<String> picList = adapter.getPicList();
                int size = picList.size();
                if (picList.get(size - 1) == null) {
                    if (li != null && li.size() < size - 1) {
                        adapter.setPicList(li);
                    }

                } else {
                    if (li != null && li.size() < size) {
                        adapter.setPicList(li);
                    }
                }
            }
        }

    }

    public class PublishNewsPicAdapter extends BaseAdapter {

        private ArrayList<String> picList;


        private TakePhotoClickListener takePhotoClickListener;

        PhotoClickListener photoClickListener;


        public PublishNewsPicAdapter() {
            takePhotoClickListener = new TakePhotoClickListener();
            photoClickListener = new PhotoClickListener();
            picList = new ArrayList<String>();
            picList.add(null);
        }

        public void setPicList(ArrayList<String> picList) {
            this.picList = picList;
            this.picList.add(null);
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            switch (getItemViewType(position)) {
                case 0:
                    if (convertView == null) {
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(R.layout.social_publish_dynamic_grid_take_photo, parent, false);
                        holder = new ViewHolder();
                        holder.image = (ImageView) convertView.findViewById(R.id.image);
                        holder.image.setOnClickListener(takePhotoClickListener);
                    }
                    break;
                case 1:

                    if (convertView == null) {
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        convertView = inflater.inflate(
                                R.layout.social_publish_dynamic_grid, parent, false);
                        holder = new ViewHolder();
                        holder.image = (ImageView) convertView
                                .findViewById(R.id.image);
                        convertView.setTag(holder);
                    } else {
                        holder = (ViewHolder) convertView.getTag();
                    }
                    imageLoader.displayImage("file://" + picList.get(position), holder.image, options);
                    holder.image.setTag(position);
                    holder.image.setOnClickListener(photoClickListener);
                    break;
            }
            return convertView; // 返回ImageView
        }

        @Override
        public Object getItem(int position) {
            return picList.get(position);
        }

        @Override
        public int getCount() {
            return picList.size();
        }

        @Override
        public int getItemViewType(int position) {
            String data = picList.get(position);
            if (TextUtils.isEmpty(data)) {
                return 0;
            } else {
                return 1;
            }
        }

        public TakePhotoClickListener getTakePhotoClickListener() {
            return takePhotoClickListener;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        public List<String> getPicList() {
            return picList;
        }


        class TakePhotoClickListener implements View.OnClickListener {
            @Override
            public void onClick(View v) {
                if (maxImageSelectCount - picList.size() + 1 <= 0) {
                    Toast.makeText(ProductsCollectActivity.this, "最多只能选择" + maxImageSelectCount + "张图片",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ProductsCollectActivity.this, PhotoSelectedThumbnailActivity.class);
                intent.putExtra("maxImageSelectCount", maxImageSelectCount - picList.size() + 1);
                startActivityForResult(intent, THUMBNAIL_ACTIVITY);
            }
        }

        class PhotoClickListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                Intent intent = new Intent(ProductsCollectActivity.this, PhotoDeleteSliderActivity.class);
                ArrayList<String> imageList;
                if (picList.get(picList.size() - 1) == null) {
                    imageList = new ArrayList<String>(picList.subList(0, picList.size() - 1));
                } else {
                    imageList = new ArrayList<String>(picList.subList(0, picList.size()));
                }
                intent.putExtra("imageList", (Serializable) imageList);
                intent.putExtra("index", position);
                startActivityForResult(intent, DELETE_ACTIVITY);
            }
        }

        public void addPic(String path) {
            picList.add(picList.size() - 1, path);
            if (picList.size() > maxImageSelectCount) {
                picList.remove(maxImageSelectCount);
            }
            notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder {
            ImageView image;
        }

        public void removePic(int position) {
            picList.remove(position);
        }

    }


}
