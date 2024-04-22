package com.yichi.expenseTracker.bean;

import java.util.List;

//（ChartItemBean）
public class CategoryItemBean {
    int selectImageId;
    String category;
    //(ratio)
    float proportion;
    //(totalMoney)
    float totalAmount;


    public CategoryItemBean() {
    }

    public int getSelectImageId() {
        return selectImageId;
    }

    public void setSelectImageId(int selectImageId) {
        this.selectImageId = selectImageId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getProportion() {
        return proportion;
    }

    public void setProportion(float proportion) {
        this.proportion = proportion;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public CategoryItemBean(int selectImageId, String category, float proportion, float totalAmount) {
        this.selectImageId = selectImageId;
        this.category = category;
        this.proportion = proportion;
        this.totalAmount = totalAmount;

    }
}
