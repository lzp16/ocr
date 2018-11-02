package com.roey.ocr.sample;

import com.roey.ocr.analysis.Analysis;
import com.roey.ocr.entity.Sample;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.roey.ocr.util.ImageHandleUtil.getFontImageMatrix;

/**
 * Created by LiZhanPing on 2018/10/29.
 */
public class SampleLoad<T> {

    public static List<Sample<int[][]>> loadSampleData() {
        List<Sample<int[][]>> samples = new ArrayList<>();

        String basePath = Analysis.class.getClassLoader().getResource(".").getFile() + "fontsimple/shenyue/";
        basePath = basePath.replace("test-classes", "classes");
        samples.addAll(getSampleValue("序", basePath + "xu"));
        samples.addAll(getSampleValue("号", basePath + "hao"));
        samples.addAll(getSampleValue("记", basePath + "ji2"));
        samples.addAll(getSampleValue("账", basePath + "zhang"));
        samples.addAll(getSampleValue("日", basePath + "ri"));
        samples.addAll(getSampleValue("期", basePath + "qi"));
        samples.addAll(getSampleValue("归", basePath + "gui"));
        samples.addAll(getSampleValue("集", basePath + "ji3"));
        samples.addAll(getSampleValue("和", basePath + "he"));
        samples.addAll(getSampleValue("业", basePath + "ye"));
        samples.addAll(getSampleValue("务", basePath + "wu"));
        samples.addAll(getSampleValue("类", basePath + "lei"));
        samples.addAll(getSampleValue("型", basePath + "xing"));
        samples.addAll(getSampleValue("摘", basePath + "zhai"));
        samples.addAll(getSampleValue("要", basePath + "yao"));
        samples.addAll(getSampleValue("发", basePath + "fa"));
        samples.addAll(getSampleValue("生", basePath + "sheng"));
        samples.addAll(getSampleValue("额", basePath + "e"));
        samples.addAll(getSampleValue("利", basePath + "li"));
        samples.addAll(getSampleValue("原", basePath + "yuan"));
        samples.addAll(getSampleValue("因", basePath + "yin"));
        samples.addAll(getSampleValue("方", basePath + "fang"));
        samples.addAll(getSampleValue("式", basePath + "shi"));
        samples.addAll(getSampleValue("余", basePath + "yu"));

        //汇缴、年度/终结息、公积金提取还款、偿还购房贷款本息、部分提取、其他
        samples.addAll(getSampleValue(".", basePath + "dian"));
        samples.addAll(getSampleValue(",", basePath + "douhao"));
        samples.addAll(getSampleValue("-", basePath + "hengxian"));
        samples.addAll(getSampleValue("0", basePath + "0"));
        samples.addAll(getSampleValue("1", basePath + "1"));
        samples.addAll(getSampleValue("2", basePath + "2"));
        samples.addAll(getSampleValue("3", basePath + "3"));
        samples.addAll(getSampleValue("4", basePath + "4"));
        samples.addAll(getSampleValue("5", basePath + "5"));
        samples.addAll(getSampleValue("6", basePath + "6"));
        samples.addAll(getSampleValue("7", basePath + "7"));
        samples.addAll(getSampleValue("8", basePath + "8"));
        samples.addAll(getSampleValue("9", basePath + "9"));
        samples.addAll(getSampleValue("sandianshui", basePath + "sandianshui"));
        samples.addAll(getSampleValue("fang", basePath + "fang3"));
        samples.addAll(getSampleValue("汇", basePath + "hui"));
        samples.addAll(getSampleValue("缴", basePath + "jiao"));
        samples.addAll(getSampleValue("年", basePath + "nian"));
        samples.addAll(getSampleValue("度", basePath + "du"));
        samples.addAll(getSampleValue("终", basePath + "zhong"));
        samples.addAll(getSampleValue("结", basePath + "jie"));
        samples.addAll(getSampleValue("息", basePath + "xi"));
        samples.addAll(getSampleValue("公", basePath + "gong"));
        samples.addAll(getSampleValue("积", basePath + "ji"));
        samples.addAll(getSampleValue("金", basePath + "jin"));
        samples.addAll(getSampleValue("提", basePath + "ti"));
        samples.addAll(getSampleValue("取", basePath + "qu"));
        samples.addAll(getSampleValue("还", basePath + "huan"));
        samples.addAll(getSampleValue("款", basePath + "kuan"));
        samples.addAll(getSampleValue("偿", basePath + "chang"));
        samples.addAll(getSampleValue("购", basePath + "gou"));
        samples.addAll(getSampleValue("房", basePath + "fang2"));
        samples.addAll(getSampleValue("贷", basePath + "dai"));
        samples.addAll(getSampleValue("本", basePath + "ben"));
        samples.addAll(getSampleValue("部", basePath + "bu"));
        samples.addAll(getSampleValue("分", basePath + "fen"));
        samples.addAll(getSampleValue("其", basePath + "qi2"));
        samples.addAll(getSampleValue("它", basePath + "ta"));
        samples.addAll(getSampleValue("内", basePath + "nei"));
        samples.addAll(getSampleValue("转", basePath + "zhuan"));
        samples.addAll(getSampleValue("he", basePath + "he2"));
        samples.addAll(getSampleValue("duo", basePath + "duo"));

        samples.addAll(getSampleValue("共", basePath + "gong2"));
        samples.addAll(getSampleValue("条", basePath + "tiao"));
        samples.addAll(getSampleValue("录", basePath + "lu"));
        samples.addAll(getSampleValue("第", basePath + "di"));
        samples.addAll(getSampleValue("/", basePath + "xiegang"));
        samples.addAll(getSampleValue("下", basePath + "xia"));
        samples.addAll(getSampleValue("一", basePath + "yi"));
        samples.addAll(getSampleValue("页", basePath + "ye2"));

        return samples;
    }

    public static List<Sample<int[][]>> getSampleValue(String typeId, String path) {
        List<Sample<int[][]>> result = new ArrayList<>();
        File rootFile = new File(path);
        File[] files = rootFile.listFiles();
        try {
            for (int i = 0; i < files.length; i++) {
                BufferedImage image = ImageIO.read(files[i]);
                result.add(new Sample<>(typeId, getFontImageMatrix(image)));
            }
        } catch (Exception e) {
            System.out.println(path);
        }
        return result;
    }
}
