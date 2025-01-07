package com.example.clientaccesscontrol.view.ui.network

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clientaccesscontrol.R
import com.example.clientaccesscontrol.databinding.ActivityNetworkBinding
import com.example.clientaccesscontrol.view.ui.networklist.NetworkListActivity

class NetworkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNetworkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNetworkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupClickListener()
        setupActionButton()
    }

    private fun setupActionButton() {
        binding.btBack.setOnClickListener {
            finish()
        }
    }

    private fun setupClickListener() {
        val clickListener = { view: View ->
            when (view.id) {
                R.id.BTS -> navigateToNetworkList("BTS")
                R.id.Radio -> navigateToNetworkList("Radio")
                R.id.Mode -> navigateToNetworkList("Mode")
                R.id.ChannelWidth -> navigateToNetworkList("Channel Width")
                R.id.PresharedKey -> navigateToNetworkList("Preshared Key")
            }
        }

        with(binding) {
            BTS.setOnClickListener(clickListener)
            Radio.setOnClickListener(clickListener)
            Mode.setOnClickListener(clickListener)
            ChannelWidth.setOnClickListener(clickListener)
            PresharedKey.setOnClickListener(clickListener)
        }
    }

    private fun navigateToNetworkList(network: String) {
        val intent = Intent(this, NetworkListActivity::class.java)
        intent.putExtra("network", network)
        startActivity(intent)
    }
}