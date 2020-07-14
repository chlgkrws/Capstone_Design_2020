package com.hjhs_project.capstone_design_2020.myProfile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.hjhs_project.capstone_design_2020.R;
import com.hjhs_project.capstone_design_2020.menu.Menu_main;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class MyProfile extends AppCompatActivity {
    private static final int REQUEST_CODE = 0;          //갤러리 사진 request

    TextView profile_id, profile_name, profile_att;
    TextView profile_tier, profile_rank, profile_study;
    ImageView profile_user_img, profile_tier_img;
    TextView change_profile_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        String user_id = Menu_main.getUser_id();
        String user_name = Menu_main.getUser_name();


        /*------------------------변수 초기화-------------------------------*/
        profile_id = findViewById(R.id.profile_id);
        profile_name = findViewById(R.id.profile_name);
        profile_att = findViewById(R.id.profile_att);

        profile_tier = findViewById(R.id.profile_tier);
        profile_rank = findViewById(R.id.profile_rank);
        profile_study = findViewById(R.id.profile_study);

        profile_user_img = findViewById(R.id.profile_user_img);
        profile_tier_img = findViewById(R.id.profile_tier_img);

        change_profile_img = findViewById(R.id.change_profile_img);
        /*--------------------------Volley php database-------------------*/

        //Att request에 대한 리스너 객체
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");         //json 객체 success에 해당하는 값 가져오기

                    if(success){
                        String days = jsonObject.getString("days");
                        profile_att.setText("출석 횟수 - "+days+"일");
                    }else{
                        Toast.makeText(getApplicationContext(),"10099 오류",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"10100 오류 (통신)",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        };
        //requsetQueue에 리스너 add시키기
        Att_request att_request = new Att_request(user_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MyProfile.this);                //하나만 설정
        queue.add(att_request);


        //Tier request 리스너 객체
        Response.Listener<String> responseListener_tier = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");         //json 객체 success에 해당하는 값 가져오기
                    String tierToText = "";
                    if(success){
                        String tier = jsonObject.getString("tier");
                        Double real_tier = Double.parseDouble(tier);

                        if(real_tier <= 0.1){
                            tierToText = "Challenger";
                            profile_tier_img.setImageResource(R.drawable.challenger);
                        }else if(real_tier <= 0.15){
                            tierToText = "Diamond";
                            profile_tier_img.setImageResource(R.drawable.diamond);
                        }else if(real_tier <= 0.30){
                            tierToText ="Platinum";
                            profile_tier_img.setImageResource(R.drawable.platinum);
                        }else if(real_tier <= 0.50){
                            tierToText ="Gold";
                            profile_tier_img.setImageResource(R.drawable.gold);
                        }else if(real_tier <= 0.80){
                            tierToText ="Silver";
                            profile_tier_img.setImageResource(R.drawable.silver);
                        }else{
                            tierToText ="Bronze";
                            profile_tier_img.setImageResource(R.drawable.bronze);
                        }
                        profile_tier.setText("티어 - "+tierToText);

                        //이미지 뷰도 같이 띄워주기
                    }else{
                        profile_tier_img.setImageResource(R.drawable.bronze);
                        profile_tier.setText("티어 - Unranked");
                        //Toast.makeText(getApplicationContext(),"10099 오류",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"10100 오류 (통신)",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        };
        //requsetQueue에 리스너 add시키기
        Tier_request tier_request = new Tier_request(user_id, responseListener_tier);
        queue.add(tier_request);


        //Rank request 리스너 객체
        Response.Listener<String> responseListener_rank = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");         //json 객체 success에 해당하는 값 가져오기

                    if(success){
                        String rank = jsonObject.getString("rank");
                        profile_rank.setText("랭킹 - "+rank+"위");

                    }else{
                        profile_rank.setText("랭킹 -  --위 (단어를 공부해보세요!)");
                        //Toast.makeText(getApplicationContext(),"10099 오류",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"10100 오류 (통신)",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        };
        //requsetQueue에 리스너 add시키기
        Rank_request rank_request = new Rank_request(user_id, responseListener_rank);
        queue.add(rank_request);

        //Rank request 리스너 객체
        Response.Listener<String> responseListener_LC = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");         //json 객체 success에 해당하는 값 가져오기

                    if(success){
                        String wordCount = jsonObject.getString("user_word");
                        String sentenceCount = jsonObject.getString("user_sentence");

                        profile_study.setText("단어 - "+wordCount+"개   예문 - "+sentenceCount+"개");


                    }else{
                        profile_study.setText("단어 - "+"0개   예문 - "+"0개");
                        //Toast.makeText(getApplicationContext(),"10099 오류",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"10100 오류 (통신)",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        };
        //requsetQueue에 리스너 add시키기
        LearningCount_request LC_request = new LearningCount_request(user_id, responseListener_LC);
        queue.add(LC_request);


        /*---------------------고정 데이터 처리------------------------*/
        profile_id.setText("아이디 - "+user_id);
        profile_name.setText("이름 - " +user_name);
        /*--------------------------------------------------------------*/

        change_profile_img.setClickable(true);
        change_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(inputStream);
                    img = rotateImage(img,90);                                                        //이미지 정상 각도로 회전시키기
                    inputStream.close();
                    profile_user_img.setImageBitmap(img);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Bitmap rotateImage(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,true);
    }

//다이얼로그
    /*private void showMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("프로필 사진 설정");
        builder.setItems(R.array.ProfileImg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String[] items = getResources().getStringArray(R.array.ProfileImg);
                if(i == 0){
                    //앨범에서 가져오기
                }else{
                    //카메라로 촬영하기
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }*/
}
