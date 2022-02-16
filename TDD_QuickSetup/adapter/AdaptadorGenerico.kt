package com.example.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemRowBinding
import com.example.myapplication.model.models.Coins


class AdaptadorGenerico : ListAdapter<Coins, MyViewHolder>(Comparador()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val coin = getItem(position)

        with(holder.binding){
            /*
            LOGICA VISUAL
             */
        }

    }
}

class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val binding: ItemRowBinding = ItemRowBinding.bind(itemView)

    companion object{
        fun create(parent: ViewGroup):MyViewHolder{
            val layoutInflaterB = LayoutInflater.from(parent.context)
            val binding = ItemRowBinding.inflate(layoutInflaterB, parent, false)

            return MyViewHolder(binding.root)
        }
    }

}

class Comparador: DiffUtil.ItemCallback<Coins>() {
    override fun areItemsTheSame(oldItem: Coins, newItem: Coins): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Coins, newItem: Coins): Boolean {
        return oldItem.id == newItem.id

    }

}
