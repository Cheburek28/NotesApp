package com.example.androiddev

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException


class RegistrationViewModelFactory(private val mExternalDir: File?): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = RegistrationViewModel(mExternalDir) as T
}

class RegistrationViewModel(private val mExternalDir : File?) : ViewModel () {

    private val _registrationFinished = MutableLiveData<Event<Boolean>>()
    val registrationFinished: LiveData<Event<Boolean>> = _registrationFinished

    fun onSave(user: User) {
        // Созраняем файл
        val settingsFile = File(mExternalDir,".settings")

        try {
            if (!settingsFile.exists()) {
                if (!settingsFile.createNewFile()) {
                    Log.e(
                        this::class.qualifiedName ?: "",
                        "Error, Can't create settings file, please contact the developer!"
                    )
                    return
                }
            }
        } catch (e: IOException) {
            Log.e(
                this::class.qualifiedName ?: "",
                e.toString()
            )
            return
        }

        val json = JSONObject()

        try {
            json.put("name", user.get_nickname())
            json.put("password", user.get_password())
//            json.put("offices", listOf("California", "Washington", "Virginia"))
        } catch (e: JSONException) {
            e.printStackTrace()
            return
        }

        try {
            settingsFile.writeText(json.toString())
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }

        if (settingsFile.exists())
            _registrationFinished.postValue(Event(true))

    }

}