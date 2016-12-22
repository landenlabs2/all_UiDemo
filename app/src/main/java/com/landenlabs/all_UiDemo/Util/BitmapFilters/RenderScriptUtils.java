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
package com.landenlabs.all_UiDemo.Util.BitmapFilters;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.util.Log;

import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Util.ScriptC_horzblur;
import com.landenlabs.all_UiDemo.Util.ScriptC_tint;
import com.landenlabs.all_UiDemo.Util.ScriptC_vertblur;


/**
 * Bitmap pixel filters, such as blur and tint.
 *
 * @author Dennis Lang
 *
 */
public class RenderScriptUtils {
    private static final String TAG = RenderScriptUtils.class.getSimpleName();

    // ------------------------------------------------------------------------------------------
    /**
     * Box image blur using RenderScript
     *
     * Found on web - by Tobias Ritzau
     * http://stackoverflow.com/questions/13435561/android-blur-bitmap-instantly
     *
     */
    public static class Blur implements IBlur {

        private RenderScript mRS;

        private ScriptC_horzblur mHorizontalScript;
        private ScriptC_vertblur mVerticalScript;

        public Blur(Context context) {
            mRS = RenderScript.create(context);

            mHorizontalScript = new ScriptC_horzblur(mRS, mRS.getApplicationContext()
                    .getResources(), R.raw.horzblur);
            mVerticalScript = new ScriptC_vertblur(mRS, mRS.getApplicationContext().getResources(),
                    R.raw.vertblur);
        }

        public Bitmap blur(Bitmap src, int radius) {
            Bitmap dst = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

            try {
                blur(src, dst, radius);
            } catch (RSRuntimeException ex) {
                Log.e(TAG, "Blur :: renderScript crashed", ex);
            } catch (Exception ex) {
                Log.e(TAG, "Blur :: renderScript crashed", ex);
            }
            return dst;
        }

        private void hblur(int radius, int width, int height, Allocation rowIndexes, Allocation in, Allocation out) {
            mHorizontalScript.set_Radius(radius);
            // mHorizontalScript.set_xDim(width);
            // mHorizontalScript.set_yDim(height);
            // mHorizontalScript.set_numSamples(2 * radius +1);
            // mHorizontalScript.set_invN(1.0f / mHorizontalScript.get_numSamples());
            mHorizontalScript.set_inImage(in);
            mHorizontalScript.set_outImage(out);
            mHorizontalScript.invoke_init_calc();

            // Automatically partitions work across available processing cores on the device
            mHorizontalScript.forEach_root(rowIndexes);
        }

        private void vblur(int radius, int width, int height, Allocation columnIndexes, Allocation in, Allocation out) {
            mVerticalScript.set_Radius(radius);
            // mVerticalScript.set_xDim(width);
            // mVerticalScript.set_yDim(height);
            // mVerticalScript.set_numSamples(2 * radius +1);
            // mVerticalScript.set_invN(1.0f / mVerticalScript.get_numSamples());
            mVerticalScript.set_inImage(in);
            mVerticalScript.set_outImage(out);
            mVerticalScript.invoke_init_calc();

            // Automatically partitions work across available processing cores on the device
            mVerticalScript.forEach_root(columnIndexes);
        }

        // Return array populated with values 0..size-1
        private Allocation createIndex(int size) {
            Element element = Element.U16(mRS);
            Allocation allocation = Allocation.createSized(mRS, element, size);
            short[] indexes = new short[size];
            for (int idx = 0; idx < indexes.length; idx++)
                indexes[idx] = (short) idx;
            allocation.copyFrom(indexes);

            return allocation;
        }

        private void blur(Bitmap src, Bitmap dst, int radius) {
            Allocation srcImage = Allocation.createFromBitmap(mRS, src, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            Allocation tmpImage = Allocation.createTyped(mRS, srcImage.getType(), Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);

            int width = src.getWidth();
            int height = src.getHeight();

            Allocation rowIndexes = createIndex(height);    // srcImage.getType().getY());
            Allocation columnIndexes = createIndex(width);  // srcImage.getType().getX());

            // Add more iterations if you like or simply make a loop
            vblur(radius, width, height, columnIndexes, srcImage, tmpImage);
            srcImage.destroy();
            Allocation dstImage = Allocation.createFromBitmap(mRS, dst, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            hblur(radius, width, height, rowIndexes, tmpImage, dstImage);

            dstImage.copyTo(dst);

            tmpImage.destroy();
            dstImage.destroy();
        }
    }

    // ------------------------------------------------------------------------------------------
    /**
     * Tint image using Render Script.
     *
     * @author Dennis Lang
     *
     */
    public static class Tint {

        private RenderScript mRS;
        private ScriptC_tint mTintScript;

        public Tint(Context context) {
            mRS = RenderScript.create(context);
            mTintScript = new ScriptC_tint(mRS, mRS.getApplicationContext().getResources(),
                    R.raw.tint);
        }

        public Bitmap tint(Bitmap src, int argb) {
            Bitmap dst = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());

            tint(src, dst, argb);

            return dst;
        }

        private void tint(Bitmap src, Bitmap dst, int argb) {
            Allocation inImage = Allocation.createFromBitmap(mRS, src,
                    Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
            Allocation outImage = Allocation.createTyped(mRS, inImage.getType());

            // mTintScript = new ScriptC_tint(mRS, getResources(), R.raw.mono);

            mTintScript.set_gIn(inImage);
            mTintScript.set_gOut(outImage);
            mTintScript.set_gScript(mTintScript);
            mTintScript.set_gAlpha(getA(argb));
            mTintScript.set_gRed(getR(argb));
            mTintScript.set_gGreen(getG(argb));
            mTintScript.set_gBlue(getB(argb));

            mTintScript.invoke_filter();
            outImage.copyTo(dst);
        }
    }

    // Get alpha from 32bit color
    public static float getA(int argb) {
        return ((argb >> 24) & 0xff) / 255.0f;
    }

    // Get red from 32bit color
    public static float getR(int argb) {
        return ((argb >> 16) & 0xff) / 255.0f;
    }

    // Get green from 32bit color
    public static float getG(int argb) {
        return ((argb >> 8) & 0xff) / 255.0f;
    }

    // Get blue from 32bit color
    public static float getB(int argb) {
        return ((argb >> 0) & 0xff) / 255.0f;
    }
}
