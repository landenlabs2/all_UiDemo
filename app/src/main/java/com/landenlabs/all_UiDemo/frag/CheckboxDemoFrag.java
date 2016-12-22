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
import android.widget.CheckedTextView;
import android.widget.CompoundButton;

import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrate Checkbox ui components.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */
public class CheckboxDemoFrag  extends UiFragment implements View.OnClickListener {

    protected View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.checkbox_right_demo, container, false);
        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.checkbox_demo_id;
    }

    @Override
    public String getName() {
        return "CheckboxDemo";
    }

    @Override
    public String getDescription() {
        return "??";
    }

    private List<View> mViewList = new ArrayList<>();

    private void addView(View view) {
        if (view != null) {
            mViewList.add(view);
            view.setOnClickListener(this);
        }
    }

    protected void setup() {

        addView(Ui.needViewById(mRootView, R.id.p2Button1));
        addView(Ui.viewById(mRootView, R.id.p2Button2));
        addView(Ui.needViewById(mRootView, R.id.p2TextView1));
        addView(Ui.viewById(mRootView, R.id.p2TextView2));

        addView(Ui.needViewById(mRootView, R.id.p2Checkbox1));
        addView(Ui.viewById(mRootView, R.id.p2Checkbox2));
        addView(Ui.viewById(mRootView, R.id.p2Checkbox3));
        addView(Ui.viewById(mRootView, R.id.p2Checkbox4));
        addView(Ui.needViewById(mRootView, R.id.p2Checkbox5));

        addView(Ui.needViewById(mRootView, R.id.p2ToggleButton1));
        addView(Ui.viewById(mRootView, R.id.p2ToggleButton2));
        addView(Ui.needViewById(mRootView, R.id.p2ImageButton1));

        addView(Ui.needViewById(mRootView, R.id. p2RadioButton1));
        addView(Ui.viewById(mRootView, R.id. p2RadioButton2));

        addView(Ui.needViewById(mRootView, R.id. p2Switch1));
        addView(Ui.viewById(mRootView, R.id. p2Switch2));

        Ui.needViewById(mRootView, R.id.p2Enable).setOnClickListener(this);
        Ui.needViewById(mRootView, R.id.p2Checked).setOnClickListener(this);
        Ui.needViewById(mRootView, R.id.p2Background).setOnClickListener(this);
    }

    private boolean isChecked(View view) {
        if (view instanceof CompoundButton)
            return ((CompoundButton)view).isChecked();
        else if (view instanceof CheckedTextView)
            return ((CheckedTextView)view).isChecked();
        else
            return view.isSelected();
    }

    private void setChecked(View view, boolean checked) {
        if (view instanceof CompoundButton)
            ((CompoundButton)view).setChecked(checked);
        else if (view instanceof CheckedTextView)
            ((CheckedTextView)view).setChecked(checked);
        else
            view.setSelected(checked);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.p2Button1:
            case R.id.p2Button2:
            case R.id.p2TextView1:
            case R.id.p2TextView2:
            case R.id.p2ImageButton1:
                // Fake checked notion by using selected state on Button and TextView
                view.setSelected(!view.isSelected());
                // view.playSoundEffect(SoundEffectConstants.CLICK);

                /*
                Drawable[] drawables = cBtn.getCompoundDrawables(); // left,top,right,bottom
                int[] chkState = new int[]{android.R.attr.state_checked, android.R.attr.state_enabled, android.R.attr.state_selected};
                drawables[2].setState(chkState);
                */
                break;

            case R.id.p2RadioButton1:
            case R.id.p2RadioButton2:
                boolean isDirty = view.isDirty();
                if (isDirty == false)
                    setChecked(view, !isChecked(view));
                break;

            case R.id.p2Checkbox3:
            case R.id.p2Checkbox4:
                // Silly - have to set checkbox manually when clicked.
                setChecked(view, !isChecked(view));
                break;

            // -- global  controls --
            case R.id.p2Enable:
                for (View viewItem : mViewList) {
                    viewItem.setEnabled(isChecked(view));
                }
                break;
            case R.id.p2Checked:
                for (View viewItem : mViewList) {
                    setChecked(viewItem, isChecked(view));
                }
                break;
            case R.id.p2Background:
                if (isChecked(view)) {
                    mRootView.setBackgroundResource(R.drawable.paper_white);
                } else {
                    mRootView.setBackgroundResource(R.drawable.paper_black);
                }
                break;
        }
    }
}