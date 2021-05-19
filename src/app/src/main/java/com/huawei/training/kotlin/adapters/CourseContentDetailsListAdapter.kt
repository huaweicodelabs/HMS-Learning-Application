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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.huawei.training.R
import com.huawei.training.kotlin.activities.CourseContentDetailsActivity
import com.huawei.training.kotlin.listeners.CourseContentItemClick
import com.huawei.training.kotlin.models.CourseContentDataModel
import java.util.*

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class CourseContentDetailsListAdapter(listdata: List<*>?,
                                      CourseContentItemClick: CourseContentItemClick) : RecyclerView.Adapter<CourseContentDetailsListAdapter.ViewHolder>() {
    /**
     * The Course content item click.
     */
    var courseContentItemClick: CourseContentItemClick
    private val listdata: MutableList<CourseContentDataModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val coursesListItem = layoutInflater.inflate(
                R.layout.courses_content_item, parent, false)
        return ViewHolder(coursesListItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val courseContentDataModel = listdata[position]
        holder.courseName.text = courseContentDataModel.courseName
        holder.cardView.setOnClickListener { start: View? ->
            courseContentItemClick
                    .courseOnClick(listdata[position])
        }
        holder.cardView.visibility = View.VISIBLE
        if (listdata[position].courseName!!.length < 1) {
            holder.cardView.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return listdata.size
    }

    /**
     * The type View holder.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * The Course name.
         */
        val courseName: TextView

        /**
         * The Card view.
         */
        val cardView: CardView

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        init {
            courseName = itemView.findViewById(R.id.coursename)
            cardView = itemView.findViewById(R.id.cardview)
            val arrow = itemView.findViewById<ImageView>(R.id.course_ra)
            if (itemView.context is CourseContentDetailsActivity) {
                arrow.visibility = View.GONE
            }
        }
    }

    /**
     * Instantiates a new Course content details list adapter.
     *
     * @param listdata               the listdata
     * @param CourseContentItemClick the course content item click
     */
    init {
        this.listdata = ArrayList()
        this.listdata.addAll(listdata as List<CourseContentDataModel>)
        courseContentItemClick = CourseContentItemClick
    }
}