package com.example.labidnp06;

import android.content.Context;

public class Metodos {

    //
    public static int dip2px(Context context, float dipValue){
        //Densidad lógica de la pantalla
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
    //
    public static int sp2px(Context context, float spValue) {
        // Un factor de escala para las fuentes que se muestran en la pantalla.
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
