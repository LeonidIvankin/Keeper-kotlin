package ru.leonidivankin.kotlinforandroid.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import ru.leonidivankin.kotlinforandroid.data.NotesRepository
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.data.model.NoteResult
import ru.leonidivankin.kotlinforandroid.ui.base.BaseViewModel

class MainViewModel : BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = Observer<NoteResult>{
        if(it == null) return@Observer

        when(it){
            is NoteResult.Success<*> ->
                viewStateLiveData.value = MainViewState(notes = it.data as? List<Note>)
            is NoteResult.Error ->
                viewStateLiveData.value = MainViewState(error = it.error)
        }
    }

    private val repositoryNotes = NotesRepository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)

    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }

    fun viewState(): LiveData<MainViewState> = viewStateLiveData



}