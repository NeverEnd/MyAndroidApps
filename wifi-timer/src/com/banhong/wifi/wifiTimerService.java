package com.banhong.wifi;

import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;

public class wifiTimerService extends Service {
    private AlarmManager open_wifi = null;
    private AlarmManager stop_wifi = null;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        open_wifi = (AlarmManager) getSystemService(ALARM_SERVICE);
        stop_wifi = (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int starthour = intent.getIntExtra("startHour", 0);
        int startmin = intent.getIntExtra("startMin", 0);
        int endhour = intent.getIntExtra("EndHour", 0);
        int endmin = intent.getIntExtra("EndMin", 0);
        long tempmillin = 0;
        Calendar systemtime = Calendar.getInstance();
        Calendar optiontime = Calendar.getInstance();

        // Date systemtime = new Date(System.currentTimeMillis());
        // Date option_time = new Date(systemtime.getDate());
        optiontime.set(systemtime.get(Calendar.YEAR), systemtime.get(Calendar.MONTH), systemtime.get(Calendar.DAY_OF_MONTH), starthour, startmin, systemtime.get(Calendar.SECOND));
        long millinCut = optiontime.getTimeInMillis() - systemtime.getTimeInMillis();
        if (millinCut <= 0) {
            tempmillin = 0;
        }
        else {
            tempmillin = millinCut;
        }

        Intent Openreciver = new Intent(this, WifiOpenTimeOut.class);
        PendingIntent Opensender = PendingIntent.getBroadcast(this, 0, Openreciver, 0);
        open_wifi.setRepeating(AlarmManager.RTC, tempmillin, 24 * 60 * 60 * 1000, Opensender);

        // optiontime.set(systemtime.get(Calendar.YEAR),
        // systemtime.get(Calendar.MONTH),
        // systemtime.get(Calendar.DAY_OF_MONTH),endhour, endmin,
        // systemtime.get(Calendar.SECOND));

        if (endhour == 0 && endmin == 0) {
            tempmillin = 1;
        }
        else {
            tempmillin = systemtime.getTimeInMillis() + endhour * 60 * 60 * 1000 + endmin * 60 * 1000;
        }

        Intent Stopreciver = new Intent(this, WifiStopTimeOut.class);
        PendingIntent Stopsender = PendingIntent.getBroadcast(this, 0, Stopreciver, 0);
        stop_wifi.setRepeating(AlarmManager.RTC, tempmillin, 24 * 60 * 60 * 1000, Stopsender);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Intent Openreciver = new Intent(this, WifiOpenTimeOut.class);
        PendingIntent Opensender = PendingIntent.getBroadcast(this, 0, Openreciver, 0);
        open_wifi.cancel(Opensender);

        Intent Stopreciver = new Intent(this, WifiStopTimeOut.class);
        PendingIntent Stopsender = PendingIntent.getBroadcast(this, 0, Stopreciver, 0);

        stop_wifi.cancel(Stopsender);
    }

    public class WifiOpenTimeOut extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            WifiManager wifiM = (WifiManager) getSystemService(WIFI_SERVICE);
            wifiM.setWifiEnabled(true);
            // SearchManager wifiM =
            // (SearchManager)getSystemService(SEARCH_SERVICE);
            // wifiM.startSearch("11111", true, intent.getComponent(), null,
            // false);

        }
    }

    public class WifiStopTimeOut extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // SearchManager wifiM =
            // (SearchManager)getSystemService(SEARCH_SERVICE);
            // wifiM.stopSearch();
            WifiManager wifiM = (WifiManager) getSystemService(WIFI_SERVICE);
            wifiM.setWifiEnabled(false);
        }

    }
}
