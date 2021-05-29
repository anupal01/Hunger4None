package com.dream_warriors.hunger4none.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dream_warriors.hunger4none.databinding.ViewTypeItemBinding

class TypesAdapter constructor(private val listener: (String, Int) -> Unit) : RecyclerView.Adapter<TypesAdapter.ViewHolder>()
{
    private var types = mutableListOf<String>()

    fun setData(types: MutableList<String>)
    {
        this.types = mutableListOf()
        this.types.addAll(types)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(ViewTypeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = types.size

    private fun getItem(position: Int) = types[position]

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        holder.binding.item.text = getItem(position)
    }

    inner class ViewHolder(val binding: ViewTypeItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        init
        {
            binding.item.setOnClickListener { listener(getItem(layoutPosition), layoutPosition) }
        }
    }
}