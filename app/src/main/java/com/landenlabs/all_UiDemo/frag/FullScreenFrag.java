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

import android.animation.AnimatorInflater;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Ui;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstrate Image Scale modes.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class FullScreenFrag  extends UiFragment implements View.OnClickListener  {


    private final List<String> mListStrings = Arrays.asList(
            "FullScreen",
            "Hide Navigation",
            "Immersive",
            "Layout FullScreen",
            "Layout Hide Nav",
            "Layout Stable",
            "Light Status Bar",
            "Low Profile",
            "Visible"
            );

    // ---- Local Data ----
    private View mRootView;
    private ListView mList1View;
    private ListView mList2View;
    private ListView mList3View;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fullscreen, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.fullscreen_id;
    }

    @Override
    public String getName() {
        return "FullScreen";
    }

    @Override
    public String getDescription() {
        return "??";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setVisualBtn1:
                setSystemUiVisibility(mList1View);
                break;
            case R.id.setVisualBtn2:
                setSystemUiVisibility(mList2View);
                break;
        }
    }

    private void setup() {
        mList1View = setupList(R.id.sysuilist1);
        mList2View = setupList(R.id.sysuilist2);

        Ui.viewById(mRootView, R.id.setVisualBtn1).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.setVisualBtn2).setOnClickListener(this);
    }

    private ListView setupList(int listResId) {
        ListView listView = Ui.viewById(mRootView, listResId);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // String itemStr = listView.getItemAtPosition(position).toString();
                // title.setText(itemStr);
                if(Build.VERSION.SDK_INT >= 21) {
                    view.setStateListAnimator(AnimatorInflater.loadStateListAnimator(view.getContext(), R.anim.press));
                }
                view.setPressed(true);
            }
        });

        int mRowLayoutRes = android.R.layout.simple_list_item_multiple_choice;
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(new ArrayAdapter<String>(mRootView.getContext(), mRowLayoutRes, mListStrings));

        return listView;
    }

    private void setSystemUiVisibility(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();

        int visualFlags = 0;
        int cnt = listAdapter.getCount();
        for (int idx = 0; idx != cnt; idx++) {
            View view = listView.getChildAt(idx);
            String str = (String)listAdapter.getItem(idx);
            if (view instanceof AppCompatCheckedTextView) {
                AppCompatCheckedTextView cb = (AppCompatCheckedTextView)view;
                if (cb.isChecked())
                switch (str) {
                    case "FullScreen":
                        visualFlags |= View.SYSTEM_UI_FLAG_FULLSCREEN;
                        break;
                    case "Hide Navigation":
                        visualFlags |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                        break;
                    case "Immersive":
                        visualFlags |= View.SYSTEM_UI_FLAG_IMMERSIVE;
                        break;
                    case "Layout FullScreen":
                        visualFlags |= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                        break;
                    case "Layout Hide Nav":
                        visualFlags |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
                        break;
                    case "Layout Stable":
                        visualFlags |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                        break;
                    case "Light Status Bar":
                        visualFlags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
                        break;
                    case "Low Profile":
                        visualFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                        break;
                    case "Visible":
                        visualFlags |= View.SYSTEM_UI_FLAG_VISIBLE;
                        break;
                }
            }
        }

        mRootView.setSystemUiVisibility(visualFlags);
    }
}
