package ru.leonidivankin.kotlinforandroid.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.koin.android.viewmodel.ext.android.viewModel
import ru.leonidivankin.kotlinforandroid.R
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.ui.base.BaseActivity
import ru.leonidivankin.kotlinforandroid.ui.note.NoteActivity
import ru.leonidivankin.kotlinforandroid.ui.splash.SplashActivity

class MainActivity : BaseActivity<List<Note>?, MainViewState>() {

    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).run {
            context.startActivity(this)
        }
    }

    override val viewModel: MainViewModel by viewModel()

    override val layoutRes = R.layout.activity_main


    lateinit var adapter: NotesRvAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

        rv_notes.layoutManager = GridLayoutManager(this, 2)
        adapter = NotesRvAdapter {
            NoteActivity.start(this, it)
        }
        rv_notes.adapter = adapter

        fab.setOnClickListener {
            NoteActivity.start(this)
        }
    }

    override fun renderData(data: List<Note>?) {
        data?.let {
            adapter.notes = it
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean = MenuInflater(this)
            .inflate(R.menu.menu_main, menu)
            .let{true}

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.logout -> showLogoutDialog().let { true }
        else -> false
    }

    private fun showLogoutDialog() {
        alert {
            titleResource = R.string.logout_dialog_title
            messageResource = R.string.logout_dialog_message
            positiveButton(R.string.note_ok) { onLogout() }
            negativeButton(R.string.logout_dialog_cancel) { it.dismiss() }
        }.show()
    }

    fun onLogout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener {
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                }
    }
}
