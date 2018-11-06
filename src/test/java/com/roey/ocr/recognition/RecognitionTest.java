package com.roey.ocr.recognition;

import com.roey.ocr.util.ImageHandleUtil;
import org.junit.Test;

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
public class RecognitionTest {

    @Test
    public void recognizeTable() throws IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\B-0036\\Desktop\\ocr\\huangshi\\huangshi_10.png"));
        image = ImageHandleUtil.binaryImage(image, 180);
        image = ImageHandleUtil.removeBothEnds(image);
        image = ImageHandleUtil.optimizeColumnSpace(image, 1, 2, 3);
//        ImageShowUtil.img(image);
        Recognition recognition = new Recognition();
        recognition.loadSample();
        List<List<String>> lists = recognition.recognizeTable(image);
        for (int i = 0; i < lists.size(); i++) {
            System.out.println(lists.get(i));
        }
    }
}
