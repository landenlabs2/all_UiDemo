/**
 * Copyright (c) 2015 Dennis Lang (LanDen Labs) landenlabs@gmail.com
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Dennis Lang  (3/21/2015)
 * @see http://landenlabs.com
 */
package com.landenlabs.all_UiDemo.frag;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Ui;

/**
 * Demonstrate Image Scale modes.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class ImageScalesFrag  extends UiFragment implements View.OnClickListener  {

    View mRootView;
    ViewGroup mImageHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.image_scales, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.image_scales_id;
    }

    @Override
    public String getName() {
        return "ImageScales";
    }

    @Override
    public String getDescription() {
        return "??";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_size_50:
                setImage(R.drawable.image50);
                break;
            case R.id.image_size_220: // 220x124 = 16:9
                setImage(R.drawable.image_16_9);
                break;
            case R.id.image_size_100w300h:
                setImage(R.drawable.image100w300h);
                break;
            case R.id.image_size_300w100h:
                setImage(R.drawable.image300w100h);
                break;
            case R.id.image_size_200:
                setImage(R.drawable.image200);
                break;
            case R.id.image_size_400:
                setImage(R.drawable.image400);
                break;
            case R.id.image_size_vec:
                setImage(R.drawable.scr_home);
                break;
        }
    }

    ImageView mUpperLeftFill;
    ImageView mLowerLeftFill;
    View mCenterFill;
    Bitmap mPrevBm1;
    Bitmap mPrevBm2;
    Bitmap mPrevBm3;

    private void setup() {
        mImageHolder = Ui.viewById(mRootView, R.id.image_holder);
        mUpperLeftFill = Ui.viewById(mRootView, R.id.image_upperLeftFill);
        mLowerLeftFill = Ui.viewById(mRootView, R.id.image_lowerLeftFill);
        mCenterFill = Ui.viewById(mRootView, R.id.view_centerFill);

        Ui.viewById(mRootView, R.id.image_size_50).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.image_size_220).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.image_size_vec).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.image_size_100w300h).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.image_size_300w100h).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.image_size_200).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.image_size_400).setOnClickListener(this);

        mImageHolder.post(new Runnable() {
            @Override
            public void run() {
                setImage(R.drawable.image200);
            }
        });

    }

    private void setImage(ImageView imageView, int imageRes) {
        if (false &&  imageRes == R.drawable.scr_home) {
            // ScaleDrawable does nothing.
            setScaledVector(imageView, imageRes);
        } else {
            imageView.setImageResource(imageRes);
        }
    }

    private void setScaledVector(ImageView imageView, int imageRes) {

        Drawable rawDrawable;
        if (Build.VERSION.SDK_INT >= 21) {
            rawDrawable = getResources().getDrawable(imageRes, getTheme());
        } else {
            rawDrawable = getResources().getDrawable(imageRes);
        }

        Drawable drawable = rawDrawable.mutate();
        /*
        int pad = 50;
        drawable.setBounds(0, pad, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        */

        // ScaleDrawable does nothing !!!!
        if (false) {
            ScaleDrawable scaleDrawable = new ScaleDrawable(rawDrawable, Gravity.TOP | Gravity.LEFT, 2.0f, 0.8f);
            scaleDrawable.setLevel(8000);
            drawable = scaleDrawable.getDrawable();
        }

        if (false) {
            int tintColor = 0x80ff0000;
            ColorStateList colorStateList = new ColorStateList(
                    new int[][]{new int[]{}},
                    new int[]{tintColor}
            );

            drawable = DrawableCompat.wrap(drawable.mutate());
            DrawableCompat.setTintList(drawable, colorStateList);
            DrawableCompat.setTintMode(drawable, PorterDuff.Mode.MULTIPLY);
        }

        //imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageDrawable(drawable);
        // imageView.setImageDrawable(null);
        // imageView.setBackgroundDrawable(drawable);
    }

    private void setImage(int imageRes) {
        for (int idx = 0; idx < mImageHolder.getChildCount(); idx++) {
            View view = mImageHolder.getChildAt(idx);
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView)view;
                if (imageView.getScaleType() != ImageView.ScaleType.MATRIX) {
                    setImage(imageView, imageRes);
                }
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup)view;
                for (int childIdx = 0; childIdx != viewGroup.getChildCount(); childIdx++) {
                    View child = viewGroup.getChildAt(childIdx);
                    if (child instanceof ImageView) {
                        ImageView imageView = (ImageView)child;
                        if (imageView.getScaleType() != ImageView.ScaleType.MATRIX) {
                            setImage(imageView, imageRes);
                        }
                    }
                }
            }
        }

        mPrevBm1 = fillUpperLeft(getDrawable(imageRes), mUpperLeftFill, mPrevBm1);
        mPrevBm2 = fillLowerLeft(getDrawable(imageRes), mLowerLeftFill, mPrevBm2);
        mPrevBm3 = setScaledImage(getDrawable(imageRes), mCenterFill, mPrevBm3);
    }

    private Bitmap fillUpperLeft(final Drawable drawable, ImageView imageView, Bitmap prevBm) {

        // Compute  matrix to fill viewer with drawable starting with upper left
        // Stretching till 2nd edge is crossed (filling screen) with correct aspect ratio.
        Matrix m = new Matrix();
        m.reset();
        int imgWidth = drawable.getIntrinsicWidth();
        int imgHeight = drawable.getIntrinsicHeight();
        int viewWidth = imageView.getWidth();
        int viewHeight = imageView.getHeight();
        float xScale = (float)viewWidth / imgWidth ;
        float yScale = (float)viewHeight / imgHeight;
        float maxScale = Math.max(xScale,  yScale);

        m.postScale(maxScale, maxScale);

        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        imageView.setImageMatrix(m);
        imageView.setImageDrawable(drawable);
        return prevBm;
    }

    private Bitmap fillLowerLeft(final Drawable drawable, ImageView imageView, Bitmap prevBm) {

        // Compute  matrix to fill viewer with drawable starting with upper left
        // Stretching till 2nd edge is crossed (filling screen) with correct aspect ratio.
        Matrix m = new Matrix();
        m.reset();
        int imgWidth = drawable.getIntrinsicWidth();
        int imgHeight = drawable.getIntrinsicHeight();
        int viewWidth = imageView.getWidth();
        int viewHeight = imageView.getHeight();
        float xScale = (float)viewWidth / imgWidth ;
        float yScale = (float)viewHeight / imgHeight;
        float maxScale = Math.max(xScale,  yScale);

        float dy = (imgHeight * maxScale - viewHeight) / maxScale;
        // dy = imgHeight / 2;
        m.preTranslate(0, -dy);
        m.postScale(maxScale, maxScale);

        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        imageView.setImageMatrix(m);
        imageView.setImageDrawable(drawable);
        return prevBm;
    }

    private Bitmap setScaledImage(final Drawable bgImage, View view, Bitmap prevScaled) {
        if (bgImage != null) {
            if (prevScaled != null)
                prevScaled.recycle();

            int screenWidthPx = Resources.getSystem().getDisplayMetrics().widthPixels;
            int viewHeightPx = view.getMeasuredHeight();

            // view.setBackgroundResource(bgImage);
            if (bgImage instanceof  BitmapDrawable) {
                BitmapDrawable bmDrawable = (BitmapDrawable) bgImage;
                Bitmap bmImage = bmDrawable.getBitmap();

                prevScaled = scaleCenterCrop(bmImage, screenWidthPx, viewHeightPx);
                view.setBackgroundDrawable(new BitmapDrawable(prevScaled));
            } else {

                // TODO - compute scale factor from screenWidthPx and viewHeightx
                Drawable scaleDrawable = new ScaleDrawable(bgImage, Gravity.NO_GRAVITY, 1.0f, 0.1f);
                view.setBackgroundDrawable(scaleDrawable);
            }
        }

        return prevScaled;
    }

    /**
     * Scale using Center Crop (source scaled till filling both new dimensions)
     * @param source
     * @param newWidth
     * @param newHeight
     * @return Center Crop scaled image.
     */
    public static Bitmap scaleCenterCrop2(Bitmap source, int newWidth, int newHeight) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the scaled source bitmap
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        if (newWidth > 0 &&  newHeight > 0) {
            // Finally, we create a new bitmap of the specified size and draw our new,
            // scaled bitmap onto it.
            Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
            Canvas canvas = new Canvas(dest);
            canvas.drawBitmap(source, null, targetRect, null);
            return dest;
        }

        return null;
    }

    public static Bitmap scaleCenterCrop(Bitmap source,  int newWidth, int newHeight) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        double targetAspect = (double)newWidth / newHeight;
        double sourceAspect = (double)sourceWidth / sourceHeight;

        int left = 0;
        int top = 0;
        if (sourceAspect >= targetAspect) {
            // Souce is wider then we need, compute side offset
            left = (int)((sourceWidth - targetAspect * sourceHeight) / 2);
        } else {
            // Source is taller then we need cmopute top and bottom offset.
            top = (int)((sourceHeight - sourceWidth / targetAspect) / 2);
        }

        // Source and target rectangle for the scaled  bitmap
        Rect sourceRect = new Rect(left, top, sourceWidth - left, sourceHeight - top);
        Rect targetRect = new Rect(0, 0, newWidth, newHeight);

        if (newWidth > 0 &&  newHeight > 0) {
            // Finally, we create a new bitmap of the specified size and draw our new,
            // scaled bitmap onto it.
            Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
            Canvas canvas = new Canvas(dest);
            canvas.drawBitmap(source, sourceRect, targetRect, null);
            return dest;
        }

        return null;
    }

}
