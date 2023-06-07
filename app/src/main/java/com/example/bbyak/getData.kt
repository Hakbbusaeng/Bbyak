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

data class mUser(val uid: String, val name: String, val manager: Boolean)
data class uMeeting(val code: String, val time: List<String>, val submit: Boolean)
data class newMeeting(val code: String, val name: String, val date: List<String>, val creator: String, val done: Boolean)
data class confirmedTime(val year: Int, val month: Int, val day: Int, val start: Int, val end: Int)

fun getUid(): String {
    return user?.uid.toString()
}

// 유저 이름
private suspend fun returnUserName(uid: String): String {

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
fun getUserName(uid: String): String = runBlocking {
    returnUserName(uid)
}

// 유저 스케줄
private suspend fun returnUserSchedule(): List<String> {
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
fun getUserSchedule(): List<String> = runBlocking {
    returnUserSchedule()
}
fun toSelectedTime(): ArrayList<Pair<Int, Int>> {
    val selectedTime = ArrayList<Pair<Int, Int>>()

    val uSch = getUserSchedule()
    //println("uSch: $uSch")
    val uSchInt = uSch.map { it.map { digit -> digit.toString().toInt() } }
    //println("uSchInt: $uSchInt")

    var day = 1
    for (i in uSchInt) {
       var time = 8
       for (j in i) {
            if (j == 0) selectedTime.add(Pair(day, time))
            time += 1
        }
        day += 1
    }
    //println("schedule: $schedule")
    return selectedTime
}
fun toUserSchedule(selectedTime: ArrayList<Pair<Int, Int>>): ArrayList<String> {
    val arrSelectedTime = MutableList(7){ mutableListOf(1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1) }

    for (i in selectedTime) {
        arrSelectedTime[i.first-1][i.second-8] = 0
    }

    val schedule = ArrayList<String>()
    for (i in arrSelectedTime) {
        schedule.add(i.joinToString(""))
    }
    println("schedule: $schedule")
    return schedule
}

// 유저 미팅 리스트
private suspend fun returnUserMeeting(): List<String>{
    val uid = getUid()

    val meetingList = ArrayList<String>()

    return withContext(Dispatchers.IO) {
        val ref = usersRef.child(uid).child("meeting")
        val snapshot = ref.get().await()

        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                val meeting = childSnapshot.key.toString()
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
private suspend fun returnMeeting(code: String): Meeting{
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

        val managerRef = ref.child("user").child(getUid()).child("manager")
        val managerSnap = managerRef.get().await()
        if (managerSnap.exists()) {
            meeting.isManager = managerSnap.value as Boolean
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
private suspend fun returnMeetingDate(code: String): List<String>{
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
fun getMeetingDate(code: String): List<Triple<Int, Int, Int>> = runBlocking {
    val dateList = ArrayList<Triple<Int, Int, Int>>()
    val mDate = returnMeetingDate(code)

    for (d in mDate) {
        val dateParts = d.split("/")
        val year = dateParts[0].toInt()
        val month = dateParts[1].toInt()
        val day = dateParts[2].toInt()
        val date = Triple(year, month, day)

        dateList.add(date)
    }

    dateList
}

// 미팅 리스트
private suspend fun returnMeetingList(): List<String>{
    val meetingList = ArrayList<String>()

    return withContext(Dispatchers.IO) {
        val ref = meetingsRef
        val snapshot = ref.get().await()

        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                val code = childSnapshot.key.toString()
                meetingList.add(code)
            }
        } else {
            ""
        }

        meetingList
    }
}
fun getMeetingList(): List<String> = runBlocking {
    returnMeetingList()
}

// 제출한 유저 리스트
private suspend fun returnSubmitUserList(code: String): ArrayList<String>{
    val userList = ArrayList<String>()
    val submitUserList = ArrayList<String>()

    return withContext(Dispatchers.IO) {

        val mRef = meetingsRef.child(code).child("user")
        val mSnapshot = mRef.get().await()
        if (mSnapshot.exists()) {
            for (childSnapshot in mSnapshot.children) {
                val uid = childSnapshot.key.toString()
                userList.add(uid)
            }
        } else {
            ""
        }

        for (uid in userList) {
            val uRef = usersRef.child(uid).child("meeting").child(code).child("submit")
            val uSnapshot = uRef.get().await()
            if (uSnapshot.exists()) {
                val submit = uSnapshot.value as Boolean
                if (submit) submitUserList.add(uid)
            } else {
                ""
            }
        }

        submitUserList
    }
}
fun getSubmitUserList(code: String): ArrayList<String> = runBlocking {
    returnSubmitUserList(code)
}

// 유저 미팅 시간
private suspend fun returnUserTime(uid: String, code: String): List<String>{
    val timeList = ArrayList<String>()

    return withContext(Dispatchers.IO) {
        val ref = usersRef.child(uid).child("meeting").child(code).child("time")
        val snapshot = ref.get().await()

        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                val time = childSnapshot.value.toString()
                timeList.add(time)
            }
        } else {
            ""
        }

        timeList
    }
}
fun getUserTime(uid: String, code: String): List<String> = runBlocking {
    returnUserTime(uid, code)
}

// 유저 미팅 스케줄 제출 여부
private suspend fun returnUserSubmit(uid: String, code: String): Boolean {

    return withContext(Dispatchers.IO) {
        val ref = usersRef.child(uid).child("meeting").child(code).child("submit")
        val snapshot = ref.get().await()

        if (snapshot.exists()) {
            val submit = snapshot.value
            submit == true
        } else {
            false
        }
    }
}
fun getUserSubmit(uid: String, code: String): Boolean = runBlocking {
    returnUserSubmit(uid, code)
}

// 유저 확정된 뺙 정보
private suspend fun returnUserBbyak(uid: String): List<MyMeeting> {
    val bbyakList = ArrayList<MyMeeting>()
    val meetingList = ArrayList<String>()

    return withContext(Dispatchers.IO) {
        val ref = usersRef.child(uid).child("bbyak")
        val snapshot = ref.get().await()

        if (snapshot.exists()) {
            for (childSnapshot in snapshot.children) {
                val code = childSnapshot.key.toString()
                meetingList.add(code)
            }
        } else {
            false
        }

        for (code in meetingList) {
            var year = 0
            var month = 0
            var day = 0
            var start = 0
            var end = 0
            var name = ""
            var creator = ""
            val partUid = getSubmitUserList(code)
            val part = ArrayList<String>()

            for (uid in partUid) {
                part.add(getUserName(uid))
            }

            val yRef = usersRef.child(uid).child("bbyak").child(code).child("year")
            val ySnapshot = yRef.get().await()
            if (ySnapshot.exists()) {
                year = ySnapshot.value.toString().toInt()
            } else {
                ""
            }

            val mRef = usersRef.child(uid).child("bbyak").child(code).child("month")
            val mSnapshot = mRef.get().await()
            if (mSnapshot.exists()) {
                month = mSnapshot.value.toString().toInt()
            } else {
                ""
            }


            val dRef = usersRef.child(uid).child("bbyak").child(code).child("day")
            val dSnapshot = dRef.get().await()
            if (dSnapshot.exists()) {
                day = dSnapshot.value.toString().toInt()
            } else {
                ""
            }


            val sRef = usersRef.child(uid).child("bbyak").child(code).child("start")
            val sSnapshot = sRef.get().await()
            if (sSnapshot.exists()) {
                start = sSnapshot.value.toString().toInt()
            } else {
                ""
            }

            val eRef = usersRef.child(uid).child("bbyak").child(code).child("end")
            val eSnapshot = eRef.get().await()
            if (eSnapshot.exists()) {
                end = eSnapshot.value.toString().toInt()
            } else {
                ""
            }

            val nRef = meetingsRef.child(code).child("name")
            val nSnapshot = nRef.get().await()
            if (nSnapshot.exists()) {
                name = nSnapshot.value.toString()
            } else {
                ""
            }

            val cRef = meetingsRef.child(code).child("creator")
            val cSnapshot = cRef.get().await()
            if (cSnapshot.exists()) {
                creator = nSnapshot.value.toString()
            } else {
                ""
            }

            bbyakList.add(MyMeeting(year, month-1, day, name, creator, start, end, part))
        }

        bbyakList
    }
}
fun getUserBbyak(uid: String): List<MyMeeting> = runBlocking {
    returnUserBbyak(uid)
}
