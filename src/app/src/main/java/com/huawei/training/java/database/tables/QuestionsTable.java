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
public class QuestionsTable extends CloudDBZoneObject {
    @PrimaryKey
    private Long questionId;

    private Integer examId;

    private Integer courseId;

    private String question;

    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    private Integer marks;

    private String answer;

    public QuestionsTable() {
        super();
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setExamId(Integer examId) {
        this.examId = examId;
    }

    public Integer getExamId() {
        return examId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setMarks(Integer marks) {
        this.marks = marks;
    }

    public Integer getMarks() {
        return marks;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }
}