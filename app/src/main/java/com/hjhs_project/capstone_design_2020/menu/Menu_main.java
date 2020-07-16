package com.hjhs_project.capstone_design_2020.menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjhs_project.capstone_design_2020.Detector.DetectorActivity;
import com.hjhs_project.capstone_design_2020.MainActivity;
import com.hjhs_project.capstone_design_2020.R;
import com.hjhs_project.capstone_design_2020.login.Login;
import com.hjhs_project.capstone_design_2020.myProfile.MyProfile;
import com.hjhs_project.capstone_design_2020.notepad.NotePad;

import java.io.IOException;

public class Menu_main extends AppCompatActivity {
    private final long FINISH_INTERVAL_TIME = 2000;         //2초내로 두번누르면 화면 종료
    private long backPressedTime = 0;                       //시간 관련
    private static String user_id, user_name;

    int language = 0;                       // 0 : 한국어 -> 영어, 1 : 영어 -> 한국어
    TextView target_translation_word, result_translation, toeic_th,toeic_reception, toeic_test_day, toeic_score;
    Button button_to_translation, change_to_language;
    LinearLayout today_word_layout;
    LinearLayout toeicInfo;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Bundle bundle = getIntent().getExtras();
        user_id = bundle.getString("user_id");
        user_name = bundle.getString("user_name");
        String[] toeic = MainActivity.getToeicInfo().split("&&");

        target_translation_word = findViewById(R.id.target_translation_word);
        result_translation = findViewById(R.id.result_translation);

        toeic_th = findViewById(R.id.toeic_th);
        toeic_reception = findViewById(R.id.toeic_reception);
        toeic_test_day = findViewById(R.id.toeic_test_day);
        toeic_score = findViewById(R.id.toeic_score);

        button_to_translation = findViewById(R.id.button_to_translation);
        change_to_language = findViewById(R.id.change_to_language);

        today_word_layout = findViewById(R.id.today_word_layout);
        toeicInfo = findViewById(R.id.toeicInfo);

        /*토익 날짜*/
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        param.setMargins(0,5,0,5);

        LinearLayout.LayoutParams paramTh = (LinearLayout.LayoutParams) toeic_th.getLayoutParams();
        LinearLayout.LayoutParams paramRecept = (LinearLayout.LayoutParams) toeic_reception.getLayoutParams();
        LinearLayout.LayoutParams paramDay = (LinearLayout.LayoutParams) toeic_test_day.getLayoutParams();
        LinearLayout.LayoutParams paramScore = (LinearLayout.LayoutParams) toeic_score.getLayoutParams();
        for(int i = 0; i < 2; i++){
            String[] temp = toeic[i].split(" ");
            LinearLayout toeicLayout = new LinearLayout(this);
            toeicLayout.setLayoutParams(param);
            toeicLayout.setOrientation(LinearLayout.HORIZONTAL);
            toeicLayout.setWeightSum(10);
            toeicLayout.setGravity(Gravity.CENTER);

            TextView th = new TextView(this);
            TextView reception = new TextView(this);
            TextView testDay = new TextView(this);
            TextView score = new TextView(this);

            th.setLayoutParams(paramTh);
            th.setGravity(Gravity.CENTER);

            th.setText(temp[0].charAt(0)+" "+temp[0].substring(1));

            reception.setLayoutParams(paramRecept);
            reception.setGravity(Gravity.CENTER);
            reception.setText(temp[2]+"\n     "+temp[3]+temp[4]);

            testDay.setLayoutParams(paramDay);
            testDay.setGravity(Gravity.CENTER);
            testDay.setText(temp[6]);

            score.setLayoutParams(paramScore);
            score.setGravity(Gravity.CENTER);
            score.setText(temp[8]);

            toeicLayout.addView(th);
            toeicLayout.addView(reception);
            toeicLayout.addView(testDay);
            toeicLayout.addView(score);

            toeicInfo.addView(toeicLayout);
        }


        //한국어 -> 영어 버튼
        change_to_language.setClickable(true);
        change_to_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(language == 0){
                    language = 1;
                    change_to_language.setText("영어 -> 한국어");
                    target_translation_word.setText(null);
                    result_translation.setText(null);

                }else{
                    language = 0;
                    change_to_language.setText("한국어 -> 영어");
                    target_translation_word.setText(null);
                    result_translation.setText(null);
                }
            }
        });
        //번역 버튼
        button_to_translation.setClickable(true);
        button_to_translation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(){
                    @Override
                    public void run() {
                        String word = target_translation_word.getText().toString();
                        Menu_papago papago = new Menu_papago();
                        String resultWord;
                        if(language == 0){
                            resultWord= papago.getTranslation(word,"ko","en");
                        }else{
                            resultWord= papago.getTranslation(word,"en","ko");
                        }

                        Bundle papagoBundle = new Bundle();
                        papagoBundle.putString("resultWord",resultWord);

                        Message msg = papago_handler.obtainMessage();
                        msg.setData(papagoBundle);
                        papago_handler.sendMessage(msg);
                    }
                }.start();
            }
        });

        new Thread(){
            @Override
            public void run() {
                Menu_today_word mtw = new Menu_today_word();
                try {
                    String[][] todayWords = mtw.getTodayWord();

                    Bundle todayBundle = new Bundle();
                    todayBundle.putStringArray("todayWordEn",todayWords[0]);
                    todayBundle.putStringArray("todayWordKr",todayWords[1]);
                    Message msg = today_handler.obtainMessage();
                    msg.setData(todayBundle);
                    today_handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();





        //바텀 네비게이션 바
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView_main_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.bottom_bar_note:
                                Intent intent = new Intent(Menu_main.this, NotePad.class);
                                startActivity(intent);
                                return true;
                            case R.id.bottom_bar_camera:
                                Intent intent2 = new Intent(Menu_main.this, DetectorActivity.class);
                                intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent2);
                                return true;
                            case R.id.bottom_bar_profile:
                                Intent intent3 = new Intent(Menu_main.this, MyProfile.class);
                                startActivity(intent3);
                                return true;
                        }


                        return false;
                    }
                }
        );

        Toolbar toolbar = findViewById(R.id.toolbar);        // 액션바 생성을 위한
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);        //기본 제목을 없애줍니다.


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                //select logout item
                Intent intent = new Intent(this, Login.class);                       // 첫번째 액티비티로 인탠트를 설정
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);                                    // 스택에 남아있는 중간 액티비티 삭제
                startActivity(intent);                                                              // 액티비티를 실행한다
                finish();                                                                           // 현재 액티비티를 종료한다.
                return true;

            case android.R.id.home:
                //select back button
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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

    @SuppressLint("HandlerLeak")
    Handler papago_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String resultWord = bundle.getString("resultWord");
            result_translation.setText(resultWord);
            //Toast.makeText(getApplicationContext(),resultWord,Toast.LENGTH_SHORT).show();
        }
    };

    @SuppressLint("HandlerLeak")
    Handler today_handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //레이아웃 생성
            Bundle bundle = msg.getData();
            String[] todayWordEn = bundle.getStringArray("todayWordEn");
            String[] todayWordKr = bundle.getStringArray("todayWordKr");
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            param.setMargins(20,20,0,0);
            for(int i = 0; i < 5; i++){
                TextView enTextView = new TextView(Menu_main.this);
                TextView krTextView = new TextView(Menu_main.this);
                enTextView.setText(todayWordEn[i]);
                krTextView.setText(todayWordKr[i]);
                enTextView.setTextSize(20);
                enTextView.setTypeface(null, Typeface.BOLD);
                krTextView.setTextSize(15);

                LinearLayout wordSet = new LinearLayout(Menu_main.this);
                wordSet.setLayoutParams(param);
                wordSet.setOrientation(LinearLayout.VERTICAL);
                wordSet.addView(enTextView);
                wordSet.addView(krTextView);
                today_word_layout.addView(wordSet);
            }

        }
    };

}
