/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.training.java.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources.NotFoundException;
import android.graphics.SurfaceTexture;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.videokit.player.WisePlayer;
import com.huawei.hms.videokit.player.common.PlayerConstants.BandwidthSwitchMode;
import com.huawei.hms.videokit.player.common.PlayerConstants.PlayMode;
import com.huawei.hms.videokit.player.common.PlayerConstants.ResumeType;
import com.huawei.training.R;
import com.huawei.training.java.adapters.TabsPagerAdapter;
import com.huawei.training.java.database.CloudDbAction;
import com.huawei.training.java.database.CloudDbHelper;
import com.huawei.training.java.database.CloudDbUiCallbackListener;
import com.huawei.training.java.database.tables.CourseContentTable;
import com.huawei.training.java.database.tables.CourseDetailsTable;
import com.huawei.training.java.database.tables.CoursesCodelabDetailsTable;
import com.huawei.training.java.database.tables.ExamTable;
import com.huawei.training.java.listeners.OnPlayWindowListener;
import com.huawei.training.java.listeners.OnWisePlayerListener;
import com.huawei.training.java.utils.AppUtils;
import com.huawei.training.java.utils.video.Constants;
import com.huawei.training.java.utils.video.DataFormatUtil;
import com.huawei.training.java.utils.video.DeviceUtil;
import com.huawei.training.java.utils.video.DialogUtil;
import com.huawei.training.java.utils.video.PlayControlUtil;
import com.huawei.training.java.utils.video.StringUtil;
import com.huawei.training.java.utils.video.control.PlayControl;
import com.huawei.training.java.utils.video.entity.PlayEntity;
import com.huawei.training.java.utils.view.PlayView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class PlayActivity extends BaseActivity implements OnPlayWindowListener,
        OnWisePlayerListener, CloudDbUiCallbackListener {
    private InterstitialAd interstitialAd;
    // Player view
    private FrameLayout playerLayout;
    // Play view
    private PlayView playView;
    // Play control
    private PlayControl playControl;
    // Whether the user is touch seekbar
    private boolean isUserTrackingTouch = false;
    // Whether at the front desk
    private boolean isResume = false;
    // Whether the suspend state
    private boolean isSuspend = false;
    // Vertical screen properties
    private int systemUiVisibility = 0;
    // Whether to suspend the buffer
    private int streamRequestMode = 0;
    // Smooth/manual switch bitrate
    private boolean isAutoSwitchBitrate;
    // Play complete
    private boolean isPlayComplete = false;
    // Play the View is created
    private boolean hasSurfaceCreated = false;
    // Play status
    private boolean isPlaying = false;
    /**
     * The Tabs pager adapter.
     */
    TabsPagerAdapter tabsPagerAdapter;
    /**
     * The Co details.
     */
    CourseDetailsTable coDetails = null;
    /**
     * The Co content.
     */
    CourseContentTable coContent;
    /**
     * The Co codelab doc.
     */
    CoursesCodelabDetailsTable coCodelabDoc;
    /**
     * The Exam.
     */
    ExamTable exam;
    /**
     * The View pager.
     */
    ViewPager viewPager;
    /**
     * The Course id.
     */
    private int courseId = 0;
    private CloudDbHelper cloudDbHelper;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Keep the screen on
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        String courseName = getIntent().getStringExtra(Constants.COURSE_NAME);
        courseId = Integer.parseInt(Objects.requireNonNull(
                getIntent().getStringExtra(Constants.COURSE_ID)));
        if(courseName!=null){
            setToolbar(courseName);
        }
        cloudDbHelper=CloudDbHelper.getInstance(getApplicationContext());
        cloudDbHelper.addCallBackListener(this);
        initView();
        loadInterstitialAd();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
        if (hasSurfaceCreated) {
            playControl.setBookmark();
            playControl.playResume(ResumeType.KEEP);
            if (!updateViewHandler.hasMessages(Constants.PLAYING_WHAT)) {
                updateViewHandler.sendEmptyMessage(Constants.PLAYING_WHAT);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResume = false;
        playView.onPause();
        playControl.onPause();
        if (updateViewHandler != null) {
            updateViewHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * init the layout
     */
    private void initView() {
        playView = new PlayView(this, this, this);
        setContentView(playView.getContentView());
        playControl = new PlayControl(this, this);
        // Some of the properties of preserving vertical screen
        systemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
        // Set the current vertical screen
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        viewPager = findViewById(R.id.view_pager);
        FloatingActionButton share = findViewById(R.id.share);
        playerLayout = findViewById(R.id.frame_layout);
        TabLayout tabs = findViewById(R.id.tabs);
        LinearLayout linear_dec = findViewById(R.id.linear_dec);
        LinearLayout linear_tabs = findViewById(R.id.linear_tabs);
        linear_dec.setVisibility(View.GONE);
        linear_tabs.setVisibility(View.VISIBLE);

        viewPager.addOnPageChangeListener(
                new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset,
                                               int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        switch (position) {
                            case 0:
                                isPlaying = false;
                                changePlayState();
                                playerLayout.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                            case 2:
                                isPlaying = true;
                                changePlayState();
                                playerLayout.setVisibility(View.GONE);
                                break;
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
        tabs.setupWithViewPager(viewPager);
        // share app
        share.setOnClickListener(start -> AppUtils.shareActivity(PlayActivity.this));
        queryWithCourseId();
    }

    /**
     * Prepare playing
     */
    private void ready() {
        playControl.setCurrentPlayData(getIntentExtra());
        playView.showBufferingView();
    }

    /**
     * get the video data
     *
     * @return the video data
     */
    private Serializable getIntentExtra() {
        Intent intent = getIntent();
        if (intent != null) {
            return intent.getSerializableExtra(Constants.VIDEO_PLAY_DATA);
        } else {
            return null;
        }
    }

    @Override
    public void surfaceCreated(@NotNull SurfaceHolder holder) {
        hasSurfaceCreated = true;
        playControl.setSurfaceView(playView.getSurfaceView());
        if (isSuspend) {
            isSuspend = false;
            playControl.playResume(ResumeType.KEEP);
            if (!updateViewHandler.hasMessages(Constants.PLAYING_WHAT)) {
                updateViewHandler.sendEmptyMessage(Constants.PLAYING_WHAT);
            }
        }
    }

    @Override
    public void surfaceChanged(@NotNull SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(@NotNull SurfaceHolder holder) {
        hasSurfaceCreated = false;
        isSuspend = true;
        playControl.suspend();
    }

    @Override
    public void onLoadingUpdate(WisePlayer wisePlayer, final int percent) {
        runOnUiThread(
                () -> {
                    if (percent < 100) {
                        playView.updateBufferingView(percent);
                    } else {
                        playView.dismissBufferingView();
                    }
                });
    }

    @Override
    public void onStartPlaying(WisePlayer wisePlayer) {
        isPlayComplete = false;
    }

    @Override
    public boolean onError(WisePlayer wisePlayer, int what, int extra) {
        playView.dismissBufferingView();
        updateViewHandler.sendEmptyMessageDelayed(Constants.PLAY_ERROR_FINISH,
                Constants.DELAY_MILLIS_3000);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isPlayComplete) {
            playControl.savePlayProgress();
            isPlayComplete = false;
        }
        playControl.stop();
        playControl.release();
        // Mute only on the current video effect
        PlayControlUtil.setIsMute(false);
        if (updateViewHandler != null) {
            updateViewHandler.removeCallbacksAndMessages(null);
            updateViewHandler = null;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isUserTrackingTouch = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isPlayComplete = false;
        playControl.updateCurProgress(seekBar.getProgress());
        playView.showBufferingView();
        playView.updatePlayProgressView(
                seekBar.getProgress(),
                playControl.getBufferTime(),
                playControl.getBufferingSpeed(),
                playControl.getCurrentBitrate());
        isUserTrackingTouch = false;
        updateViewHandler.sendEmptyMessageDelayed(Constants.PLAYING_WHAT,
                Constants.DELAY_MILLIS_500);
    }

    // Update the player view
    @SuppressLint("HandlerLeak")
    private Handler updateViewHandler =
            new Handler() {
                @Override
                public void handleMessage(@NotNull Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case Constants.PLAYING_WHAT:
                            if (!isUserTrackingTouch) {
                                playView.updatePlayProgressView(
                                        playControl.getCurrentTime(),
                                        playControl.getBufferTime(),
                                        playControl.getBufferingSpeed(),
                                        playControl.getCurrentBitrate());
                                sendEmptyMessageDelayed(Constants.PLAYING_WHAT,
                                        Constants.DELAY_MILLIS_500);
                            }
                            break;
                        case Constants.UPDATE_PLAY_STATE:
                            playView.updatePlayCompleteView();
                            removeCallbacksAndMessages(null);
                            break;
                        case Constants.PLAY_ERROR_FINISH:
                            finish();
                            break;
                        default:
                            break;
                    }
                }
            };

    // Set the play mode
    private void setPlayMode(int playMode) {
        playControl.setPlayMode(playMode, true);
    }

    /**
     * Sets cycle mode.
     *
     * @param isCycleMode the is cycle mode
     */
// Set cycle mode
    public void setCycleMode(boolean isCycleMode) {
        playControl.setCycleMode(isCycleMode);
    }

    // Select the play speed
    private void setPlaySpeed() {
        try {
            String[] showTextArray = getResources().getStringArray(R.array.play_speed_text);
            String speedValue = DataFormatUtil.getPlaySpeedString(playControl.getPlaySpeed());
            playView.showSettingDialogValue(
                    Constants.PLAYER_SWITCH_PLAY_SPEED, Arrays.asList(showTextArray), speedValue);
        } catch (NotFoundException exp) {
        }
    }

    // Set the play speed
    private void onSwitchPlaySpeed(String speedValue) {
        playControl.setPlaySpeed(speedValue);
        playView.setSpeedButtonText(speedValue);
    }

    // Close logo
    private void closeLogo() {
        playControl.closeLogo();
    }

    // Whether to stop the downloading
    private void onSwitchRequestMode(String selectValue) {
        if (selectValue.equals(StringUtil.getStringFromResId(
                PlayActivity.this, R.string.video_keep_download))) {
            streamRequestMode = 0;
        }
        if (selectValue.equals(StringUtil.getStringFromResId(
                PlayActivity.this, R.string.video_stop_download))) {
            streamRequestMode = 1;
        }
        playControl.setBufferingStatus(streamRequestMode == 0, true);
    }

    // Set the bitrate
    private void onSwitchBitrate(String itemSelect) {
        if (itemSelect.equals(StringUtil.getStringFromResId(
                PlayActivity.this, R.string.automatic_adaptation))) {
            if (isAutoSwitchBitrate) {
                playControl.switchBitrateSmooth(0);
            } else {
                playControl.switchBitrateDesignated(0);
            }
        } else {
            if (isAutoSwitchBitrate) {
                playControl.switchBitrateSmooth(Integer.parseInt(itemSelect));
            } else {
                playControl.switchBitrateDesignated(Integer.parseInt(itemSelect));
            }
        }
    }

    // Modify the state of play
    private void changePlayState() {
        playControl.setPlayData(isPlaying);
        if (isPlaying) {
            isPlaying = false;
            updateViewHandler.removeCallbacksAndMessages(null);
            playView.setPlayView();
        } else {
            isPlaying = true;
            isPlayComplete = false;
            updateViewHandler.sendEmptyMessage(Constants.PLAYING_WHAT);
            playView.setPauseView();
        }
    }

    // Select the way to switch bitrate
    private void switchBitrateAuto() {
        List<String> showTextList = new ArrayList<>();
        showTextList.add(getResources().getString(R.string.video_bitrate_auto));
        showTextList.add(getResources().getString(R.string.video_bitrate_designated));
        playView.showSettingDialog(Constants.PLAYER_SWITCH_AUTO_DESIGNATED,
                showTextList, Constants.DIALOG_INDEX_ONE);
    }

    // Set the bandwidth switch
    private void switchBandwidthMode() {
        List<String> showTextList = new ArrayList<>();
        showTextList.add(getResources().getString(R.string.close_adaptive_bandwidth));
        showTextList.add(getResources().getString(R.string.open_adaptive_bandwidth));
        playView.showSettingDialog(Constants.PLAYER_SWITCH_BANDWIDTH_MODE,
                showTextList, Constants.DIALOG_INDEX_ONE);
    }

    // Set the play mode
    private void switchPlayMode() {
        List<String> showTextList = new ArrayList<>();
        showTextList.add(getResources().getString(R.string.play_video));
        showTextList.add(getResources().getString(R.string.play_audio));
        playView.showSettingDialog(Constants.PLAYER_SWITCH_PLAY_MODE,
                showTextList, playControl.getPlayMode());
    }

    // If set up a video loop
    private void switchPlayLoop() {
        List<String> showTextList = new ArrayList<>();
        showTextList.add(getResources().getString(R.string.video_loop_play));
        showTextList.add(getResources().getString(R.string.video_not_loop_play));
        playView.showSettingDialog(
                Constants.PLAYER_SWITCH_LOOP_PLAY_MODE,
                showTextList,
                playControl.isCycleMode() ? Constants.DIALOG_INDEX_ONE : Constants.DIALOG_INDEX_TWO);
    }

    // Select whether the mute
    private void switchVideoMute() {
        List<String> showTextList = new ArrayList<>();
        showTextList.add(getResources().getString(R.string.video_mute));
        showTextList.add(getResources().getString(R.string.video_not_mute));
        playView.showSettingDialog(
                Constants.PLAYER_SWITCH_VIDEO_MUTE_MODE,
                showTextList,
                PlayControlUtil.isMute() ? Constants.DIALOG_INDEX_ONE : Constants.DIALOG_INDEX_TWO);
    }

    // Show the volume dialog
    private void setVideoVolume() {
        DialogUtil.showSetVolumeDialog(
                this,
                inputText -> playControl.setVolume(StringUtil.valueOf(inputText)));
    }

    // Switching bitrate
    private void switchBitrateMenu(boolean isAuto) {
        List<String> bitrateList = playControl.getBitrateList();
        bitrateList.add(StringUtil.getStringFromResId(
                PlayActivity.this, R.string.automatic_adaptation));
        isAutoSwitchBitrate = isAuto;
        int selectedValueIndex;
        int currentBitrate = playControl.getCurrentBitrate();
        if (currentBitrate == 0) {
            selectedValueIndex = bitrateList.size() - 1;
        } else {
            selectedValueIndex = playControl.getCurrentBitrateIndex();
        }
        playView.showSettingDialog(Constants.PLAYER_SWITCH_BITRATE,
                bitrateList, selectedValueIndex);
    }

    // Whether to stop the download dialog
    private void stopRequestStream() {
        List<String> showTextList = new ArrayList<>();
        showTextList.add(StringUtil.getStringFromResId(
                PlayActivity.this, R.string.video_keep_download));
        showTextList.add(StringUtil.getStringFromResId(PlayActivity.this, R.string.video_stop_download));
        playView.showSettingDialog(Constants.PLAYER_SWITCH_STOP_REQUEST_STREAM,
                showTextList, streamRequestMode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.play_btn:
                changePlayState();
                break;
            case R.id.fullscreen_btn:
                setFullScreen();
                break;
            case R.id.play_speed_btn:
                setPlaySpeed();
                break;
            default:
                break;
        }
    }

    // Set up the full screen
    @SuppressLint("SourceLockedOrientationActivity")
    private void setFullScreen() {
        if (DeviceUtil.isPortrait(getApplicationContext())) {
            playView.setFullScreenView(playControl.getCurrentPlayName());
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            // Set up the full screen
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            playControl.setSurfaceChange();
        }
    }

    @Override
    public void onBackPressed() {
        // Click the back button to return to the landscape conditions vertical screen
        if (!DeviceUtil.isPortrait(getApplicationContext())) {
            backPress();
        } else {
            super.onBackPressed();
        }
    }

    // Click the back button in the upper left corner
    @SuppressLint("SourceLockedOrientationActivity")
    private void backPress() {
        if (!DeviceUtil.isPortrait(getApplicationContext())) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            playView.setPortraitView();
            // Remove the full screen
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().getDecorView().setSystemUiVisibility(systemUiVisibility);
            playControl.setSurfaceChange();
        } else {
            finish();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(@NotNull SurfaceTexture surface, int width, int height) {
        hasSurfaceCreated = true;
        playControl.setTextureView(playView.getTextureView());
        if (isSuspend) {
            isSuspend = false;
            playControl.playResume(ResumeType.KEEP);
            if (!updateViewHandler.hasMessages(Constants.PLAYING_WHAT)) {
                updateViewHandler.sendEmptyMessage(Constants.PLAYING_WHAT);
            }
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(@NotNull SurfaceTexture surface,
                                            int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(@NotNull SurfaceTexture surface) {
        hasSurfaceCreated = false;
        isSuspend = true;
        playControl.suspend();
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(@NotNull SurfaceTexture surface) {
    }

    @Override
    public void onItemClick(int pos) {
        if (!isPlayComplete) {
            playControl.savePlayProgress();
            isPlayComplete = false;
        }
        if (updateViewHandler != null) {
            updateViewHandler.removeCallbacksAndMessages(null);
        }
        PlayEntity playEntity = playControl.getPlayFromPosition(pos);
        if (playEntity != null) {
            playControl.reset();
            playView.reset();
            if (playControl.initPlayFail()) {
                finish();
            } else {
                playControl.setCurrentPlayData(playEntity);
                restartPlayer();
            }
        }
    }

    private void msgSettings(String itemSelect) {
        if (TextUtils.equals(
                itemSelect, StringUtil.getStringFromResId(
                        PlayActivity.this, R.string.video_bitrate_title))) {
            switchBitrateAuto();
        } else if (TextUtils.equals(
                itemSelect,
                StringUtil.getStringFromResId(PlayActivity.this, R.string.video_stop_downloading))) {
            stopRequestStream();
        } else if (TextUtils.equals(
                itemSelect, StringUtil.getStringFromResId(
                        PlayActivity.this, R.string.video_set_play_speed))) {
            setPlaySpeed();
        } else if (TextUtils.equals(
                itemSelect,
                StringUtil.getStringFromResId(
                        PlayActivity.this, R.string.video_set_bandwidth_mode))) {
            switchBandwidthMode();
        } else if (TextUtils.equals(
                itemSelect, StringUtil.getStringFromResId(
                        PlayActivity.this, R.string.close_logo))) {
            closeLogo();
        } else if (TextUtils.equals(
                itemSelect, StringUtil.getStringFromResId(
                        PlayActivity.this, R.string.play_mode))) {
            switchPlayMode();
        } else if (TextUtils.equals(
                itemSelect, StringUtil.getStringFromResId(
                        PlayActivity.this, R.string.video_set_loop_play))) {
            switchPlayLoop();
        } else if (TextUtils.equals(
                itemSelect, StringUtil.getStringFromResId(
                        PlayActivity.this, R.string.video_mute_setting))) {
            switchVideoMute();
        } else {
            setVideoVolume();
        }
    }

    private void playerSwitchAutoDesignated(String itemSelect) {
        if (TextUtils.equals(
                itemSelect, StringUtil.getStringFromResId(
                        PlayActivity.this, R.string.video_bitrate_auto))) {
            switchBitrateMenu(true);
        } else {
            switchBitrateMenu(false);
        }
    }

    private void playerSwtchBandwidth(String itemSelect) {
        if (TextUtils.equals(
                itemSelect,
                StringUtil.getStringFromResId(
                        PlayActivity.this, R.string.open_adaptive_bandwidth))) {
            playControl.setBandwidthSwitchMode(
                    BandwidthSwitchMode.AUTO_SWITCH_MODE, true);
        } else {
            playControl.setBandwidthSwitchMode(
                    BandwidthSwitchMode.MANUAL_SWITCH_MODE, true);
        }
    }

    private void playerPlay(String itemSelect) {
        if (TextUtils.equals(
                itemSelect, StringUtil.getStringFromResId(PlayActivity.this, R.string.play_audio))) {
            setPlayMode(PlayMode.PLAY_MODE_AUDIO_ONLY);
        } else {
            setPlayMode(PlayMode.PLAY_MODE_NORMAL);
        }
    }

    private void playerLoopPlay(String itemSelect) {
        if (TextUtils.equals(
                itemSelect, StringUtil.getStringFromResId(
                        PlayActivity.this, R.string.video_loop_play))) {
            setCycleMode(true);
        } else {
            setCycleMode(false);
        }
    }

    private void playerVideo(String itemSelect) {
        if (TextUtils.equals(
                itemSelect, StringUtil.getStringFromResId(PlayActivity.this, R.string.video_mute))) {
            playControl.setMute(true);
        } else {
            playControl.setMute(false);
        }
    }

    @Override
    public void onSettingItemClick(String itemSelect, int settingType) {
        switch (settingType) {
            case Constants.MSG_SETTING:
                msgSettings(itemSelect);
                break;
            case Constants.PLAYER_SWITCH_AUTO_DESIGNATED:
                playerSwitchAutoDesignated(itemSelect);
                break;
            case Constants.PLAYER_SWITCH_STOP_REQUEST_STREAM:
                onSwitchRequestMode(itemSelect);
                break;
            case Constants.PLAYER_SWITCH_PLAY_SPEED:
                onSwitchPlaySpeed(itemSelect);
                break;
            case Constants.PLAYER_SWITCH_BANDWIDTH_MODE:
                playerSwtchBandwidth(itemSelect);
                break;
            case Constants.PLAYER_SWITCH_PLAY_MODE:
                playerPlay(itemSelect);
                break;
            case Constants.PLAYER_SWITCH_LOOP_PLAY_MODE:
                playerLoopPlay(itemSelect);
                break;
            case Constants.PLAYER_SWITCH_VIDEO_MUTE_MODE:
                playerVideo(itemSelect);
                break;
            case Constants.PLAYER_SWITCH_BITRATE:
                onSwitchBitrate(itemSelect);
                break;
            default:
                break;
        }
    }

    // Reset player play parameters
    private void restartPlayer() {
        playControl.setMute(false);
        playControl.ready();
        if (hasSurfaceCreated) {
            if (PlayControlUtil.isSurfaceView()) {
                playControl.setSurfaceView(playView.getSurfaceView());
            } else {
                playControl.setTextureView(playView.getTextureView());
            }
            isSuspend = false;
            playControl.playResume(ResumeType.KEEP);
            if (!updateViewHandler.hasMessages(Constants.PLAYING_WHAT)) {
                updateViewHandler.sendEmptyMessage(Constants.PLAYING_WHAT);
            }
        }
    }

    @Override
    public boolean onEvent(WisePlayer wisePlayer, int what, int extra, Object o) {
        isPlaying = false;
        return true;
    }

    @Override
    public void onPlayEnd(WisePlayer wisePlayer) {
        playControl.clearPlayProgress();
        isPlaying = false;
        isPlayComplete = true;
        updateViewHandler.sendEmptyMessageDelayed(
                Constants.UPDATE_PLAY_STATE, Constants.DELAY_MILLIS_1000);
    }

    @Override
    public void onReady(final WisePlayer wisePlayer) {
        wisePlayer.start();
        isPlaying = true;
        runOnUiThread(
                () -> {
                    playView.updatePlayView(wisePlayer);
                    if (isResume) {
                        playView.setPauseView();
                    }
                    playView.setContentView(wisePlayer, playControl.getCurrentPlayName());

                });
    }

    @Override
    public void onResolutionUpdated(WisePlayer wisePlayer, int w, int h) {
        playView.setContentView(wisePlayer, playControl.getCurrentPlayName());
    }

    @Override
    public void onSeekEnd(WisePlayer wisePlayer) {
    }

    private AdListener adListener =
            new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    showInterstitial();
                }

                @Override
                public void onAdFailed(int errorCode) {
                    if (playControl.initPlayFail()) {
                        showToast("Player initialisation failed");
                    } else {
                        playControl.ready();
                        if (hasSurfaceCreated) {
                            if (PlayControlUtil.isSurfaceView()) {
                                playControl.setSurfaceView(playView.getSurfaceView());
                            } else {
                                playControl.setTextureView(playView.getTextureView());
                            }
                            isSuspend = false;
                            playControl.playResume(ResumeType.KEEP);
                            if (!updateViewHandler.hasMessages(Constants.PLAYING_WHAT)) {
                                updateViewHandler.sendEmptyMessage(Constants.PLAYING_WHAT);
                            }
                        }
                    }
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    if (playControl.initPlayFail()) {
                        showToast("Player initialisation failed");
                    } else {
                        playControl.ready();
                        if (hasSurfaceCreated) {
                            if (PlayControlUtil.isSurfaceView()) {
                                playControl.setSurfaceView(playView.getSurfaceView());
                            } else {
                                playControl.setTextureView(playView.getTextureView());
                            }
                            isSuspend = false;
                            playControl.playResume(ResumeType.KEEP);
                            if (!updateViewHandler.hasMessages(Constants.PLAYING_WHAT)) {
                                updateViewHandler.sendEmptyMessage(Constants.PLAYING_WHAT);
                            }
                        }
                    }
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }
            };

    // load Interstitial Add
    private void loadInterstitialAd() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdId(getResources().getString(R.string.video_ad_id));
        interstitialAd.setAdListener(adListener);
        AdParam adParam = new AdParam.Builder().build();
        interstitialAd.loadAd(adParam);
    }

    // Display an interstitial ad.
    private void showInterstitial() {
        if (interstitialAd != null && interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    @Override
    public void onSuccessDbData(CloudDbAction cloudDbAction, List<CloudDBZoneObject> dataList) {
        switch (cloudDbAction) {
            case GET_ALL_COURSES:
                List<CourseDetailsTable> coDetailsList = (List) dataList;
                this.coDetails = coDetailsList.get(0);
                queryCourseContent();
                playControl.videoUrl = coDetails.getCourseVideoUrl();
                ready();
                break;
            case GET_EXAM_DETAILS:
                List<ExamTable> examList = (List) dataList;
                this.exam = examList.get(0);
                tabsPagerAdapter =
                        new TabsPagerAdapter(
                                this, getSupportFragmentManager(),
                                coContent, coCodelabDoc, exam);
                viewPager.setAdapter(tabsPagerAdapter);
                break;
            case GET_CODE_LAB:
                List<CoursesCodelabDetailsTable> coCodelabDocList = (List) dataList;
                this.coCodelabDoc = coCodelabDocList.get(0);
                queryCourseExam();
                break;
            case GET_COURSE_CONTENT:
                List<CourseContentTable> coContentList = (List) dataList;
                if (coContentList.size() > 0){
                    this.coContent = coContentList.get(0);
                    queryCodeLabDoc();
                }
                break;
            case GET_COURSE_DOC:
                break;
        }
    }

    @Override
    public void onSuccessDbQueryMessage(CloudDbAction cloudDbAction, String message) {
    }

    @Override
    public void onFailureDbQueryMessage(CloudDbAction cloudDbAction, String message) {
    }

    private void queryWithCourseId() {
        CloudDBZoneQuery cloudDBZoneQuery = CloudDBZoneQuery.where(CourseDetailsTable.class);
        cloudDBZoneQuery.equalTo("courseId", courseId);
        cloudDbHelper.getCloudDbQueyCalls().queryCourseDetailsTable(
                cloudDBZoneQuery, CloudDbAction.GET_ALL_COURSES);
    }

    private void queryCodeLabDoc() {
        CloudDBZoneQuery cloudDBZoneQuery = CloudDBZoneQuery.where(
                CoursesCodelabDetailsTable.class);
        cloudDBZoneQuery.equalTo("courseId", courseId);
        cloudDbHelper.getCloudDbQueyCalls().queryCodelab(
                cloudDBZoneQuery, CloudDbAction.GET_CODE_LAB);
    }

    private void queryCourseContent() {
        CloudDBZoneQuery cloudDBZoneQuery = CloudDBZoneQuery.where(CourseContentTable.class);
        cloudDBZoneQuery.equalTo("courseId", Long.parseLong("" + courseId));
        cloudDbHelper.getCloudDbQueyCalls().queryCourseContentTable(
                cloudDBZoneQuery, CloudDbAction.GET_COURSE_CONTENT);
    }

    private void queryCourseExam() {
        CloudDBZoneQuery cloudDBZoneQuery = CloudDBZoneQuery.where(ExamTable.class);
        cloudDBZoneQuery.equalTo("examId", courseId);
        cloudDbHelper.getCloudDbQueyCalls().queryExamTableDetails(
                cloudDBZoneQuery, CloudDbAction.GET_EXAM_DETAILS);
    }
}