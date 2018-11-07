package com.roey.ocr.algorithm.knn;

import com.roey.ocr.sample.SampleLoad;
import org.springframework.stereotype.Component;

import static com.roey.ocr.util.CommonUtil.extMatrix;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/10/30 10:41
 **/
@Component
public class TwoArrayKnnClassification extends AbstractKnnClassification<int[][]> {

    public TwoArrayKnnClassification() {
        SampleLoad.loadSampleData().forEach(sample -> addSample(sample.getValue(), sample.getTypeId()));
    }

    @Override
    public double similarScore(int[][] o1, int[][] o2) {
        int row;
        int column;
        if (o1.length >= o2.length) {
            row = o1.length;
        } else {
            row = o2.length;
        }
        if (o1[0].length >= o2[0].length) {
            column = o1[0].length;
        } else {
            column = o2[0].length;
        }
        o1 = extMatrix(o1, row, column);
        o2 = extMatrix(o2, row, column);
        int score = 0;
        for (int i = 0; i < o1.length; i++) {
            for (int j = 0; j < o1[i].length; j++) {
                score += (o1[i][j] - o2[i][j]) * (o1[i][j] - o2[i][j]);
            }
        }
        return Math.sqrt(score);
    }
}
