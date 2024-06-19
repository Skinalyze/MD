package com.example.skinalyze.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.skinalyze.Utils.labelArrayToSkinProblem
import com.example.skinalyze.Utils.skinTypeTranslate
import com.example.skinalyze.data.response.Recommendation
import com.example.skinalyze.databinding.ItemRowHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        val recommendation = getItem(position)
        holder.bind(recommendation)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(recommendation.id.toString())
        }

    }

    class MyViewHolder(private val binding: ItemRowHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(recommendation: Recommendation){
            binding.tvProblems.text = labelArrayToSkinProblem(recommendation.skinProblem.split(", "))
            if (recommendation.isSensitive == 1) {
                binding.tvSkinType.text = skinTypeTranslate(recommendation.skinType) + ", Sensitif"
            }
            else {
                binding.tvSkinType.text = skinTypeTranslate(recommendation.skinType)
            }


            val originalFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
            val targetFormat = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss", Locale("id", "ID"))
            val date: Date? = originalFormat.parse(recommendation.timestamp.toString())
            val formattedDate: String = date?.let { targetFormat.format(it) }.toString()

            binding.tvTimestamp.text = formattedDate
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