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
public class BannerModel {
    /**
     * The Banner url.
     */
    private String bannerUrl;
    /**
     * The Banner name.
     */
    private String bannerName;

    /**
     * The Banner image.
     */
    private int bannerImage;

    /**
     * Instantiates a new Banner model.
     *
     * @param bannerUrl   the banner url
     * @param bannerName  the banner name
     * @param bannerImage the banner image
     */
    public BannerModel(String bannerUrl, String bannerName, int bannerImage) {
        this.bannerUrl = bannerUrl;
        this.bannerName = bannerName;
        this.bannerImage = bannerImage;
    }

    /**
     * Gets banner name.
     *
     * @return the banner name
     */
    public String getBannerName() {
        return bannerName;
    }

    /**
     * Sets banner name.
     *
     * @param bannerName the banner name
     */
    public void setBannerName(String bannerName) {
        this.bannerName = bannerName;
    }

    /**
     * Gets banner url.
     *
     * @return the banner url
     */
    public String getBannerUrl() {
        return bannerUrl;
    }

    /**
     * Sets banner url.
     *
     * @param bannerUrl the banner url
     */
    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    /**
     * Gets banner image.
     *
     * @return the banner image
     */
    public int getBannerImage() {
        return bannerImage;
    }

    /**
     * Sets banner image.
     *
     * @param bannerImage the banner image
     */
    public void setBannerImage(int bannerImage) {
        this.bannerImage = bannerImage;
    }
}
