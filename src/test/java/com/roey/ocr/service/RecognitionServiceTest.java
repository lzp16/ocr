package com.roey.ocr.service;

import com.alibaba.fastjson.JSONObject;
import com.roey.ocr.util.ImageHandleUtil;
import com.roey.ocr.util.ImageShowUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/10/31 17:26
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecognitionServiceTest {

    @Autowired
    private RecognitionService recognitionService;

    @Test
    public void recognizeTable() throws IOException {
//        BufferedImage image = ImageIO.read(new File("C:\\Users\\B-0036\\Desktop\\ocr\\huangshi\\huangshi_10.png"));
        BufferedImage image = ImageIO.read(new File("C:\\Users\\15886\\Desktop\\liaoyuan.png"));
        image = ImageHandleUtil.binaryImage(image, 180);
        image = ImageHandleUtil.removeBothEnds(image);
        image = ImageHandleUtil.optimizeColumnSpace(image, 1, 2, 3);
        ImageIO.write(image, "png", new File("C:\\Users\\15886\\Desktop\\ly.png"));
        ImageShowUtil.img(image);
        List<List<String>> lists = recognitionService.recognizeTable(image);
        for (int i = 0; i < lists.size(); i++) {
            log.info(JSONObject.toJSONString(lists.get(i)));
        }
    }
}
