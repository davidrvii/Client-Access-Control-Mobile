package com.example.clientaccesscontrol.view.ui.networklist

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clientaccesscontrol.R
import com.example.clientaccesscontrol.data.result.Results
import com.example.clientaccesscontrol.databinding.ActivityNetworkListBinding
import com.example.clientaccesscontrol.databinding.CustomAddDialogBinding
import com.example.clientaccesscontrol.view.utils.FactoryViewModel

class NetworkListActivity : AppCompatActivity() {

    private val networkListViewModel by viewModels<NetworkListVM> {
        FactoryViewModel.getInstance(this)
    }
    private lateinit var binding: ActivityNetworkListBinding
    private lateinit var btsAdapter: BTSAdapter
    private lateinit var radioAdapter: RadioAdapter
    private lateinit var modeAdapter: ModeAdapter
    private lateinit var channelWidthAdapter: ChannelWidthAdapter
    private lateinit var presharedKeyAdapter: PresharedKeyAdapter
    private lateinit var bindingDialog: CustomAddDialogBinding
    private lateinit var network: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNetworkListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        network = intent.getStringExtra("network").toString()

        setupNetworkList()
        buttonBackAction()
        buttonAddAction()
    }

    @SuppressLint("SetTextI18n")
    private fun buttonAddAction() {
        binding.btAdd.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            bindingDialog = CustomAddDialogBinding.inflate(layoutInflater)

            dialog.setContentView(bindingDialog.root)
            dialog.setCancelable(true)

            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val cardView = bindingDialog.root.findViewById<CardView>(R.id.AddCard)
            val layoutParams = cardView.layoutParams as ViewGroup.MarginLayoutParams
            val margin = (40 * resources.displayMetrics.density).toInt()
            layoutParams.setMargins(margin, 0, margin, 0)
            cardView.layoutParams = layoutParams

            bindingDialog.tvAdd.text = "Add New $network ?"
            bindingDialog.tvAddDesc.text = "This $network will be added to the list"
            bindingDialog.etNetworkUpload.hint = "New $network"

            bindingDialog.btYesAdd.setOnClickListener {
                dialog.dismiss()
                val networkValue = bindingDialog.etNetworkUpload.text.toString()
                Log.d("Button Yes Clicked", "Network: $network and Network Value: $networkValue")
                if (networkValue.isNotEmpty()) {
                    when (network) {
                        "BTS" -> networkListViewModel.createBTS(networkValue)
                        "Radio" -> networkListViewModel.createRadio(networkValue)
                        "Mode" -> networkListViewModel.createMode(networkValue)
                        "Channel Width" -> networkListViewModel.createChannelWidth(networkValue)
                        "Preshared Key" -> networkListViewModel.createPresharedKey(networkValue)
                    }
                } else {
                    Toast.makeText(this, "$network cannot be empty", Toast.LENGTH_SHORT).show()
                }
                addResult()
            }
            bindingDialog.btCancelAdd.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun buttonBackAction() {
        binding.btBack.setOnClickListener {
            finish()
        }
    }

    private fun addResult() {
        networkListViewModel.createBTS.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error creating BTS: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Results.Loading -> {}
            }
        }

        networkListViewModel.createRadio.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                }

                is Results.Error -> {
                    Toast.makeText(
                        this,
                        "Error creating Radio: ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Results.Loading -> {}
            }
        }

        networkListViewModel.createMode.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error creating Mode: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Results.Loading -> {}
            }
        }

        networkListViewModel.createChannelWidth.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                }

                is Results.Error -> {
                    Toast.makeText(
                        this,
                        "Error creating ChannelWidth: ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Results.Loading -> {}
            }
        }

        networkListViewModel.createPresharedKey.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                }

                is Results.Error -> {
                    Toast.makeText(
                        this,
                        "Error creating PresharedKey: ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Results.Loading -> {}
            }
        }
    }

    private fun setupNetworkList() {
        binding.tvTopbar.text = network

        when (network) {
            "BTS" -> {
                networkListViewModel.getBTS.observe(this) { result ->
                    when (result) {
                        is Results.Success -> btsAdapter.updateData(
                            result.data.bts?.filterNotNull() ?: emptyList()
                        )

                        is Results.Error -> Log.d("NetworkListActivity", "Error: ${result.error}")
                        is Results.Loading -> {}
                    }
                }
                setupBTSRecyclerView()
            }

            "Radio" -> {
                networkListViewModel.getRadio.observe(this) { result ->
                    when (result) {
                        is Results.Success -> radioAdapter.updateData(
                            result.data.radios?.filterNotNull() ?: emptyList()
                        )

                        is Results.Error -> Log.d("NetworkListActivity", "Error: ${result.error}")
                        is Results.Loading -> {}
                    }
                }
                setupRadioRecyclerView()
            }

            "Mode" -> {
                networkListViewModel.getMode.observe(this) { result ->
                    when (result) {
                        is Results.Success -> modeAdapter.updateData(
                            result.data.modes?.filterNotNull() ?: emptyList()
                        )

                        is Results.Error -> Log.d("NetworkListActivity", "Error: ${result.error}")
                        is Results.Loading -> {}
                    }
                }
                setupModeRecyclerView()
            }

            "Channel Width" -> {
                networkListViewModel.getChannelWidth.observe(this) { result ->
                    when (result) {
                        is Results.Success -> channelWidthAdapter.updateData(
                            result.data.channelWidths?.filterNotNull() ?: emptyList()
                        )

                        is Results.Error -> Log.d("NetworkListActivity", "Error: ${result.error}")
                        is Results.Loading -> {}
                    }
                }
                setupChannelWidthRecyclerView()
            }

            "Preshared Key" -> {
                networkListViewModel.getPresharedKey.observe(this) { result ->
                    when (result) {
                        is Results.Success -> presharedKeyAdapter.updateData(
                            result.data.presharedKeys?.filterNotNull() ?: emptyList()
                        )

                        is Results.Error -> Log.d("NetworkListActivity", "Error: ${result.error}")
                        is Results.Loading -> {}
                    }
                }
                setupPresharedKeyRecyclerView()
            }
        }
    }

    private fun setupBTSRecyclerView() {
        btsAdapter = BTSAdapter(emptyList())
        binding.rvNetworkList.apply {
            adapter = btsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupRadioRecyclerView() {
        radioAdapter = RadioAdapter(emptyList())
        binding.rvNetworkList.apply {
            adapter = radioAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupModeRecyclerView() {
        modeAdapter = ModeAdapter(emptyList())
        binding.rvNetworkList.apply {
            adapter = modeAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupChannelWidthRecyclerView() {
        channelWidthAdapter = ChannelWidthAdapter(emptyList())
        binding.rvNetworkList.apply {
            adapter = channelWidthAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupPresharedKeyRecyclerView() {
        presharedKeyAdapter = PresharedKeyAdapter(emptyList())
        binding.rvNetworkList.apply {
            adapter = presharedKeyAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}