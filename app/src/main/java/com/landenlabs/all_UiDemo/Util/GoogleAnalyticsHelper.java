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
package com.landenlabs.all_UiDemo.Util;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Google Analytics helper class.
 *
 * @author Dennis Lang
 */
public class GoogleAnalyticsHelper {
    private final Context mContext;
    private GoogleAnalytics mGoogleAnalytics;
    private Tracker mTracker;

    public GoogleAnalyticsHelper(Application context, boolean isDebug) {
        mContext = context;
        int keyResId = context.getResources().getIdentifier("google_analytic_key", "string", context.getPackageName());
        if (keyResId > 0) {
            String googleAnalyticKey = context.getResources().getString(keyResId);

            mGoogleAnalytics = GoogleAnalytics.getInstance(context);
            mGoogleAnalytics.enableAutoActivityReports(context);

            // Use following when debugging Google Analtyics ---
            //   mGoogleAnalytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE.ordinal());
            //   mGoogleAnalytics.setLocalDispatchPeriod(10);

            mTracker = mGoogleAnalytics.newTracker(googleAnalyticKey);
            mTracker.enableAutoActivityTracking(true);
            // mTracker.setSampleRate(mSampling);

            mTracker.setScreenName("Home");

            // Enable if you want google to manage crash reports.
            // Disable similar call by Flurry.
            // mTracker.enableExceptionReporting(true);
        }
    }

    /**
     * Send Screen tracker and update last sent time.
     *
     * @param screenName
     */
    public void sendScreen(String screenName) {
        mTracker.setScreenName(screenName);

        try {
            HitBuilders.ScreenViewBuilder hitBuilders = new HitBuilders.ScreenViewBuilder();
            mTracker.send(hitBuilders.build());
        } catch (Exception ex) {
        }
    }

    /**
     * Send Event if enabled and screen event has been sent.
     *
     * @param cat
     * @param action
     * @param label
     */
    public void sendEvent(String cat, String action, String label) {
        try {
            // Google Analytics v4
            HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder()
                    .setCategory(cat)
                    .setAction(action)
                    .setLabel(label);

            mTracker.send(eventBuilder.build());

        } catch (Exception ex) {
        }
    }
}
