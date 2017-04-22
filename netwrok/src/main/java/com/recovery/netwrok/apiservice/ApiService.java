package com.recovery.netwrok.apiservice;

import android.net.Uri;
import android.util.Log;

import com.recovery.netwrok.model.CollectRecordInfo;
import com.recovery.netwrok.model.ImageUploadInfo;
import com.recovery.netwrok.model.PackCreateResultInfo;
import com.recovery.netwrok.model.PackageCreateInfo;
import com.recovery.netwrok.model.PurchaseInfo;
import com.recovery.netwrok.model.ResponseResult;
import com.recovery.netwrok.model.SellerInfo;
import com.recovery.netwrok.model.TagInfo;
import com.recovery.netwrok.model.TagStateInfo;
import com.recovery.netwrok.model.UnitsInfo;
import com.recovery.netwrok.model.UserInfo;
import com.recovery.netwrok.util.RetrofitUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by tom on 2017/4/22.
 */

public class ApiService {
    private static IAPiService iAPiService= RetrofitUtil.get().create(IAPiService.class);



    public static Observable<ImageUploadInfo> uploadFile(Uri fileUri) {
        // create upload service client


        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file=null ;

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", "file", requestFile);

        // add another part within the multipart request
        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);
       return RetrofitUtil.handleApiCall( iAPiService.imageUpload(description,body));


    }


    /**
     * 用户登录
     输入用户名和密码登录
     * 请求参数：
     username：用户名
     password：密码
     * @return
     */
    public static Observable<UserInfo> login(String username,String password){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("username",username);
        hashMap.put("password",password);
        return RetrofitUtil.handleApiCall(iAPiService.userLogin(hashMap));
    }


    /**
     * 取得商户信息
     现场刷NFC卡，根据NFC卡的ID，来取得商户的信息。

     访问路径：GET /vendor/{id}

     请求参数：
     id： NFC卡的ID
     * @param nfcId
     * @return
     */
    public static Observable<SellerInfo> getSellerInfo(String nfcId){
       return RetrofitUtil.handleApiCall(iAPiService.nfcQuerySellerInfo(nfcId));
    }

    /**
     * 查询标签状态
     根据二维码标签的ID，来查询标签的状态。要注意的是，二维码的内容是形如http://qs0.me/v/73jKLD62的网址，而这里所说的标签ID，指的是网址最后的8位字符串。

     请求路径：GET /barcode/{id}/state

     请求参数：
     id：标签ID
     * @return
     */
    public static Observable<TagStateInfo> tagqueryState(String tagid){
        return RetrofitUtil.handleApiCall(iAPiService.tagCodeQueryState(tagid));
    }

    /**
     * 保存采收记录
     保存采收记录。记录中需要包含地理信息，请统一使用百度地图的坐标。

     访问路径： POST /purchase/create
     * @return
     */
    public static Observable<Object> savePurchase(CollectRecordInfo collectRecordInfo){
        ResponseResult<CollectRecordInfo> recordInfoResponseResult=new ResponseResult<>();
        recordInfoResponseResult.setData(collectRecordInfo);
        return RetrofitUtil.handleApiCall(iAPiService.savePurchase(recordInfoResponseResult));
    }

    /**
     * 取得商户关联的单元
     即取得商户所拥有的单元（如大棚，鱼塘等）

     访问路径：GET /vendor/{vendorId}/units

     请求参数：
     vendorId： 商户ID
     * @return
     */
    public static Observable<UnitsInfo> getUnitsInfo(String vendorId){
        return RetrofitUtil.handleApiCall(iAPiService.getUnitInfo(vendorId));
    }


    /**
     * 取得标签内容
     扫描二维码标签，取得标签所绑定的内容。该API只能取得采收绑定的二维码，否则将返回错误。

     访问路径：GET /tag/{id}

     请求参数：
     id： 标签ID
     * @return
     */
    public static Observable<TagInfo> getTagInfo(String tagID){
        return RetrofitUtil.handleApiCall(iAPiService.getTagInfo(tagID));
    }

    /**
     * 保存打包信息
     把产品打包后保存

     访问路径：POST /package/create


     * @param packageCreateInfo
     * @return
     */
    public static Observable<PackCreateResultInfo> packageCreate(PackageCreateInfo packageCreateInfo){
        return RetrofitUtil.handleApiCall(iAPiService.packageCreate(packageCreateInfo));
    }

    /**
     请求参数：
     startTime：开始时间，到毫秒的时间戳
     endTime：结束时间，到毫秒的时间戳
     vendor：商户名
     * @return
     */
    public static Observable<PurchaseInfo> purchaseHistory(Long startTime,Long endTime,String vendor){
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("startTime",startTime);
        hashMap.put("endTime",endTime);
        hashMap.put("vendor",vendor);
        return RetrofitUtil.handleApiCall(iAPiService.purchaseHistory(hashMap));
    }

    public interface IAPiService{
        @GET("user/login")
        Observable<ResponseResult<UserInfo>> userLogin(@QueryMap Map<String,Object> map);

        /**
         * @param id nfc的卡号id
         * @return
         */
        @GET("vendor/{id}")
        Observable<ResponseResult<SellerInfo>> nfcQuerySellerInfo(@Path("id")String id);

        @GET("barcode/{id}/state")
        Observable<ResponseResult<TagStateInfo>> tagCodeQueryState(@Path("id")String id);

        @Multipart
        @POST("file/upload")
        Observable<ResponseResult<ImageUploadInfo>> imageUpload(@Part("description") RequestBody description,
                                                                @Part MultipartBody.Part file);

        @POST("purchase/create")
        Observable<ResponseResult<Object>> savePurchase(@Body ResponseResult<CollectRecordInfo> collectRecordInfoResponseResult);

        @GET("vendor/{vendorId}/units")
        Observable<ResponseResult<UnitsInfo>>getUnitInfo(@Path("vendorId")String vendorId);

        @GET("tag/{id}")
        Observable<ResponseResult<TagInfo>>getTagInfo(@Path("id")String TagId);

        @POST("package/create")
        Observable<ResponseResult<PackCreateResultInfo>> packageCreate(@Body PackageCreateInfo packageCreateInfo);

        @GET("purchase/history")
        Observable<ResponseResult<PurchaseInfo>> purchaseHistory(@QueryMap Map<String,Object> quryMap);
    }
}
