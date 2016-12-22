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
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.landenlabs.all_UiDemo.MainActivity;
import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Ui;
import com.landenlabs.all_UiDemo.Util.PageItem;
import com.landenlabs.all_UiDemo.Util.UiSplashScreen;

import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * Demonstrate Image Scale modes.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class MenuFrag extends UiFragment
        implements View.OnClickListener {

    // ---- Local Data ----
    private View mRootView;
    private ViewGroup mListVg;
    private WeakReference<MainActivity> mMainActivityWeakRef;
    private UiSplashScreen mUiSplashScreen = new UiSplashScreen();

    private static boolean mDidSplash = false;
    private static int FIRST_MENU_PAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.menu_frag, container, false);

        View page = Ui.viewById(mRootView, R.id.menu_page);
        page.setOnClickListener(this);

        MainActivity mainActivity = (MainActivity) getActivity();
        mMainActivityWeakRef = new WeakReference<MainActivity>(mainActivity);

        if (mDidSplash == false) {

            mUiSplashScreen.show(mRootView);
            mDidSplash = true;
        } else {
            mUiSplashScreen.hide();
        }

        setupGrid(fixPageItems(mMainActivityWeakRef.get().getPageItems()));

        Ui.viewById(mRootView, R.id.menu_page_grid_btn).setOnClickListener(this);
        Ui.viewById(mRootView, R.id.menu_page_list_btn).setOnClickListener(this);
        return mRootView;
    }

    // =============================================================================================
    // Fragment

    @Override
    public void onResume() {
        super.onResume();
        setupGrid(fixPageItems(mMainActivityWeakRef.get().getPageItems()));
    }

    // =============================================================================================
    // UiFragment

    @Override
    public int getFragId() {
        return R.id.menu_frag_id;
    }

    @Override
    public String getName() {
        return "Menu";
    }

    @Override
    public String getDescription() {
        return "Menu Grid";
    }


    // =============================================================================================
    // View.OnClickListener

    @Override
    public void onClick(View view) {
        if (mUiSplashScreen.isDone()) {
            if ( view.getTag() instanceof Integer) {
                final Integer idx = (Integer) view.getTag();
                mListVg.callOnClick();
                // Delay action to allow any ripple to play out.
                mListVg.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMainActivityWeakRef.get().selectPage(idx.intValue() + FIRST_MENU_PAGE);
                    }
                }, 500);
                return;
            }
        }

        switch (view.getId()) {
            case -1:
            case R.id.menu_page:
                mUiSplashScreen.hide();
                break;
            case R.id.menu_page_grid_btn:
                setupGrid(fixPageItems(mMainActivityWeakRef.get().getPageItems()));
                break;
            case R.id.menu_page_list_btn:
                setupList(fixPageItems(mMainActivityWeakRef.get().getPageItems()));
                break;
        }
    }

    private PageItem[] fixPageItems(PageItem[] pageItems) {
        return Arrays.copyOfRange(pageItems, FIRST_MENU_PAGE, pageItems.length);
    }

    // =============================================================================================
    // =============================================================================================

    private void setupList(PageItem[] pageItems) {
        Ui.viewById(mRootView, R.id.menu_page_gridview).setVisibility(View.GONE);
        mListVg = Ui.viewById(mRootView, R.id.menu_page_list);
        mListVg.setVisibility(View.VISIBLE);
        mListVg.removeAllViews();
        mListVg.setBackgroundResource(R.drawable.ripple_boarderless);
        int idx = 0;
        for (PageItem item : pageItems) {
            TextView textView = new TextView(mListVg.getContext());
            textView.setText(item.mTitle);
            textView.setTextColor(0xff202020);
            textView.setTextSize(18.0f);
            textView.setBackgroundResource(R.drawable.shadow1);
            textView.setTag(new Integer(idx++));

            mListVg.addView(textView);

            textView.setOnClickListener(this);
        }
        // Ui.viewById(mRootView, R.id.setVisualBtn2).setOnClickListener(this);
    }

    private void setupGrid(PageItem[] pageItems) {

        mListVg = Ui.viewById(mRootView, R.id.menu_page_list);
        mListVg.setVisibility(View.GONE);

        /*
        mListVg.removeAllViews();
        mListVg.setBackgroundResource(R.drawable.ripple_boarderless);

        GridView gridView = new GridView(mListVg.getContext());
        int pad = Ui.dpToPx(10);
        gridView.setColumnWidth(Ui.dpToPx(90));
        gridView.setGravity(Gravity.CENTER);
        gridView.setHorizontalSpacing(Ui.dpToPx(10));
        gridView.setNumColumns(GridView.AUTO_FIT);
        gridView.setPadding(pad, pad, pad, pad);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setVerticalSpacing(Ui.dpToPx(10));
        gridView.setScrollContainer(false);
        gridView.setBackgroundColor(0xffff0000);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mListVg.addView(gridView, lp);
        */

        GridView gridView = Ui.viewById(mRootView, R.id.menu_page_gridview);
        gridView.setVisibility(View.VISIBLE);
        gridView.setAdapter(new GridAdapter(getActivity(), pageItems));
    }

    // =============================================================================================
    // Adapter

    public class GridAdapter extends BaseAdapter {
        final private Context mContext;
        final private PageItem[] mPageItems;

        public GridAdapter(Context context, PageItem[] pageItems) {
            mContext = context;
            mPageItems = pageItems;
        }

        public int getCount() {
            return mPageItems.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }


        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            Button itemView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                itemView = new Button(mContext);
                itemView.setLines(4);
                // itemView.setLayoutParams(new GridView.LayoutParams(200, 200));
                // itemView.setPadding(8, 8, 8, 8);

                if (Build.VERSION.SDK_INT >= 21) {
                    itemView.setElevation(10);
                    itemView.setStateListAnimator(AnimatorInflater.loadStateListAnimator(mContext, R.anim.press));

                    itemView.setBackgroundResource(R.drawable.round_border_sel);
                    Ui.setRectOutline(itemView);
                }
            } else {
                itemView = (Button) convertView;
            }

            itemView.setText(mPageItems[position].mTitle);
            itemView.setTag(new Integer(position));
            itemView.setOnClickListener(MenuFrag.this);

            return itemView;
        }
    }
}