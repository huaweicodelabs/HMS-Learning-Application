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
package com.huawei.training.kotlin.utils.account

import android.app.Activity
import android.content.Context
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.result.AuthHuaweiId
import com.huawei.training.database.CloudDbHelper
import com.huawei.training.kotlin.database.CloudDbAction
import com.huawei.training.kotlin.database.tables.UsersInfoTable
import com.huawei.training.kotlin.models.UserObj
import com.huawei.training.kotlin.utils.AppUtils
import com.huawei.training.kotlin.utils.LearningApplication
import com.huawei.training.kotlin.utils.video.Constants

/**
 * @since 2020
 * @author Huawei DTSE India
 */
object AccountUtils {
    /**
     * Sign in.
     *
     * @param activity the activity
     */
    @JvmStatic
    fun signIn(activity: Activity) {
        val mAuthParam = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setProfile()
                .setEmail()
                .setAuthorizationCode()
                .createParams()
        val mAuthManager = HuaweiIdAuthManager.getService(activity, mAuthParam)
        activity.startActivityForResult(mAuthManager.signInIntent, Constants.REQUEST_SIGN_IN_LOGIN_CODE)
    }

    /**
     * Sets user info.
     *
     * @param huaweiAccount the huawei account
     * @param cloudDbHelper the cloud db helper
     * @param context       the context
     */
    // set user info
    fun setUserInfo(huaweiAccount: AuthHuaweiId, cloudDbHelper: CloudDbHelper, context: Context) {
        var utils = AppUtils(context)
        utils.setPref(Constants.UID, huaweiAccount.getUnionId())
        var userObj = UserObj()
        userObj.setuId(huaweiAccount.getUnionId())
        userObj.firstName = huaweiAccount.getDisplayName()
        userObj.emailId = huaweiAccount.getEmail()
        var userInfo = UsersInfoTable()
        userInfo.userId = huaweiAccount.getUnionId()
        userInfo.displayName = huaweiAccount.getDisplayName()
        userInfo.displayName = huaweiAccount.getGivenName()
        userInfo.email = huaweiAccount.getEmail()
        userInfo.userImageUrl = huaweiAccount.avatarUri.toString()
        userInfo.mobile = null
        cloudDbHelper.cloudDbQueyCalls?.upsertUserInfo(userInfo, CloudDbAction.INSERT_USER_INFO)
        (context.applicationContext as LearningApplication).userObj = userObj
        (context.applicationContext as LearningApplication).setUserStatus()
    }
}