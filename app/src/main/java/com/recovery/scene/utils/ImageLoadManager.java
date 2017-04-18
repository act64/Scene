package com.recovery.scene.utils;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.recovery.scene.application.BaseApplication;

import java.io.File;
import java.io.IOException;

public class ImageLoadManager {

    public static File imageCacheDir = StorageUtils.getOwnCacheDirectory(BaseApplication.context,
            "imageloader/Cachenew");

    private static ImageLoader imageLoader = ImageLoader.getInstance();

    public static void init() {
        ImageLoaderConfiguration config = null;
        ImageLoaderConfiguration.Builder builder = null;
        try {
            builder = new ImageLoaderConfiguration.Builder(
                    BaseApplication.context)
                    .memoryCacheExtraOptions(388, 1080)
                    .threadPoolSize(3)
                    // 线程池内加载的数量
                    .threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(5 * 1024 * 1024)) //建议内存设在5-10M,可以有比较好的表现
                    // You can pass your own memory cache
                    // implementation/你可以通过自己的内存缓存实现
                    // 自定义缓存路径
                    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                    .diskCacheFileCount(100)
                    .imageDownloader(
                            new BaseImageDownloader(BaseApplication.context, 5 * 1000, 30 * 1000));
            builder = builder
                    .tasksProcessingOrder(QueueProcessingType.FIFO)
                    .diskCache(new LruDiskCache(imageCacheDir, new Md5FileNameGenerator(), 300 * 1024 * 1024));
            config = builder.build();
        } catch (IOException e) {
            if (builder == null) {
                config = new ImageLoaderConfiguration.Builder(
                        BaseApplication.context)
                        .build();
            } else {
                config = builder.build();
            }
            e.printStackTrace();
        }// 开始构建
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }

    public static DisplayImageOptions getOptionsD(int defaultImg) {
//        Drawable
        if (defaultImg < 0) {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    // 设置下载的图片是否缓存在内存中
                    .cacheInMemory(true)
                    // 设置下载的图片是否缓存在SD卡中
                    .cacheOnDisk(true)
                    .imageScaleType(ImageScaleType.NONE)
                    // 设置图片的解码类型
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                    .build();

            return options;
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // 设置图片在下载期间显示的图片
                .showImageOnLoading(defaultImg)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(defaultImg)
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(defaultImg)
                // 设置下载的图片是否缓存在内存中
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.NONE)
                // 设置图片的解码类型
                .bitmapConfig(Bitmap.Config.RGB_565)
                .delayBeforeLoading(100)//载入图片前稍做延时可以提高整体滑动的流畅度
                .build();

        return options;
    }

    public static DisplayImageOptions getOptionsRoundedD(int rounded,
                                                         int defaultImageId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // 设置图片在下载期间显示的图片
                .showImageOnLoading(defaultImageId)
                // 设置图片Uri为空或是错误的时候显示的图片
                .showImageForEmptyUri(defaultImageId)
                // 设置图片加载/解码过程中错误时候显示的图片
                .showImageOnFail(defaultImageId)
                // 设置下载的图片是否缓存在内存中
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisk(true)
                // 保留Exif信息
                .considerExifParams(true)
                // 设置图片以如何的编码方式显示
                .imageScaleType(ImageScaleType.EXACTLY)
                // 设置图片的解码类型
                .bitmapConfig(Bitmap.Config.RGB_565)
                // .decodingOptions(android.graphics.BitmapFactory.Options
                // decodingOptions)//设置图片的解码配置
                .considerExifParams(true)
                // 设置图片下载前的延迟
//				.delayBeforeLoading(100)// int
                // delayInMillis为你设置的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(rounded))// 是否设置为圆角，弧度为多少
                // .displayer(new FadeInBitmapDisplayer(100))// 淡入
                .build();
        return options;
    }

    public static DisplayImageOptions getOptionsRounded(int rounded) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                // 设置下载的图片是否缓存在内存中
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisk(true)
                // 保留Exif信息
                .considerExifParams(true)
                // 设置图片以如何的编码方式显示
                .imageScaleType(ImageScaleType.EXACTLY)
                // 设置图片的解码类型
                .bitmapConfig(Bitmap.Config.RGB_565)
                // .decodingOptions(android.graphics.BitmapFactory.Options
                // decodingOptions)//设置图片的解码配置
                .considerExifParams(true)
                // 设置图片下载前的延迟
//				.delayBeforeLoading(100)// int
                // delayInMillis为你设置的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                .displayer(new RoundedBitmapDisplayer(rounded))// 是否设置为圆角，弧度为多少
                // .displayer(new FadeInBitmapDisplayer(100))// 淡入
                .build();
        return options;
    }

    public static DisplayImageOptions getOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisk(true)
                // 设置图片以如何的编码方式显示
                .imageScaleType(ImageScaleType.EXACTLY)
                // 设置图片的解码类型
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                // 设置图片下载前的延迟
//				.delayBeforeLoading(100)// int
                // delayInMillis为你设置的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                // .displayer(new FadeInBitmapDisplayer(100))// 淡入
                .build();

        return options;
    }


    private static String getWebpUrl(String url) {
        return url;
//        if (url.startsWith("file://"))
//            return url;
//        String buildType = ;
//        if (buildType.equals("debug") || buildType.equals("qatest")) {
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
    }


    public static void loadImageByDefaultImage(String url, ImageView iv, int defaultImg) {
        if (url == null || url.endsWith("null")) {
            iv.setImageResource(defaultImg);
            return;
        }
        DisplayImageOptions options = getOptionsD(defaultImg);
//        imageLoader.displayImage(url, iv, options);
        ImageAware imageAware = new ImageViewAware(iv, false);
        imageLoader.displayImage(getWebpUrl(url), imageAware, options);
    }

    public static void loadImageRoundedByDefaultImage(String url,
                                                      ImageView iv, int rounded, int defaultImg) {
        if (url == null || url.endsWith("null")) {
            iv.setImageResource(defaultImg);
            return;
        }
        DisplayImageOptions options = getOptionsRoundedD(rounded, defaultImg);
        ImageAware imageAware = new ImageViewAware(iv, false);
//        imageLoader.displayImage(url, iv, options);
        imageLoader.displayImage(getWebpUrl(url), imageAware, options);
    }

    public static void loadImageRounded(String url, ImageView iv,
                                        int rounded) {
        if (url == null || url.endsWith("null")) {
            return;
        }
        DisplayImageOptions options = getOptionsRounded(rounded);
        ImageAware imageAware = new ImageViewAware(iv, false);
//        imageLoader.displayImage(url, iv, options);
        imageLoader.displayImage(getWebpUrl(url), imageAware, options);
    }


    public static void loadImageByDefaultImage(String url, ImageView iv, int defaultImg,
                                               ImageLoadingListener listener) {
        if (url == null || url.endsWith("null")) {
            iv.setImageResource(defaultImg);
            return;
        }
        DisplayImageOptions options = getOptionsD(defaultImg);
//        imageLoader.displayImage(url, iv, options, listener);
        ImageAware imageAware = new ImageViewAware(iv, false);
        imageLoader.displayImage(getWebpUrl(url), imageAware, options, listener);
    }

    public static void loadImageByDefaultImageUseaFast(String url,
                                                       final ImageView iv, final int defaultImg) {
        if (url == null || url.endsWith("null")) {
            iv.setImageResource(defaultImg);
            return;
        }
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                // 设置下载的图片是否缓存在SD卡中
                .cacheOnDisk(true)
                // 设置图片以如何的编码方式显示
                .imageScaleType(ImageScaleType.EXACTLY)
                // 设置图片的解码类型
                .bitmapConfig(Bitmap.Config.RGB_565)
                .considerExifParams(true)
                // 设置图片下载前的延迟
//				.delayBeforeLoading(100)// int
                // delayInMillis为你设置的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)
                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                // .displayer(new FadeInBitmapDisplayer(100))// 淡入
                .build();
        iv.setTag(getWebpUrl(url));
        imageLoader.displayImage(getWebpUrl(url), new ImageViewAware(iv, false), options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                if (iv.getTag().equals(imageUri)) {
                    iv.setImageResource(defaultImg);
                }
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });
    }

    public static void loadImage(String url, ImageView iv, DisplayImageOptions options) {
        if (url == null || url.endsWith("null")) {
            return;
        }
        ImageAware imageAware = new ImageViewAware(iv, false);
        imageLoader.displayImage(getWebpUrl(url), imageAware, options);
    }

    public static void loadImage(String url, ImageView iv) {
        if (url == null || url.endsWith("null")) {
            return;
        }
        DisplayImageOptions options = getOptions();
        ImageAware imageAware = new ImageViewAware(iv, false);
        imageLoader.displayImage(url, imageAware, options);
    }

    public static void loadImage(String url, ImageView iv, ImageLoadingListener listener,
                                 DisplayImageOptions options) {
        if (url == null || url.endsWith("null")) {
            return;
        }
        ImageAware imageAware = new ImageViewAware(iv, false);
        imageLoader.displayImage(getWebpUrl(url), imageAware, options, listener);
    }


    public static void loadImage(String url, ImageView iv,
                                 ImageLoadingListener listener) {
        if (url == null || url.endsWith("null")) {
            return;
        }
        DisplayImageOptions options = getOptionsD(-1);
//        imageLoader.displayImage(url, iv, options, listener);
        ImageAware imageAware = new ImageViewAware(iv, false);
        imageLoader.displayImage(getWebpUrl(url), imageAware, options, listener);
    }

    /**
     * 加载本地文件
     */
    public static void displayByLocaleFile(String uri, ImageView iv) {
        if (uri == null || uri.endsWith("null")) {
            return;
        }
        DisplayImageOptions options = getOptions();
//        imageLoader.displayImage(url, iv, options);
        imageLoader.displayImage(uri, iv, options);
    }

    public static Bitmap getBitmap(String url, ImageSize size) {
        if (url == null || url.endsWith("null")) {
            return null;
        }
//        return imageLoader.loadImageSync(url, size);
        return imageLoader.loadImageSync(getWebpUrl(url), size);
    }

    public static Bitmap getBitmapByLocal(String url, ImageSize size) {
//        return imageLoader.loadImageSync(url, size);
        if (url == null || url.endsWith("null")) {
            return null;
        }
        return imageLoader.loadImageSync(url, size);
    }


    public static Bitmap getBitmap(String url) {
//        return imageLoader.loadImageSync(url, size);
        if (url == null || url.endsWith("null")) {
            return null;
        }
        return imageLoader.loadImageSync(getWebpUrl(url));
    }

    public static Bitmap loadImageSync(String url) {
        if (url == null || url.endsWith("null")) {
            return null;
        }
//        return imageLoader.loadImageSync(url);
        return imageLoader.loadImageSync(getWebpUrl(url));
    }

    /**
     * 从drawable中异步加载本地图片
     *
     * @param imageId
     * @param imageView
     */
    public static void displayFromDrawable(int imageId, ImageView imageView) {
        imageLoader.displayImage("drawable://" + imageId,
                imageView);
    }


    public static ImageLoader getImageLoader() {
        return imageLoader;
    }
}
