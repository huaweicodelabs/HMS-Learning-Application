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
package com.huawei.training.kotlin.utils.video.control

import android.content.Context
import android.view.SurfaceView
import android.view.TextureView
import com.huawei.hms.videokit.player.InitBitrateParam
import com.huawei.hms.videokit.player.WisePlayer
import com.huawei.hms.videokit.player.common.PlayerConstants
import com.huawei.training.kotlin.listeners.OnWisePlayerListener
import com.huawei.training.kotlin.utils.LearningApplication.Companion.getWisePlayerFactory
import com.huawei.training.kotlin.utils.video.PlayControlUtil
import com.huawei.training.kotlin.utils.video.StringUtil
import com.huawei.training.kotlin.utils.video.entity.PlayEntity
import java.io.Serializable
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class PlayControl(// Context
        private val context: Context, // Player listener
        private val onWisePlayerListener: OnWisePlayerListener) {

    // Player entity
    private var wisePlayer: WisePlayer? = null

    // The current play data
    private var currentPlayData: PlayEntity? = null

    // Play list data
    private val playEntityList: List<PlayEntity>? = null

    /**
     * Is http video boolean.
     *
     * @return the boolean
     */
    // Video play url start with Http/Https
    var isHttpVideo = true
        private set

    /**
     * The Video url.
     */
    var videoUrl: String? = null

    /**
     * Init
     */
    fun init() {
        initPlayer()
        setPlayListener()
    }

    /**
     * Set the play listener
     */
    private fun setPlayListener() {
        if (wisePlayer != null) {
            wisePlayer?.setErrorListener(onWisePlayerListener)
            wisePlayer?.setEventListener(onWisePlayerListener)
            wisePlayer?.setResolutionUpdatedListener(onWisePlayerListener)
            wisePlayer?.setReadyListener(onWisePlayerListener)
            wisePlayer?.setLoadingListener(onWisePlayerListener)
            wisePlayer?.setPlayEndListener(onWisePlayerListener)
            wisePlayer?.setSeekEndListener(onWisePlayerListener)
        }
    }

    /**
     * Init play fail
     *
     * @return Whether the failure
     */
    fun initPlayFail(): Boolean {
        return wisePlayer == null
    }

    /**
     * Init the player
     */
    private fun initPlayer() {
        if (getWisePlayerFactory() == null) {
            return
        }
        wisePlayer = getWisePlayerFactory()?.createWisePlayer()
    }

    /**
     * Get the current play data
     *
     * @param serializable the serializable
     */
    fun setCurrentPlayData(serializable: Serializable?) {
        if (serializable != null && serializable is PlayEntity) {
            currentPlayData = serializable
        }
    }

    /**
     * Start the player, the state of ready to start
     */
    fun ready() {
        if (wisePlayer != null) {
            isHttpVideo = true
            if (videoUrl != null) wisePlayer?.setPlayUrl(arrayOf(videoUrl!!))
            setBookmark()
            setPlayMode(PlayControlUtil.playMode, false)
            setMute(PlayControlUtil.isMute)
            setVideoType(PlayControlUtil.videoType, false)
            setBandwidthSwitchMode(PlayControlUtil.bandwidthSwitchMode, false)
            setInitBitrateEnable()
            setBitrateRange()
            setCloseLogo()
            wisePlayer?.ready()
        }
    }

    /**
     * Start playing
     */
    fun start() {
        wisePlayer?.start()
    }

    /**
     * Get the current play time
     *
     * @return The current play time
     */
    val currentTime: Int
        get() = if (wisePlayer != null) {
            wisePlayer!!.currentTime
        } else {
            0
        }

    /**
     * Get total time
     *
     * @return Total time
     */
    val duration: Int
        get() = if (wisePlayer != null) {
            wisePlayer!!.duration
        } else {
            0
        }

    /**
     * Drag the play
     *
     * @param progress Drag the time of position, the unit is milliseconds
     */
    fun updateCurProgress(progress: Int) {
        if (wisePlayer != null) {
            wisePlayer?.seek(progress)
        }
    }

    /**
     * Binding player SurfaceView
     *
     * @param surfaceView The player SurfaceView
     */
    fun setSurfaceView(surfaceView: SurfaceView?) {
        if (wisePlayer != null) {
            wisePlayer?.setView(surfaceView)
        }
    }

    /**
     *
     * Binding player TextureView
     *
     * @param textureView The player TextureView
     */
    fun setTextureView(textureView: TextureView?) {
        if (wisePlayer != null) {
            wisePlayer?.setView(textureView)
        }
    }

    /**
     * The player suspend
     */
    fun suspend() {
        if (wisePlayer != null) {
            setBufferingStatus(false, false)
            wisePlayer?.suspend()
        }
    }

    /**
     * Release player
     */
    fun release() {
        if (wisePlayer != null) {
            wisePlayer?.release()
            wisePlayer = null
        }
    }

    /**
     * stop play
     */
    fun stop() {
        if (wisePlayer != null) {
            wisePlayer?.stop()
        }
    }

    /**
     * Set the play/pause state
     *
     * @param isPlaying The player status
     */
    fun setPlayData(isPlaying: Boolean) {
        if (isPlaying) {
            wisePlayer?.pause()
            setBufferingStatus(false, false)
        } else {
            wisePlayer?.start()
            setBufferingStatus(true, false)
        }
    }

    /**
     * Gets the current play name
     *
     * @return The current play name
     */
    val currentPlayName: String
        get() = if (currentPlayData != null) {
            StringUtil.getNotEmptyString(currentPlayData?.name).toString()
        } else {
            ""
        }

    /**
     * Players resume play
     *
     * @param play Resume after the player is in a state of play or pause state 0:1: pause play - 1: keep
     */
    fun playResume(play: Int) {
        if (wisePlayer != null) {
            setBufferingStatus(true, false)
            wisePlayer?.resume(play)
        }
    }

    /**
     * Set the player whether to allow for the buffer.Usage scenarios such as suspended under 4g networks, not for the
     * buffer
     *
     * @param status        False: stop the background buffer load.True: allows the background buffer load (the default)
     * @param isUpdateLocal Whether to update the local configuration
     */
    fun setBufferingStatus(status: Boolean, isUpdateLocal: Boolean) {
        if (wisePlayer != null && (isUpdateLocal || PlayControlUtil.isLoadBuff)) {
            wisePlayer?.setBufferingStatus(status)
            if (isUpdateLocal) {
                PlayControlUtil.setLoadBuff(status)
            }
        }
    }

    /**
     * Set the speed
     *
     * @param speedValue The speed of the string
     */
    fun setPlaySpeed(speedValue: String) {
        if (speedValue == "1.25x") {
            wisePlayer?.playSpeed = 1.25f
        } else if (speedValue == "1.5x") {
            wisePlayer?.playSpeed = 1.5f
        } else if (speedValue == "1.75x") {
            wisePlayer?.playSpeed = 1.75f
        } else if (speedValue == "2.0x") {
            wisePlayer?.playSpeed = 2.0f
        } else if (speedValue == "0.5x") {
            wisePlayer?.playSpeed = 0.5f
        } else if (speedValue == "0.75x") {
            wisePlayer?.playSpeed = 0.75f
        } else {
            wisePlayer?.playSpeed = 1.0f
        }
    }

    /**
     * Get the current cache progress
     *
     * @return Cache progress unit of milliseconds
     */
    val bufferTime: Int
        get() = if (wisePlayer != null) {
            wisePlayer!!.bufferTime
        } else {
            0
        }

    /**
     * Get to download speed
     *
     * @return Download speed unit b/s
     */
    val bufferingSpeed: Long
        get() = if (wisePlayer != null) {
            wisePlayer!!.bufferingSpeed
        } else {
            0
        }

    /**
     * Get bitrate list data (String)
     *
     * @return Bitrate list data
     */
    val bitrateList: MutableList<String>
        get() {
            val bitrateList: MutableList<String> = ArrayList()
            val bitrateIntList = bitrateIntegerList
            for (i in bitrateIntList.indices) {
                bitrateList.add(bitrateIntList[i].toString())
            }
            return bitrateList
        }

    /**
     * Get bitrate list data (Integer)
     *
     * @return Bitrate list data
     */
    private val bitrateIntegerList: List<Int>
        private get() {
            val bitrateIntList: MutableList<Int> = ArrayList()
            if (wisePlayer != null) {
                getBitrates(bitrateIntList)
            }
            return bitrateIntList
        }

    private fun getBitrates(bitrateIntList: MutableList<Int>) {
        val videoInfo = wisePlayer?.videoInfo
        if (videoInfo != null && videoInfo.streamInfos != null) {
            for (streamInfo in videoInfo.streamInfos) {
                if (streamInfo != null) {
                    bitrateIntList.add(streamInfo.bitrate)
                }
            }
        }
    }

    /**
     * Gets the current rate in Dialog rate index in the list
     *
     * @return The location of the current rate, the default back to the first
     */
    val currentBitrateIndex: Int
        get() {
            val bitrateIntList = bitrateIntegerList
            return if (bitrateIntList.size > 0) {
                bitrateIntList.indexOf(currentBitrate)
            } else {
                0
            }
        }

    /**
     * Smooth transition rate
     *
     * @param currentBitrate The current need to set the bitrate
     */
    fun switchBitrateSmooth(currentBitrate: Int) {
        if (wisePlayer != null) {
            wisePlayer?.switchBitrateSmooth(currentBitrate)
        }
    }

    /**
     * Designated transition rate
     *
     * @param currentBitrate The current need to set the bitrate
     */
    fun switchBitrateDesignated(currentBitrate: Int) {
        if (wisePlayer != null) {
            wisePlayer?.switchBitrateDesignated(currentBitrate)
        }
    }

    /**
     * Get the play stream bitrate
     *
     * @return The play stream bitrate
     */
    val currentBitrate: Int
        get() {
            if (wisePlayer != null) {
                val streamInfo = wisePlayer?.currentStreamInfo
                if (streamInfo != null) {
                    return streamInfo.bitrate
                }
            }
            return 0
        }

    /**
     * Set the bandwidth switching mode
     *
     * @param mod          The bandwidth switching mode
     * @param updateLocate Whether to update the local configuration
     */
    fun setBandwidthSwitchMode(mod: Int, updateLocate: Boolean) {
        if (wisePlayer != null) {
            wisePlayer?.setBandwidthSwitchMode(mod)
        }
        if (updateLocate) {
            PlayControlUtil.bandwidthSwitchMode = mod
        }
    }

    /**
     * Get play speed
     *
     * @return Play speed
     */
    val playSpeed: Float
        get() = if (wisePlayer != null) {
            wisePlayer!!.playSpeed
        } else 1f

    /**
     * Close logo
     */
    fun closeLogo() {
        if (wisePlayer != null) {
            wisePlayer?.closeLogo()
        }
    }

    /**
     * Set play mode
     *
     * @param playMode     Play mode
     * @param updateLocate Whether to update the local configuration
     */
    fun setPlayMode(playMode: Int, updateLocate: Boolean) {
        if (wisePlayer != null) {
            wisePlayer?.playMode = playMode
        }
        if (updateLocate) {
            PlayControlUtil.playMode = playMode
        }
    }

    /**
     * Get play mode
     *
     * @return Play mode
     */
    val playMode: Int
        get() = if (wisePlayer != null) {
            wisePlayer!!.playMode
        } else {
            1
        }

    /**
     * Whether cycle mode
     *
     * @return Is cycle mode
     */
    /**
     * Set cycle mode
     *
     * @param isCycleMode Whether open loop
     */
    var isCycleMode: Boolean
        get() = if (wisePlayer != null) {
            wisePlayer?.cycleMode == PlayerConstants.CycleMode.MODE_CYCLE
        } else {
            false
        }
        set(isCycleMode) {
            if (wisePlayer != null) {
                wisePlayer?.cycleMode = if (isCycleMode) PlayerConstants.CycleMode.MODE_CYCLE else PlayerConstants.CycleMode.MODE_NORMAL
            }
        }

    /**
     * Set the mute
     *
     * @param status Whether quiet
     */
    fun setMute(status: Boolean) {
        if (wisePlayer != null) {
            wisePlayer?.setMute(status)
        }
        PlayControlUtil.setIsMute(status)
    }

    /**
     * Set the volume, the current player is interval [0, 1]
     *
     * @param volume The volume interval [0, 1]
     */
    fun setVolume(volume: Float) {
        if (wisePlayer != null) {
            wisePlayer?.setVolume(volume)
        }
    }

    /**
     * Set play type 0: on demand (the default) 1: live
     *
     * @param videoType    Play types
     * @param updateLocate Whether to update the local configuration
     */
    fun setVideoType(videoType: Int, updateLocate: Boolean) {
        if (wisePlayer != null) {
            wisePlayer?.setVideoType(videoType)
        }
        if (updateLocate) {
            PlayControlUtil.videoType = videoType
        }
    }

    /**
     * Set change notification
     */
    fun setSurfaceChange() {
        if (wisePlayer != null) {
            wisePlayer?.setSurfaceChange()
        }
    }

    /**
     * Set up the bitrate
     */
    fun setInitBitrateEnable() {
        if (PlayControlUtil.isInitBitrateEnable && wisePlayer != null) {
            val initBitrateParam = InitBitrateParam()
            initBitrateParam.bitrate = PlayControlUtil.initBitrate
            initBitrateParam.height = PlayControlUtil.initHeight
            initBitrateParam.width = PlayControlUtil.initWidth
            initBitrateParam.type = PlayControlUtil.initType
            wisePlayer?.setInitBitrate(initBitrateParam)
        }
    }

    /**
     * Set the bitrate range
     */
    fun setBitrateRange() {
        if (PlayControlUtil.isSetBitrateRangeEnable && wisePlayer != null) {
            wisePlayer?.setBitrateRange(PlayControlUtil.minBitrate, PlayControlUtil.maxBitrate)
            PlayControlUtil.clearBitrateRange()
        }
    }

    /**
     * Remove the current play bookmark
     */
    fun clearPlayProgress() {
        if (currentPlayData != null) {
            PlayControlUtil.clearPlayData(currentPlayData!!.url)
        }
    }

    /**
     * Save the current sources of progress
     */
    fun savePlayProgress() {
        if (currentPlayData != null && wisePlayer != null) {
            PlayControlUtil.savePlayData(currentPlayData?.url.toString(), wisePlayer!!.currentTime)
        }
    }

    /**
     * Bookmark play position
     */
    fun setBookmark() {
        if (currentPlayData != null) {
            val bookmark = PlayControlUtil.getPlayData(currentPlayData!!.url)
            if (wisePlayer != null && bookmark != 0) {
                wisePlayer?.setBookmark(bookmark)
            }
        }
    }

    /**
     * Get the selected data
     *
     * @param position The selected position
     * @return The play data
     */
    fun getPlayFromPosition(position: Int): PlayEntity? {
        return if (playEntityList != null && playEntityList.size > position) {
            playEntityList[position]
        } else null
    }

    /**
     * Player reset
     */
    fun reset() {
        if (wisePlayer != null) {
            wisePlayer?.reset()
        }
    }

    /**
     * Close logo
     */
    fun setCloseLogo() {
        if (wisePlayer != null) {
            if (PlayControlUtil.isCloseLogo) {
                if (!PlayControlUtil.isTakeEffectOfAll) {
                    PlayControlUtil.isCloseLogo = false
                }
                wisePlayer?.closeLogo()
            }
        }
    }

    /**
     * Video into the background
     */
    fun onPause() {
        if (currentPlayData != null && wisePlayer != null) {
            PlayControlUtil.savePlayData(currentPlayData?.url.toString(), wisePlayer!!.currentTime)
            suspend()
        }
    }

    companion object {
        private const val TAG = "PlayControl"
    }

    /**
     * Constructor
     *
     * @param context              Context
     * @param onWisePlayerListener Player listener
     */
    init {
        init()
    }
}