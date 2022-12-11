package com.example.androiddev


import android.util.Log
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

fun main() {
//    val client = KMongo.createClient(get_mongo_connection_string())
//    val database = client.getDatabase("NoteApp")
//
//    val users_coll = database.getCollection("users")
//
//    runBlocking {
//        val user = users_coll.findOne("{\"name\" : \"sld;f\"}")
//        users_coll.insertOne(Document("name", "third"))
//        println(user)
//    }
//    println(isConnectedToThisServer("192.168.1.20:49168/"))

    val conn = runBlocking {
        val conn = async {
            try {
                val conn = DriverManager.getConnection(
                    "jdbc:mariadb://${MySQLConnectionInfo().host}/${MySQLConnectionInfo().database}",
                    MySQLConnectionInfo().user, MySQLConnectionInfo().pass
                )

                return@async conn
            } catch (e: SQLException) {
                Log.d("Unable to init conection to db", e.toString())
                return@async null
            }

        }
        return@runBlocking conn.await()
    }

    print(conn)

    isConnectedToThisServer("google.com")

    sendGet()

    return
}

fun isConnectedToThisServer(host: String): Boolean {
    val runtime = Runtime.getRuntime();
    try {
        val ipProcess = runtime.exec("ping -c 1 -w 1 $host")
        val exitValue = ipProcess.waitFor();
        return exitValue == 0
    } catch (e: IOException) {
        e.printStackTrace();
    } catch (e: InterruptedException) {
        e.printStackTrace();
    }
    return false;
}

fun sendGet() {
    val url = URL("jdbc:mariadb://178.251.137.54:3333/note_app")

    with(url.openConnection() as HttpURLConnection) {
        requestMethod = "GET"  // optional default is GET

        println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")

        inputStream.bufferedReader().use {
            it.lines().forEach { line ->
                println(line)
            }
        }
    }
}