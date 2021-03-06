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
public class MyCoursesTable extends CloudDBZoneObject {
    @PrimaryKey
    private Integer myCourseId;

    private String userId;

    private Integer courseId;

    private String courseName;

    public MyCoursesTable() {
        super();
    }

    public void setMyCourseId(Integer myCourseId) {
        this.myCourseId = myCourseId;
    }

    public Integer getMyCourseId() {
        return myCourseId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }
}
