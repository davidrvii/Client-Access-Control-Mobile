package com.example.clientaccesscontrol.view.ui.networklist

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clientaccesscontrol.data.cacresponse.ModesItem
import com.example.clientaccesscontrol.databinding.NetworkListBinding
import com.example.clientaccesscontrol.view.ui.networkfilter.NetworkFilterActivity

class ModeAdapter(private var listModeAdapter: List<ModesItem>) :
    RecyclerView.Adapter<ModeAdapter.ViewHolder>() {

    class ViewHolder(private val binding: NetworkListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ModesItem) {
            binding.tvNetworkName.text = item.mode.toString()

            binding.root.setOnClickListener {
                val intent = Intent(itemView.context, NetworkFilterActivity::class.java).apply {
                    putExtra(NetworkFilterActivity.NETWORK_NAME, item.mode)
                    putExtra(NetworkFilterActivity.NETWORK_ID, item.modeId)
                    putExtra(NetworkFilterActivity.NETWORK, "Mode")
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = NetworkListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listModeAdapter.subList(1, listModeAdapter.size)[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listModeAdapter.size - 1

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<ModesItem>) {
        listModeAdapter = newList
        notifyDataSetChanged()
    }
}