package com.roey.ocr.algorithm;

import com.roey.ocr.entity.Sample;
import com.roey.ocr.sample.SampleLoad;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.roey.ocr.util.CommonUtil.extMatrix;

/**
 * Created by LiZhanPing on 2018/10/29.
 */
@Component
public class PixelContrastClassification implements Classification<int[][]> {

    private static List<Sample<int[][]>> samples;

    public PixelContrastClassification() {
        SampleLoad.loadSampleData().forEach(sample -> addSample(sample.getValue(), sample.getTypeId()));
    }

    /**
     * 向模型中添加记录
     *
     * @param value  样本数据
     * @param typeId 样本类型
     */
    @Override
    public void addSample(int[][] value, String typeId) {
        if (samples == null) {
            samples = new ArrayList<>();
        }
        samples.add(new Sample(value, typeId));
    }

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
     * @param o1 二维数组
     * @param o2 二维数组
     * @return 差值
     */
    public int contrastPixel(int[][] o1, int[][] o2) {
        int result = 0;
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
        int[][] simpleDataExt = extMatrix(o1, row, column);
        int[][] unknowDataExt = extMatrix(o2, row, column);
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
