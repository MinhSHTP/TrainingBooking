package com.shtptraining.trainingbooking.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI;
import com.shtptraining.trainingbooking.Commons.Constants;
import com.shtptraining.trainingbooking.Commons.Helpers;
import com.shtptraining.trainingbooking.Models.StatusColorCourse;
import com.shtptraining.trainingbooking.R;

import java.util.ArrayList;
import java.util.List;

import static com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI.retrofit;

public class DetailCourseAct extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private String TAG = "DetailCourseAct";
    public static CallWebAPI _callWebAPI = retrofit.create(CallWebAPI.class);

    private String _Role = Constants.ACCOUNT_LOGIN.getRole();

    public EditText _et_name_course, _et_duration_date_course,
            _et_duration_course, _et_duration_time_course,
            _et_fee_course, _et_numberOf_course;

    public Spinner _spinner_course_trainer, _spinner_status_course;

    public TimePickerDialog _timePickerDialog;

    public DatePickerDialog _datePickerDialog;

    private Dialog _chose_date_dialog;

    private int _lastSelectedYear;
    private int _lastSelectedMonth;
    private int _lastSelectedDayOfMonth;

    private int _lastSelectedHourStart;
    private int _lastSelectedMinutesStart;

    private int _lastSelectedHourEnd;
    private int _lastSelectedMinutesEnd;

    private int _selectedStatusCode = 0;
    private String _selectedTrainerName = "";
    private String _selectedTrainerEmail = "";

    List<StatusColorCourse> _statusColorCourses = new ArrayList<>();

    List<String> _selectedDates = new ArrayList<>();

    public Button _btn_start_time_course, _btn_end_time_course, _btn_date_course, _btn_start_date_course, _btn_confirm, _btn_back;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_course);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        _et_name_course = findViewById(R.id.et_name_course);
        _et_duration_date_course = findViewById(R.id.et_duration_date_course);
        _et_duration_course = findViewById(R.id.et_duration_course);
        _et_duration_time_course = findViewById(R.id.et_duration_time_course);
        _et_fee_course = findViewById(R.id.et_fee_course);
        _et_numberOf_course = findViewById(R.id.et_numberOf_course);

        _spinner_course_trainer = findViewById(R.id.spinner_course_trainer);
        _spinner_status_course = findViewById(R.id.spinner_status_course);

        _btn_start_time_course = findViewById(R.id.btn_start_time_course);
        _btn_end_time_course = findViewById(R.id.btn_end_time_course);
        _btn_date_course = findViewById(R.id.btn_date_course);
        _btn_start_date_course = findViewById(R.id.btn_start_date_course);
        _btn_confirm = findViewById(R.id.btn_confirm);
        _btn_back = findViewById(R.id.btn_back);

        _btn_start_date_course.setOnClickListener(this);
        _btn_start_time_course.setOnClickListener(this);
        _btn_end_time_course.setOnClickListener(this);
        _btn_date_course.setOnClickListener(this);
        _btn_confirm.setOnClickListener(this);
        _btn_back.setOnClickListener(this);

        _et_duration_date_course.setOnFocusChangeListener(this);
        _et_duration_time_course.setOnFocusChangeListener(this);
        _et_duration_course.setOnFocusChangeListener(this);
        _et_fee_course.setOnFocusChangeListener(this);

        if (!_Role.equals("0")) {
            //is not Admin
            _btn_confirm.setVisibility(View.INVISIBLE);
            _et_name_course.setEnabled(false);
            _et_duration_date_course.setEnabled(false);
            _et_duration_course.setEnabled(false);
            _et_duration_time_course.setEnabled(false);
            _et_fee_course.setEnabled(false);
            _et_numberOf_course.setEnabled(false);

            _spinner_course_trainer.setEnabled(false);
            _spinner_status_course.setEnabled(false);

            _btn_start_time_course.setEnabled(false);
            _btn_end_time_course.setEnabled(false);
            _btn_date_course.setEnabled(false);
            _btn_start_date_course.setEnabled(false);
        }
    }

    private boolean validateForm() {
        if (_et_name_course.getText().toString().isEmpty()) {
            Helpers.showToast(DetailCourseAct.this, "Vui lòng nhập tên khóa học / môn học", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_duration_date_course.getText().toString().isEmpty()) {
            Helpers.showToast(DetailCourseAct.this, "Vui lòng nhập Thời gian học", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_duration_course.getText().toString().isEmpty()) {
            Helpers.showToast(DetailCourseAct.this, "Vui lòng nhập Số buổi học", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_duration_time_course.getText().toString().isEmpty()) {
            Helpers.showToast(DetailCourseAct.this, "Vui lòng nhập Số giờ học", Toast.LENGTH_SHORT);
            return false;
        }

        if (_btn_start_time_course.getHint().toString().equals(getString(R.string.btn_chose_time_course))) {
            Helpers.showToast(DetailCourseAct.this, "Vui lòng nhập Giờ bắt đầu", Toast.LENGTH_SHORT);
            return false;
        }

        if (_btn_end_time_course.getHint().toString().equals(getString(R.string.btn_chose_time_course))) {
            Helpers.showToast(DetailCourseAct.this, "Vui lòng nhập Giờ kết thúc", Toast.LENGTH_SHORT);
            return false;
        }

        if (_btn_date_course.getHint().toString().equals(getString(R.string.btn_chose_date_course))) {
            Helpers.showToast(DetailCourseAct.this, "Vui lòng nhập Ngày học trong tuần", Toast.LENGTH_SHORT);
            return false;
        }

        if (_btn_start_date_course.getHint().toString().equals(getString(R.string.btn_chose_start_date_course))) {
            Helpers.showToast(DetailCourseAct.this, "Vui lòng nhập Ngày khai giảng", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_fee_course.getText().toString().isEmpty()) {
            Helpers.showToast(DetailCourseAct.this, "Vui lòng nhập Học phí", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_numberOf_course.getText().toString().isEmpty()) {
            Helpers.showToast(DetailCourseAct.this, "Vui lòng nhập Số lượng tuyển sinh", Toast.LENGTH_SHORT);
            return false;
        }

        if (_selectedTrainerName.isEmpty()) {
            Helpers.showToast(DetailCourseAct.this, "Vui lòng chọn Giảng viên", Toast.LENGTH_SHORT);
            return false;
        }

        if (_selectedStatusCode == 0) {
            Helpers.showToast(DetailCourseAct.this, "Vui lòng chọn Trạng thái môn học", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }
}
