package com.example.tutoexpressroom.listadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tutoexpressroom.databinding.ItemRowBinding
import com.example.tutoexpressroom.model.Mensaje

class MensajeListAdapter(private val miExtractor: MiExtractor) : ListAdapter<Mensaje, MiViewHolder>(MensajeComparador()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiViewHolder {
        return MiViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MiViewHolder, position: Int) {
        val mensaje = getItem(position)
        with(holder.binding){
            textView2.text = mensaje.mensaje
        }

        holder.binding.imageView.setOnClickListener {
            miExtractor.extraerMensaje(mensaje)
        }
    }

    interface MiExtractor{
        fun extraerMensaje(mensaje: Mensaje)
    }
}

class MiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val binding = ItemRowBinding.bind(itemView)
    companion object {
        fun create(parent: ViewGroup): MiViewHolder {
            val layoutInflaterB = LayoutInflater.from(parent.context)
            val binding = ItemRowBinding.inflate(layoutInflaterB, parent, false)

            return MiViewHolder(binding.root)
        }
    }

}

class MensajeComparador: DiffUtil.ItemCallback<Mensaje>() {
    override fun areItemsTheSame(oldItem: Mensaje, newItem: Mensaje): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Mensaje, newItem: Mensaje): Boolean {
        return  oldItem.id == newItem.id
    }

}
