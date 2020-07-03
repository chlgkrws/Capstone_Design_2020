package com.hjhs_project.capstone_design_2020.search_naver;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hjhs_project.capstone_design_2020.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;

public class ShowWordsFromNaver extends AppCompatActivity  implements Serializable {
    String resultText;
    String source;
    String searched_words;      //추출한 단어 목록
    String[] split_words;
    String[][] sentences;
    int index;
    int pageIndex;

    TextView en_sentence1, en_sentence2, en_sentence3, en_sentence4, en_sentence5;
    TextView kr_sentence1, kr_sentence2, kr_sentence3, kr_sentence4, kr_sentence5;
    Button button_sentence1, button_sentence2, button_sentence3, button_sentence4, button_sentence5;
    Button button_push;

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
        pageIndex =1;                   //현재 페이지 인덱스
        sentences = new String[N][5];   //단어별 "예문 + 해석"이 들어가있는 2차원 배열

        /*-------------------------------------------변수 초기화------------------------------------*/
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

        /*---------------------------------단어 별 스레드-----------------------------------*/
        for(int i = 0; i < N; i++){
            String text = split_words[i];

            new Thread(){
                @Override
                public void run() {
                    Document doc = null;
                    try {
                        String nums = "";
                        doc = Jsoup.connect("https://endic.naver.com/search_example.nhn?sLn=kr&query="+ text +"&preQuery=&searchOption=example&examType=&forceRedirect=N").get();
                        Elements contents = doc.select("span.fnt_e09._ttsText");          //회차 id값 가져오기
                        Elements contents2 = doc.select("div.fnt_k10");
                        for (int j = 0; j < 5; j++){
                            nums += contents.get(j).text() +"&&"+ contents2.get(j).text() +"\n";
                        }


                        bundle.putString("numbers", nums);                               //핸들러를 이용해서 Thread()에서 가져온 데이터를 메인 쓰레드에 보내준다.
                        Message msg = handler.obtainMessage();
                        msg.setData(bundle);
                        handler.sendMessage(msg);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        button_push.setClickable(true);
        button_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] enSentence = new String[5];
                String[] krSentence = new String[5];

                for(int j = 0; j <5; j++){
                    enSentence[j] = sentences[pageIndex][j].split("&&")[0];
                    krSentence[j] = sentences[pageIndex][j].split("&&")[1];
                }
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

                if(pageIndex == N-1){
                    pageIndex = 0;
                }else{
                    pageIndex++;
                }

            }
        });

    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String tempString = bundle.getString("numbers");                   //이런식으로 View를 메인 쓰레드에서 뿌려줘야한다.

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
            index++;
        }
    };
}


//String user_Agent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.106 Safari/537.36";
