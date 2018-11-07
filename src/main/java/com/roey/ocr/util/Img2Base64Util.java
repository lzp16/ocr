package com.roey.ocr.util;

import org.apache.tomcat.util.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Img2Base64Util {

    public static String toBase64Str(String imgFile) throws IOException {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        in = new FileInputStream(imgFile);
        data = new byte[in.available()];
        in.read(data);
        in.close();
        return new String(Base64.encodeBase64(data));
    }

    /**
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @param imgStr 图片数据
     * @return
     */
    public static BufferedImage toImage(String imgStr) throws IOException {
        //图像数据为空
        if (imgStr == null) {
            return null;
        }
        //Base64解码
        byte[] b = Base64.decodeBase64(imgStr);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {//调整异常数据
                b[i] += 256;
            }
        }
        //生成jpeg图片
        return ImageIO.read(new ByteArrayInputStream(b));
    }

    public static void main(String[] args) throws IOException {
        ImageShowUtil.img(toImage(toBase64Str("C:\\Users\\15886\\Desktop\\huangshi1.png")));
    }
}
