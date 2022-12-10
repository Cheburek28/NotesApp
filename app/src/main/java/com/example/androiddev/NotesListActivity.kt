package com.example.androiddev

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddev.databinding.NoteslistActivityBinding

class NotesListActivity  : AppCompatActivity() {

    private val viewModel by viewModels<NotesListViewModel> ()
    private val viewBinding by viewBinding(NoteslistActivityBinding::bind)
    private val notesListAdapter = NotesListAdapter { viewModel.onNoteSelected(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.noteslist_activity)

        with(viewBinding.notesList) {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = notesListAdapter
        }

        viewModel.notes.observe(this) { notes ->
            notesListAdapter.setData(notes)
        }

        viewModel.openNote.observe(this) {
            openNote(it)
        }

        viewBinding.createnotebutton.setOnClickListener {
            openNote(Note("nick", "", "")) // Нужно передавать текущую дату и ник текущего пользователя
        }

    }

    private fun openNote(note: Note) {
        startActivity(
            Intent(this, NoteEditActivity::class.java)
                .apply {
                    putExtra(NoteEditActivity.NOTE_DETAIL_ARGUMENT_KEY, note)
                },
        )
    }


}

