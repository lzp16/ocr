package com.roey.ocr.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ProjectionUtil {

    /**
     * 分割投影波
     *
     * @param projections        水平或竖直方向上的投影
     * @param minRange：波峰的最小幅度
     * @param minWaveLen：列的最小长度
     * @param minSpace：列间空白的最小长度
     */
    public static LinkedHashMap<Integer, Integer> divideProjectionWave(int[] projections, int minRange, int minWaveLen, int minSpace) {
        LinkedHashMap<Integer, Integer> peekRange = new LinkedHashMap<>();
        int begin = 0;
        int end;
        for (int i = 0; i < projections.length; i++) {
            if (projections[i] >= minRange && begin == 0) {
                begin = i;
            } else if (projections[i] >= minRange && begin != 0) {
                if (i == projections.length - 1) {
                    end = i;
                    if (end - begin + 1 >= minWaveLen) {
                        peekRange.put(begin, end);
                    }
                }
            } else if (projections[i] < minRange && begin != 0) {
                end = i - 1;
                boolean flag = true;
                for (int j = i; j < i + minSpace; j++) {
                    if (j < projections.length) {
                        if (projections[j] >= minRange) {
                            flag = false;
                            break;
                        }
                    }
                }
                if (end - begin + 1 >= minWaveLen && flag) {
                    peekRange.put(begin, end);
                    begin = 0;
                }
            }
        }
        return peekRange;
    }


    /**
     * 分割投影波
     *
     * @param projections        水平或竖直方向上的投影
     * @param minRange：波峰的最小幅度
     * @param minWaveLen：列的最小长度
     * @param minSpace：列间空白的最小长度
     */
    public static List<Integer> divideProjectionWave2(int[] projections, int minRange, int minWaveLen, int minSpace) {
        List<Integer> list = new ArrayList<>();
        int begin = 0;
        int end;
        for (int i = 0; i < projections.length; i++) {
            if (projections[i] >= minRange && begin == 0) {
                begin = i;
            } else if (projections[i] >= minRange && begin != 0) {
                if (i == projections.length - 1) {
                    end = i;
                    if (end - begin + 1 >= minWaveLen) {
                        list.add(begin);
                        list.add(end);
                    }
                }
            } else if (projections[i] < minRange && begin != 0) {
                end = i - 1;
                boolean flag = true;
                for (int j = i; j < i + minSpace; j++) {
                    if (j < projections.length) {
                        if (projections[j] >= minRange) {
                            flag = false;
                            break;
                        }
                    }
                }
                if (end - begin + 1 >= minWaveLen && flag) {
                    list.add(begin);
                    list.add(end);
                    begin = 0;
                }
            }
        }
        return list;
    }

    /**
     * 分割投影波
     *
     * @param projections              水平或竖直方向上的投影
     * @param minRange：最小波幅
     * @param minWaveLen：最小波长
     * @param minWaveSpace：最小波间隔
     * @param minWaveGroupSpace：最小波组间隔
     */
    public static List<List<Integer>> divideProjectionWaveExt(int[] projections, int minRange, int minWaveLen, int minWaveSpace, int minWaveGroupSpace) {
        List<List<Integer>> result = new ArrayList<>();
        int begin = -1;
        int end;
        int space = 0;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < projections.length; i++) {
            if (projections[i] >= minRange) {
                if (begin == -1 || space >= minWaveSpace) {
                    begin = i;
                }
                for (; i < projections.length; i++) {
                    if (i == (projections.length - 1)) {
                        if (projections[i] < minRange) {
                            end = i - 1;
                            if (end - begin + 1 >= minWaveLen) {
                                list.add(begin);
                                list.add(end);
                            }
                            result.add(list);
                        } else {
                            end = i;
                            if (end - begin + 1 >= minWaveLen) {
                                list.add(begin);
                                list.add(end);
                            }
                            result.add(list);
                        }
                        break;
                    }
                    if (projections[i] < minRange) {
                        break;
                    }
                }
            }
            if (i == (projections.length - 1)) {
                break;
            }
            space = 0;
            if (projections[i] < minRange) {
                for (; i < projections.length; i++) {
                    if (i == (projections.length - 1)) {
                        if (projections[i] < minRange) {
                            end = i - (space + 1);
                            if (end - begin + 1 >= minWaveLen) {
                                list.add(begin);
                                list.add(end);
                            }
                            result.add(list);
                        } else {
                            end = i - 1 - space;
                            if (end - begin + 1 >= minWaveLen) {
                                list.add(begin);
                                list.add(end);
                            }
                            result.add(list);
                        }
                        break;
                    }
                    if (projections[i] >= minRange) {
                        i--;
                        break;
                    } else {
                        space++;
                    }
                }
            }
            if (i == (projections.length - 1)) {
                break;
            }
            if (space >= minWaveSpace) {
                end = i - space;
                if (end - Math.abs(begin) + 1 >= minWaveLen) {
                    list.add(begin);
                    list.add(end);
                    if (space >= minWaveGroupSpace) {
                        result.add(list);
                        list = new ArrayList<>();
                    }
                }
            }
        }
        return result;
    }

    /**
     * 边缘侦测
     *
     * @param data 数组
     * @param left 方向
     * @return 返回数字不为的下标
     */
    public static int edgeDetection(int[] data, boolean left) {
        int result = 0;
        if (left) {
            for (int i = 0; i < data.length; i++) {
                if (data[i] != 0) {
                    result = i;
                    break;
                }
            }
        } else {
            for (int i = data.length - 1; i >= 0; i--) {
                if (data[i] != 0) {
                    result = i;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 获取数组中指定数字相连的最小长度，可以指定最小值的边界且最小值是大于边界值的
     *
     * @param array    数组
     * @param boundary 最小长度的边界
     * @return 返回投影波的长度
     */
    public static int getMinWaveLen(int[] array, int specNum, int boundary) {
        int minLen = Integer.MAX_VALUE;
        int temp = 0;
        for (int anArray : array) {
            if (anArray >= specNum) {
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
        for (int anArray : array) {
            if (anArray <= specNum) {
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
        for (int anArray : array) {
            if (anArray >= specNum) {
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
        for (int anArray : array) {
            if (anArray <= 0) {
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
}
