/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 * Generated by the CloudDB ObjectType compiler.  DO NOT EDIT!
 */
package com.huawei.training.kotlin.database.tables

import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.annotations.IsIndex
import com.huawei.agconnect.cloud.database.annotations.PrimaryKey
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class BookInfo : CloudDBZoneObject() {
    @PrimaryKey
    var id: Int? = null

    @IsIndex(indexName = "bookName")
    var bookName: String? = null
    var author: String? = null
    var price: Double? = null
    var publisher: String? = null
    var publishTime: Date? = null
    var shadowFlag: Boolean? = null

}