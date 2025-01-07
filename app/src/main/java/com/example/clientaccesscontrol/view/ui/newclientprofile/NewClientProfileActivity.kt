package com.example.clientaccesscontrol.view.ui.newclientprofile

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
import com.example.clientaccesscontrol.databinding.ActivityNewClientProfileBinding
import com.example.clientaccesscontrol.view.ui.newclientqueue.NewClientQueueActivity
import com.example.clientaccesscontrol.view.utils.FactoryViewModel

class NewClientProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewClientProfileBinding
    private val newClientProfileViewModel by viewModels<NewClientProfileVM> {
        FactoryViewModel.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewClientProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupActionSpinner()
        setupActionButton()

    }

    private fun setupActionSpinner() {
        newClientProfileViewModel.getAccess.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val accessList =
                        result.data.access?.mapNotNull { it?.internetAccess } ?: emptyList()
                    val accessAdapter =
                        ArrayAdapter(this, R.layout.spinner_dropdown_item, accessList)
                    accessAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spInternetAccess.adapter = accessAdapter
                }

                is Results.Error -> Toast.makeText(
                    this,
                    "Error: ${result.error}",
                    Toast.LENGTH_SHORT
                ).show()

                is Results.Loading -> {}
            }

            binding.spInternetAccess.onItemSelectedListener =
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

        newClientProfileViewModel.getSpeed.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val speedList =
                        result.data.speed?.mapNotNull { it?.internetSpeed } ?: emptyList()
                    val speedAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, speedList)
                    speedAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spInternetSpeed.adapter = speedAdapter
                }

                is Results.Error -> Toast.makeText(
                    this,
                    "Error: ${result.error}",
                    Toast.LENGTH_SHORT
                ).show()

                is Results.Loading -> {}
            }

            binding.spInternetSpeed.onItemSelectedListener =
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
        binding.btNext.setOnClickListener {
            val intent = Intent(this, NewClientQueueActivity::class.java)
            startActivity(intent)
        }
        binding.btBack.setOnClickListener {
            finish()
        }
    }
}