package com.fmsz.sleeplogger.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // tell the AlarmManager to cancel the alarm
        AlarmManager.cancelAlarm(context)

        // just show a toast for now
        Toast.makeText(context, "Alarm went off!", Toast.LENGTH_LONG).show();

    }
}
