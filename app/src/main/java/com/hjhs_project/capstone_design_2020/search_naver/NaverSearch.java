package com.hjhs_project.capstone_design_2020.search_naver;

public class NaverSearch {
    String enSentence;
    String krSentence;
    String url;

    public NaverSearch() { }

    public NaverSearch(String num,String name,String url) {
        this.enSentence = num;
        this.krSentence = name;
        this.url = url;
    }

    public String getEnSentence() {
        return enSentence;
    }

    public void setEnSentence(String enSentence) {
        this.enSentence = enSentence;
    }

    public String getKrSentence() {
        return krSentence;
    }

    public void setKrSentence(String krSentence) {
        this.krSentence = krSentence;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

