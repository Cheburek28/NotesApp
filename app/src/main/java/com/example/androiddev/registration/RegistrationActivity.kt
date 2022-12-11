package com.example.androiddev.registration

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.androiddev.databinding.RegistrationBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddev.*
import com.example.androiddev.entities.User

class RegistrationActivity : AppCompatActivity() {
    private val viewModel by viewModels<RegistrationViewModel> {
        RegistrationViewModelFactory(getExternalFilesDir(null))
    }
    private val viewBinding by viewBinding(RegistrationBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)

        viewBinding.saveRegistrationButton.setOnClickListener {
            viewModel.onSave(
                User(viewBinding.editTextName.text.toString(),
                viewBinding.editTextPassword.text.toString())
            )
        }

        viewModel.registrationFinished.observe(this, Observer { onRegistrationFinished(it) }) // Привязываем сигнал об окончании регистрации

    }

    private fun onRegistrationFinished(res: Event<EventRes>) {
        when (res.peekContent().res ) {
            0 -> {
                Toast.makeText(
                    applicationContext,
                    "Registration success",
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

