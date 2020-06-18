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
                int sign_age = Integer.parseInt(sign_up_user_age.getText().toString());

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
        });

        //취소 -> 로그인화면
        Button_back_to_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sign_up.this, Login.class);
                startActivity(intent);
            }
        });



    }
}
