package com.roey.ocr.service.impl;

import com.roey.ocr.algorithm.Classification;
import com.roey.ocr.entity.Cell;
import com.roey.ocr.entity.CharArea;
import com.roey.ocr.postprocess.Compose;
import com.roey.ocr.preprocess.Division;
import com.roey.ocr.service.RecognitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.roey.ocr.util.ImageHandleUtil.getCharImageMatrix;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/10/24 16:17
 **/
@Service
public class RecognitionServiceImpl implements RecognitionService {

    @Autowired
    private Division division;
    @Qualifier("twoArrayKnnClassification")
    @Autowired
    private Classification<int[][]> classification;
    @Autowired
    private Compose compose;

    @Override
    public List<List<String>> recognizeTable(BufferedImage image) {
        List<List<String>> result = new ArrayList<>();
        //1、图像处理
        List<Cell> cells = division.divideCell(image);
        List<String> rows = new ArrayList<>();
        for (int i = 0; i < cells.size(); i++) {
            if (i != 0 && cells.get(i).getRowNum() != cells.get(i - 1).getRowNum()) {
                result.add(rows);
                rows = new ArrayList<>();
            }
            StringBuilder values = new StringBuilder();
            for (CharArea charArea : cells.get(i).getValues()) {
                //2、字符识别
                String value = recognizeChar(image, charArea);
                values.append(value);
            }
            //3、字符处理
            rows.add(compose.composeChar(values.toString()));
            if (i == cells.size() - 1) {
                result.add(rows);
            }
        }
        return result;
    }

    @Override
    public String recognizeChar(BufferedImage image, CharArea charArea) {
        BufferedImage charImage = image.getSubimage(charArea.getX1(), charArea.getY1(), charArea.getWidth(), charArea.getHeight());
        int[][] unknownCharData = getCharImageMatrix(charImage);
        return classification.getTypeId(unknownCharData);
    }
}
