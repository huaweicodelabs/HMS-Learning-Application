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
package com.huawei.training.java.utils.video;

import com.huawei.hms.videokit.player.common.PlayerConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class PlayControlUtil {
    /**
     * Local loads of players if the View is SurfaceView
     */
    private static boolean isSurfaceView = true;

    /**
     * Set play type 0: on demand (the default) 1: live
     */
    private static int videoType = 0;

    /**
     * Set the mute
     * Default is mute
     */
    private static boolean isMute = false;

    /**
     * Set the play mode
     * Default is video play
     */
    private static int playMode = PlayerConstants.PlayMode.PLAY_MODE_NORMAL;

    /**
     * Set the bandwidth switching mode
     * The default is adaptive
     */
    private static int bandwidthSwitchMode = 0;

    /**
     * Whether to set up the bitrate
     */
    private static boolean initBitrateEnable = false;

    /**
     * The Bitrate type
     * 0：The default priority search upwards
     * 1：The priority search down
     */
    private static int initType = 0;

    /**
     * Bitrate (if set up by resolution rate setting is effective)
     */
    private static int initBitrate = 0;

    /**
     * Resolution width (width height must be set up in pairs)
     */
    private static int initWidth = 0;

    /**
     * Resolution height (width height must be set up in pairs)
     */
    private static int initHeight = 0;

    /**
     * Whether close the logo
     */
    private static boolean closeLogo = false;

    /**
     * Close the logo, whether to affect all sources
     * True: affects the whole play false: only under the influence of the sources of logo
     */
    private static boolean takeEffectOfAll = false;

    /**
     * Save the data
     */
    private static Map<String, Integer> savePlayDataMap = new HashMap<>();

    /**
     * The minimum bitrate
     */
    private static int minBitrate;

    /**
     * The maximum rate
     */
    private static int maxBitrate;

    private static boolean isLoadBuff = true;

    /**
     * Is surface view boolean.
     *
     * @return the boolean
     */
    public static boolean isSurfaceView() {
        return isSurfaceView;
    }

    /**
     * Gets video type.
     *
     * @return the video type
     */
    public static int getVideoType() {
        return videoType;
    }

    /**
     * Sets video type.
     *
     * @param videoType the video type
     */
    public static void setVideoType(int videoType) {
        PlayControlUtil.videoType = videoType;
    }

    /**
     * Is mute boolean.
     *
     * @return the boolean
     */
    public static boolean isMute() {
        return isMute;
    }

    /**
     * Sets is mute.
     *
     * @param isMute the is mute
     */
    public static void setIsMute(boolean isMute) {
        PlayControlUtil.isMute = isMute;
    }

    /**
     * Gets play mode.
     *
     * @return the play mode
     */
    public static int getPlayMode() {
        return playMode;
    }

    /**
     * Sets play mode.
     *
     * @param playMode the play mode
     */
    public static void setPlayMode(int playMode) {
        PlayControlUtil.playMode = playMode;
    }

    /**
     * Gets bandwidth switch mode.
     *
     * @return the bandwidth switch mode
     */
    public static int getBandwidthSwitchMode() {
        return bandwidthSwitchMode;
    }

    /**
     * Sets bandwidth switch mode.
     *
     * @param bandwidthSwitchMode the bandwidth switch mode
     */
    public static void setBandwidthSwitchMode(int bandwidthSwitchMode) {
        PlayControlUtil.bandwidthSwitchMode = bandwidthSwitchMode;
    }

    /**
     * Is init bitrate enable boolean.
     *
     * @return the boolean
     */
    public static boolean isInitBitrateEnable() {
        return initBitrateEnable;
    }


    /**
     * Gets init type.
     *
     * @return the init type
     */
    public static int getInitType() {
        return initType;
    }

    /**
     * Sets init type.
     *
     * @param initType the init type
     */
    public static void setInitType(int initType) {
        PlayControlUtil.initType = initType;
    }

    /**
     * Gets init bitrate.
     *
     * @return the init bitrate
     */
    public static int getInitBitrate() {
        return initBitrate;
    }

    /**
     * Sets init bitrate.
     *
     * @param initBitrate the init bitrate
     */
    public static void setInitBitrate(int initBitrate) {
        PlayControlUtil.initBitrate = initBitrate;
    }

    /**
     * Gets init width.
     *
     * @return the init width
     */
    public static int getInitWidth() {
        return initWidth;
    }

    /**
     * Sets init width.
     *
     * @param initWidth the init width
     */
    public static void setInitWidth(int initWidth) {
        PlayControlUtil.initWidth = initWidth;
    }

    /**
     * Gets init height.
     *
     * @return the init height
     */
    public static int getInitHeight() {
        return initHeight;
    }

    /**
     * Sets init height.
     *
     * @param initHeight the init height
     */
    public static void setInitHeight(int initHeight) {
        PlayControlUtil.initHeight = initHeight;
    }

    /**
     * Is take effect of all boolean.
     *
     * @return the boolean
     */
    public static boolean isTakeEffectOfAll() {
        return takeEffectOfAll;
    }

    /**
     * Save play data.
     *
     * @param url      the url
     * @param progress the progress
     */
    public static void savePlayData(String url, int progress) {
        if (!StringUtil.isEmpty(url)) {
            savePlayDataMap.put(url, progress);
        }
    }

    /**
     * Gets play data.
     *
     * @param url the url
     * @return the play data
     */
    public static int getPlayData(String url) {
        if (savePlayDataMap.get(url) == null) return 0;
        return savePlayDataMap.get(url);
    }

    /**
     * Clear play data.
     *
     * @param url the url
     */
    public static void clearPlayData(String url) {
        if (StringUtil.isEmpty(url)) {
            savePlayDataMap.remove(url);
        }
    }

    /**
     * Is close logo boolean.
     *
     * @return the boolean
     */
    public static boolean isCloseLogo() {
        return closeLogo;
    }

    /**
     * Sets close logo.
     *
     * @param closeLogo the close logo
     */
    public static void setCloseLogo(boolean closeLogo) {
        PlayControlUtil.closeLogo = closeLogo;
    }

    /**
     * Gets min bitrate.
     *
     * @return the min bitrate
     */
    public static int getMinBitrate() {
        return minBitrate;
    }

    /**
     * Gets max bitrate.
     *
     * @return the max bitrate
     */
    public static int getMaxBitrate() {
        return maxBitrate;
    }

    /**
     * Whether need to modify the code bitrate range
     *
     * @return the boolean
     */
    public static boolean isSetBitrateRangeEnable() {
        return maxBitrate != 0 || minBitrate != 0;
    }

    /**
     * Remove bitrate range data
     */
    public static void clearBitrateRange() {
        maxBitrate = 0;
        minBitrate = 0;
    }

    /**
     * Is load buff boolean.
     *
     * @return the boolean
     */
    public static boolean isLoadBuff() {
        return isLoadBuff;
    }

    /**
     * Sets load buff.
     *
     * @param isLoadBuff the is load buff
     */
    public static void setLoadBuff(boolean isLoadBuff) {
        PlayControlUtil.isLoadBuff = isLoadBuff;
    }
}
