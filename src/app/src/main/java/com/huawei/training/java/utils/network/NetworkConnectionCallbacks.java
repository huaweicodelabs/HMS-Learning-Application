package com.huawei.training.java.utils.network;


/**
 * @since 2020
 * @author Huawei DTSE India
 */
public interface NetworkConnectionCallbacks {
    /**
     * On net connected.
     */
    void onNetConnected();

    /**
     * On net disconnected.
     */
    void onNetDisconnected();
}
