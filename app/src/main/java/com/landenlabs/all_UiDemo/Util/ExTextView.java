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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.landenlabs.all_UiDemo.R;

/**
 * Extended TextView, has additional properties:
 * <pre>
 *    underline true|false  default false
 *    decenders true|false  default true
 * </pre>
 * underline enabled painting underline
 * <br>
 * decenders disabled shrinks height to baseline
 *
 * Created by Dennis Lang on 5/19/16.
 */

public class ExTextView extends TextView {

    boolean mHasDecenders = true;
    int mOrgHeight = 0;
    public ExTextView(Context context) {
        this(context, null);
    }

    public ExTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

        if (null != attrs) {
            TypedArray attributeArray = context.obtainStyledAttributes(
                    attrs,
                    R.styleable.TextAppearanceSwitch);

            if (attributeArray.getBoolean(R.styleable.TextAppearanceSwitch_underline, false)) {
                setPaintFlags(getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }

            mHasDecenders = attributeArray.getBoolean(R.styleable.TextAppearanceSwitch_descenders, true);
            if (!mHasDecenders) {
                // setPaintFlags(getPaintFlags() |Paint.STRIKE_THRU_TEXT_FLAG);
            }
            attributeArray.recycle();
        }
    }

    public ExTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ExTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public int getLineHeight() {
        return super.getLineHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!mHasDecenders) {
            int hgt = getHeight();
            int bl = getBaseline();
            if (hgt > bl) {
                mOrgHeight = hgt;
                setHeight(bl);
            }
        }
        super.onDraw(canvas);
    }
}
