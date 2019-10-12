package ru.leonidivankin.kotlinforandroid.ui.splash

import ru.leonidivankin.kotlinforandroid.data.NotesRepository
import ru.leonidivankin.kotlinforandroid.data.errors.NoAuthException
import ru.leonidivankin.kotlinforandroid.ui.base.BaseViewModel

class SplashViewModel(private val notesRepository: NotesRepository) : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        notesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(authenticated = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}