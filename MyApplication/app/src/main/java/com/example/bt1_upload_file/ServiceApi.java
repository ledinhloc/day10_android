package com.example.bt1_upload_file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ServiceApi {
    public static final String BASE_URL = "http://app.iotstar.vn:8081/appfoods/";
    Gson gson = new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create();

    ServiceApi  serviceApi = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ServiceApi.class);

    @Multipart
    @POST("upload.php")
    Call<List<ImageUpload>> upload(@Part(Const.MY_USERNAME)RequestBody username, @Part MultipartBody.Part avatar);

    @Multipart
    @POST("updateimages.php")
    Call<ApiResponse> updateimage(@Part(Const.MY_USERNAME)RequestBody username, @Part MultipartBody.Part avatar);


}
