package com.banhong.wifi;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;

import com.banhong.wifi.DB;

public class wifiTimerService extends Service {
    private AlarmManager open_wifi = null;
    private AlarmManager stop_wifi = null;
    private DB db=null;
    private Timer open_timer;
    private Timer close_timer;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        open_wifi = (AlarmManager) getSystemService(ALARM_SERVICE);
        stop_wifi = (AlarmManager) getSystemService(ALARM_SERVICE);
        db= new DB(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        if (!db.getServiceState())
            return START_STICKY;

        int starthour = db.getStartTime().hour;
        int startmin = db.getStartTime().minute;
        int endhour = db.getEndTime().hour;
        int endmin = db.getEndTime().minute;
        			 
        Calendar systemtime = Calendar.getInstance();
        Calendar optiontime = Calendar.getInstance();
        optiontime.set(systemtime.get(Calendar.YEAR), systemtime.get(Calendar.MONTH), systemtime.get(Calendar.DAY_OF_MONTH), starthour, startmin, systemtime.get(Calendar.SECOND));
        
        open_timer =new Timer();
        close_timer = new Timer();
        myTimerTask openWIFI_task = new myTimerTask(1);
        myTimerTask closeWIFI_task = new myTimerTask(2);
        
        open_timer.schedule(openWIFI_task, optiontime.getTime());
        optiontime.add(Calendar.MINUTE, endmin);
        optiontime.add(Calendar.HOUR_OF_DAY, endhour);
        close_timer.schedule(closeWIFI_task, optiontime.getTime(), 24*60*60*1000);
        
        return START_STICKY;
    }
      
    private class myTimerTask extends TimerTask{
        private Handler my_handler;
        private Message msg;
        
        public myTimerTask(int timerType){
            msg = new Message();
            msg.arg1=timerType;
        }
        
        @Override
        public void run() {
            // TODO Auto-generated method stub
            my_handler = new Handler(){
                
                @Override
                public void handleMessage(Message msg){
                    super.handleMessage(msg);
                    if(msg.arg1==1){
                        WifiManager wifiM = (WifiManager) getSystemService(WIFI_SERVICE);
                        wifiM.setWifiEnabled(true);
                    }
                    else if(msg.arg1==2)
                    {
                        WifiManager wifiM = (WifiManager) getSystemService(WIFI_SERVICE);
                        wifiM.setWifiEnabled(false);
                    }
                }
            };
            my_handler.sendMessage(msg);
        }
    };
    @Override
    public void onDestroy() {
        open_timer.cancel();
        close_timer.cancel();
    }


}
