package com.shtptraining.trainingbooking.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI;
import com.shtptraining.trainingbooking.Commons.Constants;
import com.shtptraining.trainingbooking.Commons.Helpers;
import com.shtptraining.trainingbooking.MainActivity;
import com.shtptraining.trainingbooking.Models.Account;
import com.shtptraining.trainingbooking.Models.MessageFromAPI;
import com.shtptraining.trainingbooking.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI.retrofit;

public class LoginAct extends AppCompatActivity implements View.OnClickListener {
    private String TAG = "LoginAct";
    private ArrayList<Account> _Accounts = new ArrayList<Account>();
    Gson _gson = new Gson();
    TextInputEditText _et_email, _et_password;
    Button _btnLogin, _btnLoginViaGmail, _btnSignUp, _btnForgetPassword;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        init();
    }

    private void init() {
        _et_email = (TextInputEditText) findViewById(R.id.et_email);
        _et_password = (TextInputEditText) findViewById(R.id.et_password);

        _btnLogin = (Button) findViewById(R.id.btnLogin);
        _btnLogin.setOnClickListener(this);

        _btnLoginViaGmail = (Button) findViewById(R.id.btnLoginViaGmail);
        _btnLoginViaGmail.setOnClickListener(this);

        _btnForgetPassword = (Button) findViewById(R.id.btnForgetPassword);
        _btnForgetPassword.setOnClickListener(this);

        _btnSignUp = (Button) findViewById(R.id.btnSignUp);
        _btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                Helpers.showLoadingDialog(this, "Đang đăng nhập...");
                CallWebAPI callWebAPI = retrofit.create(CallWebAPI.class);
                callWebAPI.Login(_et_email.getText().toString(), _et_password.getText().toString()).enqueue(new Callback<MessageFromAPI>() {
                    @Override
                    public void onResponse(Call<MessageFromAPI> call, Response<MessageFromAPI> response) {
                        if (response.body() != null && response.body().getStatus().equals("success")) {
                            Helpers.showToast(getBaseContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT);
                            Intent mainAct = new Intent(LoginAct.this, MainActivity.class);
                            ArrayList<Account> account_login = new Gson().fromJson(response.body().getData(), new TypeToken<ArrayList<Account>>() {
                            }.getType());
                            Constants.ACCOUNT_LOGIN = account_login.get(0);
                            Helpers.showToast(getApplicationContext(), "Xin chào " + Constants.ACCOUNT_LOGIN.getName().toString(), 0);
                            Helpers.dismissLoadingDialog();
                            startActivity(mainAct);
                            finish();
                        } else {
                            switch (response.body().toString()) {
                                case "Server is down":
                                    Helpers.showToast(getBaseContext(), "Kết nối server thất bại", Toast.LENGTH_SHORT);
                                    break;
                                case "Login failed":
                                    Helpers.showToast(getBaseContext(), "Email hoặc mật khẩu không đúng", Toast.LENGTH_SHORT);
                                    break;
                                default:
                                    Helpers.showToast(getBaseContext(), "Kết nối server thất bại", Toast.LENGTH_SHORT);
                                    break;
                            }
                            Helpers.dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageFromAPI> call, Throwable t) {
                        Helpers.dismissLoadingDialog();
                        Helpers.showToast(getBaseContext(), t.getMessage(), Toast.LENGTH_SHORT);
                    }
                });
                break;
            case R.id.btnLoginViaGmail:
                break;
            case R.id.btnForgetPassword:
                break;
            case R.id.btnSignUp:
                System.gc();
                Helpers.showLoadingDialog(this, "Chuyển đến trang đăng ký...");
                Intent signUpAct = new Intent(LoginAct.this, SignUpAct.class);
                startActivity(signUpAct);
                Helpers.dismissLoadingDialog();
                break;
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Helpers.showAlertDialogExitApp(this);
        }
        return false;
    }
}
