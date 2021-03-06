/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 * Generated by the CloudDB ObjectType compiler.  DO NOT EDIT!
 */
package com.huawei.training.java.database;

import com.huawei.agconnect.cloud.database.ObjectTypeInfo;
import com.huawei.training.java.database.tables.BookInfo;
import com.huawei.training.java.database.tables.CourseContentTable;
import com.huawei.training.java.database.tables.CourseDetailsTable;
import com.huawei.training.java.database.tables.CoursePlatformTable;
import com.huawei.training.java.database.tables.CoursesCodelabDetailsTable;
import com.huawei.training.java.database.tables.CoursesDocTable;
import com.huawei.training.java.database.tables.CoursesMainCategoryTable;
import com.huawei.training.java.database.tables.CoursesPriceType;
import com.huawei.training.java.database.tables.CoursesSubCategoryTable;
import com.huawei.training.java.database.tables.CoursesTypeTable;
import com.huawei.training.java.database.tables.ExamTable;
import com.huawei.training.java.database.tables.FavPodcast;
import com.huawei.training.java.database.tables.FavouritePodcast;
import com.huawei.training.java.database.tables.MyCoursesTable;
import com.huawei.training.java.database.tables.QuestionsTable;
import com.huawei.training.java.database.tables.RecentlyViewedCoursesTable;
import com.huawei.training.java.database.tables.Test;
import com.huawei.training.java.database.tables.UserActivitiesTable;
import com.huawei.training.java.database.tables.UsersInfoTable;

import java.util.Arrays;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class ObjectTypeInfoHelper {
    private final static int FORMAT_VERSION = 1;
    private final static int OBJECT_TYPE_VERSION = 86;

    public static ObjectTypeInfo getObjectTypeInfo() {
        ObjectTypeInfo objectTypeInfo = new ObjectTypeInfo();
        objectTypeInfo.setFormatVersion(FORMAT_VERSION);
        objectTypeInfo.setObjectTypeVersion(OBJECT_TYPE_VERSION);
        objectTypeInfo.setObjectTypes(Arrays.asList(CoursesPriceType.class,
                UserActivitiesTable.class,
                FavouritePodcast.class,
                BookInfo.class,
                CoursesDocTable.class,
                ExamTable.class,
                CourseDetailsTable.class,
                RecentlyViewedCoursesTable.class,
                CoursePlatformTable.class,
                CoursesMainCategoryTable.class,
                FavPodcast.class,
                CoursesCodelabDetailsTable.class,
                CoursesTypeTable.class,
                CourseContentTable.class,
                QuestionsTable.class,
                Test.class,
                CoursesSubCategoryTable.class,
                UsersInfoTable.class,
                MyCoursesTable.class));
        return objectTypeInfo;
    }
}
