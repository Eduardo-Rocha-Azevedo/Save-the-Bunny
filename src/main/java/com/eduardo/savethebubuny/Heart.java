package com.eduardo.savethebubuny;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.eduardo.savethebubuny.entity.Entity;

import java.util.Random;

public class Heart extends Entity {
   int heartFrame;
   private Bitmap[] heart = new Bitmap[1];
   private int heartX;
   int heartY;
   int heartVelocity;

   public Heart(Context context, GamePanel gp) {
      super(gp);
      this.heart[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart_full);
      random = new Random();
      resetPosition();
   }

   public Bitmap getHeart(int heartFrame) {
      return heart[heartFrame];
   }

   public void resetPosition() {
      heartX = random.nextInt(gp.dWidth - getWidth());
      heartY = -200 + random.nextInt(600) * -1;
      heartVelocity = 35 + random.nextInt(16);
   }

   public int getHeartX() {
      return heartX;
   }

   public int getHeartY() {
      return heartY;
   }

   public int getHeartWidth() {
      return heart[0].getWidth();
   }

   public int getHeartHeight() {
      return heart[0].getHeight();
   }
}
