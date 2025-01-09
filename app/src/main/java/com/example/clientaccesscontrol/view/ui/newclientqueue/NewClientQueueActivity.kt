package com.example.clientaccesscontrol.view.ui.newclientqueue

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clientaccesscontrol.R
import com.example.clientaccesscontrol.databinding.ActivityNewClientQueueBinding
import com.example.clientaccesscontrol.view.ui.newclientrouter.NewClientRouterActivity

class NewClientQueueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewClientQueueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewClientQueueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupActionButton()
    }

    private fun setupActionButton() {
        textFieldWatcher()

        binding.btNext.setOnClickListener {
            val nameUpload = binding.etNameUpload.text.toString()
            val nameDownload = binding.etNameDownload.text.toString()

            val clientName = intent.getStringExtra(CLIENT_NAME).toString()
            val phone = intent.getStringExtra(PHONE_NUMBER).toString()
            val address = intent.getStringExtra(ADDRESS).toString()
            val comment = intent.getStringExtra(COMMENT).toString()
            val accessId = intent.getIntExtra(ACCESSID, 0)
            val speedId = intent.getIntExtra(SPEEDID, 0)

            val newUploadPacketMark = intent.getStringExtra(NEWUPLOADPACKETMARTK).toString()
            val newDownloadPacketMark = intent.getStringExtra(NEWDOWNLOADPACKETMARTK).toString()

            val intent = Intent(this, NewClientRouterActivity::class.java).apply {
                putExtra(NewClientRouterActivity.NAMEUPLOAD, nameUpload)
                putExtra(NewClientRouterActivity.NAMEDOWNLOAD, nameDownload)

                putExtra(NewClientRouterActivity.CLIENT_NAME, clientName)
                putExtra(NewClientRouterActivity.PHONE_NUMBER, phone)
                putExtra(NewClientRouterActivity.ADDRESS, address)
                putExtra(NewClientRouterActivity.COMMENT, comment)
                putExtra(NewClientRouterActivity.ACCESSID, accessId)
                putExtra(NewClientRouterActivity.SPEEDID, speedId)

                putExtra(NewClientRouterActivity.NEWUPLOADPACKETMARTK, newUploadPacketMark)
                putExtra(NewClientRouterActivity.NEWDOWNLOADPACKETMARTK, newDownloadPacketMark)
            }
            startActivity(intent)
        }
    }

    private fun textFieldWatcher() {
        val textFields = listOf(
            binding.etNameUpload,
            binding.etNameDownload,
            binding.etPacketMarksUpload,
            binding.etPacketMarksDownload
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
    }

    private fun buttonSet() {
        val nameUpload = binding.etNameUpload.text.toString()
        val nameDownload = binding.etNameDownload.text.toString()
        val uploadPacketMark = binding.etPacketMarksUpload.text.toString()
        val downloadPacketMark = binding.etPacketMarksDownload.text.toString()

        val isFieldFilled =
                    nameUpload.isNotEmpty() &&
                    nameDownload.isNotEmpty() &&
                    uploadPacketMark.isNotEmpty() &&
                    downloadPacketMark.isNotEmpty()

        if (isFieldFilled) {
            binding.btNext.isEnabled = true
            binding.btNext.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#000000"))
        } else {
            binding.btNext.isEnabled = false
            binding.btNext.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4D4D4D"))
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
    }
}