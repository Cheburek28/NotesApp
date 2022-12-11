package com.example.androiddev.allowd_users

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddev.Event
import com.example.androiddev.EventRes
import com.example.androiddev.R
import com.example.androiddev.databinding.RegistrationBinding
import com.example.androiddev.databinding.SetAllowedUserBinding
import com.example.androiddev.entities.Note
import com.example.androiddev.entities.User
import com.example.androiddev.note.NoteEditActivity

class SetAllowedUserActivity : AppCompatActivity()  {
    private val viewModel by viewModels<AllowedUserViewModel> ()

    private val viewBinding by viewBinding(SetAllowedUserBinding::bind)

    companion object {
        const val NOTE_DETAIL_ARGUMENT_KEY = "NOTE_DETAIL_ARGUMENT_KEY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.set_allowed_user)

        viewBinding.button.setOnClickListener {
            onButtonClicked()
        }

        viewModel.additingFinished.observe(this, Observer { additingFinished(it) })

    }

    private fun onButtonClicked() {
        val note = intent.getParcelableExtra<Note>(NoteEditActivity.NOTE_DETAIL_ARGUMENT_KEY)

        if (note != null) {
            note.allowed_user_name = viewBinding.editTextPersonName.text.toString()
            viewModel.addAllowedUser(note)
        } else {
            Toast.makeText(
                applicationContext,
                "This wwindow didn't fetch note! Please contact the developer!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun additingFinished(res: Event<EventRes>) {
        when (res.peekContent().res ) {
            0 -> {
                Toast.makeText(
                    applicationContext,
                    "Additing success",
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
}