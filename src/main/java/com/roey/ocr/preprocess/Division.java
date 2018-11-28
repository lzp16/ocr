package com.roey.ocr.preprocess;

import com.roey.ocr.entity.Cell;
import com.roey.ocr.entity.CharArea;
import com.roey.ocr.util.ImageHandleUtil;
import com.roey.ocr.util.ImageShowUtil;
import com.roey.ocr.util.ProjectionUtil;
import org.springframework.stereotype.Component;

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
@Component
public class Division {

    public List<CharArea> divideChar(BufferedImage image) {
        int[] horizontalProjections = ImageHandleUtil.imageProjection(image, ImageHandleUtil.HORIZONTAL);
        int minCharHeight = ProjectionUtil.getMinWaveLen(horizontalProjections, 1, 1);
        int minRowSpace = ProjectionUtil.getMinSpaceLen(horizontalProjections, 0, 0);
        LinkedHashMap<Integer, Integer> verticalCharacterBorder = ProjectionUtil.divideProjectionWave(horizontalProjections, ImageHandleUtil.DEF_MIN_RANGE, minCharHeight, minRowSpace);
        List<CharArea> list = new ArrayList<>();
        verticalCharacterBorder.forEach((k, v) -> {
            int[] verticalProjections = ImageHandleUtil.imageProjection(image.getSubimage(0, k, image.getWidth(), v - k + 1), 1);
            int minCharWidth = ProjectionUtil.getMinWaveLen(verticalProjections, 1, 1);
            int minCharSpace = ProjectionUtil.getMinSpaceLen(verticalProjections, 0, 1);
            LinkedHashMap<Integer, Integer> horizontalCharacterBorder = ProjectionUtil.divideProjectionWave(verticalProjections, ImageHandleUtil.DEF_MIN_RANGE, minCharWidth, minCharSpace);
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

    public List<Cell> divideCell(BufferedImage image) {
        List<Cell> cells = new ArrayList<>();
        int[] horizontalProjections = ImageHandleUtil.imageProjection(image, ImageHandleUtil.HORIZONTAL);
        int minCharHeight = ProjectionUtil.getMinWaveLen(horizontalProjections, 1, 1);
        int minRowSpace = ProjectionUtil.getMinSpaceLen(horizontalProjections, 0, 0);
        List<Integer> horizontalWaveRange = ProjectionUtil.divideProjectionWave2(horizontalProjections, ImageHandleUtil.DEF_MIN_RANGE, minCharHeight, minRowSpace);
        for (int i = 0; i < horizontalWaveRange.size(); i = i + 2) {
            int[] verticalProjections = ImageHandleUtil.imageProjection(image.getSubimage(0, horizontalWaveRange.get(i), image.getWidth(), horizontalWaveRange.get(i + 1) - horizontalWaveRange.get(i) + 1), ImageHandleUtil.VERTICAL);
            int maxCharWidth = ProjectionUtil.getMaxWaveLen(verticalProjections, 1, Integer.MAX_VALUE);
            int minCharWidth = ProjectionUtil.getMinWaveLen(verticalProjections, 1, 1);
            int mdCharWidth = (maxCharWidth + minCharWidth) / 2;
            int minCharSpace = ProjectionUtil.getMinSpaceLen(verticalProjections, 0, 0);
            int minColumnSpace = ProjectionUtil.getMinSpaceLen(verticalProjections, 0, mdCharWidth);
            List<List<Integer>> verticalWaveRange = ProjectionUtil.divideProjectionWaveExt(verticalProjections, ImageHandleUtil.DEF_MIN_RANGE, minCharWidth, minCharSpace, minColumnSpace);
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


    public void showDivision(BufferedImage image) {
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
