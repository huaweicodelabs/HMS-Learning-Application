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
package com.huawei.training.kotlin.utils.pushservice

import android.content.Context
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
import com.huawei.training.kotlin.utils.video.Constants

/**
 * @since 2020
 * @author Huawei DTSE India
 */
object PushUtil {
    /**
     * Gets token.
     *
     * @param context the context
     */
    fun getToken(context: Context?) {
        object : Thread() {
            override fun run() {
                val appId = AGConnectServicesConfig.fromContext(context).getString("client/app_id")
                try {
                    Constants.PUSH_TOKEN = HmsInstanceId.getInstance(context).getToken(appId, "HCM")
                } catch (exp: ApiException) {
                }
            }
        }.start()
    }
}