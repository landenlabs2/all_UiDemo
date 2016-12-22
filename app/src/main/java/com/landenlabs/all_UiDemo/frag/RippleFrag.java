package com.landenlabs.all_UiDemo.frag;

import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.landenlabs.all_UiDemo.R;

/**
 * Created by Dennis Lang on 7/21/16.
 */
public class RippleFrag extends UiFragment implements View.OnClickListener {

    public class DbgStateListDrawable extends StateListDrawable {
        @Override
        public boolean selectDrawable(int idx) {
            Log.d("foo", "SelectDrawable " + idx);
            return super.selectDrawable(idx);
        }
    }

    // ---- Local Data ----
    private View mRootView;
    private ListView mList1View;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.ripple_frag, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.ripple_id;
    }

    @Override
    public String getName() {
        return "Ripple";
    }

    @Override
    public String getDescription() {
        return "??";
    }

    @Override
    public void onClick(View view) {
    }

    private void setup() {
        // Ui.viewById(mRootView, R.id.setVisualBtn1).setOnClickListener(this);
    }

    private void foo() {

             /*
            int[][] states = new int[][] {
                    new int[] { android.R.attr.state_checked},
                    new int[] { android.R.attr.state_selected},
                    new int[] { android.R.attr.state_activated},
                    new int[] { }
            };
            ColorStateList colorStateList = new ColorStateList(states, new int[] {
                    skinNavMenu.selectionBgColor,
                    skinNavMenu.selectionBgColor,
                    skinNavMenu.selectionBgColor,
                    skinNavMenu.itemBgColor}
                    );

            // StateListDrawable stateListDrawable = new StateListDrawable();

            // mDrawerListView.setSelector();
            // Drawable selector = mDrawerListView.getSelector().getConstantState().newDrawable().mutate();
            Drawable selector = mDrawerListView.getResources().getDrawable(R.drawable.selector_white);

            if (Build.VERSION.SDK_INT >= 21) {
                selector.setTintMode(PorterDuff.Mode.MULTIPLY);
                selector.setTintList(colorStateList);
            } else {
                selector = DrawableCompat.wrap(selector);
                DrawableCompat.setTintList(selector.mutate(), colorStateList);
                DrawableCompat.setTintMode(selector.mutate(), PorterDuff.Mode.MULTIPLY);
            }

            mDrawerListView.setSelector(selector);
            */

        ListView mListView = null;
        int selectionBgColor = 0xffff0000;
        ColorDrawable selectionBgDrawable = new ColorDrawable(selectionBgColor);

        if (Build.VERSION.SDK_INT >= 21) {
                /*
                <ripple
                     xmlns:android="http://schemas.android.com/apk/res/android"
                     android:color="#40ffffff" >

                    <!-- Limit ripple to view object, can also use shape such as oval -->
                    <item android:id="@android:id/mask" android:drawable="@android:color/white" />
                 </ripple>
                 */
            // ColorStateList colorStateList = new ColorStateList(
            //         new int[][] { new int[]{} },
            //         new int[] { 0x40ffffff }  );
            ColorStateList colorStateList = ColorStateList.valueOf(0x40ffffff); // ripple color
            RippleDrawable rippleDrawable = new RippleDrawable(colorStateList, selectionBgDrawable, null);
            mListView.setSelector(rippleDrawable);
        } else   {
            // StateListDrawable stateListDrawable = new StateListDrawable();
            // stateListDrawable.addState(new int[]{}, drawableItemBg));
            // mDrawerListView.setSelector(stateListDrawable);
            mListView.setSelector(selectionBgDrawable);
        }




        /*
        ColorDrawable colorDrawable = new ColorDrawable(mSkinNavMenu.selectionBgColor);
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[] { android.R.attr.state_checked}, colorDrawable);
        stateListDrawable.addState(new int[] { android.R.attr.state_selected}, colorDrawable);
        stateListDrawable.addState(new int[] { android.R.attr.state_activated}, colorDrawable);
        mSelectorDrawable = stateListDrawable;
        */

        /*
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked},
                new int[] { android.R.attr.state_selected},
                new int[] { android.R.attr.state_activated},
                new int[] { }
        };
        ColorStateList colorStateList = new ColorStateList(states, new int[] {
                mSkinNavMenu.selectionBgColor,
                mSkinNavMenu.selectionBgColor,
                mSkinNavMenu.selectionBgColor,
                0xffff0000
                // mSkinNavMenu.itemBgColor
                }
        );


        // mDrawerListView.setSelector();
        // Drawable selector = mDrawerListView.getSelector().getConstantState().newDrawable().mutate();
        mSelectorDrawable = context.getResources().getDrawable(R.drawable.selector_white);

        if (Build.VERSION.SDK_INT >= 21) {
            mSelectorDrawable.setTintMode(PorterDuff.Mode.MULTIPLY);
            mSelectorDrawable.setTintList(colorStateList);
        } else {
            mSelectorDrawable = DrawableCompat.wrap(mSelectorDrawable);
            DrawableCompat.setTintList(mSelectorDrawable.mutate(), colorStateList);
            DrawableCompat.setTintMode(mSelectorDrawable.mutate(), PorterDuff.Mode.MULTIPLY);
        }
        */
    }
}
