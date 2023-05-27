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

data class newUser(val uid: String, val name: String, val time: List<String>?, val master: Boolean)
data class newMeeting(val id: String, val name: String, val date: List<String>, val creator: String, val done: Boolean)

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
    returnUserName()
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
    //println("uSch: $uSch")
    val uSchInt = uSch.map { it.split(",").map { num -> num.toInt() }}
    //println("uSchInt: $uSchInt")

    var day = 1
    for (i in uSchInt) {
       var time = 8
       for (j in i) {
            if (j == 1) schedule.add(Pair(day, time))
            time += 1
        }
        day += 1
    }
    //println("schedule: $schedule")
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

// 유저 미팅 리스트
suspend fun returnUserMeeting(): List<String>{
    val uid = getUid()

    val meetingList = ArrayList<String>()

    return withContext(Dispatchers.IO) {
        val ref = usersRef.child(uid).child("meeting")
        val snapshot = ref.get().await()

        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                val meeting = childSnapshot.value.toString()
                meetingList.add(meeting)
            }
        } else {
            ""
        }

        meetingList
    }
}
fun getUserMeeting(): List<String> = runBlocking {
    returnUserMeeting()
}

// 미팅 정보
suspend fun returnMeeting(code: String): Meeting{
    var meeting = Meeting(code, "", "", false, false)

    val ref = meetingsRef.child(code)
    return withContext(Dispatchers.IO) {
        val nameRef = ref.child("name")
        val nameSnap = nameRef.get().await()
        if (nameSnap.exists()) {
            meeting.name = nameSnap.value.toString()
        } else {
            ""
        }

        val creatorRef = ref.child("creator")
        val creatorSnap = creatorRef.get().await()
        if (creatorSnap.exists()) {
            meeting.creator = creatorSnap.value.toString()
        } else {
            ""
        }

        val masterRef = ref.child("user").child(getUid()).child("master")
        val masterSnap = masterRef.get().await()
        if (masterSnap.exists()) {
            meeting.isMaster = masterSnap.value as Boolean
        } else {
            ""
        }

        val doneRef = ref.child("done")
        val doneSnap = doneRef.get().await()
        if (doneSnap.exists()) {
            meeting.isDone = doneSnap.value as Boolean
        } else {
            ""
        }

        meeting
    }
}
fun getMeeting(code: String): Meeting = runBlocking {
    returnMeeting(code)
}

// 미팅 날짜
suspend fun returnMeetingDate(code: String): List<String>{
    val meetingDate = ArrayList<String>()

    return withContext(Dispatchers.IO) {
        val ref = meetingsRef.child(code).child("date")
        val snapshot = ref.get().await()

        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                val date = childSnapshot.value.toString()
                meetingDate.add(date)
            }
        } else {
            ""
        }

        meetingDate
    }
}
fun getMeetingDate(code: String): List<String> = runBlocking {
    returnMeetingDate(code)
}