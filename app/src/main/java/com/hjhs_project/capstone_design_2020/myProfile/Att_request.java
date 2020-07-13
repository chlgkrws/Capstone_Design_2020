package com.hjhs_project.capstone_design_2020.myProfile;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Att_request extends StringRequest {
    final static private String URL ="http://wolwoljin.iptime.org:10/runtoapp/checktheday.php";
    private Map<String,String> map;

    public Att_request(String user_id, Response.Listener<String> listener) {
        super(Request.Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("user_id", user_id);                //Post방식에는 Hashmap에 값을 넣어 getParams 메서드로 반환해주면 된다.

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}

