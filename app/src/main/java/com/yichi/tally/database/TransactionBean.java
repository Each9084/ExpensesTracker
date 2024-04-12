package com.yichi.tally.database;

//AccountBean

/**
 * 描述记录一条数据的相关内容（支出与收入的数据）
 */
public class TransactionBean {
    int id;
    String typename;
    int selectImageId;
    String note;//beizhu
    float money;
    String time;
    int year;
    int month;
    int day;
    int category;//类型 收入-1 支出-0

    public TransactionBean() {
    }

    public TransactionBean(int id, String typename, int selectImageId, String note, float money, String time, int year, int month, int day, int category) {
        this.id = id;
        this.typename = typename;
        this.selectImageId = selectImageId;
        this.note = note;
        this.money = money;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public int getSelectImageId() {
        return selectImageId;
    }

    public void setSelectImageId(int selectImageId) {
        this.selectImageId = selectImageId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
