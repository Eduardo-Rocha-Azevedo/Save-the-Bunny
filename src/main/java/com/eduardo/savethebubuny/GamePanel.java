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

import com.eduardo.savethebubuny.entity.Spike;

import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends View {
    private boolean isHeartFalling = false;
    private Bitmap background;
    private Bitmap ground;
    public Bitmap player;
    private Bitmap heart;
    private Rect rectBackground, rectGround;
    private Context context;
    private Handler handler;
    private final long UPDATE_MILLIS = 30;
    private Runnable runnable;
    private Paint textPaint = new Paint();
    private Paint healthPaint = new Paint();
    private float textSize = 120;
    private int points = 0;
    private int life = 8;
    public int dWidth;
    int dHeight;
    private Random random;
    private float rabbitX, rabbitY;
    private float oldX, oldRabbitX;
    private ArrayList<Spike> spikes;
    private ArrayList<Explosion> explosions;
    private ArrayList<Heart> hearts;

    public GamePanel(Context context) {
        super(context);
        this.context = context;
        background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        player = BitmapFactory.decodeResource(getResources(), R.drawable.rabbit);
        heart = BitmapFactory.decodeResource(getResources(), R.drawable.heart_full);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0, 0, dWidth, dHeight);
        rectGround = new Rect(0, dHeight - ground.getHeight(), dWidth, dHeight);

        handler = new Handler();
        random = new Random();
        rabbitX = (float) dWidth / 2 - player.getWidth();
        rabbitY = dHeight - ground.getHeight() - player.getHeight();

        spikes = new ArrayList<>();
        explosions = new ArrayList<>();
        hearts = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Spike spike = new Spike(context, this);
            spikes.add(spike);
        }

        // Inicializa o texto e a pintura de saúde
        textPaint.setColor(Color.rgb(255, 165, 0));
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.x12y16pxmarumonica));
        healthPaint.setColor(Color.GREEN);

        // Inicializa o loop de atualização
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };

        // Inicia o processo de geração de corações periodicamente
        spawnHeartsPeriodically();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Desenha o fundo e o chão
        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);

        // Desenha o jogador
        canvas.drawBitmap(player, rabbitX, rabbitY, null);

        // Desenha os spikes e verifica colisões
        for (Spike spike : spikes) {
            canvas.drawBitmap(spike.getSpike(spike.spikeFrame), spike.spikeX, spike.spikeY, null);
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
                spike.resetPosition(spike);
            }
        }

        // Desenha e atualiza a posição dos corações
        for (Heart heart : hearts) {
            canvas.drawBitmap(heart.getHeart(heart.heartFrame), heart.getHeartX(), heart.getHeartY(), null);
            heart.heartY += heart.heartVelocity; // Atualiza a posição do coração
            if (heart.heartY >= dHeight) { // Verifica se o coração caiu fora da tela
                heart.resetPosition(); // Reposiciona o coração
            }
            if (heart.getHeartX() + heart.getHeartWidth() >= rabbitX
                    && heart.getHeartX() <= rabbitX + player.getWidth()
                    && heart.heartY + heart.getHeartHeight() >= rabbitY
                    && heart.heartY <= rabbitY + player.getHeight()) {
                life++; // Aumenta a vida
                heart.resetPosition(); // Reposiciona o coração
            }
        }

        // Verifica colisões com os spikes
        for (Spike spike : spikes) {
            if (spike.spikeX + spike.getSpikeWidth() >= rabbitX
                    && spike.spikeX <= rabbitX + player.getWidth()
                    && spike.spikeY + spike.getSpikeWidth() >= rabbitY
                    && spike.spikeY + spike.getSpikeWidth() <= rabbitY + player.getHeight()) {
                life--;
                spike.resetPosition();
                if (life <= 0) {
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("Points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }

        // Desenha as explosões e remove as antigas
        for (int i = 0; i < explosions.size(); i++) {
            Explosion explosion = explosions.get(i);
            canvas.drawBitmap(explosion.getExplosion(explosion.explosionFrame), explosion.explosionX, explosion.explosionY, null);
            explosion.explosionFrame++;
            if (explosion.explosionFrame > 3) {
                explosions.remove(i);
            }
        }

        // Atualiza a cor da barra de vida com base na quantidade de vida restante
        if (life == 2) {
            healthPaint.setColor(Color.YELLOW);
        } else if (life == 1) {
            healthPaint.setColor(Color.RED);
        }

        // Desenha a barra de vida
        canvas.drawRect(dWidth - 200, 30, dWidth - 200 + 60 * life, 80, healthPaint);

        // Desenha a pontuação
        canvas.drawText("" + points, 30, textSize, textPaint);

        // Programa a próxima atualização
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
                else if (newRabbitX >= dWidth - player.getWidth())
                    rabbitX = dWidth - player.getWidth();
                else
                    rabbitX = newRabbitX;
            }
        }
        return true;
    }

    // Método para criar um novo coração e adicioná-lo à lista
    private void spawnHeart() {
        if (!isHeartFalling) {
            Heart heart = new Heart(context, this);
            hearts.add(heart);
            isHeartFalling = true; // Ativa a queda de corações
        }
    }

    // Método para gerar corações periodicamente
    private void spawnHeartsPeriodically() {
        // Define um intervalo de tempo em milissegundos para gerar corações (por exemplo, a cada 5 segundos)
        long heartSpawnInterval = 20000;

        // Cria um novo Runnable para gerar corações periodicamente
        Runnable heartSpawner = new Runnable() {
            @Override
            public void run() {
                // Gera um novo coração e o adiciona à lista
                spawnHeart();

                // Programa a próxima chamada deste Runnable após o intervalo de tempo especificado
                handler.postDelayed(this, heartSpawnInterval);
            }
        };

        // Inicia o processo de geração de corações
        handler.postDelayed(heartSpawner, heartSpawnInterval);
    }
}
