package ru.leonidivankin.kotlinforandroid.data.provider

import androidx.constraintlayout.solver.widgets.Snapshot
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.data.model.NoteResult
import timber.log.Timber

class FireStoreProvider: RemoteDataProvider {

    companion object{
        private const val NOTES_COLLECTION  = "notes"
    }

    private val store = FirebaseFirestore.getInstance()
    private val notesReference = store.collection(NOTES_COLLECTION)


    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.addSnapshotListener{snapShort, e ->
            if(e != null){
                result.value = NoteResult.Error(e)

            } else if(snapShort != null){
                val notes = mutableListOf<Note>()

                for(doc: QueryDocumentSnapshot in snapShort){
                    notes.add(doc.toObject(Note::class.java))
                }

                result.value = NoteResult.Success(notes)
            }

        }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(id).get()
                .addOnSuccessListener {
                    result.value = NoteResult.Success(it.toObject(Note::class.java))
                }.addOnFailureListener {
                    result.value = NoteResult.Error(it)
                }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        notesReference.document(note.id)
                .set(note)
                .addOnSuccessListener {
                    Timber.d("note $note is saved")
                    result.value = NoteResult.Success(note)
                }.addOnFailureListener {
                    Timber.e(it)
                    result.value = NoteResult.Error(it)
                }

        return result

    }


}