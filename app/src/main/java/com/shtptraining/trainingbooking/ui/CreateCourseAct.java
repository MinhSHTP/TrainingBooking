package com.shtptraining.trainingbooking.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shtptraining.trainingbooking.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Hashtable;

public class CreateCourseAct extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "CreateCourseAct";

    public EditText _et_name_course, _et_duration_date_course,
            _et_duration_course, _et_duration_time_course,
            _et_fee_course, _et_numberOf_course;

    public Spinner _spinner_course_trainer;

    public Button _btn_time_course, _btn_date_course, _btn_start_date_course;

    public ImageView _iv_status_course;

    public TimePickerDialog _timePickerDialog;

    public DatePickerDialog _datePickerDialog;

    private Dialog _chose_date_dialog;

    private int _lastSelectedYear;
    private int _lastSelectedMonth;
    private int _lastSelectedDayOfMonth;

    private int _lastSelectedDayOfWeek;

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
        _btn_start_date_course = (Button) findViewById(R.id.btn_start_date_course);

        _iv_status_course = (ImageView) findViewById(R.id.iv_status_course);

        _btn_start_date_course.setOnClickListener(this);
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
                _chose_date_dialog = new Dialog(this);
                _chose_date_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                _chose_date_dialog.setContentView(R.layout.custom_chose_date_dialog);

                Button btn_confirm_chose_date = (Button) _chose_date_dialog.findViewById(R.id.btn_confirm_chose_date);

                CheckBox chkbox_Monday = (CheckBox) _chose_date_dialog.findViewById(R.id.chkbox_Monday);
                CheckBox chkbox_Tuesday = (CheckBox) _chose_date_dialog.findViewById(R.id.chkbox_Tuesday);
                CheckBox chkbox_Wednesday = (CheckBox) _chose_date_dialog.findViewById(R.id.chkbox_Wednesday);
                CheckBox chkbox_Thursday = (CheckBox) _chose_date_dialog.findViewById(R.id.chkbox_Thursday);
                CheckBox chkbox_Friday = (CheckBox) _chose_date_dialog.findViewById(R.id.chkbox_Friday);
                CheckBox chkbox_Saturday = (CheckBox) _chose_date_dialog.findViewById(R.id.chkbox_Saturday);
                CheckBox chkbox_Sunday = (CheckBox) _chose_date_dialog.findViewById(R.id.chkbox_Sunday);

                Dictionary<String, CheckBox> chkbox_days = new Hashtable<>();
                chkbox_days.put(getBaseContext().getString(R.string.chkbox_Monday), chkbox_Monday);
                chkbox_days.put(getBaseContext().getString(R.string.chkbox_Tuesday), chkbox_Tuesday);
                chkbox_days.put(getBaseContext().getString(R.string.chkbox_Wednesday), chkbox_Wednesday);
                chkbox_days.put(getBaseContext().getString(R.string.chkbox_Thursday), chkbox_Thursday);
                chkbox_days.put(getBaseContext().getString(R.string.chkbox_Friday), chkbox_Friday);
                chkbox_days.put(getBaseContext().getString(R.string.chkbox_Saturday), chkbox_Saturday);
                chkbox_days.put(getBaseContext().getString(R.string.chkbox_Sunday), chkbox_Sunday);

                ArrayList<String> daysOfThatWeek = new ArrayList<>();
                daysOfThatWeek.add(getString(R.string.chkbox_Monday));
                daysOfThatWeek.add(getString(R.string.chkbox_Tuesday));
                daysOfThatWeek.add(getString(R.string.chkbox_Wednesday));
                daysOfThatWeek.add(getString(R.string.chkbox_Thursday));
                daysOfThatWeek.add(getString(R.string.chkbox_Friday));
                daysOfThatWeek.add(getString(R.string.chkbox_Saturday));
                daysOfThatWeek.add(getString(R.string.chkbox_Sunday));

                String btnDateCourseHint = _btn_date_course.getHint().toString();
                String[] dayOfWeekArrayStrings = btnDateCourseHint == getString(R.string.btn_chose_date_course) ? null : btnDateCourseHint.split(", ");
                if (dayOfWeekArrayStrings != null) {
                    for (int i = 0; i < dayOfWeekArrayStrings.length; i++) {
                        if (daysOfThatWeek.contains(dayOfWeekArrayStrings[i])) {
                            chkbox_days.get(dayOfWeekArrayStrings[i]).setChecked(true);
                        }
                    }
                }

                btn_confirm_chose_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringBuilder selected_days = new StringBuilder();

                        for (int i = 0; i < daysOfThatWeek.size(); i++) {
                            if (chkbox_days.get(daysOfThatWeek.get(i)).isChecked()) {
                                selected_days.append(daysOfThatWeek.get(i) + ", ");
                            }
                        }

//                        selected_days.append(chkbox_Monday.isChecked() ? getBaseContext().getString(R.string.chkbox_Monday) + ", " : "");

//                        selected_days.append(chkbox_Tuesday.isChecked() ? getBaseContext().getString(R.string.chkbox_Tuesday) + ", " : "");

//                        selected_days.append(chkbox_Wednesday.isChecked() ? getBaseContext().getString(R.string.chkbox_Wednesday) + ", " : "");

//                        selected_days.append(chkbox_Thursday.isChecked() ? getBaseContext().getString(R.string.chkbox_Thursday) + ", " : "");

//                        selected_days.append(chkbox_Friday.isChecked() ? getBaseContext().getString(R.string.chkbox_Friday) + ", " : "");

//                        selected_days.append(chkbox_Saturday.isChecked() ? getBaseContext().getString(R.string.chkbox_Saturday) + ", " : "");

//                        selected_days.append(chkbox_Sunday.isChecked() ? getBaseContext().getString(R.string.chkbox_Sunday) : "");

                        if (!selected_days.toString().isEmpty()) {
                            if (selected_days.toString().endsWith(", ")) {
                                _btn_date_course.setHint(selected_days.toString().substring(0, selected_days.length() - 2));
                            } else {
                                _btn_date_course.setHint(selected_days.toString());
                            }
                            _chose_date_dialog.hide();
                        }
                    }
                });
                _chose_date_dialog.show();
                break;
            case R.id.iv_status_course:
                break;
            case R.id.btn_start_date_course:
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
        }
    }
}
