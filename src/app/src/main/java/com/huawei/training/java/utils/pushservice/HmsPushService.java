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

package com.huawei.training.java.utils.pushservice;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class HmsPushService extends HmsMessageService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onMessageSent(String msg) {
        super.onMessageSent(msg);
    }

    @Override
    public void onMessageDelivered(String msg, Exception exp) {
        super.onMessageDelivered(msg, exp);
    }

    @Override
    public void onSendError(String msg, Exception exp) {
        super.onSendError(msg, exp);
    }

    @Override
    public void onNewToken(String msg) {
        super.onNewToken(msg);
    }

    @Override
    public void onTokenError(Exception exp) {
        super.onTokenError(exp);
    }
}
