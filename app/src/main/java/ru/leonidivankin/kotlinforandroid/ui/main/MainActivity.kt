package ru.leonidivankin.kotlinforandroid.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.firebase.ui.auth.AuthUI
import kotlinx.android.synthetic.main.activity_main.*
import ru.leonidivankin.kotlinforandroid.R
import ru.leonidivankin.kotlinforandroid.data.entity.Note
import ru.leonidivankin.kotlinforandroid.ui.base.BaseActivity
import ru.leonidivankin.kotlinforandroid.ui.note.NoteActivity
import ru.leonidivankin.kotlinforandroid.ui.splash.SplashActivity

class MainActivity : BaseActivity<List<Note>?, MainViewState>(), LogoutDialog.LogoutListener {

    companion object {
        fun start(context: Context) = Intent(context, MainActivity::class.java).run {
            context.startActivity(this)
        }
    }

    override val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

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
        supportFragmentManager
                .findFragmentByTag(LogoutDialog.TAG) ?: LogoutDialog.createInstance()
                .show(supportFragmentManager, LogoutDialog.TAG)
    }

    override fun onLogout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener {
                    startActivity(Intent(this, SplashActivity::class.java))
                    finish()
                }
    }
}
