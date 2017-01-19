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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Ui;

/**
 * Demonstrate Image Blend using PorterDuff modes.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class ImageBlendFrag  extends UiFragment implements View.OnClickListener  {

    View mRootView;
    ViewGroup mImageHolder;
    int mColor = Color.RED;
    int mSrcImg = R.drawable.image100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.image_blend, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.image_blend_id;
    }

    @Override
    public String getName() {
        return "ImageBlend";
    }

    @Override
    public String getDescription() {
        return "??";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blend_red:
                mColor = Color.RED;
                setImage(mSrcImg);
                break;
            case R.id.blend_green:
                mColor = Color.GREEN;
                setImage(mSrcImg);
                break;
            case R.id.blend_blue:
                mColor = Color.BLUE;
                setImage(mSrcImg);
                break;
            case R.id.blend_img1:
                mColor = 0;
                setImage(mSrcImg, R.drawable.checkmark5);
                break;
            case R.id.blend_img2:
                mColor = 0;
                setImage(mSrcImg, R.drawable.image_a);
                break;
            case R.id.blend_img3:
                mColor = 0;
                setImage(mSrcImg, R.drawable.image_e);
                break;
            case R.id.blend_img4:
                mColor = 0;
                setImage(mSrcImg, R.drawable.uidemo_sm);
                break;
        }
    }

    private void setup() {
        mImageHolder = Ui.viewById(mRootView, R.id.image_holder);

        Ui.viewById(mRootView, R.id.blend_red).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.blend_green).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.blend_blue).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.blend_img1).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.blend_img2).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.blend_img3).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.blend_img4).setOnClickListener(this);

        mImageHolder.removeAllViews();
        addImages();
        setImage(mSrcImg, R.drawable.uidemo_sm);
    }

    private void setImage(int imageRes) {
        for (int idx = 0; idx < mImageHolder.getChildCount(); idx++) {
            View view = mImageHolder.getChildAt(idx);
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView)view;
                imageView.setImageResource(imageRes);
                PorterDuff.Mode mode = (PorterDuff.Mode)imageView.getTag();
                imageView.setColorFilter(mColor, mode);
            }
        }
    }

    private void setImage(int imageSrcRes, int imageDstRes) {
        for (int idx = 0; idx < mImageHolder.getChildCount(); idx++) {
            View view = mImageHolder.getChildAt(idx);
            if (view instanceof ImageView) {
                ImageView imageView = (ImageView)view;
                imageView.setColorFilter(null);

                PorterDuff.Mode mode = (PorterDuff.Mode)imageView.getTag();
                Bitmap srcBm = BitmapFactory.decodeResource(getResources(), imageSrcRes);
                Bitmap dstBm = BitmapFactory.decodeResource(getResources(), imageDstRes);
                Bitmap resultBm = Bitmap.createBitmap(srcBm.getWidth(), srcBm.getHeight(), Bitmap.Config.ARGB_8888);

                Canvas resultCanvas = new Canvas(resultBm);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                Rect srcRect = new Rect(0, 0, srcBm.getWidth(), srcBm.getHeight());
                resultCanvas.drawBitmap(srcBm, 0, 0, paint);

                paint.setXfermode(new PorterDuffXfermode(mode));
                Rect dstRect = new Rect(0, 0, dstBm.getWidth(), dstBm.getHeight());
                resultCanvas.drawBitmap(dstBm, dstRect, srcRect, paint);

                imageView.setImageBitmap(resultBm);
            }
        }
    }

    private void addImages() {
        TextView tv;
        ImageView iv;

        addImage(PorterDuff.Mode.SRC);
        addImage(PorterDuff.Mode.DST);
        addImage(PorterDuff.Mode.SRC_IN);
        addImage(PorterDuff.Mode.DST_IN);
        addImage(PorterDuff.Mode.SRC_OUT);
        addImage(PorterDuff.Mode.DST_OUT);
        addImage(PorterDuff.Mode.SRC_ATOP);
        addImage(PorterDuff.Mode.DST_ATOP);
        addImage(PorterDuff.Mode.SRC_OVER);
        addImage(PorterDuff.Mode.DST_OVER);
        addImage(PorterDuff.Mode.ADD);
        addImage(PorterDuff.Mode.DARKEN);
        addImage(PorterDuff.Mode.LIGHTEN);
        addImage(PorterDuff.Mode.MULTIPLY);
        addImage(PorterDuff.Mode.SCREEN);
        addImage(PorterDuff.Mode.XOR);
    }

    private void addImage(PorterDuff.Mode mode) {
        TextView tv = new TextView(mImageHolder.getContext());
        tv.setBackgroundResource(R.color.rowtx);
        tv.setGravity(Gravity.CENTER);
        // tv.setTextAppearance(R.style.TextAppearanceWhite20);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tv.setTextColor(Color.WHITE);
        tv.setText(mode.toString());
        tv.setPadding(5,5,5,5);
        mImageHolder.addView(tv);
        ImageView iv = new ImageView(mImageHolder.getContext());
        iv.setBackgroundResource(R.color.row0);
        mImageHolder.addView(iv);
        iv.setColorFilter(mColor, mode);
        iv.setTag(mode);
    }
}
