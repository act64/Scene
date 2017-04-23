package com.recovery.scene.activity;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.recovery.scene.model.ProductItemPOJO;
import com.recovery.scene.utils.APPUtils;

import hotjavi.lei.com.base_module.activity.BaseSetMainActivity;

/**
 * Created by tom on 2017/4/23.
 * 支持 nfc 定位 扫码
 */

public abstract class BaseDeviceFuncActivity extends BaseSetMainActivity{

    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    private IntentFilter[] mWriteTagFilters;
    private long lastNfcReadTime=0;
    private LocationClient mLocationClient;
    private BDLocationListener myListener = new MyLocationListener();
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (!TextUtils.isEmpty(bdLocation.getLocationDescribe())) {
                onBdLocationFind(bdLocation);
                mLocationClient.unRegisterLocationListener(myListener);
                mLocationClient.stop();
            }
        }
    }

    protected abstract void onBdLocationFind(BDLocation bdLocatio);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNfc();
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        initLocation();
        mLocationClient.start();
        registerReceiver(mReceiver, mSCTestIntentfilter);

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
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient!=null){
            mLocationClient.unRegisterLocationListener(myListener);
            mLocationClient.stop();
        }
        if (mReceiver!=null){
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())||NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())
                ||NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
//            NdefMessage[] msgs = getNdefMessages(intent);
            readNFCId(intent);
//            String body = new String(msgs[0].getRecords()[0].getPayload());

//           Log.e ("nfcread","***读取数据***" + body);
        }
    }

    private void readNFCId(Intent intent){
        byte[] bytesId =intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
//        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        byte[] dataId = tag.getId();
//        Log.e("dataId",dataId+"");
        String strId = bytesToHexString(bytesId);// 字符序列转换为16进制字符串
        Log.e("nfcID",strId+"");
        long timenow=System.currentTimeMillis();
        if (timenow-lastNfcReadTime>=2000) {
            lastNfcReadTime=timenow;
            onNFCIDRead(strId);
        }


    }

    protected abstract void onNFCIDRead(String str);

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


    protected void startLocation(){
        try {
            mLocationClient.registerLocationListener(myListener);
            mLocationClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static final String SCN_CUST_ACTION_SCODE = "com.android.server.scannerservice.broadcast";
    IntentFilter mSCTestIntentfilter = new IntentFilter(SCN_CUST_ACTION_SCODE);
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
               onScanCode(code);
            }
        }

    };

    protected void onScanCode(String tagCode){}
}
