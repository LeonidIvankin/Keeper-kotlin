package ru.leonidivankin.kotlinforandroid.data.provider

import kotlinx.coroutines.channels.ReceiveChannel
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.data.entity.User
import ru.leonidivankin.kotlinforandroid.data.model.NoteResult

interface RemoteDataProvider {

    fun subscribeToAllNotes(): ReceiveChannel<NoteResult>
    suspend fun getNoteById(id: String): Note
    suspend fun saveNote(note: Note): Note
    suspend fun getCurrentUser(): User?
    suspend fun deleteNote(id: String)
}