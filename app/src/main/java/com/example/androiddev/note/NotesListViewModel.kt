package com.example.androiddev.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddev.Event
import com.example.androiddev.EventRes
import com.example.androiddev.common.SingleLiveEvent
import com.example.androiddev.entities.Note
import com.example.androiddev.internet.MySQLDBRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException

class NotesListViewModel(private val user_name: String, private val mExternalDir: File?) : ViewModel() {

    private val _creationFinished = MutableLiveData<Event<EventRes>>()
    val creationFinished: LiveData<Event<EventRes>> = _creationFinished

    private val _creationInLocallyFinished = MutableLiveData<Event<EventRes>>()
    val creationInLocallyFinished: LiveData<Event<EventRes>> = _creationInLocallyFinished

    private val _updatingFinished = MutableLiveData<Event<EventRes>>()
    val updatingFinished: LiveData<Event<EventRes>> = _updatingFinished

    private var _notes = MutableLiveData<List<Note>>()
    val notes :  LiveData<List<Note>> = _notes

    private val _openNote = SingleLiveEvent<Note>()
    val openNote: LiveData<Note> = _openNote

    init {
        viewModelScope.launch(Dispatchers.IO) {
            update_notes()
        }
    }

    fun update_notes() {
        viewModelScope.launch(Dispatchers.IO) {
            val repo = MySQLDBRepository()
            val notes = repo.getUserNotes(user_name).toMutableList()

            _updatingFinished.postValue(Event(repo.last_res))

//            if (notes.size == 0) {
//                _notes.postValue(getAllNotesFromLocalStorage())
//                return@launch
//            }

            val local_notes = getAllNotesFromLocalStorage()
            for (local_note in local_notes) {
                var is_in_online = false
                for (note in notes) {
                    if (note.id == local_note.id) {
                        is_in_online = true
                        break
                    }
                }
                if (!is_in_online) {
                    repo.addNote(local_note)
                    notes.add(local_note)
                }
            }

            _notes.postValue(notes.toList())
        }
    }

    fun onNoteSelected(note : Note) {
        _openNote.postValue(note)
    }

    fun createNewNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = viewModelScope.async(Dispatchers.IO) {
                MySQLDBRepository().addNote(note)
            }

            val r = res.await()
            _creationFinished.postValue(Event(r))

            if (r.res == 0)
                putNewNoteToLocalStorage(note)

        }
    }

    private fun getAllNotesFromLocalStorage() : List<Note> {
        val dataFile = File(mExternalDir,"appdata.json")

        val notes : MutableList<Note> = mutableListOf()

        try {
            if (!dataFile.exists()) {
                if (!dataFile.createNewFile()) {
                    _creationFinished.postValue(Event(EventRes(2, "Unable to work with file. Please contact the developer")))
                    return emptyList()
                }
            }
        } catch (e: IOException) {
            _creationFinished.postValue(Event(EventRes(2, "Unable to work with file. Please contact the developer")))
            return emptyList()
        }

        try {
            var json = JSONObject()
            if (dataFile.readText() != "")
                json = JSONObject(dataFile.readText())
            val gson = Gson()

            for (id in json.keys()) {
                val n_json = JSONObject(json.getString(id))
                val n = Note(
                    n_json.getString("owner_name"),
                    n_json.getString("id"),
                    n_json.getString("title"),
                    n_json.getString("content"),
                    allowed_user_name = n_json.getString("allowed_user_name")
                )
                notes.add(n)
            }
        }
        catch (e: JSONException) {
            e.printStackTrace()
            _creationFinished.postValue(Event(EventRes(-1, e.stackTraceToString())))
            return emptyList()
        } catch (e: IOException) {
            _creationFinished.postValue(Event(EventRes(2, "Unable to work with file. Please contact the developer")))
            e.printStackTrace()
            return emptyList()
        }

        return notes.toList()

    }

    private fun putNewNoteToLocalStorage(note: Note) : Boolean {
        val dataFile = File(mExternalDir,"appdata.json")

        try {
            if (!dataFile.exists()) {
                if (!dataFile.createNewFile()) {
                    _creationFinished.postValue(Event(EventRes(2, "Unable to work with file. Please contact the developer")))
                    return false
                }
            }
        } catch (e: IOException) {
            _creationFinished.postValue(Event(EventRes(2, "Unable to work with file. Please contact the developer")))
            return false
        }

        try {
            var json = JSONObject()
            if (dataFile.readText() != "")
                json = JSONObject(dataFile.readText())
            val gson = Gson()
            json.put(note.id, gson.toJson(note))
            dataFile.writeText(json.toString())
        }
        catch (e: JSONException) {
            e.printStackTrace()
            _creationFinished.postValue(Event(EventRes(-1, e.stackTraceToString())))
            return false
        } catch (e: IOException) {
            _creationFinished.postValue(Event(EventRes(2, "Unable to work with file. Please contact the developer")))
            e.printStackTrace()
            return false
        }

        return true

    }

}

