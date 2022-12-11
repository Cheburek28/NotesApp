package com.example.androiddev.note

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddev.*
import com.example.androiddev.allowd_users.SetAllowedUserActivity
import com.example.androiddev.entities.Note
import com.example.androiddev.databinding.NoteEditBinding
import com.example.androiddev.internet.MySQLDBRepository
import kotlinx.coroutines.coroutineScope

class NoteEditActivity : AppCompatActivity(){

    private val viewBinding by viewBinding(NoteEditBinding::bind)

    private val viewModel by viewModels<NoteEditViewModel>()

    companion object {
        const val NOTE_DETAIL_ARGUMENT_KEY = "NOTE_DETAIL_ARGUMENT_KEY"
//        const val NOTE_DETAIL_RESULT_KEY = "NOTE_DETAIL_RESULT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_edit)

//        Toast.makeText(
//            applicationContext,
//            "Password Should Greater than 7 Digits",
//            Toast.LENGTH_SHORT
//        ).show()

        val note = intent.getParcelableExtra<Note>(NOTE_DETAIL_ARGUMENT_KEY)

        if (note != null) {
            viewBinding.edittitleofnote.setText(note.title)
            viewBinding.editcontentofnote.setText(note.content)
        }

        viewBinding.saveeditnote.setOnClickListener { onSaveClicked() }

        viewBinding.menupopbutton.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this, viewBinding.menupopbutton)
            popupMenu.gravity = Gravity.END
            popupMenu.menu.add("Delete").setOnMenuItemClickListener {
                deleteNote()
                false
            }
            popupMenu.menu.add("Add user").setOnMenuItemClickListener {
                addUser()
                false
            }
            popupMenu.show()

        }

        viewModel.savingFinished.observe(this, Observer { saveFinished(it) })
        viewModel.deletingFinished.observe(this, Observer { deletingFinished(it) })

    }

    private fun onSaveClicked() {
        val note = intent.getParcelableExtra<Note>(NOTE_DETAIL_ARGUMENT_KEY)

        if (note != null) {
            note.title = viewBinding.edittitleofnote.text.toString()
            note.content = viewBinding.editcontentofnote.text.toString()
            viewModel.saveNote(note, getExternalFilesDir(null))
        } else {
            Toast.makeText(
                applicationContext,
                "This wwindow didn't fetch note! Please contact the developer!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun saveFinished(res: Event<EventRes>) {
        when (res.peekContent().res ) {
            0 -> {
                Toast.makeText(
                    applicationContext,
                    "Saving success",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
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
                    res.peekContent().text,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun deletingFinished(res: Event<EventRes>) {
        when (res.peekContent().res ) {
            0 -> {
                Toast.makeText(
                    applicationContext,
                    "Deleting success",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
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
                    res.peekContent().text,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun deleteNote() {
        val note = intent.getParcelableExtra<Note>(NOTE_DETAIL_ARGUMENT_KEY)

        if (note != null) {
            viewModel.deleteNote(note, getExternalFilesDir(null))
        } else {
            Toast.makeText(
                applicationContext,
                "This wwindow didn't fetch note! Please contact the developer!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun addUser() {
        val note = intent.getParcelableExtra<Note>(NOTE_DETAIL_ARGUMENT_KEY)

        if (note != null) {
            startActivity(
                Intent(this, SetAllowedUserActivity::class.java)
                    .apply {
                        putExtra(SetAllowedUserActivity.NOTE_DETAIL_ARGUMENT_KEY, note)
                    },
            )
        } else {
            Toast.makeText(
                applicationContext,
                "This wwindow didn't fetch note! Please contact the developer!",
                Toast.LENGTH_LONG
            ).show()
        }
    }



}