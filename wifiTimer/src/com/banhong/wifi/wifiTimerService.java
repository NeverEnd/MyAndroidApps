package com.banhong.wifi;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
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

    private DB db=null;
    private Timer open_timer;
    private Timer close_timer;
    private myTimerTask openWIFI_task;
    private myTimerTask closeWIFI_task;
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        db= new DB(this);
        open_timer =new Timer();
        close_timer = new Timer();
        openWIFI_task = new myTimerTask(1);
        closeWIFI_task = new myTimerTask(2);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        if (!db.getServiceState())
            return START_STICKY;

        int starthour = db.getStartTime().hour;
        int startmin = db.getStartTime().minute;
        int endhour = db.getEndTime().hour;
        int endmin = db.getEndTime().minute;
        			    
        Date date = new Date(System.currentTimeMillis());
        date.setHours(starthour);
        date.setMinutes(startmin);
        date.setSeconds(0);
        openWIFI_task.cancel();
        closeWIFI_task.cancel();
        openWIFI_task = new myTimerTask(1);
        closeWIFI_task = new myTimerTask(2);
        open_timer.schedule(openWIFI_task, date, 24*60*60*1000);
        if((starthour+endhour)*60+startmin+endmin >= 24*60){
            date.setHours(23);
            date.setMinutes(59);
        }else{
            date.setHours(starthour+endhour);
            date.setMinutes(startmin+endmin);
        }
        close_timer.schedule(closeWIFI_task, date, 24*60*60*1000);
        
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
            Looper.prepare();
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
            
            my_handler.handleMessage(msg);
            Looper.loop();
         
        }
    };
    @Override
    public void onDestroy() {
        openWIFI_task.cancel();
        closeWIFI_task.cancel();
        open_timer.cancel();
        close_timer.cancel();

    }


}
