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
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.training.R
import com.huawei.training.kotlin.database.CloudDbHelper
import com.huawei.training.kotlin.dialogs.LoginDialog
import com.huawei.training.kotlin.models.UserObj
import com.huawei.training.kotlin.utils.AppUtils
import com.huawei.training.kotlin.utils.AppUtils.Companion.cancelLoading
import com.huawei.training.kotlin.utils.LearningApplication
import com.huawei.training.kotlin.utils.account.AccountUtils
import com.huawei.training.kotlin.utils.eventmanager.AppAnalytics
import com.huawei.training.kotlin.utils.video.Constants
import java.util.*

/**
 * @author Huawei DTSE India
 * @since 2020
 */
abstract class BaseActivity : AppCompatActivity() {


    /**
     * The App analytics.
     */
    @JvmField
    var appAnalytics: AppAnalytics? = null

    /**
     * The User obj.
     */
    @JvmField
    var userObj: UserObj? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appAnalytics = (applicationContext as LearningApplication).appAnalytics
        userObj = (application as LearningApplication).userObj
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_SIGN_IN_LOGIN_CODE) {
            val authHuaweiIdTask = HuaweiIdAuthManager
                    .parseAuthResultFromIntent(data)
            if (authHuaweiIdTask.isSuccessful) {
                CloudDbHelper.getInstance(applicationContext)?.let { AccountUtils.setUserInfo(authHuaweiIdTask.result, it, this) }
                userObj = (application as LearningApplication).userObj
                (application as LearningApplication).isRecentlyViewedCoresRefresh = true
                showToast(getString(R.string.login_success))
            } else {
                (applicationContext as LearningApplication).userObj = null
                showToast(getString(R.string.login_fail))
            }
        }
    }

    /**
     * Show login dialog boolean.
     *
     * @return the boolean
     */
    fun showLoginDialog(): Boolean {
        val utils = AppUtils(this@BaseActivity)
        val uid = "" + utils.getPref(Constants.UID)
        if (uid.length == 0) {
            val loginDialog = LoginDialog(this)
            loginDialog.show()
            return true
        }
        return false
    }

    /**
     * Show toast.
     *
     * @param msg the msg
     */
    fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    /**
     * Sets toolbar.
     *
     * @param name the name
     */
    fun setToolbar(name: String?) {
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        if (name != null) {
            supportActionBar?.title = "" + name
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        userObj = (application as LearningApplication).userObj
    }

    override fun onDestroy() {
        cancelLoading()
        super.onDestroy()
    }
}