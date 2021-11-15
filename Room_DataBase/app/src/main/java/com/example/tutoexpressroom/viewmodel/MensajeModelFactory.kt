package com.example.tutoexpressroom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tutoexpressroom.repository.Repositorio

class MensajeModelFactory(private val repositorio: Repositorio): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MensajeViewModel(repositorio) as T
    }
}