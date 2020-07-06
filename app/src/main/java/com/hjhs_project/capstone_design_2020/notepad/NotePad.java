package com.hjhs_project.capstone_design_2020.notepad;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.hjhs_project.capstone_design_2020.R;
import com.hjhs_project.capstone_design_2020.menu.Menu_main;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotePad extends AppCompatActivity {

    private RecyclerView recyclerview;
    TextView testText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        testText = findViewById(R.id.TextText);
        String user_id = Menu_main.getUser_id();

        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        List<ExpandableListAdapter.Item> data = new ArrayList<>();


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");         //json 객체 success에 해당하는 값 가져오기
                    String compareKey = "테스트";
                    if(success){
                        JSONArray arr = (JSONArray)jsonObject.get("univ");
                        String s = "";
                        for(int i = 0; i<arr.length(); i++){
                            JSONObject tmp =(JSONObject)arr.get(i);

                            String en_word = (String)tmp.get("en_word");
                            String kr_word = (String)tmp.get("kr_word");
                            s += en_word + "&&"+ kr_word+"\n";
                        }
                        testText.setText(s);

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
        Noted_request noted_request = new Noted_request(user_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(NotePad.this);
        queue.add(noted_request);


    }
}
