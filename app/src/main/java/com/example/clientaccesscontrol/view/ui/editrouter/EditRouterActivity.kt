package com.example.clientaccesscontrol.view.ui.editrouter

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private var preSharedKeySelectedId: Int = 1
    private var bts: String = ""
    private var mode: String = ""
    private var radio: String = ""
    private var channelWidth: String = ""
    private var preSharedKey: String = ""
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

        setupClientDetail()
        setupActionButton()
    }

    private fun setupClientDetail() {
        editRouterViewModel.getClientDetail.observe(this) { getClientDetailResult ->
            when (getClientDetailResult) {
                is Results.Success -> {
                    showLoading(false)

                    val clientDetail = getClientDetailResult.data.clientDetail?.firstOrNull()
                    if (clientDetail != null) {
                        binding.etSSID.setText(clientDetail.ssid)
                        binding.etIPAddress.setText(clientDetail.ipAddress)
                        binding.etRadioName.setText(clientDetail.radioName)
                        binding.etIPRadio.setText(clientDetail.ipRadio)
                        binding.etFrequency.setText(clientDetail.frequency)
                        binding.etSignal.setText(clientDetail.radioSignal)
                        binding.etAPLocation.setText(clientDetail.apLocation)
                        binding.etWLANMacAddress.setText(clientDetail.wlanMacAddress)
                        binding.etPassword.setText(clientDetail.password)
                        binding.etComment.setText(clientDetail.comment)
                        binding.etClientName.setText(clientDetail.name)
                        binding.etClientPhone.setText(clientDetail.phone)
                        binding.etClientAddress.setText(clientDetail.address)

                        bts = clientDetail.bts.toString()
                        mode = clientDetail.mode.toString()
                        radio = clientDetail.type.toString()
                        channelWidth = clientDetail.channelWidth.toString()
                        preSharedKey = clientDetail.presharedKey.toString()


                        setupActionSpinner()
                    } else {
                        Log.d("Edit Router", "Client Detail is Null")
                    }
                }

                is Results.Error -> {
                    showLoading(false)
                    Log.e("Edit Router", "Error Getting Client Detail: ${getClientDetailResult.error}")
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }
    }

    private fun setupActionSpinner() {
        editRouterViewModel.getBTS.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    val btsList = result.data.bts?.mapNotNull { it?.bts } ?: emptyList()
                    val btsAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, btsList)
                    btsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spBTS.adapter = btsAdapter

                    if (btsList.isNotEmpty()) {
                        binding.spBTS.setSelection(btsList.indexOf(bts))
                    }

                    binding.spBTS.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long,
                            ) {
                                btsSelectedId = position + 1
                            }
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                btsSelectedId
                            }
                        }
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("Edit Router", "Error Getting BTS: ${result.error}")
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        editRouterViewModel.getMode.observe(this) { getModeResult ->
            when (getModeResult) {
                is Results.Success -> {
                    showLoading(false)
                    val modeList = getModeResult.data.modes?.mapNotNull { it?.mode } ?: emptyList()
                    val modeAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, modeList)
                    modeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spMode.adapter = modeAdapter

                    if (modeList.isNotEmpty()) {
                        binding.spMode.setSelection(modeList.indexOf(mode))
                    }

                    binding.spMode.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long,
                            ) {
                                modeSelectedId = position + 1
                            }
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                modeSelectedId
                            }
                        }
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("Edit Router", "Error Getting Mode: ${getModeResult.error}")
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        editRouterViewModel.getRadio.observe(this) { getRadioResult ->
            when (getRadioResult) {
                is Results.Success -> {
                    showLoading(false)
                    val radioList = getRadioResult.data.radios?.mapNotNull { it?.type } ?: emptyList()
                    val radioAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, radioList)
                    radioAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spRadio.adapter = radioAdapter

                    if (radioList.isNotEmpty()) {
                        binding.spRadio.setSelection(radioList.indexOf(radio))
                    }

                    binding.spRadio.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long,
                            ) {
                                radioSelectedId = position + 1
                            }
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                radioSelectedId
                            }
                        }
                }
                is Results.Error -> {
                    showLoading(false)
                    Log.e("Edit Router", "Error Getting Radio: ${getRadioResult.error}")
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        editRouterViewModel.getChannelWidth.observe(this) { getChannelWidthResult ->
            when (getChannelWidthResult) {
                is Results.Success -> {
                    showLoading(false)
                    val channelWidthList = getChannelWidthResult.data.channelWidths?.mapNotNull { it?.channelWidth } ?: emptyList()
                    val channelWidthAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, channelWidthList)
                    channelWidthAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spChannelWidth.adapter = channelWidthAdapter

                    if (channelWidthList.isNotEmpty()) {
                        binding.spChannelWidth.setSelection(channelWidthList.indexOf(channelWidth))
                    }

                    binding.spChannelWidth.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long,
                            ) {
                                channelWidthSelectedId = position + 1
                            }
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                channelWidthSelectedId
                            }
                        }
                }

                is Results.Error -> {
                    showLoading(false)
                    Log.e("Edit Router", "Error Getting Channel Width: ${getChannelWidthResult.error}")
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
        }

        editRouterViewModel.getPresharedKey.observe(this) { getPreSharedKeyResult ->
            when (getPreSharedKeyResult) {
                is Results.Success -> {
                    showLoading(false)
                    val preSharedKeyList = getPreSharedKeyResult.data.presharedKeys?.mapNotNull { it?.presharedKey } ?: emptyList()
                    val preSharedKeyAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, preSharedKeyList)
                    preSharedKeyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spPresharedKey.adapter = preSharedKeyAdapter

                    if (preSharedKeyList.isNotEmpty()) {
                        binding.spPresharedKey.setSelection(preSharedKeyList.indexOf(preSharedKey))
                    }

                    binding.spPresharedKey.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>?,
                                view: View?,
                                position: Int,
                                id: Long,
                            ) {
                                preSharedKeySelectedId = position + 1
                            }
                            override fun onNothingSelected(p0: AdapterView<*>?) {
                                preSharedKeySelectedId
                            }
                        }
                }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error Getting PreShared Key: ${getPreSharedKeyResult.error}", Toast.LENGTH_SHORT).show()
                }
                is Results.Loading -> {
                    showLoading(true)
                }
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
            val name = binding.etClientName.text.toString()
            val phone = binding.etClientPhone.text.toString()
            val address = binding.etClientAddress.text.toString()
            val bts = btsSelectedId
            val mode = modeSelectedId
            val ssid = binding.etSSID.text.toString()
            val ipAddress = binding.etIPAddress.text.toString()
            val radio = radioSelectedId
            val radioName = binding.etRadioName.text.toString()
            val ipRadio = binding.etIPRadio.text.toString()
            val frequency = binding.etFrequency.text.toString()
            val channelWidth = channelWidthSelectedId
            val radioSignal = binding.etSignal.text.toString()
            val preSharedKey = preSharedKeySelectedId
            val apLocation = binding.etAPLocation.text.toString()
            val wlanMacAddress = binding.etWLANMacAddress.text.toString()
            val password = binding.etPassword.text.toString()
            val comment = binding.etComment.text.toString()

            editRouterViewModel.updateClient(clientId, name, phone, address)
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
                preSharedKey,
                comment,
                password,
                bts
            )

            editRouterViewModel.updateClient.removeObservers(this)
            editRouterViewModel.updateClient.observe(this) { updateClientResult ->
                when (updateClientResult) {
                    is Results.Success -> {
                        showLoading(false)
                        UPDATE_DETAIL_CLIENT = "TRUE"
                        finish()
                        dialog.dismiss()
                    }
                    is Results.Error -> {
                        showLoading(false)
                        Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
                        Log.d("Edit Router", "Update Failed: ${updateClientResult.error}")
                    }
                    is Results.Loading -> {
                        showLoading(true)
                    }
                }
            }

            editRouterViewModel.updateNetwork.removeObservers(this)
            editRouterViewModel.updateNetwork.observe(this) { updateNetworkResult ->
                when (updateNetworkResult) {
                    is Results.Success -> {
                        showLoading(false)
                        UPDATE_DETAIL_CLIENT = "TRUE"
                        finish()
                        dialog.dismiss()
                    }

                    is Results.Error -> {
                        showLoading(false)
                        Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
                        Log.d("Edit Router", "Update Failed: ${updateNetworkResult.error}")
                    }

                    is Results.Loading -> {
                        showLoading(true)
                    }
                }
            }
        }
        bindingDialog.btCancelSave.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val CLIENT_ID = "CLIENT_ID"
        var UPDATE_DETAIL_CLIENT = "FALSE"
    }
}