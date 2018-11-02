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
    public static void generateGjjOcrSample(String imagePath) throws IOException {
        BufferedImage image = ImageIO.read(new File(imagePath));
        image = ImageHandleUtil.binaryImage(image, 180);
        generateGjjOcrSample(image);
    }

    public static void generateGjjOcrSample(BufferedImage image) throws IOException {
        List<FontRange> fontRanges = Division.divideFont(image);
        String date = LocalDate.now().toString().replace("-", "");
        String preFilePath = "E:\\" + date + "\\" + date;
        for (int i = 0; i < fontRanges.size(); i++) {
            BufferedImage fontImage = image.getSubimage(fontRanges.get(i).getX1(), fontRanges.get(i).getY1(), fontRanges.get(i).getWidth(), fontRanges.get(i).getHeight());
            ImageIO.write(fontImage, "png", new File(preFilePath + +i + ".png"));
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\B-0036\\Desktop\\ocr\\huangshi\\huangshi_3.png"));
        image = ImageHandleUtil.binaryImage(image, 180);
        image = ImageHandleUtil.removeBothEnds(image);
        image = ImageHandleUtil.optimizeColumnSpace(image,1,2,3);
        generateGjjOcrSample(image);
    }
}
