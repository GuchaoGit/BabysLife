package com.guc.babyslife.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by guc on 2019/10/16.
 * 描述：成长数据
 */
@Entity
public class GrowData {
    @Id(autoincrement = true)//设置自增长
    private Long id;
    @NotNull
    private String uuid;//通过uuid绑定baby
    private int code;
    @NotNull
    private int age;
    private String ageDesc;
    private float height;//身高 cm
    private float weight;//体重 kg
    private long addTime;
    private String measureDate;//测量日期
    private String photo;//图片

    @Generated(hash = 875006577)
    public GrowData(Long id, @NotNull String uuid, int code, int age,
                    String ageDesc, float height, float weight, long addTime,
                    String measureDate, String photo) {
        this.id = id;
        this.uuid = uuid;
        this.code = code;
        this.age = age;
        this.ageDesc = ageDesc;
        this.height = height;
        this.weight = weight;
        this.addTime = addTime;
        this.measureDate = measureDate;
        this.photo = photo;
    }

    @Generated(hash = 320260142)
    public GrowData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAgeDesc() {
        return this.ageDesc;
    }

    public void setAgeDesc(String ageDesc) {
        this.ageDesc = ageDesc;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return this.weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public long getAddTime() {
        return this.addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public String getMeasureDate() {
        return this.measureDate;
    }

    public void setMeasureDate(String measureDate) {
        this.measureDate = measureDate;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
