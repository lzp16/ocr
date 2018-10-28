package com.roey.ocr.pageAnalysis;

import com.roey.ocr.entity.Cell;
import com.roey.ocr.entity.FontRange;
import com.roey.ocr.preprocess.Division;
import com.roey.ocr.simple.SimpleLoad;
import com.roey.ocr.util.ImageHandleUtil;
import com.roey.ocr.util.ImageShowUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.roey.ocr.algorithm.PixelContrast.contrastPixel;
import static com.roey.ocr.util.ImageHandleUtil.getFontImageMatrix;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/10/24 16:17
 **/
public class Analysis {

    public static Map<String, List<int[][]>> simpleMap = SimpleLoad.loadSimpleData();

    public static List<List<String>> analysisTable(BufferedImage image) {
        List<List<String>> result = new ArrayList<>();
        List<Cell> cells = Division.divideCell(image);
        List<String> rows = new ArrayList<>();
        for (int i = 0; i < cells.size(); i++) {
            if (i != 0 && cells.get(i).getRowNum() != cells.get(i - 1).getRowNum()) {
                result.add(rows);
                rows = new ArrayList<>();
            }
            StringBuilder values = new StringBuilder();
            for (FontRange fontRange : cells.get(i).getValues()) {
                String value = analysisFont(image, fontRange);
                values.append(value);
            }
            rows.add(values.toString());
        }
        return result;
    }

    public static String analysisFont(BufferedImage image, FontRange fontRange) {
        BufferedImage fontImage = image.getSubimage(fontRange.getX1(), fontRange.getY1(), fontRange.getWidth(), fontRange.getHeight());
//        ImageShowUtil.img(fontImage);
        int[][] unknownFontData = getFontImageMatrix(fontImage);
        int score = 0;
        String result = "";
        int lastScore = Integer.MAX_VALUE;
        for (Map.Entry<String, List<int[][]>> entry : simpleMap.entrySet()) {
//            System.out.println(">>>>>>>>>" + entry.getKey() + ">>>>>>>>>>>");
            score = contrastPixel(entry.getValue().get(0), unknownFontData);
//            System.out.println("<<<<<<<<<" + score + "<<<<<<<<<<<");
            if (lastScore > score) {
//                if(score>80){
//                    continue;
//                }
                result = entry.getKey();
                lastScore = score;
            }
        }
        System.out.println("<<<<<<<<<" + score + "<<<<<<<<<<<");
        System.out.println("<<<<<<<<<" + result + "<<<<<<<<<<<");
        return result;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\LiZhanPing\\Desktop\\ocr\\huangshi\\huangshi_2.png"));
//        BufferedImage image = ImageIO.read(new File("E:\\chifeng_1.png"));
        image = ImageHandleUtil.binaryImage(image, 180);
        ImageShowUtil.img(image);
        List<List<String>> lists = analysisTable(image);
        for (int i = 0; i < lists.size(); i++) {
            System.out.println(lists.get(i));
        }
    }
}
