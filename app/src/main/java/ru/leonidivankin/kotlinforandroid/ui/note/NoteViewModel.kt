package ru.leonidivankin.kotlinforandroid.ui.note

import androidx.lifecycle.ViewModel
import ru.leonidivankin.kotlinforandroid.data.NoteRepository
import ru.leonidivankin.kotlinforandroid.data.entity.Note

class NoteViewModel : ViewModel() {
    private var pendingNote: Note? = null

    fun save(note: Note){
        pendingNote = note
    }

    override fun onCleared() {
        if(pendingNote != null){
            NoteRepository.saveNote(pendingNote!!)
        }
    }
}