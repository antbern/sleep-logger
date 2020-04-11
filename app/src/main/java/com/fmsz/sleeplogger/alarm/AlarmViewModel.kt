package com.fmsz.sleeplogger.alarm

import android.app.Application
import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.*

class AlarmViewModel(app: Application, private val state: SavedStateHandle) :
    AndroidViewModel(app) {

    // alarmTime is the time the alarm is supposed to go off
    val alarmTime: LiveData<Long> = AlarmManager.getAlarmTime(getApplication())

    // alarmStartTime is the time when the alarm was enabled/started
    val alarmStartTime: LiveData<Long> = AlarmManager.getAlarmStartTime(getApplication())

    // alarmEnabled is true if the alarm is active/has been started/enabled
    val alarmEnabled = alarmStartTime.map { it > 0 }

    val alarmEnabledText = alarmEnabled.map {
        when (it) {
            true -> "Alarm ON"
            false -> "Alarm OFF"
        }
    }

    // expose the alarmTime as a properly formatted text string
    val alarmText = alarmTime.map { alarmTime ->
        DateUtils.formatDateTime(getApplication(), alarmTime, DateUtils.FORMAT_SHOW_TIME)
    }

    // stuff for showing the progress
    private val _progressValue = MutableLiveData<Int>()
    val progressValue: LiveData<Int>
        get() = _progressValue

    init {
        // initialize variables here
    }

    // called when the switch is toggled, turn on and off the alarm here
    fun setAlarmEnabled(enabled: Boolean) {
        if (enabled) {
            AlarmManager.setAlarm(getApplication(), AlarmTime.fromMillis(alarmTime.value!!))
        } else {
            AlarmManager.cancelAlarm(getApplication())
        }
    }

    // sets a new time for the alarm
    fun onSetAlarmTime(newTime: AlarmTime) {
        AlarmManager.setAlarm(getApplication(), newTime)
    }

    override fun onCleared() {
        super.onCleared()
    }

}
