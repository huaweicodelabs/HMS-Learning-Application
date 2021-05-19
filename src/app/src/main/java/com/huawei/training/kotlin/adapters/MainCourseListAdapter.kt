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

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.huawei.training.R
import com.huawei.training.kotlin.listeners.CourseItemClick
import com.huawei.training.kotlin.models.MainCoursesModel

/**
 * @since 2020
 * @author Huawei DTSE India
 */
class MainCourseListAdapter
/**
 * Instantiates a new Main course list adapter.
 *
 * @param listdata        the listdata
 * @param courseItemClick the course item click
 * @param context         the context
 */(private val listdata: MutableList<MainCoursesModel?>,
    /**
     * The Course item click.
     */
    var courseItemClick: CourseItemClick,
    /**
     * The Context.
     */
    var context: Context) : RecyclerView.Adapter<MainCourseListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.maincourses_list_item, parent, false)
        return ViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myListData = listdata[position]
        holder.heading.text = myListData?.name
        holder.itemslist.layoutManager = GridLayoutManager(context, 2)
        holder.itemslist.adapter = myListData?.courseDataModels?.let { CourseListAdapter(it, courseItemClick) }
    }

    override fun getItemCount(): Int {
        return listdata.size
    }

    /**
     * The type View holder.
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * The Itemslist.
         */
        val itemslist: RecyclerView

        /**
         * The Heading.
         */
        val heading: TextView

        /**
         * Instantiates a new View holder.
         *
         * @param itemView the item view
         */
        init {
            itemslist = itemView.findViewById(R.id.itemslist)
            heading = itemView.findViewById(R.id.heading)
        }
    }

}