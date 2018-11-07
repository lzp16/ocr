package com.roey.ocr.postprocess;

import org.springframework.stereotype.Component;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/11/2 12:14
 **/
@Component
public class Compose {
    public String composeChar(String str) {
        return str.replace("sandianshuifang", "汇").replace("heduo", "移").replace("yibo", "补");
    }
}
