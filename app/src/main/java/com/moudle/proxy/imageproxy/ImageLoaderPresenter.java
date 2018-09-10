package com.moudle.proxy.imageproxy;

import android.app.Activity;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/8/8.
 */

public class ImageLoaderPresenter implements ImageLoader {

    private static ImageLoaderPresenter instance;
    private ImageLoader mImageLoader;
    public static void init(ImageLoader imageLoader){
        if(instance == null){
            synchronized (ImageLoaderPresenter.class){
                if(instance == null){
                    instance = new ImageLoaderPresenter(imageLoader);
                }
            }
        }
    }
    private ImageLoaderPresenter(ImageLoader imageLoader){
        this.mImageLoader = imageLoader;
    }

    public ImageLoaderPresenter getInstance(){
        return instance;
    }

    @Override
    public void showImageLoader(Activity activity, String imageUrl, ImageView imageView) {
      mImageLoader.showImageLoader(activity,imageUrl,imageView);
    }
}
