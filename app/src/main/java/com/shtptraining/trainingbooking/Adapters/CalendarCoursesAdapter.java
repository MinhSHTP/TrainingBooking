package com.shtptraining.trainingbooking.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shtptraining.trainingbooking.Interfaces.RecyclerViewClickListener;
import com.shtptraining.trainingbooking.Models.Course;
import com.shtptraining.trainingbooking.R;
import com.shtptraining.trainingbooking.ui.DetailCourseAct;

import java.io.Serializable;
import java.util.List;

public class CalendarCoursesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Course> _courses;
    Context _context;
    private static RecyclerViewClickListener _itemListener;
    public CalendarCoursesAdapter(Context context, List<Course> courses) {
        _context = context;
        _courses = courses;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(_context).inflate(R.layout.item_calendar_courses, null);
        return new ViewHolderCalendarCourses(itemView);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderCalendarCourses viewHolderCalendarCourses = (ViewHolderCalendarCourses) holder;
        viewHolderCalendarCourses.tv_name_course.setText(_courses.get(position).getName());
        viewHolderCalendarCourses.tv_time_course.setText(_courses.get(position).getTime());
        viewHolderCalendarCourses.iv_status_color.setBackgroundColor(Color.parseColor(_courses.get(position).getStatusColor()));
        viewHolderCalendarCourses.tv_see_detail_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailCourse = new Intent(_context, DetailCourseAct.class);
                detailCourse.putExtra("SELECTED_COURSE", (Serializable) _courses.get(position));
                _context.startActivity(detailCourse);
            }
        });
    }

    @Override
    public int getItemCount() {
        return _courses.size();
    }

    public class ViewHolderCalendarCourses extends RecyclerView.ViewHolder {
        TextView tv_name_course, tv_time_course, tv_see_detail_course;
        ImageView iv_status_color;

        public ViewHolderCalendarCourses(View itemView) {
            super(itemView);
            tv_name_course = itemView.findViewById(R.id.tv_name_course);
            tv_time_course = itemView.findViewById(R.id.tv_time_course);
            iv_status_color = itemView.findViewById(R.id.iv_status_color);
            tv_see_detail_course = itemView.findViewById(R.id.tv_see_detail_course);
        }
    }
}

