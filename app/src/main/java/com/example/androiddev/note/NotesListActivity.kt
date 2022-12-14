package com.example.androiddev.note

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddev.Event
import com.example.androiddev.EventRes
import com.example.androiddev.entities.Note
import com.example.androiddev.R
import com.example.androiddev.databinding.NoteslistActivityBinding
import org.json.JSONObject
import java.io.File


fun get_current_user_name(externalDir: File?): String {
    val settingsFile = File(externalDir,".settings")
    val json = JSONObject(settingsFile.readText())
    return json.get("name").toString()
}

class NotesListActivity : AppCompatActivity() {

    companion object {
        const val USER_DETAIL_ARGUMENT_KEY = "USER_DETAIL_ARGUMENT_KEY"
    }

    private var userName : String = ""
//        intent.getStringExtra(USER_DETAIL_ARGUMENT_KEY).toString()

    private val viewModel by viewModels<NotesListViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NotesListViewModel(get_current_user_name(getExternalFilesDir(null)),
                                        getExternalFilesDir(null)) as T
            }
        }
    }
    private val viewBinding by viewBinding(NoteslistActivityBinding::bind)
    private val notesListAdapter = NotesListAdapter { viewModel.onNoteSelected(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.noteslist_activity)

        userName = intent.getStringExtra(USER_DETAIL_ARGUMENT_KEY).toString()

        viewBinding.NotesTextView.text = "Your notes, $userName"

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
            createNoteClicked() // ?????????? ???????????????????? ?????????????? ???????? ?? ?????? ???????????????? ????????????????????????
        }

        viewModel.creationFinished.observe(this, Observer { onCreationFinished(it) })

        viewModel.updatingFinished.observe(this, Observer { onUpdatingFinished(it) })

        viewBinding.progressBar.visibility = View.GONE

    }

    override fun onStart() {
        super.onStart()

        viewModel.update_notes()
    }

    private fun onUpdatingFinished(res: Event<EventRes>?) {
        when (res?.peekContent()?.res ) {
            0 -> {
//                Toast.makeText(
//                    applicationContext,
//                    "",
//                    Toast.LENGTH_SHORT
//                ).show()
                return
            }
            -1 -> {
                Toast.makeText(
                    applicationContext,
                    "Unknown error! Please contact the developer. ${res.peekContent().text}",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                Toast.makeText(
                    applicationContext,
                    res?.peekContent()?.text,
                    Toast.LENGTH_SHORT
                ).show()

                viewBinding.NotesTextView.text = "Your local notes, $userName"
            }
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

    private fun createNoteClicked() {
        val note = Note(userName)

        viewBinding.progressBar.visibility = View.VISIBLE

        viewModel.createNewNote(note)
    }


    private fun onCreationFinished(res: Event<EventRes>?) {
        when (res?.peekContent()?.res ) {
            0 -> {
                Toast.makeText(
                    applicationContext,
                    "Creation success",
                    Toast.LENGTH_SHORT
                ).show()

            }
            -1 -> {
                Toast.makeText(
                    applicationContext,
                    "Unknown error! Please contact the developer. ${res.peekContent().text}",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {
                Toast.makeText(
                    applicationContext,
                    res?.peekContent()?.text,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        // ???????????????????? ????????????????
        startActivity(Intent(this, NotesListActivity::class.java).apply {
            putExtra(NotesListActivity.USER_DETAIL_ARGUMENT_KEY, userName)
        })
        finish()
    }


}

