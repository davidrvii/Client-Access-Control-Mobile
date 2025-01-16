package com.example.clientaccesscontrol.view.ui.newclientrouter

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.clientaccesscontrol.databinding.ActivityNewClientRouterBinding
import com.example.clientaccesscontrol.databinding.CustomLoadingDialogBinding
import com.example.clientaccesscontrol.view.ui.home.MainActivity
import com.example.clientaccesscontrol.view.utils.FactoryViewModel

class NewClientRouterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewClientRouterBinding
    private lateinit var bindingDialog: CustomLoadingDialogBinding
    private var loadingDialog: Dialog? = null
    private var btsSelectedId: Int = 1
    private var modeSelectedId: Int = 1
    private var radioSelectedId: Int = 1
    private var channelWidthSelectedId: Int = 1
    private var presharedKeySelectedId: Int = 1
    private var limitAt: String = ""
    private var maxLimit: String = ""
    private var burstTime: String = ""
    private var burstThreshold: String = ""
    private var burstLimit: String = ""
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
        //BTS
        newClientRouterViewModel.getBTS.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    val btsList = result.data.bts?.mapNotNull { it?.bts } ?: emptyList()
                    val btsAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, btsList)
                    btsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spBTS.adapter = btsAdapter
                }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    Log.e("NewClientRouterActivity", "Error Getting BTS: ${result.error}")
                }
                is Results.Loading -> {
                    showLoading(true)
                }
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

        //Mode
        newClientRouterViewModel.getMode.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    val modeList = result.data.modes?.mapNotNull { it?.mode } ?: emptyList()
                    val modeAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, modeList)
                    modeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spMode.adapter = modeAdapter
                }

                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    Log.e("NewClientRouterActivity", "Error Getting Mode: ${result.error}")
                }

                is Results.Loading -> {
                    showLoading(true)
                }
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

        //Radio
        newClientRouterViewModel.getRadio.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    val radioList = result.data.radios?.mapNotNull { it?.type } ?: emptyList()
                    val radioAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, radioList)
                    radioAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spRadio.adapter = radioAdapter
                }

                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    Log.e("NewClientRouterActivity", "Error Getting Radio: ${result.error}")
                }

                is Results.Loading -> {
                    showLoading(true)
                }
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

        //Channel Width
        newClientRouterViewModel.getChannelWidth.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    val channelWidthList = result.data.channelWidths?.mapNotNull { it?.channelWidth } ?: emptyList()
                    val channelWidthAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, channelWidthList)
                    channelWidthAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spChannelWidth.adapter = channelWidthAdapter
                }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    Log.e("NewClientRouterActivity", "Error Getting Channel Width: ${result.error}")
                }
                is Results.Loading -> {
                    showLoading(true)
                }
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

        //Pre Shared Key
        newClientRouterViewModel.getPresharedKey.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showLoading(false)
                    val presharedKeyList = result.data.presharedKeys?.mapNotNull { it?.presharedKey } ?: emptyList()
                    val presharedKeyAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, presharedKeyList)
                    presharedKeyAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spPresharedKey.adapter = presharedKeyAdapter
                }
                is Results.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    Log.e("NewClientRouterActivity", "Error Getting PreShared Key: ${result.error}")
                }
                is Results.Loading -> {
                    showLoading(true)
                }
            }
            binding.spPresharedKey.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long,
                    ) {
                        presharedKeySelectedId = position + 1
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        presharedKeySelectedId
                    }
                }
        }
    }

    private fun setupActionButton() {
        textFieldWatcher()

        binding.btDone.setOnClickListener {
            createMangle()
        }
    }

    private fun createMangle() {
        val comment = intent.getStringExtra(COMMENT).toString().trim()
        val ipAddress = binding.etIPInternet.text.toString().trim()

        val newUploadPacketMark = intent.getStringExtra(NEW_UPLOAD_PACKET_MARK).toString().trim()
        val newDownloadPacketMark = intent.getStringExtra(NEW_DOWNLOAD_PACKET_MARK).toString().trim()

        //Create Mangle Upload
        newClientRouterViewModel.createMangleUpload(comment, "forward", ipAddress, "bridge1", "mark-packet", newUploadPacketMark)

        //Mangle Upload Observer
        newClientRouterViewModel.createMangleUpload.removeObservers(this)
        newClientRouterViewModel.createMangleUpload.observe(this) { newMangleUploadResult ->
            when (newMangleUploadResult) {
                is Results.Success -> {
                    showCustomDialogLoading(false)
                    //Create Mangle Download
                    newClientRouterViewModel.createMangleDownload(comment, "output", ipAddress, "bridge1", "mark-packet", newDownloadPacketMark)
                }
                is Results.Error -> {
                    showCustomDialogLoading(false)
                    Toast.makeText(this, "Create Mangle Error: ${newMangleUploadResult.error}", Toast.LENGTH_SHORT).show()
                    Log.d("NewClientRouterActivity", "Create Mangle Error: ${newMangleUploadResult.error}")
                }
                is Results.Loading -> {
                    showCustomDialogLoading(true)
                }
            }
        }

        //Mangle Download Observer
        newClientRouterViewModel.createMangleDownload.removeObservers(this)
        newClientRouterViewModel.createMangleDownload.observe(this) { newMangleDownloadResult ->
            when (newMangleDownloadResult) {
                is Results.Success -> {
                    showCustomDialogLoading(false)
                    //Create Mangle LAN
                    newClientRouterViewModel.createMangleLAN(comment, "forward", ipAddress, "!LAN", "mark-packet", newDownloadPacketMark)
                }
                is Results.Error -> {
                    showCustomDialogLoading(false)
                    Toast.makeText(this, "Create Mangle Error: ${newMangleDownloadResult.error}", Toast.LENGTH_SHORT).show()
                    Log.d("NewClientRouterActivity", "Create Mangle Error: ${newMangleDownloadResult.error}")
                }
                is Results.Loading -> {
                    showCustomDialogLoading(true)
                }
            }
        }

        //Mangle LAN Observer
        newClientRouterViewModel.createMangleLAN.removeObservers(this)
        newClientRouterViewModel.createMangleLAN.observe(this) { newMangleLANResult ->
            when (newMangleLANResult) {
                is Results.Success -> {
                    showCustomDialogLoading(false)
                    createQueueTree()
                }
                is Results.Error -> {
                    showCustomDialogLoading(false)
                    Toast.makeText(this, "Create Mangle Error: ${newMangleLANResult.error}", Toast.LENGTH_SHORT).show()
                    Log.d("NewClientRouterActivity", "Create Mangle Error: ${newMangleLANResult.error}")
                }
                is Results.Loading -> {
                    showCustomDialogLoading(true)
                }
            }

        }
    }

    private fun createQueueTree() {
        val newQueueTreeUpload = intent.getStringExtra(NEW_QUEUE_TREE_UPLOAD).toString().trim()
        val newQueueTreeDownload = intent.getStringExtra(NEW_QUEUE_TREE_DOWNLOAD).toString().trim()

        val newUploadPacketMark = intent.getStringExtra(NEW_UPLOAD_PACKET_MARK).toString().trim()
        val newDownloadPacketMark = intent.getStringExtra(NEW_DOWNLOAD_PACKET_MARK).toString().trim()

        val speedId = intent.getIntExtra(SPEED_ID, 0)
        clientSpeed(speedId)

        val comment = intent.getStringExtra(COMMENT).toString().trim()

        //Create Queue Tree Upload
        newClientRouterViewModel.createQueueTreeUpload(newQueueTreeUpload, "Total Upload", comment, newUploadPacketMark, limitAt, maxLimit, burstTime, burstThreshold, burstLimit)

        //Queue Tree Upload Observer
        newClientRouterViewModel.createQueueTreeUpload.removeObservers(this)
        newClientRouterViewModel.createQueueTreeUpload.observe(this) { newQueueTreeResult ->
            when (newQueueTreeResult) {
                is Results.Success -> {
                    showCustomDialogLoading(false)
                    //Create Queue Tree Download
                    newClientRouterViewModel.createQueueTreeDownload(newQueueTreeDownload, "Total Download", comment, newDownloadPacketMark, limitAt, maxLimit, burstTime, burstThreshold, burstLimit)
                }
                is Results.Error -> {
                    showCustomDialogLoading(false)
                    Toast.makeText(this, "Create Queue Tree Error: ${newQueueTreeResult.error}", Toast.LENGTH_SHORT).show()
                    Log.d("NewClientRouterActivity", "Create Queue Tree Error: ${newQueueTreeResult.error}")
                }
                is Results.Loading -> {
                    showCustomDialogLoading(true)
                }
            }
        }

        //Queue Tree Download Observer
        newClientRouterViewModel.createQueueTreeDownload.removeObservers(this)
        newClientRouterViewModel.createQueueTreeDownload.observe(this) { newQueueTreeResult ->
            when (newQueueTreeResult) {
                is Results.Success -> {
                    showCustomDialogLoading(false)
                    createFilterRules()
                }
                is Results.Error -> {
                    showCustomDialogLoading(false)
                    Toast.makeText(this, "Create Queue Tree Error: ${newQueueTreeResult.error}", Toast.LENGTH_SHORT).show()
                    Log.d("NewClientRouterActivity", "Create Queue Tree Error: ${newQueueTreeResult.error}")
                }
                is Results.Loading -> {
                    showCustomDialogLoading(true)
                }
            }
        }
    }

    private fun createFilterRules() {
        val comment = intent.getStringExtra(COMMENT).toString().trim()
        val ipAddress = binding.etIPInternet.text.toString().trim()
        val accessId = intent.getIntExtra(ACCESS_ID, 0)

        when (accessId) {
            1 -> newClientRouterViewModel.createFilterRules(comment, "forward", ipAddress, "drop", "false")
            2 -> newClientRouterViewModel.createFilterRules(comment, "forward", ipAddress, "drop", "true")
            else -> Log.d("ClientDetailActivity", "Selected Access Doesn't Counted")
        }

        newClientRouterViewModel.createFilterRules.removeObservers(this)
        newClientRouterViewModel.createFilterRules.observe(this) { createFilterRulesResult ->
            when (createFilterRulesResult) {
                is Results.Success -> {
                    showCustomDialogLoading(false)
                    createNewClient()
                }
                is Results.Error -> {
                    showCustomDialogLoading(false)
                    Toast.makeText(this, "Create Filter Rules Error: ${createFilterRulesResult.error}", Toast.LENGTH_SHORT).show()
                    Log.d("NewClientRouterActivity", "Create Filter Rules Error: ${createFilterRulesResult.error}")
                }
                is Results.Loading -> {
                    showCustomDialogLoading(true)
                }
            }
        }
    }

    private fun createNewClient() {
        val bts = btsSelectedId
        val mode = modeSelectedId
        val ssid = binding.etSSID.text.toString()
        val ipAddress = binding.etIPInternet.text.toString()
        val radio = radioSelectedId
        val radioName = binding.etRadioName.text.toString()
        val ipRadio = binding.etIPRadio.text.toString()
        val frequency = binding.etFrequency.text.toString()
        val channelWidth = channelWidthSelectedId
        val signal = binding.etSignal.text.toString()
        val preSharedKey = presharedKeySelectedId
        val apLocation = binding.etAPLocation.text.toString()
        val wlanMacAddress = binding.etWLANMacAddress.text.toString()
        val password = binding.etPassword.text.toString()

        val clientName = intent.getStringExtra(CLIENT_NAME).toString()
        val phone = intent.getStringExtra(PHONE_NUMBER).toString()
        val address = intent.getStringExtra(ADDRESS).toString()
        val comment = intent.getStringExtra(COMMENT).toString()
        val accessId = intent.getIntExtra(ACCESS_ID, 0)
        val speedId = intent.getIntExtra(SPEED_ID, 0)

        //Crate New Client
        newClientRouterViewModel.createNewClient(clientName, phone, address, accessId, speedId)

        //New Client Observer
        newClientRouterViewModel.createNewClient.removeObservers(this)
        newClientRouterViewModel.createNewClient.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showCustomDialogLoading(false)
                    val clientId = result.data.newClient?.clientId!!.toInt()
                    newClientRouterViewModel.updateNetwork(
                        clientId,
                        radioName,
                        frequency,
                        ipRadio,
                        ipAddress,
                        wlanMacAddress,
                        ssid,
                        signal,
                        apLocation,
                        radio,
                        mode,
                        channelWidth,
                        preSharedKey,
                        comment,
                        password,
                        bts
                    )
                }
                is Results.Error -> {
                    showCustomDialogLoading(false)
                    Toast.makeText(this, "Create New Client Error: ${result.error}", Toast.LENGTH_SHORT).show()
                    Log.d("NewClientRouterActivity", "Create New Client Error: ${result.error}")
                }
                is Results.Loading -> {
                    showCustomDialogLoading(true)
                }
            }
        }

        //Update New Client Network Observer
        newClientRouterViewModel.updateNetwork.removeObservers(this)
        newClientRouterViewModel.updateNetwork.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    showCustomDialogLoading(false)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                is Results.Error -> {
                    showCustomDialogLoading(false)
                    Toast.makeText(this, "Update New Client Failed", Toast.LENGTH_SHORT).show()
                    Log.d("NewClientRouterActivity", "Update New Client Error: ${result.error}")
                }

                is Results.Loading -> {
                    showCustomDialogLoading(true)
                }
            }
        }
    }

    private fun clientSpeed(speedId: Int) {
        when (speedId) {
            1 -> {
                limitAt = "0M"
                maxLimit = "0M"
                burstTime = "0"
                burstThreshold = "0M"
                burstLimit = "0M"
            }
            2 -> {
                limitAt = "5M"
                maxLimit = "10M"
                burstTime = "10"
                burstThreshold = "7M"
                burstLimit = "15M"
            }
            3 -> {
                limitAt = "10M"
                maxLimit = "20M"
                burstTime = "10"
                burstThreshold = "15M"
                burstLimit = "30M"
            }
            4 -> {
                limitAt = "15M"
                maxLimit = "30M"
                burstTime = "10"
                burstThreshold = "22M"
                burstLimit = "40M"
            }
            5 -> {
                limitAt = "25M"
                maxLimit = "50M"
                burstTime = "10"
                burstThreshold = "37M"
                burstLimit = "65M"
            }
            6 -> {
                limitAt = "50M"
                maxLimit = "100M"
                burstTime = "10"
                burstThreshold = "75M"
                burstLimit = "120M"
            }
        }
    }

    private fun showCustomDialogLoading(status: Boolean) {
        when (status) {
            true -> {
                if (loadingDialog == null) {
                    loadingDialog = Dialog(this).apply {
                        requestWindowFeature(Window.FEATURE_NO_TITLE)
                        bindingDialog = CustomLoadingDialogBinding.inflate(layoutInflater)
                        setContentView(bindingDialog.root)
                        setCancelable(false)
                        window?.setLayout(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT
                        )
                        window?.setBackgroundDrawableResource(android.R.color.transparent)
                    }
                    val cardView = bindingDialog.root.findViewById<CardView>(R.id.LoadingCard)
                    val layoutParams = cardView.layoutParams as ViewGroup.MarginLayoutParams
                    val margin = (40 * resources.displayMetrics.density).toInt()
                    layoutParams.setMargins(margin, 0, margin, 0)
                    cardView.layoutParams = layoutParams
                }
                loadingDialog?.show()
            }
            false -> {
                loadingDialog?.dismiss()
                loadingDialog = null
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun textFieldWatcher() {
        //Text Fields Watcher
        val textFields = listOf(
            binding.etSSID,
            binding.etIPInternet,
            binding.etRadioName,
            binding.etIPRadio,
            binding.etFrequency,
            binding.etSignal,
            binding.etAPLocation,
            binding.etWLANMacAddress,
            binding.etPassword
        )
        for (textField in textFields) {
            textField.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    buttonSet()
                }
                override fun afterTextChanged(p0: Editable?) {}
            })
        }

        //Spinner Watcher
        val spinnerFields = listOf(
            binding.spBTS,
            binding.spMode,
            binding.spRadio,
            binding.spChannelWidth,
            binding.spPresharedKey
        )
        for (spinnerField in spinnerFields) {
            spinnerField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    buttonSet()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun buttonSet() {
        val bts = btsSelectedId
        val mode = modeSelectedId
        val ssid = binding.etSSID.text.toString()
        val ipAddress = binding.etIPInternet.text.toString()
        val radio = radioSelectedId
        val radioName = binding.etRadioName.text.toString()
        val ipRadio = binding.etIPRadio.text.toString()
        val frequency = binding.etFrequency.text.toString()
        val channelWidth = channelWidthSelectedId
        val signal = binding.etSignal.text.toString()
        val preSharedKey = presharedKeySelectedId
        val apLocation = binding.etAPLocation.text.toString()
        val wlanMacAddress = binding.etWLANMacAddress.text.toString()
        val password = binding.etPassword.text.toString()

        val isFieldFilled =
                    bts != 0 &&
                    mode != 0 &&
                    ssid.isNotEmpty() &&
                    ipAddress.isNotEmpty() &&
                    radio != 0 &&
                    radioName.isNotEmpty() &&
                    ipRadio.isNotEmpty() &&
                    frequency.isNotEmpty() &&
                    channelWidth != 0 &&
                    signal.isNotEmpty() &&
                    preSharedKey != 0 &&
                    apLocation.isNotEmpty() &&
                    wlanMacAddress.isNotEmpty() &&
                    password.isNotEmpty()

        if (isFieldFilled) {
            binding.btDone.isEnabled = true
            binding.btDone.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        } else {
            binding.btDone.isEnabled = false
            binding.btDone.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4D4D4D"))
        }
    }

    companion object {
        const val CLIENT_NAME = "client_name"
        const val PHONE_NUMBER = "phone_number"
        const val ADDRESS = "address"
        const val COMMENT = "comment"
        const val ACCESS_ID = "access_id"
        const val SPEED_ID = "speed_id"

        const val NEW_UPLOAD_PACKET_MARK = "new_upload_packet_mark"
        const val NEW_DOWNLOAD_PACKET_MARK = "new_download_packet_mark"

        const val NEW_QUEUE_TREE_UPLOAD = "new_queue_tree_upload"
        const val NEW_QUEUE_TREE_DOWNLOAD = "new_queue_tree_download"
    }
}