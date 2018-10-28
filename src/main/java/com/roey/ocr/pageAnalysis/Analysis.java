package com.roey.ocr.pageAnalysis;

import com.roey.ocr.entity.Cell;
import com.roey.ocr.entity.FontRange;
import com.roey.ocr.preprocess.Division;
import com.roey.ocr.simple.SimpleLoad;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.roey.ocr.algorithm.PixelContrast.contrastPixel;
import static com.roey.ocr.util.CommonUtil.extMatrix;
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
        int x1 = fontRange.getX1();
        int y1 = fontRange.getY1();
        int x2 = fontRange.getX2();
        int y2 = fontRange.getY2();
        BufferedImage fontImage = image.getSubimage(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
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
        int[][] a = new int[3][3];
        a[0][0] = 1;
        a[0][1] = 0;
        a[0][2] = 1;

        a[1][0] = 1;
        a[1][1] = 0;
        a[1][2] = 1;

        a[2][0] = 1;
        a[2][1] = 0;
        a[2][2] = 1;

        int[][] ints = extMatrix(a, 5, 4);
        for (int i = 0; i < ints.length; i++) {
            System.out.println(Arrays.toString(ints[i]));
        }

//        loadSimpleData();
//        BufferedImage image = ImageIO.read(new File("C:\\Users\\LiZhanPing\\Desktop\\ocr\\huangshi\\huangshi_2.png"));
////        BufferedImage image = ImageIO.read(new File("E:\\chifeng_1.png"));
//        image = ImageHandleUtil.binaryImage(image, 180);
//        ImageShowUtil.img(image);
//        List<List<String>> lists = analysisTable(image);
//        for (int i = 0; i < lists.size(); i++) {
//            System.out.println(lists.get(i));
//        }
    }
}
