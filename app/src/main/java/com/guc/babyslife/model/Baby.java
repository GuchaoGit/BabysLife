package com.guc.babyslife.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by guc on 2019/10/15.
 * 描述：婴儿信息
 */
public class Baby implements Parcelable {
    public String uuid;//唯一标识
    public String name;
    public int age;//天数
    public int birthYear;
    public int birthMonth;//月0-11
    public int birthDay;
    public String birthday;//生日
    public String ageDesc;
    public String sex;//性别 1:男  2:女

    public static final Parcelable.Creator<Baby> CREATOR = new Parcelable.Creator<Baby>() {
        @Override
        public Baby createFromParcel(Parcel source) {
            return new Baby(source);
        }

        @Override
        public Baby[] newArray(int size) {
            return new Baby[size];
        }
    };

    public Baby() {
    }

    protected Baby(Parcel in) {
        this.uuid = in.readString();
        this.name = in.readString();
        this.age = in.readInt();
        this.birthYear = in.readInt();
        this.birthMonth = in.readInt();
        this.birthDay = in.readInt();
        this.birthday = in.readString();
        this.ageDesc = in.readString();
        this.sex = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uuid);
        dest.writeString(this.name);
        dest.writeInt(this.age);
        dest.writeInt(this.birthYear);
        dest.writeInt(this.birthMonth);
        dest.writeInt(this.birthDay);
        dest.writeString(this.birthday);
        dest.writeString(this.ageDesc);
        dest.writeString(this.sex);
    }
}
