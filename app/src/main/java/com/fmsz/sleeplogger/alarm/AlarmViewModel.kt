package com.fmsz.sleeplogger.alarm

import android.app.Application
import android.content.Context
import android.text.format.DateUtils
import androidx.lifecycle.*

class AlarmViewModel(app: Application, private val state: SavedStateHandle) :
    AndroidViewModel(app) {
    companion object {
        // constants used for the SaveState library
        private const val KEY_ALARM_TIME = "alarm_time"
        private const val KEY_ALARM_START = "alarm_start"

        // constants for storing state in SharedPreferences
        private const val PREFERENCES_NAME = "alarm_storage"
    }

    // alarmTime is the time the alarm is supposed to go off
    private val _alarmTime = state.getLiveData<Long>(KEY_ALARM_TIME)
    val alarmTime: LiveData<Long>
        get() = _alarmTime

    // alarmStartTime is the time when the alarm was enabled/started
    private val _alarmStartTime = state.getLiveData<Long>(KEY_ALARM_START)
    val alarmStartTime: LiveData<Long>
        get() = _alarmStartTime

    // alarmEnabled is true if the alarm is active/has been started/enabled
    val alarmEnabled = _alarmStartTime.map { it != null && it > 0 }

    // expose the alarmTime as a properly formatted text string
    val alarmText = _alarmTime.map { alarmTime ->
        when (alarmTime) {
            null -> "--:--"
            else -> DateUtils.formatDateTime(
                getApplication(),
                alarmTime,
                DateUtils.FORMAT_SHOW_TIME
            )
        }
    }

    init {
        // load old values from persistent storage only if no existing value was given by the SavedStateHandle
        // if there is currently a value, we are currently recovering from a system death in which
        // case the onCleared function would not be called, and ths nothing saved to the persistence storage
        if (_alarmTime.value == null)
            loadStateFromPreferences()
    }

    // called when the switch is toggled
    fun setAlarmEnabled(enabled: Boolean) {
        // turn on and off the alarm here
//        setAlarmTime(System.currentTimeMillis())
    }

    // sets a new time for the alarm, should ideally
    fun onSetAlarmTime(newTime: Long) {
        _alarmTime.value = newTime
    }

    override fun onCleared() {
        super.onCleared()

        // write to persistent storage since the ViewModel is being destroyed
        writeStateToPreferences()
    }

    private fun getPreferences() =
        getApplication<Application>().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    private fun loadStateFromPreferences() {
        with(getPreferences()) {
            _alarmTime.value = getLong(KEY_ALARM_TIME, System.currentTimeMillis())
            _alarmStartTime.value = getLong(KEY_ALARM_START, 0)
        }
    }

    private fun writeStateToPreferences() {
        with(getPreferences().edit()) {
            putLong(KEY_ALARM_TIME, alarmTime.value ?: System.currentTimeMillis())
            putLong(KEY_ALARM_START, alarmStartTime.value ?: 0)
            apply() // saves to file asynchronously
        }
    }
}
