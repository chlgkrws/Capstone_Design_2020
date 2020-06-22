package com.hjhs_project.capstone_design_2020.menu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.hjhs_project.capstone_design_2020.R;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu_camera extends AppCompatActivity implements AutoPermissionsListener {
    ImageView imageView;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = findViewById(R.id.imageView);
        CircleImageView button = findViewById(R.id.picture);
        button.setClickable(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }



    public void takePicture() {
        if(file == null){
            file = createFile();
        }

        Uri fileUri = FileProvider.getUriForFile(this,"com.hjhs_project.capstone_design_2020.fileprovider", file); //File객체로부터 Uri객체 만들기
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, 101);  //사진 찍기 화면 띄우기, result값 받기
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {     //카메라를 찍고나서 데이터를 응답받는 부분
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 101 && resultCode == RESULT_OK){
            BitmapFactory.Options options = new BitmapFactory.Options();  //이미지 파일을 Bitmap 객체로 만들기
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            bitmap = rotateImage(bitmap, 90);
            imageView.setImageBitmap(bitmap);

        }
    }

    public static Bitmap rotateImage(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,true);
    }

    private File createFile() {
        String filename = "capture.jpg";
        File storageDir = Environment.getExternalStorageDirectory();
        File outFile = new File(storageDir, filename);

        return outFile;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AutoPermissions.Companion.parsePermissions(this,requestCode,permissions,this);
    }

    @Override
    public void onDenied(int i, String[] strings) {
        Toast.makeText(this, "permission denied : " + strings.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int i, String[] strings) {
        Toast.makeText(this,"permissions granted : "+strings.length, Toast.LENGTH_LONG).show();
    }
}
