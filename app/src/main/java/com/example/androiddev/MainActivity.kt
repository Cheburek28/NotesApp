package com.example.androiddev

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddev.databinding.ActivityMainBinding
import org.json.JSONObject
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewBinding.signinButton.setOnClickListener { passwordReady() }
    }

    override fun onStart() {
        super.onStart()
        val settingsFile = File(getExternalFilesDir(null),".settings")

        if (!settingsFile.exists()) {
            openRegistration()
        }
    }

    private fun passwordReady() {
        val settingsFile = File(getExternalFilesDir(null),".settings")
        val json : JSONObject = JSONObject(settingsFile.readText())
        val password = viewBinding.editTextNumberPassword.text.toString()

        if (password == json.get("password").toString()) {
            openNotesList()
        }
    }


    private fun openRegistration() {
        startActivity(Intent(this, RegistrationActivity::class.java))
    }

    private fun openNotesList() {
        startActivity(Intent(this, NotesListActivity::class.java))
    }
}

//
//abstract class BaseActivity : AppCompatActivity() { // for logging
//    private val tag: String
//        get() = this::class.qualifiedName ?: ""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        Log.d(tag, "onCreate $savedInstanceState")
//        super.onCreate(savedInstanceState)
//    }
//
//    override fun onStart() {
//        Log.d(tag, "onStart")
//        super.onStart()
//    }
//
//    override fun onResume() {
//        Log.d(tag, "onCreate")
//        super.onResume()
//    }
//
//    override fun onPause() {
//        Log.d(tag, "onPause")
//        super.onPause()
//    }
//
//    override fun onStop() {
//        Log.d(tag, "onStop")
//        super.onStop()
//    }
//
//    override fun onDestroy() {
//        Log.d(tag, "onDestroy")
//        super.onDestroy()
//    }
//
//}