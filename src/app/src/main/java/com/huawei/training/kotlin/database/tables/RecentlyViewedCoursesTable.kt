/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 * Generated by the CloudDB ObjectType compiler.  DO NOT EDIT!
 */
package com.huawei.training.kotlin.database.tables

import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.annotations.PrimaryKey

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class RecentlyViewedCoursesTable : CloudDBZoneObject() {
    @PrimaryKey
    var recentlyViewedId: Int? = null
    var courseId: String? = null
    var userId: String? = null
    var courseName: String? = null

}