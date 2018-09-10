package com.moudle.proxy.imageproxy;

import android.app.Activity;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/8/8.
 */

public interface ImageLoader {

    /**
     *
     * @param activity
     * @param imageUrl
     * @param imageView
     */
    public void showImageLoader(Activity activity, String imageUrl, ImageView imageView);
}
