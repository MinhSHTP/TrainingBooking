package com.shtptraining.trainingbooking.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shtptraining.trainingbooking.R;

import java.util.List;

public class HorizontalCalendarCoursesAdapter extends RecyclerView.Adapter<HorizontalCalendarCoursesAdapter.ViewHolder> {
    List<String> _dayOfWeek;
    Context _context;

    public HorizontalCalendarCoursesAdapter(Context context, List<String> dayOfWeek) {
        _context = context;
        _dayOfWeek = dayOfWeek;

    }


    @NonNull
    @Override
    public HorizontalCalendarCoursesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewDayOfWeek = LayoutInflater.from(_context).inflate(R.layout.item_horizontal_calendar_courses, null);
        return new ViewHolder(viewDayOfWeek);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalCalendarCoursesAdapter.ViewHolder holder, int position) {
        holder.tv_day_of_week.setText(_dayOfWeek.get(position));
        holder.tv_date_of_week.setText(_dayOfWeek.get(position));
    }

    @Override
    public int getItemCount() {
        return _dayOfWeek.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_day_of_week, tv_date_of_week;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_day_of_week = itemView.findViewById(R.id.tv_day_of_week);
            tv_date_of_week = itemView.findViewById(R.id.tv_date_of_week);
        }
    }
}
