package com.example.yuunagi;

import com.example.yuunagi.crawlers.BilibiliCrawler;
import com.example.yuunagi.utils.BVAVDecipher;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws InterruptedException {
        assertEquals(4, 2 + 2);
//        System.out.println(BVAVDecipher.getInstance().dec(BVAVDecipher.getInstance().enc(12323L)));
        BilibiliCrawler.getInstance().crawlVideoCoverUrl("斋藤飞鸟", 1);
    }
}