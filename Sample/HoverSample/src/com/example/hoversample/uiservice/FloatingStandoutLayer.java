package com.example.hoversample.uiservice;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.hoversample.R;

public class FloatingStandoutLayer extends BaseFloatingDialog
{
	private View rootView;
	private TextView textView;
	private ImageView img;
	private FrameLayout floatingLayer;
	//----
	private long lastmodified;
	private float startX;
	private float startY;


	@Override
	public void createAndAttachView(int id, FrameLayout frame)
	{
		LayoutInflater inflater = LayoutInflater.from(this);
		rootView = inflater.inflate(
				R.layout.merge_floating_layer, frame, true);		
		floatingLayer = (FrameLayout) rootView.findViewById(R.id.floatinglayer);
		textView = (TextView) rootView.findViewById(R.id.textView1);
		img = (ImageView) rootView.findViewById(R.id.imageView1);
		
		setHoverListener(floatingLayer,textView);
	}

	public void setHoverListener(final ViewGroup parent,View hoverView)
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

}
