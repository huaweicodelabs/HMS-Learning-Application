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
package com.huawei.training.kotlin.utils.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.*
import com.huawei.hms.videokit.player.WisePlayer
import com.huawei.training.R
import com.huawei.training.kotlin.listeners.OnPlayWindowListener
import com.huawei.training.kotlin.listeners.OnWisePlayerListener
import com.huawei.training.kotlin.utils.video.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class PlayView
/**
 * Constructor
 *
 * @param context              Context
 * @param onPlayWindowListener Android listener
 * @param onWisePlayerListener VideoKit SDK listener
 */(
        // Context
        private val context: Context, // Android listener
        private val onPlayWindowListener: OnPlayWindowListener, // VideoKit SDK listener
        private val onWisePlayerListener: OnWisePlayerListener) {

    /**
     * Get SurfaceView
     *
     * @return SurfaceView surface view
     */
    // Play SurfaceView
    var surfaceView: SurfaceView? = null
        private set

    /**
     * Get TextureView
     *
     * @return TextureView texture view
     */
    // Play TextureView
    var textureView: TextureView? = null
        private set

    // Play seekBar
    private var seekBar: SeekBar? = null

    // Current play time
    private var currentTimeTv: TextView? = null

    // Current play total time
    private var totalTimeTv: TextView? = null

    // Play/Stop button
    private var playImg: ImageView? = null

    // Video buffer view
    private var videoBufferLayout: RelativeLayout? = null

    // Video buffer load percentage
    private var bufferPercentTv: TextView? = null

    // Play speed value
    private var speedTv: TextView? = null

    // Full screen Button
    private var fullScreenBt: Button? = null

    // Video action button layout
    private var controlLayout: FrameLayout? = null

    // Video bottom layout
    private var contentLayout: LinearLayout? = null

    // Video name
    private var videoNameTv: TextView? = null

    // Video width and height
    private var videoSizeTv: TextView? = null

    // Video play total time
    private var videoTimeTv: TextView? = null

    // Buffer progress
    private var videoDownloadTv: TextView? = null

    // Video of the current rate
    private var videoBitrateTv: TextView? = null

    /**
     * Get parent view
     *
     * @return Parent view
     */
    val contentView: View
        get() {
            @SuppressLint("InflateParams") val view = LayoutInflater.from(context).inflate(R.layout.play_view, null)
            initView(view)
            showDefaultValueView()
            return view
        }

    /**
     * Init view
     *
     * @param view Parent view
     */
    private fun initView(view: View?) {
        if (view != null) {
            surfaceView = view.findViewById(R.id.surface_view)
            textureView = view.findViewById(R.id.texture_view)
            if (PlayControlUtil.isSurfaceView) {
                val surfaceHolder = surfaceView?.getHolder()
                surfaceHolder?.addCallback(onPlayWindowListener)
                surfaceHolder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
                textureView?.setVisibility(View.GONE)
                surfaceView?.setVisibility(View.VISIBLE)
            } else {
                textureView?.setSurfaceTextureListener(onPlayWindowListener)
                textureView?.setVisibility(View.VISIBLE)
                surfaceView?.setVisibility(View.GONE)
            }
            seekBar = view.findViewById(R.id.seek_bar)
            seekBar?.setOnSeekBarChangeListener(onWisePlayerListener)
            currentTimeTv = view.findViewById(R.id.current_time_tv)
            totalTimeTv = view.findViewById(R.id.total_time_tv)
            playImg = view.findViewById(R.id.play_btn)
            playImg?.setOnClickListener(onPlayWindowListener)
            fullScreenBt = view.findViewById(R.id.fullscreen_btn)
            fullScreenBt?.setOnClickListener(onPlayWindowListener)
            videoBufferLayout = view.findViewById(R.id.buffer_rl)
            videoBufferLayout?.setVisibility(View.GONE)
            controlLayout = view.findViewById(R.id.control_layout)
            bufferPercentTv = view.findViewById(R.id.play_process_buffer)
            contentLayout = view.findViewById(R.id.content_layout)
            speedTv = view.findViewById(R.id.play_speed_btn)
            speedTv?.setOnClickListener(onPlayWindowListener)
            videoNameTv = view.findViewById(R.id.tv_video_name)
            videoSizeTv = view.findViewById(R.id.video_width_and_height)
            videoTimeTv = view.findViewById(R.id.video_time)
            videoDownloadTv = view.findViewById(R.id.video_download_speed)
            videoBitrateTv = view.findViewById(R.id.video_bitrate)
        }
    }

    /**
     * Update play view
     *
     * @param wisePlayer WisePlayer
     */
    fun updatePlayView(wisePlayer: WisePlayer?) {
        if (wisePlayer != null) {
            val totalTime = wisePlayer.duration
            seekBar?.max = totalTime
            if (TimeUtil.formatLongToTimeStr(totalTime) != null) {
                totalTimeTv?.text = TimeUtil.formatLongToTimeStr(totalTime)
            }
            if (TimeUtil.formatLongToTimeStr(0) != null) {
                currentTimeTv?.text = TimeUtil.formatLongToTimeStr(0)
            }
            seekBar?.progress = 0
            contentLayout?.visibility = View.VISIBLE
        }
    }

    /**
     * Update buffer
     *
     * @param percent percent
     */
    @SuppressLint("SetTextI18n")
    fun updateBufferingView(percent: Int) {
        if (videoBufferLayout?.visibility == View.GONE) {
            videoBufferLayout?.visibility = View.VISIBLE
        }
        bufferPercentTv?.text = "$percent%"
    }

    /**
     * Show buffer view
     */
    fun showBufferingView() {
        videoBufferLayout?.visibility = View.VISIBLE
        bufferPercentTv?.text = "0%"
    }

    /**
     * Dismiss buffer view
     */
    fun dismissBufferingView() {
        videoBufferLayout?.visibility = View.GONE
    }

    /**
     * Set stop background
     */
    fun setPauseView() {
        playImg?.setImageResource(R.drawable.ic_full_screen_suspend_normal)
    }

    /**
     * Set start background
     */
    fun setPlayView() {
        playImg?.setImageResource(R.drawable.ic_play)
    }

    /**
     * Updating the progress view
     *
     * @param progress       the progress
     * @param bufferPosition the buffer position
     * @param bufferingSpeed the buffering speed
     * @param bitrate        the bitrate
     */
    fun updatePlayProgressView(progress: Int, bufferPosition: Int, bufferingSpeed: Long, bitrate: Int) {
        seekBar?.progress = progress
        seekBar?.secondaryProgress = bufferPosition
        currentTimeTv?.text = TimeUtil.formatLongToTimeStr(progress)
        videoDownloadTv?.text = context.resources.getString(R.string.video_download_speed, bufferingSpeed)
        if (bitrate == 0) {
            videoBitrateTv?.text = context.resources.getString(R.string.video_bitrate_empty)
        } else {
            videoBitrateTv?.text = context.resources.getString(R.string.video_bitrate, bitrate)
        }
    }

    /**
     * Set full screen layout
     *
     * @param name Video name
     */
    fun setFullScreenView(name: String?) {
        fullScreenBt?.visibility = View.GONE
        contentLayout?.visibility = View.GONE
        surfaceView?.layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    /**
     * Set portrait layout
     */
    fun setPortraitView() {
        surfaceView?.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtil.dp2px(context, Constants.HEIGHT_DP))
        fullScreenBt?.visibility = View.VISIBLE
        contentLayout?.visibility = View.VISIBLE
    }

    /**
     * Set play complete
     */
    fun updatePlayCompleteView() {
        playImg?.setImageResource(R.drawable.ic_play)
        controlLayout?.visibility = View.VISIBLE
    }

    /**
     * Player back to the background
     */
    fun onPause() {
        dismissBufferingView()
    }

    /**
     * Update Video view
     *
     * @param wisePlayer WisePlayer
     * @param name       Video name
     */
    fun setContentView(wisePlayer: WisePlayer?, name: String?) {
        if (wisePlayer != null) {
            videoNameTv?.text = context.resources.getString(R.string.video_name, name)
            videoSizeTv?.text = context.resources
                    .getString(
                            R.string.video_width_and_height,
                            wisePlayer.videoWidth,
                            wisePlayer.videoHeight)
            videoTimeTv?.text = context.resources
                    .getString(R.string.video_time, TimeUtil.formatLongToTimeStr(wisePlayer.duration))
        }
    }

    /**
     * Show setting dialog
     *
     * @param settingType  Setting type
     * @param showTextList Setting text list
     * @param selectIndex  Default select index
     */
    fun showSettingDialog(settingType: Int, showTextList: List<String?>?, selectIndex: Int) {
        DialogUtil.onSettingDialogSelectIndex(context, settingType, showTextList, selectIndex, onPlayWindowListener)
    }

    /**
     * Show setting dialog
     *
     * @param settingType  Setting type
     * @param showTextList Setting text list
     * @param selectValue  Default select value
     */
    fun showSettingDialogValue(settingType: Int, showTextList: List<String?>?, selectValue: String?) {
        DialogUtil.onSettingDialogSelectValue(context, settingType, showTextList, selectValue, onPlayWindowListener)
    }

    /**
     * Set speed button text
     *
     * @param speedText speed value
     */
    fun setSpeedButtonText(speedText: String?) {
        speedTv?.text = speedText
    }

    /**
     * Set default value
     */
    @SuppressLint("SetTextI18n")
    fun showDefaultValueView() {
        currentTimeTv?.text = TimeUtil.formatLongToTimeStr(0)
        totalTimeTv?.text = TimeUtil.formatLongToTimeStr(0)
        speedTv?.text = "1.0x"
        videoNameTv?.text = context.resources.getString(R.string.video_name, "")
        videoSizeTv?.text = context.resources.getString(R.string.video_width_and_height, 0, 0)
        videoTimeTv?.text = context.resources.getString(R.string.video_time, TimeUtil.formatLongToTimeStr(0))
        videoDownloadTv?.text = context.resources.getString(R.string.video_download_speed, 0)
        videoBitrateTv?.text = context.resources.getString(R.string.video_bitrate_empty)
    }

    /**
     * Reset video view
     */
    fun reset() {
        showBufferingView()
        playImg?.setImageResource(R.drawable.ic_play)
        showDefaultValueView()
    }

}