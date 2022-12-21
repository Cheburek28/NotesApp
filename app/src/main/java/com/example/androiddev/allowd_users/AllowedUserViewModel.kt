package com.example.androiddev.allowd_users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androiddev.Event
import com.example.androiddev.EventRes
import com.example.androiddev.entities.Note
import com.example.androiddev.internet.MySQLDBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AllowedUserViewModel : ViewModel() {

    private val _additingFinished = MutableLiveData<Event<EventRes>>()
    val additingFinished: LiveData<Event<EventRes>> = _additingFinished

    fun addAllowedUser(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = MySQLDBRepository().addAllowedUserToNote(note) // updating only on server because it is not possible info for local storage

            _additingFinished.postValue(Event(res))
        }
    }

}