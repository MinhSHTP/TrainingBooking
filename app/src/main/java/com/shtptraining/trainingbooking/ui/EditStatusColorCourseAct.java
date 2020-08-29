package com.shtptraining.trainingbooking.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI;
import com.shtptraining.trainingbooking.Commons.Helpers;
import com.shtptraining.trainingbooking.Models.StatusColorCourse;
import com.shtptraining.trainingbooking.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI.retrofit;

public class EditStatusColorCourseAct extends AppCompatActivity {
    private String TAG = "EditStatusColorCourseAct";
    public static CallWebAPI _callWebAPI = retrofit.create(CallWebAPI.class);
    List<StatusColorCourse> _statusColorCourses = new ArrayList<StatusColorCourse>();
    List<StatusColorCourse> _statusColorCoursesAdd = new ArrayList<StatusColorCourse>();
    List<String> _colorUsed = new ArrayList<String>();
    TableLayout _tbl_layout_status_color_course;
    Resources _resources;
    BottomNavigationView _bottomNavigationView;
    FloatingActionButton _floatingActionButton;
    boolean _pressedBtnSave = false;

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
        _bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        _bottomNavigationView.getMenu().findItem(R.id.btn_Save);
        _bottomNavigationView.getMenu().findItem(R.id.btn_Save).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                _pressedBtnSave = true;
                //call API update & insert data

                return true;
            }
        });
        _bottomNavigationView.getMenu().findItem(R.id.btn_Undo).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                loadDataStatusColorCourse();
                _statusColorCoursesAdd.clear();
                _pressedBtnSave = false;
                return true;
            }
        });

        _floatingActionButton = findViewById(R.id.btn_Add_Status_Color);
        _floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextIndex = _statusColorCourses.size() + _statusColorCoursesAdd.size();
                TableRow tr = new TableRow(EditStatusColorCourseAct.this);
                tr.setPadding(4, 4, 4, 4);

                CheckBox checkBox = new CheckBox(EditStatusColorCourseAct.this);
                int heightChkBox = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, _resources.getDisplayMetrics());
                int widthChkBox = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, _resources.getDisplayMetrics());
                checkBox.setLayoutParams(new TableLayout.LayoutParams(widthChkBox, heightChkBox));
                checkBox.setTag("tag_" + nextIndex);
                tr.addView(checkBox);

                EditText editText = new EditText(EditStatusColorCourseAct.this);
                int heightET = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, _resources.getDisplayMetrics());
                int widthET = _resources.getDisplayMetrics().widthPixels * 6 / 7;
                editText.setLayoutParams(new TableRow.LayoutParams(widthET, heightET));
                editText.setTag("et_edit_color_" + nextIndex);
                editText.setText("status" + nextIndex);
                editText.setTextAppearance(EditStatusColorCourseAct.this, R.style.row_tbl_status_color_course);
                editText.setGravity(Gravity.CENTER);
                tr.addView(editText);

                ImageView imageView = new ImageView(new ContextThemeWrapper(EditStatusColorCourseAct.this, R.style.row_tbl_status_color_course), null, 0);
                imageView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                String colorRandom = randomColorNotExist();
                imageView.setBackgroundColor(Color.parseColor(colorRandom));
                imageView.setTag("new_" + colorRandom);
                tr.addView(imageView);

                final int index = nextIndex;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ColorPickerDialogBuilder colorPickerDialogBuilder = initColorPicker(view, index, Color.parseColor(colorRandom));
                        colorPickerDialogBuilder.build().show();
                    }
                });
                _tbl_layout_status_color_course.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                StatusColorCourse newStatusColorCourse = new StatusColorCourse();
                newStatusColorCourse.setName(editText.getText().toString());

                ColorDrawable drawableColor = (ColorDrawable) imageView.getBackground();
                newStatusColorCourse.setColorString("#" + Integer.toHexString(drawableColor.getColor()));
                _statusColorCoursesAdd.add(newStatusColorCourse);

                _colorUsed.add(colorRandom);

                _pressedBtnSave = false;
            }
        });
//        _tbl_layout_status_color_course.setColumnStretchable(0,true);
//        _tbl_layout_status_color_course.setColumnStretchable(1,true);
        loadDataStatusColorCourse();
    }

    @Override
    public void onBackPressed() {
        if (!_pressedBtnSave) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Chưa lưu thiết lập")
                    .setMessage("Bạn chưa lưu các thiết lập, bạn chắc chắn muốn thoát?")
                    .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }

                    })
                    .setNegativeButton("Không", null)
                    .show();
        } else {
            super.onBackPressed();
        }
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
                    int width = _resources.getDisplayMetrics().widthPixels * 6 / 7;
                    editText.setLayoutParams(new TableRow.LayoutParams(width, height));
                    editText.setTag("et_edit_color_" + i);
                    editText.setText(response.body().get(i).getName());
                    editText.setTextAppearance(EditStatusColorCourseAct.this, R.style.row_tbl_status_color_course);
                    editText.setGravity(Gravity.CENTER);
                    tr.addView(editText);

                    ImageView imageView = new ImageView(new ContextThemeWrapper(getApplicationContext(), R.style.row_tbl_status_color_course), null, 0);
                    imageView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
                    imageView.setBackgroundColor(Color.parseColor(_statusColorCourses.get(i).getColorString()));
                    imageView.setTag("old_" + _statusColorCourses.get(i).getColorString());
                    tr.addView(imageView);

                    final int index = i;
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ColorPickerDialogBuilder colorPickerDialogBuilder = initColorPicker(view, index, Color.parseColor(_statusColorCourses.get(index).getColorString()));
                            colorPickerDialogBuilder.build().show();
                        }
                    });
                    _tbl_layout_status_color_course.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    _colorUsed.add(_statusColorCourses.get(index).getColorString());
                }
            }

            @Override
            public void onFailure(Call<List<StatusColorCourse>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        });
    }

    private ColorPickerDialogBuilder initColorPicker(View view, int index, int colorInit) {
        return ColorPickerDialogBuilder
                .with(EditStatusColorCourseAct.this)
                .setTitle("Chọn màu cho trạng thái ")
                .initialColor(colorInit)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(10)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        duplicateColor(Integer.toHexString(selectedColor));
//                        Helpers.showToast(EditStatusColorCourseAct.this, "#" + Integer.toHexString(selectedColor), 0);
                        Helpers.showToast(EditStatusColorCourseAct.this, "#" + Integer.toHexString(selectedColor) + " đã được sử dụng. Vui lòng chọn màu khác", 1);
                    }
                })
                .setPositiveButton("Chọn", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        view.setBackgroundColor(Color.parseColor("#" + Integer.toHexString(selectedColor)));
                        if (view.getTag().toString().contains("new_")) {
                            int indexInNewList = index - _statusColorCourses.size();
                            _statusColorCoursesAdd.get(indexInNewList).setColorString("#" + Integer.toHexString(selectedColor));
                        } else {
                            _statusColorCourses.get(index).setColorString("#" + Integer.toHexString(selectedColor));
                        }
                        _colorUsed.add("#" + Integer.toHexString(selectedColor));
                    }
                })
                .setNegativeButton("Đóng", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
    }

    private String randomColorNotExist() {
        Random rnd = new Random();
        int colorRandom = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        while (_colorUsed.indexOf(colorRandom) != -1) {
            colorRandom = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        }
        return "#" + Integer.toHexString(colorRandom);
    }

    private boolean duplicateColor(String codeColor) {
        if (_colorUsed.indexOf(codeColor) != -1) {
            return true;
        }
        return false;
    }
}
