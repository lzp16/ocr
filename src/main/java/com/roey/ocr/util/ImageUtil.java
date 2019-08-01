package com.roey.ocr.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * @Created lizhanping
 * @Company 垫脚石
 * @Date 10:43 2019/7/31
 * @Version 1.0
 */
@Slf4j
public class ImageUtil {

    static {
        System.setProperty("java.awt.headless", "false");
    }

    private static BASE64Encoder encoder = new BASE64Encoder();
    private static BASE64Decoder decoder = new BASE64Decoder();

    public static String imageToBase64(BufferedImage image, String formateName) {
        if (image == null) {
            return null;
        }
        if (StringUtils.isBlank(formateName)) {
            formateName = "jpg";
        }
        return encoder.encodeBuffer(image2Byte(image, formateName)).trim();
    }

    public static BufferedImage base64ToImage(String base64Str) {
        if (base64Str == null) {
            return null;
        }
        try {
            return byte2Image(decoder.decodeBuffer(base64Str));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.toString());
        }
        return null;
    }

    public static byte[] image2Byte(BufferedImage image) {
        return image2Byte(image,"jpg");
    }

    public static byte[] image2Byte(BufferedImage image, String formateName) {
        if (image == null) {
            return null;
        }
        if (StringUtils.isBlank(formateName)) {
            formateName = "jpg";
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, formateName, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static BufferedImage byte2Image(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayInputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(bytes);
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String show(Image image) {
        return show(image, "图片");
    }

    public static String show(Image image, String title) {
        ImageIcon icon = new ImageIcon(image);
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (width > 1000 || height > 500) {
            width /= 2;
            height /= 2;
        }
        icon.setImage(icon.getImage().getScaledInstance(width, height + 30, Image.SCALE_DEFAULT));
        return JOptionPane.showInputDialog(null, icon, title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void main(String[] args) throws IOException {
        ImageUtil.show(ImageIO.read(new File("C:\\Users\\donglin\\Desktop\\1.jpg")));
    }
}
