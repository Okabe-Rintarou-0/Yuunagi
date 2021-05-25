package com.example.yuunagi.crawlers;

import android.util.Log;

import java.io.IOException;

public class FansMonitor {
    private static FansMonitor fansMonitor;

    private static final String videoUrlFormat = "https://www.bilibili.com/video/%s";

    public static FansMonitor getInstance() {
        if (fansMonitor == null)
            fansMonitor = new FansMonitor();
        return fansMonitor;
    }

    public String crawFansNumber(final Integer uid) {
        BilibiliCrawler bilibiliCrawler = BilibiliCrawler.getInstance();
        try {
            bilibiliCrawler.crawlUserInfo(uid);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("fans", bilibiliCrawler.getFansNumber());
        return bilibiliCrawler.getFansNumber();
    }

}
