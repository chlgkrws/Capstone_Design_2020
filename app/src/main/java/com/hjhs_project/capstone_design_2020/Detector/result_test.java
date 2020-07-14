package com.hjhs_project.capstone_design_2020.Detector;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjhs_project.capstone_design_2020.R;
import com.hjhs_project.capstone_design_2020.search_naver.ShowWordsFromNaver;

public class result_test extends AppCompatActivity {
    Button button_to_learn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        button_to_learn = findViewById(R.id.to_learn);

        TextView textView = (TextView) findViewById(R.id.resultText);
        ImageView imageView = (ImageView) findViewById(R.id.resultImage);

        Bundle bundle = getIntent().getExtras();
        String s = bundle.getString("value");                       //물체인식으로 추출한 단어
        textView.setText(s);
        Bitmap img = com.hjhs_project.capstone_design_2020.Detector.DetectorActivity.getCropCopyBitmap();
        imageView.setImageBitmap(com.hjhs_project.capstone_design_2020.Detector.DetectorActivity.getCropCopyBitmap());


        button_to_learn.setClickable(true);
        button_to_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(result_test.this, ShowWordsFromNaver.class);

                intent.putExtra("words", s);
                startActivity(intent);
                finish();
            }
        });

    }


}
