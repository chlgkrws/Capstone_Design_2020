package com.hjhs_project.capstone_design_2020.myProfile;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

         //MultipartBody.FORM,
public class FileUploadUtils {
    public static void send2Server(File file) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("files", file.getName(), RequestBody.create(MediaType.parse("image/jpeg"), file))
                .addFormDataPart("result","photo_image")
                .build();

        Request request = new Request.Builder().url("http://stapl.iptime.org:10/upload/upload.php").post(requestBody).build();// Server URL 은 본인 IP를 입력
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();

            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                Log.d("TEST : ", response.body().string());

            }
        });
    }
}

