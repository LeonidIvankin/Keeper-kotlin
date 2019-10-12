package ru.leonidivankin.kotlinforandroid.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_note.*
import org.jetbrains.anko.alert
import org.koin.android.viewmodel.ext.android.viewModel
import ru.leonidivankin.kotlinforandroid.R
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.ui.base.BaseActivity
import ru.leonidivankin.kotlinforandroid.ui.common.format
import ru.leonidivankin.kotlinforandroid.ui.common.getColorInt
import timber.log.Timber
import java.util.*
import kotlin.contracts.ExperimentalContracts

class NoteActivity : BaseActivity<Note?, NoteViewState>() {

    companion object {
        private val EXTRA_NOTE = "EXTRA_NOTE"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, note: Note? = null) {
            val intent = Intent(context, NoteActivity::class.java).apply {
                note?.let {
                    putExtra(EXTRA_NOTE, it.id)
                }
            }
            context.startActivity(intent)

        }
    }

    private var color: Note.Color = Note.Color.WHITE
    private var note: Note? = null
    override val layoutRes = R.layout.activity_note
    override val viewModel: NoteViewModel by viewModel()

    val textChangeListener = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            saveNote()
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getStringExtra(EXTRA_NOTE)
        noteId?.let {
            viewModel.loadNote(it)
        } ?: let {
            getString(R.string.new_note_title)
        }
    }

    @ExperimentalContracts
    override fun renderData(data: Note?) {
        this.note = data
        supportActionBar?.title = note?.let {
            it.lastChanged.format(DATE_TIME_FORMAT)
        } ?: getString(R.string.new_note_title)

        initView()
    }

    private fun initView() {

        et_title.removeTextChangedListener(textChangeListener)
        et_body.removeTextChangedListener(textChangeListener)

        note?.let { note ->
            et_body.setText(note.text)
            et_title.setText(note.title)
            toolbar.setBackgroundColor(note.color.getColorInt(this))
        }

        et_title.addTextChangedListener(textChangeListener)
        et_body.addTextChangedListener(textChangeListener)

        colorPicker.onColorClickListener = {
            color = it
            toolbar.setBackgroundColor(color.getColorInt(this))
            saveNote()
        }

    }

    private fun saveNote() {
        if (et_title.text == null || (et_title.text?.length ?: 0) < 3)
            return

        note = note?.copy(
                title = et_title.text.toString(),
                text = et_body.text.toString(),
                lastChanged = Date(),
                color = color
        ) ?: Note(
                UUID.randomUUID().toString(),
                et_title.text.toString(),
                et_body.text.toString())

        note?.let { viewModel.save(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu?) = menuInflater.inflate(R.menu.menu_note, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> super.onBackPressed().let { true }
        R.id.note_delete -> deleteNote().let { true }
        R.id.note_select_color -> togglePallete().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun togglePallete() {
        Timber.d("togglePallete")
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    private fun deleteNote() {
        alert {
            messageResource = R.string.note_delete_message
            negativeButton(R.string.note_delete_cancel) { dialog -> dialog.dismiss() }
            positiveButton(R.string.note_ok) { viewModel.deleteNote() }
        }.show()
    }
}
