package com.example.yuunagi.entity;

public class Rank {
    private String coverUrl;
    private String bangumiTitle;
    private String score;

   public Rank(String coverUrl, String bangumiTitle, String score) {
        this.coverUrl = coverUrl;
        this.bangumiTitle = bangumiTitle;
        this.score = score;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getBangumiTitle() {
        return bangumiTitle;
    }

    public String getScore() {
        return score;
    }
}


