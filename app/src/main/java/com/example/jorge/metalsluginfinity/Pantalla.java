package com.example.jorge.metalsluginfinity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.jorge.metalsluginfinity.models.PMarcoRossi;
import com.example.jorge.metalsluginfinity.models.PersonajeModel;
import com.example.jorge.metalsluginfinity.models.PolloComida;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Pantalla extends View {

    private final int INDICADOR_DESCENSO_COMIDA = 30;
    private final int NUMERO_ELEMENTO_COMIDA = 1;


    private Bitmap fondo;
    private PersonajeModel personaje;

    private int posicionVerticalComida = 0;

    private Handler handler;

    private Pantalla pantalla = this;

    private List<PersonajeModel> listaComida;


    public Pantalla(Context context) {

        super(context);
        personaje = new PMarcoRossi(getResources());

        this.handler = new Handler();
        listaComida = new ArrayList<>();
        handler.postDelayed(GameLoop, 1000);
    }

    protected  void  onDraw(Canvas canvas) {
        pintaFondo(canvas);
        pintaPersonaje(canvas);
        pintaComida(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initComida();
        personaje.setEjeX(getWidth()/2);
        personaje.setEjeY(getHeight()-300);
    }

    private void initComida() {
        Random r = new Random();
        for (int i = 0; i < NUMERO_ELEMENTO_COMIDA; i++) {
            int low = posicionVerticalComida;
            int heigh = -600;
            int ejeY = r.nextInt(low - heigh) + heigh;
            PersonajeModel comidaPollo = new PolloComida(getResources());
            comidaPollo.setEjeY(ejeY);
            comidaPollo.setEjeX(r.nextInt(getWidth()) -100);

            listaComida.add(comidaPollo);
        }
    }


    public void pintaComida(Canvas canvas) {
        for (PersonajeModel comida: listaComida) {
            int y = comida.getEjeY() + posicionVerticalComida;
            Log.e("Comida", comida.toString() + "  YY: " +y);
            Log.e("Personaje", personaje.toString());
            if (!existeColision(comida, canvas)) {
                canvas.drawBitmap(comida.getBitmap(), comida.getEjeX(), y, null);
            }
        }
    }

    private boolean existeColision(PersonajeModel comida, Canvas canvas) {
        int xPosicionPersonaje = personaje.getEjeX();
        int yPosicionPersonaje = personaje.getEjeY();

        int alturaPersonaceSrc = personaje.getHeightResourceImgCharacter();
        int alturaCanvas = canvas.getHeight();

        int alturaColision = alturaCanvas - alturaPersonaceSrc;
        Log.e("Altura Colision", alturaColision + "");
        if (xPosicionPersonaje == comida.getEjeX() && yPosicionPersonaje >= alturaColision) {
            Log.e("Existe colision", "COLLISION");
            return true;
        }


        return false;
    }

    public void actualizaEstadoCanvas() {
        posicionVerticalComida += INDICADOR_DESCENSO_COMIDA;
        invalidate();
    }

    private void pintaPersonaje(Canvas canvas) {
        canvas.drawBitmap(personaje.getBitmap(), personaje.getEjeX(), personaje.getEjeY(), null);
    }

    private void pintaFondo(Canvas canvas) {
        fondo = BitmapFactory.decodeResource(getResources(), R.drawable.fondo1);
        canvas.drawBitmap(Bitmap.createScaledBitmap(fondo, getWidth(), getHeight(), false), 0 , 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Log.e("Personaje: ", personaje.toString());

        if (MotionEvent.ACTION_UP == event.getAction()) {
            Matrix matrix = new Matrix();
            if (getWidth()/2 < event.getX()) {
                personaje.moverX(PersonajeModel.INDICADOR_MOVIMIENTO_PERSONAJE_DERECHA);
                if (!isUltimoMovimientoDerecha() && personaje.getLastXPosition() != 0) {
                    matrix.preScale(-1.0f, 1.0f);
                }

            } else if (getWidth()/2 > event.getX()){
                personaje.moverX(PersonajeModel.INDICADOR_MOVIMIENTO_PERSONAJE_IZQUIERDA);
                if (personaje.getLastXPosition() == 0 || isUltimoMovimientoDerecha()) {
                    matrix.preScale(-1.0f, 1.0f);
                }

            }

            personaje.addPositionToXHistorical(event.getX());
            Bitmap personajeBitmap = Bitmap.createBitmap(personaje.getBitmap(), 0, 0, personaje.getBitmap().getWidth(), personaje.getBitmap().getHeight(), matrix, true);
            personaje.setBitmap(personajeBitmap);
            invalidate();

        }

        return true;

    }

    private boolean isUltimoMovimientoDerecha() {
        if (getWidth() / 2 < personaje.getLastXPosition()) {
            return true;
        }

        return  false;
    }


    public Runnable GameLoop = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(GameLoop, 1000);
            pantalla.actualizaEstadoCanvas();
        }
    };
}
