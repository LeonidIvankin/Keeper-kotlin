package ru.leonidivankin.kotlinforandroid.note

import android.content.Intent
import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doReturn
import ru.leonidivankin.kotlinforandroid.R
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.ui.note.NoteActivity
import ru.leonidivankin.kotlinforandroid.ui.note.NoteViewModel
import ru.leonidivankin.kotlinforandroid.ui.note.NoteViewState

class NoteActivityTest {
    @get:Rule
    val activityTestRule = IntentsTestRule(NoteActivity::class.java, true, false)

    private val model: NoteViewModel = Mockito.mock(NoteViewModel::class.java)

    private val viewStateLiveData = MutableLiveData<NoteViewState>()

    private val testNote = Note("1", "title1", "text1")

    @Before
    fun setup() {
        loadKoinModules(
                listOf(
                        module {
                            viewModel(override = true) { model }
                        }
                )
        )

        doReturn(viewStateLiveData).`when`(model).getViewState()

        doNothing().`when`(model).loadNote(testNote.id)
        doNothing().`when`(model).save(Mockito.any())
        doNothing().`when`(model).deleteNote()

        Intent().apply {
            putExtra("EXTRA_NOTE", testNote.id)
        }.let {
            activityTestRule.launchActivity(null)
        }

    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun should_show_color_picker() {
        onView(withId(R.id.note_select_color)).perform(click())
        onView(withId(R.id.colorPicker)).check(matches(isCompletelyDisplayed()))
    }
}