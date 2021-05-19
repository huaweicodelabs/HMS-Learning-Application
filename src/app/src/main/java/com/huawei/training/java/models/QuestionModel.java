/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.training.java.models;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class QuestionModel {
    private int id;
    private String question;
    private String optA;
    private String optB;
    private String optC;
    private String optD;
    private String answer;

    /**
     * Instantiates a new Question model.
     */
    public QuestionModel() {
        id = 0;
        question = "";
        optA = "";
        optB = "";
        optC = "";
        optD = "";
        answer = "";
    }

    /**
     * Instantiates a new Question model.
     *
     * @param question the question
     * @param optA     the opt a
     * @param optB     the opt b
     * @param optC     the opt c
     * @param optD     the opt d
     * @param answer   the answer
     */
    public QuestionModel(String question, String optA, String optB, String optC, String optD, String answer) {
        this.question = question;
        this.optA = optA;
        this.optB = optB;
        this.optC = optC;
        this.optD = optD;
        this.answer = answer;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets answer.
     *
     * @return the answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     * Gets opt c.
     *
     * @return the opt c
     */
    public String getOptC() {
        return optC;
    }

    /**
     * Gets opt b.
     *
     * @return the opt b
     */
    public String getOptB() {
        return optB;
    }

    /**
     * Gets opt a.
     *
     * @return the opt a
     */
    public String getOptA() {
        return optA;
    }

    /**
     * Gets question.
     *
     * @return the question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets question.
     *
     * @param question the question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * Sets opt a.
     *
     * @param optA the opt a
     */
    public void setOptA(String optA) {
        this.optA = optA;
    }

    /**
     * Sets opt b.
     *
     * @param optB the opt b
     */
    public void setOptB(String optB) {
        this.optB = optB;
    }

    /**
     * Sets opt c.
     *
     * @param optC the opt c
     */
    public void setOptC(String optC) {
        this.optC = optC;
    }

    /**
     * Sets answer.
     *
     * @param answer the answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     * Gets opt d.
     *
     * @return the opt d
     */
    public String getOptD() {
        return optD;
    }

    /**
     * Sets opt d.
     *
     * @param optD the opt d
     */
    public void setOptD(String optD) {
        this.optD = optD;
    }
}
