/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.huawei.training.kotlin.utils.video

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.huawei.training.R
import com.huawei.training.kotlin.listeners.OnDialogInputValueListener
import com.huawei.training.kotlin.listeners.OnPlaySettingListener
import com.huawei.training.kotlin.utils.video.StringUtil.getStringFromResId
import com.huawei.training.kotlin.utils.view.PlaySettingDialog

/**
 * @since 2020
 * @author Huawei DTSE India
 */
object DialogUtil {
    /**
     * Play activity Settings dialog
     *
     * @param context               Context
     * @param settingType           Set the play type
     * @param showTextList          Set the list of options
     * @param selectIndex           The default Settings index
     * @param onPlaySettingListener Click listener
     */
    fun onSettingDialogSelectIndex(
            context: Context?,
            settingType: Int,
            showTextList: List<String?>?,
            selectIndex: Int,
            onPlaySettingListener: OnPlaySettingListener?) {
        val dialog = PlaySettingDialog(context).setList(showTextList as List<String>)
        dialog.setTitle(getStringFromResId(context!!, R.string.settings))
        dialog.setSelectIndex(selectIndex)
        dialog.setNegativeButton(getStringFromResId(context, R.string.setting_cancel), null)
        dialog.initDialog(onPlaySettingListener, settingType)
        dialog.show()
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
    fun onSettingDialogSelectValue(
            context: Context?,
            settingType: Int,
            showTextList: List<String?>?,
            selectValue: String?,
            onPlaySettingListener: OnPlaySettingListener?) {
        val dialog = PlaySettingDialog(context).setList(showTextList as List<String>)
        dialog.setTitle(getStringFromResId(context!!, R.string.settings))
        dialog.setSelectValue(selectValue!!)
                .setNegativeButton(getStringFromResId(context, R.string.setting_cancel), null)
        dialog.initDialog(onPlaySettingListener, settingType)
        dialog.show()
    }

    /**
     * Get the volume Settings dialog
     *
     * @param context                    Context
     * @param onDialogInputValueListener Click listener
     */
    fun showSetVolumeDialog(
            context: Context?, onDialogInputValueListener: OnDialogInputValueListener?) {
        val view = LayoutInflater.from(context).inflate(R.layout.set_volume_dialog, null)
        val dialog = AlertDialog.Builder(context)
                .setTitle(getStringFromResId(context!!, R.string.video_set_volume))
                .setView(view)
                .create()
        dialog.show()
        val volumeValueEt = view.findViewById<EditText>(R.id.set_volume_et)
        val okBt = view.findViewById<Button>(R.id.set_volume_bt_ok)
        val cancelBt = view.findViewById<Button>(R.id.set_volume_bt_cancel)
        okBt.setOnClickListener { start: View? ->
            if (onDialogInputValueListener != null) {
                var inputText = ""
                if (volumeValueEt.text != null) {
                    inputText = volumeValueEt.text.toString()
                }
                onDialogInputValueListener.dialogInputListener(inputText)
                dialog.dismiss()
            }
        }
        cancelBt.setOnClickListener { start: View? -> dialog.dismiss() }
    }
}