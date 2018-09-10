package com.moudle.proxy.image;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moudle.proxy.imageproxy.ImageLoader;

/**
 * Created by Administrator on 2018/8/8.
 */

public class GlideImageLoader implements ImageLoader{

    @Override
    public void showImageLoader(Activity activity, String imageUrl, ImageView imageView) {
        Glide.with(activity).load(imageUrl).into(imageView);
    }
}
