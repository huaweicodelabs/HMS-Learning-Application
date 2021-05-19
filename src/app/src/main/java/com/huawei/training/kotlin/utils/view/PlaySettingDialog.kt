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
package com.huawei.training.kotlin.utils.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Pair
import com.huawei.training.kotlin.listeners.OnPlaySettingListener
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class PlaySettingDialog(context: Context?) : DialogInterface.OnClickListener {
    // Dialog builder
    private val builder: AlertDialog.Builder

    // Listener
    private var onPlaySettingListener: OnPlaySettingListener? = null

    // Show data list
    private var showTextList: MutableList<Pair<String, String>> = ArrayList()

    // Setting Type
    private var playSettingType = 0

    // The default selection
    private var selectIndex = 0

    /**
     * Set dialog title
     *
     * @param title Title
     * @return dialog title
     */
    fun setTitle(title: CharSequence?): PlaySettingDialog {
        builder.setTitle(title)
        return this
    }

    /**
     * Set dialog data
     *
     * @param strList data list
     * @return dialog list
     */
    fun setList(strList: List<String>?): PlaySettingDialog {
        showTextList = ArrayList()
        if (strList != null) {
            for (temp in strList) {
                showTextList.add(Pair(temp, temp))
            }
        }
        return this
    }

    /**
     * Set dialog default selection
     *
     * @param value The select value
     * @return Dialog select value
     */
    fun setSelectValue(value: String): PlaySettingDialog {
        for (iLoop in showTextList.indices) {
            if (showTextList[iLoop].first == value) {
                selectIndex = iLoop
                break
            }
        }
        return this
    }

    /**
     * Set dialog default selection
     *
     * @param index The select index
     * @return Dialog select index
     */
    fun setSelectIndex(index: Int): PlaySettingDialog {
        selectIndex = index
        return this
    }

    /**
     * Settings dialog parameter
     *
     * @param playSettingListener Listener
     * @param playSettingType     Player set type
     * @return Dialog play setting dialog
     */
    fun initDialog(playSettingListener: OnPlaySettingListener?, playSettingType: Int): PlaySettingDialog {
        onPlaySettingListener = playSettingListener
        this.playSettingType = playSettingType
        return this
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (onPlaySettingListener != null) {
            onPlaySettingListener!!.onSettingItemClick(showTextList[which].first, playSettingType)
        }
        dialog.dismiss()
    }

    /**
     * Cancel button listener
     *
     * @param text     Cancel button value
     * @param listener Listener
     * @return Dialog negative button
     */
    fun setNegativeButton(text: String?, listener: DialogInterface.OnClickListener?): PlaySettingDialog {
        builder.setNegativeButton(text, listener)
        return this
    }

    /**
     * Show dialog
     *
     * @return Dialog play setting dialog
     */
    fun show(): PlaySettingDialog {
        val items = arrayOfNulls<String>(showTextList.size)
        for (iLoop in items.indices) {
            items[iLoop] = showTextList[iLoop].second
        }
        builder.setSingleChoiceItems(items, selectIndex, this)
        val dialog = builder.create()
        val window = dialog.window!!
        val lp = window.attributes
        lp.alpha = 0.7f
        window.attributes = lp
        dialog.show()
        return this
    }

    /**
     * Constructor
     *
     * @param context Context
     */
    init {
        builder = AlertDialog.Builder(context)
    }
}