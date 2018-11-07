package com.roey.ocr.algorithm;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/10/30 11:03
 **/
public interface Classification<T> {
    String getTypeId(T value);

    void addSample(T value, String typeId);
}
