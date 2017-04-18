package com.recovery.scene.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.recovery.scene.R;
import com.recovery.scene.application.BaseApplication;

import java.io.File;
import java.io.IOException;


public class ImageManager {

//    public static File imageCacheDir = StorageUtils.getOwnCacheDirectory(BaseApplication.context,
//            "imageloader/Cachenew");
//
    public static String userGenerateImageDir = StorageUtils.getOwnCacheDirectory(BaseApplication.context,
            "userGenerateImageDir/").getPath();
//
//    private static ImageLoader imageLoader = ImageLoader.getInstance();
//
//    public static void init(Context context) {
//        ImageLoaderConfiguration config = null;
//        try {
//            config = new ImageLoaderConfiguration.Builder(
//                    context)
//                    .memoryCacheExtraOptions(3000, 3000)
//                    .threadPoolSize(3)
//                    // 线程池内加载的数量
//                    .threadPriority(Thread.NORM_PRIORITY - 2)
//
//                    .denyCacheImageMultipleSizesInMemory()
//                    .memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))
//
//                    // You can pass your own memory cache
//                    // implementation/你可以通过自己的内存缓存实现
//                    .tasksProcessingOrder(QueueProcessingType.LIFO)
//                    // 缓存的文件数量
//                    .diskCache(new LruDiskCache(imageCacheDir, new Md5FileNameGenerator(), 300 * 1024 * 1024))
//                    // 自定义缓存路径
//                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//                    .diskCacheFileCount(200)
//                    .imageDownloader(
//                            new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
////					.writeDebugLogs() // Remove for release app
//                    .build();
//            ImageLoader.getInstance().init(config);// 全局初始化此配置
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }// 开始构建
//        // Initialize ImageLoader with configuration.
//    }
//

//
//    public static DisplayImageOptions getOptions() {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                // 设置下载的图片是否缓存在SD卡中
//                .cacheOnDisk(true)
//                // 设置图片以如何的编码方式显示
//                .imageScaleType(ImageScaleType.EXACTLY)
//                // 设置图片的解码类型
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .considerExifParams(true)
//                // 设置图片下载前的延迟
////				.delayBeforeLoading(100)// int
//                // delayInMillis为你设置的延迟时间
//                // 设置图片加入缓存前，对bitmap进行设置
//                // .preProcessor(BitmapProcessor preProcessor)
//                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
//                // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
//                // .displayer(new FadeInBitmapDisplayer(100))// 淡入
//                .build();
//
//        return options;
//    }
//
//

//
//    public static DisplayImageOptions getOptionsD(int defaultImg) {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                // 设置图片在下载期间显示的图片
//                .showImageOnLoading(defaultImg)
//                // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageForEmptyUri(defaultImg)
//                // 设置图片加载/解码过程中错误时候显示的图片
//                .showImageOnFail(defaultImg)
//                // 设置下载的图片是否缓存在内存中
//                .cacheInMemory(true)
//                // 设置下载的图片是否缓存在SD卡中
//                .cacheOnDisk(true)
//                // 保留Exif信息
//                // .considerExifParams(true)
//                // 设置图片以如何的编码方式显示
//                .imageScaleType(ImageScaleType.EXACTLY)
//                // 设置图片的解码类型
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                // .decodingOptions(android.graphics.BitmapFactory.Options
//                // decodingOptions)//设置图片的解码配置
//                .considerExifParams(true)
//                // 设置图片下载前的延迟
////				.delayBeforeLoading(100)// int
//                // delayInMillis为你设置的延迟时间
//                // 设置图片加入缓存前，对bitmap进行设置
//                // .preProcessor(BitmapProcessor preProcessor)
//                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
//                // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
//                // .displayer(new FadeInBitmapDisplayer(100))// 淡入
//                .build();
//
//        return options;
//    }
//
//    public static DisplayImageOptions getOptionsRounded(int rounded) {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                // 设置下载的图片是否缓存在内存中
//                .cacheInMemory(true)
//                // 设置下载的图片是否缓存在SD卡中
//                .cacheOnDisk(true)
//                // 保留Exif信息
//                .considerExifParams(true)
//                // 设置图片以如何的编码方式显示
//                .imageScaleType(ImageScaleType.EXACTLY)
//                // 设置图片的解码类型
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                // .decodingOptions(android.graphics.BitmapFactory.Options
//                // decodingOptions)//设置图片的解码配置
//                .considerExifParams(true)
//                // 设置图片下载前的延迟
////				.delayBeforeLoading(100)// int
//                // delayInMillis为你设置的延迟时间
//                // 设置图片加入缓存前，对bitmap进行设置
//                // .preProcessor(BitmapProcessor preProcessor)
//                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
//                .displayer(new RoundedBitmapDisplayer(rounded))// 是否设置为圆角，弧度为多少
//                // .displayer(new FadeInBitmapDisplayer(100))// 淡入
//                .build();
//        return options;
//    }
//
//    public static DisplayImageOptions getOptionsRoundedD(int rounded,
//                                                         int defaultImageId) {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                // 设置图片在下载期间显示的图片
//                .showImageOnLoading(defaultImageId)
//                // 设置图片Uri为空或是错误的时候显示的图片
//                .showImageForEmptyUri(defaultImageId)
//                // 设置图片加载/解码过程中错误时候显示的图片
//                .showImageOnFail(defaultImageId)
//                // 设置下载的图片是否缓存在内存中
//                .cacheInMemory(true)
//                // 设置下载的图片是否缓存在SD卡中
//                .cacheOnDisk(true)
//                // 保留Exif信息
//                .considerExifParams(true)
//                // 设置图片以如何的编码方式显示
//                .imageScaleType(ImageScaleType.EXACTLY)
//                // 设置图片的解码类型
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                // .decodingOptions(android.graphics.BitmapFactory.Options
//                // decodingOptions)//设置图片的解码配置
//                .considerExifParams(true)
//                // 设置图片下载前的延迟
////				.delayBeforeLoading(100)// int
//                // delayInMillis为你设置的延迟时间
//                // 设置图片加入缓存前，对bitmap进行设置
//                // .preProcessor(BitmapProcessor preProcessor)
//                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
//                .displayer(new RoundedBitmapDisplayer(rounded))// 是否设置为圆角，弧度为多少
//                // .displayer(new FadeInBitmapDisplayer(100))// 淡入
//                .build();
//        return options;
//    }
//
//    public static DisplayImageOptions getNotResetOptions() {
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                // 设置图片在下载期间显示的图片
//                .cacheInMemory(true)
//                .cacheOnDisk(true)
//                .imageScaleType(ImageScaleType.EXACTLY)
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .considerExifParams(true)
//                .resetViewBeforeLoading(false)// 设置图片在下载前是否重置，复位
//                .build();
//
//        return options;
//    }
//
//    public static void loadImage(String url, Context ctx, ImageView iv, DisplayImageOptions options) {
////        imageLoader.displayImage(url, iv, options);
//        imageLoader.displayImage(getWebpUrl(url), iv, options);
//    }
//
//    private static String getWebpUrl(String url) {
//        if (AppImpl.getAppRroxy().geBuildType().equals("debug") || AppImpl.getAppRroxy().geBuildType().equals("qatest")) {
//            if (TextUtils.isEmpty(url))
//                return url;
//            else
//                return url.replace("@.webp", "");
//        }
//        String webpUrl = url;
//        if (!TextUtils.isEmpty(url) && !url.contains("@.webp")) {
//            if (url.contains("@"))
//                webpUrl = url + ".webp";
//            else
//                webpUrl = url + "@.webp";
//        }
//        return webpUrl;
//    }
//
//    public static void loadImage(String url, Context ctx, ImageView iv) {
//        if (url == null || url.endsWith("null")) {
//            return;
//        }
//        DisplayImageOptions options = getOptions();
////        imageLoader.displayImage(url, iv, options);
//        imageLoader.displayImage(getWebpUrl(url), iv, options);
//    }
//
//    /**
//     * 加载本地文件
//     */
//    public static void displayByLocaleFile(String uri, ImageView iv) {
//        DisplayImageOptions options = getOptions();
////        imageLoader.displayImage(url, iv, options);
//        imageLoader.displayImage(uri, iv, options);
//    }
//
//
//    public static void loadImage(String url, Context ctx, ImageView iv, ImageLoadingListener imageLoadingListener) {
//        if (url == null || url.endsWith("null")) {
//            return;
//        }
//        DisplayImageOptions options = getOptions();
////        imageLoader.displayImage(url, iv, options, imageLoadingListener);
//        imageLoader.displayImage(getWebpUrl(url), iv, options, imageLoadingListener);
//    }
//
//    public static Bitmap getBitmap(String url, ImageSize size) {
//        if (url == null || url.endsWith("null")) {
//            return null;
//        }
////        return imageLoader.loadImageSync(url, size);
//        return imageLoader.loadImageSync(getWebpUrl(url), size);
//    }
//
//    public static Bitmap getBitmapByLocal(String url, ImageSize size) {
////        return imageLoader.loadImageSync(url, size);
//        if (url == null || url.endsWith("null")) {
//            return null;
//        }
//        return imageLoader.loadImageSync(url, size);
//    }
//
//
//    public static Bitmap getBitmap(String url) {
////        return imageLoader.loadImageSync(url, size);
//        if (url == null || url.endsWith("null")) {
//            return null;
//        }
//        return imageLoader.loadImageSync(getWebpUrl(url));
//    }
//
//    public static Bitmap loadImageSync(String url) {
//        if (url == null || url.endsWith("null")) {
//            return null;
//        }
////        return imageLoader.loadImageSync(url);
//        return imageLoader.loadImageSync(getWebpUrl(url));
//    }
//
//    /**
//     * 从drawable中异步加载本地图片
//     *
//     * @param imageId
//     * @param imageView
//     */
//    public static void displayFromDrawable(int imageId, ImageView imageView) {
//        imageLoader.displayImage("drawable://" + imageId,
//                imageView);
//    }
//
//    /**
//     * 用户默认头像
//     *
//     * @param url
//     * @param ctx
//     * @param iv
//     */
//    public static void loadImageByDefaultUserImage(String url, Context ctx,
//                                                   ImageView iv) {
//        if (url == null || url.endsWith("null")) {
//            return;
//        }
//        DisplayImageOptions options = getDefaultUserOptions();
////        imageLoader.displayImage(url, iv, options);
//        imageLoader.displayImage(getWebpUrl(url), iv, options);
//    }
//
//    public static void loadImageByDefaultImage(String url, Context ctx,
//                                               ImageView iv, int defaultImg) {
//        if (url == null || url.endsWith("null")) {
//            iv.setImageResource(defaultImg);
//            return;
//        }
//        DisplayImageOptions options = getOptionsD(defaultImg);
////        imageLoader.displayImage(url, iv, options);
//        imageLoader.displayImage(getWebpUrl(url), iv, options);
//    }
//
//    public static void loadImageByDefaultImageUseaFast(String url, Context ctx,
//                                                       final ImageView iv, final int defaultImg) {
//        if (url == null || url.endsWith("null")) {
//            iv.setImageResource(defaultImg);
//            return;
//        }
//        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .cacheInMemory(true)
//                // 设置下载的图片是否缓存在SD卡中
//                .cacheOnDisk(true)
//                // 设置图片以如何的编码方式显示
//                .imageScaleType(ImageScaleType.EXACTLY)
//                // 设置图片的解码类型
//                .bitmapConfig(Bitmap.Config.RGB_565)
//                .considerExifParams(true)
//                // 设置图片下载前的延迟
////				.delayBeforeLoading(100)// int
//                // delayInMillis为你设置的延迟时间
//                // 设置图片加入缓存前，对bitmap进行设置
//                // .preProcessor(BitmapProcessor preProcessor)
//                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
//                // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
//                // .displayer(new FadeInBitmapDisplayer(100))// 淡入
//                .build();
//        iv.setTag(getWebpUrl(url));
//        imageLoader.displayImage(getWebpUrl(url), new ImageViewAware(iv, false), options, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                if (iv.getTag().equals(imageUri)) {
//                    iv.setImageResource(defaultImg);
//                }
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//
//            }
//        });
//    }
//
//
//    public static void loadImageByDefaultImage(String url, Context ctx,
//                                               ImageView iv, int defaultImg, ImageLoadingListener listener) {
//        if (url == null || url.endsWith("null")) {
//            iv.setImageResource(defaultImg);
//            return;
//        }
//        DisplayImageOptions options = getOptionsD(defaultImg);
////        imageLoader.displayImage(url, iv, options, listener);
//        imageLoader.displayImage(getWebpUrl(url), iv, options, listener);
//    }
//
//
//    public static void loadImageByImage(String url, Context ctx,
//                                        ImageView iv, int defaultImg, ImageLoadingListener listener) {
//        if (url == null || url.endsWith("null")) {
//            iv.setImageResource(defaultImg);
//            return;
//        }
//        DisplayImageOptions options = getOptionsD(defaultImg);
////        imageLoader.displayImage(url, iv, options, listener);
//        imageLoader.displayImage(url, iv, options, listener);
//    }
//
//    public static void loadImageRounded(String url, Context ctx, ImageView iv,
//                                        int rounded) {
//        if (url == null || url.endsWith("null")) {
//            return;
//        }
//        DisplayImageOptions options = getOptionsRounded(rounded);
////        imageLoader.displayImage(url, iv, options);
//        imageLoader.displayImage(getWebpUrl(url), iv, options);
//    }
//
//    public static void loadImageRoundedByDefaultImage(String url, Context ctx,
//                                                      ImageView iv, int rounded, int defaultImg) {
//        if (url == null || url.endsWith("null")) {
//            return;
//        }
//        DisplayImageOptions options = getOptionsRoundedD(rounded, defaultImg);
////        imageLoader.displayImage(url, iv, options);
//        imageLoader.displayImage(getWebpUrl(url), iv, options);
//    }

    public static Uri takePhoto(Activity context, int requestCode) {
        Intent intent = new Intent();
        // 指定开启系统相机的Action
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        // 根据文件地址创建文件
        File file = getTakePhotoImageTemp();
        if (file.exists()) {
            file.delete();
        }
        // 把文件地址转换成Uri格式
        Uri resUri = Uri.fromFile(file);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, resUri);
        context.startActivityForResult(intent, requestCode);
        return resUri;
    }

    private static File getTakePhotoImageTemp() {
        String path = userGenerateImageDir + "/takePhoto/";
        File file = new File(path);
        if (!file.exists() && !file.mkdirs())
            return null;
        try {
            File uniqueFile = File.createTempFile("jkys", ".jpg", file);
            return uniqueFile;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static void cropImage(Activity context, Uri uri, int outputX, int outputY, int requestCode) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        // intent.putExtra("circleCrop", new String("true"));
        // intent.putExtra("circleCrop", true);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        File file = getCropImageTemp();
        if (file.exists()) {
            file.delete();
        }
        // 把文件地址转换成Uri格式
        Uri resUri = Uri.fromFile(file);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, resUri);
        context.startActivityForResult(intent, requestCode);
    }

    public static void cropImage(Activity context, File photoFile, int outputX, int outputY, int requestCode) {
        // getUriForFile这个方法 返回的是Uri即目前photoUri 打印结果: content://cn.dreamplus.wentang.fileprovider/camera_photos/*.png
        Uri photoUri = FileProvider.getUriForFile(context, "cn.dreamplus.wentang.fileprovider", photoFile);
        Intent intent = new Intent("com.android.camera.action.CROP");
        // 7.0 的还需写权限在上一行getUriForFile方法的注释中可以看到 鼠标停留在gerUriForFile方法按F1即可。
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        // intent.putExtra("circleCrop", new String("true"));
        // intent.putExtra("circleCrop", true);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        File file = getCropImageTemp();
        if (file.exists()) {
            file.delete();
        }
        // 把文件地址转换成Uri格式
        Uri resUri = Uri.fromFile(file);
        // 设置系统相机拍摄照片完成后图片文件的存放地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT, resUri);
        context.startActivityForResult(intent, requestCode);
    }

    public static File getCropImageTemp() {
        String path = userGenerateImageDir + "/cropPhoto/";
        File file = new File(path);
        if (!file.exists() && !file.mkdirs())
            return null;
        file = new File(path, "cropPhoto.jpg");
        return file;
    }


}
