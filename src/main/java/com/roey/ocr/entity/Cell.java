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
    private List<FontRange> values;

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

    public List<FontRange> getValues() {
        return values;
    }

    public void setValues(List<FontRange> values) {
        this.values = values;
    }
}
