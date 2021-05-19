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

package com.huawei.training.java.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Toast;

import com.huawei.training.R;
import com.huawei.training.java.database.tables.CourseContentTable;
import com.huawei.training.java.database.tables.CoursesCodelabDetailsTable;
import com.huawei.training.java.models.CourseContentDataModel;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class AppUtils {
    private static Dialog progressDialog;
    /**
     * The Main shared preference.
     */
    SharedPreferences mainSharedPreference;

    /**
     * Instantiates a new App utils.
     *
     * @param context the context
     */
    public AppUtils(Context context) {
        mainSharedPreference = context.getSharedPreferences("main_activity", Context.MODE_PRIVATE);
    }

    /**
     * Instantiates a new App utils.
     */
    public AppUtils() {
    }

    /**
     * Share activity.
     *
     * @param context the context
     */
    public static void shareActivity(Context context) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.appsharing_url));
        context.startActivity(Intent.createChooser(shareIntent, context.getResources().getString(R.string.sharevia)));
    }

    /**
     * get course content or code lab content from db
     *
     * @param coContent    is a course content
     * @param coCodelabDoc is a code lab content
     * @return course or codelab content data
     */
    public static ArrayList<CourseContentDataModel> getCourseOrCodelabContentData(
            CourseContentTable coContent, CoursesCodelabDetailsTable coCodelabDoc) {
        ArrayList<CourseContentDataModel> myListData = new ArrayList<>();
        try {
            for (int i = 1; i <= 10; i++) {
                CourseContentDataModel courseContentDataModel = getCodeLabContent(i, coContent, coCodelabDoc);
                myListData.add(courseContentDataModel);
            }
        } catch (SecurityException | NoClassDefFoundError | NoSuchMethodException |
                InvocationTargetException | IllegalAccessException ex) {
            return myListData;
        }
        return myListData;
    }

    private static CourseContentDataModel getCodeLabContent
            (int i, CourseContentTable coContent, CoursesCodelabDetailsTable coCodelabDoc)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String[] data;
        String dataString;
        String courseId;
        String contentStr;
        String url = null;
        String[] columnsArray = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L"};
        if (coContent != null) {
            courseId = coContent.getCourseId().toString();
            dataString = "" + coContent.getClass().getMethod("getContent" + columnsArray[i])
                    .invoke(coContent);
        } else {
            courseId = coCodelabDoc.getCourseId().toString();
            dataString = "" + coCodelabDoc.getClass().getMethod("getCodeLabContent" + columnsArray[i])
                    .invoke(coCodelabDoc);
        }
        if (dataString.contains("@#@")) {
            data = dataString.split("@#@");
            contentStr = data[0];
            url = data[1];
        } else
            contentStr = dataString;
        return new CourseContentDataModel(contentStr, url, courseId);
    }

    /**
     * Network connection check
     *
     * @param context the context
     * @return status of network
     */
    public static boolean isNetworkConnected(Context context) {
        boolean networkConnection = ((LearningApplication) context.getApplicationContext()).
                getNetWorkConnectionStatus();
        if (!networkConnection) {
            Toast.makeText(context, "Network disconnected", Toast.LENGTH_LONG).show();
        }
        return networkConnection;
    }

    /**
     * Show loading dialog.
     *
     * @param context the context
     */
    public static void showLoadingDialog(Context context) {
        //synchronized (context) {
            if (!(progressDialog != null && progressDialog.isShowing())) {
                progressDialog = new Dialog(context);
                Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressDialog.show();
                progressDialog.setContentView(R.layout.progress_dialog);
                progressDialog.setCancelable(true);
            }
       // }
    }

    /**
     * Cancel loading.
     */
    public static void cancelLoading() {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.cancel();
    }

    /**
     * Gets pref.
     *
     * @param key the key
     * @return the pref
     */
    public String getPref(String key) {
        return mainSharedPreference.getString(key, "");
    }

    /**
     * Sets pref.
     *
     * @param key   the key
     * @param value the value
     */
    public void setPref(String key, String value) {
        SharedPreferences.Editor edit = mainSharedPreference.edit();
        edit.putString(key, value);
        edit.apply();
    }

    /**
     * Clear all pref.
     */
    public void clearAllPref() {
        SharedPreferences.Editor edit = mainSharedPreference.edit();
        edit.clear().apply();
    }
}
