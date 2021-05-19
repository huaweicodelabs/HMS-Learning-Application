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

/**
 * @since 2020
 * @author Huawei DTSE India
 */
object DataFormatUtil {
    /**
     * Get play speed string value
     *
     * @param playSpeed Play speed
     * @return play speed string value
     */
    fun getPlaySpeedString(playSpeed: Float): String {
        return if (isFloatEqual(playSpeed, 0.5f)) {
            "0.5x"
        } else if (isFloatEqual(playSpeed, 0.75f)) {
            "0.75x"
        } else if (isFloatEqual(playSpeed, 1.0f)) {
            "1.0x"
        } else if (isFloatEqual(playSpeed, 1.25f)) {
            "1.25x"
        } else if (isFloatEqual(playSpeed, 1.5f)) {
            "1.5x"
        } else if (isFloatEqual(playSpeed, 1.75f)) {
            "1.75x"
        } else if (isFloatEqual(playSpeed, 2.0f)) {
            "2.0x"
        } else {
            "1.0x"
        }
    }

    /**
     * Compare the two float values are equal
     *
     * @param originalValue Float value
     * @param targetValue   Float value
     * @return Is equal
     */
    fun isFloatEqual(originalValue: Float, targetValue: Float): Boolean {
        return Math.abs(originalValue - targetValue) < 0.05
    }
}