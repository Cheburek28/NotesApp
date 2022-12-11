package com.example.androiddev.note

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddev.Event
import com.example.androiddev.EventRes
import com.example.androiddev.entities.Note
import com.example.androiddev.internet.MySQLDBRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class NoteEditViewModel : ViewModel() {

    private val _savingFinished = MutableLiveData<Event<EventRes>>()
    val savingFinished: LiveData<Event<EventRes>> = _savingFinished

    fun saveNote(note: Note, externalDir: File?) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = MySQLDBRepository().saveNote(note)

            if (res.res != 0)
                _savingFinished.postValue(Event(res))
            else {
                if (saveNoteToFile(note, externalDir))
                    _savingFinished.postValue(Event(res))
                else {
                    _savingFinished.postValue(Event(EventRes(-1)))
                }
            }
        }
    }

    private fun saveNoteToFile(note: Note, externalDir: File?) : Boolean {

        val dataFile = File(externalDir,"appdata.json")

        try {
            if (!dataFile.exists()) {
                if (!dataFile.createNewFile()) {
                    _savingFinished.postValue(Event(EventRes(2, "Unable to work with file. Please contact the developer")))
                    return false
                }
            }
        } catch (e: IOException) {
            _savingFinished.postValue(Event(EventRes(2, "Unable to work with file. Please contact the developer")))
            return false
        }

        try {
            var json = JSONObject()
            if (dataFile.readText() != "")
                json = JSONObject(dataFile.readText())
            val gson = Gson()
            if (json.has(note.id))
                json.remove(note.id)
            json.put(note.id, gson.toJson(note))
            dataFile.writeText(json.toString())
            Log.i("Data file","\n\n\n\n Current file state \n${dataFile.readText()}\n\n\n\n\n\n\n")
        }
        catch (e: JSONException) {
            e.printStackTrace()
            _savingFinished.postValue(Event(EventRes(-1, e.stackTraceToString())))
            return false
        } catch (e: IOException) {
            _savingFinished.postValue(Event(EventRes(2, "Unable to work with file. Please contact the developer")))
            e.printStackTrace()
            return false
        }

        return true


    }


}