package com.fmsz.sleeplogger.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmBootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the Android OS has finished booting
        if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            // let the alarm manager reset the alarm if there was any
            AlarmManager.onSystemReboot(context)
        }

        // TODO: Maybe listen for TIME_SET and TIMEZONE_CHANGED too
    }
}
