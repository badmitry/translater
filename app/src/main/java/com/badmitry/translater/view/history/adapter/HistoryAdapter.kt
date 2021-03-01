package com.badmitry.translater.view.history.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.badmitry.translater.databinding.HistoryRvItemBinding
import com.badmitry.translator.model.data.DataModel

class HistoryAdapter(
    private val context: Context
) : RecyclerView.Adapter<HistoryAdapter.RecyclerItemViewHolder>() {

    lateinit var adapterBinding: HistoryRvItemBinding

    private var data: List<DataModel> = arrayListOf()

    fun setData(data: List<DataModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        adapterBinding =
            HistoryRvItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return RecyclerItemViewHolder(adapterBinding)
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(private val binding: HistoryRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataModel) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                binding.headerHistoryTextviewRecyclerItem.text = data.text
                itemView.setOnClickListener {
                    Toast.makeText(itemView.context, "on click: ${data.text}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
