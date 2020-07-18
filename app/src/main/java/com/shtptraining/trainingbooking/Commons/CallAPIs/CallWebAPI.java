package com.shtptraining.trainingbooking.Commons.CallAPIs;

import com.shtptraining.trainingbooking.Commons.Constants;
import com.shtptraining.trainingbooking.Models.Course;
import com.shtptraining.trainingbooking.Models.MessageFromAPI;
import com.shtptraining.trainingbooking.Models.StatusColorCourse;

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

    @FormUrlEncoded
    @POST("AccountLogin.php?")
    Call<MessageFromAPI> getAccountLogin(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("GetAllCoursesByDate.php")
        //0 = Đang chiêu sinh, 1 = Đã kết thúc, 2 = Đang đào tạo, 3 = Dời lại
    Call<List<Course>> getAllCoursesByDate(@Field("startDate") String startDate);

    @GET("GetAllCoursesWithStatusColor.php")
        //0 = Đang chiêu sinh, 1 = Đã kết thúc, 2 = Đang đào tạo, 3 = Dời lại
    Call<List<Course>> getAllCourses();

    @GET("GetAllStatusColorCourses.php")
    Call<List<StatusColorCourse>> getAllStatusColorCourses();


}
