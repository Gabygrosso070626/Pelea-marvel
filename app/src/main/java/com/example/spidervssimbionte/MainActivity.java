package com.example.spidervssimbionte;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.*;
import android.view.*;
import android.content.Context;
import java.util.Random;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setNavigationBarColor(Color.BLACK);
        setContentView(new FightView(this));
    }
}

class FightView extends View {

    Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    Random random = new Random();

    float spiderX = 0.28f;
    float simbionteX = 0.72f;

    int spiderHP = 100;
    int simbionteHP = 100;

    int spiderEnergy = 0;
    int simbionteEnergy = 0;

    boolean left = false;
    boolean right = false;
    boolean attack = false;
    boolean special = false;

    long lastTime = System.currentTimeMillis();

    FightView(Context context) {
        super(context);
        p.setTypeface(Typeface.create("sans", Typeface.BOLD));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();
        int height = getHeight();

        float delta =
                Math.min(
                        0.04f,
                        (System.currentTimeMillis() - lastTime) / 1000f
                );

        lastTime = System.currentTimeMillis();

        // =========================
        // MOVIMIENTO
        // =========================

        if (left)
            spiderX -= delta * 0.22f;

        if (right)
            spiderX += delta * 0.22f;

        spiderX = Math.max(0.08f, Math.min(0.52f, spiderX));


        // =========================
        // ATAQUE
        // =========================

        float distancia =
                Math.abs(spiderX - simbionteX);

        if (attack && distancia < 0.23f) {

            simbionteHP =
                    Math.max(0, simbionteHP - 1);

            spiderEnergy =
                    Math.min(100, spiderEnergy + 1);
        }


        // =========================
        // ATAQUE ESPECIAL
        // =========================

        if (special &&
                spiderEnergy >= 30 &&
                distancia < 0.32f) {

            simbionteHP =
                    Math.max(0, simbionteHP - 3);

            spiderEnergy -= 30;
        }


        // =========================
        // ATAQUE DEL SIMBIONTE
        // =========================

        if (random.nextFloat() < delta * 0.45f &&
                distancia < 0.25f) {

            spiderHP =
                    Math.max(0, spiderHP - 1);

            simbionteEnergy =
                    Math.min(100, simbionteEnergy + 1);
        }


        // =========================
        // FONDO
        // =========================

        canvas.drawColor(
                Color.rgb(24, 28, 42)
        );

        p.setColor(
                Color.rgb(40, 70, 95)
        );

        canvas.drawRect(
                0,
                height * 0.58f,
                width,
                height,
                p
        );

        p.setColor(
                Color.rgb(55, 55, 65)
        );

        canvas.drawRect(
                0,
                height * 0.76f,
                width,
                height,
                p
        );


        // SOL

        p.setColor(
                Color.rgb(235, 205, 150)
        );

        canvas.drawCircle(
                width * 0.82f,
                height * 0.32f,
                height * 0.10f,
                p
        );


        // =========================
        // BARRAS DE VIDA
        // =========================

        drawHealthBar(
                canvas,
                width * 0.06f,
                height * 0.07f,
                width * 0.38f,
                spiderHP,
                true
        );

        drawHealthBar(
                canvas,
                width * 0.56f,
                height * 0.07f,
                width * 0.94f,
                simbionteHP,
                false
        );


        // =========================
        // PERSONAJES
        // =========================

        drawFighter(
                canvas,
                spiderX * width,
                height * 0.60f,
                true
        );

        drawFighter(
                canvas,
                simbionteX * width,
                height * 0.60f,
                false
        );


        // =========================
        // TÍTULO
        // =========================

        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(height * 0.055f);
        p.setColor(Color.WHITE);

        canvas.drawText(
                "SPIDER  VS  SIMBIONTE",
                width / 2f,
                height * 0.09f,
                p
        );


        // =========================
        // GANADOR
        // =========================

        if (spiderHP == 0 ||
                simbionteHP == 0) {

            p.setTextSize(height * 0.11f);
            p.setColor(Color.YELLOW);

            String ganador;

            if (spiderHP == 0)
                ganador = "SIMBIONTE GANA";
            else
                ganador = "SPIDER GANA";

            canvas.drawText(
                    ganador,
                    width / 2f,
                    height * 0.43f,
                    p
            );
        }


        // =========================
        // CONTROLES
        // =========================

        drawControls(
                canvas,
                width,
                height
        );

        invalidate();
    }


    // ==================================================
    // BARRA DE VIDA
    // ==================================================

    void drawHealthBar(
            Canvas canvas,
            float left,
            float top,
            float right,
            int hp,
            boolean spider) {

        p.setColor(Color.DKGRAY);

        canvas.drawRect(
                left,
                top,
                right,
                top + getHeight() * 0.
