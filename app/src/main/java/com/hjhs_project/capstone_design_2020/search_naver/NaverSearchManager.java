package com.hjhs_project.capstone_design_2020.search_naver;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;


public class NaverSearchManager implements Serializable {
    ArrayList<NaverSearch> arrayList;  // NaverSearch객체를 담는 ArrayList 선언

    public NaverSearchManager () {
        arrayList = new ArrayList<NaverSearch>();
    }

    public int getNaverSearchSize() {
        return arrayList.size();
    }

    public void setNaverSearch(NaverSearch naverSearch) {
        arrayList.add(naverSearch);
    }

    public ArrayList<NaverSearch> naverSearchProcess() {
        String num = "";
        String name = "";
        String url = "";

        try {
            Document doc = Jsoup.connect("https://www.naver.com").get();
            Elements contents;
            //contents = doc.select("ul.ah_l li.ah_item a span.ah_k");
            contents = doc.select("ul.ah_l li.ah_item a");  // 해당 태그 부분을 추출

            for (Element e : contents) {
                url = e.attr("href");  // href
                num = e.select("span.ah_r").text();
                name = e.select("span.ah_k").text();

                NaverSearch naver = new NaverSearch(num,name,url);
                arrayList.add(naver);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.v("NaverSearchManager",e.getMessage());
        }
        return arrayList;
    }
}