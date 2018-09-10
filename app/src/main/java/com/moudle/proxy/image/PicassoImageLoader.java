package com.moudle.proxy.image;

import android.app.Activity;
import android.widget.ImageView;

import com.moudle.proxy.imageproxy.ImageLoader;
import com.squareup.picasso.Picasso;

/**
 * Created by Administrator on 2018/8/8.
 */

public class PicassoImageLoader implements ImageLoader {

    @Override
    public void showImageLoader(Activity activity, String imageUrl, ImageView imageView) {
        Picasso.get().load(imageUrl).into(imageView);
    }
}
