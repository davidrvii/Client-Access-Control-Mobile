package com.example.clientaccesscontrol.view.ui.newclientprofile

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.clientaccesscontrol.view.ui.newclientrouter.NewClientRouterActivity
import com.example.clientaccesscontrol.view.utils.FactoryViewModel

class NewClientProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewClientProfileBinding
    private var accessId: Int = 1
    private var speedId: Int = 1
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
                    val accessList = result.data.access?.mapNotNull { it?.internetAccess } ?: emptyList()
                    val accessAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, accessList)
                    accessAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spInternetAccess.adapter = accessAdapter
                }
                is Results.Error -> Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                is Results.Loading -> {}
            }
            binding.spInternetAccess.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: android.view.View?,
                        position: Int,
                        id: Long,
                    ) {
                        accessId = position+1
                        if (accessId == 3) {
                            Toast.makeText(this@NewClientProfileActivity, "Can't Choose The Selection", Toast.LENGTH_SHORT).show()
                            binding.spInternetAccess.setSelection(1)
                            accessId = 2
                        }
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        accessId
                    }
                }
        }

        newClientProfileViewModel.getSpeed.observe(this) { result ->
            when (result) {
                is Results.Success -> {
                    val speedList = result.data.speed?.mapNotNull { it?.internetSpeed } ?: emptyList()
                    val speedAdapter = ArrayAdapter(this, R.layout.spinner_dropdown_item, speedList)
                    speedAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                    binding.spInternetSpeed.adapter = speedAdapter
                }
                is Results.Error -> Toast.makeText(this, "Error: ${result.error}",
                    Toast.LENGTH_SHORT).show()
                is Results.Loading -> {}
            }
            binding.spInternetSpeed.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: android.view.View?,
                        position: Int,
                        id: Long,
                    ) {
                        speedId = position+1
                    }
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        speedId
                    }
                }
        }
    }

    private fun setupActionButton() {
        textFieldWatcher()

        binding.btNext.setOnClickListener {
            val clientName = binding.etClientName.text.toString().trim()
            val phone = binding.etPhoneNumber.text.toString().trim()
            val address = binding.etAddress.text.toString().trim()
            val comment = binding.etComment.text.toString().trim()
            val accessId = accessId
            val speedId = speedId

            val newUploadPacketMark = binding.etUploadPacketMark.text.toString().trim()
            val newDownloadPacketMark = binding.etDownloadPacketMark.text.toString().trim()

            val newQueueTreeUpload = binding.etQueueTreeUpload.text.toString()
            val newQueueTreeDownload = binding.etQueueTreeDownload.text.toString()

            val intent = Intent(this, NewClientRouterActivity::class.java).apply {
                putExtra(NewClientRouterActivity.CLIENT_NAME, clientName)
                putExtra(NewClientRouterActivity.PHONE_NUMBER, phone)
                putExtra(NewClientRouterActivity.ADDRESS, address)
                putExtra(NewClientRouterActivity.COMMENT, comment)
                putExtra(NewClientRouterActivity.ACCESS_ID, accessId)
                putExtra(NewClientRouterActivity.SPEED_ID, speedId)

                putExtra(NewClientRouterActivity.NEW_UPLOAD_PACKET_MARK, newUploadPacketMark)
                putExtra(NewClientRouterActivity.NEW_DOWNLOAD_PACKET_MARK, newDownloadPacketMark)

                putExtra(NewClientRouterActivity.NEW_QUEUE_TREE_UPLOAD, newQueueTreeUpload)
                putExtra(NewClientRouterActivity.NEW_QUEUE_TREE_DOWNLOAD, newQueueTreeDownload)
            }
            startActivity(intent)
        }

        binding.btBack.setOnClickListener {
            finish()
        }
    }

    private fun textFieldWatcher() {
        val textFields = listOf(
            binding.etClientName,
            binding.etPhoneNumber,
            binding.etAddress,
            binding.etComment,
            binding.etUploadPacketMark,
            binding.etDownloadPacketMark,
            binding.etQueueTreeUpload,
            binding.etQueueTreeDownload
        )

        for (textField in textFields) {
            textField.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    buttonSet()
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }

        val spinnerFields = listOf(
            binding.spInternetAccess,
            binding.spInternetSpeed
        )
        for (spinner in spinnerFields) {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                    buttonSet()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
    }

    private fun buttonSet() {
        val clientName = binding.etClientName.text.toString()
        val phone = binding.etPhoneNumber.text.toString()
        val address = binding.etAddress.text.toString()
        val comment = binding.etComment.text.toString()
        val accessId = accessId
        val speedId = speedId

        val newUploadPacketMark = binding.etUploadPacketMark.text.toString()
        val newDownloadPacketMark = binding.etDownloadPacketMark.text.toString()

        val newQueueTreeUpload = binding.etQueueTreeUpload.text.toString()
        val newQueueTreeDownload = binding.etQueueTreeDownload.text.toString()

        val isFieldFilled =
                    clientName.isNotEmpty() &&
                    phone.isNotEmpty() &&
                    address.isNotEmpty() &&
                    comment.isNotEmpty() &&
                    newUploadPacketMark.isNotEmpty() &&
                    newDownloadPacketMark.isNotEmpty() &&
                    newQueueTreeUpload.isNotEmpty() &&
                    newQueueTreeDownload.isNotEmpty() &&
                    accessId in 1..2 &&
                    speedId in 1..6

        if (isFieldFilled) {
            binding.btNext.isEnabled = true
            binding.btNext.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        } else {
            binding.btNext.isEnabled = false
            binding.btNext.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4D4D4D"))
        }
    }
}