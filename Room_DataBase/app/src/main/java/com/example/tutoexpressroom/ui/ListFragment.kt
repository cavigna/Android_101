package com.example.tutoexpressroom.ui

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tutoexpressroom.R
import com.example.tutoexpressroom.application.MensajeApplication
import com.example.tutoexpressroom.databinding.FragmentListBinding
import com.example.tutoexpressroom.listadapter.MensajeListAdapter
import com.example.tutoexpressroom.model.Mensaje
import com.example.tutoexpressroom.viewmodel.MensajeModelFactory
import com.example.tutoexpressroom.viewmodel.MensajeViewModel


class ListFragment : Fragment(), MensajeListAdapter.MiExtractor {
    private lateinit var binding: FragmentListBinding
    private lateinit var application: Application

    private val viewModel by viewModels<MensajeViewModel> {
        MensajeModelFactory((application as MensajeApplication).repositorio)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = requireActivity().application

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(layoutInflater, container, false)

        val recyclerView = binding.recyclerView
        val adapter = MensajeListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        viewModel.listadoMensaje.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })


        return binding.root

    }

    override fun extraerMensaje(mensaje: Mensaje) {
        viewModel.borrarMensaje(mensaje)
        Toast.makeText(requireContext(), "Mensaje Eliminado", Toast.LENGTH_SHORT).show()
    }


}