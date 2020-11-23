package com.shtptraining.trainingbooking.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;
import com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI;
import com.shtptraining.trainingbooking.Commons.Constants;
import com.shtptraining.trainingbooking.Commons.Helpers;
import com.shtptraining.trainingbooking.R;

import java.util.Dictionary;
import java.util.Hashtable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.shtptraining.trainingbooking.Commons.CallAPIs.CallWebAPI.retrofit;

public class RatingCourseAct extends AppCompatActivity {

    private String TAG = "RatingCourseAct";

    public static CallWebAPI _callWebAPI = retrofit.create(CallWebAPI.class);

//    public RatingBar _rb_question_1, _rb_question_2, _rb_question_3, _rb_question_4,
//            _rb_question_5, _rb_question_6, _rb_question_7, _rb_question_8, _rb_question_9,
//            _rb_question_10;

    public SmileRating _rb_question_1, _rb_question_2, _rb_question_3, _rb_question_4,
            _rb_question_5, _rb_question_6, _rb_question_7, _rb_question_8, _rb_question_9,
            _rb_question_10;
    //    public SmileRating _smile_rating_question_1;
    public EditText _et_question_11, _et_question_12, _et_question_13,
            _et_question_14, _et_question_15;

    public Integer _id_course = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_course);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        _id_course = bundle.getInt("ID_COURSE");


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        _rb_question_1 = findViewById(R.id.rb_question_1);
        InitializeSmileRating(_rb_question_1);
        _rb_question_1.setTag(1);

        _rb_question_2 = findViewById(R.id.rb_question_2);
        InitializeSmileRating(_rb_question_2);
        _rb_question_2.setTag(2);

        _rb_question_3 = findViewById(R.id.rb_question_3);
        InitializeSmileRating(_rb_question_3);
        _rb_question_3.setTag(3);

        _rb_question_4 = findViewById(R.id.rb_question_4);
        InitializeSmileRating(_rb_question_4);
        _rb_question_4.setTag(4);

        _rb_question_5 = findViewById(R.id.rb_question_5);
        InitializeSmileRating(_rb_question_5);
        _rb_question_5.setTag(5);

        _rb_question_6 = findViewById(R.id.rb_question_6);
        InitializeSmileRating(_rb_question_6);
        _rb_question_6.setTag(6);

        _rb_question_7 = findViewById(R.id.rb_question_7);
        InitializeSmileRating(_rb_question_7);
        _rb_question_7.setTag(7);

        _rb_question_8 = findViewById(R.id.rb_question_8);
        InitializeSmileRating(_rb_question_8);
        _rb_question_8.setTag(8);

        _rb_question_9 = findViewById(R.id.rb_question_9);
        InitializeSmileRating(_rb_question_9);
        _rb_question_9.setTag(9);

        _rb_question_10 = findViewById(R.id.rb_question_10);
        InitializeSmileRating(_rb_question_10);
        _rb_question_10.setTag(10);

        _et_question_11 = findViewById(R.id.et_question_11);
        _et_question_11.setTag(11);

        _et_question_12 = findViewById(R.id.et_question_12);
        _et_question_12.setTag(12);

        _et_question_13 = findViewById(R.id.et_question_13);
        _et_question_13.setTag(13);

        _et_question_14 = findViewById(R.id.et_question_14);
        _et_question_14.setTag(14);

        _et_question_15 = findViewById(R.id.et_question_15);
        _et_question_15.setTag(15);


        Dictionary<Integer, SmileRating> rb_questions = new Hashtable<>();
        rb_questions.put(0, _rb_question_1);
        rb_questions.put(1, _rb_question_2);
        rb_questions.put(2, _rb_question_3);
        rb_questions.put(3, _rb_question_4);
        rb_questions.put(4, _rb_question_5);
        rb_questions.put(5, _rb_question_6);
        rb_questions.put(6, _rb_question_7);
        rb_questions.put(7, _rb_question_8);
        rb_questions.put(8, _rb_question_9);
        rb_questions.put(9, _rb_question_10);

        Dictionary<Integer, EditText> et_questions = new Hashtable<>();
        et_questions.put(0, _et_question_11);
        et_questions.put(1, _et_question_12);
        et_questions.put(2, _et_question_13);
        et_questions.put(3, _et_question_14);
        et_questions.put(4, _et_question_15);

        Button btn_send_ratings_course = findViewById(R.id.btn_send_ratings_course);
        btn_send_ratings_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendRatings(rb_questions, et_questions) == true) {
                    Helpers.showToast(RatingCourseAct.this, "Gửi đánh giá môn học thành công", Toast.LENGTH_SHORT);
                    finish();
                } else {
                    Helpers.showToast(RatingCourseAct.this, "Gửi đánh giá môn học thất bại", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void InitializeSmileRating(SmileRating smileRating) {
        smileRating.setSelectedSmile(BaseRating.OKAY);
        smileRating.setNameForSmile(BaseRating.TERRIBLE, getString(R.string.smile_lv1));
        smileRating.setNameForSmile(BaseRating.BAD, getString(R.string.smile_lv2));
        smileRating.setNameForSmile(BaseRating.OKAY, getString(R.string.smile_lv3));
        smileRating.setNameForSmile(BaseRating.GOOD, getString(R.string.smile_lv4));
        smileRating.setNameForSmile(BaseRating.GREAT, getString(R.string.smile_lv5));
    }

    private boolean sendRatings(Dictionary<Integer, SmileRating> rb_questions, Dictionary<Integer, EditText> et_questions) {
        try {
            for (int i = 0; i < 10; i++) {

                _callWebAPI.createDetailEvaluationCourse(
                        _id_course,
                        Integer.parseInt(rb_questions.get(i).getTag().toString()),
                        Float.parseFloat(String.valueOf(rb_questions.get(i).getRating())),
                        true,
                        "",
                        Constants.ACCOUNT_LOGIN.getEmail()


                ).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().equals("Create detail evaluation course successfull")) {
                            Log.d(TAG, "Gửi đánh giá môn học thành công question");
                        } else {
                            Log.d(TAG, "Gửi đánh giá môn học thất bại question");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });

            }
            for (int i = 0; i < 5; i++) {
                _callWebAPI.createDetailEvaluationCourse(
                        _id_course,
                        Integer.parseInt(et_questions.get(i).getTag().toString()),
                        0.0f,
                        false,
                        "",
                        Constants.ACCOUNT_LOGIN.getEmail()
                ).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.body().equals("Create detail evaluation course successfull")) {
                            Log.d(TAG, "Gửi đánh giá môn học thành công question");
                        } else {
                            Log.d(TAG, "Gửi đánh giá môn học thất bại question");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
}
