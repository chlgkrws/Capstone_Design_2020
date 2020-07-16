package com.hjhs_project.capstone_design_2020.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.hjhs_project.capstone_design_2020.CToast;
import com.hjhs_project.capstone_design_2020.R;
import com.hjhs_project.capstone_design_2020.menu.Menu_main;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    TextInputEditText user_id, user_pass;
    Button Button_login;
    Button Button_sign_up;

    public static String a = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_view);

        user_id = findViewById(R.id.User_id);
        user_pass = findViewById(R.id.User_pass);
        Button_login = findViewById(R.id.Button_login);
        Button_sign_up = findViewById(R.id.Button_Sign_up);
        Button_login.setClickable(true);
        Button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String User_id = user_id.getText().toString();
                String User_pass = user_pass.getText().toString();
                //Request Queue에 보내는 리스너 객체정의
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");         //json 객체 success에 해당하는 값 가져오기

                            if(success){
                                String user_name = jsonObject.getString("user_name");
                                String user_id = jsonObject.getString("user_id");
                                //Toast.makeText(getApplicationContext(), "로그인성공", Toast.LENGTH_SHORT).show();
                                CToast ct = new CToast();
                                LayoutInflater inflater = getLayoutInflater();
                                ct.showCToast("로그인 성공!",inflater,Login.this);
                                Intent intent = new Intent(Login.this, Menu_main.class);
                                intent.putExtra("user_id",user_id);
                                intent.putExtra("user_name",user_name);
                                startActivity(intent);

                            }else{
                                Toast.makeText(getApplicationContext(),"아이디와 비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"로그인 오류",Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                };
                //requsetQueue에 리스너 add시키기
                Login_request login_request = new Login_request(User_id, User_pass,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                queue.add(login_request);



            }
        });

        Button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Sign_up.class);
                startActivity(intent);
            }
        });


    }
    @Override
    public void onBackPressed() {
        finishAffinity();         //해당 앱의 루트 액티비티를 종료시킨다.
        System.runFinalization(); //간단히 말해 현재 작업중인 쓰레드가 다 종료되면 종료시키라는 명령어
        System.exit(0);    //현재 액티비티를 종료시킨다.
    }
}
