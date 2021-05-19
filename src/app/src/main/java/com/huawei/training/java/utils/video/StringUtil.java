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

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.text.TextUtils;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class StringUtil {
    /**
     * Get empty string
     *
     * @return Empty string
     */
    public static String emptyStringValue() {
        return "";
    }

    /**
     * whether a string is empty
     *
     * @param value String value
     * @return Whether a string is empty
     */
    public static boolean isEmpty(String value) {
        return TextUtils.isEmpty(value) || value.trim().length() == 0;
    }

    /**
     * Get not empty value
     *
     * @param value String value
     * @return Not empty value
     */
    public static String getNotEmptyString(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        } else {
            return value;
        }
    }

    /**
     * Get a string from the resources
     *
     * @param context Context
     * @param resId   Resource id
     * @return String value
     */
    public static String getStringFromResId(Context context, int resId) {
        String stringValue = "";
        try {
            stringValue = context.getResources().getString(resId);
        } catch (NotFoundException exp) {

        }
        return stringValue;
    }

    /**
     * String to float
     *
     * @param value String
     * @return float float
     */
    public static float valueOf(String value) {
        float intValue = 0f;
        if (TextUtils.isEmpty(value)) {
            return intValue;
        }
        intValue = Float.valueOf(value);

        return intValue;
    }

}
