package ru.leonidivankin.kotlinforandroid.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.leonidivankin.kotlinforandroid.R
import ru.leonidivankin.kotlinforandroid.ui.note.NoteActivity

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var adapter: NotesRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        rv_notes.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesRvAdapter {
            NoteActivity.start(this, it)
        }
        rv_notes.adapter = adapter

        viewModel.viewState().observe(this, Observer { it ->
            it?.let { adapter.notes = it.notes }
        })

        fab.setOnClickListener {
            NoteActivity.start(this)
        }


    }
}
