package com.example.clientaccesscontrol.view.ui.networkfilter

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
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
import com.example.clientaccesscontrol.R
import com.example.clientaccesscontrol.data.result.Results
import com.example.clientaccesscontrol.databinding.ActivityNetworkFilterBinding
import com.example.clientaccesscontrol.databinding.CustomDeleteDialogBinding
import com.example.clientaccesscontrol.view.utils.FactoryViewModel

class NetworkFilterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNetworkFilterBinding
    private var networkId: Int = 0
    private lateinit var networkName: String
    private lateinit var network: String
    private lateinit var bindingDialog: CustomDeleteDialogBinding
    private val networkFilterViewModel by viewModels<NetworkFilterVM> {
        FactoryViewModel.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNetworkFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkId = intent.getIntExtra(NETWORK_ID, 0)
        networkName = intent.getStringExtra(NETWORK_NAME).toString()
        network = intent.getStringExtra(NETWORK).toString()
        binding.tvTopbar.text = networkName

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonDeleteAction()
        buttonBackAction()
    }

    private fun buttonBackAction() {
        binding.btBack.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun buttonDeleteAction() {
        binding.btDelete.setOnClickListener {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

            bindingDialog = CustomDeleteDialogBinding.inflate(layoutInflater)

            dialog.setContentView(bindingDialog.root)
            dialog.setCancelable(true)

            dialog.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            val cardView = bindingDialog.root.findViewById<CardView>(R.id.DeleteCard)
            val layoutParams = cardView.layoutParams as ViewGroup.MarginLayoutParams
            val margin = (40 * resources.displayMetrics.density).toInt()
            layoutParams.setMargins(margin, 0, margin, 0)
            cardView.layoutParams = layoutParams

            bindingDialog.tvDelete.text = "Delete $networkName ?"
            bindingDialog.tvDeleteDesc.text = "This $network will be removed from the list"

            bindingDialog.btYesDelete.setOnClickListener {
                dialog.dismiss()
                when (network) {
                    "BTS" -> networkFilterViewModel.deleteBTS(networkId)
                    "Radio" -> networkFilterViewModel.deleteRadio(networkId)
                    "Mode" -> networkFilterViewModel.deleteMode(networkId)
                    "Channel Width" -> networkFilterViewModel.deleteChannelWidth(networkId)
                    "Preshared Key" -> networkFilterViewModel.deletePresharedKey(networkId)
                }
                finish()
                deleteResult()
            }
            bindingDialog.btCancelDelete.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }

    private fun deleteResult() {
        networkFilterViewModel.deleteBTS.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error deleting BTS: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Results.Loading -> {}
            }
        }

        networkFilterViewModel.deleteMode.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    finish()
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error deleting Mode: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Results.Loading -> {}
            }
        }

        networkFilterViewModel.deleteRadio.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    finish()
                }

                is Results.Error -> {
                    Toast.makeText(
                        this,
                        "Error deleting Radio: ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Results.Loading -> {}
            }
        }

        networkFilterViewModel.deleteChannelWidth.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    finish()
                }

                is Results.Error -> {
                    Toast.makeText(
                        this,
                        "Error deleting ChannelWidth: ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Results.Loading -> {}
            }
        }

        networkFilterViewModel.deletePresharedKey.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    finish()
                }

                is Results.Error -> {
                    Toast.makeText(
                        this,
                        "Error deleting PresharedKey: ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is Results.Loading -> {}
            }
        }
    }

    companion object {
        const val NETWORK_ID = "NETWORK_ID"
        const val NETWORK_NAME = "NETWORK_NAME"
        const val NETWORK = "NETWORK"
    }
}