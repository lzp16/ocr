package com.roey.ocr.postprocess;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/11/2 12:14
 **/
public class Compose {
    public static String composeFont(String str) {
        return str.replace("sandianshuifang", "汇").replace("heduo", "移");
    }
}
