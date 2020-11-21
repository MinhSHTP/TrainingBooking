package com.shtptraining.trainingbooking.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Predicate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI;
import com.shtptraining.trainingbooking.Commons.Helpers;
import com.shtptraining.trainingbooking.Commons.MappingAddress.QuanHuyenPhuongXa;
import com.shtptraining.trainingbooking.Commons.MappingAddress.TinhThanhPho;
import com.shtptraining.trainingbooking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI.retrofit;

public class SignUpAct extends AppCompatActivity {
    private String TAG = "SignUpAct";
    public static CallWebAPI _callWebAPI = retrofit.create(CallWebAPI.class);

    String jsonStringTinhThanh;
    String jsonStringPhuongXa;
    String jsonStringQuanHuyen;
    String _address = "";
    Gson _gson = new Gson();

    public static ArrayList<TinhThanhPho> _TinhThanhPhos = new ArrayList<TinhThanhPho>();
    public static ArrayList<QuanHuyenPhuongXa> _QuanHuyens = new ArrayList<QuanHuyenPhuongXa>();
    public static ArrayList<QuanHuyenPhuongXa> _PhuongXas = new ArrayList<QuanHuyenPhuongXa>();

    public static ArrayList<QuanHuyenPhuongXa> _MappingQuanHuyens = new ArrayList<QuanHuyenPhuongXa>();
    public static ArrayList<QuanHuyenPhuongXa> _MappingPhuongXas = new ArrayList<QuanHuyenPhuongXa>();
    public static ArrayList<String> _genders = new ArrayList<>();

    Button _btnConfirm;

    Spinner _spinnerTp;
    Spinner _spinnerQuanHuyen;
    Spinner _spinnerPhuongXa;
    Spinner _spinner_gender;

    String _selectedTp;
    String _selectedQuanHuyen;
    String _selectedPhuongXa;
    String _selectedGioiTinh = "Nam";

    EditText _et_email;
    EditText _et_hoten;
    EditText _et_sdt;
    EditText _et_diachi;
    EditText _et_duong;
    EditText _et_password, _et_confirm_password;
    EditText _et_birth_year;

    @Override
    protected void onResume() {
        super.onResume();
        clearDataHanhChinh();
        init();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        jsonStringTinhThanh = loadJSONFromAsset(getBaseContext(), "63_tinh_thanhpho.json");
        jsonStringQuanHuyen = loadJSONFromAsset(getBaseContext(), "quan_huyen.json");
        jsonStringPhuongXa = loadJSONFromAsset(getBaseContext(), "phuong_xa.json");
//        init();

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        _spinnerTp = findViewById(R.id.spinner_tinhthanhpho);
        _spinnerQuanHuyen = findViewById(R.id.spinner_quanhuyen);
        _spinnerPhuongXa = findViewById(R.id.spinner_phuongxa);
        _et_email = findViewById(R.id.et_email);
        _et_hoten = findViewById(R.id.et_hoten);
        _et_sdt = findViewById(R.id.et_sdt);
        _et_diachi = findViewById(R.id.et_diachi);
        _et_duong = findViewById(R.id.et_duong);

        _spinner_gender = findViewById(R.id.spinner_gender);
        _et_password = findViewById(R.id.et_password);
        _et_confirm_password = findViewById(R.id.et_confirm_password);
        _et_confirm_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //focus out
                    if (!_et_password.getText().toString().equals(_et_confirm_password.getText().toString())) {
                        Helpers.showToast(SignUpAct.this, "Xác nhận mật khẩu không trùng khớp", Toast.LENGTH_SHORT);
                    }

                }
            }
        });
        _et_birth_year = findViewById(R.id.et_birth_year);

        init();
    }

    private void clearDataHanhChinh() {
        _TinhThanhPhos.clear();
        _QuanHuyens.clear();
        _PhuongXas.clear();
        _MappingPhuongXas.clear();
        _MappingQuanHuyens.clear();
        _genders.clear();
    }

    private void initializeDataHanhChinh(String jsonString, int type) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            switch (type) {
                case 0:
                    JSONArray jsonArrayCities = jsonObject.getJSONArray("Cities");
                    _TinhThanhPhos = _gson.fromJson(String.valueOf(jsonArrayCities), new TypeToken<ArrayList<TinhThanhPho>>() {
                    }.getType());
                    break;
                case 1:
                    JSONArray jsonArrayDistricts = jsonObject.getJSONArray("Districts");
                    _QuanHuyens = _gson.fromJson(String.valueOf(jsonArrayDistricts), new TypeToken<ArrayList<QuanHuyenPhuongXa>>() {
                    }.getType());
                    break;
                case 2:
                    JSONArray jsonArrayWards = jsonObject.getJSONArray("Wards");
                    _PhuongXas = _gson.fromJson(String.valueOf(jsonArrayWards), new TypeToken<ArrayList<QuanHuyenPhuongXa>>() {
                    }.getType());
                    break;
            }
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }

    private void init() {

        _et_duong.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //focus out
                    autoFillAddress();
                }
            }
        });
        initializeDataHanhChinh(jsonStringTinhThanh, 0);
        System.gc();
        initializeDataHanhChinh(jsonStringQuanHuyen, 1);
        System.gc();
        initializeDataHanhChinh(jsonStringPhuongXa, 2);
        System.gc();
//
        mappingDataHanhChinh(_spinnerTp, 0, null);

        _genders.add("Nam");
        _genders.add("Nữ");
        _genders.add("Giới tính thứ 3");
        ArrayAdapter<String> adapterGender = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, _genders);
        _spinner_gender.setAdapter(adapterGender);
        _spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _selectedGioiTinh = _genders.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _spinnerTp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _spinnerQuanHuyen.setAdapter(null);
                _spinnerPhuongXa.setAdapter(null);

                _selectedQuanHuyen = "";
                _selectedPhuongXa = "";
                if (position != 0) {
                    String code = _TinhThanhPhos.get(position - 1).getCode();
                    mappingDataHanhChinh(_spinnerQuanHuyen, 1, code);
                    _selectedTp = _TinhThanhPhos.get(position - 1).getName_with_type();
                } else {
                    _selectedTp = "";
                }
                autoFillAddress();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//
        _spinnerQuanHuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _spinnerPhuongXa.setAdapter(null);
                _selectedPhuongXa = "";
                if (position != 0) {
                    Predicate<QuanHuyenPhuongXa> validParentCodeByNamePredicate = quanHuyen -> quanHuyen.getName_with_type().equals(String.valueOf(_spinnerQuanHuyen.getItemAtPosition(position)));
                    ArrayList<String> filteredParentCodeByName = Helpers.filterParentCodeByName(_QuanHuyens, validParentCodeByNamePredicate);
                    String code = filteredParentCodeByName.get(0);
                    mappingDataHanhChinh(_spinnerPhuongXa, 2, code);
                    _selectedQuanHuyen = _QuanHuyens.get(position).getName_with_type();
                } else {
                    _selectedQuanHuyen = "";
                }
                autoFillAddress();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _spinnerPhuongXa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    _selectedPhuongXa = _PhuongXas.get(position).getName_with_type();
                } else {
                    _selectedPhuongXa = "";
                }
                autoFillAddress();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        _btnConfirm = (Button) findViewById(R.id.btnConfirm);
        _btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    _callWebAPI.createAccount(
                            _et_hoten.getText().toString().isEmpty() ? "" : _et_hoten.getText().toString(),
                            _selectedGioiTinh,
                            _et_password.getText().toString(),
                            _et_email.getText().toString().isEmpty() ? "" : _et_email.getText().toString(),
                            _et_password.getText().toString(),
                            _et_diachi.getText().toString().isEmpty() ? "" : _et_diachi.getText().toString(),
                            _et_sdt.getText().toString().isEmpty() ? "" : _et_sdt.getText().toString(),
                            1
                    ).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.body() != null && response.body().equals("Create course successfull")) {
                                Helpers.showToast(SignUpAct.this, "Đăng ký thành công", Toast.LENGTH_SHORT);
                                finish();
                            } else {
                                Helpers.showToast(SignUpAct.this, "Đăng ký thất bại", Toast.LENGTH_SHORT);
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e(TAG, t.getMessage());
                            Helpers.showToast(SignUpAct.this, t.getMessage(), Toast.LENGTH_SHORT);
                        }
                    });
                }

            }
        });
    }

    private boolean validateForm() {

        if (_et_email.getText().toString().isEmpty()) {
            Helpers.showToast(SignUpAct.this, "Vui lòng nhập Email", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_password.getText().toString().isEmpty()) {
            Helpers.showToast(SignUpAct.this, "Vui lòng nhập Mật khẩu", Toast.LENGTH_SHORT);
            return false;
        }

        if (!_et_password.getText().toString().equals(_et_confirm_password.getText().toString())) {
            Helpers.showToast(SignUpAct.this, "Xác nhận mật khẩu không trùng khớp", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_hoten.getText().toString().isEmpty()) {
            Helpers.showToast(SignUpAct.this, "Vui lòng nhập Họ tên", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_hoten.getText().toString().isEmpty()) {
            Helpers.showToast(SignUpAct.this, "Vui lòng nhập Họ tên", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_birth_year.getText().toString().isEmpty()) {
            Helpers.showToast(SignUpAct.this, "Vui lòng nhập Năm sinh", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_sdt.getText().toString().isEmpty()) {
            Helpers.showToast(SignUpAct.this, "Vui lòng nhập Số điện thoại", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_diachi.getText().toString().isEmpty()) {
            Helpers.showToast(SignUpAct.this, "Vui lòng điền Địa chỉ", Toast.LENGTH_SHORT);
            return false;
        }

        if (_et_sdt.getText().toString().isEmpty()) {
            Helpers.showToast(SignUpAct.this, "Vui lòng nhập Đường", Toast.LENGTH_SHORT);
            return false;
        }


        if (_selectedTp.equals("")) {
            Helpers.showToast(SignUpAct.this, "Vui lòng Chọn Tỉnh / Thành phố", Toast.LENGTH_SHORT);
            return false;
        }

        if (_selectedQuanHuyen.equals("")) {
            Helpers.showToast(SignUpAct.this, "Vui lòng Chọn Quận / Huyện", Toast.LENGTH_SHORT);
            return false;
        }

        if (_selectedPhuongXa.equals("")) {
            Helpers.showToast(SignUpAct.this, "Vui lòng Chọn Phường / Xã", Toast.LENGTH_SHORT);
            return false;
        }

        return true;
    }

    private void autoFillAddress() {
        _et_diachi.setText(_et_duong.getText() + ", " + _selectedPhuongXa + ", " + _selectedQuanHuyen + ", " + _selectedTp);
    }

    private void mappingDataHanhChinh(Spinner spinnerMapping, int type, String code) {
        ArrayList<String> stringArray = new ArrayList<String>();
        switch (type) {
            case 0:
                stringArray.add("--Chọn Tỉnh/Thành phố--");
                for (int i = 0; i < _TinhThanhPhos.size(); i++) {
                    stringArray.add(_TinhThanhPhos.get(i).getName_with_type());
                }
                break;
            case 1:
                stringArray.add("--Chọn Quận/Huyện--");
                Predicate<QuanHuyenPhuongXa> validQuanHuyenPredicate = quanHuyen -> quanHuyen.getParent_code().equals(code);
                ArrayList<String> filteredQuanHuyenList = Helpers.filterNamesByParentCode(_QuanHuyens, validQuanHuyenPredicate);
                stringArray.addAll(filteredQuanHuyenList);
                break;
            case 2:
                stringArray.add("--Chọn Phường/Xã--");
                Predicate<QuanHuyenPhuongXa> validPhuongXaPredicate = phuongXa -> phuongXa.getParent_code().equals(code);
                ArrayList<String> filteredPhuongXaList = Helpers.filterNamesByParentCode(_PhuongXas, validPhuongXaPredicate);
                stringArray.addAll(filteredPhuongXaList);
                break;
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item,
                stringArray);
        spinnerAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerMapping.setAdapter(spinnerAdapter);
    }

    private QuanHuyenPhuongXa createNewInstanceQuanHuyenPhuongXa(JSONObject jsonObjByCode) {
        try {
            QuanHuyenPhuongXa QuanHuyenPhuongXa = new QuanHuyenPhuongXa();
            QuanHuyenPhuongXa.setName(jsonObjByCode.getString("name"));
            QuanHuyenPhuongXa.setType(jsonObjByCode.getString("type"));
            QuanHuyenPhuongXa.setSlug(jsonObjByCode.getString("slug"));
            QuanHuyenPhuongXa.setName_with_type(jsonObjByCode.getString("name_with_type"));
            QuanHuyenPhuongXa.setCode(jsonObjByCode.getString("code"));
            QuanHuyenPhuongXa.setParent_code(jsonObjByCode.getString("parent_code"));
            QuanHuyenPhuongXa.setPath_with_type(jsonObjByCode.getString("path_with_type"));
            QuanHuyenPhuongXa.setPath(jsonObjByCode.getString("path"));
            return QuanHuyenPhuongXa;
        } catch (JSONException e) {
            return null;
        }
    }

    private TinhThanhPho createNewInstanceTinhThanhPho(JSONObject jsonObjByCode) {
        try {
            TinhThanhPho tinhThanhPho = new TinhThanhPho();
            tinhThanhPho.setName(jsonObjByCode.getString("name"));
            tinhThanhPho.setType(jsonObjByCode.getString("type"));
            tinhThanhPho.setSlug(jsonObjByCode.getString("slug"));
            tinhThanhPho.setName_with_type(jsonObjByCode.getString("name_with_type"));
            tinhThanhPho.setCode(jsonObjByCode.getString("code"));
            return tinhThanhPho;
        } catch (JSONException e) {
            return null;
        }

    }

    public String loadJSONFromAsset(Context context, String filename) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(filename);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            Helpers.showToast(getBaseContext(), "Đọc dữ liệu tỉnh / thành phố thất bại", 1);
            return null;
        }
        return json;
    }
}
