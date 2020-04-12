package com.fmsz.sleeplogger.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.lifecycle.LiveData
import androidx.navigation.NavDeepLinkBuilder
import com.fmsz.sleeplogger.R
import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

class AlarmManager {

    companion object {
        // constants for storing state in SharedPreferences
        private const val PREFERENCES_NAME = "alarm_storage"
        private const val KEY_ALARM_TIME = "alarm_time"
        private const val KEY_ALARM_START = "alarm_start"

        private fun getPreferences(context: Context) =
            context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

        fun getAlarmTime(c: Context): LiveData<Long> = SharedPreferencesLiveDataLong(
            getPreferences(c), KEY_ALARM_TIME, System.currentTimeMillis()
        )

        fun getAlarmStartTime(c: Context): LiveData<Long> = SharedPreferencesLiveDataLong(
            getPreferences(c), KEY_ALARM_START, 0
        )

        fun setAlarm(c: Context, alarmTime: AlarmTime) =
            setAlarm(c, alarmTime.getCorrectedTimeMillis())

        private fun setAlarm(c: Context, alarmTime: Long) {

            // use AlarmManager to enable the alarm if its available
            getAlarmManager(c)?.let { manager ->
                // set the system alarm clock
                manager.setAlarmClock(
                    AlarmManager.AlarmClockInfo(alarmTime, getActivityIntent(c)),
                    getAlarmReceiverIntent(c)
                )

                // update alarm time and start time in persistent storage
                getPreferences(c).edit()
                    .putLong(KEY_ALARM_TIME, alarmTime)
                    .putLong(KEY_ALARM_START, System.currentTimeMillis())
                    .apply()

                // alert the user of the new alarm
                advertiseAlarm(c, alarmTime)
            }
        }

        fun cancelAlarm(c: Context) {
            // cancel the system alarm
            getAlarmManager(c)?.cancel(getAlarmReceiverIntent(c))

            // set the alarm start time to 0 to disable the alarm
            getPreferences(c).edit()
                .putLong(KEY_ALARM_START, 0)
                .apply()
        }

        private fun getAlarmManager(context: Context): AlarmManager? {
            return context.getSystemService()
        }

        // returns the PendingIntent to be called when the alarm goes off
        private fun getAlarmReceiverIntent(context: Context): PendingIntent {
            return PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, AlarmReceiver::class.java),
                0
            )
        }

        // returns the PendingIntent to be called when the user clicks the alarm icon in the status bar
        private fun getActivityIntent(context: Context): PendingIntent {
            return NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.alarmFragment)
                .createPendingIntent()
        }

        fun onSystemReboot(c: Context) {
            // check if there is any pending alarm by reading from the preferences
            val alarmStartTime = getPreferences(c).getLong(KEY_ALARM_START, -1)
            if (alarmStartTime > 0) {

                // there is an active alarm, check whether it has already passed or not and set the alarm accordingly
                val alarmTime = getPreferences(c).getLong(KEY_ALARM_TIME, -1)
                when (alarmTime > System.currentTimeMillis()) {
                    true -> setAlarm(c, alarmTime)
                    false -> cancelAlarm(c)
                }
            }
        }

        // shows an informative Toast to the user about when the alarm will go off
        private fun advertiseAlarm(c: Context, alarmTime: Long) {
            // calculate difference from now
            val diffMinutes = (alarmTime - System.currentTimeMillis()) / (1000.0 * 60.0)
            val minutes = (ceil(diffMinutes) % 60).toInt()
            val hours = (floor(diffMinutes) / 60).toInt()

            // build text string
            val message =
                "Alarm goes off in ${c.resources.getQuantityString(
                    R.plurals.hour, hours, hours
                )} and ${c.resources.getQuantityString(R.plurals.minute, minutes, minutes)}"

            // and show toast
            Toast.makeText(c, message, Toast.LENGTH_LONG).show()
        }
    }
}

data class AlarmTime(val hour: Int, val minute: Int) {
    companion object {
        fun fromMillis(millis: Long): AlarmTime {
            val cal = Calendar.getInstance()
            cal.timeInMillis = millis
            return AlarmTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
        }
    }

    fun getCorrectedTimeMillis(): Long {

        // start with now
        val c = Calendar.getInstance()
        c.timeInMillis = System.currentTimeMillis()
        val nowHour = c[Calendar.HOUR_OF_DAY]
        val nowMinute = c[Calendar.MINUTE]

        // if alarm is behind current time, advance one day
        if (hour < nowHour || hour == nowHour && minute <= nowMinute) {
            c.add(Calendar.DAY_OF_YEAR, 1)
        }

        // set values and reset the second and milliseconds
        c[Calendar.HOUR_OF_DAY] = hour
        c[Calendar.MINUTE] = minute
        c[Calendar.SECOND] = 0
        c[Calendar.MILLISECOND] = 0

        return c.timeInMillis
    }
}

/**
 * Class to provide a LiveData object from a SharedPreferences value
 * Inspired by this article: https://medium.com/@jurajkunier/android-shared-preferences-listener-implemented-by-rxjava-and-livedata-cfac02683eac]
 */
class SharedPreferencesLiveDataLong(
    private val preferences: SharedPreferences,
    private val preferenceKey: String,
    private val defaultValue: Long
) : LiveData<Long>() {

    init {
        // just initialize with the current value (in case none is observing yet..)
        value = preferences.getLong(preferenceKey, defaultValue)
    }

    private val mListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == preferenceKey) {
                value = sharedPreferences.getLong(preferenceKey, defaultValue)
            }
        }

    override fun onActive() {
        super.onActive()
        value = preferences.getLong(preferenceKey, defaultValue)
        preferences.registerOnSharedPreferenceChangeListener(mListener)
    }


    override fun onInactive() {
        super.onInactive()
        preferences.unregisterOnSharedPreferenceChangeListener(mListener)
    }
}