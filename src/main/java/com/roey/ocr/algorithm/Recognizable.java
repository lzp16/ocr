package com.roey.ocr.algorithm;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/10/30 11:03
 **/
public interface Recognizable<T> {
    String getTypeId(T value);
}
