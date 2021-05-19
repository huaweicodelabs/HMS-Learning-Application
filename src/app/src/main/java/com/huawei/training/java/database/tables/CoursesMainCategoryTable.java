/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2019-2019. All rights reserved.
 * Generated by the CloudDB ObjectType compiler.  DO NOT EDIT!
 */
package com.huawei.training.java.database.tables;

import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.annotations.PrimaryKey;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class CoursesMainCategoryTable extends CloudDBZoneObject {
    @PrimaryKey
    private Integer courseMainCategoryId;

    private String courseMainCategoryName;

    public CoursesMainCategoryTable() {
        super();
    }

    public void setCourseMainCategoryId(Integer courseMainCategoryId) {
        this.courseMainCategoryId = courseMainCategoryId;
    }

    public Integer getCourseMainCategoryId() {
        return courseMainCategoryId;
    }

    public void setCourseMainCategoryName(String courseMainCategoryName) {
        this.courseMainCategoryName = courseMainCategoryName;
    }

    public String getCourseMainCategoryName() {
        return courseMainCategoryName;
    }
}
