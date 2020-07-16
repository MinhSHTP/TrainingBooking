package com.shtptraining.trainingbooking.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.common.base.Predicate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shtptraining.trainingbooking.Commons.Helpers;
import com.shtptraining.trainingbooking.Commons.MappingAddress.QuanHuyenPhuongXa;
import com.shtptraining.trainingbooking.Commons.MappingAddress.TinhThanhPho;
import com.shtptraining.trainingbooking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class SignUpAct extends AppCompatActivity {
    String jsonStringTinhThanh;
    String jsonStringPhuongXa;
    String jsonStringQuanHuyen;

    Gson _gson = new Gson();

    public static ArrayList<TinhThanhPho> _TinhThanhPhos = new ArrayList<TinhThanhPho>();
    public static ArrayList<QuanHuyenPhuongXa> _QuanHuyens = new ArrayList<QuanHuyenPhuongXa>();
    public static ArrayList<QuanHuyenPhuongXa> _PhuongXas = new ArrayList<QuanHuyenPhuongXa>();

    public static ArrayList<QuanHuyenPhuongXa> _MappingQuanHuyens = new ArrayList<QuanHuyenPhuongXa>();
    public static ArrayList<QuanHuyenPhuongXa> _MappingPhuongXas = new ArrayList<QuanHuyenPhuongXa>();

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

        init();

    }

    private void clearDataHanhChinh() {
        _TinhThanhPhos.clear();
        _QuanHuyens.clear();
        _PhuongXas.clear();
        _MappingPhuongXas.clear();
        _MappingQuanHuyens.clear();
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
        jsonStringTinhThanh = loadJSONFromAsset(getBaseContext(), "63_tinh_thanhpho.json");
        jsonStringQuanHuyen = loadJSONFromAsset(getBaseContext(), "quan_huyen.json");
        jsonStringPhuongXa = loadJSONFromAsset(getBaseContext(), "phuong_xa.json");

        final Spinner spinnerTp = findViewById(R.id.spinner_tinhthanhpho);
        final Spinner spinnerQuanHuyen = findViewById(R.id.spinner_quanhuyen);
        final Spinner spinnerPhuongXa = findViewById(R.id.spinner_phuongxa);

        initializeDataHanhChinh(jsonStringTinhThanh, 0);
        System.gc();
        initializeDataHanhChinh(jsonStringQuanHuyen, 1);
        System.gc();
        initializeDataHanhChinh(jsonStringPhuongXa, 2);
        System.gc();
//
        mappingDataHanhChinh(spinnerTp, 0, null);
        spinnerTp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerQuanHuyen.setAdapter(null);
                spinnerPhuongXa.setAdapter(null);
                if (position != 0) {
                    String code = _TinhThanhPhos.get(position - 1).getCode();
                    mappingDataHanhChinh(spinnerQuanHuyen, 1, code);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//
        spinnerQuanHuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerPhuongXa.setAdapter(null);
                if (position != 0) {
                    Predicate<QuanHuyenPhuongXa> validParentCodeByNamePredicate = quanHuyen -> quanHuyen.getName_with_type().equals(String.valueOf(spinnerQuanHuyen.getItemAtPosition(position)));
                    ArrayList<String> filteredParentCodeByName = Helpers.filterParentCodeByName(_QuanHuyens, validParentCodeByNamePredicate);
                    String code = filteredParentCodeByName.get(0);
                    mappingDataHanhChinh(spinnerPhuongXa, 2, code);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
