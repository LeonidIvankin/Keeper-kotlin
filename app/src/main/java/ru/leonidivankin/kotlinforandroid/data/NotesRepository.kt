package ru.leonidivankin.kotlinforandroid.data

import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.data.provider.RemoteDataProvider

class NotesRepository(private val remoteProvider: RemoteDataProvider) {

    fun getNotes() = remoteProvider.subscribeToAllNotes()

    fun saveNote(note: Note) = remoteProvider.saveNote(note)

    fun getNoteById(id: String) = remoteProvider.getNoteById(id)

    fun getCurrentUser() = remoteProvider.getCurrentUser()

    fun deleteNote(id: String) = remoteProvider.deleteNote(id)
}