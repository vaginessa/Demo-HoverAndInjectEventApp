package com.example.hoversample.uiservice;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.hoversample.R;

public class FloatingOverlayService extends Service
{
	private View rootView;
	private TextView textView;
	private ImageView img;
	private FrameLayout floatingLayer;
	// -------------------
	private long lastmodified;
	private float startX;
	private float startY;

	@Override
	public void onCreate()
	{
		super.onCreate();

		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		rootView = inflater.inflate(R.layout.view_floating_layer, null);
		floatingLayer = (FrameLayout) rootView.findViewById(R.id.floatinglayer);
		textView = (TextView) rootView.findViewById(R.id.textView1);
		img = (ImageView) rootView.findViewById(R.id.imageView1);

		WindowManager.LayoutParams playAreaParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
						| WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
				PixelFormat.TRANSLUCENT);

		playAreaParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		playAreaParams.height =1000;

		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.addView(rootView, playAreaParams);

		setHoverListener(floatingLayer, textView);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (rootView != null)
		{
			((WindowManager) getSystemService(WINDOW_SERVICE))
					.removeView(rootView);
			rootView = null;
		}
	}

	public void setHoverListener(final ViewGroup parent, View hoverView)
	{
		hoverView.setOnHoverListener(new OnHoverListener()
		{

			@Override
			public boolean onHover(View view, MotionEvent motionEvent)
			{

				FrameLayout.LayoutParams layoutParam = (android.widget.FrameLayout.LayoutParams) img
						.getLayoutParams();

				int screenWidth = getResources().getDisplayMetrics().widthPixels - 1;
				int screenHeight = getResources().getDisplayMetrics().heightPixels - 1;

				switch (motionEvent.getAction())
				{
					case MotionEvent.ACTION_HOVER_ENTER: // Enter
						startX = motionEvent.getX();
						startY = motionEvent.getY();

						break;
					case MotionEvent.ACTION_HOVER_EXIT: // Leave Hovered

						break;
					case MotionEvent.ACTION_HOVER_MOVE: // On Hover
						float currentX = motionEvent.getX();
						float currentY = motionEvent.getY();

						int factor = 1;
						float deltaX = (currentX - startX) / factor;
						float deltaY = (currentY - startY) / factor;

						int newX = (int) currentX;// (int)
													// (layoutParam.leftMargin +
													// deltaX);
						int newY = (int) currentY;// (int)
													// (layoutParam.topMargin +
													// deltaY);

						Log.i("DEBUG", "Current x,y " + currentX + " "
								+ currentY);
						Log.i("DEBUG", "Delta x,y " + deltaX + " " + deltaY);

						Log.i("DEBUG", "Layout Params x,y "
								+ layoutParam.leftMargin + " "
								+ layoutParam.topMargin);
						Log.i("DEBUG", "New Layout Params x,y " + newX + " "
								+ newY);
						Log.i("DEBUG", "Screen Width,Height " + screenWidth
								+ " " + screenHeight);
						Log.i("DEBUG", "------------");

						if (newX > 0 && newY > 0 && newX < screenWidth
								&& newY < screenHeight)
						{
							layoutParam.leftMargin = newX
									- (img.getWidth() / 2);
							layoutParam.topMargin = newY
									- (img.getHeight() / 2);
							parent.updateViewLayout(img, layoutParam);
						}

						updateTextView(motionEvent);
						break;
				}
				return false;
			}

			public boolean updateTextView(MotionEvent motionEvent)
			{
				long now = System.currentTimeMillis();
				long diff = now - lastmodified;
				if (diff > 500)
				{
					textView.setText(motionEvent.getX() + " "
							+ motionEvent.getY());
					lastmodified = now;
					return true;
				}
				else
				{
					return false;
				}
			}
		});

		hoverView.setHovered(true);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
