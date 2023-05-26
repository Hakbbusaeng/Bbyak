package com.example.bbyak

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

lateinit var auth: FirebaseAuth
val user = Firebase.auth.currentUser
val database = Firebase.database
val usersRef = database.getReference("Users")
val meetingsRef = database.getReference("Meetings")

fun getUid(): String {
    return user?.uid.toString()
}

// 유저 이름
suspend fun returnUserName(): String {
    val uid = getUid()

    return withContext(Dispatchers.IO) {
        val ref = usersRef.child(uid).child("name")
        val snapshot = ref.get().await()
        if (snapshot.exists()) {
            snapshot.value.toString()
        } else {
            ""
        }
    }
}
fun getUserName(): String = runBlocking {
    val uName = returnUserName()
    uName
}

// 유저 스케줄
suspend fun returnUserSchedule(): List<String> {
    val uid = getUid()

    return withContext(Dispatchers.IO) {
        val ref = usersRef.child(uid).child("schedule")
        val snapshot = ref.get().await()

        val uSch = ArrayList<String>()

        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                val sch = childSnapshot.value.toString()
                uSch.add(sch)
            }
        } else {
            ""
        }

        uSch

    }
}
fun getUserSchedule(): ArrayList<Pair<Int, Int>> = runBlocking {
    val schedule = ArrayList<Pair<Int, Int>>()

    val uSch = returnUserSchedule()
    println("uSch: $uSch")
    val uSchInt = uSch.map { it.split(",").map { num -> num.toInt() }}
    println("uSchInt: $uSchInt")

   for (i in uSchInt) {
        var day = 1
        for (j in i) {
            var time = 8
            if (j == 1) schedule.add(Pair(day, time))
            time++
        }
        day++
    }
    println("schedule: $schedule")
    schedule
}
fun returnSelectedTime(selectedTime: ArrayList<Pair<Int, Int>>): ArrayList<String> {
    val arrSelectedTime = MutableList(7){ mutableListOf(0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0) }

    for (i in selectedTime) {
        arrSelectedTime[i.first-1][i.second-8] = 1
    }

    val schedule = ArrayList<String>()
    for (i in arrSelectedTime) {
        schedule.add(i.joinToString(",") { it.toString() })
    }

    return schedule
}

