package com.example.androiddev

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.sql.Date


@Parcelize
class Note (
    val owner_name : String,
    val name : String,
    val content : String,
    val last_update_date : Date = Date(0),
    val other_user_name : String = ""
) : Parcelable