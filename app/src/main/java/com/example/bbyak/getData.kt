package com.example.bbyak

import android.util.Log
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

private val user = Firebase.auth.currentUser
private val database = Firebase.database

fun getUid(): String {
    return user?.uid.toString()
}

suspend fun returnUserName(): String {
    val uid = getUid()

    return withContext(Dispatchers.IO) {
        val ref = database.getReference("Users").child(uid).child("name")
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