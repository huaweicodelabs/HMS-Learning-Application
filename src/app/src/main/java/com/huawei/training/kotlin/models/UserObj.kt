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
package com.huawei.training.kotlin.models

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class UserObj {
    /**
     * The U id.
     */
    private var uId: String? = null

    /**
     * Gets first name.
     *
     * @return the first name
     */
    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    /**
     * The First name.
     */
    var firstName: String? = null

    /**
     * Gets last name.
     *
     * @return the last name
     */
    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    /**
     * The Last name.
     */
    var lastName: String? = null

    /**
     * Gets email id.
     *
     * @return the email id
     */
    /**
     * Sets email id.
     *
     * @param emailId the email id
     */
    /**
     * The Email id.
     */
    var emailId: String? = null

    /**
     * Gets mobile.
     *
     * @return the mobile
     */
    /**
     * Sets mobile.
     *
     * @param mobile the mobile
     */
    /**
     * The Mobile.
     */
    var mobile: String? = null

    /**
     * Gets image url.
     *
     * @return the image url
     */
    /**
     * Sets image url.
     *
     * @param imageUrl the image url
     */
    /**
     * The Image url.
     */
    var imageUrl: String? = null

    /**
     * Gets logged in.
     *
     * @return the logged in
     */
    /**
     * Sets logged in.
     *
     * @param loggedIn the logged in
     */
    /**
     * The Is logged in.
     */
    var loggedIn: Boolean? = null

    /**
     * Gets id.
     *
     * @return the id
     */
    fun getuId(): String? {
        return uId
    }

    /**
     * Sets id.
     *
     * @param uId the u id
     */
    fun setuId(uId: String?) {
        this.uId = uId
    }

}