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

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.text.TextUtils

/**
 * @since 2020
 * @author Huawei DTSE India
 */
object StringUtil {
    /**
     * Get empty string
     *
     * @return Empty string
     */
    fun emptyStringValue(): String {
        return ""
    }

    /**
     * whether a string is empty
     *
     * @param value String value
     * @return Whether a string is empty
     */
    @JvmStatic
    fun isEmpty(value: String): Boolean {
        return TextUtils.isEmpty(value) || value.trim { it <= ' ' }.length == 0
    }

    /**
     * Get not empty value
     *
     * @param value String value
     * @return Not empty value
     */
    fun getNotEmptyString(value: String?): String? {
        return if (TextUtils.isEmpty(value)) {
            ""
        } else {
            value
        }
    }

    /**
     * Get a string from the resources
     *
     * @param context Context
     * @param resId   Resource id
     * @return String value
     */
    @JvmStatic
    fun getStringFromResId(context: Context, resId: Int): String {
        var stringValue = ""
        try {
            stringValue = context.resources.getString(resId)
        } catch (exp: NotFoundException) {
        }
        return stringValue
    }

    /**
     * String to float
     *
     * @param value String
     * @return float float
     */
    fun valueOf(value: String?): Float {
        var intValue = 0f
        if (TextUtils.isEmpty(value)) {
            return intValue
        }
        intValue = java.lang.Float.valueOf(value)
        return intValue
    }
}