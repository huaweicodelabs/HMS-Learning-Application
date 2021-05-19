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
package com.huawei.training.kotlin.utils

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Toast
import com.huawei.training.R
import com.huawei.training.kotlin.database.tables.CourseContentTable
import com.huawei.training.kotlin.database.tables.CoursesCodelabDetailsTable
import com.huawei.training.kotlin.models.CourseContentDataModel
import java.lang.reflect.InvocationTargetException
import java.util.*
import kotlin.jvm.Throws

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class AppUtils {
    /**
     * The Main shared preference.
     */
    var mainSharedPreference: SharedPreferences? = null

    /**
     * Instantiates a new App utils.
     *
     * @param context the context
     */
    constructor(context: Context) {
        mainSharedPreference = context.getSharedPreferences("main_activity", Context.MODE_PRIVATE)
    }

    /**
     * Instantiates a new App utils.
     */
    constructor() {}

    /**
     * Gets pref.
     *
     * @param key the key
     * @return the pref
     */
    fun getPref(key: String?): String? {
        return mainSharedPreference!!.getString(key, "")
    }

    /**
     * Sets pref.
     *
     * @param key   the key
     * @param value the value
     */
    fun setPref(key: String?, value: String?) {
        val edit = mainSharedPreference!!.edit()
        edit.putString(key, value)
        edit.apply()
    }

    /**
     * Clear all pref.
     */
    fun clearAllPref() {
        val edit = mainSharedPreference!!.edit()
        edit.clear().apply()
    }

    companion object {
        private var progressDialog: Dialog? = null

        /**
         * Share activity.
         *
         * @param context the context
         */
        fun shareActivity(context: Context) {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.resources.getString(R.string.app_name))
            shareIntent.putExtra(Intent.EXTRA_TEXT, context.resources.getString(R.string.appsharing_url))
            context.startActivity(Intent.createChooser(shareIntent, context.resources.getString(R.string.sharevia)))
        }

        /**
         * get course content or code lab content from db
         *
         * @param coContent    is a course content
         * @param coCodelabDoc is a code lab content
         * @return course or codelab content data
         */
        fun getCourseOrCodelabContentData(
                coContent: CourseContentTable?, coCodelabDoc: CoursesCodelabDetailsTable?): ArrayList<CourseContentDataModel> {
            val myListData = ArrayList<CourseContentDataModel>()
            try {
                for (i in 1..10) {
                    val courseContentDataModel = getCodeLabContent(i, coContent, coCodelabDoc)
                    myListData.add(courseContentDataModel)
                }
            } catch (ex: SecurityException) {
                return myListData
            } catch (ex: NoClassDefFoundError) {
                return myListData
            } catch (ex: NoSuchMethodException) {
                return myListData
            } catch (ex: InvocationTargetException) {
                return myListData
            } catch (ex: IllegalAccessException) {
                return myListData
            }
            return myListData
        }

        @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
        private fun getCodeLabContent(i: Int, coContent: CourseContentTable?, coCodelabDoc: CoursesCodelabDetailsTable?): CourseContentDataModel {
            val data: Array<String>
            val dataString: String
            val courseId: String
            val contentStr: String
            var url: String? = null
            val columnsArray = arrayOf("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L")
            if (coContent != null) {
                courseId = coContent.courseId.toString()
                dataString = "" + coContent.javaClass.getMethod("getContent" + columnsArray[i])
                        .invoke(coContent)
            } else {
                courseId = coCodelabDoc?.courseId.toString()
                dataString = "" + coCodelabDoc?.javaClass?.getMethod("getCodeLabContent" + columnsArray[i])
                        ?.invoke(coCodelabDoc)
            }
            if (dataString.contains("@#@")) {
                data = dataString.split("@#@".toRegex()).toTypedArray()
                contentStr = data[0]
                url = data[1]
            } else contentStr = dataString
            return CourseContentDataModel(contentStr, url, courseId)
        }

        /**
         * Network connection check
         *
         * @param context the context
         * @return status of network
         */
        fun isNetworkConnected(context: Context): Boolean {
            val networkConnection = (context.applicationContext as LearningApplication).netWorkConnectionStatus
            if (!networkConnection) {
                Toast.makeText(context, "Network disconnected", Toast.LENGTH_LONG).show()
            }
            return networkConnection
        }

        /**
         * Show loading dialog.
         *
         * @param context the context
         */
        fun showLoadingDialog(context: Context?) {
            //synchronized (context) {
            if (!(progressDialog != null && progressDialog!!.isShowing)) {
                progressDialog = Dialog(context!!)
                Objects.requireNonNull(progressDialog?.window)?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                progressDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                progressDialog?.show()
                progressDialog?.setContentView(R.layout.progress_dialog)
                progressDialog?.setCancelable(true)
            }
            // }
        }

        /**
         * Cancel loading.
         */
        fun cancelLoading() {
            if (progressDialog != null && progressDialog!!.isShowing) progressDialog!!.cancel()
        }
    }
}