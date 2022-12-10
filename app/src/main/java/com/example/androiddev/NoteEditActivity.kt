package com.example.androiddev

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddev.databinding.NoteEditBinding

class NoteEditActivity : AppCompatActivity(){

    private val viewBinding by viewBinding(NoteEditBinding::bind)

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
            viewBinding.edittitleofnote.setText(note.name)
            viewBinding.editcontentofnote.setText(note.content)
        }

        viewBinding.saveeditnote.setOnClickListener { onSaveClicked() }

    }

    private fun onSaveClicked() {
        // Save note

        finish()
    }

}