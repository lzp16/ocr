package com.roey.ocr.entity;

/**
 * 样本数据及类别
 *
 * @author: lizhanping
 * @date: 2018/10/29 17:47
 **/
public class Sample<T> {

    /**
     * 记录值
     */
    private T value;

    /**
     * 分类ID
     */
    private String typeId;

    public Sample(T value, String typeId) {
        this.value = value;
        this.typeId = typeId;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
