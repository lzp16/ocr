package com.roey.ocr.entity;

/**
 * 类别及得分
 *
 * @author: lizhanping
 * @date: 2018/10/29 17:50
 **/
public class Score {

    /**
     * 该分类得分
     */
    private double score;

    /**
     * 分类ID
     */
    private String typeId;


    public Score(double score, String typeId) {
        this.score = score;
        this.typeId = typeId;
    }


    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

}