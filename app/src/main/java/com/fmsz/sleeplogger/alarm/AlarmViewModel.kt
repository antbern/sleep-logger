package com.fmsz.sleeplogger.alarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class AlarmViewModel(app: Application) : AndroidViewModel(app) {

    // internal counter variable
    private val _counter = MutableLiveData<Int>()

    init {
        _counter.value = 0
    }

    // expose counter as a formatted text string
    val counterText = Transformations.map(_counter) {
        String.format("Count: %d", it)
    }

    // called when the button is clicked
    fun increaseCount() {
        _counter.value = _counter.value?.plus(1)
    }

}