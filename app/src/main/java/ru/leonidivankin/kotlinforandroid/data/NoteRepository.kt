package ru.leonidivankin.kotlinforandroid.data

import ru.leonidivankin.kotlinforandroid.data.entity.Note

object NoteRepository{
    private val notes: List<Note> = listOf(
            Note("1 note", "text of 1 note", 0xfff06292.toInt()),
            Note("2 note", "text of 2 note", 0xff9575cd.toInt()),
            Note("3 note", "text of 3 note", 0xff64b5f6.toInt()),
            Note("4 note", "text of 4 note", 0xff4db6ac.toInt()),
            Note("5 note", "text of 5 note", 0xffb2ff59.toInt()),
            Note("6 note", "text of 6 note", 0xffffeb3b.toInt())
    )

    fun getNotes() = notes
}