package com.roey.ocr.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/11/5 10:48
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleGenerateTest {

    @Autowired
    private SampleGenerate sampleGenerate;

    @Test
    public void generateShenYueSample1() throws IOException {
//        sampleGenerate.generateShenYueSample("C:\\Users\\B-0036\\Desktop\\ocr\\huangshi\\huangshi_7.png", 180, 1, 2, 3);
        sampleGenerate.generateShenYueSample("C:\\Users\\15886\\Desktop\\无数据.png", 180);
    }

    @Test
    public void generateYzmSample() throws IOException {
        sampleGenerate.generateYzmSample("C:\\Users\\B-0036\\Desktop\\yzm1.jpg", 120, "test");
    }
}
