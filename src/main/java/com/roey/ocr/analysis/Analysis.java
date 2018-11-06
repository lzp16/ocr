package com.roey.ocr.analysis;

import com.roey.ocr.algorithm.knn.TwoArrayKnnClassification;
import com.roey.ocr.entity.Cell;
import com.roey.ocr.entity.CharArea;
import com.roey.ocr.postprocess.Compose;
import com.roey.ocr.preprocess.Division;
import com.roey.ocr.sample.SampleLoad;
import org.springframework.stereotype.Component;

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
@Component
public class Analysis {

    TwoArrayKnnClassification classification = new TwoArrayKnnClassification();

    public List<List<String>> analysisTable(BufferedImage image) {
        List<List<String>> result = new ArrayList<>();
        List<Cell> cells = Division.divideCell(image);
        List<String> rows = new ArrayList<>();
        for (int i = 0; i < cells.size(); i++) {
            if (i != 0 && cells.get(i).getRowNum() != cells.get(i - 1).getRowNum()) {
                result.add(rows);
                rows = new ArrayList<>();
            }
            StringBuilder values = new StringBuilder();
            for (CharArea charArea : cells.get(i).getValues()) {
                String value = analysisChar(image, charArea);
                values.append(value);
            }
            rows.add(Compose.composeChar(values.toString()));
            if (i == cells.size() - 1) {
                result.add(rows);
            }
        }
        return result;
    }

    public String analysisChar(BufferedImage image, CharArea charArea) {
        BufferedImage charImage = image.getSubimage(charArea.getX1(), charArea.getY1(), charArea.getWidth(), charArea.getHeight());
        int[][] unknownCharData = getCharImageMatrix(charImage);
        return classification.getTypeId(unknownCharData);
    }


    public void loadSample() {
        SampleLoad.loadSampleData().forEach(sample -> classification.addRecord(sample.getValue(), sample.getTypeId()));
    }
}
