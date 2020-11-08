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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

public class CreateCourseAct extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private String TAG = "CreateCourseAct";
    public static CallWebAPI _callWebAPI = retrofit.create(CallWebAPI.class);
    public EditText _et_name_course, _et_duration_date_course,
            _et_duration_course, _et_duration_time_course,
            _et_fee_course, _et_numberOf_course;

    public Spinner _spinner_course_trainer;

    public Button _btn_start_time_course, _btn_end_time_course, _btn_date_course, _btn_start_date_course, _btn_confirm_create_course;

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

    private int _lastSelectedHourStart;
    private int _lastSelectedMinutesStart;

    private int _lastSelectedHourEnd;
    private int _lastSelectedMinutesEnd;

    private int _selectedStatusCode = 0;
    private String _selectedTrainerName = "";
    private String _selectedTrainerEmail = "";

    List<StatusColorCourse> _statusColorCourses = new ArrayList<>();

    List<String> _selectedDates = new ArrayList<>();

    //int MAX_SELECTED_DATES = 0;

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
        _btn_confirm_create_course = findViewById(R.id.btn_confirm_create_course);

//        _iv_status_course = (ImageView) findViewById(R.id.iv_status_course);
//        _tv_status_course = (TextView) findViewById(R.id.tv_status_course);

        _btn_start_date_course.setOnClickListener(this);
//        _iv_status_course.setOnClickListener(this);
        _btn_start_time_course.setOnClickListener(this);
        _btn_end_time_course.setOnClickListener(this);
        _btn_date_course.setOnClickListener(this);
        _btn_confirm_create_course.setOnClickListener(this);
//        _spinner_course_trainer.setOnItemSelectedListener(this);


//        _spinner_status_course.setOnItemSelectedListener(this);


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
                trainerNames.add("Chọn Giảng viên");
                for (int i = 0; i < accounts.size(); i++) {
                    if (accounts.get(i).getRole().equals("1")) {
                        trainerNames.add(accounts.get(i).getName());
                    }
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(CreateCourseAct.this, R.layout.simple_spinner_item, trainerNames);
                spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                _spinner_course_trainer.setAdapter(spinnerAdapter);
                _spinner_course_trainer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            _selectedTrainerName = _spinner_course_trainer.getSelectedItem().toString();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
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
                StatusColorCourse statusColorCourseNull = new StatusColorCourse();
                statusColorCourseNull.setName("Chọn trạng thái");
                _statusColorCourses.add(0, statusColorCourseNull);
                StatusColorCoursesSpinnerAdapter spinnerAdapter = new StatusColorCoursesSpinnerAdapter(CreateCourseAct.this, _statusColorCourses);
                _spinner_status_course.setAdapter(spinnerAdapter);
                _spinner_status_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position > 0) {
                            _selectedStatusCode = Integer.parseInt(_statusColorCourses.get(position).getCode());
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
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
            case R.id.btn_start_time_course:
                _lastSelectedHourStart = cldr.get(Calendar.HOUR_OF_DAY);
                _lastSelectedMinutesStart = cldr.get(Calendar.MINUTE);
                _timePickerDialog = new TimePickerDialog(CreateCourseAct.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hourOfDayString = String.valueOf(hourOfDay).length() == 1 ? "0" + hourOfDay : String.valueOf(hourOfDay);
                        String minuteString = String.valueOf(minute).length() == 1 ? "0" + minute : String.valueOf(minute);
                        String timeStartString = hourOfDayString + ":" + minuteString;
                        _btn_start_time_course.setHint(timeStartString);
                        _lastSelectedHourStart = hourOfDay;
                        _lastSelectedMinutesStart = minute;
                    }
                }, _lastSelectedHourStart, _lastSelectedMinutesStart, true);
                _timePickerDialog.show();

                break;
            case R.id.btn_end_time_course:
                _lastSelectedHourEnd = cldr.get(Calendar.HOUR_OF_DAY);
                _lastSelectedMinutesEnd = cldr.get(Calendar.MINUTE);
                _timePickerDialog = new TimePickerDialog(CreateCourseAct.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String hourOfDayString = String.valueOf(hourOfDay).length() == 1 ? "0" + hourOfDay : String.valueOf(hourOfDay);
                        String minuteString = String.valueOf(minute).length() == 1 ? "0" + minute : String.valueOf(minute);
                        String timeEndString = hourOfDayString + ":" + minuteString;
                        _btn_end_time_course.setHint(timeEndString);
                        _lastSelectedHourEnd = hourOfDay;
                        _lastSelectedMinutesEnd = minute;
                    }
                }, _lastSelectedHourEnd, _lastSelectedMinutesEnd, true);
                _timePickerDialog.show();

                break;
            case R.id.btn_date_course:
                _chose_date_dialog = new Dialog(this);
                _chose_date_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                _chose_date_dialog.setContentView(R.layout.custom_chose_date_dialog);

                Button btn_confirm_chose_date = _chose_date_dialog.findViewById(R.id.btn_confirm_chose_date);

                CheckBox chkbox_Monday = _chose_date_dialog.findViewById(R.id.chkbox_Monday);
                CheckBox chkbox_Tuesday = _chose_date_dialog.findViewById(R.id.chkbox_Tuesday);
                CheckBox chkbox_Wednesday = _chose_date_dialog.findViewById(R.id.chkbox_Wednesday);
                CheckBox chkbox_Thursday = _chose_date_dialog.findViewById(R.id.chkbox_Thursday);
                CheckBox chkbox_Friday = _chose_date_dialog.findViewById(R.id.chkbox_Friday);
                CheckBox chkbox_Saturday = _chose_date_dialog.findViewById(R.id.chkbox_Saturday);
                CheckBox chkbox_Sunday = _chose_date_dialog.findViewById(R.id.chkbox_Sunday);

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
                        _selectedDates.clear();
                        for (int i = 0; i < daysOfThatWeek.size(); i++) {
                            if (chkbox_days.get(daysOfThatWeek.get(i)).isChecked()) {
                                selected_days.append(daysOfThatWeek.get(i) + ", ");
                                _selectedDates.add(daysOfThatWeek.get(i));
                            }
                        }

                        if (!selected_days.toString().isEmpty()) {
                            if (selected_days.toString().endsWith(", ")) {
                                _btn_date_course.setHint(selected_days.toString().substring(0, selected_days.length() - 2));

                            } else {
                                _btn_date_course.setHint(selected_days.toString());
                            }
                        } else {
                            _btn_date_course.setHint(getString(R.string.btn_chose_date_course));
                        }

                        if (_selectedDates.size() > 0) {
                            _et_duration_course.setText("0" + _selectedDates.size() + " buổi / tuần");
                        } else {
                            _et_duration_course.setText("");
                        }
                        _chose_date_dialog.hide();
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
                androidx.appcompat.app.AlertDialog.Builder builder = Helpers.showAlertDialogConfirmInfor(this, "Bạn có chắc muốn tạo khóa học / môn học mới này?", "Tạo khóa học / môn học");
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (validateForm()) {
                            _callWebAPI.getAccountByName(_selectedTrainerName).enqueue(new Callback<List<Account>>() {
                                @Override
                                public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                                    _selectedTrainerEmail = response.body().get(0).getEmail();
                                }

                                @Override
                                public void onFailure(Call<List<Account>> call, Throwable t) {
                                    Log.e(TAG, t.getMessage());
                                }
                            });

                            String timeString = _btn_start_time_course.getHint() + " - " + _btn_end_time_course.getHint();

                            _callWebAPI.createCourse(
                                    _et_name_course.getText().toString(),
                                    _et_duration_date_course.getText().toString(),
                                    _et_duration_time_course.getText().toString(),
                                    _et_duration_course.getText().toString(),
                                    timeString,
                                    _btn_date_course.getHint().toString(),
                                    _btn_start_date_course.getHint().toString(),
                                    _selectedTrainerEmail,
                                    _et_fee_course.getText().toString(),
                                    String.valueOf(_selectedStatusCode),
                                    String.valueOf(Integer.parseInt(_et_numberOf_course.getText().toString()))
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
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
    }

    private boolean validateForm() {
        if (_et_name_course.getText().toString().isEmpty()) {
            Helpers.showToast(CreateCourseAct.this, "Vui lòng nhập tên khóa học / môn học", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_duration_date_course.getText().toString().isEmpty()) {
            Helpers.showToast(CreateCourseAct.this, "Vui lòng nhập Thời gian học", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_duration_course.getText().toString().isEmpty()) {
            Helpers.showToast(CreateCourseAct.this, "Vui lòng nhập Số buổi học", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_duration_time_course.getText().toString().isEmpty()) {
            Helpers.showToast(CreateCourseAct.this, "Vui lòng nhập Số giờ học", Toast.LENGTH_SHORT);
            return false;
        }

        if (_btn_start_time_course.getHint().toString().equals(getString(R.string.btn_chose_time_course))) {
            Helpers.showToast(CreateCourseAct.this, "Vui lòng nhập Giờ bắt đầu", Toast.LENGTH_SHORT);
            return false;
        }

        if (_btn_end_time_course.getHint().toString().equals(getString(R.string.btn_chose_time_course))) {
            Helpers.showToast(CreateCourseAct.this, "Vui lòng nhập Giờ kết thúc", Toast.LENGTH_SHORT);
            return false;
        }

        if (_btn_date_course.getHint().toString().equals(getString(R.string.btn_chose_date_course))) {
            Helpers.showToast(CreateCourseAct.this, "Vui lòng nhập Ngày học trong tuần", Toast.LENGTH_SHORT);
            return false;
        }

        if (_btn_start_date_course.getHint().toString().equals(getString(R.string.btn_chose_start_date_course))) {
            Helpers.showToast(CreateCourseAct.this, "Vui lòng nhập Ngày khai giảng", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_fee_course.getText().toString().isEmpty()) {
            Helpers.showToast(CreateCourseAct.this, "Vui lòng nhập Học phí", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_numberOf_course.getText().toString().isEmpty()) {
            Helpers.showToast(CreateCourseAct.this, "Vui lòng nhập Số lượng tuyển sinh", Toast.LENGTH_SHORT);
            return false;
        }

        if (_selectedTrainerName.isEmpty()) {
            Helpers.showToast(CreateCourseAct.this, "Vui lòng chọn Giảng viên", Toast.LENGTH_SHORT);
            return false;
        }

        if (_selectedStatusCode == 0) {
            Helpers.showToast(CreateCourseAct.this, "Vui lòng chọn Trạng thái môn học", Toast.LENGTH_SHORT);
            return false;
        }
        return true;
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        switch (view.getId()) {
//            case R.id.spinner_course_trainer:
//                _selectedTrainerName = _spinner_course_trainer.getSelectedItem().toString();
//                break;
//            case R.id.spinner_status_course:
//                _selectedStatusCode = Integer.parseInt(_statusColorCourses.get(position).getCode());
//                break;
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            //Focus out
            switch (v.getId()) {
                case R.id.et_duration_date_course:
                    if (_et_duration_date_course.getText().toString().length() == 1 && Integer.parseInt(_et_duration_date_course.getText().toString()) >= 1) {
                        _et_duration_date_course.setText("0" + _et_duration_date_course.getText().toString() + " tháng");
                    } else if (_et_duration_date_course.getText().toString().length() > 1 && Integer.parseInt(_et_duration_date_course.getText().toString()) >= 1) {
                        _et_duration_date_course.setText(_et_duration_date_course.getText().toString() + " tháng");
                    } else {
                        _et_duration_date_course.setText("");
                    }
                    break;
                case R.id.et_duration_time_course:
                    if (_et_duration_time_course.getText().toString().length() == 1 && Integer.parseInt(_et_duration_time_course.getText().toString()) >= 1) {
                        _et_duration_time_course.setText("0" + _et_duration_time_course.getText().toString() + " tiếng / buổi");
                    } else if (_et_duration_time_course.getText().toString().length() > 1 && Integer.parseInt(_et_duration_time_course.getText().toString()) >= 1) {
                        if (Integer.parseInt(_et_duration_time_course.getText().toString()) > 24) {
                            Helpers.showToast(CreateCourseAct.this, "Số giờ học không quá 24 tiếng / buổi", Toast.LENGTH_SHORT);
                            _et_duration_time_course.setText("");
                        } else {
                            _et_duration_time_course.setText(_et_duration_time_course.getText().toString() + " tiếng / buổi");
                        }
                    } else {
                        _et_duration_time_course.setText("");
                    }

                    break;
                case R.id.et_duration_course:
                    if (_et_duration_course.getText().toString().length() == 1 && Integer.parseInt(_et_duration_course.getText().toString()) >= 1) {
                        if (Integer.parseInt(_et_duration_course.getText().toString()) > 7) {
                            Helpers.showToast(CreateCourseAct.this, "Số buổi không được quá 7 buổi / tuần", Toast.LENGTH_SHORT);
                            _et_duration_course.setText("");
                        } else {
                            _et_duration_course.setText("0" + _et_duration_course.getText().toString() + " buổi / tuần");
                        }
                    } else if (_et_duration_course.getText().toString().length() > 1 && Integer.parseInt(_et_duration_course.getText().toString()) >= 1) {
                        if (Integer.parseInt(_et_duration_course.getText().toString()) > 7) {
                            Helpers.showToast(CreateCourseAct.this, "Số buổi không được quá 7 buổi / tuần", Toast.LENGTH_SHORT);
                            _et_duration_course.setText("");
                        } else {
                            _et_duration_course.setText(_et_duration_course.getText().toString() + " buổi / tuần");
                        }
                    } else {
                        _et_duration_course.setText("");
                    }

                    break;
                case R.id.et_fee_course:
                    DecimalFormat formatter = new DecimalFormat("###,###,###");
                    if (!_et_fee_course.getText().toString().isEmpty() && Integer.parseInt(_et_fee_course.getText().toString()) >= 1) {
                        String formattedNumber = formatter.format(Integer.parseInt(_et_fee_course.getText().toString()));
                        _et_fee_course.setText(formattedNumber + " VNĐ");
                    } else {
                        _et_fee_course.setText("");
                    }
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
                        _et_fee_course.setText(String.valueOf(Integer.parseInt(_et_fee_course.getText().toString().split(" VNĐ")[0].replace(".", ""))));
                    }
                    break;
            }
        }
    }
}
