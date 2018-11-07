package com.roey.ocr.controller;

import com.alibaba.fastjson.JSONObject;
import com.roey.ocr.service.RecognitionService;
import com.roey.ocr.util.ImageHandleUtil;
import com.roey.ocr.util.Img2Base64Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@RequestMapping("/recognition")
@RestController
public class RecognitionController {

    @Autowired
    private RecognitionService recognitionService;

    @RequestMapping("/recognizeShenYueChar")
    public List<List<String>> recognizeShenYueChar(String imageStr, boolean removeBothEnds, String colsstr) throws IOException {

        BufferedImage image = Img2Base64Util.toImage(imageStr);
        image = ImageHandleUtil.binaryImage(image, 180);
        if (removeBothEnds) {
            image = ImageHandleUtil.removeBothEnds(image);
        }
        List<Integer> columnIndexes = JSONObject.parseArray(colsstr, Integer.class);
        if (columnIndexes.size() > 0) {
            Integer[] cols = new Integer[columnIndexes.size()];
            columnIndexes.toArray(cols);
            image = ImageHandleUtil.optimizeColumnSpace(image, cols);
        }
        return recognitionService.recognizeTable(image);
    }


    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
}
