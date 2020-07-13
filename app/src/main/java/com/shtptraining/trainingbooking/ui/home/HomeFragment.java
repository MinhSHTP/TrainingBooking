package com.shtptraining.trainingbooking.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shtptraining.trainingbooking.Adapters.CalendarCoursesAdapter;
import com.shtptraining.trainingbooking.Adapters.StatusColorCoursesGridViewAdapter;
import com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI;
import com.shtptraining.trainingbooking.Commons.Helpers;
import com.shtptraining.trainingbooking.Models.Course;
import com.shtptraining.trainingbooking.Models.StatusColorCourse;
import com.shtptraining.trainingbooking.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI.retrofit;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private String TAG = "HomeFragment";

    private HomeViewModel homeViewModel;

    private RecyclerView _gv_status_courses, _gv_calendar_courses;

    private ImageView _iv_chose_date;
    public View _root;

    List<Course> _courses = new ArrayList<Course>();
    List<StatusColorCourse> _statusColorCourses = new ArrayList<StatusColorCourse>();

    TextView _tv_date_of_week_Mon, _tv_date_of_week_Tues, _tv_date_of_week_Wed, _tv_date_of_week_Thurs, _tv_date_of_week_Fri, _tv_date_of_week_Sat, _tv_date_of_week_Sun;

    public Calendar _myCalendar = Calendar.getInstance();

    public static CallWebAPI _callWebAPI = retrofit.create(CallWebAPI.class);

    public SwipeRefreshLayout _swipRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        _root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return _root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Helpers.showLoadingDialog(getContext(), "Đang lấy dữ liệu...");
            init();
            boolean resultLoadData = loadDataFromAPI();
            if (resultLoadData)
                Helpers.showToast(getContext(), "Load dữ liệu thành công", Toast.LENGTH_LONG);
            Helpers.showToast(getContext(), "Load dữ liệu thất bại", Toast.LENGTH_LONG);
        } catch (Exception ex) {
            Log.d(TAG, ex.toString());
            Helpers.showToast(getContext(), "Load dữ liệu thất bại", Toast.LENGTH_LONG);
        } finally {
            Helpers.dismissLoadingDialog();
        }
    }

    private boolean loadDataFromAPI() {
        try {
            loadStatusColorCourses(_callWebAPI);
            loadCalendarCourse(_callWebAPI, _myCalendar);
            return true;
        } catch (Exception ex) {
            Log.d(TAG, ex.toString());
            return false;
        }
    }

    private void init() {
        _tv_date_of_week_Mon = (TextView) _root.findViewById(R.id.tv_date_of_week_Mon);
        _tv_date_of_week_Tues = (TextView) _root.findViewById(R.id.tv_date_of_week_Tues);
        _tv_date_of_week_Wed = (TextView) _root.findViewById(R.id.tv_date_of_week_Wed);
        _tv_date_of_week_Thurs = (TextView) _root.findViewById(R.id.tv_date_of_week_Thurs);
        _tv_date_of_week_Fri = (TextView) _root.findViewById(R.id.tv_date_of_week_Fri);
        _tv_date_of_week_Sat = (TextView) _root.findViewById(R.id.tv_date_of_week_Sat);
        _tv_date_of_week_Sun = (TextView) _root.findViewById(R.id.tv_date_of_week_Sun);

        _iv_chose_date = (ImageView) _root.findViewById(R.id.iv_chose_date);
        _iv_chose_date.setOnClickListener(this);

        _swipRefreshLayout = (SwipeRefreshLayout) _root.findViewById(R.id.swipeRefresh);
        _swipRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");
                        try {
                            Helpers.showLoadingDialog(getContext(), "Đang lấy dữ liệu...");
                            loadDataFromAPI();
                            Helpers.showToast(getContext(), "Load dữ liệu thành công", Toast.LENGTH_LONG);
                        } catch (Exception ex) {
                            Log.d(TAG, ex.toString());
                            Helpers.showToast(getContext(), "Load dữ liệu thất bại", Toast.LENGTH_LONG);
                        } finally {
                            _swipRefreshLayout.setRefreshing(false);
                            Helpers.dismissLoadingDialog();
                        }
                    }
                }
        );
    }

    private void loadStatusColorCourses(CallWebAPI callWebAPI) {
        callWebAPI.getAllStatusColorCourses().enqueue(new Callback<List<StatusColorCourse>>() {
            @Override
            public void onResponse(Call<List<StatusColorCourse>> call, Response<List<StatusColorCourse>> response) {
                _statusColorCourses = response.body();
//                StatusColorCoursesGridViewAdapter adapter = new StatusColorCoursesGridViewAdapter(getContext(),_statusColorCourses);
//                _gv_status_courses.setAdapter(adapter);
//                GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(), 3);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                RecyclerView recyclerView = _root.findViewById(R.id.rv_status_courses);
                recyclerView.setLayoutManager(layoutManager);
                StatusColorCoursesGridViewAdapter adapter = new StatusColorCoursesGridViewAdapter(getContext(), _statusColorCourses);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<StatusColorCourse>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private void loadCalendarCourse(CallWebAPI callWebAPI, Calendar dateInput) {
        _courses.clear();
        if (dateInput == null) {
            dateInput = Calendar.getInstance();
        }
        Log.d(TAG, dateInput.getTime().toString());
        for (int i = 2; i <= 8; i++) {
            int rvID = -1;
            dateInput.setFirstDayOfWeek(Calendar.MONDAY);
            dateInput.set(Calendar.DAY_OF_WEEK, dateInput.getFirstDayOfWeek());
            Calendar firstDayOfWeek = dateInput;
            switch (i) {
                case 2:
                    _tv_date_of_week_Mon.setText((String) DateFormat.format("dd-MM-yyyy", firstDayOfWeek.getTime()));
                    rvID = R.id.rv_Mon;
                    break;
                case 3:
                    firstDayOfWeek.add(Calendar.DATE, 1);
                    _tv_date_of_week_Tues.setText((String) DateFormat.format("dd-MM-yyyy", firstDayOfWeek.getTime()));
                    rvID = R.id.rv_Tues;
                    break;
                case 4:
                    firstDayOfWeek.add(Calendar.DATE, 2);
                    _tv_date_of_week_Wed.setText((String) DateFormat.format("dd-MM-yyyy", firstDayOfWeek.getTime()));
                    rvID = R.id.rv_Wed;
                    break;
                case 5:
                    firstDayOfWeek.add(Calendar.DATE, 3);
                    _tv_date_of_week_Thurs.setText((String) DateFormat.format("dd-MM-yyyy", firstDayOfWeek.getTime()));
                    rvID = R.id.rv_Thurs;
                    break;
                case 6:
                    firstDayOfWeek.add(Calendar.DATE, 4);
                    _tv_date_of_week_Fri.setText((String) DateFormat.format("dd-MM-yyyy", firstDayOfWeek.getTime()));
                    rvID = R.id.rv_Fri;
                    break;
                case 7:
                    firstDayOfWeek.add(Calendar.DATE, 5);
                    _tv_date_of_week_Sat.setText((String) DateFormat.format("dd-MM-yyyy", firstDayOfWeek.getTime()));
                    rvID = R.id.rv_Sat;
                    break;
                case 8:
                    firstDayOfWeek.add(Calendar.DATE, 6);
                    _tv_date_of_week_Sun.setText((String) DateFormat.format("dd-MM-yyyy", firstDayOfWeek.getTime()));
                    rvID = R.id.rv_Sun;
                    break;
            }

            int finalRvID = rvID;
            Log.d(TAG, (String) DateFormat.format("yyyy-MM-dd", firstDayOfWeek.getTime()));
            callWebAPI.getAllCoursesByDate((String) DateFormat.format("yyyy-MM-dd", firstDayOfWeek.getTime())).enqueue(new Callback<List<Course>>() {
                @Override
                public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
                    _courses = response.body();
//                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    RecyclerView recyclerView = _root.findViewById(finalRvID);
                    recyclerView.setLayoutManager(layoutManager);
                    CalendarCoursesAdapter adapter = new CalendarCoursesAdapter(getContext(), _courses);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<List<Course>> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });
//            course.setStartDate((String) DateFormat.format("dd-MM-yyyy", firstDayOfWeek.getTime()));
//            _courses.add(course);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_chose_date:
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        // TODO Auto-generated method stub
                        _myCalendar.set(Calendar.YEAR, year);
                        _myCalendar.set(Calendar.MONTH, monthOfYear);
                        _myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        loadCalendarCourse(_callWebAPI, _myCalendar);
                    }

                };
                new DatePickerDialog(getContext(), date, _myCalendar
                        .get(Calendar.YEAR), _myCalendar.get(Calendar.MONTH),
                        _myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }
}
