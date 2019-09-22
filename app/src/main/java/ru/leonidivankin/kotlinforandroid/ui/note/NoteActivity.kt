package ru.leonidivankin.kotlinforandroid.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_note.*
import kotlinx.android.synthetic.main.activity_note.toolbar
import ru.leonidivankin.kotlinforandroid.R
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : AppCompatActivity() {

    companion object {
        private val EXTRA_NOTE = "EXTRA_NOTE"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"
        private const val SAVE_DELAY = 2000L

        fun start(context: Context, note: Note? = null) {
            val intent = Intent(context, NoteActivity::class.java).apply {
                putExtra(EXTRA_NOTE, note)
            }
            context.startActivity(intent)

        }
    }

    private var note: Note? = null
    lateinit var viewModel: NoteViewModel

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
        setContentView(R.layout.activity_note)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        note = intent.getParcelableExtra(EXTRA_NOTE)

        viewModel  = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        supportActionBar?.title = note?.let{
            SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(note!!.lastChanged)
        } ?: getString(R.string.new_note_title)

        initView()
    }

    private fun initView(){
        if(note != null){
            et_title.setText(note!!.title)
            et_body.setText(note!!.text)

            val color = when (note!!.color) {
                Note.Color.WHITE -> R.color.white
                Note.Color.YELLOW -> R.color.yellow
                Note.Color.GREEN -> R.color.green
                Note.Color.BLUE -> R.color.blue
                Note.Color.RED -> R.color.red
                Note.Color.VIOLET -> R.color.violet
                Note.Color.PINK -> R.color.pink
            }
            toolbar.setBackgroundColor(ContextCompat.getColor(this, color))

        }

        et_title.addTextChangedListener(textChangeListener)
        et_body.addTextChangedListener(textChangeListener)
    }

    private fun saveNote(){
        if (et_title.text == null || (et_title.text?.length ?: 0) < 3)
            return

        Handler().postDelayed({
            note = note?.copy(
                    title = et_title.text.toString(),
                    text = et_body.text.toString(),
                    lastChanged = Date()
            ) ?: createNewNote()

            if(note != null) viewModel.save(note!!)

        }, SAVE_DELAY)
    }

    private fun createNewNote() = Note(UUID.randomUUID().toString(),
            et_title.text.toString(),
            et_body.text.toString())

    override fun onOptionsItemSelected(item: MenuItem): Boolean  = when(item.itemId){
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
