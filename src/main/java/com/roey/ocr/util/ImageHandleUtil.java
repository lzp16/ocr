package com.roey.ocr.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.roey.ocr.util.ProjectionUtil.*;

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


    public static BufferedImage binaryImage(BufferedImage image) {
        int grayBoundary = getGrayBoundaryByOstu(image);
        return binaryImage(image, grayBoundary);
    }

    public static BufferedImage binaryImage(BufferedImage image, int grayBoundary) {
        image = getGrayImage(image);
        ImageShowUtil.img(image);
        int w = image.getWidth();
        int h = image.getHeight();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (getAvgRgb(image.getRGB(i, j)) > grayBoundary) {
                    image.setRGB(i, j, Color.WHITE.getRGB());
                } else {
                    image.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
        return image;
    }

    private static BufferedImage getGrayImage(BufferedImage image) {
        int h = image.getHeight();
        int w = image.getWidth();
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                int avgRgb = getAvgRgb(image.getRGB(i, j));
                int rgb = new Color(avgRgb, avgRgb, avgRgb).getRGB();
                image.setRGB(i, j, rgb);
            }
        }
        return image;
    }

    private static int getAvgRgb(int i) {
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
     * 获得灰度值
     * 最大类间方差法
     *
     * @param image
     */
    public static int getGrayBoundaryByOstu(BufferedImage image) {
        int grayLevel = 256;
        int[] pixelNum = new int[grayLevel];
        //计算所有色阶的直方图
        int width = image.getWidth();
        int height = image.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int color = getAvgRgb(image.getRGB(x, y));
                pixelNum[color]++;
            }
        }

        double sum = 0;
        int total = 0;
        for (int i = 0; i < grayLevel; i++) {
            sum += i * pixelNum[i]; //x*f(x)质量矩，也就是每个灰度的值乘以其点数（归一化后为概率），sum为其总和
            total += pixelNum[i]; //n为图象总的点数，归一化后就是累积概率
        }
        double sumB = 0;//前景色质量矩总和
        int threshold = 0;
        double wF = 0;//前景色权重
        double wB = 0;//背景色权重

        double maxFreq = -1.0;//最大类间方差

        for (int i = 0; i < grayLevel; i++) {
            wB += pixelNum[i]; //wB为在当前阈值背景图象的点数
            if (wB == 0) { //没有分出前景后景
                continue;
            }

            wF = total - wB; //wB为在当前阈值前景图象的点数
            if (wF == 0) {//全是前景图像，则可以直接break
                break;
            }

            sumB += (double) (i * pixelNum[i]);
            double meanB = sumB / wB;
            double meanF = (sum - sumB) / wF;
            //freq为类间方差
            double freq = (wF) * (wB) * (meanB - meanF) * (meanB - meanF);
            if (freq > maxFreq) {
                maxFreq = freq;
                threshold = i;
            }
        }

        return threshold;
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

    public static BufferedImage optimizeColumnSpace(BufferedImage image, Integer... coluwnIndexes) {
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
            if (newCharacterBorders.size() > 0) {
                characterBorders = newCharacterBorders;
            }
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

    /**
     * 展示图片在水平或竖直方向上的投影
     *
     * @param image     图片
     * @param direction 投影方向
     */
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

    public static void main(String[] args) throws Exception {
        BufferedImage image1 = ImageIO.read(new File("C:\\Users\\15886\\Desktop\\captcha.jpg"));
//        BufferedImage image1 = ImageIO.read(new File("E:\\ganzhou.png"));
        image1 = binaryImage(image1);
        ImageShowUtil.img(image1);

//        showProjection(image1, 1);
//        image1 = image1.getSubimage(0, 35, image1.getWidth(), image1.getHeight() - 35 - 48);
//        image1 = optimizeColumnSpace(image1, 1, 3, 2, 4);
//        ImageShowUtil.img(image1);
    }
}