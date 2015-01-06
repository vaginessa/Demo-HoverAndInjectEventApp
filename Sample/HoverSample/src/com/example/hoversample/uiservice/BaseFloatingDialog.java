package com.example.hoversample.uiservice;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.example.hoversample.R;

public abstract class BaseFloatingDialog extends StandOutWindow
{

	public static boolean running ; 
	
	@Override
	public String getAppName()
	{
		return getString(R.string.app_name);
	}

	@Override
	public int getAppIcon()
	{
		return R.drawable.ic_launcher;
	}

	@Override
	public StandOutLayoutParams getParams(int id, Window window)
	{
		int width = 1000;
		int height = 500;
		int left = StandOutLayoutParams.LEFT;
		int top = StandOutLayoutParams.TOP;
		return new StandOutLayoutParams(id, width, height, left, top);
	}

	@Override
	public boolean onShow(int id, Window window)
	{
		running = true ;		
		return super.onShow(id, window);
	}
	
	@Override
	public boolean onHide(int id, Window window)
	{
		running = false ;
		return super.onHide(id, window);
	}

	@Override
	public boolean stopService(Intent name)
	{
		running = false ;
		return super.stopService(name);
	}
	
	@Override
	public boolean onClose(int id, Window window)
	{	
		running = false ;
		return super.onClose(id, window);
	}
	
	// ---------------------------------

	
	// move the window by dragging the view
	@Override
	public int getFlags(int id)
	{
		return super.getFlags(id) | StandOutFlags.FLAG_BODY_MOVE_ENABLE
				| StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
				//| StandOutFlags.FLAG_WINDOW_FOCUSABLE_DISABLE;
	}

	public static void show(Context cxt,
			Class<? extends BaseFloatingDialog> class1)
	{
		dismiss(cxt, class1);
		StandOutWindow.show(cxt, class1, StandOutWindow.DEFAULT_ID);
	}

	public static void dismiss(Context cxt,
			Class<? extends BaseFloatingDialog> class1)
	{
		StandOutWindow.closeAll(cxt, class1);
	}

	public static void sendAppData(Context cxt,
			Class<? extends BaseFloatingDialog> toClass,
			Class<? extends BaseFloatingDialog> fromClass, Bundle bundle)
	{
		sendData(cxt, toClass, StandOutWindow.DEFAULT_ID,
				StandOutWindow.DEFAULT_ID, bundle, fromClass,
				StandOutWindow.DEFAULT_ID);
	}

	@Override
	public int getThemeStyle()
	{
		return R.style.AppTheme;
	}

	@Override
	public Notification getPersistentNotification(int id)
	{
		return null;
	}
}
