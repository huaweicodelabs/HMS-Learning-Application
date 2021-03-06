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
public class UserActivitiesTable extends CloudDBZoneObject {
    @PrimaryKey
    private Long userId;

    private String viewedCourseIds;

    private String suggestedCourseIds;

    public UserActivitiesTable() {
        super();
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setViewedCourseIds(String viewedCourseIds) {
        this.viewedCourseIds = viewedCourseIds;
    }

    public String getViewedCourseIds() {
        return viewedCourseIds;
    }

    public void setSuggestedCourseIds(String suggestedCourseIds) {
        this.suggestedCourseIds = suggestedCourseIds;
    }

    public String getSuggestedCourseIds() {
        return suggestedCourseIds;
    }
}
