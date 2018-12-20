package com.example.jorge.metalsluginfinity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.jorge.metalsluginfinity.models.PMarcoRossi;
import com.example.jorge.metalsluginfinity.models.PersonajeModel;
import com.example.jorge.metalsluginfinity.models.PolloComida;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


public class Pantalla extends View {

    private final int INDICADOR_DESCENSO_COMIDA = 30;
    private final int NUMERO_ELEMENTO_COMIDA = 10;
    private final int DISTANCIA_SUELO_PERSONAJE = 300;

    private Bitmap fondo;
    private PersonajeModel personaje;

    private int posicionVerticalComida = 0;
    private int puntuacion = 0;
    private int vidas = 3;

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

    protected void onDraw(Canvas canvas) {
        Typeface bold = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTextSize(50);
        paint.setTypeface(bold);

        pintaFondo(canvas);
        pintaPersonaje(canvas);
        pintaComida(canvas);
        canvas.drawText("Score: " + String.valueOf(puntuacion), getWidth() - 450, 100, paint);
        canvas.drawText("Vidas: " + String.valueOf(vidas), 100, 100, paint);

        if (vidas <= 0) {
            vidas = 0;
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(200);
            canvas.drawText("GAME OVER! ", getWidth() / 2, getHeight() / 2, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initComida();
        personaje.setEjeX(getWidth() / 2);
        personaje.setEjeY(getHeight() - DISTANCIA_SUELO_PERSONAJE);
    }

    private void initComida() {
        Random r = new Random();
        int low = posicionVerticalComida;
        int heigh = -600;
        for (int i = 0; i < NUMERO_ELEMENTO_COMIDA; i++) {
            int ejeY = r.nextInt(low - heigh) + heigh;
            PersonajeModel comidaPollo = new PolloComida(getResources());
            comidaPollo.setEjeY(ejeY);
            comidaPollo.setEjeX(r.nextInt(getWidth() - 300) + 300);

            listaComida.add(comidaPollo);
        }
    }


    public void pintaComida(Canvas canvas) {
        Iterator<PersonajeModel> iter = listaComida.iterator();
        while (iter.hasNext()) {
            PersonajeModel comida = iter.next();
            int y = comida.getEjeY() + posicionVerticalComida;
            // Fuera pantalla por debajo.
            if (y < (canvas.getHeight())) {
                if (!existeColision(comida, canvas)) {
                    canvas.drawBitmap(comida.getBitmap(), comida.getEjeX(), y, null);
                } else {
                    iter.remove();
                }
            } else {
                vidas--;
                if (vidas < 0) {
                    vidas = 0;
                }
            }
        }

    }


    private boolean existeColision(PersonajeModel comida, Canvas canvas) {

        Rect bounds1 = new Rect(personaje.getEjeX(), personaje.getEjeY(), personaje.getEjeX() + personaje.getBitmap().getWidth(), personaje.getEjeY() + personaje.getBitmap().getHeight());
        Rect bounds2 = new Rect(comida.getEjeX(), comida.getEjeY() + posicionVerticalComida, comida.getEjeX() + comida.getBitmap().getWidth(), comida.getEjeY() + posicionVerticalComida + comida.getBitmap().getHeight());


        if (Rect.intersects(bounds1, bounds2)) {
            puntuacion++;
            Log.e("Puntuacion", String.valueOf(puntuacion));
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
        //actualizaPosicionPersonaje(canvas);

    }

    private void pintaFondo(Canvas canvas) {
        fondo = BitmapFactory.decodeResource(getResources(), R.drawable.fondo1);
        canvas.drawBitmap(Bitmap.createScaledBitmap(fondo, getWidth(), getHeight(), false), 0, 0, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("Personaje: ", getWidth() + " ---- " + event.getX());
        boolean movimiento = false;
        if (MotionEvent.ACTION_UP == event.getAction()) {
            Matrix matrix = new Matrix();
            if (getWidth() / 2 < event.getX()) {
                if (personaje.getEjeX() <= getWidth() - 300) {
                    personaje.moverX(PersonajeModel.INDICADOR_MOVIMIENTO_PERSONAJE_DERECHA);
                    movimiento = true;
                    if (!isUltimoMovimientoDerecha() && personaje.getLastXPosition() != 0) {
                        matrix.preScale(-1.0f, 1.0f);
                    }
                }

            } else if (getWidth() / 2 > event.getX()) {
                if (personaje.getEjeX() >= 100) {
                    personaje.moverX(PersonajeModel.INDICADOR_MOVIMIENTO_PERSONAJE_IZQUIERDA);
                    movimiento = true;
                    if (personaje.getLastXPosition() == 0 || isUltimoMovimientoDerecha()) {
                        matrix.preScale(-1.0f, 1.0f);
                    }
                }
            }

            if (movimiento) {
                personaje.addPositionToXHistorical(event.getX());
                Bitmap personajeBitmap = Bitmap.createBitmap(personaje.getBitmap(), 0, 0, personaje.getBitmap().getWidth(), personaje.getBitmap().getHeight(), matrix, true);
                personaje.setBitmap(personajeBitmap);
                invalidate();
            }

        }

        return true;

    }

    private boolean isUltimoMovimientoDerecha() {
        if (getWidth() / 2 < personaje.getLastXPosition()) {
            return true;
        }

        return false;
    }


    public Runnable GameLoop = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(GameLoop, 1000);
            if (vidas > 0) {
                pantalla.actualizaEstadoCanvas();
            }
        }
    };
}
