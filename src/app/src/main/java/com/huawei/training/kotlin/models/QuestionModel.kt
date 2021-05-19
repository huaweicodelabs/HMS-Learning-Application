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
package com.huawei.training.kotlin.models

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class QuestionModel {
    /**
     * Gets id.
     *
     * @return the id
     */
    /**
     * Sets id.
     *
     * @param id the id
     */
    /**
     * Gets question.
     *
     * @return the question
     */
    /**
     * Sets question.
     *
     * @param question the question
     */
    var question: String
    /**
     * Gets opt a.
     *
     * @return the opt a
     */
    /**
     * Sets opt a.
     *
     * @param optA the opt a
     */
    var optA: String
    /**
     * Gets opt b.
     *
     * @return the opt b
     */
    /**
     * Sets opt b.
     *
     * @param optB the opt b
     */
    var optB: String
    /**
     * Gets opt c.
     *
     * @return the opt c
     */
    /**
     * Sets opt c.
     *
     * @param optC the opt c
     */
    var optC: String
    /**
     * Gets opt d.
     *
     * @return the opt d
     */
    /**
     * Sets opt d.
     *
     * @param optD the opt d
     */
    var optD: String
    /**
     * Gets answer.
     *
     * @return the answer
     */
    /**
     * Sets answer.
     *
     * @param answer the answer
     */
    var answer: String

    /**
     * Instantiates a new Question model.
     */
    constructor() {
        question = ""
        optA = ""
        optB = ""
        optC = ""
        optD = ""
        answer = ""
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
    constructor(question: String, optA: String, optB: String, optC: String, optD: String, answer: String) {
        this.question = question
        this.optA = optA
        this.optB = optB
        this.optC = optC
        this.optD = optD
        this.answer = answer
    }

}