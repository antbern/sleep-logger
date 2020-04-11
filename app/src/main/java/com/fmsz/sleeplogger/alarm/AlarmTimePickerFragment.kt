package com.fmsz.sleeplogger.alarm

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment

import java.util.*

class AlarmTimePickerFragment(private val currentTime : Long?, val listener: (Long) -> Unit) : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c = Calendar.getInstance()

        if (currentTime != null)
            c.timeInMillis = currentTime

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // create a new calendar using the provided values
        val c = Calendar.getInstance()
        c.set(Calendar.HOUR_OF_DAY, hourOfDay)
        c.set(Calendar.MINUTE, minute)

        // if the time is behind the current time, assume tomorrow and add one day
        if (c.before(Calendar.getInstance()))
            c.add(Calendar.DATE, 1)

        listener(c.timeInMillis)
    }
}