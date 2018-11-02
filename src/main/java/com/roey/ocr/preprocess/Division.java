package com.roey.ocr.preprocess;

import com.roey.ocr.entity.Cell;
import com.roey.ocr.entity.FontRange;
import com.roey.ocr.util.ImageHandleUtil;
import com.roey.ocr.util.ImageShowUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/10/11 17:28
 **/
public class Division {

    public static List<FontRange> divideFont(BufferedImage image) {
        int[] horizontalProjections = ImageHandleUtil.imageProjection(image, ImageHandleUtil.HORIZONTAL);
        int minFontHeight = getMinWaveLen(horizontalProjections, 1, 1);
        int minRowSpace = getMinSpaceLen(horizontalProjections, 0, 0);
        LinkedHashMap<Integer, Integer> verticalCharacterBorder = ImageHandleUtil.divideProjectionWave(horizontalProjections, ImageHandleUtil.DEF_MIN_RANGE, minFontHeight, minRowSpace);
        List<FontRange> list = new ArrayList<>();
        verticalCharacterBorder.forEach((k, v) -> {
            int[] verticalProjections = ImageHandleUtil.imageProjection(image.getSubimage(0, k, image.getWidth(), v - k + 1), 1);
            int minFontWidth = getMinWaveLen(verticalProjections, 1, 1);
            int minFontSpace = getMinSpaceLen(verticalProjections, 0, 1);
            LinkedHashMap<Integer, Integer> horizontalCharacterBorder = ImageHandleUtil.divideProjectionWave(verticalProjections, ImageHandleUtil.DEF_MIN_RANGE, minFontWidth, minFontSpace);
            horizontalCharacterBorder.forEach((key, value) -> {
                FontRange fontRange = new FontRange();
                fontRange.setX1(key);
                fontRange.setX2(value);
                fontRange.setY1(k);
                fontRange.setY2(v);
                list.add(fontRange);
            });
        });
        return list;
    }

    public static List<Cell> divideCell(BufferedImage image) {
        List<Cell> cells = new ArrayList<>();
        int[] horizontalProjections = ImageHandleUtil.imageProjection(image, ImageHandleUtil.HORIZONTAL);
        int minFontHeight = getMinWaveLen(horizontalProjections, 1, 1);
        int minRowSpace = getMinSpaceLen(horizontalProjections, 0, 0);
        List<Integer> horizontalWaveRange = ImageHandleUtil.divideProjectionWave2(horizontalProjections, ImageHandleUtil.DEF_MIN_RANGE, minFontHeight, minRowSpace);
        int maxFontWidth = 0;
        int minFontWidth = 0;
        int mdFontWidth = 0;
        int minFontSpace = 0;
        int minColumnSpace = 0;
        for (int i = 0; i < horizontalWaveRange.size(); i = i + 2) {
            int[] verticalProjections = ImageHandleUtil.imageProjection(image.getSubimage(0, horizontalWaveRange.get(i), image.getWidth(), horizontalWaveRange.get(i + 1) - horizontalWaveRange.get(i) + 1), ImageHandleUtil.VERTICAL);
            maxFontWidth = getMaxWaveLen(verticalProjections, 1, Integer.MAX_VALUE);
            minFontWidth = getMinWaveLen(verticalProjections, 1, 1);
            mdFontWidth = (maxFontWidth + minFontWidth) / 2;
            minFontSpace = getMinSpaceLen(verticalProjections, 0, 0);
            minColumnSpace = getMinSpaceLen(verticalProjections, 0, mdFontWidth);
            List<List<Integer>> verticalWaveRange = ImageHandleUtil.divideProjectionWaveExt(verticalProjections, ImageHandleUtil.DEF_MIN_RANGE, minFontWidth, minFontSpace, minColumnSpace);
            for (int j = 0; j < verticalWaveRange.size(); j++) {
                Cell cell = new Cell();
                cell.setRowNum(i / 2);
                cell.setColNum(j);
                List<FontRange> values = new ArrayList<>();
                for (int k = 0; k < verticalWaveRange.get(j).size(); k = k + 2) {
                    FontRange fontRange = new FontRange();
                    fontRange.setX1(verticalWaveRange.get(j).get(k));
                    fontRange.setX2(verticalWaveRange.get(j).get(k + 1));
                    fontRange.setY1(horizontalWaveRange.get(i));
                    fontRange.setY2(horizontalWaveRange.get(i + 1));
                    values.add(fontRange);
                }
                cell.setValues(values);
                cells.add(cell);
            }
        }
        return cells;
    }

    /**
     * 获取数组中指定数字相连的最小长度，可以指定最小值的边界且最小值是大于边界值的
     *
     * @param array    数组
     * @param boundary 最小长度的边界
     * @return
     */
    public static int getMinWaveLen(int[] array, int specNum, int boundary) {
        int minLen = Integer.MAX_VALUE;
        int temp = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] >= specNum) {
                temp++;
            } else {
                if (temp >= boundary && temp < minLen) {
                    minLen = temp;
                }
                temp = 0;
            }
        }
        return minLen;
    }

    public static int getMinSpaceLen(int[] array, int specNum, int boundary) {
        int minLen = Integer.MAX_VALUE;
        int temp = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] <= specNum) {
                temp++;
            } else {
                if (temp >= boundary && temp < minLen) {
                    minLen = temp;
                }
                temp = 0;
            }
        }
        return minLen;
    }


    public static int getMaxWaveLen(int[] array, int specNum, int boundary) {
        int maxLen = 0;
        int temp = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] >= specNum) {
                temp++;
            } else {
                if (temp <= boundary && temp > maxLen) {
                    maxLen = temp;
                }
                temp = 0;
            }
        }
        return maxLen;
    }

    public static int getMaxSpaceLen(int[] array, int specNum, int boundary) {
        int maxLen = 0;
        int temp = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] <= 0) {
                temp++;
            } else {
                if (temp <= boundary && temp > maxLen) {
                    maxLen = temp;
                }
                temp = 0;
            }
        }
        return maxLen;
    }

    public static int getMarginLen(int[] array, boolean isLeft) {
        int temp = 0;
        int length = array.length;
        for (int i = 0; i < array.length; i++) {
            if (isLeft) {
                if (array[i + 1] == array[i]) {
                    temp++;
                } else {
                    break;
                }
            } else {
                if (array[length - i] == array[i]) {
                    temp++;
                } else {
                    break;
                }
            }
        }
        return temp;
    }

    public static void showDivision(BufferedImage image) {
        List<FontRange> divides = divideFont(image);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.RED);
        divides.forEach(fontRange -> {
            graphics.drawLine(fontRange.getX1(), fontRange.getY1(), fontRange.getX2(), fontRange.getY1());
            graphics.drawLine(fontRange.getX1(), fontRange.getY1(), fontRange.getX1(), fontRange.getY2());
            graphics.drawLine(fontRange.getX2(), fontRange.getY2(), fontRange.getX2(), fontRange.getY1());
            graphics.drawLine(fontRange.getX2(), fontRange.getY2(), fontRange.getX1(), fontRange.getY2());
        });
        graphics.dispose();
        ImageShowUtil.img(image);
    }

    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\B-0036\\Desktop\\ocr\\chifeng\\chifeng_1.png"));
        image = ImageHandleUtil.binaryImage(image, 180);
        showDivision(image);
    }
}
