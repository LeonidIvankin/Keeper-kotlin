package ru.leonidivankin.kotlinforandroid.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.*
import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.data.errors.NoAuthException
import ru.leonidivankin.kotlinforandroid.data.model.NoteResult
import ru.leonidivankin.kotlinforandroid.data.provider.FireStoreProvider

class FireStoreProviderTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private val mockAuth = mock<FirebaseAuth>()
    private val mockDb = mock<FirebaseFirestore>()

    private val mockUsersCollection = mock<CollectionReference>()
    private val mockUsersDocument = mock<DocumentReference>()
    private val mockResultCollection = mock<CollectionReference>()
    private val mockUser = mock<FirebaseUser>()

    private val mockDocument1 = mock<DocumentSnapshot>()
    private val mockDocument2 = mock<DocumentSnapshot>()
    private val mockDocument3 = mock<DocumentSnapshot>()

    private val testNotes = listOf(
            Note("1"),
            Note("2"),
            Note("3")
    )

    private val provider: FireStoreProvider = FireStoreProvider(mockAuth, mockDb)

    @Before
    fun setup() {
        reset(mockUsersCollection, mockUsersDocument, mockResultCollection, mockDocument1, mockDocument2, mockDocument3)

        whenever(mockAuth.currentUser).thenReturn(mockUser)
        whenever(mockUser.uid).thenReturn("")

        whenever(mockDb.collection(any())).thenReturn(mockUsersCollection)
        whenever(mockUsersCollection.document(any())).thenReturn(mockUsersDocument)
        whenever(mockUsersDocument.collection(any())).thenReturn(mockResultCollection)

        whenever(mockDocument1.toObject(Note::class.java)).thenReturn(testNotes[0])
        whenever(mockDocument2.toObject(Note::class.java)).thenReturn(testNotes[1])
        whenever(mockDocument3.toObject(Note::class.java)).thenReturn(testNotes[2])

    }

    @Test
    fun `should throw NoAuthException if no auth`() {
        var result: Any? = null
        whenever(mockAuth.currentUser).thenReturn(null)
        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.Error)?.error
        }
        assertTrue(result is NoAuthException)
    }

    @Test
    fun `subscribeToAllNotes returns notes`() {
        var result: Any? = null
        val mockSnapShort = mock<QuerySnapshot>()
        val captor = argumentCaptor<EventListener<QuerySnapshot>>()

        whenever(mockSnapShort.documents).thenReturn(listOf(mockDocument1, mockDocument2, mockDocument3))
        whenever(mockResultCollection.addSnapshotListener(captor.capture())).thenReturn(mock())

        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.Success<List<Note>>)?.data
        }


        captor.firstValue.onEvent(mockSnapShort, null)
        assertEquals(testNotes, result)
    }

    @Test
    fun `subscribeToAllNotes returns error`() {
        var result: Throwable? = null
        val testError = mock<FirebaseFirestoreException>()
        val captor = argumentCaptor<EventListener<QuerySnapshot>>()

        whenever(mockResultCollection.addSnapshotListener(captor.capture())).thenReturn(mock())

        provider.subscribeToAllNotes().observeForever {
            result = (it as? NoteResult.Error)?.error
        }

        captor.firstValue.onEvent(null, testError)
        assertEquals(testError, result)
    }

    @Test
    fun `saveNote calls document set`() {
        var mockDocumentReference = mock<DocumentReference>()

        whenever(mockResultCollection.document(testNotes[0].id)).thenReturn(mockDocumentReference)
        provider.saveNote(testNotes[0])
        verify(mockDocumentReference, times(1)).set(testNotes[0])
    }

    @Test
    fun `saveNote returns note`() {
        var mockDocumentReference = mock<DocumentReference>()
        var result: Note? = null
        val captor = argumentCaptor<OnSuccessListener<in Void>>()

        val mockTask = mock<Task<Void>>()

        whenever(mockTask.addOnSuccessListener(captor.capture())).thenReturn(mockTask)
        whenever(mockDocumentReference.set(testNotes[0])).thenReturn(mockTask)
        whenever(mockResultCollection.document(testNotes[0].id)).thenReturn(mockDocumentReference)

        provider.saveNote(testNotes[0]).observeForever {
            result = (it as? NoteResult.Success<Note>)?.data
        }

        captor.firstValue.onSuccess(null)

        assertNotNull(result)
        assertEquals(testNotes[0], result)
    }


}