package com.munidigital.bc2201.challengfinal.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import com.munidigital.bc2201.challengfinal.main.MainActivity
import com.munidigital.bc2201.challengfinal.R

class LoginFragment : Fragment() {

    lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_login, container, false)

        // VM
        viewModel = ViewModelProvider(requireActivity()).get(LoginViewModel::class.java)

        // Views References
        val etMail = rootView.findViewById<EditText>(R.id.et_mail)
        val etPass = rootView.findViewById<EditText>(R.id.et_pass)
        val btnLogin = rootView.findViewById<Button>(R.id.btn_login)
        val btnRegistro = rootView.findViewById<Button>(R.id.btn_Registrar)


        viewModel.state.observe(requireActivity()) {
                state ->
            when {
                (!state.loginError && state.user != null) -> { // Usuario logueado
                    onLogged(state.user) // Loguea la info del usuario...
                    val intent = Intent(requireActivity(), MainActivity::class.java )
                    startActivity(intent)
                    requireActivity().finish()
                }
                (state.loginError) -> { // El usuario intentó iniciar sesión y falló
                    Toast.makeText(requireActivity(), "Error al iniciar.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnLogin.setOnClickListener {
                val mail = etMail.text.toString()
                val pass = etPass.text.toString()
                val enviado = viewModel.login(mail, pass)
                if (!enviado) Toast.makeText(requireActivity(), "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
        }

        btnRegistro.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegistroFragment()
            findNavController().navigate(action)
        }

        return rootView
    }

    private fun onLogged(user: FirebaseUser) {
        user.apply {
            email?.let { Log.d("login", it) }
            isEmailVerified.let { Log.d("login", it.toString()) }
            uid.let { Log.d("login", it) }
        }
    }
}