package com.example.yuunagi.utils;

import java.util.HashMap;
import java.util.Map;

public class BVAVDecipher {
    final String table = "fZodR9XQDSUm21yCkr6zBqiveYah8bt4xsWpHnJE7jL5VG3guMTKNPAwcF";
    final Integer[] s = {11, 10, 3, 8, 4, 6};
    final Long xor = 177451812L;
    final Long add = 8728348608L;
    private Map<Character, Long> tr;
    static BVAVDecipher decipher;

    public static BVAVDecipher getInstance() {
        if (decipher == null)
            decipher = new BVAVDecipher();
        return decipher;
    }

    private BVAVDecipher() {
        tr = new HashMap<>();
        for (int i = 0; i < 58; ++i)
            tr.put(table.charAt(i), (long) i);
    }

    public Long dec(String bvId) {
        long r = 0L;
        for (int i = 0; i < 6; ++i) {
            r += tr.get(bvId.charAt(s[i])) * (long) Math.pow(58, i);
        }
        return (r - add) ^ xor;
    }

    public String enc(Long avId) {
        avId = (avId ^ xor) + add;
        StringBuffer r = new StringBuffer("BV1  4 1 7  ");
        for (int i = 0; i < 6; ++i) {
            r.setCharAt(s[i], table.charAt((int) (avId /(long) Math.pow(58, i) % 58)));
        }
        return r.toString();
    }

}

/*
def enc(x):
    x = (x ^ xor) + add
    r = list('BV1  4 1 7  ')
    for i in range(6):
        r[s[i]] = table[x // 58 ** i % 58]
    return ''.join(r)
 */