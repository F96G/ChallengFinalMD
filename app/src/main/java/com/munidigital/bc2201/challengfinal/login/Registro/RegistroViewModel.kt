package com.munidigital.bc2201.challengfinal.login.Registro

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegistroViewModel(app : Application): AndroidViewModel(app) {
    private val _Estado = MutableLiveData(Estado(false, null))
    val estado : LiveData<Estado> = _Estado


    data class Estado(
        val fallo: Boolean,
        val user: FirebaseUser?
    )

    fun registrar(auth: FirebaseAuth, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    _Estado.value = Estado(
                        false,
                        auth.currentUser)

                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    _Estado.value = Estado(
                        true,
                        null)
                }
            }
    }


}