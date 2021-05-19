package com.huawei.training.database

import android.content.Context
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.agconnect.cloud.database.CloudDBZone
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException
import com.huawei.training.kotlin.database.CloudDbAction
import com.huawei.training.kotlin.database.CloudDbConstants
import com.huawei.training.kotlin.database.CloudDbQueyCalls
import com.huawei.training.kotlin.database.CloudDbUiCallbackListener
import com.huawei.training.kotlin.database.ObjectTypeInfoHelper.objectTypeInfo

open class CloudDbHelper(val mContext: Context) {

    var mCloudDB: AGConnectCloudDB
    private var mCloudDBZone: CloudDBZone? = null
    private var mConfig: CloudDBZoneConfig? = null
    var cloudDbQueyCalls: CloudDbQueyCalls? = null

    /**
     * The Cloud db ui callback listener.
     */
    var cloudDbUiCallbackListener: CloudDbUiCallbackListener? = null

    init {
        initAGConnectCloudDB(mContext)
        mCloudDB = AGConnectCloudDB.getInstance()
        cloudDbQueyCalls = CloudDbQueyCalls()
    }

    /**
     * Init AGConnectCloudDB in Application
     *
     * @param context application context
     */
    open fun initAGConnectCloudDB(context: Context?) {
        AGConnectCloudDB.initialize(context!!)
    }

    /**
     * Call AGConnectCloudDB.createObjectType to init schema
     */
    open fun createObjectType() {
        try {
            mCloudDB.createObjectType(objectTypeInfo)
        } catch (exp: AGConnectCloudDBException) {
        }
    }

    /**
     * Open cloud db zone v 2.
     *
     * @param cloudDbAction the cloud db action
     */
    open fun openCloudDBZoneV2(cloudDbAction: CloudDbAction?) {
        mConfig = CloudDBZoneConfig(
                CloudDbConstants.CLOUD_DB,
                CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
                CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC)
        mConfig?.setPersistenceEnabled(true)
        val openDBZoneTask = mCloudDB.openCloudDBZone2(mConfig!!, true)
        openDBZoneTask
                .addOnSuccessListener { cloudDBZone ->
                    mCloudDBZone = cloudDBZone
                    cloudDbQueyCalls?.setmCloudDBZone(mCloudDBZone)
                    cloudDbUiCallbackListener?.onSuccessDbQueryMessage(
                            cloudDbAction, "open clouddbzone success")
                }
                .addOnFailureListener {
                    cloudDbUiCallbackListener?.onFailureDbQueryMessage(
                            cloudDbAction, "")
                }
    }


    /**
     * Add call back listener.
     *
     * @param cloudDbUiCallbackListener the cloud db ui callback listener
     */
    open fun addCallBackListener(mCloudDbUiCallbackListener: CloudDbUiCallbackListener) {
        cloudDbUiCallbackListener = mCloudDbUiCallbackListener
    }

    /**
     * Returning the cloud db query instance
     *
     * @return cloud db quey calls
     */
    fun getCloudDbAllQueyCalls(): CloudDbQueyCalls? {
        if (cloudDbQueyCalls != null) {
            cloudDbQueyCalls?.addCloudDbUiCallbackListener(cloudDbUiCallbackListener)
        }
        return cloudDbQueyCalls
    }

}