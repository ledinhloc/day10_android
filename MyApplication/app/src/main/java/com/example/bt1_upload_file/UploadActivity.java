package com.example.bt1_upload_file;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends AppCompatActivity {
    ImageView imgAvatar;
    Button btnChooseFile, btnUploadImages;
    private Uri mUri;
    private ProgressDialog mProgressDialog;
    public static final int MY_REQUEST_CODE = 100;
    public static final String TAG = UploadActivity.class.getName();

    public static String[] storge_permisstion = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permisstions_33 = {
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_upload);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_upload), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imgAvatar = findViewById(R.id.imgAvatar);
        btnChooseFile = findViewById(R.id.btnChooseFile);
        btnUploadImages = findViewById(R.id.btnUploadImages);

        mProgressDialog = new ProgressDialog(UploadActivity.this);
        mProgressDialog.setMessage("Please wait upload ....");
        btnChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckPermission();
            }
        });

        btnUploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUri != null){
                    ImageUpload1();
                }
            }
        });
    }

    private void ImageUpload1() {
        mProgressDialog.show();

//        String username = editTextUserName.getText().toString().trim();
        String username = "name loc";
        RequestBody requestUsername = RequestBody.create(MediaType.parse("multipart/form-data"), username);

        //Lấy đường dẫn thực tế của ảnh từ mUri và tạo RequestBody cho ảnh.
        String IMAGE_PATH = RealPathUtil.getRealPath(this, mUri);
        Log.e("ffff", IMAGE_PATH);
        File file = new File(IMAGE_PATH);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        //
        MultipartBody.Part partbodyavatar = MultipartBody.Part.createFormData(Const.MY_IMAGE, file.getName(), requestFile);
        //
//        ServiceApi.serviceApi.updateimage(requestUsername, partbodyavatar).enqueue(new Callback<List<ImageUpload>>() {
//            @Override
//            public void onResponse(Call<List<ImageUpload>> call, Response<List<ImageUpload>> response) {
//                mProgressDialog.dismiss();
//                List<ImageUpload> imageUpload = response.body();
//                if (imageUpload.size() > 0) {
//                    for (int i = 0; i < imageUpload.size(); i++) {
//                        //textViewUserName.setText(imageUpload.get(i).getUserName());
////                        Glide.with(MainActivity.this)
////                                .load(imageUpload.get(i).getAvatar())
////                                .into(imageViewUpload);
//                        Toast.makeText(UploadActivity.this, "Thanh cong", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    Toast.makeText(UploadActivity.this, "That bai", Toast.LENGTH_SHORT).show();
//
//                }
//            }

        ServiceApi.serviceApi.updateimage(requestUsername, partbodyavatar).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                mProgressDialog.dismiss();
                if(response.isSuccessful() && response.body() != null){
                    Toast.makeText(UploadActivity.this, "Thanh cong" + response.body().getResult(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(UploadActivity.this, "That bai", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }
//    private void CheckPermission() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            openGallery();
//            return;
//        }
//
//        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//            openGallery();
//        } else {
//            // Yêu cầu quyền đọc bộ nhớ ngoài
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
//        }
//    }


    private void CheckPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallery();
            return;
        }
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }else {
            requestPermissions(permission(), MY_REQUEST_CODE);
        }
    }

    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //Nhận ảnh do người dùng chọn.
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "onActivityResult");
                    if(result.getResultCode() == UploadActivity.RESULT_OK){
                        //
                        Intent data = result.getData();
                        if (data == null){
                            return;
                        }
                        Uri uri = data.getData();
                        mUri = uri;
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            imgAvatar.setImageBitmap(bitmap);
                        }
                        catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    public static String[] permission(){
        String[] p;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            p  = storge_permisstions_33;
        }
        else {
            p = storge_permisstion;
        }
        return p;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

}