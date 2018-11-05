package com.roey.ocr.analysis;

import com.roey.ocr.algorithm.knn.TwoArrayKnnClassification;
import com.roey.ocr.entity.Cell;
import com.roey.ocr.entity.FontRange;
import com.roey.ocr.postprocess.Compose;
import com.roey.ocr.preprocess.Division;
import com.roey.ocr.sample.SampleLoad;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static com.roey.ocr.util.ImageHandleUtil.getFontImageMatrix;

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
            for (FontRange fontRange : cells.get(i).getValues()) {
                String value = analysisFont(image, fontRange);
                values.append(value);
            }
            rows.add(Compose.composeFont(values.toString()));
            if (i == cells.size() - 1) {
                result.add(rows);
            }
        }
        return result;
    }

    public String analysisFont(BufferedImage image, FontRange fontRange) {
        BufferedImage fontImage = image.getSubimage(fontRange.getX1(), fontRange.getY1(), fontRange.getWidth(), fontRange.getHeight());
        int[][] unknownFontData = getFontImageMatrix(fontImage);
        return classification.getTypeId(unknownFontData);
    }


    public void loadSample() {
        SampleLoad.loadSampleData().forEach(sample -> classification.addRecord(sample.getValue(), sample.getTypeId()));
    }
}
