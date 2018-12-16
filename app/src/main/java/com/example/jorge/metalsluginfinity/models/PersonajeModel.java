package com.example.jorge.metalsluginfinity.models;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.jorge.metalsluginfinity.R;

import java.util.List;

public abstract class PersonajeModel {

    public static final int INDICADOR_MOVIMIENTO_PERSONAJE_DERECHA = 30;
    public static final int INDICADOR_MOVIMIENTO_PERSONAJE_IZQUIERDA = -30;


    protected int ejeX;
    protected int ejeY;


    private Bitmap bitmap;

    private Resources resources;

    protected List<Float> historialPosicionX;

    public int getEjeX() {
        return ejeX;
    }

    public void setEjeX(int ejeX) {
        this.ejeX = ejeX;
    }

    public int getEjeY() {
        return ejeY;
    }

    public void setEjeY(int ejeY) {
        this.ejeY = ejeY;
    }

    public void moverX(int x) {
        setEjeX(getEjeX() + x);

    }

    public void moverY(int y) {
        setEjeY(getEjeY() + y);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public void addPositionToXHistorical(Float position) {
        this.historialPosicionX.add(position);
    }

    public Float getLastXPosition() {
        if (this.historialPosicionX.size() > 0) {
            return this.historialPosicionX.get(this.historialPosicionX.size() - 1);
        }

        return 0F;
    }

    public int getHeightResourceImgCharacter() {
        BitmapFactory.Options dimensions = new BitmapFactory.Options();
        dimensions.inJustDecodeBounds = true;
        Bitmap yo = BitmapFactory.decodeResource(getResources(), R.drawable.personaje1, dimensions);
        return dimensions.outHeight;
    }
}
