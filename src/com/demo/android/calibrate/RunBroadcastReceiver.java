package com.demo.android.calibrate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RunBroadcastReceiver extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    if (PreferenceManager.getDefaultSharedPreferences(paramContext).getInt("CalibFlag", 0) == 1)
      return;
    try
    {
      Thread.sleep(2000L);
      Intent localIntent = new Intent(paramContext, Calibrate.class);
      localIntent.addFlags(268435456);
      paramContext.startActivity(localIntent);
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
      localInterruptedException.printStackTrace();
    }
  }
}