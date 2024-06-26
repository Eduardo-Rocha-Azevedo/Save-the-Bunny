package com.eduardo.savethebubuny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Spike {
    GamePanel gp; // Removido gp aqui, ele será passado como argumento no construtor
    Bitmap spike[]= new Bitmap[3];
    int spikeFrame = 0;
    int spikeX, spikeY, spikeVelocity;
    Random random;


    public Spike(Context context, GamePanel gp){
        this.gp = gp; // Inicializando gp com o gamePanel recebido
        spike[0] = BitmapFactory.decodeResource(context.getResources(),R.drawable.spike0);
        spike[1] = BitmapFactory.decodeResource(context.getResources(),R.drawable.spike1);
        spike[2] = BitmapFactory.decodeResource(context.getResources(),R.drawable.spike2);
        random = new Random();
        resetPosition();
    }

    public Bitmap getSpike(int spikeFrame){
        return spike[spikeFrame];
    }

    public int getSpikeWidth(){
        return spike[0].getWidth();
    }

    public int getSpikeHeight(){
        return spike[0].getHeight();
    }

    public void resetPosition() {
        spikeX = random.nextInt(gp.dWidth - getSpikeWidth());
        spikeY = -200 + random.nextInt(600)*-1;
        spikeVelocity = 35 + random.nextInt(16);
    }

}