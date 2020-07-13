package com.hjhs_project.capstone_design_2020.login;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Sign_up_request extends StringRequest {
    final static private String URL = "http://stapl.iptime.org:10/runtoapp/register.php";
    private Map<String, String> map;
    public Sign_up_request( String sign_user_id, String sign_user_pass, String sign_user_name, int sign_user_age, Response.Listener<String> listener) {
        super(Method.POST, URL, listener,null);
        map = new HashMap<>();
        map.put("sign_user_id",sign_user_id);
        map.put("sign_user_pass",sign_user_pass);
        map.put("sign_user_name",sign_user_name);
        map.put("sign_user_age",sign_user_age+"");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
