package com.recovery.scene.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.recovery.scene.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hotjavi.lei.com.base_module.activity.BaseSetMainActivity;

/**
 * Created by tom on 2017/4/18.
 */

public class ProductsCollectActivity extends BaseSetMainActivity implements View.OnClickListener {
    private static final int THUMBNAIL_ACTIVITY = 0;
    private static final int DELETE_ACTIVITY = 11;
    private int maxImageSelectCount = 9;
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
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private TextView tvLocation;
    private NfcAdapter mNfcAdapter;
    private IntentFilter[] mWriteTagFilters;
    private PendingIntent mNfcPendingIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMainContet(R.layout.activity_prodcts_collects);
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

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();
        mLocationClient.start();
        initNfc();

    }

    private void initNfc(){
        //获取默认NFC设备
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, "该设备不支持NFC！", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        //查看NFC是否开启
        if (!mNfcAdapter.isEnabled()){
            Toast.makeText(this, "请在系统设置中先启用NFC功能", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

         mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        mWriteTagFilters = new IntentFilter[] { ndefDetected ,new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED),new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)};
    }

    @Override
    protected void onResume() {
        super.onResume();
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mNfcAdapter.disableForegroundNdefPush(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())||NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())
                ||NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] msgs = getNdefMessages(intent);
            readNFCId(intent);
            String body = new String(msgs[0].getRecords()[0].getPayload());

           Log.e ("nfcread","***读取数据***" + body);
        }
    }

    private void readNFCId(Intent intent){
        byte[] bytesId =intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
//        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        byte[] dataId = tag.getId();
//        Log.e("dataId",dataId+"");
        String strId = bytesToHexString(bytesId);// 字符序列转换为16进制字符串
        Log.e("nfcID",strId+"");
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();

        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.toUpperCase(Character.forDigit(
                    (src[i] >>> 4) & 0x0F, 16));
            buffer[1] = Character.toUpperCase(Character.forDigit(src[i] & 0x0F,
                    16));
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    //实际读取数据部分
    private NdefMessage[] getNdefMessages(Intent intent)
    {
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action))
        {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null)
            {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++)
                {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else
            {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
                msgs = new NdefMessage[] { msg };
            }
        } else
        {
            // Log.d(TAG, "Unknown intent.");
            finish();
        }
        return msgs;
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient.stop();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_location: {
                mLocationClient.registerLocationListener(myListener);
                mLocationClient.start();
                break;
            }
        }
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (!TextUtils.isEmpty(bdLocation.getLocationDescribe())) {
                tvLocation.setText(bdLocation.getLocationDescribe());
                mLocationClient.unRegisterLocationListener(myListener);
                mLocationClient.stop();
            }
        }
    }

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
