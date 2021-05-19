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

package com.huawei.training.java.utils.account;

import android.app.Activity;
import android.content.Context;

import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;
import com.huawei.training.java.database.CloudDbAction;
import com.huawei.training.java.database.CloudDbHelper;
import com.huawei.training.java.database.tables.UsersInfoTable;
import com.huawei.training.java.models.UserObj;
import com.huawei.training.java.utils.AppUtils;
import com.huawei.training.java.utils.LearningApplication;
import com.huawei.training.java.utils.video.Constants;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class AccountUtils {
    /**
     * Sign in.
     *
     * @param activity the activity
     */
    public static void signIn(Activity activity) {
        HuaweiIdAuthParams mAuthParam =
                new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                        .setProfile()
                        .setEmail()
                        .setAuthorizationCode()
                        .createParams();
        HuaweiIdAuthService mAuthManager = HuaweiIdAuthManager.getService(activity, mAuthParam);
        activity.startActivityForResult(mAuthManager.getSignInIntent(), Constants.REQUEST_SIGN_IN_LOGIN_CODE);
    }

    /**
     * Sets user info.
     *
     * @param huaweiAccount the huawei account
     * @param cloudDbHelper the cloud db helper
     * @param context       the context
     */
// set user info
    public static void setUserInfo(AuthHuaweiId huaweiAccount, CloudDbHelper cloudDbHelper, Context context) {
        AppUtils utils = new AppUtils(context);
        utils.setPref(Constants.UID, huaweiAccount.getUnionId());
        UserObj userObj = new UserObj();
        userObj.setuId(huaweiAccount.getUnionId());
        userObj.setFirstName(huaweiAccount.getDisplayName());
        userObj.setEmailId(huaweiAccount.getEmail());

        UsersInfoTable userInfo = new UsersInfoTable();
        userInfo.setUserId(huaweiAccount.getUnionId());
        userInfo.setDisplayName(huaweiAccount.getDisplayName());
        userInfo.setDisplayName(huaweiAccount.getGivenName());
        userInfo.setEmail(huaweiAccount.getEmail());
        userInfo.setUserImageUrl(huaweiAccount.getAvatarUri().toString());
        userInfo.setMobile(null);

        cloudDbHelper.getCloudDbQueyCalls().upsertUserInfo(userInfo, CloudDbAction.INSERT_USER_INFO);

        ((LearningApplication) context.getApplicationContext()).setUserObj(userObj);
        ((LearningApplication) context.getApplicationContext()).setUserStatus();
    }
}
