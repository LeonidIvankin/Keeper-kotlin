package ru.leonidivankin.kotlinforandroid

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel() : ViewModel() {

    private val viewStateLiveData = MutableLiveData<String>()
    var count = 0

    init {
        viewStateLiveData.value = "good"
    }

    fun viewState(): LiveData<String> = viewStateLiveData

    fun buttonClick() {
        count++
        viewStateLiveData.value = count.toString()
    }

}