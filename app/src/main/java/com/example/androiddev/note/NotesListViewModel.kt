package com.example.androiddev.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddev.entities.Note
import com.example.androiddev.common.SingleLiveEvent

class NotesListViewModel() : ViewModel() {

    private val _notes = MutableLiveData(listOf<Note>(
        Note("nick", "офвлjdfkavjkfkvbdfkslbsfkvbjfkvbsdkvdfjv", "sdfvser"),
        Note("nick", "ывсфывсы", "serfresfwefsdegsefvfe5gsbrtgbs"),
        Note("nick", "sdfvsdfv", "erbgb tggdrvgrgrdtg bb thyunjuyj ur "),
        Note("nick", "dsfv", "erg tgevrgrtgtyhytc by ebbt"),
        Note("nick", "sdfv", "rtevrteg"),
        Note("nick", "sdfvdf", "ervtgrgct"),
        Note("nick", "asdfa", "ertgcrtgc"),
        Note("nick", "fghdfgh", "ercgrdfsdfx"),
        Note("nick", "dfghfgd", "cgtrghyer"),
        Note("nick", "aerf", "crtgertgcr"),
        Note("nick", "sdfg", "cgertgrsdsd"),
        Note("nick", "sdfgdsgsdv", "ckgkdklfgskdl;glk"),
        Note("nick", "sdvfdve", "dfglksdgj;sdj'gitoi"),
    ))

    val notes :  LiveData<List<Note>> = _notes

    private val _openNote = SingleLiveEvent<Note>()
    val openNote: LiveData<Note> = _openNote

    fun onNoteSelected(note : Note) {
        _openNote.postValue(note)
    }

}

