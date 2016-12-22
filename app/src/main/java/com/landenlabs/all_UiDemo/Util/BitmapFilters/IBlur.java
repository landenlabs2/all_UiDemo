package com.landenlabs.all_UiDemo.Util.BitmapFilters;

import android.graphics.Bitmap;

/**
 * Created by Dennis Lang on 9/14/16.
 */
public interface IBlur {

    Bitmap blur(Bitmap src, int radius);
}
