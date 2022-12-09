package com.example.androiddev

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val externalDir = getExternalFilesDir(null)

        val settings_file = File(externalDir,".setiings")

        settings_file.delete()

        var isCreated = false

        try {
            isCreated = settings_file.exists()
        } catch (e: IOException) {
            Log.e("Exception", "File check failed: $e");
        }

        if (!isCreated) {
            openRegistration()
//                .apply {
//                    putExtra()
//                } // Для добавления допаргументов
        }
    }


    private fun openRegistration() {
        startActivity(Intent(this, RegistrationActivity::class.java))
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