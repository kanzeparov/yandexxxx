package com.example.franck.myapplication.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Image implements Parcelable{


    private Integer id;

    private String name;

    private String type;


    private String image;


    public Image(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public Image(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);
        name = data[0];
        image = data[1];
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] { name, image });
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {

        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };

}
