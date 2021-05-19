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
package com.huawei.training.kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.huawei.training.R
import com.huawei.training.kotlin.utils.AppUtils
import com.huawei.training.kotlin.utils.account.AccountUtils

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    /**
     * Skip.
     *
     * @param view the view
     */
    fun skip(view: View?) {
        gotToHome()
    }

    private fun gotToHome() {
        if (AppUtils.isNetworkConnected(this)) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    /**
     * Sign in code.
     *
     * @param view the view
     */
    fun signInCode(view: View?) {
        if (AppUtils.isNetworkConnected(this)) AccountUtils.signIn(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        gotToHome()
    }
}