package com.example.jorge.metalsluginfinity.models;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.jorge.metalsluginfinity.R;

public class PolloComida extends PersonajeModel {

    private Bitmap bitmap;
    private Resources resources;

    public PolloComida (Resources resources) {
        setResources(resources);
        setBitmap(BitmapFactory.decodeResource(resources, R.drawable.food1));
    }

    public String toString() {
        return "Comida Pollo: X = " + getEjeX() + " Y: " + getEjeY();
    }
}
