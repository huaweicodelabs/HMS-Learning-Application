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
package com.huawei.training.kotlin.database

import com.huawei.agconnect.cloud.database.CloudDBZoneObject

/**
 * @since 2020
 * @author Huawei DTSE India
 */
interface CloudDbUiCallbackListener {
    /**
     * On success db data.
     *
     * @param cloudDbAction the cloud db action
     * @param dataList      the data list
     */
    fun onSuccessDbData(cloudDbAction: CloudDbAction?, dataList: List<CloudDBZoneObject?>?)

    /**
     * On success db query message.
     *
     * @param cloudDbAction the cloud db action
     * @param message       the message
     */
    fun onSuccessDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?)

    /**
     * On failure db query message.
     *
     * @param cloudDbAction the cloud db action
     * @param message       the message
     */
    fun onFailureDbQueryMessage(cloudDbAction: CloudDbAction?, message: String?)
}