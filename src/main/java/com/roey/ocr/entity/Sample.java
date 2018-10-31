package com.roey.ocr.entity;

/**
 * 样本数据及类别
 *
 * @author: lizhanping
 * @date: 2018/10/29 17:47
 **/
public class Sample<T> {

    /**
     * 分类ID
     */
    private String typeId;

    /**
     * 记录值
     */
    private T value;

    public Sample(String typeId, T value) {
        this.value = value;
        this.typeId = typeId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
