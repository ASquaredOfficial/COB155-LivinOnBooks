package com.example.myapplayout;

import android.graphics.Bitmap;

public class ModelClass {

    private Bitmap image;

    public ModelClass(Bitmap image) {
        this.image = image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }
}
