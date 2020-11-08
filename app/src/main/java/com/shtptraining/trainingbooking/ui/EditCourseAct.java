package com.shtptraining.trainingbooking.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI;
import com.shtptraining.trainingbooking.Commons.Helpers;
import com.shtptraining.trainingbooking.Models.Course;
import com.shtptraining.trainingbooking.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI.retrofit;

public class EditCourseAct extends AppCompatActivity {
    private String TAG = "EditSCourseAct";

    List<Course> _courses = new ArrayList<Course>();
    public static CallWebAPI _callWebAPI = retrofit.create(CallWebAPI.class);
    FloatingActionButton _floatingActionButton;
    TableLayout _tbl_layout_course;
    Resources _resources;
    BottomNavigationView _bottomNavigationView;
    public SwipeRefreshLayout _swipRefreshLayout;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        _resources = getResources();
        _floatingActionButton = findViewById(R.id.btn_Add_Course);
        _floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createCourse = new Intent(EditCourseAct.this, CreateCourseAct.class);
                startActivity(createCourse);
                finish();
            }
        });
        _swipRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        _swipRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                try {
                    Helpers.showLoadingDialog(EditCourseAct.this, "Đang lấy dữ liệu...");
                    loadDataCourses();
                } catch (Exception ex) {
                    Log.d(TAG, ex.toString());
                } finally {
                    _swipRefreshLayout.setRefreshing(false);
                    Helpers.dismissLoadingDialog();
                }
            }
        });
        _tbl_layout_course = (TableLayout) findViewById(R.id.tbl_layout_courses);

        loadDataCourses();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadDataCoursesByID(int IdCourse) {
        _callWebAPI.getAllCoursesByID(IdCourse).enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                _courses = response.body();
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {

            }
        });
    }

    private void loadDataCourses() {
        _callWebAPI.getAllCourses().enqueue(new Callback<List<Course>>() {
            @Override
            public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                _courses = response.body();
                if (_tbl_layout_course.getChildCount() > 2) {
                    _tbl_layout_course.removeViews(2, _tbl_layout_course.getChildCount() - 1);
                }
                for (int i = 0; i < _courses.size(); i++) {
                    TableRow tr = new TableRow(EditCourseAct.this);
                    tr.setPadding(4, 4, 4, 4);
                    int finalI = i;
                    tr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //loadDataCoursesByID(_courses.get(finalI).getId());
                        }
                    });

                    TextView tvCourseName = new TextView(EditCourseAct.this);
                    tvCourseName.setText(_courses.get(i).getName());
                    tvCourseName.setGravity(Gravity.CENTER);
                    tr.addView(tvCourseName);

                    TextView tvTrainer = new TextView(EditCourseAct.this);
                    tvTrainer.setText(_courses.get(i).getTrainer());
                    tvTrainer.setGravity(Gravity.CENTER);
                    tr.addView(tvTrainer);

                    TextView tvNumberOf = new TextView(EditCourseAct.this);
                    tvNumberOf.setText(_courses.get(i).getNumberOf());
                    tvNumberOf.setGravity(Gravity.CENTER);
                    tr.addView(tvNumberOf);

                    TextView tvStartDate = new TextView(EditCourseAct.this);
                    tvStartDate.setText(Helpers.toDDMMYYY(_courses.get(i).getStartDate()));
                    tvStartDate.setGravity(Gravity.CENTER);
                    tr.addView(tvStartDate);

                    _tbl_layout_course.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                }
            }

            @Override
            public void onFailure(Call<List<Course>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
