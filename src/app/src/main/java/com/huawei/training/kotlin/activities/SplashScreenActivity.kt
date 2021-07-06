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
package com.huawei.training.kotlin.activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.databinding.DataBindingUtil
import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.AudioFocusType
import com.huawei.hms.ads.HwAds
import com.huawei.hms.ads.splash.SplashAdDisplayListener
import com.huawei.hms.ads.splash.SplashView.SplashAdLoadListener
import com.huawei.training.R
import com.huawei.training.kotlin.database.CloudDbHelper
import com.huawei.training.kotlin.database.CloudDbAction
import com.huawei.training.kotlin.database.CloudDbUiCallbackListener
import com.huawei.training.kotlin.database.tables.UsersInfoTable
import com.huawei.training.databinding.ActivitySplashScreenBinding
import com.huawei.training.kotlin.models.UserObj
import com.huawei.training.kotlin.utils.AppUtils
import com.huawei.training.kotlin.utils.LearningApplication
import com.huawei.training.kotlin.utils.video.Constants

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class SplashScreenActivity : BaseActivity(), CloudDbUiCallbackListener {
    /**
     * The Binding.
     */
    var binding: ActivitySplashScreenBinding? = null
    private var hasPaused = false

    /**
     * The Utils.
     */
    var utils: AppUtils? = null

    /**
     * The Is db ready.
     */
    var isDbReady = false

    /**
     * The Is db logged in.
     */
    var isDbLoggedIn = false

    var cloudDbHelper: CloudDbHelper?=null


    // Callback handler used when the ad display timeout message is received.
    private val timeoutHandler = Handler(
            Handler.Callback { msg: Message? ->
                if (hasWindowFocus()) {
                    jump()
                }
                false
            })
    private val splashAdLoadListener: SplashAdLoadListener = object : SplashAdLoadListener() {
        override fun onAdLoaded() {
            // Call this method when an ad is successfully loaded.
        }

        override fun onAdFailedToLoad(errorCode: Int) {
            // Call this method when an ad fails to be loaded.
            jump()
        }

        override fun onAdDismissed() {
            // Call this method when the ad display is complete.
            jump()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
        HwAds.init(this)
        utils = AppUtils(this@SplashScreenActivity)
        cloudDbHelper= CloudDbHelper.getInstance(applicationContext)
        cloudDbHelper?.addCallBackListener(this)
        cloudDbHelper?.getCloudDbAllQueyCalls()?.login(this, CloudDbAction.DB_LOGIN)
    }

    private fun loadAd() {
        val adParam = AdParam.Builder().build()
        binding?.splashAdView?.setAdDisplayListener(object : SplashAdDisplayListener() {
            override fun onAdShowed() {}
            override fun onAdClick() {}
        })

        // Set a default app launch image.
        binding?.splashAdView?.setSloganResId(R.drawable.default_slogan)
        binding?.splashAdView?.setLogo(findViewById(R.id.logo_area))
        // Set a logo image.
        binding?.splashAdView?.setLogoResId(R.mipmap.ic_launcher)
        // Set logo description.
        binding?.splashAdView?.setMediaNameResId(R.string.media_name)
        // Set the audio focus type for a video splash ad.
        binding?.splashAdView?.setAudioFocusType(AudioFocusType.NOT_GAIN_AUDIO_FOCUS_WHEN_MUTE)
        binding?.splashAdView?.load(
                getString(R.string.ad_id_splash),
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
                adParam,
                splashAdLoadListener)
        // Remove the timeout message from the message queue.
        timeoutHandler.removeMessages(Constants.MSG_AD_TIMEOUT)
        // Send a delay message to ensure that the app
        // home screen can be displayed when the ad display times out.
        timeoutHandler.sendEmptyMessageDelayed(Constants.MSG_AD_TIMEOUT, Constants.AD_TIMEOUT.toLong())
    }

    // Switch from the splash ad screen to the app home screen when the ad display is complete.
    private fun jump() {
        if (!hasPaused) {
            hasPaused = true
            if (!isDbReady) {
                finish()
                showToast("Please Try Again")
            } else {
                checkLoginStatus()
            }
            Handler(Looper.getMainLooper())
                    .postDelayed({ finish() },
                            Constants.DELAY_MILLIS_1000)
        }
    }

    private fun checkLoginStatus() {
        if (utils?.getPref(Constants.UID) == null) {
            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
        } else {
            queryUser(utils?.getPref(Constants.UID))
        }
    }

    // Set this parameter to true when exiting the
    // app to ensure that the app home screen is not displayed.
    override fun onStop() {
        timeoutHandler.removeMessages(Constants.MSG_AD_TIMEOUT)
        hasPaused = true
        super.onStop()
    }

    // Call this method when returning to the splash ad screen
    // from another screen to access the app home screen.
    override fun onRestart() {
        super.onRestart()
        hasPaused = false
        jump()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.splashAdView?.destroyView()
    }

    override fun onPause() {
        super.onPause()
        binding?.splashAdView?.pauseView()
    }

    override fun onResume() {
        super.onResume()
        binding?.splashAdView?.resumeView()
    }

    private fun getUser(usersList: List<UsersInfoTable>) {
        if (usersList.isEmpty()) {
            startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
        } else {
            val userInfo = usersList[0]
            val userObj = UserObj()
            userObj.setuId(userInfo.userId)
            userObj.firstName = userInfo.displayName
            userObj.emailId = userInfo.email
            (this.applicationContext as LearningApplication).userObj = userObj
            (this.applicationContext as LearningApplication).setUserStatus()
            startActivity(Intent(this@SplashScreenActivity, HomeActivity::class.java))
        }
    }

    override fun onSuccessDbData(cloudDbAction: CloudDbAction?, dataList: List<CloudDBZoneObject?>?) {
        when (cloudDbAction) {
            CloudDbAction.GET_USER -> getUser(dataList as List<UsersInfoTable>)
            else -> {
            }
        }
    }

    override fun onSuccessDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {
        when (cloudDbAction) {
            CloudDbAction.DB_LOGIN -> {
                isDbLoggedIn = true
                cloudDbHelper?.createObjectType()
                cloudDbHelper?.openCloudDBZoneV2(CloudDbAction.OPEN_DB)
            }
            CloudDbAction.OPEN_DB ->{
                isDbReady = true
                loadAd()
            }
            else -> {
            }
        }
    }

    override fun onFailureDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {
        when (cloudDbAction) {
            CloudDbAction.OPEN_DB ->{
                isDbReady = false
                jump()
            }
            CloudDbAction.DB_LOGIN -> isDbLoggedIn = false
            CloudDbAction.INSERT_USER_INFO -> {
            }
            else -> showToast(message)
        }
    }

    //calling query for recently viewed courses
    private fun queryUser(uId: String?) {
        if (uId != null) {
            val cloudDBZoneQuery: CloudDBZoneQuery<*> = CloudDBZoneQuery.where(UsersInfoTable::class.java)
            cloudDBZoneQuery.equalTo("userId", uId)
            cloudDbHelper
                    ?.getCloudDbAllQueyCalls()
                    ?.queryUser(cloudDBZoneQuery as CloudDBZoneQuery<UsersInfoTable>?, CloudDbAction.GET_USER)
        }
    }
}