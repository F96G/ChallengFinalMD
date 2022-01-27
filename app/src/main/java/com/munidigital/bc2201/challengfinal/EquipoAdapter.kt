package com.munidigital.bc2201.challengfinal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.munidigital.bc2201.challengfinal.databinding.ItemEquipoBinding

class EquipoAdapter: ListAdapter<Equipo, EquipoAdapter.ViewHolder>(DiffCallbak){

    companion object DiffCallbak : DiffUtil.ItemCallback<Equipo>(){
        override fun areItemsTheSame(oldItem: Equipo, newItem: Equipo): Boolean {
            return oldItem.nombre == newItem.nombre
        }

        override fun areContentsTheSame(oldItem: Equipo, newItem: Equipo): Boolean {
            return oldItem == newItem
        }
    }

    lateinit var onItemClickListener: (Equipo) -> Unit


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipoAdapter.ViewHolder {
        val binding = ItemEquipoBinding.inflate(LayoutInflater.from(parent.context))

        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: EquipoAdapter.ViewHolder, position: Int) {
        val equipo = getItem(position)
        holder.bind(equipo)
    }


    inner class ViewHolder(private val binding: ItemEquipoBinding, private val context: Context): RecyclerView.ViewHolder(binding.root){
        fun bind(equipo: Equipo){

            binding.tvNombre.text =  equipo.nombre
            if (equipo.favorito)
                binding.ivFavorito.visibility = View.VISIBLE
            else
                binding.ivFavorito.visibility = View.GONE
            Glide.with(context).load(equipo.imagen).centerCrop().into(binding.ivEscudo)

            //Esto no es muy necesario pero agiliza la velocidad del recyclerView en caso de listas muy grandes
            binding.executePendingBindings()

            binding.root.setOnClickListener{
                if (::onItemClickListener.isInitialized)
                    onItemClickListener(equipo)
            }
        }
    }

}