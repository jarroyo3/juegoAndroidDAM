package com.example.jorge.metalsluginfinity.models;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.jorge.metalsluginfinity.R;

import java.util.ArrayList;

public class PMarcoRossi extends  PersonajeModel {

    private Bitmap bitmap;
    private Resources resources;

    public PMarcoRossi (Resources resources) {
        setResources(resources);
        setBitmap(BitmapFactory.decodeResource(resources, R.drawable.personaje1));
        historialPosicionX = new ArrayList<>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Marco Rossi ");
        sb.append("X: ");
        sb.append(getEjeX());
        sb.append(" Y: ");
        sb.append(getEjeY());
        sb.append(" Última posición X: ");
        sb.append(getLastXPosition());
        return sb.toString();
    }


}

