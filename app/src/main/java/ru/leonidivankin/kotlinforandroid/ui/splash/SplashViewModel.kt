package ru.leonidivankin.kotlinforandroid.ui.splash

import ru.leonidivankin.kotlinforandroid.data.NotesRepository
import ru.leonidivankin.kotlinforandroid.data.errors.NoAuthException
import ru.leonidivankin.kotlinforandroid.ui.base.BaseViewModel

class SplashViewModel : BaseViewModel<Boolean?, SplashViewState>() {

    fun requestUser() {
        NotesRepository.getCurrentUser().observeForever {
            viewStateLiveData.value = if (it != null) {
                SplashViewState(authenticated = true)
            } else {
                SplashViewState(error = NoAuthException())
            }
        }
    }
}