package com.example.yuunagi.entity;

public class Rank {
    private String coverUrl;
    private String bangumiTitle;
    private String points;

   public Rank(String coverUrl, String bangumiTitle, String points) {
        this.coverUrl = coverUrl;
        this.bangumiTitle = bangumiTitle;
        this.points = points;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public String getBangumiTitle() {
        return bangumiTitle;
    }

    public String getPoints() {
        return points;
    }
}


