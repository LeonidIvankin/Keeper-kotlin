package ru.leonidivankin.kotlinforandroid.ui.main

import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.ui.base.BaseViewState

class MainViewState (val notes: List<Note>? = null, error: Throwable? = null): BaseViewState<List<Note>?> (notes, error)