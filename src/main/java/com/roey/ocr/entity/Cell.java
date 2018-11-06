package com.roey.ocr.entity;

import java.util.List;

/**
 * description
 *
 * @author: lizhanping
 * @date: 2018/10/11 17:40
 **/
public class Cell {

    private int rowNum;
    private int colNum;
    private List<CharArea> values;

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }

    public int getColNum() {
        return colNum;
    }

    public void setColNum(int colNum) {
        this.colNum = colNum;
    }

    public List<CharArea> getValues() {
        return values;
    }

    public void setValues(List<CharArea> values) {
        this.values = values;
    }
}
