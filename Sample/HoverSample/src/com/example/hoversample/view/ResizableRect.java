package com.example.hoversample.view;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.example.hoversample.R;

public class ResizableRect extends View
{
	private static final String STROKE_COLOR = "#AADB1255";
	private static final String FILL_COLOR = "#55DB1255";
	private static final int TOP_LEFT = 0;
	private static final int TOP_RIGHT = 3;
	private static final int BOTTOM_LEFT = 1;
	private static final int BOTTOM_RIGHT = 2;

	/**
	 * point1 and point 3 are of same group and same as point 2 and point4
	 */
	// variable to know what ball is being dragged
	private int selectedGroupId = -1;
	private int selectedBalID = 0;
	// array that holds the balls
	private ArrayList<ColorBall> colorballs = new ArrayList<ColorBall>();
	// ----->
	private int startX, startY;
	private Paint paint;
	private boolean resizing, dragging;
	private IDragger iDragger;

	public ResizableRect(Context context)
	{
		super(context);
		paint = new Paint();
		setFocusable(true); // necessary for getting the touch events
		initBalls();
	}

	public ResizableRect(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public ResizableRect(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		paint = new Paint();
		setFocusable(true); // necessary for getting the touch events
		initBalls();
	}

	
	private static final float RATIO = 4f / 3f;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
	    int desiredWidth = getTotalWidth();
	    int desiredHeight =  getTotalHeight();
	 
	    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);
	 
	    int width;
	    int height;
	 
	    //Measure Width 
	    if (widthMode == MeasureSpec.EXACTLY) {
	        //Must be this size 
	        width = widthSize;
	    } else if (widthMode == MeasureSpec.AT_MOST) {
	        //Can't be bigger than... 
	        width = Math.min(desiredWidth, widthSize);
	    } else { 
	        //Be whatever you want 
	        width = desiredWidth;
	    } 
	 
	    //Measure Height 
	    if (heightMode == MeasureSpec.EXACTLY) {
	        //Must be this size 
	        height = heightSize;
	    } else if (heightMode == MeasureSpec.AT_MOST) {
	        //Can't be bigger than... 
	        height = Math.min(desiredHeight, heightSize);
	    } else { 
	        //Be whatever you want 
	        height = desiredHeight;
	    } 
	 
	    //MUST CALL THIS 
	    setMeasuredDimension(width, height);

	}

	// the method that draws the balls
	@Override
	protected void onDraw(Canvas canvas)
	{
		if (colorballs.isEmpty())
		{
			return;
		}

		// Smallest x for left
		// Smallest y for top
		// Biggest x for right
		// Biggest y for bottom
		int halfBallWidth = colorballs.get(TOP_LEFT).getWidthOfBall() / 2;
		int halfBallHeight = colorballs.get(TOP_LEFT).getHeightOfBall() / 2;

		int left = getRectLeft() + halfBallWidth;
		int right = getRectRight() + halfBallWidth;
		int top = getRectTop() + halfBallHeight;
		int bottom = getRectBottom() + halfBallHeight;
		Rect rect = new Rect(left, top, right, bottom);

		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeWidth(5);

		// draw stroke
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.parseColor(STROKE_COLOR));
		paint.setStrokeWidth(2);
		canvas.drawRect(rect, paint);

		// fill the rectangle
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.parseColor(FILL_COLOR));
		paint.setStrokeWidth(0);
		canvas.drawRect(rect, paint);

		// draw the balls on the corners
		paint.setColor(Color.BLUE);
		paint.setTextSize(18);
		paint.setStrokeWidth(0);
		for (int i = 0; i < colorballs.size(); i++)
		{
			ColorBall ball = colorballs.get(i);
			canvas.drawBitmap(ball.getBitmap(), ball.getX(), ball.getY(), paint);
		}
		
		setBackgroundColor(Color.RED);
	}

	// events when touching the screen
	public boolean onTouchEvent(MotionEvent event)
	{
		int eventaction = event.getAction();

		int currentX = (int) event.getX();
		int currentY = (int) event.getY();

		switch (eventaction)
		{
		// touch down so check if the finger is on a ball
			case MotionEvent.ACTION_DOWN:
				startX = currentX;
				startY = currentY;
				if (canMove(currentX, currentY))
				{
					dragging = true;
				}
				else
				{
					resizing = true;
					getSelectedBall(currentX, currentY);
				}
				break;

			case MotionEvent.ACTION_MOVE: // touch drag with the ball

				int deltaX = currentX - startX;
				int deltaY = currentY - startY;

				if (dragging)
				{
						dragRect(deltaX, deltaY);
				}
				else if (resizing)
				{
					resizeRect(currentX, currentY);
					refresh();
				}

				startX = currentX;
				startY = currentY;
				break;

			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				resizing = false;
				dragging = false;
				break;
		}

		if (resizing || dragging)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public void refresh()
	{
		invalidate();
		requestLayout();
	}

	// ------------------------------->

	// Init Balls
	public void initBalls()
	{
		initBalls(0, 0);
	}

	public void initBalls(int X, int Y)
	{
		// initialize rectangle.
		int sideLength = 700;
		Point[] points = new Point[4];

		// Top Left
		points[0] = new Point();
		points[0].x = X;
		points[0].y = Y;

		// Bottom left
		points[1] = new Point();
		points[1].x = X;
		points[1].y = Y + sideLength;

		// Bottom Right
		points[2] = new Point();
		points[2].x = X + sideLength;
		points[2].y = Y + sideLength;

		// Top Right
		points[3] = new Point();
		points[3].x = X + sideLength;
		points[3].y = Y;

		selectedBalID = 2;
		selectedGroupId = 1;

		// declare each ball with the ColorBall class
		for (Point pt : points)
		{
			colorballs.add(new ColorBall(getContext(), R.drawable.ic_launcher,
					pt));
		}
	}

	// Resize
	public void resizeRect(int X, int Y)
	{
		if (colorballs.isEmpty()) return;

		if (selectedBalID > -1)
		{
			// move the balls the same as the finger
			colorballs.get(selectedBalID).setX(X);
			colorballs.get(selectedBalID).setY(Y);
			if (selectedGroupId == 1)
			{
				colorballs.get(BOTTOM_LEFT).setX(
						colorballs.get(TOP_LEFT).getX());
				colorballs.get(BOTTOM_LEFT).setY(
						colorballs.get(BOTTOM_RIGHT).getY());
				colorballs.get(TOP_RIGHT).setX(
						colorballs.get(BOTTOM_RIGHT).getX());
				colorballs.get(TOP_RIGHT).setY(colorballs.get(TOP_LEFT).getY());
			}
			else
			{
				colorballs.get(TOP_LEFT).setX(
						colorballs.get(BOTTOM_LEFT).getX());
				colorballs.get(TOP_LEFT).setY(colorballs.get(TOP_RIGHT).getY());
				colorballs.get(BOTTOM_RIGHT).setX(
						colorballs.get(TOP_RIGHT).getX());
				colorballs.get(BOTTOM_RIGHT).setY(
						colorballs.get(BOTTOM_LEFT).getY());
			}
		}
	}

	// Drag
	public void dragRect(int deltaX, int deltaY)
	{
		int offset = 5;
		if (Math.abs(deltaX) > offset || Math.abs(deltaY) > offset)
		{
			updateBalls(deltaX, deltaY);
			if (iDragger != null)
			{
				iDragger.onDrag(deltaX, deltaY);
			}
		}
	}

	// ------------------------------->
	// Helpers

	private void updateBalls(int deltaX, int deltaY)
	{
		for (int i = 0; i < colorballs.size(); i++)
		{
			colorballs.get(i).setX(colorballs.get(i).getX() + deltaX);
			colorballs.get(i).setY(colorballs.get(i).getY() + deltaY);
		}
	}

	public IDragger getDragger()
	{
		return iDragger;
	}

	public void setDragger(IDragger iDragger)
	{
		this.iDragger = iDragger;
	}

	public void getSelectedBall(int X, int Y)
	{
		if (colorballs.isEmpty()) return;

		// resize rectangle
		selectedBalID = -1;
		selectedGroupId = -1;
		for (int i = colorballs.size() - 1; i >= 0; i--)
		{
			ColorBall ball = colorballs.get(i);

			// check if inside the bounds of the ball (circle)
			// get the center for the ball
			int ballX = ball.getX();
			int ballY = ball.getY();
			int widthOfBall = ball.getWidthOfBall();
			paint.setColor(Color.CYAN);

			// Ball Rectangle
			Rect rec = new Rect(ballX, ballY, ballX + widthOfBall, ballY
					+ widthOfBall);
			if (rec.contains(X, Y))
			{
				selectedBalID = ball.getID();
				if (selectedBalID == 1 || selectedBalID == 3)
				{
					selectedGroupId = 2;
				}
				else
				{
					selectedGroupId = 1;
				}
				break;
			}
		}
	}

	public boolean canMove(int x, int y)
	{
		if (colorballs.isEmpty()) return false;

		boolean canMove = true;
		for (int i = colorballs.size() - 1; i >= 0; i--)
		{
			ColorBall ball = colorballs.get(i);

			// check if inside the bounds of the ball (circle)
			// get the center for the ball
			int ballX = ball.getX();
			int ballY = ball.getY();
			int widthOfBall = ball.getWidthOfBall();
			int heightOfBall = ball.getHeightOfBall();

			// Ball Rectangle
			Rect rec = new Rect(ballX, ballY, ballX + widthOfBall, ballY
					+ heightOfBall);

			if (rec.contains(x, y))
			{
				canMove = false;
				break;
			}
		}

		return canMove;
	}

	public int getRectBottom()
	{
		int bottom = colorballs.get(TOP_LEFT).getY() > colorballs.get(
				BOTTOM_LEFT).getY() ? colorballs.get(TOP_LEFT).getY()
				: colorballs.get(BOTTOM_LEFT).getY();
		return bottom;
	}

	public int getRectTop()
	{
		int top = colorballs.get(TOP_LEFT).getY() < colorballs.get(BOTTOM_LEFT)
				.getY() ? colorballs.get(TOP_LEFT).getY() : colorballs.get(
				BOTTOM_LEFT).getY();
		return top;
	}

	public int getRectRight()
	{
		int right = colorballs.get(TOP_LEFT).getX() > colorballs.get(TOP_RIGHT)
				.getX() ? colorballs.get(TOP_LEFT).getX() : colorballs.get(
				TOP_RIGHT).getX();
		return right;
	}

	public int getRectLeft()
	{
		int left = colorballs.get(TOP_LEFT).getX() < colorballs.get(TOP_RIGHT)
				.getX() ? colorballs.get(TOP_LEFT).getX() : colorballs.get(
				TOP_RIGHT).getX();
		return left;
	}

	public int getTotalWidth()
	{
		return getRectRight() + colorballs.get(TOP_LEFT).getWidthOfBall();
	}

	public int getTotalHeight()
	{
		return getRectBottom() + colorballs.get(TOP_LEFT).getHeightOfBall();
	}

	// ------------------------------->

	public interface IDragger
	{
		void onDrag(int deltaX, int deltaY);
	}

	public static class ColorBall
	{

		Bitmap bitmap;
		Context mContext;
		Point point;
		int id;
		static int count = 0;

		public ColorBall(Context context, int resourceId, Point point)
		{
			this.id = count++;
			bitmap = BitmapFactory.decodeResource(context.getResources(),
					resourceId);
			mContext = context;
			this.point = point;
		}

		public int getWidthOfBall()
		{
			return bitmap.getWidth();
		}

		public int getHeightOfBall()
		{
			return bitmap.getHeight();
		}

		public Bitmap getBitmap()
		{
			return bitmap;
		}

		public int getX()
		{
			return point.x;
		}

		public int getY()
		{
			return point.y;
		}

		public int getID()
		{
			return id;
		}

		public void setX(int x)
		{
			point.x = x;
		}

		public void setY(int y)
		{
			point.y = y;
		}
	}
}