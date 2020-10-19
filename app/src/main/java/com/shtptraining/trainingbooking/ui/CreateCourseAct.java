package com.shtptraining.trainingbooking.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shtptraining.trainingbooking.R;

import java.util.Calendar;

public class CreateCourseAct extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "CreateCourseAct";

    public EditText _et_name_course, _et_duration_date_course,
            _et_duration_course, _et_duration_time_course,
            _et_fee_course, _et_numberOf_course;

    public Spinner _spinner_course_trainer;

    public Button _btn_time_course, _btn_date_course;

    public ImageView _datepicker_start_date_course, _iv_status_course;

    public TimePickerDialog _timePickerDialog;

    public DatePickerDialog _datePickerDialog;

    private int _lastSelectedYear;
    private int _lastSelectedMonth;
    private int _lastSelectedDayOfMonth;

    private int _lastSelectedHour;
    private int _lastSelectedMinutes;

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

        _btn_time_course = (Button) findViewById(R.id.btn_time_course);
        _btn_date_course = (Button) findViewById(R.id.btn_date_course);

        _datepicker_start_date_course = (ImageView) findViewById(R.id.datepicker_start_date_course);
        _iv_status_course = (ImageView) findViewById(R.id.iv_status_course);

        _datepicker_start_date_course.setOnClickListener(this);
        _iv_status_course.setOnClickListener(this);
        _btn_time_course.setOnClickListener(this);
        _btn_date_course.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        final Calendar cldr = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.btn_time_course:
                _lastSelectedHour = cldr.get(Calendar.HOUR_OF_DAY);
                _lastSelectedMinutes = cldr.get(Calendar.MINUTE);
                _timePickerDialog = new TimePickerDialog(CreateCourseAct.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        _btn_time_course.setText(hourOfDay + ":" + minute);
                        _lastSelectedHour = hourOfDay;
                        _lastSelectedMinutes = minute;
                    }
                }, _lastSelectedHour, _lastSelectedMinutes, true);
                _timePickerDialog.show();

                break;
            case R.id.btn_date_course:
                _lastSelectedDayOfMonth = cldr.get(Calendar.DAY_OF_MONTH);
                _lastSelectedMonth = cldr.get(Calendar.MONTH);
                _lastSelectedYear = cldr.get(Calendar.YEAR);
                // Date Select Listener.
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        String dayString = String.valueOf(dayOfMonth).length() == 1 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);

                        String monthString = String.valueOf(monthOfYear).length() == 1 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);

                        _btn_date_course.setText(dayString + "-" + monthString + "-" + year);

                        _lastSelectedYear = year;
                        _lastSelectedMonth = monthOfYear;
                        _lastSelectedDayOfMonth = dayOfMonth;
                    }
                };

                _datePickerDialog = new DatePickerDialog(this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        dateSetListener, _lastSelectedYear, _lastSelectedMonth, _lastSelectedDayOfMonth);

                // Show
                _datePickerDialog.show();
                break;
            case R.id.iv_status_course:
                break;
            case R.id.datepicker_start_date_course:
                break;
        }
    }
}
