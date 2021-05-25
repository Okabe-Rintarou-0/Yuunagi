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

import lecho.lib.hellocharts.model.PointValue;

public class BilibiliCrawler {
    private final String fansUrl = "https://space.bilibili.com/%S/fans/fans";

    private final String rankUrl = "https://api.bilibili.com/pgc/web/rank/list?day=3&season_type=1";

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

    public void addToFavourite(String bvId) throws IOException, InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String postUrl = "https://api.bilibili.com/x/v3/fav/resource/deal";
                Connection connection = Jsoup.connect(postUrl);
                connection
                        .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0")
                        .data("rid", BVAVDecipher.getInstance().dec(bvId).toString())
                        .data("type", "2")
                        .data("add_media_ids", "53860237")
                        .data("jsonp", "jsonp")
                        .data("csrf", "e674d724904a5c1f453729af3bdaf750")
                        .data("platform", "web")
                        .header("cookie", "_uuid=B4812012-59F7-C0CA-2A2A-3D16F8C1464D61603infoc; buvid3=320B3CF2-3BBD-4601-984B-5D88A0B80B52184999infoc; sid=aacfu6oa; buvid_fp=320B3CF2-3BBD-4601-984B-5D88A0B80B52184999infoc; CURRENT_FNVAL=80; blackside_state=1; rpdid=|(mmRmYm)lm0J'uYul)Y))~); LIVE_BUVID=AUTO5516169314895366; bp_t_offset_5200237=518623270998973597; fingerprint3=e05487c2f8b960fc96e87d1bf7a57183; fingerprint_s=30d4dc5fc51c30ce1f1b7ff8de718a69; bsource=search_baidu; PVID=6; bfe_id=fdfaf33a01b88dd4692ca80f00c2de7f; bp_video_offset_5200237=525038831160283296; fingerprint=94dc83cf5cb33dba53c1b9db5489b730; buvid_fp_plain=93A2B277-AD7E-4547-9987-85362885202D58500infoc; DedeUserID=5200237; DedeUserID__ckMd5=0edb78f23ca63f84; SESSDATA=8d8d6ec6%2C1636635616%2Ca4574*51; bili_jct=e674d724904a5c1f453729af3bdaf750")
                        .ignoreContentType(true);
                try {
                    Document document = connection.post();
                    Log.d("result", document.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
    }

    public void deleteFromFavourite(String bvId) throws IOException, InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String postUrl = "https://api.bilibili.com/x/v3/fav/resource/deal";
                Connection connection = Jsoup.connect(postUrl);
                connection
                        .header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:32.0) Gecko/    20100101 Firefox/32.0")
                        .data("rid", BVAVDecipher.getInstance().dec(bvId).toString())
                        .data("type", "2")
                        .data("del_media_ids", "53860237")
                        .data("jsonp", "jsonp")
                        .data("csrf", "e674d724904a5c1f453729af3bdaf750")
                        .data("platform", "web")
                        .header("cookie", "_uuid=B4812012-59F7-C0CA-2A2A-3D16F8C1464D61603infoc; buvid3=320B3CF2-3BBD-4601-984B-5D88A0B80B52184999infoc; sid=aacfu6oa; buvid_fp=320B3CF2-3BBD-4601-984B-5D88A0B80B52184999infoc; CURRENT_FNVAL=80; blackside_state=1; rpdid=|(mmRmYm)lm0J'uYul)Y))~); LIVE_BUVID=AUTO5516169314895366; bp_t_offset_5200237=518623270998973597; fingerprint3=e05487c2f8b960fc96e87d1bf7a57183; fingerprint_s=30d4dc5fc51c30ce1f1b7ff8de718a69; bsource=search_baidu; PVID=6; bfe_id=fdfaf33a01b88dd4692ca80f00c2de7f; bp_video_offset_5200237=525038831160283296; fingerprint=94dc83cf5cb33dba53c1b9db5489b730; buvid_fp_plain=93A2B277-AD7E-4547-9987-85362885202D58500infoc; DedeUserID=5200237; DedeUserID__ckMd5=0edb78f23ca63f84; SESSDATA=8d8d6ec6%2C1636635616%2Ca4574*51; bili_jct=e674d724904a5c1f453729af3bdaf750")
                        .ignoreContentType(true);
                try {
                    Document document = connection.post();
                    Log.d("result", document.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        thread.join();
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
                    Document document;
                    document = connection.get();
                    String _iconUrl = document.select("link[rel=\"apple-touch-icon\"]").attr("href");
//                    Log.d("document", document.html());
                    iconUrl = _iconUrl;
                    String[] personalInfo = document.select("meta[name=\"description\"]").attr("content").split("。");
                    Pattern usernamePattern = Pattern.compile(".*，");
                    Matcher matcher = usernamePattern.matcher(personalInfo[0]);
                    if (matcher.find()) {
                        username = matcher.group(0);
                        username = username.substring(0, username.length() - 1);
                    } else username = "null";
                    Log.d("username", username);
                    fansNumber = "0";
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
                rankList = null;
                rankList = new ArrayList<>();
                Connection connection = Jsoup.connect(rankUrl);
                try {
                    connection
                            .header("Accept", "*/*")
                            .header("Accept-Encoding", "gzip, deflate")
                            .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                            .header("Content-Type", "application/json;charset=UTF-8")
                            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0")
                            .timeout(10000).ignoreContentType(true);
                    Connection.Response response = connection.execute();
                    JSONObject json = JSONObject.fromObject(response.body());
                    JSONObject result = JSONObject.fromObject(json.get("result"));
                    JSONArray list = JSONArray.fromObject(result.get("list"));
                    for (int i = 0; i < 5; ++i) {
                        Object item = list.get(i);
                        JSONObject bangumi = JSONObject.fromObject(item);
                        String cover = bangumi.getString("cover");
                        String points = bangumi.getString("pts");
                        String title = bangumi.getString("title");
                        rankList.add(new Rank(cover, title, points));
                    }
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
                    Connection.Response response = connection.execute();
                    String body = response.body();
//                Log.d("bvid", BvId);
                    JSONObject jsonObject = JSONObject.fromObject(body);
                    JSONObject data = JSONObject.fromObject(jsonObject.get("data"));
                    JSONObject result = JSONObject.fromObject(JSONArray.fromObject(data.get("result")).get(0));
                    Log.d("title", result.getString("title"));
                    String title = result.getString("title");
                    String tag = String.format("<em class=\"keyword\">%S</em>", keyword);
                    String realTitle = title.replaceAll(tag, keyword);
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
