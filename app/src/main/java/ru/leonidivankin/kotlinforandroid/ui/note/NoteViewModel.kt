package ru.leonidivankin.kotlinforandroid.ui.note

import ru.leonidivankin.kotlinforandroid.data.NotesRepository
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.data.model.NoteResult
import ru.leonidivankin.kotlinforandroid.ui.base.BaseViewModel

class NoteViewModel : BaseViewModel<Note?, NoteViewState>() {

    init {
        viewStateLiveData.value = NoteViewState()
    }


    private var pendingNote: Note? = null

    fun save(note: Note) {
        pendingNote = note
    }

    override fun onCleared() {
        if (pendingNote != null) {
            NotesRepository.saveNote(pendingNote!!)
        }
    }

    fun loadNote(noteId: String) {
        NotesRepository.getNoteById(noteId).observeForever {
            if (it == null) return@observeForever
            when (it) {
                is NoteResult.Success<*> ->
                    viewStateLiveData.value = NoteViewState(note = it.data as? Note)
                is NoteResult.Error ->
                    viewStateLiveData.value = NoteViewState(error = it.error)
            }
        }
    }
}