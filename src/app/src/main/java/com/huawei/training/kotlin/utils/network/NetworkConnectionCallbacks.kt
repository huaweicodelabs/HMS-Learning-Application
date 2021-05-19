package com.huawei.training.kotlin.utils.network

/**
 * @since 2020
 * @author Huawei DTSE India
 */
interface NetworkConnectionCallbacks {
    /**
     * On net connected.
     */
    fun onNetConnected()

    /**
     * On net disconnected.
     */
    fun onNetDisconnected()
}