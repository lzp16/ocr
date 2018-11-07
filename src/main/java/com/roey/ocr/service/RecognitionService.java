package com.roey.ocr.service;

import com.roey.ocr.entity.CharArea;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/10/24 16:17
 **/
public interface RecognitionService {

    List<List<String>> recognizeTable(BufferedImage image);

    String recognizeChar(BufferedImage image, CharArea charArea);
}
