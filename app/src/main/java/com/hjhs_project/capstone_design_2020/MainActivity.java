package com.hjhs_project.capstone_design_2020;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.hjhs_project.capstone_design_2020.login.Login;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static String toeicInfo;
    public static String tosInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread() {
            @Override
            public void run() {
                Document doc = null;
                String toeic = "";
                String tos = "";
                try {
                    doc = Jsoup.connect("https://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query=%EC%98%A4%EB%8A%98%EC%9D%98+%EB%8B%A8%EC%96%B4").get();
                    doc = Jsoup.connect("https://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query=%EC%98%A4%EB%8A%98%EC%9D%98+%EB%8B%A8%EC%96%B4").get();

                    Elements th = doc.select("h4.table_title");                 //회차
                    Elements info = doc.select("");          //해석

                    String[][] todayWords = new String[2][5];
                    for (int i = 0; i < 5; i++) {
                        todayWords[0][i] = contents_en.get(i).text().split(" ")[0];
                    }

                    for (int i = 0; i < 5; i++) {
                        todayWords[1][i] = contents_kr.get(i).text();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();


        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
}
