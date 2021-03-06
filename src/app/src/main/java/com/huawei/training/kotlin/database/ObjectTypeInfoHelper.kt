/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 * Generated by the CloudDB ObjectType compiler.  DO NOT EDIT!
 */
package com.huawei.training.kotlin.database

import com.huawei.agconnect.cloud.database.ObjectTypeInfo
import com.huawei.training.kotlin.database.tables.*
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
object ObjectTypeInfoHelper {
    private const val FORMAT_VERSION = 1
    private const val OBJECT_TYPE_VERSION = 86
    @JvmStatic
    val objectTypeInfo: ObjectTypeInfo
        get() {
            val objectTypeInfo = ObjectTypeInfo()
            objectTypeInfo.formatVersion = FORMAT_VERSION
            objectTypeInfo.objectTypeVersion = OBJECT_TYPE_VERSION.toLong()
            objectTypeInfo.objectTypes = Arrays.asList(CoursesPriceType::class.java,
                    UserActivitiesTable::class.java,
                    FavouritePodcast::class.java,
                    BookInfo::class.java,
                    CoursesDocTable::class.java,
                    ExamTable::class.java,
                    CourseDetailsTable::class.java,
                    RecentlyViewedCoursesTable::class.java,
                    CoursePlatformTable::class.java,
                    CoursesMainCategoryTable::class.java,
                    FavPodcast::class.java,
                    CoursesCodelabDetailsTable::class.java,
                    CoursesTypeTable::class.java,
                    CourseContentTable::class.java,
                    QuestionsTable::class.java,
                    Test::class.java,
                    CoursesSubCategoryTable::class.java,
                    UsersInfoTable::class.java,
                    MyCoursesTable::class.java)
            return objectTypeInfo
        }
}