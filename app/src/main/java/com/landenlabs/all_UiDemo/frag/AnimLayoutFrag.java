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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Ui;

/**
 * Demonstrate animated layout of images.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class AnimLayoutFrag  extends UiFragment {

    View mRootView;
    ViewGroup mLayout1;
    ViewGroup mLayout2;
    ViewGroup mLayout3;
    ViewGroup mLayout4;
    ViewGroup mLayout5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_anim, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.layout_anim_id;
    }

    @Override
    public String getName() {
        return "AnimLayout";
    }

    @Override
    public String getDescription() {
        return "??";
    }

    private void setup() {
        mLayout1 = Ui.viewById(mRootView, R.id.layout_anim1);
        mLayout2 = Ui.viewById(mRootView, R.id.layout_anim2);
        mLayout3 = Ui.viewById(mRootView, R.id.layout_anim3);
        mLayout4 = Ui.viewById(mRootView, R.id.layout_anim4);
        mLayout5 = Ui.viewById(mRootView, R.id.layout_anim5);

        updateLayout(mLayout1, 0xffff0000, 5, 5, 1);
        updateLayout(mLayout2, 0xff00ffff, 3, 6, 2);
        updateLayout(mLayout3, 0xff0000ff, 8, 8, 1);
        updateLayout(mLayout4, 0xffff00ff, 8, 8, 1);
        updateLayout(mLayout5, 0xffffff00, 8, 8, 1);
    }

    private void updateLayout(final ViewGroup layout, final int bgColor, final int minChild, final int maxChild, final int sec) {
        int childCnt = layout.getChildCount();

        if (childCnt < maxChild) {
            TextView tv = new TextView(layout.getContext());
            tv.setText("Test Text # " + layout.getChildCount());
            tv.setTextColor(bgColor);
            tv.setTextSize(30.0f);
            layout.addView(tv);
        } else {
            while (childCnt > minChild) {
                layout.removeViewAt(0);
                childCnt--;
            }
        }

        layout.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateLayout(layout, bgColor, minChild, maxChild, sec);
            }
        }, sec * 1000);
    }

}
