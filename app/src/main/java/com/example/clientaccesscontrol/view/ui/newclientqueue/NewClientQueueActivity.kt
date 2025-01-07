package com.example.clientaccesscontrol.view.ui.newclientqueue

import android.content.Intent
import android.os.Bundle
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

        binding.btNext.setOnClickListener {
            val intent = Intent(this, NewClientRouterActivity::class.java)
            startActivity(intent)
        }
    }
}