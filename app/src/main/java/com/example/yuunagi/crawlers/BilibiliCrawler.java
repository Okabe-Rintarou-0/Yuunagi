package com.example.yuunagi.crawlers;

import android.annotation.SuppressLint;
import android.icu.util.LocaleData;
import android.util.Log;

import com.example.yuunagi.R;
import com.example.yuunagi.entity.Rank;
import com.example.yuunagi.utils.BVAVDecipher;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BilibiliCrawler {
    private final String fansUrl = "https://space.bilibili.com/%S/fans/fans";

    private final String rankUrl = "https://www.bilibili.com/v/popular/rank/all";

    private final String searchUserUrl = "https://search.bilibili.com/upuser?keyword=%S&from_source=web_search";

    private final String searchVideoUrl = "https://search.bilibili.com/all?keyword=%S&page=%d";

    private final String searchApiUrl = "https://api.bilibili.com/x/web-interface/search/type?search_type=video&keyword=%S&page=%d";

    private static BilibiliCrawler bilibiliCrawler;

    private List<Rank> rankList;

    private List<String> BvIdList;

    private String fansNumber = "0";

    private String iconUrl = "";

    private Integer uid;

    private String username;

    private Map<String, String> videoInfo;

    private List<Map<String, String>> videoInfoList;

    private BilibiliCrawler() {
    }

    public static BilibiliCrawler getInstance() {
        if (bilibiliCrawler == null)
            bilibiliCrawler = new BilibiliCrawler();
        return bilibiliCrawler;
    }

    public void crawlUserInfo(final Integer uid) throws IOException, InterruptedException {
        List<Thread> threads = new ArrayList<>();
        rankList = null;
        rankList = new ArrayList<>();
        threads.add(new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = Jsoup.connect(String.format(fansUrl, uid.toString()));
                connection.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
                try {
                    Document document = connection.get();
                    String _iconUrl = document.select("link[rel=\"apple-touch-icon\"]").attr("href");
//                    Log.d("iconUrl", _iconUrl);
                    iconUrl = _iconUrl;
                    String[] personalInfo = document.select("meta[name=\"description\"]").attr("content").split("；");
                    Pattern pattern = Pattern.compile("[0-9]+");
//                    Log.d("personal", document.select("meta[name=\"description\"]").attr("content"));
                    username = personalInfo[0].endsWith("，") ?
                            personalInfo[0].substring(4, personalInfo[0].length() - 1) : personalInfo[0].substring(4);
                    int fansIndex;
                    for (fansIndex = 1; fansIndex < personalInfo.length; ++fansIndex) {
                        if (!personalInfo[fansIndex].contains("粉丝数：")) {
                            continue;
                        }
                        break;
                    }
//                    Log.d("username", username);
                    Matcher matcher = pattern.matcher(personalInfo[fansIndex]);
                    if (matcher.find()) {
                        fansNumber = matcher.group(0);
                    } else fansNumber = "0";
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
        for (Thread thread : threads) {
            thread.start();
            thread.join();
        }
//        Log.d("fansNumber", fansNumber.toString());
    }

    public void crawlUidByUsername(final String username) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = Jsoup.connect(String.format(searchUserUrl, username));
                connection.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
                try {
                    Document document = connection.get();
                    String personalZoneUrl = document.select("div.headline a").attr("href");
                    String uidStr = Pattern.compile("[^0-9]").matcher(personalZoneUrl).replaceAll("");
                    if (!uidStr.isEmpty())
                        uid = Integer.parseInt(uidStr);
                    else uid = 1;
                } catch (IOException e) {
                    uid = 1;
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
    }

    public void crawlRankInfo() throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Connection connection = Jsoup.connect(rankUrl);
                connection.header("User-Agent", "User-Agent:Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0;");
                try {
                    Document document = connection.get();
                    List<Element> content = document.select("div[class=\"content\"] span");
                    Log.d("content", document.html());
                    for (int i = 0; i < 30; i += 3) {
//                        Rank basicInfo = new Rank();
//                        basicInfo.put("play", content.get(i).text());
//                        basicInfo.put("commenting", content.get(i + 1).text());
//                        basicInfo.put("author", content.get(i + 2).text());
//                        rankList.add(basicInfo);
                    }
                    content = document.select("div[class=\"content\"] a[class=\"title\"]");
                    for (int i = 0; i < 10; ++i) ;
//                        rankList.get(i).put("title", content.get(i).text());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
    }

    public void crawCoverUrl(final String keyword, final Integer pageIndex) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                videoInfoList = null;
                videoInfoList = new ArrayList<>();
                @SuppressLint("DefaultLocale") Connection connection = Jsoup.connect(String.format(searchApiUrl, keyword, pageIndex));
                connection
                        .header("Accept", "*/*")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                        .header("Content-Type", "application/json;charset=UTF-8")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                        .timeout(10000).ignoreContentType(true);
                try {
                    connection.get();
                    Connection.Response response = null;
                    try {
                        response = connection.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String body = response.body();
//                Log.d("bvid", BvId);
                    JSONObject jsonObject = JSONObject.fromObject(body);
                    JSONObject data = JSONObject.fromObject(jsonObject.get("data"));
                    JSONObject result = JSONObject.fromObject(JSONArray.fromObject(data.get("result")).get(0));
                    Log.d("title", result.getString("title"));
                    String title = result.getString("title");
                    String tag = String.format("<em class=\"keyword\">%S</em>", keyword);
                    String realTitle = title.replaceAll(tag,keyword);
                    Log.d("cover", result.getString("pic"));
                    Log.d("bvId", result.getString("bvid"));
                    videoInfo = new HashMap<>();
                    videoInfo.put("cover", "http:" + result.getString("pic"));
                    videoInfo.put("bvId", result.getString("bvid"));
                    videoInfo.put("title", realTitle);
                    videoInfoList.add(videoInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
    }

    public void crawlVideoInfoBvId(final String BvId) throws IOException, InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                videoInfo = null;
                videoInfo = new HashMap<>();
                BVAVDecipher decipher = BVAVDecipher.getInstance();
                String videoUrl = "https://api.bilibili.com/x/web-interface/view?aid=" + decipher.dec(BvId).toString();
                Connection connection = Jsoup.connect(videoUrl);
                connection
                        .header("Accept", "*/*")
                        .header("Accept-Encoding", "gzip, deflate")
                        .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                        .header("Content-Type", "application/json;charset=UTF-8")
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                        .timeout(10000).ignoreContentType(true);
                Connection.Response response = null;
                try {
                    response = connection.execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String body = response.body();
//                Log.d("bvid", BvId);
                JSONObject jsonObject = JSONObject.fromObject(body);
                JSONObject data = JSONObject.fromObject(jsonObject.getString("data"));
//                Log.d("data", data.toString());
                videoInfo.put("cover", data.getString("pic"));
                videoInfo.put("title", data.getString("title"));
            }
        });
        thread.start();
        thread.join();
    }

    public void crawlVideoBVIdUrl(final String keyword, final Integer thisIndex) throws InterruptedException {
        BvIdList = null;
        BvIdList = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> videoUrlList = new ArrayList<>();
                videoUrlList = new ArrayList<>();
                Connection connection = Jsoup.connect(String.format(searchVideoUrl, keyword, thisIndex));
                connection.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0");
                try {
                    Document document = connection.get();
                    List<Element> aTagList = document.select("a.img-anchor[href*='BV']");
                    for (Element aTag : aTagList) {
                        String coverUrl = aTag.attr("href");
                        videoUrlList.add(coverUrl);
//                        Log.d("coverUrl", coverUrl);
                    }
                    for (String videoUrl : videoUrlList) {
                        Pattern pattern = Pattern.compile("BV\\w+");
                        Matcher matcher = pattern.matcher(videoUrl);
                        if (matcher.find()) {
//                            Log.d("find", matcher.group(0));
                            BvIdList.add(matcher.group(0));
                        }
                    }
                } catch (IOException e) {
                    videoUrlList.clear();
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getFansNumber() {
        return fansNumber;
    }

    public List<Rank> getRankList() {
        return rankList;
    }

    public String getUsername() {
        return username;
    }

    public Integer getUid() {
        return uid;
    }

    public List<String> getBvIdList() {
        return BvIdList;
    }

    public Map<String, String> getVideoInfo() {
        return videoInfo;
    }

    public List<Map<String, String>> getVideoInfoList() {
        return videoInfoList;
    }
}
