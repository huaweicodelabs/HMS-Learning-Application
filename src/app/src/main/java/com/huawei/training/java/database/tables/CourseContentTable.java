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
public class CourseContentTable extends CloudDBZoneObject {
    @PrimaryKey
    private Integer courseContentId;

    private Long courseId;

    private String contentA;

    private String contentB;

    private String contentC;

    private String contentD;

    private String contentE;

    private String contentF;

    private String contentG;

    private String contentH;

    private String contentI;

    private String contentJ;

    private String contentK;

    private String contentL;

    public CourseContentTable() {
        super();
    }

    public void setCourseContentId(Integer courseContentId) {
        this.courseContentId = courseContentId;
    }

    public Integer getCourseContentId() {
        return courseContentId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setContentA(String contentA) {
        this.contentA = contentA;
    }

    public String getContentA() {
        return contentA;
    }

    public void setContentB(String contentB) {
        this.contentB = contentB;
    }

    public String getContentB() {
        return contentB;
    }

    public void setContentC(String contentC) {
        this.contentC = contentC;
    }

    public String getContentC() {
        return contentC;
    }

    public void setContentD(String contentD) {
        this.contentD = contentD;
    }

    public String getContentD() {
        return contentD;
    }

    public void setContentE(String contentE) {
        this.contentE = contentE;
    }

    public String getContentE() {
        return contentE;
    }

    public void setContentF(String contentF) {
        this.contentF = contentF;
    }

    public String getContentF() {
        return contentF;
    }

    public void setContentG(String contentG) {
        this.contentG = contentG;
    }

    public String getContentG() {
        return contentG;
    }

    public void setContentH(String contentH) {
        this.contentH = contentH;
    }

    public String getContentH() {
        return contentH;
    }

    public void setContentI(String contentI) {
        this.contentI = contentI;
    }

    public String getContentI() {
        return contentI;
    }

    public void setContentJ(String contentJ) {
        this.contentJ = contentJ;
    }

    public String getContentJ() {
        return contentJ;
    }

    public void setContentK(String contentK) {
        this.contentK = contentK;
    }

    public String getContentK() {
        return contentK;
    }

    public void setContentL(String contentL) {
        this.contentL = contentL;
    }

    public String getContentL() {
        return contentL;
    }
}
