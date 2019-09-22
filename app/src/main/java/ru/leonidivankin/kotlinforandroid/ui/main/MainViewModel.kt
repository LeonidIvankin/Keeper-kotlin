package ru.leonidivankin.kotlinforandroid.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.leonidivankin.kotlinforandroid.data.NoteRepository

class MainViewModel : ViewModel() {

    private val viewStateLiveData = MutableLiveData<MainViewState>()

    init {
        NoteRepository.getNotes().observeForever {
            viewStateLiveData.value = viewStateLiveData.value?.copy(notes = it!!) ?: MainViewState(it!!)
        }

    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData



}