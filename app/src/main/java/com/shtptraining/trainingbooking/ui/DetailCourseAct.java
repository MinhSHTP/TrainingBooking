package com.shtptraining.trainingbooking.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import androidx.appcompat.app.AppCompatActivity;

import com.shtptraining.trainingbooking.Adapters.StatusColorCoursesSpinnerAdapter;
import com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI;
import com.shtptraining.trainingbooking.Commons.Constants;
import com.shtptraining.trainingbooking.Commons.Helpers;
import com.shtptraining.trainingbooking.Models.Account;
import com.shtptraining.trainingbooking.Models.Course;
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

    private Course _selectedCourse = null;

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

        Bundle bundle = getIntent().getExtras();

        _selectedCourse = (Course) bundle.getSerializable("SELECTED_COURSE");

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

        loadDataStatusColorCourse();

        loadDataTrainer();

        mappingData();

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

    private void loadDataStatusColorCourse() {
        _callWebAPI.getAllStatusColorCourses().enqueue(new Callback<List<StatusColorCourse>>() {
            @Override
            public void onResponse(Call<List<StatusColorCourse>> call, Response<List<StatusColorCourse>> response) {
                _statusColorCourses = response.body();
                StatusColorCourse statusColorCourseNull = new StatusColorCourse();
                statusColorCourseNull.setName("Chọn trạng thái");
                _statusColorCourses.add(0, statusColorCourseNull);
                StatusColorCoursesSpinnerAdapter spinnerAdapter = new StatusColorCoursesSpinnerAdapter(DetailCourseAct.this, _statusColorCourses);
                _spinner_status_course.setAdapter(spinnerAdapter);

                for (int i = 1; i < _statusColorCourses.size(); i++) {

                    if (Integer.parseInt(_statusColorCourses.get(i).getCode()) == Integer.parseInt(_selectedCourse.getStatus())) {
                        _spinner_status_course.setSelection(i);
                        _selectedStatusCode = Integer.parseInt(_statusColorCourses.get(i).getCode());
                        break;
                    }
                }

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
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(DetailCourseAct.this, R.layout.simple_spinner_item, trainerNames);
                spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                _spinner_course_trainer.setAdapter(spinnerAdapter);

                for (int i = 0; i < trainerNames.size(); i++) {
                    if (trainerNames.get(i).equals(_selectedCourse.getTrainer())) {
                        _spinner_course_trainer.setSelection(i);
                        _selectedTrainerName = trainerNames.get(i);
                        break;
                    }
                }

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

    private void mappingData() {

        _et_name_course.setText(_selectedCourse.getName());
        _et_duration_date_course.setText(_selectedCourse.getDurationDate());
        _et_duration_course.setText(_selectedCourse.getDuration());
        _et_duration_time_course.setText(_selectedCourse.getDurationTime());
        _et_fee_course.setText(_selectedCourse.getFee());
        _et_numberOf_course.setText(_selectedCourse.getNumberOf());

        String startTime = _selectedCourse.getTime().split(" - ")[0];
        String endTime = _selectedCourse.getTime().split(" - ")[1];
        _btn_start_time_course.setHint(startTime);
        _btn_end_time_course.setHint(endTime);
        _btn_date_course.setHint(_selectedCourse.getDate());
        _btn_start_date_course.setHint(Helpers.toDDMMYYY(_selectedCourse.getStartDate()));

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
        final Calendar cldr = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.btn_start_time_course:
                _lastSelectedHourStart = cldr.get(Calendar.HOUR_OF_DAY);
                _lastSelectedMinutesStart = cldr.get(Calendar.MINUTE);
                _timePickerDialog = new TimePickerDialog(DetailCourseAct.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
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
                _timePickerDialog = new TimePickerDialog(DetailCourseAct.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, new TimePickerDialog.OnTimeSetListener() {
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
            case R.id.btn_back:
                finish();
                break;

        }
    }

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
                            Helpers.showToast(DetailCourseAct.this, "Số giờ học không quá 24 tiếng / buổi", Toast.LENGTH_SHORT);
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
                            Helpers.showToast(DetailCourseAct.this, "Số buổi không được quá 7 buổi / tuần", Toast.LENGTH_SHORT);
                            _et_duration_course.setText("");
                        } else {
                            _et_duration_course.setText("0" + _et_duration_course.getText().toString() + " buổi / tuần");
                        }
                    } else if (_et_duration_course.getText().toString().length() > 1 && Integer.parseInt(_et_duration_course.getText().toString()) >= 1) {
                        if (Integer.parseInt(_et_duration_course.getText().toString()) > 7) {
                            Helpers.showToast(DetailCourseAct.this, "Số buổi không được quá 7 buổi / tuần", Toast.LENGTH_SHORT);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
