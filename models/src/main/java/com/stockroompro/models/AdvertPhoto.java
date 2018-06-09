package com.stockroompro.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by bagach.alexandr on 14.05.15.
 */
public class AdvertPhoto {

    @Expose
    private ArrayList<String> img;

    public ArrayList<String> getImg() {
        return img;
    }

    public void setImg(ArrayList<String> img) {
        this.img = img;
    }
}
