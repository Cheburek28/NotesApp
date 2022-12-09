package com.example.androiddev

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.androiddev.databinding.RegistrationBinding
import by.kirich1409.viewbindingdelegate.viewBinding

class RegistrationActivity : AppCompatActivity() {
    private val viewModel by viewModels<RegistrationViewModel> {
        RegistrationViewModelFactory(getExternalFilesDir(null))
    }
    private val viewBinding by viewBinding(RegistrationBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)

        viewBinding.saveRegistrationButton.setOnClickListener {
            viewModel.onSave(User(viewBinding.editTextName.text.toString(),
                viewBinding.editTextPassword.text.toString()))
        }

        viewModel.registrationFinished.observe(this, Observer { finish() }) // Привязываем сигнал об окончании регистрации

    }



}

