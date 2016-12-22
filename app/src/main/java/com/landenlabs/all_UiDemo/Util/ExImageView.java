/**
 * Copyright (c) 2015 Dennis Lang (LanDen Labs) landenlabs@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Dennis Lang  (3/21/2015)
 * @see http://landenlabs.com
 *
 */
package com.landenlabs.all_UiDemo.Util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Debug version of ImageView to log scaling of images.
 *
 * Created by Dennis Lang on 7/4/16.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */
public class ExImageView extends ImageView {

    int mImageResID;

    public ExImageView(Context context) {
        super(context);
    }

    public ExImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setImageResource(int resId) {
        mImageResID = resId;
        super.setImageResource(resId);
    }

    public int getResourceId() {
        return mImageResID;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        long startMillis = System.currentTimeMillis();
        try {
            super.onDraw(canvas);
        } catch (Exception ex) {
            Log.e("foo", ex.getLocalizedMessage() + "\n" + getContentDescription() + " id=" + mImageResID, ex);
            return;
        }
        long endMillis = System.currentTimeMillis();

        Matrix matrix = getImageMatrix();
        if (matrix.isIdentity()) {
            // No scaling
        } else {
            float[] values = new float[9];
            matrix.getValues(values);
            float scaleX = values[Matrix.MSCALE_X];
            float scaleY = values[Matrix.MSCALE_Y];

            Log.d("foo", String.format("Res:%d W:%d H:%d Scale X:%.2f Y:%.2f",
                    mImageResID, getWidth(), getHeight(), scaleX, scaleY));
        }
    }
}
