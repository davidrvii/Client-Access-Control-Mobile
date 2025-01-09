package com.example.clientaccesscontrol.view.ui.editrouter

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clientaccesscontrol.R
import com.example.clientaccesscontrol.data.result.Results
import com.example.clientaccesscontrol.databinding.ActivityEditRouterBinding
import com.example.clientaccesscontrol.databinding.CustomSaveDialogBinding
import com.example.clientaccesscontrol.view.utils.FactoryViewModel

class EditRouterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditRouterBinding
    private lateinit var bindingDialog: CustomSaveDialogBinding
    private var clientId: Int = 0
    private var btsSelectedId: Int = 1
    private var modeSelectedId: Int = 1
    private var radioSelectedId: Int = 1
    private var channelWidthSelectedId: Int = 1
    private var presharedKeySelectedId: Int = 1
    private val editRouterViewModel by viewModels<EditRouterVM> {
        FactoryViewModel.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditRouterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        clientId = intent.getIntExtra(CLIENT_ID, 0)
        editRouterViewModel.getClientDetail(clientId)

        setupActionSpinner()
        setupActionButton()
        setupEditHint()
    }

    private fun setupEditHint() {
        editRouterViewModel.getClientDetail.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val clientDetail = result.data.clientDetail?.firstOrNull()
                    if (clientDetail != null) {
                        binding.etSSID.hint = clientDetail.ssid
                        binding.etIPAddress.hint = clientDetail.ipAddress
                        binding.etRadioName.hint = clientDetail.radioName
                        binding.etIPRadio.hint = clientDetail.ipRadio
                        binding.etFrequency.hint = clientDetail.frequency
                        binding.etSignal.hint = clientDetail.radioSignal
                        binding.etAPLocation.hint = clientDetail.apLocation
                        binding.etWLANMacAddress.hint = clientDetail.wlanMacAddress
                        binding.etPassword.hint = clientDetail.password
                        binding.etComment.hint = clientDetail.comment
                    }
                }

                is Results.Error -> {}
                is Results.Loading -> {}
            }
        }
    }

    private fun setupActionSpinner() {
        editRouterViewModel.getBTS.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val btsList =
                        result.data.bts?.mapNotNull { it?.bts } ?: emptyList()
                    val btsAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, btsList)
                    btsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spBTS.adapter = btsAdapter

                    if (btsList.isNotEmpty()) {
                        editRouterViewModel.getClientDetail.observe(this) { btsResult ->
                            when (btsResult) {
                                is Results.Success -> {
                                    val clientDetail = btsResult.data.clientDetail?.firstOrNull()
                                    if (clientDetail != null) {
                                        binding.spBTS.setSelection(btsList.indexOf(clientDetail.bts))
                                    }
                                }

                                is Results.Error -> {}
                                is Results.Loading -> {}
                            }
                        }
                    }

                    binding.spBTS.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: android.view.View?,
                                position: Int,
                                id: Long,
                            ) {
                                btsSelectedId = position+1
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {}
                        }
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {}
            }
        }

        editRouterViewModel.getMode.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val modeList =
                        result.data.modes?.mapNotNull { it?.mode } ?: emptyList()
                    val modeAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, modeList)
                    modeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spMode.adapter = modeAdapter

                    if (modeList.isNotEmpty()) {
                        editRouterViewModel.getClientDetail.observe(this) { modeResult ->
                            when (modeResult) {
                                is Results.Success -> {
                                    val clientDetail = modeResult.data.clientDetail?.firstOrNull()
                                    if (clientDetail != null) {
                                        binding.spMode.setSelection(
                                            modeList.indexOf(
                                                clientDetail.mode
                                            )
                                        )
                                    }
                                }

                                is Results.Error -> {}
                                is Results.Loading -> {}
                            }
                        }
                    }

                    binding.spMode.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: android.view.View?,
                                position: Int,
                                id: Long,
                            ) {
                                modeSelectedId = position+1
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {}
                        }
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Results.Loading -> {}
            }
        }

        editRouterViewModel.getRadio.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val radioList =
                        result.data.radios?.mapNotNull { it?.type } ?: emptyList()
                    val radioAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, radioList)
                    radioAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spRadio.adapter = radioAdapter

                    if (radioList.isNotEmpty()) {
                        editRouterViewModel.getClientDetail.observe(this) { radioResult ->
                            when (radioResult) {
                                is Results.Success -> {
                                    val clientDetail = radioResult.data.clientDetail?.firstOrNull()
                                    if (clientDetail != null) {
                                        binding.spRadio.setSelection(
                                            radioList.indexOf(
                                                clientDetail.type
                                            )
                                        )
                                    }
                                }

                                is Results.Error -> {}
                                is Results.Loading -> {}
                            }
                        }
                    }

                    binding.spRadio.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: android.view.View?,
                                position: Int,
                                id: Long,
                            ) {
                                radioSelectedId = position+1
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {}
                        }
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Results.Loading -> {}
            }
        }

        editRouterViewModel.getChannelWidth.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val channelWidthList =
                        result.data.channelWidths?.mapNotNull { it?.channelWidth } ?: emptyList()
                    val channelWidthAdapter =
                        ArrayAdapter(this, R.layout.spinner_dropdown_item, channelWidthList)
                    channelWidthAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spChannelWidth.adapter = channelWidthAdapter

                    if (channelWidthList.isNotEmpty()) {
                        editRouterViewModel.getClientDetail.observe(this) { channelWidthResult ->
                            when (channelWidthResult) {
                                is Results.Success -> {
                                    val clientDetail =
                                        channelWidthResult.data.clientDetail?.firstOrNull()
                                    if (clientDetail != null) {
                                        binding.spChannelWidth.setSelection(
                                            channelWidthList.indexOf(
                                                clientDetail.channelWidth
                                            )
                                        )
                                    }
                                }

                                is Results.Error -> {}
                                is Results.Loading -> {}
                            }
                        }
                    }

                    binding.spChannelWidth.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: android.view.View?,
                                position: Int,
                                id: Long,
                            ) {
                                channelWidthSelectedId = position+1
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {}
                        }
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {}
            }
        }

        editRouterViewModel.getPresharedKey.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val presharedKeyList =
                        result.data.presharedKeys?.mapNotNull { it?.presharedKey } ?: emptyList()
                    val presharedKeyAdapter =
                        ArrayAdapter(this, R.layout.spinner_dropdown_item, presharedKeyList)
                    presharedKeyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spPresharedKey.adapter = presharedKeyAdapter

                    if (presharedKeyList.isNotEmpty()) {
                        editRouterViewModel.getClientDetail.observe(this) { presharedKeyResult ->
                            when (presharedKeyResult) {
                                is Results.Success -> {
                                    val clientDetail = presharedKeyResult.data.clientDetail?.firstOrNull()
                                    if (clientDetail != null) {
                                        binding.spPresharedKey.setSelection(presharedKeyList.indexOf(clientDetail.presharedKey))
                                    }
                                }

                                is Results.Error -> {}
                                is Results.Loading -> {}
                            }
                        }
                    }

                    binding.spPresharedKey.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: android.view.View?,
                                position: Int,
                                id: Long,
                            ) {
                                presharedKeySelectedId = position+1
                            }

                            override fun onNothingSelected(p0: AdapterView<*>?) {}
                        }
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT)
                        .show()
                }

                is Results.Loading -> {}
            }
        }
    }

    private fun setupActionButton() {
        binding.btSave.setOnClickListener {
            showCustomDialog()
        }

        binding.btBack.setOnClickListener {
            finish()
        }
    }

    private fun showCustomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        bindingDialog = CustomSaveDialogBinding.inflate(layoutInflater)

        dialog.setContentView(bindingDialog.root)
        dialog.setCancelable(true)

        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val cardView = bindingDialog.root.findViewById<CardView>(R.id.SaveCard)
        val layoutParams = cardView.layoutParams as ViewGroup.MarginLayoutParams
        val margin = (40 * resources.displayMetrics.density).toInt()
        layoutParams.setMargins(margin, 0, margin, 0)
        cardView.layoutParams = layoutParams

        bindingDialog.btYesSave.setOnClickListener {
            val bts =  btsSelectedId
            val mode = modeSelectedId
            val ssid = binding.etSSID.text.toString()
            val ipAddress = binding.etIPAddress.text.toString()
            val radio = radioSelectedId
            val radioName = binding.etRadioName.text.toString()
            val ipRadio = binding.etIPRadio.text.toString()
            val frequency = binding.etFrequency.text.toString()
            val channelWidth = channelWidthSelectedId
            val radioSignal = binding.etSignal.text.toString()
            val presharedKey = presharedKeySelectedId
            val apLocation = binding.etAPLocation.text.toString()
            val wlanMacAddress = binding.etWLANMacAddress.text.toString()
            val password = binding.etPassword.text.toString()
            val comment = binding.etComment.text.toString()

            editRouterViewModel.updateNetwork(
                clientId,
                radioName,
                frequency,
                ipRadio,
                ipAddress,
                wlanMacAddress,
                ssid,
                radioSignal,
                apLocation,
                radio,
                mode,
                channelWidth,
                presharedKey,
                comment,
                password,
                bts
            )

            editRouterViewModel.updateNetwork.observe(this) { result ->
                when (result) {
                    is Results.Success -> {
                        UPDATE_DETAIL_CLIENT = "TRUE"
                        finish()
                        dialog.dismiss()
                    }
                    is Results.Error -> {
                        Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
                        Log.d("Edit Router", "Update Failed: ${result.error}")
                    }
                    is Results.Loading -> {}
                }
            }
        }
        bindingDialog.btCancelSave.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    companion object {
        const val CLIENT_ID = "CLIENT_ID"
        var UPDATE_DETAIL_CLIENT = "FALSE"
    }
}