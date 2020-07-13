package com.shtptraining.trainingbooking.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shtptraining.trainingbooking.Models.StatusColorCourse;
import com.shtptraining.trainingbooking.R;

import java.util.List;

public class StatusColorCoursesGridViewAdapter extends RecyclerView.Adapter<StatusColorCoursesGridViewAdapter.ViewHolder> {
    List<StatusColorCourse> _statusColorCourses;
    Context _context;

    public StatusColorCoursesGridViewAdapter(Context context, List<StatusColorCourse> statusColorCourses) {
        _context = context;
        _statusColorCourses = statusColorCourses;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(_context).inflate(R.layout.item_status_color_courses, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv.setText(_statusColorCourses.get(position).getName());
        holder.tv.setTextColor(Color.parseColor(_statusColorCourses.get(position).getColorString()));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return _statusColorCourses.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_status_name);
        }
    }
}
