package com.roey.ocr.simple;

import com.roey.ocr.pageAnalysis.Analysis;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.roey.ocr.util.ImageHandleUtil.getFontImageMatrix;

/**
 * Created by LiZhanPing on 2018/10/29.
 */
public class SimpleLoad {

    public static Map<String, List<int[][]>> loadSimpleData() {
        Map<String, List<int[][]>> simpleMap = new HashMap<>();
        //汇缴、年终结息、公积金提取还款
        String basePath = Analysis.class.getClassLoader().getResource(".").getFile() + "fontsimple/";
        simpleMap.put("序", getSimpleImage(basePath + "xu"));
        simpleMap.put("号", getSimpleImage(basePath + "hao"));
        simpleMap.put("记", getSimpleImage(basePath + "ji2"));
        simpleMap.put("账", getSimpleImage(basePath + "zhang"));
        simpleMap.put("日", getSimpleImage(basePath + "ri"));
        simpleMap.put("期", getSimpleImage(basePath + "qi"));
        simpleMap.put("归", getSimpleImage(basePath + "gui"));
        simpleMap.put("集", getSimpleImage(basePath + "ji3"));
        simpleMap.put("和", getSimpleImage(basePath + "he"));
        simpleMap.put("业", getSimpleImage(basePath + "ye"));
        simpleMap.put("务", getSimpleImage(basePath + "wu"));
        simpleMap.put("类", getSimpleImage(basePath + "lei"));
        simpleMap.put("型", getSimpleImage(basePath + "xing"));
        simpleMap.put("摘", getSimpleImage(basePath + "zhai"));
        simpleMap.put("要", getSimpleImage(basePath + "yao"));
        simpleMap.put("发", getSimpleImage(basePath + "fa"));
        simpleMap.put("生", getSimpleImage(basePath + "sheng"));
        simpleMap.put("额", getSimpleImage(basePath + "e"));
        simpleMap.put("利", getSimpleImage(basePath + "li"));
        simpleMap.put("原", getSimpleImage(basePath + "yuan"));
        simpleMap.put("因", getSimpleImage(basePath + "yin"));
        simpleMap.put("方", getSimpleImage(basePath + "fang"));
        simpleMap.put("式", getSimpleImage(basePath + "shi"));
        simpleMap.put("余", getSimpleImage(basePath + "yu"));


        simpleMap.put(".", getSimpleImage(basePath + "dian"));
        simpleMap.put(",", getSimpleImage(basePath + "douhao"));
        simpleMap.put("-", getSimpleImage(basePath + "hengxian"));
        simpleMap.put("0", getSimpleImage(basePath + "0"));
        simpleMap.put("1", getSimpleImage(basePath + "1"));
        simpleMap.put("2", getSimpleImage(basePath + "2"));
        simpleMap.put("3", getSimpleImage(basePath + "3"));
        simpleMap.put("4", getSimpleImage(basePath + "4"));
        simpleMap.put("5", getSimpleImage(basePath + "5"));
        simpleMap.put("6", getSimpleImage(basePath + "6"));
        simpleMap.put("7", getSimpleImage(basePath + "7"));
        simpleMap.put("8", getSimpleImage(basePath + "8"));
        simpleMap.put("9", getSimpleImage(basePath + "9"));
        simpleMap.put("公", getSimpleImage(basePath + "gong"));
        simpleMap.put("号", getSimpleImage(basePath + "hao"));
        simpleMap.put("还", getSimpleImage(basePath + "huan"));
        simpleMap.put("汇", getSimpleImage(basePath + "hui"));
        simpleMap.put("积", getSimpleImage(basePath + "ji"));
        simpleMap.put("缴", getSimpleImage(basePath + "jiao"));
        simpleMap.put("结", getSimpleImage(basePath + "jie"));
        simpleMap.put("金", getSimpleImage(basePath + "jin"));
        simpleMap.put("款", getSimpleImage(basePath + "kuan"));
        simpleMap.put("年", getSimpleImage(basePath + "nian"));
        simpleMap.put("度", getSimpleImage(basePath + "du"));
        simpleMap.put("取", getSimpleImage(basePath + "qu"));
        simpleMap.put("提", getSimpleImage(basePath + "ti"));
        simpleMap.put("息", getSimpleImage(basePath + "xi"));
        simpleMap.put("终", getSimpleImage(basePath + "zhong"));
        simpleMap.put("部", getSimpleImage(basePath + "bu"));
        simpleMap.put("分", getSimpleImage(basePath + "fen"));

        simpleMap.put("偿", getSimpleImage(basePath + "chang"));
        simpleMap.put("购", getSimpleImage(basePath + "gou"));
        simpleMap.put("房", getSimpleImage(basePath + "fang2"));
        simpleMap.put("贷", getSimpleImage(basePath + "dai"));
        simpleMap.put("本", getSimpleImage(basePath + "ben"));
        simpleMap.put("其", getSimpleImage(basePath + "qi2"));
        simpleMap.put("它", getSimpleImage(basePath + "ta"));


        simpleMap.put("共", getSimpleImage(basePath + "gong2"));
        simpleMap.put("条", getSimpleImage(basePath + "tiao"));
        simpleMap.put("录", getSimpleImage(basePath + "lu"));
        simpleMap.put("第", getSimpleImage(basePath + "di"));
        simpleMap.put("/", getSimpleImage(basePath + "xiegang"));
        simpleMap.put("下", getSimpleImage(basePath + "xia"));
        simpleMap.put("一", getSimpleImage(basePath + "yi"));
        simpleMap.put("页", getSimpleImage(basePath + "ye2"));

        return simpleMap;
    }

    public static List<int[][]> getSimpleImage(String path) {
        List<int[][]> result = new ArrayList<>();
        File rootFile = new File(path);
        File[] files = rootFile.listFiles();
        try {
            for (int i = 0; i < files.length; i++) {
                BufferedImage image = ImageIO.read(files[i]);
                result.add(getFontImageMatrix(image));
            }
        } catch (Exception e) {
            System.out.println(path);
        }
        return result;
    }
}
