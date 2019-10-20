package ru.leonidivankin.kotlinforandroid.ui.splash

import kotlinx.coroutines.launch
import ru.leonidivankin.kotlinforandroid.data.NotesRepository
import ru.leonidivankin.kotlinforandroid.data.errors.NoAuthException
import ru.leonidivankin.kotlinforandroid.ui.base.BaseViewModel

class SplashViewModel(private val notesRepository: NotesRepository) : BaseViewModel<Boolean?>() {

    fun requestUser() {

        launch {
            notesRepository.getCurrentUser()?.let {
                setData(true)
            } ?: setError(NoAuthException())
        }

    }
}