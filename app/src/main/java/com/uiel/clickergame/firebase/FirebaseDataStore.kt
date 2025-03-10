package com.uiel.clickergame.firebase

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.uiel.clickergame.util.getDeviceUuid

class FirebaseDataStore {
    val database = FirebaseDatabase.getInstance()
    val rankingRef = database.getReference("rankings")
    val userRef = database.getReference("users")

    fun fetchRankings() {
        rankingRef.orderByChild("score").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.reversed().forEach {
                    val userName = it.child("username").getValue(String::class.java)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Failed to read rankings: ${error.message}")
            }
        })
    }

    fun initData() {
        userRef.child(getDeviceUuid() ?: "").get().addOnSuccessListener {
            Log.d("initData",it.value.toString())
        }.addOnFailureListener {
            Log.d("initData",it.message.toString())
        }
    }
}