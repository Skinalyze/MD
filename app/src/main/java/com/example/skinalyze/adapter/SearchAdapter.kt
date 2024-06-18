package com.example.skinalyze.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.skinalyze.databinding.ItemRowProductBinding
import com.example.skinalyze.data.response.Product

class SearchAdapter: ListAdapter<Product, SearchAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class MyViewHolder(private val binding: ItemRowProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            var imageSrc = product.foto
            if (imageSrc?.endsWith("\r")!!) {
                imageSrc = imageSrc.dropLast(1)
            }

            Glide.with(binding.cardView.context).load(imageSrc).centerCrop().into(binding.imgItemPhoto)
            binding.tvItemBrand.text = product.brand
            binding.tvItemName.text = product.productName
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        val binding = ItemRowProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(product.idSkinCare)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: String)
    }
}