package com.eduardo.savethebubuny.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.eduardo.savethebubuny.GamePanel;
import com.eduardo.savethebubuny.R;

import java.util.Random;

public class Player extends Entity{
  GamePanel gp;
   Context context;
   public Player(Context context, GamePanel gp){
      super(gp);
      this.gp = gp;

      gp.player = BitmapFactory.decodeResource(context.getResources(), R.drawable.rabbit);
      random = new Random();
      resetPosition();
   }
}
