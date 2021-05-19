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
package com.huawei.training.kotlin.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.huawei.training.R
import com.huawei.training.kotlin.listeners.BannerItemClick
import com.huawei.training.kotlin.models.BannerModel

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class SliderAdapter
/**
 * Instantiates a new Slider adapter.
 *
 * @param context         the context
 * @param bannerModelList the banner model list
 * @param itemClick       the item click
 */(private val context: Context,
    /**
     * The Banner model list.
     */
    private val bannerModelList: List<BannerModel>,
    /**
     * The Item click.
     */
    var itemClick: BannerItemClick) : PagerAdapter() {

    override fun getCount(): Int {
        return bannerModelList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        @SuppressLint("InflateParams") val view = inflater.inflate(R.layout.item_slider, null)
        val model = bannerModelList[position]
        val imageView = view.findViewById<ImageView>(R.id.imageview)
        imageView.setBackgroundResource(model.bannerImage)
        val viewPager = container as ViewPager
        viewPager.addView(view, 0)
        imageView.setOnClickListener { start: View? -> itemClick.bannerClick(model) }
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val viewPager = container as ViewPager
        val view = `object` as View
        viewPager.removeView(view)
    }

}