package ru.leonidivankin.kotlinforandroid.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.*
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.leonidivankin.kotlinforandroid.data.NotesRepository
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.data.model.NoteResult
import ru.leonidivankin.kotlinforandroid.ui.main.MainViewModel

class MainViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()


    private val mockRepository = mock<NotesRepository>()
    private val notesLiveData = MutableLiveData<NoteResult>()

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        reset(mockRepository)
        whenever(mockRepository.getNotes()).thenReturn(notesLiveData)
        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun `should call getNotes once`() {
        verify(mockRepository, times(1)).getNotes()
    }

    @Test
    fun `should return Notes`() {
        var result: List<Note>? = null

        val testData = listOf(Note("1"), Note("2"))

        viewModel.getViewState().observeForever {
            result = it?.data

        }

        notesLiveData.value = NoteResult.Success(testData)

        assertEquals(testData, result)
    }

    @Test
    fun `should return error`() {
        var result: Throwable? = null

        val testData = Throwable("error")

        viewModel.getViewState().observeForever {
            result = it?.error

        }

        notesLiveData.value = NoteResult.Error(testData)

        assertEquals(testData, result)
    }

    @Test
    fun `should remove observer`() {
        viewModel.onCleared()

        assertFalse(notesLiveData.hasObservers())
    }


}