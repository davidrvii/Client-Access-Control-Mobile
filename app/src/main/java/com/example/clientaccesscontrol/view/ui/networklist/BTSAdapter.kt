package com.example.clientaccesscontrol.view.ui.networklist

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clientaccesscontrol.data.cacresponse.BtsItem
import com.example.clientaccesscontrol.databinding.NetworkListBinding
import com.example.clientaccesscontrol.view.ui.networkfilter.NetworkFilterActivity

class BTSAdapter(private var listBTSAdapter: List<BtsItem>) :
    RecyclerView.Adapter<BTSAdapter.ViewHolder>() {

    class ViewHolder(private val binding: NetworkListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BtsItem) {
            binding.tvNetworkName.text = item.bts.toString()

            binding.root.setOnClickListener {
                val intent = Intent(itemView.context, NetworkFilterActivity::class.java).apply {
                    putExtra(NetworkFilterActivity.NETWORK_NAME, item.bts)
                    putExtra(NetworkFilterActivity.NETWORK_ID, item.btsId)
                    putExtra(NetworkFilterActivity.NETWORK, "BTS")
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
        val data = listBTSAdapter.subList(1, listBTSAdapter.size)[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listBTSAdapter.size - 1

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<BtsItem>) {
        listBTSAdapter = newList
        notifyDataSetChanged()
    }
}