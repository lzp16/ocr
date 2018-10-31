package com.roey.ocr.pageAnalysis;

import com.roey.ocr.algorithm.knn.TwoArrayKnnClassification;
import com.roey.ocr.entity.Cell;
import com.roey.ocr.entity.FontRange;
import com.roey.ocr.preprocess.Division;
import com.roey.ocr.sample.SampleLoad;
import com.roey.ocr.util.ImageHandleUtil;
import com.roey.ocr.util.ImageShowUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.roey.ocr.util.ImageHandleUtil.getFontImageMatrix;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/10/24 16:17
 **/
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
            rows.add(values.toString());
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

    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(new File("C:\\Users\\B-0036\\Desktop\\ocr\\huangshi\\huangshi_3.png"));
        image = image.getSubimage(0, 40, 145, 330);
        image = ImageHandleUtil.binaryImage(image, 180);
        ImageShowUtil.img(image);
        Analysis analysis = new Analysis();
        analysis.loadSample();
        List<List<String>> lists = analysis.analysisTable(image);
        for (int i = 0; i < lists.size(); i++) {
            System.out.println(lists.get(i));
        }
    }
}
