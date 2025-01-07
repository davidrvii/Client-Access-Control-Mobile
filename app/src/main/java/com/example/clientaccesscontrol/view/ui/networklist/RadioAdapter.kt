package com.example.clientaccesscontrol.view.ui.networklist

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.clientaccesscontrol.data.cacresponse.RadiosItem
import com.example.clientaccesscontrol.databinding.NetworkListBinding
import com.example.clientaccesscontrol.view.ui.networkfilter.NetworkFilterActivity

class RadioAdapter(private var listRadioAdapter: List<RadiosItem>) :
    RecyclerView.Adapter<RadioAdapter.ViewHolder>() {

    class ViewHolder(private val binding: NetworkListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RadiosItem) {
            binding.tvNetworkName.text = item.type.toString()

            binding.root.setOnClickListener {
                val intent = Intent(itemView.context, NetworkFilterActivity::class.java).apply {
                    putExtra(NetworkFilterActivity.NETWORK_NAME, item.type)
                    putExtra(NetworkFilterActivity.NETWORK_ID, item.radioId)
                    putExtra(NetworkFilterActivity.NETWORK, "Radio")
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
        val data = listRadioAdapter.subList(1, listRadioAdapter.size)[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listRadioAdapter.size - 1

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<RadiosItem>) {
        listRadioAdapter = newList
        notifyDataSetChanged()
    }
}