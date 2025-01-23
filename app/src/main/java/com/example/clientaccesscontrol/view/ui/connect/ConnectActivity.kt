package com.example.clientaccesscontrol.view.ui.connect

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.clientaccesscontrol.R
import com.example.clientaccesscontrol.data.preference.UserPreference
import com.example.clientaccesscontrol.data.preference.dataStore
import com.example.clientaccesscontrol.data.result.Results
import com.example.clientaccesscontrol.databinding.ActivityConnectBinding
import com.example.clientaccesscontrol.view.ui.home.MainActivity
import com.example.clientaccesscontrol.view.ui.home.MainVM
import com.example.clientaccesscontrol.view.utils.FactoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException

class ConnectActivity : AppCompatActivity() {

    private val connectViewModel by viewModels<ConnectVM> {
        FactoryViewModel.getInstance(this)
    }
    private val session by viewModels<MainVM> {
        FactoryViewModel.getInstance(this)
    }
    private val preference: UserPreference by lazy {
        UserPreference.getInstance(applicationContext.dataStore)
    }
    private lateinit var binding: ActivityConnectBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()


        session.getSession().observe(this) { user ->
            if (user.isLogin) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                setupUI()
            }
        }
    }

    private fun setupUI() {
        binding = ActivityConnectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupAction()
    }

    private fun setupAction() {
        textFieldWatcher()

        binding.btConnect.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val ipAddress = binding.etAddress.text.toString().trim()
            lifecycleScope.launch(Dispatchers.IO) {
                loginProcess(username, password, ipAddress)
            }
        }
    }

    private suspend fun loginProcess(username: String, password: String, ipAddress: String) {
        try {
            preference.saveBaseUrl(ipAddress)
            Log.d("Repository", "Saved Base URL in Session: $ipAddress")
            preference.saveMikrotikLogin(username, password)
            Log.d("Repository", "Saved Username & Password for Mikrotik Login : $username & $password")

            withContext(Dispatchers.IO) {
                connectViewModel.login(ipAddress, username, password)
                withContext(Dispatchers.Main) {
                    loginObserver()
                }
            }
        } catch (e: HttpException) {
            val errorMessage = parseErrorMessage(e)
            Log.e("ConnectActivity", "Login failed: ${e.message}")

            if (errorMessage.contains("Invalid IP Address")) {
                try {
                    // If IP address is invalid, try to register
                    Log.d("ConnectActivity", "Trying to register with IP: $ipAddress")
                    connectViewModel.register(username, password, ipAddress)
                    try {
                        // If registration is successful, try to login
                        Log.d("ConnectActivity", "Trying to login with IP: $ipAddress")
                        withContext(Dispatchers.IO) {
                            connectViewModel.login(ipAddress, username, password)
                            withContext(Dispatchers.Main) {
                                loginObserver()
                            }
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            loginFailed()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        loginFailed()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    loginFailed()
                }
            }
        }
    }

    private fun loginObserver() {
        connectViewModel.loginResponse.observe(this) { loginResponseResult ->
            when (loginResponseResult) {
                is Results.Success -> {
                    val loginData = loginResponseResult.data.loginResult
                    if (loginData?.token != null && loginData.ipAddress != null) {
                        Log.d("ConnectActivity", "Login Success to ${loginData.ipAddress}")
                        loginSuccess()
                    } else {
                        Toast.makeText(this, "Login data incomplete", Toast.LENGTH_SHORT).show()
                    }
                }
                is Results.Error -> {
                    Toast.makeText(this, "Failed Connect to Server", Toast.LENGTH_SHORT).show()
                    Log.e("ConnectActivity", "Failed Connect to Server: ${loginResponseResult.error}")
                }
                is Results.Loading -> {
                }
            }
        }
    }

    private fun loginSuccess() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun loginFailed() {
        Toast.makeText(this, "Login Gagal, Silahkan Coba Lagi", Toast.LENGTH_SHORT).show()
    }

    private fun textFieldWatcher() {
        val textFields = listOf(
            binding.etUsername,
            binding.etPassword,
            binding.etAddress
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
        val username = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        val ipAddress = binding.etAddress.text.toString()

        val isFieldFilled =
            username.isNotEmpty() &&
                    password.isNotEmpty() &&
                    ipAddress.isNotEmpty()

        if (isFieldFilled) {
            binding.btConnect.isEnabled = true
            binding.btConnect.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
        } else {
            binding.btConnect.isEnabled = false
            binding.btConnect.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4D4D4D"))
        }
    }

    private fun parseErrorMessage(e: HttpException): String {
        return try {
            val errorBody = e.response()?.errorBody()?.string()
            val json = JSONObject(errorBody.toString())
            json.getString("message")
        } catch (ex: Exception) {
            "An unknown error occurred"
        }
    }
}