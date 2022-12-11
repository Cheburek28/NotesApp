package com.example.androiddev

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.androiddev.databinding.ActivityMainBinding
import com.example.androiddev.note.NoteEditActivity
import com.example.androiddev.note.NotesListActivity
import com.example.androiddev.registration.RegistrationActivity
import org.json.JSONObject
import java.io.File

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
        } else {
            if ( settingsFile.readText() == "" )
                openRegistration()
        }
    }

    private fun passwordReady() {
        val settingsFile = File(getExternalFilesDir(null),".settings")
        val json : JSONObject = JSONObject(settingsFile.readText())
        val password = viewBinding.editTextNumberPassword.text.toString()

        if (password == json.get("password").toString()) {
            openNotesList()
        } else {
            Toast.makeText(
                applicationContext,
                "Incorrect PIN, please try again!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun openRegistration() {
        startActivity(Intent(this, RegistrationActivity::class.java))
    }

    private fun openNotesList() {
        val settingsFile = File(getExternalFilesDir(null),".settings")
        val json = JSONObject(settingsFile.readText())

//        startActivity(Intent(this, NotesListActivity::class.java).apply {
//            putExtra(NotesListActivity.USER_DETAIL_ARGUMENT_KEY, json.get("name").toString())
//        })

        val intent = Intent(this@MainActivity,NotesListActivity::class.java)
        intent.putExtra(NotesListActivity.USER_DETAIL_ARGUMENT_KEY, json.get("name").toString())
        startActivity(intent)
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