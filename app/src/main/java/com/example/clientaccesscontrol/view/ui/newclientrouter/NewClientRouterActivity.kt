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
        newClientRouterViewModel.getBTS.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val btsList = result.data.bts?.mapNotNull { it?.bts } ?: emptyList()
                    val btsAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, btsList)
                    btsAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spBTS.adapter = btsAdapter
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }

                is Results.Loading -> {}
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

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
        }

        newClientRouterViewModel.getMode.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val modeList = result.data.modes?.mapNotNull { it?.mode } ?: emptyList()
                    val modeAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, modeList)
                    modeAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spMode.adapter = modeAdapter
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }

                is Results.Loading -> {}
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

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
        }

        newClientRouterViewModel.getRadio.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val radioList = result.data.radios?.mapNotNull { it?.type } ?: emptyList()
                    val radioAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, radioList)
                    radioAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spRadio.adapter = radioAdapter
                }

                is Results.Error -> {
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }

                is Results.Loading -> {}
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
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }

                is Results.Loading -> {}
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
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }

                is Results.Loading -> {}
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

                    override fun onNothingSelected(p0: AdapterView<*>?) {}
                }
        }
    }

    private fun setupActionButton() {
        textFieldWatcher()

        binding.btDone.setOnClickListener {
            val nameUpload = intent.getStringExtra(NAMEUPLOAD).toString()
            val nameDownload = intent.getStringExtra(NAMEDOWNLOAD).toString()

            val newUploadPacketMark = intent.getStringExtra(NEWUPLOADPACKETMARTK).toString()
            val newDownloadPacketMark = intent.getStringExtra(NEWDOWNLOADPACKETMARTK).toString()

            createNewClient()
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
        val accessId = intent.getIntExtra(ACCESSID, 0)
        val speedId = intent.getIntExtra(SPEEDID, 0)

        //Crate New Client
        newClientRouterViewModel.createNewClient(
            clientName,
            phone,
            address,
            comment,
            accessId,
            speedId
        )
        newClientRouterViewModel.createNewClient.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    //Get New Client ID
                    Log.d("NewClientRouterActivity", "Delay 3 detik")
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
                    hideCustomDialogLoading()
                    Toast.makeText(
                        this,
                        "Create New Client Error: ${result.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("NewClientRouterActivity", "Create New Client Error: ${result.error}")
                }

                is Results.Loading -> {
                    showCustomDialogLoading()
                }
            }
        }

        newClientRouterViewModel.updateNetwork.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    hideCustomDialogLoading()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

                is Results.Error -> {
                    hideCustomDialogLoading()
                    Toast.makeText(this, "Update New Client Failed", Toast.LENGTH_SHORT).show()
                    Log.d("NewClientRouterActivity", "Update New Client Error: ${result.error}")
                }

                is Results.Loading -> {
                    showCustomDialogLoading()
                }
            }
        }
    }

    private fun showCustomDialogLoading() {
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

    private fun hideCustomDialogLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
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
        const val ACCESSID = "access_id"
        const val SPEEDID = "speed_id"

        const val NEWUPLOADPACKETMARTK = "newuploadpacketmartk"
        const val NEWDOWNLOADPACKETMARTK = "newdownloadpacketmartk"

        const val NAMEUPLOAD = "nameupload"
        const val NAMEDOWNLOAD = "namedownload"
    }
}