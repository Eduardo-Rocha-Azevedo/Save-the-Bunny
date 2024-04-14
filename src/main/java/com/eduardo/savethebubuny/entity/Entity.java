package com.eduardo.savethebubuny.entity;

import android.content.Context;
import android.graphics.Bitmap;

import com.eduardo.savethebubuny.GamePanel;

import java.util.IllegalFormatWidthException;
import java.util.Random;
public class Entity {
   protected GamePanel gp;
   public int x, y, velocity;
   protected Random random = new Random();
   protected Bitmap bitmap;
   private boolean isActive = true;
   public Entity(GamePanel gp) {
      this.gp = gp;
   }

   // Define a posição inicial da entidade
   public void resetPosition() {
      x = random.nextInt(gp.dWidth - getWidth());
      y = -200 + random.nextInt(600) * -1;
      velocity = 35 + random.nextInt(16);
   }

   // Retorna a largura da entidade
   public int getWidth() {
      if (bitmap != null) {
         return bitmap.getWidth();
      } else {
         return 0;
      }
   }

   // Retorna o bitmap da entidade
   public Bitmap getBitmap() {
      return bitmap;
   }

   // Retorna se a entidade está ativa ou não
   public boolean isActive() {
      return true;
   }

   // Define se a entidade está ativa ou não
   public void setActive(boolean active) {
      this.isActive = active;
   }

   // Retorna a coordenada x da entidade
   public float getX() {
      return x;
   }

   // Retorna a coordenada y da entidade
   public float getY() {
      return y;
   }

   // Retorna a altura da entidade
   public float getHeight() {
      if (bitmap != null) {
         return bitmap.getHeight();
      } else {
         return 0;
      }
   }

   public void update() {
      y += velocity;
   }
}

