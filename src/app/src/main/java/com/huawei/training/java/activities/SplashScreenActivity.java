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

package com.huawei.training.java.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.databinding.DataBindingUtil;

import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.AudioFocusType;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.splash.SplashAdDisplayListener;
import com.huawei.hms.ads.splash.SplashView;
import com.huawei.training.R;
import com.huawei.training.java.database.CloudDbAction;
import com.huawei.training.java.database.CloudDbUiCallbackListener;
import com.huawei.training.java.database.tables.UsersInfoTable;
import com.huawei.training.databinding.ActivitySplashScreenBinding;
import com.huawei.training.java.models.UserObj;
import com.huawei.training.java.utils.AppUtils;
import com.huawei.training.java.utils.LearningApplication;
import com.huawei.training.java.utils.video.Constants;

import java.util.List;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class SplashScreenActivity extends BaseActivity implements CloudDbUiCallbackListener {

    /**
     * The Binding.
     */
    ActivitySplashScreenBinding binding;
    private boolean hasPaused = false;
    /**
     * The Utils.
     */
    AppUtils utils;
    /**
     * The Is db ready.
     */
    Boolean isDbReady = false;
    /**
     * The Is db logged in.
     */
    Boolean isDbLoggedIn = false;

    // Callback handler used when the ad display timeout message is received.
    private Handler timeoutHandler =
            new Handler(
                    msg -> {
                        if (SplashScreenActivity.this.hasWindowFocus()) {
                            jump();
                        }
                        return false;
                    });

    private SplashView.SplashAdLoadListener splashAdLoadListener =
            new SplashView.SplashAdLoadListener() {
                @Override
                public void onAdLoaded() {
                    // Call this method when an ad is successfully loaded.
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    // Call this method when an ad fails to be loaded.
                    jump();
                }

                @Override
                public void onAdDismissed() {
                    // Call this method when the ad display is complete.
                    jump();
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);
        HwAds.init(this);
        utils = new AppUtils(SplashScreenActivity.this);
        cloudDbHelper.addCallBackListener(this);
        cloudDbHelper.getCloudDbQueyCalls().login(this, CloudDbAction.DB_LOGIN);

    }

    private void loadAd() {
        AdParam adParam = new AdParam.Builder().build();
        binding.splashAdView.setAdDisplayListener(new SplashAdDisplayListener() {
            @Override
            public void onAdShowed() {
            }
            @Override
            public void onAdClick() {
            }
        });

        // Set a default app launch image.
        binding.splashAdView.setSloganResId(R.drawable.default_slogan);
        binding.splashAdView.setLogo(findViewById(R.id.logo_area));
        // Set a logo image.
        binding.splashAdView.setLogoResId(R.mipmap.ic_launcher);
        // Set logo description.
        binding.splashAdView.setMediaNameResId(R.string.media_name);
        // Set the audio focus type for a video splash ad.
        binding.splashAdView.setAudioFocusType(AudioFocusType.NOT_GAIN_AUDIO_FOCUS_WHEN_MUTE);
        binding.splashAdView.load(
                getString(R.string.ad_id_splash),
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT,
                adParam,
                splashAdLoadListener);
        // Remove the timeout message from the message queue.
        timeoutHandler.removeMessages(Constants.MSG_AD_TIMEOUT);
        // Send a delay message to ensure that the app
        // home screen can be displayed when the ad display times out.
        timeoutHandler.sendEmptyMessageDelayed(Constants.MSG_AD_TIMEOUT, Constants.AD_TIMEOUT);
    }

    // Switch from the splash ad screen to the app home screen when the ad display is complete.
    private void jump() {
        if (!hasPaused) {
            hasPaused = true;
            if (!isDbReady) {
                finish();
                showToast("Please Try Again");
            } else {
                checkLoginStatus();
            }
            new Handler(Looper.getMainLooper())
                    .postDelayed(
                            this::finish,
                            Constants.DELAY_MILLIS_1000);
        }
    }

    private void checkLoginStatus() {
        if (utils.getPref(Constants.UID) == null) {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        } else {
            queryUser(utils.getPref(Constants.UID));
        }
    }

    // Set this parameter to true when exiting the
    // app to ensure that the app home screen is not displayed.
    @Override
    protected void onStop() {
        timeoutHandler.removeMessages(Constants.MSG_AD_TIMEOUT);
        hasPaused = true;
        super.onStop();
    }

    // Call this method when returning to the splash ad screen
    // from another screen to access the app home screen.
    @Override
    protected void onRestart() {
        super.onRestart();
        hasPaused = false;
        jump();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.splashAdView.destroyView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.splashAdView.pauseView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.splashAdView.resumeView();
    }

    private void getUser(List<UsersInfoTable> usersList) {
        if (usersList.isEmpty()) {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
        } else {
            UsersInfoTable userInfo = usersList.get(0);
            UserObj userObj = new UserObj();
            userObj.setuId(userInfo.getUserId());
            userObj.setFirstName(userInfo.getDisplayName());
            userObj.setEmailId(userInfo.getEmail());
            ((LearningApplication) this.getApplicationContext()).setUserObj(userObj);
            ((LearningApplication) this.getApplicationContext()).setUserStatus();
            startActivity(new Intent(SplashScreenActivity.this, HomeActivity.class));
        }
    }

    @Override
    public void onSuccessDbData(CloudDbAction cloudDbAction, List<CloudDBZoneObject> dataList) {
        switch (cloudDbAction) {
            case GET_USER:
                getUser((List) dataList);
                break;
            default:
                break;
        }
    }

    @Override
    public void onSuccessDbQueryMessage(CloudDbAction cloudDbAction, String message) {
        switch (cloudDbAction) {
            case DB_LOGIN:
                isDbLoggedIn = true;
                cloudDbHelper.createObjectType();
                cloudDbHelper.openCloudDBZoneV2(CloudDbAction.OPEN_DB);
                break;
            case OPEN_DB:
                isDbReady = true;
                loadAd();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailureDbQueryMessage(CloudDbAction cloudDbAction, String message) {
        switch (cloudDbAction) {
            case OPEN_DB:
                isDbReady = false;
                jump();
                break;
            case DB_LOGIN:
                isDbLoggedIn = false;
                break;
            case INSERT_USER_INFO:
                break;
            default:
                showToast(message);
                break;
        }
    }

    //calling query for recently viewed courses
    private void queryUser(String uId) {
        if (uId != null) {
            CloudDBZoneQuery cloudDBZoneQuery = CloudDBZoneQuery.where(UsersInfoTable.class);
            cloudDBZoneQuery.equalTo("userId", uId);
            cloudDbHelper
                    .getCloudDbQueyCalls()
                    .queryUser(cloudDBZoneQuery, CloudDbAction.GET_USER);
        }
    }
}
