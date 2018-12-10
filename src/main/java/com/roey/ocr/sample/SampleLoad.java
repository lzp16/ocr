package com.roey.ocr.sample;

import com.roey.ocr.entity.Sample;
import com.roey.ocr.exception.WrongInfoException;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.roey.ocr.util.ImageHandleUtil.getCharImageMatrix;

/**
 * Created by LiZhanPing on 2018/10/29.
 */
@Slf4j
public class SampleLoad {

    public static List<Sample<int[][]>> loadSampleData() {
        List<Sample<int[][]>> samples = new ArrayList<>();

        samples.addAll(getSampleValue("序", "xu"));
        samples.addAll(getSampleValue("号", "hao"));
        samples.addAll(getSampleValue("记", "ji2"));
        samples.addAll(getSampleValue("账", "zhang"));
        samples.addAll(getSampleValue("日", "ri"));
        samples.addAll(getSampleValue("期", "qi"));
        samples.addAll(getSampleValue("归", "gui"));
        samples.addAll(getSampleValue("集", "ji3"));
        samples.addAll(getSampleValue("和", "he"));
        samples.addAll(getSampleValue("业", "ye"));
        samples.addAll(getSampleValue("务", "wu"));
        samples.addAll(getSampleValue("类", "lei"));
        samples.addAll(getSampleValue("型", "xing"));
        samples.addAll(getSampleValue("摘", "zhai"));
        samples.addAll(getSampleValue("要", "yao"));
        samples.addAll(getSampleValue("发", "fa"));
        samples.addAll(getSampleValue("生", "sheng"));
        samples.addAll(getSampleValue("额", "e"));
        samples.addAll(getSampleValue("利", "li"));
        samples.addAll(getSampleValue("原", "yuan"));
        samples.addAll(getSampleValue("因", "yin"));
        samples.addAll(getSampleValue("方", "fang"));
        samples.addAll(getSampleValue("式", "shi"));
        samples.addAll(getSampleValue("余", "yu"));

        //汇缴、年度/终结息、公积金提取还款、偿还购房贷款本息、部分提取、其他
        samples.addAll(getSampleValue(".", "dian"));
        samples.addAll(getSampleValue(",", "douhao"));
        samples.addAll(getSampleValue("-", "hengxian"));
        samples.addAll(getSampleValue("0", "0"));
        samples.addAll(getSampleValue("1", "1"));
        samples.addAll(getSampleValue("2", "2"));
        samples.addAll(getSampleValue("3", "3"));
        samples.addAll(getSampleValue("4", "4"));
        samples.addAll(getSampleValue("5", "5"));
        samples.addAll(getSampleValue("6", "6"));
        samples.addAll(getSampleValue("7", "7"));
        samples.addAll(getSampleValue("8", "8"));
        samples.addAll(getSampleValue("9", "9"));
        samples.addAll(getSampleValue("汇", "hui"));
        samples.addAll(getSampleValue("缴", "jiao"));
        samples.addAll(getSampleValue("年", "nian"));
        samples.addAll(getSampleValue("度", "du"));
        samples.addAll(getSampleValue("终", "zhong"));
        samples.addAll(getSampleValue("结", "jie"));
        samples.addAll(getSampleValue("息", "xi"));
        samples.addAll(getSampleValue("公", "gong"));
        samples.addAll(getSampleValue("积", "ji"));
        samples.addAll(getSampleValue("金", "jin"));
        samples.addAll(getSampleValue("提", "ti"));
        samples.addAll(getSampleValue("取", "qu"));
        samples.addAll(getSampleValue("还", "huan"));
        samples.addAll(getSampleValue("款", "kuan"));
        samples.addAll(getSampleValue("偿", "chang"));
        samples.addAll(getSampleValue("购", "gou"));
        samples.addAll(getSampleValue("房", "fang2"));
        samples.addAll(getSampleValue("贷", "dai"));
        samples.addAll(getSampleValue("本", "ben"));
        samples.addAll(getSampleValue("部", "bu"));
        samples.addAll(getSampleValue("分", "fen"));
        samples.addAll(getSampleValue("其", "qi2"));
        samples.addAll(getSampleValue("它", "ta"));
        samples.addAll(getSampleValue("内", "nei"));
        samples.addAll(getSampleValue("转", "zhuan"));
        samples.addAll(getSampleValue("无", "wu2"));
        samples.addAll(getSampleValue("数", "shu"));
        samples.addAll(getSampleValue("据", "ju"));
        samples.addAll(getSampleValue("户", "hu"));
        samples.addAll(getSampleValue("资", "zi"));
        samples.addAll(getSampleValue("移", "yi2"));

        //偏旁部首
        samples.addAll(getSampleValue("sandianshui", "sandianshui"));
        samples.addAll(getSampleValue("fang", "fang3"));
        samples.addAll(getSampleValue("he", "he2"));
        samples.addAll(getSampleValue("duo", "duo"));
        samples.addAll(getSampleValue("yi", "yi3"));
        samples.addAll(getSampleValue("bo", "bo"));
        samples.addAll(getSampleValue("chang", "chang2"));
        samples.addAll(getSampleValue("bei", "bei"));

        samples.addAll(getSampleValue("共", "gong2"));
        samples.addAll(getSampleValue("条", "tiao"));
        samples.addAll(getSampleValue("录", "lu"));
        samples.addAll(getSampleValue("第", "di"));
        samples.addAll(getSampleValue("/", "xiegang"));
        samples.addAll(getSampleValue("下", "xia"));
        samples.addAll(getSampleValue("一", "yi"));
        samples.addAll(getSampleValue("页", "ye2"));

        return samples;
    }

    private static List<Sample<int[][]>> getSampleValue(String typeId, String dirName) {
        List<Sample<int[][]>> result = new ArrayList<>();
        String path = SampleLoad.class.getResource("/").getPath();
        //有感叹号代表是jar/war包下加载，无感叹号代表是编译器中运行
        if (!path.contains("!")) {
            path = path.replace("test-classes", "classes");
            path = path + "charsample/shenyue/" + dirName;
            File rootFile = new File(path);
            File[] files = rootFile.listFiles();
            try {
                for (File file : files) {
                    BufferedImage image = ImageIO.read(file);
                    result.add(new Sample<>(getCharImageMatrix(image), typeId));
                }
            } catch (Exception e) {
                throw new WrongInfoException("加载训练样本异常！");
            }
        } else {
            log.info(path);
            path = path.substring(0, path.indexOf("!")).replace("file:/", "");
            try {
                JarFile localJarFile = new JarFile(path);
                Enumeration<JarEntry> entries = localJarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    String innerPath = jarEntry.getName().replace("BOOT-INF/classes/", "");
                    if (innerPath.contains("charsample/shenyue/" + dirName + "/") && !innerPath.endsWith("charsample/shenyue/" + dirName + "/")) {
                        InputStream resourceAsStream = SampleLoad.class.getClassLoader().getResourceAsStream(innerPath);
                        BufferedImage image = ImageIO.read(resourceAsStream);
                        result.add(new Sample<>(getCharImageMatrix(image), typeId));
                    }
                }
            } catch (IOException e) {
                throw new WrongInfoException("加载训练样本异常！");
            }
        }
        return result;
    }
}