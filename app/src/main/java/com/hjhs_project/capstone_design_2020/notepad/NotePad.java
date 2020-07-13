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

        //testText = findViewById(R.id.TextText);
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

                    if(success){
                        JSONArray arr = (JSONArray)jsonObject.get("univ");
                        String word = (String)((JSONObject)arr.get(0)).get("en_word");                                     //메모장에 보여지는 header 단어
                        String compareKey = word;
                        String sentence ="";                                           //예문
                        ExpandableListAdapter.Item places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, word);
                        places.invisibleChildren = new ArrayList<>();
                        for(int i = 0; i<arr.length(); i++){                           //en_word,kr_word,en_sentence_,kr_sentense를 한줄에 가져오는 object
                            JSONObject tmp =(JSONObject)arr.get(i);

                            String en_word = (String)tmp.get("en_word");
                            String kr_word = (String)tmp.get("kr_word");
                            String en_sentence = (String)tmp.get("en_sentence");
                            String kr_sentence = (String)tmp.get("kr_sentence");

                            word = en_word;
                            sentence = en_sentence + "\n"+kr_sentence;

                            if(!compareKey.equals(word)){
                                data.add(places);
                                places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, word);
                                places.invisibleChildren = new ArrayList<>();
                            }

                            places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, sentence));

                            compareKey = word;
                        }
                        data.add(places);
                        recyclerview.setAdapter(new ExpandableListAdapter(data));

                    }else{
                        Toast.makeText(getApplicationContext(),"단어를 공부해보세요!",Toast.LENGTH_SHORT).show();
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
