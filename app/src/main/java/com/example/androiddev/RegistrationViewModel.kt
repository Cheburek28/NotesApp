package com.example.androiddev

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.io.File

class RegistrationViewModelFactory(private val mExternalDir: File?): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = RegistrationViewModel(mExternalDir) as T
}

class RegistrationViewModel(private val mExternalDir : File?) : ViewModel () {

    fun onSave() {
        // Созраняем файл
        val settings_file = File(mExternalDir, ".settings")

        val isCreated = settings_file.createNewFile()

        if (!isCreated) {
            Log.i(this::class.qualifiedName ?: "", "New settings file created")
        }
    }

}