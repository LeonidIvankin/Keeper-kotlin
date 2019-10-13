package ru.leonidivankin.kotlinforandroid.mainActivity

import androidx.lifecycle.MutableLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.mockito.Mockito
import org.mockito.Mockito.doReturn
import ru.leonidivankin.kotlinforandroid.R
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.ui.main.MainActivity
import ru.leonidivankin.kotlinforandroid.ui.main.MainViewModel
import ru.leonidivankin.kotlinforandroid.ui.main.MainViewState
import ru.leonidivankin.kotlinforandroid.ui.main.NotesRvAdapter

class MainActivityTest {

    //проходит, если MainViewModel сделать open
    @get:Rule
    val activityTestRule = IntentsTestRule(MainActivity::class.java, true, false)

    private val model: MainViewModel = Mockito.mock(MainViewModel::class.java)

    private val viewStateLiveData = MutableLiveData<MainViewState>()

    private val testNotes = listOf(
            Note("1", "title1", "text1"),
            Note("2", "title2", "text2"),
            Note("3", "title3", "text3")
    )

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
        activityTestRule.launchActivity(null)
        viewStateLiveData.postValue(MainViewState(notes = testNotes))
    }


    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun check_data_id_displayed() {
        onView(withId(R.id.rv_notes)).perform(scrollToPosition<NotesRvAdapter.ViewHolder>(1))
        onView(withText(testNotes[1].text)).check(matches(isDisplayed()))
    }
}