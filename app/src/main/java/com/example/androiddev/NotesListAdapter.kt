package com.example.androiddev

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
            noteName.text = item.name
            date.text = item.last_update_date.toString()
            users.text = item.other_user_name
            root.setOnClickListener { onNoteClicked(item) }
        }

    }

    override fun getItemCount(): Int = notes.size
}