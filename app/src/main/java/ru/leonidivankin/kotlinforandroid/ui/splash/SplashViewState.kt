package ru.leonidivankin.kotlinforandroid.ui.splash

import ru.leonidivankin.kotlinforandroid.ui.base.BaseViewState

class SplashViewState(authenticated: Boolean? = null, error: Throwable? = null) : BaseViewState<Boolean?>(authenticated, error)