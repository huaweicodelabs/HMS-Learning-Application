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
package com.huawei.training.kotlin.utils.video.entity

import java.io.Serializable

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class PlayEntity : Serializable {
    /**
     * Gets name.
     *
     * @return the name
     */
    /**
     * Sets name.
     *
     * @param name the name
     */
    /**
     * The video name
     */
    var name: String? = null

    /**
     * The url type:
     * 0: a single broadcast address
     * 1: multiple broadcast address
     */
    private var urlType = 0

    /**
     * Gets url.
     *
     * @return the url
     */
    /**
     * Sets url.
     *
     * @param url the url
     */
    /**
     * The video url
     */
    var url: String? = null

    /**
     * Sets url type.
     *
     * @param urlType the url type
     */
    fun setUrlType(urlType: Int) {
        this.urlType = urlType
    }
}