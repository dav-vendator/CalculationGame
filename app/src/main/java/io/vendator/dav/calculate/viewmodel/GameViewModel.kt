package io.vendator.dav.calculate.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.timer

class GameViewModel : ViewModel() {
    val currentScore: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val currentTotal: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }


    var accumulatedTime: Long = 0
        private set

    private var startTime: Long = 0

    fun startTime() {
        startTime = Date().time
    }

    fun stopTime() {
        accumulatedTime += (Date().time - startTime)
    }


}