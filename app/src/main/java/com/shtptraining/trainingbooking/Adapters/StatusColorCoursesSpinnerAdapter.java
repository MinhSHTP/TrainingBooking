package com.shtptraining.trainingbooking.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shtptraining.trainingbooking.Models.StatusColorCourse;
import com.shtptraining.trainingbooking.R;

import java.util.List;

public class StatusColorCoursesSpinnerAdapter extends BaseAdapter {
    List<StatusColorCourse> _statusColorCourses;
    Context _context;
    LayoutInflater _inflater;

    public StatusColorCoursesSpinnerAdapter(Context context, List<StatusColorCourse> statusColorCourses) {
        _context = context;
        _statusColorCourses = statusColorCourses;
        _inflater = (LayoutInflater.from(context));
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = _inflater.inflate(R.layout.item_spinner_status_color, null);
        }
        ImageView iv = convertView.findViewById(R.id.iv_status_course);
        TextView tv = convertView.findViewById(R.id.tv_status_course);

        if (i > 0) {
            iv.setBackgroundColor(Color.parseColor(_statusColorCourses.get(i).getColorString()));
        }
        tv.setText(_statusColorCourses.get(i).getName());
        return convertView;

    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.iv.setBackgroundColor(Color.parseColor(_statusColorCourses.get(position).getColorString()));
//        holder.tv.setText(_statusColorCourses.get(position).getName());
//    }

    @Override
    public int getCount() {
        return _statusColorCourses.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);

        }
    }
}
