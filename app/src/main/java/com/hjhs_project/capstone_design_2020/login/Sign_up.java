package com.hjhs_project.capstone_design_2020.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.hjhs_project.capstone_design_2020.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Sign_up extends AppCompatActivity {
    TextInputEditText sign_up_user_id, sign_up_user_pass, sign_up_user_name, sign_up_user_age;
    Button Button_sign_up, Button_back_to_main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sign_up_user_id = findViewById(R.id.sign_up_user_id);
        sign_up_user_pass = findViewById(R.id.sign_up_user_pass);
        sign_up_user_name = findViewById(R.id.sign_up_user_name);
        sign_up_user_age = findViewById(R.id.sign_up_user_age);

        Button_sign_up = findViewById(R.id.Button_sign_up);
        Button_back_to_main = findViewById(R.id.Button_back_to_main);

        Button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sign_id = sign_up_user_id.getText().toString();
                String sign_pass = sign_up_user_pass.getText().toString();
                String sign_name = sign_up_user_name.getText().toString();
                if(sign_up_user_age.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "양식에 맞춰 기입해주세요.",Toast.LENGTH_SHORT).show();
                   return;
                }
                int sign_age = Integer.parseInt(sign_up_user_age.getText().toString());





                String idPolicy = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";               // 아이디 정규식 영문@영문.영문 형태
                Boolean id_pattern = Pattern.matches(idPolicy, sign_id);

                String passwordPolicy = "((?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{6,})";          // 비밀번호 정규식 영문, 숫자, 특수문자 6글자 이상의 형태
                Boolean password_pattern = Pattern.matches(passwordPolicy, sign_pass);

                String namePolicy = "^[가-힣]*$";                                                   // 이름 정규식 한글형태
                Boolean name_pattern = Pattern.matches(namePolicy, sign_name);

                String agePolicy = "^[0-9]+$";                                                      // 나이 정규식 숫자형태
                Boolean age_pattern = Pattern.matches(agePolicy, String.valueOf(sign_age));


                if(id_pattern){
                    if(password_pattern){
                        if(name_pattern){
                            if(age_pattern){
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            //회원가입 성공 시
                                            if(success){
                                                Toast.makeText(getApplicationContext(),"회원가입 성공",Toast.LENGTH_SHORT).show();;
                                                Intent intent = new Intent(Sign_up.this, Login.class);
                                                startActivity(intent);
                                            }else{
                                                Toast.makeText(getApplicationContext(),"회원가입 실패",Toast.LENGTH_SHORT).show();;
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(getApplicationContext(),"통신 오류",Toast.LENGTH_SHORT).show();;
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                Sign_up_request sign_up_request = new Sign_up_request(sign_id,sign_pass,sign_name,sign_age,responseListener);
                                RequestQueue queue = Volley.newRequestQueue(Sign_up.this);
                                queue.add(sign_up_request);
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"나이는 숫자만 사용해주세요.",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"이름은 한글만 사용해주세요.",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"비밀번호는 6자리이상 영문, 숫자, 특수문자를 조합해주세요.",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"이메일은 영문@이메일 형태로 해주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //취소 -> 로그인화면
        Button_back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
