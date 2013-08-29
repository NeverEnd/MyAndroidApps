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
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.widget.TimePicker;
import com.banhong.wifi.wifiTimerService;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class wifiTimer extends PreferenceActivity implements OnSharedPreferenceChangeListener, OnPreferenceClickListener {
    private static CheckBoxPreference OpenCheckbox = null;
    private static Preference StartTimeSet = null;
    private static Preference EndTimeSet = null;
    private TimePickerDialog time_dialog = null;
    private DB db = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        OpenCheckbox = (CheckBoxPreference) getPreferenceScreen().getPreference(0);
        StartTimeSet = (Preference) getPreferenceScreen().getPreference(1);
        EndTimeSet = (Preference) getPreferenceScreen().getPreference(2);
        OpenCheckbox.setOnPreferenceClickListener(this);
        StartTimeSet.setOnPreferenceClickListener(this);
        EndTimeSet.setOnPreferenceClickListener(this);

        db = new DB(this);
        // setTitle("WIFI ¶¨Ê±Æ÷");

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
        // TODO Auto-generated method stub

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
            Intent service = new Intent(this, wifiTimerService.class);
            stopService(service);

            startService(service);

        }
        else {
            //dataclean();
            stopService(new Intent(this, wifiTimerService.class));
        }

        // timersetlist.setEntries(R.array.settimers);
        // timersetlist.setEntryValues(R.array.settimers);

    }

    public void onDestroy() {
        super.onDestroy();
        // db.close();
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        if (preference.getKey().equals("setOpen")) {
            CheckBoxPreference checkbox = (CheckBoxPreference) preference;
            boolean isChecked = checkbox.isChecked();

            if (isChecked) {
                setStartTime(true);

                Intent service = new Intent(this, wifiTimerService.class);

                stopService(service);
                startService(service);
            }
            else {
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
                // TODO Auto-generated method stub
                db.setTime(hourOfDay, minute, false);
                setsummary(StartTimeSet, EndTimeSet, db);
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
                    // TODO Auto-generated method stub
                    wifiTimerClose();
                }
            });

        }
        time_dialog.show();

    }

    public void wifiTimerClose() {
//        dataclean();
        StartTimeSet.setEnabled(false);
        EndTimeSet.setEnabled(false);
        stopService(new Intent(this, wifiTimerService.class));
    }

    public void firstSetTimeDone() {
        StartTimeSet.setEnabled(false);
        EndTimeSet.setEnabled(false);
        setsummary(StartTimeSet, EndTimeSet, db);

    }
}