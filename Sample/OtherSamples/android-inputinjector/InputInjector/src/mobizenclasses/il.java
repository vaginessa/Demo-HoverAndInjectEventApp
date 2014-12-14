package mobizenclasses;

import android.os.Build;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;

public final class il
{
  is aXT;
  long aXU = -1L;
  private int aXV = 0;
  private long aXW = -1L;
  private int aXX = -1;
  final int[] aXY;
  MotionEvent.PointerCoords[] aXZ;
  
  public il()
  {
    int[] arrayOfInt = new int[2];
    arrayOfInt[1] = 1;
    this.aXY = arrayOfInt;
    MotionEvent.PointerCoords[] arrayOfPointerCoords = new MotionEvent.PointerCoords[2];
    arrayOfPointerCoords[0] = new MotionEvent.PointerCoords();
    arrayOfPointerCoords[1] = new MotionEvent.PointerCoords();
    this.aXZ = arrayOfPointerCoords;
    if (Build.VERSION.SDK_INT >= 14) {
      this.aXT = new is();
    }
  }
  
  private static boolean hI(int paramInt)
  {
    return (paramInt == 3) || (paramInt == 84) || (paramInt == 82) || (paramInt == 6) || (paramInt == 4) || (paramInt == 26);
  }
  
  public final KeyEvent X(int paramInt1, int paramInt2)
  {
    long l1 = SystemClock.uptimeMillis();
    int i = 0;
    if ((paramInt2 != 3) && (paramInt2 != 84) && (paramInt2 != 82) && (paramInt2 != 6) && (paramInt2 != 4) && (paramInt2 != 26)) {
      i = 0;
    }
    while (i != 0)
    {
      long l2 = 0;
      if (paramInt1 == 0)
      {
        if (this.aXX > 0)
        {
          return null;
         // i = 1;
         // continue;
        }
        int k = 1 + this.aXX;
        this.aXX = k;
        if (k == 0) {
          this.aXW = l1;
        }
        StringBuilder localStringBuilder = new StringBuilder("EX.");
        Object[] arrayOfObject = new Object[4];
        arrayOfObject[0] = Integer.valueOf(paramInt1);
        arrayOfObject[1] = Integer.valueOf(paramInt2);
        arrayOfObject[2] = Integer.valueOf(0);
        arrayOfObject[3] = Integer.valueOf(this.aXX);
        localStringBuilder.append(String.format("keyevent3.act(%d), code(%d), scancode(%d), repeat(%d)", arrayOfObject)).toString();
        l2 = this.aXW;
        if (this.aXX < 0) {
        //  break label206;
        }
      }
      label206:
      for (int j = this.aXX;; j = 0)
      {
        return new KeyEvent(l2, l1, paramInt1, paramInt2, j, 0, -1, 0, 0, 257);
        //this.aXX = -1;
      //  break;
      }
    }
    if (paramInt2 == 59) {
      if (paramInt1 == 0) {
        this.aXV = (0x41 | this.aXV);
      }
    }
    for (;;)
    {
      if (paramInt1 == 0) {
        this.aXW = l1;
      }
      return new KeyEvent(this.aXW, l1, paramInt1, paramInt2, 0, this.aXV, -1, 0, 0, 257);
    //  this.aXV = (0xFFFFFFBE & this.aXV);
     // continue;
//      if (paramInt2 == 57)
//      {
//        if (paramInt1 == 0) {
//          this.aXV = (0x12 | this.aXV);
//        } else {
//          this.aXV = (0xFFFFFFED & this.aXV);
//        }
//      }
//      else if (paramInt2 == 113) {
//        if (paramInt1 == 0) {
//          this.aXV = (0x3000 | this.aXV);
//        } else {
//          this.aXV = (0xFFFFDFFF & this.aXV);
//        }
//      }
    }
  }
  
  public final MotionEvent b(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this.aXZ[0].x = paramInt2;
    this.aXZ[0].y = paramInt3;
    this.aXZ[0].pressure = 1.0F;
    this.aXZ[0].size = 1.0F;
    this.aXZ[1].x = paramInt4;
    this.aXZ[1].y = paramInt5;
    this.aXZ[1].pressure = 1.0F;
    this.aXZ[1].size = 1.0F;
    switch (paramInt1)
    {
    default: 
      return null;
    case 2: 
      return MotionEvent.obtain(this.aXU, SystemClock.uptimeMillis(), 2, 2, this.aXY, this.aXZ, 0, 1.0F, 1.0F, 0, 0, 4098, 0);
    case 0: 
      this.aXU = SystemClock.uptimeMillis();
      return MotionEvent.obtain(this.aXU, this.aXU, 0, 1, this.aXY, this.aXZ, 0, 1.0F, 1.0F, 0, 0, 4098, 0);
    case 261: 
      this.aXU = SystemClock.uptimeMillis();
      return MotionEvent.obtain(this.aXU, this.aXU, 261, 2, this.aXY, this.aXZ, 0, 1.0F, 1.0F, 0, 0, 4098, 0);
    case 262: 
      return MotionEvent.obtain(this.aXU, SystemClock.uptimeMillis(), 262, 2, this.aXY, this.aXZ, 0, 1.0F, 1.0F, 0, 0, 4098, 0);
    }
  //  return MotionEvent.obtain(this.aXU, SystemClock.uptimeMillis(), 1, 1, this.aXY, this.aXZ, 0, 1.0F, 1.0F, 0, 0, 4098, 0);
  }
  
  final MotionEvent k(int paramInt1, int paramInt2, int paramInt3)
  {
    StringBuilder localStringBuilder = new StringBuilder("EX.");
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = Integer.valueOf(paramInt1);
    arrayOfObject[1] = Integer.valueOf(paramInt2);
    arrayOfObject[2] = Integer.valueOf(paramInt3);
    localStringBuilder.append(String.format("jexe.touch: %d, (%d, %d)", arrayOfObject)).toString();
    long l = SystemClock.uptimeMillis();
    if (paramInt1 == 0) {
      this.aXU = l;
    }
    MotionEvent localMotionEvent = MotionEvent.obtain(this.aXU, l, paramInt1, paramInt2, paramInt3, 0);
    localMotionEvent.setSource(4098);
    return localMotionEvent;
  }
  
  final MotionEvent l(int paramInt1, int paramInt2, int paramInt3)
  {
    return this.aXT.l(paramInt1, paramInt2, paramInt3);
  }
}
