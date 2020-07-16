package com.hjhs_project.capstone_design_2020;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjhs_project.capstone_design_2020.login.Login;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static String toeicInfo;
    public static String tosInfo;


    TextView test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // test = findViewById(R.id.test);
        new Thread() {
            @Override
            public void run() {
                Document doc = null;
                String toeic1 = "";
                String toeic2 = "";
                String tos = "";
                try {
                    doc = Jsoup.connect("https://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query=%ED%86%A0%EC%9D%B5+%EC%8B%9C%ED%97%98%EC%9D%BC%EC%A0%95").get();
                    //doc = Jsoup.connect("https://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query=%EC%98%A4%EB%8A%98%EC%9D%98+%EB%8B%A8%EC%96%B4").get();

                    Elements info = doc.select("table.schedule_inven");          //해석

                    toeic1 = info.get(2).text();
                    toeic2 = info.get(3).text();
                    String[] toeicInfo1 = toeic1.split(" ");                       //받아온 정보를 공백으로 split
                    String[] toeicInfo2 = toeic2.split(" ");

                    int[] perm = {0,5,6,7,8,15,16,18,19};                                //토익 정보에 대한 순열
                    toeic1 = "";
                    toeic2 = "";
                    for(int i = 0; i < perm.length; i++){
                        toeic1 += toeicInfo1[perm[i]] +" ";
                        toeic2 += toeicInfo2[perm[i]] +" ";
                    }

                    /*toeic1 = toeicInfo1[0] +" " + toeicInfo1[5]+" " +toeicInfo1[6] +" "+toeicInfo1[7]+" "+ toeicInfo1[8] + " "+ toeicInfo1[15]+ " "+
                            toeicInfo1[16]+" "+ toeicInfo1[18] + " " +toeicInfo1[19];
*/
                    Bundle bundle = new Bundle();
                    bundle.putString("toeic1",toeic1);
                    bundle.putString("toeic2",toeic2);


                    Thread.sleep(5000);

                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.sendMessage(msg);


                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();



    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String toeic1 = bundle.getString("toeic1");
            String toeic2 = bundle.getString("toeic2");
            toeicInfo = toeic1 +"&&"+toeic2;
            //test.setText(toeic1+toeic2);

            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        }
    };

    public static String getToeicInfo() {
        return toeicInfo;
    }

}
