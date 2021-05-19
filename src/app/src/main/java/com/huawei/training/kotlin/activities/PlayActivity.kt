/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.training.kotlin.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Resources.NotFoundException
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.SurfaceHolder
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery
import com.huawei.hms.ads.AdListener
import com.huawei.hms.ads.AdParam
import com.huawei.hms.ads.InterstitialAd
import com.huawei.hms.videokit.player.WisePlayer
import com.huawei.hms.videokit.player.common.PlayerConstants.*
import com.huawei.training.R
import com.huawei.training.kotlin.adapters.TabsPagerAdapter
import com.huawei.training.kotlin.database.CloudDbAction
import com.huawei.training.kotlin.database.CloudDbUiCallbackListener
import com.huawei.training.kotlin.database.tables.CourseContentTable
import com.huawei.training.kotlin.database.tables.CourseDetailsTable
import com.huawei.training.kotlin.database.tables.CoursesCodelabDetailsTable
import com.huawei.training.kotlin.database.tables.ExamTable
import com.huawei.training.kotlin.listeners.OnPlayWindowListener
import com.huawei.training.kotlin.listeners.OnWisePlayerListener
import com.huawei.training.kotlin.utils.AppUtils
import com.huawei.training.kotlin.utils.video.*
import com.huawei.training.kotlin.utils.video.PlayControlUtil.isSurfaceView
import com.huawei.training.kotlin.utils.video.control.PlayControl
import com.huawei.training.kotlin.utils.view.PlayView
import java.io.Serializable
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class PlayActivity : BaseActivity(), OnPlayWindowListener, OnWisePlayerListener, CloudDbUiCallbackListener {
    private var interstitialAd: InterstitialAd? = null

    // Player view
    private var playerLayout: FrameLayout? = null

    // Play view
    private var playView: PlayView? = null

    // Play control
    private var playControl: PlayControl? = null

    // Whether the user is touch seekbar
    private var isUserTrackingTouch = false

    // Whether at the front desk
    private var isResume = false

    // Whether the suspend state
    private var isSuspend = false

    // Vertical screen properties
    private var systemUiVisibility = 0

    // Whether to suspend the buffer
    private var streamRequestMode = 0

    // Smooth/manual switch bitrate
    private var isAutoSwitchBitrate = false

    // Play complete
    private var isPlayComplete = false

    // Play the View is created
    private var hasSurfaceCreated = false

    // Play status
    private var isPlaying = false

    /**
     * The Tabs pager adapter.
     */
    var tabsPagerAdapter: TabsPagerAdapter? = null

    /**
     * The Co details.
     */
    var coDetails: CourseDetailsTable? = null

    /**
     * The Co content.
     */
    var coContent: CourseContentTable? = null

    /**
     * The Co codelab doc.
     */
    var coCodelabDoc: CoursesCodelabDetailsTable? = null

    /**
     * The Exam.
     */
    var exam: ExamTable? = null

    /**
     * The View pager.
     */
    var viewPager: ViewPager? = null

    /**
     * The Course id.
     */
    private var courseId = 0

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Keep the screen on
        window.setFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val courseName = intent.getStringExtra(Constants.COURSE_NAME)
        courseId = intent.getStringExtra(Constants.COURSE_ID)?.toInt()!!
        courseName?.let { setToolbar(it) }
        cloudDbHelper?.addCallBackListener(this)
        initView()
        loadInterstitialAd()
    }

    override fun onResume() {
        super.onResume()
        isResume = true
        if (hasSurfaceCreated) {
            playControl?.setBookmark()
            playControl?.playResume(ResumeType.KEEP)
            if (!updateViewHandler!!.hasMessages(Constants.PLAYING_WHAT)) {
                updateViewHandler?.sendEmptyMessage(Constants.PLAYING_WHAT)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isResume = false
        playView?.onPause()
        playControl?.onPause()
        if (updateViewHandler != null) {
            updateViewHandler?.removeCallbacksAndMessages(null)
        }
    }

    /**
     * init the layout
     */
    private fun initView() {
        playView = PlayView(this, this, this)
        setContentView(playView!!.contentView)
        playControl = PlayControl(this, this)
        // Some of the properties of preserving vertical screen
        systemUiVisibility = window.decorView.systemUiVisibility
        // Set the current vertical screen
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        viewPager = findViewById(R.id.view_pager)
        val share = findViewById<FloatingActionButton>(R.id.share)
        playerLayout = findViewById(R.id.frame_layout)
        val tabs = findViewById<TabLayout>(R.id.tabs)
        val linear_dec = findViewById<LinearLayout>(R.id.linear_dec)
        val linear_tabs = findViewById<LinearLayout>(R.id.linear_tabs)
        linear_dec.visibility = View.GONE
        linear_tabs.visibility = View.VISIBLE
        viewPager?.addOnPageChangeListener(
                object : OnPageChangeListener {
                    override fun onPageScrolled(position: Int, positionOffset: Float,
                                                positionOffsetPixels: Int) {
                    }

                    override fun onPageSelected(position: Int) {
                        when (position) {
                            0 -> {
                                isPlaying = false
                                changePlayState()
                                playerLayout?.setVisibility(View.VISIBLE)
                            }
                            1, 2 -> {
                                isPlaying = true
                                changePlayState()
                                playerLayout?.setVisibility(View.GONE)
                            }
                            else -> {
                            }
                        }
                    }

                    override fun onPageScrollStateChanged(state: Int) {}
                })
        tabs.setupWithViewPager(viewPager)
        // share app
        share.setOnClickListener { start: View? -> AppUtils.shareActivity(this@PlayActivity) }
        queryWithCourseId()
    }

    /**
     * Prepare playing
     */
    private fun ready() {
        playControl?.setCurrentPlayData(intentExtra)
        playView?.showBufferingView()
    }

    /**
     * get the video data
     *
     * @return the video data
     */
    private val intentExtra: Serializable?
        private get() {
            val intent = intent
            return intent?.getSerializableExtra(Constants.VIDEO_PLAY_DATA)
        }

    override fun surfaceCreated(holder: SurfaceHolder) {
        hasSurfaceCreated = true
        playControl?.setSurfaceView(playView?.surfaceView)
        if (isSuspend) {
            isSuspend = false
            playControl?.playResume(ResumeType.KEEP)
            if (!updateViewHandler?.hasMessages(Constants.PLAYING_WHAT)!!) {
                updateViewHandler?.sendEmptyMessage(Constants.PLAYING_WHAT)
            }
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        hasSurfaceCreated = false
        isSuspend = true
        playControl?.suspend()
    }

    override fun onLoadingUpdate(wisePlayer: WisePlayer, percent: Int) {
        runOnUiThread {
            if (percent < 100) {
                playView?.updateBufferingView(percent)
            } else {
                playView?.dismissBufferingView()
            }
        }
    }

    override fun onStartPlaying(wisePlayer: WisePlayer) {
        isPlayComplete = false
    }

    override fun onError(wisePlayer: WisePlayer, what: Int, extra: Int): Boolean {
        playView?.dismissBufferingView()
        updateViewHandler?.sendEmptyMessageDelayed(Constants.PLAY_ERROR_FINISH,
                Constants.DELAY_MILLIS_3000)
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isPlayComplete) {
            playControl?.savePlayProgress()
            isPlayComplete = false
        }
        playControl?.stop()
        playControl?.release()
        // Mute only on the current video effect
        //PlayControlUtil.isMute(false)
        if (updateViewHandler != null) {
            updateViewHandler?.removeCallbacksAndMessages(null)
            updateViewHandler = null
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
    override fun onStartTrackingTouch(seekBar: SeekBar) {
        isUserTrackingTouch = true
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        isPlayComplete = false
        playControl?.updateCurProgress(seekBar.progress)
        playView?.showBufferingView()
        playView?.updatePlayProgressView(
                seekBar.progress,
                playControl!!.bufferTime,
                playControl!!.bufferingSpeed,
                playControl!!.currentBitrate)
        isUserTrackingTouch = false
        updateViewHandler?.sendEmptyMessageDelayed(Constants.PLAYING_WHAT,
                Constants.DELAY_MILLIS_500)
    }

    // Update the player view
    @SuppressLint("HandlerLeak")
    private var updateViewHandler: Handler? = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                Constants.PLAYING_WHAT -> if (!isUserTrackingTouch) {
                    playView?.updatePlayProgressView(
                            playControl!!.currentTime,
                            playControl!!.bufferTime,
                            playControl!!.bufferingSpeed,
                            playControl!!.currentBitrate)
                    sendEmptyMessageDelayed(Constants.PLAYING_WHAT,
                            Constants.DELAY_MILLIS_500)
                }
                Constants.UPDATE_PLAY_STATE -> {
                    playView?.updatePlayCompleteView()
                    removeCallbacksAndMessages(null)
                }
                Constants.PLAY_ERROR_FINISH -> finish()
                else -> {
                }
            }
        }
    }

    // Set the play mode
    private fun setPlayMode(playMode: Int) {
        playControl?.setPlayMode(playMode, true)
    }

    /**
     * Sets cycle mode.
     *
     * @param isCycleMode the is cycle mode
     */
    // Set cycle mode
    fun setCycleMode(isCycleMode: Boolean) {
        playControl?.isCycleMode = isCycleMode
    }

    // Select the play speed
    private fun setPlaySpeed() {
        try {
            val showTextArray = resources.getStringArray(R.array.play_speed_text)
            val speedValue = DataFormatUtil.getPlaySpeedString(playControl!!.playSpeed)
            playView?.showSettingDialogValue(
                    Constants.PLAYER_SWITCH_PLAY_SPEED, Arrays.asList(*showTextArray), speedValue)
        } catch (exp: NotFoundException) {
        }
    }

    // Set the play speed
    private fun onSwitchPlaySpeed(speedValue: String) {
        playControl?.setPlaySpeed(speedValue)
        playView?.setSpeedButtonText(speedValue)
    }

    // Close logo
    private fun closeLogo() {
        playControl?.closeLogo()
    }

    // Whether to stop the downloading
    private fun onSwitchRequestMode(selectValue: String) {
        if (selectValue == StringUtil.getStringFromResId(
                        this@PlayActivity, R.string.video_keep_download)) {
            streamRequestMode = 0
        }
        if (selectValue == StringUtil.getStringFromResId(
                        this@PlayActivity, R.string.video_stop_download)) {
            streamRequestMode = 1
        }
        playControl?.setBufferingStatus(streamRequestMode == 0, true)
    }

    // Set the bitrate
    private fun onSwitchBitrate(itemSelect: String) {
        if (itemSelect == StringUtil.getStringFromResId(
                        this@PlayActivity, R.string.automatic_adaptation)) {
            if (isAutoSwitchBitrate) {
                playControl?.switchBitrateSmooth(0)
            } else {
                playControl?.switchBitrateDesignated(0)
            }
        } else {
            if (isAutoSwitchBitrate) {
                playControl?.switchBitrateSmooth(itemSelect.toInt())
            } else {
                playControl?.switchBitrateDesignated(itemSelect.toInt())
            }
        }
    }

    // Modify the state of play
    private fun changePlayState() {
        playControl?.setPlayData(isPlaying)
        if (isPlaying) {
            isPlaying = false
            updateViewHandler?.removeCallbacksAndMessages(null)
            playView?.setPlayView()
        } else {
            isPlaying = true
            isPlayComplete = false
            updateViewHandler?.sendEmptyMessage(Constants.PLAYING_WHAT)
            playView?.setPauseView()
        }
    }

    // Select the way to switch bitrate
    private fun switchBitrateAuto() {
        val showTextList: MutableList<String> = ArrayList()
        showTextList.add(resources.getString(R.string.video_bitrate_auto))
        showTextList.add(resources.getString(R.string.video_bitrate_designated))
        playView?.showSettingDialog(Constants.PLAYER_SWITCH_AUTO_DESIGNATED,
                showTextList, Constants.DIALOG_INDEX_ONE)
    }

    // Set the bandwidth switch
    private fun switchBandwidthMode() {
        val showTextList: MutableList<String> = ArrayList()
        showTextList.add(resources.getString(R.string.close_adaptive_bandwidth))
        showTextList.add(resources.getString(R.string.open_adaptive_bandwidth))
        playView?.showSettingDialog(Constants.PLAYER_SWITCH_BANDWIDTH_MODE,
                showTextList, Constants.DIALOG_INDEX_ONE)
    }

    // Set the play mode
    private fun switchPlayMode() {
        val showTextList: MutableList<String> = ArrayList()
        showTextList.add(resources.getString(R.string.play_video))
        showTextList.add(resources.getString(R.string.play_audio))
        playView?.showSettingDialog(Constants.PLAYER_SWITCH_PLAY_MODE,
                showTextList, playControl!!.playMode)
    }

    // If set up a video loop
    private fun switchPlayLoop() {
        val showTextList: MutableList<String> = ArrayList()
        showTextList.add(resources.getString(R.string.video_loop_play))
        showTextList.add(resources.getString(R.string.video_not_loop_play))
        playView?.showSettingDialog(
                Constants.PLAYER_SWITCH_LOOP_PLAY_MODE,
                showTextList,
                if (playControl!!.isCycleMode) Constants.DIALOG_INDEX_ONE else Constants.DIALOG_INDEX_TWO)
    }

    // Select whether the mute
    private fun switchVideoMute() {
        val showTextList: MutableList<String> = ArrayList()
        showTextList.add(resources.getString(R.string.video_mute))
        showTextList.add(resources.getString(R.string.video_not_mute))
        playView?.showSettingDialog(
                Constants.PLAYER_SWITCH_VIDEO_MUTE_MODE,
                showTextList,
                if (PlayControlUtil.isMute) Constants.DIALOG_INDEX_ONE else Constants.DIALOG_INDEX_TWO)
    }

    /*// Show the volume dialog
    private fun setVideoVolume() {
        DialogUtil.showSetVolumeDialog(
                this
        ) { inputText: String? -> playControl.setVolume(StringUtil.valueOf(inputText)) }
    }*/

    // Switching bitrate
    private fun switchBitrateMenu(isAuto: Boolean) {
        val bitrateList = playControl!!.bitrateList
        bitrateList.add(StringUtil.getStringFromResId(
                this@PlayActivity, R.string.automatic_adaptation))
        isAutoSwitchBitrate = isAuto
        val selectedValueIndex: Int
        val currentBitrate = playControl!!.currentBitrate
        selectedValueIndex = if (currentBitrate == 0) {
            bitrateList.size - 1
        } else {
            playControl!!.currentBitrateIndex
        }
        playView?.showSettingDialog(Constants.PLAYER_SWITCH_BITRATE,
                bitrateList, selectedValueIndex)
    }

    // Whether to stop the download dialog
    private fun stopRequestStream() {
        val showTextList: MutableList<String> = ArrayList()
        showTextList.add(StringUtil.getStringFromResId(
                this@PlayActivity, R.string.video_keep_download))
        showTextList.add(StringUtil.getStringFromResId(this@PlayActivity, R.string.video_stop_download))
        playView?.showSettingDialog(Constants.PLAYER_SWITCH_STOP_REQUEST_STREAM,
                showTextList, streamRequestMode)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.play_btn -> changePlayState()
            R.id.fullscreen_btn -> setFullScreen()
            R.id.play_speed_btn -> setPlaySpeed()
            else -> {
            }
        }
    }

    // Set up the full screen
    @SuppressLint("SourceLockedOrientationActivity")
    private fun setFullScreen() {
        if (DeviceUtil.isPortrait(applicationContext)) {
            playView!!.setFullScreenView(playControl!!.currentPlayName)
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            // Set up the full screen
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window
                    .decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            playControl!!.setSurfaceChange()
        }
    }

    override fun onBackPressed() {
        // Click the back button to return to the landscape conditions vertical screen
        if (!DeviceUtil.isPortrait(applicationContext)) {
            backPress()
        } else {
            super.onBackPressed()
        }
    }

    // Click the back button in the upper left corner
    @SuppressLint("SourceLockedOrientationActivity")
    private fun backPress() {
        if (!DeviceUtil.isPortrait(applicationContext)) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            playView?.setPortraitView()
            // Remove the full screen
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            window.decorView.systemUiVisibility = systemUiVisibility
            playControl?.setSurfaceChange()
        } else {
            finish()
        }
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        hasSurfaceCreated = true
        playControl?.setTextureView(playView!!.textureView)
        if (isSuspend) {
            isSuspend = false
            playControl?.playResume(ResumeType.KEEP)
            if (!updateViewHandler?.hasMessages(Constants.PLAYING_WHAT)!!) {
                updateViewHandler?.sendEmptyMessage(Constants.PLAYING_WHAT)
            }
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture,
                                             width: Int, height: Int) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        hasSurfaceCreated = false
        isSuspend = true
        playControl?.suspend()
        return false
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
    override fun onItemClick(pos: Int) {
        if (!isPlayComplete) {
            playControl?.savePlayProgress()
            isPlayComplete = false
        }
        if (updateViewHandler != null) {
            updateViewHandler?.removeCallbacksAndMessages(null)
        }
        val playEntity = playControl!!.getPlayFromPosition(pos)
        if (playEntity != null) {
            playControl?.reset()
            playView?.reset()
            if (playControl!!.initPlayFail()) {
                finish()
            } else {
                playControl?.setCurrentPlayData(playEntity)
                restartPlayer()
            }
        }
    }

    private fun msgSettings(itemSelect: String) {
        if (TextUtils.equals(
                        itemSelect, StringUtil.getStringFromResId(
                        this@PlayActivity, R.string.video_bitrate_title))) {
            switchBitrateAuto()
        } else if (TextUtils.equals(
                        itemSelect,
                        StringUtil.getStringFromResId(this@PlayActivity, R.string.video_stop_downloading))) {
            stopRequestStream()
        } else if (TextUtils.equals(
                        itemSelect, StringUtil.getStringFromResId(
                        this@PlayActivity, R.string.video_set_play_speed))) {
            setPlaySpeed()
        } else if (TextUtils.equals(
                        itemSelect,
                        StringUtil.getStringFromResId(
                                this@PlayActivity, R.string.video_set_bandwidth_mode))) {
            switchBandwidthMode()
        } else if (TextUtils.equals(
                        itemSelect, StringUtil.getStringFromResId(
                        this@PlayActivity, R.string.close_logo))) {
            closeLogo()
        } else if (TextUtils.equals(
                        itemSelect, StringUtil.getStringFromResId(
                        this@PlayActivity, R.string.play_mode))) {
            switchPlayMode()
        } else if (TextUtils.equals(
                        itemSelect, StringUtil.getStringFromResId(
                        this@PlayActivity, R.string.video_set_loop_play))) {
            switchPlayLoop()
        } else if (TextUtils.equals(
                        itemSelect, StringUtil.getStringFromResId(
                        this@PlayActivity, R.string.video_mute_setting))) {
            switchVideoMute()
        } else {
            //setVideoVolume()
        }
    }

    private fun playerSwitchAutoDesignated(itemSelect: String) {
        if (TextUtils.equals(
                        itemSelect, StringUtil.getStringFromResId(
                        this@PlayActivity, R.string.video_bitrate_auto))) {
            switchBitrateMenu(true)
        } else {
            switchBitrateMenu(false)
        }
    }

    private fun playerSwtchBandwidth(itemSelect: String) {
        if (TextUtils.equals(
                        itemSelect,
                        StringUtil.getStringFromResId(
                                this@PlayActivity, R.string.open_adaptive_bandwidth))) {
            playControl?.setBandwidthSwitchMode(
                    BandwidthSwitchMode.AUTO_SWITCH_MODE, true)
        } else {
            playControl?.setBandwidthSwitchMode(
                    BandwidthSwitchMode.MANUAL_SWITCH_MODE, true)
        }
    }

    private fun playerPlay(itemSelect: String) {
        if (TextUtils.equals(
                        itemSelect, StringUtil.getStringFromResId(this@PlayActivity, R.string.play_audio))) {
            setPlayMode(PlayMode.PLAY_MODE_AUDIO_ONLY)
        } else {
            setPlayMode(PlayMode.PLAY_MODE_NORMAL)
        }
    }

    private fun playerLoopPlay(itemSelect: String) {
        if (TextUtils.equals(
                        itemSelect, StringUtil.getStringFromResId(
                        this@PlayActivity, R.string.video_loop_play))) {
            setCycleMode(true)
        } else {
            setCycleMode(false)
        }
    }

    private fun playerVideo(itemSelect: String) {
        if (TextUtils.equals(
                        itemSelect, StringUtil.getStringFromResId(this@PlayActivity, R.string.video_mute))) {
            playControl?.setMute(true)
        } else {
            playControl?.setMute(false)
        }
    }

    override fun onSettingItemClick(itemSelect: String?, settingType: Int) {
        when (settingType) {
            Constants.MSG_SETTING -> itemSelect?.let { msgSettings(it) }
            Constants.PLAYER_SWITCH_AUTO_DESIGNATED -> itemSelect?.let { playerSwitchAutoDesignated(it) }
            Constants.PLAYER_SWITCH_STOP_REQUEST_STREAM -> itemSelect?.let { onSwitchRequestMode(it) }
            Constants.PLAYER_SWITCH_PLAY_SPEED -> itemSelect?.let { onSwitchPlaySpeed(it) }
            Constants.PLAYER_SWITCH_BANDWIDTH_MODE -> itemSelect?.let { playerSwtchBandwidth(it) }
            Constants.PLAYER_SWITCH_PLAY_MODE -> itemSelect?.let { playerPlay(it) }
            Constants.PLAYER_SWITCH_LOOP_PLAY_MODE -> itemSelect?.let { playerLoopPlay(it) }
            Constants.PLAYER_SWITCH_VIDEO_MUTE_MODE -> itemSelect?.let { playerVideo(it) }
            Constants.PLAYER_SWITCH_BITRATE -> itemSelect?.let { onSwitchBitrate(it) }
            else -> {
            }
        }
    }

    // Reset player play parameters
    private fun restartPlayer() {
        playControl?.setMute(false)
        playControl?.ready()
        if (hasSurfaceCreated) {
            if (isSurfaceView) {
                playControl?.setSurfaceView(playView?.surfaceView)
            } else {
                playControl?.setTextureView(playView?.textureView)
            }
            isSuspend = false
            playControl?.playResume(ResumeType.KEEP)
            if (!updateViewHandler?.hasMessages(Constants.PLAYING_WHAT)!!) {
                updateViewHandler?.sendEmptyMessage(Constants.PLAYING_WHAT)
            }
        }
    }

    override fun onEvent(wisePlayer: WisePlayer, what: Int, extra: Int, o: Any): Boolean {
        isPlaying = false
        return true
    }

    override fun onPlayEnd(wisePlayer: WisePlayer) {
        playControl?.clearPlayProgress()
        isPlaying = false
        isPlayComplete = true
        updateViewHandler?.sendEmptyMessageDelayed(
                Constants.UPDATE_PLAY_STATE, Constants.DELAY_MILLIS_1000)
    }

    override fun onReady(wisePlayer: WisePlayer) {
        playControl?.start()
        isPlaying = true
        runOnUiThread {
            playView?.updatePlayView(wisePlayer)
            if (isResume) {
                playView?.setPauseView()
            }
            playView?.setContentView(wisePlayer, playControl?.currentPlayName)
            updateViewHandler?.sendEmptyMessageDelayed(
                    Constants.PLAYING_WHAT, Constants.DELAY_MILLIS_500)
        }
    }

    override fun onResolutionUpdated(wisePlayer: WisePlayer, w: Int, h: Int) {
        playView?.setContentView(wisePlayer, playControl?.currentPlayName)
    }

    override fun onSeekEnd(wisePlayer: WisePlayer) {}
    private val adListener: AdListener = object : AdListener() {
        override fun onAdLoaded() {
            super.onAdLoaded()
            showInterstitial()
        }

        override fun onAdFailed(errorCode: Int) {
            if (playControl!!.initPlayFail()) {
                showToast("Player initialisation failed")
            } else {
                playControl?.ready()
            }
        }

        override fun onAdClosed() {
            super.onAdClosed()
            if (playControl!!.initPlayFail()) {
                showToast("Player initialisation failed")
            } else {
                playControl?.ready()
            }
        }

        override fun onAdClicked() {
            super.onAdClicked()
        }

        override fun onAdOpened() {
            super.onAdOpened()
        }
    }

    // load Interstitial Add
    private fun loadInterstitialAd() {
        interstitialAd = InterstitialAd(this)
        interstitialAd?.adId = resources.getString(R.string.video_ad_id)
        interstitialAd?.adListener = adListener
        val adParam = AdParam.Builder().build()
        interstitialAd?.loadAd(adParam)
    }

    // Display an interstitial ad.
    private fun showInterstitial() {
        if (interstitialAd != null && interstitialAd!!.isLoaded) {
            interstitialAd?.show()
        }
    }

    override fun onSuccessDbData(cloudDbAction: CloudDbAction?, dataList: List<CloudDBZoneObject?>?) {
        when (cloudDbAction) {
            CloudDbAction.GET_ALL_COURSES -> {
                coDetails = dataList?.get(0) as CourseDetailsTable
                queryCourseContent()
                playControl?.videoUrl = coDetails?.courseVideoUrl
                ready()
            }
            CloudDbAction.GET_EXAM_DETAILS -> {
                exam = dataList?.get(0) as ExamTable
                tabsPagerAdapter = TabsPagerAdapter(
                        this, supportFragmentManager,
                        coContent!!, coCodelabDoc!!, exam!!)
                viewPager?.adapter = tabsPagerAdapter
            }
            CloudDbAction.GET_CODE_LAB -> {
                coCodelabDoc = dataList?.get(0) as CoursesCodelabDetailsTable
                queryCourseExam()
            }
            CloudDbAction.GET_COURSE_CONTENT -> {
                coContent = dataList?.get(0) as CourseContentTable
                queryCodeLabDoc()
            }
            CloudDbAction.GET_COURSE_DOC -> {
            }
        }
    }

    override fun onSuccessDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {}
    override fun onFailureDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?) {}
    private fun queryWithCourseId() {
        val cloudDBZoneQuery: CloudDBZoneQuery<*> = CloudDBZoneQuery.where(CourseDetailsTable::class.java)
        cloudDBZoneQuery.equalTo("courseId", courseId)
        cloudDbHelper?.getCloudDbAllQueyCalls()?.queryCourseDetailsTable(
                cloudDBZoneQuery as CloudDBZoneQuery<CourseDetailsTable>?, CloudDbAction.GET_ALL_COURSES)
    }

    private fun queryCodeLabDoc() {
        val cloudDBZoneQuery: CloudDBZoneQuery<*> = CloudDBZoneQuery.where(
                CoursesCodelabDetailsTable::class.java)
        cloudDBZoneQuery.equalTo("courseId", courseId)
        cloudDbHelper?.getCloudDbAllQueyCalls()?.queryCodelab(
                cloudDBZoneQuery as CloudDBZoneQuery<CoursesCodelabDetailsTable>?, CloudDbAction.GET_CODE_LAB)
    }

    private fun queryCourseContent() {
        val cloudDBZoneQuery: CloudDBZoneQuery<*> = CloudDBZoneQuery.where(CourseContentTable::class.java)
        cloudDBZoneQuery.equalTo("courseId", ("" + courseId).toLong())
        cloudDbHelper?.getCloudDbAllQueyCalls()?.queryCourseContentTable(
                cloudDBZoneQuery as CloudDBZoneQuery<CourseContentTable>?, CloudDbAction.GET_COURSE_CONTENT)
    }

    private fun queryCourseExam() {
        val cloudDBZoneQuery: CloudDBZoneQuery<*> = CloudDBZoneQuery.where(ExamTable::class.java)
        cloudDBZoneQuery.equalTo("examId", courseId)
        cloudDbHelper?.getCloudDbAllQueyCalls()?.queryExamTableDetails(
                cloudDBZoneQuery as CloudDBZoneQuery<ExamTable>?, CloudDbAction.GET_EXAM_DETAILS)
    }
}