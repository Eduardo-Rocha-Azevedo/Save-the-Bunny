package com.eduardo.savethebubuny.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.eduardo.savethebubuny.GamePanel;
import com.eduardo.savethebubuny.R;
import com.eduardo.savethebubuny.entity.Entity;

import java.util.Random;

public class Spike extends Entity {
    GamePanel gp; // Removido gp aqui, ele será passado como argumento no construtor
    Bitmap[] spike = new Bitmap[3];
    public int spikeFrame = 0;
    public int spikeX;
    public int spikeY;
    public int spikeVelocity;
    Random random;


    public Spike(Context context, GamePanel gp){
        super(gp);
        this.gp = gp; // Inicializando gp com o gamePanel recebido
        spike[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.spike0);
        spike[1] = BitmapFactory.decodeResource(context.getResources(),R.drawable.spike1);
        spike[2] = BitmapFactory.decodeResource(context.getResources(),R.drawable.spike2);
        random = new Random();
        resetPosition(this);
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

    public void resetPosition(Entity entity){
        spikeX = random.nextInt(gp.dWidth - getSpikeWidth());
        spikeY = -200 + random.nextInt(600)*-1;
        spikeVelocity = 35 + random.nextInt(16);
    }

}