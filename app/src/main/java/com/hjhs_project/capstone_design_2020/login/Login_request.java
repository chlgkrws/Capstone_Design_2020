package com.hjhs_project.capstone_design_2020.login;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Login_request extends StringRequest {
    final static private String URL ="http://stapl.iptime.org:10/runtoapp/login.php";   //
    private Map<String,String> map;

    public Login_request(String user_id, String user_pass, Response.Listener<String> listener) {
        super(Method.POST,URL, listener, null);
        map = new HashMap<>();
        map.put("user_id", user_id);                //Post방식에는 Hashmap에 값을 넣어 getParams 메서드로 반환해주면 된다.
        map.put("user_pass",user_pass);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
