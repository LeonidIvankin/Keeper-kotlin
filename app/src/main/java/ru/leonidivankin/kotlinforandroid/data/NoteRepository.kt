package ru.leonidivankin.kotlinforandroid.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import java.util.*

object NoteRepository {

    private var notesLiveData = MutableLiveData<List<Note>>()

    var notes = mutableListOf(
            Note(UUID.randomUUID().toString(), "2 note", "text of 2 note", Note.Color.WHITE),
            Note(UUID.randomUUID().toString(), "3 note", "text of 3 note", Note.Color.YELLOW),
            Note(UUID.randomUUID().toString(), "4 note", "text of 4 note", Note.Color.GREEN),
            Note(UUID.randomUUID().toString(), "5 note", "text of 5 note", Note.Color.BLUE),
            Note(UUID.randomUUID().toString(), "6 note", "text of 6 note", Note.Color.RED),
            Note(UUID.randomUUID().toString(), "6 note", "text of 6 note", Note.Color.VIOLET)
    )
        private set

    init {
        notesLiveData.value = notes
    }

    fun getNotes(): LiveData<List<Note>> {
       return notesLiveData
    }

    fun saveNote(note: Note){
        addOrReplace(note)
        notesLiveData.value = notes
    }

    private fun addOrReplace(note: Note){
        for(i in 0 until notes.size){
            if(notes[i] == note){
                notes[i] = note
                return
            }
        }

        notes.add(note)
    }
}