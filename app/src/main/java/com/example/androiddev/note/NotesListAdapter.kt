package com.example.androiddev.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddev.entities.Note
import com.example.androiddev.databinding.NotePanelBinding

class NotesListAdapter (
    private val onNoteClicked: (Note) -> Unit
        ): RecyclerView.Adapter<NotesListAdapter.Holder>() {

    private var notes: List<Note> = emptyList()

    fun setData(notes: List<Note>) {
        this.notes = notes
        notifyDataSetChanged()
    }

    class Holder(val binding : NotePanelBinding) :  RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = NotePanelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = notes[position]
        with(holder.binding) {
            noteName.text = item.title
            date.text = item.date.toString()
            users.text = item.allowed_user_name
            root.setOnClickListener { onNoteClicked(item) }
        }

    }

    override fun getItemCount(): Int = notes.size
}