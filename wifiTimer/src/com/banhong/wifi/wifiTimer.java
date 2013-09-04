package com.banhong.wifi;

import android.annotation.TargetApi;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.widget.TimePicker;
import com.banhong.wifi.wifiTimerService;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class wifiTimer extends PreferenceActivity implements OnSharedPreferenceChangeListener, OnPreferenceClickListener {
    public static final String WIFI_TIME_PREFERENCE = "WIFI_TIME";
    private static CheckBoxPreference OpenCheckbox = null;
    private static Preference StartTimeSet = null;
    private static Preference EndTimeSet = null;
    private TimePickerDialog time_dialog = null;
    
    private DB db = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName(WIFI_TIME_PREFERENCE);
        addPreferencesFromResource(R.xml.preference);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        getPreferenceScreen().setPersistent(true);
        OpenCheckbox = (CheckBoxPreference) getPreferenceScreen().getPreference(0);
        StartTimeSet = (Preference) getPreferenceScreen().getPreference(1);
        EndTimeSet = (Preference) getPreferenceScreen().getPreference(2);
        OpenCheckbox.setOnPreferenceClickListener(this);
        StartTimeSet.setOnPreferenceClickListener(this);
        EndTimeSet.setOnPreferenceClickListener(this);
        
        db = new DB(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
        // TODO Auto-generated method stub
        System.out.append("111111111111");
    }

    public void setsummary(Preference pre, Preference pre2, DB db) {
        int starthour = db.getStartTime().hour;
        int startmin = db.getStartTime().minute;
        int endhour = db.getEndTime().hour;
        int endmin = db.getEndTime().minute;

        pre.setSummary(starthour + ":" + startmin);
        pre2.setSummary(endhour + " H " + endmin + " M");
    }

    public void onStart() {
        super.onStart();
        boolean service_state = db.getServiceState();

        OpenCheckbox.setChecked(service_state);

        if (service_state) {
            setsummary(StartTimeSet, EndTimeSet, db);
            StartTimeSet.setEnabled(true);
            EndTimeSet.setEnabled(true);

        }
        else {
            wifiTimerClose();
        }

        // timersetlist.setEntries(R.array.settimers);
        // timersetlist.setEntryValues(R.array.settimers);

    }

    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("setOpen")) {
            CheckBoxPreference checkbox = (CheckBoxPreference) preference;
            boolean isChecked = checkbox.isChecked();

            if (isChecked) {
                StartTimeSet.setEnabled(true);
                EndTimeSet.setEnabled(true);
                StartTimeSet.setSelectable(true);
                EndTimeSet.setSelectable(true);
                setStartTime(true);
                
            }
            else {
                db.setSwitch(isChecked);
                wifiTimerClose();
            }
            
        }
        else if (preference.getKey().equals("setStarttimer")) {
            setStartTime(false);
        }
        else if (preference.getKey().equals("setEndtimer")) {
            setTimerLength(false);
        }
        return true;
    }

    public void setStartTime(final boolean isFirst) {
        int startHour = db.getStartTime().hour;
        int startMin = db.getStartTime().minute;

        time_dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // TODO Auto-generated method stub
                db.setTime(hourOfDay, minute, true);
                setsummary(StartTimeSet, EndTimeSet, db);
                if(!isFirst)
                    UpdateTimerService();
            }

        }, startHour, startMin, true);

        if (isFirst) {
            time_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    setTimerLength(isFirst);
                }
            });

            time_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    db.setSwitch(false);
                    wifiTimerClose();
                }
            });
        }
        time_dialog.show();
    }

    public void setTimerLength(boolean isFirst) {
        int endHour = db.getEndTime().hour;
        int endMin = db.getEndTime().minute;

        time_dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                db.setSwitch(true);
                db.setTime(hourOfDay, minute, false);
                setsummary(StartTimeSet, EndTimeSet, db);
                UpdateTimerService();
            }
        }, endHour, endMin, true);

        if (isFirst) {
            time_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    // TODO Auto-generated method stub

                }
            });

            time_dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    db.setSwitch(false);
                    wifiTimerClose();
                }
            });

        }
        time_dialog.show();

    }

    public void wifiTimerClose() {
        
        StartTimeSet.setEnabled(false);
        EndTimeSet.setEnabled(false);
        stopService(new Intent(this, wifiTimerService.class));
    }
    
    private void UpdateTimerService()
    {        
        Intent service = new Intent(this, wifiTimerService.class);
        startService(service);        
    }
}