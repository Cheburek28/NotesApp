package com.example.androiddev

import androidx.lifecycle.*
import com.example.androiddev.entities.User
import com.example.androiddev.internet.MySQLDBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.*


class RegistrationViewModelFactory(private val mExternalDir: File?): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = RegistrationViewModel(mExternalDir) as T
}

class RegistrationViewModel(private val mExternalDir : File?) : ViewModel() {

    private val _registrationFinished = MutableLiveData<Event<EventRes>>()
    val registrationFinished: LiveData<Event<EventRes>> = _registrationFinished

    fun onSave(user: User) {

        val settingsFile = File(mExternalDir,".settings")

        try {
            if (!settingsFile.exists()) {
                if (!settingsFile.createNewFile()) {
                    _registrationFinished.postValue(Event(EventRes(2, "Unable to work with file. Please contact the developer")))
                    return
                }
            }
        } catch (e: IOException) {
            _registrationFinished.postValue(Event(EventRes(2, "Unable to work with file. Please contact the developer")))
            return
        }

        val json = JSONObject()

        try {
            json.put("name", user.get_nickname())
            json.put("password", user.get_password())
//            json.put("offices", listOf("California", "Washington", "Virginia"))
        } catch (e: JSONException) {
            e.printStackTrace()
            _registrationFinished.postValue(Event(EventRes(-1, e.stackTraceToString())))
            return
        }

        try {
            settingsFile.writeText(json.toString())
        } catch (e: IOException) {
            _registrationFinished.postValue(Event(EventRes(2, "Unable to work with file. Please contact the developer")))
            e.printStackTrace()
            return
        }

        // Сохраняем в БД
        viewModelScope.launch(Dispatchers.IO) {
            val res = viewModelScope.async(Dispatchers.IO) {
                return@async MySQLDBRepository().addUser(user.get_nickname())
            }
            _registrationFinished.postValue(Event(res.await()))
        }
    }

}

//fun isConnectedToThisServer(host: String): Boolean {
//    val runtime = Runtime.getRuntime();
//    try {
//        val ipProcess = runtime.exec("/system/bin/ping ping -c 1 -w 1 google.com")
//        val stdIn = ipProcess.inputStream
//        val isr = InputStreamReader(stdIn)
//        val br = BufferedReader(isr)
//
//        var line: String? = null
//        println("<OUTPUT>")
//
//        while (br.readLine().also { line = it } != null) println(line)
//
//        println("</OUTPUT>")
//        val exitVal: Int = ipProcess.waitFor()
//        println("Process exitValue: $exitVal")
//
//
//        return exitVal == 0
//    } catch (e: IOException) {
//        e.printStackTrace();
//    } catch (e: InterruptedException) {
//        e.printStackTrace();
//    }
//    return false;
//}
//
//class Ping {
//    var net: String? = "NO_CONNECTION"
//    var host = ""
//    var ip = ""
//    var dns = Int.MAX_VALUE
//    var cnt = Int.MAX_VALUE
//}


//val client = KMongo.createClient(get_mongo_connection_string())
//val session = client.startSession()
//val database = client.getDatabase("NoteApp")
//
//val users_coll = database.getCollection("users")
//val user = users_coll.findOne(session, "{\"name\" : \"first\"}")
//users_coll.insertOne(Document("name", "third"))