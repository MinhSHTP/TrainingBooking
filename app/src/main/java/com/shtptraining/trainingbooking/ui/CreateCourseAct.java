package com.shtptraining.trainingbooking.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shtptraining.trainingbooking.Adapters.StatusColorCoursesSpinnerAdapter;
import com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI;
import com.shtptraining.trainingbooking.Commons.Helpers;
import com.shtptraining.trainingbooking.Models.Account;
import com.shtptraining.trainingbooking.Models.MessageFromAPI;
import com.shtptraining.trainingbooking.Models.StatusColorCourse;
import com.shtptraining.trainingbooking.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI.retrofit;

public class CreateCourseAct extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, View.OnFocusChangeListener {
    private String TAG = "CreateCourseAct";
    public static CallWebAPI _callWebAPI = retrofit.create(CallWebAPI.class);
    public EditText _et_name_course, _et_duration_date_course,
            _et_duration_course, _et_duration_time_course,
            _et_fee_course, _et_numberOf_course;

    public Spinner _spinner_course_trainer;

    public Button _btn_time_course, _btn_date_course, _btn_start_date_course, _btn_confirm_create_course;

//    public ImageView _iv_status_course;

//    public TextView _tv_status_course;

    public Spinner _spinner_status_course;

    public TimePickerDialog _timePickerDialog;

    public DatePickerDialog _datePickerDialog;

    private Dialog _chose_date_dialog;

    private int _lastSelectedYear;
    private int _lastSelectedMonth;
    private int _lastSelectedDayOfMonth;

    private int _lastSelectedDayOfWeek;

    private int _lastSelectedHour;
    private int _lastSelectedMinutes;

    List<StatusColorCourse> _statusColorCourses = new ArrayList<StatusColorCourse>();

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
        _spinner_status_course = (Spinner) findViewById(R.id.spinner_status_course);

        _btn_time_course = (Button) findViewById(R.id.btn_time_course);
        _btn_date_course = (Button) findViewById(R.id.btn_date_course);
        _btn_start_date_course = (Button) findViewById(R.id.btn_start_date_course);
        _btn_confirm_create_course = (Button) findViewById(R.id.btn_confirm_create_course);

//        _iv_status_course = (ImageView) findViewById(R.id.iv_status_course);
//        _tv_status_course = (TextView) findViewById(R.id.tv_status_course);

        _btn_start_date_course.setOnClickListener(this);
//        _iv_status_course.setOnClickListener(this);
        _btn_time_course.setOnClickListener(this);
        _btn_date_course.setOnClickListener(this);
        _btn_confirm_create_course.setOnClickListener(this);
        _spinner_status_course.setOnItemSelectedListener(this);

        _et_duration_date_course.setOnFocusChangeListener(this);
        _et_duration_time_course.setOnFocusChangeListener(this);
        _et_duration_course.setOnFocusChangeListener(this);
        _et_fee_course.setOnFocusChangeListener(this);

        loadDataStatusColorCourse();

        loadDataTrainer();
    }

    private void loadDataTrainer() {
        _callWebAPI.getAllAccounts().enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                List<Account> accounts = new ArrayList<>();
                accounts = response.body();

                ArrayList<String> trainerNames = new ArrayList<>();
                for (int i = 0; i < accounts.size(); i++) {
                    if (accounts.get(i).getRole().equals("1")) {
                        trainerNames.add(accounts.get(i).getName());
                    }
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(CreateCourseAct.this, R.layout.simple_spinner_item, trainerNames);
                spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                _spinner_course_trainer.setAdapter(spinnerAdapter);
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });


    }

    private void loadDataStatusColorCourse() {
        _callWebAPI.getAllStatusColorCourses().enqueue(new Callback<List<StatusColorCourse>>() {
            @Override
            public void onResponse(Call<List<StatusColorCourse>> call, Response<List<StatusColorCourse>> response) {
                _statusColorCourses = response.body();

                StatusColorCoursesSpinnerAdapter spinnerAdapter = new StatusColorCoursesSpinnerAdapter(CreateCourseAct.this, _statusColorCourses);
                _spinner_status_course.setAdapter(spinnerAdapter);

            }

            @Override
            public void onFailure(Call<List<StatusColorCourse>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
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
                        _btn_time_course.setHint(hourOfDay + ":" + minute);
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

                        _btn_start_date_course.setHint(dayString + "-" + monthString + "-" + year);

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
            case R.id.btn_confirm_create_course:
                androidx.appcompat.app.AlertDialog dialog = Helpers.showAlertDialogConfirmInfor(this, "Bạn có chắc muốn tạo khóa học / môn học mới này?", "Tạo khóa học / môn học");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Account accountTrainer = (Account) (_spinner_course_trainer.getSelectedItem());
                        StatusColorCourse statusCourse = (StatusColorCourse) (_spinner_status_course.getSelectedItem());
                        _callWebAPI.createCourse(
                                _et_name_course.getText().toString(),
                                _et_duration_date_course.getText().toString(),
                                _et_duration_time_course.getText().toString(),
                                _et_duration_course.getText().toString(),
                                _btn_time_course.getHint().toString(),
                                _btn_date_course.getHint().toString(),
                                _btn_start_date_course.getHint().toString(),
                                accountTrainer.getEmail(),
                                _et_fee_course.getText().toString(),
                                Integer.parseInt(statusCourse.getCode()),
                                Integer.parseInt(_et_numberOf_course.getText().toString())
                        ).enqueue(new Callback<MessageFromAPI>() {
                            @Override
                            public void onResponse(Call<MessageFromAPI> call, Response<MessageFromAPI> response) {
                                Log.e(TAG, String.valueOf(response.body()));
                            }

                            @Override
                            public void onFailure(Call<MessageFromAPI> call, Throwable t) {
                                Log.e(TAG, t.getMessage());
                            }
                        });
                    }
                });

                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), _statusColorCourses.get(position).getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            //Focus out
            switch (v.getId()) {
                case R.id.et_duration_date_course:
                    if (_et_duration_date_course.getText().toString().length() == 1 && Integer.parseInt(_et_duration_date_course.getText().toString()) >= 1) {
                        _et_duration_date_course.setText("0" + _et_duration_date_course.getText().toString() + " tháng");
                    } else {
                        _et_duration_date_course.setText(_et_duration_date_course.getText().toString() + " tháng");
                    }
                    break;
                case R.id.et_duration_time_course:
                    if (_et_duration_time_course.getText().toString().length() == 1 && Integer.parseInt(_et_duration_time_course.getText().toString()) >= 1) {
                        _et_duration_time_course.setText("0" + _et_duration_time_course.getText().toString() + " tiếng / buổi");
                    } else {
                        _et_duration_time_course.setText(_et_duration_time_course.getText().toString() + " tiếng / buổi");
                    }
                    break;
                case R.id.et_duration_course:
                    if (_et_duration_course.getText().toString().length() == 1 && Integer.parseInt(_et_duration_course.getText().toString()) >= 1) {
                        _et_duration_course.setText("0" + _et_duration_course.getText().toString() + " buổi / tuần");
                    } else {
                        _et_duration_course.setText(_et_duration_course.getText().toString() + " buổi / tuần");
                    }
                    break;
                case R.id.et_fee_course:
                    DecimalFormat formatter = new DecimalFormat("###,###,###");
                    String formattedNumber = formatter.format(Integer.parseInt(_et_fee_course.getText().toString()));
                    //if (!_et_fee_course.getText().toString().isEmpty() && Integer.parseInt(_et_fee_course.getText().toString()) >= 1) {
                    _et_fee_course.setText(formattedNumber + " VNĐ");
                    //} else {
                    //    _et_fee_course.setText(formattedNumber);
                    //}
                    break;
            }
        } else {
            //Focus in
            switch (v.getId()) {
                case R.id.et_duration_date_course:
                    if (!_et_duration_date_course.getText().toString().isEmpty()) {
                        _et_duration_date_course.setText(String.valueOf(Integer.parseInt(_et_duration_date_course.getText().toString().split(" tháng")[0])));
                    }
                    break;
                case R.id.et_duration_time_course:
                    if (!_et_duration_time_course.getText().toString().isEmpty()) {
                        _et_duration_time_course.setText(String.valueOf(Integer.parseInt(_et_duration_time_course.getText().toString().split(" tiếng / buổi")[0])));
                    }
                    break;
                case R.id.et_duration_course:
                    if (!_et_duration_course.getText().toString().isEmpty()) {
                        _et_duration_course.setText(String.valueOf(Integer.parseInt(_et_duration_course.getText().toString().split(" buổi / tuần")[0])));
                    }
                    break;
                case R.id.et_fee_course:
                    if (!_et_fee_course.getText().toString().isEmpty()) {
//                        DecimalFormat formatter = new DecimalFormat("###");
//                        String formattedNumber = formatter.format(Integer.parseInt(_et_fee_course.getText().toString().split(" VNĐ")[0]));
                        Log.d(TAG, String.valueOf(Integer.parseInt(_et_fee_course.getText().toString().split(" VNĐ")[0].replace(".", ""))));
                        _et_fee_course.setText(String.valueOf(Integer.parseInt(_et_fee_course.getText().toString().split(" VNĐ")[0].replace(".", ""))));
                    }
                    break;
            }
        }
    }
}
