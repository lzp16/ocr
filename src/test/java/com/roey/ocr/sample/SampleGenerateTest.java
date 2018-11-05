package com.roey.ocr.sample;

import org.junit.Test;

import java.io.IOException;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/11/5 10:48
 **/
public class SampleGenerateTest {

    @Test
    public void generateShenYueSample1() throws IOException {
        SampleGenerate.generateShenYueSample("C:\\Users\\B-0036\\Desktop\\ocr\\huangshi\\huangshi_7.png", 180, 1, 2, 3);
    }

    @Test
    public void generateYzmSample() throws IOException {
        SampleGenerate.generateYzmSample("C:\\Users\\B-0036\\Desktop\\yzm1.jpg", 120, "test");
    }
}
