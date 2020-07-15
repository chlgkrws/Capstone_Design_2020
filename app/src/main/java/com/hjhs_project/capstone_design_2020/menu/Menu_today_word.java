package com.hjhs_project.capstone_design_2020.menu;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Menu_today_word {
    public String[][] getTodayWord() throws IOException {

        Document doc = Jsoup.connect("https://search.naver.com/search.naver?sm=top_hty&fbm=0&ie=utf8&query=%EC%98%A4%EB%8A%98%EC%9D%98+%EB%8B%A8%EC%96%B4").get();
        Elements contents_en = doc.select("p.word_area");          //오늘의 단어
        Elements contents_kr = doc.select("em.mean_txt");          //해석

        String[][] todayWords = new String[2][5];
        for(int i = 0; i < 5; i++){
            todayWords[0][i] = contents_en.get(i).text().split(" ")[0];
        }

        for(int i = 0; i < 5; i++){
            todayWords[1][i] = contents_kr.get(i).text();
        }

        return todayWords;

        //nums += contents_en.get(rand_int[j]).text() +"&&"+ contents_kr.get(rand_int[j]).text().split("\\.")[2] +".\n";


    }
}
