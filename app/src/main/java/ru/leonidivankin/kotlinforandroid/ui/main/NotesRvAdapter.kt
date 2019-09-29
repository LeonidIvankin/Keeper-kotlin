package ru.leonidivankin.kotlinforandroid.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import ru.leonidivankin.kotlinforandroid.R
import ru.leonidivankin.kotlinforandroid.data.entity.Note


class NotesRvAdapter(val onItemClick: ((Note) -> Unit)? = null) : RecyclerView.Adapter<NotesRvAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
    )

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(notes[position])


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: Note) = itemView.run {
            tv_text.text = note.text
            tv_title.text = note.title

            val color = when (note.color) {
                Note.Color.WHITE -> R.color.white
                Note.Color.YELLOW -> R.color.yellow
                Note.Color.GREEN -> R.color.green
                Note.Color.BLUE -> R.color.blue
                Note.Color.RED -> R.color.red
                Note.Color.VIOLET -> R.color.violet
                Note.Color.PINK -> R.color.pink
            }

            linear_layout_item_note.setBackgroundColor(ContextCompat.getColor(itemView.context, color))
            itemView.setOnClickListener {
                onItemClick?.invoke(note)
            }

        }
    }
}