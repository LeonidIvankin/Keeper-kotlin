package ru.leonidivankin.kotlinforandroid.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.data.entity.User
import ru.leonidivankin.kotlinforandroid.data.errors.NoAuthException
import ru.leonidivankin.kotlinforandroid.data.model.NoteResult
import timber.log.Timber

class FireStoreProvider(private val firebaseAuth: FirebaseAuth, private val store: FirebaseFirestore) : RemoteDataProvider {

    companion object {
        private const val NOTES_COLLECTION = "notes"
        private const val USER_COLLECTION = "users"

    }

    private val notesReference by lazy { store.collection(NOTES_COLLECTION) }

    private val currentUser
        get() = firebaseAuth.currentUser


    override fun getCurrentUser(): LiveData<User?> = MutableLiveData<User>().apply {
        value = currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        }
    }

    private fun getUserNotesCollection() = currentUser?.let {
        store.collection(USER_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
    } ?: throw NoAuthException()

    override fun subscribeToAllNotes(): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection()
                    .addSnapshotListener { snapshot, e ->
                        value = e?.let { NoteResult.Error(it) }
                                ?: let {
                                    snapshot?.let {
                                        val notes = it.documents.map { it.toObject(Note::class.java) }
                                        NoteResult.Success(notes)
                                    }
                                }
                    }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun getNoteById(id: String): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            getUserNotesCollection()
                    .document(id).get()
                    .addOnSuccessListener { snapshot ->
                        value = NoteResult.Success(snapshot.toObject(Note::class.java))
                    }.addOnFailureListener {
                        value = NoteResult.Error(it)
                    }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun saveNote(note: Note): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {

        try {
            getUserNotesCollection()
                    .document(note.id)
                    .set(note)
                    .addOnSuccessListener {
                        Timber.d("note $note is saved")
                        value = NoteResult.Success(note)
                    }.addOnFailureListener {
                        Timber.e(it)
                        value = NoteResult.Error(it)
                    }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }

    }

    override fun deleteNote(id: String): LiveData<NoteResult> {
        return MutableLiveData<NoteResult>().apply {
            getUserNotesCollection().document(id).delete()
                    .addOnSuccessListener {
                        value = NoteResult.Success(null)
                    }.addOnFailureListener {
                        value = NoteResult.Error(it)
                    }
        }
    }


}