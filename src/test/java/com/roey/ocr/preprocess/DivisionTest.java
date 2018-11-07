package com.roey.ocr.preprocess;

import com.roey.ocr.util.ImageHandleUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/11/5 10:42
 **/
public class DivisionTest {

    @Autowired
    private Division division;

    @Test
    public void showDivision() throws IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\B-0036\\Desktop\\ocr\\chifeng\\chifeng_1.png"));
        image = ImageHandleUtil.binaryImage(image, 180);
        division.showDivision(image);
    }
}
