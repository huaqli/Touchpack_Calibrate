package com.demo.android.calibrate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.View;

public class DrawView extends View
{
  private int[][] Default9Pnts = { { 102, 1945 }, { 1024, 1945 }, { 1945, 1945 }, { 1945, 1024 }, { 1024, 1024 }, { 102, 1024 }, { 102, 102 }, { 1024, 102 }, { 1945, 102 } };
  public int count;
  public Paint pntCircle = new Paint();
  private Paint pntText = new Paint();
  public double screenHeight;
  public double screenWidth;

  static
  {
    System.loadLibrary("calib-second");
  }

  public DrawView(Context paramContext)
  {
    super(paramContext);
    setFocusable(true);
  }

  private void drawCalPoint(int paramInt, Canvas paramCanvas, Paint paramPaint)
  {
    int i = this.Default9Pnts[paramInt][0];
    int j = this.Default9Pnts[paramInt][1];
    paramPaint.setStrokeWidth(3.0F);
    Point localPoint = new Point(i * (int)this.screenWidth / 2048, j * (int)this.screenHeight / 2048);
    paramPaint.setStyle(Paint.Style.STROKE);
    paramCanvas.drawLine(-20 + localPoint.x, localPoint.y, 20 + localPoint.x, localPoint.y, paramPaint);
    paramCanvas.drawLine(localPoint.x, -20 + localPoint.y, localPoint.x, 20 + localPoint.y, paramPaint);
  }

  public native int CaliFunc();

  public native int CloseTouch();

  public native int GetCalPoint();

  public native int InitTouch();

  public native int ReadDataFunc(int paramInt);

  public native int ResetController();

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    this.screenHeight = paramCanvas.getHeight();
    this.screenWidth = paramCanvas.getWidth();
    if (this.count >= 9)
      return;
    drawCalPoint(this.count, paramCanvas, this.pntCircle);
  }
}