package com.github.javon.labassistant.classes.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.github.javon.labassistant.R;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 14/01/16.
 */
public class GradesViewHolder extends RecyclerView.ViewHolder {

    public GradesViewHolder(View itemView) {
        super(itemView);

        labTitle = (TextView) itemView.findViewById(R.id.assignment_title);
        grade = (TextView) itemView.findViewById(R.id.assignment_grade);
        markerView = (TextView) itemView.findViewById(R.id.assignment_marker);
    }

    public TextView labTitle;
    public TextView grade;
    public TextView markerView;
}
