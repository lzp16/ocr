package com.roey.ocr.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/7/23 13:51
 **/
public class ImageHandleUtil {

    //最小波幅
    public final static int DEF_MIN_RANGE = 1;
    //最小波长
    public final static int DEF_MIN_WAVE_LEN = 1;
    //最小间隔
    public final static int DEF_MIN_SPACE = 1;
    //横向
    public final static int HORIZONTAL = 0;
    //纵向
    public final static int VERTICAL = 1;

    /**
     * 图片二值化
     *
     * @param image 原始图片
     * @return BufferedImage
     */
    public static BufferedImage binaryImage(BufferedImage image) {
        if (image == null) {
            return null;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgb = image.getRGB(i, j);
                grayImage.setRGB(i, j, rgb);
            }
        }
        return grayImage;
    }

    /**
     * 拼接图片
     *
     * @param isHorizontal true代表水平拼接，fasle代表垂直拼接
     * @param imgs         待拼接的图片数组
     * @return BufferedImage
     */
    public static BufferedImage mergeImage(boolean isHorizontal, BufferedImage... imgs) {
        // 生成新图片
        BufferedImage destImage;
        // 计算新图片的长和高
        int allw = 0, allh = 0, allwMax = 0, allhMax = 0;
        // 获取总长、总宽、最长、最宽
        for (BufferedImage img : imgs) {
            allw += img.getWidth();
            allh += img.getHeight();
            if (img.getWidth() > allwMax) {
                allwMax = img.getWidth();
            }
            if (img.getHeight() > allhMax) {
                allhMax = img.getHeight();
            }
        }
        // 创建新图片
        if (isHorizontal) {
            destImage = new BufferedImage(allw, allhMax, BufferedImage.TYPE_INT_RGB);
        } else {
            destImage = new BufferedImage(allwMax, allh, BufferedImage.TYPE_INT_RGB);
        }
        // 合并所有子图片到新图片
        int wx = 0, wy = 0;
        for (BufferedImage img : imgs) {
            int w1 = img.getWidth();
            int h1 = img.getHeight();
            // 从图片中读取RGB
            int[] imageArrayOne = new int[w1 * h1];
            // 逐行扫描图像中各个像素的RGB到数组中
            imageArrayOne = img.getRGB(0, 0, w1, h1, imageArrayOne, 0, w1);
            // 水平方向合并
            if (isHorizontal) {
                // 设置上半部分或左半部分的RGB
                destImage.setRGB(wx, 0, w1, h1, imageArrayOne, 0, w1);
                // 垂直方向合并
            } else {
                // 设置上半部分或左半部分的RGB
                destImage.setRGB(0, wy, w1, h1, imageArrayOne, 0, w1);
            }
            wx += w1;
            wy += h1;
        }
        return destImage;
    }

    public static BufferedImage binaryImage(BufferedImage image, int grayBoundary) {
        int h = image.getHeight();
        int w = image.getWidth();
        // 构造一个类型为预定义图像类型之一的 BufferedImage，TYPE_BYTE_BINARY（表示一个不透明的以字节打包的 1、2 或 4 位图像。）
        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (getImageRgb(image.getRGB(i, j)) > grayBoundary) {
                    int black = new Color(255, 255, 255).getRGB();
                    bufferedImage.setRGB(i, j, black);
                } else {
                    int white = new Color(0, 0, 0).getRGB();
                    bufferedImage.setRGB(i, j, white);
                }
            }

        }
        return bufferedImage;
    }

    private static int getImageRgb(int i) {
        // 将十进制的颜色值转为十六进制
        String argb = Integer.toHexString(i);
        // argb分别代表透明,红,绿,蓝 分别占16进制2位
        //后面参数为使用进制
        int r = Integer.parseInt(argb.substring(2, 4), 16);
        int g = Integer.parseInt(argb.substring(4, 6), 16);
        int b = Integer.parseInt(argb.substring(6, 8), 16);
        return ((r + g + b) / 3);
    }

    /**
     * 对图片进行放大
     *
     * @param originalImage 原始图片
     * @param times         放大倍数
     * @return BufferedImage
     */

    public static BufferedImage zoomInImage(BufferedImage originalImage, Integer times) {
        int width = originalImage.getWidth() * times;
        int height = originalImage.getHeight() * times;
        BufferedImage newImage = new BufferedImage(width, height, originalImage.getType());
        Graphics g = newImage.getGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();
        return newImage;

    }

    /**
     * 图像投影
     *
     * @param image     图片
     * @param direction 投影方向，0是水平投影，1是竖直投影
     * @return 数组
     */
    public static int[] imageProjection(BufferedImage image, int direction) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] projection;
        if (direction == 0) {
            projection = new int[height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (image.getRGB(x, y) != -1) {
                        projection[y]++;
                    }
                }
            }
        } else {
            projection = new int[width];
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (image.getRGB(x, y) != -1) {
                        projection[x]++;
                    }
                }
            }
        }
        return projection;
    }

    public static void showProjection(BufferedImage image, int direction) {
        int height;
        if (direction == 0) {
            height = image.getHeight();
        } else {
            height = image.getWidth();
        }
        int[] projections = imageProjection(image, direction);
        int width = projections.length;
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);
        Graphics graphics = bi.getGraphics();
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setPaint(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        graphics.setColor(Color.black);
        for (int i = 0; i < width; i++) {
            graphics.drawLine(i, height, i, (height - projections[i]));
        }
        graphics.dispose();
        ImageShowUtil.img(bi);
    }

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
                continue;
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
            } else {
                continue;
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
                continue;
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
            } else {
                continue;
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
        int end = 0;
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

    public static BufferedImage removeBothEnds(BufferedImage image) {
        int[] ints = imageProjection(image, 0);
        List<Integer> integers = divideProjectionWave2(ints, 1, 6, 3);
        int head = 0;
        int tail = image.getHeight() - 1;
        if (integers.size() > 4) {
            for (int i = 0; i < integers.size(); i++) {
                if (i == 1) {
                    head = integers.get(i) + 1;
                }
                if (i == integers.size() - 2) {
                    tail = integers.get(i) - 1;
                }
            }
        }
        return image.getSubimage(0, head, image.getWidth(), tail - head + 1);
    }

    public static BufferedImage optimizeColumnSpace(BufferedImage image, int... coluwnIndexes) {
        int[] projections = imageProjection(image, VERTICAL);
        Map<Integer, Integer> characterBorders = divideProjectionWave(projections, 1, 16, 6);
        if (coluwnIndexes.length > 0) {
            Map<Integer, Integer> newCharacterBorders = new LinkedHashMap<>();
            for (int columnIndex : coluwnIndexes) {
                int index = 0;
                for (Map.Entry<Integer, Integer> entry : characterBorders.entrySet()) {
                    if (index == columnIndex) {
                        newCharacterBorders.put(entry.getKey(), entry.getValue());
                    }
                    index++;
                }
            }
            characterBorders = newCharacterBorders;
        }
        BufferedImage[] bufferedImages = new BufferedImage[characterBorders.size() * 3];
        BufferedImage fillImage = new BufferedImage(16, image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) fillImage.getGraphics();
        //设置颜色
        g2.setColor(new Color(255, 255, 255, 255));
        //填充整张图片,设置背景色
        g2.fillRect(0, 0, 16, image.getHeight());
        int i = 0;
        for (Map.Entry<Integer, Integer> entry : characterBorders.entrySet()) {
            bufferedImages[i] = fillImage;
            i++;
            bufferedImages[i] = image.getSubimage(entry.getKey(), 0, entry.getValue() - entry.getKey() + 1, image.getHeight());
            i++;
            bufferedImages[i] = fillImage;
            i++;
        }
        return mergeImage(true, bufferedImages);
    }

    public static int[][] getCharImageMatrix(BufferedImage image) {
        int[] hp = ImageHandleUtil.imageProjection(image, ImageHandleUtil.HORIZONTAL);
        int[] vp = ImageHandleUtil.imageProjection(image, ImageHandleUtil.VERTICAL);
        int x1 = edgeDetection(vp, true);
        int x2 = edgeDetection(vp, false);
        int y1 = edgeDetection(hp, true);
        int y2 = edgeDetection(hp, false);
        image = image.getSubimage(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
        int w = image.getWidth();
        int h = image.getHeight();
        int[][] data = new int[h][w];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (image.getRGB(j, i) != Color.BLACK.getRGB()) {
                    data[i][j] = 0;
                } else {
                    data[i][j] = 1;
                }
            }

        }
        return data;
    }

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


    public static void main(String[] args) throws Exception {
        BufferedImage image1 = ImageIO.read(new File("C:\\Users\\B-0036\\Desktop\\yzm1.jpg"));
        image1 = binaryImage(image1, 120);
        ImageShowUtil.img(image1);

//        showProjection(image1, 1);
//        image1 = image1.getSubimage(0, 35, image1.getWidth(), image1.getHeight() - 35 - 48);
//        image1 = optimizeColumnSpace(image1, 1, 3, 2, 4);
//        ImageShowUtil.img(image1);
    }
}