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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.training.R;
import com.huawei.training.java.listeners.CourseItemClick;
import com.huawei.training.java.models.MainCoursesModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class MainCourseListAdapter extends RecyclerView.Adapter<MainCourseListAdapter.ViewHolder> {
    /**
     * The Course item click.
     */
    CourseItemClick courseItemClick;
    private List<MainCoursesModel> listdata;
    /**
     * The Context.
     */
    Context context;
    /**
     * Instantiates a new Main course list adapter.
     *
     * @param listdata        the listdata
     * @param courseItemClick the course item click
     * @param context         the context
     */
    public MainCourseListAdapter(List<MainCoursesModel> listdata, CourseItemClick courseItemClick, Context context) {
        this.listdata = listdata;
        this.courseItemClick = courseItemClick;
        this.context = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.maincourses_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MainCoursesModel myListData = listdata.get(position);
        holder.heading.setText(myListData.getName());
        holder.itemslist.setLayoutManager(new GridLayoutManager(context, 2));
        holder.itemslist.setAdapter(new CourseListAdapter(myListData.getCourseDataModels(), courseItemClick));
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    /**
     * The type View holder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * The Itemslist.
         */
        private RecyclerView itemslist;
        /**
         * The Heading.
         */
        private TextView heading;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            this.itemslist = itemView.findViewById(R.id.itemslist);
            this.heading = itemView.findViewById(R.id.heading);
        }
    }
}
