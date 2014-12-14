package mobizenclasses;

import android.hardware.input.IInputManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.InputEvent;
import android.view.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class in
  implements ir
{
  private static Object aYc;
  private static Method aYd;
  private static Method aYe;
  private static IInputManager aYf;
  il aYa = new il();
  
  public in() throws Exception
  {
    Na();
    Method localMethod1 = Class.forName("android.os.ServiceManager").getMethod("getService", new Class[] { String.class });
    Class localClass = Class.forName("android.os.IPowerManager");
    Method localMethod2 = Class.forName("android.os.IPowerManager$Stub").getMethod("asInterface", new Class[] { IBinder.class });
    Class[] arrayOfClass = new Class[3];
    arrayOfClass[0] = Long.TYPE;
    arrayOfClass[1] = Boolean.TYPE;
    arrayOfClass[2] = Boolean.TYPE;
    aYd = localClass.getMethod("userActivityWithForce", arrayOfClass);
    aYe = localClass.getMethod("isScreenOn", new Class[0]);
    new StringBuilder("EX.").append(aYe.toString()).toString();
    aYc = localMethod2.invoke(null, new Object[] { localMethod1.invoke(null, new Object[] { "power" }) });
  }
  
  private static boolean MX() throws Exception
  {
    Na();
    Method localMethod1 = Class.forName("android.os.ServiceManager").getMethod("getService", new Class[] { String.class });
    Class localClass = Class.forName("android.os.IPowerManager");
    Method localMethod2 = Class.forName("android.os.IPowerManager$Stub").getMethod("asInterface", new Class[] { IBinder.class });
    Class[] arrayOfClass = new Class[3];
    arrayOfClass[0] = Long.TYPE;
    arrayOfClass[1] = Boolean.TYPE;
    arrayOfClass[2] = Boolean.TYPE;
    aYd = localClass.getMethod("userActivityWithForce", arrayOfClass);
    aYe = localClass.getMethod("isScreenOn", new Class[0]);
    new StringBuilder("EX.").append(aYe.toString()).toString();
    aYc = localMethod2.invoke(null, new Object[] { localMethod1.invoke(null, new Object[] { "power" }) });
    return true;
  }
  
  private static boolean MZ()
  {
    try
    {
      if (((Boolean)aYe.invoke(aYc, new Object[0])).booleanValue()) {
        return false;
      }
      Method localMethod = aYd;
      Object localObject = aYc;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = Long.valueOf(SystemClock.uptimeMillis());
      arrayOfObject[1] = Boolean.valueOf(true);
      arrayOfObject[2] = Boolean.valueOf(true);
      localMethod.invoke(localObject, arrayOfObject);
      return true;
    }
    catch (Exception localException)
    {
      new StringBuilder("EX.").append(localException.toString()).toString();
    }
    return false;
  }
  
  private static boolean Na() throws Exception
  {
    Class localClass = Class.forName("android.hardware.input.InputManager");
    try
    {
      Object localObject = localClass.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
      Field localField = localClass.getDeclaredField("mIm");
      localField.setAccessible(true);
      aYf = (IInputManager)localField.get(localObject);
      return true;
    }
    catch (Exception localException)
    {
      new StringBuilder("EX.").append(localException.toString()).toString();
    }
    return true;
  }
  
  private static void Z(int paramInt1, int paramInt2)
  {
    if (((paramInt2 == 6) || (paramInt2 == 26)) && (paramInt1 == 1) && (!isScreenOn())) {
      MZ();
    }
  }
  
  private static void a(InputEvent paramInputEvent)
  {
    try
    {
      aYf.injectInputEvent(paramInputEvent, 0);
      return;
    }
    catch (RemoteException localRemoteException) {}
  }
  
  private static boolean isScreenOn()
  {
    try
    {
      boolean bool = ((Boolean)aYe.invoke(aYc, new Object[0])).booleanValue();
      return bool;
    }
    catch (Exception localException) {}
    return true;
  }
  
  public final void Y(int paramInt1, int paramInt2)
  {
    if (((paramInt2 == 6) || (paramInt2 == 26)) && (paramInt1 == 1) && (!isScreenOn())) {
      MZ();
    }
    if (paramInt2 == 6) {
      paramInt2 = 26;
    }
    KeyEvent localKeyEvent = this.aYa.X(paramInt1, paramInt2);
    if (localKeyEvent == null) {
      return;
    }
    try
    {
      aYf.injectInputEvent(localKeyEvent, 0);
      return;
    }
    catch (RemoteException localRemoteException) {}
  }
  
  public final void c(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    if (paramInt1 == 2)
    {
      a(this.aYa.b(2, paramInt2, paramInt3, paramInt4, paramInt5));
      return;
    }
    if (paramInt1 == 0)
    {
      a(this.aYa.b(0, paramInt2, paramInt3, paramInt4, paramInt5));
      a(this.aYa.b(261, paramInt2, paramInt3, paramInt4, paramInt5));
      return;
    }
    a(this.aYa.b(262, paramInt2, paramInt3, paramInt4, paramInt5));
    a(this.aYa.b(1, paramInt2, paramInt3, paramInt4, paramInt5));
  }
  
  public final void m(int paramInt1, int paramInt2, int paramInt3)
  {
    a(this.aYa.k(paramInt1, paramInt2, paramInt3));
  }
  
  public final void n(int paramInt1, int paramInt2, int paramInt3)
  {
    StringBuilder localStringBuilder = new StringBuilder("EX.");
    Object[] arrayOfObject = new Object[3];
    arrayOfObject[0] = Integer.valueOf(paramInt1);
    arrayOfObject[1] = Integer.valueOf(paramInt2);
    arrayOfObject[2] = Integer.valueOf(paramInt3);
    localStringBuilder.append(String.format("wheel (%d, %d, delta: %d", arrayOfObject)).toString();
    a(this.aYa.l(paramInt1, paramInt2, paramInt3));
  }
}
