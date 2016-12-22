/*
 *  Copyright (c) 2015 Dennis Lang (LanDen Labs) landenlabs@gmail.com
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 *  associated documentation files (the "Software"), to deal in the Software without restriction, including
 *  without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 *  LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 *  NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 *  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 *  SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *  @author Dennis Lang  (Dec-2015)
 *  @see http://landenlabs.com
 *
 */
package com.landenlabs.all_UiDemo.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrate Toggle and Switch  ui components.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */
public class SwitchDemoFrag  extends UiFragment implements View.OnClickListener  {

    private View mRootView;
    private List<View> mViewList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.switch_demo, container, false);
        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.switch_demo_id;
    }

    @Override
    public String getName() {
        return "SwitchDemo";
    }

    @Override
    public String getDescription() {
        return "??";
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // -- global  controls --
            case R.id.enabledTb:
                for (View viewItem : mViewList) {
                    viewItem.setEnabled(isChecked(view));
                }
                break;
            case R.id.checkedTb:
                for (View viewItem : mViewList) {
                    setChecked(viewItem, isChecked(view));
                }
                break;
            case R.id.backgroundTb:
                if (isChecked(view)) {
                    Ui.needViewById(mRootView, R.id.switch_holder).setBackgroundResource(R.drawable.bg);
                } else {
                    Ui.needViewById(mRootView, R.id.switch_holder).setBackgroundResource(R.drawable.bg_dark);
                }
                break;
        }
    }

    private void setup() {
        addView(Ui.needViewById(mRootView, R.id.toggleButton1));
        addView(Ui.needViewById(mRootView, R.id.toggleButton2));
        addView(Ui.needViewById(mRootView, R.id.switchButton1));
        addView(Ui.needViewById(mRootView, R.id.switchButton2));
        addView(Ui.needViewById(mRootView, R.id.switchCompat1));
        addView(Ui.needViewById(mRootView, R.id.switchCompat2));
        addView(Ui.needViewById(mRootView, R.id.switchCompat3));

        Ui.needViewById(mRootView, R.id.enabledTb).setOnClickListener(this);
        Ui.needViewById(mRootView, R.id.checkedTb).setOnClickListener(this);
        Ui.needViewById(mRootView, R.id.backgroundTb).setOnClickListener(this);
    }

    private void addView(View view) {
        if (view != null) {
            mViewList.add(view);
            view.setOnClickListener(this);
        }
    }

    private boolean isChecked(View view) {
        if (view instanceof CompoundButton)
            return ((CompoundButton)view).isChecked();
        else
            return view.isSelected();
    }

    private void setChecked(View view, boolean checked) {
        if (view instanceof CompoundButton)
            ((CompoundButton)view).setChecked(checked);
        else
            view.setSelected(checked);
    }
}
