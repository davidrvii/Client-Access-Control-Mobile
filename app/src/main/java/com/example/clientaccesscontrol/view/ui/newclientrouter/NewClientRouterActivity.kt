package com.example.clientaccesscontrol.view.ui.newclientrouter

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clientaccesscontrol.R
import com.example.clientaccesscontrol.data.result.Results
import com.example.clientaccesscontrol.databinding.ActivityNewClientRouterBinding
import com.example.clientaccesscontrol.view.ui.home.MainActivity
import com.example.clientaccesscontrol.view.utils.FactoryViewModel

class NewClientRouterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewClientRouterBinding
    private val newClientRouterViewModel by viewModels<NewClientRouterVM> {
        FactoryViewModel.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewClientRouterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupActionButton()
        setupActionSpinner()

    }

    private fun setupActionSpinner() {
        newClientRouterViewModel
            .getBTS.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val btsList =
                        result.data.bts?.mapNotNull { it?.bts } ?: emptyList()
                    val btsAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, btsList)
                    btsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spBTS.adapter = btsAdapter
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Results.Loading -> {}
            }

            binding.spBTS.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: android.view.View?,
                        position: Int,
                        id: Long,
                    ) {

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
        }

        newClientRouterViewModel.getMode.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val modeList =
                        result.data.modes?.mapNotNull { it?.mode } ?: emptyList()
                    val modeAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, modeList)
                    modeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spMode.adapter = modeAdapter
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Results.Loading -> {}
            }

            binding.spMode.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: android.view.View?,
                        position: Int,
                        id: Long,
                    ) {

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
        }

        newClientRouterViewModel.getRadio.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val radioList =
                        result.data.radios?.mapNotNull { it?.type } ?: emptyList()
                    val radioAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, radioList)
                    radioAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spRadio.adapter = radioAdapter
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Results.Loading -> {}
            }

            binding.spRadio.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: android.view.View?,
                        position: Int,
                        id: Long,
                    ) {

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
        }

        newClientRouterViewModel.getChannelWidth.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val channelWidthList =
                        result.data.channelWidths?.mapNotNull { it?.channelWidth } ?: emptyList()
                    val channelWidthAdapter =
                        ArrayAdapter(this, R.layout.spinner_dropdown_item, channelWidthList)
                    channelWidthAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spChannelWidth.adapter = channelWidthAdapter
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Results.Loading -> {}
            }

            binding.spChannelWidth.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: android.view.View?,
                        position: Int,
                        id: Long,
                    ) {

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
        }

        newClientRouterViewModel.getPresharedKey.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val presharedKeyList =
                        result.data.presharedKeys?.mapNotNull { it?.presharedKey } ?: emptyList()
                    val presharedKeyAdapter =
                        ArrayAdapter(this, R.layout.spinner_dropdown_item, presharedKeyList)
                    presharedKeyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spPresharedKey.adapter = presharedKeyAdapter
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Results.Loading -> {}
            }

            binding.spPresharedKey.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: android.view.View?,
                        position: Int,
                        id: Long,
                    ) {

                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
        }
    }

    private fun setupActionButton() {
        binding.btDone.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}