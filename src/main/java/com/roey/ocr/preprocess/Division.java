package com.roey.ocr.preprocess;

import com.roey.ocr.entity.Cell;
import com.roey.ocr.entity.CharArea;
import com.roey.ocr.util.ImageHandleUtil;
import com.roey.ocr.util.ImageShowUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
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

    public static List<CharArea> divideChar(BufferedImage image) {
        int[] horizontalProjections = ImageHandleUtil.imageProjection(image, ImageHandleUtil.HORIZONTAL);
        int minCharHeight = getMinWaveLen(horizontalProjections, 1, 1);
        int minRowSpace = getMinSpaceLen(horizontalProjections, 0, 0);
        LinkedHashMap<Integer, Integer> verticalCharacterBorder = ImageHandleUtil.divideProjectionWave(horizontalProjections, ImageHandleUtil.DEF_MIN_RANGE, minCharHeight, minRowSpace);
        List<CharArea> list = new ArrayList<>();
        verticalCharacterBorder.forEach((k, v) -> {
            int[] verticalProjections = ImageHandleUtil.imageProjection(image.getSubimage(0, k, image.getWidth(), v - k + 1), 1);
            int minCharWidth = getMinWaveLen(verticalProjections, 1, 1);
            int minCharSpace = getMinSpaceLen(verticalProjections, 0, 1);
            LinkedHashMap<Integer, Integer> horizontalCharacterBorder = ImageHandleUtil.divideProjectionWave(verticalProjections, ImageHandleUtil.DEF_MIN_RANGE, minCharWidth, minCharSpace);
            horizontalCharacterBorder.forEach((key, value) -> {
                CharArea charArea = new CharArea();
                charArea.setX1(key);
                charArea.setX2(value);
                charArea.setY1(k);
                charArea.setY2(v);
                list.add(charArea);
            });
        });
        return list;
    }

    public static List<Cell> divideCell(BufferedImage image) {
        List<Cell> cells = new ArrayList<>();
        int[] horizontalProjections = ImageHandleUtil.imageProjection(image, ImageHandleUtil.HORIZONTAL);
        int minCharHeight = getMinWaveLen(horizontalProjections, 1, 1);
        int minRowSpace = getMinSpaceLen(horizontalProjections, 0, 0);
        List<Integer> horizontalWaveRange = ImageHandleUtil.divideProjectionWave2(horizontalProjections, ImageHandleUtil.DEF_MIN_RANGE, minCharHeight, minRowSpace);
        int maxCharWidth = 0;
        int minCharWidth = 0;
        int mdCharWidth = 0;
        int minCharSpace = 0;
        int minColumnSpace = 0;
        for (int i = 0; i < horizontalWaveRange.size(); i = i + 2) {
            int[] verticalProjections = ImageHandleUtil.imageProjection(image.getSubimage(0, horizontalWaveRange.get(i), image.getWidth(), horizontalWaveRange.get(i + 1) - horizontalWaveRange.get(i) + 1), ImageHandleUtil.VERTICAL);
            maxCharWidth = getMaxWaveLen(verticalProjections, 1, Integer.MAX_VALUE);
            minCharWidth = getMinWaveLen(verticalProjections, 1, 1);
            mdCharWidth = (maxCharWidth + minCharWidth) / 2;
            minCharSpace = getMinSpaceLen(verticalProjections, 0, 0);
            minColumnSpace = getMinSpaceLen(verticalProjections, 0, mdCharWidth);
            List<List<Integer>> verticalWaveRange = ImageHandleUtil.divideProjectionWaveExt(verticalProjections, ImageHandleUtil.DEF_MIN_RANGE, minCharWidth, minCharSpace, minColumnSpace);
            for (int j = 0; j < verticalWaveRange.size(); j++) {
                Cell cell = new Cell();
                cell.setRowNum(i / 2);
                cell.setColNum(j);
                List<CharArea> values = new ArrayList<>();
                for (int k = 0; k < verticalWaveRange.get(j).size(); k = k + 2) {
                    CharArea charArea = new CharArea();
                    charArea.setX1(verticalWaveRange.get(j).get(k));
                    charArea.setX2(verticalWaveRange.get(j).get(k + 1));
                    charArea.setY1(horizontalWaveRange.get(i));
                    charArea.setY2(horizontalWaveRange.get(i + 1));
                    values.add(charArea);
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
        List<CharArea> divides = divideChar(image);
        Graphics graphics = image.getGraphics();
        graphics.setColor(Color.RED);
        divides.forEach(charArea -> {
            graphics.drawLine(charArea.getX1(), charArea.getY1(), charArea.getX2(), charArea.getY1());
            graphics.drawLine(charArea.getX1(), charArea.getY1(), charArea.getX1(), charArea.getY2());
            graphics.drawLine(charArea.getX2(), charArea.getY2(), charArea.getX2(), charArea.getY1());
            graphics.drawLine(charArea.getX2(), charArea.getY2(), charArea.getX1(), charArea.getY2());
        });
        graphics.dispose();
        ImageShowUtil.img(image);
    }
}
