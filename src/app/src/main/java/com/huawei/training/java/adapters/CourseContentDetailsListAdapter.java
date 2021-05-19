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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.training.R;
import com.huawei.training.java.activities.CourseContentDetailsActivity;
import com.huawei.training.java.listeners.CourseContentItemClick;
import com.huawei.training.java.models.CourseContentDataModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @since 2020
 * @author Huawei DTSE India
 */
public class CourseContentDetailsListAdapter extends
        RecyclerView.Adapter<CourseContentDetailsListAdapter.ViewHolder> {
    /**
     * The Course content item click.
     */
    CourseContentItemClick courseContentItemClick;
    private ArrayList<CourseContentDataModel> listdata;

    /**
     * Instantiates a new Course content details list adapter.
     *
     * @param listdata               the listdata
     * @param CourseContentItemClick the course content item click
     */
    public CourseContentDetailsListAdapter(List listdata,
                                           CourseContentItemClick CourseContentItemClick) {
        this.listdata = new ArrayList<>();
        this.listdata.addAll(listdata);
        this.courseContentItemClick = CourseContentItemClick;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View coursesListItem = layoutInflater.inflate(
                R.layout.courses_content_item, parent, false);
        return new ViewHolder(coursesListItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CourseContentDataModel courseContentDataModel = listdata.get(position);
        holder.courseName.setText(courseContentDataModel.getCourseName());
        holder.cardView.setOnClickListener(start -> courseContentItemClick
                .courseOnClick(listdata.get(position)));
        holder.cardView.setVisibility(View.VISIBLE);
        if (listdata.get(position).getCourseName().length() < 1) {
            holder.cardView.setVisibility(View.GONE);
        }
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
         * The Course name.
         */
        private TextView courseName;
        /**
         * The Card view.
         */
        private CardView cardView;

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        public ViewHolder(View itemView) {
            super(itemView);
            this.courseName = itemView.findViewById(R.id.coursename);
            this.cardView = itemView.findViewById(R.id.cardview);
            ImageView arrow = itemView.findViewById(R.id.course_ra);
            if (itemView.getContext() instanceof CourseContentDetailsActivity) {
                arrow.setVisibility(View.GONE);
            }
        }
    }
}
