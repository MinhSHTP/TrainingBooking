package com.shtptraining.trainingbooking.Commons.CallAPIs;

import com.shtptraining.trainingbooking.Commons.Constants;
import com.shtptraining.trainingbooking.Models.Account;
import com.shtptraining.trainingbooking.Models.Course;
import com.shtptraining.trainingbooking.Models.EvaluationCourse;
import com.shtptraining.trainingbooking.Models.MessageFromAPI;
import com.shtptraining.trainingbooking.Models.StatusColorCourse;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface CallWebAPI {
    OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();

    //0 = Admin role, 1 = Trainer role, 2 = Student role
    @FormUrlEncoded
    @POST("Account/POST/AccountLogin.php?")
    Call<MessageFromAPI> Login(@Field("email") String email, @Field("password") String password);

    @GET("Account/GET/GetAllAccounts.php")
    Call<List<Account>> getAllAccounts();

    @FormUrlEncoded
    @POST("Account/GET/GetAccountByName.php")
    Call<List<Account>> getAccountByName(@Field("name") String name);

    @FormUrlEncoded
    @POST("Account/POST/CreateAccount.php")
        //Call<String> createAccount(@Body Account account);
    Call<String> createAccount(@Field("name") String name, @Field("gender") String gender, @Field("birth_year") String birth_year, @Field("email") String email,
                               @Field("password") String password, @Field("address") String address, @Field("phone") String phone, @Field("role") Integer role);


    //=================================================================================================
    @FormUrlEncoded
    @POST("Course/GET/GetAllCoursesByDate.php")
    //0 = Đang chiêu sinh, 1 = Đã kết thúc, 2 = Đang đào tạo, 3 = Dời lại
    Call<List<Course>> getAllCoursesByDate(@Field("startDate") String startDate);

    @FormUrlEncoded
    @POST("Course/GET/GetAllCoursesByID.php")
        //0 = Đang chiêu sinh, 1 = Đã kết thúc, 2 = Đang đào tạo, 3 = Dời lại
    Call<List<Course>> getAllCoursesByID(@Field("ID") Integer ID);

    @GET("Course/GET/GetAllCoursesWithStatusColor.php")
        //0 = Đang chiêu sinh, 1 = Đã kết thúc, 2 = Đang đào tạo, 3 = Dời lại
    Call<List<Course>> getAllCourses();

    @GET("StatusColorCourse/GET/GetAllStatusColorCourses.php")
    Call<List<StatusColorCourse>> getAllStatusColorCourses();

    @FormUrlEncoded
    @POST("Course/POST/CreateCourse.php")
    Call<String> createCourse(
            @Field("name") String name, @Field("duration_date") String duration_date, @Field("duration_time") String duration_time,
            @Field("duration") String duration, @Field("time") String time, @Field("date") String date,
            @Field("start_date") Date start_date, @Field("trainer") String trainer, @Field("fee") String fee,
            @Field("status") Integer status, @Field("numberof") Integer numberof);

    @FormUrlEncoded
    @POST("Course/PUT/UpdateCourse.php")
    Call<String> updateCourse(
            @Field("name") String name, @Field("duration_date") String duration_date, @Field("duration_time") String duration_time,
            @Field("duration") String duration, @Field("time") String time, @Field("date") String date,
            @Field("start_date") Date start_date, @Field("trainer") String trainer, @Field("fee") String fee,
            @Field("status") Integer status, @Field("numberof") Integer numberof, @Field("key1") Date old_start_date,
            @Field("key2") String old_name, @Field("key3") String old_trainer);

    //========================================================================================
    @GET("EvaluationCourse/GET/GetAllEvaluationCourses.php")
    Call<List<EvaluationCourse>> getAllEvaluationCourses();

    @FormUrlEncoded
    @POST("EvaluationCourse/POST/CreateDetailEvaluationCourse.php")
    Call<String> createDetailEvaluationCourse(
            @Field("id_course") Integer id_course, @Field("id_evaluation_course") Integer id_evaluation_course,
            @Field("point") Integer point, @Field("is_point") Boolean is_point,
            @Field("note") String note, @Field("email") String email
    );
}
