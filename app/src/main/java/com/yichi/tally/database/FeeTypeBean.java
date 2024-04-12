package com.yichi.tally.database;

/*
* 表示收入或者支出具体类型的类 TypeBean
*
* */
public class FeeTypeBean {
    int id;
    String typename;    //类型你改成
    int imageId;//未被选中时图片id
    int selectImageId;//被选中的图片id
    int category;//类型 收入1 支出0 kind

    public FeeTypeBean(int id, String typename, int imageId, int selectImageId, int category) {
        this.id = id;
        this.typename = typename;
        this.imageId = imageId;
        this.selectImageId = selectImageId;
        this.category = category;
    }

    public FeeTypeBean() {
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

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getSelectImageId() {
        return selectImageId;
    }

    public void setSelectImageId(int selectImageId) {
        this.selectImageId = selectImageId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
