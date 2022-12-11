package com.example.androiddev.internet

import android.util.Log
import com.example.androiddev.EventRes
import com.example.androiddev.MySQLConnectionInfo
import kotlinx.coroutines.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.Socket
import java.net.SocketAddress
import java.net.URL


import java.sql.SQLException
import java.sql.DriverManager
import java.sql.Connection
import java.util.concurrent.TimeoutException

class MySQLDBRepository() {

    suspend fun getConnection() : Connection? {
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
                try {
                    val connection = getConnection() ?: return@async EventRes(-1)

                    val result =
                        connection.prepareStatement("SELECT name FROM users WHERE name = \"${username}\"").executeQuery()

                    return@async EventRes(-1)
//                    val session = client.startSession()
//                    val dbUser =
//                        usersColl.findOne("{\"name\" : \"${username}\"}")
//
//                    if (dbUser != null) {
//                        return@async EventRes(
//                            1,
//                            "This user name is already in use. Please try other one."
//                        )
//                    } else {
//                        usersColl.insertOne(Document("name", username))
//                        return@async EventRes()
//                }
                } catch (e: TimeoutException) {
                    return@async EventRes(
                        3,
                        "Unable to connect to database. Please check you internet connection!"
                    )
                } catch (e: Exception) {
                    return@async (EventRes(-1, e.stackTraceToString()))
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