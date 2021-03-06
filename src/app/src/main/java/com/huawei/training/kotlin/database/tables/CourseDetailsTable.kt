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
class CourseDetailsTable : CloudDBZoneObject() {
    @PrimaryKey
    var courseId: Int? = null
    var mainCategoryId: Int? = null
    var subCategoryId: Int? = null
    var courseTypeId: Int? = null
    var coursePriceId: Int? = null
    var courseDocId: String? = null
    var courseNmae: String? = null
    var courseDesc: String? = null
    var courseImageUrl: String? = null
    var courseVideoUrl: String? = null
    var courseAudioUrl: String? = null
    var courseAuthor: String? = null
    var coursePlaformId: String? = null
    var courseCodelabId: Int? = null
    var courseContentId: Int? = null

}