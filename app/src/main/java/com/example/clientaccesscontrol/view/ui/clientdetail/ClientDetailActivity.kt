package com.example.clientaccesscontrol.view.ui.clientdetail

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clientaccesscontrol.R
import com.example.clientaccesscontrol.data.result.Results
import com.example.clientaccesscontrol.databinding.ActivityClientDetailBinding
import com.example.clientaccesscontrol.databinding.CustomDeleteDialogBinding
import com.example.clientaccesscontrol.view.ui.editrouter.EditRouterActivity
import com.example.clientaccesscontrol.view.utils.FactoryViewModel

class ClientDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClientDetailBinding
    private lateinit var bindingDialog: CustomDeleteDialogBinding
    private var clientId: Int = 0
    private var accessSelectedId: Int = 0
    private var speedSelectedId: Int = 0
    private var previousAccessSelectedId: Int = 0
    private var previousSpeedSelectedId: Int = 0
    private val clientDetailViewModel by viewModels<ClientDetailVM> {
        FactoryViewModel.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityClientDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        clientId = intent.getIntExtra(CLIENT_ID, 0)
        setupActionButton()
        setupActionSpinner()
        setupClientDetail()
    }

    private fun setupClientDetail() {
        clientDetailViewModel.getClientDetail(clientId)
        clientDetailViewModel.getClientDetail.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    val clientDetail = result.data.clientDetail?.firstOrNull()
                    if (clientDetail != null) {
                        binding.tvClientName.text = clientDetail.name
                        binding.tvPhone.text = clientDetail.phone
                        binding.tvAddress.text = clientDetail.address
                        binding.tvClientBTS.text = clientDetail.bts
                        binding.tvClientMode.text = clientDetail.mode
                        binding.tvClientSSID.text = clientDetail.ssid
                        binding.tvClientSSID.text = clientDetail.ssid
                        binding.tvClientIPInternet.text = clientDetail.ipAddress
                        binding.tvClientRadio.text = clientDetail.type
                        binding.tvClientRadioName.text = clientDetail.radioName
                        binding.tvClientFrequency.text = clientDetail.frequency
                        binding.tvClientChannelWidth.text = clientDetail.channelWidth
                        binding.tvClientSignal.text = clientDetail.radioSignal
                        binding.tvClientPresharedKey.text = clientDetail.presharedKey
                        binding.tvClientAPLocation.text = clientDetail.apLocation
                        binding.tvClientWLANMacAddress.text = clientDetail.wlanMacAddress
                        binding.tvClientUserPassword.text = clientDetail.password
                        binding.tvClientComment.text = clientDetail.comment
                    }
                }

                is Results.Error -> { showLoading(false) }
                is Results.Loading -> { showLoading(true) }
            }
        }
    }

    private fun setupActionSpinner() {
        clientDetailViewModel.getAccess.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    val accessList = result.data.access?.mapNotNull { it?.internetAccess } ?: emptyList()
                    val accessAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, accessList)
                    accessAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spInternetAccess.adapter = accessAdapter

                    if (accessList.isNotEmpty()) {
                        clientDetailViewModel.getClientDetail.observe(this) { accessResult ->
                            when (accessResult) {
                                is Results.Success -> {
                                    showLoading(false)
                                    val clientDetail = accessResult.data.clientDetail?.firstOrNull()
                                    if (clientDetail != null) {
                                        previousAccessSelectedId = accessList.indexOf(clientDetail.internetAccess) + 1
                                        binding.spInternetAccess.setSelection(accessList.indexOf(clientDetail.internetAccess))
                                    }
                                }
                                is Results.Error -> { showLoading(false) }
                                is Results.Loading -> { showLoading(true) }
                            }
                        }
                    }

                    binding.spInternetAccess.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long,
                            ) {
                                accessSelectedId = position + 1
                                if (accessSelectedId != previousAccessSelectedId) {
                                    updateClient()
                                    previousAccessSelectedId = accessSelectedId
                                }
                            }
                            override fun onNothingSelected(p0: AdapterView<*>?) {}
                        }
                }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> { showLoading(true) }
            }
        }

        clientDetailViewModel.getSpeed.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    val speedList = result.data.speed?.mapNotNull { it?.internetSpeed } ?: emptyList()
                    val speedAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, speedList)
                    speedAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spInternetSpeed.adapter = speedAdapter

                    if (speedList.isNotEmpty()) {
                        clientDetailViewModel.getClientDetail.observe(this) { speedResult ->
                            when (speedResult) {
                                is Results.Success -> {
                                    showLoading(false)
                                    val clientDetail = speedResult.data.clientDetail?.firstOrNull()
                                    if (clientDetail != null) {
                                        previousSpeedSelectedId = speedList.indexOf(clientDetail.internetSpeed) + 1
                                        binding.spInternetSpeed.setSelection(speedList.indexOf(clientDetail.internetSpeed))
                                    }
                                }
                                is Results.Error -> { showLoading(false) }
                                is Results.Loading -> { showLoading(true) }
                            }
                        }
                    }

                    binding.spInternetSpeed.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long,
                            ) {
                                speedSelectedId = position + 1
                                if (speedSelectedId != previousSpeedSelectedId) {
                                    updateClient()
                                    previousSpeedSelectedId = speedSelectedId
                                }
                            }
                            override fun onNothingSelected(p0: AdapterView<*>?) {}
                        }
                }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> { showLoading(true) }
            }
        }

        clientDetailViewModel.updateClient.observe(this) { result ->
            when (result) {
                is Results.Success -> { showLoading(false) }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> { showLoading(true) }
            }
        }
    }

    private fun updateClient() {
        if (accessSelectedId != 0 && speedSelectedId != 0) {
            UPDATE_CLIENT = "TRUE"
            clientDetailViewModel.updateClient(clientId, accessSelectedId, speedSelectedId)
        }
    }


    private fun setupActionButton() {
        binding.btDelete.setOnClickListener {
            customDialogDelete()
        }
        binding.btBack.setOnClickListener {
            finish()
        }
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
        binding.btEdit.setOnClickListener {
            val intent = Intent(this, EditRouterActivity::class.java).apply {
                putExtra(EditRouterActivity.CLIENT_ID, clientId)
            }
            startActivity(intent)
        }
    }

    private fun customDialogDelete() {
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

        bindingDialog.btYesDelete.setOnClickListener {
            clientDetailViewModel.deleteClient(clientId)
            deleteResult()
            dialog.dismiss()
            finish()
        }
        bindingDialog.btCancelDelete.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun deleteResult() {
        clientDetailViewModel.deleteClient.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                }

                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error deleting client: ${result.error}", Toast.LENGTH_SHORT).show()
                }

                is Results.Loading -> {showLoading(true)}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        clientDetailViewModel.getClientDetail(clientId)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val CLIENT_ID = "CLIENT_ID"
        var UPDATE_CLIENT = "UPDATE_CLIENT"
    }
}