package com.demo.android.calibrate;

import android.app.Application;

public class GlobalVariable extends Application
{
  public int ScreenHeight = 0;
  public int ScreenWidth = 0;

  public int GetScreenHeight()
  {
    return this.ScreenHeight;
  }

  public int GetScreenWidth()
  {
    return this.ScreenWidth;
  }

  public void SetScreenHeight(int paramInt)
  {
    this.ScreenHeight = paramInt;
  }

  public void SetScreenWidth(int paramInt)
  {
    this.ScreenWidth = paramInt;
  }
}