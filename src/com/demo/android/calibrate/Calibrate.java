
package com.demo.android.calibrate;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Calibrate extends Activity
{
    private final static String TAG = "Calibrate";
    private Handler DrawPointHandler = new Handler();
    private Runnable DrawPointTimer = new Runnable()
    {
        public void run()
        {
            Calibrate.this.SetDrawPoint();
            if (Calibrate.this.running != 1)
                return;
            Calibrate.this.mDrawView.invalidate();
            Calibrate.this.DrawPointHandler.postDelayed(this, 100L);
        }
    };
    private Handler ReadDataHandler = new Handler();
    private Runnable ReadDataTimer = new Runnable()
    {
        public void run()
        {
            Calibrate.this.mDrawView.ReadDataFunc(Calibrate.this.activeSampling);
            if (Calibrate.this.running != 1)
                return;
            Calibrate.this.ReadDataHandler.postDelayed(this, 20L);
        }
    };
    private int active = 11;
    private int activeSampling = 1;
    private DrawView mDrawView;
    private int running = 0;

    private void DisableSystemUI()
    {
        try
        {
            Process su;
            String cmd = "su -c service call activity 42 s16 com.android.systemui";
            su = Runtime.getRuntime().exec(cmd);
            su.waitFor();
            return;
        } catch (Exception localException)
        {
            localException.printStackTrace();
        }
    }

    private void EnableSystemUI()
    {
        try
        {
            Process su;
            String cmd;

            if (Build.VERSION.SDK_INT > 16)
            {
                cmd = "am startservice --user 0 -n com.android.systemui/.SystemUIService";
            } else {
                cmd = "am startservice -n com.android.systemui/.SystemUIService";
            }
            su = Runtime.getRuntime().exec(cmd);
            su.waitFor();
            return;
        } catch (Exception localException)
        {
            localException.printStackTrace();
        }
    }

    private void SetDrawPoint()
    {
        this.mDrawView.count = this.mDrawView.GetCalPoint();
        this.active = this.mDrawView.CaliFunc();
        
        if (this.active == 11) {
            this.mDrawView.pntCircle.setColor(-256);
            return;
        }
        
        if (this.active == 22) {
            this.mDrawView.pntCircle.setColor(-16776961);
            return;
        }
        
        if (this.active != 55)
            return;
        /*
        do {
           
            if (this.active == 88) {
                this.activeSampling = 0;
                this.mDrawView.pntCircle.setColor(-65536);
                return;
            }
            
            if (this.active != 33)
                continue;
            
            this.activeSampling = 1;
            this.mDrawView.pntCircle.setColor(-65536);
            return;
        } while (this.active != 55);
        */
        this.mDrawView.pntCircle.setColor(-65536);
        this.running = 0;
        debug("calibration done");
        this.ReadDataHandler.removeCallbacks(this.ReadDataTimer);
        this.DrawPointHandler.removeCallbacks(this.DrawPointTimer);
        SharedPreferences.Editor localEditor = PreferenceManager.getDefaultSharedPreferences(this)
                .edit();
        localEditor.putInt("CalibFlag", 1);
        localEditor.commit();
        MessageBox("Calibration is done");
        this.finish();
        //System.exit(0);
    }

    private void debug(String paramString)
    {
        try
        {
            if (Environment.getExternalStorageState().equals("mounted"))
            {
                File localFile = new File(Environment.getExternalStorageDirectory() + "/test.txt");
                if (!(localFile.exists()))
                    localFile.createNewFile();
                FileWriter localFileWriter = new FileWriter(localFile, true);
                localFileWriter.write(paramString + "\n");
                localFileWriter.close();
            }
            return;
        } catch (IOException localIOException)
        {
            localIOException.printStackTrace();
        }
    }

    public void Init()
    {
        int i = this.mDrawView.InitTouch();
        String str = String.valueOf(i);
        //Log.e(TAG, "result of InitTouch is: " + str);
        debug(str);
        if (i != 1)
        {
            if (i == 100)
            {
                MessageBox("Unable to open device: " + str);
                finish();
                return;
            }
            if (i == 99)
            {
                MessageBox("device not found: " + str);
                finish();
                return;
            }
            if (i == 13)
            {
                MessageBox("No permisson to open device: " + str);
                finish();
                return;
            }
            if (i == 101)
            {
                MessageBox("Unable to set digitizer mode: " + str);
                finish();
                return;
            }
            if (i == 102)
            {
                MessageBox("Unable to set calibration mode: " + str);
                finish();
                return;
            }
            if (i == 103)
            {
                MessageBox("Unable to set Parameter: " + str);
                finish();
                return;
            }
            if (i == 104)
            {
                MessageBox("Unable to set raw mode: " + str);
                finish();
                return;
            }
            if (i == 105)
            {
                MessageBox("Unable to Auto Detector: " + str);
                finish();
                return;
            }
            MessageBox("error code: " + str);
            finish();
            return;
        }

        this.DrawPointHandler.removeCallbacks(this.DrawPointTimer);
        this.DrawPointHandler.postDelayed(this.DrawPointTimer, 100L);
        this.ReadDataHandler.removeCallbacks(this.ReadDataTimer);
        this.DrawPointHandler.postDelayed(this.ReadDataTimer, 20L);
        this.running = 1;
    }

    public void MessageBox(String paramString)
    {
        Toast.makeText(this, paramString, 0).show();
    }

    public void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        requestWindowFeature(1);

        DisableSystemUI();
        this.mDrawView = new DrawView(this);
        setContentView(this.mDrawView);
        Init();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        EnableSystemUI();
        this.mDrawView.ResetController();
        this.mDrawView.CloseTouch();
    }
}
