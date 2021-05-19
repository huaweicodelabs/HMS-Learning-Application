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

package com.huawei.training.java.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.huawei.hms.support.hwid.ui.HuaweiIdAuthButton;
import com.huawei.training.R;
import com.huawei.training.java.utils.account.AccountUtils;

/**
 * @author Huawei DTSE India
 * @since 2020
 */
public class LoginDialog extends BottomSheetDialog {
    /**
     * The Sheet view.
     */
    View sheetView;
    /**
     * The Activity.
     */
    Activity activity;
    /**
     * The Huawei id auth button.
     */
    HuaweiIdAuthButton huaweiIdAuthButton;

    /**
     * Instantiates a new Login dialog.
     *
     * @param activity the activity
     */
    public LoginDialog(Activity activity) {
        super(activity);
        this.activity = activity;
        init();
    }

    @SuppressLint("InflateParams")
    private void init() {
        sheetView = activity.getLayoutInflater().inflate(R.layout.login_dialog, null);
        setContentView(sheetView);
        huaweiIdAuthButton = findViewById(R.id.singin_btn);
        assert huaweiIdAuthButton != null;
        huaweiIdAuthButton.setOnClickListener(view -> signInCode());
    }

    /**
     * Invoking huawei login page
     */
    private void signInCode() {
        AccountUtils.signIn(activity);
        dismiss();
    }
}
