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
package com.huawei.training.kotlin.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import com.huawei.agconnect.apms.APMS
import com.huawei.agconnect.crash.AGConnectCrash
import com.huawei.hms.ads.HwAds
import com.huawei.hms.videokit.player.InitFactoryCallback
import com.huawei.hms.videokit.player.WisePlayerFactory
import com.huawei.hms.videokit.player.WisePlayerFactoryOptions
import com.huawei.training.kotlin.models.UserObj
import com.huawei.training.kotlin.utils.eventmanager.AppAnalytics
import com.huawei.training.kotlin.utils.network.NetworkConnectionCallbacks
import com.huawei.training.kotlin.utils.network.NetworkReceiver
import com.huawei.training.kotlin.utils.pushservice.PushUtil

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class LearningApplication : Application(), ActivityLifecycleCallbacks, NetworkConnectionCallbacks {
    /**
     * Network connection check
     *
     * @return net work connection status
     */
    /**
     * The Is network connected.
     */
    @SuppressLint("StaticFieldLeak")
    var netWorkConnectionStatus = false
        private set

    /**
     * Is recently viewed cores refresh boolean.
     *
     * @return the boolean
     */
    /**
     * Sets recently viewed cores refresh.
     *
     * @param recentlyViewedCoresRefresh the recently viewed cores refresh
     */
    /**
     * The Is recently viewed cores refresh.
     */
    var isRecentlyViewedCoresRefresh = false
    /**
     * get the user details
     *
     * @return user obj
     */
    /**
     * Sets user obj.
     *
     * @param userObj the user obj
     */
    var userObj: UserObj? = null
    private var myReceiver: BroadcastReceiver? = null

    /**
     * Gets app analytics.
     *
     * @return the app analytics
     */
    /**
     * The App analytics.
     */
    var appAnalytics: AppAnalytics? = null




    override fun onCreate() {
        super.onCreate()
        init()
        HwAds.init(this)
        initPlayer()
        myReceiver = NetworkReceiver(this)
        registerActivityLifecycleCallbacks(this@LearningApplication)

    }

    override fun onTerminate() {
        super.onTerminate()
    }

    private fun init() {
        appAnalytics = AppAnalytics(this)
    }

    /**
     * Init the player
     */
    private fun initPlayer() {
        // DeviceId test is used in the demo, specific access to incoming deviceId after encryption
        val factoryOptions = WisePlayerFactoryOptions.Builder().setDeviceId("xxx").build()
        WisePlayerFactory.initFactory(this, factoryOptions, initFactoryCallback)
    }

    /**
     * Setting the user login status
     *
     */
    fun setUserStatus() {}

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        AGConnectCrash.getInstance().enableCrashCollection(true)
        APMS.getInstance().enableAnrMonitor(true)
        APMS.getInstance().enableCollection(true)
        PushUtil.getToken(this)
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {
        registerReceiver(myReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onActivityPaused(activity: Activity) {
        unregisterReceiver(myReceiver)
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}
    override fun onNetConnected() {
        netWorkConnectionStatus = true
    }

    override fun onNetDisconnected() {
        netWorkConnectionStatus = false
    }

    companion object {
        /**
         * Get WisePlayer Factory
         *
         * @return WisePlayer Factory
         */
        @JvmStatic
        @SuppressLint("StaticFieldLeak")
        private var wisePlayerFactory: WisePlayerFactory? = null

        /**
         * Player initialization callback
         */
        private val initFactoryCallback: InitFactoryCallback = object : InitFactoryCallback {
            override fun onSuccess(wisePlayerFactory: WisePlayerFactory) {
                setWisePlayerFactory(wisePlayerFactory)

            }

            override fun onFailure(errorCode: Int, reason: String) {
            }
        }

        private fun setWisePlayerFactory(wisePlayerFactory: WisePlayerFactory) {
            Companion.wisePlayerFactory = wisePlayerFactory
        }

        fun getWisePlayerFactory(): WisePlayerFactory? {
            return wisePlayerFactory
        }

    }
}