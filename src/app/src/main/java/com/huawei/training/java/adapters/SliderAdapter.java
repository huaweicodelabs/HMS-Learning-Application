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

package com.huawei.training.java.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.huawei.training.R;
import com.huawei.training.java.listeners.BannerItemClick;
import com.huawei.training.java.models.BannerModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class SliderAdapter extends PagerAdapter {
    /**
     * The Item click.
     */
    BannerItemClick itemClick;
    /**
     * The Banner model list.
     */
    private List<BannerModel> bannerModelList;
    private Context context;
    /**
     * Instantiates a new Slider adapter.
     *
     * @param context         the context
     * @param bannerModelList the banner model list
     * @param itemClick       the item click
     */
    public SliderAdapter(Context context, List<BannerModel> bannerModelList, BannerItemClick itemClick) {
        this.context = context;
        this.bannerModelList = bannerModelList;
        this.itemClick = itemClick;
    }

    @Override
    public int getCount() {
        return bannerModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return view == object;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.item_slider, null);
        BannerModel model = bannerModelList.get(position);
        ImageView imageView = view.findViewById(R.id.imageview);
        imageView.setBackgroundResource(model.getBannerImage());
        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);
        imageView.setOnClickListener(start -> itemClick.bannerClick(model));
        return view;
    }

    @Override
    public void destroyItem(@NotNull ViewGroup container, int position, @NotNull Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}
