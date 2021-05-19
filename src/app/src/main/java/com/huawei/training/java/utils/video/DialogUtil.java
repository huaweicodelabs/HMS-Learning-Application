/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.training.java.utils.video;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.huawei.training.R;
import com.huawei.training.java.listeners.OnDialogInputValueListener;
import com.huawei.training.java.listeners.OnPlaySettingListener;
import com.huawei.training.java.utils.view.PlaySettingDialog;

import java.util.List;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class DialogUtil {
    /**
     * Play activity Settings dialog
     *
     * @param context               Context
     * @param settingType           Set the play type
     * @param showTextList          Set the list of options
     * @param selectIndex           The default Settings index
     * @param onPlaySettingListener Click listener
     */
    public static void onSettingDialogSelectIndex(
            Context context,
            int settingType,
            List<String> showTextList,
            int selectIndex,
            OnPlaySettingListener onPlaySettingListener) {
        PlaySettingDialog dialog = new PlaySettingDialog(context).setList(showTextList);
        dialog.setTitle(StringUtil.getStringFromResId(context, R.string.settings));
        dialog.setSelectIndex(selectIndex);
        dialog.setNegativeButton(StringUtil.getStringFromResId(context, R.string.setting_cancel), null);
        dialog.initDialog(onPlaySettingListener, settingType);
        dialog.show();
    }

    /**
     * Play activity Settings dialog
     *
     * @param context               Context
     * @param settingType           Set the play type
     * @param showTextList          Set the list of options
     * @param selectValue           The default Settings string
     * @param onPlaySettingListener Click listener
     */
    public static void onSettingDialogSelectValue(
            Context context,
            int settingType,
            List<String> showTextList,
            String selectValue,
            OnPlaySettingListener onPlaySettingListener) {
        PlaySettingDialog dialog = new PlaySettingDialog(context).setList(showTextList);
        dialog.setTitle(StringUtil.getStringFromResId(context, R.string.settings));
        dialog.setSelectValue(selectValue)
                .setNegativeButton(StringUtil.getStringFromResId(context, R.string.setting_cancel), null);
        dialog.initDialog(onPlaySettingListener, settingType);
        dialog.show();
    }

    /**
     * Get the volume Settings dialog
     *
     * @param context                    Context
     * @param onDialogInputValueListener Click listener
     */
    public static void showSetVolumeDialog(
            Context context, final OnDialogInputValueListener onDialogInputValueListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.set_volume_dialog, null);
        final AlertDialog dialog =
                new AlertDialog.Builder(context)
                        .setTitle(StringUtil.getStringFromResId(context, R.string.video_set_volume))
                        .setView(view)
                        .create();
        dialog.show();
        final EditText volumeValueEt = view.findViewById(R.id.set_volume_et);
        Button okBt = view.findViewById(R.id.set_volume_bt_ok);
        Button cancelBt = view.findViewById(R.id.set_volume_bt_cancel);
        okBt.setOnClickListener(
                start -> {
                    if (onDialogInputValueListener != null) {
                        String inputText = "";
                        if (volumeValueEt.getText() != null) {
                            inputText = volumeValueEt.getText().toString();
                        }
                        onDialogInputValueListener.dialogInputListener(inputText);
                        dialog.dismiss();
                    }
                });
        cancelBt.setOnClickListener(
                start -> dialog.dismiss());
    }


}
