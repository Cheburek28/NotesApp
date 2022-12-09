package com.example.androiddev

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.androiddev.databinding.RegistrationBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import java.io.File

class RegistrationActivity : AppCompatActivity() {
    private val viewModel by viewModels<RegistrationViewModel> {
        RegistrationViewModelFactory(getExternalFilesDir(null))
    }
    private val viewBinding by viewBinding(RegistrationBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration)

        viewBinding.saveRegistrationButton.setOnClickListener {
            viewModel.onSave()
        }

    }

}

