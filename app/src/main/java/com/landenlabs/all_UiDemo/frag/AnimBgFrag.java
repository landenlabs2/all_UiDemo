
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


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.landenlabs.all_UiDemo.R;
import com.landenlabs.all_UiDemo.Ui;

/**
 * Demonstrate Animated Background (texture)
 *
 * @author Dennis Lang (LanDen Labs)
 * @see <a href="http://landenlabs.com/android/index-m.html"> author's web-site </a>
 */

public class AnimBgFrag  extends UiFragment implements View.OnClickListener {

    View        mRootView;

    // http://stackoverflow.com/questions/27671653/background-animation-with-repeat

    // https://github.com/flavienlaurent/PanningView

    // http://flavienlaurent.com/blog/2013/08/05/make-your-background-moving-like-on-play-music-app/
    enum Direction {
        ToLeft {
            @Override
            public Direction reverse() {
                return ToRight;
            }
            @Override
            public float getX(float value, RectF displayRect) {
                return value % displayRect.width();
            }
        },
        ToRight{
            @Override
            public Direction reverse() {
                return ToLeft;
            }
            @Override
            public float getX(float value, RectF displayRect) {
                return value % displayRect.width();
            }
        },
        ToBottom{
            @Override
            public Direction reverse() {
                return ToTop;
            }
            @Override
            public float getY(float value, RectF displayRect) {
                return value % displayRect.height();
            }
        },
        ToTop{
            @Override
            public Direction reverse() {
                return ToBottom;
            }
            @Override
            public float getY(float value, RectF displayRect) {
                return value % displayRect.height();
            }
        };

        public abstract Direction reverse();
        public float getX(float value, RectF displayRect) {
            return 0;
        }
        public float getY(float value, RectF displayRect) {
            return 0;
        }
    };
    // private static final int ToLeft = 1;
    // private static final int ToRight = 2;

    private ImageView mImageView1;
    private ImageView mImageView2;
    private float mScaleFactor;

    // =============================================================================================
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.anim_bg_frag, container, false);

        setup();
        return mRootView;
    }

    @Override
    public int getFragId() {
        return R.id.anim_bg_id;
    }

    @Override
    public String getName() {
        return "AnimBg";
    }

    @Override
    public String getDescription() {
        return "??";
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            default:
                break;
        }
    }

    private void setup() {
        mImageView1 = Ui.viewById(mRootView, R.id.anim_bg_image1);
        mImageView1.post(new Runnable() {
            @Override
            public void run() {
                mScaleFactor = (float)  mImageView1.getHeight() / (float) mImageView1.getDrawable().getIntrinsicHeight();
                Matrix matrix = new Matrix();
                // mMatrix.postScale(mScaleFactor, mScaleFactor);
                mImageView1.setImageMatrix(matrix);
                animate(mImageView1, Direction.ToLeft, 15000, false);
            }
        });

        mImageView2 = Ui.viewById(mRootView, R.id.anim_bg_image2);
        mImageView2.post(new Runnable() {
            @Override
            public void run() {
                mScaleFactor = (float)  mImageView2.getHeight() / (float) mImageView2.getDrawable().getIntrinsicHeight();
                Matrix matrix = new Matrix();
                // matrix.postScale(mScaleFactor, mScaleFactor);
                mImageView2.setImageMatrix(matrix);
                animate(mImageView2, Direction.ToTop, 5000, true);
            }
        });
    }

    private void animate(ImageView imageView, Direction direction, final int durationMilli, boolean doRev) {
        RectF displayRect = new RectF(0, 0,
                imageView.getDrawable().getIntrinsicWidth(), imageView.getDrawable().getIntrinsicHeight());
        imageView.getImageMatrix().mapRect(displayRect);

        switch (direction) {
            case ToLeft:
                animate(imageView, direction, durationMilli, doRev, displayRect,
                    displayRect.left, imageView.getWidth() + displayRect.left - displayRect.right);
                break;
            case ToRight:
                animate(imageView, direction, durationMilli, doRev, displayRect, displayRect.left, 0.0f);
                break;

            case ToTop:
                animate(imageView, direction, durationMilli, doRev, displayRect,
                        displayRect.top, imageView.getHeight() + displayRect.top - displayRect.bottom);
                break;
            case ToBottom:
                animate(imageView, direction, durationMilli, doRev, displayRect, displayRect.top, 0.0f);
                break;
        }
    }

    private void animate(final ImageView imageView,
             final Direction direction, final int durationMilli, final boolean doRev,
             final RectF displayRect,
             final float from, final float to) {
        ValueAnimator animator;
        animator = ValueAnimator.ofFloat(from, to);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (Float) animation.getAnimatedValue();

                Matrix matrix = new Matrix(); // imageView.getImageMatrix();
                matrix.reset();
                // matrix.postScale(mScaleFactor, mScaleFactor);
                float x = direction.getX(value, displayRect);
                float y = direction.getY(value, displayRect);
                matrix.postTranslate(x, y);
                // Log.d("foo", String.format("x=%.0f, y=%.0f w=%.0f", x, y, displayRect.width()));

                imageView.setImageMatrix(matrix);
            }
        });

        animator.setDuration(durationMilli);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (doRev) {
                    animate(imageView, direction.reverse(), durationMilli, doRev);
                } else {
                    animate(imageView, direction, durationMilli, doRev, displayRect, from, to);
                }
            }
        });
        animator.start();
    }

}
