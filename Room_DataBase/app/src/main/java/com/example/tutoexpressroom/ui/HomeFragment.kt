package com.example.tutoexpressroom.ui

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.tutoexpressroom.R
import com.example.tutoexpressroom.application.MensajeApplication
import com.example.tutoexpressroom.databinding.FragmentHomeBinding
import com.example.tutoexpressroom.model.Mensaje
import com.example.tutoexpressroom.viewmodel.MensajeModelFactory
import com.example.tutoexpressroom.viewmodel.MensajeViewModel


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var application: Application

    private val viewModel by viewModels<MensajeViewModel> {
        MensajeModelFactory((application as MensajeApplication).repositorio)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application =  requireActivity().application

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val boton = binding.button

        boton.setOnClickListener {
            val et = binding.etmensaje.text.toString()


            val mensaje = Mensaje(mensaje = et)
            viewModel.agregarMensaje(mensaje)

            Toast.makeText(requireContext(), "Mensaje Agregado", Toast.LENGTH_SHORT).show()
        }

        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_listFragment)
        }

        return binding.root
    }


}