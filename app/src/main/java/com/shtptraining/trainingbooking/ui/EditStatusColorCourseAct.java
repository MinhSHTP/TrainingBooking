package com.shtptraining.trainingbooking.ui;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI;
import com.shtptraining.trainingbooking.Models.StatusColorCourse;
import com.shtptraining.trainingbooking.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI.retrofit;

public class EditStatusColorCourseAct extends AppCompatActivity {
    private String TAG = "EditStatusColorCourseAct";
    public static CallWebAPI _callWebAPI = retrofit.create(CallWebAPI.class);
    List<StatusColorCourse> _statusColorCourses = new ArrayList<StatusColorCourse>();
    TableLayout _tbl_layout_status_color_course;
    Resources _resources;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status_color_course);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        _resources = getResources();
        _tbl_layout_status_color_course = (TableLayout) findViewById(R.id.tbl_layout_status_color_course);
        _callWebAPI.getAllStatusColorCourses().enqueue(new Callback<List<StatusColorCourse>>() {
            @Override
            public void onResponse(Call<List<StatusColorCourse>> call, Response<List<StatusColorCourse>> response) {
                _statusColorCourses = response.body();
                for (int i = 0; i < _statusColorCourses.size(); i++) {
                    TableRow tr = new TableRow(getApplicationContext());

                    EditText editText = new EditText(new ContextThemeWrapper(getApplicationContext(), R.style.row_tbl_status_color_course), null, 0);
                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, _resources.getDisplayMetrics());
                    editText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, height));
                    Drawable d = getResources().getDrawable(R.drawable.edit_txt_corners);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        editText.setBackground(d);
                    }
                    tr.addView(editText);

                    ImageView imageView = new ImageView(new ContextThemeWrapper(getApplicationContext(), R.style.row_tbl_status_color_course), null, 0);
                    imageView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    imageView.setBackgroundColor(Color.parseColor(_statusColorCourses.get(i).getColorString()));
                    tr.addView(imageView);

                    _tbl_layout_status_color_course.addView(tr);
                }
            }

            @Override
            public void onFailure(Call<List<StatusColorCourse>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }
}
