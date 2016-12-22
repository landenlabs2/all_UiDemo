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
import android.widget.ScrollView;
import android.widget.TextView;

import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Ui;

/**
 * Demonstrate Scrollview resizing with views above and below.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class ScrollResizeFrag  extends UiFragment implements View.OnClickListener {

    View mRootView;
    ViewGroup mDataHolder;
    ScrollView mScrollView;
    TextView mScrollIdxView;
    String[] mDataArray;
    int mDataIdx = 0;
    final int DataStartCnt = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.layout_scroll_resize, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.layout_scroll_resize_id;
    }

    @Override
    public String getName() {
        return "ScrollResize";
    }

    @Override
    public String getDescription() {
        return "??";
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.scroll_add:
                addItem();
                break;
            case R.id.scroll_del:
                delItem();
                break;
        }
    }

    private void setup() {
        mDataHolder = Ui.viewById(mRootView, R.id.scroll_holder);
        mDataArray = getResources().getStringArray(R.array.states);

        Ui.viewById(mRootView, R.id.scroll_add).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.scroll_del).setOnClickListener(this);
        mScrollView = Ui.viewById(mRootView, R.id.resizing_scrollview);
        mScrollIdxView = Ui.viewById(mRootView, R.id.scroll_idx);

        while (mDataIdx < DataStartCnt)
            addItem();
    }

    private void addItem() {
        mDataIdx = mDataIdx % mDataArray.length;
        TextView tv = new TextView(mDataHolder.getContext());
        tv.setText(String.format("%3d %s", mDataIdx, mDataArray[mDataIdx++]));
        tv.setTextSize(20.0f);
        mDataHolder.addView(tv);
        mScrollView.scrollBy(0, 20);
        mScrollIdxView.setText(String.format("Idx:%d Cnt:%d", mDataIdx, mDataHolder.getChildCount()));
        // mScrollView.fullScroll(View.FOCUS_DOWN);
    }

    private void delItem() {
        int cnt = mDataHolder.getChildCount();
        if (cnt > 0) {
            mDataHolder.removeViewAt(0);
            mScrollIdxView.setText(String.format("Idx:%d Cnt:%d", mDataIdx, mDataHolder.getChildCount()));
        }
    }
}
