package ru.leonidivankin.kotlinforandroid.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.leonidivankin.kotlinforandroid.data.NoteRepository

class MainViewModel : ViewModel() {

    private val viewStateLiveData = MutableLiveData<MainViewState>()

    init {
        viewStateLiveData.value = MainViewState(NoteRepository.getNotes())
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData



}