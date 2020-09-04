package com.shtptraining.trainingbooking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shtptraining.trainingbooking.Commons.Constants;
import com.shtptraining.trainingbooking.Commons.Helpers;
import com.shtptraining.trainingbooking.ui.EditCourseAct;
import com.shtptraining.trainingbooking.ui.EditStatusColorCourseAct;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Constants.ACCOUNT_LOGIN.getRole().equals("0")) {
            getMenuInflater().inflate(R.menu.main_activity_actionbar_menu, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_edit_account:
                Helpers.showToast(MainActivity.this, "Chức năng này hiện đang trong giai đoạn phát triển", 0);
                break;
            case R.id.btn_edit_course:
                Intent editCourseAct = new Intent(MainActivity.this, EditCourseAct.class);
                startActivity(editCourseAct);
//                Helpers.showToast(MainActivity.this, "Chức năng này hiện đang trong giai đoạn phát triển", 0);
                break;
            case R.id.btn_edit_status_color_course:
                Intent editStatusColorCourseAct = new Intent(MainActivity.this, EditStatusColorCourseAct.class);
                startActivity(editStatusColorCourseAct);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
