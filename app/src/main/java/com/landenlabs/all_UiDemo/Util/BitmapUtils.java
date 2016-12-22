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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RSRuntimeException;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

/**
 * Created by Dennis Lang on 8/8/16.
 *
 * debug v8  image2 500,xxx,xxx Msec immage 1789 x 1527
 * debug std fails
 */
public class BitmapUtils {

    /**
     * Create bitmap with blur effect.
     *
     * @param context
     *            application context
     * @param resId
     *            bitmap resource id
     * @param radius
     *            the radius of the blur. Supported range 0 < radius <= 25
     * @return bitmap that has been added blur effect
     */
    public static Bitmap createBlurBitmap(Context context, int resId, int radius) {
        return addBlurEffect(context, BitmapFactory.decodeResource(context.getResources(), resId), radius);
    }

    /**
     * Create bitmap with blur effect.
     *
     * @param context
     *            application context
     * @param bitmap
     *            PNG bitmap to convert
     * @param radius
     *            the radius of the blur. Supported range 0 < radius <= 25
     * @return bitmap that has been added blur effect
     */
    public static Bitmap createBlurBitmap(Context context, Bitmap bitmap, int radius) {
        try {
            if (radius <= 25) {
                return addBlurEffect(context, bitmap, radius);
            } else {
                Bitmap blurred = bitmap;
                int pass = radius / 8;
                for (int idx = 0; idx < pass; idx++) {
                    blurred = addBlurEffect(context, blurred, 8);
                }
                return blurred;
            }
        } catch (RSRuntimeException rsex) {
            rsex.printStackTrace();
            return bitmap;
        }
    }

    /**
     * Convert bitmap format to <code>Bitmap.Config.ARGB_8888</code>.
     *
     * @param img
     *            JPEG bitmap to convert
     * @return {@link Bitmap} in ARGB8888 format
     */
    public static Bitmap convertBitmapFormatToARGB888(Bitmap img) {
        int numPixels = img.getWidth() * img.getHeight();
        int[] pixels = new int[numPixels];

        // Get JPEG pixels. Each int is the color values for one pixel.
        img.getPixels(pixels, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());

        // Create a Bitmap of the appropriate format.
        Bitmap result = Bitmap.createBitmap(img.getWidth(), img.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Set RGB pixels.
        result.setPixels(pixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());
        return result;
    }

    /**
     * Create bitmap with blur effect. Use support render script.
     *
     * @param context
     *            application context
     * @param bitmap
     *            PNG bitmap to convert
     * @param radius
     *            the radius of the blur. Supported range 0 < radius <= 25
     * @return bitmap that has been added blur effect
     */
    private static Bitmap addBlurEffect(Context context, Bitmap bitmap, int radius) {
        if ((radius < 0) || (radius > 25)) {
            throw new IllegalArgumentException("Blur radius must be in range [0,25]");
        }

        if (null == bitmap) {
            return null;
        }

        /**
         * Support render script library has bug with blur effect on down
         * versions than <code>Build.VERSION_CODES.JELLY_BEAN</code>. To fix
         * this bug need convert bitmap format to
         * <code>Bitmap.Config.ARGB_8888</code>.
         */
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            bitmap = convertBitmapFormatToARGB888(bitmap);
        }

        Bitmap blurBitmap = bitmap.copy(bitmap.getConfig(), true);

        final RenderScript rs = RenderScript.create(context);
        final Allocation input = Allocation.createFromBitmap(rs, bitmap,
                Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setRadius(radius);
        script.setInput(input);
        script.forEach(output);
        output.copyTo(blurBitmap);

        return blurBitmap;
    }

    /**
     * Adds tint effect to target bitmap.
     *
     * @param src
     *            PNG bitmap to apply tint affect
     * @param argb
     *            tint color
     * @return bitmap with tint affect
     */
    public static Bitmap addTintEffect(Bitmap src, int argb) {
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(argb, PorterDuff.Mode.SRC_OVER));

        Canvas bottomCanvas = new Canvas(src);
        bottomCanvas.drawBitmap(src, 0, 0, paint);

        return src;
    }

    /**
     * Create darkened version of input drawable offset.
     *
     * @param res
     * @param inImage
     * @param offsetX
     * @param offsetY
     * @param blurRadius   Not currently used.
     * @param shadowColor
     * @return
     */
    public static BitmapDrawable shadowImage(Resources res, Drawable inImage, int offsetX, int offsetY, float blurRadius, int shadowColor) {

        Bitmap inBitmap;
        if (inImage instanceof BitmapDrawable) {
            inBitmap = ((BitmapDrawable)inImage).getBitmap();
        } else {
            // Bitmap from drawable
            int imgWidth = inImage.getIntrinsicWidth();
            int imgHeight = inImage.getIntrinsicHeight();
            inBitmap = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);
            Canvas bottomCanvas = new Canvas(inBitmap);
            inImage.setBounds(0, 0, bottomCanvas.getWidth(), bottomCanvas.getHeight());
            inImage.draw(bottomCanvas);
        }

        Bitmap blurBitmap = shadowBitmap(inBitmap, offsetX, offsetY, blurRadius, shadowColor);
        return new BitmapDrawable(res, blurBitmap);
    }

    /**
     * Draw input bitmap in shadow color and offset. Assumes offset is positive.
     *
     * @param inBitmap
     * @param offsetX
     * @param offsetY
     * @param blurRadius        Not used
     * @param shadowColor
     * @return
     */
    public static Bitmap shadowBitmap(Bitmap inBitmap, int offsetX, int offsetY, float blurRadius, int shadowColor) {

        int imgWidth = inBitmap.getWidth();
        int imgHeight = inBitmap.getHeight();

        Bitmap bottomBm = Bitmap.createBitmap(imgWidth, imgHeight, Bitmap.Config.ARGB_8888);
        Canvas bottomCanvas = new Canvas(bottomBm);
        // bottomCanvas.drawColor(shadowColor);

        Paint bottomPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bottomPaint.setColor(shadowColor);
        bottomCanvas.drawRect(offsetX, offsetY, imgWidth, imgHeight, bottomPaint);

    //    MaskFilter filter = new BlurMaskFilter(Math.max(0.5f, blurRadius), BlurMaskFilter.Blur.NORMAL);
        bottomPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
    //    bottomPaint.setShadowLayer(blurRadius, offsetX, offsetX, shadowColor);
    //    bottomPaint.setMaskFilter(filter);
        bottomCanvas.drawBitmap(inBitmap, offsetX, offsetY, bottomPaint);

        return bottomBm;
    }


    /**
     * http://stackoverflow.com/questions/2067955/fast-bitmap-blur-for-android-sdk
     *
     * Stack Blur v1.0 from
     * http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
     * Java Author: Mario Klingemann <mario at quasimondo.com>
     * http://incubator.quasimondo.com
     *
     * created Feburary 29, 2004
     * Android port : Yahel Bouaziz <yahel at kayenko.com>
     * http://www.kayenko.com
     * ported april 5th, 2012
     *
     * This is a compromise between Gaussian Blur and Box blur
     * It creates much better looking blurs than Box Blur, but is
     * 7x faster than my Gaussian Blur implementation.
     *
     * I called it Stack Blur because this describes best how this
     * filter works internally: it creates a kind of moving stack
     * of colors whilst scanning through the image. Thereby it
     * just has to add one new block of color to the right side
     * of the stack and remove the leftmost color. The remaining
     * colors on the topmost layer of the stack are either added on
     * or reduced by one, depending on if they are on the right or
     * on the left side of the stack.
     *
     * If you are using this algorithm in your code please add
     * the following line:
     * Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>
     */

    public Bitmap fastblur(Bitmap sentBitmap, float scale, int radius) {

        int width = Math.round(sentBitmap.getWidth() * scale);
        int height = Math.round(sentBitmap.getHeight() * scale);
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false);

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    //
    //

}
