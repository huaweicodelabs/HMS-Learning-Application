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

package com.huawei.training.java.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.training.R;
import com.huawei.training.java.database.CloudDbHelper;
import com.huawei.training.java.dialogs.LoginDialog;
import com.huawei.training.java.models.UserObj;
import com.huawei.training.java.utils.AppUtils;
import com.huawei.training.java.utils.LearningApplication;
import com.huawei.training.java.utils.account.AccountUtils;
import com.huawei.training.java.utils.eventmanager.AppAnalytics;
import com.huawei.training.java.utils.video.Constants;

import java.util.Objects;

/**
 * @author Huawei DTSE India
 * @since 2020
 */
public abstract class BaseActivity extends AppCompatActivity {
    /**
     * The Cloud db helper.
     */
    public CloudDbHelper cloudDbHelper;
    /**
     * The App analytics.
     */
    public AppAnalytics appAnalytics;
    /**
     * The User obj.
     */
    public UserObj userObj = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cloudDbHelper = ((LearningApplication) getApplicationContext()).getCloudDbHelper();
        appAnalytics = ((LearningApplication) getApplicationContext()).getAppAnalytics();
        userObj = ((LearningApplication) getApplication()).getUserObj();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_SIGN_IN_LOGIN_CODE) {
            Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager
                    .parseAuthResultFromIntent(data);
            if (authHuaweiIdTask.isSuccessful()) {
                AccountUtils.setUserInfo(authHuaweiIdTask.getResult(), cloudDbHelper, this);
                userObj = ((LearningApplication) getApplication()).getUserObj();
                ((LearningApplication) getApplication()).setRecentlyViewedCoresRefresh(true);
                showToast(getString(R.string.login_success));
            } else {
                ((LearningApplication) getApplicationContext()).setUserObj(null);
                showToast(getString(R.string.login_fail));
            }
        }
    }

    /**
     * Show login dialog boolean.
     *
     * @return the boolean
     */
    public Boolean showLoginDialog() {
        AppUtils utils = new AppUtils(BaseActivity.this);
        String uid = "" + utils.getPref(Constants.UID);
        if (uid.length() == 0) {
            LoginDialog loginDialog = new LoginDialog(this);
            loginDialog.show();
            return true;
        }
        return false;
    }

    /**
     * Show toast.
     *
     * @param msg the msg
     */
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Sets toolbar.
     *
     * @param name the name
     */
    public void setToolbar(String name) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (name != null) {
            getSupportActionBar().setTitle("" + name);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        userObj = ((LearningApplication) getApplication()).getUserObj();
    }

    @Override
    protected void onDestroy() {
        AppUtils.cancelLoading();
        super.onDestroy();
    }
}
