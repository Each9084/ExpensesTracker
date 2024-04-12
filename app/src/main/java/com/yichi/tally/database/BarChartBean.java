package com.yichi.tally.database;

//BarChartItemBean 柱状图里的 单个柱子 表示的对象
public class BarChartBean {
    int year;
    int month;
    int day;
    //summoney
    float allAmount;

    public BarChartBean(int year, int month, int day, float allAmount) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.allAmount = allAmount;
    }

    public BarChartBean() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public float getAllAmount() {
        return allAmount;
    }

    public void setAllAmount(float allAmount) {
        this.allAmount = allAmount;
    }
}
