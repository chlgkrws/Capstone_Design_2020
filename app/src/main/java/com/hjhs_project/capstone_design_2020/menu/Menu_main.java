package com.hjhs_project.capstone_design_2020.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hjhs_project.capstone_design_2020.Detector.DetectorActivity;
import com.hjhs_project.capstone_design_2020.R;
import com.hjhs_project.capstone_design_2020.myProfile.MyProfile;
import com.hjhs_project.capstone_design_2020.notepad.NotePad;

import de.hdodenhof.circleimageview.CircleImageView;

public class Menu_main extends AppCompatActivity {
    private final long FINISH_INTERVAL_TIME = 2000;         //2초내로 두번누르면 화면 종료
    private long backPressedTime = 0;                       //시간 관련
    private static String user_id, user_name;

    CircleImageView go_to_camera;
    Button go_to_note, go_to_info;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Bundle bundle = getIntent().getExtras();
        user_id = bundle.getString("user_id");
        user_name = bundle.getString("user_name");

        go_to_camera = findViewById(R.id.go_to_camera);
        go_to_note = findViewById(R.id.go_to_note);
        go_to_info = findViewById(R.id.go_to_info);

        go_to_camera.setClickable(true);
        go_to_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_main.this, DetectorActivity.class);
                startActivity(intent);
            }
        });


        //내 정보 가는 버튼
        go_to_info.setClickable(true);
        go_to_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_main.this, MyProfile.class);
                startActivity(intent);
            }
        });

        go_to_note.setClickable(true);
        go_to_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu_main.this, NotePad.class);
                startActivity(intent);
            }
        });

    }



    public static String getUser_id(){
        return user_id;
    }

    public static String getUser_name(){
        return user_name;
    }


    //뒤로가기 종료
    @Override
    public void onBackPressed(){            //뒤로가기 키 두번 누를 시 종료

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if(0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime){
            finishAffinity();         //해당 앱의 루트 액티비티를 종료시킨다.
            System.runFinalization(); //간단히 말해 현재 작업중인 쓰레드가 다 종료되면 종료시키라는 명령어
            System.exit(0);    //현재 액티비티를 종료시킨다.
        }else{
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누를 시 종료됩니다.",Toast.LENGTH_SHORT).show();

        }

    }
}
