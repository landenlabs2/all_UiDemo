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

import net.hockeyapp.android.Constants;
import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;
import net.hockeyapp.android.metrics.MetricsManager;
import net.hockeyapp.android.objects.CrashManagerUserInput;
import net.hockeyapp.android.objects.CrashMetaData;

import java.lang.ref.WeakReference;

/**
 * Utility class to optionally initialize HockeyApp crash reporting
 * <br>
 * Populate description with additional information.
 *
 * @author Dennis Lang on 11/15/16.
 */

public class AppCrash extends CrashManagerListener {

    Context mContext;

    public AppCrash(Context context) {
        mContext = context;
    }

    public static void initalize(Application application, boolean isDebug) {
        Context context = application;
        int keyResId = context.getResources().getIdentifier("hockeyapp_key", "string", context.getPackageName());
        int pkgResId = context.getResources().getIdentifier("hockeyapp_pkg", "string", context.getPackageName());
        if (keyResId > 0 && pkgResId > 0) {
            final String HOCKEY_APP_ID = context.getResources().getString(keyResId);
            final String HOCKEY_APP_PKG = context.getResources().getString(pkgResId);

            // HockeyApp Crash reporting
            // HockeyLog.setLogLevel(Log.VERBOSE);     // For debug testing only !!!!!

            /*
             *  Initialize HockeyApp manually so we can modify the package name to a fixed package
             *  to force all trace to aggregate under one account.
             *
             *  Use the meta data to identify individual client information.
             *
             *  Call execute to post any previously generated traces.
             */
            AppCrash hockeyAppCrashListener = new AppCrash(context);
            CrashManager.initialize(context, HOCKEY_APP_ID, hockeyAppCrashListener);
            Constants.APP_PACKAGE = HOCKEY_APP_PKG;
            CrashMetaData crashMetaData = new CrashMetaData();
            crashMetaData.setUserDescription(context.getApplicationInfo().processName);
            crashMetaData.setUserID(context.getPackageName());
            CrashManager.handleUserInput(CrashManagerUserInput.CrashManagerUserInputAlwaysSend, crashMetaData, hockeyAppCrashListener,
                    new WeakReference<Context>(context), false);
            CrashManager.execute(context, hockeyAppCrashListener);

            MetricsManager.register((Application)context, HOCKEY_APP_ID);
            MetricsManager.trackEvent("start");
        }
    }

    @Override
    public boolean shouldAutoUploadCrashes() {
        return true;
    }

    @Override
    public void onNewCrashesFound() {
        super.onNewCrashesFound();
    }

    @Override
    public void onCrashesNotSent() {
        super.onCrashesNotSent();
    }

    @Override
    public void onCrashesSent() {
        super.onCrashesSent();
    }

    /**
     * Please all Log 'errors' in description.
     * @return
     */
    public String getDescription() {
        String description = "";
        String eol = System.getProperty("line.separator");

        description = addRes(description, "TargetSDK=", "targetSdkVersion");
        description = addRes(description, "CompilerSDK=", "compileSdkVersion");
        description = addRes(description, "BuildTools=", "buildToolsVersion");

        return description;
    }

    private String addRes(String inStr, String title, String resName) {
        int resId = mContext.getResources().getIdentifier(
                resName, "string", mContext.getPackageName());
        if (resId > 0) {
            inStr += title + mContext.getResources().getString(resId);
        }

        return inStr;
    }
}
