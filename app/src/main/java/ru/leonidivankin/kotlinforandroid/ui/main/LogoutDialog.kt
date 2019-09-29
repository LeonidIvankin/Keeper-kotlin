package ru.leonidivankin.kotlinforandroid.ui.main

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class LogoutDialog : DialogFragment() {

    companion object {
        val TAG = LogoutDialog::class.java.name + "TAG"
        fun createInstance() = LogoutDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(context!!)
                    .setTitle("Выход")
                    .setMessage("Вы уверены?")
                    .setPositiveButton("Да") { _, _ -> (activity as LogoutListener).onLogout() }
                    .setNegativeButton("Нет") { _, _ -> dismiss() }
                    .create()


    interface LogoutListener {
        fun onLogout()
    }
}