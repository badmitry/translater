package com.badmitry.translater.view.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.badmitry.translater.databinding.ActivityMainRecyclerviewItemBinding
import com.badmitry.translator.model.data.DataModel

class MainAdapter(
    private var onListItemClickListener: OnListItemClickListener,
    private val context: Context
) :
    RecyclerView.Adapter<MainAdapter.RecyclerItemViewHolder>() {

    lateinit var adapterBinding: ActivityMainRecyclerviewItemBinding

    private var data: List<DataModel> = arrayListOf()

    fun setData(data: List<DataModel>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        adapterBinding =
            ActivityMainRecyclerviewItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return RecyclerItemViewHolder(adapterBinding)
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class RecyclerItemViewHolder(private val binding: ActivityMainRecyclerviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataModel) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                binding.headerTextviewRecyclerItem.text = data.text
                binding.descriptionTextviewRecyclerItem.text =
                    data.meanings?.get(0)?.translation?.translation
                itemView.setOnClickListener { openInNewWindow(data) }
            }
        }
    }

    private fun openInNewWindow(listItemData: DataModel) {
        onListItemClickListener.onItemClick(listItemData)
    }

    interface OnListItemClickListener {
        fun onItemClick(data: DataModel)
    }
}
