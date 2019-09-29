package ru.leonidivankin.kotlinforandroid.data

import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.data.provider.FireStoreProvider
import ru.leonidivankin.kotlinforandroid.data.provider.RemoteDataProvider

object NotesRepository {

    private val remoteProvider: RemoteDataProvider = FireStoreProvider()

    fun getNotes() = remoteProvider.subscribeToAllNotes()

    fun saveNote(note: Note) = remoteProvider.saveNote(note)

    fun getNoteById(id: String) = remoteProvider.getNoteById(id)

    fun getCurrentUser() = remoteProvider.getCurrentUser()
}