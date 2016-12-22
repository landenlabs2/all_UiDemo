package com.landenlabs.all_UiDemo;

/*
 * Copyright (c) 2015 Dennis Lang (LanDen Labs) landenlabs@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the
 *  following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or substantial
 *  portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 *  @author Dennis Lang  (3/21/2015)
 *  @see http://landenlabs.com
 *
 */

import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;

import com.landenlabs.all_UiDemo.Util.AppCrash;
import com.landenlabs.all_UiDemo.Util.GoogleAnalyticsHelper;
import com.landenlabs.all_UiDemo.Util.PageItem;


/**
 * Main Activity to Ui Demo app.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */
public class MainActivity extends ActionBarActivity    {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    ViewPager mViewPager;
    ActionBar mActionBar;

    static final String STATE_ADAPTER = "secPageAdapter";
    Parcelable mAdapterParcelable;

    GoogleAnalyticsHelper mAnalytics;

    public static boolean isDebug(ApplicationInfo appInfo) {
        return ((appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
    }

    // ---------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean DEBUG = isDebug(getApplicationInfo());
        // m_uncaughtExceptionHandler = new UncaughtExceptionHandler(DEBUG);
        AppCrash.initalize(getApplication(), DEBUG);
        mAnalytics = new GoogleAnalyticsHelper(getApplication(), DEBUG);

        setContentView(R.layout.activity_main);

        mActionBar = getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setTitle("UiDemo API" + Build.VERSION.SDK_INT +
                    (BuildConfig.DEBUG ? " Dbg" : ""));

            mActionBar.setSubtitle(BuildConfig.VERSION_NAME);
            mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_HOME);
            mActionBar.setIcon(R.drawable.uidemo_sm);
        }

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (mActionBar != null && mSectionsPagerAdapter != null) {
                    mActionBar.setSubtitle(mSectionsPagerAdapter.getPageTitle(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mViewPager != null && mViewPager.getAdapter() == null) {
            Log.d("foo", "onResume");
            // Create the adapter that will return a fragment for each page.
            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager.setAdapter(mSectionsPagerAdapter);
            if (false) {
                // Optionally set limit of pages to keep.
                mViewPager.setOffscreenPageLimit(mSectionsPagerAdapter.getCount());
            }
            if (mAdapterParcelable != null) {
                mSectionsPagerAdapter.restoreState(mAdapterParcelable, null);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("foo", "onPause");
        if (mSectionsPagerAdapter != null)
            mAdapterParcelable = mSectionsPagerAdapter.saveState();
        mSectionsPagerAdapter = null;
        if (mViewPager != null)
             mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SubMenu pageMenu = menu.addSubMenu("Pages...");
        int groupId = 1;
        int itemId = 100;
        for (PageItem item : mItems) {
            pageMenu.add(groupId, itemId, itemId, item.mTitle);
            itemId++;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id >= 100 && id < 100 + mItems.length) {
            selectPage(id - 100);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mViewPager.getCurrentItem() == 0) {
            super.onBackPressed(); // This will pop the Activity from the stack.
        } else {
            selectPage(0);
        }
    }

    public void selectPage(int idx) {
        mViewPager.setCurrentItem(idx);
    }

    public static PageItem[] getPageItems() {
        return mItems;
    }

    // =============================================================================================

    static final PageItem[] mItems = new PageItem[] {

            new PageItem( "Menu", R.layout.page_menu_frag),


            // new PageItem( "DrawerLayout", R.layout.page_drawer_layout),
            new PageItem( "Scroll Resize", R.layout.page_scroll_resize),

            new PageItem( "Assorted", R.layout.page_assorted),
            new PageItem( "Text Alignment", R.layout.page_text),
            new PageItem( "TextSize", R.layout.page_text_height),

            new PageItem( "GridView Images", R.layout.page_grid_image),
            new PageItem( "GridLayout", R.layout.page_grid_layout),
            new PageItem( "Image Scales", R.layout.page_image_scales),
            new PageItem( "Image Overlap",  R.layout.page_image_over ),

            new PageItem( "RadioBtn Tabs", R.layout.page_radio_btns),
            new PageItem( "RadioBtn List", R.layout.page_radio_list),
            new PageItem( "CkBox List", R.layout.page_list1),       // min api 21
            new PageItem( "Custom List",  R.layout.page_anim_list ),

            new PageItem( "Toggle/Switch",  R.layout.page_switches),
            new PageItem( "Checkbox Right",  R.layout.page_checkbox_right ),
            new PageItem( "Checkbox Left",  R.layout.page_checkbox_left ),

            new PageItem( "Relative Layout",  R.layout.page_rellayout ),
            new PageItem( "Other Layout",  R.layout.page_otherlayout_frag ),
            new PageItem( "Layout Anim",  R.layout.page_layout_anim ),
            new PageItem( "Full Screen",  R.layout.page_fullscreen ),

            // API 21
            new PageItem( "ElevShadow (API21)",  R.layout.page_elevation ),
            new PageItem( "Coordinated (API21)", R.layout.page_coordinated ),
            new PageItem( "TabLayout (API21)", R.layout.page_tablayout),

            new PageItem( "GL Cube", R.layout.page_glcube_frag),
            new PageItem( "Graph Line", R.layout.page_graphline_frag),
            new PageItem( "Anim Bg", R.layout.page_anim_bg_frag),

            new PageItem( "View Shadows", R.layout.page_shadows),
            new PageItem( "Render Blur", R.layout.page_renderscript)
    };

    // =============================================================================================
    // SectionsPagerAdapter - implement page swipes
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            // ActionBar actionBar = getActionBar();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PageFragment (defined as a static inner class below).
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mItems.length;  // View swipe page count.
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mItems[position].mTitle;
        }
    }

    /**
     * A page fragment with selectable layouts per page number.
     */
    public static class PageFragment extends Fragment {
        // The fragment argument representing the section number for this  fragment.
        static final String ARG_page_number = "page_number";
        int m_pageNum = 0;

        // Returns a new instance of this fragment for the given section  number.
        public static PageFragment newInstance(int sectionNumber) {
            PageFragment fragment = new PageFragment();

            Bundle args = new Bundle();
            args.putInt(ARG_page_number, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            m_pageNum = getArguments().getInt(ARG_page_number);
            int layout =  mItems[m_pageNum].mLayout;
            View rootView = inflater.inflate(layout, container, false);

            return rootView;
        }
    }
}
