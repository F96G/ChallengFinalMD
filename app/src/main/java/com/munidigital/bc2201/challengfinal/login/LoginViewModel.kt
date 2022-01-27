package com.munidigital.bc2201.challengfinal.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginViewModel(app : Application) : AndroidViewModel(app) {

    private var auth: FirebaseAuth = Firebase.auth

    private val _state = MutableLiveData(State(false, null))

    val state: LiveData<State> get() = _state

    data class State(
        val loginError: Boolean,
        val user: FirebaseUser?
    )

    init {
        val user = auth.currentUser
        if (user != null) {
            _state.value = (State(
                loginError = false,
                user = user
            ))
        }
    }


    fun login(mail: String, pass: String): Boolean {
        if (mail.isNotBlank() && pass.isNotBlank()) {
            Log.d("login", "sending")
            auth.signInWithEmailAndPassword(mail, pass)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Logueo correcto, cambio el estado
                        _state.value = State(
                            false,
                            auth.currentUser
                        )
                        Log.d("login", "success")
                    } else {
                        // Logueo incorrecto, cambio el estado
                        _state.value = State(
                            true,
                            null
                        )
                        Log.d("login", "failure", task.exception)
                    }
                }
            return true
        } else {
            return false
        }
    }

    fun logout() {
        auth.signOut()
        _state.value = State(
            false,
            null
        )
    }
}
