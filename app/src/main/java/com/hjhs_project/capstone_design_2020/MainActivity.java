package com.hjhs_project.capstone_design_2020;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
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
    private static Bitmap user_profile = null;

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

                    Message msg = handler.obtainMessage();
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                Document doc = null;
                String tos1 = "";
                String tos2 = "";

                try {
                    doc = Jsoup.connect("https://appexam.ybmnet.co.kr/toeicswt/receipt/schedule.asp").get();
                    Elements info = doc.select("div.exam_ing_table tbody tr td");

                    for(int i = 0; i < 3; i++){
                        if(i == 0){
                            tos1 += info.get(i).text().replaceAll(" ",".").replace("년","").replaceFirst("월","").replaceFirst("일","")+",";
                            tos2 += info.get(i+3).text().replaceAll(" ",".").replace("년","").replaceFirst("월","").replaceFirst("일","")+",";
                        }else if(i == 1){
                            String temp[] = info.get(i).text().split("~");
                            String temp2[] =info.get(i+3).text().split("~");

                            temp[0] = temp[0].replaceAll(" ",".").replace("년","").replaceFirst("월","").replaceFirst("일","").substring(0,13);
                            temp[1] = temp[1].replaceAll(" ",".").replace("년","").replaceFirst("월","").replaceFirst("일","").substring(1,14);

                            temp2[0] = temp2[0].replaceAll(" ",".").replace("년","").replaceFirst("월","").replaceFirst("일","").substring(0,13);
                            temp2[1] = temp2[1].replaceAll(" ",".").replace("년","").replaceFirst("월","").replaceFirst("일","").substring(1,14);

                            tos1 += temp[0] +"\n   ~"+temp[1]+",";
                            tos2 += temp2[0] +"\n   ~"+temp2[1]+",";


                        }else{
                            tos1 += info.get(i).text().replaceAll(" ",".").replace("년","").replaceFirst("월","").replaceFirst("일","").substring(0,13)+",";
                            tos2 += info.get(i+3).text().replaceAll(" ",".").replace("년","").replaceFirst("월","").replaceFirst("일","").substring(0,13)+",";
                        }



                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("tos1",tos1);
                    bundle.putString("tos2",tos2);
                    Thread.sleep(300);                                                 //스플래쉬 표시시간
                    Message msg = tosHandler.obtainMessage();
                    msg.setData(bundle);
                    tosHandler.sendMessage(msg);


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
           // Toast.makeText(getApplicationContext(),toeicInfo,Toast.LENGTH_LONG);

        }
    };

    @SuppressLint("HandlerLeak")
    Handler tosHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String tos1 = bundle.getString("tos1");
            String tos2 = bundle.getString("tos2");
            tosInfo = tos1 +"&&"+tos2;
            //Toast.makeText(getApplicationContext(),tosInfo,Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }
    };

    public static String getToeicInfo() {
        return toeicInfo;
    }
    public static String getTosInfo(){return tosInfo;}
    public static Bitmap getUser_profile() {
        return user_profile;
    }

    public static void setUser_profile(Bitmap user_profile) {
        MainActivity.user_profile = user_profile;
    }

}
