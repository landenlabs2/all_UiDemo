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

import android.animation.AnimatorInflater;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Ui;

/**
 * Demonstrate grid layout of images.
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class GridImagesFrag  extends UiFragment   {

    View mRootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.grid_images, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.grid_images_id;
    }

    @Override
    public String getName() {
        return "FullScreen";
    }

    @Override
    public String getDescription() {
        return "??";
    }

    private void setup() {

        GridView gridview = Ui.needViewById(mRootView, R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getActivity()));

        /*
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(getActivity(), "" + position,  Toast.LENGTH_SHORT).show();
            }
        });
        */
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);


                final int pos = position;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(view.getContext(), "Grid Pos:" + pos,  Toast.LENGTH_SHORT).show();
                    }
                });
                if (Build.VERSION.SDK_INT >= 21) {
                    imageView.setStateListAnimator(AnimatorInflater.loadStateListAnimator(mContext, R.anim.press));
                }
            } else {
                imageView = (ImageView) convertView;
            }

            if (Build.VERSION.SDK_INT >= 21) {
                // imageView.setElevation((position & 1) != 0 ? 0 : 10);
            }

        //    if ((position & 1) != 0)
        //        imageView.setImageResource(mThumbIds[position]);
        //    else
                imageView.setBackgroundResource(mThumbIds[position]);

            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.image_a, R.drawable.image_c,
                R.drawable.image_e, R.drawable.image_f,
                R.drawable.image_p, R.drawable.image_s,

                R.drawable.image100, R.drawable.image200,
                R.drawable.image400, R.drawable.image600,

                R.drawable.image_a, R.drawable.image_c,
                R.drawable.image_e, R.drawable.image_f,
                R.drawable.image_p, R.drawable.image_s,

                R.drawable.image_a, R.drawable.image_c,
                R.drawable.image_e, R.drawable.image_f,
                R.drawable.image_p, R.drawable.image_s,

                R.drawable.image_a, R.drawable.image_c,
                R.drawable.image_e, R.drawable.image_f,
                R.drawable.image_p, R.drawable.image_s,


        };
    }
}
