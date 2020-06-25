package com.hjhs_project.capstone_design_2020.search_naver;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjhs_project.capstone_design_2020.R;

import java.io.Serializable;

public class ShowWordsFromNaver extends AppCompatActivity  implements Serializable {
    String resultText;
    String source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showwordsfromnaver);

        TextView textView = (TextView) findViewById(R.id.text1);
        WebView webView = (WebView) findViewById(R.id.webView);
        Bundle bundle = getIntent().getExtras();            //result_test에서 가져다 준 Extras값을 받음
        String searchWord = bundle.getString("words"); //추출된 단어 목록
        source = "hi";

        resultText = "";
        webView.getSettings().setJavaScriptEnabled(true); //자바스크립트 활성화
        webView.addJavascriptInterface(new MyJavascriptInterface(), "Android");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('body')[0].innerHTML);");

            }
        });
        webView.loadUrl("https://en.dict.naver.com/#/search?query=people");
        textView.setText(source);

    }


    //자바스크립트가 호출되면 여기로 html이 반환
    public class MyJavascriptInterface{
        @JavascriptInterface
        public void getHtml(String html){
            source = html;
        }
    }
}


//String user_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36";
