package com.hjhs_project.capstone_design_2020.search_naver;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Addnote_request extends StringRequest {
    final static private String URL ="http://stapl.iptime.org:10/runtoapp/addnote.php";
    private Map<String,String> map;

    public Addnote_request(String user_id, String en_word, String kr_word, String en_sentence, String kr_sentence, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("user_id", user_id);                //Post방식에는 Hashmap에 값을 넣어 getParams 메서드로 반환해주면 된다.
        map.put("en_word", en_word);
        map.put("kr_word", kr_word);
        map.put("en_sentence", en_sentence);
        map.put("kr_sentence", kr_sentence);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
