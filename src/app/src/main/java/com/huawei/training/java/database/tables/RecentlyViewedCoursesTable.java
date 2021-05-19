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
public class RecentlyViewedCoursesTable extends CloudDBZoneObject {
    @PrimaryKey
    private Integer recentlyViewedId;

    private String courseId;

    private String userId;

    private String courseName;

    public RecentlyViewedCoursesTable() {
        super();
    }

    public void setRecentlyViewedId(Integer recentlyViewedId) {
        this.recentlyViewedId = recentlyViewedId;
    }

    public Integer getRecentlyViewedId() {
        return recentlyViewedId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseName() {
        return courseName;
    }
}
