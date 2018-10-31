package com.roey.ocr.algorithm;

import com.roey.ocr.entity.Sample;
import com.roey.ocr.sample.SampleLoad;

import java.util.List;

import static com.roey.ocr.util.CommonUtil.extMatrix;

/**
 * Created by LiZhanPing on 2018/10/29.
 */
public class PixelContrast implements Recognizable<int[][]> {

    public static List<Sample<int[][]>> samples = SampleLoad.loadSampleData();

    @Override
    public String getTypeId(int[][] value) {
        String result = "";
        int lastScore = Integer.MAX_VALUE;
        for (Sample<int[][]> sample : samples) {
            int score = contrastPixel(sample.getValue(), value);
            if (score < lastScore) {
                result = sample.getTypeId();
                lastScore = score;
            }
        }
        return result;
    }

    /**
     * 以图片左下角为原点进行像素对比
     *
     * @param simpleData
     * @param unknowData
     * @return
     */
    public static int contrastPixel(int[][] simpleData, int[][] unknowData) {
        int result = 0;
        int row;
        int column;
        if (simpleData.length >= unknowData.length) {
            row = simpleData.length;
        } else {
            row = unknowData.length;
        }
        if (simpleData[0].length >= unknowData[0].length) {
            column = simpleData[0].length;
        } else {
            column = unknowData[0].length;
        }
        int[][] simpleDataExt = extMatrix(simpleData, row, column);
        int[][] unknowDataExt = extMatrix(unknowData, row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (simpleDataExt[i][j] != unknowDataExt[i][j]) {
                    result++;
                }
            }
        }
        return result;
    }
}
