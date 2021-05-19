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

import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class HmsPushService : HmsMessageService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
    }

    override fun onMessageSent(msg: String) {
        super.onMessageSent(msg)
    }

    override fun onMessageDelivered(msg: String, exp: Exception) {
        super.onMessageDelivered(msg, exp)
    }

    override fun onSendError(msg: String, exp: Exception) {
        super.onSendError(msg, exp)
    }

    override fun onNewToken(msg: String) {
        super.onNewToken(msg)
    }

    override fun onTokenError(exp: Exception) {
        super.onTokenError(exp)
    }
}