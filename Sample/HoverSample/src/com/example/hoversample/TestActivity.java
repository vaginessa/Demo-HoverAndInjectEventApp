package com.example.hoversample;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class TestActivity extends Activity
{
	private ImageView img;
	private FrameLayout layout, vg_motion_field;
	// -------------------------------->
	private boolean makeAction,mOpenAsSmallWindow = true;
	private Point point;
	// -------------------------------->
	// Activity Methods
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		img = (ImageView) findViewById(R.id.imageView1);

		layout = (FrameLayout) findViewById(R.id.FrameLayout1);

		vg_motion_field = (FrameLayout) findViewById(R.id.vg_motion_field);

		setHoverListener(layout, vg_motion_field);

		vg_motion_field.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				makeAction = true;
				finish();
			}
		});

	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (makeAction && point!=null)
		{
			commandTap(point);
		}
	}

	@Override
	public void onAttachedToWindow()
	{
		super.onAttachedToWindow();
		if (mOpenAsSmallWindow)
		{
			final View view = getWindow().getDecorView();
			final WindowManager.LayoutParams lp = (WindowManager.LayoutParams) view
					.getLayoutParams();
			lp.gravity = Gravity.LEFT | Gravity.TOP;
			// Width and height
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			lp.width = size.x;
			lp.height = size.y - getStatusBarHeight();
			// Update
			getWindowManager().updateViewLayout(view, lp);
		}
	}

	// -------------------------------->
	//Methods
	
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
					case MotionEvent.ACTION_HOVER_MOVE: // On Hover
						float currentX = motionEvent.getX();
						float currentY = motionEvent.getY();

						currentX = getPointCoord(screenWidth,
								vg_motion_field.getWidth(), (int) currentX);
						currentY = getPointCoord(screenHeight,
								vg_motion_field.getHeight(), (int) currentY);
						
						point = new Point((int)currentX,(int)currentY);

						layoutParam.leftMargin = (int) (currentX - (img
								.getWidth() / 2));
						layoutParam.topMargin = (int) (currentY - (img
								.getHeight() / 2));
						parent.updateViewLayout(img, layoutParam);
						break;
				}
				return false;
			}
		});

		hoverView.setHovered(true);
	}
	
	public void commandTap(final Point point)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					ServerClient.tap(point);
				}
				catch (Exception e)
				{
					Log.e("DEBUG", e.getMessage(), e);
				}
			}
		}).start();
	}
	
	// -------------------------------->
	//Helpers

	public int getStatusBarHeight()
	{
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0)
		{
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}

	public int getPointCoord(int screen, int rectangle, int point)
	{
		return (screen * point) / rectangle;
	}

}
