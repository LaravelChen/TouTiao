package com.example.laravelchen.toutiao.extra;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by LaravelChen on 2017/6/15.
 */

public class ImgLoader {
    private ImageLoader imageLoader;
    private DisplayImageOptions displayImageOptions;
    private ImageLoaderConfiguration imageLoaderConfiguration;

    public ImgLoader(Context context) {
        displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        imageLoaderConfiguration = new ImageLoaderConfiguration.Builder(context).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(imageLoaderConfiguration);
    }

    public void disPlayimg(String url, ImageView imageView) {
        imageLoader.displayImage(url, imageView, displayImageOptions);
    }
}
