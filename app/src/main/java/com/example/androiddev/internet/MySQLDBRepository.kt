package com.example.androiddev.internet

import android.util.Log
import com.example.androiddev.EventRes
import com.example.androiddev.MySQLConnectionInfo
import com.example.androiddev.entities.Note
import kotlinx.coroutines.*
import java.sql.SQLException
import java.sql.DriverManager
import java.sql.Connection
import java.sql.Date

class MySQLDBRepository() {

    private suspend fun getConnection() : Connection? {
        return coroutineScope {
            val conn : Deferred<Connection?> = async(Dispatchers.IO) {
                var conn: Connection? = null
                try {
                    conn = DriverManager.getConnection(
                        "jdbc:mariadb://${MySQLConnectionInfo().host}/${MySQLConnectionInfo().database}",
                        MySQLConnectionInfo().user, MySQLConnectionInfo().pass
                    )
                } catch (e: SQLException) {
                    Log.d("Unable to init connection to db", e.toString())
                }
                return@async conn
            }
            return@coroutineScope conn.await()
        }
    }

    suspend fun addUser(username: String) : EventRes {

        return coroutineScope {
            val res: Deferred<EventRes> = async {

                val connection = getConnection() ?: return@async EventRes(1, "DataBase connection error. Please check your internet connection")

                try {
                    val resultSet = connection.prepareStatement("SELECT name FROM users WHERE name = \"${username}\"").executeQuery()

                    if (resultSet!!.first()) {
                        return@async EventRes(
                            1,
                            "This user name is already in use. Please try other one."
                        )
                    } else {
                        connection.prepareStatement("INSERT INTO users(name) VALUE (\"${username}\")").executeQuery()
                        return@async EventRes()
                    }
                } catch (e: SQLException) {
                    // handle any errors
                    return@async EventRes (
                        3,
                        e.toString()
                    )
                }
            }
            return@coroutineScope res.await()
        }
    }

    suspend fun getUserNotes(username: String) : List<Note> {

        return coroutineScope {
            val res: Deferred<List<Note>> = async {
                val notes : MutableList<Note> = mutableListOf()
                val connection = getConnection() ?: return@async emptyList()

                try {
                    val resultSet = connection.prepareStatement("SELECT id, title, content, owner_name, allowed_user_name, date " +
                            "FROM notes " +
                            "WHERE owner_name = \"${username}\" OR allowed_user_name = \"${username}\"").executeQuery()

                    while (resultSet!!.next()) {
                        val n = Note (
                            owner_name = resultSet.getString("owner_name"),
                            title = resultSet.getString("title"),
                            date = Date(resultSet.getTimestamp("date").time),
                            id = resultSet.getString("id"),
                            allowed_user_name = resultSet.getString("allowed_user_name") ?: "",
                            content = resultSet.getString("content"),
                        )

                        notes.add(n)
                    }

                    return@async notes.toList()
                } catch (e: SQLException) {
                    // handle any errors
                    e.printStackTrace()
                    return@async notes.toList()
                }
            }
            return@coroutineScope res.await()
        }
    }

    suspend fun addNote(note: Note) : EventRes {

        return coroutineScope {
            val res: Deferred<EventRes> = async {
                val connection = getConnection() ?: return@async EventRes(1, "DataBase connection error. Please check your internet connection")

                try {
                    val statement = connection.prepareStatement("INSERT INTO notes (id, title, content, owner_name) " +
                            "VALUES (${note.id}, \"${note.title}\", \"${note.content}\", \"${note.owner_name}\") ")
                    statement.executeQuery()

                    return@async  EventRes()
                } catch (e: SQLException) {
                    // handle any errors
                    e.printStackTrace()
                    return@async  EventRes(-1, e.toString())
                }
            }
            return@coroutineScope res.await()
        }
    }

    suspend fun saveNote(note: Note) : EventRes {

        return coroutineScope {
            val res: Deferred<EventRes> = async {
                val connection = getConnection() ?: return@async EventRes(1, "DataBase connection error. Please check your internet connection")

                try {
                    val statement = connection.prepareStatement("UPDATE notes SET title = \"${note.title}\", content = \"${note.content}\" " +
                            "WHERE id = \"${note.id}\"")
                    statement.executeQuery()

                    return@async  EventRes()
                } catch (e: SQLException) {
                    // handle any errors
                    e.printStackTrace()
                    return@async  EventRes(-1, e.toString())
                }
            }
            return@coroutineScope res.await()
        }
    }

    suspend fun deleteNote(note: Note) : EventRes {

        return coroutineScope {
            val res: Deferred<EventRes> = async {
                val connection = getConnection() ?: return@async EventRes(1, "DataBase connection error. Please check your internet connection")

                try {
                    val statement = connection.prepareStatement("DELETE FROM notes " +
                            "WHERE id = \"${note.id}\"")
                    statement.executeQuery()

                    return@async  EventRes()
                } catch (e: SQLException) {
                    // handle any errors
                    e.printStackTrace()
                    return@async  EventRes(-1, e.toString())
                }
            }
            return@coroutineScope res.await()
        }
    }

    suspend fun addAllowedUserToNote(note: Note) : EventRes {

        return coroutineScope {
            val res: Deferred<EventRes> = async {
                val connection = getConnection() ?: return@async EventRes(1, "DataBase connection error. Please check your internet connection")

                try {
                    val resultSet = connection.prepareStatement("SELECT name FROM users WHERE name = \"${note.allowed_user_name}\"").executeQuery()

                    if (resultSet!!.first()) {

                        val statement = connection.prepareStatement("UPDATE notes SET allowed_user_name = \"${note.allowed_user_name}\" " +
                                "WHERE id = \"${note.id}\"")
                        statement.executeQuery()

                        return@async  EventRes()

                    }
                   return@async EventRes(
                            1,
                    "There is no user with this name. Please try other one."
                    )

                } catch (e: SQLException) {
                    // handle any errors
                    e.printStackTrace()
                    return@async  EventRes(-1, e.toString())
                }
            }
            return@coroutineScope res.await()
        }
    }

}

//fun isConnectedToThisServer(host: String): Boolean {
//    val s = Socket("178.251.137.54", 3333)
//    println(s.isConnected)
//
//    val runtime = Runtime.getRuntime();
//
//    try {
//        val ipProcess = runtime.exec("/system/bin/ping -c 1 -w 1 $host")
//        val exitValue = ipProcess.waitFor();
//        return exitValue == 0
//    } catch (e: IOException) {
//        e.printStackTrace();
//    } catch (e: InterruptedException) {
//        e.printStackTrace();
//    }
//    return false;
//}
//
//fun sendGet() {
//    val url = URL("http://baobab.su")
//
//    with(url.openConnection() as HttpURLConnection) {
//        requestMethod = "GET"  // optional default is GET
//
//        println("\nSent 'GET' request to URL : $url; Response Code : $responseCode")
//
//        inputStream.bufferedReader().use {
//            it.lines().forEach { line ->
//                println(line)
//            }
//        }
//    }
//}
//
//fun main() {
//    runBlocking {
////        launch {
////            printWorld()
////        }
////        launch {
////            printRes()
////        }
////        print("Hello")
//
////        launch(Dispatchers.Default) { printThread() } // Выбор потока Default - вычисления, IO создает новые потоки (для сети его), Unconfined - не используется
////    }
//
//        val mdb = MongoDBRepository()
//        mdb.doSmth()
//        mdb.cancelAllLaunches()
//    }
//    Thread.sleep(2000)
//
//}
//
//private fun printThread() {
//    println(Thread.currentThread())
//}
//
//suspend fun printWorld() {
//    coroutineScope {
//        val job: Job = launch {
//            delay(2000)
//            print("!")
//        }
//        job.join()
//        launch {
//            delay(1000)
//            print("World")
//        }
//    }
//}
//
//suspend fun  printRes() {
//    coroutineScope {
//        val time = measureTimeMillis {
//            val res: Deferred<Int> = async {
//                delay(1000)
//                return@async 10
//            }
//            val res1: Deferred<Int> = async {
//                delay(1000)
//                20
//            }
//
//            println(res1.await() + res.await())
//        }
//        println("time $time")
//    }
//}