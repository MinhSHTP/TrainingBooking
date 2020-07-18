package com.shtptraining.trainingbooking.ui;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI;
import com.shtptraining.trainingbooking.Commons.Helpers;
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
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _resources = getResources();
        _tbl_layout_status_color_course = (TableLayout) findViewById(R.id.tbl_layout_status_color_course);
//        _tbl_layout_status_color_course.setColumnStretchable(0,true);
//        _tbl_layout_status_color_course.setColumnStretchable(1,true);
        loadDataStatusColorCourse();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_status_color_course_act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_Save:
                Helpers.showToast(EditStatusColorCourseAct.this, "Saved", 0);
                break;
            case R.id.btn_Undo:
                loadDataStatusColorCourse();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDataStatusColorCourse() {

        _callWebAPI.getAllStatusColorCourses().enqueue(new Callback<List<StatusColorCourse>>() {
            @Override
            public void onResponse(Call<List<StatusColorCourse>> call, Response<List<StatusColorCourse>> response) {
                _statusColorCourses = response.body();
                if (_tbl_layout_status_color_course.getChildCount() > 0) {
                    _tbl_layout_status_color_course.removeAllViewsInLayout();
                }
                for (int i = 0; i < _statusColorCourses.size(); i++) {
                    TableRow tr = new TableRow(EditStatusColorCourseAct.this);
                    tr.setPadding(4, 4, 4, 4);

                    EditText editText = new EditText(EditStatusColorCourseAct.this);
                    int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, _resources.getDisplayMetrics());
                    int width = _resources.getDisplayMetrics().widthPixels * 3 / 4;
                    editText.setLayoutParams(new TableRow.LayoutParams(width, height));
                    editText.setTag("et_edit_color_" + i);
                    editText.setText(response.body().get(i).getName());
                    editText.setTextAppearance(EditStatusColorCourseAct.this, R.style.row_tbl_status_color_course);
                    editText.setGravity(Gravity.CENTER);
                    tr.addView(editText);

                    ImageView imageView = new ImageView(new ContextThemeWrapper(getApplicationContext(), R.style.row_tbl_status_color_course), null, 0);
                    imageView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    imageView.setBackgroundColor(Color.parseColor(_statusColorCourses.get(i).getColorString()));
                    imageView.setTag(_statusColorCourses.get(i).getColorString());
                    tr.addView(imageView);
                    _tbl_layout_status_color_course.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }
            }

            @Override
            public void onFailure(Call<List<StatusColorCourse>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }


}
