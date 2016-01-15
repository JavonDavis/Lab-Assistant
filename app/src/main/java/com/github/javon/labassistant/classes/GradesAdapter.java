package com.github.javon.labassistant.classes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.javon.labassistant.R;
import com.github.javon.labassistant.classes.holders.GradesViewHolder;
import com.github.javon.labassistant.fragments.GradesDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Javon Davis
 *         Created by Javon Davis on 14/01/16.
 */
public class GradesAdapter extends RecyclerView.Adapter<GradesViewHolder> {

    private List<GradesDialogFragment.GradeItem> mItems = new ArrayList<>();

    public GradesAdapter(List<GradesDialogFragment.GradeItem> mItems) {
        this.mItems = mItems;
    }

    @Override
    public GradesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grades_list_item, parent, false);
        return new GradesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GradesViewHolder holder, int position) {
        GradesDialogFragment.GradeItem item = mItems.get(position);
        holder.labTitle.setText(item.title);
        holder.grade.setText(Integer.toString(item.grade));
        holder.markerView.setText(item.marker);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
