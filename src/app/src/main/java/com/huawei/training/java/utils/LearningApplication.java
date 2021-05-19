/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.training.java.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.agconnect.apms.APMS;
import com.huawei.agconnect.crash.AGConnectCrash;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.videokit.player.InitFactoryCallback;
import com.huawei.hms.videokit.player.WisePlayerFactory;
import com.huawei.hms.videokit.player.WisePlayerFactoryOptions;
import com.huawei.training.java.database.CloudDbHelper;
import com.huawei.training.java.models.UserObj;
import com.huawei.training.java.utils.eventmanager.AppAnalytics;
import com.huawei.training.java.utils.network.NetworkConnectionCallbacks;
import com.huawei.training.java.utils.network.NetworkReceiver;
import com.huawei.training.java.utils.pushservice.PushUtil;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class LearningApplication extends Application
        implements Application.ActivityLifecycleCallbacks, NetworkConnectionCallbacks {
    /**
     * The Is network connected.
     */
    @SuppressLint("StaticFieldLeak")
    private boolean isNetworkConnected;
    /**
     * The Is recently viewed cores refresh.
     */
    private boolean isRecentlyViewedCoresRefresh = false;
    @SuppressLint("StaticFieldLeak")
    private static WisePlayerFactory wisePlayerFactory = null;
    private UserObj userObj = null;
    private BroadcastReceiver myReceiver = null;
    /**
     * The App analytics.
     */
    AppAnalytics appAnalytics;
    /**
     * The Cloud db helper.
     */
    CloudDbHelper cloudDbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        HwAds.init(this);
        initPlayer();
        myReceiver = new NetworkReceiver(this);
        registerActivityLifecycleCallbacks(LearningApplication.this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void init() {
        appAnalytics = new AppAnalytics(this);
        cloudDbHelper = CloudDbHelper.getInstance(this);
    }

    /**
     * Init the player
     */
    private void initPlayer() {
        // DeviceId test is used in the demo, specific access to incoming deviceId after encryption
        WisePlayerFactoryOptions factoryOptions = new WisePlayerFactoryOptions.Builder().setDeviceId("xxx").build();
        WisePlayerFactory.initFactory(this, factoryOptions, initFactoryCallback);
    }

    /**
     * Player initialization callback
     */
    private static InitFactoryCallback initFactoryCallback =
            new InitFactoryCallback() {
                @Override
                public void onSuccess(WisePlayerFactory wisePlayerFactory) {
                    setWisePlayerFactory(wisePlayerFactory);
                }

                @Override
                public void onFailure(int errorCode, String reason) {
                }
            };

    /**
     * Get WisePlayer Factory
     *
     * @return WisePlayer Factory
     */
    public static WisePlayerFactory getWisePlayerFactory() {
        return wisePlayerFactory;
    }

    private static void setWisePlayerFactory(WisePlayerFactory wisePlayerFactory) {
        LearningApplication.wisePlayerFactory = wisePlayerFactory;
    }

    /**
     * Setting the user login status
     *
     */
    public void setUserStatus() {
    }

    /**
     * Gets cloud db helper.
     *
     * @return the cloud db helper
     */
    public CloudDbHelper getCloudDbHelper() {
        return cloudDbHelper;
    }

    /**
     * get the user details
     *
     * @return user obj
     */
    public UserObj getUserObj() {
        return userObj;
    }

    /**
     * Sets user obj.
     *
     * @param userObj the user obj
     */
    public void setUserObj(UserObj userObj) {
        this.userObj = userObj;
    }

    /**
     * Gets app analytics.
     *
     * @return the app analytics
     */
    public AppAnalytics getAppAnalytics() {
        return appAnalytics;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        AGConnectCrash.getInstance().enableCrashCollection(true);
        APMS.getInstance().enableAnrMonitor(true);
        APMS.getInstance().enableCollection(true);
        PushUtil.getToken(this);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        registerReceiver(myReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        unregisterReceiver(myReceiver);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    @Override
    public void onNetConnected() {
        isNetworkConnected = true;
    }

    @Override
    public void onNetDisconnected() {
        isNetworkConnected = false;
    }

    /**
     * Network connection check
     *
     * @return net work connection status
     */
    public boolean getNetWorkConnectionStatus() {
        return isNetworkConnected;
    }

    /**
     * Is recently viewed cores refresh boolean.
     *
     * @return the boolean
     */
    public boolean isRecentlyViewedCoresRefresh() {
        return isRecentlyViewedCoresRefresh;
    }

    /**
     * Sets recently viewed cores refresh.
     *
     * @param recentlyViewedCoresRefresh the recently viewed cores refresh
     */
    public void setRecentlyViewedCoresRefresh(boolean recentlyViewedCoresRefresh) {
        isRecentlyViewedCoresRefresh = recentlyViewedCoresRefresh;
    }
}
