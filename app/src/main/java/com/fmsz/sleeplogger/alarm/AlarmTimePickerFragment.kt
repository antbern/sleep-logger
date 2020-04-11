package com.fmsz.sleeplogger.alarm

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

import java.util.*

class AlarmTimePickerFragment(private val currentTime : AlarmTime, val listener: (AlarmTime) -> Unit) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(activity, this, currentTime.hour, currentTime.minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        listener(AlarmTime(hourOfDay, minute))
    }
}