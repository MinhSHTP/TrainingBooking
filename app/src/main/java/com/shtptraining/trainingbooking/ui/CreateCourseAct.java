package com.shtptraining.trainingbooking.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shtptraining.trainingbooking.R;

public class CreateCourseAct extends AppCompatActivity {
    private String TAG = "CreateCourseAct";

    public EditText _et_name_course, _et_duration_date_course,
            _et_duration_course, _et_duration_time_course,
            _et_fee_course, _et_numberOf_course;

    public Spinner _spinner_course_trainer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        _et_name_course = (EditText) findViewById(R.id.et_name_course);
        _et_duration_date_course = (EditText) findViewById(R.id.et_duration_date_course);
        _et_duration_course = (EditText) findViewById(R.id.et_duration_course);
        _et_duration_time_course = (EditText) findViewById(R.id.et_duration_time_course);
        _et_fee_course = (EditText) findViewById(R.id.et_fee_course);
        _et_numberOf_course = (EditText) findViewById(R.id.et_numberOf_course);

        _spinner_course_trainer = (Spinner) findViewById(R.id.spinner_course_trainer);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
