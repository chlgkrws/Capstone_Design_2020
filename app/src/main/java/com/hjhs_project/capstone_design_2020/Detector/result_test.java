package com.hjhs_project.capstone_design_2020.Detector;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjhs_project.capstone_design_2020.R;

public class result_test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        TextView textView = (TextView) findViewById(R.id.resultText);
        ImageView imageView = (ImageView) findViewById(R.id.resultImage);
        Bundle bundle = getIntent().getExtras();
        String s = bundle.getString("value");
        textView.setText(s);
        imageView.setImageBitmap(com.hjhs_project.capstone_design_2020.Detector.DetectorActivity.getCropCopyBitmap());

    }
}
