package com.roey.ocr.algorithm.knn;

import com.roey.ocr.algorithm.Recognizable;
import com.roey.ocr.entity.Sample;
import com.roey.ocr.entity.Score;

import java.util.*;

/**
 * KNN分类
 *
 * @author: lizhanping
 * @date: 2018/10/29 17:55
 **/

@SuppressWarnings({"rawtypes"})
public abstract class AbstractKnnClassification<T> implements Recognizable<T> {

    private List<Sample> dataArray;

    private int k = 1;

    public int getK() {
        return k;
    }

    public void setK(int k) {
        if (k < 1) {
            throw new IllegalArgumentException("k must greater than 0");
        }
        this.k = k;
    }

    /**
     * @param value
     * @param typeId
     * @Author:lulei
     * @Description: 向模型中添加记录
     */
    public void addRecord(T value, String typeId) {
        if (dataArray == null) {
            dataArray = new ArrayList<>();
        }
        dataArray.add(new Sample(typeId, value));
    }

    /**
     * @param value
     * @return
     * @Author:lulei
     * @Description: KNN分类判断value的类别
     */
    @Override
    public String getTypeId(T value) {
        Score[] array = getKType(value);
        Map<String, Integer> map = new HashMap<String, Integer>(k);
        for (Score bean : array) {
            System.out.println("=========>>" + bean.getTypeId() + "===========>>" + bean.getScore());
            if (bean != null) {
                if (map.containsKey(bean.getTypeId())) {
                    map.put(bean.getTypeId(), map.get(bean.getTypeId()) + 1);
                } else {
                    map.put(bean.getTypeId(), 1);
                }
            }
        }
        String maxTypeId = null;
        int maxCount = 0;
        Iterator<Map.Entry<String, Integer>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Integer> entry = iter.next();
            if (maxCount < entry.getValue()) {
                maxCount = entry.getValue();
                maxTypeId = entry.getKey();
            }
        }
        return maxTypeId;
    }

    /**
     * @param value
     * @return
     * @Author:lulei
     * @Description: 获取K个距离最近的分类
     */
    private Score[] getKType(T value) {
        int k = 0;
        Score[] topK = new Score[this.k];
        for (Sample<T> bean : dataArray) {
            double score = similarScore(bean.getValue(), value);
            if (k == 0) {
                //数组中的记录个数为0是直接添加
                topK[k] = new Score(score, bean.getTypeId());
                k++;
            } else {
                //topk的个数等于this.k且score比topk中最后一个分数大等于时则跳过
                if (!(k == this.k && score >= topK[k - 1].getScore())) {//k=3 && score<topK[2].score
                    int i = 0;
                    //找到要插入的点，每个score都要从前往后比较，且i必须小于k（不然报空指针异常），分数大于等于时则跳过和后一个比较
                    for (; i < k && score >= topK[i].getScore(); i++) {//i=3 && score<topK[0].score
                    }
                    int j = k - 1;//j=2
                    if (k < this.k) {
                        j = k;
                        k++;
                    }
                    for (; j > i; j--) {
                        topK[j] = topK[j - 1];
                    }
                    if (i < this.k) {
                        topK[i] = new Score(score, bean.getTypeId());
                    }
                }
            }
        }
        return topK;
    }

    /**
     * @param o1
     * @param o2
     * @return
     * @Author:lulei
     * @Description: o1 o2之间的相似度
     */
    public abstract double similarScore(T o1, T o2);
}
