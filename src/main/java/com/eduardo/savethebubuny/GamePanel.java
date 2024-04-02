package com.eduardo.savethebubuny;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends View {

    Bitmap background, ground, rabbit;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30; // Corrigido o nome da variável UPDATE_MILIS para UPDATE_MILLIS
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healthPaint = new Paint();
    float textSize = 120; // Renomeada a variável text_size para textSize para seguir as convenções de nomenclatura
    int points = 0;
    int life = 3;
    public int dWidth, dHeight;
    Random random;
    float rabbitX, rabbitY;
    float oldX, oldRabbitX;
    ArrayList<Spike> spikes;
    ArrayList<Explosion> explosions;

    public GamePanel(Context context) {
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        rabbit = BitmapFactory.decodeResource(getResources(), R.drawable.rabbit);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGround = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        textPaint.setColor(Color.rgb(255, 165, 0));
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.x12y16pxmarumonica));
        healthPaint.setColor(Color.GREEN);
        random = new Random();
        rabbitX = (float) dWidth / 2 - rabbit.getWidth();
        rabbitY = dHeight - ground.getHeight() - rabbit.getHeight();
        spikes = new ArrayList<>();
        explosions = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Spike spike = new Spike(context, GamePanel.this);
            spikes.add(spike);
        }
    }

    @Override
    protected void onDraw(Canvas cv) {
        super.onDraw(cv);
        cv.drawBitmap(background, null, rectBackground, null);
        cv.drawBitmap(ground, null, rectGround, null);
        cv.drawBitmap(rabbit, rabbitX, rabbitY, null);

        for (int i = 0; i < spikes.size(); i++) {
            Spike spike = spikes.get(i);
            cv.drawBitmap(spike.getSpike(spike.spikeFrame), spike.spikeX, spike.spikeY, null);
            spike.spikeFrame++;

            if (spike.spikeFrame > 2) {
                spike.spikeFrame = 0;
            }
            spike.spikeY += spike.spikeVelocity;
            if (spike.spikeY + spike.getSpikeHeight() >= dHeight - ground.getHeight()) {
                points++;
                Explosion explosion = new Explosion(context);
                explosion.explosionX = spike.spikeX;
                explosion.explosionY = spike.spikeY;
                explosions.add(explosion);
                spike.resetPosition();
            }
        }

        // Collision
        // Collision
        for(int i = 0; i < spikes.size(); i++){
            if(spikes.get(i).spikeX + spikes.get(i).getSpikeWidth() >= rabbitX
                    && spikes.get(i).spikeX <= rabbitX + rabbit.getWidth()
                    && spikes.get(i).spikeY + spikes.get(i).getSpikeWidth() >= rabbitY
                    && spikes.get(i).spikeY + spikes.get(i).getSpikeWidth() <= rabbitY + rabbit.getHeight()){

                life--;
                spikes.get(i).resetPosition();
                if(life <= 0) {
                    Intent intent = new Intent(context,GameOver.class);
                    intent .putExtra("Points", points);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            }
        }

        for (int i = 0; i < explosions.size(); i++) {
            Explosion explosion = explosions.get(i);
            cv.drawBitmap(explosion.getExplosion(explosion.explosionFrame), explosion.explosionX, explosion.explosionY, null);
            explosion.explosionFrame++;
            if (explosion.explosionFrame > 3) {
                explosions.remove(i);
            }
        }

        if (life == 2) {
            healthPaint.setColor(Color.YELLOW);
        } else if (life == 1) {
            healthPaint.setColor(Color.RED);
        }
        cv.drawRect(dWidth - 200, 30, dWidth - 200 + 60 * life, 80, healthPaint);
        cv.drawText("" + points, 20, textSize, textPaint);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (touchY >= rabbitY) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.getX();
                oldRabbitX = rabbitX;
            }
            if (action == MotionEvent.ACTION_MOVE) {
                float shift = oldX - touchX;
                float newRabbitX = oldRabbitX - shift;

                if (newRabbitX <= 0)
                    rabbitX = 0;
                else if (newRabbitX >= dWidth - rabbit.getWidth())
                    rabbitX = dWidth - rabbit.getWidth();
                else
                    rabbitX = newRabbitX;
            }
        }
        return true;
    }
}
