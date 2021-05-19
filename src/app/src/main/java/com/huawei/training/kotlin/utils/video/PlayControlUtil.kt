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
package com.huawei.training.kotlin.utils.video

import com.huawei.hms.videokit.player.common.PlayerConstants
import com.huawei.training.kotlin.utils.video.StringUtil.isEmpty
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
object PlayControlUtil {
    /**
     * Is surface view boolean.
     *
     * @return the boolean
     */
    /**
     * Local loads of players if the View is SurfaceView
     */
    const val isSurfaceView = true

    /**
     * Gets video type.
     *
     * @return the video type
     */
    /**
     * Sets video type.
     *
     * @param videoType the video type
     */
    /**
     * Set play type 0: on demand (the default) 1: live
     */
    var videoType = 0

    /**
     * Is mute boolean.
     *
     * @return the boolean
     */
    /**
     * Sets is mute.
     *
     * @param isMute the is mute
     */
    /**
     * Set the mute
     * Default is mute
     */
    var isMute = false

    /**
     * Gets play mode.
     *
     * @return the play mode
     */
    /**
     * Sets play mode.
     *
     * @param playMode the play mode
     */
    /**
     * Set the play mode
     * Default is video play
     */
    var playMode = PlayerConstants.PlayMode.PLAY_MODE_NORMAL

    /**
     * Gets bandwidth switch mode.
     *
     * @return the bandwidth switch mode
     */
    /**
     * Sets bandwidth switch mode.
     *
     * @param bandwidthSwitchMode the bandwidth switch mode
     */
    /**
     * Set the bandwidth switching mode
     * The default is adaptive
     */
    var bandwidthSwitchMode = 0

    /**
     * Is init bitrate enable boolean.
     *
     * @return the boolean
     */
    /**
     * Whether to set up the bitrate
     */
    const val isInitBitrateEnable = false

    /**
     * Gets init type.
     *
     * @return the init type
     */
    /**
     * Sets init type.
     *
     * @param initType the init type
     */
    /**
     * The Bitrate type
     * 0：The default priority search upwards
     * 1：The priority search down
     */
    var initType = 0

    /**
     * Gets init bitrate.
     *
     * @return the init bitrate
     */
    /**
     * Sets init bitrate.
     *
     * @param initBitrate the init bitrate
     */
    /**
     * Bitrate (if set up by resolution rate setting is effective)
     */
    var initBitrate = 0

    /**
     * Gets init width.
     *
     * @return the init width
     */
    /**
     * Sets init width.
     *
     * @param initWidth the init width
     */
    /**
     * Resolution width (width height must be set up in pairs)
     */
    var initWidth = 0

    /**
     * Gets init height.
     *
     * @return the init height
     */
    /**
     * Sets init height.
     *
     * @param initHeight the init height
     */
    /**
     * Resolution height (width height must be set up in pairs)
     */
    var initHeight = 0

    /**
     * Is close logo boolean.
     *
     * @return the boolean
     */
    /**
     * Sets close logo.
     *
     * @param closeLogo the close logo
     */
    /**
     * Whether close the logo
     */
    var isCloseLogo = false

    /**
     * Is take effect of all boolean.
     *
     * @return the boolean
     */
    /**
     * Close the logo, whether to affect all sources
     * True: affects the whole play false: only under the influence of the sources of logo
     */
    const val isTakeEffectOfAll = false

    /**
     * Save the data
     */
    private val savePlayDataMap: MutableMap<String, Int> = HashMap()

    /**
     * Gets min bitrate.
     *
     * @return the min bitrate
     */
    /**
     * The minimum bitrate
     */
    var minBitrate = 0
        private set

    /**
     * Gets max bitrate.
     *
     * @return the max bitrate
     */
    /**
     * The maximum rate
     */
    var maxBitrate = 0
        private set
    /**
     * Is load buff boolean.
     *
     * @return the boolean
     */
    /**
     * Sets load buff.
     *
     * @param isLoadBuff the is load buff
     */
    var isLoadBuff = true

    /**
     * Save play data.
     *
     * @param url      the url
     * @param progress the progress
     */
    fun savePlayData(url: String, progress: Int) {
        if (!isEmpty(url)) {
            savePlayDataMap[url] = progress
        }
    }

    /**
     * Gets play data.
     *
     * @param url the url
     * @return the play data
     */
    fun getPlayData(url: String?): Int {
        return if (savePlayDataMap[url] == null) 0 else savePlayDataMap.get(url)!!
    }

    /**
     * Clear play data.
     *
     * @param url the url
     */
    fun clearPlayData(url: String?) {
        if (isEmpty(url!!)) {
            savePlayDataMap.remove(url)
        }
    }

    /**
     * Whether need to modify the code bitrate range
     *
     * @return the boolean
     */
    val isSetBitrateRangeEnable: Boolean
        get() = maxBitrate != 0 || minBitrate != 0

    /**
     * Remove bitrate range data
     */
    fun clearBitrateRange() {
        maxBitrate = 0
        minBitrate = 0
    }
    fun setIsMute(isMute: Boolean) {
        PlayControlUtil.isMute = isMute
    }

    /**
     * Sets load buff.
     *
     * @param isLoadBuff the is load buff
     */
    @JvmName("functionOfKotlin")
    fun setLoadBuff(isLoadBuff: Boolean) {
        PlayControlUtil.isLoadBuff = isLoadBuff
    }
}