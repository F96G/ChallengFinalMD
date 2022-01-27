package com.munidigital.bc2201.challengfinal.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.munidigital.bc2201.challengfinal.R

class RegistroFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_registro, container, false)

        auth = Firebase.auth
        val viewModel = ViewModelProvider(requireActivity()).get(RegistroViewModel::class.java)


        viewModel.estado.observe(requireActivity()){
                estado->
            when {
                (!estado.fallo && estado.user != null) -> { // Usuario registrado
                    findNavController().popBackStack()
                }
                (estado.fallo) -> { // El usuario intentó iniciar sesión y falló
                    Toast.makeText(requireActivity(), "Error al registrar.", Toast.LENGTH_SHORT).show()
                }
            }
        }


        val etMail = rootView.findViewById<EditText>(R.id.et_nuevoEail)
        val etPass = rootView.findViewById<EditText>(R.id.et_nuevaPass)
        val etRepetirPass = rootView.findViewById<EditText>(R.id.et_repetirPass)
        val btnRegistrar = rootView.findViewById<Button>(R.id.btn_registrar)

        btnRegistrar.setOnClickListener {
            val email = etMail.text.toString()
            val password = etPass.text.toString()
            val repPasswrd = etRepetirPass.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && repPasswrd.isNotEmpty()){
                if (password == repPasswrd){
                    viewModel.registrar(auth,email, password)
                }else{
                    Toast.makeText(requireActivity(), "Contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(requireActivity(), "Ingrese un usuario y contraseña", Toast.LENGTH_SHORT).show()
            }
        }


        return rootView
    }

}