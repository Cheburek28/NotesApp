package com.example.androiddev.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date
import java.time.LocalDate
import java.time.ZoneId


@Parcelize
class Note(
    val owner_name: String,
    val id: String = System.currentTimeMillis().toString(),
    var title: String = "",
    var content: String = "",
    val date: Date = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) as Date,
    var allowed_user_name: String = ""
) : Parcelable