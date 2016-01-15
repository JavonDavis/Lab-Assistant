package com.github.javon.labassistant.classes.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.fragments.CourseFragment;
import com.javon.parserecyclerviewadapter.annotations.Layout;
import com.javon.parserecyclerviewadapter.annotations.ParseName;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 13/01/16.
 */
@Layout(R.layout.course_list_item)
public class CourseViewHolder extends RecyclerView.ViewHolder
{


    public CourseViewHolder(View itemView) {
        super(itemView);
        courseTitle = (TextView) itemView.findViewById(R.id.course_title);
    }

    @ParseName("title")
    public TextView courseTitle;
}
