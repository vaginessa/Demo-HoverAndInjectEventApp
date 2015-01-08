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
import android.widget.FrameLayout;
import com.example.hoversample.R;

public class ResizableRect extends View
{
	private static final String STROKE_COLOR = "#AADB1255";
	private static final String FILL_COLOR = "#55DB1255";
	private static final int MAX_CLICK_DISTANCE = 15;
	private static final int MAX_CLICK_DURATION = 200;
	private static final int MOVE_THERSHOLD = 5;

	private static final int GROUP_0_AND_2 = 1;
	private static final int POINT0 = 0;
	private static final int POINT2 = 2;
	private static final int GROUP_1_AND_3 = 2;
	private static final int POINT1 = 1;
	private static final int POINT3 = 3;

	/**
	 * point0 and point 2 are of group1 and point 1 and point3 of group2
	 */
	// variable to know what ball is being dragged
	private int selectedGroupId = -1;
	private int selectedBalID = 0;
	// array that holds the balls
	private ArrayList<ColorBall> colorballs = new ArrayList<ColorBall>();
	// ----->
	private long startClickTime;
	private int startX, startY;
	private Paint paint;
	private boolean resizing, dragging;
	private IDragger iDragger;
	private Rect rect;

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

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int desiredWidth = getTotalWidth();
		int desiredHeight = getTotalHeight();

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;

		// Measure Width
		if (widthMode == MeasureSpec.EXACTLY)
		{
			// Must be this size
			width = widthSize;
		}
		else if (widthMode == MeasureSpec.AT_MOST)
		{
			// Can't be bigger than...
			width = Math.min(desiredWidth, widthSize);
		}
		else
		{
			// Be whatever you want
			width = desiredWidth;
		}

		// Measure Height
		if (heightMode == MeasureSpec.EXACTLY)
		{
			// Must be this size
			height = heightSize;
		}
		else if (heightMode == MeasureSpec.AT_MOST)
		{
			// Can't be bigger than...
			height = Math.min(desiredHeight, heightSize);
		}
		else
		{
			// Be whatever you want
			height = desiredHeight;
		}

		// MUST CALL THIS
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
		int halfBallWidth = colorballs.get(POINT0).getWidthOfBall() / 2;
		int halfBallHeight = colorballs.get(POINT0).getHeightOfBall() / 2;

		int left = getRectLeft() + halfBallWidth;
		int right = getRectRight() + halfBallWidth;
		int top = getRectTop() + halfBallHeight;
		int bottom = getRectBottom() + halfBallHeight;
		rect.left = left;
		rect.right = right;
		rect.top = top;
		rect.bottom = bottom;

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

		setBackgroundColor(Color.BLUE);
	}

	// events when touching the screen
	public boolean onTouchEvent(MotionEvent event)
	{
		int action = event.getAction();
		int currentX = (int) event.getX();
		int currentY = (int) event.getY();
		switch (action)
		{
			case MotionEvent.ACTION_DOWN:
			{
				// touch down so check if the finger is on a ball
				startClickTime = System.currentTimeMillis();
				// Start X and Y
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
			}
			case MotionEvent.ACTION_MOVE: // touch drag with the ball
			{
				// Delta X and Y
				if (dragging)
				{
					dragRect(currentX, currentY);
				}
				else if (resizing)
				{
					resizeRect(currentX, currentY);
				}
				// Start X and Y
				startX = currentX;
				startY = currentY;
				break;
			}
			case MotionEvent.ACTION_UP:
				// End X and Y
				if (isAClick(startX, startY, currentX, currentY))
				{
					// performClick();
					return true;
				}
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

	private boolean isAClick(float x1, float y1, float x2, float y2)
	{
		long pressDuration = System.currentTimeMillis() - startClickTime;
		float dx = x1 - x2;
		float dy = y1 - y2;
		float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);

		if (pressDuration < MAX_CLICK_DURATION
				&& pxToDp(distanceInPx) < MAX_CLICK_DISTANCE)
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
		rect = new Rect();
		initBalls(0, 0);
	}

	public void initBalls(int X, int Y)
	{
		// initialize rectangle.
		int sideLength = 400;
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
		int icon = R.drawable.ic_launcher;

		colorballs.add(new ColorBall(getContext(), icon, points[0]));

		colorballs.add(new ColorBall(getContext(), icon, points[1]));

		colorballs.add(new ColorBall(getContext(), icon, points[2]));

		colorballs.add(new ColorBall(getContext(), icon, points[3]));
	}

	// Resize
	public void resizeRect(int currentX, int currentY)
	{

		if (colorballs.isEmpty() || !(getParent() instanceof FrameLayout)) return;
		if (selectedBalID > -1 && selectedBalID < colorballs.size())
		{
			FrameLayout parentFrameLayout = (FrameLayout) getParent();
			FrameLayout.LayoutParams layoutParam = (android.widget.FrameLayout.LayoutParams) getLayoutParams();
			ColorBall selectedColorBall = colorballs.get(selectedBalID);
			
			// All Balls
			ColorBall bottomRightBall = getBottomRightBall();
			ColorBall topRightBall = getTopRightBall();
			ColorBall bottomLeftBall = getBottomLeftBall();
			
			int deltaX = currentX - startX;
			int deltaY = currentY - startY;

			// move the balls the same as the finger
			if (selectedColorBall.equals(getTopLeftBall()))
			{
				// Change X 
				// Bottom right
				if(bottomRightBall!=null)
				bottomRightBall.setX(bottomRightBall.getX()-deltaX);	
				// Top right
				if(topRightBall!=null)
				topRightBall.setX(topRightBall.getX()-deltaX);
				
				// change Y	
				// Bottom right
				if(bottomRightBall!=null)
				bottomRightBall.setY(bottomRightBall.getY()-deltaY);	
				updateRelativeBalls(selectedGroupId);
				// Bottom left
				if(bottomLeftBall!=null)
					bottomLeftBall.setY(bottomLeftBall.getY()-deltaY);
					
				// Move
				layoutParam.leftMargin= layoutParam.leftMargin+deltaX;
				layoutParam.topMargin= layoutParam.topMargin+deltaY;
				parentFrameLayout.updateViewLayout(this,layoutParam);
			}
			else if (selectedColorBall.equals(getBottomLeftBall()))
			{
				// Change X 
				// Bottom right
				if(bottomRightBall!=null)
				bottomRightBall.setX(bottomRightBall.getX()-deltaX);	
				// Top right
				if(topRightBall!=null)
				topRightBall.setX(topRightBall.getX()-deltaX);
				
				// change Y	
				selectedColorBall.setY(currentY);
				updateRelativeBalls(selectedGroupId);
					
				// Move
				layoutParam.leftMargin= layoutParam.leftMargin+deltaX;
				parentFrameLayout.updateViewLayout(this,layoutParam);
			}
			else if (selectedColorBall.equals(getTopRightBall()))
			{
				// X
				selectedColorBall.setX(currentX);
				updateRelativeBalls(selectedGroupId);
				
				// change Y	
				// Bottom right
				if(bottomRightBall!=null)
				bottomRightBall.setY(bottomRightBall.getY()-deltaY);	
				
				// Bottom left
				if(bottomLeftBall!=null)
					bottomLeftBall.setY(bottomLeftBall.getY()-deltaY);
					
				// Move
				layoutParam.topMargin= layoutParam.topMargin+deltaY;
				parentFrameLayout.updateViewLayout(this,layoutParam);
						
			}
			else
			{
				selectedColorBall.setX(currentX);
				selectedColorBall.setY(currentY);
				updateRelativeBalls(selectedGroupId);
				refresh();
			}
		}
	}

	// Drag
	public void dragRect(int currentX, int currentY)
	{

		int deltaX = currentX - startX;
		int deltaY = currentY - startY;

		if (Math.abs(deltaX) > MOVE_THERSHOLD
				|| Math.abs(deltaY) > MOVE_THERSHOLD)
		{
			if (iDragger != null)
			{
				iDragger.onDrag(deltaX, deltaY);
			}
		}
	}

	// ------------------------------->
	// Helpers

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
				if (selectedBalID == POINT1 || selectedBalID == POINT3)
				{
					selectedGroupId = GROUP_1_AND_3;
				}
				else
				{
					selectedGroupId = GROUP_0_AND_2;
				}
				break;
			}
		}
	}

	public void updateRelativeBalls(int selectedGroupId)
	{
		if (selectedGroupId == GROUP_0_AND_2)
		{
			colorballs.get(POINT1).setX(colorballs.get(POINT0).getX());
			colorballs.get(POINT1).setY(colorballs.get(POINT2).getY());

			colorballs.get(POINT3).setX(colorballs.get(POINT2).getX());
			colorballs.get(POINT3).setY(colorballs.get(POINT0).getY());
		}
		else if (selectedGroupId == GROUP_1_AND_3)
		{
			colorballs.get(POINT0).setX(colorballs.get(POINT1).getX());
			colorballs.get(POINT0).setY(colorballs.get(POINT3).getY());
			colorballs.get(POINT2).setX(colorballs.get(POINT3).getX());
			colorballs.get(POINT2).setY(colorballs.get(POINT1).getY());
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

	// --------------------------------->

	public int getRectBottom()
	{
		int bottom = colorballs.get(0).getY();
		for (int i = 1; i < colorballs.size(); i++)
		{
			bottom = bottom < colorballs.get(i).getY() ? colorballs.get(i)
					.getY() : bottom;
		}
		return bottom;
	}

	public int getRectTop()
	{
		int top = colorballs.get(0).getY();
		for (int i = 1; i < colorballs.size(); i++)
		{
			top = top > colorballs.get(i).getY() ? colorballs.get(i).getY()
					: top;
		}
		return top;
	}

	public int getRectRight()
	{
		int right = colorballs.get(0).getX();
		for (int i = 1; i < colorballs.size(); i++)
		{
			right = right < colorballs.get(i).getX() ? colorballs.get(i).getX()
					: right;
		}
		return right;
	}

	public int getRectLeft()
	{
		int left = colorballs.get(0).getX();
		for (int i = 1; i < colorballs.size(); i++)
		{
			left = left > colorballs.get(i).getX() ? colorballs.get(i).getX()
					: left;
		}
		return left;
	}

	public ColorBall getTopLeftBall()
	{
		ColorBall selectedColorBall = null;
		Point point = new Point(getRectLeft(), getRectTop());
		for (ColorBall ball : colorballs)
		{
			if (ball.getPoint().equals(point))
			{
				selectedColorBall = ball;
				break;
			}
		}
		return selectedColorBall;
	}

	public ColorBall getBottomLeftBall()
	{
		ColorBall selectedColorBall = null;
		Point point = new Point(getRectLeft(), getRectBottom());
		for (ColorBall ball : colorballs)
		{
			if (ball.getPoint().equals(point))
			{
				selectedColorBall = ball;
				break;
			}
		}
		return selectedColorBall;
	}

	public ColorBall getBottomRightBall()
	{
		ColorBall selectedColorBall = null;
		Point point = new Point(getRectRight(), getRectBottom());
		for (ColorBall ball : colorballs)
		{
			if (ball.getPoint().equals(point))
			{
				selectedColorBall = ball;
				break;
			}
		}
		return selectedColorBall;
	}

	public ColorBall getTopRightBall()
	{
		ColorBall selectedColorBall = null;
		Point point = new Point(getRectRight(), getRectTop());
		for (ColorBall ball : colorballs)
		{
			if (ball.getPoint().equals(point))
			{
				selectedColorBall = ball;
				break;
			}
		}
		return selectedColorBall;
	}

	public int getTotalWidth()
	{
		return getRectRight() + colorballs.get(POINT0).getWidthOfBall();
	}

	public int getTotalHeight()
	{
		return getRectBottom() + colorballs.get(POINT0).getHeightOfBall();
	}

	// ------------------------------->

	public float pxToDp(float px)
	{
		return px / getResources().getDisplayMetrics().density;
	}

	public interface IDragger
	{
		void onDrag(int deltaX, int deltaY);
	}

	public static class ColorBall
	{
		private int id;
		private Bitmap bitmap;
		private Point point;

		private static int count = 0;

		public ColorBall(Context context, int resourceId, Point point)
		{
			this.id = count++;
			if (resourceId != -1)
			{
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						resourceId);
			}
			this.point = point;
		}

		public int getWidthOfBall()
		{
			if (bitmap != null)
			{
				return bitmap.getWidth();
			}
			else
			{
				return 0;
			}
		}

		public int getHeightOfBall()
		{
			if (bitmap != null)
			{
				return bitmap.getHeight();
			}
			else
			{
				return 0;
			}

		}

		public Bitmap getBitmap()
		{
			return bitmap;
		}

		public Point getPoint()
		{
			return point;
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

		@Override
		public boolean equals(Object o)
		{
			boolean isEqual = false ;
			if(o != null && o instanceof ColorBall)
			{
				isEqual = point.equals((((ColorBall) o).point));
			}
			return isEqual;
		}
	}

}