package mobizenclasses;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.MotionEvent.PointerProperties;

public final class is
{
  private MotionEvent.PointerProperties[] aYj;
  private MotionEvent.PointerCoords[] aYk;
  
  public is()
  {
    MotionEvent.PointerProperties[] arrayOfPointerProperties = new MotionEvent.PointerProperties[1];
    arrayOfPointerProperties[0] = new MotionEvent.PointerProperties();
    this.aYj = arrayOfPointerProperties;
    MotionEvent.PointerCoords[] arrayOfPointerCoords = new MotionEvent.PointerCoords[1];
    arrayOfPointerCoords[0] = new MotionEvent.PointerCoords();
    this.aYk = arrayOfPointerCoords;
  }
  
  final MotionEvent l(int paramInt1, int paramInt2, int paramInt3)
  {
    StringBuilder localStringBuilder = new StringBuilder("EX.");
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = Integer.valueOf(paramInt1);
    arrayOfObject[1] = Integer.valueOf(paramInt2);
    arrayOfObject[2] = Integer.valueOf(paramInt3);
    localStringBuilder.append(String.format("jexe.wheel: (%d, %d), %d", arrayOfObject)).toString();
    long l = SystemClock.uptimeMillis();
    this.aYj[0].id = 0;
    this.aYj[0].toolType = 3;
    this.aYk[0].setAxisValue(0, paramInt1);
    this.aYk[0].setAxisValue(1, paramInt2);
    this.aYk[0].setAxisValue(9, paramInt3);
    return MotionEvent.obtain(l, l, 8, 1, this.aYj, this.aYk, 0, 0, 0.0F, 0.0F, 0, 0, 8194, 0);
  }
}
