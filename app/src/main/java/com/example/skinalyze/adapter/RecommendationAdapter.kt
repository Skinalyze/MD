package com.example.skinalyze.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skinalyze.data.response.Recommendation
import com.example.skinalyze.databinding.ItemRowHistoryBinding

class RecommendationAdapter : ListAdapter<Recommendation, RecommendationAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val account = getItem(position)
        holder.bind(account)
//        val favoriteUser = FavoriteUser(
//            username = account.login, avatarUrl = account.avatarUrl
//        )
//        holder.itemView.setOnClickListener {
//            onItemClickCallback.onItemClicked(favoriteUser)
//        }

    }

    class MyViewHolder(private val binding: ItemRowHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(recommendation: Recommendation){
            binding.tvProblems.text = recommendation.skinProblem
            if (recommendation.isSensitive == true) {
                binding.tvSkinType.text = recommendation.skinType + ", Sensitif"
            }
            else {
                binding.tvSkinType.text = recommendation.skinType
            }
            binding.tvTimestamp.text = recommendation.timestamp.toString()
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recommendation>() {
            override fun areItemsTheSame(oldItem: Recommendation, newItem: Recommendation): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: Recommendation, newItem: Recommendation): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(id: String)
    }
}