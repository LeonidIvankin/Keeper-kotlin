package ru.leonidivankin.kotlinforandroid.ui.note

import androidx.annotation.VisibleForTesting
import kotlinx.coroutines.launch
import ru.leonidivankin.kotlinforandroid.data.NotesRepository
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.ui.base.BaseViewModel

open class NoteViewModel(private val notesRepository: NotesRepository) :
        BaseViewModel<NoteData>() {

    private val currentNote: Note?
        get() = getViewState().poll()?.note

    fun save(note: Note) {
        setData(NoteData(note = note))
    }

    init {
        setData(NoteData())
    }

    @VisibleForTesting
    public override fun onCleared() {
        launch {
            currentNote?.let {
                notesRepository.saveNote(it)
                super.onCleared()
            }
        }
    }

    fun loadNote(noteId: String) {

        launch {
            try {
                setData(NoteData(note = notesRepository.getNoteById(noteId)))
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }

    fun deleteNote() {
        launch {
            try {
                currentNote?.let {
                    notesRepository.deleteNote(it.id)
                    setData(NoteData(isDeleted = true))
                }
            } catch (e: Throwable) {
                setError(e)
            }
        }
    }
}