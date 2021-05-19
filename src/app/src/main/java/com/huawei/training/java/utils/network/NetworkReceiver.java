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

package com.huawei.training.java.utils.network;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class NetworkReceiver extends BroadcastReceiver {
    /**
     * The Network connection callbacks.
     */
    NetworkConnectionCallbacks networkConnectionCallbacks;

    /**
     * Instantiates a new Network receiver.
     *
     * @param networkConnectionCallbacks the network connection callbacks
     */
    public NetworkReceiver(NetworkConnectionCallbacks networkConnectionCallbacks) {
        this.networkConnectionCallbacks = networkConnectionCallbacks;
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetworkUtil.getNetworkStatus(context)) {
            networkConnectionCallbacks.onNetDisconnected();
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_LONG).show();
        } else networkConnectionCallbacks.onNetConnected();
    }
}
