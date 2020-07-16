package com.hjhs_project.capstone_design_2020;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CToast {

    public void showCToast(String cText, LayoutInflater activityInflater, Activity activity){
        LayoutInflater inflater = activityInflater;


        View layout = inflater.inflate(R.layout.toastborder, (ViewGroup) activity.findViewById(R.id.toast_layout_root));
        TextView text = layout.findViewById(R.id.custom_toast);
        Toast toast = new Toast(activity);
        text.setText(cText);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
