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
import android.view.View;

import com.huawei.training.R;
import com.huawei.training.java.utils.AppUtils;
import com.huawei.training.java.utils.account.AccountUtils;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class LoginActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /**
     * Skip.
     *
     * @param view the view
     */
    public void skip(View view) {
        gotToHome();
    }

    private void gotToHome() {
        if (AppUtils.isNetworkConnected(this)) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    /**
     * Sign in code.
     *
     * @param view the view
     */
    public void signInCode(View view) {
        if (AppUtils.isNetworkConnected(this)) AccountUtils.signIn(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gotToHome();
    }
}
