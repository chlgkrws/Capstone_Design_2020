package com.hjhs_project.capstone_design_2020.search_naver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hjhs_project.capstone_design_2020.Detector.DetectorActivity;
import com.hjhs_project.capstone_design_2020.R;
import com.hjhs_project.capstone_design_2020.menu.Menu_main;
import com.hjhs_project.capstone_design_2020.myProfile.MyProfile;
import com.hjhs_project.capstone_design_2020.notepad.NotePad;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ShowWordsFromNaver extends AppCompatActivity  implements Serializable {
    String searched_words;      //추출한 단어 목록
    String[] titleWordMean;
    String[] split_words;
    String[][] sentences;
    int index;
    int pageIndex;
    TextView word, meaning;
    TextView en_sentence1, en_sentence2, en_sentence3, en_sentence4, en_sentence5;
    TextView kr_sentence1, kr_sentence2, kr_sentence3, kr_sentence4, kr_sentence5;
    Button button_sentence1, button_sentence2, button_sentence3, button_sentence4, button_sentence5;
    Button button_push;
    Button button_refresh;
    String user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showwordsfromnaver);

        Bundle bundle = getIntent().getExtras();            //result_test에서 가져다 준 Extras값을 받음
        searched_words = bundle.getString("words"); //추출된 단어 목록
        searched_words = searched_words.substring(1, searched_words.length()-1);            //앞 뒤 [] 짜르기
        split_words = searched_words.split(", ");                                      //단어 별로 짜르기

        int N = split_words.length;     //스레드 횟수 and 단어 개수
        index = 0;                      //메인 스레드에서 서브스레드 실행 횟수를 파악하기 위한 번호
        pageIndex =0;                   //현재 페이지 인덱스
        sentences = new String[N][5];   //단어별 "예문 + 해석"이 들어가있는 2차원 배열
        titleWordMean = new String[N];

        user_id = Menu_main.getUser_id();
        /*-------------------------------------------변수 초기화------------------------------------*/
        word = findViewById(R.id.word);
        meaning = findViewById(R.id.meaning);
        en_sentence1 = findViewById(R.id.en_sentence1);
        en_sentence2 = findViewById(R.id.en_sentence2);
        en_sentence3 = findViewById(R.id.en_sentence3);
        en_sentence4 = findViewById(R.id.en_sentence4);
        en_sentence5 = findViewById(R.id.en_sentence5);

        kr_sentence1 = findViewById(R.id.kr_sentence1);
        kr_sentence2 = findViewById(R.id.kr_sentence2);
        kr_sentence3 = findViewById(R.id.kr_sentence3);
        kr_sentence4 = findViewById(R.id.kr_sentence4);
        kr_sentence5 = findViewById(R.id.kr_sentence5);

        button_push = findViewById(R.id.changeView);
        button_refresh = findViewById(R.id.button_refresh);

        button_sentence1 = findViewById(R.id.Button_sentence1);
        button_sentence2 = findViewById(R.id.Button_sentence2);
        button_sentence3 = findViewById(R.id.Button_sentence3);
        button_sentence4 = findViewById(R.id.Button_sentence4);
        button_sentence5 = findViewById(R.id.Button_sentence5);

        /*---------------------------------------새로고침-------------------------------------------*/
        button_refresh.setClickable(true);
        button_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recreate();         //화면이 깜빡거리므로 intent

            }
        });

        /*-----------------------------------------단어 별 스레드-----------------------------------*/
        for(int i = 0; i < N; i++){
            String text = split_words[i];

            new Thread(){
                @Override
                public void run() {
                    Document doc = null;
                    try {
                        String nums = "";
                        String title = "";
//                        doc = Jsoup.connect("https://endic.naver.com/search_example.nhn?sLn=kr&query="+ text +"&preQuery=&searchOption=example&examType=&forceRedirect=N").get();
                        doc = Jsoup.connect("https://endic.naver.com/search_example.nhn?sLn=kr&examType=example&query=" + text + "&pageNo="+ThreadLocalRandom.current().nextInt(1, 11)).get();   // 예문 0 ~ 20 페이지중 랜덤
                        Elements contents = doc.select("span.fnt_e09._ttsText");          //영어 예문
                        Elements contents2 = doc.select("div.fnt_k10");                   //해석

                        int [] rand_int = new int[5];
                        Random rnd = new Random();
                        rnd.setSeed(System.currentTimeMillis()); // 시드값을 설정하여 생성
                        for (int i = 0; i < 5; i++) {
                            rand_int[i] = rnd.nextInt(20);
                        }

                        for (int j = 0; j < 5; j++){
                            if(contents2.get(rand_int[j]).text().substring(0,6).equals("이용자 참여")){
                                nums += contents.get(rand_int[j]).text() +"&&"+ contents2.get(rand_int[j]).text().split("\\.")[2] +".\n";
                            }else {
                                nums += contents.get(rand_int[j]).text() + "&&" + contents2.get(rand_int[j]).text() + "\n";        //영어+&&+해석을 구분자로 둬서 (,은 문장에 있을 수 있음) split에 활용
                            }
                        }

//                        for (int j = 0; j < 5; j++){
//                            nums += contents.get(j).text() +"&&"+ contents2.get(j).text() +"\n";        //&&을 구분자로 둬서 (,은 문장에 있을 수 있음) split에 활용
//                        }


                                                      //핸들러를 이용해서 Thread()에서 가져온 데이터를 메인 쓰레드에 보내준다.

                        /*----------------------단어와 뜻을 가져오는 크롤러---------------------*/
                        doc = Jsoup.connect("https://endic.naver.com/search.nhn?sLn=kr&query="+text+"&searchOption=entry_idiom&preQuery=&forceRedirect=N").get();
                        Elements contents3 = doc.select("span.fnt_k05");                 // 뜻,
                        title = text +"&&"+contents3.get(0).text();           //단어 뜻 합치기 구분자 &&

                        bundle.putString("title",title);
                        bundle.putString("sentences", nums);

                        Message msg = handler.obtainMessage();
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        button_push.setClickable(true);                                 //단어별 예문 페이지 넘기기
        button_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(pageIndex == N-1){
                        pageIndex = 0;
                    }else{
                        pageIndex++;
                    }

                    String[] enSentence = new String[5];
                    String[] krSentence = new String[5];

                    for(int j = 0; j <5; j++){
                        enSentence[j] = sentences[pageIndex][j].split("&&")[0];
                        krSentence[j] = sentences[pageIndex][j].split("&&")[1];
                    }
                    word.setText(titleWordMean[pageIndex].split("&&")[0]);
                    meaning.setText(" - "+titleWordMean[pageIndex].split("&&")[1]);

                    en_sentence1.setText(enSentence[0]);
                    en_sentence2.setText(enSentence[1]);
                    en_sentence3.setText(enSentence[2]);
                    en_sentence4.setText(enSentence[3]);
                    en_sentence5.setText(enSentence[4]);

                    kr_sentence1.setText(krSentence[0]);
                    kr_sentence2.setText(krSentence[1]);
                    kr_sentence3.setText(krSentence[2]);
                    kr_sentence4.setText(krSentence[3]);
                    kr_sentence5.setText(krSentence[4]);


                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

        button_sentence1.setClickable(true);                                 //단어별 노트에 추가하기
        button_sentence1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String[] enSentence = new String[5];
                    String[] krSentence = new String[5];

                    for(int j = 0; j <5; j++){
                        enSentence[j] = sentences[pageIndex][j].split("&&")[0];
                        krSentence[j] = sentences[pageIndex][j].split("&&")[1];
                    }

                    String en_word = titleWordMean[pageIndex].split("&&")[0];
                    String kr_word = " - "+titleWordMean[pageIndex].split("&&")[1];
                    String en_sentence = enSentence[0];
                    String kr_sentence = krSentence[0];

                    addNote(user_id, en_word, kr_word, en_sentence, kr_sentence);



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        button_sentence2.setClickable(true);                                 //단어별 노트에 추가하기
        button_sentence2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String[] enSentence = new String[5];
                    String[] krSentence = new String[5];

                    for(int j = 0; j <5; j++){
                        enSentence[j] = sentences[pageIndex][j].split("&&")[0];
                        krSentence[j] = sentences[pageIndex][j].split("&&")[1];
                    }

                    String en_word = titleWordMean[pageIndex].split("&&")[0];
                    String kr_word = " - "+titleWordMean[pageIndex].split("&&")[1];
                    String en_sentence = enSentence[1];
                    String kr_sentence = krSentence[1];

                    addNote(user_id, en_word, kr_word, en_sentence, kr_sentence);



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        button_sentence3.setClickable(true);                                 //단어별 노트에 추가하기
        button_sentence3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String[] enSentence = new String[5];
                    String[] krSentence = new String[5];

                    for(int j = 0; j <5; j++){
                        enSentence[j] = sentences[pageIndex][j].split("&&")[0];
                        krSentence[j] = sentences[pageIndex][j].split("&&")[1];
                    }

                    String en_word = titleWordMean[pageIndex].split("&&")[0];
                    String kr_word = " - "+titleWordMean[pageIndex].split("&&")[1];
                    String en_sentence = enSentence[2];
                    String kr_sentence = krSentence[2];

                    addNote(user_id, en_word, kr_word, en_sentence, kr_sentence);



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        button_sentence4.setClickable(true);                                 //단어별 노트에 추가하기
        button_sentence4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String[] enSentence = new String[5];
                    String[] krSentence = new String[5];

                    for(int j = 0; j <5; j++){
                        enSentence[j] = sentences[pageIndex][j].split("&&")[0];
                        krSentence[j] = sentences[pageIndex][j].split("&&")[1];
                    }

                    String en_word = titleWordMean[pageIndex].split("&&")[0];
                    String kr_word = " - "+titleWordMean[pageIndex].split("&&")[1];
                    String en_sentence = enSentence[3];
                    String kr_sentence = krSentence[3];

                    addNote(user_id, en_word, kr_word, en_sentence, kr_sentence);



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        button_sentence5.setClickable(true);                                 //단어별 노트에 추가하기
        button_sentence5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String[] enSentence = new String[5];
                    String[] krSentence = new String[5];

                    for(int j = 0; j <5; j++){
                        enSentence[j] = sentences[pageIndex][j].split("&&")[0];
                        krSentence[j] = sentences[pageIndex][j].split("&&")[1];
                    }

                    String en_word = titleWordMean[pageIndex].split("&&")[0];
                    String kr_word = " - "+titleWordMean[pageIndex].split("&&")[1];
                    String en_sentence = enSentence[4];
                    String kr_sentence = krSentence[4];

                    addNote(user_id, en_word, kr_word, en_sentence, kr_sentence);



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        /*------------------------------바텀 네비게이션 액션--------------------------*/
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView_show_naver_word);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.bottom_bar_note:
                                Intent intent = new Intent(ShowWordsFromNaver.this, NotePad.class);
                                startActivity(intent);

                                return true;
                            case R.id.bottom_bar_camera:
                                Intent intent2 = new Intent(ShowWordsFromNaver.this, DetectorActivity.class);
                                startActivity(intent2);
                                finish();
                                return true;
                            case R.id.bottom_bar_profile:
                                Intent intent3 = new Intent(ShowWordsFromNaver.this, MyProfile.class);
                                startActivity(intent3);

                                return true;
                        }


                        return false;
                    }
                }
        );


    }

    public void addNote(String user_id, String en_word, String kr_word, String en_sentence, String kr_sentence){

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");         //json 객체 success에 해당하는 값 가져오기

                    if(success){
                        Toast.makeText(getApplicationContext(),"성공!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"실패!",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(),"10100 오류 (통신)",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        };
        //requsetQueue에 리스너 add시키기
        Addnote_request Addnote_request = new Addnote_request(user_id, en_word, kr_word, en_sentence, kr_sentence, responseListener);
        RequestQueue queue = Volley.newRequestQueue(ShowWordsFromNaver.this);                //하나만 설정
        queue.add(Addnote_request);

    }



    /*핸들러 부분*/
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String tempString = bundle.getString("sentences");                   //이런식으로 View를 메인 쓰레드에서 뿌려줘야한다.
            titleWordMean[index] = bundle.getString("title");
            String[] sentenceAll = tempString.split("\n");                     //"예문,해석" ,"예문,해석", ... 식으로 나누기
            String[] enSentence = new String[5];
            String[] krSentence = new String[5];


            /*--------------------------------------------------*/
            for(int i = 0; i < 5; i++){
                sentences[index][i] = sentenceAll[i];
            }

            if(index == 0){
                for (int i = 0; i < 5; i++){                                            //en, kr 예문, 해석배열로 나누기
                    String temp = sentenceAll[i];
                    enSentence[i] = temp.split("&&")[0];
                    krSentence[i] = temp.split("&&")[1];
                }
                word.setText(titleWordMean[index].split("&&")[0]);
                meaning.setText(" - "+titleWordMean[index].split("&&")[1]);

                en_sentence1.setText(enSentence[0]);
                en_sentence2.setText(enSentence[1]);
                en_sentence3.setText(enSentence[2]);
                en_sentence4.setText(enSentence[3]);
                en_sentence5.setText(enSentence[4]);

                kr_sentence1.setText(krSentence[0]);
                kr_sentence2.setText(krSentence[1]);
                kr_sentence3.setText(krSentence[2]);
                kr_sentence4.setText(krSentence[3]);
                kr_sentence5.setText(krSentence[4]);

            }
            index++;                                                // 한 화면 데이터
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShowWordsFromNaver.this, DetectorActivity.class);
        startActivity(intent);
        finish();
    }
}


//String user_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36";
