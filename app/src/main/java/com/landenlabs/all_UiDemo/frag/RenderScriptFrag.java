
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
package com.landenlabs.all_UiDemo.frag;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Ui;
import com.landenlabs.all_UiDemo.Util.BitmapFilters.IBlur;
import com.landenlabs.all_UiDemo.Util.BitmapFilters.RenderScriptUtils;
import com.landenlabs.all_UiDemo.Util.BitmapUtils;


/**
 * Demonstrate view shadows.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class RenderScriptFrag  extends UiFragment
        implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    View mRootView;
    ViewGroup mImageScroller;
    
    TextView mRadiusTv, mScaleTv;
    SeekBar mRadiusSb, mScaleSb;

    ImageView mImage1, mImage2, mImage3;
    TextView mTime1Tv, mTime2Tv, mImageDimTv;
    TextView mMemTv;

    CheckBox mCustomBlurCk, mStdBlurCk;

    final int mSeekMax = 255;
    final int mMaxRadius = 255;
    int mRadius = 10;

    final int mMaxScale = 100;
    int mScale = 10;

    RenderScriptUtils.Blur mBlur;
    Bitmap  mSrcBitmap;

    /**
     * GPS location processing async task.
     *
     * @author Maksym Trostyanchuk
     */
    private class BlurAsyncTask extends AsyncTask<Void, Void, Bitmap> {

        private final IBlur mBlur;
        private final Bitmap mInBitmap;
        private final int mRadius;
        private final ImageView mImageView;

        private BlurAsyncTask(IBlur blur, Bitmap inBitmap, int radius, ImageView showResult) {
            super();
            mBlur = blur;
            mInBitmap = inBitmap;
            mRadius = radius;
            mImageView = showResult;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return mBlur.blur(mInBitmap, mRadius);
        }

        @Override
        protected void onPostExecute(Bitmap outBitmap) {
            mImageView.setImageBitmap(outBitmap);
        }
    }

    public  static int ArrayFind(int[] array, int find) {
        for (int idx = 0; idx < array.length; idx++){
            if (array[idx] == find)
                return idx;
        }

        return -1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.renderscript_frag, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.renderscript_id;
    }

    @Override
    public String getName() {
        return "RenderScript";
    }

    @Override
    public String getDescription() {
        return "RS Blur";
    }


    private void setup() {
        final View titleView = Ui.viewById(mRootView, R.id.title);
        
        mImageScroller = Ui.viewById(mRootView, R.id.rs_scroller);
        mTime1Tv = Ui.viewById(mRootView, R.id.rs_blur1Time);
        mTime2Tv = Ui.viewById(mRootView, R.id.rs_blur2Time);
        mImageDimTv = Ui.viewById(mRootView, R.id.rs_image_dim_tv);
        mMemTv = Ui.viewById(mRootView, R.id.rs_memory);

        mCustomBlurCk = Ui.viewById(mRootView, R.id.rs_blurCustomCk);
        mStdBlurCk = Ui.viewById(mRootView, R.id.rs_blurStdCk);
        mCustomBlurCk.setOnClickListener(this);
        mStdBlurCk.setOnClickListener(this);

        Ui.viewById(mRootView, R.id.rs_image1rb).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.rs_image2rb).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.rs_image3rb).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.rs_image4rb).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.rs_image5rb).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.rs_image6rb).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.rs_image7rb).setOnClickListener(this);

        mImage1 = Ui.viewById(mRootView, R.id.rs_image1);
        mImage2 = Ui.viewById(mRootView, R.id.rs_image2);
        mImage3 = Ui.viewById(mRootView, R.id.rs_image3);

        mRadiusSb = Ui.viewById(mRootView, R.id.rs_radius_sb);
        mRadiusTv = Ui.viewById(mRootView, R.id.rs_radius_tv);

        mScaleSb = Ui.viewById(mRootView, R.id.rs_scale_sb);
        mScaleTv = Ui.viewById(mRootView, R.id.rs_scale_tv);

        setPosSb(mRadiusSb, mRadius, mMaxRadius);
        mRadiusSb.setOnSeekBarChangeListener(this);

        setPosSb(mScaleSb, mScale, mMaxScale);
        mScaleSb.setOnSeekBarChangeListener(this);

        updateView();
    }

    private void setPosSb(SeekBar seekBar, int value, int maxValue) {
        // int iVal = (int)((maxXYZ/2 - value) * mSeekMax/maxXYZ);
        int iVal = value * mSeekMax / maxValue;
        seekBar.setProgress(iVal);
    }
    private int getPosSb(SeekBar seekBar, int maxValue) {
        // return maxXYZ/2 - seekBar.getProgress() *  maxXYZ / mSeekMax;
        return seekBar.getProgress() * maxValue / mSeekMax;
    }


    private void updateScale() {
        mScale = getPosSb(mScaleSb, mMaxScale);
        mScaleTv.setText(String.format("Scale:%d %%", mScale));

        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int displayWidth = size.x;
        int displayHeight = size.y;

        ViewGroup.LayoutParams lp = mImageScroller.getLayoutParams();
        lp.height = displayWidth * mScale / 100;
        mImageScroller.setLayoutParams(lp);
    }

    /**
     * Execute blur on selected image.
     */
    private void updateView() {

        updateScale();
        mRadius = getPosSb(mRadiusSb, mMaxRadius);
        mRadiusTv.setText(String.format("Radius:%d", mRadius));

        if (mBlur == null) {
            mBlur = new RenderScriptUtils.Blur(this.getContext());
            mSrcBitmap = ((BitmapDrawable)mImage1.getDrawable()).getBitmap();
        }

        final ProgressDialog progress = new ProgressDialog(getContext());
        progress.setTitle("Blurring...");
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        progress.show();

        final Context context = this.getContext();

        mRootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                long startNano;
                if (mCustomBlurCk.isChecked()) {

                    startNano = System.nanoTime();
                    Bitmap blurred2 = mBlur.blur(mSrcBitmap, mRadius);
                    mImage2.setImageBitmap(blurred2);
                    long blur1Nano = System.nanoTime() - startNano;
                    mTime1Tv.setText(String.format("CustomBlur: %,.2f Msec", blur1Nano / 1000.0f));
                } else {
                    mImage2.setImageBitmap(null);
                    mTime1Tv.setText("disabled");
                }

                if (mStdBlurCk.isChecked()) {
                    startNano = System.nanoTime();
                    try {
                        Bitmap blurred3 = BitmapUtils.createBlurBitmap(context, mSrcBitmap, mRadius);
                        mImage3.setImageBitmap(blurred3);
                    } catch (Exception ex) {
                        mImage3.setImageDrawable(new ColorDrawable(0xffff0000));
                    }
                    long blur2Nano = System.nanoTime() - startNano;
                    mTime2Tv.setText(String.format("StdBlur: %,.2f Msec", blur2Nano / 1000.0f));
                } else {
                    mImage3.setImageBitmap(null);
                    mTime2Tv.setText("disabled");
                }

                ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                ActivityManager activityManager = (ActivityManager)
                        getActivity().getSystemService(Context.ACTIVITY_SERVICE);
                activityManager.getMemoryInfo(mi);
                if (Build.VERSION.SDK_INT > 15) {
                    mMemTv.setText(String.format("Mem Avail:%,d Free:%,d", mi.availMem, mi.totalMem - mi.availMem));
                } else {
                    mMemTv.setText(String.format("Mem Avail:%,%d ", mi.availMem));
                }

                progress.dismiss();
            }
        }, 500);
    }


    // =============================================================================================
    // Seekbar onProgressChanged

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == mScaleSb) {
            updateScale();
        } else {
            updateView();
        }
    }
    public void onStartTrackingTouch(SeekBar seekBar) {
    }
    public void onStopTrackingTouch(SeekBar seekBar)  {
    }

    // =============================================================================================
    // View  onClick


    public void onClick(View view) {
        int id = view.getId();

        int imageRes = R.drawable.blur1;
        switch (id) {
            case R.id.rs_image1rb:
                imageRes = R.drawable.blur1;
                break;

            case R.id.rs_image2rb:
                imageRes = R.drawable.blur2;
                break;

            case R.id.rs_image3rb:
                imageRes = R.drawable.blur3;
                break;

            case R.id.rs_image4rb:
                imageRes = R.drawable.blur4;
                break;

            case R.id.rs_image5rb:
                imageRes = R.drawable.blur5;
                break;

            case R.id.rs_image6rb:
                imageRes = R.drawable.blur6;
                break;

            case R.id.rs_image7rb:
                imageRes = R.drawable.blur7;
                break;
        }

        // Image 7 goes diagonal if loaded from mdpi directory.
        mImage1.setImageResource(imageRes);

        // TODO - why does this image not blur correctly.
        // Image 7 works on nx7 if pulled out using density.
        mSrcBitmap = ((BitmapDrawable)getResources().getDrawableForDensity(imageRes, DisplayMetrics.DENSITY_DEFAULT)).getBitmap();

        // mSrcBitmap = ((BitmapDrawable)mImage1.getDrawable()).getBitmap();
        mImage1.setImageBitmap(mSrcBitmap);

        String dimStr = String.format("Dim:%d x %d PixelBytes:%d Density:%d %s",
                mSrcBitmap.getWidth(), mSrcBitmap.getHeight(),
                mSrcBitmap.getRowBytes() / mSrcBitmap.getWidth(),
                mSrcBitmap.getDensity(),
                mSrcBitmap.getConfig().name()
        );
        mImageDimTv.setText(dimStr);
        // Toast.makeText(view.getContext(), "Image\n" + dimStr, Toast.LENGTH_SHORT).show();

        updateView();
    }
}


