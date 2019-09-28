package ru.leonidivankin.kotlinforandroid.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.data.provider.FireStoreProvider
import ru.leonidivankin.kotlinforandroid.data.provider.RemoteDataProvider
import java.util.*

object NotesRepository {

    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteProvider.subscribeToAllNotes()

    fun saveNote(note: Note) = remoteProvider.saveNote(note)

    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
}