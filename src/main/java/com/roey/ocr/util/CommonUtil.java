package com.roey.ocr.util;

/**
 * Created by LiZhanPing on 2018/10/29.
 */
public class CommonUtil {

    /**
     * 以左下角为原点填充到需要的矩阵
     *
     * @param m
     * @param row
     * @param column
     * @return
     */
    public static int[][] extMatrix(int[][] m, int row, int column) {
        if (m.length > row) {
            row = m.length;
        }
        if (m[0].length > column) {
            column = m[0].length;
        }
        int[][] result = new int[row][column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (i >= (row - m.length) && j <= m[i - (row - m.length)].length - 1) {
                    result[i][j] = m[i - (row - m.length)][j];
                } else {
                    result[i][j] = 0;
                }
            }
        }
        return result;
    }
}
