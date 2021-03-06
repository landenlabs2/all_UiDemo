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

import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Ui;


/**
 * Demonstrate GridLayout, Linear, Scroll, Percent layout of stuff.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class OtherLayoutFrag  extends UiFragment implements View.OnClickListener {

    View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.otherlayout_frag, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.otherlayout_id;
    }

    @Override
    public String getName() {
        return "OtherLayout";
    }

    @Override
    public String getDescription() {
        return "??";
    }

    private void setup() {
        Ui.viewById(mRootView, R.id.card_title_btn).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.card_more_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.card_title_btn:
                setVis(Ui.viewById(mRootView, R.id.card_title));
                break;
            case R.id.card_more_btn:
                setVis(Ui.viewById(mRootView, R.id.card_more));
                break;
        }
    }

    private void setVis(View view) {

        switch (view.getVisibility()) {
            case View.VISIBLE:
                view.setVisibility(View.INVISIBLE);
                break;
            case View.INVISIBLE:
                view.setVisibility(View.GONE);
                break;
            case View.GONE:
                view.setVisibility(View.VISIBLE);
                break;
        }
    }
}
