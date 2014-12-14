package mobizenclasses;

import android.os.Build;

public class iq
{
  ir aYi;
  
//  static
//  {
//    if (!iq.class.desiredAssertionStatus()) {}
//    for (boolean bool = true;; bool = false)
//    {
//     // aNj = bool;
//     // return;
//    }
//  }
  
  public final int Nb() throws Exception
  {
    int i;
    try
    {
      i = Build.VERSION.SDK_INT;
      if ((i >= 0) && (i <= 15)) {
     //   this.aYi = new im();
      } else if (i == 16) {
        this.aYi = new in();
      }
    }
    catch (Exception localException)
    {
      new StringBuilder("EX.").append(localException.toString()).toString();
      return -1;
    }
    if (i >= 17)
    {
      this.aYi = new io();
    }
    else
    {
      this.aYi = new ip();
      new StringBuilder("invalid android version: ").append(Build.VERSION.SDK_INT).toString();
    }
    return 0;
  }
  
  public final void aa(int paramInt1, int paramInt2)
  {
	  
   // if ((!aNj) && (paramInt1 != 0) && (paramInt1 != 1))
	  {
      //throw new AssertionError();
    }
    this.aYi.Y(paramInt1, paramInt2);
  }
  
  public final void d(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    boolean aNj = false;
	if ((!aNj) && (paramInt1 != 0) && (paramInt1 != 1)) {
      throw new AssertionError();
    }
    if (paramInt4 == 32768)
    {
      this.aYi.m(paramInt1, paramInt2, paramInt3);
      return;
    }
    this.aYi.c(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  public final void p(int paramInt1, int paramInt2, int paramInt3)
  {
    this.aYi.n(paramInt1, paramInt2, paramInt3);
  }
}
