package com.roey.ocr.controller;

import com.roey.ocr.util.Img2Base64Util;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;

public class RecognitionControllerTest {


    public static void main(String[] args) throws IOException {

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add("imageStr", Img2Base64Util.toBase64Str("C:\\Users\\15886\\Desktop\\huangshi1.png"));
        multiValueMap.add("removeBothEnds", true);
        multiValueMap.add("colsstr", Arrays.asList(1, 2, 3));
        ResponseEntity<String> jsonArrayResponseEntity = restTemplate.postForEntity("http://localhost:8080/recognition/recognizeShenYueChar", multiValueMap, String.class);
        System.out.printf(jsonArrayResponseEntity.getBody());
    }
}
