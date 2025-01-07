package com.example.clientaccesscontrol.view.ui.networklist

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clientaccesscontrol.data.cacresponse.PresharedKeysItem
import com.example.clientaccesscontrol.databinding.NetworkListBinding
import com.example.clientaccesscontrol.view.ui.networkfilter.NetworkFilterActivity

class PresharedKeyAdapter(private var listPresharedKeyAdapter: List<PresharedKeysItem>) :
    RecyclerView.Adapter<PresharedKeyAdapter.ViewHolder>() {

    class ViewHolder(private val binding: NetworkListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PresharedKeysItem) {
            binding.tvNetworkName.text = item.presharedKey.toString()

            binding.root.setOnClickListener {
                val intent = Intent(itemView.context, NetworkFilterActivity::class.java).apply {
                    putExtra(NetworkFilterActivity.NETWORK_NAME, item.presharedKey)
                    putExtra(NetworkFilterActivity.NETWORK_ID, item.presharedKeyId)
                    putExtra(NetworkFilterActivity.NETWORK, "Preshared Key")
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
        val data = listPresharedKeyAdapter.subList(1, listPresharedKeyAdapter.size)[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listPresharedKeyAdapter.size - 1

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<PresharedKeysItem>) {
        listPresharedKeyAdapter = newList
        notifyDataSetChanged()
    }
}