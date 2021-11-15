package com.example.tutoexpressroom.viewmodel

import androidx.lifecycle.*
import com.example.tutoexpressroom.model.Mensaje
import com.example.tutoexpressroom.repository.Repositorio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class MensajeViewModel(private val repositorio: Repositorio): ViewModel() {

    val listadoMensaje : LiveData<List<Mensaje>> = repositorio.listadoMensaje().asLiveData()



    fun agregarMensaje(mensaje: Mensaje){
        viewModelScope.launch(IO) {
            repositorio.agregarMensaje(mensaje)
        }
    }

    fun borrarMensaje(mensaje: Mensaje){
        viewModelScope.launch(IO) {
            repositorio.borrarMensaje(mensaje)
        }


    }

}