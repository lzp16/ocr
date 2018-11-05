package com.roey.ocr.sample;

import com.roey.ocr.entity.FontRange;
import com.roey.ocr.preprocess.Division;
import com.roey.ocr.util.ImageHandleUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/10/24 16:56
 **/
public class SampleGenerate {

    public static void main(String[] args) throws IOException {
        generateShenYueSample("C:\\Users\\B-0036\\Desktop\\ocr\\huangshi\\huangshi_7.png", 180, 1, 2, 3);
//        generateYzmSample("C:\\Users\\B-0036\\Desktop\\yzm1.jpg", 120, "test");
    }

    public static void generateYzmSample(String imagePath, int grayBoundary, String tag) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        image = ImageHandleUtil.binaryImage(image, grayBoundary);
        String date = LocalDate.now().toString().replace("-", "");
        String dirPath = "E:\\yzm\\" + tag + "\\" + date + "\\" + date;
        generateGjjOcrSample(image, dirPath, "jpg");
    }

    public static void generateShenYueSample(String imagePath, int grayBoundary, int... columnIndexes) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        image = ImageHandleUtil.binaryImage(image, grayBoundary);
        image = ImageHandleUtil.removeBothEnds(image);
        image = ImageHandleUtil.optimizeColumnSpace(image, columnIndexes);
        String date = LocalDate.now().toString().replace("-", "");
        String dirPath = "E:\\shenyue\\" + date + "\\";
        generateGjjOcrSample(image, dirPath, "png");
    }


    public static void generateGjjOcrSample(BufferedImage image, String dirPath, String imageType) throws IOException {
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        List<FontRange> fontRanges = Division.divideFont(image);
        for (int i = 0; i < fontRanges.size(); i++) {
            BufferedImage fontImage = image.getSubimage(fontRanges.get(i).getX1(), fontRanges.get(i).getY1(), fontRanges.get(i).getWidth(), fontRanges.get(i).getHeight());
            ImageIO.write(fontImage, imageType, new File(dirPath + +i + "." + imageType));
        }
    }
}
